package com.sso.zjx.crm.controller;

import com.sso.zjx.crm.bean.ClientInfoVO;
import com.sso.zjx.crm.util.MockDatabaseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthController {

    @RequestMapping("/checkLogin")
    public String main(String redirectUrl, HttpSession session,Model model){
        //1.判断是否有全局会话
        String token = (String)session.getAttribute("token");
        if(StringUtils.isEmpty(token)){
            //表示没有全局会话
            //跳转到统一认证中心的登录页面
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        }else{
            //有全局会话
            return "redirect:"+redirectUrl+"?token="+token;
        }
    }

    /**
     * 登录功能
     */
    @RequestMapping("/login")
    public String login(String userName, String passWord, String redirectUrl, HttpSession session, Model model, HttpServletResponse response) throws IOException {
        if("admin".equals(userName)&&"admin".equals(passWord)){
            //账号密码匹配创建令牌信息
            //1.创建令牌信息
            String token = UUID.randomUUID().toString();
            //2.创建全局回话，把令牌信息放入会话中
            session.setAttribute("token",token);
            //3.需要把令牌信息放入到数据库
            MockDatabaseUtil.T_TOKEN.add(token);
            //4.重定向到redirectUrl
//            model.addAttribute("token",token);
            return "redirect:"+redirectUrl+"?token="+token;
        }
        model.addAttribute("redirectUrl",redirectUrl);
        return "login";
    }

    /**
     * 校验token是否由统一认证中心产生的
     */
    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token,String clientLogOutUrl,String jsessionId){
        if(MockDatabaseUtil.T_TOKEN.contains(token)){
            //把客户端的登出地址记录
            List<ClientInfoVO> clientInfoVOS = MockDatabaseUtil.T_CLIENT_INFO.get(token);
            if(clientInfoVOS==null){
                clientInfoVOS = new ArrayList<ClientInfoVO>();
                MockDatabaseUtil.T_CLIENT_INFO.put(token,clientInfoVOS);
            }
            ClientInfoVO clientInfoVO = new ClientInfoVO();
            clientInfoVO.setClientUrl(clientLogOutUrl);
            clientInfoVO.setJsessionId(jsessionId);
            clientInfoVOS.add(clientInfoVO);
            //说明令牌有效，返回true
            return "true";
        }
        return "false";
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session){
        //销毁全局会话
        session.invalidate();

        return "logOut";
    }

    @RequestMapping("/test")
    public String test(){

        return "redirect:http://www.crm.com:8081/test";
    }
}

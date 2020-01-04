package com.sso.zjx.crm.listener;

import com.sso.zjx.crm.bean.ClientInfoVO;
import com.sso.zjx.crm.util.HttpClientUtil;
import com.sso.zjx.crm.util.MockDatabaseUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.List;

public class MySessionListener implements HttpSessionListener{
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //获取出注册的子系统，依次调用子系统的登出方法
        HttpSession session = se.getSession();
        String token = (String)session.getAttribute("token");
        //删除t_token表中的数据
        List<ClientInfoVO> clientInfoVOS = MockDatabaseUtil.T_CLIENT_INFO.get(token);
        for(ClientInfoVO vo:clientInfoVOS){
            //获取出注册的子系统，依次调用系统的登出方法
            try {
                HttpClientUtil.sendRequest(vo.getClientUrl(),vo.getJsessionId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

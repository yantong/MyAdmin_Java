package com.example.demo.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.decorator.NeedLogin;
import com.example.demo.login.service.LoginService;
import com.example.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("authorization");

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //检查是否有NeedLogin注释
        if (method.isAnnotationPresent(NeedLogin.class)) {
            DecodedJWT tokenInfo = JWTUtil.getTokenInfo(token);
            String account = JWTUtil.getUserId(tokenInfo);
            if (token != null && tokenInfo != null) {
                String userToken = loginService.getUserToken(account);

                if (userToken.equals(token) && !JWTUtil.isTimeOut(tokenInfo)) {
                    return true;
                }
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            PrintWriter out = null;

            JSONObject res = new JSONObject();
            res.put("code", 402);

            try {
                out = response.getWriter();
                out.append(JSON.toJSONString(res));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(out!=null)
                    out.close();
            }


            return false;
        }

        return true;
    }
}
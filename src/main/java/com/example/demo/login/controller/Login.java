package com.example.demo.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.decorator.NeedLogin;
import com.example.demo.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Login {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    public JSONObject login(@RequestBody JSONObject json) {
        return loginService.login(json);
    }

    @GetMapping("logout")
    public JSONObject logout() {
        JSONObject json = new JSONObject();

        return loginService.logout(json);
    }

    @PostMapping("getUserInfo")
    @NeedLogin
    public JSONObject getUserInfo(@RequestBody JSONObject json) {
        return loginService.getUserInfo(json);
    }
}
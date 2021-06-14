package com.example.demo.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.decorator.NeedLogin;
import com.example.demo.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @GetMapping("getUserInfo")
    @NeedLogin
    public JSONObject getUserInfo(@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst("authorization");

        return loginService.getUserInfo(token);
    }

    @PostMapping("addUser")
    public JSONObject addUser(@RequestBody JSONObject json) {
        return loginService.addUser(json);
    }

    @DeleteMapping ("deleteUser")
    public JSONObject deleteUser(@RequestBody JSONObject json) {
        return loginService.deleteUser(json);
    }


    @PostMapping("editUser")
    public JSONObject editUser(@RequestBody JSONObject json, @RequestHeader HttpHeaders headers) {
        String token = headers.getFirst("authorization");

        return loginService.editUser(json, token);
    }


    @GetMapping("getUsers")
    public JSONObject getUsers(@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst("authorization");

        return loginService.getUsers(token);
    }
}
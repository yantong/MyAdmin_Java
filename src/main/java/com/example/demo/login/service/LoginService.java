package com.example.demo.login.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.login.database.LoginDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginServiceInterface {

    @Autowired
    private LoginDatabase dao;

    @Override
    public JSONObject login(JSONObject json) {
        JSONObject res = this.dao.loginJudege(json);

        return res;
    }

    @Override
    public JSONObject logout(JSONObject json) {
        JSONObject res = new JSONObject();

        res.put("success", true);

        return res;
    }

    @Override
    public JSONObject getUserInfo(String token) {
        JSONObject res = this.dao.getUserInfo(token);

        return res;
    }

    @Override
    public JSONObject addUser(JSONObject json) {
        JSONObject res = this.dao.addUser(json);

        return res;
    }

    @Override
    public JSONObject deleteUser(JSONObject json) {
        JSONObject res = this.dao.deleteUser(json);

        return res;
    }

    @Override
    public JSONObject editUser(JSONObject json,String token) {
        JSONObject res = this.dao.editUser(json,token);

        return res;
    }

    @Override
    public JSONObject getUsers(String token) {
        JSONObject res = this.dao.getUsers(token);

        return res;
    }

    @Override
    public String getUserToken(String account) {
        String res = this.dao.getUserToken(account);

        return res;
    }


}

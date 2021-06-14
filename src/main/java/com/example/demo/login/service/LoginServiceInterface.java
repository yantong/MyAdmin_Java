package com.example.demo.login.service;

import com.alibaba.fastjson.JSONObject;

public interface LoginServiceInterface {
    JSONObject login(JSONObject json);
    JSONObject logout(JSONObject json);
    String getUserToken(String account);

    JSONObject getUserInfo(String token);
    JSONObject addUser(JSONObject json);
    JSONObject deleteUser(JSONObject json);
    JSONObject editUser(JSONObject json, String token);
    JSONObject getUsers(String token);

}

package com.example.demo.login.database;

import com.alibaba.fastjson.JSONObject;

public interface LoginDatabaseInterface {
    JSONObject loginJudege(JSONObject json);
    JSONObject getUserInfo(String token);
    JSONObject addUser(JSONObject json);
    JSONObject deleteUser(JSONObject json);
    JSONObject editUser(JSONObject json, String token);
    JSONObject getUsers(String token);
    String getUserToken(String account);
}

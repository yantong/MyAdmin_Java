package com.example.demo.login.database;

import com.alibaba.fastjson.JSONObject;

public interface LoginDatabaseInterface {
    JSONObject loginJudege(JSONObject json);
    JSONObject getUserInfo(JSONObject json);
    String getUserToken(String account);
}

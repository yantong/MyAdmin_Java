package com.example.demo.login.service;

import com.alibaba.fastjson.JSONObject;

public interface LoginServiceInterface {
    JSONObject login(JSONObject json);
    JSONObject logout(JSONObject json);
    JSONObject getUserInfo(JSONObject json);
    String getUserToken(String account);
}

package com.example.demo.login.database;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoginDatabase implements LoginDatabaseInterface {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JSONObject loginJudege(JSONObject json) {
        JSONObject res = new JSONObject();
        String sql = String.format("select * from user where account = '%s'", json.get("account"));
        String sql2 = String.format("select * from user where account = '%s' and password = '%s'", json.get("account"), json.get("password"));
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> rows2 = this.jdbcTemplate.queryForList(sql2);

        if (rows.size() == 0) {
            res.put("errorMsg", "账号不存在！");
            res.put("success", false);
        } else if (rows2.size() == 0) {
            res.put("errorMsg", "密码错误！");
            res.put("success", false);
        } else {
            String Token = JWTUtil.createToken(json.get("account").toString());
            String tokenSsql = String.format("insert into user_token (account,token) values('%s' , '%s') on DUPLICATE key update token = '%s'",
                    json.get("account"), Token, Token);

            this.jdbcTemplate.execute(tokenSsql);


            res.put("success", true);
            res.put("token", Token);
        }

        return res;
    }

    @Override
    public JSONObject getUserInfo(JSONObject json) {
        JSONObject res = new JSONObject();
        JSONObject info = new JSONObject();
        String sql = String.format("select * from user_info where account = '%s'", json.get("account"));
        String router_sql = String.format("select * from user_router where account = '%s'", json.get("account"));
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> router_rows = this.jdbcTemplate.queryForList(router_sql);

        List<HashMap<String, String>> routers = router_rows.stream().map(item -> {
            HashMap<String, String> map = new HashMap<>();

            map.put("router", item.get("router").toString());
            map.put("buttons", item.get("buttons").toString());

            return map;
        }).collect(Collectors.toList());

        info.put("user", rows.get(0));
        info.put("router", routers);

        res.put("info", info);
        res.put("success", true);

        return res;
    }

    @Override
    public String getUserToken(String account) {
        JSONObject res = new JSONObject();
        String sql = String.format("select * from user_token where account = '%s'", account);
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);


        return rows.get(0).get("TOKEN").toString();
    }


}

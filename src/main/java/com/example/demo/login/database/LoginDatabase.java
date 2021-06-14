package com.example.demo.login.database;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public JSONObject getUsers(String token) {
        JSONObject res = new JSONObject();
        List<JSONObject> info = new ArrayList<>();

        String sql = String.format("select * from user_token where token = '%s'", token);
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
        String account = rows.get(0).get("account").toString();

        sql = String.format("select * from user where account != '%s'", account);
        rows = this.jdbcTemplate.queryForList(sql);

        for (Map<String, Object> user : rows) {
            account = user.get("account").toString();

            info.add(getUserInfoPrivate(account).getJSONObject("info"));
        }


        res.put("users", info);
        res.put("success", true);

        return res;
    }

    public JSONObject getUserInfoPrivate(String account) {
        JSONObject res = new JSONObject();
        JSONObject info = new JSONObject();
        String sql = String.format("select * from user_info where account = '%s'", account);
        String router_sql = String.format("select * from user_router where account = '%s'", account);
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
    public JSONObject getUserInfo(String token) {
        String sql = String.format("select * from user_token where token = '%s'", token);
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
        String account = rows.get(0).get("account").toString();

        return getUserInfoPrivate(account);
    }

    @Override
    public JSONObject addUser(JSONObject json) {
        JSONObject res = new JSONObject();
        JSONObject permissions = json.getJSONObject("permission");

        String userQry = String.format("select * from user_info where account = '%s'", json.get("name"));
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(userQry);

        if (rows.size() > 0) {
            res.put("errorMsg", "用户已存在！");
            res.put("success", false);

            return res;
        }

        String sql = String.format("insert into user (account, name, password) values ('%s', '%s', '%s')", json.get("name"), json.get("name"), json.get("name"));
        this.jdbcTemplate.execute(sql);

        sql = String.format("insert into user_info (account, name) values ('%s', '%s')", json.get("name"), json.get("name"));
        this.jdbcTemplate.execute(sql);

        for (String router : permissions.keySet()) {
            sql = String.format("insert into user_router (account, router, buttons) values ('%s', '%s', '%s');", json.get("name"), router, permissions.get(router));
            this.jdbcTemplate.execute(sql);
        }

        res.put("success", true);

        return res;
    }

    @Override
    public JSONObject deleteUser(JSONObject json) {
        JSONObject res = new JSONObject();

        String sql = String.format("delete  from user where account = '%s';", json.get("account"));
        this.jdbcTemplate.execute(sql);

        sql = String.format("delete  from user_info where account = '%s';", json.get("account"));
        this.jdbcTemplate.execute(sql);

        sql = String.format("delete  from user_token where account = '%s';", json.get("account"));
        this.jdbcTemplate.execute(sql);

        sql = String.format("delete  from user_router where account = '%s';", json.get("account"));
        this.jdbcTemplate.execute(sql);

        res.put("success", true);

        return res;
    }

    @Override
    public JSONObject editUser(JSONObject json, String token) {
        JSONObject res = new JSONObject();
        JSONObject permissions = json.getJSONObject("permission");
        String account = json.get("account").toString();

        String sql = String.format("UPDATE user SET name= '%s' where account = '%s';", json.get("name"), account);
        this.jdbcTemplate.execute(sql);

        sql = String.format("UPDATE user_info SET name= '%s' where account = '%s';", json.get("name"), account);
        this.jdbcTemplate.execute(sql);

        sql = String.format("delete  from user_router where account = '%s';",account);
        this.jdbcTemplate.execute(sql);

        for (String router : permissions.keySet()) {
            sql = String.format("insert into user_router (account, router, buttons) values ('%s', '%s', '%s');", account, router, permissions.get(router));
            this.jdbcTemplate.execute(sql);
        }

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

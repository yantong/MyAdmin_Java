package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

public class JWTUtil {

    private static final String SECRET = "密钥";
    private static final Integer TIME_OUT_DAY = 1;


    public static String createToken(String account) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, TIME_OUT_DAY);
        String token = JWT.create()
                .withClaim("account", account)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }


    public static DecodedJWT getTokenInfo(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUserId(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("account").asString();
    }


    public static boolean isTimeOut(DecodedJWT decodedJWT) {
        Date timeoutDate = decodedJWT.getExpiresAt();
        Calendar calendar = Calendar.getInstance();

        if (timeoutDate.before(calendar.getTime())) {
            return true;
        }

        return false;
    }

}
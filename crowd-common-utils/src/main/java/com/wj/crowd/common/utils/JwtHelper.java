package com.wj.crowd.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * @author wj
 * @descript
 * @date 2022/4/1 - 15:06
 */
public class JwtHelper {
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "wHospital";

    public static String createToken(String userId, String userName) {
        String token = Jwts.builder()
                .setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    public static String getUserId(String token) {
        if (ObjectUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userId");

    }

    public static String getUserName(String token) {
        if (ObjectUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken("1522936790275686401", "15570756116");
//        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScg7yD3fRDQ12DVLSUUqtKFCyMjQzNTI0MzO1sNBRKi1OLfJMAYqZGhlZGpuZWxoYmZuaWZiZGBhCJP0Sc1OBphiampobAGUMDc2UagELwOVdWwAAAA.4fcaa54kBsrO8QF7R7G6VBlv1LM0ZYBfr8S6qbVMMmHlxl-ct6zV6_LWeVq1vPovGAbKipjOTcPKK7nNqY32ow";
//        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSiox099ANDXYNUtJRSq0oULIyNDM1MjQzs7Q001EqLU4t8kwBqjI0NTKyNDYztzQwMjc1szAzMTBUgkj7JeamghWYmhsApQwNzZRqATYv7JlcAAAA.Z4GR9f8cgVdWl7Me0bkWhsiVsHcUaIrVn-ulg1EocPV0U_tdU5h49E0MCC74JqcsM6n5bcGpvL4jwPRdRVm3Tw";
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }
}


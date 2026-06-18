/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 *
 * @author minhtb
 */
public class JWTUtils {
//    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final String SECRET = "this-is-a-very-strong-super-secret-key-123456";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1 ngày

    public static String generateToken(String identifier, String role, Integer id) {
        return Jwts.builder()
                .setSubject(identifier)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    //Kiểm tra xem token có hợp lệ 
    public static boolean verifyToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Token lấy identifier
    public static String getIdentifier(String token) {
        try {
            return parseToken(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
    // Token lấy role

    public static String getRole(String token) {
        try {
            return parseToken(token).getBody().get("role", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

}

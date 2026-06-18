/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 *
 * @author Astersa
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue(); // ✅ Đảm bảo token có giá trị

                    try {
                        DecodedJWT jwt = JWT.require(Algorithm.HMAC256("this-is-a-very-strong-super-secret-key-123456")) // trùng với bên JWTUtils
                                .build()
                                .verify(token); // ✅ Dùng biến token
                        
                        String name = jwt.getSubject(); // nếu bạn dùng setSubject() trong JWTUtils
                        String role = jwt.getClaim("role").asString();
                        Integer id = jwt.getClaim("id").asInt();
                        
                        request.setAttribute("loggedInUserName", name);
                        request.setAttribute("loggedInUserRole", role);
                        request.setAttribute("loggedInUserId", id);
                    } catch (JWTVerificationException e) {
                        // Token không hợp lệ
                    }
                }
            }
        }

        chain.doFilter(req, res);
    }
}

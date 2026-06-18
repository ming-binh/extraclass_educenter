/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.AccountDAO;
import modal.AccountModal;
import utils.JWTUtils;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean redirected = false;

        try {
            String identifier = request.getParameter("identifier");
            String password = request.getParameter("password");

            request.setAttribute("enteredIdentifier", identifier);

            AccountDAO dao = new AccountDAO();
            AccountModal acc = dao.checkLogin(identifier, password);

            if (acc != null) {
                switch (acc.getStatus()) {
                    case active:
                        // Tạo JWT
                        String token = JWTUtils.generateToken(identifier, acc.getRole().name(), acc.getId());
                        Cookie cookie = new Cookie("accessToken", token);
                        cookie.setHttpOnly(true);
                        cookie.setMaxAge(24 * 60 * 60); // 1 ngày
                        response.addCookie(cookie);

                        String role = acc.getRole().toString().toLowerCase();
                        switch (role) {
                            case "manager":
                            case "admin":
                                response.sendRedirect(request.getContextPath() + "/bang-dieu-khien");
                                break;
                            case "staff":
                                response.sendRedirect(request.getContextPath() + "/quan-ly-tu-van");
                                break;
                            default:
                                response.sendRedirect(request.getContextPath() + "/trang-chu");
                                break;
                        }
                        break;
                    case inactive:
                        request.setAttribute("error", "Tài khoản của bạn đã bị vô hiệu hoá.");
                        break;
                    default:
                        request.setAttribute("error", "Trạng thái tài khoản không hợp lệ.");
                        break;
                }
            } else {
                request.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }

        // Nếu chưa redirect thì forward lại form login
        if (!response.isCommitted() && !redirected) {
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng nhập";
    }
}

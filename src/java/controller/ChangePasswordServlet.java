/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modal.AccountModal;
import utils.HashUtils;
import utils.JWTUtils;

/**
 *
 * @author ASUS
 */
public class ChangePasswordServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
                request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
                return;
            }

            // Lấy JWT từ cookie
            String token = null;
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }

            if (token == null || !JWTUtils.verifyToken(token)) {
                response.sendRedirect(request.getContextPath() + "/dang-nhap");
                return;
            }

            String username = JWTUtils.getIdentifier(token);

            AccountDAO dao = new AccountDAO();
            AccountModal acc = dao.getAccountByUsername(username);

            if (acc == null) {
                request.setAttribute("error", "Không tìm thấy tài khoản.");
                request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
                return;
            }

            if (!HashUtils.checkPassword(oldPassword, acc.getPassword())) {
                request.setAttribute("error", "Mật khẩu cũ không đúng.");
                request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
                return;
            }

            String newHashed = HashUtils.hashPassword(newPassword);
            boolean success = dao.updatePassword(username, newHashed);

            if (success) {
                request.setAttribute("message", "Đổi mật khẩu thành công!");
                request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Có lỗi khi cập nhật mật khẩu.");
                request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/views/changepw.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý đổi mật khẩu";
    }
}

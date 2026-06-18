/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.NotificationDao;
import jakarta.servlet.http.HttpSession;
import modal.NotificationModal;
import utils.JWTUtils;

/**
 *
 * @author Minh Thu
 */
public class ManagerDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String token = request.getHeader("Authorization"); // hoặc lấy từ cookie

        if (token != null && JWTUtils.verifyToken(token)) {
            String role = JWTUtils.getRole(token);

            if ("manager".equals(role)) {
                session.setAttribute("loggedInUserRole", "manager");
                response.sendRedirect("managerDashboard.jsp");
            } else {
                // Không phải manager → từ chối truy cập hoặc chuyển hướng
                response.sendRedirect("accessDenied.jsp");
            }
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

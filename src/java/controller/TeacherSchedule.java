/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.TeacherScheduleDAO;
import dao.TeacherDAO;
import dao.AccountDAO;
import modal.TeacherModal;
import modal.AccountModal;

/**
 *
 * @author Astersa
 */
public class TeacherSchedule extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Lấy thông tin user từ request attribute (được set bởi AuthFilter)
            String loggedInUserName = (String) request.getAttribute("loggedInUserName");
            String loggedInUserRole = (String) request.getAttribute("loggedInUserRole");
            
            // Kiểm tra đăng nhập và role
            if (loggedInUserName == null || loggedInUserRole == null) {
                response.sendRedirect("dang-nhap");
                return;
            }
            
            if (!"teacher".equals(loggedInUserRole)) {
                response.sendRedirect("dang-nhap");
                return;
            }
            
            // Lấy thông tin account từ database
            AccountDAO accountDAO = new AccountDAO();
            AccountModal account = accountDAO.getAccountByUsername(loggedInUserName);
            
            if (account == null) {
                response.sendRedirect("dang-nhap");
                return;
            }
            
            // Lấy thông tin giáo viên
            TeacherDAO teacherDAO = new TeacherDAO();
            TeacherModal teacher = teacherDAO.getTeacherByAccountID(account.getId());
            
            if (teacher == null) {
                request.setAttribute("error", "Không tìm thấy thông tin giáo viên");
                request.getRequestDispatcher("/views/error.jsp").forward(request, response);
                return;
            }
            
            // Lấy parameter week nếu có
            String weekParam = request.getParameter("week");
            java.time.LocalDate targetWeek = null;
            
            if (weekParam != null && !weekParam.isEmpty()) {
                try {
                    targetWeek = java.time.LocalDate.parse(weekParam);
                } catch (Exception e) {
                    // Nếu parse lỗi thì dùng tuần hiện tại
                    targetWeek = java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY);
                }
            } else {
                // Mặc định là tuần hiện tại
                targetWeek = java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY);
            }
            
            // Lấy schedule từ database
            TeacherScheduleDAO scheduleDAO = new TeacherScheduleDAO();
            Map<String, Object> scheduleData = scheduleDAO.getTeacherSchedule(teacher.getId());
            
            // Truyền dữ liệu cho JSP
            request.setAttribute("scheduleData", scheduleData);
            request.setAttribute("teacher", teacher);
            request.setAttribute("account", account);
            request.setAttribute("targetWeek", targetWeek);
            
            request.getRequestDispatcher("/views/teacherSchedule.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải thời khóa biểu: " + e.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}

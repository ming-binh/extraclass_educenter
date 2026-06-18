/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.ParentScheduleDAO;
import dao.ParentDAO;
import dao.AccountDAO;
import modal.ParentModal;
import modal.AccountModal;

/**
 *
 * @author Astersa
 */
public class ParentSchedule extends HttpServlet {

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
            
            if (!"parent".equals(loggedInUserRole)) {
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
            
            // Lấy thông tin phụ huynh
            ParentDAO parentDAO = new ParentDAO();
            ParentModal parent = parentDAO.getParentByAccountID(account.getId());
            
            if (parent == null) {
                request.setAttribute("error", "Không tìm thấy thông tin phụ huynh");
                request.getRequestDispatcher("/views/error.jsp").forward(request, response);
                return;
            }
            
            // Lấy danh sách học sinh của phụ huynh
            ParentScheduleDAO scheduleDAO = new ParentScheduleDAO();
            List<Map<String, Object>> students = scheduleDAO.getParentStudents(parent.getId());
            
            if (students.isEmpty()) {
                request.setAttribute("error", "Bạn chưa có học sinh nào được đăng ký");
                request.getRequestDispatcher("/views/error.jsp").forward(request, response);
                return;
            }
            
            // Lấy parameter studentId nếu có
            String studentIdParam = request.getParameter("studentId");
            Integer selectedStudentId = null;
            
            if (studentIdParam != null && !studentIdParam.isEmpty()) {
                try {
                    selectedStudentId = Integer.parseInt(studentIdParam);
                    // Kiểm tra xem studentId có thuộc về phụ huynh này không
                    boolean isValidStudent = false;
                    for (Map<String, Object> student : students) {
                        if (selectedStudentId.equals(student.get("id"))) {
                            isValidStudent = true;
                            break;
                        }
                    }
                    if (!isValidStudent) {
                        selectedStudentId = null;
                    }
                } catch (NumberFormatException e) {
                    selectedStudentId = null;
                }
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
            
            // Lấy schedule của học sinh được chọn
            Map<String, Object> scheduleData = null;
            if (selectedStudentId != null) {
                scheduleData = scheduleDAO.getStudentSchedule(selectedStudentId);
            }
            
            // Truyền dữ liệu cho JSP
            request.setAttribute("students", students);
            request.setAttribute("selectedStudentId", selectedStudentId);
            request.setAttribute("scheduleData", scheduleData);
            request.setAttribute("parent", parent);
            request.setAttribute("account", account);
            request.setAttribute("targetWeek", targetWeek);
            
            request.getRequestDispatcher("/views/parentSchedule.jsp").forward(request, response);
            
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.StudentCourseDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author hungd
 */
public class RegisterCourseServlet extends HttpServlet {

    private final StudentCourseDAO studentCourseDao = new StudentCourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String courseIdStr = request.getParameter("courseId");
        String studentIdStr = request.getParameter("studentId");
        System.out.println(courseIdStr); 
        System.out.println(studentIdStr); 

        try {
            int courseId = Integer.parseInt(courseIdStr);
            int studentId = Integer.parseInt(studentIdStr);

            // Thêm học sinh vào khóa học
            boolean success = studentCourseDao.addStudentToCourse(studentId, courseId);
            if (success) {
                session.setAttribute("successMessage", "Đăng ký thành công! Yêu cầu đang chờ duyệt.");
            } else {
                session.setAttribute("errorMessage", "Không thể đăng ký học sinh. Vui lòng thử lại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Đăng ký thất bại. Vui lòng thử lại sau.");
        }

        // Redirect về trang trước (sau try-catch)
        response.sendRedirect("thong-tin-lop-hoc?courseId=" + courseIdStr);

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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.StudentCourseDAO;
import dto.CoursePendingRequestDTO;
import java.io.IOException;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modal.CourseModal;
import modal.StudentCourseModal;

/**
 *
 * @author hungd
 */
public class ManagerCourseRegistration extends HttpServlet {

    final StudentCourseDAO studentCourseDAO = new StudentCourseDAO();
    final CourseDAO courseDAO = new CourseDAO();

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

        try {

            // Lấy tất cả bản ghi có status = "pending"
            List<StudentCourseModal> pendingList = studentCourseDAO.getStudentCoursesByStatus("pending");

            // Map để đếm số yêu cầu mỗi courseId
            Map<Integer, Integer> courseRequestCountMap = new HashMap<>();

            for (StudentCourseModal sc : pendingList) {
                int courseId = sc.getCourseId();
                // Tăng số lượng nếu đã có, chưa có thì gán là 1
                if (courseRequestCountMap.containsKey(courseId)) {
                    int current = courseRequestCountMap.get(courseId);
                    courseRequestCountMap.put(courseId, current + 1);
                } else {
                    courseRequestCountMap.put(courseId, 1);
                }
            }

            // Chuẩn bị danh sách DTO để gửi sang JSP
            List<CoursePendingRequestDTO> overviewList = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : courseRequestCountMap.entrySet()) {
                int courseId = entry.getKey();
                int pendingCount = entry.getValue();

                CourseModal course = courseDAO.getCourseById(courseId);
                if (course != null) {
                    CoursePendingRequestDTO dto = new CoursePendingRequestDTO(
                            courseId,
                            course.getName(),
                            pendingCount
                    );
                    overviewList.add(dto);
                }
            }

            // Gửi sang JSP
            request.setAttribute("courseRequestOverview", overviewList);
            request.getRequestDispatcher("views/managerCourseRegistration.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xử lý yêu cầu.");
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

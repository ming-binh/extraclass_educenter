/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.StudentCourseDAO;
import dao.StudentPaymentScheduleDAO;
import dao.StudentSectionDAO;
import dto.CourseDTO;
import dto.StudentCourseRequestDTO;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import modal.StudentCourseModal;

/**
 *
 * @author hungd
 */
public class CourseRegistrationDetails extends HttpServlet {

    private final CourseDAO courseDao = new CourseDAO();
    private final StudentCourseDAO studentCourseDao = new StudentCourseDAO();
    private final StudentSectionDAO studentSectionDAO = new StudentSectionDAO();
     private final StudentPaymentScheduleDAO studentPaymentSchedule = new StudentPaymentScheduleDAO();

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
        String courseIdStr = request.getParameter("courseId");

        if (courseIdStr == null || courseIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Thiếu mã khóa học.");
            request.getRequestDispatcher("views/managerCourseRegistration.jsp").forward(request, response);
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdStr);

            // Lấy session hiện tại
            HttpSession session = request.getSession();

            // Nhận message nếu có (khi redirect từ POST)
            String message = request.getParameter("message");
            if (message != null && !message.isEmpty()) {
                request.setAttribute("message", message); // để hiển thị ra JSP
            }

            // Lấy danh sách yêu cầu tham gia khóa học
            List<StudentCourseRequestDTO> requestList = studentCourseDao.getStudentCoursesRequest(courseId);

            // Lấy thông tin khóa học từ CourseDTO
            CourseDTO course = courseDao.getCourseByIdCourse(courseId);
            if (course == null) {
                request.setAttribute("errorMessage", "Không tìm thấy thông tin khóa học với ID = " + courseId);
            } else {
                session.setAttribute("course", course); // Lưu cả CourseDTO
            }

            session.setAttribute("requestList", requestList);
            request.getRequestDispatcher("views/courseRegistrationDetails.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID khóa học không hợp lệ.");
            request.getRequestDispatcher("views/managerCourseRegistration.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CourseRegistrationDetails.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống.");
            request.getRequestDispatcher("views/managerCourseRegistration.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String action = request.getParameter("action");

        try {
            String message;
            int courseId = -1;

            if ("accept".equals(action)) {
                boolean updated = studentCourseDao.updateStatus(requestId, "accepted");
                if (updated) {
                    // Lấy thông tin khóa học liên quan đến yêu cầu
                    StudentCourseModal studentCourse = studentCourseDao.getStudentCourseById(requestId);
                    courseId = studentCourse.getCourseId();
                    int studentId = studentCourse.getStudentId();

                    // Tăng sĩ số lớp
                    courseDao.incrementEnrollment(courseId);

                    // Thêm học sinh vào các buổi học đang hoạt động và lấy IDs
                    List<Integer> studentSectionIds = studentSectionDAO.addStudentToActiveSections(studentId, courseId);

                    

                    // Tạo payment schedule cho các student sections
                    if (!studentSectionIds.isEmpty()) {
                        try {
                            boolean paymentScheduleCreated = studentPaymentSchedule.createPaymentSchedulesForStudentSections(studentSectionIds, courseId);
                            
                            if (!paymentScheduleCreated) {
                                message = "Yêu cầu đã được chấp nhận nhưng có lỗi khi tạo lịch thanh toán.";
                            } else {
                                message = "Yêu cầu đã được chấp nhận và lịch thanh toán đã được tạo thành công.";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            message = "Yêu cầu đã được chấp nhận nhưng có lỗi khi tạo lịch thanh toán: " + e.getMessage();
                        }
                    } else {
                        message = "Yêu cầu đã được chấp nhận nhưng không có buổi học nào đang hoạt động.";
                    }
                } else {
                    message = "Cập nhật trạng thái thất bại.";
                    StudentCourseModal sc = studentCourseDao.getStudentCourseById(requestId);
                    if (sc != null) {
                        courseId = sc.getCourseId();
                    }
                }

            } else if ("reject".equals(action)) {
                boolean updated = studentCourseDao.updateStatus(requestId, "rejected");

                StudentCourseModal sc = studentCourseDao.getStudentCourseById(requestId);
                if (sc != null) {
                    courseId = sc.getCourseId();
                }

                message = updated ? "Yêu cầu đã bị từ chối." : "Cập nhật trạng thái thất bại.";
            } else {
                message = "Hành động không hợp lệ.";
            }

            if (courseId != -1) {
                response.sendRedirect("chi-tiet-yeu-cau?courseId=" + courseId + "&message=" + URLEncoder.encode(message, "UTF-8"));
            } else {
                response.sendRedirect("danh-sach-khoa-hoc?message=" + URLEncoder.encode("Không xác định được khóa học.", "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Đã xảy ra lỗi: " + e.getMessage();
            response.sendRedirect("danh-sach-khoa-hoc?message=" + URLEncoder.encode(errorMessage, "UTF-8"));
        }
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

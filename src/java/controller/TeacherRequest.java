/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RequestDAO;
import dao.TeacherScheduleDAO;
import dao.CourseDAO;
import modal.RequestModal;
import modal.AccountModal;
import modal.CourseModal;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.SectionDAO;
import dao.TeacherDAO;
import modal.TeacherModal;
/**
 *
 * @author Astersa
 */
@WebServlet(name = "TeacherRequest", urlPatterns = {"/yeu-cau-giao-vien"})
public class TeacherRequest extends HttpServlet {

    private RequestDAO requestDAO;
    private TeacherScheduleDAO teacherScheduleDAO;
    private CourseDAO courseDAO;
    private SectionDAO sectionDAO;
    private TeacherDAO teacherDAO;
    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAO();
        teacherScheduleDAO = new TeacherScheduleDAO();
        courseDAO = new CourseDAO();
        sectionDAO = new SectionDAO();
        teacherDAO = new TeacherDAO();
    }

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
        response.setContentType("text/html;charset=UTF-8");
        
        // Lấy thông tin user từ request attribute (được set bởi AuthFilter)
        String loggedInUserName = (String) request.getAttribute("loggedInUserName");
        String loggedInUserRole = (String) request.getAttribute("loggedInUserRole");
        Integer loggedInUserId = (Integer) request.getAttribute("loggedInUserId");
        
        // Kiểm tra đăng nhập và role
        if (loggedInUserName == null || loggedInUserRole == null) {
            response.sendRedirect("dang-nhap");
            return;
        }
        
        try {
            // Lấy danh sách yêu cầu của giáo viên đã đăng nhập
            List<RequestModal> requests = requestDAO.getRequestsByUser(loggedInUserId);
            
            // Format ngày tạo cho mỗi request
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (RequestModal req : requests) {
                if (req.getCreatedAt() != null) {
                    String formattedDate = req.getCreatedAt().format(formatter);
                    req.setDescription(req.getDescription() + " | Tạo lúc: " + formattedDate);
                }
            }
            
            request.setAttribute("requests", requests);
            TeacherModal teacher = teacherDAO.getTeacherByAccountID(loggedInUserId);
            List<Map<String, Object>> teacherSections = sectionDAO.getTeacherSections(teacher.getId());
            request.setAttribute("sections", teacherSections);
            
            // Lấy danh sách courses của giáo viên
            List<CourseModal> teacherCourses = courseDAO.getCourseByTeacher(teacher.getId());
            request.setAttribute("courses", teacherCourses);
            
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi tải dữ liệu: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/teacherRequest.jsp").forward(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        
        // Lấy thông tin user từ request attribute (được set bởi AuthFilter)
        String loggedInUserName = (String) request.getAttribute("loggedInUserName");
        String loggedInUserRole = (String) request.getAttribute("loggedInUserRole");
        Integer loggedInUserId = (Integer) request.getAttribute("loggedInUserId");
        
        // Kiểm tra đăng nhập và role
        if (loggedInUserName == null || loggedInUserRole == null) {
            response.sendRedirect("dang-nhap");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            try {
                String type = request.getParameter("type");
                String description = request.getParameter("description");
                String sectionIdStr = request.getParameter("sectionId");
                String courseIdStr = request.getParameter("courseId");
                
                if (description == null || description.trim().isEmpty()) {
                    request.setAttribute("error", "Vui lòng nhập mô tả chi tiết!");
                    doGet(request, response);
                    return;
                }
                
                RequestModal newRequest = new RequestModal();
                newRequest.setRequestBy(loggedInUserId);
                newRequest.setType(RequestModal.ReqType.valueOf(type));
                newRequest.setDescription(description.trim());
                newRequest.setStatus(RequestModal.Status.pending);
                newRequest.setCreatedAt(LocalDateTime.now());
                
                // Xử lý sectionId hoặc courseId tùy theo loại yêu cầu
                if ("teacher-add-section".equals(type)) {
                    if (courseIdStr != null && !courseIdStr.trim().isEmpty()) {
                        try {
                            Integer courseId = Integer.parseInt(courseIdStr);
                            newRequest.setCourseId(courseId);
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "ID khóa học không hợp lệ");
                            doGet(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("error", "Vui lòng chọn khóa học!");
                        doGet(request, response);
                        return;
                    }
                } else {
                    if (sectionIdStr != null && !sectionIdStr.trim().isEmpty()) {
                        try {
                            Integer sectionId = Integer.parseInt(sectionIdStr);
                            newRequest.setSectionId(sectionId);
                        } catch (NumberFormatException e) {
                            request.setAttribute("error", "ID buổi học không hợp lệ");
                            doGet(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("error", "Vui lòng chọn buổi học!");
                        doGet(request, response);
                        return;
                    }
                }
                
                boolean success = requestDAO.createRequest(newRequest);
                
                if (success) {
                    request.setAttribute("success", "Yêu cầu đã được gửi thành công!");
                } else {
                    request.setAttribute("error", "Có lỗi xảy ra khi gửi yêu cầu");
                }
                
            } catch (Exception e) {
                request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            }
        }
        
        doGet(request, response);
    }

}
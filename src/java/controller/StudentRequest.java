/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import dao.RequestDAO;
import dao.CourseDAO;
import modal.RequestModal;
import modal.RequestModal.Status;
import modal.CourseModal;

/**
 *
 * @author Astersa
 */
@WebServlet(name = "StudentRequest", urlPatterns = {"/yeu-cau-hoc-sinh"})
public class StudentRequest extends HttpServlet {

    private RequestDAO requestDAO;
    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAO();
        courseDAO = new CourseDAO();
    }

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
        
        String loggedInUserName = (String) request.getAttribute("loggedInUserName");
        String loggedInUserRole = (String) request.getAttribute("loggedInUserRole");
        Integer loggedInUserId = (Integer) request.getAttribute("loggedInUserId");
        
        // Kiểm tra đăng nhập và role
        if (loggedInUserName == null || loggedInUserRole == null) {
            response.sendRedirect("dang-nhap");
            return;
        }
        
        try {
            List<RequestModal> userRequests = requestDAO.getRequestsByUser(loggedInUserId);
            
            for (RequestModal req : userRequests) {
                if (req.getCreatedAt() != null) {
                    String formattedDate = req.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    req.setDescription(req.getDescription() + " [Ngày tạo: " + formattedDate + "]");
                }
            }
            
            request.setAttribute("requests", userRequests);
            
            // Lấy danh sách khóa học đang hoạt động
            List<CourseModal> fromCourses = courseDAO.getEnrollCourseByAccountId(loggedInUserId);
            List<CourseModal> toCourses = courseDAO.getUnenrolledCoursesByAccountId(loggedInUserId);
            request.setAttribute("fromCourses", fromCourses);
            request.setAttribute("toCourses", toCourses);
            
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi tải dữ liệu: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/studentRequest.jsp").forward(request, response);
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
            createRequest(request, response, loggedInUserId);
        } else {
            // Mặc định hiển thị danh sách
            doGet(request, response);
        }
    }
    
    private void createRequest(HttpServletRequest request, HttpServletResponse response, Integer loggedInUserId) 
            throws ServletException, IOException {
        
        try {
            String type = request.getParameter("type");
            String description = request.getParameter("description");
            String fromCourseIdStr = request.getParameter("fromCourseId");
            String toCourseIdStr = request.getParameter("toCourseId");
            
            if (type == null || type.trim().isEmpty()) {
                request.setAttribute("error", "Loại yêu cầu không được để trống");
                doGet(request, response);
                return;
            }
            
            if (description == null || description.trim().isEmpty()) {
                request.setAttribute("error", "Mô tả không được để trống");
                doGet(request, response);
                return;
            }
            
            RequestModal newRequest = new RequestModal();
            newRequest.setRequestBy(loggedInUserId);
            newRequest.setType(RequestModal.ReqType.valueOf(type));
            newRequest.setDescription(description);
            newRequest.setStatus(Status.pending);
            
            // Xử lý courseId nếu có
            if (fromCourseIdStr != null && !fromCourseIdStr.trim().isEmpty() && toCourseIdStr != null && !toCourseIdStr.trim().isEmpty()) {
                try {
                    Integer fromCourseId = Integer.parseInt(fromCourseIdStr);
                    Integer toCourseId = Integer.parseInt(toCourseIdStr);
                    newRequest.setFromCourseId(fromCourseId);
                    newRequest.setToCourseId(toCourseId);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "ID khóa học không hợp lệ");
                    doGet(request, response);
                    return;
                }
            }
            
            // Lưu vào database
            boolean success = requestDAO.createRequest(newRequest);
            
            if (success) {
                request.setAttribute("success", "Yêu cầu đã được gửi thành công!");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi gửi yêu cầu");
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        doGet(request, response);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RequestDAO;
import modal.RequestModal;
import modal.AccountModal;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Astersa
 */
public class ParentRequest extends HttpServlet {

    private RequestDAO requestDAO;

    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAO();
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
            // Lấy danh sách yêu cầu của phụ huynh đã đăng nhập
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
            
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách yêu cầu: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/views/parentRequest.jsp").forward(request, response);
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

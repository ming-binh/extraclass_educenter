/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import modal.AccountModal;
import org.apache.catalina.User;
import utils.FileUploadUtils;

/**
 *
 * @author vankh
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class UploadAvatarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin từ Filter (không cần thay đổi session)
        Integer accountId = (Integer) request.getAttribute("loggedInUserId");
        String role = (String) request.getAttribute("loggedInUserRole");
        String usname = (String) request.getAttribute("loggedInUserName");

        if (accountId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Part filePart = request.getPart("avatar");
        if (filePart == null || filePart.getSize() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded");
            return;
        }

        try {
            // Lưu file vật lý
            String relativePath = FileUploadUtils.saveFile(filePart, "avatars", getServletContext());
            String newAvatarUrl = "/assets/" + relativePath;

            // Cập nhật database
            AccountDAO accountDAO = new AccountDAO();
            boolean success = accountDAO.updateAvatar(usname, newAvatarUrl);

            if (success) {
                response.setContentType("application/json");
                response.getWriter().print("{\"success\":true, \"avatarUrl\":\"" + newAvatarUrl + "\"}");
                response.sendRedirect("userProfile");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database update failed");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Upload failed: " + e.getMessage());
        }
    }
}

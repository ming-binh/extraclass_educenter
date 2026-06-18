/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import modal.AccountModal;
import utils.EmailUtil;
import utils.HashUtils;

/**
 *
 * @author ASUS
 */
public class ForgotPasswordServlet extends HttpServlet {


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
private AccountDAO accountDAO = new AccountDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String step = request.getParameter("step");
        if (step == null) step = "1";
        
        switch (step) {
            case "1":
                request.getRequestDispatcher("/views/forgot-password-step1.jsp").forward(request, response);
                break;
            case "2":
                request.getRequestDispatcher("/views/forgot-password-step2.jsp").forward(request, response);
                break;
            case "3":
                request.getRequestDispatcher("/views/forgot-password-step3.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("quen-mat-khau");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        switch (action) {
            case "send-code":
                handleSendCode(request, response, session);
                break;
            case "verify-code":
                handleVerifyCode(request, response, session);
                break;
            case "reset-password":
                handleResetPassword(request, response, session);
                break;
            default:
                response.sendRedirect("quen-mat-khau");
        }
    }
    
    private void handleSendCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws IOException, ServletException {
        
        String email = request.getParameter("email");
        
        try {
            // Kiểm tra email có tồn tại không
            AccountModal account = accountDAO.getAccountByUsername(email);
            if (account == null) {
                request.setAttribute("error", "Email không tồn tại trong hệ thống!");
                request.getRequestDispatcher("/views/forgot-password-step1.jsp").forward(request, response);
                return;
            }
            
            // Tạo mã xác thực
            String verificationCode = EmailUtil.generateVerificationCode();
            
            // Gửi email
            boolean emailSent = EmailUtil.sendVerificationCode(email, verificationCode);
            if (!emailSent) {
                request.setAttribute("error", "Không thể gửi email! Vui lòng thử lại.");
                request.getRequestDispatcher("/views/forgot-password-step1.jsp").forward(request, response);
                return;
            }
            
            // Lưu thông tin vào session
            session.setAttribute("reset_email", email);
            session.setAttribute("verification_code", verificationCode);
            session.setAttribute("code_time", LocalDateTime.now());
            
            response.sendRedirect("quen-mat-khau?step=2");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra! Vui lòng thử lại.");
            request.getRequestDispatcher("/views/forgot-password-step1.jsp").forward(request, response);
        }
    }
    
    private void handleVerifyCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws IOException, ServletException {
        
        String inputCode = request.getParameter("code");
        String sessionCode = (String) session.getAttribute("verification_code");
        LocalDateTime codeTime = (LocalDateTime) session.getAttribute("code_time");
        
        // Kiểm tra mã có tồn tại không
        if (sessionCode == null || codeTime == null) {
            request.setAttribute("error", "Phiên làm việc đã hết hạn! Vui lòng thử lại.");
            response.sendRedirect("quen-mat-khau");
            return;
        }
        
        // Kiểm tra mã có hết hạn không (10 phút)
        if (codeTime.plusMinutes(10).isBefore(LocalDateTime.now())) {
            session.removeAttribute("verification_code");
            session.removeAttribute("code_time");
            request.setAttribute("error", "Mã xác thực đã hết hạn! Vui lòng thử lại.");
            response.sendRedirect("quen-mat-khau");
            return;
        }
        
        // Kiểm tra mã có đúng không
        if (!sessionCode.equals(inputCode)) {
            request.setAttribute("error", "Mã xác thực không đúng!");
            request.getRequestDispatcher("/views/forgot-password-step2.jsp").forward(request, response);
            return;
        }
        
        // Xác thực thành công
        session.setAttribute("code_verified", true);
        response.sendRedirect("quen-mat-khau?step=3");
    }
    
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
            throws IOException, ServletException {
        
        Boolean codeVerified = (Boolean) session.getAttribute("code_verified");
        if (codeVerified == null || !codeVerified) {
            response.sendRedirect("quen-mat-khau");
            return;
        }
        
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = (String) session.getAttribute("reset_email");
        
        // Kiểm tra mật khẩu
        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("/views/forgot-password-step3.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/views/forgot-password-step3.jsp").forward(request, response);
            return;
        }
        
        try {
            // Hash mật khẩu mới
            String hashedPassword = HashUtils.hashPassword(newPassword);
            
            // Cập nhật mật khẩu
            boolean updated = accountDAO.updatePassword(email, hashedPassword);
            if (updated) {
                // Xóa session
                session.removeAttribute("reset_email");
                session.removeAttribute("verification_code");
                session.removeAttribute("code_time");
                session.removeAttribute("code_verified");
                
                request.setAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
                request.getRequestDispatcher("/views/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không thể cập nhật mật khẩu! Vui lòng thử lại.");
                request.getRequestDispatcher("/views/forgot-password-step3.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra! Vui lòng thử lại.");
            request.getRequestDispatcher("/views/forgot-password-step3.jsp").forward(request, response);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

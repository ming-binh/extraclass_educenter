/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import modal.AccountModal;
import utils.HashUtils;
import utils.JWTUtils;

/**
 *
 * @author ASUS
 */
public class CreateStaffAndManagerAccountServlet extends HttpServlet {

    private final Pattern textPattern = Pattern.compile("^[\\p{L} ]{3,}$");
    private final Pattern phonePattern = Pattern.compile("^\\d{10}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendRedirect("dang-nhap");
            return;
        }

        String currentRole = JWTUtils.getRole(token);
        String targetRole;

        if ("admin".equals(currentRole)) {
            targetRole = "manager";
        } else if ("manager".equals(currentRole)) {
            targetRole = "staff";
        } else {
            request.setAttribute("errorLog", "Bạn không có quyền tạo tài khoản.");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("targetRole", targetRole);
        request.getRequestDispatcher("/views/create-staff-manager-account.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendRedirect("dang-nhap");
            return;
        }

        String creatorRole = JWTUtils.getRole(token);
        String targetRole = request.getParameter("role");

        if ("admin".equals(creatorRole) && !"manager".equals(targetRole)
                || "manager".equals(creatorRole) && !"staff".equals(targetRole)) {
            request.setAttribute("error", "Không có quyền tạo vai trò này.");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        try {
            String name = request.getParameter("name");
            String username = request.getParameter("username");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String address = request.getParameter("address");
            String dobRaw = request.getParameter("dob");

            // Validate input
            if (!textPattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Họ tên không hợp lệ (ít nhất 3 ký tự, không chứa số).");
            }

            if (address != null && !address.isEmpty() && !textPattern.matcher(address).matches()) {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ (chỉ chữ, ít nhất 3 ký tự).");
            }

            if (phone != null && !phone.isEmpty() && !phonePattern.matcher(phone).matches()) {
                throw new IllegalArgumentException("Số điện thoại phải có đúng 10 chữ số.");
            }

            LocalDate dob = null;
            if (dobRaw != null && !dobRaw.isEmpty()) {
                try {
                    dob = LocalDate.parse(dobRaw);
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Định dạng ngày sinh không đúng (YYYY-MM-DD).");
                }

                if (dob.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Ngày sinh không thể là tương lai.");
                }

                if (dob.isBefore(LocalDate.now().minusYears(100))) {
                    throw new IllegalArgumentException("Ngày sinh không hợp lý.");
                }

                if (dob.isAfter(LocalDate.now().minusYears(18))) {
                    throw new IllegalArgumentException("Bạn phải từ 18 tuổi trở lên.");
                }
            }

            AccountDAO dao = new AccountDAO();

            if (dao.isUsernameExist(username)) {
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
            }

            if (phone != null && !phone.isEmpty() && dao.isPhoneExist(phone)) {
                throw new IllegalArgumentException("Số điện thoại đã được sử dụng.");
            }

            // Tạo đối tượng tài khoản
            AccountModal account = new AccountModal();
            account.setName(name);
            account.setUsername(username);
            account.setPhone(phone);
            account.setPassword(HashUtils.hashPassword(password));
            account.setAddress(address);
            account.setDob(dob != null ? dob.atStartOfDay() : null);
            account.setRole(AccountModal.Role.valueOf(targetRole));
            account.setStatus(AccountModal.Status.active);
            account.setCreatedAt(LocalDateTime.now());
            account.setUpdatedAt(LocalDateTime.now());
            account.setAvatarURL(null);

            int id = dao.insertAccount(account);
            if (id > 0) {
                response.sendRedirect("danh-sach-nhan-vien");
            } else {
                request.setAttribute("error", "Không thể tạo tài khoản.");
                request.setAttribute("targetRole", targetRole);
                request.getRequestDispatcher("/views/create-staff-manager-account.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("targetRole", targetRole);
            request.getRequestDispatcher("/views/create-staff-manager-account.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("targetRole", targetRole);
            request.getRequestDispatcher("/views/create-staff-manager-account.jsp").forward(request, response);
        }
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public String getServletInfo() {
        return "Tạo tài khoản staff hoặc manager (chỉ admin hoặc manager có quyền)";
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.ParentDAO;
import dao.SchoolClassDAO;
import dao.SchoolDAO;
import dao.StudentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import modal.AccountModal;
import modal.ParentModal;
import modal.SchoolClass;
import modal.SchoolModal;
import modal.StudentModal;
import utils.HashUtils;
import utils.JWTUtils;

/**
 *
 * @author ASUS
 */
public class CreateStudentAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendRedirect("dang-nhap");
            return;
        }

        String role = JWTUtils.getRole(token);
        String username = JWTUtils.getIdentifier(token);

        if (!"parent".equals(role)) {
            request.setAttribute("errorLog", "Chỉ phụ huynh mới có thể tạo tài khoản học sinh.");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        try {
            AccountModal parentAccount = new AccountDAO().getAccountByUsername(username);
            ParentModal parent = new ParentDAO().getParentByAccountID(parentAccount.getId());

            if (parent == null) {
                request.setAttribute("errorLog", "Không tìm thấy thông tin phụ huynh.");
                request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                return;
            }
            SchoolDAO schoolDAO = new SchoolDAO();
            SchoolClassDAO classDAO = new SchoolClassDAO();
            // Lấy dữ liệu cần thiết
            List<SchoolModal> schoolList = schoolDAO.getAllSchools();
            Map<Integer, List<SchoolClass>> schoolClassMap = classDAO.getMapSchoolClass();
            Map<Integer, String> schoolIdNameMap = schoolDAO.getIdNameMap();

            List<StudentModal> existingChildren = new StudentDAO().getChildrenOfParent(parent.getId());
            int childCount = existingChildren.size() + 1;
            String formattedIndex = String.format("hs%02d", childCount); // hs01, hs02,...
            String studentUsername = parentAccount.getPhone() + formattedIndex;

            if (new AccountDAO().isUsernameExist(studentUsername)) {
                request.setAttribute("error", "Tên tài khoản học sinh đã tồn tại.");
                request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
                return;
            }
            // Gửi sang JSP
            request.setAttribute("generatedUsername", studentUsername);
            request.setAttribute("schools", schoolList);
            request.setAttribute("schoolClassMap", schoolClassMap);
            request.setAttribute("schoolIdNameMap", schoolIdNameMap);
            request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
            request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendRedirect("dang-nhap");
            return;
        }

        String role = JWTUtils.getRole(token);
        String username = JWTUtils.getIdentifier(token);

        if (!"parent".equals(role)) {
            request.setAttribute("errorLog", "Chỉ phụ huynh mới có thể tạo tài khoản học sinh.");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        try {
            AccountModal parentAccount = new AccountDAO().getAccountByUsername(username);
            ParentModal parent = new ParentDAO().getParentByAccountID(parentAccount.getId());

            if (parent == null) {
                request.setAttribute("errorLog", "Không tìm thấy thông tin phụ huynh.");
                request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                return;
            }

            // Lấy dữ liệu từ form
            String name = request.getParameter("name");
            String stuusername = request.getParameter("username");
            String phoneInput = request.getParameter("phone");
            String dobRaw = request.getParameter("dob");
            String address = request.getParameter("address");
            String password = request.getParameter("password");
            String currentGrade = request.getParameter("currentGrade");
            String schoolName = request.getParameter("schoolName");

            // Validate dữ liệu đầu vào
            Pattern textPattern = Pattern.compile("^[\\p{L}\\s]{3,}$"); // chữ unicode & space, >=3

            if (!textPattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Họ tên không hợp lệ (ít nhất 3 chữ và không chứa số).");
            }
            if (!textPattern.matcher(address).matches()) {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ.");
            }
            if (schoolName != null && !schoolName.trim().isEmpty() && !textPattern.matcher(schoolName).matches()) {
                throw new IllegalArgumentException("Tên trường không hợp lệ.");
            }

            if (dobRaw == null || dobRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Ngày sinh không được để trống.");
            }

            LocalDate dob;
            try {
                dob = LocalDate.parse(dobRaw);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Định dạng ngày sinh không đúng.");
            }

            if (dob.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Ngày sinh không thể là tương lai.");
            }

            String phone = (phoneInput == null || phoneInput.trim().isEmpty()) ? null : phoneInput.trim();
            if (phone != null) {
                Pattern phonePattern = Pattern.compile("^\\d{10}$");
                if (!phonePattern.matcher(phone).matches()) {
                    throw new IllegalArgumentException("Số điện thoại phải đúng 10 chữ số.");
                }
            }

            // Mã hóa mật khẩu
            String hashedPassword = HashUtils.hashPassword(password);

            // Tạo account
            AccountModal studentAccount = new AccountModal();
            studentAccount.setName(name);
            studentAccount.setUsername(stuusername);
            studentAccount.setPhone(phone);
            studentAccount.setPassword(hashedPassword);
            studentAccount.setDob(dob != null ? dob.atStartOfDay() : null);
            studentAccount.setAddress(address);
            studentAccount.setStatus(AccountModal.Status.pending);
            studentAccount.setRole(AccountModal.Role.student);
            studentAccount.setCreatedAt(LocalDateTime.now());
            studentAccount.setUpdatedAt(LocalDateTime.now());
            studentAccount.setAvatarURL(null);

            int accountId = new AccountDAO().insertAccount(studentAccount);
            if (accountId <= 0) {
                request.setAttribute("error", "Không thể tạo tài khoản học sinh.");
                request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
                return;
            }

            // Tạo hồ sơ student
            StudentModal student = new StudentModal();
            student.setAccountId(accountId);
            student.setParentId(parent.getId());
            student.setCurrentGrade(currentGrade);
            student.setCreatedAt(LocalDateTime.now());
            student.setUpdatedAt(LocalDateTime.now());

            try {
                new StudentDAO().insertStudent(student);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Không thể tạo hồ sơ học sinh: " + e.getMessage());
                request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("viewChild");
        } catch (IllegalArgumentException e) {
            // Lỗi do validate dữ liệu
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
            request.getRequestDispatcher("/views/create-student-account.jsp").forward(request, response);
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
        return "Short description";
    }// </editor-fold>

}

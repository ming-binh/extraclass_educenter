/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ConsultationDAO;
import dao.SchoolClassDAO;
import dao.SchoolDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import modal.ConsultationModal;
import modal.SchoolClass;
import modal.SchoolModal;
import utils.FileUploadUtils;

/**
 *
 * @author ASUS
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 5 * 1024 * 1024, // 5MB
        maxRequestSize = 20 * 1024 * 1024 // 20MB
)
public class ConsultationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SchoolDAO schoolDAO = new SchoolDAO();
        SchoolClassDAO classDAO = new SchoolClassDAO();

        // Lấy dữ liệu cần thiết
        List<SchoolModal> schoolList = schoolDAO.getAllSchools();
        Map<Integer, List<SchoolClass>> schoolClassMap = classDAO.getMapSchoolClass();
        Map<Integer, String> schoolIdNameMap = schoolDAO.getIdNameMap();

        // Gửi sang JSP
        request.setAttribute("schools", schoolList);
        request.setAttribute("schoolClassMap", schoolClassMap);
        request.setAttribute("schoolIdNameMap", schoolIdNameMap);

        // Chuyển tiếp sang trang JSP
        request.getRequestDispatcher("/views/consultation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Luôn nạp lại dữ liệu cho form
        loadSchoolData(request);

        try {
            // --- 1. Đọc và validate input ---
            String role = request.getParameter("role");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dobRaw = request.getParameter("dob");
            String schoolName = request.getParameter("schoolName");
            String schoolClassIdRaw = request.getParameter("schoolClassId");
            String subjectRaw = request.getParameter("subject"); // Nhận dạng String từ form
            String experience = request.getParameter("experience");

            Pattern textPattern = Pattern.compile("^[\\p{L}\\s]{3,}$"); // chữ unicode & space, >=3
            Pattern phonePattern = Pattern.compile("^\\d{10}$");
            Pattern emailPattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

            if (!textPattern.matcher(name).matches()) {
                throw new IllegalArgumentException("Họ tên không hợp lệ (ít nhất 3 chữ và không chứa số).");
            }
            if (!textPattern.matcher(address).matches()) {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ.");
            }
            if (schoolName != null && !schoolName.isEmpty() && !textPattern.matcher(schoolName).matches()) {
                throw new IllegalArgumentException("Tên trường không hợp lệ.");
            }

            if (dobRaw == null || dobRaw.trim().isEmpty()) {
                throw new IllegalArgumentException("Ngày sinh không được để trống.");
            }
            LocalDate dob;
            try {
                dob = LocalDate.parse(dobRaw, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Định dạng ngày sinh không đúng.");
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

            Integer schoolClassId = null;
            if (schoolClassIdRaw != null && !schoolClassIdRaw.isEmpty()) {
                schoolClassId = Integer.parseInt(schoolClassIdRaw);
            }
            if (!phonePattern.matcher(phone).matches()) {
                throw new IllegalArgumentException("Số điện thoại phải gồm đúng 10 chữ số.");
            }

            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email không được để trống.");
            }
            if (!emailPattern.matcher(email).matches()) {
                throw new IllegalArgumentException("Địa chỉ email không hợp lệ.");
            }

            // Xử lý subject enum - chỉ áp dụng cho teacher
            ConsultationModal.Subject subjectEnum = null;
            if ("teacher".equals(role)) {
                if (subjectRaw == null || subjectRaw.trim().isEmpty()) {
                    throw new IllegalArgumentException("Môn học không được để trống đối với giáo viên.");
                }
                try {
                    subjectEnum = ConsultationModal.Subject.valueOf(subjectRaw);
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Môn học không hợp lệ.");
                }

                // Validate experience cho teacher
                if (experience == null || experience.trim().isEmpty()) {
                    throw new IllegalArgumentException("Kinh nghiệm không được để trống đối với giáo viên.");
                }
                if (experience.trim().length() < 10) {
                    throw new IllegalArgumentException("Mô tả kinh nghiệm phải có ít nhất 10 ký tự.");
                }
            }

            // --- 2. Lưu vào DB ---
            ConsultationDAO dao = new ConsultationDAO();
            Integer schoolId = dao.getSchoolIdByName(schoolName);
            if (schoolId == null) {
                schoolId = dao.insertSchool(schoolName);
            }

            ConsultationModal c = new ConsultationModal();
            c.setName(name);
            c.setPhone(phone);
            c.setEmail(email);
            c.setAddress(address);
            c.setDob(dob.atStartOfDay());
            c.setSchoolId(schoolId);
            c.setSchoolClassId(schoolClassId);
            c.setStatus(ConsultationModal.Status.pending);

            // Set subject và experience chỉ cho teacher
            if ("teacher".equals(role)) {
                c.setSubject(subjectEnum);
                c.setExperience(experience);
            } else {
                c.setSubject(null);
                c.setExperience(null);
            }

            int consultationId = dao.insertConsultation(c);

            // Xử lý upload certificates chỉ cho teacher
            if ("teacher".equals(role)) {
                for (Part part : request.getParts()) {
                    if ("certificates".equals(part.getName()) && part.getSize() > 0) {
                        String savedPath = FileUploadUtils.saveFile(part, "certs/", getServletContext());
                        dao.insertCertificate(consultationId, savedPath);
                    }
                }
            }

            // Thành công: chuyển tới trang riêng
            response.sendRedirect("dang-ky-tu-van-thanh-cong");
            return;

        } catch (IllegalArgumentException ex) {
            // Validation của người dùng: hiện lại form cùng message
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/views/consultation.jsp").forward(request, response);
            return;
        } catch (Exception ex) {
            // Lỗi bất ngờ: log full và forward sang error.jsp
            ex.printStackTrace();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            request.setAttribute("errorLog", sw.toString());
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }
    }

    private void loadSchoolData(HttpServletRequest request) {
        SchoolDAO schoolDAO = new SchoolDAO();
        SchoolClassDAO classDAO = new SchoolClassDAO();
        List<SchoolModal> schoolList = schoolDAO.getAllSchools();
        Map<Integer, List<SchoolClass>> schoolClassMap = classDAO.getMapSchoolClass();
        Map<Integer, String> schoolIdNameMap = schoolDAO.getIdNameMap();

        request.setAttribute("schools", schoolList);
        request.setAttribute("schoolClassMap", schoolClassMap);
        request.setAttribute("schoolIdNameMap", schoolIdNameMap);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

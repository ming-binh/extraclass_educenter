/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import utils.JWTUtils;
import dao.AssignmentDAO;
import dao.CourseDAO;
import dao.SubmissionDAO;
import modal.AssignmentModal;
import java.io.File;
import java.util.List;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import modal.CourseModal;
import modal.SubmissionModal;

/**
 * Servlet cho phép học sinh xem danh sách bài tập, tải file bài tập và nộp bài.
 * Bổ sung chức năng upload bài nộp của học sinh.
 *
 * URL chính: - GET /studentAssignments?courseId=... -> Danh sách bài tập - POST
 * /studentAssignments?action=submit -> Học sinh nộp bài
 *
 *  *
 * @author HanND
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)              // 50MB
public class StudentAssignmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer studentId = getStudentIdFromToken(request, response);
        if (studentId == null) {
            return; // Đã redirect nếu không hợp lệ
        }
        String action = request.getParameter("action");
        if ("download".equalsIgnoreCase(action)) {
            handleDownload(request, response);
            return;
        }

        String courseIdParam = request.getParameter("courseId");
        if (courseIdParam != null) {
            int courseId = Integer.parseInt(courseIdParam);
            AssignmentDAO assignmentDAO = new AssignmentDAO();
            List<AssignmentModal> assignments = assignmentDAO.getAssignmentsByCourse(courseId);

            SubmissionDAO submissionDAO = new SubmissionDAO();
            List<SubmissionModal> history = submissionDAO.getSubmissionHistoryByStudentAndCourse(studentId, courseId);
            request.setAttribute("assignments", assignments);
            request.setAttribute("courseId", courseId);
            request.setAttribute("history", history);
            request.getRequestDispatcher("/views/studentAssignments.jsp").forward(request, response);
        } else {
            CourseDAO courseDAO = new CourseDAO();
            List<CourseModal> courses = courseDAO.getCoursesByStudentId(studentId);
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/views/studentCourses.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer studentId = getStudentIdFromToken(request, response);
        if (studentId == null) {
            return;
        }

        String action = request.getParameter("action");
        if ("submit".equalsIgnoreCase(action)) {
            handleUploadSubmission(request, response, studentId);
        } else {
            doGet(request, response);
        }
    }

    /**
     * Xử lý upload bài nộp của học sinh.
     */
    private void handleUploadSubmission(HttpServletRequest request, HttpServletResponse response, int studentId)
            throws IOException, ServletException {
        try {
            String sectionAssignmentIdStr = request.getParameter("sectionAssignmentId");
            String courseIdStr = request.getParameter("courseId");

            if (sectionAssignmentIdStr == null || courseIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin sectionAssignmentId hoặc courseId.");
                return;
            }

            int sectionAssignmentId = Integer.parseInt(sectionAssignmentIdStr);
            int courseId = Integer.parseInt(courseIdStr);
            String note = request.getParameter("note");

            SubmissionDAO submissionDAO = new SubmissionDAO();

            // Lấy file nộp bài
            Part filePart = request.getPart("submissionFile");
            if (filePart == null || filePart.getSize() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Vui lòng chọn file để nộp.");
                return;
            }

            // Tạo tên file duy nhất
            String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
            String fileName = "submission_" + studentId + "_" + timestamp + "_" + originalName;

            // Thư mục lưu file
            String uploadPath = getServletContext().getRealPath("/submissions");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File fileToSave = new File(uploadDir, fileName);
            filePart.write(fileToSave.getAbsolutePath());
            System.out.println("Đã lưu bài nộp: " + fileToSave.getAbsolutePath());

            // Lưu thông tin bài nộp
            submissionDAO.insertSubmission(sectionAssignmentId, courseId, studentId, fileName);
            request.setAttribute("success", "Bạn đã nộp bài thành công.");

            AssignmentDAO assignmentDAO = new AssignmentDAO();
            List<AssignmentModal> assignments = assignmentDAO.getAssignmentsByCourse(courseId);
            List<SubmissionModal> history = submissionDAO.getSubmissionHistoryByStudentAndCourse(studentId, courseId);

            request.setAttribute("assignments", assignments);
            request.setAttribute("history", history); 
            request.setAttribute("courseId", courseId);
            request.getRequestDispatcher("/views/studentAssignments.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý tải file bài tập.
     */
    private void handleDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String assignmentIdParam = request.getParameter("assignmentId");
        if (assignmentIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu assignmentId");
            return;
        }

        int assignmentId = Integer.parseInt(assignmentIdParam);
        AssignmentDAO dao = new AssignmentDAO();
        AssignmentModal assignment = dao.getAssignmentById(assignmentId);

        if (assignment == null || assignment.getFilePath() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy bài tập hoặc file");
            return;
        }

        String uploadPath = getServletContext().getRealPath("/uploads");
        File file = new File(uploadPath, assignment.getFilePath());

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File không tồn tại");
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        try (FileInputStream in = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Lấy studentId từ JWT trong cookie. Nếu không hợp lệ sẽ redirect.
     */
    private Integer getStudentIdFromToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendRedirect("login.jsp");
            return null;
        }

        Jws<Claims> claims = JWTUtils.parseToken(token);
        String role = claims.getBody().get("role", String.class);
        Integer accountId = claims.getBody().get("id", Integer.class);

        if (accountId == null || !"student".equalsIgnoreCase(role)) {
            response.sendRedirect("login.jsp");
            return null;
        }
        return accountId;
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị danh sách khóa học, bài tập và hỗ trợ học sinh nộp bài.";
    }
}

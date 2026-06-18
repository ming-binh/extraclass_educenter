/*
 * Improved AttendanceReport servlet with better error handling and CourseModal integration
 */
package controller;

import dao.AccountDAO;
import dao.CourseDAO;
import dao.StudentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.StudentSectionDAO;
import dao.TeacherDAO;
import jakarta.servlet.http.Cookie;
import modal.CourseModal;
import modal.SectionModal;
import modal.StudentSectionModal;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;
import modal.AccountModal;
import modal.TeacherModal;
import utils.JWTUtils;

public class AttendanceReportServlet extends HttpServlet {

    private AccountDAO accountDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private CourseDAO courseDAO;
    private StudentSectionDAO dao;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
        studentDAO = new StudentDAO();
        teacherDAO = new TeacherDAO();
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = getTokenFromCookies(request);

        if (token == null || !JWTUtils.verifyToken(token)) {
            request.setAttribute("errorLog", "Token không hợp lệ");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        String identifier = JWTUtils.getIdentifier(token);
        String role = JWTUtils.getRole(token);

        if (!"teacher".equals(role)) {
            request.setAttribute("errorLog", "Chỉ giáo viên được phép truy cập trang này");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        AccountModal teacherAccount = null;
        try {
            teacherAccount = accountDAO.getAccountByUsername(identifier);
        } catch (Exception e) {
            request.setAttribute("errorLog", "Lỗi khi lấy thông tin giáo viên: " + e.getMessage());
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }
        if (teacherAccount == null) {
            request.setAttribute("errorLog", "Không tìm thấy giáo viên");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }
        TeacherModal teacher = teacherDAO.getTeacherByAccountID(teacherAccount.getId());
        if (teacher == null) {
            request.setAttribute("errorLog", "Không tìm thấy thông tin giáo viên");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            StudentSectionDAO dao = new StudentSectionDAO();

            switch (action) {
                case "list":
                    showCourseList(request, response, dao, teacher.getId());
                    break;
                case "course-report":
                    showCourseReport(request, response, dao, teacher.getId());
                    break;
                case "section-report":
                    showSectionReport(request, response, dao, teacher.getId());
                    break;
                case "student-details":
                    showStudentDetails(request, response, dao, teacher.getId());
                    break;
                case "filter-courses":
                    showFilteredCourses(request, response, dao);
                    break;
                default:
                    showCourseList(request, response, dao, teacher.getId());
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void showCourseList(HttpServletRequest request, HttpServletResponse response,
            StudentSectionDAO dao, int teacherId) throws Exception {

        // Get all courses with enhanced details
        List<CourseModal> courses = dao.getCoursesWithDetailsByTeacherId(teacherId);

        // Add course type and subject filter options for the UI
        request.setAttribute("courses", courses);
        request.setAttribute("courseTypes", CourseModal.CourseType.values());
        request.setAttribute("subjects", CourseModal.Subject.values());
        request.setAttribute("levels", CourseModal.Level.values());
        request.setAttribute("statuses", CourseModal.Status.values());

        request.getRequestDispatcher("/views/attendance-course-list.jsp").forward(request, response);
    }

    private void showFilteredCourses(HttpServletRequest request, HttpServletResponse response,
            StudentSectionDAO dao) throws Exception {

        String subject = request.getParameter("subject");
        String level = request.getParameter("level");
        String courseType = request.getParameter("courseType");
        String status = request.getParameter("status");

        List<CourseModal> filteredCourses = dao.getCoursesByFilter(subject, level, courseType, status);

        // Keep filter values for the form
        request.setAttribute("courses", filteredCourses);
        request.setAttribute("selectedSubject", subject);
        request.setAttribute("selectedLevel", level);
        request.setAttribute("selectedCourseType", courseType);
        request.setAttribute("selectedStatus", status);

        request.setAttribute("courseTypes", CourseModal.CourseType.values());
        request.setAttribute("subjects", CourseModal.Subject.values());
        request.setAttribute("levels", CourseModal.Level.values());
        request.setAttribute("statuses", CourseModal.Status.values());

        request.getRequestDispatcher("/views/attendance-course-list.jsp").forward(request, response);
    }

    private void showCourseReport(HttpServletRequest request, HttpServletResponse response,
            StudentSectionDAO dao, int teacherId) throws Exception {

        // Improved parameter validation
        String courseIdParam = request.getParameter("courseId");
        if (courseIdParam == null || courseIdParam.trim().isEmpty()) {
            request.setAttribute("error", "Course ID is required");
            showCourseList(request, response, dao, teacherId);
            return;
        }

        int courseId;
        try {
            courseId = Integer.parseInt(courseIdParam.trim());
            if (courseId <= 0) {
                throw new NumberFormatException("Course ID must be positive");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Course ID format: " + e.getMessage());
            showCourseList(request, response, dao, teacherId);
            return;
        }

        // Use the enhanced method to get all courses
        List<CourseModal> courses = dao.getCoursesWithDetails();
        CourseModal selectedCourse = courses.stream()
                .filter(c -> c.getId() != null && c.getId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (selectedCourse == null) {
            request.setAttribute("error", "Khóa học không tồn tại hoặc đã bị xóa");
            showCourseList(request, response, dao, teacherId);
            return;
        }

        // Check if course is accessible for reporting
        if (selectedCourse.getStatus() == CourseModal.Status.rejected
                || selectedCourse.getStatus() == CourseModal.Status.inactivated) {
            request.setAttribute("warning", "Khóa học này đã bị từ chối hoặc vô hiệu hóa");
        }

        List<Map<String, Object>> attendanceReport = dao.getAttendanceReportByCourse(courseId);
        Map<String, Object> statistics = dao.getAttendanceStatistics(courseId);
        List<SectionModal> sections = dao.getSectionsByCourseId(courseId);

        request.setAttribute("course", selectedCourse);
        request.setAttribute("attendanceReport", attendanceReport);
        request.setAttribute("statistics", statistics);
        request.setAttribute("sections", sections);
        request.setAttribute("courseId", courseId);

        request.getRequestDispatcher("/views/attendance-course-report.jsp").forward(request, response);
    }

    private void showSectionReport(HttpServletRequest request, HttpServletResponse response,
            StudentSectionDAO dao, int teacherId) throws Exception {

        // Improved parameter validation
        String sectionIdParam = request.getParameter("sectionId");
        String courseIdParam = request.getParameter("courseId");

        if (sectionIdParam == null || sectionIdParam.trim().isEmpty()
                || courseIdParam == null || courseIdParam.trim().isEmpty()) {
            request.setAttribute("error", "Section ID and Course ID are required");
            showCourseList(request, response, dao, teacherId);
            return;
        }

        int sectionId, courseId;
        try {
            sectionId = Integer.parseInt(sectionIdParam.trim());
            courseId = Integer.parseInt(courseIdParam.trim());
            if (sectionId <= 0 || courseId <= 0) {
                throw new NumberFormatException("IDs must be positive");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ID format: " + e.getMessage());
            showCourseList(request, response, dao, teacherId);
            return;
        }

        List<Map<String, Object>> sectionReport = dao.getAttendanceReportBySection(sectionId);
        List<SectionModal> sections = dao.getSectionsByCourseId(courseId);

        SectionModal selectedSection = sections.stream()
                .filter(s -> s.getId() != null && s.getId().equals(sectionId))
                .findFirst()
                .orElse(null);

        if (selectedSection == null) {
            request.setAttribute("error", "Section không tồn tại hoặc không thuộc khóa học này");
            // Redirect back to course report instead of course list
            request.setAttribute("courseId", courseId);
            showCourseReport(request, response, dao, teacherId);
            return;
        }

        // Get course information for context
        List<CourseModal> courses = dao.getCoursesWithDetails();
        CourseModal course = courses.stream()
                .filter(c -> c.getId() != null && c.getId().equals(courseId))
                .findFirst()
                .orElse(null);

        request.setAttribute("course", course);
        request.setAttribute("section", selectedSection);
        request.setAttribute("sectionReport", sectionReport);
        request.setAttribute("courseId", courseId);
        request.setAttribute("sectionId", sectionId);

        request.getRequestDispatcher("/views/attendance-section-report.jsp").forward(request, response);
    }

    private void showStudentDetails(HttpServletRequest request, HttpServletResponse response,
            StudentSectionDAO dao, int teacherId) throws Exception {

        String studentIdParam = request.getParameter("studentId");
        String courseIdParam = request.getParameter("courseId");

        if (studentIdParam == null || studentIdParam.trim().isEmpty()
                || courseIdParam == null || courseIdParam.trim().isEmpty()) {
            request.setAttribute("error", "Student ID and Course ID are required");
            showCourseList(request, response, dao, teacherId);
            return;
        }

        int studentId, courseId;
        try {
            studentId = Integer.parseInt(studentIdParam.trim());
            courseId = Integer.parseInt(courseIdParam.trim());
            if (studentId <= 0 || courseId <= 0) {
                throw new NumberFormatException("IDs must be positive");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid ID format: " + e.getMessage());
            showCourseList(request, response, dao, teacherId);
            return;
        }

        List<StudentSectionModal> attendanceDetails = dao.getStudentAttendanceDetails(studentId, courseId);
        List<SectionModal> sections = dao.getSectionsByCourseId(courseId);

        if (attendanceDetails.isEmpty()) {
            request.setAttribute("warning", "Không tìm thấy dữ liệu điểm danh cho học sinh này");
        }

        Map<Integer, SectionModal> sectionMap = sections.stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(SectionModal::getId, s -> s));

        // Get course information for context
        List<CourseModal> courses = dao.getCoursesWithDetails();
        CourseModal course = courses.stream()
                .filter(c -> c.getId() != null && c.getId().equals(courseId))
                .findFirst()
                .orElse(null);

        request.setAttribute("course", course);
        request.setAttribute("attendanceDetails", attendanceDetails);
        request.setAttribute("sectionMap", sectionMap);
        request.setAttribute("studentId", studentId);
        request.setAttribute("courseId", courseId);

        request.getRequestDispatcher("/views/attendance-student-detail.jsp").forward(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {

        // In lỗi ra console để dev kiểm tra
        e.printStackTrace();

        // Tạo thông tin lỗi chi tiết (nếu cần hiển thị trong JSP)
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String fullStackTrace = sw.toString();

        // Set các thuộc tính lỗi để hiển thị ra giao diện
        request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
        request.setAttribute("errorLog", fullStackTrace);
        request.setAttribute("errorType", e.getClass().getSimpleName());

        // Điều hướng sang trang lỗi
        request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Attendance Report Servlet - Manages attendance reporting functionality with enhanced CourseModal support";
    }
}

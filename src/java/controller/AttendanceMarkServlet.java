package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import modal.*;
import utils.JWTUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AttendanceMarkServlet extends HttpServlet {

    private AccountDAO accountDAO;
    private CourseDAO courseDAO;
    private SectionDAO sectionDAO;
    private StudentSectionDAO studentSectionDAO;
    private TeacherDAO teacherDAO;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
        courseDAO = new CourseDAO();
        sectionDAO = new SectionDAO();
        studentSectionDAO = new StudentSectionDAO();
        teacherDAO = new TeacherDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            request.setAttribute("errorLog", "Token không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        if (!"teacher".equals(JWTUtils.getRole(token))) {
            request.setAttribute("errorLog", "Chỉ giáo viên được phép truy cập trang này.");
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        try {
            TeacherModal teacher = getLoggedInTeacher(token);
            int teacherId = teacher.getId();

            List<CourseModal> courses = studentSectionDAO.getCoursesWithDetailsByTeacherId(teacherId);
            if (courses.isEmpty()) {
                request.setAttribute("error", "Không có khóa học nào để điểm danh.");
            }

            String courseIdParam = request.getParameter("courseId");
            String sectionIdParam = request.getParameter("sectionId");

            if (courseIdParam != null && !courseIdParam.isEmpty()) {
                int courseId = Integer.parseInt(courseIdParam);
                List<SectionModal> sections = studentSectionDAO.getSectionsByCourseId(courseId);
                request.setAttribute("sections", sections);
                request.setAttribute("courseId", courseId);

                if (sectionIdParam != null && !sectionIdParam.isEmpty()) {
                    int sectionId = Integer.parseInt(sectionIdParam);
                    SectionModal section = sectionDAO.getSectionById(sectionId);
                    CourseModal course = courseDAO.getCourseById(courseId);

                    if (section == null || course == null || (section.getTeacherId() != teacherId && course.getTeacherId() != teacherId)) {
                        request.setAttribute("errorLog", "Bạn không có quyền truy cập buổi học này.");
                        request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                        return;
                    }

                    List<Map<String, Object>> students = studentSectionDAO.getStudentsInSection(sectionId);
                    Map<Integer, String> attendanceMap = new HashMap<>();
                    for (Map<String, Object> student : students) {
                        int studentId = (int) student.get("studentId");
                        StudentSectionModal.AttendanceStatus status = studentSectionDAO.getAttendanceStatus(studentId, sectionId);
                        if (status != null) {
                            attendanceMap.put(studentId, status.name());  // lưu "present", "absent", etc.
                        }
                    }
                    List<Map<String, Object>> relatedRequests = new RequestDAO().getRequestsWithAccountBySectionAndCourse(sectionId, courseId);
                    request.setAttribute("relatedRequests", relatedRequests);
                    request.setAttribute("students", students);
                    request.setAttribute("sectionId", sectionId);
                    request.setAttribute("attendanceMap", attendanceMap);
                    request.setAttribute("note", section.getNote());
                }
            }

            request.setAttribute("courses", courses);
        } catch (Exception e) {
            request.setAttribute("errorLog", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/views/take-attendance-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc đã hết hạn.");
            return;
        }

        if (!"teacher".equals(JWTUtils.getRole(token))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ giáo viên được phép thực hiện hành động này.");
            return;
        }

        String sectionIdParam = request.getParameter("sectionId");
        String courseIdParam = request.getParameter("courseId");
        String[] studentIds = request.getParameterValues("studentIds");
        String note = request.getParameter("note");

        if (sectionIdParam == null || courseIdParam == null || studentIds == null) {
            request.setAttribute("error", "Thiếu thông tin buổi học, học sinh hoặc khóa học.");
            request.getRequestDispatcher("/views/take-attendance-form.jsp").forward(request, response);
            return;
        }

        try {
            int sectionId = Integer.parseInt(sectionIdParam);
            int courseId = Integer.parseInt(courseIdParam);
            SectionModal section = sectionDAO.getSectionById(sectionId);
            CourseModal course = courseDAO.getCourseById(courseId);

            if (section == null || course == null) {
                request.setAttribute("error", "Không tìm thấy buổi học hoặc khóa học.");
                request.getRequestDispatcher("/views/take-attendance-form.jsp").forward(request, response);
                return;
            }

            TeacherModal teacher = getLoggedInTeacher(token);
            int teacherId = teacher.getId();
            if (section.getTeacherId() != teacherId && course.getTeacherId() != teacherId) {
                request.setAttribute("error", "Bạn không có quyền điểm danh cho buổi học này.");
                request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                return;
            }

            // Check thời gian hợp lệ
            if (LocalDate.now().isAfter(section.getDateTime().toLocalDate().plusDays(2))) {
                request.setAttribute("error", "Buổi học đã quá 2 ngày nên không thể điểm danh.");
                request.getRequestDispatcher("/views/take-attendance-form.jsp").forward(request, response);
                return;
            }

            Map<String, String[]> paramMap = request.getParameterMap();
            boolean success = true;

            for (String studentIdStr : studentIds) {
                try {
                    int studentId = Integer.parseInt(studentIdStr);
                    String[] values = paramMap.get("attendance_" + studentId);
                    if (values == null || values.length == 0) {
                        continue;
                    }

                    StudentSectionModal.AttendanceStatus status = StudentSectionModal.AttendanceStatus.valueOf(values[0]);
                    if (!studentSectionDAO.upsertAttendance(studentId, sectionId, status)) {
                        success = false;
                    }
                } catch (Exception e) {
                    success = false;
                }
            }

            // Cập nhật note nếu có
            if (note != null && !note.trim().isEmpty()) {
                if (note.length() <= 500) {
                    sectionDAO.updateNote(sectionId, note.trim());
                } else {
                    request.setAttribute("error", "Ghi chú quá dài, tối đa 500 ký tự.");
                }
            }

            if (success) {
                request.setAttribute("success", "Điểm danh đã được lưu thành công.");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi lưu điểm danh cho một số học sinh.");
            }

            doGet(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi hệ thống khi lưu điểm danh: " + e.getMessage());
            request.getRequestDispatcher("/views/take-attendance-form.jsp").forward(request, response);
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

    private TeacherModal getLoggedInTeacher(String token) throws Exception {
        String username = JWTUtils.getIdentifier(token);
        AccountModal acc = accountDAO.getAccountByUsername(username);
        return teacherDAO.getTeacherByAccountID(acc.getId());
    }

    @Override
    public String getServletInfo() {
        return "Attendance Mark Servlet for managing student attendance.";
    }
}

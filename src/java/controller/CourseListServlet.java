/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.CourseDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modal.AccountModal;
import modal.CourseModal;
import modal.StudentModal;
import modal.TeacherModal;

/**
 * Servlet xử lý hiển thị danh sách khóa học, bao gồm lọc theo môn học, trình
 * độ, loại, lớp và từ khóa tìm kiếm.
 *
 * @author hungd
 */
public class CourseListServlet extends HttpServlet {

    // DAO truy cập dữ liệu từ database
    private final CourseDAO courseDao = new CourseDAO();
    private final AccountDAO accountDao = new AccountDAO();
    private final StudentDAO studentDao = new StudentDAO();
    private final TeacherDAO teacherDao = new TeacherDAO();

    /**
     * Lấy danh sách khóa học ban đầu dựa vào người dùng đang đăng nhập: - Nếu
     * là học sinh: lấy các khóa học theo lớp hiện tại của học sinh - Nếu không
     * phải học sinh: lấy danh sách các khóa học đang hoạt động
     */
    private List<CourseModal> getInitialCourseList(HttpSession session) {
        AccountModal account = (AccountModal) session.getAttribute("account");
        if (account != null && account.getRole() == AccountModal.Role.student) {
            StudentModal student = studentDao.getStudentByAccountId(account.getId());
            if (student != null) {
                List<CourseModal> gradeCourses = courseDao.getCourseByGrade(student.getCurrentGrade());
                if (gradeCourses != null && !gradeCourses.isEmpty()) {
                    return gradeCourses;
                }
            }
        }
        // Nếu không phải học sinh hoặc không có lớp phù hợp → trả về danh sách khóa học đang hoạt động
        return courseDao.getCoursesByStatuses(List.of("activated", "upcoming"));

    }

    /**
     * Xử lý yêu cầu GET: hiển thị danh sách khóa học mặc định
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CourseModal> courseListToShow = getInitialCourseList(session);

        try {
            prepareCommonAttributes(request, session, courseListToShow, null, null, null, null, false);
        } catch (Exception ex) {
            Logger.getLogger(CourseListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Điều hướng sang giao diện JSP
        request.getRequestDispatcher("/views/course-list.jsp").forward(request, response);
    }

    /**
     * Xử lý yêu cầu POST: lọc danh sách khóa học theo tiêu chí từ form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Lấy dữ liệu từ form gửi lên
        String subject = request.getParameter("selectedSubject");
        String gradeStr = request.getParameter("selectedGrade");
        String level = request.getParameter("selectedLevel");
        String courseType = request.getParameter("selectedType");
        String keyword = request.getParameter("keyword");

        // Chuẩn hóa dữ liệu: nếu là "Tất cả" hoặc null thì bỏ qua
        subject = (subject == null || subject.equalsIgnoreCase("Tất cả")) ? null : subject;
        level = (level == null || level.equalsIgnoreCase("Tất cả")) ? null : level;
        courseType = (courseType == null || courseType.equalsIgnoreCase("Tất cả")) ? null : courseType;
        keyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim().toLowerCase() : null;

        // Chuyển đổi gradeStr sang số nguyên nếu có
        Integer grade = null;
        if (gradeStr != null && !gradeStr.equalsIgnoreCase("Tất cả")) {
            try {
                grade = Integer.parseInt(gradeStr.replaceAll("\\D+", ""));
            } catch (NumberFormatException e) {
                grade = null;
            }
        }

        // Bước 1: Lọc khóa học theo tiêu chí subject, grade, level, type
        List<CourseModal> filteredCourses = courseDao.getFilteredCourses(subject, grade, level, courseType);
        if (filteredCourses == null) {
            filteredCourses = new ArrayList<>();
        }

        // Bước 2: Nếu có từ khóa thì tiếp tục lọc theo tên khóa học hoặc tên giáo viên
        if (keyword != null) {
            Map<Integer, AccountModal> teacherMap = buildTeacherAccountMapFromCourses(filteredCourses);
            List<CourseModal> matchedCourses = new ArrayList<>();

            for (CourseModal course : filteredCourses) {
                boolean matchCourseName = course.getName() != null && course.getName().toLowerCase().contains(keyword);
                boolean matchTeacherName = false;

                AccountModal teacher = teacherMap.get(course.getTeacherId());
                if (teacher != null && teacher.getName() != null
                        && teacher.getName().toLowerCase().contains(keyword)) {
                    matchTeacherName = true;
                }

                // Nếu khớp tên khóa học hoặc giáo viên thì giữ lại
                if (matchCourseName || matchTeacherName) {
                    matchedCourses.add(course);
                }
            }

            filteredCourses = matchedCourses;
        }

        // Kiểm tra xem có đang lọc không (để hiển thị tiêu đề phù hợp)
        boolean isFiltered = subject != null || level != null || courseType != null || grade != null || keyword != null;

        try {
            prepareCommonAttributes(request, session, filteredCourses, subject, level, courseType, gradeStr, isFiltered);
        } catch (Exception ex) {
            Logger.getLogger(CourseListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Gán keyword để hiển thị lại trong ô tìm kiếm
        request.setAttribute("keyword", keyword != null ? keyword : "");

        // Điều hướng sang JSP
        request.getRequestDispatcher("/views/course-list.jsp").forward(request, response);
    }

    /**
     * Gán các thuộc tính chung để hiển thị danh sách khóa học
     */
    private void prepareCommonAttributes(HttpServletRequest request, HttpSession session,
            List<CourseModal> courseList,
            String subject, String level, String courseType, String gradeStr,
            boolean isFiltered) {

        // Gán danh sách khóa học và thông tin giáo viên tương ứng
        session.setAttribute("courseList", courseList);
        session.setAttribute("teacherMap", buildTeacherAccountMapFromCourses(courseList));

        // Gán danh sách các lựa chọn bộ lọc
        session.setAttribute("subjectList", CourseModal.Subject.values());
        session.setAttribute("levelList", CourseModal.Level.values());
        session.setAttribute("typeCourseList", CourseModal.CourseType.values());

        // Gán lại các lựa chọn đã chọn để hiển thị trên giao diện
        request.setAttribute("selectedSubject", subject != null ? subject : "Tất cả");
        request.setAttribute("selectedLevel", level != null ? level : "Tất cả");
        request.setAttribute("selectedType", courseType != null ? courseType : "Tất cả");
        request.setAttribute("selectedGrade", gradeStr != null ? gradeStr : "Tất cả");

        // Tiêu đề hiển thị
        request.setAttribute("coursePageTitle", isFiltered ? "Kết quả lọc khóa học" : "Khoá học đề xuất");
    }

    /**
     * Tạo ánh xạ từ teacherId ➝ AccountModal để lấy thông tin tên giáo viên
     */
    private Map<Integer, AccountModal> buildTeacherAccountMapFromCourses(List<CourseModal> courseList) {
        List<TeacherModal> teachers = teacherDao.getAllTeacher();
        List<AccountModal> accounts = accountDao.getAllAccountByRole("teacher");

        // Map teacherId -> TeacherModal
        Map<Integer, TeacherModal> teacherMap = new HashMap<>();
        for (TeacherModal teacher : teachers) {
            teacherMap.put(teacher.getId(), teacher);
        }

        // Map accountId -> AccountModal
        Map<Integer, AccountModal> accountMap = new HashMap<>();
        for (AccountModal acc : accounts) {
            accountMap.put(acc.getId(), acc);
        }

        // Kết hợp: teacherId -> AccountModal (thông qua teacher.accountId)
        Map<Integer, AccountModal> teacherAccountMap = new HashMap<>();
        for (CourseModal course : courseList) {
            TeacherModal teacher = teacherMap.get(course.getTeacherId());
            if (teacher != null) {
                AccountModal acc = accountMap.get(teacher.getAccountId());
                if (acc != null) {
                    teacherAccountMap.put(course.getTeacherId(), acc);
                }
            }
        }

        return teacherAccountMap;
    }
}

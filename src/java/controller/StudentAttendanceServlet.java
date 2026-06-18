package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import modal.*;
import utils.JWTUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudentAttendanceServlet extends HttpServlet {

    private CourseDAO courseDAO;
    private StudentSectionDAO studentSectionDAO;
    private SectionDAO sectionDAO;
    private StudentDAO studentDAO;
    private ParentDAO parentDAO;

    @Override
    public void init() {
        courseDAO = new CourseDAO();
        studentSectionDAO = new StudentSectionDAO();
        sectionDAO = new SectionDAO();
        studentDAO = new StudentDAO();
        parentDAO = new ParentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = getTokenFromCookies(request);
        if (token == null || !JWTUtils.verifyToken(token)) {
            response.sendRedirect("dang-nhap");
            return;
        }

        String role = JWTUtils.getRole(token);
        String identifier = JWTUtils.getIdentifier(token);

        try {
            AccountModal account = new AccountDAO().getAccountByUsername(identifier);

            if (account == null) {
                request.setAttribute("errorLog", "Không tìm thấy tài khoản.");
                request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                return;
            }

            if ("student".equals(role)) {
                StudentModal student = studentDAO.getStudentByAccountId(account.getId());
                if (student == null) {
                    request.setAttribute("errorLog", "Không tìm thấy hồ sơ học sinh.");
                    request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                    return;
                }
                processAttendanceForStudent(request, response, student);
                request.getRequestDispatcher("/views/student-attendance.jsp").forward(request, response);
                return;
            } else if ("parent".equals(role)) {
                ParentModal parent = parentDAO.getParentByAccountID(account.getId());
                if (parent == null) {
                    request.setAttribute("errorLog", "Không tìm thấy hồ sơ phụ huynh.");
                    request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
                    return;
                }

                List<StudentModal> children = studentDAO.getChildrenOfParent(parent.getId());
                Map<Integer, String> studentNameMap = new HashMap<>();

                for (StudentModal child : children) {
                    try {
                        String name = new StudentDAO().getStudentNameById(child.getId());
                        studentNameMap.put(child.getId(), name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                request.setAttribute("children", children);
                request.setAttribute("studentNameMap", studentNameMap);

                String studentIdParam = request.getParameter("studentId");
                if (studentIdParam != null && !studentIdParam.isEmpty()) {
                    try {
                        int studentId = Integer.parseInt(studentIdParam);
                        StudentModal selectedStudent = studentDAO.getStudentById(studentId);
                        if (selectedStudent != null) {

                            request.setAttribute("selectedStudentId", studentId);
                            processAttendanceForStudent(request, response, selectedStudent);
                            request.getRequestDispatcher("/views/student-attendance.jsp").forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "ID học sinh không hợp lệ.");
                    }
                }
                request.getRequestDispatcher("/views/student-attendance.jsp").forward(request, response);
            } else {
                request.setAttribute("errorLog", "Vai trò không được hỗ trợ.");
                request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi truy xuất tài khoản: " + e.getMessage());
            request.getRequestDispatcher("/views/layout/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void processAttendanceForStudent(HttpServletRequest request, HttpServletResponse response, StudentModal student)
            throws ServletException, IOException {
        int studentId = student.getId();
        try {
            String studentName = studentDAO.getStudentNameById(studentId);

            List<CourseModal> studentCourses = courseDAO.getCoursesByStudentId(studentId);
            request.setAttribute("studentName", studentName);
            request.setAttribute("studentCourses", studentCourses);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("studentName", "Không xác định");
        }

        String courseIdParam = request.getParameter("courseId");
        if (courseIdParam != null && !courseIdParam.trim().isEmpty()) {
            try {
                int courseId = Integer.parseInt(courseIdParam);
                List<StudentSectionModal> attendanceDetails = studentSectionDAO.getStudentAttendanceDetails(studentId, courseId);
                List<SectionModal> sections = studentSectionDAO.getSectionsByCourseId(courseId);

                Map<Integer, SectionModal> sectionMap = sections.stream()
                        .filter(s -> s.getId() != null)
                        .collect(Collectors.toMap(SectionModal::getId, s -> s));

                request.setAttribute("attendanceDetails", attendanceDetails);
                request.setAttribute("sectionMap", sectionMap);
                request.setAttribute("selectedCourseId", courseId);

            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID khóa học không hợp lệ.");
            } catch (Exception e) {
                request.setAttribute("error", "Lỗi khi truy xuất dữ liệu: " + e.getMessage());
            }
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
}

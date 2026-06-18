/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.CourseDAO;
import dao.ParentDAO;
import dao.SectionDAO;
import dao.StudentCourseDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modal.AccountModal;
import modal.CourseModal;
import modal.SectionModal.DayOfWeekEnum;
import modal.StudentModal;
import modal.TeacherAchivementModal;
import modal.TeacherModal;
import utils.CurrencyFormatter;

/**
 * Servlet xử lý hiển thị chi tiết khóa học, bao gồm thông tin khóa học, giáo
 * viên, học phí, chứng chỉ, và lịch học theo tuần.
 *
 * Xử lý hai phương thức: - doGet(): hiển thị dữ liệu ban đầu - doPost(): cập
 * nhật lịch học theo tuần được chọn
 *
 * @author hungd
 */
public class CourseDetailsServlet extends HttpServlet {

    private final CourseDAO courseDao = new CourseDAO();
    private final TeacherDAO teacherDao = new TeacherDAO();
    private final StudentDAO studentDao = new StudentDAO();
    private final SectionDAO sectionDao = new SectionDAO();
    private final ParentDAO parentDao = new ParentDAO();
    private final AccountDAO accountDao = new AccountDAO();
    private final static StudentCourseDAO studentCourseDao = new StudentCourseDAO();

    /**
     * Xử lý yêu cầu GET để hiển thị chi tiết khóa học. Lấy courseId từ URL,
     * truy vấn cơ sở dữ liệu, format dữ liệu và forward tới trang JSP.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // ===== Lấy loggedInUserId và Role từ Filter =====
        Integer loggedInUserId = (Integer) request.getAttribute("loggedInUserId");
        String loggedInUserRole = (String) request.getAttribute("loggedInUserRole");

        // ===== Lấy courseId =====
        String courseIdStr = request.getParameter("courseId");
        if (courseIdStr == null || courseIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Mã khóa học không hợp lệ.");
            response.sendRedirect("danh-sach-lop");
            return;
        }

        int courseId;
        try {
            courseId = Integer.parseInt(courseIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã khóa học không đúng định dạng.");
            response.sendRedirect("danh-sach-lop");
            return;
        }

        // ===== Lấy Course =====
        CourseModal course = courseDao.getCourseById(courseId);
        if (course == null) {
            session.setAttribute("errorMessage", "Không tìm thấy khóa học yêu cầu.");
            response.sendRedirect("danh-sach-lop");
            return;
        }

        // ===== Kiểm tra đã tham gia (chỉ Student) =====
        String studentCourseStatus = null;

        if ("student".equalsIgnoreCase(loggedInUserRole) && loggedInUserId != null) {
            try {
                StudentModal student = studentDao.getStudentByAccountId(loggedInUserId);
                if (student != null) {
                    int studentId = student.getId();
                    studentCourseStatus = studentCourseDao.getStudentCourseStatus(studentId, courseId);
                    request.setAttribute("studentId", studentId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ===== Nếu là Phụ huynh → Lấy danh sách con =====
        if ("parent".equalsIgnoreCase(loggedInUserRole) && loggedInUserId != null) {
            try {
                List<StudentModal> childrenList = studentDao.getChildrenOfParent(parentDao.getParentByAccountID(loggedInUserId).getId());
                session.setAttribute("childrenList", childrenList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ===== Xử lý giá =====
        BigDecimal discount = course.getDiscountPercentage() != null ? course.getDiscountPercentage() : BigDecimal.ZERO;
        BigDecimal originalPrice;
        BigDecimal finalPrice;
        String priceLabel;

        switch (course.getCourseType()) {
            case combo:
                originalPrice = course.getFeeCombo();
                priceLabel = "Học trọn gói chỉ với";
                break;
            case daily:
                originalPrice = course.getFeeDaily();
                priceLabel = "Mức giá theo từng buổi";
                break;
            default:
                originalPrice = BigDecimal.ZERO;
                priceLabel = "";
        }

        BigDecimal discountAmount = originalPrice.multiply(discount).divide(BigDecimal.valueOf(100));
        finalPrice = originalPrice.subtract(discountAmount);

        String originalPriceStr = CurrencyFormatter.formatCurrency(originalPrice);
        String finalPriceStr = CurrencyFormatter.formatCurrency(finalPrice);

        // ===== Format ngày bắt đầu/kết thúc =====
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startFormatted = course.getStartDate().format(formatter);
        String endFormatted = course.getEndDate().format(formatter);

        // ===== Lấy giáo viên, chứng chỉ, lịch học, tuần, khoá học liên quan =====
        TeacherModal teacher = teacherDao.getTeacherById(course.getTeacherId());
        List<TeacherAchivementModal> achiveOfTeacher = teacherDao.getAchiveOfTeacher(teacher.getId());
        List<DayOfWeekEnum> daysOfWeek = sectionDao.getDaysOfWeekForCourse(courseId);
        List<CourseModal> relatedCourses = courseDao.getRelatedCourses(courseId, course.getSubject().name(), course.getGrade());

        // ===== Lưu session =====
        session.setAttribute("course", course);
        session.setAttribute("teacher", teacher);
        session.setAttribute("teacherMap", buildTeacherAccountMapFromCourses(courseDao.getAllCourse()));
        try {
            session.setAttribute("childrenMap", buildChildrenMap(studentDao.getAllStudents()));
            session.setAttribute("studentJoinStatusMap", getStudentJoinStatusMap(studentDao.getAllStudents(), courseId));
        } catch (Exception ex) {
            Logger.getLogger(CourseDetailsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        session.setAttribute("relatedCourses", relatedCourses);
        session.setAttribute("originalPriceStr", originalPriceStr);
        session.setAttribute("finalPriceStr", finalPriceStr);
        session.setAttribute("hasDiscount", discount.compareTo(BigDecimal.ZERO) > 0);
        session.setAttribute("startFormatted", startFormatted);
        session.setAttribute("endFormatted", endFormatted);
        session.setAttribute("priceLabel", priceLabel);
        session.setAttribute("achiveOfTeacher", achiveOfTeacher);
        session.setAttribute("achiveYearMap", getAchiveYearMap(achiveOfTeacher));
        session.setAttribute("daysOfWeek", daysOfWeek);

        System.out.println(studentCourseStatus);
        // ===== Thuộc request =====
        request.setAttribute("studentCourseStatus", studentCourseStatus);

        // Luôn set role → JSP dùng cho c:choose
        request.setAttribute("loggedInUserRole", loggedInUserRole);

        // ===== Forward =====
        request.getRequestDispatcher("views/course-details.jsp").forward(request, response);
    }

    /**
     * Xử lý POST khi người dùng chọn một tuần khác để xem lịch học. Chỉ cập
     * nhật phần lịch học và tuần, các phần khác giữ nguyên từ session.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Tạo ánh xạ từ teacherId ➝ AccountModal để có tên đăng nhập/tên hiển thị.
     * Logic này lấy từ tất cả giáo viên và tất cả account, kết nối qua
     * accountId.
     */
    private Map<Integer, AccountModal> buildTeacherAccountMapFromCourses(List<CourseModal> courseList) {
        List<TeacherModal> teachers = teacherDao.getAllTeacher();
        List<AccountModal> accounts = accountDao.getAllAccountByRole("teacher");

        Map<Integer, TeacherModal> teacherMap = new HashMap<>();
        for (TeacherModal teacher : teachers) {
            teacherMap.put(teacher.getId(), teacher);
        }

        Map<Integer, AccountModal> accountMap = new HashMap<>();
        for (AccountModal acc : accounts) {
            accountMap.put(acc.getId(), acc);
        }

        // Kết hợp từ teacherId → teacher → accountId → Account
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

    private Map<Integer, String> buildChildrenMap(List<StudentModal> childrenList) {
        // Lấy danh sách account của các con (role = "student")
        List<AccountModal> accounts = accountDao.getAllAccountByRole("student");

        // Tạo map accountId -> AccountModal
        Map<Integer, AccountModal> accountMap = new HashMap<>();
        for (AccountModal acc : accounts) {
            accountMap.put(acc.getId(), acc);
        }

        Map<Integer, String> childrenMap = new HashMap<>();

        for (StudentModal child : childrenList) {
            AccountModal acc = accountMap.get(child.getAccountId()); // giả sử ChildModal có accountId
            if (acc != null) {
                childrenMap.put(child.getId(), acc.getName()); // hoặc acc.getUsername(), tùy bạn muốn lấy gì làm tên
            }
        }

        return childrenMap;
    }

    public static Map<Integer, String> getStudentJoinStatusMap(List<StudentModal> childrenList, int courseId) {
        Map<Integer, String> statusMap = new HashMap<>();
        for (StudentModal child : childrenList) {
            String status = "none";
            try {
                status = studentCourseDao.getStudentCourseStatus(child.getId(), courseId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            statusMap.put(child.getId(), status);
        }
        return statusMap;
    }

    /**
     * Tạo map từ id thành tích ➝ năm đat thành tích. Dùng trong JSP để hiển thị
     * năm nhanh chóng.
     */
    public static Map<Integer, String> getAchiveYearMap(List<TeacherAchivementModal> achives) {
        Map<Integer, String> certYearMap = new HashMap<>();
        for (TeacherAchivementModal achive : achives) {
            String year = (achive.getIssuedDate() != null)
                    ? String.valueOf(achive.getIssuedDate().getYear())
                    : "N/A";
            certYearMap.put(achive.getId(), year);
        }
        return certYearMap;
    }

}

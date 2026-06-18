/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.SchoolDAO;
import dao.TeacherDAO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modal.AccountModal;
import modal.CourseModal;
import modal.SchoolModal;
import modal.TeacherModal;

/**
 * Servlet xử lý hiển thị danh sách giáo viên và tìm kiếm/lọc theo bộ lọc (môn
 * học, khối, từ khóa). - Dữ liệu kết hợp từ TeacherModal + AccountModal (được
 * kết hợp vào mảng Object[]). - Giao diện: /views/teacher-list.jsp
 *
 * GET: Hiển thị danh sách tất cả giáo viên. POST: Lọc danh sách giáo viên theo
 * bộ lọc được chọn từ client.
 *
 * @author hungd
 */
public class TeacherListServlet extends HttpServlet {

    private final TeacherDAO teacherDao = new TeacherDAO();
    private final AccountDAO accountDao = new AccountDAO();
    private final SchoolDAO schoolDao = new SchoolDAO();

    /**
     * Xử lý HTTP GET: hiển thị danh sách toàn bộ giáo viên.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Lấy toàn bộ giáo viên
        List<TeacherModal> allTeachers = teacherDao.getAllTeacher();

        // Lấy toàn bộ tài khoản có role là "teacher"
        List<AccountModal> teacherAccounts = accountDao.getAllAccountByRole("teacher");

        // Tạo map để truy cập tài khoản theo accountId nhanh chóng
        Map<Integer, AccountModal> accountMap = buildAccountMap(teacherAccounts);

        // Kết hợp dữ liệu teacher + account thành List<Object[]>
        List<Object[]> teacherList = buildTeacherList(allTeachers, accountMap);

        // Lấy danh sách trường và build thành Map để truy cập theo id
        List<SchoolModal> schoolList = schoolDao.getAllSchools();
        Map<Integer, SchoolModal> schoolMap = new HashMap<>();
        for (SchoolModal school : schoolList) {
            schoolMap.put(school.getId(), school);
        }
        request.setAttribute("schoolMap", schoolMap);

        try {
            // Gán các attribute chung để hiển thị lên view
            prepareCommonAttributes(request, session, teacherList, null, null, null, false);
        } catch (Exception e) {
            Logger.getLogger(TeacherListServlet.class.getName()).log(Level.SEVERE, null, e);
        }

        // Forward đến trang JSP danh sách giáo viên
        request.getRequestDispatcher("/views/teacher-list.jsp").forward(request, response);
    }

    /**
     * Xử lý HTTP POST: Lọc giáo viên theo môn học, khối, từ khóa.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Lấy tham số từ form lọc
        String subjectParam = request.getParameter("selectedSubject");
        String gradeParam = request.getParameter("selectedGrade");
        String keywordParam = request.getParameter("keyword");

        // Xử lý dữ liệu đầu vào
        String subject = parseSubject(subjectParam);
        String grade = gradeParam != null ? gradeParam : "";
        String keyword = parseKeyword(keywordParam);

        List<TeacherModal> filteredTeachers;
        try {
            // Lọc giáo viên dựa theo các tiêu chí đã chọn
            filteredTeachers = teacherDao.getFilteredTeacher(subject, grade, keyword);
        } catch (Exception e) {
            Logger.getLogger(TeacherListServlet.class.getName()).log(Level.SEVERE, "Lỗi lọc giáo viên", e);
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi lọc giáo viên.");
            filteredTeachers = new ArrayList<>();
        }

        // Lấy lại danh sách tài khoản + kết hợp
        List<AccountModal> teacherAccounts = accountDao.getAllAccountByRole("teacher");
        Map<Integer, AccountModal> accountMap = buildAccountMap(teacherAccounts);
        List<Object[]> teacherList = buildTeacherList(filteredTeachers, accountMap);

        // Gán dữ liệu ra request và session
        boolean isFiltered = !subject.isEmpty() || !grade.isEmpty() || !keyword.isEmpty();
        prepareCommonAttributes(request, session, teacherList, subjectParam, gradeParam, keywordParam, isFiltered);

        // Forward đến view
        request.getRequestDispatcher("/views/teacher-list.jsp").forward(request, response);
    }

    /**
     * Gán các thuộc tính dùng chung cho cả GET và POST.
     *
     * @param request request hiện tại
     * @param session session hiện tại
     * @param teacherList danh sách giáo viên đã ghép account
     * @param subjectParam môn học lọc
     * @param gradeParam khối học lọc
     * @param keywordParam từ khóa tìm kiếm
     * @param isFiltered true nếu có sử dụng bộ lọc
     */
    private void prepareCommonAttributes(HttpServletRequest request, HttpSession session,
            List<Object[]> teacherList,
            String subjectParam, String gradeParam, String keywordParam,
            boolean isFiltered) {

        session.setAttribute("teacherList", teacherList);

        // Lấy top giáo viên (5 người) để hiển thị nổi bật
        session.setAttribute("topTeachers", getTopTeachersCombined());

        // Gán danh sách môn học vào session để dùng trong bộ lọc
        session.setAttribute("subjectList", CourseModal.Subject.values());

        // Gán danh sách trường học vào session
        session.setAttribute("schoolMap", buildSchoolMap(schoolDao.getAllSchools()));

        // Giữ lại giá trị lọc đã chọn để giữ UI người dùng
        request.setAttribute("selectedSubject", subjectParam != null ? subjectParam : "Tất cả");
        request.setAttribute("selectedGrade", gradeParam != null ? gradeParam : "Tất cả");
        request.setAttribute("selectedKeyword", keywordParam != null ? keywordParam : "");

        // Đổi tiêu đề nếu có lọc
        request.setAttribute("teacherPageTitle", isFiltered ? "Kết quả lọc giáo viên" : "Danh sách giáo viên");
    }

    /**
     * Lấy danh sách top giáo viên và kết hợp với tài khoản.
     */
    private List<Object[]> getTopTeachersCombined() {
        List<TeacherModal> topTeachers = teacherDao.getTopTeachers(5);
        List<AccountModal> teacherAccounts = accountDao.getAllAccountByRole("teacher");
        Map<Integer, AccountModal> accountMap = buildAccountMap(teacherAccounts);
        return buildTeacherList(topTeachers, accountMap);
    }

    /**
     * Tạo map account theo ID để truy cập nhanh.
     */
    private Map<Integer, AccountModal> buildAccountMap(List<AccountModal> accounts) {
        Map<Integer, AccountModal> map = new HashMap<>();
        for (AccountModal acc : accounts) {
            map.put(acc.getId(), acc);
        }
        return map;
    }

    /**
     * Kết hợp danh sách giáo viên với tài khoản tương ứng (theo accountId). Mỗi
     * phần tử trong list là Object[]{AccountModal, TeacherModal}
     */
    private List<Object[]> buildTeacherList(List<TeacherModal> teacherInfos, Map<Integer, AccountModal> accountMap) {
        List<Object[]> list = new ArrayList<>();
        for (TeacherModal teacher : teacherInfos) {
            AccountModal acc = accountMap.get(teacher.getAccountId());
            if (acc != null) {
                list.add(new Object[]{acc, teacher});
            }
        }
        return list;
    }

    /**
     * Tạo map trường học theo ID.
     */
    private Map<Integer, SchoolModal> buildSchoolMap(List<SchoolModal> schools) {
        Map<Integer, SchoolModal> map = new HashMap<>();
        for (SchoolModal school : schools) {
            map.put(school.getId(), school);
        }
        return map;
    }

    /**
     * Chuẩn hóa dữ liệu môn học (loại bỏ "Tất cả").
     */
    private String parseSubject(String raw) {
        if (raw == null || raw.equalsIgnoreCase("Tất cả")) {
            return "";
        }
        return raw.trim();
    }

    /**
     * Chuẩn hóa dữ liệu từ khóa tìm kiếm.
     */
    private String parseKeyword(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.trim().toLowerCase();
    }
}

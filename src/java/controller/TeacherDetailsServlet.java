/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.CourseDAO;
import dao.TeacherDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modal.AccountModal;
import modal.CourseModal;
import modal.TeacherCertificateModal;
import modal.TeacherModal;
import static controller.CourseDetailsServlet.getAchiveYearMap;
import modal.TeacherAchivementModal;

/**
 * Servlet xử lý hiển thị thông tin chi tiết của một giáo viên. Nhận `teacherId`
 * từ request, lấy thông tin giáo viên, tài khoản và các khóa học giảng dạy. Nếu
 * có lỗi (ID không tồn tại, không đúng định dạng...), chuyển hướng với thông
 * báo lỗi.
 *
 * @author hungd
 */
public class TeacherDetailsServlet extends HttpServlet {

    // Khởi tạo DAO dùng để truy xuất dữ liệu
    private final TeacherDAO teacherDao = new TeacherDAO();
    private final AccountDAO accountDao = new AccountDAO();
    private final CourseDAO courseDao = new CourseDAO();

    /**
     * Xử lý HTTP GET - hiển thị chi tiết giáo viên.
     *
     * @param request yêu cầu HTTP từ client
     * @param response phản hồi HTTP gửi về client
     * @throws ServletException nếu có lỗi khi forward
     * @throws IOException nếu có lỗi khi đọc/ghi dữ liệu
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy phiên làm việc hiện tại
        HttpSession session = request.getSession();

        // Lấy parameter "teacherId" từ URL
        String idParam = request.getParameter("teacherId");

        // Bước 1: Kiểm tra xem parameter có rỗng hoặc null không
        if (idParam == null || idParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Mã giáo viên không hợp lệ.");
            response.sendRedirect("danh-sach-giao-vien");
            return;
        }

        try {
            // Bước 2: Chuyển đổi teacherId sang kiểu số nguyên
            int teacherId = Integer.parseInt(idParam);

            // Bước 3: Truy vấn giáo viên theo ID
            TeacherModal teacher = teacherDao.getTeacherById(teacherId);
            List<TeacherCertificateModal> certOfTeacher = teacherDao.getCertOfTeacher(teacherId);
            List<TeacherAchivementModal> achiveOfTeacher = teacherDao.getAchiveOfTeacher(teacherId);

            // Nếu không tìm thấy giáo viên
            if (teacher == null) {
                session.setAttribute("errorMessage", "Không tìm thấy giáo viên với ID = " + teacherId);
                response.sendRedirect("danh-sach-giao-vien");
                return;
            }

            // Bước 4: Kiểm tra xem giáo viên đã liên kết với tài khoản chưa
            if (teacher.getAccountId() == null) {
                session.setAttribute("errorMessage", "Giáo viên chưa có tài khoản liên kết.");
                response.sendRedirect("danh-sach-giao-vien");
                return;
            }

            // Bước 5: Truy vấn tài khoản tương ứng với accountId của giáo viên
            AccountModal account = accountDao.getAccountById(teacher.getAccountId());

            // Nếu không tìm thấy tài khoản
            if (account == null) {
                session.setAttribute("errorMessage", "Không tìm thấy tài khoản của giáo viên.");
                response.sendRedirect("danh-sach-giao-vien");
                return;
            }

            // Bước 6: Lấy danh sách khóa học mà giáo viên đang giảng dạy
            List<CourseModal> courseOfTeacher = courseDao.getCourseByTeacher(teacher.getId());

            // Bước 7: Gán dữ liệu vào request để hiển thị trên JSP
            request.setAttribute("teacher", teacher); // Thông tin giáo viên
            request.setAttribute("infoOfTeacher", account); // Tài khoản giáo viên
            request.setAttribute("courseOfTeacher", courseOfTeacher); // Danh sách khóa học
            request.setAttribute("followers", teacherDao.countStudentsFollowByTeacherId(teacherId));// Số học sinh theo dõi
            request.setAttribute("certOfTeacher", certOfTeacher);// Lấy chứng chỉ của giáo viên
            request.setAttribute("achiveOfTeacher", achiveOfTeacher);// Lấy thành tích của giáo viên
            request.setAttribute("certYearMap", getCertYearMap(certOfTeacher)); // Map chứng chỉ với năm
            request.setAttribute("achiveYearMap", getAchiveYearMap(achiveOfTeacher)); // Map bằng cấp với năm

            // Ghi log số lượng khóa học (debug)
            System.out.println(courseOfTeacher.size());

            // Bước 8: Chuyển tiếp sang trang chi tiết giáo viên
            request.getRequestDispatcher("/views/teacher-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Nếu teacherId không phải là số
            session.setAttribute("errorMessage", "ID giáo viên không đúng định dạng.");
            response.sendRedirect("danh-sach-giao-vien");

        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("danh-sach-giao-vien");
        }
    }

    /**
     * Xử lý HTTP POST - hiện không sử dụng.
     *
     * @param request yêu cầu HTTP từ client
     * @param response phản hồi HTTP gửi về client
     * @throws ServletException nếu có lỗi khi xử lý
     * @throws IOException nếu có lỗi IO
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public static Map<Integer, String> getCertYearMap(List<TeacherCertificateModal> certs) {
        Map<Integer, String> certYearMap = new HashMap<>();
        for (TeacherCertificateModal cert : certs) {
            String year = (cert.getIssuedDate() != null)
                    ? String.valueOf(cert.getIssuedDate().getYear())
                    : "N/A";
            certYearMap.put(cert.getId(), year);
        }
        return certYearMap;
    }

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

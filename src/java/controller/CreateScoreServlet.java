/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.SectionDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import dto.StudentBasicInfo;
import dto.TeacherProfile;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import modal.AccountModal;
import modal.SectionModal;
import modal.StudentMarkFeedbackModal;
import modal.StudentMarkFeedbackModal;
import modal.TeacherModal;
import modal.TeacherModal;

/**
 *
 * @author vankh
 */
public class CreateScoreServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CreateScore</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateScore at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SectionDAO secDao = new SectionDAO();
        String grade = request.getParameter("grade");
        String courseId = request.getParameter("courseId");
        String nameCourse = request.getParameter("name");
        String level = request.getParameter("level");
        String subject = request.getParameter("subject");
        List<SectionModal> listSec = secDao.getSectionsByCourseId(Integer.parseInt(courseId));
        String resultMessage = request.getParameter("result");
        if (resultMessage != null && !resultMessage.isBlank()) {
            request.setAttribute("message", resultMessage);
        }

// Tạo danh sách mới chứa cả đối tượng SectionModal và chuỗi ngày đã định dạng
        List<Map<String, Object>> formattedSessions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));

        for (SectionModal session : listSec) {
            Map<String, Object> sessionMap = new HashMap<>();
            sessionMap.put("section", session);
            sessionMap.put("formattedDate", session.getDateTime().format(formatter));
            formattedSessions.add(sessionMap);
        }

        request.setAttribute("formattedSessions", formattedSessions);
        try {
            String result = request.getParameter("result");
            if (result != null && !result.isBlank()) {
                request.setAttribute("result", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi xử lý kết quả hiển thị.");
        }

        String studentEnrollment = request.getParameter("studentEnrollment");
        int cId = Integer.parseInt(courseId);
        StudentDAO stDao = new StudentDAO();
        try {
            List<StudentBasicInfo> listStudent = stDao.getStudentsByCourseId(cId);
            request.setAttribute("listStudent", listStudent);
        } catch (Exception ex) {
            Logger.getLogger(CreateScoreServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("grade", grade);
        request.setAttribute("courseId", courseId);
        request.setAttribute("nameCourse", nameCourse);
        request.setAttribute("level", level);
        request.setAttribute("subject", subject);
        request.setAttribute("currentEnrollment", studentEnrollment);
        request.getRequestDispatcher("/views/createScore.jsp").forward(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String usname = (String) request.getAttribute("loggedInUserName");
            AccountDAO acdao = new AccountDAO();
            AccountModal ac = null;
            try {
                ac = acdao.getAccountByUsername(usname);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(ViewScoreServlet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            String userName = ac.getPhone();
            String grade = request.getParameter("grade");
            String nameCourse = request.getParameter("nameCourse");
            String level = request.getParameter("level");
            String subject = request.getParameter("subject");
            String studentEnrollment = request.getParameter("currentEnrollment");
            String courseIdParam = request.getParameter("courseId");
            String sessionDate = request.getParameter("sessionDate");
            LocalDateTime sessionLocalDate = null;
            if (sessionDate == null || sessionDate.isEmpty()) {
                sessionLocalDate = LocalDateTime.now();
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                sessionLocalDate = LocalDateTime.parse(sessionDate, formatter);
            }

            int courseId = 0;
            if (courseIdParam != null && !courseIdParam.isBlank()) {
                courseId = Integer.parseInt(courseIdParam);
            } else {
                throw new IllegalArgumentException("Thiếu hoặc sai courseId");
            }

            String[] studentIds = request.getParameterValues("studentIds");
            String typeScore = request.getParameter("typeScore");

            TeacherDAO teacherDao = new TeacherDAO();
            TeacherModal teacher = teacherDao.getTeacherByPhone(userName);
            int teacherId = teacher.getId();

            LocalDateTime now = LocalDateTime.now();
            List<StudentMarkFeedbackModal> feedbackList = new ArrayList<>();

            if (studentIds != null) {
                for (String studentIdStr : studentIds) {
                    int studentId = Integer.parseInt(studentIdStr);
                    String scoreParam = "score_" + studentIdStr;
                    String scoreStr = request.getParameter(scoreParam);

                    // feedback nhập riêng theo từng học sinh
                    String feedbackParam = "feedback_" + studentIdStr;
                    String feedback = request.getParameter(feedbackParam);

                    if (scoreStr != null && !scoreStr.trim().isEmpty()) {
                        BigDecimal mark = new BigDecimal(scoreStr);

                        StudentMarkFeedbackModal item = new StudentMarkFeedbackModal();
                        item.setStudentId(studentId);
                        item.setCourseId(courseId);
                        item.setTakeBy(teacherId);
                        item.setMark(mark);
                        item.setFeedback(feedback);
                        item.setDate(sessionLocalDate);
                        item.setCreatedAt(now);
                        item.setType(typeScore);
                        feedbackList.add(item);
                    }
                }
            }

            StudentDAO stDao = new StudentDAO();
            String resultMessage = stDao.insertScore(feedbackList) ? "Thêm điểm thành công !" : "Lỗi thêm điểm";

            // Encode các tham số để truyền lại lên URL safely
            String redirectUrl = String.format(
                    "createScore?courseId=%d&grade=%s&name=%s&level=%s&subject=%s&studentEnrollment=%s&result=%s",
                    courseId,
                    safeEncode(grade),
                    safeEncode(nameCourse),
                    safeEncode(level),
                    safeEncode(subject),
                    safeEncode(studentEnrollment),
                    safeEncode(resultMessage)
            );

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi lưu điểm!");
            request.getRequestDispatcher("views/createScore.jsp").forward(request, response);
        }
    }

// Hàm encode an toàn
    private String safeEncode(String input) {
        if (input == null) {
            return "";
        }
        try {
            return URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

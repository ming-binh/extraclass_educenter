package controller;

import dao.*;
import modal.PaymentModal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PaymentApprovalServlet extends HttpServlet {

    private final NotificationDao notificationDao;
    private final StudentPaymentScheduleDAO studentPaymentScheduleDAO;
    
    public PaymentApprovalServlet() {
        this.notificationDao = new NotificationDao();
        this.studentPaymentScheduleDAO = new StudentPaymentScheduleDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String, Object>> pendingSchedules = dao.StudentPaymentScheduleDAO.getPendingSchedules();
        request.setAttribute("pendingSchedules", pendingSchedules);
        request.getRequestDispatcher("views/payment_approval.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int sectionId = Integer.parseInt(request.getParameter("sectionId"));
        String courseId = request.getParameter("courseId");
        String action = request.getParameter("action");
        if ("approve".equals(action)) {
            dao.StudentPaymentScheduleDAO.markPaid(studentId, sectionId);
            notificationDao.addNotification(studentId, "Thanh toán buổi học của bạn đã được duyệt.");
            response.sendRedirect("trang-xac-nhan-thanh-toan?msg=done"
                + "&studentId=" + studentId
                + "&sectionId=" + sectionId
                + (courseId != null ? "&courseId=" + courseId : ""));
        } else if ("reject".equals(action)) {
            notificationDao.addNotification(studentId, "Thanh toán buổi học của bạn đã bị từ chối.");
            response.sendRedirect("trang-xac-nhan-thanh-toan?msg=reject"
                + "&studentId=" + studentId
                + "&sectionId=" + sectionId);
        }
    }
}
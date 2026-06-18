package controller;

import dao.*;
import modal.*;
import dto.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashBoardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getAttribute("loggedInUserRole") == null) {
                response.sendRedirect("dang-nhap.jsp");
                return;
            }
            
            String role = (String) request.getAttribute("loggedInUserRole");
            int accountId = (int) request.getAttribute("loggedInUserId");
            
            // Lấy notification cho người dùng
            NotificationDao notiDao = new NotificationDao();
            List<NotificationModal> notifications = notiDao.getNotificationsByAccountId(accountId);
            request.setAttribute("notifications", notifications);
            
            // Lấy tổng doanh thu
            PaymentDAO paymentDao = new PaymentDAO();
            BigDecimal totalRevenue = paymentDao.getTotalRevenue();
            request.setAttribute("totalRevenue", totalRevenue);
            
            // Lấy tổng số khóa học
            CourseDAO courseDao = new CourseDAO();
            List<CourseModal> courses = courseDao.getAllCourse();
            request.setAttribute("totalCourses", courses.size());
            
            // Lấy tổng số giáo viên
            TeacherDAO teacherDao = new TeacherDAO();
            List<TeacherModal> teachers = teacherDao.getAllTeacher();
            request.setAttribute("totalTeachers", teachers.size());
            
            // Lấy tổng số học sinh
            StudentDAO studentDao = new StudentDAO();
            List<StudentModal> students = studentDao.getAllStudents();
            request.setAttribute("totalStudents", students.size());
            
            // Lấy doanh thu theo tháng
            List<Map<String, Object>> monthlyRevenue = getMonthlyRevenue();
            request.setAttribute("monthlyRevenue", monthlyRevenue);
            
            
            request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(DashBoardServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<Map<String, Object>> getMonthlyRevenue() {
    List<Map<String, Object>> monthlyData = new ArrayList<>();
    PaymentDAO paymentDao = new PaymentDAO();
    
    LocalDateTime now = LocalDateTime.now();
    BigDecimal previousRevenue = null;
    
    for (int i = 11; i >= 0; i--) {
        LocalDateTime start = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        
        BigDecimal revenue = paymentDao.getRevenueByDateRange(start, end);
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }
        
        Map<String, Object> monthData = new HashMap<>();
        monthData.put("month", start.getMonthValue());
        monthData.put("year", start.getYear());
        monthData.put("revenue", revenue);
        
        // Tính tăng trưởng so với tháng trước
        if (previousRevenue != null) {
            if (previousRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal growth = revenue.subtract(previousRevenue)
                        .multiply(new BigDecimal(100))
                        .divide(previousRevenue, 2, RoundingMode.HALF_UP);
                monthData.put("growthRate", growth);
            } else if (revenue.compareTo(BigDecimal.ZERO) > 0) {
                // Nếu tháng trước là 0 nhưng tháng này có doanh thu, coi như tăng trưởng 100%
                monthData.put("growthRate", new BigDecimal(100));
            } else {
                monthData.put("growthRate", BigDecimal.ZERO);
            }
        } else {
            monthData.put("growthRate", BigDecimal.ZERO);
        }
        
        monthlyData.add(monthData);
        previousRevenue = revenue; 
    }
    
    return monthlyData;
}
}
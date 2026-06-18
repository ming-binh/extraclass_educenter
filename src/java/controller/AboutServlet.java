package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtil;

public class AboutServlet extends HttpServlet {
   
    private Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        try (Connection conn = DBUtil.getConnection()) {
            
            // Đếm số học sinh
            String studentQuery = "SELECT COUNT(*) as student_count FROM student";
            try (PreparedStatement ps = conn.prepareStatement(studentQuery);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("studentCount", rs.getInt("student_count"));
                }
            }
            
            // Đếm số giáo viên
            String teacherQuery = "SELECT COUNT(*) as teacher_count FROM teacher";
            try (PreparedStatement ps = conn.prepareStatement(teacherQuery);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("teacherCount", rs.getInt("teacher_count"));
                }
            }
            
            // Đếm số khóa học
            String courseQuery = "SELECT COUNT(*) as course_count FROM course WHERE status = 'activated' or status = 'completed'";
            try (PreparedStatement ps = conn.prepareStatement(courseQuery);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("courseCount", rs.getInt("course_count"));
                }
            }
            
            // Đếm số phụ huynh
            String parentQuery = "SELECT COUNT(*) as parent_count FROM parent";
            try (PreparedStatement ps = conn.prepareStatement(parentQuery);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("parentCount", rs.getInt("parent_count"));
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi, trả về giá trị mặc định
            stats.put("studentCount", 500);
            stats.put("teacherCount", 50);
            stats.put("courseCount", 20);
            stats.put("parentCount", 400);
        }
        
        return stats;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            // Lấy dữ liệu thống kê từ database
            Map<String, Integer> stats = getStatistics();
            
            // Đặt dữ liệu vào request attribute
            request.setAttribute("stats", stats);
            
            // Forward đến trang JSP
            request.getRequestDispatcher("/views/about.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error");
        }
    }

}

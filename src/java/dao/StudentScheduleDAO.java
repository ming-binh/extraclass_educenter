/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import utils.DBUtil;

/**
 *
 * @author Astersa
 */
public class StudentScheduleDAO {
    
    /**
     * Lấy thông tin schedule của học sinh với trạng thái thanh toán
     * @param studentId ID của học sinh
     * @return Map chứa thông tin schedule theo ngày và thời gian
     */
    public Map<String, Object> getStudentSchedule(int studentId) throws Exception {
        Map<String, Object> scheduleData = new HashMap<>();
        
        // Lấy thông tin học sinh
        String studentInfoSql = """
            SELECT s.id, s.currentGrade, a.name as studentName, sc.className
            FROM student s
            JOIN account a ON s.accountId = a.id
            LEFT JOIN school_class sc ON s.schoolClassId = sc.id
            WHERE s.id = ?
        """;
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(studentInfoSql)) {
            
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> studentInfo = new HashMap<>();
                    studentInfo.put("id", rs.getInt("id"));
                    studentInfo.put("name", rs.getString("studentName"));
                    studentInfo.put("grade", rs.getString("currentGrade"));
                    studentInfo.put("className", rs.getString("className"));
                    scheduleData.put("studentInfo", studentInfo);
                }
            }
        }
        
        // Lấy các khóa học và section của học sinh
        String scheduleSql = """
            SELECT 
                c.id as courseId,
                c.name as courseName,
                c.subject as subject,
                c.grade as courseGrade,
                sec.id as sectionId,
                sec.dayOfWeek,
                sec.startTime,
                sec.endTime,
                sec.classroom,
                sec.dateTime,
                sec.status as sectionStatus,
                sc.status as enrollmentStatus,
                sc.isPaid as coursePaid,
                ss.attendanceStatus,
                ss.isPaid as sectionPaid,
                t.id as teacherId,
                ta.name as teacherName
            FROM student_course sc
            JOIN course c ON sc.courseId = c.id
            JOIN section sec ON c.id = sec.courseId
            LEFT JOIN student_section ss ON sc.studentId = ss.studentId AND sec.id = ss.sectionId
            LEFT JOIN teacher t ON c.teacherId = t.id
            LEFT JOIN account ta ON t.accountId = ta.id
            WHERE sc.studentId = ? 
            AND sc.status = 'accepted'
            AND (c.status = 'activated' OR c.status = 'upcoming')
            ORDER BY sec.dayOfWeek, sec.startTime
        """;
        
        Map<String, List<Map<String, Object>>> scheduleByDay = new HashMap<>();
        Map<String, String> legendItems = new HashMap<>();
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(scheduleSql)) {
            
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Lấy ngày thực tế từ dateTime thay vì dayOfWeek từ database
                    Timestamp dateTime = rs.getTimestamp("dateTime");
                    String dayOfWeek = "Unknown";
                    
                    if (dateTime != null) {
                        // Chuyển đổi dateTime thành ngày trong tuần
                        java.time.DayOfWeek actualDayOfWeek = dateTime.toLocalDateTime().getDayOfWeek();
                        switch (actualDayOfWeek) {
                            case MONDAY:
                                dayOfWeek = "Monday";
                                break;
                            case TUESDAY:
                                dayOfWeek = "Tuesday";
                                break;
                            case WEDNESDAY:
                                dayOfWeek = "Wednesday";
                                break;
                            case THURSDAY:
                                dayOfWeek = "Thursday";
                                break;
                            case FRIDAY:
                                dayOfWeek = "Friday";
                                break;
                            case SATURDAY:
                                dayOfWeek = "Saturday";
                                break;
                            case SUNDAY:
                                dayOfWeek = "Sunday";
                                break;
                        }
                    } else {
                        // Fallback nếu không có dateTime
                        dayOfWeek = rs.getString("dayOfWeek");
                    }
                    
                    String timeSlot = formatTimeSlot(rs.getTime("startTime"), rs.getTime("endTime"));
                    
                    Map<String, Object> section = new HashMap<>();
                    section.put("courseId", rs.getInt("courseId"));
                    section.put("courseName", rs.getString("courseName"));
                    section.put("subject", rs.getString("subject"));
                    section.put("teacherName", rs.getString("teacherName"));
                    section.put("classroom", rs.getString("classroom"));
                    section.put("timeSlot", timeSlot);
                    section.put("startTime", rs.getTime("startTime"));
                    section.put("endTime", rs.getTime("endTime"));
                    section.put("dateTime", dateTime);
                    
                    // Xác định trạng thái thanh toán và màu sắc
                    boolean coursePaid = rs.getBoolean("coursePaid");
                    boolean sectionPaid = rs.getBoolean("sectionPaid");
                    LocalDateTime sectionDateTime = dateTime != null ? 
                        dateTime.toLocalDateTime() : null;
                    LocalDateTime now = LocalDateTime.now();
                    
                    String status = determinePaymentStatus(coursePaid, sectionPaid, sectionDateTime, now);
                    section.put("status", status);
                    section.put("statusClass", getStatusClass(status));
                    
                    // Thêm vào legend
                    if (!legendItems.containsKey(status)) {
                        legendItems.put(status, getStatusDisplayName(status));
                    }
                    
                    // Thêm vào schedule theo ngày
                    if (!scheduleByDay.containsKey(dayOfWeek)) {
                        scheduleByDay.put(dayOfWeek, new ArrayList<>());
                    }
                    scheduleByDay.get(dayOfWeek).add(section);
                }
            }
        }
        
        scheduleData.put("schedule", scheduleByDay);
        scheduleData.put("legend", legendItems);
        
        return scheduleData;
    }
    
    /**
     * Xác định trạng thái thanh toán
     */
    private String determinePaymentStatus(boolean coursePaid, boolean sectionPaid, 
                                        LocalDateTime sectionDateTime, LocalDateTime now) {
        if (coursePaid && sectionPaid) {
            return "paid"; // Đã đóng tiền - xanh
        } else if (sectionDateTime != null && sectionDateTime.isBefore(now)) {
            return "overdue"; // Buổi học đã diễn ra nhưng chưa đóng - đỏ
        } else {
            return "pending"; // Buổi học chưa diễn ra hoặc đang diễn ra chưa đóng - vàng
        }
    }
    
    /**
     * Lấy CSS class cho trạng thái
     */
    private String getStatusClass(String status) {
        switch (status) {
            case "paid":
                return "ss-lesson-paid";
            case "overdue":
                return "ss-lesson-overdue";
            case "pending":
                return "ss-lesson-pending";
            default:
                return "ss-lesson-type";
        }
    }
    
    /**
     * Lấy tên hiển thị cho trạng thái
     */
    private String getStatusDisplayName(String status) {
        switch (status) {
            case "paid":
                return "Đã thanh toán";
            case "overdue":
                return "Chưa thanh toán (Quá hạn)";
            case "pending":
                return "Chưa thanh toán";
            default:
                return "Không xác định";
        }
    }
    
    /**
     * Format thời gian slot
     */
    private String formatTimeSlot(Time startTime, Time endTime) {
        if (startTime != null && endTime != null) {
            return startTime.toString() + " - " + endTime.toString();
        }
        return "";
    }
    
    /**
     * Lấy danh sách các khóa học của học sinh
     */
    public List<Map<String, Object>> getStudentCourses(int studentId) throws Exception {
        List<Map<String, Object>> courses = new ArrayList<>();
        
        String sql = """
            SELECT 
                c.id,
                c.name,
                c.subject,
                c.grade,
                c.courseType,
                sc.status as enrollmentStatus,
                sc.isPaid,
                sc.enrollmentDate,
                t.id as teacherId,
                ta.name as teacherName
            FROM student_course sc
            JOIN course c ON sc.courseId = c.id
            LEFT JOIN teacher t ON c.teacherId = t.id
            LEFT JOIN account ta ON t.accountId = ta.id
            WHERE sc.studentId = ?
            ORDER BY sc.enrollmentDate DESC
        """;
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> course = new HashMap<>();
                    course.put("id", rs.getInt("id"));
                    course.put("name", rs.getString("name"));
                    course.put("subject", rs.getString("subject"));
                    course.put("grade", rs.getString("grade"));
                    course.put("courseType", rs.getString("courseType"));
                    course.put("enrollmentStatus", rs.getString("enrollmentStatus"));
                    course.put("isPaid", rs.getBoolean("isPaid"));
                    course.put("enrollmentDate", rs.getTimestamp("enrollmentDate"));
                    course.put("teacherName", rs.getString("teacherName"));
                    courses.add(course);
                }
            }
        }
        
        return courses;
    }
    
    /**
     * Test method
     */
    public static void main(String[] args) {
        try {
            StudentScheduleDAO dao = new StudentScheduleDAO();
            
            // Test với student ID = 1
            Map<String, Object> schedule = dao.getStudentSchedule(1);
            
            System.out.println("=== Student Schedule Test ===");
            System.out.println("Student Info: " + schedule.get("studentInfo"));
            System.out.println("Schedule: " + schedule.get("schedule"));
            System.out.println("Legend: " + schedule.get("legend"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
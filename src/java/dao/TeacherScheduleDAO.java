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
public class TeacherScheduleDAO {
    
    /**
     * Lấy thông tin schedule của giáo viên
     * @param teacherId ID của giáo viên
     * @return Map chứa thông tin schedule theo ngày và thời gian
     */
    public Map<String, Object> getTeacherSchedule(int teacherId) throws Exception {
        Map<String, Object> scheduleData = new HashMap<>();
        
        // Lấy thông tin giáo viên
        String teacherInfoSql = """
            SELECT t.id, t.subject, a.name as teacherName, t.experience
            FROM teacher t
            JOIN account a ON t.accountId = a.id
            WHERE t.id = ?
        """;
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(teacherInfoSql)) {
            
            stmt.setInt(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> teacherInfo = new HashMap<>();
                    teacherInfo.put("id", rs.getInt("id"));
                    teacherInfo.put("name", rs.getString("teacherName"));
                    teacherInfo.put("subject", rs.getString("subject"));
                    teacherInfo.put("experience", rs.getString("experience"));
                    scheduleData.put("teacherInfo", teacherInfo);
                }
            }
        }
        
        // Lấy các khóa học và section mà giáo viên quản lý
        String scheduleSql = """
            SELECT 
                c.id as courseId,
                c.name as courseName,
                c.subject as subject,
                c.grade as courseGrade,
                c.courseType,
                sec.id as sectionId,
                sec.dayOfWeek,
                sec.startTime,
                sec.endTime,
                sec.classroom,
                sec.dateTime,
                sec.status as sectionStatus,
                COUNT(sc.studentId) as studentCount
            FROM course c
            JOIN section sec ON c.id = sec.courseId
            LEFT JOIN student_course sc ON c.id = sc.courseId AND sc.status = 'accepted'
            WHERE c.teacherId = ? 
            AND (c.status = 'activated' OR c.status = 'upcoming')
            GROUP BY c.id, c.name, c.subject, c.grade, c.courseType, 
                     sec.id, sec.dayOfWeek, sec.startTime, sec.endTime, 
                     sec.classroom, sec.dateTime, sec.status
            ORDER BY sec.dayOfWeek, sec.startTime
        """;
        
        Map<String, List<Map<String, Object>>> scheduleByDay = new HashMap<>();
        Map<String, String> legendItems = new HashMap<>();
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(scheduleSql)) {
            
            stmt.setInt(1, teacherId);
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
                    String courseType = rs.getString("courseType");
                    
                    Map<String, Object> section = new HashMap<>();
                    section.put("courseId", rs.getInt("courseId"));
                    section.put("courseName", rs.getString("courseName"));
                    section.put("subject", rs.getString("subject"));
                    section.put("courseGrade", rs.getString("courseGrade"));
                    section.put("courseType", courseType);
                    section.put("classroom", rs.getString("classroom"));
                    section.put("timeSlot", timeSlot);
                    section.put("startTime", rs.getTime("startTime"));
                    section.put("endTime", rs.getTime("endTime"));
                    section.put("dateTime", dateTime);
                    section.put("studentCount", rs.getInt("studentCount"));
                    
                    // Xác định loại khóa học và màu sắc
                    String typeClass = getCourseTypeClass(courseType);
                    section.put("typeClass", typeClass);
                    
                    // Thêm vào legend
                    if (!legendItems.containsKey(courseType)) {
                        legendItems.put(courseType, getCourseTypeDisplayName(courseType));
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
     * Lấy CSS class cho loại khóa học
     */
    private String getCourseTypeClass(String courseType) {
        switch (courseType) {
            case "regular":
                return "ts-lesson-type";
            case "intensive":
                return "ts-meeting-type";
            case "remedial":
                return "ts-extra-type";
            case "advanced":
                return "ts-admin-type";
            default:
                return "ts-lesson-type";
        }
    }
    
    /**
     * Lấy tên hiển thị cho loại khóa học
     */
    private String getCourseTypeDisplayName(String courseType) {
        switch (courseType) {
            case "regular":
                return "Khóa học thường";
            case "intensive":
                return "Khóa học cấp tốc";
            case "remedial":
                return "Khóa học phụ đạo";
            case "advanced":
                return "Khóa học nâng cao";
            default:
                return "Khóa học khác";
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
     * Lấy danh sách các khóa học của giáo viên
     */
    public List<Map<String, Object>> getTeacherCourses(int teacherId) throws Exception {
        List<Map<String, Object>> courses = new ArrayList<>();
        
        String sql = """
            SELECT 
                c.id,
                c.name,
                c.subject,
                c.grade,
                c.courseType,
                c.status,
                c.createdAt,
                COUNT(sc.studentId) as studentCount
            FROM course c
            LEFT JOIN student_course sc ON c.id = sc.courseId AND sc.status = 'accepted'
            WHERE c.teacherId = ?
            GROUP BY c.id, c.name, c.subject, c.grade, c.courseType, c.status, c.createdAt
            ORDER BY c.createdAt DESC
        """;
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> course = new HashMap<>();
                    course.put("id", rs.getInt("id"));
                    course.put("name", rs.getString("name"));
                    course.put("subject", rs.getString("subject"));
                    course.put("grade", rs.getString("grade"));
                    course.put("courseType", rs.getString("courseType"));
                    course.put("status", rs.getString("status"));
                    course.put("studentCount", rs.getInt("studentCount"));
                    course.put("createdAt", rs.getTimestamp("createdAt"));
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
            TeacherScheduleDAO dao = new TeacherScheduleDAO();
            
            // Test với teacher ID = 1
            Map<String, Object> schedule = dao.getTeacherSchedule(1);
            
            System.out.println("=== Teacher Schedule Test ===");
            System.out.println("Teacher Info: " + schedule.get("teacherInfo"));
            System.out.println("Schedule: " + schedule.get("schedule"));
            System.out.println("Legend: " + schedule.get("legend"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import modal.CourseModal;
import org.apache.tomcat.dbcp.dbcp2.SQLExceptionList;
import utils.DBUtil;

/**
 *
 * @author Astersa
 */
public class StudentPaymentScheduleDAO  {

    public static void markPaid(int studentId, int sectionId) {
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(
                "UPDATE student_payment_schedule sps " +
                "JOIN student_section ss ON sps.student_section_id = ss.id " +
                "SET sps.isPaid = 1, sps.paid_date = NOW() " +
                "WHERE ss.studentId = ? AND ss.sectionId = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, sectionId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateMarkPaying(int studentId, int id, boolean markPaying) {
    try (java.sql.Connection conn = utils.DBUtil.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(
            "UPDATE student_payment_schedule sps " +
            "JOIN student_section ss ON sps.student_section_id = ss.id " +
            "SET sps.markPaying = ? " +
            "WHERE ss.studentId = ? AND (ss.sectionId = ? OR sps.courseId = ?)")) {
        ps.setBoolean(1, markPaying);
        ps.setInt(2, studentId); 
        ps.setInt(3, id);
        ps.setInt(4, id);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static boolean isPaid(int studentId, int sectionId) {
        boolean paid = false;
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(
                "SELECT sps.isPaid FROM student_payment_schedule sps " +
                "JOIN student_section ss ON sps.student_section_id = ss.id " +
                "WHERE ss.student_id = ? AND ss.section_id = ?")) {
            ps.setInt(1, studentId);
            ps.setInt(2, sectionId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                paid = rs.getBoolean("isPaid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paid;
    }

    public static List<Map<String, Object>> getPendingSchedules() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT sps.id as schedule_id, ss.studentId, ss.sectionId, sps.amount, sps.isPaid, sps.due_date,sps.markPaying, " +
                     "a.name as student_name, c.name as course_name, sec.dateTime, sec.startTime, sec.endTime, sps.courseId " +
                     "FROM student_payment_schedule sps " +
                     "LEFT JOIN student_section ss ON sps.student_section_id = ss.id " +
                     "LEFT JOIN student s ON ss.studentId = s.id " +
                     "LEFT JOIN account a ON s.accountId = a.id " +
                     "LEFT JOIN section sec ON ss.sectionId = sec.id " +
                     "LEFT JOIN course c ON (sec.courseId = c.id OR sps.courseId = c.id) " +
                     "WHERE sps.isPaid = 0";
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new java.util.HashMap<>();
                row.put("scheduleId", rs.getInt("schedule_id"));
                row.put("studentId", rs.getObject("studentId"));
                row.put("sectionId", rs.getObject("sectionId"));
                row.put("amount", rs.getBigDecimal("amount"));
                row.put("markPaying", rs.getBoolean("markPaying"));
                row.put("isPaid", rs.getBoolean("isPaid"));
                row.put("dueDate", rs.getTimestamp("due_date"));
                row.put("studentName", rs.getString("student_name"));
                row.put("courseName", rs.getString("course_name"));
                row.put("dateTime", rs.getTimestamp("dateTime"));
                row.put("startTime", rs.getTimestamp("startTime"));
                row.put("endTime", rs.getTimestamp("endTime"));
                row.put("courseId", rs.getObject("courseId"));
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

public boolean isPaymentPending(int studentId, Integer courseId, Integer sectionId) {
    try (java.sql.Connection conn = utils.DBUtil.getConnection();
         java.sql.PreparedStatement ps = conn.prepareStatement(
            "SELECT COUNT(*) FROM student_payment_schedule sps " +
            "LEFT JOIN student_section ss ON sps.student_section_id = ss.id " +
            "WHERE ss.studentId = ? " +  
            "AND (sps.courseId = ? OR ss.sectionId = ?) " +
            "AND (sps.markPaying = true OR sps.isPaid = true)")) {
        
        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.setInt(3, sectionId != null ? sectionId : courseId);
        
        java.sql.ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public static List<Map<String, Object>> getPendingSchedules(int limit, int offset) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT sps.id as schedule_id, ss.student_id, ss.section_id, sps.amount, sps.isPaid, sps.due_date,sps.markPaying, " +
                     "a.name as student_name, c.name as course_name, sec.date_time, sec.start_time, sec.end_time " +
                     "FROM student_payment_schedule sps " +
                     "JOIN student_section ss ON sps.student_section_id = ss.id " +
                     "JOIN student s ON ss.student_id = s.id " +
                     "JOIN account a ON s.accountId = a.id " +
                     "JOIN section sec ON ss.section_id = sec.id " +
                     "JOIN course c ON sec.course_id = c.id " +
                     "WHERE sps.isPaid = 0 " +
                     "ORDER BY sps.due_date ASC " +
                     "LIMIT ? OFFSET ?";
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new java.util.HashMap<>();
                row.put("scheduleId", rs.getInt("schedule_id"));
                row.put("studentId", rs.getInt("student_id"));
                row.put("sectionId", rs.getInt("section_id"));
                row.put("amount", rs.getBigDecimal("amount"));
                row.put("markPaying", rs.getBoolean("markPaying"));
                row.put("isPaid", rs.getBoolean("isPaid"));
                row.put("dueDate", rs.getTimestamp("due_date"));
                row.put("studentName", rs.getString("student_name"));
                row.put("courseName", rs.getString("course_name"));
                row.put("dateTime", rs.getTimestamp("date_time"));
                row.put("startTime", rs.getTimestamp("start_time"));
                row.put("endTime", rs.getTimestamp("end_time"));
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int countPendingSchedules() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM student_payment_schedule sps " +
                     "JOIN student_section ss ON sps.student_section_id = ss.id " +
                     "WHERE sps.isPaid = 0";
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public void deleteUnpaidByStudentId(int studentId) {
        String sql = """
            DELETE FROM student_payment_schedule
            WHERE student_section_id IN (
                SELECT id FROM student_section WHERE studentId = ?
            )
            AND isPaid = false
        """;
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPaymentSchedule(int studentSectionId, BigDecimal amount, LocalDateTime dueDate) {
        String sql = """
            INSERT INTO student_payment_schedule
            (student_section_id, amount, due_date, markPaying, isPaid, created_at, updated_at)
            VALUES (?, ?, ?, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        """;
        try (java.sql.Connection conn = utils.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentSectionId);
            ps.setBigDecimal(2, amount);
            ps.setTimestamp(3, Timestamp.valueOf(dueDate));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createPaymentSchedulesForStudentSections(List<Integer> studentSectionIds, 
                                                          int courseId) throws Exception {
        if (studentSectionIds == null || studentSectionIds.isEmpty()) {
            return true;
        }
        
        // Lấy thông tin course để xác định loại phí
        CourseModal course = getCourseInfo(courseId);
        if (course == null) {
            throw new Exception("Course not found with ID: " + courseId);
        }
        
        // Xác định logic tạo payment schedule dựa trên course type
        if (course.getCourseType() == CourseModal.CourseType.combo) {
            // Với combo course: tạo 1 payment record cho toàn bộ course với feeCombo
            return createComboPaymentSchedule(studentSectionIds.get(0), course.getFeeCombo(), courseId);
        } else {
            // Với daily course: tạo payment record cho từng buổi học với feeDaily
            return createDailyPaymentSchedules(studentSectionIds, course.getFeeDaily(), courseId);
        }
    }
    
    private boolean createComboPaymentSchedule(int firstStudentSectionId, 
                                             BigDecimal feeCombo, 
                                             int courseId) throws Exception {
        String insertSql = """
        INSERT INTO student_payment_schedule 
        (student_section_id, amount, due_date, markPaying, isPaid, courseId, created_at, updated_at)
        SELECT ?, ?, c.startDate, false, false, ?, NOW(), NOW()
        FROM course c
        WHERE c.id = ?
        """;
        
        try (java.sql.Connection conn = DBUtil.getConnection(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            
            stmt.setInt(1, firstStudentSectionId);
            stmt.setBigDecimal(2, feeCombo);
            stmt.setInt(3, courseId);
            stmt.setInt(4, courseId);
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLExceptionList e) {
            e.printStackTrace();
            throw new Exception("Error creating combo payment schedule: " + e.getMessage());
        }
    }
    
    private boolean createDailyPaymentSchedules(List<Integer> studentSectionIds, 
                                              BigDecimal feeDaily, 
                                              int courseId) throws Exception {
        String insertSql = """
        INSERT INTO student_payment_schedule 
        (student_section_id, amount, due_date, markPaying, isPaid, courseId, created_at, updated_at)
        SELECT ?, ?, s.dateTime, false, false, ?, NOW(), NOW()
        FROM student_section ss
        JOIN section s ON ss.sectionId = s.id
        WHERE ss.id = ?
        """;
        
        try (java.sql.Connection conn = DBUtil.getConnection(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            
            conn.setAutoCommit(false);
            
            try {
                for (Integer studentSectionId : studentSectionIds) {
                    stmt.setInt(1, studentSectionId);
                    stmt.setBigDecimal(2, feeDaily);
                    stmt.setInt(3, courseId);
                    stmt.setInt(4, studentSectionId);
                    stmt.addBatch();
                }
                
                int[] results = stmt.executeBatch();
                conn.commit();
                
                // Kiểm tra tất cả đều thành công
                for (int result : results) {
                    if (result <= 0) {
                        return false;
                    }
                }
                
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error creating daily payment schedules: " + e.getMessage());
        }
    }
    
    private CourseModal getCourseInfo(int courseId) throws Exception {
        String sql = """
        SELECT id, courseType, feeCombo, feeDaily, startDate
        FROM course 
        WHERE id = ?
        """;
        
        try (java.sql.Connection conn = DBUtil.getConnection(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            java.sql.ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                CourseModal course = new CourseModal();
                course.setId(rs.getInt("id"));
                course.setCourseType(CourseModal.CourseType.valueOf(rs.getString("courseType")));
                course.setFeeCombo(rs.getBigDecimal("feeCombo"));
                course.setFeeDaily(rs.getBigDecimal("feeDaily"));
                course.setStartDate(rs.getTimestamp("startDate").toLocalDateTime());
                return course;
            }
            
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error getting course info: " + e.getMessage());
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.SectionTime;
import dto.StudentCourseRequestDTO;
import java.util.ArrayList;
import java.util.List;
import modal.StudentCourseModal;
import utils.DBUtil;
import java.sql.*;
import java.time.*;

/**
 *
 * @author hungd
 */
public class StudentCourseDAO extends DBUtil {

    // Helper methods remain the same but with better error handling
    private StudentCourseModal mapResultSet(ResultSet rs) throws Exception {
        StudentCourseModal sc = new StudentCourseModal();
        sc.setId(rs.getInt("id"));
        sc.setStudentId(rs.getInt("studentId"));
        sc.setCourseId(rs.getInt("courseId"));
        sc.setStatus(StudentCourseModal.Status.valueOf(rs.getString("status")));
        sc.setIsPaid(rs.getBoolean("isPaid"));
        sc.setEnrollmentDate(rs.getObject("enrollment_date", LocalDateTime.class));

        return sc;
    }

    public List<StudentCourseModal> getAllStudentCourses() throws Exception {
        List<StudentCourseModal> studentCourses = new ArrayList<>();
        String sql = "SELECT * FROM student_course ORDER BY id";

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                studentCourses.add(mapResultSet(rs));
            }
        }
        return studentCourses;
    }

    public List<StudentCourseModal> getStudentCoursesByStatus(String status) throws Exception {
        List<StudentCourseModal> studentCourses = new ArrayList<>();
        String sql = "SELECT * FROM student_course WHERE status = ? ORDER BY id";

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    studentCourses.add(mapResultSet(rs));
                }
            }
        }
        return studentCourses;
    }

    public List<StudentCourseRequestDTO> getStudentCoursesRequest(int courseId) throws Exception {
        List<StudentCourseRequestDTO> studentCourseRequests = new ArrayList<>();

        String sql = """
        SELECT sc.id AS request_id,
               sc.studentId,
               a.name AS student_name,
               sc.courseId,
               c.name AS course_name,
               sc.status,
               sc.isPaid,
               sc.enrollment_date
        FROM student_course sc
        JOIN student s ON sc.studentId = s.id
        JOIN account a ON s.accountId = a.id
        JOIN course c ON sc.courseId = c.id
        WHERE sc.status = 'pending' AND sc.courseId = ?
        ORDER BY sc.id
    """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentCourseRequestDTO dto = new StudentCourseRequestDTO();

                    dto.setRequestId(rs.getInt("request_id"));
                    dto.setStudentId(rs.getInt("studentId"));
                    dto.setStudentName(rs.getString("student_name"));
                    dto.setCourseId(rs.getInt("courseId"));
                    dto.setCourseName(rs.getString("course_name"));
                    dto.setStatus(StudentCourseRequestDTO.Status.valueOf(rs.getString("status").trim()));
                    dto.setIsPaid(rs.getBoolean("isPaid"));
                    dto.setEnrollmentDate(rs.getObject("enrollment_date", LocalDateTime.class));

                    // Kiểm tra hợp lệ
                    String validStatus = checkValidStatus(con, dto.getStudentId(), dto.getCourseId());
                    dto.setValidStatus(StudentCourseRequestDTO.isValid.valueOf(validStatus.trim()));

                    studentCourseRequests.add(dto);
                }
            }
        }

        return studentCourseRequests;
    }

    private String checkValidStatus(Connection con, int studentId, int courseId) throws Exception {
        // 1. Kiểm tra nợ học phí các môn đã kết thúc
        String sqlDebt = """
         SELECT COUNT(*) 
            FROM student_course sc 
            JOIN course c ON sc.courseId = c.id
            WHERE sc.studentId = ? 
              AND sc.status = 'accepted'
              AND c.endDate < CURDATE()
              AND (sc.isPaid IS NULL OR sc.isPaid = 0)
    """;
        try (PreparedStatement stmt = con.prepareStatement(sqlDebt)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return "invalid_fees";
                }
            }
        }

        // 2. Kiểm tra lớp còn chỗ không
        String sqlCapacity = """
        SELECT studentEnrollment, maxStudents
        FROM course
        WHERE id = ?
    """;
        try (PreparedStatement stmt = con.prepareStatement(sqlCapacity)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int enrolled = rs.getInt("studentEnrollment");
                    int max = rs.getInt("maxStudents");
                    if (enrolled >= max) {
                        return "invalid_full";
                    }
                }
            }
        }

        // 3. Kiểm tra trùng lịch học
        boolean hasConflict = checkScheduleConflict(con, studentId, courseId);
        if (hasConflict) {
            return "invalid_schedule";
        }

        return "valid";
    }

    private boolean checkScheduleConflict(Connection con, int studentId, int newCourseId) throws Exception {
        // 1. Lấy lịch học của khóa học mới
        String sqlNewSections = "SELECT dayOfWeek, startTime, endTime FROM section WHERE courseId = ?";
        List<SectionTime> newSections = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sqlNewSections)) {
            stmt.setInt(1, newCourseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SectionTime st = new SectionTime();
                    st.setDayOfWeek(DayOfWeek.valueOf(rs.getString("dayOfWeek").toUpperCase()));
                    st.setStartTime(rs.getTime("startTime").toLocalTime());
                    st.setEndTime(rs.getTime("endTime").toLocalTime());
                    newSections.add(st);
                }
            }
        }

        // 2. Lấy danh sách courseId đã tham gia (accepted)
        String sqlJoinedCourses = """
        SELECT DISTINCT sc.courseId
        FROM student_course sc
        WHERE sc.studentId = ? AND sc.status = 'accepted' AND sc.courseId <> ?
    """;
        List<Integer> joinedCourseIds = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sqlJoinedCourses)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, newCourseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    joinedCourseIds.add(rs.getInt("courseId"));
                }
            }
        }

        // 3. So sánh từng section của các khóa đã tham gia với section mới
        String sqlSections = "SELECT dayOfWeek, startTime, endTime FROM section WHERE courseId = ?";
        for (int courseId : joinedCourseIds) {
            try (PreparedStatement stmt = con.prepareStatement(sqlSections)) {
                stmt.setInt(1, courseId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        DayOfWeek existingDay = DayOfWeek.valueOf(rs.getString("dayOfWeek").toUpperCase());
                        LocalTime existingStart = rs.getTime("startTime").toLocalTime();
                        LocalTime existingEnd = rs.getTime("endTime").toLocalTime();

                        for (SectionTime newSec : newSections) {
                            if (newSec.getDayOfWeek().equals(existingDay)) {
                                boolean isOverlap = newSec.getStartTime().isBefore(existingEnd)
                                        && existingStart.isBefore(newSec.getEndTime());
                                if (isOverlap) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean addStudentToCourse(int studentId, int courseId) throws Exception {
        String sql = "INSERT INTO student_course (studentId, courseId) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    public String getStudentCourseStatus(int studentId, int courseId) throws Exception {
        String sql = "SELECT status FROM student_course WHERE studentId = ? AND courseId = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status"); // "accepted", "pending", ...
                }
            }
        }
        return null; // Không tồn tại bản ghi
    }

    public boolean updateStatus(int requestId, String status) throws Exception {
        String sql = "UPDATE student_course SET status = ? WHERE id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, requestId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public StudentCourseModal getStudentCourseById(int id) throws Exception {
        String sql = "SELECT * FROM student_course WHERE id = ?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public void deleteByStudentId(int studentId) {
        String sql = "DELETE FROM student_course WHERE student_id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createStudentCourse(int studentId, int courseId) {
        String sql = """
            INSERT INTO student_course (studentId, courseId, status, isPaid, enrollment_date)
            VALUES (?, ?, 'accepted', false, CURRENT_TIMESTAMP)
        """;
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

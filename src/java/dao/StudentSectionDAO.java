/*
 * Fixed version of StudentSectionDAO with proper column naming and error handling
 */
package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modal.CourseModal;
import modal.SectionModal;
import modal.StudentSectionModal;
import utils.DBUtil;

public class StudentSectionDAO {

    public List<CourseModal> getAllActiveCourses() throws Exception {
        List<CourseModal> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE status = 'activated' ORDER BY name";

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }
    
        public List<CourseModal> getCoursesWithDetailsByTeacherId(int teacherId) throws Exception {
        List<CourseModal> courses = new ArrayList<>();
        String sql = """
        SELECT * FROM course
        WHERE teacherId = ? AND status = 'activated'
        ORDER BY 
            CASE status 
                WHEN 'activated' THEN 1 
                WHEN 'upcoming' THEN 2 
                WHEN 'pending' THEN 3 
                ELSE 4 
            END, 
            name
    """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        }
        return courses;
    }


    public List<SectionModal> getSectionsByCourseId(int courseId) throws Exception {
        List<SectionModal> sections = new ArrayList<>();
        String sql = "SELECT * FROM section WHERE courseId = ? ORDER BY dayOfWeek, startTime";

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sections.add(mapResultSetToSection(rs));
                }
            }
        }
        return sections;
    }

    public List<Map<String, Object>> getAttendanceReportByCourse(int courseId) throws Exception {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = """
            SELECT 
            s.id as student_id,
            a.name as student_name,
            a.username as student_email,
            sc.status as enrollment_status,
            sc.isPaid as course_paid,
            COUNT(ss.id) as total_sections,
            SUM(CASE WHEN ss.attendanceStatus = 'present' THEN 1 ELSE 0 END) as attended_sections,
            SUM(CASE WHEN ss.attendanceStatus = 'absent' THEN 1 ELSE 0 END) as missed_sections,
            ROUND((SUM(CASE WHEN ss.attendanceStatus = 'present' THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(ss.id), 0)), 2) as attendance_percentage
        FROM student s
        JOIN account a ON s.accountId = a.id
        JOIN student_course sc ON s.id = sc.studentId
        LEFT JOIN student_section ss ON s.id = ss.studentId
        LEFT JOIN section sec ON ss.sectionId = sec.id
        WHERE sc.courseId = ? AND (sec.courseId = ? OR sec.courseId IS NULL)
        GROUP BY s.id, a.name, a.username, sc.status, sc.isPaid
        ORDER BY a.name
        """;

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            stmt.setInt(2, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> studentReport = new HashMap<>();
                    studentReport.put("studentId", rs.getInt("student_id"));
                    studentReport.put("studentName", rs.getString("student_name"));
                    studentReport.put("studentEmail", rs.getString("student_email"));
                    studentReport.put("enrollmentStatus", rs.getString("enrollment_status"));
                    studentReport.put("coursePaid", rs.getBoolean("course_paid"));
                    studentReport.put("totalSections", rs.getInt("total_sections"));
                    studentReport.put("attendedSections", rs.getInt("attended_sections"));
                    studentReport.put("missedSections", rs.getInt("missed_sections"));
                    
                    // Handle null percentage
                    Double percentage = rs.getDouble("attendance_percentage");
                    studentReport.put("attendancePercentage", rs.wasNull() ? 0.0 : percentage);
                    
                    report.add(studentReport);
                }
            }
        }
        return report;
    }

    public Map<String, Object> getAttendanceStatistics(int courseId) throws Exception {
        Map<String, Object> stats = new HashMap<>();
        String sql = """
            SELECT 
            COUNT(DISTINCT sc.studentId) as total_students,
            COUNT(DISTINCT sec.id) as total_sections,
            COUNT(ss.id) as total_attendance_records,
            SUM(CASE WHEN ss.attendanceStatus = 'present' THEN 1 ELSE 0 END) as total_present,
            SUM(CASE WHEN ss.attendanceStatus = 'absent' THEN 1 ELSE 0 END) as total_absent,
            ROUND((SUM(CASE WHEN ss.attendanceStatus = 'present' THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(ss.id), 0)), 2) as overall_attendance_rate
        FROM student_course sc
        LEFT JOIN section sec ON sc.courseId = sec.courseId
        LEFT JOIN student_section ss ON sc.studentId = ss.studentId AND ss.sectionId = sec.id
        WHERE sc.courseId = ?
        """;

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalStudents", rs.getInt("total_students"));
                    stats.put("totalSections", rs.getInt("total_sections"));
                    stats.put("totalAttendanceRecords", rs.getInt("total_attendance_records"));
                    stats.put("totalPresent", rs.getInt("total_present"));
                    stats.put("totalAbsent", rs.getInt("total_absent"));
                    
                    Double rate = rs.getDouble("overall_attendance_rate");
                    stats.put("overallAttendanceRate", rs.wasNull() ? 0.0 : rate);
                }
            }
        }
        return stats;
    }

    // FIXED: Section report with consistent column naming
    public List<Map<String, Object>> getAttendanceReportBySection(int sectionId) throws Exception {
        List<Map<String, Object>> report = new ArrayList<>();
        
        // Updated query with consistent column naming
        String sql = """
        SELECT 
            s.id AS student_id,
            a.name AS student_name,
            a.username AS student_email,
            ss.attendanceStatus AS attendance_status,
            sc.isPaid AS course_paid,
            ss.created_at AS attendance_date,
            sec.classroom,
            sec.dayOfWeek AS day_of_week,
            sec.startTime AS start_time,
            sec.endTime AS end_time,
            sec.dateTime AS date_time,
            c.name AS course_name
        FROM student s
        JOIN account a ON s.accountId = a.id
        JOIN student_section ss ON s.id = ss.studentId
        JOIN section sec ON ss.sectionId = sec.id
        JOIN course c ON sec.courseId = c.id
        JOIN student_course sc ON s.id = sc.studentId AND c.id = sc.courseId
        WHERE ss.sectionId = ?
        ORDER BY a.name
        """;

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> studentReport = new HashMap<>();
                    studentReport.put("studentId", rs.getInt("student_id"));
                    studentReport.put("studentName", rs.getString("student_name"));
                    studentReport.put("studentEmail", rs.getString("student_email"));
                    // Lấy attendanceStatus là chuỗi ENUM → chuyển thành display name từ enum
                    String statusStr = rs.getString("attendance_status");
                    System.out.println("Raw status from DB: " + statusStr); // <-- DEBUG

                    StudentSectionModal.AttendanceStatus status = null;
                    try {
                        status = StudentSectionModal.AttendanceStatus.valueOf(statusStr);
                        System.out.println("Mapped enum: " + status + " → displayName: " + status.name()); // <-- DEBUG
                    } catch (IllegalArgumentException | NullPointerException e) {
                        System.out.println("Invalid status value: " + statusStr); // <-- DEBUG
                        status = null;
                    }
                    studentReport.put("attendanceStatus", status != null ? status.name() : "Không xác định");
                    studentReport.put("coursePaid", rs.getBoolean("course_paid"));
                    
                    // Handle potential null attendance_date
                    Timestamp attendanceTimestamp = rs.getTimestamp("attendance_date");
                    if (attendanceTimestamp != null) {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        LocalDateTime attendanceDateTime = attendanceTimestamp.toLocalDateTime();
                        studentReport.put("attendanceDate", dateFormatter.format(attendanceDateTime));
                    } else {
                        studentReport.put("attendanceDate", "");
                    }

                    studentReport.put("classroom", rs.getString("classroom"));
                    studentReport.put("dayOfWeek", rs.getString("day_of_week"));
                    
                    // Handle time formatting safely
                    Time startTimeDb = rs.getTime("start_time");
                    Time endTimeDb = rs.getTime("end_time");
                    if (startTimeDb != null && endTimeDb != null) {
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime startTime = startTimeDb.toLocalTime();
                        LocalTime endTime = endTimeDb.toLocalTime();
                        studentReport.put("startTime", timeFormatter.format(startTimeDb.toLocalTime()));
                        studentReport.put("endTime", timeFormatter.format(endTimeDb.toLocalTime()));
                    }

                    studentReport.put("courseName", rs.getString("course_name"));
                    
                    // Handle section date safely
                    Timestamp sectionTimestamp = rs.getTimestamp("date_time");
                    if (sectionTimestamp != null) {
                        LocalDateTime sectionDateTime = sectionTimestamp.toLocalDateTime();
                        studentReport.put("sectionDate", java.sql.Date.valueOf(sectionDateTime.toLocalDate()));
                    }
                    
                    report.add(studentReport);
                }
            }
        }
        return report;
    }

    // Helper methods remain the same but with better error handling
    private CourseModal mapResultSetToCourse(ResultSet rs) throws Exception {
        CourseModal c = new CourseModal();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setStatus(CourseModal.Status.valueOf(rs.getString("status")));
        c.setCourseType(CourseModal.CourseType.valueOf(rs.getString("courseType")));
        c.setSubject(CourseModal.Subject.valueOf(rs.getString("subject")));
        c.setGrade(rs.getString("grade"));
        c.setLevel(CourseModal.Level.valueOf(rs.getString("level")));
        c.setMaxStudents(rs.getInt("maxStudents"));
        c.setStudentEnrollment(rs.getInt("studentEnrollment"));
        return c;
    }

    private SectionModal mapResultSetToSection(ResultSet rs) throws Exception {
        SectionModal s = new SectionModal();
        s.setId(rs.getInt("id"));
        s.setCourseId(rs.getInt("courseId"));
        s.setDayOfWeek(SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")));
        
        // Improved DateTime mapping
        Timestamp dateTimeTs = rs.getTimestamp("dateTime");
        Time startTimeT = rs.getTime("startTime");
        Time endTimeT = rs.getTime("endTime");
        
        if (dateTimeTs != null && startTimeT != null && endTimeT != null) {
            LocalDateTime dateTime = dateTimeTs.toLocalDateTime();
            LocalTime startTime = startTimeT.toLocalTime();
            LocalTime endTime = endTimeT.toLocalTime();
            
            s.setStartTime(startTime.atDate(dateTime.toLocalDate()));
            s.setEndTime(endTime.atDate(dateTime.toLocalDate()));
            s.setDateTime(dateTime);
        }
        
        s.setClassroom(rs.getString("classroom"));
        s.setStatus(SectionModal.Status.valueOf(rs.getString("status")));
        s.setTeacherId(rs.getInt("teacherId"));
        s.setNote(rs.getString("note"));
        return s;
    }

    private StudentSectionModal mapResultSetToStudentSection(ResultSet rs) throws Exception {
        StudentSectionModal ss = new StudentSectionModal();
        ss.setId(rs.getInt("id"));
        ss.setStudentId(rs.getInt("studentId"));
        ss.setSectionId(rs.getInt("sectionId"));
        ss.setIsPaid(rs.getBoolean("isPaid"));
        String statusStr = rs.getString("attendanceStatus");
        if (statusStr != null) {
            ss.setAttendanceStatus(StudentSectionModal.AttendanceStatus.valueOf(statusStr));
        }

        
        Timestamp createdAtTs = rs.getTimestamp("created_at");
        if (createdAtTs != null) {
            ss.setCreatedAt(createdAtTs.toLocalDateTime());
        }
        
        return ss;
    }

     public List<CourseModal> getCoursesWithDetails() throws Exception {
        List<CourseModal> courses = new ArrayList<>();
        String sql = """
            SELECT * FROM course 
            ORDER BY 
                CASE status 
                    WHEN 'activated' THEN 1 
                    WHEN 'upcoming' THEN 2 
                    WHEN 'pending' THEN 3 
                    ELSE 4 
                END, 
                name
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        }
        return courses;
    }

    // Method to get courses by filter criteria
    public List<CourseModal> getCoursesByFilter(String subject, String level, String courseType, String status) throws Exception {
        List<CourseModal> courses = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM course WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (subject != null && !subject.isEmpty()) {
            sql.append(" AND subject = ?");
            params.add(subject);
        }

        if (level != null && !level.isEmpty()) {
            sql.append(" AND level = ?");
            params.add(level);
        }

        if (courseType != null && !courseType.isEmpty()) {
            sql.append(" AND courseType = ?");
            params.add(courseType);
        }

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY name");

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapResultSetToCourse(rs));
                }
            }
        }
        return courses;
    }

    public List<SectionModal> getSectionsByTeacherId(int teacherId) throws Exception {
        List<SectionModal> sections = new ArrayList<>();
        String sql = """
        SELECT sec.* FROM section sec
        JOIN course c ON sec.courseId = c.id
        WHERE c.teacherId = ?
        ORDER BY sec.dateTime
    """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sections.add(mapResultSetToSection(rs));
                }
            }
        }
        return sections;
    }

    public List<StudentSectionModal> getStudentAttendanceBySection(int sectionId) throws Exception {
        List<StudentSectionModal> list = new ArrayList<>();
        String sql = """
        SELECT ss.* FROM student_section ss
        WHERE ss.sectionId = ?
        ORDER BY ss.studentId
    """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToStudentSection(rs));
                }
            }
        }
        return list;
    }

    public List<Map<String, Object>> getStudentsInSection(int sectionId) throws Exception {
        List<Map<String, Object>> students = new ArrayList<>();

        String sql = """
        SELECT s.id AS student_id, a.name AS student_name
        FROM student s
        JOIN account a ON s.accountId = a.id
        JOIN student_course sc ON s.id = sc.studentId
        JOIN section sec ON sc.courseId = sec.courseId
        WHERE sec.id = ?
        ORDER BY a.name
    """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> student = new HashMap<>();
                    student.put("studentId", rs.getInt("student_id"));
                    student.put("studentName", rs.getString("student_name"));
                    students.add(student);
                }
            }
        }

        return students;
    }

    public StudentSectionModal.AttendanceStatus getAttendanceStatus(int studentId, int sectionId) throws Exception {
        String sql = "SELECT attendanceStatus FROM student_section WHERE studentId = ? AND sectionId = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String statusStr = rs.getString("attendanceStatus");
                    if (statusStr != null) {
                        try {
                            return StudentSectionModal.AttendanceStatus.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid attendance status: " + statusStr);
                            return null;
                        }
                    }
                }
                return null;
            }
        }
    }

    public List<StudentSectionModal> getStudentAttendanceDetails(int studentId, int courseId) throws Exception {
        List<StudentSectionModal> attendanceList = new ArrayList<>();
        String sql = """
            SELECT ss.* FROM student_section ss
            JOIN section sec ON ss.sectionId = sec.id
            WHERE ss.studentId = ? AND sec.courseId = ?
            ORDER BY sec.dateTime
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attendanceList.add(mapResultSetToStudentSection(rs));
                }
            }
        }
        return attendanceList;
    }

    public boolean upsertAttendance(int studentId, int sectionId, StudentSectionModal.AttendanceStatus attendanceStatus) {
        String checkSql = "SELECT COUNT(*) FROM student_section WHERE studentId = ? AND sectionId = ?";
        String insertSql = "INSERT INTO student_section (studentId, sectionId, attendanceStatus, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        String updateSql = "UPDATE student_section SET attendanceStatus = ?, created_at = CURRENT_TIMESTAMP WHERE studentId = ? AND sectionId = ?";

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false); // Start transaction
            try {
                // Check if record exists
                try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, studentId);
                    checkStmt.setInt(2, sectionId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            // Update existing record
                            try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
                                updateStmt.setString(1, attendanceStatus.name());
                                updateStmt.setInt(2, studentId);
                                updateStmt.setInt(3, sectionId);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            // Insert new record
                            try (PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
                                insertStmt.setInt(1, studentId);
                                insertStmt.setInt(2, sectionId);
                                insertStmt.setString(3, attendanceStatus.name());
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
                con.commit(); // Commit transaction
                return true;
            } catch (SQLException e) {
                con.rollback(); // Rollback on error
                throw new SQLException("Error upserting attendance: " + e.getMessage(), e);
            } finally {
                con.setAutoCommit(true); // Restore auto-commit
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

         public static void main(String[] args) {
        try {
            StudentSectionDAO dao = new StudentSectionDAO();

            int testCourseId = 1;
            int testStudentId = 1;

            System.out.println("=== Test getSectionsByCourseId ===");
            List<SectionModal> sections = dao.getSectionsByCourseId(testCourseId);
            for (SectionModal s : sections) {
                System.out.println("Section ID: " + s.getId());
                System.out.println("  Classroom: " + s.getClassroom());
                System.out.println("  DateTime: " + s.getDateTime());
                System.out.println("  Start: " + s.getStartTime());
                System.out.println("  End: " + s.getEndTime());
                System.out.println("  Status: " + s.getStatus());
                System.out.println("----------");
            }

            System.out.println("\n=== Test getStudentAttendanceDetails ===");
            List<StudentSectionModal> attendanceList = dao.getStudentAttendanceDetails(testStudentId, testCourseId);
            for (StudentSectionModal record : attendanceList) {
                System.out.println("Student ID: " + record.getStudentId());
                System.out.println("  Section ID: " + record.getSectionId());
                System.out.println("----------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
         
         public List<Integer> addStudentToActiveSections(int studentId, int courseId) throws Exception {
        List<Integer> createdStudentSectionIds = new ArrayList<>();
        
        String insertSql = """
        INSERT INTO student_section (studentId, sectionId)
        SELECT ?, s.id
        FROM section s
        WHERE s.courseId = ?
          AND s.dateTime > NOW()
          AND NOT EXISTS (
              SELECT 1 FROM student_section ss
              WHERE ss.studentId = ?
                AND ss.sectionId = s.id
          );
        """;
        
        // Query để lấy các student_section IDs cho các section active
        String getCreatedIdsSql = """
        SELECT ss.id 
        FROM student_section ss
        JOIN section s ON ss.sectionId = s.id
        WHERE ss.studentId = ? 
          AND s.courseId = ?
          AND s.dateTime > NOW()
        ORDER BY s.dateTime ASC
        """;
        
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Insert student sections
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, studentId);
                    insertStmt.setInt(2, courseId);
                    insertStmt.setInt(3, studentId);
                    insertStmt.executeUpdate();
                }
                
                // Lấy các IDs của student_section cho active sections
                try (PreparedStatement selectStmt = conn.prepareStatement(getCreatedIdsSql)) {
                    selectStmt.setInt(1, studentId);
                    selectStmt.setInt(2, courseId);
                    ResultSet rs = selectStmt.executeQuery();
                    
                    while (rs.next()) {
                        createdStudentSectionIds.add(rs.getInt("id"));
                    }
                }
                
                conn.commit();
                return createdStudentSectionIds;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error adding student to active sections: " + e.getMessage());
        }
    }

    public void deleteAllByStudentId(int studentId) {
        String sql = "DELETE FROM student_section WHERE studentId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createStudentSection(int studentId, int sectionId) {
        String sql = """
            INSERT INTO student_section (studentId, sectionId, isPaid, attendanceStatus, created_at)
            VALUES (?, ?, false, 'absent', CURRENT_TIMESTAMP)
        """;
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, studentId);
            ps.setInt(2, sectionId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

     
}

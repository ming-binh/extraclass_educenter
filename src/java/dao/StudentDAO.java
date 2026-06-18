/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import dto.CourseDTO;
import dto.StudentBasicInfo;
import dto.StudentProfile;
import dto.StudentScoreView;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modal.StudentMarkFeedbackModal;
import modal.StudentModal;
import utils.DBUtil;

/**
 *
 * @author hungd
 */
public class StudentDAO extends DBUtil {

    private StudentModal mapResultSet(ResultSet rs) throws SQLException {
        StudentModal student = new StudentModal();
        student.setId(rs.getInt("id"));
        student.setAccountId(rs.getInt("accountId"));
        student.setSchoolId(rs.getInt("schoolId"));
        student.setSchoolClassId(rs.getInt("schoolClassId"));
        student.setParentId(rs.getInt("parentId"));
        student.setCurrentGrade(rs.getString("currentGrade"));
        student.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        student.setUpdatedAt(rs.getObject("created_at", LocalDateTime.class));

        return student;
    }

    /**
     * Lấy thông tin học sinh theo AccountId.
     *
     * @param accountId
     * @return
     */
    public StudentModal getStudentByAccountId(int accountId) {
        String sql = "SELECT * FROM student WHERE accountId = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setInt(1, accountId);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<StudentModal> getAllStudents() throws Exception {
        List<StudentModal> list = new ArrayList<>();

        String sql = "SELECT id, accountId, schoolId, schoolClassId, parentId, currentGrade, created_at, updated_at FROM student";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StudentModal student = new StudentModal();

                student.setId(rs.getInt("id"));
                student.setAccountId(rs.getInt("accountId"));
                student.setSchoolId(rs.getInt("schoolId"));
                student.setSchoolClassId(rs.getInt("schoolClassId"));
                student.setParentId(rs.getInt("parentId"));
                student.setCurrentGrade(rs.getString("currentGrade"));

                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                if (createdAt != null) {
                    student.setCreatedAt(createdAt.toLocalDateTime());
                }
                if (updatedAt != null) {
                    student.setUpdatedAt(updatedAt.toLocalDateTime());
                }

                list.add(student);
            }
        }

        return list;
    }

    public StudentProfile getStudentProfileByPhone(String phone) throws Exception {
        StudentProfile profile = null;

        String sql = "SELECT ac.name, ac.phone, ac.dob, ac.address, ac.avatarURL, "
                + "st.currentGrade, st.schoolId, sc.name AS schoolName "
                + "FROM account AS ac "
                + "JOIN student AS st ON st.accountId = ac.id "
                + "JOIN school AS sc ON sc.id = st.schoolId "
                + "WHERE ac.phone = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    profile = new StudentProfile();
                    profile.setName(rs.getString("name"));
                    profile.setPhone(rs.getString("phone"));
                    profile.setDob(rs.getDate("dob").toLocalDate());
                    profile.setAddress(rs.getString("address"));
                    profile.setAvatarUrl(rs.getString("avatarURL"));
                    profile.setCurrentGrade(rs.getString("currentGrade"));
                    profile.setSchoolName(rs.getString("schoolName"));
                    profile.setSchoolId(rs.getInt("schoolId"));
                }
            }
        }

        return profile;
    }

    public void updateStudent(StudentModal student) throws Exception {
        String sql = "UPDATE student SET currentGrade = ?, schoolId = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getCurrentGrade());
            ps.setInt(2, student.getSchoolId());
            ps.setInt(3, student.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi cập nhật thông tin học sinh", e);
        }
    }

    public List<StudentBasicInfo> getStudentsByCourseId(int courseId) throws Exception {
        List<StudentBasicInfo> list = new ArrayList<>();

        String sql = "SELECT ac.name, st.id "
                + "FROM course AS co "
                + "JOIN student_course AS stco ON stco.courseId = co.id "
                + "JOIN student AS st ON st.id = stco.studentId "
                + "JOIN account AS ac ON ac.id = st.accountId "
                + "WHERE co.id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StudentBasicInfo student = new StudentBasicInfo();
                    student.setName(rs.getString("name"));
                    student.setId(rs.getInt("id"));
                    list.add(student);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error retrieving students by course ID", e);
        }

        return list;

    }

    public boolean insertScore(List<StudentMarkFeedbackModal> list) {
        String sql = "INSERT INTO student_mark_feedback (studentId, courseId, takeBy, mark, feedback, date, type, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (StudentMarkFeedbackModal item : list) {
                ps.setInt(1, item.getStudentId());
                ps.setInt(2, item.getCourseId());
                ps.setInt(3, item.getTakeBy());
                ps.setBigDecimal(4, item.getMark());
                ps.setString(5, item.getFeedback());
                ps.setTimestamp(6, Timestamp.valueOf(item.getDate()));
                ps.setString(7, item.getType());
                ps.setTimestamp(8, Timestamp.valueOf(item.getCreatedAt()));
                ps.addBatch();
            }

            int[] result = ps.executeBatch();

            // Nếu có ít nhất 1 dòng bị ảnh hưởng thì trả về true
            for (int r : result) {
                if (r >= 0) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, List<StudentScoreView>> getStudentScoresGroupedByCourse(String phone) throws Exception {
        Map<String, List<StudentScoreView>> scoreMap = new LinkedHashMap<>();

        String sql = """
        SELECT co.name AS courseName, stmf.mark, stmf.type, stmf.feedback, stmf.date
        FROM account ac
        JOIN student st ON st.accountId = ac.id
        JOIN student_course stco ON stco.studentId = st.id
        JOIN course co ON co.id = stco.courseId
        JOIN student_mark_feedback stmf ON stmf.courseId = co.id AND stmf.studentId = st.id
        WHERE ac.phone = ?
        ORDER BY co.name, stmf.date DESC
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String courseName = rs.getString("courseName");
                BigDecimal mark = rs.getBigDecimal("mark");
                String type = rs.getString("type");
                String feedback = rs.getString("feedback");
                Timestamp date = rs.getTimestamp("date");

                StudentScoreView score = new StudentScoreView();
                score.setCourseName(courseName);
                score.setMark(mark);
                score.setType(type);
                score.setFeedback(feedback);
                if (date != null) {
                    score.setDate(date.toLocalDateTime());
                }

                // Thêm vào map
                scoreMap.computeIfAbsent(courseName, k -> new ArrayList<>()).add(score);
            }
        }

        return scoreMap;
    }

    public Map<String, List<StudentScoreView>> getStudentScoresGroupedByCourse(int studentId) throws Exception {
        Map<String, List<StudentScoreView>> scoreMap = new LinkedHashMap<>();

        String sql = """
        SELECT co.name AS courseName, stmf.mark, stmf.type, stmf.feedback, stmf.date
        FROM student st
        JOIN student_course stco ON stco.studentId = st.id
        JOIN course co ON co.id = stco.courseId
        JOIN student_mark_feedback stmf ON stmf.courseId = co.id AND stmf.studentId = st.id
        WHERE st.id = ?
        ORDER BY co.name, stmf.date DESC
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String courseName = rs.getString("courseName");
                BigDecimal mark = rs.getBigDecimal("mark");
                String type = rs.getString("type");
                String feedback = rs.getString("feedback");
                Timestamp date = rs.getTimestamp("date");

                StudentScoreView score = new StudentScoreView();
                score.setCourseName(courseName);
                score.setMark(mark);
                score.setType(type);
                score.setFeedback(feedback);
                if (date != null) {
                    score.setDate(date.toLocalDateTime());
                }

                // Thêm vào map
                scoreMap.computeIfAbsent(courseName, k -> new ArrayList<>()).add(score);
            }
        }

        return scoreMap;
    }

    public List<StudentBasicInfo> getChildrenByParentPhone(String parentPhone) throws Exception {
        List<StudentBasicInfo> children = new ArrayList<>();

        String sql = """
        SELECT s.id, a.name 
        FROM account a
        JOIN student s ON a.id = s.accountId
        JOIN parent p ON s.parentId = p.id
        JOIN account pa ON p.accountId = pa.id
        WHERE pa.phone = ?
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parentPhone);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int studentId = rs.getInt("id");
                    String studentName = rs.getString("name");
                    children.add(new StudentBasicInfo(studentName, studentId));
                }
            }
        }

        return children;
    }

    public static void main(String[] args) throws Exception {
        StudentDAO stdao = new StudentDAO();
        Map<String, List<StudentScoreView>> scoreMap = stdao.getStudentScoresGroupedByCourse("0901234572");
        for (Map.Entry<String, List<StudentScoreView>> entry : scoreMap.entrySet()) {
            String courseName = entry.getKey();
            List<StudentScoreView> scores = entry.getValue();

            System.out.println("Khóa học: " + courseName);
            for (StudentScoreView score : scores) {
                System.out.println(" - Loại điểm: " + score.getType()
                        + ", Điểm: " + score.getMark()
                        + ", Nhận xét: " + score.getFeedback()
                        + ", Ngày: " + score.getDate());
            }
        }

    }


    public StudentModal getStudentById(int id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StudentModal> getChildrenOfParent(int parentId) {
        List<StudentModal> children = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE parentId = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    children.add(mapResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }

    public String getStudentNameById(int studentId) throws Exception {
    String sql = """
        SELECT a.name 
        FROM student s
        JOIN account a ON s.accountId = a.id
        WHERE s.id = ?
    """;

    try (Connection conn = DBUtil.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("name");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new Exception("Lỗi khi lấy tên học sinh theo ID", e);
    }

    return null;
}

    public static String getNameById(int studentId) {
        String name = "";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT a.name FROM student s JOIN account a ON s.accountId = a.id WHERE s.id = ?")) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public List<CourseDTO> getCoursesByStudentId(int studentId) {
        List<CourseDTO> list = new ArrayList<>();
        String sql = "SELECT c.id, c.name FROM course c \n"
                + "                JOIN student_course sc ON c.id = sc.courseid \n"
                + "               WHERE sc.studentid = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CourseDTO course = new CourseDTO();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                list.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insertStudent(StudentModal student) throws Exception {
        String sql = "INSERT INTO student (accountId, parentId, schoolId, schoolClassId, currentGrade, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, student.getAccountId());
            ps.setInt(2, student.getParentId());
            ps.setObject(3, student.getSchoolId());
            ps.setObject(4, student.getSchoolClassId());
            ps.setString(5, student.getCurrentGrade());
            ps.setObject(6, student.getCreatedAt());
            ps.setObject(7, student.getUpdatedAt());
            ps.executeUpdate();
        }
    }

    public List<StudentProfile> getChildrenProfilesByParentPhone(String parentPhone) throws Exception {
        List<StudentProfile> children = new ArrayList<>();

        String sql = """
        SELECT s.id, a.name, a.phone, a.dob, a.address, a.avatarURL, 
               s.currentGrade, s.schoolId, sc.name AS schoolName, a.status
        FROM student s
        JOIN account a ON s.accountId = a.id
        JOIN parent p ON s.parentId = p.id
        JOIN account pa ON p.accountId = pa.id
        LEFT JOIN school sc ON s.schoolId = sc.id
        WHERE pa.phone = ?
        ORDER BY a.name
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parentPhone);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StudentProfile profile = new StudentProfile();
                    profile.setId(rs.getInt("id"));
                    profile.setName(rs.getString("name"));
                    profile.setPhone(rs.getString("phone"));

                    // Xử lý ngày sinh
                    java.sql.Date dob = rs.getDate("dob");
                    if (dob != null) {
                        profile.setDob(dob.toLocalDate());
                    }

                    profile.setAddress(rs.getString("address"));
                    profile.setAvatarUrl(rs.getString("avatarURL"));
                    profile.setCurrentGrade(rs.getString("currentGrade"));
                    profile.setSchoolId(rs.getInt("schoolId"));
                    profile.setSchoolName(rs.getString("schoolName"));
                    String statusStr = rs.getString("status");
                    if (statusStr != null) {
                        profile.setStatus(StudentProfile.Status.valueOf(statusStr));
                    }


                    children.add(profile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi lấy danh sách profile con của phụ huynh", e);
        }

        return children;
    }


}

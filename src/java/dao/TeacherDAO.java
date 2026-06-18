/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.ClassBeingTaughtDTO;
import dto.TeacherDTO;
import dto.TeacherProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modal.TeacherAchivementModal;
import modal.TeacherCertificateModal;
import modal.TeacherModal;
import utils.DBUtil;
import java.sql.*;

/**
 *
 * @author hungd
 */
public class TeacherDAO extends DBUtil {

    private TeacherModal mapResultSetToTeacher(ResultSet rs) throws SQLException {
        TeacherModal teacher = new TeacherModal();
        teacher.setId(rs.getInt("id"));
        teacher.setAccountId(rs.getInt("accountId"));
        teacher.setSchoolId(rs.getInt("schoolId"));
        teacher.setSchoolClassId(rs.getInt("schoolClassId"));
        teacher.setExperience(rs.getString("experience"));
        teacher.setSubject(TeacherModal.Subject.valueOf(rs.getString("subject")));
        teacher.setBio(rs.getString("bio"));
        teacher.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        teacher.setUpdatedAt(rs.getObject("created_at", LocalDateTime.class));

        return teacher;
    }

    private TeacherCertificateModal mapResultSetToCertificate(ResultSet rs) throws SQLException {
        TeacherCertificateModal certificate = new TeacherCertificateModal();
        certificate.setId(rs.getInt("id"));
        certificate.setTeacherId(rs.getInt("teacherId"));
        certificate.setCertificateName(rs.getString("certificate_name"));
        certificate.setImageURL(rs.getString("imageURL"));
        certificate.setIssuedDate(rs.getObject("issued_date", LocalDateTime.class));
        certificate.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

        return certificate;
    }

    private TeacherAchivementModal mapResultSetToAchivement(ResultSet rs) throws SQLException {
        TeacherAchivementModal achivement = new TeacherAchivementModal();
        achivement.setId(rs.getInt("id"));
        achivement.setTeacherId(rs.getInt("teacherId"));
        achivement.setAchivementName(rs.getString("achivement_name"));
        achivement.setImageURL(rs.getString("imageURL"));
        achivement.setIssuedDate(rs.getObject("issued_date", LocalDateTime.class));
        achivement.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

        return achivement;
    }

    public List<TeacherCertificateModal> getCertOfTeacher(int teacherId) {
        List<TeacherCertificateModal> certificateList = new ArrayList<>();

        String sql = "SELECT tc.* "
                + "FROM teacher t "
                + "LEFT JOIN teacher_certificate tc ON t.id = tc.teacherId "
                + "WHERE t.id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    certificateList.add(mapResultSetToCertificate(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return certificateList;
    }

    public List<TeacherAchivementModal> getAchiveOfTeacher(int teacherId) {
        List<TeacherAchivementModal> achivementList = new ArrayList<>();

        String sql = "SELECT tc.* "
                + "FROM teacher t "
                + "LEFT JOIN teacher_achivement tc ON t.id = tc.teacherId "
                + "WHERE t.id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teacherId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    achivementList.add(mapResultSetToAchivement(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return achivementList;
    }

    /**
     * Lấy danh sách tất cả giáo viên trong hệ thống.
     *
     * @return Danh sách đối tượng TeacherModal
     */
    public List<TeacherModal> getAllTeacher() {
        List<TeacherModal> teacherList = new ArrayList<>();
        String sql = "SELECT * FROM teacher ORDER BY id";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                teacherList.add(mapResultSetToTeacher(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    public List<TeacherDTO> getAllTeachers() {
        List<TeacherDTO> teacherList = new ArrayList<>();
        String sql = """
        SELECT t.id, a.name, t.subject
        FROM teacher t
        JOIN account a ON t.accountId = a.id
        WHERE a.status = 'active'
    """;
        try (
                Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TeacherDTO teacher = new TeacherDTO();
                teacher.setId(rs.getInt("id"));
                teacher.setName(rs.getString("name"));
                teacher.setSubject(rs.getString("subject"));
                teacherList.add(teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teacherList;
    }

    /**
     * Lấy thông tin giáo viên dựa theo `account_id`.
     *
     * @param account_id ID của tài khoản giáo viên
     * @return Đối tượng TeacherModal nếu tìm thấy, ngược lại trả về null
     */
    public TeacherModal getTeacherByAccountID(int account_id) {
        String sql = "SELECT * FROM teacher WHERE accountId = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, account_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy thông tin giáo viên dựa theo `teacherId`.
     *
     * @param id ID của giáo viên
     * @return Đối tượng TeacherModal nếu tìm thấy, ngược lại trả về null
     */
    public TeacherModal getTeacherById(int id) {
        String sql = "SELECT * FROM teacher WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách giáo viên có số lượng khóa học nhiều nhất.
     *
     * @param limit Giới hạn số lượng giáo viên cần lấy
     * @return Danh sách giáo viên được sắp xếp theo số lượng khóa học giảm dần
     */
    public List<TeacherModal> getTopTeachers(int limit) {
        List<TeacherModal> topTeachers = new ArrayList<>();

        String sql = """
        SELECT t.*
        FROM teacher t
        JOIN (
            SELECT teacherId, COUNT(*) AS course_count
            FROM course
            GROUP BY teacherId
            ORDER BY course_count DESC
            LIMIT ?
        ) top ON t.id = top.teacherId
        """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topTeachers.add(mapResultSetToTeacher(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return topTeachers;
    }

    /**
     * Lấy danh sách giáo viên nổi bật bao gồm thông tin account
     *
     * @param limit Giới hạn số lượng giáo viên
     * @return List<Object[]> với Object[0] là AccountModal, Object[1] là
     * TeacherModal
     */
    public List<Object[]> getTopTeachersWithAccount(int limit) {
        List<Object[]> topTeachers = new ArrayList<>();

        String sql = """
        SELECT t.*, a.id as acc_id, a.name, a.phone, a.avatarURL, a.status
        FROM teacher t
        JOIN account a ON t.accountId = a.id
        JOIN (
            SELECT teacherId, COUNT(*) AS course_count
            FROM course
            GROUP BY teacherId
            ORDER BY course_count DESC
            LIMIT ?
        ) top ON t.id = top.teacherId
        ORDER BY top.course_count DESC
        """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Tạo AccountModal
                    modal.AccountModal account = new modal.AccountModal();
                    account.setId(rs.getInt("acc_id"));
                    account.setName(rs.getString("name"));
                    account.setPhone(rs.getString("phone"));
                    account.setAvatarURL(rs.getString("avatarURL"));
                    // Tạo TeacherModal
                    TeacherModal teacher = mapResultSetToTeacher(rs);

                    // Thêm vào list
                    topTeachers.add(new Object[]{account, teacher});
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return topTeachers;
    }

    /**
     * Lọc danh sách giáo viên theo các tiêu chí: môn học, lớp học và tên.
     *
     * @param subject Môn học cần lọc (có thể null hoặc rỗng nếu không lọc theo
     * môn)
     * @param grade Lớp học cần lọc (có thể null hoặc rỗng nếu không lọc theo
     * lớp)
     * @param name Tên giáo viên cần tìm (có thể null hoặc rỗng nếu không lọc
     * theo tên)
     * @return Danh sách giáo viên thỏa mãn điều kiện lọc
     */
    public List<TeacherModal> getFilteredTeacher(String subject, String grade, String name) throws Exception {
        List<TeacherModal> teacherList = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT t.* FROM teacher t "
                + "JOIN account a ON a.id = t.accountId "
                + "LEFT JOIN course c ON t.id = c.teacherId "
                + "WHERE a.status = 'active'"
        );

        List<Object> params = new ArrayList<>();

        if (subject != null && !subject.trim().isEmpty() && !"Tất cả".equalsIgnoreCase(subject.trim())) {
            sql.append(" AND t.subject LIKE ?");
            params.add("%" + subject.trim() + "%");
        }

        if (grade != null && !grade.trim().isEmpty() && !"Tất cả".equalsIgnoreCase(grade.trim())) {
            sql.append(" AND c.grade LIKE ?");
            params.add("%" + grade.trim() + "%");
        }

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND LOWER(a.name) LIKE ?");
            params.add("%" + name.trim().toLowerCase() + "%");
        }

        sql.append(" GROUP BY t.id ");
        sql.append(" ORDER BY a.name ASC ");

        try (
                Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pre.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    teacherList.add(mapResultSetToTeacher(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getFilteredTeacher: " + e.getMessage());
            e.printStackTrace();
        }

        return teacherList;
    }

    public int countStudentsFollowByTeacherId(int teacherId) {
        String sql = "SELECT COUNT(DISTINCT e.studentId) "
                + "FROM student_course e "
                + "JOIN course c ON e.courseId = c.id "
                + "WHERE c.teacherId = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, teacherId);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public TeacherModal getTeacherByPhone(String phone) throws Exception {
        String sql = "SELECT t.id, t.accountId, t.schoolId, t.schoolClassId, t.experience, t.subject, t.bio, t.created_at, t.updated_at "
                + "FROM teacher t "
                + "JOIN account a ON t.accountId = a.id "
                + "WHERE a.phone = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TeacherModal teacher = new TeacherModal();

                    teacher.setId(rs.getInt("id"));
                    teacher.setAccountId(rs.getInt("accountId"));
                    teacher.setSchoolId(rs.getInt("schoolId"));
                    teacher.setSchoolClassId(rs.getInt("schoolClassId"));
                    teacher.setExperience(rs.getString("experience"));

                    String subjectStr = rs.getString("subject");
                    try {
                        teacher.setSubject(TeacherModal.Subject.valueOf(subjectStr));
                    } catch (IllegalArgumentException | NullPointerException e) {
                        teacher.setSubject(null);
                    }
                    teacher.setBio(rs.getString("bio"));

                    Timestamp createdAt = rs.getTimestamp("created_at");
                    Timestamp updatedAt = rs.getTimestamp("updated_at");

                    if (createdAt != null) {
                        teacher.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    if (updatedAt != null) {
                        teacher.setUpdatedAt(updatedAt.toLocalDateTime());
                    }

                    return teacher;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi lấy giáo viên theo số điện thoại", e);
        }

        return null;
    }

    public TeacherProfile getTeacherProfileByPhone(String phone) throws Exception {
        String sql = "SELECT ac.name, sc.name as schoolName, t.experience, t.bio, t.subject, t.schoolId "
                + "FROM account AS ac "
                + "JOIN teacher AS t ON t.accountId = ac.id "
                + "JOIN school AS sc ON sc.id = t.schoolId "
                + "WHERE ac.phone = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TeacherProfile profile = new TeacherProfile();
                    profile.setName(rs.getString("name"));
                    profile.setSchoolName(rs.getString("schoolName"));
                    profile.setExperience(rs.getString("experience"));
                    profile.setBio(rs.getString("bio"));
                    profile.setSubject(rs.getString("subject"));
                    profile.setSchoolId(rs.getInt("schoolId"));
                    return profile;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching teacher profile", e);
        }

        return null;
    }

    public boolean updateTeacher(TeacherModal teacher) throws Exception {
        String sql = "UPDATE teacher SET schoolId = ?, experience = ?, bio = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacher.getSchoolId());
            ps.setString(2, teacher.getExperience());
            ps.setString(3, teacher.getBio());
            ps.setInt(4, teacher.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi cập nhật thông tin giáo viên", e);
        }
    }

    public TeacherModal getTeacherByAccountId(int accountId) throws Exception {
        String sql = "SELECT id, accountId, schoolId, subject, experience, bio, created_at, updated_at "
                + "FROM teacher WHERE accountId = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TeacherModal teacher = new TeacherModal();
                    teacher.setId(rs.getInt("id"));
                    teacher.setAccountId(rs.getInt("accountId"));
                    teacher.setSchoolId(rs.getInt("schoolId"));

                    String subjectStr = rs.getString("subject");
                    try {
                        teacher.setSubject(TeacherModal.Subject.valueOf(subjectStr));
                    } catch (IllegalArgumentException | NullPointerException e) {
                        teacher.setSubject(null);
                    }
                    teacher.setExperience(rs.getString("experience"));
                    teacher.setBio(rs.getString("bio"));

                    Timestamp createdAt = rs.getTimestamp("created_at");
                    Timestamp updatedAt = rs.getTimestamp("updated_at");
                    if (createdAt != null) {
                        teacher.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    if (updatedAt != null) {
                        teacher.setUpdatedAt(updatedAt.toLocalDateTime());
                    }

                    return teacher;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi lấy thông tin giáo viên theo accountId", e);
        }

        return null;
    }

    public List<ClassBeingTaughtDTO> getClassesByTeacherPhone(String phone) throws Exception {
        List<ClassBeingTaughtDTO> list = new ArrayList<>();

        String sql = """
        SELECT c.teacherId, c.id AS courseId, c.name, c.grade, c.subject, c.level, c.studentEnrollment
        FROM account ac
        JOIN teacher t ON t.accountId = ac.id
        JOIN course c ON c.teacherId = t.id
        WHERE ac.phone = ?
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClassBeingTaughtDTO cb = new ClassBeingTaughtDTO();
                    cb.setTeacherId(rs.getInt("teacherId"));
                    cb.setCourseId(rs.getInt("courseId"));
                    cb.setName(rs.getString("name"));
                    cb.setGrade(rs.getString("grade"));
                    cb.setSubject(rs.getString("subject"));
                    cb.setLevel(rs.getString("level"));
                    cb.setStudentEnrollment(rs.getInt("studentEnrollment"));
                    list.add(cb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi lấy danh sách lớp đang dạy theo phone", e);
        }

        return list;
    }

    public void insertTeacher(TeacherModal t) {
        String sql = "INSERT INTO teacher (accountId, subject, experience, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getAccountId());

            // Kiểm tra null cho subject
            if (t.getSubject() != null) {
                ps.setString(2, t.getSubject().name());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }

            ps.setString(3, t.getExperience());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertCertificate(TeacherCertificateModal c) {
        String sql = "INSERT INTO teacher_certificate (teacherId,certificate_name, imageUrl, issued_date, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getTeacherId());
            ps.setString(2, c.getCertificateName());
            ps.setString(3, c.getImageURL());
            ps.setTimestamp(4, c.getIssuedDate() != null ? Timestamp.valueOf(c.getIssuedDate()) : null);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

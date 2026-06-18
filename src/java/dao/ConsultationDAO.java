/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.ConsultationDTO;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modal.ConsultationCertificateModal;
import modal.ConsultationModal;
import utils.DBUtil;

/**
 *
 * @author ASUS
 */
public class ConsultationDAO {

    private ConsultationCertificateModal mapResultSetToConsultationCertificate(ResultSet rs) throws SQLException {
        ConsultationCertificateModal certificate = new ConsultationCertificateModal();
        certificate.setId(rs.getInt("id"));
        certificate.setConsultationId(rs.getInt("consultationId"));
        certificate.setImageURL(rs.getString("imageURL"));
        certificate.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        return certificate;
    }

    public int insertConsultation(ConsultationModal c) throws Exception {
        String sql = "INSERT INTO consultation (name, email, dob, phone, status, address, subject, experience, schoolId, schoolClassId, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setDate(3, java.sql.Date.valueOf(c.getDob().toLocalDate()));
            ps.setString(4, c.getPhone());
            ps.setString(5, c.getStatus().name());
            ps.setString(6, c.getAddress());

            // Xử lý subject enum - có thể null nếu là phụ huynh
            if (c.getSubject() != null) {
                ps.setString(7, c.getSubject().name());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }

            ps.setString(8, c.getExperience()); // Có thể null nếu là phụ huynh
            ps.setInt(9, c.getSchoolId());

            if (c.getSchoolClassId() != null) {
                ps.setInt(10, c.getSchoolClassId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<ConsultationDTO> listAndSearchConsultations(String name, String status, String role) {
        List<ConsultationDTO> all = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.dob, c.phone, c.status, c.email, c.subject "
                + "FROM consultation c WHERE 1=1 ";

        if (name != null && !name.trim().isEmpty()) {
            sql += " AND LOWER(TRIM(c.name)) LIKE ?";
        }
        if (status != null && !status.trim().isEmpty()) {
            sql += " AND c.status = ?";
        }

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + name.trim().toLowerCase() + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            ResultSet rs = ps.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while (rs.next()) {
                ConsultationDTO c = new ConsultationDTO();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));

                Timestamp dobTs = rs.getTimestamp("dob");
                if (dobTs != null) {
                    LocalDateTime dob = dobTs.toLocalDateTime();
                    c.setDob(dob);
                    c.setDobString(dob.format(formatter));
                }

                c.setPhone(rs.getString("phone"));
                c.setStatus(ConsultationDTO.Status.valueOf(rs.getString("status")));
                c.setEmail(rs.getString("email"));
                c.setSubjectFromString(rs.getString("subject"));

                if (c.getSubject() != null) {
                    c.setRole(ConsultationDTO.Role.teacher);
                } else {
                    c.setRole(ConsultationDTO.Role.parent);
                }

                all.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return all;
    }

    public ConsultationDTO getConsultationById(int id) {
        String sql = "SELECT c.*, s.name AS schoolName, sc.classname AS schoolClassName, c.email, cert.imageURL AS certificateImageUrl "
                + "FROM consultation c "
                + "LEFT JOIN school s ON c.schoolId = s.id "
                + "LEFT JOIN school_class sc ON c.schoolClassId = sc.id "
                + "LEFT JOIN consultation_certificate cert ON c.id = cert.consultationId "
                + "WHERE c.id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ConsultationDTO c = new ConsultationDTO();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));

                Timestamp dobTs = rs.getTimestamp("dob");
                if (dobTs != null) {
                    LocalDateTime dob = dobTs.toLocalDateTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    c.setDob(dob);
                    c.setDobString(dob.format(formatter));
                }

                c.setPhone(rs.getString("phone"));
                c.setStatus(ConsultationDTO.Status.valueOf(rs.getString("status")));
                c.setAddress(rs.getString("address"));
                String subjectStr = rs.getString("subject");
                c.setSubjectFromString(subjectStr);

                c.setExperience(rs.getString("experience"));
                c.setSchoolId(rs.getInt("schoolId"));
                c.setSchoolClassId(rs.getInt("schoolClassId"));
                c.setSchoolName(rs.getString("schoolName"));
                c.setSchoolClassName(rs.getString("schoolClassName"));
                c.setCertificateImageUrl(rs.getString("certificateImageUrl"));
                c.setEmail(rs.getString("email"));

                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStatus(int consultationId, ConsultationDTO.Status newStatus) {
        String sql = "UPDATE consultation SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, consultationId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ConsultationCertificateModal> getConsultationCertificates(int consultationId) throws Exception {
        List<ConsultationCertificateModal> certificates = new ArrayList<>();
        String sql = "SELECT * FROM consultation_certificate WHERE consultationId = ? ORDER BY created_at";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    certificates.add(mapResultSetToConsultationCertificate(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi lấy danh sách chứng chỉ consultation", e);
        }
        return certificates;
    }

    public ConsultationCertificateModal getConsultationCertificateById(int id) throws Exception {
        String sql = "SELECT * FROM consultation_certificate WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToConsultationCertificate(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi lấy thông tin chứng chỉ consultation theo ID", e);
        }
        return null;
    }

    public boolean insertConsultationCertificate(ConsultationCertificateModal certificate) throws Exception {
        String sql = "INSERT INTO consultation_certificate (consultationId, imageURL, created_at) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, certificate.getConsultationId());
            ps.setString(2, certificate.getImageURL());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi thêm chứng chỉ consultation", e);
        }
    }

    public void insertCertificate(int consultationId, String imageURL) throws Exception {
        String sql = "INSERT INTO consultation_certificate (consultationId, imageURL, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            ps.setString(2, imageURL);
            ps.executeUpdate();
        }
    }

    public Integer getSchoolIdByName(String name) throws Exception {
        String sql = "SELECT id FROM school WHERE name = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return null;
    }

    public int insertSchool(String name) throws Exception {
        String sql = "INSERT INTO school (name, created_at) VALUES (?, NOW())";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modal.AssignmentModal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtil;

/**
 * Lớp AssignmentDAO chịu trách nhiệm thực hiện các thao tác với cơ sở dữ liệu
 * liên quan đến bảng {@code section_assignment}, bao gồm:
 * <ul>
 * <li>Lấy danh sách bài tập theo khóa học.</li>
 * <li>Thêm mới bài tập.</li>
 * <li>Xóa bài tập theo ID.</li>
 * <li>Lấy chi tiết bài tập theo ID.</li>
 * </ul>
 *
 * Sử dụng lớp {@link DBUtil} để quản lý kết nối cơ sở dữ liệu.
 *
 * @author Admin
 */
public class AssignmentDAO {

    /**
     * Lấy danh sách các bài tập thuộc một khóa học.
     *
     * @param courseId ID của khóa học cần lấy bài tập.
     * @return Danh sách {@link AssignmentModal} chứa thông tin các bài tập.
     */
    public List<AssignmentModal> getAssignmentsByCourse(int courseId) {
        List<AssignmentModal> list = new ArrayList<>();
        String sql = "SELECT id, courseId, title, description, file_path, uploaded_at, due_at "
                + "FROM section_assignment WHERE courseId = ? ORDER BY id DESC";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AssignmentModal a = new AssignmentModal();
                    a.setId(rs.getInt("id"));
                    a.setCourseId(rs.getInt("courseId"));
                    a.setTitle(rs.getString("title"));
                    a.setDescription(rs.getString("description"));
                    a.setFilePath(rs.getString("file_path"));
                    a.setUploadedAt(rs.getTimestamp("uploaded_at"));
                    a.setDueAt(rs.getTimestamp("due_at"));
                    list.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm một bài tập mới vào bảng {@code section_assignment}.
     *
     * @param assignment Đối tượng {@link AssignmentModal} chứa thông tin bài
     * tập cần thêm.
     */
    public void insertAssignment(AssignmentModal assignment) {
        String sql = "INSERT INTO section_assignment (courseId, title, description, file_path, teacher_id, due_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignment.getCourseId());
            ps.setString(2, assignment.getTitle());
            ps.setString(3, assignment.getDescription());
            ps.setString(4, assignment.getFilePath());
            ps.setInt(5, assignment.getTeacherId());
            ps.setTimestamp(6, assignment.getDueAt());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa một bài tập theo ID.
     *
     * @param id ID của bài tập cần xóa.
     */
    public void deleteAssignmentById(int id) {
        String sql = "DELETE FROM section_assignment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy chi tiết một bài tập theo ID.
     *
     * @param id ID của bài tập cần lấy.
     * @return Đối tượng {@link AssignmentModal} chứa thông tin bài tập hoặc
     * {@code null} nếu không tìm thấy.
     */
    public AssignmentModal getAssignmentById(int id) {
        String sql = "SELECT * FROM section_assignment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AssignmentModal a = new AssignmentModal();
                    a.setId(rs.getInt("id"));
                    a.setCourseId(rs.getInt("courseId"));
                    a.setTitle(rs.getString("title"));
                    a.setDescription(rs.getString("description"));
                    a.setFilePath(rs.getString("file_path"));
                    a.setTeacherId(rs.getInt("teacher_id"));
                    a.setUploadedAt(rs.getTimestamp("uploaded_at"));
                    a.setDueAt(rs.getTimestamp("due_at"));
                    return a;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách bài tập theo section (cột courseId trong DB hiện đang lưu
     * sectionId).
     */
    public List<AssignmentModal> getAssignmentsBySection(int sectionId) {
        return getAssignmentsByCourse(sectionId); // tái dùng hàm sẵn có
    }

    /**
     * Lấy sectionId chứa assignment (dùng khi download để kiểm tra quyền).
     *
     * @return sectionId hoặc -1 nếu không tìm thấy.
     */
    public int getSectionIdByAssignment(int assignmentId) {
        String sql = "SELECT courseId FROM section_assignment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("courseId");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}

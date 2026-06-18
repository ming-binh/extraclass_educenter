/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modal.SubmissionModal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtil;

/**
 *
 * @author HanND
 */
/**
 * DAO cho bảng submission (bài nộp).
 */
public class SubmissionDAO {

    // 1. Xem lịch sử đã nộp của học sinh
    public List<SubmissionModal> getSubmissionsByStudent(int studentId) throws Exception {
        List<SubmissionModal> list = new ArrayList<>();
        String sql = "SELECT * FROM submission_assignment WHERE student_id = ? ORDER BY submitted_at DESC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SubmissionModal sa = new SubmissionModal();
                sa.setId(rs.getInt("id"));
                sa.setSectionAssignmentId(rs.getInt("section_assignment_id"));
                sa.setCourseId(rs.getInt("course_id"));
                sa.setStudentId(rs.getInt("student_id"));
                sa.setFilePath(rs.getString("file_path"));
                sa.setSubmittedAt(rs.getTimestamp("submitted_at"));
                sa.setComment(rs.getString("comment"));
                sa.setGrade(rs.getDouble("grade"));
                list.add(sa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 2. Insert nếu không có SubmissionModal
    public void insertSubmission(int sectionAssignmentId, int courseId, int studentId, String filePath) throws Exception {
        String sql = "INSERT INTO submission_assignment "
                + "(section_assignment_id, course_id, student_id, file_path, submitted_at) "
                + "VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionAssignmentId);
            ps.setInt(2, courseId);
            ps.setInt(3, studentId);
            ps.setString(4, filePath);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi insert submission: " + e.getMessage());
        }
    }

    // 3. Lịch sử nộp bài theo student và course
    public List<SubmissionModal> getSubmissionHistoryByStudentAndCourse(int studentId, int courseId) {
        List<SubmissionModal> list = new ArrayList<>();
        String sql = "SELECT sa.*, sec.courseId, sec.title FROM submission_assignment sa "
                + "JOIN section_assignment sec ON sa.section_assignment_id = sec.id "
                + "WHERE sa.student_id = ? AND sec.courseId = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SubmissionModal sub = new SubmissionModal();
                sub.setId(rs.getInt("id"));
                sub.setSectionAssignmentId(rs.getInt("section_assignment_id"));
                sub.setStudentId(rs.getInt("student_id"));
                sub.setFilePath(rs.getString("file_path"));
                sub.setSubmittedAt(rs.getTimestamp("submitted_at"));
                sub.setAssignmentTitle(rs.getString("title"));
                sub.setGrade(rs.getDouble("grade"));
                sub.setComment(rs.getString("comment"));
                list.add(sub);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // 4. Giáo viên xem các bài học sinh đã nộp (theo course hoặc theo assignment)

    public List<SubmissionModal> getSubmissionsByCourse(int courseId) throws Exception {
        List<SubmissionModal> list = new ArrayList<>();
        String sql = "SELECT sa.*, acc.name AS student_name, sec.title AS assignment_title\n"
                + "FROM submission_assignment sa\n"
                + "JOIN student stu ON sa.student_id = stu.id\n"
                + "JOIN account acc ON stu.accountId = acc.id\n"
                + "JOIN section_assignment sec ON sa.section_assignment_id = sec.id\n"
                + "WHERE sa.course_id = ?\n"
                + "ORDER BY sa.submitted_at DESC;";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SubmissionModal sub = new SubmissionModal();
                sub.setId(rs.getInt("id"));
                sub.setSectionAssignmentId(rs.getInt("section_assignment_id"));
                sub.setCourseId(rs.getInt("course_id"));
                sub.setStudentId(rs.getInt("student_id"));
                sub.setFilePath(rs.getString("file_path"));
                sub.setSubmittedAt(rs.getTimestamp("submitted_at"));
                sub.setComment(rs.getString("comment"));
                sub.setGrade(rs.getObject("grade") != null ? rs.getDouble("grade") : null);
                sub.setAssignmentTitle(rs.getString("assignment_title"));
                sub.setStudentName(rs.getString("student_name"));
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Giáo viên chấm điểm và viết nhận xét
    public void gradeSubmission(int submissionId, double grade, String comment) throws Exception {
        String sql = "UPDATE submission_assignment SET grade = ?, comment = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, grade);
            ps.setString(2, comment);
            ps.setInt(3, submissionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

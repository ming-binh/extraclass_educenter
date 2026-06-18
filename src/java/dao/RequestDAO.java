/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.RequestDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modal.RequestModal;
import modal.RequestModal.Status;
import utils.DBUtil;

/**
 *
 * @author Astersa
 */
public class RequestDAO {

    public List<RequestDTO> getAllRequests() throws Exception {
        List<RequestDTO> requests = new ArrayList<>();
        String sql = "SELECT * FROM request JOIN account ON request.requestBy = account.id ORDER BY createdAt DESC ";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RequestDTO request = mapResultSetToRequestDTO(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<RequestModal> getRequestsByUser(Integer userId) throws Exception {
        List<RequestModal> requests = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE requestBy = ? ORDER BY createdAt DESC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestModal request = mapResultSetToRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<RequestModal> getRequestsByType(String type) throws Exception {
        List<RequestModal> requests = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE type = ? ORDER BY createdAt DESC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestModal request = mapResultSetToRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<RequestModal> getRequestsByStatus(Status status) throws Exception {
        List<RequestModal> requests = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE status = ? ORDER BY createdAt DESC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestModal request = mapResultSetToRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public RequestModal getRequestById(Integer id) throws Exception {
        String sql = "SELECT * FROM request WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToRequest(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createRequest(RequestModal request) throws Exception {
        String sql = "INSERT INTO request (requestBy, type, description, sectionId, fromCourseId,toCourseId, status) VALUES (?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, request.getRequestBy());
            ps.setString(2, request.getType().name());
            ps.setString(3, request.getDescription());
            ps.setObject(4, request.getSectionId());
            ps.setObject(5, request.getFromCourseId());
            ps.setObject(6, request.getToCourseId());
            ps.setString(7, request.getStatus().name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    request.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateRequestStatus(Integer requestId, Status status, Integer processedBy) throws Exception {
        String sql = "UPDATE request SET status = ?, processedBy = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setObject(2, processedBy);
            ps.setInt(3, requestId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRequest(Integer id) throws Exception {
        String sql = "DELETE FROM request WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<RequestModal> searchRequests(String searchTerm) throws Exception {
        List<RequestModal> requests = new ArrayList<>();
        String sql = "SELECT * FROM request WHERE description LIKE ? OR type LIKE ? ORDER BY createdAt DESC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RequestModal request = mapResultSetToRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public int getRequestCountByStatus(Status status) throws Exception {
        String sql = "SELECT COUNT(*) FROM request WHERE status = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private RequestModal mapResultSetToRequest(ResultSet rs) throws Exception {
        RequestModal request = new RequestModal();

        request.setId(rs.getInt("id"));
        request.setRequestBy(rs.getInt("requestBy"));

        Integer processedBy = rs.getObject("processedBy", Integer.class);
        request.setProcessedBy(processedBy);

        request.setType(RequestModal.ReqType.valueOf(rs.getString("type")));
        request.setDescription(rs.getString("description"));
        request.setStatus(Status.valueOf(rs.getString("status")));

        Integer sectionId = rs.getObject("sectionId", Integer.class);
        request.setSectionId(sectionId);

        Integer courseId = rs.getObject("courseId", Integer.class);
        request.setCourseId(courseId);

        Integer fromCourseId = rs.getObject("fromCourseId", Integer.class);
        request.setFromCourseId(fromCourseId);

        Integer toCourseId = rs.getObject("toCourseId", Integer.class);
        request.setToCourseId(toCourseId);

        request.setCreatedAt(rs.getObject("createdAt", LocalDateTime.class));
        request.setUpdatedAt(rs.getObject("updatedAt", LocalDateTime.class));

        return request;
    }
        
    private RequestDTO mapResultSetToRequestDTO(ResultSet rs) throws Exception {
        RequestDTO request = new RequestDTO();

        request.setId(rs.getInt("id"));
        request.setRequestBy(rs.getInt("requestBy"));

        request.setRequestByName(rs.getString("name"));

        Integer processedBy = rs.getObject("processedBy", Integer.class);
        request.setProcessedBy(processedBy);

        request.setType(RequestDTO.ReqType.valueOf(rs.getString("type")));
        request.setDescription(rs.getString("description"));
        request.setStatus(RequestDTO.Status.valueOf(rs.getString("status")));

        Integer sectionId = rs.getObject("sectionId", Integer.class);
        request.setSectionId(sectionId);

        Integer courseId = rs.getObject("courseId", Integer.class);
        request.setCourseId(courseId);

        Integer fromCourseId = rs.getObject("fromCourseId", Integer.class);
        request.setFromCourseId(fromCourseId);

        Integer toCourseId = rs.getObject("toCourseId", Integer.class);
        request.setToCourseId(toCourseId);

        request.setCreatedAt(rs.getObject("createdAt", LocalDateTime.class));
        request.setUpdatedAt(rs.getObject("updatedAt", LocalDateTime.class));

        return request;
    }

    public List<Map<String, Object>> getRequestsWithAccountBySectionAndCourse(int sectionId, int courseId) throws Exception {
        List<Map<String, Object>> requests = new ArrayList<>();
        String sql = "SELECT r.*, a.name AS requesterName FROM request r JOIN account a ON r.requestBy = a.id WHERE r.sectionId = ? AND r.courseId = ? ORDER BY r.createdAt DESC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sectionId);
            ps.setInt(2, courseId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> req = new HashMap<>();
                req.put("id", rs.getInt("id"));
                req.put("type", rs.getString("type"));
                req.put("description", rs.getString("description"));
                req.put("status", rs.getString("status"));
                req.put("requesterName", rs.getString("requesterName"));
                req.put("createdAt", rs.getTimestamp("createdAt").toLocalDateTime());
                requests.add(req);
            }
        }

        return requests;
    }

    public void updateStatus(int requestId, RequestModal.Status status) throws Exception {
        String sql = "UPDATE request SET status = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ?";
        try (
                Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name()); // enum name: PENDING, ACCEPTED, REJECTED
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Lỗi khi cập nhật trạng thái request: " + e.getMessage(), e);
        }
    }

    public boolean processRequest(int id) throws Exception {
        RequestModal req = getRequestById(id);
        if (req == null || req.getStatus() != Status.pending) {
            return false;
        }

        // Thực hiện business logic đổi khoá học ở đây
        // ...
        // Giả sử xử lý OK
        updateStatus(id, Status.accepted); // hoặc REJECTED nếu có lỗi
        return true;
    }

}

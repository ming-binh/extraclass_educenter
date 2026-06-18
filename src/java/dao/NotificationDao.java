/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import java.lang.*;
import java.sql.*;
import java.util.*;
import modal.NotificationModal;
import utils.DBUtil;

/**
 *
 * @author Minh Thu
 */
public class NotificationDao {

    public void addNotification(int accountId, String description) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO notification (accountId, description, is_read, created_at) VALUES (?, ?, 0, NOW())")) {
            ps.setInt(1, accountId);
            ps.setString(2, description);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNotification(List<Integer> accountIds, String description) {
        for (int accountId : accountIds) {
            this.addNotification(accountId, description);
        }
    }

    public boolean addNotificationToMultipleUsers(List<Integer> accountIds, String description) {
        String sql = "INSERT INTO notifications (account_id, description, is_read, created_at) VALUES (?, ?, false, NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Integer accountId : accountIds) {
                ps.setInt(1, accountId);
                ps.setString(2, description);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<NotificationModal> getNotificationsByAccountId(int accountId) {
        List<NotificationModal> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM notification WHERE accountId = ? ORDER BY created_at DESC")) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NotificationModal n = new NotificationModal(
                    rs.getInt("id"),
                    rs.getInt("accountId"),
                    rs.getString("description"),
                    rs.getBoolean("is_read"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                list.add(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<NotificationModal> getUnreadNotificationsByAccountId(int accountId) {
        List<NotificationModal> list = new ArrayList<>();
        String sql = "SELECT id, account_id, description, is_read, created_at FROM notifications WHERE account_id = ? AND is_read = false ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new NotificationModal(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("description"),
                        rs.getBoolean("is_read"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markAllAsReadByAccountId(int accountId) {
        String sql = "UPDATE notifications SET is_read = true WHERE account_id = ? AND is_read = false";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNotification(int id) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countUnreadNotifications(int accountId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE account_id = ? AND is_read = false";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public NotificationModal getNotificationById(int id) {
        String sql = "SELECT id, account_id, description, is_read, created_at FROM notifications WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NotificationModal(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("description"),
                        rs.getBoolean("is_read"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> getStaffAccountIds() {
        List<Integer> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM account WHERE role = 'staff' AND status = 'active'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modal.AccountModal;
import utils.DBUtil;
import utils.HashUtils;


/**
 *
 * @author ASUS
 */
public class AccountDAO {

    // Ánh xạ ResultSet thành AccountModal
    private AccountModal mapResultSetToAccount(ResultSet rs) throws SQLException {
        AccountModal acc = new AccountModal();

        acc.setId(rs.getInt("id"));
        acc.setName(rs.getString("name"));
        acc.setUsername(rs.getString("username"));
        acc.setPhone(rs.getString("phone"));
        acc.setPassword(rs.getString("password"));
        acc.setAddress(rs.getString("address"));
        acc.setAvatarURL(rs.getString("avatarUrl"));

        Timestamp dobTs = rs.getTimestamp("dob");
        if (dobTs != null) {
            acc.setDob(dobTs.toLocalDateTime());
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            acc.setStatus(AccountModal.Status.valueOf(statusStr));
        }

        String roleStr = rs.getString("role");
        if (roleStr != null) {
            acc.setRole(AccountModal.Role.valueOf(roleStr));
        }

        Timestamp createdAtTs = rs.getTimestamp("created_at");
        if (createdAtTs != null) {
            acc.setCreatedAt(createdAtTs.toLocalDateTime());
        }

        Timestamp updatedAtTs = rs.getTimestamp("updated_at");
        if (updatedAtTs != null) {
            acc.setUpdatedAt(updatedAtTs.toLocalDateTime());
        }

        return acc;
    }

 public List<AccountModal> getAllAccounts() throws Exception {
        List<AccountModal> list = new ArrayList<>();

        String sql = "SELECT id, name, phone, password, dob, address, avatarURL, status, role, created_at, updated_at FROM account";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AccountModal acc = new AccountModal();

                acc.setId(rs.getInt("id"));
                acc.setName(rs.getString("name"));
                acc.setPhone(rs.getString("phone"));
                acc.setPassword(rs.getString("password"));

                Timestamp dob = rs.getTimestamp("dob");
                if (dob != null) {
                    acc.setDob(dob.toLocalDateTime());
                }

                acc.setAddress(rs.getString("address"));
                acc.setAvatarURL(rs.getString("avatarURL"));

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    acc.setStatus(AccountModal.Status.valueOf(statusStr));
                }

                String roleStr = rs.getString("role");
                if (roleStr != null) {
                    acc.setRole(AccountModal.Role.valueOf(roleStr));
                }

                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    acc.setCreatedAt(createdAt.toLocalDateTime());
                }

                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    acc.setUpdatedAt(updatedAt.toLocalDateTime());
                }

                list.add(acc);
            }
        }

        return list;
    }
        
        
    public AccountModal checkLogin(String identifier, String plainPassword) {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, identifier);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (HashUtils.checkPassword(plainPassword, storedHash)) {
                    return mapResultSetToAccount(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AccountModal getAccountById(int id) throws Exception {
        String sql = "SELECT * FROM account WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        }
        return null;
    }

    public AccountModal getAccountByPhone(String phone) throws Exception {
        String sql = "SELECT * FROM account WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        }
        return null;
    }

    public AccountModal getAccountByUsername(String username) throws Exception {
        String sql = "SELECT * FROM account WHERE username = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        }
        return null;
    }
    
        /**
     * Lấy danh sách tất cả tài khoản theo vai trò chỉ định, chỉ lấy các tài
     * khoản đang hoạt động.
     *
     * @param role Vai trò của tài khoản cần lọc (ví dụ: "teacher", "student",
     * "admin", ...)
     * @return Danh sách các đối tượng AccountModal có vai trò tương ứng và
     * trạng thái "activated"
     */
    public List<AccountModal> getAllAccountByRole(String role) {
        List<AccountModal> accountList = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE role = ? AND status = 'active'";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql)) {

            pre.setString(1, role);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    accountList.add(mapResultSetToAccount(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public boolean isPhoneExist(String phone) throws Exception {
        String sql = "SELECT COUNT(*) FROM account WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean isUsernameExist(String username) throws Exception {
        String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
     public void updateAccount(AccountModal account) throws Exception {
        String sql = "UPDATE account SET name = ?,address = ?, dob = ?, avatarURL = ? , phone=? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getName());
            ps.setString(2, account.getAddress());
            if (account.getDob() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(account.getDob()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }
            ps.setString(4, account.getAvatarURL());
            ps.setString(5, account.getPhone());
            ps.setInt(6, account.getId());
            ps.executeUpdate();
        }
    }


    public boolean updatePassword(String username, String newHashedPassword) throws Exception {
        String sql = "UPDATE account SET password = ?, updated_at = ? WHERE username = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHashedPassword);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, username);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateAvatar(String username, String avatarUrl) {
        String sql = "UPDATE account SET avatarUrl = ?, updated_at = ? WHERE username = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, avatarUrl);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, username);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProfile(AccountModal account) {
        String sql = "UPDATE account SET name = ?, dob = ?, address = ?, updated_at = ? WHERE username = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getName());
            ps.setTimestamp(2, account.getDob() != null ? Timestamp.valueOf(account.getDob()) : null);
            ps.setString(3, account.getAddress());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, account.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertAccount(AccountModal acc) {
        String sql = "INSERT INTO account (name, username, phone, password, dob, address, avatarURL, status, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, acc.getName());
            ps.setString(2, acc.getUsername());
            ps.setString(3, acc.getPhone());
            ps.setString(4, acc.getPassword());
            ps.setTimestamp(5, acc.getDob() != null ? Timestamp.valueOf(acc.getDob()) : null);
            ps.setString(6, acc.getAddress());
            ps.setString(7, acc.getAvatarURL());
            ps.setString(8, acc.getStatus().name());
            ps.setString(9, acc.getRole().name());
            ps.setTimestamp(10, Timestamp.valueOf(acc.getCreatedAt()));
            ps.setTimestamp(11, Timestamp.valueOf(acc.getUpdatedAt()));

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi insert account: " + e.getMessage(), e);
        }
        return -1;
    }
    
        public List<AccountModal> getAllAccountByRoleNotFilter(String role) {
        List<AccountModal> accountList = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE role = ?";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql)) {

            pre.setString(1, role);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    accountList.add(mapResultSetToAccount(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountList;
    }

            public boolean updatePasswordById(int id, String newHashedPassword) throws Exception {
        String sql = "UPDATE account SET password = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHashedPassword);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        }
    }

                public void updateStatusById(int id, String newStatus) throws Exception {
        String sql = "UPDATE account SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM account WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

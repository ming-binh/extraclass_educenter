/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import modal.PaymentInfoModal;
import utils.DBUtil;

/**
 *
 * @author Astersa
 */
public class PaymentInfoDAO {
    
    public List<PaymentInfoModal> getAllPaymentInfo() throws Exception {
        List<PaymentInfoModal> paymentInfos = new ArrayList<>();
        String sql = "SELECT * FROM payment_info ORDER BY order_index ASC, created_at DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                PaymentInfoModal paymentInfo = new PaymentInfoModal();
                paymentInfo.setId(rs.getInt("id"));
                paymentInfo.setBankName(rs.getString("bank_name"));
                paymentInfo.setAccountNumber(rs.getString("account_number"));
                paymentInfo.setAccountName(rs.getString("account_name"));
                paymentInfo.setBranch(rs.getString("branch"));
                paymentInfo.setSwiftCode(rs.getString("swift_code"));
                paymentInfo.setQrCodeUrl(rs.getString("qr_code_url"));
                paymentInfo.setIsActive(rs.getBoolean("is_active"));
                paymentInfo.setOrderIndex(rs.getInt("order_index"));
                paymentInfo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                paymentInfo.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                paymentInfos.add(paymentInfo);
            }
        }
        return paymentInfos;
    }
    
    public PaymentInfoModal getActivePaymentInfo() {
        PaymentInfoModal info = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_info WHERE is_active = 1 LIMIT 1")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = new PaymentInfoModal(
                    rs.getInt("id"),
                    rs.getString("bank_name"),
                    rs.getString("account_number"),
                    rs.getString("account_name"),
                    rs.getString("branch"),
                    rs.getString("swift_code"),
                    rs.getString("qr_code_url"),
                    rs.getBoolean("is_active"),
                    rs.getInt("order_index"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
    
    public PaymentInfoModal getPaymentInfoById(int id) throws Exception {
        String sql = "SELECT * FROM payment_info WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PaymentInfoModal paymentInfo = new PaymentInfoModal();
                    paymentInfo.setId(rs.getInt("id"));
                    paymentInfo.setBankName(rs.getString("bank_name"));
                    paymentInfo.setAccountNumber(rs.getString("account_number"));
                    paymentInfo.setAccountName(rs.getString("account_name"));
                    paymentInfo.setBranch(rs.getString("branch"));
                    paymentInfo.setSwiftCode(rs.getString("swift_code"));
                    paymentInfo.setQrCodeUrl(rs.getString("qr_code_url"));
                    paymentInfo.setIsActive(rs.getBoolean("is_active"));
                    paymentInfo.setOrderIndex(rs.getInt("order_index"));
                    paymentInfo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    paymentInfo.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return paymentInfo;
                }
            }
        }
        return null;
    }
    
    public int createPaymentInfo(PaymentInfoModal paymentInfo) throws Exception {
        String sql = "INSERT INTO payment_info (bank_name, account_number, account_name, branch, swift_code, qr_code_url, is_active, order_index, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, paymentInfo.getBankName());
            ps.setString(2, paymentInfo.getAccountNumber());
            ps.setString(3, paymentInfo.getAccountName());
            ps.setString(4, paymentInfo.getBranch());
            ps.setString(5, paymentInfo.getSwiftCode());
            ps.setString(6, paymentInfo.getQrCodeUrl());
            ps.setBoolean(7, paymentInfo.getIsActive());
            ps.setInt(8, paymentInfo.getOrderIndex());
            ps.setTimestamp(9, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(10, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }
    
    public boolean updatePaymentInfo(PaymentInfoModal paymentInfo) throws Exception {
        String sql = "UPDATE payment_info SET bank_name = ?, account_number = ?, account_name = ?, branch = ?, swift_code = ?, qr_code_url = ?, is_active = ?, order_index = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, paymentInfo.getBankName());
            ps.setString(2, paymentInfo.getAccountNumber());
            ps.setString(3, paymentInfo.getAccountName());
            ps.setString(4, paymentInfo.getBranch());
            ps.setString(5, paymentInfo.getSwiftCode());
            ps.setString(6, paymentInfo.getQrCodeUrl());
            ps.setBoolean(7, paymentInfo.getIsActive());
            ps.setInt(8, paymentInfo.getOrderIndex());
            ps.setTimestamp(9, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(10, paymentInfo.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deletePaymentInfo(int id) throws Exception {
        String sql = "DELETE FROM payment_info WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean setActivePaymentInfo(int id) throws Exception {
        String deactivateSql = "UPDATE payment_info SET is_active = false, updated_at = ?";
        String activateSql = "UPDATE payment_info SET is_active = true, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(deactivateSql)) {
                ps.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                ps.executeUpdate();
            }
            
            try (PreparedStatement ps = conn.prepareStatement(activateSql)) {
                ps.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(2, id);
                return ps.executeUpdate() > 0;
            }
        }
    }
    
    public boolean updatePaymentInfoQrCode(int id, String qrCodeUrl) throws Exception {
        String sql = "UPDATE payment_info SET qr_code_url = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, qrCodeUrl);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);
            
            return ps.executeUpdate() > 0;
        }
    }
} 
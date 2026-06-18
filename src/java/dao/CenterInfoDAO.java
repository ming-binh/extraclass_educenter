/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import modal.CenterInfoModal;
import utils.DBUtil;

/**
 *
 * @author Astersa
 */
public class CenterInfoDAO {
    
    public CenterInfoModal getCenterInfo() throws Exception {
        String sql = "SELECT * FROM center_info LIMIT 1";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                CenterInfoModal centerInfo = new CenterInfoModal();
                centerInfo.setId(rs.getInt("id"));
                centerInfo.setCenterName(rs.getString("center_name"));
                centerInfo.setAddress(rs.getString("address"));
                centerInfo.setPhone(rs.getString("phone"));
                centerInfo.setEmail(rs.getString("email"));
                centerInfo.setWebsite(rs.getString("website"));
                centerInfo.setDescription(rs.getString("description"));
                centerInfo.setLogoUrl(rs.getString("logo_url"));
                centerInfo.setWorkingHours(rs.getString("working_hours"));
                centerInfo.setFacebook(rs.getString("facebook"));
                centerInfo.setYoutube(rs.getString("youtube"));
                centerInfo.setInstagram(rs.getString("instagram"));
                centerInfo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                centerInfo.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                return centerInfo;
            }
        }
        return null;
    }
    
    public int createCenterInfo(CenterInfoModal centerInfo) throws Exception {
        String sql = "INSERT INTO center_info (center_name, address, phone, email, website, description, logo_url, working_hours, facebook, youtube, instagram, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, centerInfo.getCenterName());
            ps.setString(2, centerInfo.getAddress());
            ps.setString(3, centerInfo.getPhone());
            ps.setString(4, centerInfo.getEmail());
            ps.setString(5, centerInfo.getWebsite());
            ps.setString(6, centerInfo.getDescription());
            ps.setString(7, centerInfo.getLogoUrl());
            ps.setString(8, centerInfo.getWorkingHours());
            ps.setString(9, centerInfo.getFacebook());
            ps.setString(10, centerInfo.getYoutube());
            ps.setString(11, centerInfo.getInstagram());
            ps.setTimestamp(12, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(13, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            
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
    
    public boolean updateCenterInfo(CenterInfoModal centerInfo) throws Exception {
        String sql = "UPDATE center_info SET center_name = ?, address = ?, phone = ?, email = ?, website = ?, description = ?, logo_url = ?, working_hours = ?, facebook = ?, youtube = ?, instagram = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, centerInfo.getCenterName());
            ps.setString(2, centerInfo.getAddress());
            ps.setString(3, centerInfo.getPhone());
            ps.setString(4, centerInfo.getEmail());
            ps.setString(5, centerInfo.getWebsite());
            ps.setString(6, centerInfo.getDescription());
            ps.setString(7, centerInfo.getLogoUrl());
            ps.setString(8, centerInfo.getWorkingHours());
            ps.setString(9, centerInfo.getFacebook());
            ps.setString(10, centerInfo.getYoutube());
            ps.setString(11, centerInfo.getInstagram());
            ps.setTimestamp(12, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(13, centerInfo.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean updateCenterInfoLogo(int id, String logoUrl) throws Exception {
        String sql = "UPDATE center_info SET logo_url = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, logoUrl);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);
            
            return ps.executeUpdate() > 0;
        }
    }
} 
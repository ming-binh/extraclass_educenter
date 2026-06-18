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
import java.util.ArrayList;
import java.util.List;
import modal.BannerModal;
import utils.DBUtil;

/**
 *
 * @author Astersa
 */
public class BannerDAO {
    
    public List<BannerModal> getAllBanners() throws Exception {
        List<BannerModal> banners = new ArrayList<>();
        String sql = "SELECT * FROM banners ORDER BY order_index ASC, created_at DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                BannerModal banner = new BannerModal();
                banner.setId(rs.getInt("id"));
                banner.setTitle(rs.getString("title"));
                banner.setDescription(rs.getString("description"));
                banner.setImageUrl(rs.getString("image_url"));
                banner.setOrderIndex(rs.getInt("order_index"));
                banner.setIsActive(rs.getBoolean("is_active"));
                banner.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                banner.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                banners.add(banner);
            }
        }
        return banners;
    }
    
    public List<BannerModal> getActiveBanners() throws Exception {
        List<BannerModal> banners = new ArrayList<>();
        String sql = "SELECT * FROM banners WHERE is_active = true ORDER BY order_index ASC, created_at DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                BannerModal banner = new BannerModal();
                banner.setId(rs.getInt("id"));
                banner.setTitle(rs.getString("title"));
                banner.setDescription(rs.getString("description"));
                banner.setImageUrl(rs.getString("image_url"));
                banner.setOrderIndex(rs.getInt("order_index"));
                banner.setIsActive(rs.getBoolean("is_active"));
                banner.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                banner.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                banners.add(banner);
            }
        }
        return banners;
    }
    
    public BannerModal getBannerById(int id) throws Exception {
        String sql = "SELECT * FROM banners WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BannerModal banner = new BannerModal();
                    banner.setId(rs.getInt("id"));
                    banner.setTitle(rs.getString("title"));
                    banner.setDescription(rs.getString("description"));
                    banner.setImageUrl(rs.getString("image_url"));
                    banner.setOrderIndex(rs.getInt("order_index"));
                    banner.setIsActive(rs.getBoolean("is_active"));
                    banner.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    banner.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return banner;
                }
            }
        }
        return null;
    }
    
    public int createBanner(BannerModal banner) throws Exception {
        String sql = "INSERT INTO banners (title, description, image_url, order_index, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, banner.getTitle());
            ps.setString(2, banner.getDescription());
            ps.setString(3, banner.getImageUrl());
            ps.setInt(4, banner.getOrderIndex());
            ps.setBoolean(5, banner.getIsActive());
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            
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
    
    public boolean updateBanner(BannerModal banner) throws Exception {
        String sql = "UPDATE banners SET title = ?, description = ?, image_url = ?, order_index = ?, is_active = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, banner.getTitle());
            ps.setString(2, banner.getDescription());
            ps.setString(3, banner.getImageUrl());
            ps.setInt(4, banner.getOrderIndex());
            ps.setBoolean(5, banner.getIsActive());
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(7, banner.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteBanner(int id) throws Exception {
        String sql = "DELETE FROM banners WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean toggleBannerStatus(int id, boolean isActive) throws Exception {
        String sql = "UPDATE banners SET is_active = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBoolean(1, isActive);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);
            
            return ps.executeUpdate() > 0;
        }
    }
} 
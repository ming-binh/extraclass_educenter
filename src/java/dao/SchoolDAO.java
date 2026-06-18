/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import modal.SchoolModal;
import utils.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public class SchoolDAO {

    private SchoolModal mapResultSetToSchool(ResultSet rs) throws SQLException {
        SchoolModal school = new SchoolModal();
        school.setId(rs.getInt("id"));
        school.setName(rs.getString("name"));
        school.setAddress(rs.getString("address"));
        school.setPhone(rs.getString("phone"));
        school.setSchoolType(SchoolModal.SchoolType.valueOf(rs.getString("school_type")));
        school.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        school.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));

        return school;
    }

    /**
     * Lấy danh sách tất cả khóa học, sắp xếp theo id tăng dần. (For admin)
     *
     * @return
     */
    public List<SchoolModal> getAllSchools() {
        List<SchoolModal> schoolList = new ArrayList<>();
        String sql = "SELECT * FROM school ORDER BY id";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql); ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {
                schoolList.add(mapResultSetToSchool(rs));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return schoolList;
    }


    public Map<Integer, String> getIdNameMap() {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT id, name FROM school";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}

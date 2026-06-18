/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import utils.DBUtil;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import modal.SchoolClass;
/**
 *
 * @author ASUS
 */
public class SchoolClassDAO {
    
    public List<SchoolClass> getAllSchoolClasses() throws Exception {
        List<SchoolClass> list = new ArrayList<>();

        String sql = "SELECT id, schoolId, class_name, grade, academic_year, created_at, updated_at FROM school_class";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SchoolClass sc = new SchoolClass();

                sc.setId(rs.getInt("id"));
                sc.setSchoolId(rs.getInt("schoolId"));
                sc.setClassName(rs.getString("className"));
                sc.setGrade(rs.getString("grade"));
                sc.setAcademicYear(rs.getString("academic_year"));

                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                if (createdAt != null) {
                    sc.setCreatedAt(createdAt.toLocalDateTime());
                }
                if (updatedAt != null) {
                    sc.setUpdatedAt(updatedAt.toLocalDateTime());
                }

                list.add(sc);
            }
        }

        return list;
    }

    
    public Map<Integer, List<SchoolClass>> getMapSchoolClass() {
    Map<Integer, List<SchoolClass>> map = new HashMap<>();
    String sql = "SELECT * FROM school_class";
    try (Connection con = DBUtil.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            SchoolClass sc = new SchoolClass();
            sc.setId(rs.getInt("id"));
            sc.setSchoolId(rs.getInt("schoolId"));
            sc.setClassName(rs.getString("className"));
            sc.setGrade(rs.getString("grade"));
            sc.setAcademicYear(rs.getString("academic_year"));
            sc.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            sc.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            map.computeIfAbsent(sc.getSchoolId(), k -> new ArrayList<>()).add(sc);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return map;
}

}

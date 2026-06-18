package dao;

import java.math.BigDecimal;
import modal.PaymentModal;
import utils.DBUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentDAO {
    public void addPayment(PaymentModal payment) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO payments (amount, payment_date, student_id, course_id, section_id, payment_type, description, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())")) {
            ps.setBigDecimal(1, payment.getAmount());
            ps.setTimestamp(2, Timestamp.valueOf(payment.getPaymentDate()));
            ps.setInt(3, payment.getStudentId());
            ps.setInt(4, payment.getCourseId());
            if (payment.getSectionId() != null) {
                ps.setInt(5, payment.getSectionId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, payment.getPaymentType().name());
            ps.setString(7, payment.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<PaymentModal> getPendingPayments() {
        List<PaymentModal> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment WHERE status = 'pending'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PaymentModal p = new PaymentModal(
                    rs.getInt("id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("payment_date").toLocalDateTime(),
                    rs.getInt("student_id"),
                    rs.getInt("course_id"),
                    rs.getObject("section_id") != null ? rs.getInt("section_id") : null,
                    PaymentModal.PaymentType.valueOf(rs.getString("payment_type")),
                    rs.getString("description"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateStatus(int paymentId, String status) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE payment SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PaymentModal getPaymentById(int paymentId) {
        PaymentModal p = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment WHERE id = ?")) {
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new PaymentModal(
                    rs.getInt("id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("payment_date").toLocalDateTime(),
                    rs.getInt("student_id"),
                    rs.getInt("course_id"),
                    rs.getObject("section_id") != null ? rs.getInt("section_id") : null,
                    PaymentModal.PaymentType.valueOf(rs.getString("payment_type")),
                    rs.getString("description"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public BigDecimal getRevenueByDateRange(LocalDateTime start, LocalDateTime end) {
    String sql = "SELECT SUM(amount) FROM payments WHERE payment_date BETWEEN ? AND ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setTimestamp(1, Timestamp.valueOf(start));
        ps.setTimestamp(2, Timestamp.valueOf(end));
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBigDecimal(1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return BigDecimal.ZERO;
}

public BigDecimal getTotalRevenue() {
    String sql = "SELECT SUM(amount) FROM payments";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            BigDecimal total = rs.getBigDecimal(1);
            return total != null ? total : BigDecimal.ZERO;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return BigDecimal.ZERO;
}
} 
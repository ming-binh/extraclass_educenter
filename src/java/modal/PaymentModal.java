/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author Astersa
 */
public class PaymentModal {
     private Integer id;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private Integer studentId;
    private Integer courseId;
    private Integer sectionId;
    private PaymentType paymentType;
    private String description;
    private LocalDateTime createdAt;

    public enum PaymentType {
        course_fee, section_fee, other
    }

    public PaymentModal() {
    }

    public PaymentModal(Integer id, BigDecimal amount, LocalDateTime paymentDate, Integer studentId, Integer courseId, Integer sectionId, PaymentType paymentType, String description, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.studentId = studentId;
        this.courseId = courseId;
        this.sectionId = sectionId;
        this.paymentType = paymentType;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getSectionId() {
        return sectionId;
    }
    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

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
public class StudentPaymentScheduleModal {
    private Integer id;
    private Integer studentSectionId;
    private BigDecimal amount;
    private LocalDateTime dueDate;
    private Boolean markPaying; 
    private Boolean isPaid;
    private LocalDateTime paidDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer courseId;

    public StudentPaymentScheduleModal() {
    }

    public StudentPaymentScheduleModal(Integer id, Integer studentSectionId, BigDecimal amount, LocalDateTime dueDate, Boolean markPaying,Boolean isPaid, LocalDateTime paidDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentSectionId = studentSectionId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.markPaying = markPaying;
        this.isPaid = isPaid;
        this.paidDate = paidDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentSectionId() {
        return studentSectionId;
    }

    public void setStudentSectionId(Integer studentSectionId) {
        this.studentSectionId = studentSectionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getMarkPaying() {
        return markPaying;
    }

    public void setMarkPaying(Boolean isPaid) {
        this.markPaying = markPaying;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getCourseId() {
        return courseId;
    }
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}

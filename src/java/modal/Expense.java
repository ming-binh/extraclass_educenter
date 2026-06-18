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
public class Expense {
    private Integer id;
    private String description;
    private BigDecimal amount;
    private Integer approvedBy; // managerId
    private LocalDateTime approvalDate;
    private LocalDateTime createdAt;

    public Expense() {
    }

    public Expense(Integer id, String description, BigDecimal amount, Integer approvedBy, LocalDateTime approvalDate, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.approvedBy = approvedBy;
        this.approvalDate = approvalDate;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}

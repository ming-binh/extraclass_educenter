/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.time.LocalDateTime;

/**
 *
 * @author Astersa
 */
public class NotificationModal {
    private Integer id;

    private Integer accountId;
    private String description;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationModal() {
    }

    public NotificationModal(Integer id, Integer accountId, String description, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.description = description;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "NotificationModal{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", description='" + description + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }

}

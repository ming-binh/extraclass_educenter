/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.time.LocalDateTime;

/**
 *
 * @author hungd
 */
public class TeacherAchivementModal {

    private Integer id;

    private Integer teacherId;
    private String imageURL;
    private String achivementName;
    private LocalDateTime issuedDate;
    private LocalDateTime createdAt;

    public TeacherAchivementModal() {
    }

    public TeacherAchivementModal(Integer id, Integer teacherId, String imageURL, String achivementName, LocalDateTime issuedDate, LocalDateTime createdAt) {
        this.id = id;
        this.teacherId = teacherId;
        this.imageURL = imageURL;
        this.achivementName = achivementName;
        this.issuedDate = issuedDate;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getAchivementName() {
        return achivementName;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setAchivementName(String achivementName) {
        this.achivementName = achivementName;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

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
public class TeacherModal {
    private Integer id;
    private Integer accountId;
    private Integer schoolId;
    private Integer schoolClassId;
    private String experience;
    private Subject subject;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

        public enum Subject {
        Mathematics("Toán học"),
        Literature("Văn học"),
        English("Tiếng Anh"),
        Physics("Vật lý"),
        Chemistry("Hóa học"),
        Biology("Sinh học"),
        History("Lịch sử"),
        Geography("Địa lý"),
        Civic_Education("Giáo dục công dân"),
        Informatics("Tin học");

        private final String displayName;

        Subject(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    
    public TeacherModal() {
    }

    public TeacherModal(Integer id, Integer accountId, Integer schoolId, Integer schoolClassId, String experience, Subject subject, String bio, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.accountId = accountId;
        this.schoolId = schoolId;
        this.schoolClassId = schoolClassId;
        this.experience = experience;
        this.subject = subject;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getExperienceNumber() {
        if (experience == null) {
            return "";
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\d+").matcher(experience);
        return matcher.find() ? matcher.group() : "";
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

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(Integer schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

   
}

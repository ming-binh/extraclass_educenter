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
public class ConsultationModal {

    private Integer id;
    private String name;
    private String email;
    private LocalDateTime dob;
    private String phone;
    private Status status;
    private String address;
    private Subject subject;
    private String experience;
    private Integer schoolId;
    private Integer schoolClassId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        pending, accepted, rejected
    }

    public ConsultationModal() {
    }

    public ConsultationModal(Integer id, String name, LocalDateTime dob, String phone, Status status, String address, Subject subject, String experience, Integer schoolId, Integer schoolClassId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.status = status;
        this.address = address;
        this.subject = subject;
        this.experience = experience;
        this.schoolId = schoolId;
        this.schoolClassId = schoolClassId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ConsultationModal(Integer id, String name, String email, LocalDateTime dob, String phone, Status status, String address, Subject subject, String experience, Integer schoolId, Integer schoolClassId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.status = status;
        this.address = address;
        this.subject = subject;
        this.experience = experience;
        this.schoolId = schoolId;
        this.schoolClassId = schoolClassId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public enum Subject {
        Mathematics("Toán học"),
        Literature("Ngữ văn"),
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;
import modal.ConsultationModal;

/**
 *
 * @author Admin
 */
public class ConsultationDTO {

    private Integer id;
    private String name;
    private LocalDateTime dob;
    private String phone;
    private Status status;
    private Role role;
    private String address;
    private ConsultationModal.Subject subject; // Thay đổi từ String thành enum
    private String experience;
    private Integer schoolId;
    private Integer schoolClassId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String dobString;
    private Integer certificateId;
    private String certificateImageUrl;
    private String email;

    private String schoolName;
    private String schoolClassName;

    // Enum trạng thái
    public enum Status {
        pending, accepted, rejected
    }

    public enum Role {
        teacher, parent
    }

    public ConsultationDTO() {
    }

    public ConsultationDTO(Integer id, String name, LocalDateTime dob, String phone, Status status, String address, ConsultationModal.Subject subject, String experience, Integer schoolId, Integer schoolClassId, LocalDateTime createdAt, LocalDateTime updatedAt, String schoolName, String schoolClassName, String email) {
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
        this.schoolName = schoolName;
        this.schoolClassName = schoolClassName;
        this.email = email;
    }

    public ConsultationDTO(Integer id, String name, LocalDateTime dob, String phone, Status status, Role role, String address, ConsultationModal.Subject subject, String experience, Integer schoolId, Integer schoolClassId, LocalDateTime createdAt, LocalDateTime updatedAt, String dobString, Integer certificateId, String certificateImageUrl, String email, String schoolName, String schoolClassName) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.status = status;
        this.role = role;
        this.address = address;
        this.subject = subject;
        this.experience = experience;
        this.schoolId = schoolId;
        this.schoolClassId = schoolClassId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dobString = dobString;
        this.certificateId = certificateId;
        this.certificateImageUrl = certificateImageUrl;
        this.email = email;
        this.schoolName = schoolName;
        this.schoolClassName = schoolClassName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getDobString() {
        return dobString;
    }

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateImageUrl() {
        return certificateImageUrl;
    }

    public void setCertificateImageUrl(String certificateImageUrl) {
        this.certificateImageUrl = certificateImageUrl;
    }

    public void setDobString(String dobString) {
        this.dobString = dobString;
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

    public ConsultationModal.Subject getSubject() {
        return subject;
    }

    public void setSubject(ConsultationModal.Subject subject) {
        this.subject = subject;
    }

    // Thêm method helper để lấy display name của subject
    public String getSubjectDisplayName() {
        return subject != null ? subject.getDisplayName() : null;
    }

    public void setSubjectFromString(String subjectStr) {
        if (subjectStr != null && !subjectStr.trim().isEmpty()) {
            try {
                this.subject = ConsultationModal.Subject.valueOf(subjectStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid subject enum value: " + subjectStr);
                this.subject = null;
            }
        } else {
            this.subject = null;
        }
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolClassName() {
        return schoolClassName;
    }

    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
    }
}

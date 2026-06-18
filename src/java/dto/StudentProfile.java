/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author vankh
 */
import java.time.LocalDate;

public class StudentProfile {
    private int id;
    private String name;
    private String phone;
    private LocalDate dob;
    private String address;
    private String avatarUrl;
    private String currentGrade;
    private String schoolName;
    private int schoolId;
    private Status status;

     public enum Status {
        inactive("Ngưng hoạt động"),
        active("Hoạt động"),
        pending("Đang chờ duyệt");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


    public StudentProfile() {
    }

    public StudentProfile(String name, String phone, LocalDate dob, String address, String avatarUrl, String currentGrade, String schoolName, int schoolId, Status status) {
        this.name = name;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.currentGrade = currentGrade;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}

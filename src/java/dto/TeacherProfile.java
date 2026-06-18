/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author vankh
 */
public class TeacherProfile {

    private String name;
    private String schoolName;
    private String experience;
    private String bio;
    private String subject;
    private int schoolId;

    public TeacherProfile() {
    }

    public TeacherProfile(String name, String schoolName, String experience, String bio, String subject, int schoolId) {
        this.name = name;
        this.schoolName = schoolName;
        this.experience = experience;
        this.bio = bio;
        this.subject = subject;
        this.schoolId = schoolId;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

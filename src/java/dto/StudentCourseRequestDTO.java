/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;

/**
 *
 * @author hungd
 */
public class StudentCourseRequestDTO {

    private int requestId;
    private int courseId;
    private String courseName;

    private int studentId;
    private String studentName;

    private boolean isPaid;
    private Status status;
    private isValid validStatus;      // valid / invalid_fees / invalid_schedule / invalid_full

    private LocalDateTime enrollmentDate;

    public enum Status {
        pending, accepted, rejected
    }

    public enum isValid {
        valid, invalid_fees, invalid_schedule, invalid_full
    }

    public StudentCourseRequestDTO() {
    }

    public StudentCourseRequestDTO(int requestId, int courseId, String courseName, int studentId, String studentName, boolean isPaid, Status status, isValid validStatus, LocalDateTime enrollmentDate) {
        this.requestId = requestId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.isPaid = isPaid;
        this.status = status;
        this.validStatus = validStatus;
        this.enrollmentDate = enrollmentDate;
    }

    
    public int getRequestId() {
        return requestId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public boolean isIsPaid() {
        return isPaid;
    }


    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Status getStatus() {
        return status;
    }

    public isValid getValidStatus() {
        return validStatus;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setValidStatus(isValid validStatus) {
        this.validStatus = validStatus;
    }



    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

}

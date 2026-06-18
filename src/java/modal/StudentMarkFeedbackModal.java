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
public class StudentMarkFeedbackModal {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private Integer takeBy;
    private BigDecimal mark;
    private String feedback;
    private LocalDateTime date;
    private String type;
    private LocalDateTime createdAt;

    public StudentMarkFeedbackModal() {
    }

    public StudentMarkFeedbackModal(Integer id, Integer studentId, Integer courseId, Integer takeBy, BigDecimal mark, String feedback, LocalDateTime date, String type, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.takeBy = takeBy;
        this.mark = mark;
        this.feedback = feedback;
        this.date = date;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTakeBy() {
        return takeBy;
    }

    public void setTakeBy(Integer takeBy) {
        this.takeBy = takeBy;
    }

    public BigDecimal getMark() {
        return mark;
    }

    public void setMark(BigDecimal mark) {
        this.mark = mark;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    
}

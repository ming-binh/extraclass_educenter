/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author vankh
 */
public class StudentScoreView {

    private String courseName;
    private BigDecimal mark;
    private String feedback;
    private LocalDateTime date;
    private String type;
    private String formattedDate;

    public StudentScoreView() {
    }

    public StudentScoreView(String courseName, BigDecimal mark, String feedback, LocalDateTime date, String type) {
        this.courseName = courseName;
        this.mark = mark;
        this.feedback = feedback;
        this.date = date;
        this.type = type;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate; // Sửa lại dòng này
    }
}

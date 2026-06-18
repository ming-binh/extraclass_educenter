/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HanND
 */
public class CourseDTO {

    private int id;
    private String name;
    private String subject;
    private String grade;
    private String description;
    private String courseType;
    private BigDecimal feeCombo;
    private BigDecimal feeDaily;
    private Date startDate;
    private Date endDate;
    private int weekAmount;
    private int studentEnrollment;
    private int maxStudents;
    private String level;
    private boolean isHot;
    private BigDecimal discountPercentage;
    private String status;
    private String teacherName;
    private String courseImg;


    public CourseDTO() {
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public BigDecimal getFeeCombo() {
        return feeCombo;
    }

    public void setFeeCombo(BigDecimal feeCombo) {
        this.feeCombo = feeCombo;
    }

    public BigDecimal getFeeDaily() {
        return feeDaily;
    }

    public void setFeeDaily(BigDecimal feeDaily) {
        this.feeDaily = feeDaily;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(int weekAmount) {
        this.weekAmount = weekAmount;
    }

    public int getStudentEnrollment() {
        return studentEnrollment;
    }

    public void setStudentEnrollment(int studentEnrollment) {
        this.studentEnrollment = studentEnrollment;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isIsHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

}

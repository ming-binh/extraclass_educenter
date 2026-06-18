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
public class CourseModal {

    private Integer id;
    private String course_img;
    private Integer teacherId;
    private String name;
    private String description;
    private Status status;
    private CourseType courseType;
    private BigDecimal feeCombo;
    private BigDecimal feeDaily;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer weekAmount;
    private Integer studentEnrollment;
    private Integer maxStudents;
    private Level level;
    private Boolean isHot;
    private Subject subject;
    private String grade;
    private BigDecimal discountPercentage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        activated("Đã đi vào hoạt động"),
        pending("Đang xét duyệt"),
        upcoming("Sắp tới"),
        rejected("Bị loại bỏ"),
        completed("Đã hoàn thành"),
        inactivated("Chưa kích hoạt");
        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

     public enum CourseType {
        combo("Khóa Combo"),
        daily("Khóa Học Hàng Ngày");

        private final String displayName;

        CourseType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Level {
        Foundation("Nền tảng"),
        Basic("Cơ bản"),
        Advanced("Nâng cao"),
        Excellent("Học sinh giỏi"),
        Topics_Exam("Chuyên đề/Luyện thi");

        private final String displayName;

        Level(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

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


    public CourseModal() {
    }

    public CourseModal(Integer id, String course_img, Integer teacherId, String name, String description, Status status, CourseType courseType, BigDecimal feeCombo, BigDecimal feeDaily, LocalDateTime startDate, LocalDateTime endDate, Integer weekAmount, Integer studentEnrollment, Integer maxStudents, Level level, Boolean isHot, Subject subject, String grade, BigDecimal discountPercentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.course_img = course_img;
        this.teacherId = teacherId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.courseType = courseType;
        this.feeCombo = feeCombo;
        this.feeDaily = feeDaily;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekAmount = weekAmount;
        this.studentEnrollment = studentEnrollment;
        this.maxStudents = maxStudents;
        this.level = level;
        this.isHot = isHot;
        this.subject = subject;
        this.grade = grade;
        this.discountPercentage = discountPercentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourse_img() {
        return course_img;
    }

    public void setCourse_img(String course_img) {
        this.course_img = course_img;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(Integer weekAmount) {
        this.weekAmount = weekAmount;
    }

    public Integer getStudentEnrollment() {
        return studentEnrollment;
    }

    public void setStudentEnrollment(Integer studentEnrollment) {
        this.studentEnrollment = studentEnrollment;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
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

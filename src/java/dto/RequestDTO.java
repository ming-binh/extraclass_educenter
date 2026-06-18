/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import dto.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Astersa
 */
public class RequestDTO {

    private Integer id;
    private Integer requestBy;
    private Integer processedBy;
    private ReqType type;
    private String description;
    private Status status;
    private Integer sectionId;
    private Integer courseId;
    private Integer fromCourseId;
    private Integer toCourseId;
    private String requestByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Date getCreatedAtAsDate() {
        if (createdAt == null) return null;
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public enum ReqType {
        STUDENT_CHANGE_COURSE("Xin thay đổi Lớp Học"),
        STUDENT_ABSENT_REQUEST("Xin nghỉ học"),
        TEACHER_ABSENT("Xin nghỉ dạy"),
        TEACHER_CHANGE_SECTION("Xin dạy bù"),
        OTHER("Khác");

        private final String displayName;

        ReqType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Status {
        pending("Đang xử lý"), rejected("Đã từ chối"), accepted("Đã chấp nhận");
        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public RequestDTO() {
    }

    public RequestDTO(Integer id, Integer requestBy, Integer processedBy, ReqType type, String description, Status status, Integer sectionId, Integer courseId, Integer fromCourseId, Integer toCourseId, String requestByName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.requestBy = requestBy;
        this.processedBy = processedBy;
        this.type = type;
        this.description = description;
        this.status = status;
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.fromCourseId = fromCourseId;
        this.toCourseId = toCourseId;
        this.requestByName = requestByName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Integer requestBy) {
        this.requestBy = requestBy;
    }

    public Integer getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Integer processedBy) {
        this.processedBy = processedBy;
    }

    public ReqType getType() {
        return type;
    }

    public void setType(ReqType type) {
        this.type = type;
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

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getFromCourseId() {
        return fromCourseId;
    }

    public void setFromCourseId(Integer fromCourseId) {
        this.fromCourseId = fromCourseId;
    }

    public Integer getToCourseId() {
        return toCourseId;
    }

    public void setToCourseId(Integer toCourseId) {
        this.toCourseId = toCourseId;
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

    public void setRequestByName(String requestByName) {
        this.requestByName = requestByName;
    }

    public String getRequestByName() {
        return requestByName;
    }

    
}

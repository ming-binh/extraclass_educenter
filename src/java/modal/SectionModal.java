/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Astersa
 */
public class SectionModal {

    private Integer id;
    private Integer courseId;
    private DayOfWeekEnum dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String classroom;
    private LocalDateTime dateTime;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String note;
    private int teacherId;

    public enum Status {
        inactive("Chưa diễn ra"),
        active("Đang diễn ra"),
        completed("Đã hoàn thành");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DayOfWeekEnum {
        Monday("Thứ 2"),
        Tuesday("Thứ 3"),
        Wednesday("Thứ 4"),
        Thursday("Thứ 5"),
        Friday("Thứ 6"),
        Saturday("Thứ 7"),
        Sunday("Chủ nhật");

        private final String displayName;

        private DayOfWeekEnum(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public SectionModal() {
    }



    public SectionModal(Integer id, Integer courseId, DayOfWeekEnum dayOfWeek, LocalDateTime startTime, LocalDateTime endTime, String classroom, LocalDateTime dateTime, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, String note, int teacherId) {
        this.id = id;
        this.courseId = courseId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
        this.dateTime = dateTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.note = note;
        this.teacherId = teacherId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public DayOfWeekEnum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getFormattedStartTime() {
        if (startTime != null) {
            return startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    public String getFormattedEndTime() {
        if (endTime != null) {
            return endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    public String getFormattedTimeRange() {
        if (startTime != null && endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return startTime.format(formatter) + " - " + endTime.format(formatter);
        }
        return "";
    }

    public String getFormattedDate() {
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dateTime.format(formatter);
        }
        return "Chưa xác định";
    }

}

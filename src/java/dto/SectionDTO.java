/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.time.LocalDateTime;
import modal.SectionModal;
import modal.SectionModal.DayOfWeekEnum;
import modal.SectionModal.Status;

/**
 *
 * @author HanND
 */
public class SectionDTO {

    private Integer id;
    private Integer courseId;
    private String courseName;
    private DayOfWeekEnum dayOfWeek;
    private String classroom;
    private Status status;
    private String teacherName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime dateTime;
    private String startTimeFormatted;
    private String endTimeFormatted;
    private String dateFormatted;
    private int teacherId;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }


    private SectionModal section;

    public SectionDTO() {
    }

    public SectionDTO(String startTimeFormatted, String endTimeFormatted, String dateFormatted, SectionModal section) {
        this.startTimeFormatted = startTimeFormatted;
        this.endTimeFormatted = endTimeFormatted;
        this.dateFormatted = dateFormatted;
        this.section = section;
    }

    public String getStartTimeFormatted() {
        return startTimeFormatted;
    }

    public void setStartTimeFormatted(String startTimeFormatted) {
        this.startTimeFormatted = startTimeFormatted;
    }

    public String getEndTimeFormatted() {
        return endTimeFormatted;
    }

    public void setEndTimeFormatted(String endTimeFormatted) {
        this.endTimeFormatted = endTimeFormatted;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public DayOfWeekEnum getDayOfWeek() {
        return dayOfWeek;
    }

    public String getClassroom() {
        return classroom;
    }

    public Status getStatus() {
        return status;
    }

    public SectionModal getSection() {
        return section;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSection(SectionModal section) {
        this.section = section;
    }

}

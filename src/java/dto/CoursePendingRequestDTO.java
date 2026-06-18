/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author hungd
 */
public class CoursePendingRequestDTO {

    private int courseId;
    private String courseName;
    private int pendingCount;

    public CoursePendingRequestDTO() {
    }

    public CoursePendingRequestDTO(int courseId, String courseName, int pendingCount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.pendingCount = pendingCount;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

}

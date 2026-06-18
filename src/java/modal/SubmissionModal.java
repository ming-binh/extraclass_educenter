/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modal;

import java.sql.Timestamp;

/**
 *
 * @author HanND
 */
public class SubmissionModal {

    private int id;
    private int sectionAssignmentId;
    private int courseId;
    private int studentId;
    private String filePath;
    private Timestamp submittedAt;
    private String comment;
    private Double grade;
    private String assignmentTitle;
    private String studentName;

    public SubmissionModal() {
    }

    public SubmissionModal(int id, int sectionAssignmentId, int courseId, int studentId, String filePath, Timestamp submittedAt, String comment, Double grade, String assignmentTitle, String studentName) {
        this.id = id;
        this.sectionAssignmentId = sectionAssignmentId;
        this.courseId = courseId;
        this.studentId = studentId;
        this.filePath = filePath;
        this.submittedAt = submittedAt;
        this.comment = comment;
        this.grade = grade;
        this.assignmentTitle = assignmentTitle;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSectionAssignmentId() {
        return sectionAssignmentId;
    }

    public void setSectionAssignmentId(int sectionAssignmentId) {
        this.sectionAssignmentId = sectionAssignmentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

}

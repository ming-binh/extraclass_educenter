package modal;

import java.time.LocalDateTime;

public class StudentSectionModal {

    
    private Integer id;
    private Integer studentId;
    private Integer sectionId;
    private Boolean isPaid;
    private AttendanceStatus attendanceStatus;
    private LocalDateTime createdAt;

public enum AttendanceStatus {
        notyet("Chưa diễn ra buổi học"),
        present("Có mặt"),
        absent("Vắng"),
        excused("Có phép");

        private final String displayName;

        AttendanceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

    }


    public StudentSectionModal() {
    }

    public StudentSectionModal(Integer id, Integer studentId, Integer sectionId, Boolean isPaid,
                                AttendanceStatus attendanceStatus, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.isPaid = isPaid;
        this.attendanceStatus = attendanceStatus;
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

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.SectionDTO;
import dto.StudentSectionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modal.SectionModal;
import modal.SectionModal.DayOfWeekEnum;
import utils.DBUtil;

/**
 *
 * @author hungd
 */
public class SectionDAO extends DBUtil {

    /**
     * Chuyển đổi một dòng dữ liệu từ ResultSet thành đối tượng SectionModal.
     */
    private SectionModal mapResultSet(ResultSet rs) throws SQLException {
        SectionModal section = new SectionModal();
        section.setId(rs.getInt("id"));
        section.setCourseId(rs.getInt("courseId"));
        section.setClassroom(rs.getString("classroom"));
        section.setDayOfWeek(SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")));
        section.setStartTime(rs.getObject("startTime", LocalDateTime.class));
        section.setEndTime(rs.getObject("endTime", LocalDateTime.class));
        section.setDateTime(rs.getObject("dateTime", LocalDateTime.class));
        section.setStatus(SectionModal.Status.valueOf(rs.getString("status")));
        section.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        section.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        section.setTeacherId(rs.getInt("teacherId"));
        section.setNote(rs.getString("note"));

        return section;
    }

    /**
     * Lấy danh sách tất cả lớp học, sắp xếp theo id tăng dần.
     *
     * @return
     */
    public List<SectionModal> getAllSections() {
        List<SectionModal> sectionList = new ArrayList<>();
        String sql = "SELECT * FROM section ORDER BY id";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql); ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {
                sectionList.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sectionList;
    }

    public List<SectionDTO> getAllSectionsByCourse() {
        List<SectionDTO> sectionList = new ArrayList<>();

        String sql = """
           SELECT s.id,
                   c.name AS courseName,
                   s.courseId,
                   s.dayOfWeek,
                   s.startTime,
                   s.endTime,
                   s.classroom,
                   s.dateTime,
                   s.status
            FROM section s
            JOIN course c ON s.courseId = c.id
            ORDER BY s.dateTime ASC;
        """;

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SectionModal section = new SectionModal();
                section.setId(rs.getInt("id"));
                section.setCourseId(rs.getInt("courseId"));
                section.setDayOfWeek(SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")));

                // Các trường thời gian
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                LocalDateTime dateTime = rs.getTimestamp("dateTime").toLocalDateTime();
                LocalDate date = dateTime.toLocalDate();

                LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

                section.setStartTime(startDateTime);
                section.setEndTime(endDateTime);
                section.setDateTime(dateTime);

                section.setClassroom(rs.getString("classroom"));
                section.setStatus(SectionModal.Status.valueOf(rs.getString("status")));

                SectionDTO dto = new SectionDTO();
                dto.setId(section.getId());
                dto.setCourseId(section.getCourseId());
                dto.setDayOfWeek(section.getDayOfWeek());
                dto.setClassroom(section.getClassroom());
                dto.setStatus(section.getStatus());
                dto.setCourseName(rs.getString("courseName"));
                dto.setSection(section);

                dto.setStartTimeFormatted(startTime.format(timeFormatter));
                dto.setEndTimeFormatted(endTime.format(timeFormatter));
                dto.setDateFormatted(date.format(dateFormatter));

                sectionList.add(dto);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sectionList;
    }

    /**
     * Lấy danh sách lớp học của khóa học đó
     *
     * @param courseId
     * @return
     */
    public List<DayOfWeekEnum> getDaysOfWeekForCourse(int courseId) {
        List<DayOfWeekEnum> dayList = new ArrayList<>();
        String sql = "SELECT DISTINCT dayOfWeek FROM section WHERE courseId = ? ORDER BY CASE dayOfWeek "
                + "WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 WHEN 'Thursday' THEN 4 "
                + "WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 WHEN 'Sunday' THEN 7 END";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String dayStr = rs.getString("dayOfWeek");
                    try {
                        dayList.add(DayOfWeekEnum.valueOf(dayStr));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid dayOfWeek in DB: " + dayStr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dayList;
    }

    public List<Map<String, Object>> getTeacherSections(Integer teacherId) throws Exception {
        List<Map<String, Object>> sections = new ArrayList<>();

        String sql = """
            SELECT 
                sec.id,
                sec.courseId,
                c.name as courseName,
                sec.dateTime,
                sec.classroom,
                sec.dayOfWeek,
                sec.startTime,
                sec.endTime
            FROM section sec
            JOIN course c ON sec.courseId = c.id
            WHERE c.teacherId = ? 
            AND sec.dateTime > GETDATE()
            AND c.status = 'activated'
            ORDER BY sec.dateTime ASC
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> section = new HashMap<>();
                    section.put("id", rs.getInt("id"));
                    section.put("courseId", rs.getInt("courseId"));
                    section.put("courseName", rs.getString("courseName"));

                    // Format dateTime
                    Timestamp dateTime = rs.getTimestamp("dateTime");
                    if (dateTime != null) {
                        LocalDateTime localDateTime = dateTime.toLocalDateTime();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        section.put("dateTime", localDateTime.format(formatter));
                    }

                    section.put("classroom", rs.getString("classroom"));
                    section.put("dayOfWeek", rs.getString("dayOfWeek"));
                    section.put("startTime", rs.getTime("startTime"));
                    section.put("endTime", rs.getTime("endTime"));
                    sections.add(section);
                }
            }
        }
        return sections;
    }

    public void updateSection(SectionDTO section) {
        String updateSQL = """
        UPDATE section 
        SET courseId = ?, 
            dayOfWeek = ?, 
            startTime = ?, 
            endTime = ?, 
            classroom = ?, 
            dateTime = ? 
        WHERE id = ?
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setInt(1, section.getCourseId());
            stmt.setString(2, section.getDayOfWeek().toString());
            stmt.setTime(3, Time.valueOf(section.getStartTime().toLocalTime()));
            stmt.setTime(4, Time.valueOf(section.getEndTime().toLocalTime()));
            stmt.setString(5, section.getClassroom());
            stmt.setTimestamp(6, Timestamp.valueOf(section.getDateTime()));
            stmt.setInt(7, section.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cập nhật lớp học thành công.");
            } else {
                System.out.println("Không tìm thấy lớp học để cập nhật (ID không đúng?).");
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật section:");
            e.printStackTrace();
        }

    }

    /**
     * Xóa lớp học dựa trên ID.
     *
     * @param id ID lớp học cần xóa
     */
    public void deleteSection(int id) {
        String sql = "DELETE FROM section WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy chi tiết lớp học bao gồm tên khóa học và tên giáo viên.
     *
     * @param sectionId ID của lớp học
     * @return SectionDTO nếu tìm thấy, null nếu không
     */
    public SectionDTO getSectionDetail(int sectionId) {
        String sql = """
        SELECT s.id, s.dayOfWeek, s.startTime, s.endTime, s.classroom,
               s.dateTime, s.status, c.id AS courseId, c.name AS courseName, 
               a.name AS teacherName
        FROM section s
        JOIN course c ON s.courseId = c.id
        JOIN teacher t ON c.teacherId = t.id
        JOIN account a ON t.accountId = a.id
        WHERE s.id = ?
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sectionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                SectionModal section = new SectionModal();
                section.setId(rs.getInt("id"));
                section.setDayOfWeek(SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")));
                LocalTime startTimeOnly = rs.getTime("startTime").toLocalTime();
                LocalTime endTimeOnly = rs.getTime("endTime").toLocalTime();
                LocalDate date = rs.getTimestamp("dateTime").toLocalDateTime().toLocalDate();

                LocalDateTime startTime = LocalDateTime.of(date, startTimeOnly);
                LocalDateTime endTime = LocalDateTime.of(date, endTimeOnly);

                section.setStartTime(startTime);
                section.setEndTime(endTime);
                section.setDateTime(LocalDateTime.of(date, startTimeOnly));

                section.setClassroom(rs.getString("classroom"));
                section.setStatus(SectionModal.Status.valueOf(rs.getString("status")));

                SectionDTO dto = new SectionDTO();
                dto.setId(section.getId());
                dto.setCourseId(rs.getInt("courseId"));
                dto.setDayOfWeek(section.getDayOfWeek());
                dto.setClassroom(section.getClassroom());
                dto.setStatus(section.getStatus());
                dto.setCourseName(rs.getString("courseName"));
                dto.setTeacherName(rs.getString("teacherName"));
                dto.setSection(section);

                dto.setStartTimeFormatted(startTimeOnly.format(timeFormatter));
                dto.setEndTimeFormatted(endTimeOnly.format(timeFormatter));
                dto.setDateFormatted(date.format(dateFormatter));
                return dto;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách học sinh trong một lớp học cụ thể.
     *
     * @param sectionId ID lớp học
     * @return danh sách StudentSectionDTO
     */
    public List<StudentSectionDTO> getStudentDetailsInSection(int sectionId) {
        List<StudentSectionDTO> list = new ArrayList<>();

        String sql = """
        SELECT st.id AS studentId, a.name AS studentName, 
               ss.isPaid, ss.attendanceStatus
        FROM student_section ss
        JOIN student st ON ss.studentId = st.id
        JOIN account a ON st.accountId = a.id
        WHERE ss.sectionId = ?
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentSectionDTO dto = new StudentSectionDTO();
                dto.setStudentId(rs.getInt("studentId"));
                dto.setStudentName(rs.getString("studentName"));
                dto.setIsPaid(rs.getBoolean("isPaid"));
                String statusStr = rs.getString("attendanceStatus");
                StudentSectionDTO.AttendanceStatus status;
                try {
                    status = StudentSectionDTO.AttendanceStatus.valueOf(statusStr);
                } catch (IllegalArgumentException | NullPointerException e) {
                    status = StudentSectionDTO.AttendanceStatus.notyet;
                }
                dto.setAttendanceStatus(status);

                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Tạo các buổi học (section) lặp lại hàng tuần dựa trên ngày bắt đầu và kết
     * thúc của khóa học. Mỗi buổi học sẽ được thêm mới vào bảng `section` nếu
     * trùng với ngày trong tuần đã chọn.
     *
     * @param courseId ID của khóa học
     * @param dayOfWeek Thứ trong tuần cần tạo lớp
     * @param startTime Giờ bắt đầu lớp học
     * @param endTime Giờ kết thúc lớp học
     * @param classroom Phòng học
     * @param startDate Ngày bắt đầu khóa học
     * @param endDate Ngày kết thúc khóa học
     * @param status Trạng thái của lớp học
     */
    public void addSections(int courseId, String dayOfWeek, LocalTime startTime, LocalTime endTime,
            String classroom, LocalDate startDate, LocalDate endDate, int teacherId) {

        String sql = "INSERT INTO section (courseId, dayOfWeek, startTime, endTime, classroom, dateTime, status, teacherId) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Lấy enum DayOfWeek tương ứng với tham số truyền vào (VD: "MONDAY")
            java.time.DayOfWeek targetDay = java.time.DayOfWeek.valueOf(dayOfWeek.toUpperCase());

            // Bắt đầu từ startDate
            LocalDate date = startDate;

            // Tìm ngày đầu tiên trong tuần khớp với dayOfWeek (VD: thứ 2 gần nhất sau startDate)
            while (date.getDayOfWeek() != targetDay) {
                date = date.plusDays(1); // tăng dần đến đúng thứ cần tìm
            }
            LocalDateTime now = LocalDateTime.now();
            int count = 0;
            // Duyệt theo từng tuần cho đến endDate
            while (!date.isAfter(endDate)) {
                // Gộp ngày và giờ bắt đầu/kết thúc thành LocalDateTime
                LocalDateTime startDateTime = date.atTime(startTime);
                String sectionStatus;
                if (startDateTime.toLocalDate().isBefore(now.toLocalDate())) {
                    sectionStatus = "completed";
                } else if (startDateTime.toLocalDate().isEqual(now.toLocalDate())) {
                    sectionStatus = "active";
                } else {
                    sectionStatus = "inactive";
                }
                // Gán các giá trị vào PreparedStatement
                ps.setInt(1, courseId);
                ps.setString(2, dayOfWeek);
                ps.setTime(3, Time.valueOf(startTime));
                ps.setTime(4, Time.valueOf(endTime));
                ps.setString(5, classroom);
                ps.setTimestamp(6, Timestamp.valueOf(startDateTime));
                ps.setString(7, sectionStatus);
                ps.setInt(8, teacherId);

                // Thêm vào batch để thực hiện nhiều insert 1 lúc(batch là để thu thập dữ liệu )
                ps.addBatch();

                // Tăng ngày thêm 1 tuần để tạo buổi học tiếp theo
                count++;
                date = date.plusWeeks(1);
            }

            // Thực thi toàn bộ batch insert
            ps.executeBatch();
            System.out.println("Tạo section theo tuần thành công cho courseId = " + courseId);

        } catch (Exception e) {
            System.out.println("Lỗi khi tạo danh sách section:");
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm section theo từ khóa tên khóa học, thứ trong tuần và trạng thái.
     *
     * @param keyword từ khóa tìm kiếm theo tên khóa học
     * @param dayOfWeek lọc theo thứ trong tuần
     * @param status lọc theo trạng thái lớp học
     * @return danh sách SectionDTO phù hợp
     */
    public List<SectionDTO> searchSections(String keyword, String dayOfWeek, String status) {
        List<SectionDTO> list = new ArrayList<>();

        String sql = """
        SELECT s.id, c.name AS courseName, s.courseId, s.dayOfWeek,
               s.startTime, s.endTime, s.classroom, s.dateTime, s.status
        FROM section s
        JOIN course c ON s.courseId = c.id
        WHERE 1 = 1
    """;

        // Thêm điều kiện lọc
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND LOWER(c.name) LIKE LOWER(?)";
        }
        if (dayOfWeek != null && !dayOfWeek.trim().isEmpty()) {
            sql += " AND s.dayOfWeek = ?";
        }
        if (status != null && !status.trim().isEmpty()) {
            sql += " AND s.status = ?";
        }

        sql += " ORDER BY s.dateTime ASC";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(index++, "%" + keyword.trim() + "%");
            }
            if (dayOfWeek != null && !dayOfWeek.trim().isEmpty()) {
                ps.setString(index++, dayOfWeek);
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while (rs.next()) {
                SectionModal section = new SectionModal();
                section.setId(rs.getInt("id"));
                section.setCourseId(rs.getInt("courseId"));
                section.setDayOfWeek(SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")));
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();
                LocalDateTime dateTime = rs.getTimestamp("dateTime").toLocalDateTime();
                section.setStartTime(startTime);
                section.setEndTime(endTime);
                section.setDateTime(dateTime);

                section.setClassroom(rs.getString("classroom"));
                section.setStatus(SectionModal.Status.valueOf(rs.getString("status")));

                SectionDTO dto = new SectionDTO();
                dto.setId(section.getId());
                dto.setCourseId(section.getCourseId());
                dto.setDayOfWeek(section.getDayOfWeek());
                dto.setClassroom(section.getClassroom());
                dto.setStatus(section.getStatus());
                dto.setCourseName(rs.getString("courseName"));
                dto.setSection(section);

                dto.setStartTimeFormatted(startTime.format(timeFmt));
                dto.setEndTimeFormatted(endTime.format(timeFmt));
                dto.setDateFormatted(dateTime.toLocalDate().format(dateFmt));

                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean isConflictSection(int id, String classroom, LocalDate date, LocalTime startTime, LocalTime endTime) {
        String sql = """
        SELECT COUNT(*) 
        FROM section
        WHERE classroom = ?
          AND id != ?
          AND DATE(startTime) = ?
          AND (
                (TIME(startTime) < TIME(?) AND TIME(endTime) > TIME(?))
              )
    """;

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, classroom);
            ps.setInt(2, id);
            ps.setDate(3, java.sql.Date.valueOf(date));
            ps.setTime(4, Time.valueOf(endTime));
            ps.setTime(5, Time.valueOf(startTime));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCourseNameById(int courseId) {
        String sql = "SELECT name FROM course WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public SectionModal getSectionById(int id) throws Exception {
        String sql = "SELECT * FROM section WHERE id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs); // 🔁 Dùng hàm đã viết
                }
            }
        }
        return null;
    }

    public boolean updateNote(int sectionId, String note) throws Exception {
        String sql = "UPDATE section SET note = ? WHERE id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, note);
            stmt.setInt(2, sectionId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Integer getTeacherIdBySectionId(int sectionId) throws Exception {
        String sql = "SELECT teacherId FROM section WHERE id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("teacherId");
                }
            }
        }
        return null;
    }

    public List<SectionModal> getSectionsByCourseId(int courseId) {
        List<SectionModal> sectionList = new ArrayList<>();
        String sql = "SELECT * FROM section WHERE courseId = ? ORDER BY dateTime";

        try (Connection connection = DBUtil.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sectionList.add(mapResultSet(rs)); // dùng lại hàm ánh xạ có sẵn
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sectionList;
    }

    public List<SectionDTO> getAllSectionsDTO() {
        List<SectionDTO> sectionList = new ArrayList<>();

        String sql = """
        SELECT s.id,
               s.courseId,
               c.name AS courseName,
               a.name AS teacherName,
               s.dayOfWeek,
               s.classroom,
               s.status,
               s.startTime,
               s.endTime,
               s.dateTime,
               s.teacherId
        FROM section s
        JOIN course c ON s.courseId = c.id
        JOIN teacher t ON s.teacherId = t.id
        JOIN account a ON t.accountId = a.id
        ORDER BY s.dateTime ASC;
    """;

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Tạo đối tượng SectionModal để chứa thông tin gốc
                SectionModal section = new SectionModal();
                section.setId(rs.getInt("id"));
                section.setCourseId(rs.getInt("courseId"));
                section.setDayOfWeek(SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")));
                section.setClassroom(rs.getString("classroom"));
                section.setStatus(SectionModal.Status.valueOf(rs.getString("status")));

                // Lấy và xử lý thời gian
                LocalTime startTime = rs.getTime("startTime").toLocalTime();
                LocalTime endTime = rs.getTime("endTime").toLocalTime();
                LocalDateTime dateTime = rs.getTimestamp("dateTime").toLocalDateTime();
                LocalDate date = dateTime.toLocalDate();

                LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

                section.setStartTime(startDateTime);
                section.setEndTime(endDateTime);
                section.setDateTime(dateTime);

                // Tạo DTO để truyền ra view
                SectionDTO dto = new SectionDTO();
                dto.setId(section.getId());
                dto.setCourseId(section.getCourseId());
                dto.setDayOfWeek(section.getDayOfWeek());
                dto.setClassroom(section.getClassroom());
                dto.setStatus(section.getStatus());
                dto.setCourseName(rs.getString("courseName"));
                dto.setTeacherName(rs.getString("teacherName"));
                dto.setTeacherId(rs.getInt("teacherId"));
                dto.setSection(section);

                dto.setStartTimeFormatted(startTime.format(timeFormatter));
                dto.setEndTimeFormatted(endTime.format(timeFormatter));
                dto.setDateFormatted(date.format(dateFormatter));

                sectionList.add(dto);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sectionList;
    }

    public List<SectionModal> getSectionsByCourse(int courseId) {
        List<SectionModal> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE courseId = ? ")) {
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionModal s = new SectionModal(
                        rs.getInt("id"),
                        rs.getInt("courseId"),
                        SectionModal.DayOfWeekEnum.valueOf(rs.getString("dayOfWeek")),
                        rs.getTimestamp("startTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime(),
                        rs.getString("classroom"),
                        rs.getTimestamp("dateTime").toLocalDateTime(),
                        SectionModal.Status.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getString("note"),
                        rs.getInt("teacherId")
                );
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getInfoById(int sectionId) {
        String info = "";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT date_time, start_time, end_time FROM section WHERE id = ?")) {
            ps.setInt(1, sectionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info = rs.getTimestamp("date_time").toLocalDateTime().toLocalDate() + " ("
                        + rs.getTimestamp("start_time").toLocalDateTime().toLocalTime() + "-"
                        + rs.getTimestamp("end_time").toLocalDateTime().toLocalTime() + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public List<Map<String, Object>> getUnpaidSectionsByCourseForStudent(int courseId, int studentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT sec.*, sps.amount "
                + "FROM section sec "
                + "JOIN student_section ss ON sec.id = ss.sectionId AND ss.studentId = ? "
                + "JOIN student_payment_schedule sps ON sps.student_section_id = ss.id "
                + "WHERE sec.courseId = ? AND sps.isPaid = 0";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("id"));
                row.put("courseId", rs.getInt("courseId"));
                row.put("dayOfWeek", rs.getString("dayOfWeek"));
                row.put("startTime", rs.getTimestamp("startTime"));
                row.put("endTime", rs.getTimestamp("endTime"));
                row.put("classroom", rs.getString("classroom"));
                row.put("dateTime", rs.getTimestamp("dateTime"));
                row.put("status", rs.getString("status"));
                row.put("createdAt", rs.getTimestamp("created_at"));
                row.put("updatedAt", rs.getTimestamp("updated_at"));
                row.put("note", rs.getString("note"));
                row.put("teacherId", rs.getInt("teacherId"));
                row.put("amount", rs.getBigDecimal("amount"));
                list.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String rescheduleAllSectionsInDay(LocalDate fromDate, LocalDate toDate) {
        String getSectionsSQL = "SELECT id FROM section WHERE DATE(dateTime) = ?";
        List<Integer> sectionIds = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(getSectionsSQL)) {

            stmt.setDate(1, java.sql.Date.valueOf(fromDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sectionIds.add(rs.getInt("id"));
            }

            for (int sectionId : sectionIds) {
                String result = updateSectionDate(sectionId, toDate);
                if (result.equalsIgnoreCase("CONFLICT_TEACHER")) {
                    return "Lỗi: Giáo viên có ca học bị trùng vào ngày " + toDate + ", Section ID: " + sectionId;
                } else if (result.equalsIgnoreCase("CONFLICT_CLASSROOM")) {
                    return "Lỗi: Phòng học bị trùng vào ngày " + toDate + ", Section ID: " + sectionId;
                } else if (result.equalsIgnoreCase("NOT_FOUND")) {
                    return "Lỗi: Không tìm thấy lớp học với ID: " + sectionId;
                } else if (result.equalsIgnoreCase("ERROR")) {
                    return "Lỗi hệ thống khi xử lý lớp học ID: " + sectionId;
                }
            }
            return "Tất cả các ca đã được chuyển từ " + fromDate + " sang " + toDate + " thành công.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi truy vấn danh sách ca học trong ngày " + fromDate;
        }
    }

    public String updateSectionDate(int sectionId, LocalDate newDate) {
        String checkTeacherSQL = """
            SELECT COUNT(*) FROM section
            WHERE id != ?
              AND teacherId = (SELECT teacherId FROM section WHERE id = ?)
              AND TIME(startTime) = (SELECT TIME(startTime) FROM section WHERE id = ?)
              AND DATE(dateTime) = ?
        """;

        String checkRoomSQL = """
            SELECT COUNT(*) FROM section
            WHERE id != ?
              AND classroom = (SELECT classroom FROM section WHERE id = ?)
              AND TIME(startTime) = (SELECT TIME(startTime) FROM section WHERE id = ?)
              AND DATE(dateTime) = ?
        """;

        String updateSQL = """
            UPDATE section
            SET dateTime = ?
            WHERE id = ?
        """;

        try (Connection conn = DBUtil.getConnection()) {

            // ✅ Check trùng giáo viên
            try (PreparedStatement stmt = conn.prepareStatement(checkTeacherSQL)) {
                stmt.setInt(1, sectionId);
                stmt.setInt(2, sectionId);
                stmt.setInt(3, sectionId);
                stmt.setDate(4, java.sql.Date.valueOf(newDate));
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "CONFLICT_TEACHER";
                }
            }

            // ✅ Check trùng phòng học
            try (PreparedStatement stmt = conn.prepareStatement(checkRoomSQL)) {
                stmt.setInt(1, sectionId);
                stmt.setInt(2, sectionId);
                stmt.setInt(3, sectionId);
                stmt.setDate(4, java.sql.Date.valueOf(newDate));
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "CONFLICT_CLASSROOM";
                }
            }

            // ✅ Cập nhật nếu không trùng
            try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                LocalDateTime newDateTime = newDate.atStartOfDay();
                stmt.setTimestamp(1, Timestamp.valueOf(newDateTime));
                stmt.setInt(2, sectionId);
                int rows = stmt.executeUpdate();
                return (rows > 0) ? "SUCCESS" : "NOT_FOUND";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public String updateSectionTeacher(int sectionId, int newTeacherId) {
        String getSectionSQL = "SELECT DATE(dateTime) AS date, startTime, endTime FROM section WHERE id = ?";
        String checkConflictSQL = """
        SELECT COUNT(*) FROM section
        WHERE teacherId = ? AND DATE(dateTime) = ? AND id != ?
          AND (
              (startTime < ? AND endTime > ?)  -- giao thời gian
          )
    """;
        String updateSQL = "UPDATE section SET teacherId = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement getStmt = conn.prepareStatement(getSectionSQL); PreparedStatement checkStmt = conn.prepareStatement(checkConflictSQL); PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

            // 1. Lấy ngày và ca học của section
            getStmt.setInt(1, sectionId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                return "ERROR_NOT_FOUND"; // Không có lớp học
            }

            Date date = rs.getDate("date");
            Time startTime = rs.getTime("startTime");
            Time endTime = rs.getTime("endTime");


            // 2. Kiểm tra trùng lịch giáo viên
            checkStmt.setInt(1, newTeacherId);
            checkStmt.setDate(2, date);
            checkStmt.setInt(3, sectionId);
            checkStmt.setTime(4, endTime);   
            checkStmt.setTime(5, startTime);


            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            int conflictCount = checkRs.getInt(1);

            if (conflictCount > 0) {
                return "TEACHER_CONFLICT"; // Trùng lịch giáo viên
            }

            // 3. Cập nhật giáo viên mới
            updateStmt.setInt(1, newTeacherId);
            updateStmt.setInt(2, sectionId);
            int rows = updateStmt.executeUpdate();

                       return rows > 0 ? "SUCCESS" : "ERROR_UPDATE_FAILED";


        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR_DATABASE";
        }
    }

    public String updateSectionClassroom(int sectionId, String newRoom) {
        String checkSql = """
        SELECT COUNT(*) FROM section
        WHERE classroom = ? AND id != ?
              AND DATE(dateTime) = (SELECT DATE(dateTime) FROM section WHERE id = ?)
              AND startTime = (SELECT startTime FROM section WHERE id = ?)
    """;

        String updateSql = "UPDATE section SET classroom = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            // 1. Check trùng phòng
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, newRoom);
                checkStmt.setInt(2, sectionId);
                checkStmt.setInt(3, sectionId);
                checkStmt.setInt(4, sectionId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "ROOM_CONFLICT"; // Trùng phòng học
                    }
                }
            }

            // 2. Nếu không trùng thì update
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, newRoom);
                updateStmt.setInt(2, sectionId);

                int rows = updateStmt.executeUpdate();
                return (rows > 0) ? "SUCCESS" : "ERROR_NOT_FOUND";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR_DATABASE";
        }
    }

    public void updateStatus(int sectionId, String newStatus) {
        String sql = "UPDATE section SET status = ? WHERE id = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus); 
            ps.setInt(2, sectionId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package controller;

import dao.CourseDAO;
import dao.RoomDAO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import dto.SectionDTO;
import dao.SectionDAO;
import dao.TeacherDAO;
import dto.CourseDTO;
import dto.TeacherDTO;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import modal.RoomModal;

/**
 *
 * @author Admin
 */
public class ScheduleManagementServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    SectionDAO sectionDao = new SectionDAO();
    TeacherDAO teacherDao = new TeacherDAO();
    CourseDAO courseDao = new CourseDAO();
    RoomDAO roomDao = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        viewSchedule(request, response, null, null, null, null);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        String action = request.getParameter("action");

        String direction = request.getParameter("direction");
        String view = request.getParameter("view");
        String dayLabel = request.getParameter("dayLabel");
        String teacherIdStr = request.getParameter("teacherSelected");
        String roomIdStr = request.getParameter("roomSelected");
        String changeType = request.getParameter("changeType");
        String dateChoose = request.getParameter("dateChoose");
        String courseIdStr = request.getParameter("courseSelected");

        if (dateChoose != null) {
            dayLabel = dateChoose;
        }

        if (teacherIdStr != null) {
            if (teacherIdStr.equalsIgnoreCase("all")) {
                teacherIdStr = null;
            }
        }
        if (roomIdStr != null) {
            if (roomIdStr.equalsIgnoreCase("all")) {
                roomIdStr = null;
            }
        }

        if (courseIdStr != null) {
            if (courseIdStr.equalsIgnoreCase("all")) {
                courseIdStr = null;
            }
        }

        if (direction != null && !direction.isBlank()) {
            viewScheduleByView(request, response, direction, view, dayLabel, teacherIdStr, roomIdStr, courseIdStr);
        } else if (changeType != null) {
            System.out.println("Vao day roi");
            changeSchedule(request, response, changeType, java.sql.Date.valueOf(dayLabel), teacherIdStr, roomIdStr, courseIdStr);

        } else {
            // Xử lý chuyển view mà không chuyển ngày
            Date parsedDate = (dayLabel != null && !dayLabel.isBlank())
                    ? java.sql.Date.valueOf(dayLabel)
                    : null;
            viewSchedule(request, response, parsedDate, teacherIdStr, roomIdStr, courseIdStr);
        }
    }

    void changeSchedule(HttpServletRequest request, HttpServletResponse response, String changeType, Date dayLabel, String teacherId, String roomId, String courseIdStr) {
//        String teacherIdStr = request.getParameter("teacherId");
        String sectionIdStr = request.getParameter("scheduleId");
        String newTimeStr = request.getParameter("newDate");
        String message = null;

        if (changeType.equalsIgnoreCase("changeSchedule")) {

            String scheduleChangeMode = request.getParameter("scheduleChangeMode");
            if (scheduleChangeMode != null) {
                System.out.println("Chay den day la changeMode ko null");
                System.out.println(sectionIdStr);
                System.out.println(newTimeStr);
                if (scheduleChangeMode.equalsIgnoreCase("byTeacher") && sectionIdStr != null && newTimeStr != null) {
                    System.out.println("Chay vao day la doi theo giao vien");
                    int sectionId = Integer.parseInt(sectionIdStr);
                    LocalDate newTime = LocalDate.parse(newTimeStr);
                    String result = sectionDao.updateSectionDate(sectionId, newTime);
                    if (result.equalsIgnoreCase("SUCCESS")) {
                        message = "Cập nhật thành công!";
                    } else if (result.equalsIgnoreCase("CONFLICT_TEACHER")) {
                        message = "Giáo viên đã có lớp vào khung giờ này.";
                    } else if (result.equalsIgnoreCase("CONFLICT_CLASSROOM")) {
                        message = "Phòng học đã được sử dụng vào khung giờ này.";
                    } else if (result.equalsIgnoreCase("NOT_FOUND")) {
                        message = "Không tìm thấy lớp học để cập nhật.";
                    } else {
                        message = "Lỗi hệ thống khi cập nhật.";
                    }
                } else if (scheduleChangeMode.equalsIgnoreCase("byDate")) {
                    String sourceDateStr = request.getParameter("sourceDate");
                    String targetDateStr = request.getParameter("targetDate");
                    LocalDate sourceDate = LocalDate.parse(sourceDateStr);
                    LocalDate targetDate = LocalDate.parse(targetDateStr);
                    message = sectionDao.rescheduleAllSectionsInDay(sourceDate, targetDate);
                }
            }
            System.out.println("in ra " + message);
            request.setAttribute("mesage", message);
            try {
                System.out.println("Chay den day roi");
                viewSchedule(request, response, dayLabel, teacherId, roomId, courseIdStr);
            } catch (Exception ex) {
                Logger.getLogger(ScheduleManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (changeType.equalsIgnoreCase("changeTeacher") && sectionIdStr != null) {
//                    int teacherId = Integer.parseInt(teacherIdStr);
            int sectionId = Integer.parseInt(sectionIdStr);
            String newTeacherIdStr = request.getParameter("newTeacherId");
            int newTeacherId;
            if (newTeacherIdStr != null) {
                newTeacherId = Integer.parseInt(newTeacherIdStr);
                String result = sectionDao.updateSectionTeacher(sectionId, newTeacherId);
                if (result.equals("SUCCESS")) {
                    message = "Cập nhật giáo viên thành công.";
                } else if (result.equals("TEACHER_CONFLICT")) {
                    message = " Giáo viên bị trùng lịch, không thể cập nhật.";
                } else if (result.equals("ERROR_NOT_FOUND")) {
                    message = " Không tìm thấy lớp học.";
                } else if (result.equals("ERROR_DATABASE")) {
                    message = " Lỗi hệ thống hoặc database.";
                } else {
                    message = "Lỗi không xác định: " + result;
                }
            }
            request.setAttribute("mesage", message);
            try {
                viewSchedule(request, response, dayLabel, teacherId, roomId, courseIdStr);
            } catch (Exception ex) {
                Logger.getLogger(ScheduleManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            int sectionId = Integer.parseInt(sectionIdStr);
            String newRoomId = request.getParameter("newRoomId");
            String result = sectionDao.updateSectionClassroom(sectionId, newRoomId);
            if (result.equals("SUCCESS")) {
                message = " Cập nhật phòng học thành công.";
            } else if (result.equals("ROOM_CONFLICT")) {
                message = "️ Phòng học bị trùng lịch, không thể cập nhật.";
            } else if (result.equals("ERROR_NOT_FOUND")) {
                message = " Không tìm thấy lớp học.";
            } else if (result.equals("ERROR_DATABASE")) {
                message = " Lỗi hệ thống hoặc cơ sở dữ liệu.";
            } else {
                message = " Lỗi không xác định: " + result;
            }
            request.setAttribute("message", message);
            try {
                viewSchedule(request, response, dayLabel, teacherId, roomId, courseIdStr);
            } catch (Exception ex) {
                Logger.getLogger(ScheduleManagementServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    void viewScheduleByView(HttpServletRequest request, HttpServletResponse response, String direction, String view, String dayLabel, String teacherId, String roomId, String courseId) {
        try {
            // Định dạng theo kiểu bạn đang dùng (ví dụ: yyyy-MM-dd)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Chuyển String -> LocalDate
            LocalDate currentDate = LocalDate.parse(dayLabel, formatter);

            // Xử lý direction
            if ("prev".equalsIgnoreCase(direction)) {
                if ("day".equalsIgnoreCase(view)) {
                    currentDate = currentDate.minusDays(1);
                } else if ("week".equalsIgnoreCase(view)) {
                    currentDate = currentDate.minusWeeks(1);
                } else if ("month".equalsIgnoreCase(view)) {
                    currentDate = currentDate.minusMonths(1);
                }

            } else if ("next".equalsIgnoreCase(direction)) {
                if ("day".equalsIgnoreCase(view)) {
                    currentDate = currentDate.plusDays(1);
                } else if ("week".equalsIgnoreCase(view)) {
                    currentDate = currentDate.plusWeeks(1);
                } else if ("month".equalsIgnoreCase(view)) {
                    currentDate = currentDate.plusMonths(1);
                }
            }
            // Chuyển lại thành String (nếu cần)
            String newDayLabel = currentDate.format(formatter);

            // Set lại cho request
            request.setAttribute("dayLabel", newDayLabel);
            request.setAttribute("view", view);
            request.setAttribute("currentView", view);

            System.out.println("Chuyển lịch sang: " + newDayLabel);
            viewSchedule(request, response, java.sql.Date.valueOf(newDayLabel), teacherId, roomId, courseId);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý ngày");
        }
    }

    private void viewSchedule(HttpServletRequest request, HttpServletResponse response, Date dayLabel, String teacherId, String roomId, String courseId)
            throws ServletException, IOException {

        // Forward to JSP
        List<TeacherDTO> teachers = teacherDao.getAllTeachers();
        List<SectionDTO> sections = sectionDao.getAllSectionsDTO();
        List<CourseDTO> courses = courseDao.getAllCourses();
        List<RoomModal> rooms = roomDao.getAllRooms();

        if (teacherId != null && !teacherId.isBlank()) {
            int idTeacher = Integer.parseInt(teacherId);
            sections = sections.stream()
                    .filter(s -> s.getTeacherId() == idTeacher)
                    .collect(Collectors.toList());
            request.setAttribute("teacherSelected", teacherId);
        }
        if (roomId != null) {
            sections = sections.stream()
                    .filter(s -> s.getClassroom().equalsIgnoreCase(roomId))
                    .collect(Collectors.toList());
            request.setAttribute("roomSelected", roomId);
        }
        if (courseId != null) {
            int id = Integer.parseInt(courseId);
            sections = sections.stream()
                    .filter(s -> s.getCourseId() == id)
                    .collect(Collectors.toList());
            request.setAttribute("courseSelected", courseId);
        }

        // Định dạng ngày tháng
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Date realTime = new Date();
        if (dayLabel == null) {
            dayLabel = java.sql.Date.valueOf(today);
        }
        LocalDate localDayLabel = ((java.sql.Date) dayLabel).toLocalDate();

        String view = request.getParameter("view");
        if (view == null || view.isBlank()) {
            view = "week";
        }
        // 1. Tính toán tuần hiện tại (từ Thứ 2 đến Chủ Nhật)
        LocalDate monday = localDayLabel.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<String> currentWeekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            currentWeekDates.add(monday.plusDays(i).format(dateFormatter));
        }

        // 2. Tính toán tháng hiện tạ
        int daysInMonth = localDayLabel.lengthOfMonth();
        LocalDate firstDayOfMonth = localDayLabel.withDayOfMonth(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        request.setAttribute("rooms", rooms);
        request.setAttribute("realTime", realTime);
        request.setAttribute("today", today.format(dateFormatter));
        request.setAttribute("currentWeekDates", currentWeekDates);
        request.setAttribute("sections", sections);
        request.setAttribute("teachers", teachers);
        request.setAttribute("dayLabel", dayLabel);
        request.setAttribute("courses", courses);
        request.setAttribute("currentView", view);
        request.setAttribute("daysInMonth", daysInMonth);
        request.setAttribute("firstDayOfMonth", firstDayOfWeek);
        request.setAttribute("currentMonth", localDayLabel.format(DateTimeFormatter.ofPattern("yyyy-MM")));

        // Forward đến JSP
        request.getRequestDispatcher("./views/scheduleManagement.jsp").forward(request, response);
    }

    public List<SectionDTO> filterSectionsByDay(List<SectionDTO> sections, LocalDate date) {
        return sections.stream()
                .filter(s -> s.getStartTime() != null && s.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<SectionDTO> filterSectionsByWeek(List<SectionDTO> sections, LocalDate anyDateInWeek) {
        LocalDate startOfWeek = anyDateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = anyDateInWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return sections.stream()
                .filter(s -> {
                    if (s.getStartTime() == null) {
                        return false;
                    }
                    LocalDate sectionDate = s.getStartTime().toLocalDate();
                    return (sectionDate.isEqual(startOfWeek) || sectionDate.isAfter(startOfWeek))
                            && (sectionDate.isEqual(endOfWeek) || sectionDate.isBefore(endOfWeek));
                })
                .collect(Collectors.toList());
    }

    public List<SectionDTO> filterSectionsByMonth(List<SectionDTO> sections, YearMonth month) {
        return sections.stream()
                .filter(s -> {
                    if (s.getStartTime() == null) {
                        return false;
                    }
                    YearMonth sectionMonth = YearMonth.from(s.getStartTime().toLocalDate());
                    return sectionMonth.equals(month);
                })
                .collect(Collectors.toList());
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import dao.RoomDAO;
import dao.SectionDAO;
import dao.TeacherDAO;
import dto.CourseDTO;
import dto.TeacherDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import modal.CourseModal;
import modal.CourseModal.Status;
import modal.RoomModal;

/**
 * Servlet dùng để quản lý các chức năng liên quan đến khóa học, bao gồm: - Hiển
 * thị danh sách khóa học (tìm kiếm, lọc) - Thêm mới khóa học - Cập nhật khóa
 * học - Xóa khóa học - Xem chi tiết khoá học
 *
 * URL mapping: /quan-ly-khoa-hoc
 *
 * Các action hỗ trợ qua tham số ?action=: - list - add - edit - delete - detail
 *
 * Phần "add" và "update" được xử lý ở method POST.
 *
 * @author HanND
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ManagerCourseServlet extends HttpServlet {

    /**
     * Xử lý các request GET như hiển thị danh sách, form thêm, form cập nhật.
     *
     * @param request HTTP request chứa tham số truy vấn
     * (?action=list|edit|add|delete|detail)
     * @param response HTTP response để điều hướng hoặc render trang
     * @throws ServletException nếu có lỗi Servlet
     * @throws IOException nếu có lỗi I/O
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        CourseDAO courseDAO = new CourseDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        switch (action) {
            case "list": {
                String name = request.getParameter("name");
                String subject = request.getParameter("subject");
                String level = request.getParameter("level");
                String status = request.getParameter("status");
                String gradeStr = request.getParameter("grade");
                String teacherIdStr = request.getParameter("teacherId");

                Integer grade = null;
                if (gradeStr != null && !gradeStr.trim().isEmpty()) {
                    grade = Integer.parseInt(gradeStr.trim());
                }

                Integer teacherId = null;
                if (teacherIdStr != null && !teacherIdStr.trim().isEmpty()) {
                    teacherId = Integer.parseInt(teacherIdStr.trim());
                }

                List<CourseDTO> courseList;
                if ((name != null && !name.trim().isEmpty())
                        || (subject != null && !subject.trim().isEmpty())
                        || (level != null && !level.trim().isEmpty())
                        || (status != null && !status.trim().isEmpty())
                        || grade != null
                        || teacherId != null) {

                    courseList = courseDAO.searchCourses(name, subject, level, status, grade, teacherId);
                } else {
                    courseList = courseDAO.getAllCourses();
                }
                for (CourseDTO course : courseList) {
                    if (course.getStartDate() != null && course.getEndDate() != null) {
                        LocalDate start = ((Timestamp) course.getStartDate()).toLocalDateTime().toLocalDate();
                        LocalDate end = ((Timestamp) course.getEndDate()).toLocalDateTime().toLocalDate();

                        String currentStatus = determineStatus(start, end).name();
                        if (!course.getStatus().equals(currentStatus)) {
                            course.setStatus(currentStatus);
                            courseDAO.updateStatus(course.getId(), currentStatus);
                        }
                    }
                }

                List<TeacherDTO> teacherList = teacherDAO.getAllTeachers();
                request.setAttribute("courseList", courseList);
                request.setAttribute("teacherList", teacherList);
                request.getRequestDispatcher("views/managerCourse.jsp").forward(request, response);
                break;
            }
            case "edit": {
                int id = Integer.parseInt(request.getParameter("id"));
                CourseModal course = courseDAO.getCourseById(id);
                if (course == null) {
                    response.sendRedirect("quan-ly-khoa-hoc?action=list&error=notfound");
                    return;
                }
                List<TeacherDTO> teacherList = teacherDAO.getAllTeachers();
                request.setAttribute("course", course);
                request.setAttribute("teacherList", teacherList);
                request.setAttribute("statusString", course.getStatus().name());

                if (course.getStartDate() != null && course.getEndDate() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    request.setAttribute("formattedStartDate", course.getStartDate().toLocalDate().format(formatter));
                    request.setAttribute("formattedEndDate", course.getEndDate().toLocalDate().format(formatter));
                }

                request.getRequestDispatcher("views/editCourse.jsp").forward(request, response);
                break;
            }
            case "add": {
                List<TeacherDTO> teacherList = teacherDAO.getAllTeachers();
                RoomDAO roomDAO = new RoomDAO();
                List<RoomModal> roomList = roomDAO.getAllRooms();

                request.setAttribute("teacherList", teacherList);
                request.setAttribute("roomList", roomList);
                request.getRequestDispatcher("views/addCourse.jsp").forward(request, response);
                break;
            }
            case "delete": {
                int id = Integer.parseInt(request.getParameter("id"));
                courseDAO.deleteCourseById(id);
                response.sendRedirect("quan-ly-khoa-hoc?action=list");
                break;
            }
            case "detail": {
                int id = Integer.parseInt(request.getParameter("id"));
                CourseDTO course = courseDAO.getCourseByIdFull(id);
                if (course == null) {
                    response.sendRedirect("quan-ly-khoa-hoc?action=list&error=notfound");
                    return;
                }
                List<TeacherDTO> teacherList = teacherDAO.getAllTeachers();
                request.setAttribute("course", course);
                request.setAttribute("teacherList", teacherList);
                request.setAttribute("teacherName", course.getTeacherName());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                if (course.getStartDate() != null) {
                    LocalDate start = ((Timestamp) course.getStartDate()).toLocalDateTime().toLocalDate();
                    request.setAttribute("formattedStartDate", start.format(formatter));
                }

                if (course.getEndDate() != null) {
                    LocalDate end = ((Timestamp) course.getEndDate()).toLocalDateTime().toLocalDate();
                    request.setAttribute("formattedEndDate", end.format(formatter));
                }

                request.getRequestDispatcher("views/detailCourse.jsp").forward(request, response);
                break;
            }

            case "checkDuplicate": {
                String name = request.getParameter("name");
                boolean exists = new CourseDAO().existsByName(name);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"exists\":" + exists + "}");
                return;
            }
            default: {
                response.sendRedirect("quan-ly-khoa-hoc?action=list");
            }
        }
    }

    /**
     * Xử lý các request POST để thêm hoặc cập nhật khóa học.
     *
     * @param request HTTP request chứa dữ liệu khóa học gửi từ form
     * @param response HTTP response để điều hướng sau xử lý
     * @throws ServletException nếu có lỗi Servlet
     * @throws IOException nếu có lỗi I/O
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        CourseDAO dao = new CourseDAO();
        SectionDAO sectionDAO = new SectionDAO();

        try {
            if ("add".equals(action)) {
                try {
                    CourseModal course = new CourseModal();
                    LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                    //Thêm thông tin khoá học 
                    course.setName(request.getParameter("name"));
                    course.setTeacherId(Integer.valueOf(request.getParameter("teacherId")));
                    course.setSubject(CourseModal.Subject.valueOf(request.getParameter("subject")));
                    course.setGrade(request.getParameter("grade"));
                    course.setDescription(request.getParameter("description"));
                    course.setCourseType(CourseModal.CourseType.valueOf(request.getParameter("courseType")));
                    course.setStartDate(startDate.atStartOfDay());
                    course.setEndDate(endDate.atStartOfDay());
                    course.setStatus(determineStatus(startDate, endDate));

                    course.setStartDate(LocalDate.parse(request.getParameter("startDate")).atStartOfDay());
                    course.setEndDate(LocalDate.parse(request.getParameter("endDate")).atStartOfDay());
                    course.setWeekAmount(Integer.parseInt(request.getParameter("weekAmount")));
                    course.setStudentEnrollment(Integer.parseInt(request.getParameter("studentEnrollment")));
                    course.setMaxStudents(Integer.parseInt(request.getParameter("maxStudents")));
                    course.setLevel(CourseModal.Level.valueOf(request.getParameter("level")));
                    course.setIsHot("true".equalsIgnoreCase(request.getParameter("isHot")));

                    String discountStr = request.getParameter("discountPercentage");
                    course.setDiscountPercentage((discountStr != null && !discountStr.isEmpty()) ? new BigDecimal(discountStr) : null);

                    if (course.getCourseType() == CourseModal.CourseType.combo) {
                        String feeComboStr = request.getParameter("feeCombo");
                        course.setFeeCombo((feeComboStr != null && !feeComboStr.isEmpty()) ? new BigDecimal(feeComboStr) : null);
                        course.setFeeDaily(null);
                    } else {
                        String feeDailyStr = request.getParameter("feeDaily");
                        course.setFeeDaily((feeDailyStr != null && !feeDailyStr.isEmpty()) ? new BigDecimal(feeDailyStr) : null);
                        course.setFeeCombo(null);
                    }
                    //thêm ảnh 
                    Part filePart = request.getPart("course_img");
                    String fileName = null;

                    if (filePart != null && filePart.getSize() > 0) {
                        fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        course.setCourse_img(fileName);
                    } else {
                        course.setCourse_img("default.png");
                    }
                    //Thêm khoá học tạo ra courseID để tạo section theo courseId
                    int courseId = dao.addCourseReturnId(course);

                    if (filePart != null && filePart.getSize() > 0) {
                        String uploadPath = getServletContext().getRealPath("/") + "assets/banners_course";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdirs();
                        }
                        filePart.write(uploadPath + File.separator + fileName);
                    }

                    String dayOfWeek = request.getParameter("dayOfWeek");
                    String startTimeStr = request.getParameter("startTime");
                    String endTimeStr = request.getParameter("endTime");
                    String classroom = request.getParameter("classroom");

                    if (dayOfWeek != null && !dayOfWeek.isEmpty()
                            && startTimeStr != null && !startTimeStr.isEmpty()
                            && endTimeStr != null && !endTimeStr.isEmpty()
                            && classroom != null && !classroom.isEmpty()) {

                        LocalTime startTime = LocalTime.parse(startTimeStr);
                        LocalTime endTime = LocalTime.parse(endTimeStr);
                        //Thêm section 
                        sectionDAO.addSections(courseId, dayOfWeek, startTime, endTime, classroom, startDate, endDate, course.getTeacherId());
                    }

                    response.sendRedirect("quan-ly-khoa-hoc?action=list");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    request.setAttribute("error", "Lỗi xảy ra: " + ex.getMessage());
                    request.getRequestDispatcher("views/addCourse.jsp").forward(request, response);
                }
                return;
            }

            if ("update".equals(action)) {
                CourseModal course = new CourseModal();
                LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                course.setId(Integer.parseInt(request.getParameter("id")));
                course.setName(request.getParameter("name"));
                course.setTeacherId(Integer.parseInt(request.getParameter("teacherId")));
                course.setSubject(CourseModal.Subject.valueOf(request.getParameter("subject")));
                course.setGrade(request.getParameter("grade"));
                course.setFeeCombo(parseBigDecimal(request.getParameter("feeCombo")));
                course.setFeeDaily(parseBigDecimal(request.getParameter("feeDaily")));
                course.setStartDate(LocalDate.parse(request.getParameter("startDate")).atStartOfDay());
                course.setEndDate(LocalDate.parse(request.getParameter("endDate")).atStartOfDay());
                course.setStudentEnrollment(Integer.parseInt(request.getParameter("studentEnrollment")));
                course.setMaxStudents(Integer.parseInt(request.getParameter("maxStudents")));
                course.setWeekAmount(Integer.parseInt(request.getParameter("weekAmount")));
                course.setLevel(CourseModal.Level.valueOf(request.getParameter("level")));
                course.setStartDate(startDate.atStartOfDay());
                course.setEndDate(endDate.atStartOfDay());
                course.setStatus(determineStatus(startDate, endDate));

                //cập nhật ảnh của course
                Part part = request.getPart("course_img");
                String oldImage = request.getParameter("oldImage");
                String fileName = oldImage != null ? oldImage : "default.png"; // mặc định dùng ảnh cũ

                if (part != null && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                    String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    fileName = course.getId() + "_" + originalFileName;

                    String realPath = request.getServletContext().getRealPath("/assets/banners_course");
                    //Thêm /assets/banners_course nếu chưa có 
                    File uploadDir = new File(realPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    part.write(realPath + File.separator + fileName);
                }

                course.setCourse_img(fileName);
                dao.updateCourse(course);
                response.sendRedirect("quan-ly-khoa-hoc?action=list");
            }
        } catch (Exception e) {
            response.sendRedirect("quan-ly-khoa-hoc?action=list&error=failed");
        }
    }

    /**
     * Hàm hỗ trợ parse BigDecimal từ string, nếu null hoặc rỗng thì trả về
     * null.
     *
     * @param value chuỗi đầu vào
     * @return BigDecimal hoặc null nếu không hợp lệ
     */
    //Hàm này giúp tránh lỗi khi người dùng không nhập gì.
    private BigDecimal parseBigDecimal(String value) {
        return (value != null && !value.isEmpty()) ? new BigDecimal(value) : null;
    }

    private Status determineStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();

        if (now.isBefore(startDate)) {
            return Status.upcoming;
        } else if (!now.isAfter(endDate)) {
            return Status.activated;
        } else {
            return Status.completed;
        }
    }

    /**
     * Trả về mô tả ngắn cho servlet.
     *
     * @return mô tả ngắn
     */
    @Override
    public String getServletInfo() {
        return "Quản lý khóa học – hiển thị, thêm, sửa, xóa";
    }
}

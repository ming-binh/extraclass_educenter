<%-- 
    Document   : scheduleManagement
    Created on : Jul 10, 2025, 10:00:00 AM
    Author     : vankhoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setLocale value="vi_VN" />
<jsp:include page="layout/adminHeader.jsp" />
<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<c:set var="monthNumber" value="${fn:substring(dayLabel, 5, 7)}" />
<c:set var="year" value="${fn:substring(dayLabel, 0, 4)}"/>
<c:choose>
    <c:when test="${monthNumber == '01'}"><c:set var="monthNumber" value="1"/></c:when>
    <c:when test="${monthNumber == '02'}"><c:set var="monthNumber" value="2"/></c:when>
    <c:when test="${monthNumber == '03'}"><c:set var="monthNumber" value="3"/></c:when>
    <c:when test="${monthNumber == '04'}"><c:set var="monthNumber" value="4"/></c:when>
    <c:when test="${monthNumber == '05'}"><c:set var="monthNumber" value="5"/></c:when>
    <c:when test="${monthNumber == '06'}"><c:set var="monthNumber" value="6"/></c:when>
    <c:when test="${monthNumber == '07'}"><c:set var="monthNumber" value="7"/></c:when>
    <c:when test="${monthNumber == '08'}"><c:set var="monthNumber" value="8"/></c:when>
    <c:when test="${monthNumber == '09'}"><c:set var="monthNumber" value="9"/></c:when>
</c:choose>
<style>
    .calendar-day.today {
        background-color: #f0f8ff;
        border-color: #007bff;
    }
    .month-view-day.today {
        background-color: #e3f2fd;
        border-color: #2196f3;
    }

    .bg-success {
        background-color: #248a27 !important;
    }
    .bg-primary {
        background-color: #1768aa !important;
    }
    .bg-danger {
        background-color: #a1170e !important;
    }
    .text-white {
        color: white !important;
    }

    .calendar-container {
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 0 15px rgba(0,0,0,0.05);
        margin-bottom: 30px;
    }

    .calendar-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 15px 20px;
        border-bottom: 1px solid #eee;
    }

    .calendar-view-options .btn {
        margin-left: 5px;
    }

    .calendar-body {
        padding: 20px;
    }

    .calendar-filters {
        background: #f9f9f9;
        padding: 15px 20px;
        border-radius: 8px;
        margin-bottom: 20px;
    }

    .calendar-grid {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 10px;
    }

    .calendar-day-header {
        text-align: center;
        font-weight: bold;
        padding: 10px;
        background: #f5f5f5;
        border-radius: 5px;
    }

    .calendar-day {
        min-height: 120px;
        border: 1px solid #eee;
        border-radius: 5px;
        padding: 10px;
        position: relative;
    }

    .calendar-day.today {
        background-color: #f0f8ff;
        border-color: #007bff;
    }

    .calendar-day-number {
        position: absolute;
        top: 5px;
        right: 10px;
        font-weight: bold;
        color: #777;
    }

    .calendar-event {
        background: #e1f5fe;
        border-left: 3px solid #03a9f4;
        padding: 5px 8px;
        margin-bottom: 5px;
        border-radius: 3px;
        font-size: 12px;
        cursor: pointer;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .calendar-event.conflict {
        background: #ffebee;
        border-left-color: #f44336;
    }

    .modal-body .form-group {
        margin-bottom: 15px;
    }

    .conflict-warning {
        color: #f44336;
        margin-top: 10px;
        display: none;
    }

    .export-options {
        margin-top: 20px;
    }

    /* Time slots for day view */
    .time-slot {
        position: relative;
        border-bottom: 1px solid #eee;
        padding: 10px;
        height: 60px;
        min-height: 60px;
    }

    .time-label {
        position: absolute;
        left: -70px;
        width: 60px;
        text-align: right;
        font-size: 13px;
        color: #666;
        top: 0;
    }

    .day-view-events {
        margin-left: 70px;
        position: relative;
        height: 100%;
    }

    /* Day view container */
    #dayView {
        display: flex;
        flex-direction: column;
        width: 100%;
        border: 1px solid #ccc;
        position: relative;
        padding-left: 80px;
        background: #fafafa;
        box-sizing: border-box;
    }

    .time-slots-container {
        position: relative;
    }

    /* Month view styles */
    .month-view-grid {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 2px;
        background: #f5f5f5;
        padding: 10px;
        border-radius: 8px;
    }

    .month-view-day {
        min-height: 100px;
        border: 1px solid #ddd;
        padding: 5px;
        font-size: 12px;
        background: white;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.2s;
    }

    .month-view-day:hover {
        background-color: #f8f9fa;
    }

    .month-view-day.today {
        background-color: #e3f2fd;
        border-color: #2196f3;
    }

    .month-view-day.empty-day {
        background-color: #f9f9f9;
        cursor: default;
    }

    .month-view-day.empty-day:hover {
        background-color: #f9f9f9;
    }

    .month-view-day-number {
        font-weight: bold;
        margin-bottom: 3px;
        color: #333;
    }

    .month-view-events {
        display: flex;
        flex-direction: column;
        gap: 2px;
    }

    .month-view-event {
        background: #e1f5fe;
        border-left: 2px solid #03a9f4;
        padding: 2px 4px;
        border-radius: 2px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        font-size: 10px;
        line-height: 1.2;
    }

    .event-time {
        font-weight: bold;
        color: #0277bd;
    }

    .event-room {
        color: #666;
    }

    .event-teacher {
        color: #666;
        font-style: italic;
    }

    .more-events {
        background: #f5f5f5;
        border-left-color: #999;
        text-align: center;
        font-weight: bold;
        color: #666;
    }
    .calendar-actions {
        display: flex;
        gap: 20px;
        justify-content: space-around;
        margin: 20px 0;
    }

    .action-item {
        background-color: #f0f4ff;
        padding: 15px 25px;
        border-radius: 12px;
        text-align: center;
        font-weight: 600;
        color: #2c3e50;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
        cursor: pointer;
        transition: all 0.3s ease;
        min-width: 160px;
    }

    .action-item:hover {
        background-color: #d0e2ff;
        transform: translateY(-3px);
        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
    }

</style>

<div class="container-fluid">


    <!-- Calendar Container -->
    <div class="calendar-container">
        <!-- Hiện ngày -->
        <div class="calendar-header">
            <div class="calendar-navigation d-flex">

                <!-- Nút prev -->
                <form action="quan-ly-lich-hoc" method="post" class="me-2">
                    <input type="hidden" name="dayLabel" value="${dayLabel}" />
                    <input type="hidden" name="view" value="${currentView}" />
                    <input type="hidden" name="direction" value="prev" />
                    <input type="hidden" name="roomSelected" value="${not empty roomSelected ? roomSelected : 'All'}">
                    <input type="hidden" name="teacherSelected" value="${not empty teacherSelected ? teacherSelected : 'All'}">
                    <input type="hidden" name="courseSelected" value="${not empty courseSelected ? courseSelected : 'All'}">
                    <button type="submit" class="btn btn-outline-secondary" id="prevBtn">
                        <i class="fa fa-chevron-left"></i>
                    </button>
                </form>

                <!-- Ngày hiện tại -->
                <fmt:formatDate var="dayLabelFormatted" value="${dayLabel}" pattern="'Ngày' d 'tháng' M 'năm' yyyy" />
                <span class="mx-3 h5 d-inline-flex align-items-center" id="currentViewLabel">
                    ${dayLabelFormatted}
                    <form action="quan-ly-lich-hoc" method="post" class="d-inline-block position-relative" style="margin-left: 20px">
                        <input type="hidden" name="dayLabel" value="${dayLabel}" />
                        <input type="hidden" name="view" value="${currentView}" />
                        <input type="hidden" name="roomSelected" value="${not empty roomSelected ? roomSelected : 'All'}" />
                        <input type="hidden" name="teacherSelected" value="${not empty teacherSelected ? teacherSelected : 'All'}" />
                        <input type="hidden" name="courseSelected" value="${not empty courseSelected ? courseSelected : 'All'}">
                        <i class="bi bi-calendar-date fs-5 text-primary"></i>
                        <input type="date" name="dateChoose"
                               class="position-absolute top-0 start-0"
                               style="opacity: 0; width: 1.5em; height: 1.5em; cursor: pointer;"
                               onchange="this.form.submit()" />
                    </form>
                </span>




                <!-- Nút next -->
                <form action="quan-ly-lich-hoc" method="post" class="ms-2">
                    <input type="hidden" name="dayLabel" value="${dayLabel}" />
                    <input type="hidden" name="view" value="${currentView}" />
                    <input type="hidden" name="direction" value="next" />
                    <input type="hidden" name="roomSelected" value="${not empty roomSelected ? roomSelected : 'All'}">
                    <input type="hidden" name="teacherSelected" value="${not empty teacherSelected ? teacherSelected : 'All'}">
                    <input type="hidden" name="courseSelected" value="${not empty courseSelected ? courseSelected : 'All'}">
                    <button type="submit" class="btn btn-outline-secondary" id="nextBtn">
                        <i class="fa fa-chevron-right"></i>
                    </button>
                </form>

                <!-- Nút hôm nay -->
                <form action="quan-ly-lich-hoc" method="post" class="ms-3">

                    <input type="hidden" name="dayLabel" value="<%= java.time.LocalDate.now().toString()%>" />
                    <input type="hidden" name="view" value="day" />
                    <input type="hidden" name="roomSelected" value="${not empty roomSelected ? roomSelected : 'All'}">
                    <input type="hidden" name="teacherSelected" value="${not empty teacherSelected ? teacherSelected : 'All'}">
                    <input type="hidden" name="courseSelected" value="${not empty courseSelected ? courseSelected : 'All'}">
                    <button type="submit" class="btn btn-outline-primary" id="todayBtn">Hôm nay</button>
                </form>
            </div>

            <div class="calendar-view-options">
                <div class="calendar-view-options">
                    <form action="quan-ly-lich-hoc" method="post" class="d-flex">
                        <input type="hidden" name="dayLabel" value="${dayLabel}" />
                        <input type="hidden" name="roomSelected" value="${not empty roomSelected ? roomSelected : 'All'}">
                        <input type="hidden" name="teacherSelected" value="${not empty teacherSelected ? teacherSelected : 'All'}">
                        <input type="hidden" name="courseSelected" value="${not empty courseSelected ? courseSelected : 'All'}">
                        <button class="btn btn-outline-secondary me-2 ${currentView == 'day' ? 'active' : ''}" name="view" value="day">Ngày</button>
                        <button class="btn btn-outline-secondary me-2 ${currentView == 'week' || currentView == null ? 'active' : ''}" name="view" value="week">Tuần</button>
                        <button class="btn btn-outline-secondary ${currentView == 'month' ? 'active' : ''}" name="view" value="month">Tháng</button>
                    </form>
                </div>
            </div>
        </div>


        <!-- Danh sách giáo viên , phòng , khóa học để lọc  -->
        <div class="calendar-filters">
            <form id="filterForm" class="row g-3" action="quan-ly-lich-hoc" method="post">
                <input type="hidden" name="dayLabel" value="${dayLabel}" />
                <input type="hidden" name="view" value="${currentView}" />
                <div class="col-md-3">
                    <label class="form-label">Giáo viên</label>
                    <select class="form-select" name="teacherSelected">
                        <option value="All" ${teacherSelected == null ? 'selected' : ''}>Tất cả giáo viên</option>
                        <c:forEach items="${teachers}" var="teacher">
                            <option value="${teacher.id}" 
                                    <c:if test="${teacher.id == teacherSelected}">selected</c:if>>
                                ${teacher.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Phòng học</label>
                    <select class="form-select" name="roomSelected">
                        <option value="All" ${roomSelected ==null ? 'selected' : ''}>Tất cả phòng học</option>
                        <c:forEach items="${rooms}" var="room">
                            <option value="${room.id}"
                                    <c:if test="${room.id == roomSelected}">selected</c:if>>${room.roomName}
                                    </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Môn học </label>
                    <select class="form-select" name="courseSelected">
                        <option value="All" ${courseSelected == null ? 'selected' : ''}>Tất cả môn học</option>
                        <c:forEach items="${courses}" var="c">
                            <option value="${c.id}"
                                    <c:if test="${c.id == courseSelected}">selected</c:if>>${c.name}
                                    </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">Lọc</button>

                </div>
                <div class="col-md-6 d-flex align-items-end gap-2">
                    <button type="button" class="btn btn-warning w-50" data-bs-toggle="modal" data-bs-target="#changeScheduleModal">
                        Đổi lịch / Giáo viên
                    </button>
                </div>
            </form>
        </div>

        <!-- Lịch học -->
        <div class="calendar-body">
            <c:if test="${not empty message}">
                <div>

                </div>
            </c:if>
            <!-- Ngày hiện trên bảng  -->
            <c:choose>
                <c:when test="${currentView == 'month'}">
                    <div style="font-size: 24px; font-weight: bold; text-align: center; margin: 20px 0; color: #2c3e50;">
                        Lịch học trong tháng ${monthNumber} năm ${year}
                    </div>
                </c:when>
                <c:when test="${currentView == 'week'}">
                    <div style="font-size: 24px; font-weight: bold; text-align: center; margin: 20px 0; color: #2c3e50;">
                        Lịch học của tuần trong tháng ${monthNumber}
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="font-size: 24px; font-weight: bold; text-align: center; margin: 20px 0; color: #2c3e50;">
                        <fmt:formatDate var="dayLabelFormatted" value="${dayLabel}" pattern="'Ngày' d 'tháng' M 'năm' yyyy" />
                        Lịch học của ${dayLabelFormatted}
                    </div>
                </c:otherwise>
            </c:choose>


            <!-- Giao diện xem theo tuần -->
            <div id="weekView" class="calendar-view active">
                <div class="calendar-grid">
                    <!-- Tiêu đề các ngày trong tuần -->
                    <div class="calendar-day-header">Thứ Hai</div>
                    <div class="calendar-day-header">Thứ Ba</div>
                    <div class="calendar-day-header">Thứ Tư</div>
                    <div class="calendar-day-header">Thứ Năm</div>
                    <div class="calendar-day-header">Thứ Sáu</div>
                    <div class="calendar-day-header">Thứ Bảy</div>
                    <div class="calendar-day-header">Chủ Nhật</div>

                    <!-- Hiển thị từng ngày trong tuần -->
                    <c:forEach var="currentDate" items="${currentWeekDates}" varStatus="day">
                        <!-- Đánh dấu ngày hôm nay -->
                        <div class="calendar-day ${currentDate == fn:substring(dayLabel, 0, 10) ? 'today' : ''}">
                            <!-- Hiển thị số ngày (2 chữ số cuối) -->
                            <div class="calendar-day-number">${fn:substring(currentDate, 8, 10)}</div>

                            <!-- Hiển thị các section đúng ngày -->
                            <c:forEach var="section" items="${sections}">
                                <c:if test="${section.dateFormatted == currentDate}">
                                    <c:set var="statusClass" value="" />
                                    <c:choose>
                                        <c:when test="${section.status == 'completed'}">
                                            <c:set var="statusClass" value="bg-success text-white" />
                                        </c:when>
                                        <c:when test="${section.status == 'active'}">
                                            <c:set var="statusClass" value="bg-primary text-white" />
                                        </c:when>
                                        <c:when test="${section.status == 'inactive'}">
                                            <c:set var="statusClass" value="bg-danger text-white" />
                                        </c:when>
                                    </c:choose>

                                    <div class="calendar-event ${statusClass}">
                                        <strong>${section.startTimeFormatted} - ${section.endTimeFormatted}</strong><br>
                                        P.${section.classroom}<br>
                                        <small>GV: ${section.teacherName}</small>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Giao diện xem theo ngày -->
            <div id="dayView" class="calendar-view" style="display: none;">
                <div class="time-slots-container">
                    <c:forEach begin="7" end="21" var="hour">
                        <div class="time-slot">
                            <div class="time-label">${hour}:00</div>
                            <div class="day-view-events">
                                <c:set var="displayedEvents" value="" />
                                <c:forEach var="section" items="${sections}">
                                    <c:if test="${section.dateFormatted == fn:substring(dayLabel, 0, 10)}">
                                        <c:set var="sectionHour" value="${fn:split(section.startTimeFormatted, ':')[0]}" />
                                        <c:if test="${sectionHour == hour}">
                                            <c:set var="eventIdentifier" value="${section.startTimeFormatted}_${section.endTimeFormatted}_${section.teacherName}" />
                                            <c:if test="${!fn:contains(displayedEvents, eventIdentifier)}">
                                                <c:set var="statusClass" value="" />
                                                <c:choose>
                                                    <c:when test="${section.status == 'completed'}">
                                                        <c:set var="statusClass" value="bg-success text-white" />
                                                    </c:when>
                                                    <c:when test="${section.status == 'active'}">
                                                        <c:set var="statusClass" value="bg-primary text-white" />
                                                    </c:when>
                                                    <c:when test="${section.status == 'inactive'}">
                                                        <c:set var="statusClass" value="bg-danger text-white" />
                                                    </c:when>
                                                </c:choose>

                                                <div class="calendar-event ${statusClass}">
                                                    <strong>${section.startTimeFormatted} - ${section.endTimeFormatted}</strong> |
                                                    ${section.courseName} - ${section.classroom} |
                                                    GV: ${section.teacherName}
                                                </div>
                                                <c:set var="displayedEvents" value="${displayedEvents},${eventIdentifier}" />
                                            </c:if>
                                        </c:if>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>


            <!-- Giao diện xem theo tháng -->
            <div id="monthView" class="calendar-view" style="display: none;">
                <div class="month-view-grid" id="monthGrid">
                    <!-- Tiêu đề các ngày trong tuần -->
                    <div class="calendar-day-header">Thứ Hai</div>
                    <div class="calendar-day-header">Thứ Ba</div>
                    <div class="calendar-day-header">Thứ Tư</div>
                    <div class="calendar-day-header">Thứ Năm</div>
                    <div class="calendar-day-header">Thứ Sáu</div>
                    <div class="calendar-day-header">Thứ Bảy</div>
                    <div class="calendar-day-header">Chủ Nhật</div>

                    <!-- Ô rỗng đầu tháng (firstDayOfMonth: 1=Thứ 2, ... 7=Chủ Nhật) -->
                    <c:forEach begin="1" end="${firstDayOfMonth - 1}" var="i">
                        <div class="month-view-day empty-day"></div>
                    </c:forEach>

                    <!-- Hiển thị các ngày trong tháng -->
                    <c:forEach begin="1" end="${daysInMonth}" var="day">
                        <!-- Tạo định dạng ngày yyyy-MM-dd -->
                        <c:set var="currentDate" value="${currentMonth}-${day < 10 ? '0' : ''}${day}" />

                        <div class="month-view-day ${currentDate == fn:substring(dayLabel, 0, 10) ? 'today' : ''}">
                            <div class="month-view-day-number">${day}</div>

                            <!-- Hiển thị sự kiện theo ngày -->
                            <c:forEach var="section" items="${sections}">
                                <c:if test="${section.dateFormatted == currentDate}">
                                    <c:set var="statusClass" value="" />
                                    <c:choose>
                                        <c:when test="${section.status == 'completed'}">
                                            <c:set var="statusClass" value="bg-success text-white" />
                                        </c:when>
                                        <c:when test="${section.status == 'active'}">
                                            <c:set var="statusClass" value="bg-primary text-white" />
                                        </c:when>
                                        <c:when test="${section.status == 'inactive'}">
                                            <c:set var="statusClass" value="bg-danger text-white" />
                                        </c:when>
                                    </c:choose>

                                    <div class="month-view-event ${statusClass}">
                                        <strong>${section.startTimeFormatted} - ${section.endTimeFormatted}</strong><br>
                                        P.${section.classroom}<br>
                                        <small>GV: ${section.teacherName}</small>
                                    </div>

                                </c:if>
                            </c:forEach>
                        </div>
                    </c:forEach>

                </div>
            </div>

        </div>

        <!-- Modal đổi lịch / giáo viên -->
        <div class="modal fade" id="changeScheduleModal" tabindex="-1" aria-labelledby="changeScheduleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title" id="changeScheduleModalLabel">Đổi lịch học / giáo viên</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>

                    <form action="quan-ly-lich-hoc" method="post">
                        <input type="hidden" name="dayLabel" value="${dayLabel}" />
                        <input type="hidden" name="view" value="${currentView}" />
                        <input type="hidden" name="roomSelected" value="${not empty roomSelected ? roomSelected : 'All'}">
                        <input type="hidden" name="teacherSelected" value="${not empty teacherSelected ? teacherSelected : 'All'}">
                        <input type="hidden" name="courseSelected" value="${not empty courseSelected ? courseSelected : 'All'}">
                        <div class="modal-body">

                            <!-- Chọn loại đổi -->
                            <select class="form-select" id="changeType" name="changeType">
                                <option value="changeSchedule">Đổi lịch học</option>
                                <option value="changeTeacher">Đổi giáo viên</option>
                                <option value="changeRoom">Đổi phòng học</option>
                            </select>


                            <!-- === ĐỔI LỊCH HỌC === -->
                            <div id="changeScheduleForm" style="display: block;">
                                <!-- Chọn kiểu đổi lịch -->
                                <div class="mb-3">
                                    <label class="form-label">Chọn cách đổi lịch:</label>
                                    <select class="form-select" id="scheduleChangeMode" name="scheduleChangeMode">
                                        <option value="byTeacher">Đổi theo giáo viên</option>
                                        <option value="byDate">Đổi theo ngày</option>
                                    </select>
                                </div>

                                <!-- Đổi theo giáo viên -->
                                <div id="changeScheduleByTeacher" style="display: block;">
                                    <div class="mb-3">
                                        <label class="form-label">Chọn giáo viên:</label>
                                        <select class="form-select" id="scheduleTeacherSelect" name="teacherId"
                                                onchange="filterScheduleOptions('schedule', this.value)">
                                            <c:forEach items="${teachers}" var="t">
                                                <option value="${t.id}">${t.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Chọn lịch cần đổi:</label>
                                        <select class="form-select" name="scheduleId" id="scheduleOriginalSelect">
                                            <c:forEach items="${sections}" var="s">
                                                <option value="${s.id}" data-teacher="${s.teacherId}">
                                                    ${s.classroom} - ${s.startTimeFormatted} - ${s.endTimeFormatted} || ${s.dateFormatted}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Chọn ngày mới:</label>
                                        <input type="date" class="form-control" name="newDate" />
                                    </div>

                                </div>

                                <!-- Đổi toàn bộ lịch theo ngày -->
                                <div id="changeScheduleByDate" style="display: none;">
                                    <div class="mb-3">
                                        <label class="form-label">Ngày cần đổi:</label>
                                        <input type="date" class="form-control" name="sourceDate" />
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Chuyển sang ngày:</label>
                                        <input type="date" class="form-control" name="targetDate" />
                                    </div>
                                </div>
                            </div>

                            <!-- === ĐỔI GIÁO VIÊN === -->
                            <div id="changeTeacherForm" style="display: none;">
                                <div class="mb-3">
                                    <label class="form-label">Chọn giáo viên:</label>
                                    <select class="form-select" id="scheduleTeacherSelect" name="teacherId"
                                            onchange="filterScheduleOptions('schedule', this.value)">
                                        <c:forEach items="${teachers}" var="t">
                                            <option value="${t.id}">${t.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Chọn lịch cần đổi:</label>
                                    <select class="form-select" name="scheduleId" id="scheduleOriginalSelect">
                                        <c:forEach items="${sections}" var="s">
                                            <option value="${s.id}" data-teacher="${s.teacherId}">
                                                ${s.classroom} - ${s.startTimeFormatted} - ${s.endTimeFormatted} || ${s.dateFormatted}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Chọn giáo viên thay thế:</label>
                                    <select class="form-select" name="newTeacherId" id="newTeacherSelect">
                                        <c:forEach items="${teachers}" var="t">
                                            <option value="${t.id}">${t.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>


                            <!-- === ĐỔI PHÒNG HỌC === -->
                            <div id="changeRoomForm" style="display: none;">
                                <div class="mb-3">
                                    <label class="form-label">Chọn giáo viên:</label>
                                    <select class="form-select" id="scheduleTeacherSelect" name="teacherId"
                                            onchange="filterScheduleOptions('schedule', this.value)">
                                        <c:forEach items="${teachers}" var="t">
                                            <option value="${t.id}">${t.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Chọn lịch cần đổi:</label>
                                    <select class="form-select" name="scheduleId" id="scheduleOriginalSelect">
                                        <c:forEach items="${sections}" var="s">
                                            <option value="${s.id}" data-teacher="${s.teacherId}">
                                                ${s.classroom} - ${s.startTimeFormatted} - ${s.endTimeFormatted} || ${s.dateFormatted}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Chọn phòng học mới:</label>
                                    <select class="form-select" name="newRoomId">
                                        <c:forEach items="${rooms}" var="room">
                                            <option value="${room.id}">${room.roomName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                        </div>

                        <div class="modal-footer">
                            <input type="hidden" name="action" value="change_request" />
                            <button type="submit" class="btn btn-success">Xác nhận thay đổi</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        </div>
                    </form>

                </div>
            </div>
        </div>

    </div>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            let currentView = '${currentView}'; // Giá trị do servlet set

            const weekView = document.getElementById('weekView');
            const dayView = document.getElementById('dayView');
            const monthView = document.getElementById('monthView');

            // Hàm chuyển view
            function updateCalendarView() {
                weekView.style.display = 'none';
                dayView.style.display = 'none';
                monthView.style.display = 'none';

                if (currentView === 'day') {
                    dayView.style.display = 'block';
                } else if (currentView === 'week') {
                    weekView.style.display = 'block';
                } else if (currentView === 'month') {
                    monthView.style.display = 'block';
                }
            }

            // Gọi cập nhật sau khi load
            updateCalendarView();

            const changeTypeSelect = document.getElementById("changeType");
            const changeScheduleForm = document.getElementById("changeScheduleForm");
            const changeTeacherForm = document.getElementById("changeTeacherForm");
            const changeRoomForm = document.getElementById("changeRoomForm");

            const scheduleChangeMode = document.getElementById("scheduleChangeMode");
            const changeScheduleByTeacher = document.getElementById("changeScheduleByTeacher");
            const changeScheduleByDate = document.getElementById("changeScheduleByDate");

            // Xử lý hiển thị form theo loại thay đổi
            function handleChangeType() {
                const val = changeTypeSelect.value;

                changeScheduleForm.style.display = (val === "changeSchedule") ? "block" : "none";
                changeTeacherForm.style.display = (val === "changeTeacher") ? "block" : "none";
                changeRoomForm.style.display = (val === "changeRoom") ? "block" : "none";
            }

            // Xử lý hiển thị theo kiểu đổi lịch học (giáo viên hoặc ngày)
            function handleScheduleMode() {
                const mode = scheduleChangeMode.value;

                changeScheduleByTeacher.style.display = (mode === "byTeacher") ? "block" : "none";
                changeScheduleByDate.style.display = (mode === "byDate") ? "block" : "none";
            }

            // Gọi lại khi thay đổi
            changeTypeSelect.addEventListener("change", handleChangeType);
            scheduleChangeMode.addEventListener("change", handleScheduleMode);

            // Gọi ngay lần đầu để khởi tạo đúng trạng thái
            handleChangeType();
            handleScheduleMode();

            document.getElementById("hiddenDateInput").addEventListener("change", function () {
                this.form.submit();
            });
        });

        function filterScheduleOptions(selectIdPrefix, selectedTeacherId) {
            const scheduleSelect = document.getElementById(selectIdPrefix + 'OriginalSelect');
            const options = scheduleSelect.options;

            for (let i = 0; i < options.length; i++) {
                const option = options[i];
                const teacherId = option.getAttribute("data-teacher");

                if (!selectedTeacherId || teacherId === selectedTeacherId) {
                    option.style.display = "block";
                } else {
                    option.style.display = "none";
                }
            }

            // Nếu option đang chọn bị ẩn đi thì bỏ chọn
            if (scheduleSelect.selectedOptions.length > 0 &&
                    scheduleSelect.selectedOptions[0].style.display === "none") {
                scheduleSelect.selectedIndex = -1;
            }
        }
    </script>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <jsp:include page="layout/footer.jsp" />
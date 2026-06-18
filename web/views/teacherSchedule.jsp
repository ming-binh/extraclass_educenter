<%-- 
    Document   : teacherSchedule.jsp
    Created on : May 23, 2025, 4:29:19 AM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
    // Tính toán tuần hiện tại
    java.time.LocalDate targetWeek = (java.time.LocalDate) request.getAttribute("targetWeek");
    if (targetWeek == null) {
        targetWeek = java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY);
    }
    
    java.util.List<String> weekDays = new java.util.ArrayList<>();
    for (int i = 0; i < 7; i++) {
        java.time.LocalDate day = targetWeek.plusDays(i);
        weekDays.add(day.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
    request.setAttribute("currentWeekDays", weekDays);
    request.setAttribute("currentMonday", targetWeek);
%>

<!DOCTYPE html>
<% request.setAttribute("title", "Thời khóa biểu giáo viên");%>

<jsp:include page="layout/header.jsp" />

<style>
/* Teacher Schedule Styles */
.ts-schedule-header {
    background: white;
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    border-bottom: 1px solid #e1e8ed;
    padding: 1.5rem 0;
}
.ts-schedule-header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.ts-schedule-header h1 {
    font-size: 2rem;
    font-weight: 700;
    color: #333;
    margin-bottom: 0.5rem;
}
.ts-schedule-header p {
    color: #666;
    font-size: 0.95rem;
}
.ts-schedule-main {
    max-width: 1200px;
    margin: 2rem auto;
    padding: 0 2rem;
}
.ts-schedule-container {
    background: white;
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    overflow: hidden;
    border: 1px solid #e1e8ed;
}
.ts-schedule-header-info {
    padding: 2rem;
    border-bottom: 1px solid #e1e8ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: linear-gradient(135deg, rgba(102,126,234,0.05), rgba(118,75,162,0.05));
}
.ts-user-info {
    display: flex;
    align-items: center;
    gap: 1rem;
}
.ts-user-avatar {
    width: 60px;
    height: 60px;
    background: linear-gradient(135deg, #667eea, #764ba2);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-weight: bold;
    font-size: 1.5rem;
}
.ts-user-details h2 {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 0.25rem;
}
.ts-user-details p {
    color: #666;
}
.ts-week-navigator {
    display: flex;
    align-items: center;
    gap: 1rem;
}
.ts-nav-btn {
    width: 40px;
    height: 40px;
    border: none;
    background: white;
    border-radius: 8px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.ts-nav-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
.ts-week-info {
    font-weight: 500;
    color: #333;
    min-width: 120px;
    text-align: center;
}
.ts-schedule-grid {
    padding: 2rem;
}
.ts-schedule-table {
    display: grid;
    grid-template-columns: 120px repeat(7, 1fr);
    gap: 0.5rem;
}
.ts-time-header, .ts-day-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    color: #333;
    border-radius: 12px;
    font-size: 0.9rem;
}
.ts-time-header {
    background: #f8f9ff;
    border: 1px solid #e1e8ed;
}
.ts-day-header {
    background: linear-gradient(135deg, rgba(102,126,234,0.1), rgba(118,75,162,0.1));
    border: 1px solid rgba(102,126,234,0.2);
}
.ts-time-slot {
    height: 140px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 0.85rem;
    color: #495057;
    background: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    font-weight: 600;
    text-align: center;
}
.ts-schedule-cell {
    height: 140px;
    padding: 12px;
    background: white;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    position: relative;
}
.ts-lesson {
    background: #e3f2fd;
    border: 1px solid #bbdefb;
    border-radius: 6px;
    padding: 10px;
    margin-bottom: 8px;
    font-size: 0.85rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s ease;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: 0;
}
.ts-lesson:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}
.ts-lesson-title {
    font-weight: 600;
    color: #1976d2;
    margin-bottom: 4px;
    font-size: 0.9rem;
    line-height: 1.3;
}
.ts-lesson-subject {
    color: #424242;
    font-weight: 500;
    margin-bottom: 3px;
}
.ts-lesson-grade {
    color: #666;
    font-size: 0.8rem;
    margin-bottom: 3px;
}
.ts-lesson-time {
    color: #1976d2;
    font-weight: 500;
    font-size: 0.8rem;
    margin-bottom: 3px;
}
.ts-lesson-room {
    color: #666;
    font-size: 0.8rem;
    margin-bottom: 3px;
}
.ts-lesson-students {
    color: #666;
    font-size: 0.8rem;
}

/* Course Type Colors */
.ts-lesson-type {
    background: #e3f2fd;
    border-color: #2196f3;
}
.ts-lesson-type .ts-lesson-title {
    color: #1976d2;
}
.ts-lesson-type .ts-lesson-time {
    color: #1976d2;
}

.ts-meeting-type {
    background: #f3e5f5;
    border-color: #9c27b0;
}
.ts-meeting-type .ts-lesson-title {
    color: #7b1fa2;
}
.ts-meeting-type .ts-lesson-time {
    color: #7b1fa2;
}

.ts-extra-type {
    background: #e8f5e8;
    border-color: #4caf50;
}
.ts-extra-type .ts-lesson-title {
    color: #388e3c;
}
.ts-extra-type .ts-lesson-time {
    color: #388e3c;
}

.ts-admin-type {
    background: #fff3e0;
    border-color: #ff9800;
}
.ts-admin-type .ts-lesson-title {
    color: #f57c00;
}
.ts-admin-type .ts-lesson-time {
    color: #f57c00;
}

/* Legend Styles */
.ts-legend {
    background: #f8f9fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e9ecef;
}
.ts-legend-title {
    font-weight: 600;
    margin-bottom: 10px;
    color: #495057;
}
.ts-legend-items {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
}
.ts-legend-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 0.9rem;
}
.ts-legend-color {
    width: 16px;
    height: 16px;
    border-radius: 3px;
    border: 1px solid #dee2e6;
}

/* Responsive Design */
@media (max-width: 1200px) {
    .ts-schedule-table {
        grid-template-columns: 100px repeat(7, 1fr);
        min-width: 800px;
    }
    .ts-time-slot {
        font-size: 0.8rem;
        padding: 10px 5px;
        height: 130px;
    }
    .ts-day-header {
        font-size: 0.85rem;
        padding: 10px 5px;
        height: 50px;
    }
    .ts-schedule-cell {
        height: 130px;
        padding: 10px;
    }
    .ts-lesson {
        padding: 8px;
        font-size: 0.8rem;
        height: calc(100% - 12px);
    }
    .ts-lesson-title {
        font-size: 0.85rem;
    }
}

@media (max-width: 768px) {
    .ts-schedule-main {
        padding: 0 1rem;
        overflow-x: auto;
    }
    .ts-schedule-container {
        min-width: 900px;
    }
    .ts-schedule-table {
        grid-template-columns: 80px repeat(7, 1fr);
        min-width: 900px;
    }
    .ts-time-slot {
        font-size: 0.75rem;
        padding: 8px 4px;
        height: 120px;
    }
    .ts-day-header {
        font-size: 0.8rem;
        padding: 8px 4px;
        height: 45px;
    }
    .ts-schedule-cell {
        height: 120px;
        padding: 8px;
    }
    .ts-lesson {
        padding: 8px;
        font-size: 0.75rem;
        height: calc(100% - 10px);
    }
    .ts-lesson-title {
        font-size: 0.8rem;
    }
    .ts-lesson-subject {
        font-size: 0.75rem;
    }
    .ts-lesson-grade {
        font-size: 0.7rem;
    }
    .ts-lesson-time {
        font-size: 0.7rem;
    }
    .ts-lesson-room {
        font-size: 0.7rem;
    }
    .ts-lesson-students {
        font-size: 0.7rem;
    }
}
</style>

<!-- Header Section -->
<div class="ts-schedule-header">
    <div class="ts-schedule-header-container">
        <div>
            <h1>Thời khóa biểu giáo viên</h1>
            <p>Xem lịch giảng dạy của bạn</p>
        </div>
    </div>
</div>

<div class="ts-schedule-main">
    <div class="ts-schedule-container">
        <div class="ts-schedule-header-info">
            <div class="ts-user-info">
                <div class="ts-user-avatar" id="user-avatar">
                    <c:if test="${not empty account}">${account.name.charAt(0)}</c:if>
                </div>
                <div class="ts-user-details">
                    <h2 id="user-name">
                        <c:if test="${not empty account}">${account.name}</c:if>
                    </h2>
                    <p id="user-info">
                        <c:if test="${not empty scheduleData.teacherInfo}">
                            ${scheduleData.teacherInfo.subject}
                            <c:if test="${not empty scheduleData.teacherInfo.experience}">
                                - ${scheduleData.teacherInfo.experience} năm kinh nghiệm
                            </c:if>
                        </c:if>
                    </p>
                </div>
            </div>
            <div class="ts-week-navigator">
                <button class="ts-nav-btn" onclick="previousWeek()">←</button>
                <span class="ts-week-info">
                    <c:if test="${not empty currentWeekDays and not empty currentWeekDays[0]}">
                        Tuần từ ${currentWeekDays[0]}
                    </c:if>
                </span>
                <button class="ts-nav-btn" onclick="nextWeek()">→</button>
            </div>
        </div>
        
        <c:choose>
            <c:when test="${not empty scheduleData.schedule}">
        <div class="ts-schedule-grid">
            <div class="ts-schedule-table" id="schedule-table">
                        <!-- Header row -->
                        <div class="ts-time-header">Tiết học</div>
                        <div class="ts-day-header">Thứ 2</div>
                        <div class="ts-day-header">Thứ 3</div>
                        <div class="ts-day-header">Thứ 4</div>
                        <div class="ts-day-header">Thứ 5</div>
                        <div class="ts-day-header">Thứ 6</div>
                        <div class="ts-day-header">Thứ 7</div>
                        <div class="ts-day-header">Chủ nhật</div>
                        
                        <!-- Tạo danh sách time slots (3 tiếng mỗi slot) -->
                        <c:set var="timeSlots" value="" />
                        <c:forEach var="hour" begin="7" end="19" step="3">
                            <c:set var="startHour" value="${hour}" />
                            <c:set var="endHour" value="${hour + 3}" />
                            <c:set var="timeSlot" value="${startHour < 10 ? '0' : ''}${startHour}:00:00 - ${endHour < 10 ? '0' : ''}${endHour}:00:00" />
                            <c:set var="timeSlots" value="${timeSlots}${timeSlot}," />
                        </c:forEach>
                        <c:forTokens var="timeSlot" items="${timeSlots}" delims=",">
                            <div class="ts-time-slot">${timeSlot}</div>
                            
                            <!-- Hiển thị section cho từng ngày -->
                            <c:forEach var="day" items="${['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']}">
                                <div class="ts-schedule-cell">
                                    <c:forEach var="section" items="${scheduleData.schedule[day]}">
                                        <c:set var="sectionDate" value="${section.dateTime}" />
                                        <c:if test="${not empty sectionDate}">
                                            <fmt:parseDate value="${sectionDate}" pattern="yyyy-MM-dd HH:mm:ss" var="parsedDate" />
                                            <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" var="formattedDate" />
                                            <fmt:formatDate value="${parsedDate}" pattern="EEEE" var="actualDayOfWeek" />
                                            
                                            <!-- Kiểm tra section có thuộc tuần hiện tại và đúng ngày không -->
                                            <c:set var="isCurrentWeek" value="false" />
                                            <c:forEach var="weekDay" items="${currentWeekDays}">
                                                <c:if test="${weekDay eq formattedDate}">
                                                    <c:set var="isCurrentWeek" value="true" />
                                                </c:if>
                                            </c:forEach>
                                            
                                            <!-- Kiểm tra section có thuộc time slot hiện tại không -->
                                            <c:set var="sectionStartTime" value="${section.startTime}" />
                                            <c:set var="sectionEndTime" value="${section.endTime}" />
                                            <c:set var="belongsToTimeSlot" value="false" />
                                            
                                            <c:if test="${not empty sectionStartTime and not empty sectionEndTime}">
                                                <!-- Parse time slot string để lấy start và end hour -->
                                                <c:set var="timeSlotStart" value="${fn:substring(timeSlot, 0, 2)}" />
                                                <c:set var="timeSlotEnd" value="${fn:substring(timeSlot, 11, 13)}" />
                                                
                                                <!-- Parse section start time -->
                                                <c:set var="sectionStartHour" value="${fn:substring(sectionStartTime, 0, 2)}" />
                                                <c:set var="sectionEndHour" value="${fn:substring(sectionEndTime, 0, 2)}" />
                                                
                                                <!-- Kiểm tra xem section có thuộc time slot này không -->
                                                <c:if test="${sectionStartHour >= timeSlotStart and sectionStartHour < timeSlotEnd}">
                                                    <c:set var="belongsToTimeSlot" value="true" />
                                                </c:if>
                                            </c:if>
                                            
                                            <!-- Hiển thị section nếu thuộc tuần hiện tại, đúng ngày và đúng time slot -->
                                            <c:if test="${isCurrentWeek and actualDayOfWeek eq day and belongsToTimeSlot}">
                                                <!-- Ánh xạ subject từ tiếng Anh sang tiếng Việt -->
                                                <c:set var="subjectVi" value="" />
                                                <c:choose>
                                                    <c:when test="${section.subject eq 'Mathematics'}">
                                                        <c:set var="subjectVi" value="Toán học" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Physics'}">
                                                        <c:set var="subjectVi" value="Vật lý" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Chemistry'}">
                                                        <c:set var="subjectVi" value="Hóa học" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Biology'}">
                                                        <c:set var="subjectVi" value="Sinh học" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Literature'}">
                                                        <c:set var="subjectVi" value="Ngữ văn" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'History'}">
                                                        <c:set var="subjectVi" value="Lịch sử" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Geography'}">
                                                        <c:set var="subjectVi" value="Địa lý" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'English'}">
                                                        <c:set var="subjectVi" value="Tiếng Anh" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Computer Science'}">
                                                        <c:set var="subjectVi" value="Tin học" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Physical Education'}">
                                                        <c:set var="subjectVi" value="Thể dục" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Art'}">
                                                        <c:set var="subjectVi" value="Mỹ thuật" />
                                                    </c:when>
                                                    <c:when test="${section.subject eq 'Music'}">
                                                        <c:set var="subjectVi" value="Âm nhạc" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="subjectVi" value="${section.subject}" />
                                                    </c:otherwise>
                                                </c:choose>
                                                
                                                <div class="ts-lesson ${section.typeClass}">
                                                    <div class="ts-lesson-subject">${subjectVi}</div>
                                                    <div class="ts-lesson-grade">Lớp ${section.courseGrade}</div>
                                                    <div class="ts-lesson-time">${section.timeSlot}</div>
                                                    <div class="ts-lesson-room">Phòng ${section.classroom}</div>
                                                    <div class="ts-lesson-students">${section.studentCount} học sinh</div>
                                                </div>
                                            </c:if>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </c:forEach>
                        </c:forTokens>
            </div>
        </div>
        <div class="ts-schedule-legend">
            <div class="ts-legend-title">Chú thích:</div>
            <div class="ts-legend-items" id="legend-items">
                        <c:forEach var="legendEntry" items="${scheduleData.legend}">
                            <div class="ts-legend-item">
                                <c:choose>
                                    <c:when test="${legendEntry.key eq 'regular'}">
                                        <div class="ts-legend-color ts-lesson-type"></div>
                                    </c:when>
                                    <c:when test="${legendEntry.key eq 'intensive'}">
                                        <div class="ts-legend-color ts-meeting-type"></div>
                                    </c:when>
                                    <c:when test="${legendEntry.key eq 'remedial'}">
                                        <div class="ts-legend-color ts-extra-type"></div>
                                    </c:when>
                                    <c:when test="${legendEntry.key eq 'advanced'}">
                                        <div class="ts-legend-color ts-admin-type"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="ts-legend-color ts-lesson-type"></div>
                                    </c:otherwise>
                                </c:choose>
                                <span class="ts-legend-text">${legendEntry.value}</span>
                            </div>
                        </c:forEach>
            </div>
        </div>
            </c:when>
            <c:otherwise>
                <div class="ts-no-schedule">
                    <i class="fas fa-calendar-times"></i>
                    <h3>Chưa có thời khóa biểu</h3>
                    <p>Bạn chưa có khóa học nào được sắp xếp lịch giảng dạy.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    let currentWeek = new Date();
    
    <c:if test="${not empty currentMonday}">
        currentWeek = new Date('${currentMonday}');
    </c:if>

    function previousWeek() {
        currentWeek.setDate(currentWeek.getDate() - 7);
        updateWeekDisplay();
        const url = new URL(window.location);
        url.searchParams.set('week', currentWeek.toISOString().split('T')[0]);
        window.location.href = url.toString();
    }

    function nextWeek() {
        currentWeek.setDate(currentWeek.getDate() + 7);
        updateWeekDisplay();
        const url = new URL(window.location);
        url.searchParams.set('week', currentWeek.toISOString().split('T')[0]);
        window.location.href = url.toString();
    }

    function updateWeekDisplay() {
        const weekNumber = Math.ceil((currentWeek.getDate() + new Date(currentWeek.getFullYear(), currentWeek.getMonth(), 1).getDay()) / 7);
        const year = currentWeek.getFullYear();
        const startDate = new Date(currentWeek);
        const endDate = new Date(currentWeek);
        endDate.setDate(endDate.getDate() + 4);
    }

    document.addEventListener('DOMContentLoaded', function() {
        updateWeekDisplay();
    });
</script>

<jsp:include page="layout/footer.jsp" /> 
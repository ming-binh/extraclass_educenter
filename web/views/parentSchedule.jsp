<%-- 
    Document   : parentSchedule.jsp
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
<% request.setAttribute("title", "Thời khóa biểu phụ huynh");%>

<jsp:include page="layout/header.jsp" />

<style>
.ps-parent-header {
    background: white;
    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    border-bottom: 1px solid #e1e8ed;
    padding: 1.5rem 0;
}
.ps-parent-header-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.ps-parent-header h1 {
    font-size: 2rem;
    font-weight: 700;
    color: #333;
    margin-bottom: 0.5rem;
}
.ps-parent-header p {
    color: #666;
    font-size: 0.95rem;
}
.ps-parent-main {
    max-width: 1200px;
    margin: 2rem auto;
    padding: 0 2rem;
}
.ps-child-selector {
    background: white;
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    padding: 2rem;
    margin-bottom: 2rem;
    border: 1px solid #e1e8ed;
}
.ps-child-selector h2 {
    font-size: 1.25rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 1.5rem;
}
.ps-children-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1rem;
}
.ps-child-card {
    padding: 1.5rem;
    border-radius: 12px;
    border: 2px solid #e1e8ed;
    cursor: pointer;
    transition: all 0.3s ease;
    text-align: left;
    background: white;
}
.ps-child-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}
.ps-child-card.selected {
    border-color: #667eea;
    background: linear-gradient(135deg, rgba(102,126,234,0.05), rgba(118,75,162,0.05));
    box-shadow: 0 0 0 3px rgba(102,126,234,0.1);
}
.ps-child-info {
    display: flex;
    align-items: center;
    gap: 1rem;
}
.ps-child-avatar {
    font-size: 2rem;
    width: 50px;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #667eea, #764ba2);
    border-radius: 50%;
    color: white;
}
.ps-child-details h3 {
    font-weight: 600;
    color: #333;
    margin-bottom: 0.25rem;
}
.ps-child-details p {
    color: #666;
    font-size: 0.9rem;
}
.ps-schedule-container {
    background: white;
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    overflow: hidden;
    border: 1px solid #e1e8ed;
    display: none;
}
.ps-schedule-header-info {
    padding: 2rem;
    border-bottom: 1px solid #e1e8ed;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: linear-gradient(135deg, rgba(102,126,234,0.05), rgba(118,75,162,0.05));
}
.ps-user-info {
    display: flex;
    align-items: center;
    gap: 1rem;
}
.ps-user-avatar {
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
.ps-user-details h2 {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 0.25rem;
}
.ps-user-details p {
    color: #666;
}
.ps-week-navigator {
    display: flex;
    align-items: center;
    gap: 1rem;
}
.ps-nav-btn {
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
.ps-nav-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
.ps-week-info {
    font-weight: 500;
    color: #333;
    min-width: 120px;
    text-align: center;
}
.ps-schedule-grid {
    padding: 2rem;
}
.ps-schedule-table {
    display: grid;
    grid-template-columns: 120px repeat(7, 1fr);
    gap: 0.5rem;
}
.ps-time-header, .ps-day-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    color: #333;
    border-radius: 12px;
    font-size: 0.9rem;
}
.ps-time-header {
    background: #f8f9ff;
    border: 1px solid #e1e8ed;
}
.ps-day-header {
    background: linear-gradient(135deg, rgba(102,126,234,0.1), rgba(118,75,162,0.1));
    border: 1px solid rgba(102,126,234,0.2);
}
.ps-time-slot {
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

/* Fixed CSS for schedule cells and lessons */
.ps-schedule-cell {
    position: relative;
    height: 140px;
    padding: 12px;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    background: white;
}

.ps-lesson {
    background: #e3f2fd;
    border: 1px solid #bbdefb;
    border-radius: 6px;
    padding: 8px;
    font-size: 0.8rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s ease;
    height: calc(100% - 6px);
    display: flex;
    flex-direction: column;
    justify-content: center;
    position: absolute;
    top: 2px;
    left: 2px;
    right: 2px;
    bottom: 2px;
    margin: 0;
}

.ps-lesson:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.ps-lesson-subject {
    color: #424242;
    font-weight: 600;
    margin-bottom: 3px;
    font-size: 0.8rem;
    line-height: 1.2;
}

.ps-lesson-teacher {
    color: #666;
    font-size: 0.75rem;
    margin-bottom: 2px;
    line-height: 1.2;
}

.ps-lesson-time {
    color: #1976d2;
    font-weight: 500;
    font-size: 0.75rem;
    margin-bottom: 2px;
    line-height: 1.2;
}

.ps-lesson-room {
    color: #666;
    font-size: 0.75rem;
    line-height: 1.2;
}

/* Status Colors */
.ps-lesson-paid {
    background: #e8f5e8;
    border-color: #c8e6c9;
}
.ps-lesson-paid .ps-lesson-time {
    color: #2e7d32;
}

.ps-lesson-pending {
    background: #fff3e0;
    border-color: #ffcc80;
}
.ps-lesson-pending .ps-lesson-time {
    color: #f57c00;
}

.ps-lesson-overdue {
    background: #ffebee;
    border-color: #ffcdd2;
}
.ps-lesson-overdue .ps-lesson-time {
    color: #c62828;
}

.ps-schedule-legend {
    padding: 1.5rem 2rem 2rem;
    background: #f8f9ff;
    border-top: 1px solid #e1e8ed;
}
.ps-legend-title {
    font-size: 0.9rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 1rem;
}
.ps-legend-items {
    display: flex;
    flex-wrap: wrap;
    gap: 1.5rem;
}
.ps-legend-item {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}
.ps-legend-color {
    width: 16px;
    height: 16px;
    border-radius: 4px;
    border: 1px solid;
}
.ps-legend-text {
    font-size: 0.8rem;
    color: #666;
}

/* No Selection Message */
.ps-no-selection {
    background: white;
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    padding: 4rem 2rem;
    text-align: center;
    border: 1px solid #e1e8ed;
    margin-top: 2rem;
}
.ps-no-selection-content h3 {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 1rem;
}
.ps-no-selection-content p {
    color: #666;
    font-size: 1rem;
    max-width: 400px;
    margin: 0 auto;
}

@media (max-width: 768px) {
    .ps-parent-header-container {
        flex-direction: column;
        gap: 1rem;
        text-align: center;
    }
    .ps-schedule-header-info {
        flex-direction: column;
        gap: 1rem;
        text-align: center;
    }
    .ps-schedule-main {
        padding: 0 1rem;
        overflow-x: auto;
    }
    .ps-schedule-container {
        min-width: 900px;
    }
    .ps-schedule-table {
        grid-template-columns: 80px repeat(7, 1fr);
        min-width: 900px;
        gap: 0.25rem;
    }
    .ps-time-slot {
        height: 120px;
        font-size: 0.75rem;
        padding: 8px 4px;
    }
    .ps-schedule-cell {
        height: 120px;
        padding: 8px;
    }
    .ps-lesson {
        padding: 8px;
        font-size: 0.75rem;
        height: calc(100% - 10px);
    }
    .ps-lesson-subject {
        font-size: 0.75rem;
        margin-bottom: 2px;
    }
    .ps-lesson-teacher,
    .ps-lesson-time,
    .ps-lesson-room {
        font-size: 0.7rem;
        margin-bottom: 1px;
    }
    .ps-children-grid {
        grid-template-columns: 1fr;
    }
}

@keyframes ps-fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
.ps-schedule-container {
    animation: ps-fadeInUp 0.6s ease;
}
.ps-child-card {
    animation: ps-fadeInUp 0.6s ease;
}
</style>

<!-- Header Section -->
<div class="ps-parent-header">
    <div class="ps-parent-header-container">
        <div>
            <h1>Thời khóa biểu phụ huynh</h1>
            <p>Xem thời khóa biểu của con em</p>
        </div>
    </div>
</div>

<div class="ps-parent-main">
    <!-- Child Selector -->
    <div class="ps-child-selector">
        <h2>Chọn con để xem thời khóa biểu</h2>
        <div class="ps-children-grid">
            <c:forEach var="student" items="${students}">
                <div class="ps-child-card ${student.id eq selectedStudentId ? 'selected' : ''}" 
                     onclick="selectChild(${student.id})">
                    <div class="ps-child-info">
                        <div class="ps-child-avatar">${student.name.charAt(0)}</div>
                        <div class="ps-child-details">
                            <h3>${student.name}</h3>
                            <p>Lớp ${student.grade}
                                <c:if test="${not empty student.className}">
                                    - ${student.className}
                                </c:if>
                            </p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Schedule Display -->
    <c:choose>
        <c:when test="${not empty selectedStudentId and not empty scheduleData}">
        <div id="schedule-display" class="ps-schedule-container" style="display: block;">
            <div class="ps-schedule-header-info">
                <div class="ps-user-info">
                    <div class="ps-user-avatar" id="user-avatar">
                        <c:if test="${not empty scheduleData.studentInfo}">${scheduleData.studentInfo.name.charAt(0)}</c:if>
                    </div>
                    <div class="ps-user-details">
                        <h2 id="user-name">
                            <c:if test="${not empty scheduleData.studentInfo}">${scheduleData.studentInfo.name}</c:if>
                        </h2>
                        <p id="user-info">
                            <c:if test="${not empty scheduleData.studentInfo}">
                                Lớp ${scheduleData.studentInfo.grade}
                                <c:if test="${not empty scheduleData.studentInfo.className}">
                                    - ${scheduleData.studentInfo.className}
                                </c:if>
                            </c:if>
                        </p>
                    </div>
                </div>
                <div class="ps-week-navigator">
                    <button class="ps-nav-btn" onclick="previousWeek()">←</button>
                    <span class="ps-week-info">
                        <c:if test="${not empty currentWeekDays and not empty currentWeekDays[0]}">
                            Tuần từ ${currentWeekDays[0]}
                        </c:if>
                    </span>
                    <button class="ps-nav-btn" onclick="nextWeek()">→</button>
                </div>
            </div>
            
            <div class="ps-schedule-grid">
                <div class="ps-schedule-table" id="schedule-table">
                    <!-- Header row -->
                    <div class="ps-time-header">Tiết học</div>
                    <div class="ps-day-header">Thứ 2</div>
                    <div class="ps-day-header">Thứ 3</div>
                    <div class="ps-day-header">Thứ 4</div>
                    <div class="ps-day-header">Thứ 5</div>
                    <div class="ps-day-header">Thứ 6</div>
                    <div class="ps-day-header">Thứ 7</div>
                    <div class="ps-day-header">Chủ nhật</div>
                    
                    <!-- Time slots and lessons -->
                    <c:forEach var="hour" begin="7" end="19" step="3">
                        <c:set var="startHour" value="${hour}" />
                        <c:set var="endHour" value="${hour + 3}" />
                        <c:set var="timeSlot" value="${startHour < 10 ? '0' : ''}${startHour}:00:00 - ${endHour < 10 ? '0' : ''}${endHour}:00:00" />
                        
                        <!-- Time slot header -->
                        <div class="ps-time-slot">${timeSlot}</div>
                        
                        <!-- Days -->
                        <c:forEach var="day" items="${['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']}">
                            <c:set var="found" value="false" />
                            <c:forEach var="section" items="${scheduleData.schedule[day]}">
                                <c:set var="sectionDate" value="${section.dateTime}" />
                                <c:if test="${not empty sectionDate}">
                                    <fmt:parseDate value="${sectionDate}" pattern="yyyy-MM-dd HH:mm:ss" var="parsedDate" />
                                    <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" var="formattedDate" />
                                    <fmt:formatDate value="${parsedDate}" pattern="EEEE" var="actualDayOfWeek" />
                                    
                                    <c:set var="isCurrentWeek" value="false" />
                                    <c:forEach var="weekDay" items="${currentWeekDays}">
                                        <c:if test="${weekDay eq formattedDate}">
                                            <c:set var="isCurrentWeek" value="true" />
                                        </c:if>
                                    </c:forEach>
                                    
                                    <c:set var="sectionStartTime" value="${section.startTime}" />
                                    <c:set var="sectionEndTime" value="${section.endTime}" />
                                    <c:set var="belongsToTimeSlot" value="false" />
                                    
                                    <c:if test="${not empty sectionStartTime and not empty sectionEndTime}">
                                        <c:set var="timeSlotStart" value="${fn:substring(timeSlot, 0, 2)}" />
                                        <c:set var="timeSlotEnd" value="${fn:substring(timeSlot, 11, 13)}" />
                                        <c:set var="sectionStartHour" value="${fn:substring(sectionStartTime, 0, 2)}" />
                                        <c:set var="sectionEndHour" value="${fn:substring(sectionEndTime, 0, 2)}" />
                                        <c:if test="${sectionStartHour >= timeSlotStart and sectionStartHour < timeSlotEnd}">
                                            <c:set var="belongsToTimeSlot" value="true" />
                                        </c:if>
                                    </c:if>
                                    
                                    <c:if test="${isCurrentWeek and actualDayOfWeek eq day and belongsToTimeSlot and not found}">
                                        <c:set var="found" value="true" />
                                        <div class="ps-schedule-cell">
                                            <c:set var="subjectVi" value="" />
                                            <c:choose>
                                                <c:when test="${section.subject eq 'Mathematics'}"><c:set var="subjectVi" value="Toán học" /></c:when>
                                                <c:when test="${section.subject eq 'Physics'}"><c:set var="subjectVi" value="Vật lý" /></c:when>
                                                <c:when test="${section.subject eq 'Chemistry'}"><c:set var="subjectVi" value="Hóa học" /></c:when>
                                                <c:when test="${section.subject eq 'Biology'}"><c:set var="subjectVi" value="Sinh học" /></c:when>
                                                <c:when test="${section.subject eq 'Literature'}"><c:set var="subjectVi" value="Ngữ văn" /></c:when>
                                                <c:when test="${section.subject eq 'History'}"><c:set var="subjectVi" value="Lịch sử" /></c:when>
                                                <c:when test="${section.subject eq 'Geography'}"><c:set var="subjectVi" value="Địa lý" /></c:when>
                                                <c:when test="${section.subject eq 'English'}"><c:set var="subjectVi" value="Tiếng Anh" /></c:when>
                                                <c:when test="${section.subject eq 'Computer Science'}"><c:set var="subjectVi" value="Tin học" /></c:when>
                                                <c:when test="${section.subject eq 'Physical Education'}"><c:set var="subjectVi" value="Thể dục" /></c:when>
                                                <c:when test="${section.subject eq 'Art'}"><c:set var="subjectVi" value="Mỹ thuật" /></c:when>
                                                <c:when test="${section.subject eq 'Music'}"><c:set var="subjectVi" value="Âm nhạc" /></c:when>
                                                <c:otherwise><c:set var="subjectVi" value="${section.subject}" /></c:otherwise>
                                            </c:choose>
                                            
                                            <div class="ps-lesson ${section.statusClass}">
                                                <div class="ps-lesson-subject">${subjectVi}</div>
                                                <div class="ps-lesson-teacher">${section.teacherName}</div>
                                                <div class="ps-lesson-time">${section.timeSlot}</div>
                                                <div class="ps-lesson-room">${section.classroom}</div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                            
                            <c:if test="${not found}">
                                <div class="ps-schedule-cell"></div>
                            </c:if>
                        </c:forEach>
                    </c:forEach>
                </div>
            </div>
            
            <div class="ps-schedule-legend">
                <div class="ps-legend-title">Chú thích:</div>
                <div class="ps-legend-items" id="legend-items">
                    <c:forEach var="legendEntry" items="${scheduleData.legend}">
                        <div class="ps-legend-item">
                            <c:choose>
                                <c:when test="${legendEntry.key eq 'paid'}">
                                    <div class="ps-legend-color ps-lesson-paid"></div>
                                </c:when>
                                <c:when test="${legendEntry.key eq 'overdue'}">
                                    <div class="ps-legend-color ps-lesson-overdue"></div>
                                </c:when>
                                <c:when test="${legendEntry.key eq 'pending'}">
                                    <div class="ps-legend-color ps-lesson-pending"></div>
                                </c:when>
                                <c:otherwise>
                                    <div class="ps-legend-color ps-lesson-type"></div>
                                </c:otherwise>
                            </c:choose>
                            <span class="ps-legend-text">${legendEntry.value}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        </c:when>
        <c:otherwise>
            <div class="ps-no-selection">
                <div class="ps-no-selection-content">
                    <i class="fas fa-user-graduate" style="font-size: 4rem; color: #667eea; margin-bottom: 1rem;"></i>
                    <h3>Vui lòng chọn con để xem thời khóa biểu</h3>
                    <p>Nhấp vào một trong các con ở trên để xem lịch học và trạng thái thanh toán</p>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    let currentWeek = new Date();
    
    <c:if test="${not empty currentMonday}">
        currentWeek = new Date('${currentMonday}');
    </c:if>

    function selectChild(childId) {
        // Redirect với studentId parameter
        const url = new URL(window.location);
        url.searchParams.set('studentId', childId);
        // Giữ nguyên week parameter nếu có
        if (url.searchParams.has('week')) {
            const weekParam = url.searchParams.get('week');
            url.searchParams.set('week', weekParam);
        }
        window.location.href = url.toString();
    }

    function previousWeek() {
        currentWeek.setDate(currentWeek.getDate() - 7);
        updateWeekDisplay();
        const url = new URL(window.location);
        url.searchParams.set('week', currentWeek.toISOString().split('T')[0]);
        // Giữ nguyên studentId parameter
        if (url.searchParams.has('studentId')) {
            const studentId = url.searchParams.get('studentId');
            url.searchParams.set('studentId', studentId);
        }
        window.location.href = url.toString();
    }

    function nextWeek() {
        currentWeek.setDate(currentWeek.getDate() + 7);
        updateWeekDisplay();
        const url = new URL(window.location);
        url.searchParams.set('week', currentWeek.toISOString().split('T')[0]);
        // Giữ nguyên studentId parameter
        if (url.searchParams.has('studentId')) {
            const studentId = url.searchParams.get('studentId');
            url.searchParams.set('studentId', studentId);
        }
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
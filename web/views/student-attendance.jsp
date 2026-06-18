<%-- 
    Document   : attendance_mark
    Created on : Jun 26, 2025, 1:11:17 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Điểm Danh Học Sinh</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .status-present {
                color: #28a745;
                font-weight: bold;
            }
            .status-absent {
                color: #dc3545;
                font-weight: bold;
            }
            .status-excused {
                color: #ffc107;
                font-weight: bold;
            }
            .status-notyet {
                color: #6c757d;
                font-weight: bold;
            }
        </style>
    </head>
    <body class="bg-light">
        <jsp:include page="layout/header.jsp"/>

        <div class="container py-4">
            <h2 class="mb-4"><i class="fas fa-user-check me-2"></i>Điểm Danh</h2>

            <!-- Nếu là phụ huynh -->
            <c:if test="${not empty children}">
                <form method="get" class="mb-4">
                    <div class="row g-2 align-items-center">
                        <div class="col-auto">
                            <label for="studentId" class="form-label">Chọn học sinh:</label>
                        </div>
                        <div class="col-auto">
                            <select name="studentId" id="studentId" class="form-select" onchange="this.form.submit()">
                                <option value="">-- Chọn học sinh --</option>
                                <c:forEach var="child" items="${children}">
                                    <option value="${child.id}" ${child.id == selectedStudentId ? 'selected' : ''}>
                                        ${studentNameMap[child.id]}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
            </c:if>

            <!-- Tên học sinh -->
            <c:if test="${not empty studentName}">
                <p class="mb-3">Học sinh: <strong>${studentName}</strong></p>
            </c:if>

            <!-- Form chọn khóa học -->
            <c:if test="${not empty studentCourses}">
                <form method="get" class="mb-4">
                    <!-- Giữ lại studentId khi có -->
                    <c:if test="${not empty selectedStudentId}">
                        <input type="hidden" name="studentId" value="${selectedStudentId}"/>
                    </c:if>
                    <div class="row g-2 align-items-center">
                        <div class="col-auto">
                            <label for="courseId" class="form-label">Chọn khóa học:</label>
                        </div>
                        <div class="col-auto">
                            <select name="courseId" id="courseId" class="form-select" onchange="this.form.submit()">
                                <option value="">-- Chọn khóa học --</option>
                                <c:forEach var="course" items="${studentCourses}">
                                    <option value="${course.id}" ${course.id == selectedCourseId ? 'selected' : ''}>
                                        ${course.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
            </c:if>

            <!-- Bảng điểm danh -->
            <c:if test="${not empty studentCourses}">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-calendar-day me-2"></i>Danh Sách Buổi Học & Trạng Thái</h5>
                    </div>
                    <div class="card-body p-0">
                        <c:choose>
                            <c:when test="${empty attendanceDetails}">
                                <div class="p-4 text-center text-muted">
                                    <i class="fas fa-exclamation-circle fa-2x mb-2"></i><br>
                                    <c:choose>
                                        <c:when test="${empty selectedCourseId}">
                                            Vui lòng chọn khóa học để xem điểm danh.
                                        </c:when>
                                        <c:otherwise>
                                            Không có dữ liệu điểm danh cho khóa học này.
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-striped mb-0">
                                        <thead class="table-light">
                                            <tr>
                                                <th>STT</th>
                                                <th>Ngày học</th>
                                                <th>Thời gian</th>
                                                <th>Phòng học</th>
                                                <th>Trạng thái</th>
                                                <th>Ghi chú</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="record" items="${attendanceDetails}" varStatus="loop">
                                                <c:set var="section" value="${sectionMap[record.sectionId]}"/>
                                                <tr>
                                                    <td>${loop.index + 1}</td>
                                                    <td>${section.formattedDate}</td>
                                                    <td>${section.formattedStartTime} - ${section.formattedEndTime}</td>
                                                    <td>${section.classroom}</td>
                                                    <td>
                                                        <span class="status-${record.attendanceStatus}">
                                                            <c:choose>
                                                                <c:when test="${record.attendanceStatus == 'present'}">Có mặt</c:when>
                                                                <c:when test="${record.attendanceStatus == 'absent'}">Vắng mặt</c:when>
                                                                <c:when test="${record.attendanceStatus == 'excused'}">Có phép</c:when>
                                                                <c:otherwise>Chưa điểm danh</c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </td>
                                                    <td>${section.note}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </div>

    </body>
</html>
<%-- 
    Document   : attendance-student-details
    Created on : Jun 26, 2025, 2:56:15 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi Tiết Điểm Danh Học Sinh</title>
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
            .card-hover:hover {
                background-color: #f8f9fa;
            }
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
            .status-pending {
                color: #6c757d;
                font-weight: bold;
                font-style: italic;
            }
        </style>
    </head>
    <jsp:include page="layout/header.jsp" />
    <body class="bg-light">
        <div class="container py-4">
            <!-- Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="mb-1"><i class="fas fa-user-check me-2"></i>Chi Tiết Điểm Danh</h2>
                    <p class="text-muted mb-0">Mã học sinh: ${studentId}, Khóa học: ${courseId}</p>
                </div>
                <a href="bao-cao-diem-danh?action=course-report&courseId=${courseId}" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-1"></i>Quay lại khóa học
                </a>
            </div>

            <!-- Attendance Detail Table -->
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="fas fa-calendar-day me-2"></i>Danh Sách Buổi Học & Trạng Thái</h5>
                </div>
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${empty attendanceDetails}">
                            <div class="p-4 text-center text-muted">
                                <i class="fas fa-exclamation-circle fa-2x mb-2"></i><br>
                                Không có dữ liệu điểm danh cho học sinh này.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-striped table-hover mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>STT</th>
                                            <th>Ngày học</th>
                                            <th>Thời gian</th>
                                            <th>Phòng học</th>
                                            <th>Trạng thái</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="record" items="${attendanceDetails}" varStatus="status">
                                            <tr class="card-hover">
                                                <td>${status.index + 1}</td>
                                                <c:set var="section" value="${sectionMap[record.sectionId]}" />
                                                <td>${section.formattedDate}</td>
                                                <td>${section.formattedStartTime} - ${section.formattedEndTime}</td>
                                                <td>${section.classroom}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.attendanceStatus == 'present'}">
                                                            <span class="status-present">Có mặt</span>
                                                        </c:when>
                                                        <c:when test="${record.attendanceStatus == 'absent'}">
                                                            <span class="status-absent">Vắng mặt</span>
                                                        </c:when>
                                                        <c:when test="${record.attendanceStatus == 'excused'}">
                                                            <span class="status-excused">Có phép</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-pending">Chưa điểm danh</span>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </body>
</html>

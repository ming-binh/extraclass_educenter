<%-- 
    Document   : attendance-section-report
    Created on : Jun 26, 2025, 2:52:13 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Báo Cáo Điểm Danh Buổi Học</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .section-info {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }
            .attendance-status {
                font-weight: bold;
            }
            .student-card {
                transition: transform 0.2s;
            }
            .student-card:hover {
                transform: translateY(-2px);
            }
            .present {
                border-left: 4px solid #28a745;
                background-color: #f8fff9;
            }
            .absent {
                border-left: 4px solid #dc3545;
                background-color: #fff8f8;
            }
        </style>
    </head>
            <jsp:include page="layout/header.jsp" />
    <body class="bg-light">
        <div class="container-fluid py-4">
            <!-- Header -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h2 class="mb-1">
                                <i class="fas fa-calendar-check me-2"></i>Báo Cáo Điểm Danh Buổi Học
                            </h2>
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item">
                                        <a href="bao-cao-diem-danh">Báo cáo điểm danh</a>
                                    </li>
                                    <li class="breadcrumb-item">
                                        <a href="bao-cao-diem-danh?action=course-report&courseId=${courseId}">
                                            ${not empty sectionReport and sectionReport.size() > 0 ? sectionReport[0].courseName : 'Khóa học'}
                                        </a>
                                    </li>
                                    <li class="breadcrumb-item active">Buổi học</li>
                                </ol>
                            </nav>
                        </div>
                        <div>
                            <a href="bao-cao-diem-danh?action=course-report&courseId=${courseId}" 
                               class="btn btn-outline-secondary">
                                <i class="fas fa-arrow-left me-1"></i>Quay lại khóa học
                            </a>
                            <a href="chuyen-can?courseId=${courseId}&sectionId=${section.id}" 
                               class="btn btn-outline-primary">
                                <i class="fas fa-pen me-1"></i>Chuyển đến trang điểm danh
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section Information -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="card section-info">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col-md-8">
                                    <h4 class="mb-2">
                                        <i class="fas fa-calendar-day me-2"></i>
                                        ${section.dayOfWeek.displayName} - ${section.classroom}
                                    </h4>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <i class="fas fa-clock me-1"></i>
                                            <strong>Thời gian:</strong><br>
                                            ${section.formattedStartTime} - ${section.formattedEndTime}
                                        </div>
                                        <div class="col-md-4">
                                            <i class="fas fa-calendar me-1"></i>
                                            <strong>Ngày học:</strong><br>
                                            ${section.formattedDate}

                                        </div>
                                        <div class="col-md-4">
                                            <i class="fas fa-info-circle me-1"></i>
                                            <strong>Trạng thái:</strong><br>
                                            <span class="badge bg-light text-dark">${section.status.displayName}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4 text-md-end">
                                    <div class="h2 mb-0">${sectionReport.size()}</div>
                                    <div>Học sinh tham gia</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Statistics -->
            <div class="row mb-4">
                <c:set var="presentCount" value="0"/>
                <c:forEach var="student" items="${sectionReport}">
                    <c:if test="${student.attendanceStatus == 'present'}">
                        <c:set var="presentCount" value="${presentCount + 1}"/>
                    </c:if>
                </c:forEach>

                <div class="col-md-3">
                    <div class="card text-center h-100">
                        <div class="card-body">
                            <div class="h3 text-primary">${sectionReport.size()}</div>
                            <div class="text-muted">Tổng học sinh</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center h-100">
                        <div class="card-body">
                            <div class="h3 text-success">${presentCount}</div>
                            <div class="text-muted">Có mặt</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center h-100">
                        <div class="card-body">
                            <div class="h3 text-danger">${sectionReport.size() - presentCount}</div>
                            <div class="text-muted">Vắng mặt</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center h-100">
                        <div class="card-body">
                            <c:set var="attendanceRate" value="${sectionReport.size() > 0 ? (presentCount * 100) / sectionReport.size() : 0}"/>
                            <div class="h3
                                 <c:choose>
                                     <c:when test="${attendanceRate >= 80}">text-success</c:when>
                                     <c:when test="${attendanceRate >= 60}">text-warning</c:when>
                                     <c:otherwise>text-danger</c:otherwise>
                                 </c:choose>">
                                <fmt:formatNumber value="${attendanceRate}" pattern="#.#"/>%
                            </div>
                            <div class="text-muted">Tỷ lệ có mặt</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <div class="d-flex flex-wrap gap-2">
                                <button class="btn btn-outline-success" onclick="filterByAttendance('present')">
                                    <i class="fas fa-check me-1"></i>Chỉ xem có mặt (${presentCount})
                                </button>
                                <button class="btn btn-outline-danger" onclick="filterByAttendance('absent')">
                                    <i class="fas fa-times me-1"></i>Chỉ xem vắng mặt (${sectionReport.size() - presentCount})
                                </button>
                                <button class="btn btn-outline-primary" onclick="filterByAttendance('all')">
                                    <i class="fas fa-users me-1"></i>Xem tất cả
                                </button>
                                <button class="btn btn-outline-warning" onclick="filterByPayment('unpaid')">
                                    <i class="fas fa-exclamation-triangle me-1"></i>Chưa đóng học phí
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Student List -->
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">
                                <i class="fas fa-users me-2"></i>Danh Sách Học Sinh
                            </h5>
                            <div>
                                <input type="text" class="form-control form-control-sm" id="searchInput" 
                                       placeholder="Tìm kiếm theo tên hoặc email" style="width: 250px;">
                            </div>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${empty sectionReport}">
                                    <div class="text-center py-5">
                                        <i class="fas fa-users-slash fa-4x text-muted mb-3"></i>
                                        <h5 class="text-muted">Không có dữ liệu học sinh cho buổi học này</h5>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="row" id="studentList">
                                        <c:forEach var="student" items="${sectionReport}">
                                            <div class="col-md-4 mb-3 student-card-wrapper"
                                                 data-attendance="${student.attendanceStatus}"
                                                 data-paid="${student.coursePaid}"
                                                 data-name="${student.studentName.toLowerCase()}"
                                                 data-email="${student.studentEmail.toLowerCase()}">
                                                 <div class="card student-card
                                                 ${student.attendanceStatus == 'present' ? 'present' :
                                                   student.attendanceStatus == 'absent' ? 'absent' :
                                                   student.attendanceStatus == 'excused' ? 'excused' :
                                                   'notyet'}"
                                                   >
                                                    <div class="card-body">
                                                        <h6 class="card-title mb-1">${student.studentName}</h6>
                                                        <p class="mb-1 small">
                                                            <i class="fas fa-check-circle me-1
                                                            ${student.attendanceStatus == 'present' ? 'text-success' :
                                                              student.attendanceStatus == 'absent' ? 'text-danger' :
                                                              student.attendanceStatus == 'excused' ? 'text-warning' :
                                                              'text-secondary'}"></i>
                                                         Trạng thái: 
                                                         <span class="attendance-status
                                                         ${student.attendanceStatus == 'present' ? 'text-success' :
                                                           student.attendanceStatus == 'absent' ? 'text-danger' :
                                                           student.attendanceStatus == 'excused' ? 'text-warning' :
                                                           'text-secondary'}">
                                                         <c:choose>
                                                             <c:when test="${student.attendanceStatus == 'present'}">Có mặt</c:when>
                                                             <c:when test="${student.attendanceStatus == 'absent'}">Vắng mặt</c:when>
                                                             <c:when test="${student.attendanceStatus == 'excused'}">Có phép</c:when>
                                                             <c:when test="${student.attendanceStatus == 'notyet'}">Chưa diễn ra</c:when>
                                                             <c:otherwise>Không xác định</c:otherwise>
                                                         </c:choose>
                                                            </span>
                                                        </p>
                                                        <p class="mb-0 small">
                                                            <i class="fas fa-money-bill me-1"></i>
                                                            Học phí: 
                                                            <span class="${student.coursePaid ? 'text-success' : 'text-danger'}">
                                                                ${student.coursePaid ? 'Đã đóng' : 'Chưa đóng'}
                                                            </span>
                                                        </p>
                                                        <c:if test="${not empty student.attendanceDate}">
                                                            <p class="mb-0 small text-muted">
                                                                <i class="fas fa-clock me-1"></i>
                                                                Cập nhật: ${student.attendanceDate}
                                                            </p>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            const filterByAttendance = (status) => {
                document.querySelectorAll('.student-card-wrapper').forEach(el => {
                    const attendance = el.dataset.attendance;
                    if (status === 'all') {
                        el.style.display = '';
                    } else {
                        el.style.display = (attendance === status) ? '' : 'none';
                    }
                });
            };

            const filterByPayment = (status) => {
                document.querySelectorAll('.student-card-wrapper').forEach(el => {
                    const paid = el.dataset.paid === 'true';
                    if (status === 'unpaid') {
                        el.style.display = paid ? 'none' : '';
                    } else {
                        el.style.display = '';
                    }
                });
            };

            document.getElementById('searchInput').addEventListener('input', function () {
                const keyword = this.value.toLowerCase();
                document.querySelectorAll('.student-card-wrapper').forEach(el => {
                    const name = el.dataset.name;
                    const email = el.dataset.email;
                    el.style.display = (name.includes(keyword) || email.includes(keyword)) ? '' : 'none';
                });
            });
        </script>
    </body>
</html>

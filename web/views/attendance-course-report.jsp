<%-- 
    Document   : attendance-course-report
    Created on : Jun 26, 2025, 2:46:05 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Báo Cáo Điểm Danh - <c:out value="${course.name}" default="N/A"/></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .attendance-percentage {
                font-weight: bold;
            }
            .attendance-good {
                color: #28a745;
            }
            .attendance-warning {
                color: #ffc107;
            }
            .attendance-danger {
                color: #dc3545;
            }

            .section-card {
                transition: transform 0.2s;
                cursor: pointer;
            }
            .section-card:hover {
                transform: translateY(-2px);
            }

            .stats-card {
                border-left: 4px solid #007bff;
            }

            .student-row:hover {
                background-color: #f8f9fa;
            }

            .error-alert {
                margin-bottom: 20px;
            }
        </style>
    </head>
            <jsp:include page="layout/header.jsp" />
    <body class="bg-light">
        <div class="container-fluid py-4">
            <!-- Error Display -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger error-alert" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <c:out value="${error}"/>
                </div>
            </c:if>

            <!-- Header -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h2 class="mb-1">
                                <i class="fas fa-chart-line me-2"></i>Báo Cáo Điểm Danh
                            </h2>
                            <c:if test="${not empty course}">
                                <h4 class="text-muted"><c:out value="${course.name}"/></h4>
                                <p class="text-muted mb-0"><c:out value="${course.description}"/></p>
                            </c:if>
                        </div>
                        <div>
                            <a href="bao-cao-diem-danh" class="btn btn-outline-secondary me-2">
                                <i class="fas fa-arrow-left me-1"></i>Quay lại
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <c:if test="${not empty course}">
                <!-- Course Info -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-3">
                                        <strong>Môn học:</strong> 
                                        <c:out value="${course.subject.displayName}" default="N/A"/>
                                    </div>
                                    <div class="col-md-3">
                                        <strong>Lớp:</strong> 
                                        <c:out value="${course.grade}" default="N/A"/>
                                    </div>
                                    <div class="col-md-3">
                                        <strong>Cấp độ:</strong> 
                                        <c:out value="${course.level.displayName}" default="N/A"/>
                                    </div>
                                    <div class="col-md-3">
                                        <strong>Loại khóa học:</strong> 
                                        <span class="badge bg-info">
                                            <c:out value="${course.courseType.displayName}" default="N/A"/>
                                        </span>
                                    </div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-6">
                                        <strong>Sĩ số tối đa:</strong> <c:out value="${course.maxStudents}" default="0"/>
                                    </div>
                                    <div class="col-md-6">
                                        <strong>Đã đăng ký:</strong> <c:out value="${course.studentEnrollment}" default="0"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Statistics -->
                <c:if test="${not empty statistics}">
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="card stats-card h-100">
                                <div class="card-body text-center">
                                    <div class="h2 text-primary"><c:out value="${statistics.totalStudents}" default="0"/></div>
                                    <div class="text-muted">Tổng học sinh</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card stats-card h-100">
                                <div class="card-body text-center">
                                    <div class="h2 text-info"><c:out value="${statistics.totalSections}" default="0"/></div>
                                    <div class="text-muted">Tổng buổi học</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card stats-card h-100">
                                <div class="card-body text-center">
                                    <div class="h2 text-success"><c:out value="${statistics.totalPresent}" default="0"/></div>
                                    <div class="text-muted">Có mặt</div>
                                    <small class="text-muted d-block">Vắng: <c:out value="${statistics.totalAbsent}" default="0"/></small>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card stats-card h-100">
                                <div class="card-body text-center">
                                    <div class="h2
                                         <c:choose>
                                             <c:when test="${statistics.overallAttendanceRate >= 80}">text-success</c:when>
                                             <c:when test="${statistics.overallAttendanceRate >= 60}">text-warning</c:when>
                                             <c:otherwise>text-danger</c:otherwise>
                                         </c:choose>">
                                        <fmt:formatNumber value="${statistics.overallAttendanceRate}" pattern="#.#" minFractionDigits="1"/>%
                                    </div>
                                    <div class="text-muted">Tỷ lệ điểm danh</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Sections -->
                <c:if test="${not empty sections}">
                    <div class="row mb-4">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0"><i class="fas fa-calendar-alt me-2"></i>Các Buổi Học</h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <c:forEach var="section" items="${sections}">
                                            <div class="col-md-4 col-lg-3 mb-3">
                                                <div class="card section-card" 
                                                     onclick="viewSectionReport(<c:out value='${section.id}'/>, <c:out value='${courseId}'/>)">
                                                    <div class="card-body">
                                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                                            <h6 class="card-title mb-0">
                                                                <c:out value="${section.dayOfWeek.displayName}" default="N/A" />
                                                            </h6>
                                                            <span class="badge
                                                                  <c:choose>
                                                                      <c:when test="${section.status.toString() == 'active'}">bg-success</c:when>
                                                                      <c:when test="${section.status.toString() == 'completed'}">bg-primary</c:when>
                                                                      <c:when test="${section.status.toString() == 'cancelled'}">bg-danger</c:when>
                                                                      <c:otherwise>bg-secondary</c:otherwise>
                                                                  </c:choose>">
                                                                <c:out value="${section.status.displayName}" default="N/A" />
                                                            </span>
                                                        </div>

                                                        <c:choose>
                                                            <c:when test="${not empty section.startTime and not empty section.endTime}">
                                                                <p class="card-text small text-muted">
                                                                    <i class="fas fa-clock me-1"></i>
                                                                    ${section.formattedTimeRange}
                                                                </p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p class="card-text small text-muted">
                                                                    <i class="fas fa-clock me-1"></i>
                                                                    Chưa xác định thời gian
                                                                </p>
                                                            </c:otherwise>
                                                        </c:choose>

                                                        <c:choose>
                                                            <c:when test="${not empty section.dateTime}">
                                                                <p class="card-text small text-muted">
                                                                    <i class="fas fa-calendar me-1"></i>
                                                                    ${section.formattedDate}
                                                                </p>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <p class="card-text small text-muted">
                                                                    <i class="fas fa-calendar me-1"></i>
                                                                    Chưa xác định ngày
                                                                </p>
                                                            </c:otherwise>
                                                        </c:choose>

                                                        <p class="card-text small text-muted">
                                                            <i class="fas fa-map-marker-alt me-1"></i>
                                                            <c:out value="${section.classroom}" default="Chưa xác định" />
                                                        </p>

                                                        <button type="button" class="btn btn-outline-primary btn-sm w-100">
                                                            <i class="fas fa-eye me-1"></i>Chi tiết
                                                        </button>
                                                    </div>

                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <c:if test="${empty sections}">
                                        <div class="text-center text-muted py-4">
                                            <i class="fas fa-calendar-times fa-2x mb-2 d-block"></i>
                                            Chưa có buổi học nào được tạo
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Attendance Report Table -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h5 class="mb-0"><i class="fas fa-users me-2"></i>Báo Cáo Điểm Danh Học Sinh</h5>
                                <div>
                                    <button class="btn btn-sm btn-outline-primary" onclick="toggleFilters()">
                                        <i class="fas fa-filter me-1"></i>Bộ lọc
                                    </button>
                                </div>
                            </div>

                            <!-- Filters -->
                            <div id="filters" class="card-body bg-light" style="display: none;">
                                <div class="row">
                                    <div class="col-md-3">
                                        <label class="form-label">Trạng thái đăng ký:</label>
                                        <select class="form-select form-select-sm" id="statusFilter">
                                            <option value="">Tất cả</option>
                                            <option value="accepted">Đã chấp nhận</option>
                                            <option value="pending">Chờ duyệt</option>
                                            <option value="rejected">Từ chối</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">Tình trạng học phí:</label>
                                        <select class="form-select form-select-sm" id="paidFilter">
                                            <option value="">Tất cả</option>
                                            <option value="true">Đã đóng</option>
                                            <option value="false">Chưa đóng</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">Tỷ lệ điểm danh:</label>
                                        <select class="form-select form-select-sm" id="attendanceFilter">
                                            <option value="">Tất cả</option>
                                            <option value="good">Tốt (>=80%)</option>
                                            <option value="average">Trung bình (60-79%)</option>
                                            <option value="poor">Kém (<60%)</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">Tìm kiếm:</label>
                                        <input type="text" class="form-control form-control-sm" id="searchInput" 
                                               placeholder="Tên hoặc email học sinh">
                                    </div>
                                </div>
                            </div>

                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="attendanceTable">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>STT</th>
                                                <th>Tên học sinh</th>
                                                <th>Trạng thái</th>
                                                <th>Học phí</th>
                                                <th>Tổng buổi</th>
                                                <th>Có mặt</th>
                                                <th>Vắng mặt</th>
                                                <th>Tỷ lệ (%)</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty attendanceReport}">
                                                    <tr>
                                                        <td colspan="10" class="text-center text-muted py-4">
                                                            <i class="fas fa-users-slash fa-2x mb-2 d-block"></i>
                                                            Chưa có dữ liệu điểm danh
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="student" items="${attendanceReport}" varStatus="status">
                                                        <tr class="student-row" 
                                                            data-status="<c:out value='${student.enrollmentStatus}'/>"
                                                            data-paid="<c:out value='${student.coursePaid}'/>"
                                                            data-attendance="<c:out value='${student.attendancePercentage}'/>"
                                                            data-name="<c:out value='${student.studentName}'/>"
                                                            data-email="<c:out value='${student.studentEmail}'/>">
                                                            <td>${status.index + 1}</td>
                                                            <td>
                                                                <strong><c:out value="${student.studentName}"/></strong>
                                                            </td>
                                                            <td>
                                                                <span class="badge
                                                                      <c:choose>
                                                                          <c:when test="${student.enrollmentStatus == 'accepted'}">bg-success</c:when>
                                                                          <c:when test="${student.enrollmentStatus == 'pending'}">bg-warning text-dark</c:when>
                                                                          <c:otherwise>bg-danger</c:otherwise>
                                                                      </c:choose>">
                                                                    <c:out value="${student.enrollmentStatus}"/>
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${student.coursePaid}">
                                                                        <span class="badge bg-success">
                                                                            <i class="fas fa-check me-1"></i>Đã đóng
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-danger">
                                                                            <i class="fas fa-times me-1"></i>Chưa đóng
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td class="text-center"><c:out value="${student.totalSections}" default="0"/></td>
                                                            <td class="text-center text-success">
                                                                <strong><c:out value="${student.attendedSections}" default="0"/></strong>
                                                            </td>
                                                            <td class="text-center text-danger">
                                                                <strong><c:out value="${student.missedSections}" default="0"/></strong>
                                                            </td>
                                                            <td class="text-center">
                                                                <span class="attendance-percentage
                                                                      <c:choose>
                                                                          <c:when test="${student.attendancePercentage >= 80}">attendance-good</c:when>
                                                                          <c:when test="${student.attendancePercentage >= 60}">attendance-warning</c:when>
                                                                          <c:otherwise>attendance-danger</c:otherwise>
                                                                      </c:choose>">
                                                                    <fmt:formatNumber value="${student.attendancePercentage}" pattern="#.#" minFractionDigits="1"/>%
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <button class="btn btn-sm btn-outline-primary" 
                                                                        onclick="viewStudentDetails(<c:out value='${student.studentId}'/>, <c:out value='${courseId}'/>)"
                                                                        title="Xem chi tiết điểm danh">
                                                                    <i class="fas fa-eye me-1"></i>Chi tiết
                                                                </button>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <script>
            function viewSectionReport(sectionId, courseId) {
                if (sectionId && courseId) {
                    window.location.href = 'bao-cao-diem-danh?action=section-report&sectionId=' + sectionId + '&courseId=' + courseId;
                } else {
                    alert('Thông tin không hợp lệ');
                }
            }

            function viewStudentDetails(studentId, courseId) {
                if (studentId && courseId) {
                    window.location.href = 'bao-cao-diem-danh?action=student-details&studentId=' + studentId + '&courseId=' + courseId;
                } else {
                    alert('Thông tin không hợp lệ');
                }
            }

            function toggleFilters() {
                const filters = document.getElementById('filters');
                if (filters.style.display === 'none') {
                    filters.style.display = 'block';
                } else {
                    filters.style.display = 'none';
                }
            }

            // Filter functionality
            document.addEventListener('DOMContentLoaded', function () {
                const statusFilter = document.getElementById('statusFilter');
                const paidFilter = document.getElementById('paidFilter');
                const attendanceFilter = document.getElementById('attendanceFilter');
                const searchInput = document.getElementById('searchInput');

                function filterTable() {
                    const rows = document.querySelectorAll('.student-row');
                    let visibleCount = 0;

                    rows.forEach((row, index) => {
                        let show = true;

                        // Status filter
                        if (statusFilter && statusFilter.value && row.dataset.status !== statusFilter.value) {
                            show = false;
                        }

                        // Paid filter
                        if (paidFilter && paidFilter.value && row.dataset.paid !== paidFilter.value) {
                            show = false;
                        }

                        // Attendance filter
                        if (attendanceFilter && attendanceFilter.value) {
                            const attendance = parseFloat(row.dataset.attendance) || 0;
                            if (attendanceFilter.value === 'good' && attendance < 80)
                                show = false;
                            if (attendanceFilter.value === 'average' && (attendance < 60 || attendance >= 80))
                                show = false;
                            if (attendanceFilter.value === 'poor' && attendance >= 60)
                                show = false;
                        }

                        // Search filter
                        if (searchInput && searchInput.value) {
                            const searchTerm = searchInput.value.toLowerCase();
                            const name = (row.dataset.name || '').toLowerCase();
                            if (!name.includes(searchTerm) && !email.includes(searchTerm)) {
                                show = false;
                            }
                        }

                        if (show) {
                            row.style.display = '';
                            visibleCount++;
                            // Update row number
                            const firstCell = row.querySelector('td:first-child');
                            if (firstCell) {
                                firstCell.textContent = visibleCount;
                            }
                        } else {
                            row.style.display = 'none';
                        }
                    });

                    // Show message if no results
                    const tbody = document.querySelector('#attendanceTable tbody');
                    let noResultsRow = tbody.querySelector('.no-results-row');

                    if (visibleCount === 0 && rows.length > 0) {
                        if (!noResultsRow) {
                            noResultsRow = document.createElement('tr');
                            noResultsRow.className = 'no-results-row';
                            noResultsRow.innerHTML = `
                                <td colspan="10" class="text-center text-muted py-4">
                                    <i class="fas fa-search fa-2x mb-2 d-block"></i>
                                    Không tìm thấy kết quả phù hợp
                                </td>
                            `;
                            tbody.appendChild(noResultsRow);
                        }
                        noResultsRow.style.display = '';
                    } else if (noResultsRow) {
                        noResultsRow.style.display = 'none';
                    }
                }

                // Add event listeners
                if (statusFilter)
                    statusFilter.addEventListener('change', filterTable);
                if (paidFilter)
                    paidFilter.addEventListener('change', filterTable);
                if (attendanceFilter)
                    attendanceFilter.addEventListener('change', filterTable);
                if (searchInput)
                    searchInput.addEventListener('input', filterTable);

                // Clear filters button
                const clearFiltersBtn = document.createElement('button');
                clearFiltersBtn.className = 'btn btn-sm btn-outline-secondary ms-2';
                clearFiltersBtn.innerHTML = '<i class="fas fa-times me-1"></i>Xóa bộ lọc';
                clearFiltersBtn.onclick = function () {
                    if (statusFilter)
                        statusFilter.value = '';
                    if (paidFilter)
                        paidFilter.value = '';
                    if (attendanceFilter)
                        attendanceFilter.value = '';
                    if (searchInput)
                        searchInput.value = '';
                    filterTable();
                };

                const filtersDiv = document.getElementById('filters');
                if (filtersDiv) {
                    const lastCol = filtersDiv.querySelector('.row .col-md-3:last-child');
                    if (lastCol) {
                        lastCol.appendChild(clearFiltersBtn);
                    }
                }
            });

            // Auto-hide alerts after 5 seconds
            document.addEventListener('DOMContentLoaded', function () {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(alert => {
                    setTimeout(() => {
                        alert.style.transition = 'opacity 0.5s';
                        alert.style.opacity = '0';
                        setTimeout(() => {
                            alert.remove();
                        }, 500);
                    }, 5000);
                });
            });
        </script>
    </body>
</html>

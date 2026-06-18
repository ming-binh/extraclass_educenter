<%-- 
    Document   : attendance_report
    Created on : Jun 26, 2025, 1:15:05 AM
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
        <title>Báo Cáo Điểm Danh - Danh Sách Khóa Học</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .course-card {
                transition: transform 0.2s, box-shadow 0.2s;
                cursor: pointer;
            }
            .course-card:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            .status-badge {
                font-size: 0.8em;
            }
            .course-type-badge {
                font-size: 0.75em;
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
                            <h2 class="mb-1"><i class="fas fa-chart-line me-2"></i>Báo Cáo Điểm Danh</h2>
                            <p class="text-muted mb-0">Chọn khóa học để xem báo cáo điểm danh chi tiết</p>
                        </div>
                        <div>
                            <button class="btn btn-outline-primary" onclick="window.history.back()">
                                <i class="fas fa-arrow-left me-1"></i>Quay lại
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="row mb-3">
                    <div class="col-12">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-triangle me-2"></i>${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Courses Grid -->
            <div class="row">
                <c:choose>
                    <c:when test="${empty courses}">
                        <div class="col-12">
                            <div class="text-center py-5">
                                <i class="fas fa-book-open fa-4x text-muted mb-3"></i>
                                <h4 class="text-muted">Không có khóa học nào</h4>
                                <p class="text-muted">Hiện tại không có khóa học đang hoạt động để tạo báo cáo điểm danh.</p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="course" items="${courses}">
                            <div class="col-md-6 col-lg-4 mb-4">
                                <div class="card course-card h-100" onclick="viewCourseReport(${course.id})">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <div>
                                            <span class="badge bg-success status-badge">
                                                <i class="fas fa-check-circle me-1"></i>${course.status.displayName}
                                            </span>
                                            <span class="badge bg-info course-type-badge ms-1">
                                                ${course.courseType.displayName}
                                            </span>
                                        </div>
                                    </div>

                                    <div class="card-body">
                                        <h5 class="card-title">${course.name}</h5>
                                        <p class="card-text text-muted small">
                                            ${course.description}
                                        </p>

                                        <div class="row text-center mb-3">
                                            <div class="col-4">
                                                <div class="border-end">
                                                    <div class="h6 mb-0">${course.studentEnrollment}</div>
                                                    <small class="text-muted">Đã đăng ký</small>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="border-end">
                                                    <div class="h6 mb-0">${course.maxStudents}</div>
                                                    <small class="text-muted">Tối đa</small>
                                                </div>
                                            </div>
                                            <div class="col-4">
                                                <div class="h6 mb-0">
                                                    <fmt:formatNumber value="${(course.studentEnrollment * 100) / course.maxStudents}" 
                                                                      pattern="#.#"/>%
                                                </div>
                                                <small class="text-muted">Đầy</small>
                                            </div>
                                        </div>

                                        <div class="row small text-muted">
                                            <div class="col-6">
                                                <i class="fas fa-book me-1"></i>
                                                ${course.subject.displayName}
                                            </div>
                                            <div class="col-6">
                                                <i class="fas fa-layer-group me-1"></i>
                                                ${course.grade}
                                            </div>
                                        </div>
                                        <div class="row small text-muted mt-1">
                                            <div class="col-12">
                                                <i class="fas fa-level-up-alt me-1"></i>
                                                Level: ${course.level.displayName}
                                            </div>
                                        </div>
                                    </div>

                                    <div class="card-footer bg-transparent">
                                        <div class="d-grid">
                                            <button class="btn btn-primary btn-sm" onclick="event.stopPropagation(); viewCourseReport(${course.id})">
                                                <i class="fas fa-chart-bar me-1"></i>Xem Báo Cáo Điểm Danh
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Statistics Summary -->
            <c:if test="${not empty courses}">
                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0"><i class="fas fa-info-circle me-2"></i>Tổng Quan</h5>
                            </div>
                            <div class="card-body">
                                <div class="row text-center">
                                    <div class="col-md-3">
                                        <div class="h3 text-primary">${courses.size()}</div>
                                        <div class="text-muted">Khóa học hoạt động</div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="h3 text-success">
                                            <c:set var="totalEnrollment" value="0"/>
                                            <c:forEach var="course" items="${courses}">
                                                <c:set var="totalEnrollment" value="${totalEnrollment + course.studentEnrollment}"/>
                                            </c:forEach>
                                            ${totalEnrollment}
                                        </div>
                                        <div class="text-muted">Tổng học sinh</div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="h3 text-info">
                                            <c:set var="totalCapacity" value="0"/>
                                            <c:forEach var="course" items="${courses}">
                                                <c:set var="totalCapacity" value="${totalCapacity + course.maxStudents}"/>
                                            </c:forEach>
                                            ${totalCapacity}
                                        </div>
                                        <div class="text-muted">Tổng sức chứa</div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="h3 text-warning">
                                            <fmt:formatNumber value="${(totalEnrollment * 100.0) / totalCapacity}" pattern="#.#"/>%
                                        </div>
                                        <div class="text-muted">Tỷ lệ lấp đầy</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

        </div>

        <script>
                                                function viewCourseReport(courseId) {
                                                    window.location.href = 'bao-cao-diem-danh?action=course-report&courseId=' + courseId;
                                                }
        </script>
    </body>
</html>

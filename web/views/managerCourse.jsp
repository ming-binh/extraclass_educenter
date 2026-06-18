<%-- 
    Document   : managerCourse
    Created on : Jun 24, 2025, 11:40:31 PM
    Author     : HanND
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="layout/adminHeader.jsp" />

<style>
    .table-hover tbody tr:hover {
        background-color: #f9f9f9 ;
    }

    .action-buttons .btn {
        opacity: 0.6;
        transition: 0.2s ease;
    }

    tr:hover .action-buttons .btn {
        opacity: 1;
        box-shadow: 0 0 4px rgba(0, 0, 0, 0.15);
    }

    .table th,
    .table td {
        vertical-align: middle;
    }

    .card-header.bg-primary {
        background-color: #ffffff !important;
        color: #212529;
        border-bottom: 1px solid #dee2e6;
    }

    .card-header h5 i {
        color: #0d6efd;
    }

    .card-body {
        background-color: #ffffff;
    }

</style>
<!-- PHẦN BỘ LỌC -->
<form method="get" action="quan-ly-khoa-hoc" class="mb-4">
    <input type="hidden" name="action" value="list" />
    <div class="card border-0 shadow-sm">
        <div class="card-body bg-light">
            <div class="row g-3 align-items-end">
                <!-- Hàng 1: Tên + Giáo viên + Môn -->
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Tên khóa học</label>
                    <input type="text" class="form-control" name="name" value="${param.name}" placeholder="VD: Toán nâng cao" />
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Giáo viên phụ trách</label>
                    <select name="teacherId" class="form-select">
                        <option value="">-- Tất cả --</option>
                        <c:forEach var="t" items="${teacherList}">
                            <option value="${t.id}" ${param.teacherId == t.id.toString() ? 'selected' : ''}>
                                ${t.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Môn học</label>
                    <select class="form-select" name="subject">
                        <option value="">-- Tất cả --</option>
                        <option value="Mathematics" ${param.subject == 'Mathematics' ? 'selected' : ''}>Toán</option>
                        <option value="Physics" ${param.subject == 'Physics' ? 'selected' : ''}>Vật lý</option>
                        <option value="Chemistry" ${param.subject == 'Chemistry' ? 'selected' : ''}>Hóa học</option>
                        <option value="Biology" ${param.subject == 'Biology' ? 'selected' : ''}>Sinh học</option>
                        <option value="Literature" ${param.subject == 'Literature' ? 'selected' : ''}>Ngữ văn</option>
                        <option value="English" ${param.subject == 'English' ? 'selected' : ''}>Tiếng Anh</option>
                        <option value="History" ${param.subject == 'History' ? 'selected' : ''}>Lịch sử</option>
                        <option value="Geography" ${param.subject == 'Geography' ? 'selected' : ''}>Địa lý</option>
                        <option value="Civics" ${param.subject == 'Civics' ? 'selected' : ''}>GDCD</option>
                        <option value="IT" ${param.subject == 'IT' ? 'selected' : ''}>Tin học</option>
                    </select>
                </div>

                <!-- Hàng 2: Khối + Trình độ + Trạng thái + Nút -->
                <div class="col-md-2">
                    <label class="form-label fw-semibold">Khối</label>
                    <select class="form-select" name="grade">
                        <option value="">-- Tất cả --</option>
                        <c:forEach var="g" begin="1" end="12">
                            <option value="${g}" ${param.grade == g.toString() ? 'selected' : ''}>${g}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-semibold">Trình độ</label>
                    <select class="form-select" name="level">
                        <option value="">-- Tất cả --</option>
                        <option value="Foundation" ${param.level == 'Foundation' ? 'selected' : ''}>Nhập môn</option>
                        <option value="Basic" ${param.level == 'Basic' ? 'selected' : ''}>Cơ bản</option>
                        <option value="Advanced" ${param.level == 'Advanced' ? 'selected' : ''}>Nâng cao</option>
                        <option value="Excellent" ${param.level == 'Excellent' ? 'selected' : ''}>Xuất sắc</option>
                        <option value="Topics_Exam" ${param.level == 'Topics_Exam' ? 'selected' : ''}>Chuyên đề / Luyện thi</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-semibold">Trạng thái</label>
                    <select class="form-select" name="status">
                        <option value="">-- Tất cả --</option>
                        <option value="activated" ${param.status == 'activated' ? 'selected' : ''}>Đang hoạt động</option>
                        <option value="pending" ${param.status == 'pending' ? 'selected' : ''}>Chờ duyệt</option>
                        <option value="upcoming" ${param.status == 'upcoming' ? 'selected' : ''}>Sắp diễn ra</option>
                        <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Đã hoàn thành</option>
                        <option value="inactivated" ${param.status == 'inactivated' ? 'selected' : ''}>Tạm ngưng</option>
                        <option value="rejected" ${param.status == 'rejected' ? 'selected' : ''}>Từ chối</option>
                    </select>
                </div>
                <div class="col-md-2 d-grid">
                    <button type="submit" class="btn btn-primary fw-bold">
                        <i class="fas fa-filter me-1"></i> Lọc
                    </button>
                </div>
            </div>
        </div>
    </div>
</form>

<!-- Danh sach khoa hoc -->
<div class="card shadow-sm border-0 mt-4">
    <div class="card-header d-flex justify-content-between align-items-center bg-white">
        <h5 class="mb-0 fw-semibold"><i class="fas fa-book me-2 text-primary"></i>Danh sách khóa học</h5>
        <a href="quan-ly-khoa-hoc?action=add" class="btn btn-outline-primary btn-sm fw-bold">
            <i class="fas fa-plus me-1"></i>Thêm khóa học
        </a>
    </div>
    <div class="card-body table-responsive">
        <table class="table table-bordered table-hover align-middle text-center bg-white rounded">
            <thead class="table-light">
                <tr>
                    <th>STT</th>
                    <th>Ảnh</th>
                    <th>Tên</th>
                    <th>Giáo viên</th>
                    <th>Môn</th>
                    <th>Khối</th>
                    <th>Học phí</th>
                    <th>Thời gian</th>
                    <th>Sĩ số</th>
                    <th>Tuần</th>
                    <th>Trình độ</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${courseList}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty course.courseImg}">
                                    <img src="${pageContext.request.contextPath}/assets/banners_course/${course.courseImg}" 
                                         alt="Ảnh khóa học" 
                                         style="width: 120px; height: auto;">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/default_course.jpg" alt="Ảnh mặc định" style="width: 100px;">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-start">${course.name}</td>
                        <td>${course.teacherName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${course.subject eq 'Mathematics'}">Toán</c:when>
                                <c:when test="${course.subject eq 'Physics'}">Vật lý</c:when>
                                <c:when test="${course.subject eq 'Chemistry'}">Hóa học</c:when>
                                <c:when test="${course.subject eq 'Biology'}">Sinh học</c:when>
                                <c:when test="${course.subject eq 'Literature'}">Ngữ văn</c:when>
                                <c:when test="${course.subject eq 'English'}">Tiếng Anh</c:when>
                                <c:when test="${course.subject eq 'History'}">Lịch sử</c:when>
                                <c:when test="${course.subject eq 'Geography'}">Địa lý</c:when>
                                <c:when test="${course.subject eq 'Civics'}">GDCD</c:when>
                                <c:when test="${course.subject eq 'IT'}">Tin học</c:when>
                                <c:otherwise>${course.subject}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${course.grade}</td>
                        <td>
                            <c:choose>
                                <c:when test="${course.feeCombo != null}">
                                    <fmt:formatNumber value="${course.feeCombo}" type="number" groupingUsed="true"/> ₫ (Gói)
                                </c:when>
                                <c:when test="${course.feeDaily != null}">
                                    <fmt:formatNumber value="${course.feeDaily}" type="number" groupingUsed="true"/> ₫/ngày
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">N/A</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:formatDate value="${course.startDate}" pattern="dd/MM/yyyy" />
                            –
                            <fmt:formatDate value="${course.endDate}" pattern="dd/MM/yyyy" />
                        </td>
                        <td>
                            <span class="fw-bold text-primary">${course.studentEnrollment}</span> /
                            <span class="text-muted">${course.maxStudents}</span>
                        </td>
                        <td>${course.weekAmount}</td>
                        <td>
                            <c:choose>
                                <c:when test="${course.level eq 'Foundation'}">Nhập môn</c:when>
                                <c:when test="${course.level eq 'Basic'}">Cơ bản</c:when>
                                <c:when test="${course.level eq 'Advanced'}">Nâng cao</c:when>
                                <c:when test="${course.level eq 'Excellent'}">Xuất sắc</c:when>
                                <c:when test="${course.level eq 'Topics_Exam'}">Chuyên đề / Luyện thi</c:when>
                                <c:otherwise>Không xác định</c:otherwise>
                            </c:choose>
                        </td>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${course.status eq 'activated'}">
                                    <span class="badge bg-success">Đang hoạt động</span>
                                </c:when>
                                <c:when test="${course.status eq 'pending'}">
                                    <span class="badge bg-warning text-dark">Chờ duyệt</span>
                                </c:when>
                                <c:when test="${course.status eq 'upcoming'}">
                                    <span class="badge bg-primary">Sắp diễn ra</span>
                                </c:when>
                                <c:when test="${course.status eq 'completed'}">
                                    <span class="badge bg-info">Đã hoàn thành</span>
                                </c:when>
                                <c:when test="${course.status eq 'inactivated'}">
                                    <span class="badge bg-secondary">Tạm ngưng</span>
                                </c:when>
                                <c:when test="${course.status eq 'rejected'}">
                                    <span class="badge bg-danger">Từ chối</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-light text-dark">${course.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="d-flex justify-content-center gap-2">
                                <a href="quan-ly-khoa-hoc?action=detail&id=${course.id}" class="btn btn-sm btn-outline-info" title="Xem chi tiết">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="quan-ly-khoa-hoc?action=edit&id=${course.id}" class="btn btn-sm btn-outline-primary">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="quan-ly-khoa-hoc?action=delete&id=${course.id}"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa khóa học này?');">
                                    <i class="fas fa-trash-alt"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty courseList}">
                    <tr>
                        <td colspan="12" class="text-center text-muted">Không có khóa học nào phù hợp.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>



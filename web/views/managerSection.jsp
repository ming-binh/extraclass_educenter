<%-- 
    Document   : managerSection
    Created on : Jun 26, 2025, 1:05:57 PM
    Author     : HanND
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/adminHeader.jsp" />

<style>
    .table-hover tbody tr:hover {
        background-color: #f9f9f9;
    }

    .action-buttons .btn {
        opacity: 0.6;
        transition: 0.2s ease;
    }

    tr:hover .action-buttons .btn {
        opacity: 1;
        box-shadow: 0 0 4px rgba(0, 0, 0, 0.15);
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
<form method="get" action="quan-ly-lop-hoc" class="mb-4">
    <div class="card border-0 shadow-sm">
        <div class="card-body bg-light">
            <div class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Tên khóa học</label>
                    <input type="text" class="form-control" name="keyword" value="${param.keyword}" placeholder="VD: Lớp luyện thi đại học" />
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-semibold">Ngày học</label>
                    <select class="form-select" name="dayOfWeek">
                        <option value="">-- Tất cả --</option>
                        <option value="Monday" ${param.dayOfWeek == 'Monday' ? 'selected' : ''}>Thứ 2</option>
                        <option value="Tuesday" ${param.dayOfWeek == 'Tuesday' ? 'selected' : ''}>Thứ 3</option>
                        <option value="Wednesday" ${param.dayOfWeek == 'Wednesday' ? 'selected' : ''}>Thứ 4</option>
                        <option value="Thursday" ${param.dayOfWeek == 'Thursday' ? 'selected' : ''}>Thứ 5</option>
                        <option value="Friday" ${param.dayOfWeek == 'Friday' ? 'selected' : ''}>Thứ 6</option>
                        <option value="Saturday" ${param.dayOfWeek == 'Saturday' ? 'selected' : ''}>Thứ 7</option>
                        <option value="Sunday" ${param.dayOfWeek == 'Sunday' ? 'selected' : ''}>Chủ nhật</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-semibold">Trạng thái</label>
                    <select class="form-select" name="status">
                        <option value="">-- Tất cả --</option>
                        <option value="active" ${param.status == 'active' ? 'selected' : ''}>Hoạt động</option>
                        <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Chưa diễn ra</option>
                        <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Hoàn tất</option>
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

<!-- Danh sách lớp học -->
<div class="card shadow-sm border-0 mt-4">
    <div class="card-header d-flex justify-content-between align-items-center bg-white">
        <h5 class="mb-0 fw-semibold"><i class="fas fa-users me-2 text-primary"></i>Danh sách lớp học</h5>
    </div>
    <div class="card-body table-responsive">
        <table class="table table-bordered table-hover align-middle text-center bg-white rounded">
            <thead class="table-light">
                <tr>
                    <th>STT</th>
                    <th>Tên khóa học</th>
                    <th>Thứ</th>
                    <th>Bắt đầu</th>
                    <th>Kết thúc</th>
                    <th>Phòng</th>
                    <th>Ngày học</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="section" items="${sectionList}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td class="text-start">${section.courseName}</td>
                        <td>
                            <c:out value="${
                                   section.dayOfWeek.toString() == 'Monday' ? 'Thứ 2' :
                                       section.dayOfWeek.toString() == 'Tuesday' ? 'Thứ 3' :
                                       section.dayOfWeek.toString() == 'Wednesday' ? 'Thứ 4' :
                                       section.dayOfWeek.toString() == 'Thursday' ? 'Thứ 5' :
                                       section.dayOfWeek.toString() == 'Friday' ? 'Thứ 6' :
                                       section.dayOfWeek.toString() == 'Saturday' ? 'Thứ 7' :
                                       section.dayOfWeek.toString() == 'Sunday' ? 'Chủ nhật' :
                                       section.dayOfWeek
                                   }"/>
                        </td>
                        <td>${section.startTimeFormatted}</td>
                        <td>${section.endTimeFormatted}</td>
                        <td>${section.classroom}</td>
                        <td>${section.dateFormatted}</td>
                        <td>
                            <c:choose>
                                <c:when test="${section.status.toString() == 'active'}">
                                    <span class="badge bg-success">Hoạt động</span>
                                </c:when>
                                <c:when test="${section.status.toString() == 'inactive'}">
                                    <span class="badge bg-secondary">Chưa diễn ra</span>
                                </c:when>
                                <c:when test="${section.status.toString() == 'completed'}">
                                    <span class="badge bg-info text-dark">Hoàn tất</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-warning text-dark">${section.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="action-buttons">
                            <div class="d-flex justify-content-center gap-2">
                                <a href="quan-ly-lop-hoc?action=detail&id=${section.id}" class="btn btn-sm btn-outline-info" title="Xem chi tiết">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <a href="quan-ly-lop-hoc?action=edit&id=${section.id}" class="btn btn-sm btn-outline-primary" title="Chỉnh sửa">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="quan-ly-lop-hoc?action=delete&id=${section.id}"
                                   class="btn btn-sm btn-outline-danger"
                                   onclick="return confirm('Bạn có chắc muốn xóa lớp học này?');"
                                   title="Xóa">
                                    <i class="fas fa-trash-alt"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty sectionList}">
                    <tr>
                        <td colspan="9" class="text-center text-muted">Không có lớp học nào phù hợp.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>


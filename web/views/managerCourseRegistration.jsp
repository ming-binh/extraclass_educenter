<%-- 
    Document   : managerCourseRegistration
    Created on : Jul 5, 2025, 4:45:52 PM
    Author     : hungd
--%>


<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/adminHeader.jsp" />

<style>
    .table-hover tbody tr:hover {
        background-color: #f9f9f9;
    }

    .table th, .table td {
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

    .badge-count {
        padding: 0.5em 1em;
        font-size: 1rem;
    }

    .btn-action {
        transition: 0.2s ease;
        opacity: 0.7;
    }

    tr:hover .btn-action {
        opacity: 1;
        box-shadow: 0 0 4px rgba(0, 0, 0, 0.15);
    }
</style>

<div class="card shadow-sm border-0 mt-4">
    <div class="card-header d-flex justify-content-between align-items-center bg-white">
        <h5 class="mb-0 fw-semibold">
            <i class="fas fa-user-check me-2 text-primary"></i>
            Yêu cầu tham gia lớp học
        </h5>
    </div>
    <div class="card-body table-responsive">
        <table class="table table-bordered table-hover align-middle text-center bg-white rounded">
            <thead class="table-light">
                <tr>
                    <th>STT</th>
                    <th class="text-start">Tên khóa học</th>
                    <th>Số yêu cầu chờ duyệt</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${courseRequestOverview}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td class="text-start fw-semibold">${item.courseName}</td>
                        <td>
                            <span class="badge bg-warning text-dark px-3 py-2 fs-6">${item.pendingCount}</span>
                        </td>
                        <td>
                            <a href="chi-tiet-yeu-cau?courseId=${item.courseId}" class="btn btn-sm btn-primary">
                                <i class="fas fa-eye me-1"></i> Xem chi tiết
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty courseRequestOverview}">
                    <tr>
                        <td colspan="4" class="text-center text-muted">Không có yêu cầu nào đang chờ duyệt.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>


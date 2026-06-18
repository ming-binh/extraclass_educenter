<%-- 
    Document   : managerConsultation
    Created on : Jun 26, 2025, 8:16:01 PM
    Author     : HanND
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    /* Mặc định nút trong btn-group trong suốt, chỉ có viền */
    .btn-group .btn {
        background-color: transparent;
        color: #333;
        border: 1px solid #ccc;
        transition: background-color 0.3s ease, color 0.3s ease;
    }

    /* Khi hover, nút mới đổi màu nền và màu chữ tương ứng */
    .btn-group .btn:hover {
        color: #fff;
    }

    .btn-info.btn-group .btn:hover,
    .btn-info:hover {
        background-color: #0dcaf0;
        border-color: #0dcaf0;
    }

    .btn-success.btn-group .btn:hover,
    .btn-success:hover {
        background-color: #198754;
        border-color: #198754;
    }

    .btn-primary.btn-group .btn:hover,
    .btn-primary:hover {
        background-color: #0d6efd;
        border-color: #0d6efd !important;
    }

    /* Tắt shadow mặc định của Bootstrap btn khi hover */
    .btn-group .btn:focus, .btn-group .btn:active {
        box-shadow: none !important;
    }

    /* Căn đều và căn giữa các cột */
    table thead th, table tbody td {
        text-align: center;
        vertical-align: middle;
    }

    /* Định nghĩa độ rộng cột hợp lý cho cả hai bảng */
    table th:nth-child(1), table td:nth-child(1) {
        width: 5%;
    }     /* STT */
    table th:nth-child(2), table td:nth-child(2) {
        width: 20%;
        text-align: left;
        padding-left: 12px;
    } /* Họ tên */
    table th:nth-child(3), table td:nth-child(3) {
        width: 12%;
    }    /* Ngày sinh */
    table th:nth-child(4), table td:nth-child(4) {
        width: 13%;
    }    /* SĐT */
    table th:nth-child(5), table td:nth-child(5) {
        width: 22%;
        text-align: left;
        padding-left: 12px;
    } /* Email */
    table th:nth-child(6), table td:nth-child(6) {
        width: 13%;
    }    /* Trạng thái */
    table th:nth-child(7), table td:nth-child(7) {
        width: 15%;
    }    /* Hành động */

</style>

<jsp:include page="layout/adminHeader.jsp" />

<div class="container-fluid">
    <h4 class="my-3 fw-bold text-center">DANH SÁCH TƯ VẤN VÀ TUYỂN DỤNG</h4>

    <form action="quan-ly-tu-van" method="get" class="d-flex justify-content-center gap-3 my-4">
        <div class="input-group w-auto">
            <input type="text" class="form-control" name="name" placeholder="Tìm kiếm theo tên..." value="${param.name}">
        </div>
        <div class="input-group w-auto">
            <select class="form-select" name="status">
                <option value="">-- Tất cả trạng thái --</option>
                <option value="pending" ${param.status == 'pending' ? 'selected' : ''}>Chờ xử lý</option>
                <option value="accepted" ${param.status == 'accepted' ? 'selected' : ''}>Đã xử lý</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Lọc</button>
    </form>

    <!-- Danh sách Giáo viên -->
    <c:if test="${not empty teacherList}">
        <h5 class="mt-4 fw-bold">Giáo viên</h5>
        <table class="table table-hover table-bordered align-middle text-center">
            <thead class="table-primary">
                <tr>
                    <th>STT</th>
                    <th>Họ tên</th>
                    <th>Ngày sinh</th>
                    <th>SĐT</th>
                    <th>Email</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="consult" items="${teacherList}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${consult.name}</td>
                        <td>${consult.dobString}</td>
                        <td>${consult.phone}</td>
                        <td>${consult.email}</td>
                        <td>
                            <c:choose>
                                <c:when test="${consult.status.toString() == 'pending'}">
                                    <span class="badge bg-warning text-dark">Chờ xử lý</span>
                                </c:when>
                                <c:when test="${consult.status.toString() == 'accepted'}">
                                    <span class="badge bg-success">Đã xử lý</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Không rõ</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <!-- Xem chi tiết -->
                            <a href="quan-ly-tu-van?action=detail&id=${consult.id}" class="text-info me-2" title="Chi tiết">
                                <i class="fas fa-eye"></i>
                            </a>

                            <!-- Duyệt yêu cầu -->
                            <a href="quan-ly-tu-van?action=approve&id=${consult.id}" class="text-success me-2" title="Duyệt tư vấn">
                                <i class="fas fa-check-circle"></i>
                            </a>

                            <!-- Tạo tài khoản -->
                            <a href="quan-ly-tu-van?action=create&id=${consult.id}" class="text-primary" title="Tạo tài khoản">
                                <i class="fas fa-user-plus"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <!-- Danh sách Phụ huynh / Học sinh -->
    <c:if test="${not empty studentParentList}">
        <h5 class="mt-4 fw-bold">Phụ huynh</h5>
        <table class="table table-hover table-bordered align-middle text-center">
            <thead class="table-success">
                <tr>
                    <th>STT</th>
                    <th>Họ tên</th>
                    <th>Ngày sinh</th>
                    <th>SĐT</th>
                    <th>Email</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="consult" items="${studentParentList}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${consult.name}</td>
                        <td>${consult.dobString}</td>
                        <td>${consult.phone}</td>
                        <td>${consult.email}</td>
                        <td>
                            <c:choose>
                                <c:when test="${consult.status.toString() == 'pending'}">
                                    <span class="badge bg-warning text-dark">Chờ xử lý</span>
                                </c:when>
                                <c:when test="${consult.status.toString() == 'accepted'}">
                                    <span class="badge bg-success">Đã xử lý</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Không rõ</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <!-- Xem chi tiết -->
                            <a href="quan-ly-tu-van?action=detail&id=${consult.id}" class="text-info me-2" title="Chi tiết">
                                <i class="fas fa-eye"></i>
                            </a>

                            <!-- Duyệt yêu cầu -->
                            <a href="quan-ly-tu-van?action=approve&id=${consult.id}" class="text-success me-2" title="Duyệt tư vấn">
                                <i class="fas fa-check-circle"></i>
                            </a>

                            <!-- Tạo tài khoản -->
                            <a href="quan-ly-tu-van?action=create&id=${consult.id}" class="text-primary" title="Tạo tài khoản">
                                <i class="fas fa-user-plus"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

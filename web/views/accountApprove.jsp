<%-- 
    Document   : accountApprove
    Created on : Jul 25, 2025, 9:29:23 AM
    Author     : vankh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/adminHeader.jsp" />

<style>
    .status-pending {
        background-color: #a79916;
        color: white;
        padding: 5px 10px;
        border-radius: 20px;
        font-weight: 500;
        display: inline-block;
    }
    .action-buttons .btn {
        padding: 5px 10px;
        margin: 0 2px;
        font-size: 14px;
    }

    .table-responsive {
        margin-top: 20px;
    }

    .search-filter {
        background: #f9f9f9;
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 20px;
    }

    .role-staff {
        background-color: #a0ad26;
        color: white;
        padding: 3px 8px;
        border-radius: 4px;
    }

    .role-teacher {
        background-color: #1693a7;
        color: white;
        padding: 3px 8px;
        border-radius: 4px;
    }

    .role-parent {
        background-color: #941cac;
        color: white;
        padding: 3px 8px;
        border-radius: 4px;
    }

    .role-student {
        background-color: #188618;
        color: white;
        padding: 3px 8px;
        border-radius: 4px;
    }

    .add-account-btn {
        margin-bottom: 20px;

    }

    #pagination-controls button {
        margin: 0 4px;
        padding: 6px 12px;
        border: none;
        background-color: #007bff;
        color: white;
        border-radius: 4px;
        cursor: pointer;
        margin-bottom: 5px;
    }
    #pagination-controls button.active {
        background-color: #0056b3;
        font-weight: bold;
    }


</style>

<div class="container-fluid">

    <!-- Search and Filter Section -->
    <div class="search-filter">
        <form class="row g-3" method="get" action="duyet-tai-khoan">
            <!--            <input type="hidden" name="action" value="filter" >-->
            <!--            <div class="col-md-4">
                            <label class="form-label">Tìm kiếm</label>
                            <input type="text" class="form-control" name="searchKeyword" placeholder="Họ tên, tài khoản..." value="${param.searchKeyword}">
                        </div>-->
            <!--            <div class="col-md-3">
                            <label class="form-label">Trạng thái</label>
                            <select class="form-select" name="statusFilter">
                                <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>Tất cả</option>
                                <option value="active" ${statusFilter == 'active' ? 'selected' : ''}>Hoạt động</option>
                                <option value="inactive" ${statusFilter == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                            </select>
                        </div>-->
            <div class="col-md-3">
                <label class="form-label">Chức vụ</label>
                <select class="form-select" name="roleFilter">
                    <option value="all" ${roleFilter == 'all' ? 'selected' : ''}>Tất cả</option>
                    <option value="staff" ${roleFilter == 'staff' ? 'selected' : ''}>Nhân viên tư vấn </option>
                    <option value="teacher" ${roleFilter == 'teacher' ? 'selected' : ''}>Giáo viên</option>
                    <option value="student" ${roleFilter == 'student' ? 'selected' : ''}>Học viên</option>
                </select>
            </div>
            <div class="col-md-2 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">Lọc</button>
            </div>
        </form>
    </div>

    <!-- Accounts Table -->
    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead class="table-dark">
                <tr>
                    <th>STT</th>
                    <th>Họ và tên</th>
                    <th>Tên tài khoản</th>
                    <th>Số điện thoại</th>
                    <th>Địa chỉ</th>
                    <th>Trạng thái</th>
                    <th>Chức vụ</th>
                    <th>Hoạt động</th>
                </tr>
            </thead>
            <tbody id="account-table-body">
                <c:forEach var="account" items="${accounts}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${account.name}</td>
                        <td>${account.username}</td>
                        <td>${account.phone}</td>
                        <td>${account.address}</td>
                        <td>
                            <span class="status-pending">
                                Chờ
                            </span>

                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${account.role == 'teacher'}">
                                    <span class="role-teacher">Giáo viên</span>
                                </c:when>
                                <c:when test="${account.role == 'staff'}">
                                    <span class="role-staff">Nhân viên tư vấn</span>
                                </c:when>
                                <c:when test="${account.role == 'parent'}">
                                    <span class="role-parent">Phụ huynh</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="role-student">Học sinh</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="action-buttons">
                            <a href="duyet-tai-khoan?accountId=${account.id}&action=delete&roleFilter=${roleFilter}" 
                               class="btn btn-danger btn-sm" title="Xóa tài khoản">
                                <i class="fas fa-trash"></i>
                            </a>
                            <a href="duyet-tai-khoan?accountId=${account.id}&action=acept&roleFilter=${roleFilter}" 
                               class="btn btn-warning btn-sm" title="Duyệt tài khoản ">
                                <i class="fas fa-user-check" style="color: #63E6BE;"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table> 
        </table> 
        <div id="pagination-controls" class="mt-3 text-center"></div>

    </div>
</div>

<script>
    function confirmDelete(accountId) {
        document.getElementById('deleteAccountId').value = accountId;
        $('#deleteConfirmModal').modal('show');
    }

    function resetPassword(accountId) {
        document.getElementById('resetPasswordAccountId').value = accountId;
        $('#resetPasswordModal').modal('show');
    }

    const rowsPerPage = 10;
    const tableBody = document.getElementById("account-table-body");
    const rows = tableBody.getElementsByTagName("tr");
    const paginationControls = document.getElementById("pagination-controls");

    let currentPage = 1;
    const totalPages = Math.ceil(rows.length / rowsPerPage);

    function showPage(page) {
        currentPage = page;
        for (let i = 0; i < rows.length; i++) {
            rows[i].style.display = (i >= (page - 1) * rowsPerPage && i < page * rowsPerPage) ? "" : "none";
        }
        renderPagination();
    }

    function renderPagination() {
        paginationControls.innerHTML = "";
        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.className = (i === currentPage) ? "active" : "";
            btn.onclick = () => showPage(i);
            paginationControls.appendChild(btn);
        }
    }

    // Hiển thị trang đầu tiên khi tải xong
    showPage(1);

</script>

<jsp:include page="layout/footer.jsp" />

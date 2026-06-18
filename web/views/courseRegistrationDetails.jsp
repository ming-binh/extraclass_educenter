<%-- 
    Document   : courseRegistrationDetails
    Created on : Jul 6, 2025, 2:45:04 PM
    Author     : hungd
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="utils.DateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    .btn-action {
        transition: 0.2s ease;
        opacity: 0.7;
    }
    tr:hover .btn-action {
        opacity: 1;
        box-shadow: 0 0 4px rgba(0, 0, 0, 0.15);
    }
    .course-info strong {
        color: #333;
    }

    .badge {
        font-size: 0.95rem; /* mặc định thường là 0.75rem */
        padding: 0.5em 0.75em;
        font-weight: 500;
    }

    /* Lớp phủ nền đen mờ */
    #confirm-popup.popup-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background-color: rgba(0, 0, 0, 0.5);
        display: none;
        align-items: center;
        justify-content: center;
        z-index: 10000;
    }

    /* Nội dung hộp thoại */
    #confirm-popup .popup-box {
        background-color: #fff8f2;
        border: 2px solid #ffa94d;
        border-radius: 20px;
        padding: 30px;
        width: 400px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.25);
        animation: fadeInUp 0.3s ease-out;
        text-align: center;
    }

    /* Tiêu đề popup */
    #confirm-popup .popup-header {
        font-size: 20px;
        font-weight: 700;
        color: #ff7f00;
        margin-bottom: 12px;
    }

    /* Nội dung */
    #popup-message {
        font-size: 16px;
        color: #333;
        margin-bottom: 24px;
    }

    /* Nút */
    .popup-footer button {
        padding: 8px 20px;
        font-weight: 600;
        font-size: 14px;
        border-radius: 8px;
        margin: 0 8px;
        min-width: 100px;
        cursor: pointer;
    }

    .popup-footer .btn-primary {
        background-color: #ff8800;
        color: white;
        border: none;
    }

    .popup-footer .btn-primary:hover {
        background-color: #e67600;
    }

    .popup-footer .btn-secondary {
        background-color: #f1f1f1;
        color: #333;
        border: none;
    }

    .popup-footer .btn-secondary:hover {
        background-color: #e0e0e0;
    }

    /* Hiệu ứng xuất hiện */
    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .custom-alert {
        position: relative;
        padding: 0.75rem 1.25rem;
        margin: 1rem;
        border: 1px solid transparent;
        border-radius: 0.25rem;
        font-size: 1rem;
        font-family: system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        display: flex;
        align-items: center;
        animation: fadeIn 0.3s ease-in-out;
        box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.05);
    }

    .custom-alert.success {
        color: #0f5132;
        background-color: #d1e7dd;
        border-color: #badbcc;
    }

    .custom-alert.error {
        color: #842029;
        background-color: #f8d7da;
        border-color: #f5c2c7;
    }

    .custom-alert .alert-icon {
        margin-right: 0.5rem;
        font-size: 1.25rem;
        line-height: 1;
    }

    .custom-alert .close-btn {
        position: absolute;
        top: 0.5rem;
        right: 0.75rem;
        font-size: 1.25rem;
        font-weight: bold;
        line-height: 1;
        color: inherit;
        background: none;
        border: none;
        cursor: pointer;
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(-10px);
        }
        to   {
            opacity: 1;
            transform: translateY(0);
        }
    }

</style>

<!-- THÔNG TIN KHÓA HỌC -->
<div class="card border-0 shadow-sm mt-4 mb-3">
    <div class="card-body course-info">
        <h5 class="fw-bold text-dark mb-3">${course.name}</h5>
        <div class="row g-3">
            <div class="col-md-4"><strong>Giáo viên phụ trách:</strong> ${course.teacherName}</div>
            <div class="col-md-4"><strong>Môn học:</strong> ${course.subject}</div>
            <div class="col-md-4"><strong>Khối:</strong> ${course.grade}</div>

            <div class="col-md-4">
                <strong>Thời gian diễn ra:</strong>
                <fmt:formatDate value="${course.startDate}" pattern="dd/MM/yyyy" /> – 
                <fmt:formatDate value="${course.endDate}" pattern="dd/MM/yyyy" />
            </div>

            <div class="col-md-4">
                <strong>Sĩ số hiện tại:</strong> ${course.studentEnrollment} / ${course.maxStudents}
            </div>
        </div>
    </div>
    <c:if test="${not empty message}">
        <div class="custom-alert success" id="custom-alert-message">
            <span class="alert-icon">✔️</span> ${message}
            <span class="close-btn" onclick="this.parentElement.style.display = 'none';">×</span>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="custom-alert error" id="custom-alert-error">
            <span class="alert-icon">❌</span> ${errorMessage}
            <button class="close-btn" onclick="this.parentElement.style.display = 'none';">×</button>
        </div>
    </c:if>
</div>

<!-- DANH SÁCH YÊU CẦU -->
<div class="card shadow-sm border-0">
    <div class="card-header bg-white d-flex justify-content-between align-items-center">
        <h5 class="mb-0 fw-semibold text-dark">
            <i class="fas fa-user-clock me-2 text-primary"></i> Danh sách yêu cầu tham gia
        </h5>
    </div>
    <div class="card-body table-responsive">
        <table class="table table-bordered table-hover align-middle text-center bg-white rounded">
            <thead class="table-light">
                <tr>
                    <th>STT</th>
                    <th>Học sinh</th>
                    <th>Thời gian đăng ký</th>
                    <th>Trạng thái hợp lệ</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="request" items="${requestList}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td class="text-start fw-semibold">${request.studentName}</td>
                        <td>
                            ${DateFormat.formatDateTime(request.enrollmentDate)}
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${request.validStatus eq 'valid'}">
                                    <span class="badge bg-success">Hợp lệ</span>
                                </c:when>
                                <c:when test="${request.validStatus eq 'invalid_fees'}">
                                    <span class="badge bg-danger">Nợ xấu học phí</span>
                                </c:when>
                                <c:when test="${request.validStatus eq 'invalid_schedule'}">
                                    <span class="badge bg-warning text-dark">Trùng lịch học</span>
                                </c:when>
                                <c:when test="${request.validStatus eq 'invalid_full'}">
                                    <span class="badge bg-secondary">Lớp đầy</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-light text-dark">Không xác định</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form method="post" action="chi-tiet-yeu-cau" id="confirmForm-${request.requestId}">
                                <input type="hidden" name="requestId" value="${request.requestId}" />
                                <input type="hidden" name="action" id="action-${request.requestId}" />

                                <button type="button" class="btn btn-sm btn-success btn-action me-1"
                                        onclick="openConfirmPopup('${request.requestId}', 'accept')"
                                        title="Chấp nhận">
                                    <i class="fas fa-check"></i>
                                </button>

                                <button type="button" class="btn btn-sm btn-danger btn-action"
                                        onclick="openConfirmPopup('${request.requestId}', 'reject')"
                                        title="Từ chối">
                                    <i class="fas fa-times"></i>
                                </button>
                            </form>
                        </td>

                    </tr>
                </c:forEach>

                <c:if test="${empty requestList}">
                    <tr>
                        <td colspan="5" class="text-center text-muted">Không có yêu cầu nào chờ duyệt.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <!-- Popup xác nhận -->
        <div id="confirm-popup" class="popup-overlay" style="display: none;">
            <div class="popup-box">
                <div class="popup-header">
                    <i class="fas fa-exclamation-circle me-2 text-warning"></i>
                    <span class="fw-bold">XÁC NHẬN YÊU CẦU</span>
                </div>
                <div class="popup-body" id="popup-message">
                    Bạn có chắc chắn muốn thực hiện yêu cầu này không?
                </div>
                <div class="popup-footer">
                    <button id="confirm-button" class="btn btn-primary">Xác nhận</button>
                    <button class="btn btn-secondary" onclick="closePopup()">Hủy</button>
                </div>
            </div>
        </div>

    </div>
</div>

<script>
    let currentRequestId = null;
    let currentAction = null;

    function openConfirmPopup(requestId, action) {
        currentRequestId = requestId;
        currentAction = action;

        // Không cần ghi rõ Chấp nhận hay Từ chối nữa
        document.getElementById("popup-message").innerText =
                "Bạn có chắc chắn muốn thực hiện yêu cầu này không?";

        document.getElementById("confirm-button").innerText = "Xác nhận";
        document.getElementById("confirm-popup").style.display = "flex";
    }

    function closePopup() {
        document.getElementById("confirm-popup").style.display = "none";
    }

    document.querySelector("#confirm-button").addEventListener("click", function () {
        document.getElementById("action-" + currentRequestId).value = currentAction;
        document.getElementById("confirmForm-" + currentRequestId).submit();
    });

    setTimeout(function () {
        let success = document.getElementById('custom-alert-message');
        let error = document.getElementById('custom-alert-error');
        if (success)
            success.style.display = 'none';
        if (error)
            error.style.display = 'none';
    }, 8000);

</script>



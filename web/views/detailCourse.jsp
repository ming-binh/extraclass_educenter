<%-- 
    Document   : detailCourse
    Created on : Jul 25, 2025, 1:35:09 PM
    Author     : HanND
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="layout/adminHeader.jsp" />

<style>
    .label {
        font-weight: bold;
        margin-bottom: 4px;
        display: inline-block;
    }
    .info-box {
        margin-bottom: 1.5rem;
    }
    .description-box {
        max-height: 120px;
        overflow-y: auto;
        padding: 10px;
        background-color: #f8f9fa;
        border-radius: 6px;
        border: 1px solid #ccc;
    }
    .container {
        max-width: 1000px;
    }
    .card {
        border-radius: 10px;
        border: 1px solid #dee2e6;
        background-color: #ffffff;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
    }
    .badge {
        padding: 0.4em 0.6em;
        font-size: 0.9em;
    }
</style>

<div class="container mt-5">
    <h3 class="mb-4 text-center text-primary">
        <i class="fas fa-info-circle me-2"></i>Chi tiết khóa học
    </h3>

    <div class="row align-items-center mb-4 text-center text-md-start">
        <div class="col-md-4 mb-3 mb-md-0">
            <img src="assets/banners_course/${course.courseImg}" 
                 alt="Ảnh khóa học" 
                 class="img-fluid rounded shadow-sm border" 
                 style="max-height: 200px; object-fit: cover; width: 100%;">
        </div>
        <div class="col-md-8">
            <h4 class="text-success fw-bold">${course.name}</h4>
        </div>
    </div>
    <div class="row info-box">
        <div class="col-md-6">
            <span class="label">Môn học:</span>
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
        </div>
        <div class="col-md-6">
            <span class="label">Khối lớp:</span> ${course.grade}
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-12">
            <span class="label">Mô tả khóa học:</span>
            <div class="description-box">${course.description}</div>
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-6">
            <span class="label">Loại khóa học:</span>
            <c:choose>
                <c:when test="${course.courseType eq 'combo'}">Combo</c:when>
                <c:when test="${course.courseType eq 'daily'}">Hàng ngày</c:when>
                <c:otherwise>Không xác định</c:otherwise>
            </c:choose>
        </div>
        <div class="col-md-6">
            <span class="label">Học phí:</span>
            <c:choose>
                <c:when test="${course.courseType eq 'combo'}">
                    <fmt:formatNumber value="${course.feeCombo}" type="number" maxFractionDigits="0" /> VNĐ
                </c:when>
                <c:when test="${course.courseType eq 'daily'}">
                    <fmt:formatNumber value="${course.feeDaily}" type="number" maxFractionDigits="0" /> VNĐ
                </c:when>
                <c:otherwise>0 VNĐ</c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-6">
            <span class="label">Ngày bắt đầu:</span> ${formattedStartDate}
        </div>
        <div class="col-md-6">
            <span class="label">Ngày kết thúc:</span> ${formattedEndDate}
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-6">
            <span class="label">Số tuần học:</span> ${course.weekAmount}
        </div>
        <div class="col-md-6">
            <span class="label">Sĩ số:</span> ${course.studentEnrollment} / ${course.maxStudents}
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-6">
            <span class="label">Trình độ:</span>
            <c:choose>
                <c:when test="${course.level eq 'Foundation'}">Nhập môn</c:when>
                <c:when test="${course.level eq 'Basic'}">Cơ bản</c:when>
                <c:when test="${course.level eq 'Advanced'}">Nâng cao</c:when>
                <c:when test="${course.level eq 'Excellent'}">Xuất sắc</c:when>
                <c:when test="${course.level eq 'Topics_Exam'}">Chuyên đề / Luyện thi</c:when>
                <c:otherwise>Không xác định</c:otherwise>
            </c:choose>
        </div>
        <div class="col-md-6">
            <span class="label">Hot:</span>
            <c:if test="${course.isHot}">
                <span class="badge bg-danger">HOT</span>
            </c:if>
            <c:if test="${!course.isHot}">
                <span class="text-muted">Không</span>
            </c:if>
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-6">
            <span class="label">Giảm giá:</span> ${course.discountPercentage}%
        </div>
        <div class="col-md-6">
            <span class="label">Trạng thái:</span>
            <c:choose>
                <c:when test="${course.status eq 'activated'}"><span class="badge bg-success">Đang mở</span></c:when>
                <c:when test="${course.status eq 'upcoming'}"><span class="badge bg-info text-dark">Sắp mở</span></c:when>
                <c:when test="${course.status eq 'pending'}"><span class="badge bg-warning text-dark">Chờ duyệt</span></c:when>
                <c:when test="${course.status eq 'inactivated'}"><span class="badge bg-secondary">Ngừng hoạt động</span></c:when>
                <c:when test="${course.status eq 'rejected'}"><span class="badge bg-danger">Từ chối</span></c:when>
                <c:otherwise><span class="badge bg-dark">Không xác định</span></c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="row info-box">
        <div class="col-md-12">
            <span class="label">Giáo viên phụ trách:</span> <strong>${course.teacherName}</strong>
        </div>
    </div>

    <div class="text-end mt-4">
        <a href="quan-ly-khoa-hoc" class="btn btn-secondary">← Quay lại danh sách</a>
    </div>
</div>
</div>

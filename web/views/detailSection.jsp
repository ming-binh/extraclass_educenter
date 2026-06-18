<%-- 
    Document   : detailSection
    Created on : Jun 26, 2025, 4:28:14 PM
    Author     : HanND
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/adminHeader.jsp" />

<div class="container mt-4">
    <h4 class="mb-4"><i class="fas fa-info-circle text-info me-2"></i>Chi tiết lớp học</h4>

    <div class="card p-3 mb-4">
        <h5 class="mb-3 text-primary">${section.courseName}</h5>

        <div class="row mb-2">
            <div class="col-md-6">
                <strong>Giáo viên:</strong> ${section.teacherName}
            </div>
            <div class="col-md-6">
                <strong>Thứ:</strong> 
                <c:choose>
                    <c:when test="${section.dayOfWeek == 'Monday'}">Thứ 2</c:when>
                    <c:when test="${section.dayOfWeek == 'Tuesday'}">Thứ 3</c:when>
                    <c:when test="${section.dayOfWeek == 'Wednesday'}">Thứ 4</c:when>
                    <c:when test="${section.dayOfWeek == 'Thursday'}">Thứ 5</c:when>
                    <c:when test="${section.dayOfWeek == 'Friday'}">Thứ 6</c:when>
                    <c:when test="${section.dayOfWeek == 'Saturday'}">Thứ 7</c:when>
                    <c:otherwise>Chủ nhật</c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="row mb-2">
            <div class="col-md-6"><strong>Giờ bắt đầu:</strong> ${section.startTimeFormatted}</div>
            <div class="col-md-6"><strong>Giờ kết thúc:</strong> ${section.endTimeFormatted}</div>
        </div>
        <div class="row mb-2">
            <div class="col-md-6"><strong>Ngày học:</strong> ${section.dateFormatted}</div>
            <div class="col-md-6"><strong>Phòng học:</strong> ${section.classroom}</div>
        </div>
        <div class="row mb-2">
            <div class="col-md-6">
                <strong>Trạng thái:</strong> 
                <c:choose>
                    <c:when test="${section.status == 'active'}">Hoạt động</c:when>
                    <c:when test="${section.status == 'inactive'}">Tạm ngưng</c:when>
                    <c:otherwise>Hoàn tất</c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <h5 class="mb-3 text-success"><i class="fas fa-users me-2"></i>Danh sách học sinh</h5>
    <table class="table table-bordered table-striped">
        <thead class="table-secondary">
            <tr>
                <th>STT</th>
                <th>Họ tên</th>
                <th>Đã thanh toán</th>
                <th>Điểm danh</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="student" items="${studentList}" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}</td>
                    <td>${student.studentName}</td>
                    <td>
                        <span class="badge bg-${student.isPaid ? 'success' : 'danger'}">
                            ${student.isPaid ? 'Đã thanh toán' : 'Chưa thanh toán'}
                        </span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${student.attendanceStatus == 'present'}">
                                <span class="badge bg-info">Có mặt</span>
                            </c:when>
                            <c:when test="${student.attendanceStatus == 'absent'}">
                                <span class="badge bg-danger">Vắng</span>
                            </c:when>
                            <c:when test="${student.attendanceStatus == 'excused'}">
                                <span class="badge bg-warning text-dark">Có phép</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">Chưa diễn ra</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                </tr>
            </c:forEach>
            <c:if test="${empty studentList}">
                <tr>
                    <td colspan="4" class="text-center">Chưa có học sinh nào đăng ký lớp này.</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <a href="quan-ly-lop-hoc" class="btn btn-secondary mt-3">Quay lại danh sách</a>
</div>


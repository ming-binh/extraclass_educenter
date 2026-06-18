<%-- 
    Document   : detailConsultation
    Created on : Jun 26, 2025, 10:28:16 PM
    Author     : HanND
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="layout/adminHeader.jsp" />

<div class="container mt-4">
    <h2 class="mb-4 text-center">Chi tiết Tư vấn</h2>

    <table class="table table-bordered">
        <tbody>
            <tr>
                <th>Họ tên</th>
                <td>${consultation.name}</td>
            </tr>
            <tr>
                <th>Ngày sinh</th>
                <td>${consultation.dobString}</td>
            </tr>
            <tr>
                <th>Điện thoại</th>
                <td>${consultation.phone}</td>
            </tr>
            <tr>
                <th>Email</th>
                <td>${consultation.email}</td>
            </tr>
            <tr>
                <th>Trạng thái</th>
                <td class="text-capitalize">
                    <c:choose>
                        <c:when test="${consultation.status == 'pending'}">Chờ xử lý</c:when>
                        <c:when test="${consultation.status == 'accepted'}">Đã chấp nhận</c:when>
                        <c:when test="${consultation.status == 'rejected'}">Từ chối</c:when>
                        <c:otherwise>Không xác định</c:otherwise>
                    </c:choose>
                </td>

            </tr>

            <c:if test="${not empty consultation.address}">
                <tr>
                    <th>Địa chỉ</th>
                    <td>${consultation.address}</td>
                </tr>
            </c:if>

            <c:if test="${not empty consultation.subject}">
                <tr>
                    <th>Môn học</th>
                    <td>${consultation.subject}</td>
                </tr>
            </c:if>

            <c:if test="${not empty consultation.experience}">
                <tr>
                    <th>Kinh nghiệm</th>
                    <td>${consultation.experience}</td>
                </tr>
            </c:if>

            <c:if test="${not empty consultation.schoolName}">
                <tr>
                    <th>Trường</th>
                    <td>${consultation.schoolName}</td>
                </tr>
            </c:if>

            <c:if test="${not empty consultation.schoolClassName}">
                <tr>
                    <th>Lớp</th>
                    <td>${consultation.schoolClassName}</td>
                </tr>
            </c:if>

            <c:if test="${not empty consultation.certificateImageUrl}">
                <tr>
                    <th>Chứng chỉ</th>
                    <td>
                        <img src="${pageContext.request.contextPath}/${consultation.certificateImageUrl}" 
                             alt="Chứng chỉ" 
                             style="max-width: 300px; max-height: 200px;" />

                    </td>
                </tr>
            </c:if>

        </tbody>
    </table>

    <a href="quan-ly-tu-van" class="btn btn-secondary">Quay lại danh sách</a>
</div>

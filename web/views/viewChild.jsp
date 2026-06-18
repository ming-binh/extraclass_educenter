<%-- 
    Document   : viewChild
    Created on : Jun 27, 2025, 2:07:11 AM
    Author     : vankh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="layout/header.jsp" />
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

<style>
    .container-fluid {
        max-width: 1000px;
        margin: 80px auto 30px;
        padding: 0 15px;
    }

    .widget-box {
        background: #fff;
        border-radius: 12px;
        padding: 25px;
        box-shadow: 0 4px 16px rgba(0,0,0,0.08);
    }

    .wc-title h4 {
        text-align: start;
        font-size: 24px;
        margin-bottom: 15px;
        color: #333;
        font-size: 40px;
    }

    .parent-info {
        text-align: start;
        color: #666;
        margin-bottom: 20px;
        font-size: 20px;
    }

    .children-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
        gap: 20px;
    }

    .child-card {
        background: #f7f9fc;
        border-radius: 10px;
        padding: 20px;
        color: #333;
        border: 1px solid #e1e5eb;
    }

    .child-header {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
    }

    .child-avatar, .default-avatar {
        width: 55px;
        height: 55px;
        border-radius: 50%;
        object-fit: cover;
        margin-right: 12px;
        background: #ddd;
        display: flex;
        justify-content: center;
        align-items: center;
        font-weight: bold;
        font-size: 20px;
        color: #555;
    }

    .child-name {
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 2px;
    }

    .child-phone {
        font-size: 14px;
        color: #888;
    }

    .info-row {
        font-size: 14px;
        margin-bottom: 10px;
        display: flex;
        align-items: center;
    }

    .info-row i {
        width: 18px;
        margin-right: 8px;
        color: #666;
    }

    .view-scores-btn {
        display: inline-block;
        margin-top: 10px;
        padding: 8px 14px;
        background: #4a69bd;
        color: #fff;
        border: none;
        border-radius: 20px;
        font-size: 14px;
        cursor: pointer;
        transition: background 0.3s;
    }

    .view-scores-btn:hover {
        background: #3b55a0;
    }

    .no-data {
        text-align: center;
        padding: 60px 20px;
        font-size: 16px;
        color: #777;
    }
</style>

<div class="container-fluid">
    <div class="widget-box">
        <div class="wc-title" style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <h4>Danh sách con của phụ huynh</h4>
                <c:if test="${not empty parentName}">
                    <div class="parent-info">
                        <strong>${parentName}</strong>
                        <c:if test="${not empty parentPhone}">
                            | SĐT: ${parentPhone}
                        </c:if>
                    </div>
                </c:if>
            </div>

            <a href="tao-tai-khoan-hoc-sinh" class="view-scores-btn" style="background: #4a69bd;text-decoration: none;">
                <i class="fa-solid fa-user-plus" style="margin-right: 6px; "></i> Tạo tài khoản học sinh
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty childrenProfiles}">
                <div class="children-grid">
                    <c:forEach var="child" items="${childrenProfiles}">
                        <div class="child-card">
                            <div class="child-header">
                                <c:choose>
                                    <c:when test="${not empty child.avatarUrl}">
                                        <img src="${child.avatarUrl}" class="child-avatar" alt="avatar">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="default-avatar">${child.name.charAt(0)}</div>
                                    </c:otherwise>
                                </c:choose>
                                <div>
                                    <div class="child-name">${child.name}</div>
                                    <div class="child-phone">${child.phone}</div>
                                </div>
                            </div>
                            <div class="info">
                                <c:if test="${not empty child.status}">
                                    <div class="info-row"
                                         style="
                                         color: <c:choose>
                                             <c:when test='${child.status.name() == "active"}'>green</c:when>
                                             <c:when test='${child.status.name() == "pending"}'>orange</c:when>
                                             <c:when test='${child.status.name() == "inactive"}'>gray</c:when>
                                         </c:choose>;
                                         ">
                                        <i class="fa-solid
                                           <c:choose>
                                               <c:when test='${child.status.name() == "active"}'>fa-face-smile</c:when>
                                               <c:when test='${child.status.name() == "pending"}'>fa-face-meh</c:when>
                                               <c:when test='${child.status.name() == "inactive"}'>fa-solid fa-face-dizzy</c:when>
                                           </c:choose>"></i>
                                        Trạng thái: ${child.status.displayName}
                                    </div>
                                </c:if>

                                <c:if test="${not empty child.dob}">
                                    <div class="info-row">
                                        <i class="fa-solid fa-calendar-days"></i>
                                        Ngày sinh: ${dobMap[child.id]}
                                    </div>
                                </c:if>

                                <c:if test="${not empty child.currentGrade}">
                                    <div class="info-row">
                                        <i class="fa-solid fa-chalkboard-user"></i>
                                        Lớp hiện tại: ${child.currentGrade}
                                    </div>
                                </c:if>

                                <c:if test="${not empty child.schoolName}">
                                    <div class="info-row">
                                        <i class="fa-solid fa-school"></i>
                                        Trường: ${child.schoolName}
                                    </div>
                                </c:if>

                                <c:if test="${not empty child.address}">
                                    <div class="info-row">
                                        <i class="fa-solid fa-location-dot"></i>
                                        Địa chỉ: ${child.address}
                                    </div>
                                </c:if>

                                <div style="display: flex; gap: 10px; margin-top: 15px;">
                                    <form action="viewScore" method="post">
                                        <input type="hidden" name="studentId" value="${child.id}">
                                        <button type="submit" class="view-scores-btn">
                                            <i class="fa-solid fa-chart-line" style="margin-right: 5px;"></i> Xem điểm
                                        </button>
                                    </form>

                                    <a href="diem-chuyen-can?studentId=${child.id}" class="view-scores-btn" style="background: #38ada9; text-decoration: none;">
                                        <i class="fa-solid fa-user-check" style="margin-right: 5px;"></i> Chuyên cần
                                    </a>
                                    <a href="thoi-gian-bieu-phu-huynh?studentId=${child.id}" class="view-scores-btn" style="background: #7448fd; text-decoration: none;">
                                        <i class="fa-solid fa-user-check" style="margin-right: 5px;"></i> Xem lịch học
                                    </a>
                                </div>

                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    <i class="fa-solid fa-children fa-2x" style="margin-bottom: 10px;"></i><br/>
                    Không tìm thấy thông tin con nào<br/>
                    <small>Vui lòng liên hệ với nhà trường để cập nhật thông tin</small>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

</div>

<jsp:include page="layout/footer.jsp" />

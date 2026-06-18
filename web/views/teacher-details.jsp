<%-- 
    Document   : teacher-details
    Created on : Jun 22, 2025, 11:25:18 PM
    Author     : hungd
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<% request.setAttribute("title", "Thông tin giáo viên");%>
<jsp:include page="layout/header.jsp" />
<style>
    /* ===== Reset & Base ===== */
    body {
        margin: 0;
        font-family: "Segoe UI", sans-serif;
        background: #fff;
        color: #333;
        line-height: 1.6;
    }
    main {
        overflow-x: hidden;
    }
    /* ===== Banner (ảnh phải, chữ trái) ===== */
    .banner.full-banner {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 60px 80px;
        background: linear-gradient(to right, #ffffff, #e6f0ff);
        flex-wrap: wrap;
        gap: 40px;
    }
    .banner-left {
        flex: 1;
        min-width: 300px;
    }
    .banner-left .sub-title {
        font-size: 18px;
        color: #888;
        letter-spacing: 1px;
        text-transform: uppercase;
        margin-bottom: 10px;
    }
    .banner-left .teacher-name {
        font-size: 40px;
        color: #007BFF;
        margin: 0;
    }
    .banner-left .subject {
        font-size: 20px;
        color: #333;
    }
    .banner-right {
        flex: 1;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .banner-img-full {
        max-height: 440px;
        width: auto;
        border-radius: 20px;
        border: 6px solid #fff;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
        background: #fff;
    }
    /* ===== Stats section ===== */
    .stats {
        display: flex;
        justify-content: center;
        gap: 60px;
        background: #2b2f3e;
        color: #fff;
        padding: 40px 20px;
        flex-wrap: wrap;
        text-align: center;
    }
    .stat-box strong {
        font-size: 28px;
        display: block;
    }
    .stat-box p {
        margin-top: 5px;
        font-size: 16px;
    }
    /* ===== Info Box ===== */
    .info-box {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        padding: 40px 20px;
        background: #f7f9fb;
        gap: 30px;
    }
    .info-left .avatar {
        width: 200px;
        border-radius: 10px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    }
    .info-right {
        max-width: 500px;
    }
    .info-right h2 {
        font-size: 24px;
        margin-bottom: 20px;
        color: #007BFF;
    }
    .info-right ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    .info-right li {
        margin-bottom: 10px;
        font-size: 16px;
    }
    /* ===== Story Section ===== */
    .story {
        background-color: #fff8dc; /* vàng nhạt (cornsilk)*/
        padding: 30px;
        border-radius: 12px;
        max-width: 900px;
        margin: 40px auto; /* căn giữa theo chiều ngang */
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        text-align: center;
    }
    
    .story h2 {
        font-size: 24px;
        margin-bottom: 30px;
        color: #007BFF;
    }
    .story-content {
        display: flex;
        gap: 30px;
        flex-wrap: wrap;
        align-items: flex-start;
        justify-content: center;
    }
    .story-content .avatar {
        width: 280px;
        max-height: 300px;
        object-fit: cover;
        border-radius: 10px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
    }
    .story-text {
        max-width: 600px;
        font-size: 16px;
        text-align: left;
        line-height: 1.7;
    }

    ul li a {
        text-decoration: none;
        font-weight: bold;
        color: #333;
        transition: color 0.2s ease;
    }

    ul li a:hover {
        color: #007bff; /* màu xanh khi hover */
    }

    ul li img {
        max-width: 100%;
        max-height: 300px;
        border-radius: 6px;
        border: 1px solid #ccc;
    }
    /* ===== Course List ===== */
    .teacher-course-section {
        padding: 40px 150px;
        background-color: #f9fafb;
    }
    .teacher-course-section h2 {
        font-size: 1.6rem;
        font-weight: bold;
        text-align: center;
        margin-bottom: 30px;
        color: #4b2995;
    }
    /* Carousel Wrapper: chiều rộng giới hạn, không cuộn ngang */
    .teacher-carousel-wrapper {
        overflow: hidden;
        width: 100%;
        position: relative;
    }
    /* Carousel: dùng flex, độ rộng theo số page */
    .teacher-carousel {
        display: flex;
        transition: transform 0.4s ease-in-out;
        width: 100%;
    }
    /* Mỗi khóa học */
    .teacher-course-card {
        flex: 0 0 25%;
        max-width: 25%;
        padding: 0 12px;
        box-sizing: border-box;
        cursor: pointer;
        display: flex;
        flex-direction: column;
        height: 100%; /* giúp thẻ cao bằng nhau */
    }
    /* Card image giữ tỉ lệ đẹp */
    .teacher-card-image {
        width: 100%;
        aspect-ratio: 4 / 2.8;
        border-radius: 12px;
        overflow: hidden;
        position: relative;
        background-color: #fff;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
    }
    /* Đồng nhất chiều cao nội dung */
    .teacher-course-content {
        flex: 1;
        padding: 10px 12px;
        background-color: white;
        border-radius: 10px;
        margin-top: 10px;
        text-align: left;
        box-shadow: 0 1px 4px rgba(0,0,0,0.04);
        display: flex;
        flex-direction: column;
        justify-content: space-between; /* giãn đều nếu có nhiều nội dung */
        min-height: 120px; /* bạn có thể điều chỉnh số này tùy vào thiết kế */
    }
    .teacher-card-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.25s ease;
    }
    .teacher-card-image img:hover {
        transform: scale(1.03);
    }
    /* Giảm giá */
    .teacher-discount-badge {
        position: absolute;
        top: 10px;
        right: 10px;
        background-color: #ef4444;
        color: white;
        font-size: 0.75rem;
        padding: 4px 8px;
        border-radius: 8px;
        font-weight: bold;
    }
    .teacher-course-content h5 {
        font-size: 1rem;
        font-weight: 600;
        margin-bottom: 6px;
        color: #111827;
    }
    .teacher-course-content p {
        font-size: 0.85rem;
        margin: 3px 0;
        color: #4b5563;
    }
    /* Pagination */
    .teacher-carousel-pagination {
        display: flex;
        justify-content: center;
        gap: 10px;
        margin-top: 20px;
    }
    .teacher-carousel-pagination button {
        background-color: #7e3ff2;
        color: white;
        padding: 8px 18px;
        border: none;
        border-radius: 6px;
        font-size: 0.95rem;
        font-weight: 500;
        cursor: pointer;
        transition: background 0.2s ease;
    }
    .teacher-carousel-pagination button:hover {
        background-color: #5b21b6;
    }
    /* Responsive */
    @media (max-width: 992px) {
        .teacher-course-card {
            flex: 0 0 50%;
            max-width: 50%;
            margin-bottom: 20px;
        }
    }
    @media (max-width: 600px) {
        .teacher-course-card {
            flex: 0 0 100%;
            max-width: 100%;
        }
    }
    /* ===== Responsive ===== */
    @media (max-width: 768px) {
        .banner.full-banner {
            flex-direction: column-reverse;
            padding: 40px 20px;
            text-align: center;
        }
        .banner-img-full {
            max-height: 300px;
        }
        .stats {
            gap: 20px;
        }
        .story-content {
            flex-direction: column;
            align-items: center;
        }
        .carousel-btn {
            display: none;
        }
        .grid-wrapper {
            overflow-x: scroll;
        }
    }
</style>
<main>
    <!-- Gán biến từ requestScope -->
    <c:set var="teacher" value="${requestScope.teacher}" />
    <c:set var="info" value="${requestScope.infoOfTeacher}" />
    <c:set var="courseOfTeacher" value="${requestScope.courseOfTeacher}" />
    <!-- PHẦN 1: Banner -->
    <section class="banner full-banner">
        <div class="banner-left">
            <p class="sub-title">Giáo viên</p>
            <h1 class="teacher-name">${info.name}</h1>
            <p class="subject">Giáo Viên ${teacher.subject.displayName}</p>
        </div>
        <div class="banner-right">
            <img src="${pageContext.request.contextPath}/assets/avatars/${info.avatarURL}" class="banner-img-full" alt="Ảnh giáo viên">
        </div>
    </section>
    <!-- PHẦN 2: Số liệu thống kê -->
    <section class="stats">
        <div class="stat-box">
            <strong>${followers} +</strong>
            <p>học sinh đang theo học</p>
        </div>
        <div class="stat-box">
            <strong>${fn:length(courseOfTeacher)} +</strong>
            <p>khóa học</p>
        </div>
        <div class="stat-box">
            <strong>${teacher.experienceNumber} +</strong>
            <p>năm kinh nghiệm</p>
        </div>
    </section>
    <!-- PHẦN 3: Thông tin chi tiết -->
    <section class="info-box">
        <div class="info-left">
            <img src="${pageContext.request.contextPath}/assets/avatars/${info.avatarURL}" class="avatar" alt="Avatar giáo viên">
        </div>
        <div class="info-right">
            <h2>THÔNG TIN GIÁO VIÊN</h2>
            <ul>
                <li><strong>Họ và tên:</strong> ${info.name}</li>
                <li><strong>Nơi công tác:</strong> ${sessionScope.schoolMap[teacher.schoolId].name}</li>
                <li><strong>Môn dạy:</strong> ${teacher.subject.displayName}</li>
                <li><strong>Liên hệ:</strong> ${info.phone}</li>
            </ul>
        </div>
    </section>
    <!-- PHẦN 4: Câu chuyện thú vị -->
    <section class="story">
        <h2>ĐÔI NÉT VỀ GIÁO VIÊN</h2>
        <div class="story-content">
            <img src="${pageContext.request.contextPath}/assets/avatars/${info.avatarURL}" class="avatar" alt="Avatar giáo viên">
            <div class="story-text">
                <p>${teacher.bio}</p>

                <c:if test="${not empty achiveOfTeacher}">
                    <div class="teacher-awards">
                        <p><strong>Thành tích nổi bật:</strong></p>
                        <ul>
                            <c:forEach var="ach" items="${achiveOfTeacher}">
                                <li>${ach.achivementName} (${achiveYearMap[ach.id]})</li>
                                </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <c:if test="${not empty certOfTeacher}">
                    <div class="teacher-certificates">
                        <p><strong>Chứng chỉ đạt được:</strong></p>
                        <ul>
                            <c:forEach var="cert" items="${certOfTeacher}">
                                <li>
                                    <a href="javascript:void(0);" onclick="toggleCertImage('${cert.id}')">
                                        ${cert.certificateName} (${certYearMap[cert.id]})
                                    </a>
                                    <div id="cert-img-${cert.id}" style="display: none; margin-top: 5px;">
                                        <img src="${pageContext.request.contextPath}/assets/${cert.imageURL}" alt="Certificate Image" style="max-width: 300px; border: 1px solid #ccc;" />
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

            </div>
        </div>
    </section>
    <!-- PHẦN 5: Khóa học -->
    <section class="teacher-course-section">
        <h2>LỚP HỌC GIẢNG DẠY</h2>
        <div class="teacher-carousel-wrapper">
            <div class="teacher-carousel" id="teacherCourseCarousel">
                <c:forEach var="course" items="${courseOfTeacher}" varStatus="status">
                    <div class="teacher-course-card" data-page="${(status.index div 4) + 1}" onclick="goToCourseDetail('${course.id}')">
                        <div class="teacher-card-image">
                            <img src="${pageContext.request.contextPath}/assets/banners_course/${course.course_img}" alt="Hình khóa học" />
                            <c:if test="${course.discountPercentage > 0}">
                                <div class="teacher-discount-badge">-${course.discountPercentage}%</div>
                            </c:if>
                        </div>
                        <div class="teacher-course-content">
                            <h5>${course.name}</h5>
                            <!-- Mô tả rút gọn -->
                            <p class="course-description">
                                <c:choose>
                                    <c:when test="${fn:length(course.description) > 80}">
                                        ${fn:substring(course.description, 0, 80)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${course.description}
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <p><strong>Môn:</strong> ${course.subject.displayName}</p>
                            <p><strong>Thời lượng:</strong> ${course.weekAmount} tuần</p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="teacher-carousel-pagination">
            <button onclick="teacherSlide(-1)">← Trước</button>
            <button onclick="teacherSlide(1)">Tiếp →</button>
        </div>
    </section>
</main>
<jsp:include page="layout/footer.jsp" />
<script>
    let teacherCurrentPage = 1;
    const coursesPerPage = 4;
    function teacherSlide(direction) {
        const items = document.querySelectorAll('.teacher-course-card');
        const totalPages = Math.ceil(items.length / coursesPerPage);
        teacherCurrentPage += direction;
        if (teacherCurrentPage < 1)
            teacherCurrentPage = 1;
        if (teacherCurrentPage > totalPages)
            teacherCurrentPage = totalPages;
        const carousel = document.getElementById('teacherCourseCarousel');
        const offset = (teacherCurrentPage - 1) * 100;
        carousel.style.transform = `translateX(-${offset}%)`;
    }
    function goToCourseDetail(courseId) {
        window.location.href = 'thong-tin-lop-hoc?courseId=' + courseId;
    }
    window.onload = () => {
        teacherSlide(0); // Khởi tạo slide đầu tiên
    };

    function toggleCertImage(certId) {
        const imgDiv = document.getElementById('cert-img-' + certId);
        if (imgDiv.style.display === 'none') {
            imgDiv.style.display = 'block';
        } else {
            imgDiv.style.display = 'none';
        }
    }
</script>

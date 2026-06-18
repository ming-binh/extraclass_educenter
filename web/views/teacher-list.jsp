<%-- 
    Document   : teacher-list
    Created on : Jun 21, 2025, 10:31:55 PM
    Author     : hungd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<% request.setAttribute("title", "Danh sách giáo viên");%>
<jsp:include page="layout/header.jsp" />


<style>
    :root {
        --primary-color: #673ab7;
        --accent-color: #ff5722;
        --bg-light: #f5f6fa;
        --text-color: #333333;
        --border-color: #e0e0e0;
    }

    body {
        font-family: 'Open Sans', sans-serif;
        margin: 0;
        padding: 0;
        background-color: var(--bg-light);
        color: var(--text-color);
        overflow-x: hidden;
    }

    .teacher-page {
        padding: 2.5rem 1.5rem;
        max-width: 1200px;
        margin: 0 auto;
    }

    .section-title {
        font-size: 2.2rem;
        font-weight: 700;
        margin-bottom: 2rem;
        color: var(--primary-color);
        text-align: center;
        position: relative;
        padding-bottom: 10px;
    }
    .section-title::after {
        content: '';
        position: absolute;
        left: 50%;
        bottom: 0;
        transform: translateX(-50%);
        width: 80px;
        height: 4px;
        background-color: var(--primary-color);
        border-radius: 2px;
    }

    /* Highlight section for featured teachers */
    .highlight-section {
        width: 100vw;
        position: relative;
        left: 50%;
        right: 50%;
        margin-left: -50vw;
        margin-right: -50vw;
        background-color: #ffffff;
        padding: 3rem 1rem 4rem 1rem;
        box-sizing: border-box;
        overflow-x: hidden;
        margin-bottom: 4rem;
    }

    .highlight-criteria {
        font-size: 14px;
        font-style: italic;
        color: #666;
        margin-top: -10px;
        margin-bottom: 20px;
        text-align: center;
    }

    .teacher-highlights-container {
        display: grid;
        grid-template-columns: repeat(5, 1fr);
        gap: 1.5rem;
        justify-items: center;
        padding: 0 1rem;
        max-width: 1300px;
        margin: 0 auto;
        box-sizing: border-box;
    }

    .teacher-slide-card {
        position: relative;
        width: 100%;
        max-width: 240px;
        height: 350px;
        overflow: hidden;
        border-radius: 20px;
        box-shadow: 0 4px 16px rgba(0,0,0,0.1);
        transition: transform 0.3s;
        cursor: pointer;
        background-color: white;
    }

    .teacher-image-wrapper {
        width: 100%;
        height: 100%;
        position: relative;
        z-index: 1;
    }

    .teacher-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
        border-radius: 20px;
        transition: transform 0.4s ease;
    }

    .teacher-info-overlay {
        position: absolute;
        top: 0;
        right: -100%;
        width: 100%;
        height: 100%;
        background-color: #fff;
        padding: 20px;
        z-index: 2;
        transition: right 0.4s ease;
        overflow-y: auto;
        border-radius: 20px;
    }

    .teacher-slide-card.active .teacher-img {
        transform: translateX(-100%);
    }
    .teacher-slide-card.active .teacher-info-overlay {
        right: 0;
    }

    .teacher-info-overlay h3 {
        font-size: 20px;
        margin-bottom: 12px;
        font-weight: bold;
        color: var(--primary-color);
        text-align: center;
    }

    .teacher-info-overlay ul {
        list-style: none;
        padding: 0;
        font-size: 14px;
        color: var(--text-color);
        margin-bottom: 12px;
    }
    .teacher-info-overlay li {
        margin-bottom: 6px;
    }

    .view-more {
        background-color: var(--accent-color);
        color: white;
        padding: 6px 14px;
        border-radius: 20px;
        text-decoration: none;
        font-weight: bold;
        display: block;
        text-align: center;
        margin-top: 1rem;
    }

    /* Filter and search section */
    .filter-section {
        background: #ffffff;
        padding: 1.5rem 2rem;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
        margin-bottom: 2.5rem;
        display: flex;
        flex-wrap: wrap;
        gap: 1.25rem;
        align-items: center;
        justify-content: center;
    }
    .filter-section select,
    .search-bar input[type="text"] {
        padding: 0.6rem 1.2rem;
        border: 1px solid var(--border-color);
        border-radius: 8px;
        font-size: 15px;
        color: #555;
        min-width: 150px;
    }

    .search-bar {
        display: flex;
        gap: 0.5rem;
    }
    .search-bar input[type="text"] {
        flex-grow: 1;
    }
    .search-bar button {
        padding: 0.6rem 1.2rem;
        background-color: var(--primary-color);
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-size: 15px;
        transition: background-color 0.2s ease;
    }
    .search-bar button:hover {
        background-color: #5e35b1;
    }

    /* Teacher grid section */
    .teacher-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 2rem;
    }

    .teacher-card {
        background: white;
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 8px 24px rgba(0,0,0,0.08);
        display: flex;
        flex-direction: column;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
        padding: 1rem;
        box-sizing: border-box;
    }
    .teacher-card:hover {
        transform: translateY(-6px);
        box-shadow: 0 12px 32px rgba(0,0,0,0.1);
    }

    .card-img {
        position: relative;
        height: 180px;
        background-color: #e9ecef;
        border-radius: 12px;
        overflow: hidden;
        padding: 0;       /* ⬅ đảm bảo không có padding */
    }

    .card-img img {
        width: 100%;      /* ⬅ chiếm toàn bộ chiều ngang */
        height: 100%;     /* ⬅ chiếm toàn bộ chiều cao */
        object-fit: cover;/* ⬅ giúp ảnh cắt vừa khít khung */
        display: block;
    }

    .teacher-name-overlay {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        background: rgba(103, 58, 183, 0.85); /* màu primary với độ trong suốt */
        color: #fff;
        padding: 8px 12px;
        font-weight: bold;
        font-size: 1rem;
        text-align: center;
        box-sizing: border-box;
    }

    .card-body {
        display: flex;
        text-align: left;
        flex-direction: column;
        gap: 0.5rem;
        margin-top: 0.75rem;
    }

    .card-title {
        font-size: 1.2rem;
        font-weight: 700;
        color: var(--primary-color);
        margin: 0;
        text-align: left;
    }

    .teacher-position {
        font-size: 0.95rem;
        font-weight: bold;
        color: #444;
        text-align: left;
    }

    .teacher-desc {
        font-size: 0.9rem;
        color: #555;
        line-height: 1.4;
        text-align: justify;
    }

    .read-more-link {
        margin-top: auto;
        text-decoration: none;
        color: var(--primary-color);
        font-weight: 600;
        position: relative;
        padding-bottom: 2px;
        display: inline-block;
        transition: color 0.3s ease;
        align-self: flex-end;
    }


    .read-more-link span {
        position: relative;
        padding-bottom: 2px;
        display: inline-block;
    }

    .read-more-link span::after {
        content: '';
        position: absolute;
        left: 0;
        bottom: 0;
        height: 2px;
        width: 100%;
        background-color: var(--primary-color);
        transform: scaleX(0);
        transform-origin: right;
        transition: transform 0.3s ease;
    }

    .read-more-link:hover span::after {
        transform: scaleX(1);
    }
    /* Pagination */
    .pagination {
        margin-top: 3rem;
        display: flex;
        justify-content: center;
        gap: 0.75rem;
        flex-wrap: wrap;
    }
    .pagination button {
        padding: 0.7rem 1.2rem;
        border: 1px solid var(--primary-color);
        background-color: white;
        color: var(--primary-color);
        border-radius: 8px;
        cursor: pointer;
        font-size: 1rem;
        font-weight: 600;
        min-width: 45px;
    }
    .pagination button.active {
        background-color: var(--primary-color);
        color: white;
    }
    .pagination button:hover:not(.active) {
        background-color: #ede7f6;
        color: #512da8;
    }
</style>



<main class="teacher-page">

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger" style="padding: 10px; background-color: #ffe5e5; color: #d8000c; border-radius: 4px; margin: 1rem;">
            ${sessionScope.errorMessage}
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <!-- 🔥 GIÁO VIÊN NỔI BẬT -->
    <section class="highlight-section">
        <h2 class="section-title">Giáo viên nổi bật</h2>
        <p class="highlight-criteria">* Dựa trên số lượng khóa học, thành tích đạt được và đánh giá từ học sinh *</p>
        <div class="teacher-highlights-container">
            <c:forEach var="pair" items="${sessionScope.topTeachers}">
                <c:set var="acc" value="${pair[0]}" />
                <c:set var="teacher" value="${pair[1]}" />

                <!-- Xử lý đường dẫn ảnh -->
                <c:choose>
                    <c:when test="${fn:startsWith(acc.avatarURL, 'http')}">
                        <c:set var="avatarSrc" value="${acc.avatarURL}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="avatarSrc" value='${pageContext.request.contextPath}/assets/avatars/${acc.avatarURL}' />
                    </c:otherwise>
                </c:choose>

                <div class="teacher-slide-card" onclick="toggleSlideInfo(this)">
                    <div class="teacher-image-wrapper">
                        <img src="${avatarSrc}" alt="${acc.name}" class="teacher-img">
                    </div>
                    <div class="teacher-info-overlay">
                        <h3>${acc.name}</h3>
                        <ul>
                            <li><strong>Nơi công tác:</strong>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.schoolMap[teacher.schoolId]}">
                                        ${sessionScope.schoolMap[teacher.schoolId].name}
                                    </c:when>
                                    <c:otherwise>Không rõ</c:otherwise>
                                </c:choose>
                            </li>

                            <c:if test="${not empty achivementList}">
                                <li><strong>Thành tích nổi bật:</strong> ${achivementList[0].achivementName}</li>
                                </c:if>

                            <li><strong>Mô tả:</strong>
                                <c:choose>
                                    <c:when test="${not empty teacher.bio}">
                                        <c:choose>
                                            <c:when test="${fn:length(teacher.bio) > 80}">
                                                ${fn:substring(teacher.bio, 0, 80)}...
                                            </c:when>
                                            <c:otherwise>
                                                ${teacher.bio}
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>Chưa có mô tả.</c:otherwise>
                                </c:choose>
                            </li>
                        </ul>
                        <a href="thong-tin-giao-vien?teacherId=${teacher.id}" class="view-more">Xem chi tiết</a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

    <!-- 🧭 BỘ LỌC GIÁO VIÊN -->
    <section class="filter-section container">     
        <!-- Lọc theo môn học -->
        <form action="danh-sach-giao-vien" method="POST">
            <select name="selectedSubject" onchange="this.form.submit()">
                <option value="Tất cả" <c:if test="${empty requestScope.selectedSubject || requestScope.selectedSubject eq 'Tất cả'}">selected</c:if>>Môn học</option>
                <c:forEach var="subject" items="${sessionScope.subjectList}">
                    <option value="${subject.name()}"
                            <c:if test="${requestScope.selectedSubject eq subject.name()}">selected</c:if>>
                        ${subject.displayName}
                    </option>
                </c:forEach>
            </select>
            <input type="hidden" name="selectedGrade" value="${requestScope.selectedGrade}" />
            <input type="hidden" name="keyword" value="${requestScope.selectedKeyword}" />
        </form>

        <!-- Lọc theo khối -->
        <form action="danh-sach-giao-vien" method="POST">
            <select name="selectedGrade" onchange="this.form.submit()">
                <option value="Tất cả" <c:if test="${empty requestScope.selectedGrade || requestScope.selectedGrade eq 'Tất cả'}">selected</c:if>>Khối</option>
                <c:forEach var="i" begin="6" end="12">
                    <c:set var="gradeLabel" value="Lớp ${i}" />
                    <option value="${gradeLabel}"
                            <c:if test="${requestScope.selectedGrade eq gradeLabel}">selected</c:if>>
                        ${gradeLabel}
                    </option>
                </c:forEach>
            </select>
            <input type="hidden" name="selectedSubject" value="${requestScope.selectedSubject}" />
            <input type="hidden" name="keyword" value="${requestScope.selectedKeyword}" />
        </form>

        <form action="danh-sach-giao-vien" method="POST" class="search-bar">
            <input type="text" name="keyword" placeholder="Tìm kiếm giáo viên..." value="${requestScope.selectedKeyword}" required />
            <input type="hidden" name="selectedSubject" value="${requestScope.selectedSubject}" />
            <input type="hidden" name="selectedGrade" value="${requestScope.selectedGrade}" />
            <button type="submit"><i class="fas fa-search"></i></button>
        </form>
    </section>

    <!-- 📋 DANH SÁCH GIÁO VIÊN -->
    <section class="list-section container">
        <c:if test="${not empty requestScope.errorMessage}">
            <div class="alert alert-danger" style="padding: 10px; background-color: #ffe5e5; color: #d8000c; border-radius: 4px; margin-bottom: 1rem;">
                ${requestScope.errorMessage}
            </div>
        </c:if>

        <h2 class="section-title">
            <c:out value="${teacherPageTitle}" default="Danh sách giáo viên"/>
        </h2>

        <div class="teacher-grid" id="teacherGrid">
            <c:choose>
                <c:when test="${empty teacherList}">
                    <div class="no-results-message">
                        <p>Không tìm thấy giáo viên phù hợp với tiêu chí.</p>
                        <a href="danh-sach-giao-vien" class="btn btn-primary">Xóa bộ lọc</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pair" items="${teacherList}">
                        <c:set var="acc" value="${pair[0]}" />
                        <c:set var="teacher" value="${pair[1]}" />

                        <!-- Xử lý đường dẫn ảnh -->
                        <c:choose>
                            <c:when test="${fn:startsWith(acc.avatarURL, 'http')}">
                                <c:set var="avatarSrc" value="${acc.avatarURL}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="avatarSrc" value="${pageContext.request.contextPath}/assets/avatars/${acc.avatarURL}" />
                            </c:otherwise>
                        </c:choose>

                        <div class="teacher-card">
                            <div class="card-img">
                                <img src="${avatarSrc}" alt="${acc.name}">
                            </div>
                            <div class="card-body">
                                <h3 class="card-title">${acc.name}</h3> <!-- Di chuyển tên giáo viên xuống đây -->
                                <p class="teacher-position">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.schoolMap[teacher.schoolId]}">
                                            ${sessionScope.schoolMap[teacher.schoolId].name}
                                        </c:when>
                                        <c:otherwise>Không rõ nơi công tác</c:otherwise>
                                    </c:choose>
                                </p>
                                <p class="teacher-desc">
                                    <c:choose>
                                        <c:when test="${not empty teacher.bio}">
                                            <c:choose>
                                                <c:when test="${fn:length(teacher.bio) > 100}">
                                                    ${fn:substring(teacher.bio, 0, 100)}...
                                                </c:when>
                                                <c:otherwise>${teacher.bio}</c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>Chưa có mô tả.</c:otherwise>
                                    </c:choose>
                                </p>
                                <a href="thong-tin-giao-vien?teacherId=${teacher.id}" class="read-more-link">
                                    <span>Chi tiết</span>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="pagination" id="pagination"></div>
    </section>

</main>

<jsp:include page="layout/footer.jsp" />

<script>
    // JS phân trang danh sách giáo viên
    document.addEventListener('DOMContentLoaded', function () {
        const cards = Array.from(document.querySelectorAll(".teacher-card"));
        const itemsPerPage = 12;
        const totalPages = Math.ceil(cards.length / itemsPerPage);
        const paginationContainer = document.getElementById("pagination");

        function showPage(page) {
            cards.forEach(card => card.style.display = "none");
            const start = (page - 1) * itemsPerPage;
            const end = page * itemsPerPage;
            cards.slice(start, end).forEach(card => card.style.display = "flex");

            paginationContainer.innerHTML = "";
            for (let i = 1; i <= totalPages; i++) {
                const btn = document.createElement("button");
                btn.textContent = i;
                btn.classList.add(i === page ? "active" : "");
                btn.onclick = () => showPage(i);
                paginationContainer.appendChild(btn);
            }
        }

        if (cards.length > 0)
            showPage(1);
    });

    function toggleSlideInfo(card) {
        // Đóng các card khác
        document.querySelectorAll('.teacher-slide-card').forEach(c => {
            if (c !== card)
                c.classList.remove('active');
        });

        // Mở hoặc đóng chính nó
        card.classList.toggle('active');
    }
</script>

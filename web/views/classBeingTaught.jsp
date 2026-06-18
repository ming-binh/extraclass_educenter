<%-- 
    Document   : classBeingTaught
    Created on : Jun 20, 2025, 11:35:41 PM
    Author     : vankh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="layout/header.jsp" />

<style>
    .container-wrapper {
        background-color: #f8fafc;
        padding: 30px 0;
        min-height: 100vh;
    }

    .grid-container {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 20px;
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
    }

    .info-card {
        background: white;
        border-radius: 10px;
        padding: 15px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        transition: all 0.2s ease;
        border: 1px solid #e2e8f0;
        height: 100%;
    }

    .info-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        border-color: #cbd5e0;
    }

    .info-item {
        margin-bottom: 8px;
        display: flex;
    }

    .info-label {
        font-weight: 600;
        color: #4a5568;
        min-width: 100px;
        font-size: 13px;
    }

    .info-value {
        color: #2d3748;
        font-size: 13px;
    }

    button[type="submit"] {
        all: unset;
        cursor: pointer;
        display: block;
        width: 100%;
        height: 100%;
    }

    .wc-title h4 {
        font-size: 22px;
        font-weight: 600;
        margin-bottom: 15px;
        color: #2d3748;
        text-align: center;
    }

    .no-class-msg {
        text-align: center;
        color: #e53e3e;
        font-size: 14px;
        margin: 20px 0;
        grid-column: 1/-1;
    }

    .filter-container {
        max-width: 800px;
        margin: 30px auto;
        padding: 0 20px;
    }

    .filter-form {
        background: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        border: 1px solid #e2e8f0;
    }

    .filter-row {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        margin-bottom: 10px;
    }

    .search-wrapper {
        position: relative;
        width: 100%;
    }

    .search-input {
        width: 100%;
        padding: 10px 15px;
        font-size: 14px;
        border: 1px solid #e2e8f0;
        border-radius: 8px;
        outline: none;
        transition: 0.2s;
    }

    .search-input:focus {
        border-color: #4299e1;
        box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.1);
    }

    .suggestion-list {
        position: absolute;
        top: 100%;
        left: 0;
        width: 100%;
        z-index: 10;
        background: white;
        border: 1px solid #e2e8f0;
        border-top: none;
        border-radius: 0 0 8px 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
        max-height: 200px;
        overflow-y: auto;
        display: none;
        animation: fadeIn 0.15s ease-out;
    }

    .suggestion-list li {
        padding: 8px 15px;
        cursor: pointer;
        font-size: 13px;
        transition: 0.15s;
        color: #4a5568;
    }

    .suggestion-list li:hover {
        background-color: #f7fafc;
        color: #2b6cb0;
    }

    select {
        flex: 1;
        min-width: 120px;
        padding: 10px 12px;
        font-size: 13px;
        border: 1px solid #e2e8f0;
        border-radius: 8px;
        background: white;
        transition: 0.2s;
        cursor: pointer;
    }

    select:focus {
        border-color: #4299e1;
        outline: none;
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(-5px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    @media (max-width: 768px) {
        .grid-container {
            grid-template-columns: 1fr;
        }

        .filter-row {
            flex-direction: column;
        }

        select {
            width: 100%;
        }
    }
    .action-buttons {
        display: flex;
        justify-content: space-between;
        gap: 10px;
        margin-bottom: 12px;
    }

    .action-buttons form {
        flex: 1;
    }

    .action-button {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 8px 10px;
        border: none;
        border-radius: 8px;
        font-size: 13px;
        font-weight: 600;
        color: #fff;
        background-color: #2b6cb0;
        cursor: pointer;
        transition: background-color 0.2s ease, transform 0.1s ease;
        width: 100%;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
        gap: 6px;
        text-align: center;
    }

    .action-button:hover {
        background-color: #2c5282;
        transform: translateY(-1px);
    }

    .action-button i {
        font-size: 15px;
    }

    /* Màu riêng cho từng nút */
    .btn-score {
        background-color: #4299e1;
    }
    .btn-score:hover {
        background-color: #3182ce;
    }

    .btn-homework {
        background-color: #4299e1;
    }
    .btn-homework:hover {
        background-color: #4299e1;
    }

    .btn-submissions {
        background-color: #4299e1;
    }
    .btn-submissions:hover {
        background-color: #4299e1;
    }
</style>

<div class="filter-container">
    <div class="filter-form">
        <div class="filter-row">
            <div class="search-wrapper">
                <input class="search-input" id="searchStudent" placeholder="Tìm kiếm khóa học..." type="text" autocomplete="off">
                <ul id="studentSuggestions" class="suggestion-list"></ul>
            </div>
        </div>

        <div class="filter-row">
            <select name="subject" id="subjectFilter">
                <option value="">Tất cả môn học</option>
                <option value="Mathematics">Toán</option>
                <option value="Literature">Ngữ văn</option>
                <option value="English">Tiếng Anh</option>
                <option value="Physics">Vật lý</option>
                <option value="Chemistry">Hóa học</option>
                <option value="Biology">Sinh học</option>
                <option value="History">Lịch sử</option>
                <option value="Geography">Địa lý</option>
                <option value="Civic Education">GDCD</option>
                <option value="Informatics">Tin học</option>
            </select>

            <select name="grade" id="gradeFilter">
                <option value="">Tất cả lớp</option>
                <option value="1">Lớp 1</option>
                <option value="2">Lớp 2</option>
                <option value="3">Lớp 3</option>
                <option value="4">Lớp 4</option>
                <option value="5">Lớp 5</option>
                <option value="6">Lớp 6</option>
                <option value="7">Lớp 7</option>
                <option value="8">Lớp 8</option>
                <option value="9">Lớp 9</option>
                <option value="10">Lớp 10</option>
                <option value="11">Lớp 11</option>
                <option value="12">Lớp 12</option>
            </select>

            <select name="level" id="levelFilter">
                <option value="">Tất cả cấp độ</option>
                <option value="Foundation">Nhập môn</option>
                <option value="Basic">Cơ bản</option>
                <option value="Advanced">Nâng cao</option>
                <option value="Excellent">Xuất sắc</option>
                <option value="Topics_Exam">Luyện thi</option>
            </select>
        </div>
    </div>
</div>

<div class="container-wrapper">
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <div class="widget-box">
                    <div class="wc-title">
                        <h4>Khóa học đang dạy</h4>
                    </div>
                    <div class="widget-inner">
                        <div class="grid-container">
                            <c:if test="${empty listClass}">
                                <div class="no-class-msg">Không có lớp nào đang dạy.</div>
                            </c:if>

                            <c:forEach var="c" items="${listClass}">
                                <div class="info-card">
                                    <div class="action-buttons">
                                        <form action="createScore" method="get">
                                            <input type="hidden" name="courseId" value="${c.getCourseId()}">
                                            <input type="hidden" name="grade" value="${c.grade}">
                                            <input type="hidden" name="name" value="${c.name}">
                                            <input type="hidden" name="level" value="${c.level}">
                                            <input type="hidden" name="subject" value="${c.subject}">
                                            <input type="hidden" name="studentEnrollment" value="${c.studentEnrollment}">
                                            <button class="action-button btn-score" type="submit">
                                                <i class="fas fa-pen-alt"></i> Chấm điểm
                                            </button>
                                        </form>
                                        <form action="uploadAssignmentServlet" method="get">
                                            <input type="hidden" name="courseId" value="${c.getCourseId()}">
                                            <button class="action-button btn-homework" type="submit">
                                                <i class="fas fa-book"></i> Bài tập
                                            </button>
                                        </form>
                                        <form action="viewSubmissionsServlet" method="get">
                                            <input type="hidden" name="courseId" value="${c.getCourseId()}">
                                            <button class="action-button btn-submissions" type="submit">
                                                <i class="fas fa-eye"></i> Đã nộp
                                            </button>
                                        </form>
                                    </div>
                                    <div class="info-item"><span class="info-label">Lớp:</span><span class="info-value">${c.grade}</span></div>
                                    <div class="info-item"><span class="info-label">Tên khóa học:</span><span class="info-value">${c.name}</span></div>
                                    <div class="info-item">
                                        <span class="info-label">Cấp độ:</span>
                                        <span class="info-value">
                                            <c:choose>
                                                <c:when test="${c.level == 'Foundation'}">Nhập môn</c:when>
                                                <c:when test="${c.level == 'Excellent'}">Xuất sắc</c:when>
                                                <c:when test="${c.level == 'Advanced'}">Nâng cao</c:when>
                                                <c:when test="${c.level == 'Basic'}">Cơ bản</c:when>
                                                <c:when test="${c.level == 'Topics_Exam'}">Luyện thi</c:when>
                                                <c:otherwise>${c.level}</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Môn:</span>
                                        <span class="info-value">
                                            <c:choose>
                                                <c:when test="${c.subject == 'Mathematics'}">Toán</c:when>
                                                <c:when test="${c.subject == 'Literature'}">Ngữ văn</c:when>
                                                <c:when test="${c.subject == 'English'}">Tiếng Anh</c:when>
                                                <c:when test="${c.subject == 'Physics'}">Vật lý</c:when>
                                                <c:when test="${c.subject == 'Chemistry'}">Hóa học</c:when>
                                                <c:when test="${c.subject == 'Biology'}">Sinh học</c:when>
                                                <c:when test="${c.subject == 'History'}">Lịch sử</c:when>
                                                <c:when test="${c.subject == 'Geography'}">Địa lý</c:when>
                                                <c:when test="${c.subject == 'Civic Education'}">Giáo dục công dân</c:when>
                                                <c:when test="${c.subject == 'Informatics'}">Tin học</c:when>
                                                <c:otherwise>${c.subject}</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="info-item"><span class="info-label">Số học sinh:</span><span class="info-value">${c.studentEnrollment}</span></div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        // Get all filter elements
        const searchInput = document.getElementById('searchStudent');
        const subjectFilter = document.getElementById('subjectFilter');
        const gradeFilter = document.getElementById('gradeFilter');
        const levelFilter = document.getElementById('levelFilter');
        const suggestionBox = document.getElementById('studentSuggestions');
        const classCards = document.querySelectorAll('.class-card');

        // Function to filter classes based on all criteria
        function filterClasses() {
            const searchTerm = searchInput.value.toLowerCase().trim();
            const selectedSubject = subjectFilter.value;
            const selectedGrade = gradeFilter.value;
            const selectedLevel = levelFilter.value;

            let hasVisibleCards = false;

            classCards.forEach(card => {
                const cardSubject = card.dataset.subject;
                const cardGrade = card.dataset.grade;
                const cardLevel = card.dataset.level;
                const cardName = card.dataset.name;

                // Check if card matches all selected filters
                const matchesSubject = !selectedSubject || cardSubject === selectedSubject;
                const matchesGrade = !selectedGrade || cardGrade === selectedGrade;
                const matchesLevel = !selectedLevel || cardLevel === selectedLevel;
                const matchesSearch = !searchTerm || cardName.includes(searchTerm);

                if (matchesSubject && matchesGrade && matchesLevel && matchesSearch) {
                    card.style.display = '';
                    hasVisibleCards = true;
                } else {
                    card.style.display = 'none';
                }
            });

            // Show "no results" message if no cards are visible
            const noClassMsg = document.querySelector('.no-class-msg');
            if (noClassMsg) {
                noClassMsg.style.display = hasVisibleCards || listClass.length === 0 ? 'none' : 'block';
            }
        }

        // Search input with suggestions
        searchInput.addEventListener('input', function () {
            const keyword = this.value.toLowerCase().trim();
            suggestionBox.innerHTML = "";

            if (keyword === "") {
                suggestionBox.style.display = "none";
                filterClasses();
                return;
            }

            const matches = [];

            classCards.forEach(card => {
                const nameEl = card.querySelector(".info-item:nth-child(2) .info-value");
                const courseName = nameEl?.textContent.toLowerCase();
                const isMatch = courseName && courseName.includes(keyword);

                if (isMatch) {
                    matches.push({
                        element: card,
                        name: nameEl.textContent
                    });
                }
            });

            // Show suggestions
            if (matches.length > 0) {
                matches.slice(0, 5).forEach(match => {
                    const li = document.createElement("li");
                    li.textContent = match.name;
                    li.addEventListener("click", function () {
                        searchInput.value = match.name;
                        suggestionBox.style.display = "none";
                        filterClasses();
                        match.element.scrollIntoView({behavior: "smooth", block: "center"});
                    });
                    suggestionBox.appendChild(li);
                });
                suggestionBox.style.display = "block";
            } else {
                suggestionBox.style.display = "none";
            }

            filterClasses();
        });

        // Add event listeners to all filter dropdowns
        subjectFilter.addEventListener('change', filterClasses);
        gradeFilter.addEventListener('change', filterClasses);
        levelFilter.addEventListener('change', filterClasses);

        // Hide dropdown when clicking outside
        document.addEventListener('click', function (e) {
            if (!suggestionBox.contains(e.target) && e.target !== searchInput) {
                suggestionBox.style.display = 'none';
            }
        });

        // Initialize filters from URL parameters if present
        function initFiltersFromUrl() {
            const urlParams = new URLSearchParams(window.location.search);

            if (urlParams.has('subject')) {
                subjectFilter.value = urlParams.get('subject');
            }
            if (urlParams.has('grade')) {
                gradeFilter.value = urlParams.get('grade');
            }
            if (urlParams.has('level')) {
                levelFilter.value = urlParams.get('level');
            }

            filterClasses();
        }

        initFiltersFromUrl();
    });
</script>
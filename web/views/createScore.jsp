<%-- 
    Document   : createScore
    Created on : Jun 20, 2025, 9:06:50 PM
    Author     : vankh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<jsp:include page="layout/header.jsp" />
<style>
    .success-message {
        color: #28a745;
        display: flex;
        align-items: center;
        gap: 5px;
        font-weight: 500;
        animation: fadeIn 0.5s;
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
    .shake-animation {
        animation: shake 0.5s;
    }

    @keyframes shake {
        0%, 100% {
            transform: translateX(0);
        }
        10%, 30%, 50%, 70%, 90% {
            transform: translateX(-5px);
        }
        20%, 40%, 60%, 80% {
            transform: translateX(5px);
        }
    }
    .page-container {
        padding: 20px;
        max-width: 1200px;
        margin: 0 auto;
    }

    /* Compact Course Info Section */
    .course-info-card {
        background-color: white;
        border-radius: 10px;
        padding: 16px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        margin-bottom: 20px;
    }

    .course-info-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 12px;
    }

    .course-info-item {
        display: flex;
        align-items: center;
    }

    .course-info-label {
        font-weight: 600;
        color: #555;
        min-width: 80px;
        font-size: 13px;
        margin-right: 8px;
    }

    .course-info-value {
        color: #222;
        font-size: 13px;
        flex-grow: 1;
    }

    /* Search Section */
    .search-container {
        margin: 20px 0;
    }

    .search-form {
        --timing: 0.3s;
        --width-of-input: 300px;
        --height-of-input: 42px;
        --border-height: 2px;
        --input-bg: #f8f9fa;
        --border-color: #4285f4;
        --border-radius: 8px;
        position: relative;
        width: 100%;
        max-width: 400px;
        height: var(--height-of-input);
        display: flex;
        align-items: center;
        padding-inline: 15px;
        border-radius: var(--border-radius);
        background: var(--input-bg);
        border: 1px solid #ddd;
        transition: all 0.3s ease;
    }

    .search-form:focus-within {
        border-color: var(--border-color);
        box-shadow: 0 0 0 2px rgba(66, 133, 244, 0.2);
    }

    .search-input {
        font-size: 14px;
        background-color: transparent;
        width: 100%;
        height: 100%;
        padding-inline: 10px;
        border: none;
        outline: none;
    }

    .search-button, .reset-button {
        border: none;
        background: none;
        color: #6c757d;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .reset-button {
        opacity: 0;
        visibility: hidden;
        transition: opacity 0.2s;
    }

    .search-input:not(:placeholder-shown) ~ .reset-button {
        opacity: 1;
        visibility: visible;
    }

    /* Score Input Section */
    .score-header {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        align-items: center;
        margin: 20px 0;
    }

    .score-type-input, .session-date-select {
        flex: 1;
        min-width: 200px;
    }

    .score-type-input input, .session-date-select select {
        width: 100%;
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-radius: 6px;
        font-size: 14px;
    }

    .session-date-select select {
        cursor: pointer;
    }

    .table-container {
        background: white;
        border-radius: 10px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        padding: 16px;
        overflow-x: auto;
    }

    .table {
        width: 100%;
        border-collapse: collapse;
        font-size: 13px;
    }

    .table th {
        background-color: #f8f9fa;
        font-weight: 600;
        color: #495057;
        padding: 10px 12px;
        text-align: center;
        border-bottom: 2px solid #e9ecef;
    }

    .table td {
        padding: 10px 12px;
        text-align: center;
        border-bottom: 1px solid #e9ecef;
        vertical-align: middle;
    }

    .table tr:hover {
        background-color: #f8f9fa;
    }

    .score-input {
        width: 70px;
        padding: 6px 8px;
        border: 1px solid #ddd;
        border-radius: 6px;
        text-align: center;
        font-size: 13px;
        transition: all 0.2s;
    }

    .score-input:focus {
        border-color: #4285f4;
        box-shadow: 0 0 0 2px rgba(66, 133, 244, 0.2);
        outline: none;
    }

    .feedback-input {
        width: 100%;
        padding: 6px 8px;
        border: 1px solid #ddd;
        border-radius: 6px;
        font-size: 13px;
        resize: vertical;
        min-height: 36px;
    }

    .submit-btn {
        background-color: #4285f4;
        color: white;
        border: none;
        padding: 8px 20px;
        border-radius: 6px;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.2s;
        margin-top: 20px;
        float: right;
    }

    .submit-btn:hover {
        background-color: #3367d6;
    }

    .highlight {
        background-color: #fff8e1 !important;
    }

    @media (max-width: 768px) {
        .course-info-grid {
            grid-template-columns: 1fr;
        }

        .score-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 12px;
        }

        .score-type-input, .session-date-select {
            width: 100%;
        }
    }
    .header-section {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        flex-wrap: wrap;
        gap: 15px;
    }

    .info-card {
        background-color: white;
        border-radius: 8px;
        padding: 16px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        width: 100%;
    }

    .info-container {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .info-row {
        display: flex;
        width: 100%;
    }

    .info-multi-items {
        gap: 15px;
    }

    .info-item {
        flex: 1;
        display: flex;
        align-items: center;
        min-height: 24px;
    }

    .info-label {
        font-weight: 600;
        color: #555;
        width: 90px;
        flex-shrink: 0;
        font-size: 13px;
    }

    .info-value {
        color: #222;
        font-size: 13px;
        flex-grow: 1;
    }

    .course-name {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    @media (max-width: 768px) {
        .info-multi-items {
            flex-direction: column;
            gap: 10px;
        }

        .info-item {
            width: 100%;
        }

        .info-label {
            width: 80px;
        }

        .course-name {
            white-space: normal;
        }
    }
    .back-btn {
        display: inline-block;
        padding: 6px 14px;
        background-color: #6c757d;
        color: #fff;
        text-decoration: none;
        border-radius: 6px;
        font-size: 13px;
        font-weight: 500;
        transition: background-color 0.2s ease;
        text-shadow: -1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000;
    }
    .back-btn:hover {
        background-color: #5a6268;
    }

</style>

<div class="page-container">
    <!-- Compact Course Information Section -->
    <div class="header-section">
        <div class="info-card">
            <div class="info-container">
                <!-- Dòng 1: 3 thông tin Lớp, Cấp độ, Môn -->
                <div class="info-row info-multi-items">
                    <div class="info-item">
                        <span class="info-label">Lớp:</span>
                        <span class="info-value">${grade}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Cấp độ:</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${level == 'Advanced'}">Nâng cao</c:when>
                                <c:when test="${level == 'Basic'}">Cơ bản</c:when>
                                <c:when test="${level == 'Topics_Exam'}">Luyện thi</c:when>
                                <c:otherwise>${level}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Môn:</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${subject == 'Mathematics'}">Toán</c:when>
                                <c:when test="${subject == 'Literature'}">Ngữ văn</c:when>
                                <c:when test="${subject == 'English'}">Tiếng Anh</c:when>
                                <c:when test="${subject == 'Physics'}">Vật lý</c:when>
                                <c:when test="${subject == 'Chemistry'}">Hóa học</c:when>
                                <c:when test="${subject == 'Biology'}">Sinh học</c:when>
                                <c:when test="${subject == 'History'}">Lịch sử</c:when>
                                <c:when test="${subject == 'Geography'}">Địa lý</c:when>
                                <c:when test="${subject == 'Civic Education'}">GDCD</c:when>
                                <c:when test="${subject == 'Informatics'}">Tin học</c:when>
                                <c:otherwise>${subject}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>

                <!-- Dòng 2: Tên khóa học -->
                <div class="info-row">
                    <div class="info-item">
                        <span class="info-label">Tên khóa học:</span>
                        <span class="info-value course-name">${nameCourse}</span>
                    </div>
                </div>

                <!-- Dòng 3: Số học sinh -->
                <div class="info-row">
                    <div class="info-item">
                        <span class="info-label">Số học sinh:</span>
                        <span class="info-value">${currentEnrollment}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Search Section -->
    <div class="search-container">
        <form class="search-form">
            <button type="button" class="search-button">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M11 19C15.4183 19 19 15.4183 19 11C19 6.58172 15.4183 3 11 3C6.58172 3 3 6.58172 3 11C3 15.4183 6.58172 19 11 19Z" stroke="#6c757d" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M21 21L16.65 16.65" stroke="#6c757d" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
            <input class="search-input" id="searchStudent" placeholder="Tìm kiếm học sinh..." type="text">
            <button type="reset" class="reset-button">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M18 6L6 18" stroke="#6c757d" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M6 6L18 18" stroke="#6c757d" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        </form>
    </div>

    <!-- Score Input Section -->
    <div class="table-container">
        <form action="createScore" method="post" onsubmit="return validateScores(event)">
            <input type="hidden" name="courseId" value="${courseId}" />
            <input type="hidden" name="grade" value="${grade}" />
            <input type="hidden" name="subject" value="${subject}" />
            <input type="hidden" name="nameCourse" value="${nameCourse}" />
            <input type="hidden" name="currentEnrollment" value="${currentEnrollment}" />
            <input type="hidden" name="level" value="${level}" />

            <div class="score-header">
                <div class="score-type-input">
                    <label style="font-weight: 600; display: block; margin-bottom: 5px;">Loại điểm:</label>
                    <input type="text" name="typeScore" required>
                </div>

                <c:if test="${sessionStatus != 'inactive'}">
                    <div class="session-date-select">
                        <label style="font-weight: 600; display: block; margin-bottom: 5px;">Ngày học:</label>
                        <select name="sessionDate" required>
                            <option value="">-- Chọn ngày học --</option>
                            <c:forEach var="sessionItem" items="${formattedSessions}">
                                <option value="${sessionItem.section.dateTime.toString()}">
                                    ${sessionItem.formattedDate}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>
            </div>

            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ và tên</th>
                        <th>Điểm</th>
                        <th>Nhận xét</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="st" items="${listStudent}">
                        <tr>
                            <td>${st.getId()}</td>
                            <td>${st.getName()}</td>
                            <td>
                                <input type="hidden" name="studentIds" value="${st.getId()}" />
                                <input class="score-input" name="score_${st.getId()}" type="number" min="0" max="10" step="0.1" required>
                            </td>
                            <td>
                                <textarea class="feedback-input" name="feedback_${st.getId()}"></textarea>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div style="display: flex; align-items: center; justify-content: flex-end; gap: 10px; margin-top: 20px;">
                <c:if test="${not empty message}">
                    <div style="color: #28a745; display: flex; align-items: center; gap: 5px;">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M9 16.17L4.83 12L3.41 13.41L9 19L21 7L19.59 5.59L9 16.17Z" fill="#28a745"/>
                        </svg>
                        ${message}
                    </div>
                </c:if>
                <button type="submit" class="submit-btn">Lưu điểm</button>
            </div>
        </form>
        <a href="classBeingTaught" class="back-btn">← Quay lại</a>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        // Xử lý tìm kiếm học sinh
        const searchInput = document.getElementById("searchStudent");
        const rows = document.querySelectorAll("table tbody tr");

        if (searchInput && rows) {
            searchInput.addEventListener("input", function () {
                const keyword = this.value.toLowerCase().trim();
                rows.forEach(row => {
                    const idText = row.children[0].textContent.toLowerCase();
                    const nameText = row.children[1].textContent.toLowerCase();
                    const isMatch = idText.includes(keyword) || nameText.includes(keyword);

                    if (keyword === "" || isMatch) {
                        row.style.display = "";
                        row.classList.add("highlight");
                        setTimeout(() => row.classList.remove("highlight"), 1500);
                    } else {
                        row.style.display = "none";
                    }
                });
            });
        }

        // Validate form khi submit
        const scoreForm = document.querySelector("form[onsubmit='return validateScores(event)']");
        if (scoreForm) {
            scoreForm.addEventListener("submit", function (event) {
                if (!validateScores(event)) {
                    event.preventDefault();
                }
            });
        }
    });

    function validateScores(event) {
        let isValid = true;
        let firstInvalidElement = null;

        // 1. Validate loại điểm
        const scoreTypeInput = document.querySelector("input[name='typeScore']");
        if (scoreTypeInput && scoreTypeInput.value.trim() === "") {
            highlightError(scoreTypeInput);
            isValid = false;
            firstInvalidElement = firstInvalidElement || scoreTypeInput;
        } else if (scoreTypeInput) {
            removeHighlight(scoreTypeInput);
        }

        // 2. Validate ngày học (nếu không phải inactive)
        const sessionStatus = "${sessionStatus}";
        if (sessionStatus !== 'inactive') {
            const sessionDateSelect = document.querySelector("select[name='sessionDate']");
            if (sessionDateSelect && sessionDateSelect.value === "") {
                highlightError(sessionDateSelect);
                isValid = false;
                firstInvalidElement = firstInvalidElement || sessionDateSelect;
            } else if (sessionDateSelect) {
                removeHighlight(sessionDateSelect);
            }
        }

        // 3. Validate điểm số
        const scoreInputs = document.querySelectorAll('.score-input');
        scoreInputs.forEach(input => {
            const value = input.value.trim();
            if (value === "") {
                highlightError(input);
                isValid = false;
                firstInvalidElement = firstInvalidElement || input;
            } else {
                const num = parseFloat(value);
                if (isNaN(num) || num < 0 || num > 10) {
                    highlightError(input);
                    isValid = false;
                    firstInvalidElement = firstInvalidElement || input;
                } else {
                    removeHighlight(input);
                }
            }
        });

        if (!isValid) {
            event.preventDefault();
            if (firstInvalidElement) {
                firstInvalidElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center'
                });
                firstInvalidElement.focus();
            }
            alert("Vui lòng điền đầy đủ thông tin bắt buộc:\n" +
                    "- Loại điểm không được để trống\n" +
                    (sessionStatus !== 'inactive' ? "- Ngày học phải được chọn\n" : "") +
                    "- Tất cả điểm số phải từ 0 đến 10 và không để trống");
            return false;
        }

        return true;
    }

    function highlightError(element) {
        element.style.border = '1px solid #dc3545';
        element.style.backgroundColor = '#fff5f5';
        // Thêm hiệu ứng shake nếu muốn
        element.classList.add('shake-animation');
        setTimeout(() => {
            element.classList.remove('shake-animation');
        }, 500);
    }

    function removeHighlight(element) {
        element.style.border = '1px solid #ddd';
        element.style.backgroundColor = '';
    }
</script>

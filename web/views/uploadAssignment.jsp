<%-- 
    Document   : uploadAssignment
    Created on : Jul 11, 2025, 9:24:03 PM
    Author     : HanND
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="layout/header.jsp" />

<style>
    body {
        font-family: 'Segoe UI', sans-serif;
        background-color: #f3f4f6;
    }

    .assignment-wrapper {
        max-width: 1100px;
        margin: auto;
        padding: 40px 20px;
        display: flex;
        gap: 40px;
        justify-content: space-between;
        align-items: flex-start;
    }

    .assignment-box,
    .assignment-list {
        background-color: #ffffff;
        border-radius: 10px;
        padding: 25px 30px;
        box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
        flex: 1;
    }

    h2,
    h4 {
        text-align: center;
        color: #2d3748;
    }

    .form-control {
        width: 100%;
        padding: 10px;
        border-radius: 6px;
        border: 1px solid #cbd5e0;
        margin-top: 5px;
        font-size: 14px;
    }

    label {
        font-weight: 600;
        color: #4a5568;
        margin-bottom: 4px;
        display: block;
    }

    textarea.form-control {
        resize: vertical;
    }

    .btn-primary {
        background-color: #3b82f6;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 6px;
        font-size: 14px;
        cursor: pointer;
        transition: 0.3s;
    }

    .btn-primary:hover {
        background-color: #2563eb;
    }

    .assignment-item {
        border-bottom: 1px solid #e2e8f0;
        padding: 12px 0;
    }

    .assignment-item {
        border-bottom: 1px solid #e2e8f0;
        padding: 16px 0;
        position: relative;
        padding-right: 80px;
    }

    .assignment-item:last-child {
        border-bottom: none;
    }

    .assignment-title {
        font-weight: 600;
        font-size: 16px;
        color: #1a202c;
    }

    .assignment-description {
        color: #4a5568;
        font-size: 14px;
        margin-top: 6px;
        white-space: pre-line;
    }

    .assignment-link {
        display: inline-block;
        margin-top: 6px;
        font-size: 13px;
        color: #3182ce;
        text-decoration: none;
    }

    .assignment-link:hover {
        text-decoration: underline;
    }

    .action-buttons {
        position: absolute;
        top: 16px;
        right: 10px;
        display: flex;
        gap: 8px;
    }

    .icon-button {
        background: none;
        border: none;
        cursor: pointer;
        font-size: 16px;
        padding: 6px;
        color: #4a5568;
        border-radius: 6px;
        transition: background-color 0.2s ease, color 0.2s ease;
    }

    .icon-button:hover {
        background-color: #edf2f7;
    }

    .icon-button.delete {
        color: #e53e3e;
    }

    .icon-button.delete:hover {
        background-color: #fed7d7;
        color: #c53030;
    }

    .icon-button.edit {
        color: #4a5568;
    }

    .icon-button.edit:hover {
        background-color: #e2e8f0;
        color: #2d3748;
    }
    .btn-secondary {
        background-color: #4a5568;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 6px;
        font-size: 14px;
        cursor: pointer;
        transition: 0.3s;
    }

    .btn-secondary:hover {
        background-color: #2d3748;
    }

    .button-row {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        margin-top: 10px;
    }

</style>

<div class="assignment-wrapper">
    <!-- FORM GIAO BÀI  -->
    <div class="assignment-box">
        <h2>📘 Giao bài tập</h2>

        <form action="uploadAssignmentServlet" method="post" enctype="multipart/form-data">
            <div style="margin-bottom: 15px;">
                <p><strong>Khóa học:</strong> ${course.name}</p>
                <p><strong>Môn học:</strong> ${course.subject.displayName}</p>
                <p><strong>Lớp:</strong> ${course.grade}</p>
                <p><strong>Cấp độ:</strong> ${course.level.displayName}</p>
                <input type="hidden" name="courseId" value="${courseId}">
            </div>

            <div style="margin-bottom: 15px;">
                <label>Tiêu đề bài tập:</label>
                <input type="text" name="title" required class="form-control" />
            </div>

            <div style="margin-bottom: 15px;">
                <label>Mô tả:</label>
                <textarea name="description" required class="form-control" rows="4" placeholder="Viết hướng dẫn hoặc nội dung chi tiết..."></textarea>
            </div>

            <div style="margin-bottom: 15px;">
                <label>Tệp đính kèm:</label>
                <input type="file" name="fileUpload" required class="form-control" />
            </div>
            <div style="margin-bottom: 15px;">
                <label>Hạn nộp:</label>
                <input type="datetime-local" name="dueAt" class="form-control" required />
            </div>
            <div class="button-row">
                <a href="classBeingTaught" class="btn-secondary" style="text-decoration: none; line-height: 1.5;">← Quay lại</a>
                <button type="submit" class="btn-primary">Giao bài</button>
            </div>

        </form>
    </div>

    <!-- DANH SÁCH BÀI TẬP  -->
    <div class="assignment-list">
        <h4>📄 Bài tập đã giao</h4>
        <c:if test="${not empty assignments}">
            <c:forEach var="a" items="${assignments}">
                <div class="assignment-item">
                    <div class="assignment-title">${a.title}</div>
                    <div class="assignment-description">${a.description}</div>
                    <c:if test="${not empty a.filePath}">
                        <a href="uploadAssignmentServlet?action=view&filename=${a.filePath}" target="_blank" class="assignment-link">
                            📎 Xem tệp đính kèm
                        </a>
                    </c:if>
                    <div style="color: #718096; font-size: 13px; margin-top: 4px;">
                        <c:if test="${a.dueAt != null}">
                            <p class="assignment-due">
                                ⏳ Hạn nộp: <fmt:formatDate value="${a.dueAt}" pattern="dd/MM/yyyy HH:mm" />
                            </p>
                        </c:if>
                        <p class="assignment-uploaded">
                            🕒 Ngày giao: <fmt:formatDate value="${a.uploadedAt}" pattern="dd/MM/yyyy HH:mm" />
                        </p>
                    </div>
                    <div class="action-buttons">
                        <form action="uploadAssignmentServlet" method="post">
                            <input type="hidden" name="action" value="delete" />
                            <input type="hidden" name="assignmentId" value="${a.id}" />
                            <input type="hidden" name="courseId" value="${courseId}" />
                            <button type="submit" class="icon-button delete" title="Xoá" onclick="return confirm('Bạn có chắc muốn xoá bài tập này không?');">
                                🗑
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:if>
        <c:if test="${empty assignments}">
            <p style="text-align:center; color: gray;">Chưa có bài tập nào được giao cho lớp này.</p>
        </c:if>
    </div>
</div>
<jsp:include page="layout/footer.jsp" /> 

<%-- 
    Document   : studentAssignments
    Created on : Jul 17, 2025, 6:35:44 PM
    Author     : HanND
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="layout/header.jsp" />

<style>
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
    }

    .page-wrapper {
        display: flex;
        flex-direction: column;
        min-height: 60vh;
    }

    .content-wrapper {
        flex: 1;
    }
    body {
        font-family: 'Segoe UI', sans-serif;
        background-color: #f3f4f6;
        margin: 0;
        padding: 0;
    }

    .assignment-wrapper {
        max-width: 900px;
        margin: 50px auto;
        max-width: 1300px;
        padding: 20px;
    }

    .assignment-list {
        background-color: #ffffff;
        border-radius: 10px;
        padding: 24px 28px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.06);
    }

    h2 {
        text-align: center;
        color: #2d3748;
        margin-bottom: 20px;
        font-size: 26px;
    }

    .assignment-description {
        color: #4a5568;
        font-size: 15px;
        margin-bottom: 20px;
        white-space: pre-line;
        text-align: center;
    }

    .assignment-item {
        border-top: 1px solid #e2e8f0;
        padding: 14px 0;
    }

    .assignment-item:first-child {
        border-top: none;
    }

    .assignment-title {
        font-weight: 600;
        font-size: 16px;
        color: #1a202c;
    }

    .assignment-description-detail {
        color: #4a5568;
        font-size: 14px;
        margin-top: 6px;
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

    .assignment-meta {
        color: #718096;
        font-size: 13px;
        margin-top: 4px;
    }

    .btn-secondary {
        background-color: #4a5568;
        color: white;
        padding: 8px 16px;
        border: none;
        border-radius: 6px;
        font-size: 14px;
        cursor: pointer;
        text-decoration: none;
        display: inline-block;
        transition: 0.3s;
    }

    .btn-secondary:hover {
        background-color: #2d3748;
    }

    .back-container {
        margin-top: 30px;
        text-align: right;
    }

    .no-assignment {
        text-align: center;
        color: gray;
        font-style: italic;
        margin-top: 20px;
    }
</style>
<div class="page-wrapper">
    <div class="content-wrapper">
        <div class="assignment-wrapper" style="display: flex; gap: 20px;">
            <!-- Cột trái: Danh sách bài tập -->
            <div class="assignment-list" style="flex: 1;">
                <c:if test="${not empty success}">
                    <div id="successMessage" style="color: green; font-weight: bold; text-align: center; margin-bottom: 16px;">
                        ${success}
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div id="errorMessage" style="color: red; font-weight: bold; text-align: center; margin-bottom: 16px;">
                        ${error}
                    </div>
                </c:if>

                <h2>📘 Bài tập cho khóa học: <c:out value="${course.name}" /></h2>

                <c:if test="${not empty course.description}">
                    <div class="assignment-description">
                        <c:out value="${course.description}" />
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${empty assignments}">
                        <p class="no-assignment">Hiện chưa có bài tập nào cho khóa học này.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="a" items="${assignments}">
                            <div class="assignment-item">
                                <div class="assignment-title">📄 <c:out value="${a.title}" /></div>
                                <div class="assignment-description-detail">
                                    <c:choose>
                                        <c:when test="${not empty a.description}">
                                            <c:out value="${a.description}" />
                                        </c:when>
                                        <c:otherwise><em>Không có mô tả</em></c:otherwise>
                                    </c:choose>
                                </div>
                                <c:if test="${not empty a.filePath}">
                                    <a href="${pageContext.request.contextPath}/studentAssignmentServlet?action=download&assignmentId=${a.id}" class="assignment-link" target="_blank">
                                        📎 Tải xuống tệp đính kèm
                                    </a>
                                </c:if>
                                <div class="assignment-meta">
                                    <c:if test="${a.dueAt != null}">
                                        ⏳ Hạn nộp: <fmt:formatDate value="${a.dueAt}" pattern="dd/MM/yyyy HH:mm" /><br/>
                                    </c:if>
                                    🕒 Ngày đăng: <fmt:formatDate value="${a.uploadedAt}" pattern="dd/MM/yyyy HH:mm" />
                                </div>

                                <!-- Form nộp bài -->
                                <form class="submission-form"
                                      action="${pageContext.request.contextPath}/studentAssignmentServlet"
                                      method="post" enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="submit" />
                                    <input type="hidden" name="courseId" value="${courseId}" />
                                    <input type="hidden" name="sectionAssignmentId" value="${a.id}" />

                                    <label for="submissionFile_${a.id}">💾 Nộp bài làm:</label>
                                    <input type="file" id="submissionFile_${a.id}" name="submissionFile" required />
                                    <button type="submit" style="margin-top:8px;">Gửi bài</button>
                                </form>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>

                <div class="back-container">
                    <a href="${pageContext.request.contextPath}/studentAssignmentServlet" class="btn-secondary">← Quay lại</a>
                </div>
            </div>

            <!-- Cột phải: Lịch sử đã nộp -->
            <div class="assignment-list" style="flex: 1;">
                <h2>📝 Lịch sử đã nộp bài</h2>

                <c:choose>
                    <c:when test="${empty history}">
                        <p class="no-assignment">Bạn chưa nộp bài nào cho khóa học này.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="h" items="${history}">
                            <div class="assignment-item">
                                <div class="assignment-title">
                                    📘 Bài: <c:out value="${h.assignmentTitle}" /><br/>
                                    📌 Ngày nộp: <fmt:formatDate value="${h.submittedAt}" pattern="dd/MM/yyyy HH:mm" />
                                </div>
                                <div class="assignment-meta">
                                    <a href="${pageContext.request.contextPath}/submissions/${h.filePath}" class="assignment-link" target="_blank">
                                        📄 Xem bài đã nộp
                                    </a>
                                    <br/>
                                    🎯 Điểm: 
                                    <strong>
                                        <c:choose>
                                            <c:when test="${not empty h.grade}">
                                                ${h.grade}
                                            </c:when>
                                            <c:otherwise>Chưa chấm</c:otherwise>
                                        </c:choose>
                                    </strong>
                                    <br/>

                                    💬 Nhận xét: 
                                    <em>
                                        <c:choose>
                                            <c:when test="${not empty h.comment}">
                                                ${h.comment}
                                            </c:when>
                                            <c:otherwise>Chưa có nhận xét</c:otherwise>
                                        </c:choose>
                                    </em>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <script>
            setTimeout(function () {
                const successMsg = document.getElementById('successMessage');
                const errorMsg = document.getElementById('errorMessage');
                if (successMsg)
                    successMsg.style.display = 'none';
                if (errorMsg)
                    errorMsg.style.display = 'none';
            }, 5000);
        </script>
    </div>
    <jsp:include page="layout/footer.jsp" />
</div>

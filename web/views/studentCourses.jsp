<%-- 
    Document   : studentCourses
    Created on : Jul 17, 2025, 7:40:55 PM
    Author     : HanND
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/views/layout/header.jsp"/>

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
        background-color: #f8fafc;
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
    }

    .container {
        max-width: 1200px;
        margin: 150px auto; /* Tăng khoảng cách top từ 40px → 60px */
        padding: 0 20px;
    }

    .page-title {
        text-align: center;
        font-size: 24px;
        font-weight: 600;
        color: #2d3748;
        margin-bottom: 30px;
    }

    .grid-container {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); /* giảm min-width từ 280 → 220 */
        gap: 20px;
    }

    .course-card {
        background-color: #ffffff;
        border-radius: 10px;
        padding: 20px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        border: 1px solid #e2e8f0;
        transition: transform 0.2s;
    }

    .course-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
    }

    .course-title {
        font-size: 16px;
        font-weight: bold;
        color: #2b6cb0;
        margin-bottom: 10px;
    }

    .course-action {
        margin-top: 10px;
    }

    .action-link {
        background-color: #4299e1;
        color: white;
        padding: 6px 12px;
        border-radius: 5px;
        font-size: 13px;
        font-weight: 500;
        text-decoration: none;
        display: inline-block;
        transition: background-color 0.2s ease;
    }

    .action-link:hover {
        background-color: #2b6cb0;
    }

    .no-course {
        text-align: center;
        font-size: 15px;
        color: #e53e3e;
        margin-top: 30px;
    }
</style>
<div class="page-wrapper">
    <div class="content-wrapper">
        <div class="container">
            <div class="page-title">Danh sách khoá học đang học</div>

            <c:if test="${empty courses}">
                <div class="no-course">Bạn chưa đăng ký khóa học nào.</div>
            </c:if>

            <div class="grid-container">
                <c:forEach var="course" items="${courses}">
                    <div class="course-card">
                        <div class="course-title">${course.name}</div>
                        <div class="course-action">
                            <a class="action-link" href="studentAssignmentServlet?courseId=${course.id}">
                                📘 Xem bài tập
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <jsp:include page="layout/footer.jsp" />
</div>

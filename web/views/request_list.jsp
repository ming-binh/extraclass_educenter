<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="layout/adminHeader.jsp" />

<html>
    <head>
        <title>Danh sách Request</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f7f7f7;
            }
            h2 {
                color: #2c3e50;
                margin-top: 30px;
            }
            table {
                border-collapse: collapse;
                width: 95%;
                margin: 30px auto;
                background: #fff;
                box-shadow: 0 2px 8px #ccc;
                border-radius: 10px;
                overflow: hidden;
            }
            th, td {
                padding: 12px 15px;
                text-align: center;
            }
            th {
                background: #2980b9;
                color: #fff;
                font-size: 16px;
            }
            tr:nth-child(even) {
                background: #f2f2f2;
            }
            tr:hover {
                background: #eaf6fb;
            }
            .pending {
                color: #f39c12;
                font-weight: bold;
            }
            .accepted {
                color: #27ae60;
                font-weight: bold;
            }
            .rejected {
                color: #e74c3c;
                font-weight: bold;
            }
            .pagination {
                text-align: center;
                margin: 25px 0;
            }
            .pagination a, .pagination span.current {
                display: inline-block;
                padding: 8px 14px;
                margin: 0 2px;
                border-radius: 4px;
                background: #eee;
                color: #2980b9;
                text-decoration: none;
                font-weight: bold;
            }
            .pagination span.current {
                background: #2980b9;
                color: #fff;
            }
            .pagination a:hover {
                background: #27ae60;
                color: #fff;
            }
            .button {
                padding: 8px 15px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-weight: bold;
                text-decoration: none;
                display: inline-block;
            }
            .button-primary {
                background: #2980b9;
                color: white;
            }
            .button-primary:hover {
                background: #3498db;
            }
            .button-success {
                background: #27ae60;
                color: white;
            }
            .button-success:hover {
                background: #2ecc71;
            }
            .button-danger {
                background: #e74c3c;
                color: white;
            }
            .button-danger:hover {
                background: #c0392b;
            }
            .button-secondary {
                background: #95a5a6;
                color: white;
            }
            .button-secondary:hover {
                background: #7f8c8d;
            }
            .button:disabled {
                background: #bdc3c7;
                cursor: not-allowed;
            }
            .modal-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                z-index: 999;
            }
            .modal-content {
                position: fixed;
                top: 10%;
                left: 50%;
                transform: translateX(-50%);
                background: #fff;
                padding: 25px;
                border-radius: 8px;
                box-shadow: 0 0 20px rgba(0,0,0,0.3);
                width: 85%;
                max-width: 900px;
                z-index: 1000;
                max-height: 80vh;
                overflow-y: auto;
            }
            .course-comparison {
                display: flex;
                justify-content: space-between;
                margin: 20px 0;
                gap: 20px;
            }
            .course-card {
                width: 45%;
                border: 2px solid #ddd;
                padding: 20px;
                border-radius: 8px;
                background: #f9f9f9;
            }
            .course-card.from {
                border-color: #e74c3c;
            }
            .course-card.to {
                border-color: #27ae60;
            }
            .course-card h4 {
                margin-top: 0;
            }
            .modal-actions {
                text-align: center;
                margin-top: 25px;
                padding-top: 20px;
                border-top: 1px solid #eee;
            }
            .modal-actions form {
                display: inline-block;
                margin: 0 10px;
            }
            .success-message {
                color: #27ae60;
                text-align: center;
                font-weight: bold;
                padding: 10px;
                background: #d4edda;
                border: 1px solid #c3e6cb;
                border-radius: 4px;
                margin: 20px auto;
                width: 95%;
            }
        </style>
    </head>
    <body>

        <h2 style="text-align:center;">Danh sách Request</h2>

        <c:if test="${not empty message}">
            <div class="success-message">${message}</div>
        </c:if>

        <table border="1">
            <tr>
                <th>ID</th>
                <th>Người gửi</th>
                <th>Loại</th>
                <th>Mô tả</th>
                <th>Trạng thái</th>
                <th>Ngày tạo</th>
                <th>Hành động</th>
            </tr>

            <c:if test="${not empty requests}">
                <c:forEach var="req" items="${requests}">
                    <tr>
                        <td>${req.id}</td>
                        <td>${req.requestByName}</td>
                        <td>${req.type.displayName}</td>
                        <td style="max-width: 200px; word-wrap: break-word;">${req.description}</td>
                        <td>
                            <span class="${req.status.name().toLowerCase()}">${req.status.displayName}</span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty req.createdAt}">
                                    <fmt:formatDate value="${req.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </c:when>
                                <c:otherwise>--</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form method="post" action="xu-ly-yeu-cau" style="margin:0;">
                                <input type="hidden" name="action" value="openModal"/>
                                <input type="hidden" name="requestId" value="${req.id}"/>
                                <input type="hidden" name="page" value="${currentPage}"/>
                                <button type="submit" class="button button-primary">Xem chi tiết</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${empty requests}">
                <tr><td colspan="7">Không có request nào</td></tr>
            </c:if>
        </table>

        <div class="pagination">
            <c:if test="${totalPages > 1}">
                <c:if test="${currentPage > 1}">
                    <a href="xu-ly-yeu-cau?page=${currentPage - 1}">&laquo; Trước</a>
                </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="xu-ly-yeu-cau?page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="xu-ly-yeu-cau?page=${currentPage + 1}">Sau &raquo;</a>
                </c:if>
            </c:if>
        </div>

        <c:if test="${not empty selectedRequest}">
            <div class="modal-overlay"></div>
            
            <div class="modal-content">
                <h3 style="color: #2c3e50; text-align: center; margin-bottom: 20px;">
                    Chi tiết yêu cầu - ID: ${selectedRequest.id}
                </h3>
                
                <div style="background: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                    <p><strong>Người gửi:</strong> ${selectedRequest.requestBy}</p>
                    <p><strong>Loại yêu cầu:</strong> ${selectedRequest.type.displayName}</p>
                    <p><strong>Trạng thái:</strong> 
                        <span class="${selectedRequest.status.name().toLowerCase()}">${selectedRequest.status.displayName}</span>
                    </p>
                    <p><strong>Mô tả:</strong> ${selectedRequest.description}</p>
                    <p><strong>Ngày tạo:</strong> 
                        <c:choose>
                            <c:when test="${not empty selectedRequest.createdAt}">
                                <fmt:formatDate value="${selectedRequest.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </c:when>
                            <c:otherwise>--</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <c:if test="${selectedRequest.type.name() == 'STUDENT_CHANGE_COURSE' and not empty fromCourse and not empty toCourse}">
                    <h4 style="text-align: center; color: #2c3e50; margin: 20px 0;">Thông tin đổi khoá học</h4>
                    
                    <div class="course-comparison">
                        <div class="course-card from">
                            <h4 style="color: #e74c3c; margin-bottom: 15px;">📚 Từ khoá học:</h4>
                            <p><strong>Tên:</strong> ${fromCourse.name}</p>
                            <p><strong>Mô tả:</strong> ${fromCourse.description}</p>
                        </div>
                        <div class="course-card to">
                            <h4 style="color: #27ae60; margin-bottom: 15px;">📚 Đến khoá học:</h4>
                            <p><strong>Tên:</strong> ${toCourse.name}</p>
                            <p><strong>Mô tả:</strong> ${toCourse.description}</p>
                        </div>
                    </div>
                    
                </c:if>
                
                <div class="modal-actions">
                    <c:if test="${selectedRequest.type.name() == 'STUDENT_CHANGE_COURSE' and selectedRequest.status.name() == 'pending'}">
                        <form method="post" action="xu-ly-yeu-cau">
                            <input type="hidden" name="action" value="confirm"/>
                            <input type="hidden" name="decision" value="accepted"/>
                            <input type="hidden" name="requestId" value="${selectedRequest.id}"/>
                            <input type="hidden" name="page" value="${currentPage}"/>
                            <button type="submit" class="button button-success">
                                ✓ Đồng ý
                            </button>
                        </form>
                        
                        <form method="post" action="xu-ly-yeu-cau">
                            <input type="hidden" name="action" value="confirm"/>
                            <input type="hidden" name="decision" value="rejected"/>
                            <input type="hidden" name="requestId" value="${selectedRequest.id}"/>
                            <input type="hidden" name="page" value="${currentPage}"/>
                            <button type="submit" class="button button-danger">
                                ✗ Từ chối
                            </button>
                        </form>
                    </c:if>
                    <c:if test="${selectedRequest.type.name() != 'STUDENT_CHANGE_COURSE' and selectedRequest.status.name() == 'pending'}">
                        <form method="post" action="xu-ly-yeu-cau">
                            <input type="hidden" name="action" value="confirm"/>
                            <input type="hidden" name="decision" value="accepted"/>
                            <input type="hidden" name="requestId" value="${selectedRequest.id}"/>
                            <input type="hidden" name="page" value="${currentPage}"/>
                            <button type="submit" class="button button-success">
                                ✓ Đồng ý
                            </button>
                        </form>
                        
                        <form method="post" action="xu-ly-yeu-cau">
                            <input type="hidden" name="action" value="confirm"/>
                            <input type="hidden" name="decision" value="rejected"/>
                            <input type="hidden" name="requestId" value="${selectedRequest.id}"/>
                            <input type="hidden" name="page" value="${currentPage}"/>
                            <button type="submit" class="button button-danger">
                                ✗ Từ chối
                            </button>
                        </form>
                    </c:if>
                    <a href="xu-ly-yeu-cau?page=${currentPage}" class="button button-secondary">
                        Đóng
                    </a>
                </div>
            </div>
        </c:if>

        <c:if test="${param.debug == 'true'}">
            <div style="background: yellow; padding: 10px; margin: 10px; border: 1px solid orange;">
                <h4>Debug Info:</h4>
                <p>selectedRequest: ${selectedRequest != null ? selectedRequest.id : 'null'}</p>
                <p>fromCourse: ${fromCourse != null ? fromCourse.name : 'null'}</p>
                <p>toCourse: ${toCourse != null ? toCourse.name : 'null'}</p>
                <p>currentPage: ${currentPage}</p>
                <p>totalPages: ${totalPages}</p>
            </div>
        </c:if>

    </body>
</html>
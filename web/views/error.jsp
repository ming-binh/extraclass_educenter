<%-- 
    Document   : error.jsp
    Created on : May 23, 2025, 4:29:19 AM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<% request.setAttribute("title", "Lỗi");%>

<jsp:include page="layout/header.jsp" />

<style>
.error-container {
    max-width: 600px;
    margin: 4rem auto;
    padding: 2rem;
    text-align: center;
    background: white;
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.1);
    border: 1px solid #e1e8ed;
}

.error-icon {
    font-size: 4rem;
    color: #f44336;
    margin-bottom: 1rem;
}

.error-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 1rem;
}

.error-message {
    color: #666;
    margin-bottom: 2rem;
    line-height: 1.6;
}

.error-actions {
    display: flex;
    gap: 1rem;
    justify-content: center;
    flex-wrap: wrap;
}

.error-btn {
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    text-decoration: none;
    font-weight: 500;
    transition: all 0.3s ease;
}

.error-btn-primary {
    background: #667eea;
    color: white;
}

.error-btn-primary:hover {
    background: #5a6fd8;
    transform: translateY(-1px);
}

.error-btn-secondary {
    background: #f5f5f5;
    color: #333;
    border: 1px solid #e1e8ed;
}

.error-btn-secondary:hover {
    background: #e8e8e8;
    transform: translateY(-1px);
}
</style>

<div class="error-container">
    <div class="error-icon">
        <i class="fas fa-exclamation-triangle"></i>
    </div>
    
    <h1 class="error-title">
        <c:choose>
            <c:when test="${not empty errorLog and errorLog.contains('quyền')}">
                Không có quyền truy cập
            </c:when>
            <c:otherwise>
                Đã xảy ra lỗi
            </c:otherwise>
        </c:choose>
    </h1>
    
    <p class="error-message">
        <c:choose>
            <c:when test="${not empty errorLog}">
                ${errorLog}
            </c:when>
            <c:when test="${not empty error}">
                ${error}
            </c:when>
            <c:otherwise>
                Có lỗi xảy ra trong quá trình xử lý yêu cầu của bạn. Vui lòng thử lại sau.
            </c:otherwise>
        </c:choose>
    </p>
    
    <div class="error-actions">
        <a href="javascript:history.back()" class="error-btn error-btn-secondary">
            <i class="fas fa-arrow-left"></i> Quay lại
        </a>
        <a href="trang-chu" class="error-btn error-btn-primary">
            <i class="fas fa-home"></i> Về trang chủ
        </a>
    </div>
</div>

<jsp:include page="layout/footer.jsp" /> 
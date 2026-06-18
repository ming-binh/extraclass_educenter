<%-- 
    Document   : parentRequest.jsp
    Created on : May 23, 2025, 4:29:19 AM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<% request.setAttribute("title", "Yêu cầu phụ huynh");%>

<jsp:include page="layout/header.jsp" />

<style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        line-height: 1.6;
        color: #333;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        min-height: 100vh;
    }

    .parentrequest-container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 2rem;
    }

    .parentrequest-header {
        background: white;
        border-radius: 20px;
        padding: 2rem;
        margin-bottom: 2rem;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
    }

    .parentrequest-header-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 1rem;
    }

    .parentrequest-title h1 {
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 0.5rem;
        font-weight: 700;
    }

    .parentrequest-title p {
        color: #666;
        font-size: 1.1rem;
    }

    .parentrequest-role-badge {
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: white;
        padding: 0.75rem 1.5rem;
        border-radius: 25px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .parentrequest-content {
        display: grid;
        grid-template-columns: 1fr 2fr;
        gap: 2rem;
    }

    .parentrequest-form-section {
        background: white;
        border-radius: 20px;
        padding: 2rem;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
        height: fit-content;
    }

    .parentrequest-form-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 2rem;
    }

    .parentrequest-form-icon {
        width: 50px;
        height: 50px;
        background: linear-gradient(135deg, #667eea, #764ba2);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 1.5rem;
    }

    .parentrequest-form-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
    }

    .parentrequest-form-group {
        margin-bottom: 1.5rem;
    }

    .parentrequest-label {
        display: block;
        font-weight: 500;
        color: #333;
        margin-bottom: 0.5rem;
        font-size: 0.9rem;
    }

    .parentrequest-input {
        width: 100%;
        padding: 0.75rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        transition: all 0.3s ease;
    }

    .parentrequest-input:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .parentrequest-textarea {
        resize: vertical;
        min-height: 120px;
    }

    .parentrequest-submit-btn {
        width: 100%;
        padding: 1rem;
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: white;
        border: none;
        border-radius: 12px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5rem;
    }

    .parentrequest-submit-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
    }

    .parentrequest-list-section {
        background: white;
        border-radius: 20px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        height: 600px;
    }

    .parentrequest-list-header {
        padding: 2rem;
        border-bottom: 1px solid #e9ecef;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 1rem;
        flex-shrink: 0;
    }

    .parentrequest-list-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
    }

    .parentrequest-search-filter {
        display: flex;
        gap: 1rem;
        align-items: center;
    }

    .parentrequest-search-box {
        position: relative;
    }

    .parentrequest-search-icon {
        position: absolute;
        left: 1rem;
        top: 50%;
        transform: translateY(-50%);
        color: #999;
    }

    .parentrequest-search-input {
        padding: 0.75rem 1rem 0.75rem 2.5rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        width: 200px;
        transition: all 0.3s ease;
    }

    .parentrequest-search-input:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .parentrequest-list-content {
        flex: 1;
        overflow-y: auto;
        padding: 0;
    }

    .parentrequest-list-content::-webkit-scrollbar {
        width: 8px;
    }

    .parentrequest-list-content::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 4px;
    }

    .parentrequest-list-content::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 4px;
    }

    .parentrequest-list-content::-webkit-scrollbar-thumb:hover {
        background: #a8a8a8;
    }

    .parentrequest-item {
        padding: 1.5rem 2rem;
        border-bottom: 1px solid #f1f3f4;
        transition: all 0.3s ease;
    }

    .parentrequest-item:hover {
        background: #f8f9ff;
    }

    .parentrequest-item:last-child {
        border-bottom: none;
    }

    .parentrequest-item-content {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 1rem;
    }

    .parentrequest-item-main {
        flex: 1;
    }

    .parentrequest-item-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 0.75rem;
    }

    .parentrequest-item-title {
        font-weight: 600;
        color: #333;
        font-size: 1rem;
    }

    .parentrequest-status {
        padding: 0.25rem 0.75rem;
        border-radius: 20px;
        font-size: 0.75rem;
        font-weight: 500;
        border: 1px solid;
    }

    .parentrequest-status.pending {
        background: #fff3cd;
        color: #856404;
        border-color: #ffeaa7;
    }

    .parentrequest-status.accepted {
        background: #d4edda;
        color: #155724;
        border-color: #c3e6cb;
    }

    .parentrequest-status.rejected {
        background: #f8d7da;
        color: #721c24;
        border-color: #f5c6cb;
    }

    .parentrequest-item-meta {
        display: flex;
        align-items: center;
        gap: 1.5rem;
        color: #666;
        font-size: 0.85rem;
    }

    .parentrequest-meta-item {
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .parentrequest-item-actions {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
    }

    .parentrequest-action-btn {
        padding: 0.5rem 1rem;
        border: none;
        border-radius: 6px;
        font-size: 0.8rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .parentrequest-action-btn.detail {
        background: #e3f2fd;
        color: #1976d2;
    }

    .parentrequest-action-btn.detail:hover {
        background: #bbdefb;
    }

    @media (max-width: 1024px) {
        .parentrequest-content {
            grid-template-columns: 1fr;
        }
        
        .parentrequest-header-content {
            flex-direction: column;
            align-items: flex-start;
        }
        
        .parentrequest-search-filter {
            flex-direction: column;
            align-items: stretch;
        }
        
        .parentrequest-search-input {
            width: 100%;
        }
    }

    @media (max-width: 768px) {
        .parentrequest-container {
            padding: 1rem;
        }
        
        .parentrequest-title h1 {
            font-size: 2rem;
        }
        
        .parentrequest-item-content {
            flex-direction: column;
            gap: 1rem;
        }
        
        .parentrequest-item-meta {
            flex-direction: column;
            align-items: flex-start;
            gap: 0.5rem;
        }
    }

    @keyframes parentrequestFadeIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .parentrequest-form-section,
    .parentrequest-list-section {
        animation: parentrequestFadeIn 0.6s ease;
    }
</style>

<div class="parentrequest-container">
    <div class="parentrequest-header">
        <div class="parentrequest-header-content">
            <div class="parentrequest-title">
                <h1>Yêu cầu phụ huynh</h1>
                <p>Quản lý các yêu cầu xin nộp muộn học phí</p>
            </div>
            
            <div class="parentrequest-role-badge">
                👨‍👩‍👧‍👦 Phụ huynh
            </div>
        </div>
    </div>

    <div class="parentrequest-content">
        <div class="parentrequest-form-section">
            <div class="parentrequest-form-header">
                <div class="parentrequest-form-icon">💰</div>
                <h2 class="parentrequest-form-title">Tạo yêu cầu mới</h2>
            </div>

            <div class="parentrequest-form-content">
                <form id="createRequestForm" method="POST" action="">
                    <input type="hidden" name="action" value="create">
                    <input type="hidden" name="type" value="parent-request-overdue">
                    
                    <div class="parentrequest-form-group">
                        <label class="parentrequest-label">Mô tả chi tiết</label>
                        <textarea class="parentrequest-input parentrequest-textarea" name="description" id="requestDescription" placeholder="Mô tả chi tiết lý do xin nộp muộn học phí..." required></textarea>
                    </div>

                    <button type="submit" class="parentrequest-submit-btn" id="submitRequest">
                        📤 Gửi yêu cầu
                    </button>
                </form>
            </div>
        </div>

        <div class="parentrequest-list-section">
            <div class="parentrequest-list-header">
                <h2 class="parentrequest-list-title">Danh sách yêu cầu của tôi</h2>
                <div class="parentrequest-search-filter">
                    <div class="parentrequest-search-box">
                        <span class="parentrequest-search-icon">🔍</span>
                        <input type="text" class="parentrequest-search-input" placeholder="Tìm kiếm..." id="searchInput">
                    </div>
                </div>
            </div>

            <div class="parentrequest-list-content" id="requestList">
                <c:choose>
                    <c:when test="${not empty requests}">
                        <c:forEach var="request" items="${requests}">
                            <div class="parentrequest-item" data-type="${request.type}">
                                <div class="parentrequest-item-content">
                                    <div class="parentrequest-item-main">
                                        <div class="parentrequest-item-header">
                                            <h3 class="parentrequest-item-title">
                                                Xin nộp muộn học phí
                                            </h3>
                                            <span class="parentrequest-status ${request.status}">
                                                <c:choose>
                                                    <c:when test="${request.status == 'pending'}">Chờ duyệt</c:when>
                                                    <c:when test="${request.status == 'accepted'}">Đã duyệt</c:when>
                                                    <c:when test="${request.status == 'rejected'}">Từ chối</c:when>
                                                    <c:otherwise>${request.status}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="parentrequest-item-meta">
                                            <div class="parentrequest-meta-item">
                                                <span>📅</span>
                                                <span>${request.createdAt}</span>
                                            </div>
                                            <div class="parentrequest-meta-item">
                                                <span>📝</span>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${fn:length(request.description) > 50}">
                                                            ${fn:substring(request.description, 0, 50)}...
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${request.description}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="parentrequest-item-actions">
                                        <button class="parentrequest-action-btn detail" onclick="viewRequestDetail(${request.id})">Xem chi tiết</button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div style="text-align: center; padding: 3rem; color: #666;">
                            <div style="font-size: 3rem; margin-bottom: 1rem;">💰</div>
                            <h3>Chưa có yêu cầu nào</h3>
                            <p>Bạn chưa tạo yêu cầu nào. Hãy tạo yêu cầu đầu tiên!</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script>
    // Form validation trước khi submit
    document.getElementById('createRequestForm').addEventListener('submit', function(e) {
        const description = document.getElementById('requestDescription').value;
        
        if (!description || description.trim() === '') {
            e.preventDefault();
            alert('Vui lòng nhập mô tả chi tiết!');
            return;
        }
    });

    // Tìm kiếm theo nội dung
    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const requestItems = document.querySelectorAll('.parentrequest-item');
        
        requestItems.forEach(item => {
            const title = item.querySelector('.parentrequest-item-title').textContent.toLowerCase();
            const description = item.querySelector('.parentrequest-meta-item:last-child span:last-child').textContent.toLowerCase();
            
            if (title.includes(searchTerm) || description.includes(searchTerm)) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });

    // Hàm xem chi tiết request
    function viewRequestDetail(requestId) {
        alert(`Xem chi tiết yêu cầu ID: ${requestId}`);
    }
</script>

<!-- Hiển thị thông báo lỗi/thành công nếu có -->
<c:if test="${not empty error}">
    <script>
        alert('${error}');
    </script>
</c:if>

<c:if test="${not empty success}">
    <script>
        alert('${success}');
    </script>
</c:if>

<jsp:include page="layout/footer.jsp" /> 
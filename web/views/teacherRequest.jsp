<%-- 
    Document   : teacherRequest.jsp
    Created on : May 23, 2025, 4:29:19 AM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<% request.setAttribute("title", "Yêu cầu giáo viên");%>

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

    .teacherrequest-container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 2rem;
    }

    .teacherrequest-header {
        background: white;
        border-radius: 20px;
        padding: 2rem;
        margin-bottom: 2rem;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
    }

    .teacherrequest-header-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 1rem;
    }

    .teacherrequest-title h1 {
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 0.5rem;
        font-weight: 700;
    }

    .teacherrequest-title p {
        color: #666;
        font-size: 1.1rem;
    }

    .teacherrequest-role-badge {
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: white;
        padding: 0.75rem 1.5rem;
        border-radius: 25px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .teacherrequest-content {
        display: grid;
        grid-template-columns: 1fr 2fr;
        gap: 2rem;
    }

    .teacherrequest-form-section {
        background: white;
        border-radius: 20px;
        padding: 2rem;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
        height: fit-content;
    }

    .teacherrequest-form-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 2rem;
    }

    .teacherrequest-form-icon {
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

    .teacherrequest-form-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
    }

    .teacherrequest-type-grid {
        display: grid;
        gap: 0.75rem;
        margin-bottom: 2rem;
    }

    .teacherrequest-type-btn {
        padding: 1rem;
        border: 2px solid #e9ecef;
        border-radius: 12px;
        background: white;
        cursor: pointer;
        transition: all 0.3s ease;
        text-align: left;
    }

    .teacherrequest-type-btn:hover {
        border-color: #667eea;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
    }

    .teacherrequest-type-btn.selected {
        border-color: #667eea;
        background: #f8f9ff;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
    }

    .teacherrequest-type-content {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .teacherrequest-type-icon {
        font-size: 1.5rem;
    }

    .teacherrequest-type-label {
        font-weight: 500;
        color: #333;
    }

    .teacherrequest-form-group {
        margin-bottom: 1.5rem;
    }

    .teacherrequest-label {
        display: block;
        font-weight: 500;
        color: #333;
        margin-bottom: 0.5rem;
        font-size: 0.9rem;
    }

    .teacherrequest-input {
        width: 100%;
        padding: 0.75rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        transition: all 0.3s ease;
    }

    .teacherrequest-input:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .teacherrequest-textarea {
        resize: vertical;
        min-height: 120px;
    }

    .teacherrequest-select {
        width: 100%;
        padding: 0.75rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        background: white;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .teacherrequest-select:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .teacherrequest-submit-btn {
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

    .teacherrequest-submit-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
    }

    .teacherrequest-list-section {
        background: white;
        border-radius: 20px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        height: 600px;
    }

    .teacherrequest-list-header {
        padding: 2rem;
        border-bottom: 1px solid #e9ecef;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 1rem;
        flex-shrink: 0;
    }

    .teacherrequest-list-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
    }

    .teacherrequest-search-filter {
        display: flex;
        gap: 1rem;
        align-items: center;
    }

    .teacherrequest-search-box {
        position: relative;
    }

    .teacherrequest-search-icon {
        position: absolute;
        left: 1rem;
        top: 50%;
        transform: translateY(-50%);
        color: #999;
    }

    .teacherrequest-search-input {
        padding: 0.75rem 1rem 0.75rem 2.5rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        width: 200px;
        transition: all 0.3s ease;
    }

    .teacherrequest-search-input:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .teacherrequest-list-content {
        flex: 1;
        overflow-y: auto;
        padding: 0;
    }

    .teacherrequest-list-content::-webkit-scrollbar {
        width: 8px;
    }

    .teacherrequest-list-content::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 4px;
    }

    .teacherrequest-list-content::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 4px;
    }

    .teacherrequest-list-content::-webkit-scrollbar-thumb:hover {
        background: #a8a8a8;
    }

    .teacherrequest-item {
        padding: 1.5rem 2rem;
        border-bottom: 1px solid #f1f3f4;
        transition: all 0.3s ease;
    }

    .teacherrequest-item:hover {
        background: #f8f9ff;
    }

    .teacherrequest-item:last-child {
        border-bottom: none;
    }

    .teacherrequest-item-content {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 1rem;
    }

    .teacherrequest-item-main {
        flex: 1;
    }

    .teacherrequest-item-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 0.75rem;
    }

    .teacherrequest-item-title {
        font-weight: 600;
        color: #333;
        font-size: 1rem;
    }

    .teacherrequest-status {
        padding: 0.25rem 0.75rem;
        border-radius: 20px;
        font-size: 0.75rem;
        font-weight: 500;
        border: 1px solid;
    }

    .teacherrequest-status.pending {
        background: #fff3cd;
        color: #856404;
        border-color: #ffeaa7;
    }

    .teacherrequest-status.accepted {
        background: #d4edda;
        color: #155724;
        border-color: #c3e6cb;
    }

    .teacherrequest-status.rejected {
        background: #f8d7da;
        color: #721c24;
        border-color: #f5c6cb;
    }

    .teacherrequest-item-meta {
        display: flex;
        align-items: center;
        gap: 1.5rem;
        color: #666;
        font-size: 0.85rem;
    }

    .teacherrequest-meta-item {
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .teacherrequest-item-actions {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
    }

    .teacherrequest-action-btn {
        padding: 0.5rem 1rem;
        border: none;
        border-radius: 6px;
        font-size: 0.8rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .teacherrequest-action-btn.detail {
        background: #e3f2fd;
        color: #1976d2;
    }

    .teacherrequest-action-btn.detail:hover {
        background: #bbdefb;
    }

    @media (max-width: 1024px) {
        .teacherrequest-content {
            grid-template-columns: 1fr;
        }
        
        .teacherrequest-header-content {
            flex-direction: column;
            align-items: flex-start;
        }
        
        .teacherrequest-search-filter {
            flex-direction: column;
            align-items: stretch;
        }
        
        .teacherrequest-search-input {
            width: 100%;
        }
    }

    @media (max-width: 768px) {
        .teacherrequest-container {
            padding: 1rem;
        }
        
        .teacherrequest-title h1 {
            font-size: 2rem;
        }
        
        .teacherrequest-item-content {
            flex-direction: column;
            gap: 1rem;
        }
        
        .teacherrequest-item-meta {
            flex-direction: column;
            align-items: flex-start;
            gap: 0.5rem;
        }
    }

    @keyframes teacherrequestFadeIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .teacherrequest-form-section,
    .teacherrequest-list-section {
        animation: teacherrequestFadeIn 0.6s ease;
    }
</style>

<div class="teacherrequest-container">
    <div class="teacherrequest-header">
        <div class="teacherrequest-header-content">
            <div class="teacherrequest-title">
                <h1>Yêu cầu giáo viên</h1>
                <p>Quản lý các yêu cầu xin nghỉ, đổi phòng học, thêm lớp bù</p>
            </div>
            
            <div class="teacherrequest-role-badge">
                👩‍🏫 Giáo viên
            </div>
        </div>
    </div>

    <div class="teacherrequest-content">
        <div class="teacherrequest-form-section">
            <div class="teacherrequest-form-header">
                <div class="teacherrequest-form-icon">➕</div>
                <h2 class="teacherrequest-form-title">Tạo yêu cầu mới</h2>
            </div>

            <div class="teacherrequest-form-content">
                <div class="teacherrequest-form-group">
                    <label class="teacherrequest-label">Loại yêu cầu</label>
                    <div class="teacherrequest-type-grid" id="requestTypeGrid">
                        <button class="teacherrequest-type-btn" data-type="TEACHER_ABSENT">
                            <div class="teacherrequest-type-content">
                                <span class="teacherrequest-type-icon">🏠</span>
                                <span class="teacherrequest-type-label">Xin nghỉ</span>
                            </div>
                        </button>
                        <button class="teacherrequest-type-btn" data-type="TEACHER_CHANGE_SECTION">
                            <div class="teacherrequest-type-content">
                                <span class="teacherrequest-type-icon">➕</span>
                                <span class="teacherrequest-type-label">Xin dạy bù</span>
                            </div>
                        </button>
                    </div>
                </div>

                <form id="createRequestForm" method="POST" action="" style="display: none;">
                    <input type="hidden" name="action" value="create">
                    <input type="hidden" name="type" id="selectedType">
                    
                    <div class="teacherrequest-form-group" id="sectionGroup">
                        <label class="teacherrequest-label">Chọn buổi học</label>
                        <select class="teacherrequest-select" name="sectionId" id="sectionSelect">
                            <option value="">-- Chọn buổi học --</option>
                            <c:forEach var="section" items="${sections}">
                                <option value="${section.id}">
                                    ${section.courseName} - ${section.dateTime} - ${section.classroom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="teacherrequest-form-group" id="courseGroup" style="display: none;">
                        <label class="teacherrequest-label">Chọn khóa học</label>
                        <select class="teacherrequest-select" name="courseId" id="courseSelect">
                            <option value="">-- Chọn khóa học --</option>
                            <c:forEach var="course" items="${courses}">
                                <option value="${course.id}">${course.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="teacherrequest-form-group">
                        <label class="teacherrequest-label">Mô tả chi tiết</label>
                        <textarea class="teacherrequest-input teacherrequest-textarea" name="description" id="requestDescription" placeholder="Mô tả chi tiết lý do..." required></textarea>
                    </div>

                    <button type="submit" class="teacherrequest-submit-btn" id="submitRequest">
                        📤 Gửi yêu cầu
                    </button>
                </form>
            </div>
        </div>

        <div class="teacherrequest-list-section">
            <div class="teacherrequest-list-header">
                <h2 class="teacherrequest-list-title">Danh sách yêu cầu của tôi</h2>
                <div class="teacherrequest-search-filter">
                    <div class="teacherrequest-search-box">
                        <span class="teacherrequest-search-icon">🔍</span>
                        <input type="text" class="teacherrequest-search-input" placeholder="Tìm kiếm..." id="searchInput">
                    </div>
                </div>
            </div>

            <div class="teacherrequest-list-content" id="requestList">
                <c:choose>
                    <c:when test="${not empty requests}">
                        <c:forEach var="request" items="${requests}">
                            <div class="teacherrequest-item" data-type="${request.type}">
                                <div class="teacherrequest-item-content">
                                    <div class="teacherrequest-item-main">
                                        <div class="teacherrequest-item-header">
                                            <h3 class="teacherrequest-item-title">
                                                <c:choose>
                                                    <c:when test="${request.type == 'TEACHER_ABSENT'}">Xin nghỉ</c:when>
                                                    <c:when test="${request.type == 'TEACHER_CHANGE_SECTION'}">Xin dạy bù</c:when>
                                                    <c:otherwise>${request.type}</c:otherwise>
                                                </c:choose>
                                            </h3>
                                            <span class="teacherrequest-status ${request.status}">
                                                <c:choose>
                                                    <c:when test="${request.status == 'pending'}">Chờ duyệt</c:when>
                                                    <c:when test="${request.status == 'accepted'}">Đã duyệt</c:when>
                                                    <c:when test="${request.status == 'rejected'}">Từ chối</c:when>
                                                    <c:otherwise>${request.status}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="teacherrequest-item-meta">
                                            <div class="teacherrequest-meta-item">
                                                <span>📅</span>
                                                <span>${request.createdAt}</span>
                                            </div>
                                            <div class="teacherrequest-meta-item">
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
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div style="text-align: center; padding: 3rem; color: #666;">
                            <div style="font-size: 3rem; margin-bottom: 1rem;">👩‍🏫</div>
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
    document.querySelectorAll('.teacherrequest-type-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.teacherrequest-type-btn').forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
            
            const requestType = this.dataset.type;
            document.getElementById('selectedType').value = requestType;
            document.getElementById('createRequestForm').style.display = 'block';
            
            // Hiển thị/ẩn các trường tùy theo loại yêu cầu
            const sectionGroup = document.getElementById('sectionGroup');
            const courseGroup = document.getElementById('courseGroup');
            
            if (requestType === 'TEACHER_CHANGE_SECTION') {
                sectionGroup.style.display = 'none';
                courseGroup.style.display = 'block';
            } else {
                sectionGroup.style.display = 'block';
                courseGroup.style.display = 'none';
            }
        });
    });

    document.getElementById('createRequestForm').addEventListener('submit', function(e) {
        const description = document.getElementById('requestDescription').value;
        const selectedType = document.getElementById('selectedType').value;
        
        if (!description || description.trim() === '') {
            e.preventDefault();
            alert('Vui lòng nhập mô tả chi tiết!');
            return;
        }
        
        if (selectedType === 'TEACHER_CHANGE_SECTION') {
            const courseId = document.getElementById('courseSelect').value;
            if (!courseId) {
                e.preventDefault();
                alert('Vui lòng chọn khóa học!');
                return;
            }
        } else {
            const sectionId = document.getElementById('sectionSelect').value;
            if (!sectionId) {
                e.preventDefault();
                alert('Vui lòng chọn buổi học!');
                return;
            }
        }
    });

    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const requestItems = document.querySelectorAll('.teacherrequest-item');
        
        requestItems.forEach(item => {
            const title = item.querySelector('.teacherrequest-item-title').textContent.toLowerCase();
            const description = item.querySelector('.teacherrequest-meta-item:last-child span:last-child').textContent.toLowerCase();
            
            if (title.includes(searchTerm) || description.includes(searchTerm)) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });

    function viewRequestDetail(requestId) {
        alert(`Xem chi tiết yêu cầu ID: ${requestId}`);
    }
</script>

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
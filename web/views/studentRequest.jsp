<%-- 
    Document   : studentRequest.jsp
    Created on : May 23, 2025, 4:29:19 AM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<% request.setAttribute("title", "Yêu cầu học sinh");%>

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

    .studentrequest-container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 2rem;
    }

    .studentrequest-header {
        background: white;
        border-radius: 20px;
        padding: 2rem;
        margin-bottom: 2rem;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
    }

    .studentrequest-header-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 1rem;
    }

    .studentrequest-title h1 {
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 0.5rem;
        font-weight: 700;
    }

    .studentrequest-title p {
        color: #666;
        font-size: 1.1rem;
    }

    .studentrequest-role-badge {
        background: linear-gradient(135deg, #667eea, #764ba2);
        color: white;
        padding: 0.75rem 1.5rem;
        border-radius: 25px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .studentrequest-content {
        display: grid;
        grid-template-columns: 1fr 2fr;
        gap: 2rem;
    }

    .studentrequest-form-section {
        background: white;
        border-radius: 20px;
        padding: 2rem;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
        height: fit-content;
    }

    .studentrequest-form-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 2rem;
    }

    .studentrequest-form-icon {
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

    .studentrequest-form-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
    }

    .studentrequest-type-grid {
        display: grid;
        gap: 0.75rem;
        margin-bottom: 2rem;
    }

    .studentrequest-type-btn {
        padding: 1rem;
        border: 2px solid #e9ecef;
        border-radius: 12px;
        background: white;
        cursor: pointer;
        transition: all 0.3s ease;
        text-align: left;
    }

    .studentrequest-type-btn:hover {
        border-color: #667eea;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
    }

    .studentrequest-type-btn.selected {
        border-color: #667eea;
        background: #f8f9ff;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
    }

    .studentrequest-type-content {
        display: flex;
        align-items: center;
        gap: 1rem;
    }

    .studentrequest-type-icon {
        font-size: 1.5rem;
    }

    .studentrequest-type-label {
        font-weight: 500;
        color: #333;
    }

    .studentrequest-form-group {
        margin-bottom: 1.5rem;
    }

    .studentrequest-label {
        display: block;
        font-weight: 500;
        color: #333;
        margin-bottom: 0.5rem;
        font-size: 0.9rem;
    }

    .studentrequest-input {
        width: 100%;
        padding: 0.75rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        transition: all 0.3s ease;
    }

    .studentrequest-input:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .studentrequest-textarea {
        resize: vertical;
        min-height: 80px;
    }

    .studentrequest-select {
        width: 100%;
        padding: 0.75rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        background: white;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .studentrequest-select:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .studentrequest-submit-btn {
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

    .studentrequest-submit-btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
    }

    .studentrequest-list-section {
        background: white;
        border-radius: 20px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        border: 1px solid #e1e8ed;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        height: 600px;
    }

    .studentrequest-list-header {
        padding: 2rem;
        border-bottom: 1px solid #e9ecef;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap;
        gap: 1rem;
        flex-shrink: 0;
    }

    .studentrequest-list-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
    }

    .studentrequest-search-filter {
        display: flex;
        gap: 1rem;
        align-items: center;
    }

    .studentrequest-search-box {
        position: relative;
    }

    .studentrequest-search-icon {
        position: absolute;
        left: 1rem;
        top: 50%;
        transform: translateY(-50%);
        color: #999;
    }

    .studentrequest-search-input {
        padding: 0.75rem 1rem 0.75rem 2.5rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        width: 200px;
        transition: all 0.3s ease;
    }

    .studentrequest-search-input:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .studentrequest-filter-select {
        padding: 0.75rem 1rem;
        border: 2px solid #e9ecef;
        border-radius: 8px;
        font-size: 0.9rem;
        background: white;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .studentrequest-filter-select:focus {
        outline: none;
        border-color: #667eea;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
    }

    .studentrequest-list-content {
        flex: 1;
        overflow-y: auto;
        padding: 0;
    }

    .studentrequest-list-content::-webkit-scrollbar {
        width: 8px;
    }

    .studentrequest-list-content::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 4px;
    }

    .studentrequest-list-content::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 4px;
    }

    .studentrequest-list-content::-webkit-scrollbar-thumb:hover {
        background: #a8a8a8;
    }

    .studentrequest-item {
        padding: 1.5rem 2rem;
        border-bottom: 1px solid #f1f3f4;
        transition: all 0.3s ease;
    }

    .studentrequest-item:hover {
        background: #f8f9ff;
    }

    .studentrequest-item:last-child {
        border-bottom: none;
    }

    .studentrequest-item-content {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 1rem;
    }

    .studentrequest-item-main {
        flex: 1;
    }

    .studentrequest-item-header {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 0.75rem;
    }

    .studentrequest-item-title {
        font-weight: 600;
        color: #333;
        font-size: 1rem;
    }

    .studentrequest-status {
        padding: 0.25rem 0.75rem;
        border-radius: 20px;
        font-size: 0.75rem;
        font-weight: 500;
        border: 1px solid;
    }

    .studentrequest-status.pending {
        background: #fff3cd;
        color: #856404;
        border-color: #ffeaa7;
    }

    .studentrequest-status.accepted {
        background: #d4edda;
        color: #155724;
        border-color: #c3e6cb;
    }

    .studentrequest-status.rejected {
        background: #f8d7da;
        color: #721c24;
        border-color: #f5c6cb;
    }

    .studentrequest-item-meta {
        display: flex;
        align-items: center;
        gap: 1.5rem;
        color: #666;
        font-size: 0.85rem;
    }

    .studentrequest-meta-item {
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .studentrequest-role-badge {
        padding: 0.25rem 0.75rem;
        background: #f8f9fa;
        color: #666;
        border-radius: 12px;
        font-size: 0.75rem;
        border: 1px solid #e9ecef;
    }

    .studentrequest-item-actions {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
    }

    .studentrequest-action-btn {
        padding: 0.5rem 1rem;
        border: none;
        border-radius: 6px;
        font-size: 0.8rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .studentrequest-action-btn.detail {
        background: #e3f2fd;
        color: #1976d2;
    }

    .studentrequest-action-btn.detail:hover {
        background: #bbdefb;
    }

    @media (max-width: 1024px) {
        .studentrequest-content {
            grid-template-columns: 1fr;
        }
        
        .studentrequest-header-content {
            flex-direction: column;
            align-items: flex-start;
        }
        
        .studentrequest-search-filter {
            flex-direction: column;
            align-items: stretch;
        }
        
        .studentrequest-search-input {
            width: 100%;
        }
    }

    @media (max-width: 768px) {
        .studentrequest-container {
            padding: 1rem;
        }
        
        .studentrequest-title h1 {
            font-size: 2rem;
        }
        
        .studentrequest-item-content {
            flex-direction: column;
            gap: 1rem;
        }
        
        .studentrequest-item-meta {
            flex-direction: column;
            align-items: flex-start;
            gap: 0.5rem;
        }
    }

    @keyframes studentrequestFadeIn {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .studentrequest-form-section,
    .studentrequest-list-section {
        animation: studentrequestFadeIn 0.6s ease;
    }
</style>

<div class="studentrequest-container">
    <div class="studentrequest-header">
        <div class="studentrequest-header-content">
            <div class="studentrequest-title">
                <h1>Yêu cầu học sinh</h1>
                <p>Quản lý các yêu cầu nghỉ học và chuyển lớp</p>
            </div>
            
            <div class="studentrequest-role-badge">
                👨‍🎓 Học sinh
            </div>
        </div>
    </div>

    <div class="studentrequest-content">
        <div class="studentrequest-form-section">
            <div class="studentrequest-form-header">
                <div class="studentrequest-form-icon">➕</div>
                <h2 class="studentrequest-form-title">Tạo yêu cầu mới</h2>
            </div>

            <div class="studentrequest-form-content">
                <div class="studentrequest-form-group">
                    <label class="studentrequest-label">Loại yêu cầu</label>
                    <div class="studentrequest-type-grid" id="requestTypeGrid">
                        <button class="studentrequest-type-btn" data-type="STUDENT_ABSENT_REQUEST">
                            <div class="studentrequest-type-content">
                                <span class="studentrequest-type-icon">🏠</span>
                                <span class="studentrequest-type-label">Xin nghỉ học</span>
                            </div>
                        </button>
                        <button class="studentrequest-type-btn" data-type="STUDENT_CHANGE_COURSE">
                            <div class="studentrequest-type-content">
                                <span class="studentrequest-type-icon">🔄</span>
                                <span class="studentrequest-type-label">Chuyển lớp</span>
                            </div>
                        </button>
                    </div>
                </div>

                <div id="requestForm" style="display: none;">
                    <form id="createRequestForm" method="POST" action="">
                        <input type="hidden" name="action" value="create">

                        <div class="studentrequest-form-group" id="fromCourseSelectGroup" style="display: none;">
                            <label class="studentrequest-label">Chọn khóa học muốn chuyển</label>
                            <select class="studentrequest-select" name="fromCourseId" id="fromCourseSelect">
                                <option value="">-- Chọn khóa học --</option>
                                <c:forEach var="course" items="${fromCourses}">
                                    <option value="${course.id}">${course.name} - ${course.grade}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="studentrequest-form-group" id="toCourseSelectGroup" style="display: none;">
                            <label class="studentrequest-label">Chọn khóa học muốn chuyển tới</label>
                            <select class="studentrequest-select" name="toCourseId" id="toCourseSelect">
                                <option value="">-- Chọn khóa học --</option>
                                <c:forEach var="course" items="${toCourses}">
                                    <option value="${course.id}">${course.name} - ${course.grade}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="studentrequest-form-group">
                            <label class="studentrequest-label">Mô tả chi tiết</label>
                            <textarea class="studentrequest-input studentrequest-textarea" name="description" id="requestDescription" placeholder="Mô tả chi tiết yêu cầu..." required></textarea>
                        </div>

                        <input type="hidden" name="type" id="selectedType" value="">
                        
                        <button type="submit" class="studentrequest-submit-btn" id="submitRequest">
                            📤 Gửi yêu cầu
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div class="studentrequest-list-section">
            <div class="studentrequest-list-header">
                <h2 class="studentrequest-list-title">Danh sách yêu cầu của tôi</h2>
                <div class="studentrequest-search-filter">
                    <div class="studentrequest-search-box">
                        <span class="studentrequest-search-icon">🔍</span>
                        <input type="text" class="studentrequest-search-input" placeholder="Tìm kiếm..." id="searchInput">
                    </div>
                    <select class="studentrequest-filter-select" id="filterType">
                        <option value="">Tất cả loại</option>
                        <option value="STUDENT_ABSENT_REQUEST">Xin nghỉ học</option>
                        <option value="STUDENT_CHANGE_COURSE">Chuyển lớp</option>
                    </select>
                </div>
            </div>

            <div class="studentrequest-list-content" id="requestList">
                <c:choose>
                    <c:when test="${not empty requests}">
                        <c:forEach var="request" items="${requests}">
                            <div class="studentrequest-item" data-type="${request.type}">
                                <div class="studentrequest-item-content">
                                    <div class="studentrequest-item-main">
                                        <div class="studentrequest-item-header">
                                            <h3 class="studentrequest-item-title">
                                                <c:choose>
                                                    <c:when test="${request.type == 'STUDENT_ABSENT_REQUEST'}">
                                                        Xin nghỉ học
                                                    </c:when>
                                                    <c:when test="${request.type == 'STUDENT_CHANGE_COURSE'}">
                                                        Chuyển lớp
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${request.type}
                                                    </c:otherwise>
                                                </c:choose>
                                            </h3>
                                            <span class="studentrequest-status ${request.status}">
                                                <c:choose>
                                                    <c:when test="${request.status == 'pending'}">Chờ duyệt</c:when>
                                                    <c:when test="${request.status == 'accepted'}">Đã duyệt</c:when>
                                                    <c:when test="${request.status == 'rejected'}">Từ chối</c:when>
                                                    <c:otherwise>${request.status}</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <div class="studentrequest-item-meta">
                                            <div class="studentrequest-meta-item">
                                                <span>📅</span>
                                                <span>
                                                    ${request.createdAt}
                                                </span>
                                            </div>
                                            <div class="studentrequest-meta-item">
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
                            <div style="font-size: 3rem; margin-bottom: 1rem;">📝</div>
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
    document.querySelectorAll('.studentrequest-type-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.studentrequest-type-btn').forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
            
            document.getElementById('requestForm').style.display = 'block';
            
            const requestType = this.dataset.type;
            const fromCourseSelectGroup = document.getElementById('fromCourseSelectGroup');
            const toCourseSelectGroup = document.getElementById('toCourseSelectGroup');
            const selectedTypeInput = document.getElementById('selectedType');
            
            // Set type vào hidden input
            selectedTypeInput.value = requestType;
            
            if (requestType === 'STUDENT_CHANGE_COURSE') {
                fromCourseSelectGroup.style.display = 'block';
                toCourseSelectGroup.style.display = 'block';
            } else {
                fromCourseSelectGroup.style.display = 'none';
                toCourseSelectGroup.style.display = 'none';
            }
        });
    });

    // Form validation trước khi submit
    document.getElementById('createRequestForm').addEventListener('submit', function(e) {
        const description = document.getElementById('requestDescription').value;
        const selectedType = document.querySelector('.studentrequest-type-btn.selected');
        const toCourseSelect = document.getElementById('toCourseSelect');
        const fromCourseSelect = document.getElementById('fromCourseSelect');
        
        if (!description || !selectedType) {
            e.preventDefault();
            alert('Vui lòng nhập mô tả và chọn loại yêu cầu!');
            return;
        }

        if (selectedType.dataset.type === 'STUDENT_CHANGE_COURSE' && !fromCourseSelect.value) {
            e.preventDefault();
            alert('Vui lòng chọn khóa học muốn chuyển!');
            return;
        }
        
        if (selectedType.dataset.type === 'STUDENT_CHANGE_COURSE' && !toCourseSelect.value) {
            e.preventDefault();
            alert('Vui lòng chọn khóa học muốn chuyển tới!');
            return;
        }
    });

    // Tìm kiếm theo nội dung
    document.getElementById('searchInput').addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();
        const requestItems = document.querySelectorAll('.studentrequest-item');
        
        requestItems.forEach(item => {
            const title = item.querySelector('.studentrequest-item-title').textContent.toLowerCase();
            const description = item.querySelector('.studentrequest-meta-item:last-child span:last-child').textContent.toLowerCase();
            
            if (title.includes(searchTerm) || description.includes(searchTerm)) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });

    // Lọc theo type
    document.getElementById('filterType').addEventListener('change', function() {
        const selectedType = this.value;
        const requestItems = document.querySelectorAll('.studentrequest-item');
        
        requestItems.forEach(item => {
            const itemType = item.dataset.type;
            
            if (!selectedType || itemType === selectedType) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });

    // Hàm xem chi tiết request
    function viewRequestDetail(requestId) {
        // Có thể mở modal hoặc chuyển trang để xem chi tiết
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
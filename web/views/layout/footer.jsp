<%-- 
    Document   : footer
    Created on : May 29, 2025, 1:52:20 PM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</main>

<style>
    .footer {
        background: #2c3e50;
        color: white;
        padding: 50px 0 20px;
    }

    .footer-content {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 2rem;
        margin-bottom: 2rem;
        max-width: 1200px;
        margin-left: auto;
        margin-right: auto;
        padding: 0 2rem;
    }

    .footer-section h3 {
        margin-bottom: 1rem;
        color: #ecf0f1;
    }

    .footer-section p {
        color: #bdc3c7;
        line-height: 1.6;
    }

    .footer-section ul {
        list-style: none;
        padding: 0;
    }

    .footer-section ul li {
        margin-bottom: 0.5rem;
    }

    .footer-section ul li a {
        color: #bdc3c7;
        text-decoration: none;
        transition: color 0.3s ease;
    }

    .footer-section ul li a:hover {
        color: white;
    }

    .footer-bottom {
        text-align: center;
        padding-top: 2rem;
        border-top: 1px solid #34495e;
        color: #bdc3c7;
        max-width: 1200px;
        margin-left: auto;
        margin-right: auto;
        padding-left: 2rem;
        padding-right: 2rem;
    }

    @media (max-width: 768px) {
        .footer-content {
            grid-template-columns: 1fr;
            text-align: center;
        }
    }
</style>

<footer class="footer">
    <div class="footer-content">
        <div class="footer-section">
            <h3>${centerInfo != null ? centerInfo.centerName : 'EduCenter'}</h3>
            <p>${centerInfo != null ? centerInfo.description : 'Trung tâm dạy thêm uy tín, chất lượng hàng đầu với sứ mệnh nâng cao trình độ học vấn cho mọi học sinh.'}</p>
        </div>
        <div class="footer-section">
            <h3>Khóa Học</h3>
            <ul>
                <li><a href="danh-sach-lop">Toán - Lý - Hóa</a></li>
                <li><a href="danh-sach-lop">Văn - Sử - Địa</a></li>
                <li><a href="danh-sach-lop">Tiếng Anh</a></li>
                <li><a href="danh-sach-lop">Luyện Thi Đại Học</a></li>
            </ul>
        </div>
        <div class="footer-section">
            <h3>Liên Hệ</h3>
            <ul>
                <li>📍 ${centerInfo != null ? centerInfo.address : '123 Đường ABC, Quận XYZ, TP.HCM'}</li>
                <li>📞 ${centerInfo != null ? centerInfo.phone : '0123 456 789'}</li>
                <li>✉️ ${centerInfo != null ? centerInfo.email : 'info@educenter.vn'}</li>
                <li>🌐 ${centerInfo != null ? centerInfo.website : 'www.educenter.vn'}</li>
            </ul>
        </div>
    </div>
    <div class="footer-bottom">
        <p>&copy; 2025 ${centerInfo != null ? centerInfo.centerName : 'EduCenter'}. Tất cả quyền được bảo lưu.</p>
    </div>
</footer>

<c:if test="${not empty pageJS}">
    <script src="${pageContext.request.contextPath}${pageJS}"></script>
</c:if>
</body>
</html>

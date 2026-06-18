<%-- 
    Document   : about.jsp
    Created on : Dec 19, 2024
    Author     : EduCenter Team
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<% request.setAttribute("title", "Giới thiệu - EduCenter");%>
<jsp:include page="layout/header.jsp" />

<style>
    .about-hero {
        background: linear-gradient(135deg, rgba(102,126,234,0.9), rgba(118,75,162,0.9)), url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1000"><defs><pattern id="bg" width="50" height="50" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="2" fill="%23ffffff" opacity="0.1"/></pattern></defs><rect width="100%" height="100%" fill="url(%23bg)"/></svg>');
        padding: 120px 0 80px;
        text-align: center;
        color: white;
        min-height: 60vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        position: relative;
    }

    .about-hero h1 {
        font-size: 3rem;
        margin-bottom: 1rem;
        font-weight: 700;
        text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        animation: fadeInUp 1s ease;
    }

    .about-hero p {
        font-size: 1.2rem;
        margin-bottom: 2rem;
        max-width: 600px;
        margin-left: auto;
        margin-right: auto;
        opacity: 0.95;
        animation: fadeInUp 1s ease 0.2s both;
    }

    .about-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 2rem;
    }

    .about-section {
        padding: 80px 0;
    }

    .about-section:nth-child(even) {
        background: #f8f9ff;
    }

    .section-title {
        text-align: center;
        margin-bottom: 4rem;
    }

    .section-title h2 {
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 1rem;
        position: relative;
    }

    .section-title h2::after {
        content: '';
        width: 60px;
        height: 4px;
        background: linear-gradient(45deg, #ff6b6b, #feca57);
        display: block;
        margin: 1rem auto;
        border-radius: 2px;
    }

    .section-title p {
        font-size: 1.1rem;
        color: #666;
        max-width: 600px;
        margin: 0 auto;
    }

    .about-content {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 4rem;
        align-items: center;
    }

    .about-text h3 {
        font-size: 1.8rem;
        color: #333;
        margin-bottom: 1.5rem;
    }

    .about-text p {
        color: #666;
        line-height: 1.8;
        margin-bottom: 1.5rem;
        font-size: 1.1rem;
    }

    .about-image {
        text-align: center;
    }

    .about-image img {
        max-width: 100%;
        border-radius: 20px;
        box-shadow: 0 20px 40px rgba(0,0,0,0.1);
    }

    .stats-section {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 80px 0;
    }

    .stats-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 2rem;
        text-align: center;
    }

    .stat-item h3 {
        font-size: 2.5rem;
        font-weight: bold;
        margin-bottom: 0.5rem;
    }

    .stat-item p {
        font-size: 1.1rem;
        opacity: 0.9;
    }

    .team-section {
        padding: 80px 0;
    }

    .team-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 2rem;
    }

    .team-card {
        background: white;
        padding: 2rem;
        border-radius: 20px;
        text-align: center;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        transition: all 0.3s ease;
    }

    .team-card:hover {
        transform: translateY(-10px);
        box-shadow: 0 20px 40px rgba(0,0,0,0.15);
    }

    .team-avatar {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        background: linear-gradient(45deg, #667eea, #764ba2);
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 1.5rem;
        font-size: 3rem;
        color: white;
    }

    .team-card h4 {
        font-size: 1.3rem;
        color: #333;
        margin-bottom: 0.5rem;
    }

    .team-card p {
        color: #666;
        font-style: italic;
    }

    .values-section {
        padding: 80px 0;
        background: #f8f9ff;
    }

    .values-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 2rem;
    }

    .value-card {
        background: white;
        padding: 2.5rem;
        border-radius: 20px;
        text-align: center;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        transition: all 0.3s ease;
    }

    .value-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 15px 35px rgba(0,0,0,0.15);
    }

    .value-icon {
        font-size: 3rem;
        margin-bottom: 1.5rem;
        display: block;
        color: #667eea;
    }

    .value-card h4 {
        font-size: 1.5rem;
        color: #333;
        margin-bottom: 1rem;
    }

    .value-card p {
        color: #666;
        line-height: 1.6;
    }

    .cta-section {
        padding: 80px 0;
        text-align: center;
        background: white;
    }

    .cta-section h2 {
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 1rem;
    }

    .cta-section p {
        font-size: 1.2rem;
        color: #666;
        margin-bottom: 2rem;
        max-width: 600px;
        margin-left: auto;
        margin-right: auto;
    }

    .btn {
        padding: 0.8rem 2rem;
        border: none;
        border-radius: 25px;
        cursor: pointer;
        font-weight: 500;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
        font-size: 1.1rem;
    }

    .btn-primary {
        background: linear-gradient(45deg, #ff6b6b, #feca57);
        color: white;
        border: none;
    }

    .btn-primary:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(255,107,107,0.4);
        color: white;
    }

    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    @media (max-width: 768px) {
        .about-content {
            grid-template-columns: 1fr;
            gap: 2rem;
        }
        
        .about-hero h1 {
            font-size: 2.5rem;
        }
        
        .section-title h2 {
            font-size: 2rem;
        }
    }
</style>

<!-- Hero Section -->
<section class="about-hero">
    <div class="about-container">
        <h1>Về EduCenter</h1>
        <p>Hệ thống quản lý giáo dục hiện đại, kết nối học sinh, phụ huynh và giáo viên trong một nền tảng thống nhất</p>
    </div>
</section>

<!-- Giới thiệu chung -->
<section class="about-section">
    <div class="about-container">
        <div class="section-title">
            <h2>Chúng tôi là ai?</h2>
            <p>EduCenter được thành lập với sứ mệnh mang lại trải nghiệm giáo dục tốt nhất cho tất cả học sinh</p>
        </div>
        
        <div class="about-content">
            <div class="about-text">
                <h3>Sứ mệnh của chúng tôi</h3>
                <p>EduCenter cam kết cung cấp một nền tảng giáo dục toàn diện, kết hợp công nghệ hiện đại với phương pháp giảng dạy tiên tiến để tạo ra môi trường học tập lý tưởng cho mọi học sinh.</p>
                
                <p>Chúng tôi tin rằng mỗi học sinh đều có tiềm năng riêng biệt và cần được hỗ trợ phát triển theo cách phù hợp nhất. Với đội ngũ giáo viên giàu kinh nghiệm và hệ thống quản lý thông minh, chúng tôi đảm bảo mọi học sinh đều có cơ hội thành công.</p>
            </div>
            <div class="about-image">
                <img src="https://images.unsplash.com/photo-1523240798131-1133d5b2b734?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80" alt="EduCenter Mission">
            </div>
        </div>
    </div>
</section>

<!-- Thống kê -->
<section class="stats-section">
    <div class="about-container">
        <div class="stats-grid">
            <div class="stat-item">
                <h3>${stats.studentCount != null ? stats.studentCount : '500'}</h3>
                <p>Học sinh đang theo học</p>
            </div>
            <div class="stat-item">
                <h3>${stats.teacherCount != null ? stats.teacherCount : '50'}</h3>
                <p>Giáo viên giàu kinh nghiệm</p>
            </div>
            <div class="stat-item">
                <h3>${stats.courseCount != null ? stats.courseCount : '20'}</h3>
                <p>Khóa học chất lượng</p>
            </div>
            <div class="stat-item">
                <h3>${stats.parentCount != null ? stats.parentCount : '400'}</h3>
                <p>Phụ huynh tin tưởng</p>
            </div>
        </div>
    </div>
</section>

<!-- Giá trị cốt lõi -->
<section class="values-section">
    <div class="about-container">
        <div class="section-title">
            <h2>Giá trị cốt lõi</h2>
            <p>Những nguyên tắc định hướng mọi hoạt động của chúng tôi</p>
        </div>
        
        <div class="values-grid">
            <div class="value-card">
                <span class="value-icon">🎯</span>
                <h4>Chất lượng</h4>
                <p>Cam kết cung cấp chất lượng giáo dục cao nhất với phương pháp giảng dạy hiện đại và hiệu quả.</p>
            </div>
            <div class="value-card">
                <span class="value-icon">🤝</span>
                <h4>Hợp tác</h4>
                <p>Tạo môi trường hợp tác giữa học sinh, phụ huynh và giáo viên để đạt kết quả tốt nhất.</p>
            </div>
            <div class="value-card">
                <span class="value-icon">❤️</span>
                <h4>Tận tâm</h4>
                <p>Đặt lợi ích của học sinh lên hàng đầu, tận tâm hỗ trợ mọi nhu cầu học tập.</p>
            </div>
        </div>
    </div>
</section>

<!-- CTA Section -->
<section class="cta-section">
    <div class="about-container">
        <h2>Sẵn sàng bắt đầu hành trình học tập?</h2>
        <p>Hãy tham gia cùng chúng tôi để khám phá tiềm năng của bạn và đạt được những thành tựu học tập xuất sắc.</p>
        <a href="trang-chu" class="btn btn-primary">Khám phá ngay</a>
    </div>
</section>

<jsp:include page="layout/footer.jsp" /> 
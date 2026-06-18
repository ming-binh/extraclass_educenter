<%-- 
    Document   : header
    Created on : May 29, 2025, 1:51:56 PM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><%= request.getAttribute("title") != null ? request.getAttribute("title") : "Trang web"%></title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <c:if test="${not empty pageCSS}">
            <link rel="stylesheet" href="${pageContext.request.contextPath}${pageCSS}">
        </c:if>

        <style>
            .header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 1rem 0;
                position: fixed;
                width: 100%;
                top: 0;
                z-index: 1000;
                box-shadow: 0 2px 20px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
            }

            .header.scrolled {
                background: rgba(102, 126, 234, 0.95);
                backdrop-filter: blur(10px);
            }

            .nav-container {
                max-width: 1200px;
                margin: 0 auto;
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 0 2rem;
            }

            .logo {
                display: flex;
                align-items: center;
                font-size: 1.5rem;
                font-weight: bold;
                color: white;
                text-decoration: none;
            }

            .logo::before {
                content: "🎓";
                margin-right: 0.5rem;
                font-size: 2rem;
            }

            .logo:hover {
                color: white;
                text-decoration: none;
            }

            .nav-menu {
                display: flex;
                list-style: none;
                gap: 2rem;
                margin: 0;
                padding: 0;
            }

            .nav-menu a {
                color: white;
                text-decoration: none;
                font-weight: 500;
                transition: all 0.3s ease;
                padding: 0.5rem 1rem;
                border-radius: 25px;
            }

            .nav-menu a:hover {
                background: rgba(255,255,255,0.2);
                transform: translateY(-2px);
                color: white;
            }

            .auth-buttons {
                display: flex;
                gap: 1rem;
            }

            .btn {
                padding: 0.7rem 1.5rem;
                border: none;
                border-radius: 25px;
                cursor: pointer;
                font-weight: 500;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-block;
            }

            .btn-outline {
                background: transparent;
                color: white;
                border: 2px solid white;
            }

            .btn-outline:hover {
                background: white;
                color: #667eea;
                transform: translateY(-2px);
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

            /* Dropdown styles */
            .dropdown-menu {
                background: white;
                border: none;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                padding: 0.5rem 0;
            }

            .dropdown-item {
                color: #333 !important;
                padding: 0.7rem 1.5rem;
                transition: all 0.3s ease;
            }

            .dropdown-item:hover {
                background: linear-gradient(45deg, #667eea, #764ba2) !important ;
                color: white;
                transform: translateX(5px);
            }

            .dropdown-divider {
                margin: 0.5rem 0;
                border-color: #e9ecef;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .nav-menu {
                    display: none;
                }

                .auth-buttons {
                    flex-direction: column;
                    gap: 0.5rem;
                }
            }
        </style>
    </head>
    <body>
        <!-- Header -->
        <header class="header">
            <div class="nav-container">
                <a href="trang-chu" class="logo">EduCenter</a>
                <nav>
                    <ul class="nav-menu">
                        <li><a href="trang-chu">Trang Chủ</a></li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                Khóa Học
                            </a>
                            <ul class="dropdown-menu">
                                <c:forEach var="grade" begin="6" end="12">
                                    <li>
                                        <form action="danh-sach-lop" method="post">
                                            <input type="hidden" name="selectedGrade" value="${grade}">
                                            <button type="submit" class="dropdown-item">Lớp ${grade}</button>
                                        </form>
                                    </li>

                                    <!-- Chèn divider sau lớp 9 -->
                                    <c:if test="${grade == 9}">
                                        <li><hr class="dropdown-divider"></li>
                                        </c:if>
                                    </c:forEach>

                            </ul>
                        </li>
                        <li><a href="gioi-thieu">Giới Thiệu</a></li>
                    </ul>
                </nav>
                <div class="auth-buttons">
                    <% if (request.getAttribute("loggedInUserName") != null) {%>
                    <div class="dropdown">
                        <a href="#" class="btn btn-outline dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                            Xin chào, <%= request.getAttribute("loggedInUserName")%>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="userProfile">Trang cá nhân</a></li>
                            <li><hr class="dropdown-divider"></li>
                                <% if (request.getAttribute("loggedInUserRole").equals("student")) {%>
                            <li><a class="dropdown-item" href="yeu-cau-hoc-sinh">Yêu cầu</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="viewScore">Xem điểm</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="studentAssignmentServlet">Bài tập</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="diem-chuyen-can">Điểm danh</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="thoi-gian-bieu-hoc-sinh">Thời gian biểu</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="trang-thanh-toan">Đóng học phí</a></li>
                                <% } else if (request.getAttribute("loggedInUserRole").equals("teacher")) {%>
                            <li><a class="dropdown-item" href="yeu-cau-giao-vien">Yêu cầu</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="classBeingTaught">Khoá học đang dạy </a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="bao-cao-diem-danh"> Báo cáo điểm danh</a></li>
                            <li><a class="dropdown-item" href="thoi-gian-bieu-giao-vien">Thời gian biểu</a></li>
                                <% } else if (request.getAttribute("loggedInUserRole").equals("parent")) {%>
                            <li><a class="dropdown-item" href="yeu-cau-phu-huynh">Yêu cầu</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="viewChild">Xem điểm con</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="diem-chuyen-can">Báo cáo điểm danh của con</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="thoi-gian-bieu-phu-huynh">Thời gian biểu</a></li>
                                <% } %>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="dang-xuat">Đăng xuất</a></li>
                        </ul>
                    </div>
                    <% } else { %>
                    <a href="dang-nhap" class="btn btn-outline">Đăng Nhập</a>
                    <% }%>
                </div>
            </div>
        </header>
        <main style="margin-top: 80px;">  

            <script>
                window.addEventListener('scroll', function () {
                    const header = document.querySelector('.header');
                    if (window.scrollY > 100) {
                        header.classList.add('scrolled');
                    } else {
                        header.classList.remove('scrolled');
                    }
                });
            </script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
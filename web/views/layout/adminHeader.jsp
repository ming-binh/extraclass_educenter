<%-- 
    Document   : header
    Created on : May 29, 2025, 1:51:56 PM
    Author     : Astersa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><%= request.getAttribute("title") != null ? request.getAttribute("title") : "Trang web"%></title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style/style.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/style/adminDashboard.css">
        <c:if test="${not empty pageCSS}">
            <link rel="stylesheet" href="${pageContext.request.contextPath}${pageCSS}">
        </c:if>
        <style>
            .notification-wrapper {
                position: relative;
            }

            .notification-button {
                font-size: 20px;
                color: white;
                position: relative;
                cursor: pointer;
            }

            .notification-button .badge {
                position: absolute;
                top: -6px;
                right: -10px;
                background-color: red;
                color: white;
                font-size: 11px;
                font-weight: bold;
                padding: 2px 6px;
                border-radius: 50%;
            }

            .ttr-sidebar-navi li ul {
                display: none;
            }

            .ttr-sidebar-navi li.open > ul {
                display: block;
            }

            /* Dropdown */
            .notification-dropdown {
                display: none;
                position: absolute;
                right: 0;
                top: 35px;
                width: 320px;
                background-color: white;
                border-radius: 6px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
                z-index: 999;
                overflow-y: auto;
                max-height: 400px;
                padding: 10px 0;
            }

            .notification-item {
                padding: 10px 15px;
                border-bottom: 1px solid #f0f0f0;
                font-size: 14px;
                color: #333;
                position: relative;
                background-color: #fff;
            }

            .notification-item.unread {
                background-color: #f7f9ff;
                font-weight: bold;
            }

            .notification-item.read {
                background-color: #fff;
            }

            .notification-item small {
                display: block;
                font-size: 11px;
                color: #999;
                margin-top: 4px;
            }

            /* Action buttons */
            .notification-item .mark-read-btn,
            .notification-item .delete-btn {
                background: none;
                border: none;
                cursor: pointer;
                font-size: 13px;
                color: #888;
                margin-left: 5px;
            }

            .notification-item .delete-btn:hover,
            .notification-item .mark-read-btn:hover {
                color: red;
            }

            /* Mark all button */
            .mark-all {
                width: 100%;
                padding: 10px;
                border: none;
                background-color: #007bff;
                color: white;
                font-weight: bold;
                cursor: pointer;
                border-radius: 0 0 6px 6px;
            }

            .mark-all:hover {
                background-color: #0056b3;
            }
            .ttr-header-submenu {
                position: absolute;
                top: 40px;
                right: 0;
                background: white;
                border-radius: 4px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                display: none; /* Sẽ bật bằng JS */
                min-width: 150px;
                z-index: 999;
            }
            .ttr-header-submenu ul {
                list-style: none;
                margin: 0;
                padding: 0;
            }

            .ttr-header-submenu ul li a {
                display: block;
                padding: 10px;
                color: #333;
                text-decoration: none;
            }

            .ttr-header-submenu ul li a:hover {
                background-color: #f0f0f0;
            }
        </style>

    </head>
    <body class="ttr-opened-sidebar ttr-pinned-sidebar">
        <header class="ttr-header">
            <div class="ttr-header-wrapper">
                <!-- Sidebar toggle -->
                <div class="ttr-toggle-sidebar ttr-material-button">
                    <i class="fa fa-bars ttr-open-icon"></i>
                    <i class="fa fa-times ttr-close-icon"></i>
                </div>

                <!-- Logo -->
                <div class="ttr-logo-box">
                    <a href="bang-dieu-khien" class="ttr-logo">EDUCENTER</a>
                </div>

                <!-- Menu trái -->
                <div class="ttr-header-menu">
                    <ul class="ttr-header-navigation">
                        <li><a href="trang-chu" class="ttr-material-button ttr-submenu-toggle">TRANG CHỦ</a></li>
                    </ul>
                </div>

                <!-- Header phải -->
                <div class="ttr-header-right">
                    <ul class="ttr-header-navigation">
                        <!-- Search -->
                        <li class="search-wrapper">
                            <form class="ttr-search-form" action="search" method="get">
                                <input 
                                    type="text" 
                                    name="query" 
                                    placeholder="Tìm kiếm..." 
                                    class="ttr-search-input"
                                    value="${param.query != null ? param.query : ''}" />
                                <button type="submit" class="ttr-search-submit">
                                    <i class="ti-search"></i>
                                </button>
                            </form>
                        </li>

                        <!-- Notification -->
                        <li class="notification-wrapper">
                            <a href="#" class="ttr-material-button notification-button">
                                <span class="material-symbols-outlined">notifications</span>
                                <c:if test="${unreadCount > 0}">
                                    <span class="badge">${unreadCount}</span>
                                </c:if>
                            </a>
                            <div class="notification-dropdown">
                                <c:forEach var="n" items="${notifications}">
                                    <div class="notification-item ${n.isRead ? 'read' : 'unread'}">
                                        <p>${n.description}</p>
                                        <small>${n.createdAt}</small>
                                        <form action="notifications" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="markRead" />
                                            <input type="hidden" name="notificationId" value="${n.id}" />
                                            <button type="submit" class="mark-read-btn">✓</button>
                                        </form>
                                        <c:if test="${sessionScope.role == 'admin'}">
                                            <a href="notifications?action=delete&id=${n.id}" class="delete-btn">🗑</a>
                                        </c:if>
                                    </div>
                                </c:forEach>
                                <form action="notifications?action=markAllRead" method="get">
                                    <button type="submit" class="mark-all">Đánh dấu tất cả đã đọc</button>
                                </form>
                            </div>
                        </li>

                        <li>
                            <a href="dang-xuat" class="ttr-material-button ttr-submenu-toggle">
                                <i class="fa-solid fa-arrow-right-from-bracket"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </header>

        <div class="ttr-sidebar">
            <div class="ttr-sidebar-wrapper content-scroll">
                <div class="ttr-sidebar-logo">
                    <a href="#" class="ttr-logo" style="color: #000 !important;"> EduCenter </a>
                    <div class="ttr-sidebar-toggle-button">
                        <i class="fa fa-arrow-left"></i>
                    </div>
                </div>
                <nav class="ttr-sidebar-navi">
                    <ul>
                        <c:choose>
                            <c:when test="${loggedInUserRole == 'manager'}">
                                <li>
                                    <a href="bang-dieu-khien" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-home"></i></span>
                                        <span class="ttr-label">Bảng điều khiển</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="quan-ly-khoa-hoc" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-book"></i></span>
                                        <span class="ttr-label">Quản lý khoá học </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="quan-ly-lop-hoc" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-chalkboard"></i></span>
                                        <span class="ttr-label">Quản lý lớp học</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="quan-ly-lich-hoc" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fas fa-calendar-alt"></i></span>
                                        <span class="ttr-label">Quản lý lịch học </span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fas fa-users"></i></span>
                                        <span class="ttr-label">Quản lý tài khoản </span>
                                        <span class="ttr-arrow-icon"><i class="fa fa-angle-down"></i></span>
                                    </a>
                                    <ul>
                                        <li>
                                            <a href="quan-ly-tai-khoan" class="ttr-material-button">
                                                <span class="ttr-icon"><i class="fas fa-users"></i></span>
                                                <span class="ttr-label">Quản lý tài khoản </span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="duyet-tai-khoan" class="ttr-material-button">
                                                <span class="ttr-icon"><i class="fas fa-user-plus"></i></span>
                                                <span class="ttr-label">Yêu cầu mở tài khoản </span>
                                            </a>
                                        </li>
                                    </ul>
                                </li>

                                <li>
                                    <a href="javascript:void(0);" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-clipboard-check"></i></span>
                                        <span class="ttr-label">Xử lý yêu cầu</span>
                                        <span class="ttr-arrow-icon"><i class="fa fa-angle-down"></i></span>
                                    </a>
                                    <ul>
                                        <li>
                                            <a href="quan-ly-tham-gia-lop-hoc" class="ttr-material-button">
                                                <span class="ttr-icon"><i class="fa fa-user-plus"></i></span>
                                                <span class="ttr-label">Tham gia lớp học</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#" class="ttr-material-button">
                                                <span class="ttr-icon"><i class="fa fa-envelope-open-text"></i></span>
                                                <span class="ttr-label">Yêu cầu khác</span>
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="userProfile" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-address-card"></i></span>
                                        <span class="ttr-label">Hồ sơ</span>
                                    </a>
                                </li>
                            </c:when>
                            <c:when test="${loggedInUserRole == 'admin'}">
                                <li>
                                    <a href="bang-dieu-khien" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-home"></i></span>
                                        <span class="ttr-label">Bảng điều khiển</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:void(0);" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fas fa-users"></i></span>
                                        <span class="ttr-label">Quản lý tài khoản </span>
                                        <span class="ttr-arrow-icon"><i class="fa fa-angle-down"></i></span>
                                    </a>
                                    <ul>
                                        <li>
                                            <a href="quan-ly-tai-khoan" class="ttr-material-button">
                                                <span class="ttr-icon"><i class="fas fa-users"></i></span>
                                                <span class="ttr-label">Quản lý tài khoản </span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="duyet-tai-khoan" class="ttr-material-button">
                                                <span class="ttr-icon"><i class="fas fa-user-plus"></i></span>
                                                <span class="ttr-label">Yêu cầu mở tài khoản </span>
                                            </a>
                                        </li>
                                    </ul>
                                <li>
                                    <a href="userProfile" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-address-card"></i></span>
                                        <span class="ttr-label">Hồ sơ</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="admin-cau-hinh-he-thong" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-cog"></i></span>
                                        <span class="ttr-label">Cấu hình</span>
                                    </a>
                                </li>
                            </c:when>
                            <c:when test="${loggedInUserRole == 'staff'}">
                                <li>
                                    <a href="quan-ly-tu-van" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-user-circle"></i></span>
                                        <span class="ttr-label">Quản lý tư vấn và tuyển dụng</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="xu-ly-yeu-cau" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-address-card"></i></span>
                                        <span class="ttr-label">Xử lý yêu cầu</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="trang-xac-nhan-thanh-toan" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-credit-card-alt"></i></span>
                                        <span class="ttr-label">Xác nhận thanh toán</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="userProfile" class="ttr-material-button">
                                        <span class="ttr-icon"><i class="fa fa-address-card"></i></span>
                                        <span class="ttr-label">Hồ sơ</span>
                                    </a>
                                </li>
                            </c:when>
                            <c:when test="${loggedInUserRole == null}">
                            </c:when>
                        </c:choose>
                        <li class="ttr-seperate"></li>
                    </ul>
                </nav>
            </div>
        </div>
        <main class="ttr-wrapper">
            <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    // Get all necessary elements
                    const body = document.body;
                    const toggleBtn = document.querySelector('.ttr-toggle-sidebar');
                    const sidebarToggleBtn = document.querySelector('.ttr-sidebar-toggle-button');
                    const notifyBtn = document.querySelector(".notification-button");
                    const dropdown = document.querySelector(".notification-dropdown");
                    const avatarBtn = document.querySelector(".ttr-user-avatar").parentElement;
                    const avatarDropdown = document.querySelector(".ttr-header-submenu");
                    // Function to toggle sidebar
                    function toggleSidebar() {
                        body.classList.toggle('ttr-opened-sidebar');
                        body.classList.toggle('ttr-pinned-sidebar');
                    }
                    // Toggle dropdown
                    notifyBtn.addEventListener("click", function (e) {
                        e.preventDefault();
                        dropdown.style.display = (dropdown.style.display === "block") ? "none" : "block";
                    });
                    // Ẩn dropdown nếu click bên ngoài
                    document.addEventListener("click", function (e) {
                        if (!notifyBtn.contains(e.target) && !dropdown.contains(e.target)) {
                            dropdown.style.display = "none";
                        }
                    });
                    avatarBtn.addEventListener("click", function (e) {
                        e.preventDefault();
                        avatarDropdown.style.display = avatarDropdown.style.display === "block" ? "none" : "block";

                    });
                    document.addEventListener("click", function (e) {
                        if (!avatarBtn.contains(e.target) && !avatarDropdown.contains(e.target)) {
                            avatarDropdown.style.display = "none";
                        }
                    });
                    // Add click event listeners
                    if (toggleBtn) {
                        toggleBtn.addEventListener('click', toggleSidebar);
                    }
                    if (sidebarToggleBtn) {
                        sidebarToggleBtn.addEventListener('click', toggleSidebar);
                    }
                    // Initialize sidebar state
                    body.classList.add('ttr-opened-sidebar');
                    body.classList.add('ttr-pinned-sidebar');
                    // Handle window resize
                    let resizeTimer;
                    window.addEventListener('resize', function () {
                        clearTimeout(resizeTimer);
                        resizeTimer = setTimeout(function () {
                            if (window.innerWidth < 992) {
                                body.classList.remove('ttr-opened-sidebar');
                                body.classList.remove('ttr-pinned-sidebar');
                            } else {
                                body.classList.add('ttr-opened-sidebar');
                                body.classList.add('ttr-pinned-sidebar');
                            }
                        }, 250);
                    });
                });
                document.querySelectorAll('.ttr-sidebar-navi > ul > li > a.ttr-material-button').forEach(function (button) {
                    button.addEventListener('click', function (e) {
                        const parentLi = this.parentElement;
                        if (parentLi.querySelector('ul')) {
                            e.preventDefault();
                            parentLi.classList.toggle('open');
                        }
                    });
                });
            </script>

<%-- 
    Document   : create-staff-manager-account
    Created on : Jul 16, 2025, 12:55:42 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Tạo tài khoản nhân sự</title>
        <style>
            body {
                font-family: Arial;
                background: #f5f6fa;
                padding: 30px;
            }

            .container {
                background: white;
                max-width: 600px;
                margin: auto;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }

            h2 {
                text-align: center;
                color: #2c3e50;
                margin-bottom: 20px;
            }

            .form-group {
                margin-bottom: 18px;
            }

            label {
                display: block;
                font-weight: bold;
                margin-bottom: 6px;
            }

            input {
                width: 100%;
                padding: 10px;
                border-radius: 8px;
                border: 1px solid #ccc;
            }

            .submit-btn {
                width: 100%;
                padding: 14px;
                font-weight: bold;
                background-color: #4a69bd;
                color: white;
                border: none;
                border-radius: 10px;
                cursor: pointer;
                margin-top: 20px;
            }

            .submit-btn:hover {
                background-color: #3b5b99;
            }

            .error {
                color: red;
                font-weight: bold;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Tạo tài khoản 
                <c:choose>
                    <c:when test="${targetRole == 'manager'}">Quản lý</c:when>
                    <c:when test="${targetRole == 'staff'}">Nhân viên</c:when>
                    <c:otherwise>Không xác định</c:otherwise>
                </c:choose>
            </h2>

            <form method="post" action="tao-tai-khoan-nhan-su">
                <input type="hidden" name="role" value="${targetRole}" />

                <div class="form-group">
                    <label>Họ tên:</label>
                    <input type="text" name="name" required />
                </div>

                <div class="form-group">
                    <label>Tên đăng nhập:</label>
                    <input type="text" name="username" required autocomplete="new-username"/>
                </div>

                <div class="form-group">
                    <label>Mật khẩu:</label>
                    <input type="password" name="password" required autocomplete="new-password"/>
                </div>

                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <input type="text" name="phone" />
                </div>

                <div class="form-group">
                    <label>Địa chỉ:</label>
                    <input type="text" name="address" required/>
                </div>

                <div class="form-group">
                    <label>Ngày sinh:</label>
                    <input type="date" name="dob" required/>
                </div>

                <button type="submit" class="submit-btn">Tạo tài khoản</button>
            </form>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
        </div>
    </body>
</html>


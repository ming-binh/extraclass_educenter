<%-- 
    Document   : changepw
    Created on : Jun 25, 2025, 2:51:13 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <jsp:include page="layout/header.jsp" />
    <head>
        <title>Đổi mật khẩu</title>
        <style>
            body {
                margin: 0;
                font-family: 'Segoe UI', sans-serif;
                background-color: #f4f4f4;
                height: 100vh;
                display: flex;
                flex-direction: column;
            }

            .main-container {
                display: flex;
                flex: 1;
            }

            .left-panel {
                width: 40%;
                background: linear-gradient(135deg, #6a11cb, #2575fc);
                color: white;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                text-align: center;
                padding: 60px;
            }

            .left-panel h1 {
                font-size: 40px;
                margin-bottom: 15px;
            }

            .left-panel p {
                font-size: 18px;
                line-height: 1.6;
            }

            .right-panel {
                width: 60%;
                height:100vh;
                background: white;
                padding: 60px 80px;
                display: flex;
                flex-direction: column;
                justify-content: center;
                box-shadow: -5px 0 15px rgba(0, 0, 0, 0.1);
            }

            .right-panel h2 {
                font-size: 30px;
                margin-bottom: 30px;
                color: #333;
                position: relative;
                padding-left: 15px;
            }

            .right-panel h2::before {
                content: '';
                position: absolute;
                left: 0;
                top: 50%;
                transform: translateY(-50%);
                width: 6px;
                height: 70%;
                background-color: #f4a100;
                border-radius: 3px;
            }

            form {
                display: flex;
                flex-direction: column;
            }

            form label {
                margin: 10px 0 5px;
                font-size: 14px;
                color: #555;
            }

            form input[type="password"] {
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
            }

            form input[type="submit"] {
                margin-top: 25px;
                background-color: #f4a100;
                color: white;
                border: none;
                padding: 12px;
                font-size: 16px;
                cursor: pointer;
                border-radius: 5px;
                transition: background-color 0.3s ease;
            }

            form input[type="submit"]:hover {
                background-color: #d99100;
            }

            .message,
            .error {
                margin-top: 20px;
                font-weight: bold;
                text-align: center;
                font-size: 14px;
            }

            .message {
                color: green;
            }

            .error {
                color: red;
            }
        </style>
    </head>
    <body>
        <div class="main-container">
            <div class="left-panel">
                <h1>Đổi mật khẩu</h1>
                <p>Giữ tài khoản của bạn an toàn bằng cách thường xuyên cập nhật mật khẩu.</p>
            </div>

            <div class="right-panel">
                <h2>Đổi mật khẩu</h2>
                <form action="doi-mat-khau" method="post">
                    <label>Mật khẩu cũ:</label>
                    <input type="password" name="oldPassword" required>

                    <label>Mật khẩu mới:</label>
                    <input type="password" name="newPassword" required>

                    <label>Nhập lại mật khẩu mới:</label>
                    <input type="password" name="confirmPassword" required>

                    <input type="submit" value="Đổi mật khẩu">
                </form>

                <% if (request.getAttribute("error") != null) {%>
                <div class="error"><%= request.getAttribute("error")%></div>
                <% } else if (request.getAttribute("message") != null) {%>
                <div class="message"><%= request.getAttribute("message")%></div>
                <% }%>
            </div>
        </div>
    </body>
</html>
<jsp:include page="layout/footer.jsp" /> 
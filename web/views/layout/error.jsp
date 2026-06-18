<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lỗi Ứng dụng</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px; }
        .error-box { background-color: #fff; border: 1px solid #cc0000; padding: 20px; margin: 0 auto; max-width: 800px; }
        h1 { color: #cc0000; }
        pre { background-color: #eee; padding: 15px; overflow-x: auto; }
    </style>
</head>
<body>
    <div class="error-box">
        <h1>Đã xảy ra lỗi trong ứng dụng</h1>
        <p>Vui lòng xem chi tiết lỗi dưới đây:</p>
        <pre>
<%= request.getAttribute("errorLog") != null ? request.getAttribute("errorLog") : "Không có thông tin lỗi." %>
        </pre>
        <a href="<%= request.getContextPath() %>/trang-chu">Quay lại trang đăng nhập</a>
    </div>
</body>
</html>

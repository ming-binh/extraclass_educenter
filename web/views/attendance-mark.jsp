<%-- 
    Document   : attendance_mark
    Created on : Jun 26, 2025, 1:11:17 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Điểm danh lớp: ${course.name} - Buổi: ${section.dateTime}</h2>
        <form action="attendance" method="post">
            <input type="hidden" name="sectionId" value="${section.id}" />
            <table border="1">
                <tr>
                    <th>STT</th><th>Họ tên</th><th>Đã thanh toán</th><th>Đi học</th>
                </tr>
                <c:forEach var="s" items="${students}" varStatus="i">
                    <tr>
                        <td>${i.index + 1}</td>
                        <td>${s.name}</td>
                        <td>${s.paid ? "✔" : "✘"}</td>
                        <td>
                            <input type="checkbox" name="attendance" value="${s.id}" 
                                   <c:if test="${s.attended}">checked</c:if> />
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <button type="submit">Lưu điểm danh</button>
        </form>

    </body>
</html>

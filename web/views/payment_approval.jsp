<%@ page import="java.util.*, modal.*, java.text.SimpleDateFormat, java.text.NumberFormat, java.util.Locale, utils.CurrencyFormatter, utils.DateFormat, java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="layout/adminHeader.jsp" />
<%
    List<Map<String, Object>> pendingSchedules = (List<Map<String, Object>>) request.getAttribute("pendingSchedules");
    int pageSize = 10;
    int totalRecords = pendingSchedules != null ? pendingSchedules.size() : 0;
    int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
    int currentPage = 1;
    if (request.getParameter("page") != null) {
        try { currentPage = Integer.parseInt(request.getParameter("page")); } catch (Exception e) { currentPage = 1; }
    }
    int startIdx = (currentPage - 1) * pageSize;
    int endIdx = Math.min(startIdx + pageSize, totalRecords);
%>
<html>
<head>
    <title>Duyệt thanh toán học phí</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f7f7f7; }
        h2 { color: #2c3e50; margin-top: 30px; }
        table { border-collapse: collapse; width: 95%; margin: 30px auto; background: #fff; box-shadow: 0 2px 8px #ccc; border-radius: 10px; overflow: hidden; }
        th, td { padding: 12px 15px; text-align: center; }
        th { background: #2980b9; color: #fff; font-size: 16px; }
        tr:nth-child(even) { background: #f2f2f2; }
        tr:hover { background: #eaf6fb; }
        button { background: #27ae60; color: #fff; border: none; padding: 8px 18px; border-radius: 5px; font-size: 15px; cursor: pointer; transition: background 0.2s; }
        button:hover { background: #219150; }
        .approved { color: #27ae60; font-weight: bold; }
        .pagination { text-align: center; margin: 25px 0; }
        .pagination a, .pagination span.current {
            display: inline-block; padding: 8px 14px; margin: 0 2px;
            border-radius: 4px; background: #eee; color: #2980b9; text-decoration: none; font-weight: bold;
        }
        .pagination span.current { background: #2980b9; color: #fff; }
        .pagination a:hover { background: #27ae60; color: #fff; }
    </style>
</head>
<body>
<%
    String msg = request.getParameter("msg");
    String studentId = null, sectionId = null, alertCourseId = null;
    if ("done".equals(msg)) {
        studentId = request.getParameter("studentId");
        sectionId = request.getParameter("sectionId");
        alertCourseId = request.getParameter("courseId");
    }
%>
<% if ("done".equals(msg) && studentId != null && sectionId != null && alertCourseId != null) { %>
<script>
    alert('Học sinh <%=studentId%> đã đóng học buổi <%=sectionId%> lớp <%=alertCourseId%>');
</script>
<% } %>
    <h2>Duyệt thanh toán học phí</h2>
    <table border="1">
        <tr>
            <th>Học sinh</th>
            <th>Khóa học</th>
            <th>Buổi học</th>
            <th>Số tiền</th>
            <th>Ngày đến hạn</th>
            <th>Hành động</th>
        </tr>
        <% if (pendingSchedules != null && !pendingSchedules.isEmpty()) {
            for (int i = startIdx; i < endIdx; i++) {
                Map<String, Object> row = pendingSchedules.get(i); %>
            <tr>
                <td>
    <%=row.get("studentName")%>
    <% if ((Boolean)row.get("markPaying")) { %>
        <span class="payment-tag" style="background: #ffd700; padding: 2px 6px; border-radius: 4px; font-size: 12px; margin-left: 5px;">
            Đã chuyển khoản
        </span>
    <% } %>
</td>
                <td><%=row.get("courseName")%></td>
                <td>
                <%
                    Integer studentSectionId = (row.get("studentSectionId") != null) ? ((Number)row.get("studentSectionId")).intValue() : null;
                    Integer courseId = (row.get("courseId") != null) ? ((Number)row.get("courseId")).intValue() : null;
                    if (studentSectionId == null && courseId != null) {
                    } else {
                        java.sql.Timestamp ts = (java.sql.Timestamp) row.get("dateTime");
                        String dateStr = (ts != null) ? utils.DateFormat.formatDate(ts.toLocalDateTime()) : "--";
                        out.print(dateStr);
                    }
                %>
                </td>
                <td>
                <%
                    String amountStr = utils.CurrencyFormatter.formatCurrency((java.math.BigDecimal)row.get("amount"));
                    amountStr = amountStr.replace("₫", "VNĐ");
                %>
                <%= amountStr %></td>
                <td>
                <%
                    java.sql.Timestamp ts1 = (java.sql.Timestamp) row.get("dueDate");
                    String dueDateStr = (ts1 != null) ? utils.DateFormat.formatDate(ts1.toLocalDateTime()) : "--";
                %>
                    <%= dueDateStr %>
                </td>
                <td>
                    <% if (!(Boolean)row.get("isPaid")) { %>
                        <form action="trang-xac-nhan-thanh-toan" method="post" style="display:inline;">
                            <input type="hidden" name="studentId" value="<%=row.get("studentId")%>"/>
                            <input type="hidden" name="sectionId" value="<%=row.get("sectionId")%>"/>
                            <input type="hidden" name="courseId" value="<%=row.get("courseId")%>"/>
                            <button type="submit" name="action" value="approve">Xác nhận</button>
                        </form>
                    <% } else { %>
                        <span class="approved">Đã duyệt</span>
                    <% } %>
                </td>
            </tr>
        <%   }
           } else { %>
            <tr><td colspan="7">Không có dữ liệu cần duyệt</td></tr>
        <% } %>
    </table>
    <div class="pagination">
        <% if (totalPages > 1) { %>
            <% if (currentPage > 1) { %>
                <a href="trang-xac-nhan-thanh-toan?page=<%=currentPage-1%>" >&laquo; Trước</a>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
                <% if (i == currentPage) { %>
                    <span class="current"><%=i%></span>
                <% } else { %>
                    <a href="trang-xac-nhan-thanh-toan?page=<%=i%>"><%=i%></a>
                <% } %>
            <% } %>
            <% if (currentPage < totalPages) { %>
                <a href="trang-xac-nhan-thanh-toan?page=<%=currentPage+1%>">Sau &raquo;</a>
            <% } %>
        <% } %>
    </div>
</body>
</html>

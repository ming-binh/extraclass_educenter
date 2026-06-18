<%@ page import="java.util.*, modal.*, java.text.SimpleDateFormat, java.text.NumberFormat, java.util.Locale, utils.CurrencyFormatter, utils.DateFormat, java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="layout/header.jsp" />
<%
    Integer studentId = (Integer) request.getAttribute("studentId");
    Integer courseId = (Integer) request.getAttribute("courseId");
    Integer sectionId = (Integer) request.getAttribute("sectionId");
    List<CourseModal> dailyCourses = (List<CourseModal>) request.getAttribute("dailyCourses");
    List<Map<String, Object>> sections = (List<Map<String, Object>>) request.getAttribute("sections");
    PaymentInfoModal paymentInfo = (PaymentInfoModal) request.getAttribute("paymentInfo");
    String transferContent = (String) request.getAttribute("transferContent");
    java.math.BigDecimal amount = (java.math.BigDecimal) request.getAttribute("amount");
    Boolean isCombo = (Boolean) request.getAttribute("isCombo");
    Boolean isPending = (Boolean) request.getAttribute("isPending");
    String msg = (String) request.getAttribute("msg");
    String qrCodeBase64 = (String) request.getAttribute("qrCodeBase64");
%>
<html>
    <head>
        <title>Thanh toán học phí</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f7f7f7;
            }
            h2 {
                color: #2c3e50;
                margin-top: 30px;
                text-align: center;
            }
            .container {
                max-width: 1000px;
                margin: 0 auto;
                padding: 20px;
            }
            .payment-container {
                display: flex;
                gap: 30px;
                margin-top: 30px;
            }
            .form-section {
                flex: 1;
            }
            .qr-section {
                flex: 1;
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 2px 8px #ccc;
                text-align: center;
            }
            form {
                background: #fff;
                padding: 30px 40px;
                border-radius: 10px;
                box-shadow: 0 2px 8px #ccc;
            }
            label {
                display: block;
                margin-top: 15px;
                font-weight: bold;
                color: #34495e;
            }
            select, input[type="text"], input[type="hidden"] {
                width: 100%;
                padding: 8px;
                margin-top: 5px;
                border-radius: 5px;
                border: 1px solid #ccc;
            }
            button {
                background: #27ae60;
                color: #fff;
                border: none;
                padding: 12px 25px;
                border-radius: 5px;
                font-size: 16px;
                margin-top: 20px;
                cursor: pointer;
                transition: background 0.2s;
                width: 100%;
            }
            button:hover {
                background: #219150;
            }
            h3 {
                color: #2980b9;
                margin-top: 25px;
            }
            p {
                margin: 8px 0;
            }
            b {
                color: #e67e22;
            }
            .alert-success {
                background: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
                padding: 15px;
                border-radius: 5px;
                margin: 20px auto;
                max-width: 800px;
                text-align: center;
                font-size: 16px;
            }
            .qr-code {
                max-width: 250px;
                height: auto;
                border: 1px solid #ddd;
                border-radius: 8px;
                margin: 20px 0;
            }
            .qr-title {
                color: #2c3e50;
                font-size: 18px;
                font-weight: bold;
                margin-bottom: 15px;
            }
            .qr-info {
                text-align: left;
                margin-top: 20px;
            }
            .qr-info p {
                background: #f8f9fa;
                padding: 10px;
                border-radius: 5px;
                margin: 5px 0;
            }
            .amount-highlight {
                background: #fff3cd;
                border: 2px solid #ffc107;
                padding: 15px;
                border-radius: 8px;
                margin: 15px 0;
                font-size: 18px;
                font-weight: bold;
                color: #856404;
            }
            .instruction {
                background: #e7f3ff;
                border-left: 4px solid #2196F3;
                padding: 15px;
                margin: 20px 0;
                border-radius: 4px;
            }
            .steps {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                margin: 20px 0;
            }
            .steps ol {
                margin: 0;
                padding-left: 20px;
            }
            .steps li {
                margin: 8px 0;
                line-height: 1.5;
            }
            @media (max-width: 768px) {
                .payment-container {
                    flex-direction: column;
                }
                .container {
                    padding: 10px;
                }
            }
            button[disabled] {
                background-color: #cccccc !important;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Thanh toán học phí</h2>

            <% if ("success".equals(msg)) { %>
            <div class="alert-success">Xác nhận thanh toán thành công! Vui lòng chờ duyệt.</div>
            <% }%>

            <div class="payment-container">
                <div class="form-section">
                    <form action="trang-thanh-toan" method="get" id="selectForm">
                        <input type="hidden" name="studentId" value="<%=studentId%>"/>
                        <label>Chọn khóa học:</label>
                        <select name="courseId" id="courseId" onchange="document.getElementById('selectForm').submit()">
                            <option value="">--Chọn--</option>
                            <% for (CourseModal c : dailyCourses) {%>
                            <option value="<%=c.getId()%>" <%= (c.getId().equals(courseId)) ? "selected" : ""%>><%=c.getName()%></option>
                            <% } %>
                        </select>

                        <% if (isCombo == null || !isCombo) {%>
                        <label>Chọn buổi học:</label>
                        <select name="sectionId" id="sectionId" <%= (courseId == null) ? "disabled" : ""%> onchange="document.getElementById('selectForm').submit()">
                            <option value="">--Chọn--</option>
                            <% for (Map<String, Object> s : sections) {%>
                            <option value="<%=s.get("id")%>" <%= (s.get("id").equals(sectionId)) ? "selected" : ""%>>
                                <%
                                    java.sql.Timestamp ts = (java.sql.Timestamp) s.get("dateTime");
                                    String dateStr = (ts != null) ? utils.DateFormat.formatDate(ts.toLocalDateTime()) : "--";
                                    String amountStr = utils.CurrencyFormatter.formatCurrency((java.math.BigDecimal) s.get("amount"));
                                    amountStr = amountStr.replace("₫", "VNĐ");
                                %>
                                <%= dateStr%> - Số tiền: <%= amountStr%>
                            </option>
                            <% } %>
                        </select>
                        <% } %>
                    </form>

                    <% if (amount != null && qrCodeBase64 != null) {%>
                    <form action="trang-thanh-toan" method="post">
                        <input type="hidden" name="studentId" value="<%=studentId%>"/>
                        <input type="hidden" name="courseId" value="<%=courseId%>"/>
                        <% if (isCombo == null || !isCombo) {%>
                        <input type="hidden" name="sectionId" value="<%=sectionId%>"/>
                        <% }%>
                        <input type="hidden" name="transferContent" value="<%=transferContent%>"/>

                        <h3>Thông tin chuyển khoản</h3>
                        <p>Ngân hàng: <%=paymentInfo != null ? paymentInfo.getBankName() : ""%></p>
                        <p>Số tài khoản: <%=paymentInfo != null ? paymentInfo.getAccountNumber() : ""%></p>
                        <p>Chủ tài khoản: <%=paymentInfo != null ? paymentInfo.getAccountName() : ""%></p>
                        <p>Chi nhánh: <%=paymentInfo != null ? paymentInfo.getBranch() : ""%></p>

                        <div class="amount-highlight">
                            Số tiền cần thanh toán: 
                            <%
                                String payAmountStr = (amount != null) ? utils.CurrencyFormatter.formatCurrency(amount) : "--";
                                payAmountStr = payAmountStr.replace("₫", "VNĐ");
                            %>
                            <%= payAmountStr%>
                        </div>

                        <p>Nội dung chuyển khoản: <b><%=transferContent%></b></p>

                        <div class="instruction">
                            <strong>Lưu ý:</strong> Vui lòng chuyển khoản đúng số tiền và nội dung để được xử lý nhanh chóng.
                        </div>

                         <button type="submit" class="btn btn-primary" 
                                <% if (isPending) { %>
                                disabled="disabled"
                                <% } %>
                                >
                            <% if (isPending) { %>
                            Đã thanh toán chờ xác nhận
                            <% } else { %>
                            Xác nhận đã chuyển khoản
                            <% } %>
                        </button>
                    </form>
                    <% } %>
                </div>

                <% if (qrCodeBase64 != null && amount != null) {%>
                <div class="qr-section">
                    <div class="qr-title">Quét mã QR để thanh toán</div>
                    <img src="data:image/png;base64,<%=qrCodeBase64%>" alt="QR Code thanh toán" class="qr-code"/>

                    <div class="steps">
                        <strong>Cách thanh toán:</strong>
                        <ol>
                            <li>Mở app ngân hàng trên điện thoại</li>
                            <li>Chọn "Quét QR" hoặc "Chuyển khoản QR"</li>
                            <li>Quét mã QR bên trên</li>
                            <li>Kiểm tra thông tin và xác nhận chuyển khoản</li>
                            <li>Nhấn "Tôi đã chuyển khoản" sau khi hoàn tất</li>
                        </ol>
                    </div>

                    <div class="qr-info">
                        <p><strong>Ngân hàng:</strong> <%=paymentInfo != null ? paymentInfo.getBankName() : ""%></p>
                        <p><strong>Số tài khoản:</strong> <%=paymentInfo != null ? paymentInfo.getAccountNumber() : ""%></p>
                        <p><strong>Số tiền:</strong> 
                            <%
                                String qrAmountStr = (amount != null) ? utils.CurrencyFormatter.formatCurrency(amount) : "--";
                                qrAmountStr = qrAmountStr.replace("₫", "VNĐ");
                            %>
                            <%= qrAmountStr%>
                        </p>
                        <p><strong>Nội dung:</strong> <%=transferContent%></p>
                    </div>
                </div>
                <% }%>
            </div>
        </div>

    </body>
</html>
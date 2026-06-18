<%-- 
    Document   : create-student-account
    Created on : Jul 14, 2025, 11:13:38 PM
    Author     : ASUS
--%>

<%@page import="modal.SchoolModal"%>
<%@page import="java.util.Map"%>
<%@page import="modal.SchoolClass"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/header.jsp" />
<html>
    <head>
        <title>Tạo tài khoản học sinh</title>
        <style>
            body {
                font-family: Arial;
                background: #f5f6fa;
            }

            .container {
                background: white;
                max-width: 700px;
                margin: 100px auto;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }

            h2 {
                text-align: center;
                margin-bottom: 25px;
                color: #2c3e50;
            }

            .form-group {
                margin-bottom: 20px;
            }

            label {
                display: block;
                margin-bottom: 8px;
                font-weight: bold;
            }

            input, select {
                width: 100%;
                padding: 10px;
                border-radius: 8px;
                border: 1px solid #ccc;
            }

            .submit-btn {
                margin-top: 30px;
                width: 100%;
                padding: 15px;
                font-weight: bold;
                background-color: #4a69bd;
                color: white;
                border: none;
                border-radius: 10px;
                cursor: pointer;
            }

            .submit-btn:hover {
                background-color: #3b5b99;
            }

            .error {
                color: red;
                margin-bottom: 20px;
                font-weight: bold;
            }

            #classDropdown {
                display: none;
            }
        </style>
    </head>
    <body>
        <%
            Map<Integer, List<SchoolClass>> schoolClassMap = (Map<Integer, List<SchoolClass>>) request.getAttribute("schoolClassMap");
            Map<Integer, String> schoolIdNameMap = (Map<Integer, String>) request.getAttribute("schoolIdNameMap");
            List<SchoolModal> schoolList = (List<SchoolModal>) request.getAttribute("schools");
        %>

        <div class="container">
            <h2>Tạo Tài Khoản Học Sinh</h2>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <form method="post" action="tao-tai-khoan-hoc-sinh" autocomplete="off">
                <!-- Thông tin cá nhân -->
                <div class="form-group">
                    <label>Họ tên học sinh:</label>
                    <input type="text" name="name" required/>
                </div>

                <div class="form-group">
                    <label>Tên đăng nhập:</label>
<input type="text" name="username" value="${generatedUsername}" readonly />
                </div>

                <div class="form-group">
                    <label>Mật khẩu:</label>
                    <input type="password" name="password" required autocomplete="new-password"/>
                </div>

                <div class="form-group">
                    <label>Ngày sinh:</label>
                    <input type="date" name="dob"/>
                </div>

                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <input type="tel" name="phone" placeholder="Bỏ trống nếu không có"/>
                </div>

                <div class="form-group">
                    <label>Địa chỉ (Tỉnh/TP):</label>
                    <input type="text" name="address" list="provinceList"/>
                    <datalist id="provinceList">
                        <option value="Hà Nội"><option value="Hồ Chí Minh"><option value="Đà Nẵng">
                        <option value="Hải Phòng"><option value="Cần Thơ"><option value="Bình Dương">
                        <option value="Đồng Nai"><option value="An Giang"><option value="Bắc Giang">
                            <!-- thêm tỉnh khác nếu cần -->
                    </datalist>
                </div>

                <!-- Thông tin trường lớp -->
                <div class="form-group">
                    <label>Trường học:</label>
                    <input type="text" id="schoolInput" name="schoolName" list="schoolList" placeholder="Tùy chọn"/>
                    <datalist id="schoolList">
                        <% if (schoolList != null) {
                                for (SchoolModal school : schoolList) {
                        %>
                        <option value="<%= school.getName()%>"/>
                        <% }
                            } %>
                    </datalist>
                </div>

                <div id="classDropdown" class="form-group">
                    <label>Lớp học:</label>
                    <select name="schoolClassId" id="classSelect">
                        <option value="">-- Chọn lớp --</option>
                        <% if (schoolClassMap != null && schoolIdNameMap != null) {
                                for (Map.Entry<Integer, List<SchoolClass>> entry : schoolClassMap.entrySet()) {
                                    int schoolId = entry.getKey();
                                    String schoolName = schoolIdNameMap.get(schoolId);
                                    for (SchoolClass cls : entry.getValue()) {
                        %>
                        <option value="<%= cls.getId()%>" data-schoolname="<%= schoolName%>">
                            <%= cls.getClassName()%> - <%= cls.getGrade()%>
                        </option>
                        <% }
                            }
                        } else { %>
                        <option disabled>Không tải được danh sách lớp học</option>
                        <% }%>
                    </select>
                </div>

                <div class="form-group">
                    <label>Lớp hiện tại (6-12):</label>
                    <select name="currentGrade">
                        <option value="">-- Chọn lớp --</option>
                        <c:forEach begin="6" end="12" var="i">
                            <option value="${i}">${i}</option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="submit-btn">Tạo tài khoản học sinh</button>
            </form>
        </div>

        <script>
            function setupSchoolClassDropdown(inputId, dropdownId, selectId) {
                const schoolInput = document.getElementById(inputId);
                const classDropdown = document.getElementById(dropdownId);
                const classSelect = document.getElementById(selectId);

                const schoolListOptions = Array.from(document.querySelectorAll("#schoolList option")).map(opt => opt.value.toLowerCase());

                function updateDropdown() {
                    const inputVal = schoolInput.value.trim().toLowerCase();
                    const isKnownSchool = schoolListOptions.includes(inputVal);

                    if (isKnownSchool) {
                        classDropdown.style.display = 'block';
                        classSelect.querySelectorAll('option').forEach(opt => {
                            const schoolName = opt.getAttribute('data-schoolname')?.toLowerCase();
                            if (!schoolName || schoolName === inputVal || opt.value === "") {
                                opt.style.display = 'block';
                            } else {
                                opt.style.display = 'none';
                            }
                        });
                    } else {
                        classDropdown.style.display = 'none';
                        classSelect.value = "";
                    }
                }

                schoolInput.addEventListener("input", updateDropdown);
                updateDropdown();
            }

            document.addEventListener("DOMContentLoaded", function () {
                setupSchoolClassDropdown("schoolInput", "classDropdown", "classSelect");
            });
        </script>

    </body>
</html>

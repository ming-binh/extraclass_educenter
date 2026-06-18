<%-- 
    Document   : consultation
    Created on : Jun 25, 2025, 3:42:00 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.time.LocalDate"%>
<%@page import="modal.ConsultationModal"%>
<%
    String selectedRole = request.getParameter("role");
    if (selectedRole == null)
        selectedRole = "parent";
%>
<!DOCTYPE html>
<html>
    <head>
        <% request.setAttribute("title", "Đăng kí tư vấn");%>
        <style>
            body {
                margin: 0;
                padding: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f9f9ff;
                color: #333;
                height: 100vh;
                display: flex;
                flex-direction: column;
            }

            .main-container {
                display: flex;
                min-height: 100vh;
                min-height: 100vh;
            }

            .left-panel {
                position: sticky;
                flex: 1;
                left: 0;
                top: 0;
                width: 40%;
                height: 100vh;
                background: url('assets/banners/banner_1.jpg') no-repeat center center;
                background-size: cover;
                color: white;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                text-align: center;
                padding: 60px 40px;
                z-index: 1;
            }

            .left-panel h1 {
                font-size: 42px;
                margin-bottom: 20px;
            }

            .left-panel p {
                font-size: 18px;
                line-height: 1.6;
            }

            .right-panel {
                width: 60%;
                background: white;
                padding: 60px 50px;
                display: flex;
                flex-direction: column;
                justify-content: flex-start;
                box-shadow: -5px 0 15px rgba(0, 0, 0, 0.08);
                min-height: 100vh;
            }

            .right-panel h2 {
                font-size: 28px;
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

            .role-selector {
                display: flex;
                gap: 16px;
                margin-bottom: 30px;
            }

            .role-button {
                flex: 1;
                padding: 12px;
                border: 2px solid #6a11cb;
                background: white;
                color: #6a11cb;
                font-weight: bold;
                text-align: center;
                border-radius: 8px;
                cursor: pointer;
                transition: all 0.3s ease;
            }

            .role-button:hover {
                background: #ede3ff;
            }

            .role-button.active {
                background: #6a11cb;
                color: white;
            }

            .form-container {
                display: none;
            }

            .form-container.active {
                display: block;
            }

            form {
                display: flex;
                flex-direction: column;
            }

            label {
                margin-top: 12px;
                font-weight: 600;
                font-size: 14px;
                color: #555;
            }

            input[type="text"],
            input[type="number"],
            input[type="date"],
            input[type="file"],
            input[type="password"],
            input[type="submit"],
            textarea,
            select {
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 6px;
                margin-top: 6px;
                font-size: 14px;
                width: 100%;
            }

            input[type="submit"] {
                background: #f4a100;
                color: white;
                font-weight: bold;
                border: none;
                margin-top: 20px;
                padding: 12px;
                font-size: 16px;
                border-radius: 6px;
                transition: background 0.3s ease;
            }

            input[type="submit"]:hover {
                background: #d99100;
            }

            input[type="checkbox"] {
                width: auto;
                margin-right: 8px;
            }

            #classDropdown,
            #classDropdownTeacher {
                margin-top: 15px;
            }

            .footer-link {
                margin-top: 40px;
                text-align: center;
            }

            .footer-link a {
                color: #6a11cb;
                text-decoration: none;
                font-weight: bold;
            }

            .footer-link .error {
                color: red;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .footer-link .message {
                color: green;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .message,
            .error {
                margin-top: 20px;
                font-weight: bold;
                text-align: center;
                font-size: 14px;
            }

            .terms-modal {
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(0,0,0,0.5);
                display: none;
                justify-content: center;
                align-items: center;
                z-index: 999;
            }

            .terms-content {
                background: white;
                padding: 30px;
                border-radius: 10px;
                max-width: 600px;
                width: 90%;
                box-shadow: 0 5px 20px rgba(0,0,0,0.3);
                max-height: 80vh;
                overflow-y: auto;
                position: relative;
            }

            .terms-content h3 {
                margin-top: 0;
                color: #6a11cb;
            }

            .terms-close {
                position: absolute;
                top: 10px;
                right: 15px;
                font-size: 18px;
                cursor: pointer;
                color: #888;
            }

            .terms-link {
                color: #6a11cb;
                text-decoration: underline;
                cursor: pointer;
            }
        </style>

        <script>
            function switchRole(role) {
                console.log("Switching role:", role);
                document.getElementById("roleInput").value = role;

                const titleEl = document.getElementById("formTitle");
                if (titleEl) {
                    titleEl.innerText = role === "teacher" ? "Đăng ký tuyển dụng" : "Đăng ký tư vấn";
                }

                document.querySelectorAll('.role-button').forEach(btn => btn.classList.remove('active'));
                document.getElementById(role + '-btn').classList.add('active');

                const teacherFields = document.querySelectorAll('.teacher-only');
                teacherFields.forEach(field => {
                    field.style.display = (role === 'teacher') ? 'block' : 'none';
                    field.querySelectorAll('input, textarea, select').forEach(input => {
                        input.required = (role === 'teacher');
                    });
                });
            }

            function showTerms() {
                const role = document.getElementById("roleInput").value;
                const modal = document.getElementById("termsModal");
                const termsText = document.getElementById("termsText");

                if (role === 'parent') {
                    termsText.innerHTML = `
                <p><strong>Điều khoản dành cho phụ huynh:</strong></p>
                <ul>
                    <li>Phụ huynh chịu trách nhiệm cung cấp thông tin chính xác.</li>
                    <li>Chúng tôi có thể liên hệ qua số điện thoại cung cấp để xác nhận.</li>
                    <li>Dữ liệu sẽ được sử dụng chỉ cho mục đích tư vấn giáo dục.</li>
                </ul>`;
                } else {
                    termsText.innerHTML = `
                <p><strong>Điều khoản dành cho giáo viên:</strong></p>
                <ul>
                    <li>Giáo viên phải cung cấp thông tin chứng chỉ đúng và hợp lệ.</li>
                    <li>Thông tin sẽ được sử dụng để xác minh trình độ chuyên môn.</li>
                    <li>Bằng cách đăng ký, bạn đồng ý tham gia vào hệ thống đánh giá của trung tâm.</li>
                </ul>`;
                }
                modal.style.display = "flex";
            }

            function closeTerms() {
                document.getElementById("termsModal").style.display = "none";
            }

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

            // Khởi động khi trang load
            document.addEventListener("DOMContentLoaded", function () {
                switchRole('<%= selectedRole != null ? selectedRole : "parent"%>');
                setupSchoolClassDropdown("schoolInput", "classDropdown", "classSelect");
            });
        </script>

    </head>
    <jsp:include page="layout/header.jsp" />
    <%@ page import="java.util.Map" %>
    <%@ page import="java.util.List" %>
    <%@ page import="modal.SchoolClass" %>
    <%@ page import="modal.SchoolModal" %>

    <%
        Map<Integer, List<SchoolClass>> schoolClassMap = (Map<Integer, List<SchoolClass>>) request.getAttribute("schoolClassMap");
        Map<Integer, String> schoolIdNameMap = (Map<Integer, String>) request.getAttribute("schoolIdNameMap");
        List<SchoolModal> schoolList = (List<SchoolModal>) request.getAttribute("schools");
    %>

    <body class="register-page">
        <div class="main-container">
            <div class="left-panel">
                <h1>Chào mừng!</h1>
                <p>Đăng ký để được tư vấn chi tiết</p>
            </div>

            <div class="right-panel">
                <h2 id="formTitle">Đăng ký tư vấn</h2>

                <div class="role-selector">
                    <div id="parent-btn" class="role-button active" onclick="switchRole('parent')">Phụ huynh</div>
                    <div id="teacher-btn" class="role-button" onclick="switchRole('teacher')">Giáo viên</div>
                </div>

                <div class="form-container active" id="consultation-form-container">
                    <form id="consultation-form" action="tu-van" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="role" id="roleInput" value="parent">

                        <label>Họ tên:</label>
                        <input type="text" name="name" required>

                        <label>Ngày sinh:</label>
                        <input type="date" name="dob" max="<%= java.time.LocalDate.now()%>" required>

                        <label>Số điện thoại:</label>
                        <input type="number" name="phone" required pattern="^0\d{9}$" title="Số điện thoại phải có 10 chữ số và bắt đầu bằng 0">

                        <label>Email:</label>
                        <input type="text" name="email" required>

                        <label>Địa chỉ (Tỉnh/TP):</label>
                        <input type="text" name="address" list="provinceList" required>

                        <label>Trường học:</label>
                        <input type="text" id="schoolInput" name="schoolName" list="schoolList">

                        <div id="classDropdown">
                            <label>Lớp học:</label>
                            <select name="schoolClassId" id="classSelect">
                                <option value="">-- Chọn lớp --</option>
                                <%
                                    if (schoolClassMap != null && schoolIdNameMap != null) {
                                        for (Map.Entry<Integer, List<SchoolClass>> entry : schoolClassMap.entrySet()) {
                                            int schoolId = entry.getKey();
                                            String schoolName = schoolIdNameMap.get(schoolId);
                                            for (SchoolClass cls : entry.getValue()) {
                                %>
                                <option value="<%= cls.getId()%>" data-schoolname="<%= schoolName%>"><%= cls.getClassName()%> - <%= cls.getGrade()%></option>
                                <%
                                        }
                                    }
                                } else {
                                %>
                                <option disabled>Không tải được danh sách lớp học</option>
                                <%
                                    }
                                %>
                            </select>
                        </div>

                        <!-- Giáo viên thêm -->
                        <div class="teacher-only" style="display:none;">
                            <label for="subject">Môn dạy:</label>
                            <select name="subject" id="subject">
                                <option value="">-- Chọn môn học --</option>
                                <%
                                    for (ConsultationModal.Subject subj : ConsultationModal.Subject.values()) {
                                %>
                                <option value="<%= subj.name()%>"><%= subj.getDisplayName()%></option>
                                <%
                                    }
                                %>
                            </select>

                            <label>Kinh nghiệm:</label>
                            <textarea name="experience" placeholder="Mô tả kinh nghiệm giảng dạy của bạn..."></textarea>

                            <label>Upload chứng chỉ:</label>
                            <input type="file" name="certificates" multiple accept=".jpg,.jpeg,.png" required>
                        </div>

                        <div>
                            <input type="checkbox" name="confirm" id="confirmBox" required>
                            <label for="confirmBox">Tôi đồng ý với <span class="terms-link" onclick="showTerms()">điều khoản</span></label>
                        </div>

                        <br><br>
                        <input type="submit" value="Đăng ký">
                    </form>
                </div>

                <datalist id="schoolList">
                    <%
                        if (schoolList != null) {
                            for (SchoolModal s : schoolList) {
                    %>
                    <option value="<%= s.getName()%>">
                        <%
                                }
                            }
                        %>
                </datalist>

                <datalist id="provinceList">
                    <option value="Hà Nội">
                    <option value="Hải Phòng">
                    <option value="Đà Nẵng">
                    <option value="Hồ Chí Minh">
                    <option value="Cần Thơ">
                    <option value="An Giang">
                    <option value="Bà Rịa – Vũng Tàu">
                    <option value="Bắc Giang">
                    <option value="Bắc Kạn">
                    <option value="Bạc Liêu">
                    <option value="Bắc Ninh">
                    <option value="Bến Tre">
                    <option value="Bình Định">
                    <option value="Bình Dương">
                    <option value="Bình Phước">
                    <option value="Bình Thuận">
                    <option value="Cà Mau">
                    <option value="Cao Bằng">
                    <option value="Đắk Lắk">
                    <option value="Đắk Nông">
                    <option value="Điện Biên">
                    <option value="Đồng Nai">
                    <option value="Đồng Tháp">
                    <option value="Gia Lai">
                    <option value="Hà Giang">
                    <option value="Hà Nam">
                    <option value="Hà Tĩnh">
                    <option value="Hải Dương">
                    <option value="Hậu Giang">
                    <option value="Hòa Bình">
                    <option value="Hưng Yên">
                    <option value="Khánh Hòa">
                    <option value="Kiên Giang">
                    <option value="Kon Tum">
                    <option value="Lai Châu">
                    <option value="Lâm Đồng">
                    <option value="Lạng Sơn">
                    <option value="Lào Cai">
                    <option value="Long An">
                    <option value="Nam Định">
                    <option value="Nghệ An">
                    <option value="Ninh Bình">
                    <option value="Ninh Thuận">
                    <option value="Phú Thọ">
                    <option value="Phú Yên">
                    <option value="Quảng Bình">
                    <option value="Quảng Nam">
                    <option value="Quảng Ngãi">
                    <option value="Quảng Ninh">
                    <option value="Quảng Trị">
                    <option value="Sóc Trăng">
                    <option value="Sơn La">
                    <option value="Tây Ninh">
                    <option value="Thái Bình">
                    <option value="Thái Nguyên">
                    <option value="Thanh Hóa">
                    <option value="Thừa Thiên Huế">
                    <option value="Tiền Giang">
                    <option value="Trà Vinh">
                    <option value="Tuyên Quang">
                    <option value="Vĩnh Long">
                    <option value="Vĩnh Phúc">
                    <option value="Yên Bái">
                </datalist>


                <!-- Thông báo -->
                <div class="footer-link">
                    <% if (request.getAttribute("error") != null) {%>
                    <div class="error"><%= request.getAttribute("error")%></div>
                    <% } else if (request.getAttribute("message") != null) {%>
                    <div class="message"><%= request.getAttribute("message")%></div>
                    <% }%>
                    <a href="trang-chu">Quay lại trang chủ</a>
                </div>
            </div>
        </div>

        <div class="terms-modal" id="termsModal">
            <div class="terms-content">
                <span class="terms-close" onclick="closeTerms()">×</span>
                <h3>Điều khoản đăng ký</h3>
                <div id="termsText"></div>
            </div>
        </div>
    </body>

</html>
<jsp:include page="layout/footer.jsp" />
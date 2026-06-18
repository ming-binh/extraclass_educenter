<%-- 
    Document   : createAccount
    Created on : Jul 9, 2025, 2:37:03 PM
    Author     : ASUS
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Tạo tài khoản từ tư vấn</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
                line-height: 1.6;
            }

            .container {
                max-width: 800px;
                margin: 0 auto;
                background: white;
                padding: 40px;
                border-radius: 20px;
                box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            }

            h2 {
                color: #2c3e50;
                text-align: center;
                margin-bottom: 30px;
                font-size: 32px;
                font-weight: 700;
                position: relative;
                padding-bottom: 15px;
            }

            h2::after {
                content: '';
                position: absolute;
                bottom: 0;
                left: 50%;
                transform: translateX(-50%);
                width: 80px;
                height: 4px;
                background: linear-gradient(90deg, #667eea, #764ba2);
                border-radius: 2px;
            }

            .form-group {
                margin-bottom: 25px;
            }

            label {
                display: block;
                margin-bottom: 8px;
                font-weight: 600;
                color: #34495e;
                font-size: 16px;
            }

            input, select {
                width: 100%;
                padding: 15px;
                border: 2px solid #e0e6ed;
                border-radius: 12px;
                font-size: 16px;
                transition: all 0.3s ease;
                background: #f8f9fa;
            }

            input:focus, select:focus {
                outline: none;
                border-color: #667eea;
                background: white;
                box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
            }

            select {
                cursor: pointer;
            }

            .role-fields {
                border: 3px solid #e8ecf4;
                border-radius: 15px;
                padding: 30px;
                margin-top: 30px;
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
                position: relative;
            }

            .role-fields::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 5px;
                background: linear-gradient(90deg, #667eea, #764ba2);
                border-radius: 15px 15px 0 0;
            }

            .role-fields h3 {
                color: #2c3e50;
                margin-bottom: 25px;
                font-size: 24px;
                font-weight: 700;
                padding-left: 35px;
                position: relative;
            }

            .role-fields h3::before {
                content: '';
                position: absolute;
                left: 0;
                top: 50%;
                transform: translateY(-50%);
                width: 25px;
                height: 25px;
                background: linear-gradient(135deg, #667eea, #764ba2);
                border-radius: 50%;
            }

            .certificate-section {
                margin-top: 30px;
                background: white;
                padding: 25px;
                border-radius: 15px;
                border: 2px solid #e0e6ed;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }

            .certificate-section h4 {
                color: #2c3e50;
                margin-bottom: 20px;
                font-size: 20px;
                font-weight: 600;
                border-bottom: 2px solid #eee;
                padding-bottom: 10px;
            }

            .certificate-item {
                margin-bottom: 25px;
                padding: 25px;
                background: linear-gradient(135deg, #e8f5e8 0%, #d4f1d4 100%);
                border-radius: 15px;
                border: 2px solid #c3e6c3;
                transition: all 0.3s ease;
            }

            .certificate-header {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
            }

            .certificate-item input[type="checkbox"] {
                width: 20px;
                height: 20px;
                margin-right: 15px;
                cursor: pointer;
                accent-color: #28a745;
            }

            .certificate-item label {
                margin-bottom: 0;
                cursor: pointer;
                color: #155724;
                font-weight: 600;
                font-size: 16px;
            }

            .certificate-name-input {
                margin-bottom: 20px;
                border: 2px solid #28a745 !important;
                background: white !important;
                padding: 15px !important;
                border-radius: 10px !important;
            }

            .certificate-image-container {
                text-align: center;
                background: white;
                padding: 15px;
                border-radius: 10px;
                border: 2px solid #28a745;
            }

            .certificate-image {
                max-width: 100%;
                height: auto;
                min-height: 200px;
                max-height: 400px;
                border-radius: 8px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                transition: all 0.3s ease;
                display: block;
                margin: 0 auto;
            }

            .submit-btn {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 18px 50px;
                border: none;
                border-radius: 15px;
                font-size: 18px;
                font-weight: 700;
                cursor: pointer;
                width: 100%;
                margin-top: 40px;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            .submit-btn:hover {
                transform: translateY(-3px);
                box-shadow: 0 15px 35px rgba(102, 126, 234, 0.4);
            }

            .error {
                color: #721c24;
                background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
                padding: 20px;
                border-radius: 12px;
                margin-bottom: 30px;
                border: 2px solid #f1aeb5;
                font-weight: 600;
                font-size: 16px;
            }

            .info-note {
                background: linear-gradient(135deg, #d1ecf1 0%, #bee5eb 100%);
                color: #0c5460;
                padding: 20px;
                border-radius: 12px;
                margin-bottom: 30px;
                border: 2px solid #abdde5;
                font-size: 16px;
                font-weight: 500;
                position: relative;
                padding-left: 60px;
            }

            .info-note::before {
                content: 'ℹ';
                position: absolute;
                left: 20px;
                top: 50%;
                transform: translateY(-50%);
                font-size: 24px;
                font-weight: bold;
                color: #0c5460;
            }

            .no-certificates {
                text-align: center;
                color: #6c757d;
                font-style: italic;
                padding: 40px;
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
                border-radius: 12px;
                border: 3px dashed #dee2e6;
                font-size: 18px;
            }

            .required {
                color: #dc3545;
                font-weight: bold;
            }

            .hidden {
                display: none;
            }

            .readonly-field {
                background-color: #e9ecef !important;
                color: #6c757d;
            }

        </style>
    </head>
    <body>
        <div class="container">
            <h2>Tạo tài khoản từ yêu cầu tư vấn</h2>

            <div class="info-note">
                <strong>Lưu ý:</strong> Tài khoản được tạo sẽ có mật khẩu mặc định là <strong>123456</strong>. 
                Thông tin được điền sẵn từ tư vấn, bạn có thể chỉnh sửa trước khi tạo tài khoản.
            </div>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <form method="post" action="tao-tai-khoan">
                <input type="hidden" name="consultationId" value="${consultation.id}" />

                <div class="form-group">
                    <label for="name">Họ tên:</label>
                    <input type="text" id="name" name="name" value="${consultation.name}" />
                </div>

                <div class="form-group">
                    <label for="username">Tên đăng nhập (username): <span class="required">*</span></label>
                    <input type="text" id="username" name="username" required 
                           value="${not empty consultation.email ? consultation.email : ''}"
                           placeholder="Nhập tên đăng nhập"/>
                </div>

                <div class="form-group">
                    <label for="dob">Ngày sinh:</label>
                    <input type="date" id="dob" name="dob" value="${dobString}" />
                </div>

                <div class="form-group">
                    <label for="phone">Số điện thoại:</label>
                    <input type="tel" id="phone" name="phone" value="${consultation.phone}" />
                </div>

                <div class="form-group">
                    <label for="address">Địa chỉ:</label>
                    <input type="text" id="address" name="address" value="${consultation.address}" />
                </div>

                <div class="form-group">
                    <label for="role">Vai trò: <span class="required">*</span></label>
                    <select id="role" name="role" required onchange="toggleRole(this.value)">
                        <option value="parent" ${defaultRole == 'parent' ? 'selected' : ''}>Phụ huynh</option>
                        <option value="teacher" ${defaultRole == 'teacher' ? 'selected' : ''}>Giáo viên</option>
                    </select>
                </div>

                <div id="parentFields" class="role-fields ${defaultRole == 'teacher' ? 'hidden' : ''}">
                    <h3>Thông tin phụ huynh</h3>
                    <div class="form-group">
                        <label for="relationship">Mối quan hệ với học sinh:</label>
                        <select id="relationship" name="relationship">
                            <option value="father">Cha</option>
                            <option value="mother">Mẹ</option>
                            <option value="guardian">Người giám hộ</option>
                        </select>
                    </div>
                </div>

                <div id="teacherFields" class="role-fields ${defaultRole == 'parent' ? 'hidden' : ''}">
                    <h3>Thông tin giáo viên</h3>
                    <div class="form-group">
                        <label for="experience">Kinh nghiệm:</label>
                        <input type="text" id="experience" name="experience" 
                               value="${consultation.experience}" 
                               placeholder="Nhập kinh nghiệm giảng dạy"/>
                    </div>

                    <div class="form-group">
                        <label for="subject">Môn học:</label>
                        <select id="subject" name="subject">
                            <option value="">-- Chọn môn học --</option>
                            <c:forEach var="teacherSubject" items="${teacherSubjects}">
                                <option value="${teacherSubject.name()}" 
                                        ${consultation.subject != null && consultation.subject.name() == teacherSubject.name() ? 'selected' : ''}>
                                    ${teacherSubject.displayName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="certificate-section">
                        <h4>Chứng chỉ có sẵn từ tư vấn:</h4>
                        <c:choose>
                            <c:when test="${not empty consultationCertificates}">
                                <c:forEach var="cert" items="${consultationCertificates}">
                                    <div class="certificate-item">
                                        <div class="certificate-header">
                                            <input type="checkbox" name="existingCertificate" 
                                                   value="${cert.id}" id="cert_${cert.id}" checked />
                                            <label for="cert_${cert.id}">Sử dụng chứng chỉ này</label>
                                        </div>

                                        <input type="text" name="existingCertificateName_${cert.id}" 
                                               class="certificate-name-input"
                                               placeholder="Nhập tên chứng chỉ..." 
                                               value="Chứng chỉ từ tư vấn"/>

                                        <div class="certificate-image-container">
                                            <img src="${pageContext.request.contextPath}/assets/${cert.imageURL}" 
                                                 alt="Chứng chỉ" class="certificate-image" 
                                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='block';" />
                                            <div style="display:none; padding: 20px; color: #666;">Không thể tải hình ảnh chứng chỉ</div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="no-certificates">
                                    <p>Không có chứng chỉ nào từ tư vấn</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <button type="submit" class="submit-btn">Tạo tài khoản</button>
            </form>
        </div>

        <script>
            function toggleRole(role) {
                const parentFields = document.getElementById('parentFields');
                const teacherFields = document.getElementById('teacherFields');

                if (role === 'parent') {
                    parentFields.classList.remove('hidden');
                    teacherFields.classList.add('hidden');
                } else {
                    parentFields.classList.add('hidden');
                    teacherFields.classList.remove('hidden');
                }
            }
        </script>
    </body>
</html>
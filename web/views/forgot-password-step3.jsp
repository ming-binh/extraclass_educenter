<%-- 
    Document   : forgot-password-step3
    Created on : Jul 24, 2025, 1:07:46 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quên mật khẩu - Bước 3</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 1rem;
            }
            .forgot-card {
                background: white;
                border-radius: 15px;
                box-shadow: 0 15px 35px rgba(0,0,0,0.1);
                overflow: hidden;
                max-width: 420px;
                width: 100%;
                animation: fadeInUp 0.5s ease;
            }
            .card-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                text-align: center;
                padding: 2rem 1.5rem;
            }
            .step-indicator {
                display: flex;
                justify-content: center;
                margin: 1rem 0;
            }
            .step {
                width: 30px;
                height: 30px;
                border-radius: 50%;
                background: #e9ecef;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 0.5rem;
                font-weight: bold;
            }
            .step.active {
                background: #007bff;
                color: white;
            }
            .step.completed {
                background: #28a745;
                color: white;
            }
            .countdown {
                font-weight: bold;
                color: #dc3545;
            }
            @keyframes fadeInUp {
                from {
                    opacity: 0;
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-4">
                    <div class="forgot-card">
                        <div class="card-header">
                            <h4><i class="fas fa-lock me-2"></i>Đặt lại mật khẩu</h4>
                            <p class="mb-0">Tạo mật khẩu mới cho tài khoản của bạn</p>
                        </div>
                        <div class="card-body p-4">
                            <div class="step-indicator">
                                <div class="step completed">1</div>
                                <div class="step completed">2</div>
                                <div class="step active">3</div>
                            </div>

                            <% if (request.getAttribute("error") != null) {%>
                            <div class="alert alert-danger">
                                <%= request.getAttribute("error")%>
                            </div>
                            <% }%>

                            <form method="post" action="quen-mat-khau" id="resetForm">
                                <input type="hidden" name="action" value="reset-password">

                                <div class="mb-3">
                                    <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                    <div class="input-group">
                                        <input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="Nhập mật khẩu mới" required>

                                    </div>
                                    <div class="form-text">Mật khẩu phải có ít nhất 6 ký tự</div>
                                </div>

                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                                    <div class="input-group">
                                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>

                                    </div>
                                    <div class="form-text" id="matchMessage"></div>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-save me-1"></i>Đặt lại mật khẩu
                                    </button>
                                    <a href="dang-nhap" class="btn btn-outline-secondary">
                                        <i class="fas fa-arrow-left me-1"></i>Quay lại đăng nhập
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function toggleVisibility(inputId, btn) {
                const input = document.getElementById(inputId);
                const icon = btn.querySelector('i');
                const isPassword = input.type === 'password';

                input.type = isPassword ? 'text' : 'password';
                icon.classList.toggle('fa-eye');
                icon.classList.toggle('fa-eye-slash');
            }

            document.getElementById('resetForm').addEventListener('submit', function (e) {
                const pwd = document.getElementById('newPassword').value;
                const confirm = document.getElementById('confirmPassword').value;

                if (pwd.length < 6) {
                    e.preventDefault();
                    alert('Mật khẩu phải có ít nhất 6 ký tự!');
                } else if (pwd !== confirm) {
                    e.preventDefault();
                    alert('Mật khẩu xác nhận không khớp!');
                }
            });

            document.getElementById('confirmPassword').addEventListener('input', function () {
                const pwd = document.getElementById('newPassword').value;
                const confirm = this.value;
                const msg = document.getElementById('matchMessage');

                if (!confirm) {
                    msg.textContent = '';
                } else if (pwd === confirm) {
                    msg.textContent = '✓ Mật khẩu khớp';
                    msg.style.color = '#28a745';
                } else {
                    msg.textContent = '✗ Mật khẩu không khớp';
                    msg.style.color = '#dc3545';
                }
            });
        </script>
    </body>
</html>

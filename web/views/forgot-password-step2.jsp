<%-- 
    Document   : forgot-password-step2
    Created on : Jul 24, 2025, 1:06:52 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quên mật khẩu - Bước 2</title>
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
                            <h3><i class="fas fa-shield-alt me-2"></i>Xác thực</h3>
                            <p class="mb-0">Nhập mã xác thực đã gửi qua email</p>
                        </div>

                        <div class="card-body p-4">
                            <!-- Step indicator -->
                            <div class="step-indicator">
                                <div class="step completed">1</div>
                                <div class="step active">2</div>
                                <div class="step">3</div>
                            </div>

                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                Mã xác thực đã được gửi đến email: <strong><%= session.getAttribute("reset_email")%></strong>
                            </div>

                            <% if (request.getAttribute("error") != null) {%>
                            <div class="alert alert-danger">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <%= request.getAttribute("error")%>
                            </div>
                            <% }%>

                            <form method="post" action="quen-mat-khau">
                                <input type="hidden" name="action" value="verify-code">

                                <div class="mb-3">
                                    <label for="code" class="form-label">
                                        <i class="fas fa-key me-2"></i>Mã xác thực (6 số)
                                    </label>
                                    <input type="text" class="form-control code-input" id="code" name="code" 
                                           placeholder="000000" maxlength="6" pattern="[0-9]{6}" required>
                                    <div class="form-text">
                                        Mã xác thực có hiệu lực trong 3 phút<span class="countdown" id="countdown"></span>
                                    </div>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-check me-2"></i>Xác thực
                                    </button>
                                    <a href="quen-mat-khau" class="btn btn-outline-secondary">
                                        <i class="fas fa-arrow-left me-2"></i>Quay lại
                                    </a>
                                </div>
                            </form>

                            <div class="text-center mt-3">
                                <p class="text-muted">Không nhận được email?</p>
                                <a href="quen-mat-khau" class="btn btn-link">
                                    <i class="fas fa-redo me-2"></i>Gửi lại mã
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                let timeLeft = 3 * 60; // 3 phút

                function updateCountdown() {
                    if (timeLeft <= 0) {
                        clearInterval(countdownInterval);
                        alert('Mã xác thực đã hết hạn! Vui lòng thử lại.');
                        window.location.href = 'quen-mat-khau';
                        return;
                    }

                    const minutes = Math.floor(timeLeft / 60);
                    const seconds = timeLeft % 60;
                    const countdownElement = document.getElementById('countdown');
                    
                    timeLeft--;
                }

                const countdownInterval = setInterval(updateCountdown, 1000);
                updateCountdown(); // Gọi ngay lập tức để hiển thị thời gian ban đầu
            });
        </script>
    </body>
</html>
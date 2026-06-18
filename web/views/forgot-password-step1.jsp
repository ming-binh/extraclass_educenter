<%-- 
    Document   : forgot-password-step1
    Created on : Jul 24, 2025, 1:06:01 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quên mật khẩu - Bước 1</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                font-family: 'Segoe UI', sans-serif;
                padding: 1rem;
            }

            .forgot-card {
                background: white;
                border-radius: 16px;
                box-shadow: 0 12px 24px rgba(0,0,0,0.15);
                max-width: 420px;
                width: 100%;
                overflow: hidden;
                animation: fadeInUp 0.4s ease;
            }

            @keyframes fadeInUp {
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .card-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                text-align: center;
                padding: 2rem 1rem;
            }

            .card-header h3 {
                margin-bottom: 0.5rem;
                font-weight: 600;
            }

            .step-indicator {
                display: flex;
                justify-content: center;
                margin-bottom: 1.5rem;
            }

            .step {
                width: 36px;
                height: 36px;
                border-radius: 50%;
                background: #dee2e6;
                color: #495057;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 0.4rem;
                font-weight: bold;
                font-size: 14px;
                transition: 0.3s;
            }

            .step.active {
                background: #0d6efd;
                color: white;
                box-shadow: 0 0 0 4px rgba(13, 110, 253, 0.2);
            }

            .step.completed {
                background: #28a745;
                color: white;
            }

            .form-label i {
                color: #6c757d;
            }

            .btn-primary {
                background-color: #4e73df;
                border-color: #4e73df;
            }

            .btn-primary:hover {
                background-color: #2e59d9;
                border-color: #2653d4;
            }

            .btn-outline-secondary:hover {
                background-color: #f8f9fa;
            }

            .alert-danger {
                font-size: 0.95rem;
            }
        </style>

    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-4">
                    <div class="forgot-card">
                        <div class="card-header">
                            <h3><i class="fas fa-key me-2"></i>Quên mật khẩu</h3>
                            <p class="mb-0">Nhập email để lấy lại mật khẩu</p>
                        </div>

                        <div class="card-body p-4">
                            <!-- Step indicator -->
                            <div class="step-indicator">
                                <div class="step active">1</div>
                                <div class="step">2</div>
                                <div class="step">3</div>
                            </div>

                            <% if (request.getAttribute("error") != null) {%>
                            <div class="alert alert-danger">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <%= request.getAttribute("error")%>
                            </div>
                            <% }%>

                            <form method="post" action="quen-mat-khau">
                                <input type="hidden" name="action" value="send-code">

                                <div class="mb-3">
                                    <label for="email" class="form-label">
                                        <i class="fas fa-envelope me-2"></i>Email (Username)
                                    </label>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           placeholder="Nhập email của bạn" required>
                                    <div class="form-text">
                                        Nhập email mà bạn đã đăng ký tài khoản
                                    </div>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-paper-plane me-2"></i>Gửi mã xác thực
                                    </button>
                                    <a href="dang-nhap" class="btn btn-outline-secondary">
                                        <i class="fas fa-arrow-left me-2"></i>Quay lại đăng nhập
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>

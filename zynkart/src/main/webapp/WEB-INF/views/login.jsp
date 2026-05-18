<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="min-vh-100 d-flex align-items-center justify-content-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 col-lg-4">
                <div class="text-center mb-4">
                    <a href="${pageContext.request.contextPath}/" class="text-decoration-none">
                        <h2 class="fw-bold text-dark">
                            <i class="fas fa-shopping-bag text-warning me-2"></i>Zynkart
                        </h2>
                    </a>
                    <p class="text-muted">Sign in to your account</p>
                </div>

                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>${successMsg}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>${errorMsg}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm" novalidate>
                            <div class="mb-3">
                                <label for="username" class="form-label fw-semibold">Username</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-user text-muted"></i></span>
                                    <input type="text" class="form-control" id="username" name="username"
                                           placeholder="Enter username" required autofocus>
                                    <div class="invalid-feedback">Username is required.</div>
                                </div>
                            </div>
                            <div class="mb-4">
                                <label for="password" class="form-label fw-semibold">Password</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fas fa-lock text-muted"></i></span>
                                    <input type="password" class="form-control" id="password" name="password"
                                           placeholder="Enter password" required>
                                    <button class="btn btn-outline-secondary" type="button" id="togglePass">
                                        <i class="fas fa-eye" id="eyeIcon"></i>
                                    </button>
                                    <div class="invalid-feedback">Password is required.</div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-dark w-100 py-2 fw-bold">
                                <i class="fas fa-sign-in-alt me-2"></i>Sign In
                            </button>
                        </form>
                        <hr class="my-3">
                        <div class="text-center">
                            <p class="mb-0 text-muted small">Don't have an account?
                                <a href="${pageContext.request.contextPath}/register" class="text-dark fw-bold text-decoration-none">Register here</a>
                            </p>
                        </div>
                    </div>
                </div>

                <div class="card mt-3 border-0 bg-info bg-opacity-10 rounded-3">
                    <div class="card-body p-3 small">
                        <strong>Demo Credentials:</strong><br>
                        Admin: <code>admin</code> / <code>admin123</code><br>
                        User: <code>user</code> / <code>user123</code>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script>
    document.getElementById('togglePass').addEventListener('click', function() {
        const passInput = document.getElementById('password');
        const eyeIcon = document.getElementById('eyeIcon');
        if (passInput.type === 'password') {
            passInput.type = 'text';
            eyeIcon.classList.replace('fa-eye', 'fa-eye-slash');
        } else {
            passInput.type = 'password';
            eyeIcon.classList.replace('fa-eye-slash', 'fa-eye');
        }
    });

    (function() {
        'use strict';
        const form = document.getElementById('loginForm');
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    })();
</script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="min-vh-100 d-flex align-items-center justify-content-center py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-7 col-lg-6">
                <div class="text-center mb-4">
                    <a href="${pageContext.request.contextPath}/" class="text-decoration-none">
                        <h2 class="fw-bold text-dark">
                            <i class="fas fa-shopping-bag text-warning me-2"></i>Zynkart
                        </h2>
                    </a>
                    <p class="text-muted">Create your account</p>
                </div>

                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <i class="fas fa-exclamation-circle me-2"></i>${errorMsg}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-body p-4">
                        <form:form action="${pageContext.request.contextPath}/register" method="post"
                                   modelAttribute="registerDTO" id="registerForm" novalidate="true">

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Username *</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-user text-muted"></i></span>
                                        <form:input path="username" cssClass="form-control" placeholder="e.g. john_doe" required="true"/>
                                    </div>
                                    <form:errors path="username" cssClass="text-danger small"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Full Name *</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-id-card text-muted"></i></span>
                                        <form:input path="fullName" cssClass="form-control" placeholder="e.g. John Doe" required="true"/>
                                    </div>
                                    <form:errors path="fullName" cssClass="text-danger small"/>
                                </div>
                                <div class="col-12">
                                    <label class="form-label fw-semibold">Email Address *</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-envelope text-muted"></i></span>
                                        <form:input path="email" type="email" cssClass="form-control" placeholder="e.g. john@email.com" required="true"/>
                                    </div>
                                    <form:errors path="email" cssClass="text-danger small"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Password *</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-lock text-muted"></i></span>
                                        <form:password path="password" cssClass="form-control" placeholder="Min 6 characters" required="true"/>
                                    </div>
                                    <form:errors path="password" cssClass="text-danger small"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Confirm Password *</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-lock text-muted"></i></span>
                                        <form:password path="confirmPassword" cssClass="form-control" placeholder="Repeat password" required="true"/>
                                    </div>
                                    <form:errors path="confirmPassword" cssClass="text-danger small"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Mobile Number</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-phone text-muted"></i></span>
                                        <form:input path="phone" cssClass="form-control" placeholder="10-digit number"/>
                                    </div>
                                    <form:errors path="phone" cssClass="text-danger small"/>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Address</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light"><i class="fas fa-map-marker-alt text-muted"></i></span>
                                        <form:input path="address" cssClass="form-control" placeholder="City, State"/>
                                    </div>
                                </div>
                            </div>

                            <div class="mt-4">
                                <button type="submit" class="btn btn-dark w-100 py-2 fw-bold">
                                    <i class="fas fa-user-plus me-2"></i>Create Account
                                </button>
                            </div>
                        </form:form>
                        <hr class="my-3">
                        <div class="text-center">
                            <p class="mb-0 text-muted small">Already have an account?
                                <a href="${pageContext.request.contextPath}/login" class="text-dark fw-bold text-decoration-none">Sign in here</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>

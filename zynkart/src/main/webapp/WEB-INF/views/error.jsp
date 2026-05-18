<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-light">
<div class="min-vh-100 d-flex align-items-center justify-content-center">
    <div class="text-center p-5">
        <i class="fas fa-exclamation-triangle text-warning" style="font-size: 5rem;"></i>
        <h2 class="fw-bold mt-4">${not empty errorTitle ? errorTitle : 'Something went wrong'}</h2>
        <p class="text-muted mb-4">${not empty errorMessage ? errorMessage : 'An unexpected error occurred.'}</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-dark px-4">
            <i class="fas fa-home me-2"></i>Go Home
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

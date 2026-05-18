<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categories - Zynkart Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../header.jsp"/>

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold mb-0"><i class="fas fa-tags me-2 text-warning"></i>Manage Categories</h4>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary btn-sm">
            <i class="fas fa-arrow-left me-1"></i>Dashboard
        </a>
    </div>

    <c:if test="${not empty successMsg}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i>${successMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle me-2"></i>${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">
        <!-- Add Category Form -->
        <div class="col-md-4">
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="fas fa-plus me-2"></i>Add Category
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/categories/add" method="post" id="catForm" novalidate>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Category Name *</label>
                            <input type="text" name="name" class="form-control" placeholder="e.g. Electronics" required>
                            <div class="invalid-feedback">Category name is required.</div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Description</label>
                            <textarea name="description" class="form-control" rows="2" placeholder="Optional description"></textarea>
                        </div>
                        <button type="submit" class="btn btn-dark w-100 fw-bold">
                            <i class="fas fa-plus me-2"></i>Add Category
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Categories List -->
        <div class="col-md-8">
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="fas fa-list me-2"></i>All Categories
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${categories}" var="cat" varStatus="s">
                                <tr>
                                    <td>${s.count}</td>
                                    <td class="fw-semibold">${cat.name}</td>
                                    <td class="text-muted small">${cat.description}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/categories/delete/${cat.id}"
                                              method="post" class="d-inline"
                                              onsubmit="return confirm('Delete category: ${cat.name}?');">
                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty categories}">
                                <tr><td colspan="4" class="text-center text-muted py-4">No categories yet.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function() {
        'use strict';
        const form = document.getElementById('catForm');
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) { e.preventDefault(); e.stopPropagation(); }
            form.classList.add('was-validated');
        });
    })();
</script>
</body>
</html>

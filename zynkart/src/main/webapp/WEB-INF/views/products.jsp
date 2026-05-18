<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container py-4">
    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="fw-bold mb-0"><i class="fas fa-store me-2 text-warning"></i>All Products</h3>
        <span class="text-muted small">${products.size()} product(s) found</span>
    </div>

    <!-- Search & Filter -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/products" method="get" class="row g-3 align-items-end">
                <div class="col-md-5">
                    <label class="form-label fw-semibold small">Search Products</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="fas fa-search text-muted"></i></span>
                        <input type="text" name="keyword" class="form-control" placeholder="Search by name..."
                               value="${keyword}">
                    </div>
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold small">Filter by Category</label>
                    <select name="categoryId" class="form-select">
                        <option value="">All Categories</option>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat.id}" ${selectedCategoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-dark me-2">
                        <i class="fas fa-filter me-1"></i>Apply
                    </button>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary">
                        <i class="fas fa-times me-1"></i>Clear
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Category Pills -->
    <div class="mb-4 d-flex flex-wrap gap-2">
        <a href="${pageContext.request.contextPath}/products"
           class="btn btn-sm ${empty selectedCategoryId ? 'btn-dark' : 'btn-outline-dark'}">All</a>
        <c:forEach items="${categories}" var="cat">
            <a href="${pageContext.request.contextPath}/products?categoryId=${cat.id}"
               class="btn btn-sm ${selectedCategoryId == cat.id ? 'btn-dark' : 'btn-outline-dark'}">
                ${cat.name}
            </a>
        </c:forEach>
    </div>

    <!-- Product Grid -->
    <c:choose>
        <c:when test="${empty products}">
            <div class="text-center py-5">
                <i class="fas fa-search fs-1 text-muted mb-3 d-block"></i>
                <h5 class="text-muted">No products found</h5>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-dark mt-2">View All Products</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                <c:forEach items="${products}" var="product">
                    <div class="col">
                        <div class="card h-100 border-0 shadow-sm product-card">
                            <a href="${pageContext.request.contextPath}/products/${product.id}" class="text-decoration-none">
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <img src="${product.imageUrl}" class="card-img-top product-img" alt="${product.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="product-img-placeholder d-flex align-items-center justify-content-center bg-light">
                                            <i class="fas fa-image fs-1 text-muted"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="card-body d-flex flex-column">
                                <p class="text-muted small mb-1">${product.category.name}</p>
                                <h6 class="card-title fw-bold mb-2 product-title">
                                    <a href="${pageContext.request.contextPath}/products/${product.id}" class="text-dark text-decoration-none">
                                        ${product.name}
                                    </a>
                                </h6>
                                <div class="mt-auto">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <span class="fs-5 fw-bold text-success">
                                            &#x20B9;<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                                        </span>
                                        <c:choose>
                                            <c:when test="${product.available}">
                                                <span class="badge bg-success-subtle text-success border border-success-subtle">In Stock</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger-subtle text-danger border border-danger-subtle">Out of Stock</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <c:if test="${product.available}">
                                        <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                            <input type="hidden" name="productId" value="${product.id}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit" class="btn btn-dark btn-sm w-100">
                                                <i class="fas fa-cart-plus me-1"></i>Add to Cart
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Zynkart - Your Online Store</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<!-- Hero Banner -->
<section class="hero-section text-white py-5">
    <div class="container py-4">
        <div class="row align-items-center">
            <div class="col-lg-6">
                <h1 class="display-4 fw-bold mb-3">Shop the Best<br>Products Online</h1>
                <p class="lead mb-4 opacity-90">Discover amazing deals on electronics, fashion, books and more. Quality products, delivered fast.</p>
                <div class="d-flex gap-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-warning btn-lg fw-bold px-4">
                        <i class="fas fa-shopping-bag me-2"></i>Shop Now
                    </a>
                    <a href="#categories" class="btn btn-outline-light btn-lg px-4">Browse Categories</a>
                </div>
            </div>
            <div class="col-lg-6 text-center d-none d-lg-block">
                <i class="fas fa-shopping-cart hero-icon"></i>
            </div>
        </div>
    </div>
</section>

<!-- Flash Messages -->
<div class="container mt-3">
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
</div>

<!-- Categories Section -->
<section id="categories" class="py-5 bg-light">
    <div class="container">
        <h2 class="fw-bold text-center mb-4">Shop by Category</h2>
        <div class="row g-3 justify-content-center">
            <c:forEach items="${categories}" var="cat">
                <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                    <a href="${pageContext.request.contextPath}/products?categoryId=${cat.id}"
                       class="text-decoration-none">
                        <div class="card border-0 shadow-sm text-center h-100 category-card">
                            <div class="card-body p-3">
                                <div class="category-icon mb-2">
                                    <i class="fas fa-tag fs-3 text-warning"></i>
                                </div>
                                <h6 class="card-title mb-0 fw-semibold text-dark">${cat.name}</h6>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Latest Products -->
<section class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold mb-0">Latest Products</h2>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-dark btn-sm">
                View All <i class="fas fa-arrow-right ms-1"></i>
            </a>
        </div>

        <c:choose>
            <c:when test="${empty latestProducts}">
                <div class="text-center py-5 text-muted">
                    <i class="fas fa-box-open fs-1 mb-3 d-block"></i>
                    <p>No products available yet.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                    <c:forEach items="${latestProducts}" var="product">
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
                                                    <span class="badge bg-success-subtle text-success border border-success-subtle small">In Stock</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger-subtle text-danger border border-danger-subtle small">Out of Stock</span>
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
</section>

<!-- Features Section -->
<section class="py-5 bg-dark text-white">
    <div class="container">
        <div class="row g-4 text-center">
            <div class="col-md-3">
                <i class="fas fa-truck fs-2 text-warning mb-3 d-block"></i>
                <h6 class="fw-bold">Free Delivery</h6>
                <p class="text-secondary small mb-0">On orders above &#x20B9;500</p>
            </div>
            <div class="col-md-3">
                <i class="fas fa-shield-alt fs-2 text-warning mb-3 d-block"></i>
                <h6 class="fw-bold">Secure Payments</h6>
                <p class="text-secondary small mb-0">100% protected transactions</p>
            </div>
            <div class="col-md-3">
                <i class="fas fa-undo fs-2 text-warning mb-3 d-block"></i>
                <h6 class="fw-bold">Easy Returns</h6>
                <p class="text-secondary small mb-0">7-day hassle-free returns</p>
            </div>
            <div class="col-md-3">
                <i class="fas fa-headset fs-2 text-warning mb-3 d-block"></i>
                <h6 class="fw-bold">24/7 Support</h6>
                <p class="text-secondary small mb-0">Always here to help you</p>
            </div>
        </div>
    </div>
</section>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>

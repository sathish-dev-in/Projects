<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none">Products</a></li>
            <li class="breadcrumb-item active">${product.name}</li>
        </ol>
    </nav>

    <!-- Flash Messages -->
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
        <!-- Product Image -->
        <div class="col-md-5">
            <div class="card border-0 shadow-sm">
                <c:choose>
                    <c:when test="${not empty product.imageUrl}">
                        <img src="${product.imageUrl}" class="img-fluid rounded-3" alt="${product.name}"
                             style="width:100%; height:380px; object-fit:cover;">
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex align-items-center justify-content-center bg-light rounded-3" style="height:380px;">
                            <i class="fas fa-image fs-1 text-muted"></i>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Product Info -->
        <div class="col-md-7">
            <div class="ps-md-3">
                <span class="badge bg-warning text-dark mb-2">${product.category.name}</span>
                <h2 class="fw-bold mb-3">${product.name}</h2>

                <div class="d-flex align-items-center gap-3 mb-3">
                    <span class="display-6 fw-bold text-success">
                        &#x20B9;<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                    </span>
                    <c:choose>
                        <c:when test="${product.available}">
                            <span class="badge bg-success fs-6">
                                <i class="fas fa-check me-1"></i>In Stock
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-danger fs-6">Out of Stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:if test="${product.available}">
                    <p class="text-muted small mb-3">${product.stock} units available</p>
                </c:if>

                <c:if test="${not empty product.description}">
                    <p class="mb-4 text-secondary">${product.description}</p>
                </c:if>

                <c:if test="${product.available}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-flex gap-2 align-items-center mb-3">
                        <input type="hidden" name="productId" value="${product.id}">
                        <div class="input-group" style="width: 130px;">
                            <button type="button" class="btn btn-outline-secondary" onclick="decreaseQty()">-</button>
                            <input type="number" id="quantityInput" name="quantity" class="form-control text-center"
                                   value="1" min="1" max="${product.stock}" readonly>
                            <button type="button" class="btn btn-outline-secondary" onclick="increaseQty(${product.stock})">+</button>
                        </div>
                        <button type="submit" class="btn btn-dark btn-lg px-4 flex-grow-1">
                            <i class="fas fa-cart-plus me-2"></i>Add to Cart
                        </button>
                    </form>
                </c:if>

                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-dark">
                    <i class="fas fa-arrow-left me-2"></i>Back to Products
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function decreaseQty() {
        const input = document.getElementById('quantityInput');
        if (parseInt(input.value) > 1) input.value = parseInt(input.value) - 1;
    }
    function increaseQty(max) {
        const input = document.getElementById('quantityInput');
        if (parseInt(input.value) < max) input.value = parseInt(input.value) + 1;
    }
</script>
</body>
</html>

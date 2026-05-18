<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Cart - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container py-4">
    <h3 class="fw-bold mb-4"><i class="fas fa-shopping-cart me-2 text-warning"></i>My Cart</h3>

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

    <c:choose>
        <c:when test="${cart.empty}">
            <div class="text-center py-5">
                <i class="fas fa-shopping-cart fs-1 text-muted mb-3 d-block"></i>
                <h5 class="text-muted">Your cart is empty</h5>
                <p class="text-muted">Add some products to get started!</p>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-dark mt-2">
                    <i class="fas fa-shopping-bag me-2"></i>Continue Shopping
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <!-- Cart Items -->
                <div class="col-lg-8">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-0">
                            <c:forEach items="${cart.cartItems}" var="item">
                                <div class="d-flex align-items-center p-3 border-bottom gap-3 cart-item">
                                    <!-- Product Image -->
                                    <c:choose>
                                        <c:when test="${not empty item.product.imageUrl}">
                                            <img src="${item.product.imageUrl}" alt="${item.product.name}"
                                                 class="rounded-2" style="width:80px; height:80px; object-fit:cover;">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="d-flex align-items-center justify-content-center bg-light rounded-2"
                                                 style="width:80px; height:80px; flex-shrink:0;">
                                                <i class="fas fa-image text-muted"></i>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                                    <!-- Product Details -->
                                    <div class="flex-grow-1">
                                        <h6 class="fw-bold mb-1">
                                            <a href="${pageContext.request.contextPath}/products/${item.product.id}"
                                               class="text-dark text-decoration-none">${item.product.name}</a>
                                        </h6>
                                        <p class="text-muted small mb-2">${item.product.category.name}</p>
                                        <p class="fw-bold text-success mb-0">
                                            &#x20B9;<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/>
                                        </p>
                                    </div>

                                    <!-- Quantity Controls -->
                                    <div class="d-flex align-items-center gap-2">
                                        <form action="${pageContext.request.contextPath}/cart/update" method="post"
                                              class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <button type="submit" name="quantity" value="${item.quantity - 1}"
                                                    class="btn btn-outline-secondary btn-sm">-</button>
                                            <span class="px-3 fw-bold">${item.quantity}</span>
                                            <button type="submit" name="quantity" value="${item.quantity + 1}"
                                                    class="btn btn-outline-secondary btn-sm">+</button>
                                        </form>
                                    </div>

                                    <!-- Subtotal -->
                                    <div class="text-end" style="min-width: 100px;">
                                        <p class="fw-bold text-dark mb-1">
                                            &#x20B9;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                        </p>
                                        <form action="${pageContext.request.contextPath}/cart/remove/${item.id}" method="post">
                                            <button type="submit" class="btn btn-link text-danger p-0 small">
                                                <i class="fas fa-trash-alt me-1"></i>Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="mt-3">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-dark">
                            <i class="fas fa-arrow-left me-2"></i>Continue Shopping
                        </a>
                    </div>
                </div>

                <!-- Order Summary -->
                <div class="col-lg-4">
                    <div class="card border-0 shadow-sm">
                        <div class="card-header bg-dark text-white fw-bold">
                            <i class="fas fa-receipt me-2"></i>Order Summary
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Items (${cart.totalItems})</span>
                                <span class="fw-semibold">&#x20B9;<fmt:formatNumber value="${cart.totalPrice}" pattern="#,##0.00"/></span>
                            </div>
                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Delivery</span>
                                <span class="text-success fw-semibold">FREE</span>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-between mb-3">
                                <span class="fw-bold fs-5">Total</span>
                                <span class="fw-bold fs-5 text-success">
                                    &#x20B9;<fmt:formatNumber value="${cart.totalPrice}" pattern="#,##0.00"/>
                                </span>
                            </div>
                            <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success w-100 py-2 fw-bold">
                                <i class="fas fa-lock me-2"></i>Proceed to Checkout
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #${order.id} - Zynkart</title>
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
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders">My Orders</a></li>
            <li class="breadcrumb-item active">Order #${order.id}</li>
        </ol>
    </nav>

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
        <!-- Items -->
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                    <span class="fw-bold"><i class="fas fa-box me-2"></i>Order #${order.id}</span>
                    <c:choose>
                        <c:when test="${order.status == 'PENDING'}"><span class="badge bg-warning text-dark">${order.status.displayName}</span></c:when>
                        <c:when test="${order.status == 'CONFIRMED'}"><span class="badge bg-info text-dark">${order.status.displayName}</span></c:when>
                        <c:when test="${order.status == 'SHIPPED'}"><span class="badge bg-primary">${order.status.displayName}</span></c:when>
                        <c:when test="${order.status == 'DELIVERED'}"><span class="badge bg-success">${order.status.displayName}</span></c:when>
                        <c:when test="${order.status == 'CANCELLED'}"><span class="badge bg-danger">${order.status.displayName}</span></c:when>
                    </c:choose>
                </div>
                <div class="card-body p-0">
                    <c:forEach items="${order.orderItems}" var="item">
                        <div class="d-flex align-items-center gap-3 p-3 border-bottom">
                            <c:if test="${not empty item.product.imageUrl}">
                                <img src="${item.product.imageUrl}" class="rounded-2"
                                     style="width:70px;height:70px;object-fit:cover;" alt="${item.product.name}">
                            </c:if>
                            <div class="flex-grow-1">
                                <h6 class="fw-bold mb-1">${item.product.name}</h6>
                                <p class="text-muted small mb-1">&#x20B9;<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/> x ${item.quantity}</p>
                            </div>
                            <span class="fw-bold text-success">
                                &#x20B9;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                            </span>
                        </div>
                    </c:forEach>
                    <div class="p-3 d-flex justify-content-between fw-bold fs-5">
                        <span>Total Paid</span>
                        <span class="text-success">&#x20B9;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                    </div>
                </div>
            </div>

            <!-- Cancel Button -->
            <c:if test="${order.cancellable}">
                <form action="${pageContext.request.contextPath}/orders/${order.id}/cancel" method="post"
                      onsubmit="return confirm('Are you sure you want to cancel this order?');">
                    <button type="submit" class="btn btn-outline-danger">
                        <i class="fas fa-times-circle me-2"></i>Cancel Order
                    </button>
                </form>
            </c:if>
        </div>

        <!-- Order Info -->
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm mb-3">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="fas fa-info-circle me-2"></i>Order Details
                </div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-5 text-muted">Order ID</dt>
                        <dd class="col-sm-7 fw-bold">#${order.id}</dd>
                        <dt class="col-sm-5 text-muted">Payment</dt>
                        <dd class="col-sm-7">${order.paymentMethod}</dd>
                        <dt class="col-sm-5 text-muted">Status</dt>
                        <dd class="col-sm-7">${order.status.displayName}</dd>
                    </dl>
                </div>
            </div>

            <div class="card border-0 shadow-sm">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="fas fa-map-marker-alt me-2"></i>Shipping Address
                </div>
                <div class="card-body">
                    <p class="mb-0 text-secondary">${order.shippingAddress}</p>
                </div>
            </div>

            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-dark w-100">
                    <i class="fas fa-arrow-left me-2"></i>Back to Orders
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container py-4">
    <h3 class="fw-bold mb-4"><i class="fas fa-credit-card me-2 text-warning"></i>Checkout</h3>

    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="fas fa-exclamation-circle me-2"></i>${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">
        <!-- Checkout Form -->
        <div class="col-lg-7">
            <form action="${pageContext.request.contextPath}/order/place" method="post" id="checkoutForm" novalidate>
                <!-- Shipping Address -->
                <div class="card border-0 shadow-sm mb-4">
                    <div class="card-header bg-dark text-white fw-bold">
                        <i class="fas fa-map-marker-alt me-2"></i>Shipping Address
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Full Address *</label>
                            <textarea name="shippingAddress" class="form-control" rows="3"
                                      placeholder="House no., Street, City, State, PIN code" required>${user.address}</textarea>
                            <div class="invalid-feedback">Please enter a shipping address.</div>
                        </div>
                    </div>
                </div>

                <!-- Payment Method -->
                <div class="card border-0 shadow-sm mb-4">
                    <div class="card-header bg-dark text-white fw-bold">
                        <i class="fas fa-wallet me-2"></i>Payment Method
                    </div>
                    <div class="card-body">
                        <div class="d-flex gap-3">
                            <div class="payment-option flex-fill">
                                <input type="radio" class="btn-check" name="paymentMethod" id="cod" value="COD" checked>
                                <label class="btn btn-outline-dark w-100 py-3" for="cod">
                                    <i class="fas fa-money-bill-wave d-block fs-4 mb-1"></i>
                                    Cash on Delivery
                                </label>
                            </div>
                            <div class="payment-option flex-fill">
                                <input type="radio" class="btn-check" name="paymentMethod" id="upi" value="UPI">
                                <label class="btn btn-outline-dark w-100 py-3" for="upi">
                                    <i class="fas fa-mobile-alt d-block fs-4 mb-1"></i>
                                    UPI Payment
                                </label>
                            </div>
                            <div class="payment-option flex-fill">
                                <input type="radio" class="btn-check" name="paymentMethod" id="card" value="Card">
                                <label class="btn btn-outline-dark w-100 py-3" for="card">
                                    <i class="fas fa-credit-card d-block fs-4 mb-1"></i>
                                    Debit/Credit Card
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-success btn-lg w-100 py-3 fw-bold">
                    <i class="fas fa-check-circle me-2"></i>Place Order
                </button>
            </form>
        </div>

        <!-- Order Summary -->
        <div class="col-lg-5">
            <div class="card border-0 shadow-sm sticky-top" style="top: 80px;">
                <div class="card-header bg-dark text-white fw-bold">
                    <i class="fas fa-shopping-bag me-2"></i>Your Items (${cart.totalItems})
                </div>
                <div class="card-body p-0">
                    <div class="overflow-auto" style="max-height: 300px;">
                        <c:forEach items="${cart.cartItems}" var="item">
                            <div class="d-flex align-items-center gap-2 p-3 border-bottom">
                                <c:if test="${not empty item.product.imageUrl}">
                                    <img src="${item.product.imageUrl}" class="rounded" style="width:50px;height:50px;object-fit:cover;" alt="">
                                </c:if>
                                <div class="flex-grow-1">
                                    <p class="mb-0 small fw-semibold">${item.product.name}</p>
                                    <p class="mb-0 text-muted small">Qty: ${item.quantity}</p>
                                </div>
                                <span class="fw-bold text-success small">
                                    &#x20B9;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                </span>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="p-3">
                        <div class="d-flex justify-content-between mb-1">
                            <span class="text-muted">Subtotal</span>
                            <span>&#x20B9;<fmt:formatNumber value="${cart.totalPrice}" pattern="#,##0.00"/></span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Delivery</span>
                            <span class="text-success">FREE</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between">
                            <span class="fw-bold fs-5">Total</span>
                            <span class="fw-bold fs-5 text-success">
                                &#x20B9;<fmt:formatNumber value="${cart.totalPrice}" pattern="#,##0.00"/>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        'use strict';
        const form = document.getElementById('checkoutForm');
        form.addEventListener('submit', function (event) {
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

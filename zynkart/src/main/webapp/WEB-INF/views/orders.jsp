<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container py-4">
    <h3 class="fw-bold mb-4"><i class="fas fa-box me-2 text-warning"></i>My Orders</h3>

    <c:if test="${not empty successMsg}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="fas fa-check-circle me-2"></i>${successMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="text-center py-5">
                <i class="fas fa-box-open fs-1 text-muted mb-3 d-block"></i>
                <h5 class="text-muted">You haven't placed any orders yet</h5>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-dark mt-2">
                    <i class="fas fa-shopping-bag me-2"></i>Start Shopping
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm">
                <div class="table-responsive">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>Items</th>
                                <th>Total</th>
                                <th>Payment</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orders}" var="order">
                                <tr>
                                    <td class="fw-bold">#${order.id}</td>
                                    <td>${order.createdAt.toLocalDate()}</td>
                                    <td>${order.orderItems.size()} item(s)</td>
                                    <td class="fw-bold text-success">
                                        &#x20B9;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                                    </td>
                                    <td>${order.paymentMethod}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.status == 'PENDING'}">
                                                <span class="badge bg-warning text-dark">${order.status.displayName}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'CONFIRMED'}">
                                                <span class="badge bg-info text-dark">${order.status.displayName}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'SHIPPED'}">
                                                <span class="badge bg-primary">${order.status.displayName}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'DELIVERED'}">
                                                <span class="badge bg-success">${order.status.displayName}</span>
                                            </c:when>
                                            <c:when test="${order.status == 'CANCELLED'}">
                                                <span class="badge bg-danger">${order.status.displayName}</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/orders/${order.id}"
                                           class="btn btn-sm btn-outline-dark">
                                            <i class="fas fa-eye me-1"></i>View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

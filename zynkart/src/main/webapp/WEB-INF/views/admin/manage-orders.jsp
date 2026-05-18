<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders - Zynkart Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../header.jsp"/>

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold mb-0"><i class="fas fa-shopping-bag me-2 text-warning"></i>Manage Orders</h4>
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

    <div class="card border-0 shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Items</th>
                        <th>Total</th>
                        <th>Payment</th>
                        <th>Status</th>
                        <th>Update Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orders}" var="order">
                        <tr>
                            <td class="fw-bold">#${order.id}</td>
                            <td>
                                <span class="fw-semibold">${order.user.fullName}</span><br>
                                <small class="text-muted">${order.user.email}</small>
                            </td>
                            <td>${order.orderItems.size()}</td>
                            <td class="fw-bold text-success">
                                &#x20B9;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                            </td>
                            <td>${order.paymentMethod}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.status == 'PENDING'}"><span class="badge bg-warning text-dark">${order.status.displayName}</span></c:when>
                                    <c:when test="${order.status == 'CONFIRMED'}"><span class="badge bg-info text-dark">${order.status.displayName}</span></c:when>
                                    <c:when test="${order.status == 'SHIPPED'}"><span class="badge bg-primary">${order.status.displayName}</span></c:when>
                                    <c:when test="${order.status == 'DELIVERED'}"><span class="badge bg-success">${order.status.displayName}</span></c:when>
                                    <c:when test="${order.status == 'CANCELLED'}"><span class="badge bg-danger">${order.status.displayName}</span></c:when>
                                </c:choose>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/orders/status/${order.id}"
                                      method="post" class="d-flex gap-1">
                                    <select name="status" class="form-select form-select-sm" style="width:140px;">
                                        <c:forEach items="${orderStatuses}" var="s">
                                            <option value="${s}" ${order.status == s ? 'selected' : ''}>${s.displayName}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-dark">
                                        <i class="fas fa-check"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty orders}">
                        <tr><td colspan="7" class="text-center text-muted py-4">No orders yet.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

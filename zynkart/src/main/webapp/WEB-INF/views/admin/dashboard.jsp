<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Zynkart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="../header.jsp"/>

<div class="container-fluid py-4">
    <div class="row g-4">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2">
            <div class="card border-0 shadow-sm">
                <div class="card-body p-0">
                    <div class="p-3 bg-dark text-white rounded-top">
                        <i class="fas fa-user-shield me-2"></i><strong>Admin Panel</strong>
                    </div>
                    <ul class="nav flex-column p-2">
                        <li class="nav-item">
                            <a class="nav-link active rounded fw-semibold" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link rounded" href="${pageContext.request.contextPath}/admin/products">
                                <i class="fas fa-boxes me-2"></i>Products
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link rounded" href="${pageContext.request.contextPath}/admin/categories">
                                <i class="fas fa-tags me-2"></i>Categories
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link rounded" href="${pageContext.request.contextPath}/admin/orders">
                                <i class="fas fa-shopping-bag me-2"></i>Orders
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link rounded text-danger" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10">
            <h4 class="fw-bold mb-4"><i class="fas fa-tachometer-alt me-2 text-warning"></i>Dashboard</h4>

            <!-- Stats Cards -->
            <div class="row g-3 mb-4">
                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm stat-card stat-card-blue">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-primary text-white rounded-3">
                                <i class="fas fa-boxes"></i>
                            </div>
                            <div>
                                <p class="text-muted small mb-0">Total Products</p>
                                <h3 class="fw-bold mb-0">${totalProducts}</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm stat-card">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-success text-white rounded-3">
                                <i class="fas fa-shopping-bag"></i>
                            </div>
                            <div>
                                <p class="text-muted small mb-0">Total Orders</p>
                                <h3 class="fw-bold mb-0">${totalOrders}</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm stat-card">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-warning text-white rounded-3">
                                <i class="fas fa-users"></i>
                            </div>
                            <div>
                                <p class="text-muted small mb-0">Registered Users</p>
                                <h3 class="fw-bold mb-0">${totalUsers}</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-xl-3">
                    <div class="card border-0 shadow-sm stat-card">
                        <div class="card-body d-flex align-items-center gap-3">
                            <div class="stat-icon bg-danger text-white rounded-3">
                                <i class="fas fa-clock"></i>
                            </div>
                            <div>
                                <p class="text-muted small mb-0">Pending Orders</p>
                                <h3 class="fw-bold mb-0">${pendingOrders}</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Orders -->
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                    <span class="fw-bold"><i class="fas fa-list me-2"></i>Recent Orders</span>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-warning btn-sm fw-bold">View All</a>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentOrders}" var="order">
                                <tr>
                                    <td class="fw-bold">#${order.id}</td>
                                    <td>${order.user.fullName}</td>
                                    <td class="text-success fw-bold">
                                        &#x20B9;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                                    </td>
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
                                        <a href="${pageContext.request.contextPath}/orders/${order.id}" class="btn btn-sm btn-outline-dark">View</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentOrders}">
                                <tr><td colspan="5" class="text-center text-muted py-3">No orders yet.</td></tr>
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
</body>
</html>

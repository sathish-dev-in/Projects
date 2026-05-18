<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Products - Zynkart Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../header.jsp"/>

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold mb-0"><i class="fas fa-boxes me-2 text-warning"></i>Manage Products</h4>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-arrow-left me-1"></i>Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-dark btn-sm">
                <i class="fas fa-plus me-1"></i>Add Product
            </a>
        </div>
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

    <div class="card border-0 shadow-sm">
        <div class="table-responsive">
            <table class="table table-hover mb-0 align-middle" id="productsTable">
                <thead class="table-dark">
                    <tr>
                        <th>#</th>
                        <th>Image</th>
                        <th>Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="product" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty product.imageUrl}">
                                        <img src="${product.imageUrl}" class="rounded" style="width:50px;height:50px;object-fit:cover;" alt="">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="bg-light rounded d-flex align-items-center justify-content-center" style="width:50px;height:50px;">
                                            <i class="fas fa-image text-muted small"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="fw-semibold">${product.name}</td>
                            <td><span class="badge bg-secondary">${product.category.name}</span></td>
                            <td class="fw-bold text-success">
                                &#x20B9;<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.stock <= 0}">
                                        <span class="text-danger fw-bold">${product.stock}</span>
                                    </c:when>
                                    <c:when test="${product.stock <= 5}">
                                        <span class="text-warning fw-bold">${product.stock}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>${product.stock}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.active}">
                                        <span class="badge bg-success">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">Inactive</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/products/edit/${product.id}"
                                   class="btn btn-sm btn-outline-primary me-1">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/products/delete/${product.id}"
                                      method="post" class="d-inline"
                                      onsubmit="return confirm('Deactivate this product?');">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                        <i class="fas fa-ban"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty products}">
                        <tr><td colspan="8" class="text-center text-muted py-4">No products found.</td></tr>
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

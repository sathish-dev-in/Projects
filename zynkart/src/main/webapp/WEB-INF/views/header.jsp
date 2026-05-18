<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow">
    <div class="container">
        <a class="navbar-brand fw-bold fs-4" href="${pageContext.request.contextPath}/">
            <i class="fas fa-shopping-bag me-2 text-warning"></i>Zynkart
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a>
                </li>
            </ul>
            <ul class="navbar-nav align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <li class="nav-item me-2">
                            <a class="nav-link position-relative" href="${pageContext.request.contextPath}/cart">
                                <i class="fas fa-shopping-cart fs-5"></i>
                                <c:if test="${sessionScope.cartCount > 0}">
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark">
                                        ${sessionScope.cartCount}
                                    </span>
                                </c:if>
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" data-bs-toggle="dropdown">
                                <i class="fas fa-user-circle me-1"></i>${sessionScope.loggedInUser.fullName}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <c:if test="${sessionScope.loggedInUser.admin}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">
                                        <i class="fas fa-tachometer-alt me-2 text-primary"></i>Admin Panel
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                </c:if>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">
                                    <i class="fas fa-box me-2 text-success"></i>My Orders
                                </a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt me-2"></i>Logout
                                </a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <i class="fas fa-sign-in-alt me-1"></i>Login
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-warning btn-sm ms-2 fw-bold" href="${pageContext.request.contextPath}/register">
                                Register
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

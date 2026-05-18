<%@ page contentType="text/html;charset=UTF-8" %>
<footer class="bg-dark text-light py-5 mt-5">
    <div class="container">
        <div class="row g-4">
            <div class="col-md-4">
                <h5 class="fw-bold text-warning mb-3">
                    <i class="fas fa-shopping-bag me-2"></i>Zynkart
                </h5>
                <p class="text-secondary small">Your one-stop destination for quality products at unbeatable prices. Shop smarter, live better.</p>
            </div>
            <div class="col-md-4">
                <h6 class="fw-bold mb-3">Quick Links</h6>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/" class="text-secondary text-decoration-none">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="text-secondary text-decoration-none">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders" class="text-secondary text-decoration-none">My Orders</a></li>
                </ul>
            </div>
            <div class="col-md-4">
                <h6 class="fw-bold mb-3">Contact Us</h6>
                <p class="text-secondary small mb-1"><i class="fas fa-envelope me-2"></i>support@zynkart.com</p>
                <p class="text-secondary small mb-1"><i class="fas fa-phone me-2"></i>+91 98765 43210</p>
                <p class="text-secondary small"><i class="fas fa-map-marker-alt me-2"></i>Mumbai, Maharashtra, India</p>
            </div>
        </div>
        <hr class="border-secondary mt-4">
        <div class="text-center text-secondary small">
            &copy; 2024 Zynkart. Built with Spring Boot, MySQL &amp; JSP.
        </div>
    </div>
</footer>

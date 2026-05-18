<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty productId ? 'Add' : 'Edit'} Product - Zynkart Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="../header.jsp"/>

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold mb-0">
            <i class="fas fa-${empty productId ? 'plus' : 'edit'} me-2 text-warning"></i>
            ${empty productId ? 'Add New Product' : 'Edit Product'}
        </h4>
        <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary btn-sm">
            <i class="fas fa-arrow-left me-1"></i>Back
        </a>
    </div>

    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm">
                <div class="card-body p-4">
                    <c:set var="formAction" value="${empty productId ?
                        pageContext.request.contextPath.concat('/admin/products/add') :
                        pageContext.request.contextPath.concat('/admin/products/edit/').concat(productId)}"/>

                    <form:form action="${formAction}" method="post" modelAttribute="productDTO" novalidate="true" id="productForm">
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label fw-semibold">Product Name *</label>
                                <form:input path="name" cssClass="form-control" placeholder="Enter product name" required="true"/>
                                <form:errors path="name" cssClass="text-danger small"/>
                            </div>
                            <div class="col-12">
                                <label class="form-label fw-semibold">Description</label>
                                <form:textarea path="description" cssClass="form-control" rows="3" placeholder="Product description"/>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Price (&#x20B9;) *</label>
                                <div class="input-group">
                                    <span class="input-group-text">&#x20B9;</span>
                                    <form:input path="price" type="number" cssClass="form-control" placeholder="0.00"
                                               step="0.01" min="0.01" required="true"/>
                                </div>
                                <form:errors path="price" cssClass="text-danger small"/>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Stock Quantity *</label>
                                <form:input path="stock" type="number" cssClass="form-control" placeholder="0"
                                           min="0" required="true"/>
                                <form:errors path="stock" cssClass="text-danger small"/>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Category *</label>
                                <form:select path="categoryId" cssClass="form-select" required="true">
                                    <form:option value="" label="-- Select Category --"/>
                                    <form:options items="${categories}" itemValue="id" itemLabel="name"/>
                                </form:select>
                                <form:errors path="categoryId" cssClass="text-danger small"/>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-semibold">Image URL</label>
                                <form:input path="imageUrl" cssClass="form-control" placeholder="https://example.com/image.jpg"/>
                                <small class="text-muted">Optional — paste a direct image link</small>
                            </div>

                            <!-- Image Preview -->
                            <div class="col-12" id="imagePreviewContainer" style="display:none;">
                                <label class="form-label fw-semibold">Image Preview</label>
                                <div>
                                    <img id="imagePreview" src="" alt="Preview" class="img-thumbnail"
                                         style="max-height:150px; max-width:200px;">
                                </div>
                            </div>
                        </div>

                        <div class="mt-4 d-flex gap-2">
                            <button type="submit" class="btn btn-dark px-4 fw-bold">
                                <i class="fas fa-save me-2"></i>${empty productId ? 'Add Product' : 'Update Product'}
                            </button>
                            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary px-4">
                                Cancel
                            </a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const imageUrlInput = document.querySelector('input[id$="imageUrl"]');
    const previewContainer = document.getElementById('imagePreviewContainer');
    const previewImg = document.getElementById('imagePreview');

    function updatePreview() {
        const url = imageUrlInput.value.trim();
        if (url) {
            previewImg.src = url;
            previewContainer.style.display = 'block';
        } else {
            previewContainer.style.display = 'none';
        }
    }

    imageUrlInput.addEventListener('input', updatePreview);
    window.addEventListener('load', updatePreview);
</script>
</body>
</html>

/* =====================================================
   Zynkart - Application JavaScript
   ===================================================== */

document.addEventListener('DOMContentLoaded', function () {

    // ── Auto-dismiss alerts after 4 seconds ──────────────────
    document.querySelectorAll('.alert').forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) bsAlert.close();
        }, 4000);
    });

    // ── Bootstrap form validation ─────────────────────────────
    document.querySelectorAll('form.needs-validation').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // ── Confirm before delete/cancel forms ───────────────────
    document.querySelectorAll('[data-confirm]').forEach(function (el) {
        el.addEventListener('click', function (e) {
            if (!confirm(el.getAttribute('data-confirm'))) {
                e.preventDefault();
            }
        });
    });

    // ── Quantity input — prevent negative values ──────────────
    document.querySelectorAll('input[type="number"]').forEach(function (input) {
        input.addEventListener('change', function () {
            const min = parseInt(this.getAttribute('min') || '0');
            const max = parseInt(this.getAttribute('max') || '9999');
            let val = parseInt(this.value);
            if (isNaN(val) || val < min) this.value = min;
            if (val > max) this.value = max;
        });
    });

    // ── Tooltip initialization ────────────────────────────────
    if (typeof bootstrap !== 'undefined') {
        document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(function (el) {
            new bootstrap.Tooltip(el);
        });
    }

    // ── Add-to-cart button loading state ─────────────────────
    document.querySelectorAll('form[action*="/cart/add"]').forEach(function (form) {
        form.addEventListener('submit', function () {
            const btn = this.querySelector('button[type="submit"]');
            if (btn) {
                btn.disabled = true;
                btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1" role="status"></span>Adding...';
            }
        });
    });

    // ── Navbar active link highlight ──────────────────────────
    const currentPath = window.location.pathname;
    document.querySelectorAll('.navbar .nav-link').forEach(function (link) {
        const href = link.getAttribute('href');
        if (href && currentPath.startsWith(href) && href !== '/') {
            link.classList.add('active');
        }
    });
});

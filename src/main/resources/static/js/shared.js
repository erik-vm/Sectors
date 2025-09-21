/**
 * Sectors Management System - Shared JavaScript
 * Global functions and utilities used across the application
 */

// Global utilities
document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide flash messages after 5 seconds
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            if (alert && alert.parentNode) {
                alert.classList.remove('show');
                setTimeout(function() {
                    if (alert && alert.parentNode) {
                        alert.remove();
                    }
                }, 150);
            }
        }, 5000);
    });

    // Enhanced form validation
    const forms = document.querySelectorAll('form[data-validate="true"]');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Smooth scrolling for anchor links
    const anchorLinks = document.querySelectorAll('a[href^="#"]');
    anchorLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                e.preventDefault();
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Loading state for forms (skip login/logout forms)
    const submitButtons = document.querySelectorAll('button[type="submit"]:not([data-skip-loading])');
    submitButtons.forEach(function(button) {
        button.setAttribute('data-original-text', button.innerHTML);

        const form = button.closest('form');
        if (form) {
            form.addEventListener('submit', function(e) {
                // Only show loading if form is valid
                if (form.checkValidity()) {
                    button.disabled = true;
                    button.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Processing...';

                    // Reset button after 10 seconds as failsafe
                    setTimeout(() => {
                        if (button.disabled) {
                            button.disabled = false;
                            button.innerHTML = button.getAttribute('data-original-text') || 'Submit';
                        }
                    }, 10000);
                }
            });
        }
    });
});

// Utility functions
window.SectorsApp = {
    // Show confirmation dialog
    confirm: function(message, callback) {
        if (confirm(message)) {
            if (typeof callback === 'function') {
                callback();
            }
            return true;
        }
        return false;
    },

    // Show loading state
    showLoading: function(element) {
        if (element) {
            element.classList.add('loading');
        }
    },

    // Hide loading state
    hideLoading: function(element) {
        if (element) {
            element.classList.remove('loading');
        }
    },

    // Format date
    formatDate: function(date) {
        return new Intl.DateTimeFormat('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        }).format(new Date(date));
    },

    // Debounce function
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
};
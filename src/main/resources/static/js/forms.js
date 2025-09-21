/**
 * Sectors Management System - Forms JavaScript
 * Interactive functionality for forms and submissions
 */

// Terms Modal Functions
function openTermsModal() {
    const modal = new bootstrap.Modal(document.getElementById('termsModal'));
    modal.show();
}

function agreeToTerms() {
    const checkbox = document.getElementById('agreeToTerms');
    const submitBtn = document.getElementById('submitBtn');

    checkbox.disabled = false;
    checkbox.checked = true;
    submitBtn.disabled = false;

    const modal = bootstrap.Modal.getInstance(document.getElementById('termsModal'));
    modal.hide();

    const termsCard = checkbox.closest('.card');
    termsCard.classList.remove('border-primary');
    termsCard.classList.add('border-success');
}

// Sector Selection Handler
function handleSectorSelection(checkbox) {
    const isChecked = checkbox.checked;
    const sectorId = checkbox.value;

    if (checkbox.classList.contains('child-sector') || checkbox.classList.contains('grandchild-sector')) {
        if (isChecked) {
            const parentId = checkbox.getAttribute('data-parent');
            if (parentId) {
                const parentCheckbox = document.querySelector('input[value="' + parentId + '"]');
                if (parentCheckbox) {
                    parentCheckbox.checked = true;
                }
            }

            if (checkbox.classList.contains('grandchild-sector')) {
                const grandparentId = checkbox.getAttribute('data-grandparent');
                if (grandparentId) {
                    const grandparentCheckbox = document.querySelector('input[value="' + grandparentId + '"]');
                    if (grandparentCheckbox) {
                        grandparentCheckbox.checked = true;
                    }
                }
            }
        }
    } else if (checkbox.classList.contains('parent-sector')) {
        if (!isChecked) {
            const childCheckboxes = document.querySelectorAll('input[data-parent="' + sectorId + '"]');
            childCheckboxes.forEach(function(childCheckbox) {
                childCheckbox.checked = false;

                const grandchildCheckboxes = document.querySelectorAll('input[data-parent="' + childCheckbox.value + '"]');
                grandchildCheckboxes.forEach(function(grandchildCheckbox) {
                    grandchildCheckbox.checked = false;
                });
            });
        }
    }
}

// Authentication Tab Switching
function initAuthTabs() {
    const urlParams = new URLSearchParams(window.location.search);
    const registerTabDiv = document.getElementById('register');
    const hasRegistrationErrors = registerTabDiv && registerTabDiv.querySelector('.alert-danger') !== null;

    if (urlParams.get('tab') === 'register' || hasRegistrationErrors) {
        const registerTab = new bootstrap.Tab(document.getElementById('register-tab'));
        registerTab.show();
    }

    if (urlParams.has('registered')) {
        setTimeout(() => {
            const signinTab = new bootstrap.Tab(document.getElementById('signin-tab'));
            signinTab.show();
        }, 3000);
    }
}

// Initialize form functionality when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize authentication tabs if they exist
    if (document.getElementById('authTabs')) {
        initAuthTabs();
    }

    // Terms agreement checkbox monitoring
    const agreeToTermsCheckbox = document.getElementById('agreeToTerms');
    if (agreeToTermsCheckbox) {
        agreeToTermsCheckbox.addEventListener('change', function() {
            const submitBtn = document.getElementById('submitBtn');
            if (submitBtn) {
                submitBtn.disabled = !this.checked;
            }
        });
    }

    // Handle collapse icon rotation
    const collapseElements = document.querySelectorAll('.collapse');
    collapseElements.forEach(function(collapseEl) {
        collapseEl.addEventListener('show.bs.collapse', function() {
            const header = document.querySelector('[data-bs-target="#' + this.id + '"]');
            if (header) {
                const icon = header.querySelector('.collapse-icon');
                if (icon) {
                    icon.style.transform = 'rotate(180deg)';
                }
            }
        });

        collapseEl.addEventListener('hide.bs.collapse', function() {
            const header = document.querySelector('[data-bs-target="#' + this.id + '"]');
            if (header) {
                const icon = header.querySelector('.collapse-icon');
                if (icon) {
                    icon.style.transform = 'rotate(0deg)';
                }
            }
        });
    });

    // Handle sector selection
    const sectorCheckboxes = document.querySelectorAll('.sector-checkbox');
    sectorCheckboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            handleSectorSelection(this);
        });
    });

    // Enhanced form validation
    const validateForms = document.querySelectorAll('.needs-validation');
    validateForms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
});
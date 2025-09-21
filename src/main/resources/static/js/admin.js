/**
 * Sectors Management System - Admin JavaScript
 * Interactive functionality for admin interfaces
 */

// Sector Form Functions
function initSectorForm() {
    const parentSelect = document.getElementById('parentId');
    if (parentSelect) {
        parentSelect.addEventListener('change', function() {
            const selectedOption = this.options[this.selectedIndex];
            if (selectedOption.value) {
                console.log('Parent selected, updating level display');
            }
        });
    }
}

// User Management Functions
function confirmUserAction(action, username) {
    return SectorsApp.confirm(`Are you sure you want to ${action} user "${username}"?`);
}

function confirmSectorAction(action, sectorName) {
    return SectorsApp.confirm(`Are you sure you want to ${action} sector "${sectorName}" and all its children?`);
}

// Initialize admin functionality when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize sector form if present
    if (document.getElementById('parentId')) {
        initSectorForm();
    }

    // Enhanced table interactions
    const adminTables = document.querySelectorAll('.table-hover tbody tr');
    adminTables.forEach(function(row) {
        row.addEventListener('click', function(e) {
            if (!e.target.closest('button') && !e.target.closest('form')) {
                const viewLink = this.querySelector('a[href*="/view"]');
                if (viewLink) {
                    window.location.href = viewLink.href;
                }
            }
        });
    });

    // Auto-refresh for real-time data (if needed)
    const autoRefreshElements = document.querySelectorAll('[data-auto-refresh]');
    autoRefreshElements.forEach(function(element) {
        const interval = parseInt(element.getAttribute('data-auto-refresh')) || 30000;
        setInterval(function() {
            if (document.hidden) return; // Don't refresh when tab is not visible

            // Add refresh logic here if needed
            console.log('Auto-refresh triggered');
        }, interval);
    });
});
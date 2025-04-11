// src/main/resources/static/js/comun/app.js

import { notifySuccess, notifyError } from './config.js';

// Funciones comunes para toda la aplicación

// Función para inicializar la aplicación
function initApp() {
    console.log('Aplicación Orden con MIMO inicializada');
    
    // Agregar listeners globales
    document.addEventListener('DOMContentLoaded', () => {
        console.log('DOM cargado completamente');
        
        // Inicializar componentes globales
        initTooltips();
        setupNavigation();
    });
}

// Inicializar tooltips y otros componentes Bootstrap
function initTooltips() {
    // Si estamos usando Bootstrap, inicializamos los tooltips
    if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
        const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
        [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
    }
}

// Configurar navegación y menú activo
function setupNavigation() {
    // Marcar el elemento de navegación activo basado en la URL actual
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.includes(href) && href !== '/') {
            link.classList.add('active');
        } else if (href === '/' && currentPath === '/') {
            link.classList.add('active');
        }
    });
}

// Función para formatear fechas
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
}

// Exportar funciones
export {
    initApp,
    formatDate
};

// Inicializar la aplicación
initApp();
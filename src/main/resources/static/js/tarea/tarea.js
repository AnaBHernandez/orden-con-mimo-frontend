// src/main/resources/static/js/tarea/tarea.js

import { fetchAPI, notifySuccess, notifyError } from '../comun/config.js';
import { formatDate } from '../comun/app.js';

// Variables globales
let tareas = [];

// Función para cargar todas las tareas
async function cargarTareas() {
    try {
        // Mostrar indicador de carga
        const contenedorTareas = document.getElementById('contenedor-tareas');
        if (contenedorTareas) {
            contenedorTareas.innerHTML = '<div class="loading">Cargando tareas...</div>';
        }

        // Obtener tareas de la API
        tareas = await fetchAPI('tareas');
        console.log('Tareas cargadas:', tareas);
        
        // Renderizar las tareas
        renderizarTareas(tareas);
    } catch (error) {
        console.error('Error al cargar tareas:', error);
        notifyError('No se pudieron cargar las tareas');
    }
}

// Función para renderizar las tareas en la interfaz
function renderizarTareas(tareas) {
    const contenedorTareas = document.getElementById('contenedor-tareas');
    if (!contenedorTareas) {
        console.error('No se encontró el contenedor de tareas');
        return;
    }
    
    // Limpiar el contenedor
    contenedorTareas.innerHTML = '';
    
    // Si no hay tareas, mostrar mensaje
    if (!tareas || tareas.length === 0) {
        contenedorTareas.innerHTML = '<div class="sin-tareas">No hay tareas disponibles</div>';
        return;
    }
    
    // Renderizar cada tarea
    tareas.forEach(tarea => {
        const categoriaClass = tarea.categoria ? tarea.categoria.toLowerCase() : '';
        
        const tareaHTML = `
            <div class="tarea-card ${categoriaClass} ${tarea.completada ? 'completada' : ''}">
                <div class="tarea-header">
                    <h3>${tarea.titulo}</h3>
                    <div class="tarea-acciones">
                        <button class="btn-completar" data-id="${tarea.id}">
                            ${tarea.completada ? 'Desmarcar' : 'Completar'}
                        </button>
                        <button class="btn-editar" data-id="${tarea.id}">Editar</button>
                        <button class="btn-eliminar" data-id="${tarea.id}">Eliminar</button>
                    </div>
                </div>
                <p class="tarea-descripcion">${tarea.descripcion || ''}</p>
                <div class="tarea-footer">
                    <span class="tarea-categoria">${tarea.categoria || 'Sin categoría'}</span>
                    <span class="tarea-fecha">${formatDate(tarea.fechaCreacion)}</span>
                </div>
            </div>
        `;
        
        contenedorTareas.innerHTML += tareaHTML;
    });
    
    // Agregar event listeners
    document.querySelectorAll('.btn-completar').forEach(btn => {
        btn.addEventListener('click', handleCompletarTarea);
    });
    
    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', handleEditarTarea);
    });
    
    document.querySelectorAll('.btn-eliminar').forEach(btn => {
        btn.addEventListener('click', handleEliminarTarea);
    });
}

// Manejadores de eventos para los botones
async function handleCompletarTarea(event) {
    const tareaId = event.target.dataset.id;
    try {
        const tareaActualizada = await fetchAPI(`tareas/${tareaId}/completar`, { method: 'PUT' });
        notifySuccess(`Tarea ${tareaActualizada.completada ? 'completada' : 'marcada como pendiente'}`);
        cargarTareas(); // Recargar las tareas
    } catch (error) {
        notifyError('No se pudo actualizar el estado de la tarea');
    }
}

async function handleEditarTarea(event) {
    const tareaId = event.target.dataset.id;
    // Por ahora, solo mostramos un mensaje
    alert(`Editar tarea ${tareaId} - Funcionalidad en desarrollo`);
    
    // En el futuro, aquí iría la navegación a la página de edición
    // window.location.href = `/tareas/editar/${tareaId}`;
}

async function handleEliminarTarea(event) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
        return;
    }
    
    const tareaId = event.target.dataset.id;
    try {
        await fetchAPI(`tareas/${tareaId}`, { method: 'DELETE' });
        notifySuccess('Tarea eliminada correctamente');
        cargarTareas(); // Recargar las tareas
    } catch (error) {
        notifyError('No se pudo eliminar la tarea');
    }
}

// Función para filtrar tareas por categoría
async function filtrarTareasPorCategoria(categoria) {
    try {
        const tareasCategoria = await fetchAPI(`tareas/categorias/${categoria}`);
        renderizarTareas(tareasCategoria);
    } catch (error) {
        notifyError('No se pudieron filtrar las tareas');
    }
}

// Inicializar la página de tareas
document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando página de tareas');
    
    // Cargar tareas al iniciar
    cargarTareas();
    
    // Configurar eventos para el formulario de nueva tarea (si existe)
    const formNuevaTarea = document.getElementById('form-nueva-tarea');
    if (formNuevaTarea) {
        formNuevaTarea.addEventListener('submit', async (event) => {
            event.preventDefault();
            
            const nuevaTarea = {
                titulo: formNuevaTarea.titulo.value,
                descripcion: formNuevaTarea.descripcion.value,
                categoria: formNuevaTarea.categoria.value
            };
            
            try {
                await fetchAPI('tareas', {
                    method: 'POST',
                    body: JSON.stringify(nuevaTarea)
                });
                
                notifySuccess('Tarea creada correctamente');
                formNuevaTarea.reset();
                
                // Redirigir a la página de tareas
                window.location.href = '/tareas';
            } catch (error) {
                notifyError('No se pudo crear la tarea');
            }
        });
    }
    
    // Configurar filtros de categoría (si existen)
    const filtrosBotones = document.querySelectorAll('.filtro-categoria');
    filtrosBotones.forEach(boton => {
        boton.addEventListener('click', () => {
            const categoria = boton.dataset.categoria;
            if (categoria === 'todas') {
                cargarTareas();
            } else {
                filtrarTareasPorCategoria(categoria);
            }
            
            // Actualizar botón activo
            filtrosBotones.forEach(b => b.classList.remove('active'));
            boton.classList.add('active');
        });
    });
});

// Exportar funciones para poder usarlas desde otros módulos
export {
    cargarTareas,
    filtrarTareasPorCategoria
};
// src/main/resources/static/js/espacio/espacio.js

import { fetchAPI, notifySuccess, notifyError } from '../comun/config.js';
import { formatDate } from '../comun/app.js';

// Variables globales
let espacios = [];

// Función para cargar todos los espacios
async function cargarEspacios() {
    try {
        // Mostrar indicador de carga
        const contenedorEspacios = document.getElementById('contenedor-espacios');
        if (contenedorEspacios) {
            contenedorEspacios.innerHTML = '<div class="loading">Cargando espacios...</div>';
        }

        // Obtener espacios de la API
        espacios = await fetchAPI('espacios');
        console.log('Espacios cargados:', espacios);
        
        // Renderizar los espacios
        renderizarEspacios(espacios);
    } catch (error) {
        console.error('Error al cargar espacios:', error);
        notifyError('No se pudieron cargar los espacios');
    }
}

// Función para renderizar los espacios en la interfaz
function renderizarEspacios(espacios) {
    const contenedorEspacios = document.getElementById('contenedor-espacios');
    if (!contenedorEspacios) {
        console.error('No se encontró el contenedor de espacios');
        return;
    }
    
    // Limpiar el contenedor
    contenedorEspacios.innerHTML = '';
    
    // Si no hay espacios, mostrar mensaje
    if (!espacios || espacios.length === 0) {
        contenedorEspacios.innerHTML = '<div class="sin-espacios">No hay espacios disponibles</div>';
        return;
    }
    
    // Renderizar cada espacio
    espacios.forEach(espacio => {
        const espacioHTML = `
            <div class="espacio-card">
                <div class="espacio-header">
                    <h3>${espacio.nombre}</h3>
                    <div class="espacio-acciones">
                        <button class="btn-ver" data-id="${espacio.id}">Ver</button>
                        <button class="btn-editar" data-id="${espacio.id}">Editar</button>
                        <button class="btn-eliminar" data-id="${espacio.id}">Eliminar</button>
                    </div>
                </div>
                <p class="espacio-descripcion">${espacio.descripcion || ''}</p>
                <div class="espacio-footer">
                    <span class="espacio-fecha">Creado: ${formatDate(espacio.fechaCreacion)}</span>
                    <span class="espacio-tareas">Tareas: ${espacio.tareas ? espacio.tareas.length : 0}</span>
                </div>
            </div>
        `;
        
        contenedorEspacios.innerHTML += espacioHTML;
    });
    
    // Agregar event listeners
    document.querySelectorAll('.btn-ver').forEach(btn => {
        btn.addEventListener('click', handleVerEspacio);
    });
    
    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', handleEditarEspacio);
    });
    
    document.querySelectorAll('.btn-eliminar').forEach(btn => {
        btn.addEventListener('click', handleEliminarEspacio);
    });
}

// Manejadores de eventos para los botones
function handleVerEspacio(event) {
    const espacioId = event.target.dataset.id;
    window.location.href = `/espacios/${espacioId}`;
}

function handleEditarEspacio(event) {
    const espacioId = event.target.dataset.id;
    // Por ahora, solo mostramos un mensaje
    alert(`Editar espacio ${espacioId} - Funcionalidad en desarrollo`);
    
    // En el futuro, aquí iría la navegación a la página de edición
    // window.location.href = `/espacios/editar/${espacioId}`;
}

async function handleEliminarEspacio(event) {
    if (!confirm('¿Estás seguro de que deseas eliminar este espacio?')) {
        return;
    }
    
    const espacioId = event.target.dataset.id;
    try {
        await fetchAPI(`espacios/${espacioId}`, { method: 'DELETE' });
        notifySuccess('Espacio eliminado correctamente');
        cargarEspacios(); // Recargar los espacios
    } catch (error) {
        notifyError('No se pudo eliminar el espacio');
    }
}

// Función para cargar tareas de un espacio específico
async function cargarTareasDeEspacio(espacioId) {
    try {
        const tareas = await fetchAPI(`espacios/${espacioId}/tareas`);
        console.log(`Tareas del espacio ${espacioId}:`, tareas);
        
        // Renderizar tareas (asumiendo que tenemos un contenedor para ellas)
        const contenedorTareas = document.getElementById('tareas-espacio');
        if (contenedorTareas) {
            if (tareas.length === 0) {
                contenedorTareas.innerHTML = '<div class="sin-tareas">Este espacio no tiene tareas</div>';
            } else {
                // Aquí renderizaríamos las tareas
                contenedorTareas.innerHTML = '';
                tareas.forEach(tarea => {
                    // Similar al renderizado de tareas en tarea.js
                    const categoriaClass = tarea.categoria ? tarea.categoria.toLowerCase() : '';
                    const tareaHTML = `
                        <div class="tarea-card ${categoriaClass} ${tarea.completada ? 'completada' : ''}">
                            <h4>${tarea.titulo}</h4>
                            <p>${tarea.descripcion || ''}</p>
                        </div>
                    `;
                    contenedorTareas.innerHTML += tareaHTML;
                });
            }
        }
        
        return tareas;
    } catch (error) {
        console.error(`Error al cargar tareas del espacio ${espacioId}:`, error);
        notifyError('No se pudieron cargar las tareas del espacio');
        return [];
    }
}

// Inicializar la página de espacios
document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando página de espacios');
    
    // Detectar si estamos en la página de listado de espacios
    const contenedorEspacios = document.getElementById('contenedor-espacios');
    if (contenedorEspacios) {
        cargarEspacios();
    }
    
    // Detectar si estamos en la página de detalle de un espacio
    const espacioDetalle = document.getElementById('espacio-detalle');
    if (espacioDetalle) {
        const espacioId = espacioDetalle.dataset.id;
        if (espacioId) {
            cargarTareasDeEspacio(espacioId);
        }
    }
    
    // Configurar eventos para el formulario de nuevo espacio (si existe)
    const formNuevoEspacio = document.getElementById('form-nuevo-espacio');
    if (formNuevoEspacio) {
        formNuevoEspacio.addEventListener('submit', async (event) => {
            event.preventDefault();
            
            const nuevoEspacio = {
                nombre: formNuevoEspacio.nombre.value,
                descripcion: formNuevoEspacio.descripcion.value
            };
            
            try {
                await fetchAPI('espacios', {
                    method: 'POST',
                    body: JSON.stringify(nuevoEspacio)
                });
                
                notifySuccess('Espacio creado correctamente');
                formNuevoEspacio.reset();
                
                // Redirigir a la página de espacios
                window.location.href = '/espacios';
            } catch (error) {
                notifyError('No se pudo crear el espacio');
            }
        });
    }
});

// Exportar funciones para poder usarlas desde otros módulos
export {
    cargarEspacios,
    cargarTareasDeEspacio
};
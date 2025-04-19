import { fetchAPI, notifySuccess, notifyError } from '../comun/config.js';
import { formatDate } from '../comun/app.js';

let tareas = [];

async function cargarTareas() {
    try {
        const contenedorTareas = document.getElementById('contenedor-tareas');
        if (contenedorTareas) {
            contenedorTareas.innerHTML = '<div class="loading">Cargando tareas...</div>';
        }

        tareas = await fetchAPI('tareas');
        console.log('Tareas cargadas:', tareas);
        
        renderizarTareas(tareas);
    } catch (error) {
        console.error('Error al cargar tareas:', error);
        notifyError('No se pudieron cargar las tareas');
    }
}

function renderizarTareas(tareas) {
    const contenedorTareas = document.getElementById('contenedor-tareas');
    if (!contenedorTareas) {
        console.error('No se encontró el contenedor de tareas');
        return;
    }
    
    contenedorTareas.innerHTML = '';
    
    if (!tareas || tareas.length === 0) {
        contenedorTareas.innerHTML = '<div class="sin-tareas">No hay tareas disponibles</div>';
        return;
    }
    
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

async function handleCompletarTarea(event) {
    const tareaId = event.target.dataset.id;
    try {
        const tareaActualizada = await fetchAPI(`tareas/${tareaId}/completar`, { method: 'PUT' });
        notifySuccess(`Tarea ${tareaActualizada.completada ? 'completada' : 'marcada como pendiente'}`);
        cargarTareas();
    } catch (error) {
        notifyError('No se pudo actualizar el estado de la tarea');
    }
}

async function handleEditarTarea(event) {
    const tareaId = event.target.dataset.id;
    alert(`Editar tarea ${tareaId} - Funcionalidad en desarrollo`);
}

async function handleEliminarTarea(event) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
        return;
    }
    
    const tareaId = event.target.dataset.id;
    try {
        await fetchAPI(`tareas/${tareaId}`, { method: 'DELETE' });
        notifySuccess('Tarea eliminada correctamente');
        cargarTareas();
    } catch (error) {
        notifyError('No se pudo eliminar la tarea');
    }
}

async function filtrarTareasPorCategoria(categoria) {
    try {
        const tareasCategoria = await fetchAPI(`tareas/categorias/${categoria}`);
        renderizarTareas(tareasCategoria);
    } catch (error) {
        notifyError('No se pudieron filtrar las tareas');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando página de tareas');
    
    cargarTareas();
    
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
                
                window.location.href = '/tareas';
            } catch (error) {
                notifyError('No se pudo crear la tarea');
            }
        });
    }
    
    const filtrosBotones = document.querySelectorAll('.filtro-categoria');
    filtrosBotones.forEach(boton => {
        boton.addEventListener('click', () => {
            const categoria = boton.dataset.categoria;
            if (categoria === 'todas') {
                cargarTareas();
            } else {
                filtrarTareasPorCategoria(categoria);
            }
            
            filtrosBotones.forEach(b => b.classList.remove('active'));
            boton.classList.add('active');
        });
    });
});

export {
    cargarTareas,
    filtrarTareasPorCategoria
};
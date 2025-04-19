import { fetchAPI, notifySuccess, notifyError } from '../comun/config.js';
import { formatDate } from '../comun/app.js';

let espacios = [];

async function cargarEspacios() {
    try {
        const contenedorEspacios = document.getElementById('contenedor-espacios');
        if (contenedorEspacios) {
            contenedorEspacios.innerHTML = '<div class="loading">Cargando espacios...</div>';
        }

        espacios = await fetchAPI('espacios');
        console.log('Espacios cargados:', espacios);
        
        renderizarEspacios(espacios);
    } catch (error) {
        console.error('Error al cargar espacios:', error);
        notifyError('No se pudieron cargar los espacios');
    }
}

function renderizarEspacios(espacios) {
    const contenedorEspacios = document.getElementById('contenedor-espacios');
    if (!contenedorEspacios) {
        console.error('No se encontró el contenedor de espacios');
        return;
    }
    
    contenedorEspacios.innerHTML = '';
    
    if (!espacios || espacios.length === 0) {
        contenedorEspacios.innerHTML = '<div class="sin-espacios">No hay espacios disponibles</div>';
        return;
    }
    
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

function handleVerEspacio(event) {
    const espacioId = event.target.dataset.id;
    window.location.href = `/espacios/${espacioId}`;
}

function handleEditarEspacio(event) {
    const espacioId = event.target.dataset.id;
    alert(`Editar espacio ${espacioId} - Funcionalidad en desarrollo`);
}

async function handleEliminarEspacio(event) {
    if (!confirm('¿Estás seguro de que deseas eliminar este espacio?')) {
        return;
    }
    
    const espacioId = event.target.dataset.id;
    try {
        await fetchAPI(`espacios/${espacioId}`, { method: 'DELETE' });
        notifySuccess('Espacio eliminado correctamente');
        cargarEspacios();
    } catch (error) {
        notifyError('No se pudo eliminar el espacio');
    }
}

async function cargarTareasDeEspacio(espacioId) {
    try {
        const tareas = await fetchAPI(`espacios/${espacioId}/tareas`);
        console.log(`Tareas del espacio ${espacioId}:`, tareas);
        
        const contenedorTareas = document.getElementById('tareas-espacio');
        if (contenedorTareas) {
            if (tareas.length === 0) {
                contenedorTareas.innerHTML = '<div class="sin-tareas">Este espacio no tiene tareas</div>';
            } else {
                contenedorTareas.innerHTML = '';
                tareas.forEach(tarea => {
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

document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando página de espacios');
    
    const contenedorEspacios = document.getElementById('contenedor-espacios');
    if (contenedorEspacios) {
        cargarEspacios();
    }
    
    const espacioDetalle = document.getElementById('espacio-detalle');
    if (espacioDetalle) {
        const espacioId = espacioDetalle.dataset.id;
        if (espacioId) {
            cargarTareasDeEspacio(espacioId);
        }
    }
    
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
                
                window.location.href = '/espacios';
            } catch (error) {
                notifyError('No se pudo crear el espacio');
            }
        });
    }
});

export {
    cargarEspacios,
    cargarTareasDeEspacio
};
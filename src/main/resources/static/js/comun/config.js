const API_BASE_URL = "http://localhost:8082/api";
const API_TIMEOUT = 5000;

const MOCK_MODE = false;

const MOCK_DATA = {
    tareas: [
        {
            id: 1,
            titulo: "Revisar gastos mensuales",
            descripcion: "Revisar los gastos del mes para identificar áreas de ahorro",
            categoria: "MIRATE",
            completada: false,
            fechaCreacion: new Date().toISOString()
        },
        {
            id: 2,
            titulo: "Planificar viaje de verano",
            descripcion: "Investigar destinos y presupuesto para vacaciones",
            categoria: "IMAGINA",
            completada: false,
            fechaCreacion: new Date().toISOString()
        },
        {
            id: 3,
            titulo: "Llamar al médico",
            descripcion: "Solicitar cita de revisión anual",
            categoria: "MUEVETE",
            completada: true,
            fechaCreacion: new Date().toISOString()
        },
        {
            id: 4,
            titulo: "Organizar armario",
            descripcion: "Clasificar la ropa por temporadas",
            categoria: "ORDENA",
            completada: false,
            fechaCreacion: new Date().toISOString()
        }
    ],
    espacios: [
        {
            id: 1,
            nombre: "Hogar",
            descripcion: "Tareas relacionadas con la casa",
            fechaCreacion: new Date().toISOString(),
            tareas: [1, 4]
        },
        {
            id: 2,
            nombre: "Finanzas",
            descripcion: "Gestión económica y financiera",
            fechaCreacion: new Date().toISOString(),
            tareas: [1]
        },
        {
            id: 3,
            nombre: "Salud",
            descripcion: "Cuidado personal y bienestar",
            fechaCreacion: new Date().toISOString(),
            tareas: [3]
        }
    ]
};

function handleApiError(error) {
    console.error("Error en la API:", error);
    
    const standardError = {
        status: error.status || 500,
        message: error.message || "Error en la comunicación con el servidor"
    };
    
    notifyError(standardError.message);
    
    return standardError;
}

function notifyError(message) {
    console.error("NOTIFICACIÓN DE ERROR:", message);
    alert("Error: " + message);
}

function notifySuccess(message) {
    console.log("NOTIFICACIÓN DE ÉXITO:", message);
    alert("Éxito: " + message);
}

async function fetchAPI(endpoint, options = {}) {
    if (MOCK_MODE) {
        console.log(`[MOCK] ${options.method || 'GET'} ${endpoint}`);
        
        await new Promise(resolve => setTimeout(resolve, 300));
        
        try {
            if (!options.method || options.method === 'GET') {
                if (endpoint === 'tareas') {
                    return [...MOCK_DATA.tareas];
                } else if (endpoint.startsWith('tareas/categorias/')) {
                    const categoria = endpoint.split('/').pop();
                    return MOCK_DATA.tareas.filter(t => t.categoria === categoria);
                } else if (endpoint.startsWith('tareas/') && endpoint.includes('/completar')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const tarea = MOCK_DATA.tareas.find(t => t.id === id);
                    if (!tarea) throw { status: 404, message: 'Tarea no encontrada' };
                    
                    tarea.completada = !tarea.completada;
                    return { ...tarea };
                } else if (endpoint.startsWith('tareas/')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const tarea = MOCK_DATA.tareas.find(t => t.id === id);
                    if (!tarea) throw { status: 404, message: 'Tarea no encontrada' };
                    
                    return { ...tarea };
                } else if (endpoint === 'espacios') {
                    return [...MOCK_DATA.espacios];
                } else if (endpoint.startsWith('espacio/') && endpoint.includes('/tareas')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const espacio = MOCK_DATA.espacios.find(e => e.id === id);
                    if (!espacio) throw { status: 404, message: 'Espacio no encontrado' };
                    
                    return MOCK_DATA.tareas.filter(t => espacio.tareas.includes(t.id));
                } else if (endpoint.startsWith('espacio/')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const espacio = MOCK_DATA.espacios.find(e => e.id === id);
                    if (!espacio) throw { status: 404, message: 'Espacio no encontrado' };
                    
                    return { ...espacio };
                }
            } else if (options.method === 'POST') {
                const body = JSON.parse(options.body);
                
                if (endpoint === 'tareas') {
                    const newTarea = {
                        id: Math.max(0, ...MOCK_DATA.tareas.map(t => t.id)) + 1,
                        ...body,
                        fechaCreacion: new Date().toISOString()
                    };
                    MOCK_DATA.tareas.push(newTarea);
                    return { ...newTarea };
                } else if (endpoint === 'espacios') {
                    const newEspacio = {
                        id: Math.max(0, ...MOCK_DATA.espacios.map(e => e.id)) + 1,
                        ...body,
                        fechaCreacion: new Date().toISOString(),
                        tareas: []
                    };
                    MOCK_DATA.espacios.push(newEspacio);
                    return { ...newEspacio };
                }
            } else if (options.method === 'PUT') {
                const body = JSON.parse(options.body);
                
                if (endpoint.startsWith('tareas/')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const index = MOCK_DATA.tareas.findIndex(t => t.id === id);
                    if (index === -1) throw { status: 404, message: 'Tarea no encontrada' };
                    
                    MOCK_DATA.tareas[index] = { ...MOCK_DATA.tareas[index], ...body };
                    return { ...MOCK_DATA.tareas[index] };
                } else if (endpoint.startsWith('espacio/')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const index = MOCK_DATA.espacios.findIndex(e => e.id === id);
                    if (index === -1) throw { status: 404, message: 'Espacio no encontrado' };
                    
                    MOCK_DATA.espacios[index] = { ...MOCK_DATA.espacios[index], ...body };
                    return { ...MOCK_DATA.espacios[index] };
                }
            } else if (options.method === 'DELETE') {
                if (endpoint.startsWith('tareas/')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const index = MOCK_DATA.tareas.findIndex(t => t.id === id);
                    if (index === -1) throw { status: 404, message: 'Tarea no encontrada' };
                    
                    const deletedTarea = MOCK_DATA.tareas[index];
                    MOCK_DATA.tareas.splice(index, 1);
                    return deletedTarea;
                } else if (endpoint.startsWith('espacio/')) {
                    const id = parseInt(endpoint.split('/')[1]);
                    const index = MOCK_DATA.espacios.findIndex(e => e.id === id);
                    if (index === -1) throw { status: 404, message: 'Espacio no encontrado' };
                    
                    const deletedEspacio = MOCK_DATA.espacios[index];
                    MOCK_DATA.espacios.splice(index, 1);
                    return deletedEspacio;
                }
            }
            
            console.warn(`[MOCK] Endpoint no implementado: ${endpoint}`);
            return [];
        } catch (error) {
            return handleApiError(error);
        }
    }
    
    try {
        const url = `${API_BASE_URL}/${endpoint}`;
        
        const fetchOptions = {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        };
        
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), API_TIMEOUT);
        fetchOptions.signal = controller.signal;
        
        const response = await fetch(url, fetchOptions);
        clearTimeout(timeoutId);
        
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw {
                status: response.status,
                message: error.message || `Error ${response.status}: ${response.statusText}`
            };
        }
        
        if (response.status === 204) {
            return null;
        }
        
        return await response.json();
    } catch (error) {
        if (error.name === 'AbortError') {
            error.message = 'La solicitud ha tardado demasiado tiempo';
        } else if (error.name === 'TypeError' && error.message.includes('NetworkError')) {
            error.message = 'No se ha podido conectar con el servidor';
        }
        
        return handleApiError(error);
    }
}

export {
    API_BASE_URL,
    fetchAPI,
    notifyError,
    notifySuccess
};
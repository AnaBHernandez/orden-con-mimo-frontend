// Basic Page Navigation & Placeholder Task Data

const pages = document.querySelectorAll('.page');
const homePage = document.getElementById('home-page');
const taskListPage = document.getElementById('task-list-page');
const taskFormPage = document.getElementById('task-form-page');

const viewTasksBtn = document.getElementById('view-tasks-btn');
const backToHomeBtn = document.getElementById('back-to-home-btn');
const addTaskBtn = document.getElementById('add-task-btn');
const cancelEditBtn = document.getElementById('cancel-edit-btn');
const taskForm = document.getElementById('task-form');
const taskList = document.getElementById('task-list');
const formTitle = document.getElementById('form-title');

// Form Inputs
const taskIdInput = document.getElementById('task-id');
const taskTitleInput = document.getElementById('task-title-input');
const taskDescInput = document.getElementById('task-desc-input');
const taskCategorySelect = document.getElementById('task-category-select');
const taskDeadlineInput = document.getElementById('task-deadline-input');

// --- Basic Navigation ---

function showPage(pageId) {
    pages.forEach(page => {
        page.classList.remove('active');
        if (page.id === pageId) {
            page.classList.add('active');
        }
    });
    window.scrollTo(0, 0); // Scroll to top when changing pages
}

if (viewTasksBtn) {
    viewTasksBtn.addEventListener('click', () => showPage('task-list-page'));
}

if (backToHomeBtn) {
    backToHomeBtn.addEventListener('click', () => showPage('home-page'));
}

if (addTaskBtn) {
    addTaskBtn.addEventListener('click', () => {
        formTitle.textContent = 'Crear Nueva Tarea';
        taskForm.reset(); // Clear form
        taskIdInput.value = ''; // Ensure no hidden ID
        showPage('task-form-page');
    });
}

if (cancelEditBtn) {
    cancelEditBtn.addEventListener('click', () => {
         taskForm.reset();
         taskIdInput.value = '';
         showPage('task-list-page'); // Go back to task list
    });
}

// --- Task Data Management ---
// In a real app, this would come from localStorage or a backend API

let tasks = [
    { id: 1, title: "Despejar la Mesa del Comedor", description: "Quitar todo lo que no pertenece a la mesa.", category: "muevete", deadline: "2024-08-10" },
    { id: 2, title: "Buscar Inspiración para el Salón", description: "Crear un tablero en Pinterest con ideas.", category: "imagina", deadline: "2024-08-15" },
    { id: 3, title: "Revisar Ropa del Armario", description: "¿Qué uso realmente? ¿Qué puedo donar?", category: "mirate", deadline: "" },
    { id: 4, title: "Organizar Cajón de Escritorio", description: "Asignar un lugar a cada cosa.", category: "ordena", deadline: "2024-08-20" },
];
let nextTaskId = 5; // Simple ID increment

// Load tasks from localStorage (if available)
function loadTasks() {
    const storedTasks = localStorage.getItem('mimoTasks');
    const storedNextId = localStorage.getItem('mimoNextTaskId');
    if (storedTasks) {
        tasks = JSON.parse(storedTasks);
    }
    if (storedNextId) {
        nextTaskId = parseInt(storedNextId);
    }
}

// Save tasks to localStorage
function saveTasksToStorage() {
    localStorage.setItem('mimoTasks', JSON.stringify(tasks));
    localStorage.setItem('mimoNextTaskId', nextTaskId.toString());
}


function getCategoryDisplayName(categoryKey) {
    switch (categoryKey) {
        case 'mirate': return 'Mírate';
        case 'imagina': return 'Imagina';
        case 'muevete': return 'Muévete';
        case 'ordena': return 'Ordena';
        default: return 'Desconocido';
    }
}

function renderTaskList() {
    if (!taskList) return;
    taskList.innerHTML = ''; // Clear existing list

    if (tasks.length === 0) {
        taskList.innerHTML = '<p class="no-tasks">¡Felicidades! No tienes tareas pendientes en tu reino.</p>';
        return;
    }

    // Sort tasks maybe? By deadline or category? For now, just render as is.
    tasks.sort((a, b) => a.id - b.id); // Simple sort by ID

    tasks.forEach(task => {
        const li = document.createElement('li');
        li.classList.add('task-item', `category-${task.category}`);
        li.dataset.id = task.id;

        li.innerHTML = `
            <div class="task-info">
                <h3 class="task-title">${task.title}</h3>
                <p class="task-desc">${task.description || ''}</p>
                <div>
                    <span class="task-category">${getCategoryDisplayName(task.category)}</span>
                    ${task.deadline ? `<span class="task-deadline">Plazo: ${task.deadline}</span>` : ''}
                </div>
            </div>
            <div class="task-actions">
                <button class="btn-icon edit-task-btn" title="Editar Tarea">
                     <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                </button>
                <button class="btn-icon delete-task-btn" title="Eliminar Tarea">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                </button>
            </div>
        `;

        // Add event listeners for edit/delete buttons within the loop
        li.querySelector('.edit-task-btn').addEventListener('click', (e) => {
            e.stopPropagation(); // Prevent triggering li click if any
            editTask(task.id);
        });
        li.querySelector('.delete-task-btn').addEventListener('click', (e) => {
             e.stopPropagation();
            deleteTask(task.id);
        });

        taskList.appendChild(li);
    });
}

// --- Task CRUD Operations ---

function saveTask(event) {
    event.preventDefault(); // Prevent form submission

    const id = taskIdInput.value ? parseInt(taskIdInput.value) : null; // Get ID if editing

    const taskData = {
        title: taskTitleInput.value.trim(),
        description: taskDescInput.value.trim(),
        category: taskCategorySelect.value,
        deadline: taskDeadlineInput.value,
    };

    if (!taskData.title || !taskData.category) {
        alert('Por favor, introduce al menos el título y la categoría.');
        return;
    }

    if (id) {
        // Update existing task
        const taskIndex = tasks.findIndex(task => task.id === id);
        if (taskIndex > -1) {
            tasks[taskIndex] = { ...tasks[taskIndex], ...taskData }; // Merge data
        }
    } else {
        // Add new task
        taskData.id = nextTaskId++; // Assign new ID
        tasks.push(taskData);
    }

    saveTasksToStorage(); // Save to localStorage
    renderTaskList(); // Re-render the list
    showPage('task-list-page'); // Go back to the list page
    taskForm.reset(); // Clear the form
    taskIdInput.value = ''; // Clear hidden ID
}

function editTask(id) {
    const task = tasks.find(task => task.id === id);
    if (!task) return;

    formTitle.textContent = 'Editar Tarea';
    taskIdInput.value = task.id;
    taskTitleInput.value = task.title;
    taskDescInput.value = task.description || '';
    taskCategorySelect.value = task.category;
    taskDeadlineInput.value = task.deadline || '';

    showPage('task-form-page');
}

function deleteTask(id) {
    const task = tasks.find(task => task.id === id);
    if (!task) return;

    // Confirmation dialog
    if (confirm(`¿Estás seguro de que quieres eliminar la tarea "${task.title}"?`)) {
        tasks = tasks.filter(task => task.id !== id);
        saveTasksToStorage(); // Save updated array to localStorage
        renderTaskList(); // Re-render the list
    }
}

// --- Initialization ---
if (taskForm) {
    taskForm.addEventListener('submit', saveTask);
}

// Load tasks from storage and render the list on initial load
loadTasks();
renderTaskList();

// Make sure the home page is shown initially if no other logic dictates otherwise
if (!document.querySelector('.page.active')) {
     showPage('home-page');
}
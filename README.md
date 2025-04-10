# 🏰 Orden con MIMO Frontend

## 📋 Descripción
Frontend de la aplicación "Orden con MIMO", una plataforma de organización personal basada en el método MIMO (Mírate, Imagina, Muévete, Ordena) con temática de reino fantástico.

Este proyecto implementa la interfaz de usuario de la aplicación, utilizando Spring Boot con Thymeleaf para renderizar las páginas y JavaScript para la comunicación con el backend.

## 🎨 Tema Reino MIMO
La aplicación sigue una temática de "Reino Fantástico" donde cada categoría MIMO tiene su propio color distintivo:

- **Mírate** 🔮: Púrpura (#614385)
- **Imagina** ✨: Dorado (#F1C40F)
- **Muévete** 🔥: Naranja (#E67E22)
- **Ordena** 🌿: Verde (#27AE60)

El esquema de colores principal de la aplicación es:
- Primary: #98D8C8 (Verde menta)
- Accent: #F5E6A8 (Amarillo suave)
- Secondary: #89CFF0 (Azul bebé)

## 🛠️ Tecnologías
- **Spring Boot**: Framework base para la aplicación
- **Thymeleaf**: Motor de plantillas para renderizar vistas
- **JavaScript**: Para interactividad y comunicación con la API
- **Bootstrap**: Framework CSS para el diseño responsive
- **CSS personalizado**: Para implementar el tema Reino MIMO

## 🚀 Instalación y ejecución

### Requisitos previos
- Java 17 o superior
- Maven

### Pasos
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/orden-con-mimo-frontend.git
   cd orden-con-mimo-frontend

2. Compilar el proyecto:
./mvnw clean package
3. Ejecutar la aplicación:
./mvnw spring-boot:run
4. Acceder a la aplicación en el navegador:
./mvnw spring-boot:run

Nota: Asegúrate de que el backend esté ejecutándose en http://localhost:8080 para que la comunicación con la API funcione correctamente.

📂 Estructura del proyecto
La estructura del proyecto sigue una organización por componentes funcionales:

src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ordenconmimo/
│   │           └── orden_con_mimo_frontend/
│   │               ├── controllers/
│   │               └── config/
│   └── resources/
│       ├── static/
│       │   ├── css/
│       │   ├── js/
│       │   └── img/
│       └── templates/
│           ├── index.html
│           ├── tarea/
│           └── espacio/

🔄 Comunicación con el backend
El frontend se comunica con el backend a través de APIs REST:

API de Tareas: Gestión de tareas personales
API de Espacios: Gestión de espacios organizativos
La configuración para la comunicación con la API se encuentra en /js/comun/config.js.

📚 Documentación
Para más información sobre el proyecto, consulta la Wiki del repositorio.

🤝 Contribución
Haz fork del proyecto
Crea una rama para tu funcionalidad (git checkout -b feature/amazing-feature)
Haz commit de tus cambios (git commit -m 'Add some amazing feature')
Push a la rama (git push origin feature/amazing-feature)
Abre un Pull Request
📜 Licencia
Este proyecto está licenciado bajo MIT License.


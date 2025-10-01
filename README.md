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
- **Spring Boot 3.4.4**: Framework base para la aplicación
- **Java 21**: Lenguaje de programación
- **Thymeleaf**: Motor de plantillas para renderizar vistas
- **JavaScript**: Para interactividad y comunicación con la API
- **Bootstrap**: Framework CSS para el diseño responsive
- **CSS personalizado**: Para implementar el tema Reino MIMO
- **H2 Database**: Base de datos en memoria para desarrollo

## 🚀 Instalación y ejecución

### Requisitos previos
- Java 21 o superior
- Maven 3.9+

### Pasos
1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/AnaBHernandez/orden-con-mimo-frontend.git
   cd orden-con-mimo-frontend
   ```

2. **Compilar el proyecto:**
   ```bash
   ./mvnw clean compile
   ```

3. **Ejecutar la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acceder a la aplicación:**
   - **URL**: http://localhost:8083
   - **Credenciales**:
     - Usuario: `admin`
     - Contraseña: `password`

### 🔧 Configuración de Puertos
- **Frontend**: Puerto 8083
- **Backend API**: Puerto 8084
- **H2 Console**: http://localhost:8083/h2-console

> **Nota**: Asegúrate de que el backend esté ejecutándose en http://localhost:8084 para que la comunicación con la API funcione correctamente.

## 📂 Estructura del proyecto
La estructura del proyecto sigue una organización por componentes funcionales:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ordenconmimo/
│   │           └── orden_con_mimo_frontend/
│   │               ├── controllers/     # Controladores REST
│   │               ├── services/        # Servicios de negocio
│   │               ├── models/          # Entidades del dominio
│   │               └── config/          # Configuraciones
│   └── resources/
│       ├── static/
│       │   ├── css/                    # Estilos personalizados
│       │   ├── js/                     # JavaScript del frontend
│       │   └── images/                  # Recursos estáticos
│       └── templates/
│           ├── index.html              # Página principal
│           ├── login.html              # Página de autenticación
│           ├── tarea/                  # Plantillas de tareas
│           └── espacios/               # Plantillas de espacios
```

## 🔄 Comunicación con el backend
El frontend se comunica con el backend a través de APIs REST:

- **API de Tareas**: Gestión de tareas personales
- **API de Espacios**: Gestión de espacios organizativos
- **Configuración**: `/js/comun/config.js`

## 🔐 Autenticación
La aplicación incluye un sistema de autenticación básico:

- **Endpoint de login**: `/login`
- **Credenciales por defecto**:
  - Usuario: `admin`
  - Contraseña: `password`
- **Sesiones**: Basadas en HttpSession

## 📚 Documentación
Para más información sobre el proyecto, consulta la [Wiki oficial del repositorio](https://github.com/AnaBHernandez/orden-con-mimo-frontend/wiki).

## 🤝 Contribución
1. Haz fork del proyecto
2. Crea una rama para tu funcionalidad (`git checkout -b feature/amazing-feature`)
3. Haz commit de tus cambios (`git commit -m 'Add some amazing feature'`)
4. Push a la rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

## 📜 Licencia
Este proyecto está licenciado bajo MIT License.

## 🎯 Características Principales
- ✅ **Interfaz intuitiva** con tema Reino MIMO
- ✅ **Gestión de tareas** por categorías MIMO
- ✅ **Espacios organizativos** para diferentes áreas
- ✅ **Autenticación** y gestión de sesiones
- ✅ **Responsive design** para todos los dispositivos
- ✅ **Comunicación con API** REST del backend
- ✅ **Base de datos H2** para desarrollo

## 🐛 Solución de Problemas
Si encuentras problemas:

1. **Verifica que Java 21 esté instalado**: `java -version`
2. **Verifica que Maven esté instalado**: `mvn -version`
3. **Revisa los logs** de la aplicación para errores específicos
4. **Asegúrate de que los puertos 8083 y 8084 estén libres**

## 📞 Soporte
Para soporte técnico o preguntas sobre el proyecto, consulta la [Wiki del repositorio](https://github.com/AnaBHernandez/orden-con-mimo-frontend/wiki) o abre un issue en GitHub.
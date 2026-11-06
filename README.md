# MCP-Server

Servidor MCP (Spring AI) de Donatrack. Expone por SSE (`/mcp/sse`) las tools que inician cada flujo en los módulos Donadores y Entidades, Donaciones, Incentivos y Logística.

## Configuración

El MCP debe consumir el sistema a través del API Gateway, no directamente a cada microservicio. El gateway expone una única URL y enruta cada recurso por prefijo de módulo.

| Variable | Default | Descripción |
|---|---|---|
| `PORT` | `8090` | Puerto del servidor |
| `URL_GATEWAY` | `https://jajava-gateway-main-15d96d2.zuplo.app` | URL base del API Gateway de Zuplo |
| `URL_DONADORES` | `${URL_GATEWAY}/donadores-entidades` | Módulo Donadores y Entidades a través del gateway |
| `URL_DONACIONES` | `${URL_GATEWAY}/donaciones` | Módulo Donaciones a través del gateway |
| `URL_INCENTIVOS` | `${URL_GATEWAY}/incentivos` | Módulo Incentivos a través del gateway |
| `URL_LOGISTICA` | `${URL_GATEWAY}/logistica` | Módulo Logística a través del gateway |
| `URL_CONSULTAS` | `${URL_GATEWAY}/consultas` | Servicio de Consultas/CQRS a través del gateway |

Ejemplo de rutas esperadas en el gateway:

- `https://jajava-gateway-main-15d96d2.zuplo.app/donadores-entidades/donadores`
- `https://jajava-gateway-main-15d96d2.zuplo.app/incentivos/insignias`
- `https://jajava-gateway-main-15d96d2.zuplo.app/donaciones/donaciones`
- `https://jajava-gateway-main-15d96d2.zuplo.app/logistica/asignaciones`
- `https://jajava-gateway-main-15d96d2.zuplo.app/consultas/donadores/{id}`

## Arquitectura de integración

El MCP no implementa reglas de negocio; delega la operación en los módulos de Donatrack a través del API Gateway.

El flujo recomendado para esta entrega es:

1. El cliente (Claude Desktop / MCP / bot) llama al servidor MCP local.
2. El servidor MCP invoca las tools definidas en Java.
3. Cada tool usa `RestClient` para enviar la request al gateway.
4. El gateway enruta la solicitud al módulo correspondiente.
5. El módulo ejecuta la lógica de negocio y devuelve la respuesta.

Esto mantiene una sola entrada de red para los clientes y evita que el MCP conozca o dependa directamente de cuatro URLs distintas.

## Alcance de tools del MCP

El MCP expone herramientas para operar el flujo principal del sistema. La intención es permitir que un modelo de lenguaje pueda completar casos de uso reales de Donatrack sin conocer la infraestructura técnica.

Las tools cubren, entre otras cosas:

- alta y listado de donadores y entidades
- registro de necesidades materiales
- catálogo de productos, categorías y subcategorías
- registro de donaciones y quejas
- creación de insignias y misiones
- consulta de misión en curso y estado del donador
- consulta de depósitos, stock y asignaciones
- registro de entrega y cierre de asignación

El MCP está pensada como capa de orquestación operacional; no reemplaza el backend ni el modelo de lectura/cqrs del servicio de consultas.

## Nota sobre el alcance completo del sistema

Para la entrega de API Gateway y consultas cruzadas, el MCP debería complementar el sistema con herramientas adicionales de lectura agregada cuando el proyecto integre el servicio de consultas y MongoDB. Es decir, no basta con exponer CRUD de módulos; también conviene incluir herramientas de consulta centralizada, por ejemplo:

- ficha integral de donador
- necesidades urgentes sin cubrir
- resumen operativo por depósito
- detalle consolidado de donaciones por entidad

Estas consultas no son reglas de negocio nuevas, sino vistas agregadas sobre el modelo de lectura.

### Consultas CQRS expuestas por el MCP

Cuando el servicio de consultas está disponible, el MCP también expone:

| Tool | Descripción |
|---|---|
| `consulta_ficha_integral_donador` | Ficha consolidada de un donador desde el modelo de lectura |
| `consulta_necesidades_urgentes` | Necesidades urgentes sin cubrir |
| `consulta_estado_depositos` | Estado consolidado de los depósitos |
| `consulta_calidad_donaciones` | Métricas de calidad de las donaciones |

## Tools

### Donadores y Entidades

| Tool | Descripción |
|---|---|
| `donadores_crear_donador` | Da de alta un donador. Queda en estado VERIFICADO y categoría OCASIONAL. |
| `donadores_listar_donadores` | Lista todos los donadores. |
| `donadores_estadisticas` | Estadísticas de un donador: categoría, misión actual e insignias. |
| `donadores_listar_quejas` | Lista las quejas registradas contra un donador. |
| `entidades_crear_entidad` | Da de alta una entidad benéfica. |
| `entidades_listar_entidades` | Lista todas las entidades benéficas. |
| `necesidades_registrar_necesidad` | Registra una necesidad material de una entidad. Valida el producto en Donaciones y asigna stock en Logística. |
| `necesidades_listar_insatisfechas_por_producto` | Lista las necesidades aún insatisfechas de un producto. |

### Donaciones

| Tool | Descripción |
|---|---|
| `donaciones_registrar_donacion` | Registra una nueva donación para un donador (donadorID, depositoID, productoID y cantidad). |
| `donaciones_consultar_donacion` | Consulta los detalles y estado actual de una donación. |
| `donaciones_buscar_por_donador` | Busca todas las donaciones hechas por un donador (fecha de inicio opcional). |
| `donaciones_ver_productos` | Muestra el catálogo completo de productos disponibles para donar. |
| `donaciones_ver_categorias` | Muestra las categorías principales de productos. |
| `donaciones_ver_subcategorias` | Muestra las subcategorías dado el ID de una categoría principal. |
| `donaciones_registrar_queja` | Registra una queja sobre una donación en estado ACEPTADA. |
| `donaciones_crear_categoria` | Crea una nueva categoría principal en el catálogo. |
| `donaciones_crear_subcategoria` | Crea una nueva subcategoría dentro de una categoría existente. |
| `donaciones_crear_identificador` | Crea un identificador para un producto (`QR` o `CODIGODEBARRAS`). Si es QR, el nombre del producto debe tener cantidad par de letras; si es CODIGODEBARRAS, la descripción debe tener 3 o más palabras. |
| `donaciones_crear_producto` | Crea un producto vinculando una subcategoría y un identificador existentes. |

### Incentivos

| Tool | Descripción |
|---|---|
| `incentivos_crear_insignia` | Crea una nueva insignia. |
| `incentivos_listar_insignias` | Lista todas las insignias existentes. |
| `incentivos_crear_mision` | Crea una nueva misión. |
| `incentivos_listar_misiones` | Lista todas las misiones existentes. |
| `incentivos_consultar_mision` | Busca una misión por su ID (formato `mis-X`). |
| `incentivos_consultar_mision_en_curso` | Consulta la misión en curso de un donador. |
| `incentivos_procesar_donador` | Fuerza el procesamiento de un donador: evalúa el progreso de su misión en curso. |
| `incentivos_consultar_estado_donador` | Devuelve el estado completo de un donador: categoría, insignias, misión actual e historial de categorías. |

### Logística

| Tool | Descripción |
|---|---|
| `logistica_crear_deposito` | Crea un nuevo depósito. |
| `logistica_consultar_depositos` | Obtiene todos los depósitos registrados. |
| `logistica_consultar_deposito` | Obtiene un depósito por ID. |
| `logistica_consultar_asignaciones` | Obtiene todas las asignaciones. |
| `logistica_consultar_stock` | Obtiene el stock de un depósito. |
| `logistica_consultar_stock_producto` | Obtiene la cantidad total disponible de un producto. |
| `logistica_consultar_asignaciones_estado` | Obtiene las asignaciones por estado (ASIGNADA o COMPLETADA). |
| `logistica_consultar_asignacion_paquete` | Obtiene una asignación a partir del ID del paquete. |
| `logistica_configurar_algoritmo` | Configura el algoritmo de matchmaking de un depósito (SUB_ATENDIDOS o PRIORIDAD_POR_SCORE). |
| `logistica_reportar_entrega` | Cambia el estado de la asignacion a COMPLETADA. |

# MCP-Server

Servidor MCP (Spring AI) de Donatrack. Expone por SSE (`/mcp/sse`) las tools que inician cada flujo en los módulos Donadores y Entidades, Donaciones, Incentivos y Logística.

## Configuración

| Variable | Default | Descripción |
|---|---|---|
| `PORT` | `8090` | Puerto del servidor |
| `URL_DONADORES` | `https://agusb1101-donadores-entidades.onrender.com` | Módulo Donadores y Entidades |
| `URL_DONACIONES` | `https://donaciones-5u8i.onrender.com` | Módulo Donaciones |
| `URL_INCENTIVOS` | `https://incentivos-yuse.onrender.com` | Módulo Incentivos |
| `URL_LOGISTICA` | `https://logistica-hjaw.onrender.com` | Módulo Logística |

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

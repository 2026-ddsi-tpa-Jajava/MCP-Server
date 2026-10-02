package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.clients.DonacionesClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class DonacionesTools {

    private final DonacionesClient donacionesClient;

    public DonacionesTools(DonacionesClient donacionesClient) {
        this.donacionesClient = donacionesClient;
    }

    @Tool(name = "donaciones_registrar_donacion", description = "Registra una nueva donación para un donador. Requiere donadorID, depositoID, productoID y cantidad.")
    public String registrarDonacion(
            @ToolParam(description = "ID numérico del donador", required = true) String donadorID,
            @ToolParam(description = "ID del depósito de logística", required = true) String depositoID,
            @ToolParam(description = "ID del producto (búscalo en el catálogo si no lo sabes)", required = true) String productoID,
            @ToolParam(description = "Cantidad donada (mayor a 0)", required = true) Integer cantidad,
            @ToolParam(description = "Descripción opcional") String descripcion) {
        return donacionesClient.registrarDonacion(donadorID, depositoID, productoID, cantidad, descripcion);
    }

    @Tool(name = "donaciones_consultar_donacion", description = "Consulta los detalles y estado actual de una donación específica.")
    public String consultarDonacion(
            @ToolParam(description = "ID numérico de la donación", required = true) String donacionId) {
        return donacionesClient.consultarDonacion(donacionId);
    }

    @Tool(name = "donaciones_registrar_queja", description = "Registra una queja sobre una donación en estado ACEPTADA.")
    public String registrarQueja(
            @ToolParam(description = "ID numérico de la donación entregada", required = true) String donacionId,
            @ToolParam(description = "Motivo detallado de la queja", required = true) String descripcion) {
        return donacionesClient.registrarQueja(donacionId, descripcion);
    }

    @Tool(name = "donaciones_buscar_por_donador", description = "Busca todas las donaciones hechas por un donador.")
    public String buscarDonaciones(
            @ToolParam(description = "ID numérico del donador", required = true) String donadorID,
            @ToolParam(description = "Opcional: Fecha de inicio en formato YYYY-MM-DD") String fechaInicio) {
        return donacionesClient.verDonacionesPorDonadorYFecha(donadorID, fechaInicio);
    }

    @Tool(name = "donaciones_ver_productos", description = "Muestra el catálogo completo de productos disponibles para donar.")
    public String verProductos() {
        return donacionesClient.verProductosDisponibles();
    }

    @Tool(name = "donaciones_ver_categorias", description = "Muestra las categorías principales de productos.")
    public String verCategorias() {
        return donacionesClient.verCategorias();
    }

    @Tool(name = "donaciones_ver_subcategorias", description = "Muestra las subcategorías dado el ID de una categoría principal.")
    public String verSubcategorias(
            @ToolParam(description = "ID de la categoría padre", required = true) String categoriaId) {
        return donacionesClient.verSubcategorias(categoriaId);
    }
}
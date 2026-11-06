package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.clients.DonadoresClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class DonadoresTools {

    private final DonadoresClient client;

    public DonadoresTools(DonadoresClient client) {
        this.client = client;
    }

    // ---------- Donadores ----------

    @Tool(name = "donadores_crear_donador", description = "Da de alta un donador. Queda en estado VERIFICADO y categoría OCASIONAL.")
    public String crearDonador(
            @ToolParam(description = "Nombre", required = true) String nombre,
            @ToolParam(description = "Apellido", required = true) String apellido,
            @ToolParam(description = "Edad", required = true) Integer edad,
            @ToolParam(description = "Email") String email,
            @ToolParam(description = "Número de documento") String nroDocumento,
            @ToolParam(description = "Domicilio") String domicilio) {
        return client.crearDonador(nombre, apellido, edad, email, nroDocumento, domicilio);
    }

    @Tool(name = "donadores_listar_donadores", description = "Lista todos los donadores.")
    public String listarDonadores() {
        return client.listarDonadores();
    }

    @Tool(name = "donadores_consultar_donador", description = "Obtiene un donador por su ID.")
    public String consultarDonador(@ToolParam(description = "ID del donador", required = true) String id) {
        return client.consultarDonador(id);
    }

    @Tool(name = "donadores_modificar_estado", description = "Cambia el estado de un donador: VERIFICADO, SOSPECHOSO o BANEADO.")
    public String modificarEstado(
            @ToolParam(description = "ID del donador", required = true) String id,
            @ToolParam(description = "VERIFICADO, SOSPECHOSO o BANEADO", required = true) String estado) {
        return client.modificarEstado(id, estado.toUpperCase());
    }

    @Tool(name = "donadores_modificar_categoria", description = "Cambia la categoría de un donador.")
    public String modificarCategoria(
            @ToolParam(description = "ID del donador", required = true) String id,
            @ToolParam(description = "OCASIONAL, COLABORADOR, TRANSFORMADOR, SALVADOR o REVOLUCIONARIO", required = true) String categoria) {
        return client.modificarCategoria(id, categoria.toUpperCase());
    }

    @Tool(name = "donadores_puede_donar", description = "Indica si un donador puede donar según su estado.")
    public String puedeDonar(@ToolParam(description = "ID del donador", required = true) String id) {
        return client.puedeDonar(id);
    }

    @Tool(name = "donadores_estadisticas", description = "Estadísticas de un donador: categoría, misión actual e insignias (datos del módulo Incentivos).")
    public String estadisticas(@ToolParam(description = "ID del donador", required = true) String id) {
        return client.estadisticas(id);
    }

    @Tool(name = "donadores_registrar_queja", description = "Registra una queja contra un donador por una donación. Superar los umbrales de quejas puede cambiar su estado.")
    public String registrarQueja(
            @ToolParam(description = "ID del donador", required = true) String id,
            @ToolParam(description = "ID de la donación que motiva la queja", required = true) String donacionID,
            @ToolParam(description = "Motivo de la queja", required = true) String descripcion) {
        return client.registrarQueja(id, donacionID, descripcion);
    }

    @Tool(name = "donadores_listar_quejas", description = "Lista las quejas registradas contra un donador.")
    public String listarQuejas(@ToolParam(description = "ID del donador", required = true) String id) {
        return client.listarQuejas(id);
    }

    // ---------- Entidades benéficas ----------

    @Tool(name = "entidades_crear_entidad", description = "Da de alta una entidad benéfica.")
    public String crearEntidad(
            @ToolParam(description = "Razón social", required = true) String razonSocial,
            @ToolParam(description = "Domicilio", required = true) String domicilio,
            @ToolParam(description = "Teléfono") String telefono,
            @ToolParam(description = "Correo electrónico") String correo) {
        return client.crearEntidad(razonSocial, domicilio, telefono, correo);
    }

    @Tool(name = "entidades_listar_entidades", description = "Lista todas las entidades benéficas.")
    public String listarEntidades() {
        return client.listarEntidades();
    }

    @Tool(name = "entidades_consultar_entidad", description = "Obtiene una entidad benéfica por su ID.")
    public String consultarEntidad(@ToolParam(description = "ID de la entidad", required = true) String id) {
        return client.consultarEntidad(id);
    }

    @Tool(name = "entidades_modificar_entidad", description = "Modifica datos de una entidad benéfica. Indicar al menos un campo; los omitidos no cambian.")
    public String modificarEntidad(
            @ToolParam(description = "ID de la entidad", required = true) String id,
            @ToolParam(description = "Nueva razón social") String razonSocial,
            @ToolParam(description = "Nuevo domicilio") String domicilio,
            @ToolParam(description = "Nuevo teléfono") String telefono,
            @ToolParam(description = "Nuevo correo") String correo) {
        return client.modificarEntidad(id, razonSocial, domicilio, telefono, correo);
    }

    // ---------- Necesidades materiales ----------

    @Tool(name = "necesidades_registrar_necesidad", description = "Registra una necesidad material de una entidad. Valida el producto en Donaciones y asigna stock en Logística.")
    public String registrarNecesidad(
            @ToolParam(description = "ID de la entidad", required = true) String entidadID,
            @ToolParam(description = "Nivel de urgencia (entero)", required = true) Integer nivelDeUrgencia,
            @ToolParam(description = "Descripción de la necesidad") String descripcion,
            @ToolParam(description = "Cantidad objetivo (mayor a 0)", required = true) Integer cantidadObjetivo,
            @ToolParam(description = "ID del producto solicitado (ver catálogo con donaciones_ver_productos)", required = true) String productoSolicitadoID,
            @ToolParam(description = "EXTRAORDINARIA o RECURRENTE", required = true) String tipo) {
        return client.registrarNecesidad(entidadID, nivelDeUrgencia, descripcion, cantidadObjetivo,
                productoSolicitadoID, tipo.toUpperCase());
    }

    @Tool(name = "necesidades_consultar_necesidad", description = "Obtiene una necesidad material por su ID.")
    public String consultarNecesidad(@ToolParam(description = "ID de la necesidad", required = true) String id) {
        return client.consultarNecesidad(id);
    }

    @Tool(name = "necesidades_listar_insatisfechas_por_producto", description = "Lista las necesidades aún insatisfechas de un producto.")
    public String listarInsatisfechasPorProducto(
            @ToolParam(description = "ID del producto", required = true) String productoID) {
        return client.listarInsatisfechasPorProducto(productoID);
    }

    @Tool(name = "necesidades_satisfacer_necesidad", description = "Reporta una entrega que satisface (total o parcialmente) una necesidad.")
    public String satisfacerNecesidad(
            @ToolParam(description = "ID de la necesidad", required = true) String id,
            @ToolParam(description = "Cantidad entregada (mayor a 0)", required = true) Integer cantidad) {
        return client.satisfacerNecesidad(id, cantidad);
    }

    @Tool(name = "necesidades_modificar_necesidad", description = "Modifica una necesidad material. Indicar al menos un campo; los omitidos no cambian.")
    public String modificarNecesidad(
            @ToolParam(description = "ID de la necesidad", required = true) String id,
            @ToolParam(description = "Nuevo nivel de urgencia") Integer nivelDeUrgencia,
            @ToolParam(description = "Nueva descripción") String descripcion,
            @ToolParam(description = "Nueva cantidad objetivo") Integer cantidadObjetivo,
            @ToolParam(description = "Nuevo ID de producto solicitado") String productoSolicitadoID) {
        return client.modificarNecesidad(id, nivelDeUrgencia, descripcion, cantidadObjetivo, productoSolicitadoID);
    }

    @Tool(name = "necesidades_eliminar_necesidad", description = "Elimina una necesidad material por su ID.")
    public String eliminarNecesidad(@ToolParam(description = "ID de la necesidad", required = true) String id) {
        return client.eliminarNecesidad(id);
    }
}

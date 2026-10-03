package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.clients.LogisticaClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class LogisticaTools {

    private final LogisticaClient logisticaClient;

    public LogisticaTools(LogisticaClient logisticaClient) {

        this.logisticaClient = logisticaClient;
    }

    @Tool(name = "logistica_crear_deposito", description = "Crea un nuevo depósito")

    public String crearDeposito(
            @ToolParam(description = "Nombre del depósito", required = true) String nombre,
            @ToolParam(description = "Dirección del depósito", required = true) String direccion,
            @ToolParam(description = "Capacidad máxima", required = true) Integer capacidadMaxima) {

        return logisticaClient.crearDeposito(nombre, direccion, capacidadMaxima);
    }

    @Tool(name = "logistica_consultar_depositos", description = "Obtiene todos los depósitos registrados")

    public String consultarDepositos() {

        return logisticaClient.consultarDepositos();
    }

    @Tool(name = "logistica_consultar_deposito", description = "Obtiene un depósito por ID")

    public String consultarDeposito(@ToolParam(description = "ID del depósito", required = true) String depositoID) {

        return logisticaClient.consultarDepositoPorId(depositoID);

    }

    @Tool(name = "logistica_consultar_stock", description = "Obtiene el stock de un depósito"
    )
    public String consultarStock(@ToolParam(description = "ID del depósito", required = true) String depositoID) {

        return logisticaClient.consultarStock(depositoID);

    }

    @Tool(name = "logistica_consultar_stock_producto", description = "Obtiene la cantidad total disponible de un producto")

    public String consultarStockProducto(@ToolParam(description = "ID del producto", required = true) String productoID) {

        return logisticaClient.consultarStockProducto(productoID);
    }

    @Tool(name = "logistica_consultar_asignaciones", description = "Obtiene todas las asignaciones")

    public String consultarAsignaciones() {

        return logisticaClient.consultarAsignaciones();
    }

    @Tool(name = "logistica_consultar_asignaciones_estado", description = "Obtiene las asignaciones por estado")

    public String consultarAsignacionesEstado(@ToolParam(description = "ASIGNADA o COMPLETADA", required = true) String estado) {

        return logisticaClient.consultarAsignacionesPorEstado(estado);

    }

    @Tool(name = "logistica_consultar_asignacion_paquete", description = "Obtiene una asignación a partir del ID del paquete")

    public String consultarAsignacionPorPaquete(@ToolParam(description = "ID del paquete", required = true) String paqueteID) {

        return logisticaClient.consultarAsignacionPorPaquete(paqueteID);

    }

    @Tool(name = "logistica_configurar_algoritmo", description = "Configura el algoritmo de matchmaking de un depósito")

    public String configurarAlgoritmo(@ToolParam(description = "ID del depósito", required = true) String depositoID,
                                      @ToolParam(description = "SUB_ATENDIDOS o PRIORIDAD_POR_SCORE", required = true) String algoritmo) {

        return logisticaClient.configurarAlgoritmo(depositoID, algoritmo);

    }

    @Tool(name = "reportar_entrega", description = "Reporta la entrega de un paquete y completa la asignación asociada")

    public String reportarEntrega(

            @ToolParam(description = "ID del paquete", required = true) String paqueteID,
            @ToolParam(description = "ID de la donación", required = true) String donacionID,
            @ToolParam(description = "ID del producto", required = true) String productoID,
            @ToolParam(description = "Cantidad entregada", required = true) Integer cantidad) {

        return logisticaClient.reportarEntrega(
                paqueteID,
                donacionID,
                productoID,
                cantidad
        );
    }
/*
    @Tool(name = "vaciar_stock", description = "Elimina todos los paquetes almacenados en un depósito")
*/
    // public String vaciarStock(@ToolParam(description = "ID del depósito", required = true) String depositoID) {

    // return logisticaClient.vaciarStock(depositoID);
    // }

    // @Tool(name = "logistica_eliminar_paquetes", description = "Elimina todos los paquetes almacenados")

    // public String eliminarPaquetes() {

    // return logisticaClient.eliminarPaquetes();
    // }

    // @Tool(name = "logistica_eliminar_asignaciones", description = "Elimina todas las asignaciones")

    // public String eliminarAsignaciones() {

    // return logisticaClient.eliminarAsignaciones();
    // }

    // @Tool(name = "logistica_eliminar_depositos", description = "Elimina todos los depósitos")

    // public String eliminarDepositos() {

    // return logisticaClient.eliminarDepositos();
    // }
}
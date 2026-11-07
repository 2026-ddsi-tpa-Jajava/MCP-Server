package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.clients.ConsultasClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class ConsultasTools {

    private final ConsultasClient consultasClient;

    public ConsultasTools(ConsultasClient consultasClient) {
        this.consultasClient = consultasClient;
    }

    @Tool(name = "consulta_ficha_integral_donador",
            description = "Obtiene la ficha integral de un donador desde el modelo de lectura CQRS, incluyendo estado, categoría, insignias, misión, donaciones, quejas y entidades beneficiadas.")
    public String fichaIntegralDonador(
            @ToolParam(description = "ID del donador", required = true) String id) {
        return consultasClient.fichaIntegralDonador(id);
    }

    @Tool(name = "consulta_necesidades_urgentes",
            description = "Obtiene las necesidades urgentes sin cubrir desde el modelo de lectura CQRS.")
    public String necesidadesUrgentes(
            @ToolParam(description = "Urgencia mínima, entre 1 y 10") Integer urgenciaMinima,
            @ToolParam(description = "Tipo de necesidad, por ejemplo EXTRAORDINARIA o RECURRENTE") String tipo,
            @ToolParam(description = "Categoría del producto, por ejemplo ALIMENTOS") String categoria,
            @ToolParam(description = "ID de subcategoría o de producto solicitado") String subcategoriaId) {
        return consultasClient.necesidadesUrgentes(urgenciaMinima, tipo, categoria, subcategoriaId);
    }

    @Tool(name = "consulta_estado_depositos",
            description = "Obtiene el estado consolidado de los depósitos desde el modelo de lectura CQRS.")
    public String estadoDepositos(
            @ToolParam(description = "ID del depósito; si se omite, devuelve todos") String depositoId,
            @ToolParam(description = "Fecha inicial, YYYY-MM-DD o YYYY-MM-DDTHH:mm:ss") String fechaDesde,
            @ToolParam(description = "Fecha final, YYYY-MM-DD o YYYY-MM-DDTHH:mm:ss") String fechaHasta) {
        return consultasClient.estadoDepositos(depositoId, fechaDesde, fechaHasta);
    }

    @Tool(name = "consulta_calidad_donaciones",
            description = "Obtiene, por categoría y subcategoría, el total de donaciones, cuántas tienen queja y su proporción, y el ranking de donadores con más quejas con su estado y categoría, desde el modelo de lectura CQRS.")
    public String calidadDonaciones() {
        return consultasClient.calidadDonaciones();
    }
}

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
    public String necesidadesUrgentes() {
        return consultasClient.necesidadesUrgentes();
    }

    @Tool(name = "consulta_estado_depositos",
            description = "Obtiene el estado consolidado de los depósitos desde el modelo de lectura CQRS.")
    public String estadoDepositos() {
        return consultasClient.estadoDepositos();
    }

    @Tool(name = "consulta_calidad_donaciones",
            description = "Obtiene las métricas de calidad de las donaciones desde el modelo de lectura CQRS.")
    public String calidadDonaciones() {
        return consultasClient.calidadDonaciones();
    }
}

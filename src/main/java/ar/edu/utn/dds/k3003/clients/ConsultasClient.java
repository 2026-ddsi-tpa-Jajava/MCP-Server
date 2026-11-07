package ar.edu.utn.dds.k3003.clients;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class ConsultasClient {

    private final RestClient restClient;

    public ConsultasClient(@Value("${servicios.url.consultas}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public String fichaIntegralDonador(String id) {
        return ejecutar(() -> restClient.get()
                .uri("/donadores/{id}", id)
                .retrieve()
                .body(String.class));
    }

    public String necesidadesUrgentes(Integer urgenciaMinima, String tipo,
                                      String categoria, String subcategoriaId) {
        return ejecutar(() -> restClient.get()
                .uri(builder -> {
                    builder.path("/necesidades-urgentes");
                    if (urgenciaMinima != null) {
                        builder.queryParam("urgenciaMinima", urgenciaMinima);
                    }
                    if (tipo != null && !tipo.isBlank()) {
                        builder.queryParam("tipo", tipo);
                    }
                    if (categoria != null && !categoria.isBlank()) {
                        builder.queryParam("categoria", categoria);
                    }
                    if (subcategoriaId != null && !subcategoriaId.isBlank()) {
                        builder.queryParam("subcategoriaId", subcategoriaId);
                    }
                    return builder.build();
                })
                .retrieve()
                .body(String.class));
    }

    public String estadoDepositos(String depositoId, String fechaDesde, String fechaHasta) {
        return ejecutar(() -> restClient.get()
                .uri(builder -> {
                    builder.path("/depositos");
                    if (depositoId != null && !depositoId.isBlank()) {
                        builder.queryParam("id", depositoId);
                    }
                    if (fechaDesde != null && !fechaDesde.isBlank()) {
                        builder.queryParam("desde", aFechaHora(fechaDesde, "T00:00:00"));
                    }
                    if (fechaHasta != null && !fechaHasta.isBlank()) {
                        builder.queryParam("hasta", aFechaHora(fechaHasta, "T23:59:59"));
                    }
                    return builder.build();
                })
                .retrieve()
                .body(String.class));
    }

    public String calidadDonaciones() {
        return ejecutar(() -> restClient.get()
                .uri("/calidad")
                .retrieve()
                .body(String.class));
    }

    // El servicio de consultas espera fecha y hora ISO; si llega solo la fecha, se completa la hora.
    private static String aFechaHora(String fecha, String horaPorDefecto) {
        String valor = fecha.trim();
        return valor.length() == 10 ? valor + horaPorDefecto : valor;
    }

    private static String ejecutar(Supplier<String> llamada) {
        try {
            return llamada.get();
        } catch (RestClientResponseException e) {
            return "Error " + e.getStatusCode().value() + ": " + e.getResponseBodyAsString();
        }
    }
}

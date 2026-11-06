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
                                      String categoriaId, String subcategoriaId) {
        return ejecutar(() -> restClient.get()
                .uri(builder -> {
                    builder.path("/necesidades-urgentes");
                    if (urgenciaMinima != null) {
                        builder.queryParam("urgenciaMinima", urgenciaMinima);
                    }
                    if (tipo != null && !tipo.isBlank()) {
                        builder.queryParam("tipo", tipo);
                    }
                    if (categoriaId != null && !categoriaId.isBlank()) {
                        builder.queryParam("categoriaId", categoriaId);
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
                        builder.queryParam("depositoId", depositoId);
                    }
                    if (fechaDesde != null && !fechaDesde.isBlank()) {
                        builder.queryParam("fechaDesde", fechaDesde);
                    }
                    if (fechaHasta != null && !fechaHasta.isBlank()) {
                        builder.queryParam("fechaHasta", fechaHasta);
                    }
                    return builder.build();
                })
                .retrieve()
                .body(String.class));
    }

    public String calidadDonaciones(String categoriaId, String subcategoriaId, Integer limite) {
        return ejecutar(() -> restClient.get()
                .uri(builder -> {
                    builder.path("/calidad");
                    if (categoriaId != null && !categoriaId.isBlank()) {
                        builder.queryParam("categoriaId", categoriaId);
                    }
                    if (subcategoriaId != null && !subcategoriaId.isBlank()) {
                        builder.queryParam("subcategoriaId", subcategoriaId);
                    }
                    if (limite != null) {
                        builder.queryParam("limite", limite);
                    }
                    return builder.build();
                })
                .retrieve()
                .body(String.class));
    }

    private static String ejecutar(Supplier<String> llamada) {
        try {
            return llamada.get();
        } catch (RestClientResponseException e) {
            return "Error " + e.getStatusCode().value() + ": " + e.getResponseBodyAsString();
        }
    }
}

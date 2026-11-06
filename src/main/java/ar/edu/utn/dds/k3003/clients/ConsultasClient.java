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

    public String necesidadesUrgentes() {
        return ejecutar(() -> restClient.get()
                .uri("/necesidades-urgentes")
                .retrieve()
                .body(String.class));
    }

    public String estadoDepositos() {
        return ejecutar(() -> restClient.get()
                .uri("/depositos")
                .retrieve()
                .body(String.class));
    }

    public String calidadDonaciones() {
        return ejecutar(() -> restClient.get()
                .uri("/calidad")
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

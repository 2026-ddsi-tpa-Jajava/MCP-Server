package ar.edu.utn.dds.k3003.clients;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IncentivosClient {

    private final RestClient restClient;

    public IncentivosClient(@Value("${servicios.url.incentivos}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public String crearInsignia(String nombre, String descripcion) {
        return restClient.post().uri("/insignias")
                .body(Map.of("nombre", nombre, "descripcion", descripcion))
                .retrieve().body(String.class);
    }

    public String listarInsignias() {
        return restClient.get().uri("/insignias").retrieve().body(String.class);
    }

    public String consultarInsignia(String insigniaID) {
        return restClient.get().uri("/insignias/{id}", insigniaID).retrieve().body(String.class);
    }

    public void asignarInsigniaADonador(String donadorID, String insigniaID) {
        restClient.post().uri("/insignias/{donadorID}", donadorID)
                .body(Map.of("insigniaID", insigniaID))
                .retrieve().toBodilessEntity();
    }

    public String consultarInsigniasDeDonador(String donadorID) {
        return restClient.get().uri("/insignias/{donadorID}", donadorID).retrieve().body(String.class);
    }

    public String crearMision(String nombre, String insigniaID, String categoriaInicio,
                              String categoriaFin, String tipo) {
        Map<String, String> body = Map.of(
                "nombre", nombre,
                "insigniaID", insigniaID,
                "categoriaInicio", categoriaInicio,
                "categoriaFin", categoriaFin,
                "tipo", tipo);
        return restClient.post().uri("/misiones").body(body).retrieve().body(String.class);
    }

    public String listarMisiones() {
        return restClient.get().uri("/misiones").retrieve().body(String.class);
    }

    public String consultarMision(String misionID) {
        return restClient.get().uri("/misiones/{id}", misionID).retrieve().body(String.class);
    }

    public String consultarMisionEnCurso(String donadorID) {
        return restClient.get().uri("/misiones/{donadorID}", donadorID).retrieve().body(String.class);
    }

    public void asignarMisionADonador(String donadorID, String misionID) {
        restClient.post().uri("/misiones/{donadorID}", donadorID)
                .body(Map.of("misionID", misionID))
                .retrieve().toBodilessEntity();
    }

    public void procesarDonador(String donadorID) {
        restClient.post().uri("/procesamiento/{donadorID}", donadorID)
                .retrieve().toBodilessEntity();
    }

    public String consultarEstadoDonador(String donadorID) {
        return restClient.get().uri("/donadores/{donadorID}/estado", donadorID)
                .retrieve().body(String.class);
    }
}

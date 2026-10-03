package ar.edu.utn.dds.k3003.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Component
public class DonacionesClient {

    private final RestClient restClient;

    public DonacionesClient(@Value("${servicios.url.donaciones}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public String registrarDonacion(String donadorID, String depositoID, String productoID, Integer cantidad, String descripcion) {
        Map<String, Object> body = Map.of(
                "donadorID", donadorID,
                "depositoID", depositoID,
                "productoID", productoID,
                "cantidad", cantidad,
                "descripcion", descripcion != null ? descripcion : ""
        );
        return restClient.post().uri("/donaciones")
                .body(body)
                .retrieve()
                .body(String.class);
    }

    public String consultarDonacion(String donacionId) {
        return restClient.get().uri("/donaciones/{id}", donacionId)
                .retrieve().body(String.class);
    }

    public String registrarQueja(String donacionId, String descripcion) {
        Map<String, String> body = Map.of("descripcion", descripcion);
        return restClient.post().uri("/donaciones/{id}/queja", donacionId)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    public String verDonacionesPorDonadorYFecha(String donadorID, String fechaInicio) {
        String uri = (fechaInicio != null && !fechaInicio.isBlank())
                ? "/donaciones/search?donadorID={donadorID}&fechaInicio={fechaInicio}"
                : "/donaciones/search?donadorID={donadorID}";
        return restClient.get().uri(uri, donadorID, fechaInicio)
                .retrieve().body(String.class);
    }

    public String verProductosDisponibles() {
        return restClient.get().uri("/productos")
                .retrieve().body(String.class);
    }

    public String verCategorias() {
        return restClient.get().uri("/categorias")
                .retrieve().body(String.class);
    }

    public String verSubcategorias(String categoriaId) {
        return restClient.get().uri("/categorias/{id}/subcategorias", categoriaId)
                .retrieve().body(String.class);
    }

    public String agregarCategoria(String nombre, String descripcion) {
        Map<String, String> body = Map.of(
                "nombre", nombre,
                "descripcion", descripcion != null ? descripcion : ""
        );
        return restClient.post().uri("/categorias")
                .body(body)
                .retrieve()
                .body(String.class);
    }

    public String agregarSubcategoria(String categoriaId, String nombre) {
        Map<String, String> body = Map.of(
                "nombre", nombre,
                "categoriaID", categoriaId
        );
        return restClient.post().uri("/categorias/{id}/subcategorias", categoriaId)
                .body(body)
                .retrieve()
                .body(String.class);
    }

    public String agregarIdentificador(String tipo, String descripcion) {
        Map<String, String> body = Map.of(
                "tipo", tipo,
                "descripcion", descripcion
        );
        return restClient.post().uri("/identificadores")
                .body(body)
                .retrieve()
                .body(String.class);
    }

    public String agregarProducto(String nombre, String descripcion, String subcategoriaId, String identificadorId) {
        Map<String, String> body = Map.of(
                "nombre", nombre,
                "descripcion", descripcion != null ? descripcion : "",
                "subcategoriaID", subcategoriaId,
                "identificadorID", identificadorId
        );
        return restClient.post().uri("/productos")
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
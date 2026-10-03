package ar.edu.utn.dds.k3003.clients;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class DonadoresClient {

    private final RestClient restClient;

    public DonadoresClient(@Value("${servicios.url.donadores}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    // ---------- Donadores ----------

    public String crearDonador(String nombre, String apellido, Integer edad, String email,
                               String nroDocumento, String domicilio) {
        Map<String, Object> body = cuerpo(
                "nombre", nombre, "apellido", apellido, "edad", edad,
                "email", email, "nroDocumento", nroDocumento, "domicilio", domicilio);
        return ejecutar(() -> restClient.post().uri("/donadores").body(body).retrieve().body(String.class));
    }

    public String listarDonadores() {
        return ejecutar(() -> restClient.get().uri("/donadores").retrieve().body(String.class));
    }

    public String consultarDonador(String id) {
        return ejecutar(() -> restClient.get().uri("/donadores/{id}", id).retrieve().body(String.class));
    }

    public String modificarEstado(String id, String estado) {
        return ejecutar(() -> restClient.patch().uri("/donadores/{id}/estado", id)
                .body(Map.of("estado", estado)).retrieve().body(String.class));
    }

    public String modificarCategoria(String id, String categoria) {
        return ejecutar(() -> restClient.patch().uri("/donadores/{id}/categoria", id)
                .body(Map.of("categoria", categoria)).retrieve().body(String.class));
    }

    public String puedeDonar(String id) {
        return ejecutar(() -> restClient.get().uri("/donadores/{id}/puede-donar", id).retrieve().body(String.class));
    }

    public String estadisticas(String id) {
        return ejecutar(() -> restClient.get().uri("/donadores/{id}/estadisticas", id).retrieve().body(String.class));
    }

    public String registrarQueja(String id, String donacionID, String descripcion) {
        return ejecutar(() -> restClient.post().uri("/donadores/{id}/quejas", id)
                .body(Map.of("donacionID", donacionID, "descripcion", descripcion))
                .retrieve().body(String.class));
    }

    public String listarQuejas(String id) {
        return ejecutar(() -> restClient.get().uri("/donadores/{id}/quejas", id).retrieve().body(String.class));
    }

    // ---------- Entidades ----------

    public String crearEntidad(String razonSocial, String domicilio, String telefono, String correo) {
        Map<String, Object> body = cuerpo(
                "razonSocial", razonSocial, "domicilio", domicilio, "telefono", telefono, "correo", correo);
        return ejecutar(() -> restClient.post().uri("/entidades").body(body).retrieve().body(String.class));
    }

    public String listarEntidades() {
        return ejecutar(() -> restClient.get().uri("/entidades").retrieve().body(String.class));
    }

    public String consultarEntidad(String id) {
        return ejecutar(() -> restClient.get().uri("/entidades/{id}", id).retrieve().body(String.class));
    }

    public String modificarEntidad(String id, String razonSocial, String domicilio, String telefono, String correo) {
        Map<String, Object> body = cuerpo(
                "razonSocial", razonSocial, "domicilio", domicilio, "telefono", telefono, "correo", correo);
        return ejecutar(() -> restClient.patch().uri("/entidades/{id}", id).body(body).retrieve().body(String.class));
    }

    // ---------- Necesidades ----------

    public String registrarNecesidad(String entidadID, Integer nivelDeUrgencia, String descripcion,
                                     Integer cantidadObjetivo, String productoSolicitadoID, String tipo) {
        Map<String, Object> body = cuerpo(
                "entidadID", entidadID, "nivelDeUrgencia", nivelDeUrgencia, "descripcion", descripcion,
                "cantidadObjetivo", cantidadObjetivo, "productoSolicitadoID", productoSolicitadoID, "tipo", tipo);
        return ejecutar(() -> restClient.post().uri("/necesidades").body(body).retrieve().body(String.class));
    }

    public String consultarNecesidad(String id) {
        return ejecutar(() -> restClient.get().uri("/necesidades/{id}", id).retrieve().body(String.class));
    }

    public String listarInsatisfechasPorProducto(String productoID) {
        return ejecutar(() -> restClient.get().uri("/necesidades?productoID={p}", productoID)
                .retrieve().body(String.class));
    }

    public String satisfacerNecesidad(String id, Integer cantidad) {
        return ejecutar(() -> restClient.post().uri("/necesidades/{id}/satisfaccion", id)
                .body(Map.of("cantidad", cantidad)).retrieve().body(String.class));
    }

    public String modificarNecesidad(String id, Integer nivelDeUrgencia, String descripcion,
                                     Integer cantidadObjetivo, String productoSolicitadoID) {
        // El controller de la API recibe Map<String, String>: los números viajan como texto.
        Map<String, Object> body = cuerpo(
                "nivelDeUrgencia", nivelDeUrgencia != null ? nivelDeUrgencia.toString() : null,
                "descripcion", descripcion,
                "cantidadObjetivo", cantidadObjetivo != null ? cantidadObjetivo.toString() : null,
                "productoSolicitadoID", productoSolicitadoID);
        return ejecutar(() -> restClient.patch().uri("/necesidades/{id}", id).body(body).retrieve().body(String.class));
    }

    public String eliminarNecesidad(String id) {
        return ejecutar(() -> restClient.delete().uri("/necesidades/{id}", id).retrieve().body(String.class));
    }

    // ---------- Helpers ----------

    /** Arma un body a partir de pares clave/valor, omitiendo los valores nulos. */
    private static Map<String, Object> cuerpo(Object... paresClaveValor) {
        Map<String, Object> body = new LinkedHashMap<>();
        for (int i = 0; i < paresClaveValor.length; i += 2) {
            if (paresClaveValor[i + 1] != null) {
                body.put((String) paresClaveValor[i], paresClaveValor[i + 1]);
            }
        }
        return body;
    }

    /** Devuelve el motivo del error HTTP como texto para que el LLM pueda leerlo. */
    private static String ejecutar(Supplier<String> llamada) {
        try {
            return llamada.get();
        } catch (RestClientResponseException e) {
            return "Error " + e.getStatusCode().value() + ": " + e.getResponseBodyAsString();
        }
    }
}

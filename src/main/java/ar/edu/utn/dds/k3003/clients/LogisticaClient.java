package ar.edu.utn.dds.k3003.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LogisticaClient {

    private final RestClient restClient;

    public LogisticaClient(@Value("${logistica.url:https://logistica-hjaw.onrender.com}") String baseUrl) {

        this.restClient = RestClient.create(baseUrl);

    }

    public String consultarDepositos() {

        return restClient.get().uri("/depositos").retrieve().body(String.class);

    }

    public String consultarDepositoPorId(String depositoID) {

        return restClient.get().uri("/depositos/{id}", depositoID).retrieve().body(String.class);
    }

    public String consultarStock(String depositoID) {

        return restClient.get().uri("/depositos/{id}/stock", depositoID).retrieve().body(String.class);
    }

    public String consultarStockProducto(String productoID) {

        return restClient.get().uri("/depositos/stock/{productoID}", productoID).retrieve().body(String.class);
    }

    public String consultarAsignaciones() {

        return restClient.get().uri("/asignaciones").retrieve().body(String.class);
    }

    public String consultarAsignacionesPorEstado(String estado) {

        return restClient.get().uri("/asignaciones/estado/{estado}", estado).retrieve().body(String.class);
    }

    public String consultarAsignacionPorPaquete(String paqueteID) {

        return restClient.get().uri("/asignaciones/{id}", paqueteID).retrieve().body(String.class);
    }

    public String crearDeposito(String nombre, String direccion, Integer capacidadMaxima) {

        return restClient.post().uri("/depositos").body(java.util.Map.of("nombre", nombre, "direccion", direccion, "capacidadMaxima", capacidadMaxima)).retrieve().body(String.class);
    }

    public String configurarAlgoritmo(String depositoID, String algoritmo) {

        restClient.patch().uri("/depositos/{id}/algoritmo", depositoID).body(java.util.Map.of("algoritmo", algoritmo.toUpperCase())).retrieve().toBodilessEntity();

        return "Algoritmo configurado correctamente";
    }

    public String vaciarStock(String depositoID) {

        restClient.delete().uri("/depositos/{id}/stock", depositoID).retrieve().toBodilessEntity();

        return "Stock vaciado correctamente";
    }

    public String eliminarPaquetes() {

        restClient.delete().uri("/depositos/stock").retrieve().toBodilessEntity();

        return "Paquetes eliminados correctamente";
    }

    public String eliminarAsignaciones() {

        restClient.delete().uri("/asignaciones").retrieve().toBodilessEntity();

        return "Asignaciones eliminadas correctamente";
    }

    public String eliminarDepositos() {

        restClient.delete().uri("/depositos").retrieve().toBodilessEntity();

        return "Depósitos eliminados correctamente";
    }
}
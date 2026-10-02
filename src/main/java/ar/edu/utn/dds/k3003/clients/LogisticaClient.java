package ar.edu.utn.dds.k3003.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LogisticaClient {

    private final RestClient restClient;

    public LogisticaClient(
            @Value("${logistica.url:https://logistica-hjaw.onrender.com}")
            String baseUrl) {

        this.restClient = RestClient.create(baseUrl);
    }

    public String consultarDepositos() {

        JsonNode lista = restClient.get().uri("/depositos").retrieve().body(JsonNode.class);

        if (lista == null || !lista.isArray() || lista.isEmpty()) {

            return "No hay depositos registrados.";
        }

        StringBuilder sb = new StringBuilder("Depositos registrados\n\n");

        for (JsonNode deposito : lista) {

            int cantidadStock = 0;

            JsonNode stockActual = deposito.path("stockActual");

            if (stockActual.isArray()) {

                for (JsonNode paquete : stockActual) {

                    cantidadStock += paquete.path("cantidad").asInt();
                }
            }

            String algoritmo = deposito.path("algoritmo").isNull() ? "SUB_ATENDIDOS (por defecto)" : deposito.path("algoritmo").asText("-");

            algoritmo = switch (algoritmo) {

                case "SUB_ATENDIDOS" -> "Sub Atendidos";

                case "PRIORIDAD_POR_SCORE" -> "Prioridad por Score";

                default -> algoritmo;
            };

            sb.append("ID: ").append(deposito.path("id").asText("?")).append("\n");

            sb.append("Nombre: ").append(deposito.path("nombre").asText("-")).append("\n");

            sb.append("Direccion: ").append(deposito.path("direccion").asText("-")).append("\n");

            sb.append("Capacidad maxima: ").append(deposito.path("capacidadMaxima").asText("-")).append("\n");

            sb.append("Algoritmo: ").append(algoritmo).append("\n");

            sb.append("Cantidad total de productos almacenados: ").append(cantidadStock).append("\n\n");
        }

        return sb.toString();
    }

    public String consultarDepositoPorId(String depositoID) {

        JsonNode deposito = restClient.get().uri("/depositos/{id}", depositoID).retrieve().body(JsonNode.class);

        int cantidadStock = 0;

        JsonNode stockActual = deposito.path("stockActual");

        if (stockActual.isArray()) {

            for (JsonNode paquete : stockActual) {

                cantidadStock += paquete.path("cantidad").asInt();
            }
        }

        String algoritmo = deposito.path("algoritmo").isNull() ? "SUB_ATENDIDOS (por defecto)" : deposito.path("algoritmo").asText("-");

        algoritmo = switch (algoritmo) {

            case "SUB_ATENDIDOS" -> "Sub Atendidos";

            case "PRIORIDAD_POR_SCORE" -> "Prioridad por Score";

            default -> algoritmo;
        };

        return """
                Deposito

                ID: %s
                Nombre: %s
                Direccion: %s
                Capacidad maxima: %s
                Algoritmo: %s
                Cantidad total de productos almacenados: %s
                """.formatted(
                deposito.path("id").asText("-"),
                deposito.path("nombre").asText("-"),
                deposito.path("direccion").asText("-"),
                deposito.path("capacidadMaxima").asText("-"),
                algoritmo,
                cantidadStock
        );
    }

    public String consultarStock(String depositoID) {

        JsonNode stock = restClient.get().uri("/depositos/{id}/stock", depositoID).retrieve().body(JsonNode.class);

        if (stock == null || !stock.isArray() || stock.isEmpty()) {

            return "El deposito no tiene stock.";
        }

        StringBuilder sb = new StringBuilder("Stock del deposito\n\n");

        for (JsonNode paquete : stock) {

            sb.append("Producto: ").append(paquete.path("producto").asText("-")).append("\n");

            sb.append("Cantidad: ").append(paquete.path("cantidad").asText("-")).append("\n");

            sb.append("Donacion: ").append(paquete.path("donacionID").asText("-")).append("\n\n");
        }

        return sb.toString();
    }

    public String consultarStockProducto(String productoID) {

        String cantidad = restClient.get().uri("/depositos/stock/{productoID}", productoID).retrieve().body(String.class);

        return """
                Stock por producto

                Producto: %s
                Cantidad total disponible: %s
                """.formatted(
                productoID,
                cantidad
        );
    }

    public String consultarAsignaciones() {

        JsonNode lista = restClient.get().uri("/asignaciones").retrieve().body(JsonNode.class);

        if (lista == null || !lista.isArray() || lista.isEmpty()) {

            return "No hay asignaciones registradas.";
        }

        StringBuilder sb = new StringBuilder("Asignaciones\n\n");

        for (JsonNode asignacion : lista) {

            sb.append("ID: ").append(asignacion.path("id").asText("-")).append("\n");

            sb.append("Paquete: ").append(asignacion.path("paqueteID").asText("-")).append("\n");

            sb.append("Necesidad: ").append(asignacion.path("necesidadID").asText("-")).append("\n");

            sb.append("Estado: ").append(asignacion.path("estado").asText("-")).append("\n\n");
        }

        return sb.toString();
    }

    public String consultarAsignacionesPorEstado(String estado) {

        JsonNode lista = restClient.get().uri("/asignaciones/estado/{estado}", estado).retrieve().body(JsonNode.class);

        if (lista == null || !lista.isArray() || lista.isEmpty()) {

            return "No hay asignaciones en estado " + estado;
        }

        StringBuilder sb = new StringBuilder("Asignaciones " + estado + "\n\n");

        for (JsonNode asignacion : lista) {

            sb.append("ID: ").append(asignacion.path("id").asText("-")).append("\n");

            sb.append("Paquete: ").append(asignacion.path("paqueteID").asText("-")).append("\n");

            sb.append("Necesidad: ").append(asignacion.path("necesidadID").asText("-")).append("\n\n");
        }

        return sb.toString();
    }

    public String consultarAsignacionPorPaquete(String paqueteID) {

        JsonNode asignacion = restClient.get().uri("/asignaciones/{id}", paqueteID).retrieve().body(JsonNode.class);

        return """
                Asignacion

                ID: %s
                Paquete: %s
                Necesidad: %s
                Estado: %s
                """.formatted(
                asignacion.path("id").asText("-"),
                asignacion.path("paqueteID").asText("-"),
                asignacion.path("necesidadID").asText("-"),
                asignacion.path("estado").asText("-")
        );
    }

    public String crearDeposito(String nombre, String direccion, Integer capacidadMaxima) {

        JsonNode deposito = restClient.post().uri("/depositos").body(java.util.Map.of("nombre", nombre, "direccion", direccion, "capacidadMaxima", capacidadMaxima)).retrieve().body(JsonNode.class);

        return """
                Deposito creado

                ID: %s
                Nombre: %s
                Direccion: %s
                Capacidad maxima: %s
                Algoritmo inicial: SUB_ATENDIDOS (por defecto)
                Cantidad total de productos almacenados: 0
                """.formatted(
                deposito.path("id").asText("-"),
                deposito.path("nombre").asText("-"),
                deposito.path("direccion").asText("-"),
                deposito.path("capacidadMaxima").asText("-")
        );
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

        return "Depositos eliminados correctamente";
    }
}
package ar.edu.utn.dds.k3003.tools;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class IncentivosTools {

    private final RestClient restClient;

    public IncentivosTools(@Value("${servicios.url.incentivos}") String incentivosUrl) {
        this.restClient = RestClient.create(incentivosUrl);
    }

    @Tool(name = "incentivos_crear_insignia", description = "Crea una nueva insignia en el sistema de Incentivos.")
    public String crearInsignia(
            @ToolParam(description = "Nombre de la insignia") String nombre,
            @ToolParam(description = "Descripción de la insignia") String descripcion) {
        return restClient.post().uri("/insignias")
                .body(Map.of("nombre", nombre, "descripcion", descripcion))
                .retrieve().body(String.class);
    }

    @Tool(name = "incentivos_listar_insignias", description = "Lista todas las insignias existentes.")
    public String listarInsignias() {
        return restClient.get().uri("/insignias").retrieve().body(String.class);
    }

    @Tool(name = "incentivos_consultar_insignia", description = "Busca una insignia por su ID (formato 'ins-X').")
    public String consultarInsignia(@ToolParam(description = "ID de la insignia, ej: 'ins-1'") String insigniaID) {
        return restClient.get().uri("/insignias/{id}", insigniaID).retrieve().body(String.class);
    }

    @Tool(name = "incentivos_asignar_insignia_a_donador", description = "Asigna manualmente una insignia a un donador.")
    public String asignarInsigniaADonador(
            @ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID,
            @ToolParam(description = "ID de la insignia, ej: 'ins-1'") String insigniaID) {
        restClient.post().uri("/insignias/{donadorID}", donadorID)
                .body(Map.of("insigniaID", insigniaID))
                .retrieve().toBodilessEntity();
        return "Insignia " + insigniaID + " asignada al donador " + donadorID;
    }

    @Tool(name = "incentivos_consultar_insignias_donador", description = "Lista las insignias que ya tiene un donador.")
    public String consultarInsigniasDeDonador(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        return restClient.get().uri("/insignias/{donadorID}", donadorID).retrieve().body(String.class);
    }

    @Tool(name = "incentivos_crear_mision", description = "Crea una nueva misión en el sistema.")
    public String crearMision(
            @ToolParam(description = "Nombre de la misión") String nombre,
            @ToolParam(description = "ID de la insignia que otorga al completarse") String insigniaID,
            @ToolParam(description = "Categoría inicial: OCASIONAL, COLABORADOR, TRANSFORMADOR, SALVADOR o REVOLUCIONARIO") String categoriaInicio,
            @ToolParam(description = "Categoría final a la que asciende") String categoriaFin,
            @ToolParam(description = "Tipo: COMPLETITUD, DONACIONES_EXITOSAS, DONACIONES_ASCENDENTES o REVOLUCION_DONADORA") String tipo) {
        Map<String, String> body = Map.of(
                "nombre", nombre, "insigniaID", insigniaID,
                "categoriaInicio", categoriaInicio, "categoriaFin", categoriaFin, "tipo", tipo);
        return restClient.post().uri("/misiones").body(body).retrieve().body(String.class);
    }

    @Tool(name = "incentivos_listar_misiones", description = "Lista todas las misiones existentes.")
    public String listarMisiones() {
        return restClient.get().uri("/misiones").retrieve().body(String.class);
    }

    @Tool(name = "incentivos_consultar_mision", description = "Busca una misión por su ID (formato 'mis-X').")
    public String consultarMision(@ToolParam(description = "ID de la misión, ej: 'mis-1'") String misionID) {
        return restClient.get().uri("/misiones/{id}", misionID).retrieve().body(String.class);
    }

    @Tool(name = "incentivos_consultar_mision_en_curso", description = "Consulta la misión en curso de un donador.")
    public String consultarMisionEnCurso(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        return restClient.get().uri("/misiones/{donadorID}", donadorID).retrieve().body(String.class);
    }

    @Tool(name = "incentivos_asignar_mision_a_donador", description = "Asigna una misión a un donador para que pase a estar en curso.")
    public String asignarMisionADonador(
            @ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID,
            @ToolParam(description = "ID de la misión, ej: 'mis-1'") String misionID) {
        restClient.post().uri("/misiones/{donadorID}", donadorID)
                .body(Map.of("misionID", misionID))
                .retrieve().toBodilessEntity();
        return "Misión " + misionID + " asignada al donador " + donadorID;
    }

    @Tool(name = "incentivos_procesar_donador", description = "Fuerza el procesamiento de un donador: evalúa el progreso de su misión en curso.")
    public String procesarDonador(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        restClient.post().uri("/procesamiento/{donadorID}", donadorID).retrieve().toBodilessEntity();
        return "Procesamiento ejecutado para el donador " + donadorID;
    }

    @Tool(name = "incentivos_consultar_estado_donador", description = "Devuelve el estado completo de un donador: categoría, insignias, misión actual e historial de categorías.")
    public String consultarEstadoDonador(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        return restClient.get().uri("/donadores/{donadorID}/estado", donadorID).retrieve().body(String.class);
    }
}
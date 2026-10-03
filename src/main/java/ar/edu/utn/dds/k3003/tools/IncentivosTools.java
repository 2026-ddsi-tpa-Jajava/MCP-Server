package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.clients.IncentivosClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class IncentivosTools {

    private final IncentivosClient incentivosClient;

    public IncentivosTools(IncentivosClient incentivosClient) {
        this.incentivosClient = incentivosClient;
    }

    @Tool(name = "incentivos_crear_insignia", description = "Crea una nueva insignia en el sistema de Incentivos.")
    public String crearInsignia(
            @ToolParam(description = "Nombre de la insignia") String nombre,
            @ToolParam(description = "Descripción de la insignia") String descripcion) {
        return incentivosClient.crearInsignia(nombre, descripcion);
    }

    @Tool(name = "incentivos_listar_insignias", description = "Lista todas las insignias existentes.")
    public String listarInsignias() {
        return incentivosClient.listarInsignias();
    }

    // @Tool(name = "incentivos_consultar_insignia", description = "Busca una insignia por su ID (formato 'ins-X').")
    // public String consultarInsignia(@ToolParam(description = "ID de la insignia, ej: 'ins-1'") String insigniaID) {
    //     return incentivosClient.consultarInsignia(insigniaID);
    // }

    // @Tool(name = "incentivos_asignar_insignia_a_donador", description = "Asigna manualmente una insignia a un donador.")
    // public String asignarInsigniaADonador(
    //         @ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID,
    //         @ToolParam(description = "ID de la insignia, ej: 'ins-1'") String insigniaID) {
    //     incentivosClient.asignarInsigniaADonador(donadorID, insigniaID);
    //     return "Insignia " + insigniaID + " asignada al donador " + donadorID;
    // }

    // @Tool(name = "incentivos_consultar_insignias_donador", description = "Lista las insignias que ya tiene un donador.")
    // public String consultarInsigniasDeDonador(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
    //     return incentivosClient.consultarInsigniasDeDonador(donadorID);
    // }

    @Tool(name = "incentivos_crear_mision", description = "Crea una nueva misión en el sistema.")
    public String crearMision(
            @ToolParam(description = "Nombre de la misión") String nombre,
            @ToolParam(description = "ID de la insignia que otorga al completarse") String insigniaID,
            @ToolParam(description = "Categoría inicial: OCASIONAL, COLABORADOR, TRANSFORMADOR, SALVADOR o REVOLUCIONARIO") String categoriaInicio,
            @ToolParam(description = "Categoría final a la que asciende") String categoriaFin,
            @ToolParam(description = "Tipo: COMPLETITUD, DONACIONES_EXITOSAS, DONACIONES_ASCENDENTES o REVOLUCION_DONADORA") String tipo) {
        return incentivosClient.crearMision(nombre, insigniaID, categoriaInicio, categoriaFin, tipo);
    }

    @Tool(name = "incentivos_listar_misiones", description = "Lista todas las misiones existentes.")
    public String listarMisiones() {
        return incentivosClient.listarMisiones();
    }

    @Tool(name = "incentivos_consultar_mision", description = "Busca una misión por su ID (formato 'mis-X').")
    public String consultarMision(@ToolParam(description = "ID de la misión, ej: 'mis-1'") String misionID) {
        return incentivosClient.consultarMision(misionID);
    }

    @Tool(name = "incentivos_consultar_mision_en_curso", description = "Consulta la misión en curso de un donador.")
    public String consultarMisionEnCurso(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        return incentivosClient.consultarMisionEnCurso(donadorID);
    }

    // @Tool(name = "incentivos_asignar_mision_a_donador", description = "Asigna una misión a un donador para que pase a estar en curso.")
    // public String asignarMisionADonador(
    //         @ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID,
    //         @ToolParam(description = "ID de la misión, ej: 'mis-1'") String misionID) {
    //     incentivosClient.asignarMisionADonador(donadorID, misionID);
    //     return "Misión " + misionID + " asignada al donador " + donadorID;
    // }

    @Tool(name = "incentivos_procesar_donador", description = "Fuerza el procesamiento de un donador: evalúa el progreso de su misión en curso.")
    public String procesarDonador(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        incentivosClient.procesarDonador(donadorID);
        return "Procesamiento ejecutado para el donador " + donadorID;
    }

    @Tool(name = "incentivos_consultar_estado_donador", description = "Devuelve el estado completo de un donador: categoría, insignias, misión actual e historial de categorías.")
    public String consultarEstadoDonador(@ToolParam(description = "ID del donador, ej: 'd-1'") String donadorID) {
        return incentivosClient.consultarEstadoDonador(donadorID);
    }
}

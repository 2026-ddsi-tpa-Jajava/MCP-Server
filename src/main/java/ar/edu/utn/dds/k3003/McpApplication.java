package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.tools.IncentivosTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpApplication.class, args);
    }


    @Bean
    public ToolCallbackProvider mcpTools(IncentivosTools incentivosTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(incentivosTools)
                // hay que agregar para cada modulo esto muchachos: .toolObjects(logisticaTools, donadoresTools)
                .build();
    }
}
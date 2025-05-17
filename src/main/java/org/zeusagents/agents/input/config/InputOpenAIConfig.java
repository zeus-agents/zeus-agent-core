package org.zeusagents.agents.input.config;

import jade.core.Agent;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InputOpenAIConfig {
    private List<String> middleAgents;
}

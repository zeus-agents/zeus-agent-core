package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class InputOpenAIConfig {
    private List<String> middleAgents;
    private InputBehaviourTypes inputBehaviourTypes;
    private Map<String, InputBehaviourTypes> behaviourForMiddleAgent;
}

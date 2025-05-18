package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class InputOpenAIConfig {
    private InputBehaviourTypes inputBehaviourTypes;
}

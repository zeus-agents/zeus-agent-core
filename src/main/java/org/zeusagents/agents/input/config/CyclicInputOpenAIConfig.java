package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CyclicInputOpenAIConfig extends InputOpenAIConfig {

    @Builder
    CyclicInputOpenAIConfig(InputBehaviourTypes inputBehaviourTypes) {
        super(inputBehaviourTypes);
    }
}

package org.zeusagents.agents.input.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InputOpenAIConfig {
    protected InputBehaviourTypes inputBehaviourTypes;

    public InputOpenAIConfig(InputBehaviourTypes inputBehaviourTypes) {
        this.inputBehaviourTypes = inputBehaviourTypes;
    }
}

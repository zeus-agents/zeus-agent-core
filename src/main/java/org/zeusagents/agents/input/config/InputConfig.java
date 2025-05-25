package org.zeusagents.agents.input.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InputConfig {
    protected InputBehaviourTypes inputBehaviourTypes;

    public InputConfig(InputBehaviourTypes inputBehaviourTypes) {
        this.inputBehaviourTypes = inputBehaviourTypes;
    }
}

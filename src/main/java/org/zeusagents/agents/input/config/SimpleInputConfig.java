package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleInputConfig extends InputConfig {
    private int maxReceived;

    @Builder
    SimpleInputConfig(InputBehaviourTypes inputBehaviourTypes, int maxReceived) {
        super(inputBehaviourTypes);
        this.maxReceived=maxReceived;
    }
}

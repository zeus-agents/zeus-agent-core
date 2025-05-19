package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleInputOpenAIConfig extends InputOpenAIConfig{
    private int maxReceived;

    @Builder
    SimpleInputOpenAIConfig(InputBehaviourTypes inputBehaviourTypes, int maxReceived) {
        super(inputBehaviourTypes);
        this.maxReceived=maxReceived;
    }
}

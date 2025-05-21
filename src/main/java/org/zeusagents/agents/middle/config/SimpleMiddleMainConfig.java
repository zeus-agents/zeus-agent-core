package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

@Getter
@Setter
public class SimpleMiddleMainConfig extends MiddleMainConfig {
    private int maxReceived;

    @Builder
    public SimpleMiddleMainConfig(AIClient AIClient, MiddleBehaviourType middleBehaviourType, int maxReceived) {
        super(AIClient, middleBehaviourType);
        this.maxReceived = maxReceived;
    }
}

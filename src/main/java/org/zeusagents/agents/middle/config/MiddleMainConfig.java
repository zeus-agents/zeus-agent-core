package org.zeusagents.agents.middle.config;

import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

@Getter
@Setter
public abstract class MiddleMainConfig {
    private AIClient AIClient;
    private MiddleBehaviourType middleBehaviourType;

    public MiddleMainConfig(AIClient AIClient, MiddleBehaviourType middleBehaviourType) {
        this.AIClient = AIClient;
        this.middleBehaviourType = middleBehaviourType;
    }
}

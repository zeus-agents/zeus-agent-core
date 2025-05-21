package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

@Getter
@Setter
public class CyclicMiddleMainConfig extends MiddleMainConfig {

    @Builder
    public CyclicMiddleMainConfig(AIClient AIClient, MiddleBehaviourType middleBehaviourType) {
        super(AIClient, middleBehaviourType);
    }
}

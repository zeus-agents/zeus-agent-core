package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

@Getter
@Setter
public class TickMiddleMainConfig extends MiddleMainConfig {
    private long period;

    @Builder
    public TickMiddleMainConfig(AIClient AIClient, MiddleBehaviourType middleBehaviourType, long period) {
        super(AIClient, middleBehaviourType);
        this.period=period;
    }
}

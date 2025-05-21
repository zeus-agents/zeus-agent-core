package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

import java.util.List;

@Getter
@Setter
public class TickMiddleMainConfig extends MiddleMainConfig {
    private long period;

    @Builder
    public TickMiddleMainConfig(AIClient AIClient, MiddleMainBehaviourType middleMainBehaviourType, long period, List<MiddleFuncBehaviourtype> orderList) {
        super(AIClient, middleMainBehaviourType, orderList);
        this.period=period;
    }
}

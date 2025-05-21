package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.AIClient.AIClient;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TickMiddleMainConfig extends MiddleMainConfig {
    private long period;

    @Builder
    public TickMiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, long period) {
        super(middleMainBehaviourType, orderBehaviourWithClient);
        this.period=period;
    }
}

package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.AIClient.AIClient;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SimpleMiddleMainConfig extends MiddleMainConfig {
    private int maxReceived;

    @Builder
    public SimpleMiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, int maxReceived, long fsmPeriod) {
        super(middleMainBehaviourType, orderBehaviourWithClient, fsmPeriod);
        this.maxReceived = maxReceived;
    }
}

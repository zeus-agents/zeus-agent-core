package org.zeusagents.agents.middle.config;

import lombok.Getter;
import lombok.Setter;
import org.zeusagents.AIClient.AIClient;
import org.zeusagents.OutputClient.OutputClient;
import org.zeusagents.inputClient.InputClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public abstract class MiddleMainConfig {
    MiddleMainBehaviourType middleMainBehaviourType;
    private Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient;
    private long fsmPeriod;

    public MiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, long fsmPeriod) {
        this.middleMainBehaviourType = middleMainBehaviourType;
        this.orderBehaviourWithClient = orderBehaviourWithClient;
        this.fsmPeriod=fsmPeriod;
    }
}

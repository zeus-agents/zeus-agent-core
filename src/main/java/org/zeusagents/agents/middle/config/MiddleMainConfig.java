package org.zeusagents.agents.middle.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class MiddleMainConfig {
    private MiddleMainBehaviourType middleMainBehaviourType;
    private Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient;
    private long fsmPeriod;
    private boolean balance;

    public MiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, long fsmPeriod, boolean balance) {
        this.middleMainBehaviourType = middleMainBehaviourType;
        this.orderBehaviourWithClient = orderBehaviourWithClient;
        this.fsmPeriod=fsmPeriod;
        this.balance = balance;
    }
}

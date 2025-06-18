package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SimpleMiddleMainConfig extends MiddleMainConfig {
    private int maxReceived;

    @Builder
    public SimpleMiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, long fsmPeriod, boolean balance, int maxReceived) {
        super(middleMainBehaviourType, orderBehaviourWithClient, fsmPeriod, balance);
        this.maxReceived = maxReceived;
    }
}

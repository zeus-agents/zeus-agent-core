package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TickMiddleMainConfig extends MiddleMainConfig {
    private long period;

    @Builder
    public TickMiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, long fsmPeriod, boolean balance, long period) {
        super(middleMainBehaviourType, orderBehaviourWithClient, fsmPeriod, balance);
        this.period=period;
    }
}

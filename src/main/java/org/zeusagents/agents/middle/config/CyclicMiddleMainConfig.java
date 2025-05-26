package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class CyclicMiddleMainConfig extends MiddleMainConfig {

    @Builder
    public CyclicMiddleMainConfig(MiddleMainBehaviourType middleMainBehaviourType, Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient, long fsmPeriod) {
        super(middleMainBehaviourType, orderBehaviourWithClient, fsmPeriod);
    }
}

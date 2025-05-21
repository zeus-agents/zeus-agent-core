package org.zeusagents.agents.middle.config;

import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

import java.util.List;

@Getter
@Setter
public abstract class MiddleMainConfig {
    private AIClient AIClient;
    private List<MiddleFuncBehaviourtype> orderList;
    private MiddleMainBehaviourType middleMainBehaviourType;

    public MiddleMainConfig(AIClient AIClient, MiddleMainBehaviourType middleMainBehaviourType, List<MiddleFuncBehaviourtype> orderList) {
        this.AIClient = AIClient;
        this.middleMainBehaviourType = middleMainBehaviourType;
        this.orderList = orderList;
    }
}

package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

import java.util.List;

@Getter
@Setter
public class CyclicMiddleMainConfig extends MiddleMainConfig {

    @Builder
    public CyclicMiddleMainConfig(AIClient AIClient, MiddleMainBehaviourType middleMainBehaviourType, List<MiddleFuncBehaviourtype> orderList) {
        super(AIClient, middleMainBehaviourType, orderList);
    }
}

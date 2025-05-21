package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.AIClient;

import java.util.List;

@Getter
@Setter
public class SimpleMiddleMainConfig extends MiddleMainConfig {
    private int maxReceived;

    @Builder
    public SimpleMiddleMainConfig(AIClient AIClient, MiddleMainBehaviourType middleMainBehaviourType, int maxReceived, List<MiddleFuncBehaviourtype> orderList) {
        super(AIClient, middleMainBehaviourType, orderList);
        this.maxReceived = maxReceived;
    }
}

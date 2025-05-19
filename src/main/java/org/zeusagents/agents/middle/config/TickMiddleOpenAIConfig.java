package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.OpenAIClient;

@Getter
@Setter
public class TickMiddleOpenAIConfig extends MiddleOpenAIConfig{
    private long period;

    @Builder
    public TickMiddleOpenAIConfig(OpenAIClient openAIClient, MiddleBehaviourType middleBehaviourType, long period) {
        super(openAIClient, middleBehaviourType);
        this.period=period;
    }
}

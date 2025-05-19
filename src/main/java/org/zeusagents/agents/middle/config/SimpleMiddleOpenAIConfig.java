package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.OpenAIClient;

@Getter
@Setter
public class SimpleMiddleOpenAIConfig extends MiddleOpenAIConfig{
    private int maxReceived;

    @Builder
    public SimpleMiddleOpenAIConfig(OpenAIClient openAIClient, MiddleBehaviourType middleBehaviourType, int maxReceived) {
        super(openAIClient, middleBehaviourType);
        this.maxReceived = maxReceived;
    }
}

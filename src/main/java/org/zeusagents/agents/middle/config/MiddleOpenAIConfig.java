package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.OpenAIClient;

@Getter
@Setter
public abstract class MiddleOpenAIConfig {
    private OpenAIClient openAIClient;
    private MiddleBehaviourType middleBehaviourType;

    public MiddleOpenAIConfig(OpenAIClient openAIClient, MiddleBehaviourType middleBehaviourType) {
        this.openAIClient = openAIClient;
        this.middleBehaviourType = middleBehaviourType;
    }
}

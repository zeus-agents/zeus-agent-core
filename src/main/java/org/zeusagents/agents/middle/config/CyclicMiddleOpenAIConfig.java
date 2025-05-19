package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.openai.OpenAIClient;

@Getter
@Setter
public class CyclicMiddleOpenAIConfig extends MiddleOpenAIConfig{

    @Builder
    public CyclicMiddleOpenAIConfig(OpenAIClient openAIClient, MiddleBehaviourType middleBehaviourType) {
        super(openAIClient, middleBehaviourType);
    }
}

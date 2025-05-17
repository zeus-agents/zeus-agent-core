package org.zeusagents.agents.middle.config;

import lombok.Builder;
import lombok.Data;
import org.zeusagents.openai.OpenAIClient;

@Data
@Builder
public class MiddleOpenAIConfig {
    private OpenAIClient openAIClient;
    private MiddleBehaviourType middleBehaviourType;
}

package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickInputOpenAIConfig extends InputOpenAIConfig{
    private long periodReceiver;
    private long periodSender;

    @Builder
    public TickInputOpenAIConfig(InputBehaviourTypes inputBehaviourTypes, long periodReceiver, long periodSender) {
        super(inputBehaviourTypes);
        this.periodReceiver=periodReceiver;
        this.periodSender=periodSender;
    }
}

package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickInputConfig extends InputConfig {
    private long periodReceiver;
    private long periodSender;

    @Builder
    public TickInputConfig(InputBehaviourTypes inputBehaviourTypes, long periodReceiver, long periodSender) {
        super(inputBehaviourTypes);
        this.periodReceiver=periodReceiver;
        this.periodSender=periodSender;
    }
}

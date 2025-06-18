package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;

import java.util.List;

@Getter
@Setter
public class TickInputConfig extends InputConfig {
    private long periodReceiver;
    private long periodSender;

    @Builder
    TickInputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, boolean management, long periodReceiver, long periodSender) {
        super(inputBehaviourTypes, loadBalanceType, management);
        this.periodReceiver=periodReceiver;
        this.periodSender=periodSender;
    }
}

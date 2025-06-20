package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.monitor.data.StatsToMonitor;

import java.util.List;

@Getter
@Setter
public class SimpleInputConfig extends InputConfig {
    private int maxReceived;

    @Builder
    SimpleInputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, boolean management, List<StatsToMonitor> statsToMonitor, int maxReceived) {
        super(inputBehaviourTypes, loadBalanceType, management, statsToMonitor);
        this.maxReceived=maxReceived;
    }
}

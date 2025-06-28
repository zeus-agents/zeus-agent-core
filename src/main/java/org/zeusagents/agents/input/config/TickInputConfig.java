package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.monitor.data.StatsToMonitor;

import java.util.List;

@Getter
@Setter
public class TickInputConfig extends InputConfig {
    private long periodReceiver;
    private long periodSender;

    @Builder
    TickInputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, boolean management, boolean enabledO2A, List<StatsToMonitor> statsToMonitor, long periodReceiver, long periodSender) {
        super(inputBehaviourTypes, loadBalanceType, management, enabledO2A, statsToMonitor);
        this.periodReceiver=periodReceiver;
        this.periodSender=periodSender;
    }
}

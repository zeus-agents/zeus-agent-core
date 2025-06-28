package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.monitor.data.StatsToMonitor;

import java.util.List;

@Getter
@Setter
public class CyclicInputConfig extends InputConfig {

    @Builder
    CyclicInputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, boolean management, boolean enabledO2A, List<StatsToMonitor> statsToMonitor) {
        super(inputBehaviourTypes, loadBalanceType, management, enabledO2A, statsToMonitor);
    }
}

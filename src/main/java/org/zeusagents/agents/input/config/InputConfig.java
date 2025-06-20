package org.zeusagents.agents.input.config;

import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.monitor.data.StatsToMonitor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class InputConfig {
    protected InputBehaviourTypes inputBehaviourTypes;
    protected LoadBalanceType loadBalanceType;
    protected boolean management = false;
    protected List<StatsToMonitor> statsToMonitor = new ArrayList<>();

    public InputConfig(InputBehaviourTypes inputBehaviourTypes) {
        this.inputBehaviourTypes = inputBehaviourTypes;
    }

    public InputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, boolean management, List<StatsToMonitor> statsToMonitor) {
        this.inputBehaviourTypes = inputBehaviourTypes;
        this.loadBalanceType = loadBalanceType;
        this.management = management;
        this.statsToMonitor=statsToMonitor;
    }
}

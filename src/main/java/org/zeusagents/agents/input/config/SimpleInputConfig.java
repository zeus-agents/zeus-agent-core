package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;

import java.util.List;

@Getter
@Setter
public class SimpleInputConfig extends InputConfig {
    private int maxReceived;

    @Builder
    SimpleInputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, List<String> loadBalancerAgentList, int maxReceived) {
        super(inputBehaviourTypes, loadBalanceType, loadBalancerAgentList);
        this.maxReceived=maxReceived;
    }
}

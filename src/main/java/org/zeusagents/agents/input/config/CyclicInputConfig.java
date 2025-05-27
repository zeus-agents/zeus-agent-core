package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;

import java.util.List;

@Getter
@Setter
public class CyclicInputConfig extends InputConfig {

    @Builder
    CyclicInputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, List<String> loadBalancerAgentList) {
        super(inputBehaviourTypes, loadBalanceType, loadBalancerAgentList);
    }
}

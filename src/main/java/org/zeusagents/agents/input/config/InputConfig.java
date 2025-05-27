package org.zeusagents.agents.input.config;

import lombok.Getter;
import lombok.Setter;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;

import java.util.List;

@Getter
@Setter
public abstract class InputConfig {
    protected InputBehaviourTypes inputBehaviourTypes;
    protected LoadBalanceType loadBalanceType;
    protected List<String> loadBalancerAgentList;

    public InputConfig(InputBehaviourTypes inputBehaviourTypes) {
        this.inputBehaviourTypes = inputBehaviourTypes;
        this.loadBalancerAgentList = null;
    }

    public InputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, List<String> loadBalancerAgentList) {
        this.inputBehaviourTypes = inputBehaviourTypes;
        this.loadBalanceType = loadBalanceType;
        this.loadBalancerAgentList = loadBalancerAgentList;
    }
}

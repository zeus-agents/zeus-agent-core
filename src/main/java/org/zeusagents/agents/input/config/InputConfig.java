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
    protected boolean management = false;

    public InputConfig(InputBehaviourTypes inputBehaviourTypes) {
        this.inputBehaviourTypes = inputBehaviourTypes;
    }

    public InputConfig(InputBehaviourTypes inputBehaviourTypes, LoadBalanceType loadBalanceType, boolean management) {
        this.inputBehaviourTypes = inputBehaviourTypes;
        this.loadBalanceType = loadBalanceType;
        this.management = management;
    }
}

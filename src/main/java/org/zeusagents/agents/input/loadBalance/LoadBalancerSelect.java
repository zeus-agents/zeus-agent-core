package org.zeusagents.agents.input.loadBalance;

import java.util.List;

public class LoadBalancerSelect {

    public static LoadBalance selectLoadBalancer(LoadBalanceType lbt){
        return switch (lbt){
            case RANDOM -> RandomLB.builder().build();
            case ROUND_ROBIN -> RoundRobinLB.builder().build();
            case NO_LOAD_BALANCER -> null;
        };
    }
}

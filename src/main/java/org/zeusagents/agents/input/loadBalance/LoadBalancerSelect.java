package org.zeusagents.agents.input.loadBalance;

import java.util.List;

public class LoadBalancerSelect {

    public static LoadBalance selectLoadBalancer(LoadBalanceType lbt, List<String> agentList){
        return switch (lbt){
            case RANDOM -> RandomLoadBalancer.builder().agentList(agentList).build();
            case ROUND_ROBIN -> RoundRobin.builder().agentList(agentList).build();
            case NO_LOAD_BALANCER -> null;
        };
    }
}

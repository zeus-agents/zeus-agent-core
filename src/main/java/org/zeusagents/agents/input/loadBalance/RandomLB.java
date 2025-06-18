package org.zeusagents.agents.input.loadBalance;

import lombok.Builder;

import java.util.Random;

@Builder
public class RandomLB implements LoadBalance {

    @Override
    public String getAgent() {
        int randomIndex = new Random().nextInt(MiddleAgentPool.agentMap.size());
        return MiddleAgentPool.agentMap.get(randomIndex);
    }
}

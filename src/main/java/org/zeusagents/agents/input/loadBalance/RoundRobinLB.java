package org.zeusagents.agents.input.loadBalance;

import lombok.Builder;

import java.util.List;

@Builder
public class RoundRobinLB implements LoadBalance{

    private static Integer offset = 0;

    @Override
    public String getAgent() {
        String agent = null;

        synchronized (offset){
            if (offset > MiddleAgentPool.agentMap.size() - 1){
                offset = 0;
            }
            agent = MiddleAgentPool.agentMap.get(offset);
            offset++;
        }
        return agent;
    }
}

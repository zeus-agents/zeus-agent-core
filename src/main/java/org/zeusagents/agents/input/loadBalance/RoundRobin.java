package org.zeusagents.agents.input.loadBalance;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoundRobin implements LoadBalance{

    public static Map<Integer, String> agentMap = new ConcurrentHashMap<>();
    private static Integer offset = 0;

    @Builder
    public RoundRobin(List<String> agentList){
        int i =0;
        for (String name: agentList){
            agentMap.put(i, name);
            i++;
        }
    }

    @Override
    public String getAgent() {
        String agent = null;

        synchronized (offset){
            if (offset > agentMap.size() - 1){
                offset = 0;
            }
            agent = agentMap.get(offset);
            offset++;
        }
        return agent;
    }
}

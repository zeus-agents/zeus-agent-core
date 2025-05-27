package org.zeusagents.agents.input.loadBalance;

import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RandomLoadBalancer implements LoadBalance {

    public static Map<Integer, String> agentMap = new ConcurrentHashMap<>();

    @Builder
    public RandomLoadBalancer(List<String> agentList) {
        int i =0;
        for (String name: agentList){
            agentMap.put(i, name);
        }
    }

    @Override
    public String getAgent() {
        int randomIndex = new Random().nextInt(agentMap.size());
        return agentMap.get(randomIndex);
    }
}

package org.zeusagents.agents.input.loadBalance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MiddleAgentPool {
    //TODO: Solo hay un pool en todo el programa se tiene que cambiar a que cada loadBalancer tenga el suyo
    public static Map<Integer, String> agentMap = new ConcurrentHashMap<>();

    synchronized public static void addAgent(String value){
        agentMap.put(agentMap.size(), value);
    }

    synchronized public static void deleteAgent(Integer key){
        agentMap.remove(key);
    }


    public static String printAgentMap() {
        StringBuilder mapAsString = new StringBuilder("{");
        for (Integer key : agentMap.keySet()) {
            mapAsString.append(key + "=" + agentMap.get(key) + ", ");
        }
        mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");

        return "MiddleAgentPool{agentMap="+mapAsString.toString()+"}";
    }
}

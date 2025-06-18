package org.zeusagents.outputclient;

import jade.core.Agent;

public class PrintOutputClient implements OutputClient{
    @Override
    public void execute(String result, Agent agent) {

        System.out.println("=== Received Message ===");
        System.out.println("Content: " + result);
        System.out.println("Agent: " + agent.getAID());
        System.out.println("=======================");

    }
}

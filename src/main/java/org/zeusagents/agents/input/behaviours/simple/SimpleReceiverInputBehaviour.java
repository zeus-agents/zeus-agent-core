package org.zeusagents.agents.input.behaviours.simple;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.behaviours.ReceiverCore;

public class SimpleReceiverInputBehaviour extends SimpleBehaviour {
    private int receivedCount = 0;
    private final int maxReceived;
    private final ReceiverCore receiverCore;

    @Builder
    public SimpleReceiverInputBehaviour(Agent inputAgent, int maxReceived) {
        super(inputAgent);
        this.maxReceived = maxReceived;
        this.receiverCore = ReceiverCore.builder().myAgent(this.myAgent).build();
    }

    @Override
    public void action() {
        System.out.println("[Input Agent] Behavior executing");
        this.receivedCount = this.receiverCore.receiveMessageSimpleBehaviour(this.receivedCount);
        block();
    }

    @Override
    public boolean done() {
        if (receivedCount >= maxReceived) {
            System.out.println(myAgent.getLocalName() + " finished processing");
            return true;
        }
        return false;
    }
}

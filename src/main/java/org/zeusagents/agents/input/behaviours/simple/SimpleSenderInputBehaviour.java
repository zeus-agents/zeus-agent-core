package org.zeusagents.agents.input.behaviours.simple;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.InputAgent;
import org.zeusagents.agents.input.behaviours.SenderCore;

public class SimpleSenderInputBehaviour extends SimpleBehaviour {

    private int receivedCount = 0;
    private int maxReceived;
    private final SenderCore senderCore;

    @Builder
    public SimpleSenderInputBehaviour(Agent inputAgent, int maxReceived) {
        super(inputAgent);
        this.maxReceived = maxReceived;
        this.senderCore = SenderCore.builder().myInputAgent((InputAgent) this.myAgent).build();
    }

    @Override
    public void action() {
        this.receivedCount = this.senderCore.sendMessageSimpleBehaviour(this, receivedCount);
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

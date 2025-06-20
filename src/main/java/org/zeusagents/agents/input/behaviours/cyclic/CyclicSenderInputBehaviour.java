package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.InputAgent;
import org.zeusagents.agents.input.behaviours.SenderCore;


public class CyclicSenderInputBehaviour extends CyclicBehaviour {

    private final SenderCore senderCore;

    @Builder
    public CyclicSenderInputBehaviour(Agent inputAgent) {
        super(inputAgent);
        this.senderCore = SenderCore.builder().myInputAgent((InputAgent) this.myAgent).build();
    }

    @Override
    public void action() {
        System.out.println("[ Input Agent] Behavior executing. ");
        this.senderCore.sendMessageCyclicBehaviour(this);
    }
}

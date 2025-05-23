package org.zeusagents.agents.input.behaviours.cyclic;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.behaviours.ReceiverCore;

public class CyclicReceiverInputBehaviourOpenAI extends CyclicBehaviour {

    private final ReceiverCore receiverCore;

    @Builder
    public CyclicReceiverInputBehaviourOpenAI(Agent inputAgent) {
        super(inputAgent);
        this.receiverCore = ReceiverCore.builder().myAgent(this.myAgent).build();
    }

    @Override
    public void action() {
        System.out.println("[Input OpenAPI Agent] Behavior executing");
        this.receiverCore.receiveMessageCyclicBehaviour();
        block();
    }
}

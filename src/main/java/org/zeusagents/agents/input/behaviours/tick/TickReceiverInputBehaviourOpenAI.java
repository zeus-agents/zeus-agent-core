package org.zeusagents.agents.input.behaviours.tick;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.behaviours.ReceiverCore;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class TickReceiverInputBehaviourOpenAI extends TickerBehaviour {

    private final ReceiverCore receiverCore;

    @Builder
    public TickReceiverInputBehaviourOpenAI(Agent inputAgent, long period) {
        super(inputAgent,period);
        this.receiverCore = ReceiverCore.builder().myAgent(this.myAgent).build();
    }

    @Override
    protected void onTick() {
        this.receiverCore.receiveMessageTickBehaviour();

    }
}

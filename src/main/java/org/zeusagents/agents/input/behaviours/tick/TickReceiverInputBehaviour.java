package org.zeusagents.agents.input.behaviours.tick;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.behaviours.ReceiverCore;

public class TickReceiverInputBehaviour extends TickerBehaviour {

    private final ReceiverCore receiverCore;

    @Builder
    public TickReceiverInputBehaviour(Agent inputAgent, long period) {
        super(inputAgent,period);
        this.receiverCore = ReceiverCore.builder().myAgent(this.myAgent).build();
    }

    @Override
    protected void onTick() {
        this.receiverCore.receiveMessageTickBehaviour();

    }
}

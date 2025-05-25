package org.zeusagents.agents.input.behaviours.tick;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.InputOpenAIAgent;
import org.zeusagents.agents.input.behaviours.SenderCore;

public class TickSenderInputBehaviour extends TickerBehaviour {

    private final SenderCore senderCore;

    @Builder
    public TickSenderInputBehaviour(Agent inputAgent, long period) {
        super(inputAgent, period);
        this.senderCore = SenderCore.builder().myInputAgent((InputOpenAIAgent) this.myAgent).build();
    }

    @Override
    protected void onTick() {
        this.senderCore.sendMessageTickBehaviour(this);
        //block();
    }
}

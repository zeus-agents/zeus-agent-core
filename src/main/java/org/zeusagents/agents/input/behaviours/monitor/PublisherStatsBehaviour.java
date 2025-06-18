package org.zeusagents.agents.input.behaviours.monitor;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.InputAgent;

public class PublisherStatsBehaviour extends TickerBehaviour {

    @Builder
    public PublisherStatsBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        String dummy = "Update at " + System.currentTimeMillis();
        InputAgent inputAgent = (InputAgent) this.myAgent;
        SubscriptionResponderBehaviour subMonitorResponder = inputAgent.getSubMonitorResponder();
        subMonitorResponder.getPublicSubscriptionManager().notifySubscribers(dummy);
    }
}

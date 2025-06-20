package org.zeusagents.agents.input.behaviours.monitor;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.InputAgent;
import org.zeusagents.agents.monitor.data.MonitorData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PublisherStatsBehaviour extends TickerBehaviour {

    @Builder
    public PublisherStatsBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        InputAgent inputAgent = (InputAgent) this.myAgent;

        MonitorData managementContent = MonitorData.builder().queueSize(inputAgent.getMessageCacheQueue().size()).build();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(managementContent);
            SubscriptionResponderBehaviour subMonitorResponder = inputAgent.getSubMonitorResponder();
            subMonitorResponder.getPublicSubscriptionManager().notifySubscribers(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

package org.zeusagents.agents.input.behaviours.monitor;

import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import org.zeusagents.agents.input.behaviours.monitor.manager.SubscriptionMonitorManager;

public class SubscriptionResponderBehaviour extends SubscriptionResponder {

    private final SubscriptionMonitorManager manager;

    public SubscriptionResponderBehaviour(Agent a, MessageTemplate mt, SubscriptionMonitorManager sm) {
        super(a, mt, sm);
        this.manager = sm;
    }

    public SubscriptionMonitorManager getPublicSubscriptionManager() {
        return this.manager;
    }
}

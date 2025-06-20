package org.zeusagents.agents.input.behaviours.monitor.manager;

import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;

import java.util.Vector;

public class SubscriptionMonitorManager implements SubscriptionResponder.SubscriptionManager {
    private final Vector<SubscriptionResponder.Subscription> subscriptions = new Vector<>();

    @Override
    public boolean register(SubscriptionResponder.Subscription s) {
        subscriptions.add(s);
        System.out.println("[Input Agent] Subscription received from " + s.getMessage().getSender().getLocalName());
        return true;
    }

    @Override
    public boolean deregister(SubscriptionResponder.Subscription s) {
        subscriptions.remove(s);
        System.out.println("[Input Agent] Unsubscribed: " + s.getMessage().getSender().getLocalName());
        return true;
    }

    public void notifySubscribers(byte[] updateMessage) {
        for (SubscriptionResponder.Subscription sub : subscriptions) {
            ACLMessage notification = sub.getMessage().createReply();
            notification.setPerformative(ACLMessage.INFORM);
            notification.setByteSequenceContent(updateMessage);
            sub.notify(notification);
        }
    }
}

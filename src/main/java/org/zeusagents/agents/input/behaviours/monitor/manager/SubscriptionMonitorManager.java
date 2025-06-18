package org.zeusagents.agents.input.behaviours.monitor.manager;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;

import java.util.Vector;

public class SubscriptionMonitorManager implements SubscriptionResponder.SubscriptionManager {
    private final Vector<SubscriptionResponder.Subscription> subscriptions = new Vector<>();

    @Override
    public boolean register(SubscriptionResponder.Subscription s) {
        subscriptions.add(s);
        System.out.println("Subscription received from " + s.getMessage().getSender().getLocalName());
        return true;
    }

    @Override
    public boolean deregister(SubscriptionResponder.Subscription s) {
        subscriptions.remove(s);
        System.out.println("Unsubscribed: " + s.getMessage().getSender().getLocalName());
        return true;
    }

    public void notifySubscribers(String updateMessage) {
        for (SubscriptionResponder.Subscription sub : subscriptions) {
            ACLMessage notification = sub.getMessage().createReply();
            notification.setPerformative(ACLMessage.INFORM);
            notification.setContent(updateMessage);
            sub.notify(notification);
        }
    }
}

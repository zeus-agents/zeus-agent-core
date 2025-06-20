package org.zeusagents.agents.monitor.behaviours.monitor;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import lombok.Builder;
import org.zeusagents.agents.monitor.data.MonitorData;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class AgentsMonitoringBehaviour extends SubscriptionInitiator {

    @Builder
    public AgentsMonitoringBehaviour(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        MonitorData monitorData = deserializeMessage(inform);
        System.out.println("[Manager Agent] INFORMED Queue: "+ monitorData.getQueueSize());
        System.out.println("[Manager Agent] INFORMED Memory: "+ monitorData.getMemoryUsed());

        if(monitorData.getQueueSize() >= 1){
            System.out.println("[Manager Agent] " + this.myAgent.getLocalName() + ": We have to create more agents by Queue size" );
        } else {
            System.out.println("[Manager Agent] " + this.myAgent.getLocalName() + ": GOOD STATUS!" );
        }

        if(monitorData.getMemoryUsed() >= 80){
            System.out.println("[Manager Agent] " + this.myAgent.getLocalName() + ": We have to create more agents by memory" );
        } else {
            System.out.println("[Manager Agent] " + this.myAgent.getLocalName() + ": GOOD STATUS!" );
        }
    }

    private MonitorData deserializeMessage(ACLMessage inform){
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inform.getByteSequenceContent()))) {
            return (MonitorData) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

package org.zeusagents.agents.middle.behaviours.functionalities;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class ReceiverMiddleBehaviour extends OneShotBehaviour {
    @Builder
    public ReceiverMiddleBehaviour(Agent agent, DataStore ds) {
        super(agent);
        this.setDataStore(ds);
    }

    @Override
    public void action() {
        ACLMessage msg = (ACLMessage) getDataStore().get("first-msg");

        BasicMessageInputAgent data = null;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(msg.getByteSequenceContent()))) {
            data = (BasicMessageInputAgent) ois.readObject();
            System.out.println("[Middle OpenAPI Agent "+ myAgent.getName()+"] Received: " + data.getMiddleAgentReceiver() +
                    " Content: " + data.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getDataStore().put("content", data);
        getDataStore().put("sender", msg.getSender());
        System.out.println("[FSM-STORE " + myAgent.getName()+ "] Stored message");
    }
}

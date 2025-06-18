package org.zeusagents.agents.manager.behaviours.monitor;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import lombok.Builder;
import org.zeusagents.agents.input.InputAgent;
import org.zeusagents.agents.manager.AgentManager;
import org.zeusagents.agents.manager.ConversationIds;
import org.zeusagents.agents.manager.data.ManagementData;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class AgentsMonitoringBehaviour extends CyclicBehaviour {

    @Builder
    public AgentsMonitoringBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        if (msg != null && msg.getConversationId().equals(ConversationIds.STATUS_REPORT.name())) {
            System.out.println(myAgent.getLocalName() + ": Received status from " + msg.getSender().getLocalName() + ": " + msg.getContent());
            ManagementData managementData = deserializeMessage(msg);

            if (managementData.getQueueSize() > 5) {
                System.out.println("[Management Agent] Create Agent ");
                Iterator<InputAgent> inputAgentIterator = ((AgentManager) this.myAgent).getInputAgentList().iterator();
                boolean find = false;
                while (inputAgentIterator.hasNext() && !find) {
                    InputAgent agent = inputAgentIterator.next();
                    find = agent.getAID().equals(msg.getSender());

                    if (find) {
                        createNewAgent(agent);
                    }
                }
            } else {
                System.out.println("[Management Agent] Kill extra Agents ");
            }

        } else {
            block();
        }
    }

    private ManagementData deserializeMessage(ACLMessage inputMsg) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(inputMsg.getByteSequenceContent()))) {
            ManagementData data = (ManagementData) ois.readObject();
            System.out.println("[Management Agent] Received: " + data.getQueueSize());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createNewAgent(InputAgent agent) {
        AgentContainer containerController = myAgent.getContainerController();

        //agent.getLoadBalance().getAgentMap().put()
    }
}

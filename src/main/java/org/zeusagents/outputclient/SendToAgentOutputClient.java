package org.zeusagents.outputclient;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.Builder;
import org.zeusagents.agents.data.BasicMessageInputAgent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendToAgentOutputClient implements OutputClient {

    private final String nextAgent;

    @Builder
    public SendToAgentOutputClient(String nextAgent) {
        this.nextAgent = nextAgent;
    }

    @Override
    public void execute(String result, Agent agent) {
        ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
        aclMessage.addReceiver(new AID(this.nextAgent, AID.ISLOCALNAME));
        aclMessage.setSender(agent.getAID());

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(BasicMessageInputAgent.builder().middleAgentReceiver(this.nextAgent).content(result).build());
            aclMessage.setByteSequenceContent(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Send: " + result + ". Next Agent: " + this.nextAgent + ". From Agent: " + aclMessage.getSender());
        agent.send(aclMessage);
    }
}

package org.zeusagents;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import org.zeusagents.agents.input.config.InputOpenAIConfig;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            AgentController middleOpenAIAgent = mainContainer.createNewAgent("middleOpenAIAgent",
                    "org.zeusagents.agents.middle.MiddleOpenAIAgent", null);
            middleOpenAIAgent.start();

            InputOpenAIConfig inputOpenAIConfig = InputOpenAIConfig.builder().middleAgents(List.of("middleOpenAIAgent")).build();
            Object[] objects = new Object[1];
            objects[0] = inputOpenAIConfig;

            AgentController inputOpenAIAgent = mainContainer.createNewAgent("inputOpenAIAgent",
                    "org.zeusagents.agents.input.InputOpenAIAgent", objects);
            inputOpenAIAgent.start();

            Thread.sleep(10000);
            sendMessage( inputOpenAIAgent, "CONFIG", "mode=production;timeout=5000");
            sendMessage( inputOpenAIAgent, "DATA", "{sensor:temp,value:23.5}");
            sendMessage( inputOpenAIAgent, "DATA", "{sensor:temp,value:23.5}");
            sendMessage( inputOpenAIAgent, "DATA", "{sensor:temp,value:23.5}");

            Thread.sleep(1000); //we need time to process more data cause the agent queue is full
            sendMessage( inputOpenAIAgent, "COMMAND", "shutdown");
            sendMessage( inputOpenAIAgent, "COMMAND", "shutdown");
            sendMessage( inputOpenAIAgent, "COMMAND", "shutdown");

        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static void sendMessage(AgentController inputOpenAIAgent, String type, String content) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(inputOpenAIAgent.getName(), AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setOntology(type);  // Using ontology as message type
            msg.setContent(content);

            System.out.println("[Main] Sending message - To: "+inputOpenAIAgent.getName()+", Type: " + type +
                    ", Content: " + content);

            inputOpenAIAgent.putO2AObject(msg, true); // false means asynchronous
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
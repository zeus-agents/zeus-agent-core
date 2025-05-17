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

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            AgentController openAIAgent = mainContainer.createNewAgent("openAIAgent", "org.zeusagents.agents.InputOpenAIAgent", null);
            openAIAgent.start();

            Thread.sleep(1000);
            sendMessage( openAIAgent, "CONFIG", "mode=production;timeout=5000");
            sendMessage( openAIAgent, "DATA", "{sensor:temp,value:23.5}");
            sendMessage( openAIAgent, "COMMAND", "shutdown");

        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static void sendMessage(AgentController openAIAgent, String type, String content) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID(openAIAgent.getName(), AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setOntology(type);  // Using ontology as message type
            msg.setContent(content);

            System.out.println("[Main] Sending message - Type: " + type +
                    ", Content: " + content);

            openAIAgent.putO2AObject(msg, false); // false means asynchronous
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
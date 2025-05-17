package org.zeusagents;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.InputOpenAIConfig;
import org.zeusagents.agents.middle.config.MiddleBehaviourType;
import org.zeusagents.agents.middle.config.MiddleOpenAIConfig;
import org.zeusagents.openai.OpenAITextGeneratorClient;

import java.util.List;

public class SimpleMain {
    public static void main(String[] args) {
        jade.core.Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);
        try {
            MiddleOpenAIConfig middleOpenAIConfig = MiddleOpenAIConfig.builder()
                    .openAIClient(new OpenAITextGeneratorClient())
                    .middleBehaviourType(MiddleBehaviourType.SIMPLE_MIDDLE_BEHAVIOUR_OPENAI)
                    .build();
            Object[] middleObjects = new Object[1];
            middleObjects[0] = middleOpenAIConfig;

            AgentController middleOpenAIAgent = mainContainer.createNewAgent("middleOpenAIAgent",
                    "org.zeusagents.agents.middle.MiddleOpenAIAgent", middleObjects);
            middleOpenAIAgent.start();

            InputOpenAIConfig inputOpenAIConfig = InputOpenAIConfig.builder()
                    .middleAgents(List.of("middleOpenAIAgent"))
                    .inputBehaviourTypes(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR_OPENAI)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputOpenAIConfig;

            AgentController inputOpenAIAgent = null;
            inputOpenAIAgent = mainContainer.createNewAgent("inputOpenAIAgent",
                    "org.zeusagents.agents.input.InputOpenAIAgent", inputObjects);
            inputOpenAIAgent.start();


            Thread.sleep(10000);
            sendMessage( inputOpenAIAgent, "CONFIG", "mode=production;timeout=5000");

        } catch (StaleProxyException | InterruptedException e) {
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

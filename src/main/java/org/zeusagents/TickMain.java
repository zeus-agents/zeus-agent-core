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
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.TickInputOpenAIConfig;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.middle.config.MiddleBehaviourType;
import org.zeusagents.agents.middle.config.TickMiddleOpenAIConfig;
import org.zeusagents.openai.OpenAITextGeneratorClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class TickMain {

    public static void main(String[] args) {
        jade.core.Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);
        try {
            createMiddleAgent(mainContainer, "middleOpenAIAgent1");
            createMiddleAgent(mainContainer, "middleOpenAIAgent2");

            TickInputOpenAIConfig inputOpenAIConfig = TickInputOpenAIConfig.builder()
                    .inputBehaviourTypes(InputBehaviourTypes.TICK_INPUT_BEHAVIOUR_OPENAI)
                    .periodReceiver(200)
                    .periodSender(1000)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputOpenAIConfig;

            AgentController inputOpenAIAgent = mainContainer.createNewAgent("inputOpenAIAgent",
                    "org.zeusagents.agents.input.InputOpenAIAgent", inputObjects);
            inputOpenAIAgent.start();

            Thread.sleep(10000);
            sendMessage( inputOpenAIAgent, "middleOpenAIAgent1","CONFIG", "mode=production;timeout=5000");
            sendMessage( inputOpenAIAgent,"middleOpenAIAgent1", "DATA1", "{sensor:temp,value:23.5}");
            sendMessage( inputOpenAIAgent,"middleOpenAIAgent2", "DATA2", "{sensor:temp,value:23.5}");
            sendMessage( inputOpenAIAgent,"middleOpenAIAgent2", "COMMAND", "shutdown");

        }catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createMiddleAgent(AgentContainer mainContainer, String nameAgent) throws StaleProxyException {
        TickMiddleOpenAIConfig middleOpenAIConfig = TickMiddleOpenAIConfig.builder()
                .openAIClient(new OpenAITextGeneratorClient())
                .middleBehaviourType(MiddleBehaviourType.TICK_MIDDLE_BEHAVIOUR_OPENAI)
                .period(200)
                .build();

        Object[] middleObjects = new Object[1];
        middleObjects[0] = middleOpenAIConfig;

        AgentController middleOpenAIAgent = mainContainer.createNewAgent(nameAgent,
                "org.zeusagents.agents.middle.MiddleOpenAIAgent", middleObjects);
        middleOpenAIAgent.start();
    }

    private static void sendMessage(AgentController inputOpenAIAgent, String middleAgentReceiver, String type, String content) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(inputOpenAIAgent.getName(), AID.ISLOCALNAME));
            msg.setEncoding("Base64");
            msg.setLanguage("English");
            msg.setOntology(type);  // Using ontology as message type

            BasicMessageInputAgent msgContent =
                    BasicMessageInputAgent.builder().middleAgentReceiver(middleAgentReceiver).content(content).build();

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(msgContent);
                msg.setByteSequenceContent(bos.toByteArray());
            }

            System.out.println("[Main] Sending message - To: " + inputOpenAIAgent.getName() + ", Type: " + type +
                    ", Content: " + content);

            inputOpenAIAgent.putO2AObject(msg, true); // false means asynchronous
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

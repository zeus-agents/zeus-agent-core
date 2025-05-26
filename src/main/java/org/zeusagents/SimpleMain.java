package org.zeusagents;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.zeusagents.OutputClient.PrintOutputClient;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.SimpleInputConfig;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.agents.middle.config.MiddleMainBehaviourType;
import org.zeusagents.agents.middle.config.SimpleMiddleMainConfig;
import org.zeusagents.AIClient.TextGeneratorClient;
import org.zeusagents.inputClient.InputACLMessageClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleMain {
    public static void main(String[] args) {
        jade.core.Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);
        try {
            createMiddleAgent(mainContainer, "middleOpenAIAgent1");
            createMiddleAgent(mainContainer, "middleOpenAIAgent2");

            SimpleInputConfig inputOpenAIConfig = SimpleInputConfig.builder()
                    .inputBehaviourTypes(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR_OPENAI)
                    .maxReceived(2)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputOpenAIConfig;

            AgentController inputOpenAIAgent = null;
            inputOpenAIAgent = mainContainer.createNewAgent("inputOpenAIAgent",
                    "org.zeusagents.agents.input.InputOpenAIAgent", inputObjects);
            inputOpenAIAgent.start();


            Thread.sleep(10000);
            sendMessage(inputOpenAIAgent, "middleOpenAIAgent1", "CONFIG", "mode=production;timeout=5001");
            sendMessage(inputOpenAIAgent, "middleOpenAIAgent2", "CONFIG", "mode=production;timeout=5002");

        } catch (StaleProxyException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createMiddleAgent(AgentContainer mainContainer, String nameAgent) throws StaleProxyException {
        Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient = new LinkedHashMap<>(); //keep the insertion order
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.RECEIVER_BEHAVIOUR, new InputACLMessageClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.GENERATOR_BEHAVIOUR, new TextGeneratorClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.FINAL_BEHAVIOUR, new PrintOutputClient());

        SimpleMiddleMainConfig middleOpenAIConfig = SimpleMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.SIMPLE)
                .orderBehaviourWithClient(orderBehaviourWithClient)
                .maxReceived(1)
                .fsmPeriod(200)
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

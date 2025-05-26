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
import org.zeusagents.OutputClient.PrintOutputClient;
import org.zeusagents.agents.input.config.CyclicInputConfig;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.middle.config.CyclicMiddleMainConfig;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.agents.middle.config.MiddleMainBehaviourType;
import org.zeusagents.AIClient.TextGeneratorClient;
import org.zeusagents.inputClient.InputACLMessageClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class CyclicMain {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            createMiddleAgent(mainContainer, "middleOpenAIAgent1");
            createMiddleAgent(mainContainer, "middleOpenAIAgent2");

            CyclicInputConfig inputOpenAIConfig = CyclicInputConfig.builder()
                    .inputBehaviourTypes(InputBehaviourTypes.CYCLIC_INPUT_BEHAVIOUR_OPENAI)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputOpenAIConfig;

            AgentController inputOpenAIAgent = mainContainer.createNewAgent("inputOpenAIAgent",
                    "org.zeusagents.agents.input.InputOpenAIAgent", inputObjects);
            inputOpenAIAgent.start();

            Thread.sleep(10000);
            sendMessage( inputOpenAIAgent, "middleOpenAIAgent1","CONFIG", "mode=production;timeout=5000");
            sendMessage( inputOpenAIAgent,"middleOpenAIAgent1", "DATA", "{sensor:temp,value:23.5}");

            sendMessage( inputOpenAIAgent,"middleOpenAIAgent2", "DATA", "{sensor:temp,value:23.5}");

            sendMessage( inputOpenAIAgent,"middleOpenAIAgent1", "COMMAND", "shutdown");
            sendMessage( inputOpenAIAgent,"middleOpenAIAgent2", "COMMAND", "shutdown");

        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createMiddleAgent(AgentContainer mainContainer, String nameAgent) throws StaleProxyException {
        Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient = new LinkedHashMap<>(); //keep the insertion order
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.RECEIVER_BEHAVIOUR, new InputACLMessageClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.GENERATOR_BEHAVIOUR, new TextGeneratorClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.FINAL_BEHAVIOUR, new PrintOutputClient());

        CyclicMiddleMainConfig middleOpenAIConfig = CyclicMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.CYCLIC)
                .orderBehaviourWithClient(orderBehaviourWithClient)
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
            msg.setEncoding("JADE-Encoding");
            msg.setLanguage("English");
            msg.setOntology(type);  // Using ontology as message type

            BasicMessageInputAgent msgContent =
                    BasicMessageInputAgent.builder().middleAgentReceiver(middleAgentReceiver).content(content).build();

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(msgContent);
                msg.setByteSequenceContent(bos.toByteArray());
            }

            System.out.println("[Main] Sending message - To: "+inputOpenAIAgent.getName()+", Type: " + type +
                    ", Content: " + content);

            inputOpenAIAgent.putO2AObject(msg, true); // false means asynchronous
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
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
import org.zeusagents.OutputClient.SendToAgentOutputClient;
import org.zeusagents.agents.AgentsClass;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.SimpleInputConfig;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.middle.config.CyclicMiddleMainConfig;
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
            createMiddleLastAgent(mainContainer, "lastTestAgent1");
            createMiddleLastAgent(mainContainer, "lastTestAgent2");

            createMiddleAgent(mainContainer, "middleTestAgent1", "lastTestAgent1");
            createMiddleAgent(mainContainer, "middleTestAgent2", "lastTestAgent2");

            SimpleInputConfig inputConfig = SimpleInputConfig.builder()
                    .inputBehaviourTypes(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR)
                    .loadBalanceType(LoadBalanceType.NO_LOAD_BALANCER)
                    .maxReceived(2)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputConfig;

            AgentController inputAgent = null;
            inputAgent = mainContainer.createNewAgent("inputTestAgent",
                    AgentsClass.INPUT_AGENT, inputObjects);
            inputAgent.start();


            Thread.sleep(10000);
            sendMessage(inputAgent, "middleTestAgent1", "CONFIG", "mode=production;timeout=5001");
            sendMessage(inputAgent, "middleTestAgent2", "CONFIG", "mode=production;timeout=5002");

        } catch (StaleProxyException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createMiddleLastAgent(AgentContainer mainContainer, String nameAgent) throws StaleProxyException {
        Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient = new LinkedHashMap<>(); //keep the insertion order
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.RECEIVER_BEHAVIOUR, new InputACLMessageClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.GENERATOR_BEHAVIOUR, new TextGeneratorClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.FINAL_BEHAVIOUR, new PrintOutputClient());

        SimpleMiddleMainConfig middleConfig = SimpleMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.SIMPLE)
                .orderBehaviourWithClient(orderBehaviourWithClient)
                .fsmPeriod(200)
                .build();

        Object[] middleObjects = new Object[1];
        middleObjects[0] = middleConfig;

        AgentController middleAgent = mainContainer.createNewAgent(nameAgent,
                AgentsClass.MIDDLE_AGENT, middleObjects);
        middleAgent.start();

    }

    private static void createMiddleAgent(AgentContainer mainContainer, String nameAgent,String nextNameAgent) throws StaleProxyException {
        Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient = new LinkedHashMap<>(); //keep the insertion order
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.RECEIVER_BEHAVIOUR, new InputACLMessageClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.GENERATOR_BEHAVIOUR, new TextGeneratorClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.FINAL_BEHAVIOUR, new SendToAgentOutputClient(nextNameAgent));

        SimpleMiddleMainConfig middleConfig = SimpleMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.SIMPLE)
                .orderBehaviourWithClient(orderBehaviourWithClient)
                .maxReceived(1)
                .fsmPeriod(200)
                .build();

        Object[] middleObjects = new Object[1];
        middleObjects[0] = middleConfig;

        AgentController middleAgent = mainContainer.createNewAgent(nameAgent,
                "org.zeusagents.agents.middle.MiddleAgent", middleObjects);
        middleAgent.start();

    }

    private static void sendMessage(AgentController inputAgent, String middleAgentReceiver, String type, String content) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(inputAgent.getName(), AID.ISLOCALNAME));
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

            System.out.println("[Main] Sending message - To: " + inputAgent.getName() + ", Type: " + type +
                    ", Content: " + content);

            inputAgent.putO2AObject(msg, true); // false means asynchronous
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

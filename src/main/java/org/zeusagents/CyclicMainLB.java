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
import org.zeusagents.AIClient.TextGeneratorClient;
import org.zeusagents.OutputClient.PrintOutputClient;
import org.zeusagents.OutputClient.SendToAgentOutputClient;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.config.CyclicInputConfig;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.middle.config.CyclicMiddleMainConfig;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.agents.middle.config.MiddleMainBehaviourType;
import org.zeusagents.inputClient.InputACLMessageClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class CyclicMainLB {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);

        try {
            createMiddleLastAgent(mainContainer, "lastOpenAIAgent1");
            createMiddleLastAgent(mainContainer, "lastOpenAIAgent2");

            createMiddleAgent(mainContainer, "middleOpenAIAgent1", "lastOpenAIAgent1");
            createMiddleAgent(mainContainer, "middleOpenAIAgent2", "lastOpenAIAgent2");

            LinkedList<String> loadBalancerAgentList = new LinkedList<>();
            loadBalancerAgentList.add("middleOpenAIAgent1");
            loadBalancerAgentList.add("middleOpenAIAgent2");

            CyclicInputConfig inputOpenAIConfig = CyclicInputConfig.builder()
                    .inputBehaviourTypes(InputBehaviourTypes.CYCLIC_INPUT_BEHAVIOUR_OPENAI)
                    .loadBalanceType(LoadBalanceType.ROUND_ROBIN)
                    .loadBalancerAgentList(loadBalancerAgentList)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputOpenAIConfig;

            AgentController inputOpenAIAgent = mainContainer.createNewAgent("inputOpenAIAgent",
                    "org.zeusagents.agents.input.InputAgent", inputObjects);
            inputOpenAIAgent.start();

            Thread.sleep(10000);
            sendMessage( inputOpenAIAgent, "CONFIG", "mode=production;timeout=5000");
            sendMessage( inputOpenAIAgent, "DATA", "{sensor:temp,value:23.5}");

            sendMessage( inputOpenAIAgent, "DATA", "{sensor:temp,value:23.5}");

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

    private static void createMiddleLastAgent(AgentContainer mainContainer, String nameAgent) throws StaleProxyException {
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
                "org.zeusagents.agents.middle.MiddleAgent", middleObjects);
        middleOpenAIAgent.start();

    }

    private static void createMiddleAgent(AgentContainer mainContainer, String nameAgent,String nextNameAgent) throws StaleProxyException {
        Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient = new LinkedHashMap<>(); //keep the insertion order
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.RECEIVER_BEHAVIOUR, new InputACLMessageClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.GENERATOR_BEHAVIOUR, new TextGeneratorClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.FINAL_BEHAVIOUR, new SendToAgentOutputClient(nextNameAgent));

        CyclicMiddleMainConfig middleOpenAIConfig = CyclicMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.CYCLIC)
                .orderBehaviourWithClient(orderBehaviourWithClient)
                .fsmPeriod(200)
                .build();

        Object[] middleObjects = new Object[1];
        middleObjects[0] = middleOpenAIConfig;

        AgentController middleOpenAIAgent = mainContainer.createNewAgent(nameAgent,
                "org.zeusagents.agents.middle.MiddleAgent", middleObjects);
        middleOpenAIAgent.start();

    }

    private static void sendMessage(AgentController inputOpenAIAgent, String type, String content) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(inputOpenAIAgent.getName(), AID.ISLOCALNAME));
            msg.setEncoding("JADE-Encoding");
            msg.setLanguage("English");
            msg.setOntology(type);  // Using ontology as message type

            BasicMessageInputAgent msgContent =
                    BasicMessageInputAgent.builder().content(content).build();

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
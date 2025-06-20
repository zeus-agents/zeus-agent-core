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
import org.zeusagents.agents.AgentsClass;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.input.config.InputBehaviourTypes;
import org.zeusagents.agents.input.config.TickInputConfig;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.middle.config.MiddleFuncBehaviourtype;
import org.zeusagents.agents.middle.config.MiddleMainBehaviourType;
import org.zeusagents.agents.middle.config.TickMiddleMainConfig;
import org.zeusagents.agents.monitor.config.MonitorConfig;
import org.zeusagents.aiclient.TextGeneratorClient;
import org.zeusagents.inputclient.InputACLMessageClient;
import org.zeusagents.outputclient.PrintOutputClient;
import org.zeusagents.outputclient.SendToAgentOutputClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TickMainManager {

    public static void main(String[] args) {
        Runtime rt = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");

        AgentContainer mainContainer = rt.createMainContainer(profile);
        try {
            createMiddleLastAgent(mainContainer, "lastTestAgent1");
            createMiddleLastAgent(mainContainer, "lastTestAgent2");

            createMiddleAgent(mainContainer, "middle1TestAgent1", "lastTestAgent1");
            createMiddleAgent(mainContainer, "middle2TestAgent2", "lastTestAgent2");

            createMiddleAgent(mainContainer, "middleTestAgent1", "middle1TestAgent1");
            createMiddleAgent(mainContainer, "middleTestAgent2", "middle2TestAgent2");

            TickInputConfig inputConfig = TickInputConfig.builder()
                    .inputBehaviourTypes(InputBehaviourTypes.TICK_INPUT_BEHAVIOUR)
                    .loadBalanceType(LoadBalanceType.NO_LOAD_BALANCER)
                    .periodReceiver(500)
                    .periodSender(500)
                    .build();

            Object[] inputObjects = new Object[1];
            inputObjects[0] = inputConfig;

            AgentController inputAgent = mainContainer.createNewAgent("inputTestAgent",
                    AgentsClass.INPUT_AGENT, inputObjects);
            inputAgent.start();

            List<AgentController> inputAgentsList = List.of(inputAgent);
            MonitorConfig monitorConfig = MonitorConfig.builder().inputAgentList(inputAgentsList).build();

            Object[] managerObjects = new Object[1];
            managerObjects[0] = monitorConfig;

            AgentController monitorAgent = mainContainer.createNewAgent("monitorAgent", AgentsClass.MANAGER_AGENT, managerObjects);
            monitorAgent.start();

            Thread.sleep(1000);
            sendMessage( inputAgent, "middleTestAgent1","CONFIG", "mode=production;timeout=5000");
            sendMessage( inputAgent,"middleTestAgent1", "DATA1", "{sensor:temp,value:23.5}");
            sendMessage( inputAgent,"middleTestAgent2", "DATA2", "{sensor:temp,value:23.5}");
            sendMessage( inputAgent,"middleTestAgent2", "COMMAND", "shutdown");

        }catch (StaleProxyException e) {
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

        TickMiddleMainConfig middleConfig = TickMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.TICK)
                .orderBehaviourWithClient(orderBehaviourWithClient)
                .period(200)
                .fsmPeriod(200)
                .build();

        Object[] middleObjects = new Object[1];
        middleObjects[0] = middleConfig;

        AgentController middleAgent = mainContainer.createNewAgent(nameAgent,
                AgentsClass.MIDDLE_AGENT, middleObjects);
        middleAgent.start();
    }

    private static void createMiddleAgent(AgentContainer mainContainer, String nameAgent, String nextNameAgent) throws StaleProxyException {
        Map<MiddleFuncBehaviourtype, Object> orderBehaviourWithClient = new LinkedHashMap<>(); //keep the insertion order
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.RECEIVER_BEHAVIOUR, new InputACLMessageClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.GENERATOR_BEHAVIOUR, new TextGeneratorClient());
        orderBehaviourWithClient.put(MiddleFuncBehaviourtype.FINAL_BEHAVIOUR, new SendToAgentOutputClient(nextNameAgent));

        TickMiddleMainConfig middleConfig = TickMiddleMainConfig.builder()
                .middleMainBehaviourType(MiddleMainBehaviourType.TICK)
                .orderBehaviourWithClient(orderBehaviourWithClient)
                .period(1000)
                .fsmPeriod(1000)
                .build();

        Object[] middleObjects = new Object[1];
        middleObjects[0] = middleConfig;

        AgentController middleAgent = mainContainer.createNewAgent(nameAgent,
                AgentsClass.MIDDLE_AGENT, middleObjects);
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

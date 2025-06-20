package org.zeusagents.agents.input;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import lombok.Getter;
import org.zeusagents.agents.input.behaviours.cyclic.CyclicReceiverInputBehaviour;
import org.zeusagents.agents.input.behaviours.monitor.PublisherStatsBehaviour;
import org.zeusagents.agents.input.behaviours.monitor.SubscriptionResponderBehaviour;
import org.zeusagents.agents.input.behaviours.monitor.manager.SubscriptionMonitorManager;
import org.zeusagents.agents.input.behaviours.simple.SimpleReceiverInputBehaviour;
import org.zeusagents.agents.input.behaviours.simple.SimpleSenderInputBehaviour;
import org.zeusagents.agents.input.behaviours.tick.TickReceiverInputBehaviour;
import org.zeusagents.agents.input.behaviours.tick.TickSenderInputBehaviour;
import org.zeusagents.agents.input.config.*;
import org.zeusagents.agents.input.loadBalance.LoadBalance;
import org.zeusagents.agents.input.loadBalance.LoadBalanceType;
import org.zeusagents.agents.input.loadBalance.LoadBalancerSelect;

import java.util.*;

@Getter
public class InputAgent extends Agent {
    private final Queue<ACLMessage> messageCacheQueue = new LinkedList<>();
    private InputConfig inputConfig;
    private LoadBalance loadBalance;
    private SubscriptionResponderBehaviour subMonitorResponder;

    protected void setup() {
        System.out.println("[Input Agent] ReceiverAgent " + getAID().getName() + " is ready");
        final Object[] args = getArguments();

        if(null != args){
            inputConfig = (InputConfig) args[0];
            //This accept data external from the JDE system
            setEnabledO2ACommunication(true, 10);

            this.loadBalance = LoadBalancerSelect.selectLoadBalancer(this.inputConfig.getLoadBalanceType());

            selectBehaviour();

            if(inputConfig.isManagement()){
                createSubscriberResponder();
            }
            System.out.println("[Input Agent] SETUP COMPLETE");
        } else{
            System.out.println("[Input Agent] SETUP ERROR!");
        }
    }

    private void selectBehaviour(){
        if(inputConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.CYCLIC_INPUT_BEHAVIOUR)){
            //In this case the Cyclic Sender is created by the Receiver to upgrade the CPU performance.
            CyclicInputConfig cyclicInputConfig = (CyclicInputConfig) inputConfig;
            addBehaviour(CyclicReceiverInputBehaviour.builder().inputAgent(this).build());
        }

        if(inputConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.SIMPLE_INPUT_BEHAVIOUR)){
            SimpleInputConfig simpleInputConfig = (SimpleInputConfig) inputConfig;
            addBehaviour(SimpleReceiverInputBehaviour.builder().inputAgent(this).maxReceived(simpleInputConfig.getMaxReceived()).build());
            addBehaviour(SimpleSenderInputBehaviour.builder().inputAgent(this).maxReceived(simpleInputConfig.getMaxReceived()).build());
        }

        if(inputConfig.getInputBehaviourTypes().equals(InputBehaviourTypes.TICK_INPUT_BEHAVIOUR)){
            TickInputConfig tickInputConfig = (TickInputConfig) inputConfig;
            addBehaviour(TickReceiverInputBehaviour.builder().inputAgent(this).period(tickInputConfig.getPeriodReceiver()).build());
            addBehaviour(TickSenderInputBehaviour.builder().inputAgent(this).period(tickInputConfig.getPeriodSender()).build());
        }
    }

    private void createSubscriberResponder(){
        System.out.println("[Input Agent] SETUP MONITORING");

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
        SubscriptionMonitorManager subscriptionMonitorManager = new SubscriptionMonitorManager();

        subMonitorResponder = new SubscriptionResponderBehaviour(this, mt, subscriptionMonitorManager);

        addBehaviour(subMonitorResponder);
        addBehaviour(PublisherStatsBehaviour.builder().a(this).period(100).build());

    }
}

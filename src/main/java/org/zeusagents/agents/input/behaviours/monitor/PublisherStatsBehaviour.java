package org.zeusagents.agents.input.behaviours.monitor;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import lombok.Builder;
import org.zeusagents.agents.input.InputAgent;
import org.zeusagents.agents.monitor.data.MonitorData;
import org.zeusagents.agents.monitor.data.StatsToMonitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;

public class PublisherStatsBehaviour extends TickerBehaviour {

    @Builder
    public PublisherStatsBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        InputAgent inputAgent = (InputAgent) this.myAgent;
        List<StatsToMonitor> statsToMonitor = inputAgent.getInputConfig().getStatsToMonitor();
        MonitorData.MonitorDataBuilder builder = MonitorData.builder();

        for ( StatsToMonitor stats: statsToMonitor){
            switch (stats){
                case CACHE_QUEUE_SIZE -> this.cacheQueueSize(builder,inputAgent);
                case QUEUE_SIZE -> this.queueSize(builder,inputAgent);
                case MEMORY_USED -> this.memoryUsed(builder);
            };
        }

        MonitorData monitorData = builder.build();
        System.out.println("[Input Agent] INFORMED Queue: "+ monitorData.getQueueSize());
        System.out.println("[Input Agent] INFORMED Memory: "+ monitorData.getMemoryUsed());

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(monitorData);
            SubscriptionResponderBehaviour subMonitorResponder = inputAgent.getSubMonitorResponder();
            subMonitorResponder.getPublicSubscriptionManager().notifySubscribers(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cacheQueueSize(MonitorData.MonitorDataBuilder builder, InputAgent inputAgent){
        builder.cacheQueueSize(inputAgent.getMessageCacheQueue().size());
    }

    public void queueSize(MonitorData.MonitorDataBuilder builder, InputAgent inputAgent){
        builder.queueSize(inputAgent.getQueueSize());
    }

    public void memoryUsed(MonitorData.MonitorDataBuilder builder){
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long used = memoryBean.getHeapMemoryUsage().getUsed();
        long max = memoryBean.getHeapMemoryUsage().getMax();
        System.out.println("[Input Agent] INFORMED Memory used: "+ used);
        System.out.println("[Input Agent] INFORMED Memory max: "+ max);
        builder.memoryUsed(used * 100 / max);
    }
}

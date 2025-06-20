package org.zeusagents.agents.monitor.config;

import jade.wrapper.AgentController;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MonitorConfig {
    private List<AgentController> inputAgentList;
}

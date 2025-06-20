package org.zeusagents.agents.monitor.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MonitorData implements Serializable {
    private int queueSize;
}

package org.zeusagents.agents.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BasicMessageInputAgent implements Serializable {
    private String middleAgentReceiver;
    private String content;
}

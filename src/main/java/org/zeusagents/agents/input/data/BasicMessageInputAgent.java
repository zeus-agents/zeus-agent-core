package org.zeusagents.agents.input.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BasicMessageInputAgent implements Serializable {
    private String middleAgentReceiver;
    private String content;
}

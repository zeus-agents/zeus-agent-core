package org.zeusagents.agents.input.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CyclicInputConfig extends InputConfig {

    @Builder
    CyclicInputConfig(InputBehaviourTypes inputBehaviourTypes) {
        super(inputBehaviourTypes);
    }
}

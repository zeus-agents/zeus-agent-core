package org.zeusagents.outputclient;

import jade.core.Agent;

public interface OutputClient {
    void execute(String result, Agent agent);
}

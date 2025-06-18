package org.zeusagents.inputclient;

import jade.core.behaviours.DataStore;
import org.zeusagents.agents.data.BasicMessageInputAgent;

public interface InputClient {
    BasicMessageInputAgent execute(DataStore ds);
}

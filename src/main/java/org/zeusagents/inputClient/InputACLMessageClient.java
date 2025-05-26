package org.zeusagents.inputClient;

import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import org.zeusagents.agents.data.BasicMessageInputAgent;
import org.zeusagents.agents.middle.config.DataStoreKeys;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class InputACLMessageClient implements InputClient {

    @Override
    public BasicMessageInputAgent execute(DataStore ds) {
        ACLMessage msg = (ACLMessage) ds.get(DataStoreKeys.INPUT_MESSAGE.name());

        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(msg.getByteSequenceContent()))) {
            return (BasicMessageInputAgent) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

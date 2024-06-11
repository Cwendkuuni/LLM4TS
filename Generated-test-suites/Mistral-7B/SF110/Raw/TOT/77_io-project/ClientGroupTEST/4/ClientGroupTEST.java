import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler clientHandler;

    @Before
    public void setup() {
        this.clientGroup = new ClientGroup(null);
        this.client1 = new Client("client1");
        this.client2 = new Client("client2");
        this.client3 = new Client("client3");
        this.clientHandler = new DummyClientHandler();
        this.clientGroup.addClientHandler(this.clientHandler);
        this.clientGroup.add(this.client1);
        this.clientGroup.add(this.client2);
        this.clientGroup.add(this.client3);
    }

    @Test
    public void testNotifyMessageSent() {
        this.clientGroup.notifyMessageSent(this.client1, "message1");
        // true since client1 is in the clientGroup

        this.clientGroup.notifyMessageSent(this.client3, "message2");
        // true since client3 is in the clientGroup or in any of its subGroups (as we haven't added any subGroups yet)

        ClientGroup subGroup = this.clientGroup.createSubGroup();
        this.clientGroup.add(subGroup);
        subGroup.add(this.client2);

        this.clientGroup.notifyMessageSent(this.client2, "message3");
        // true since client2 is in the current clientGroup or in its subGroups

        this.clientGroup.notifyMessageSent(this.client1, "message4");
        // true since client1 is in the clientGroup

        this.clientGroup.notifyMessageSent(new Client("client4"), "message5");
        // false since client4 is not in the clientGroup
    }
}

class DummyClientHandler implements ClientHandler {
    @Override
    public void messageSent(Client client, Object message) {
        // Do nothing
    }
}
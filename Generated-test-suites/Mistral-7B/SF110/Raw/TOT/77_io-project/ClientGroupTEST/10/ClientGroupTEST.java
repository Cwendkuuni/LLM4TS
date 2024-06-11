import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ClientGroupTest {
    // ... other imports and class setup ...

    private ClientGroup clientGroup;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setUp() {
        this.clientGroup = new ClientGroup(null);
        this.client = new Client("Client1");
        this.handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                // no-op
            }
        };
        this.clientGroup.addClientHandler(this.handler);
        this.clientGroup.add(this.client);
    }

    @Test
    public void testNotifyMessageSent() {
        Object message = new Object();
        assertTrue(this.clientGroup.notifyMessageSent(this.client, message));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifyMessageSentToNonExistentClient() {
        Client nonExistentClient = new Client("ClientX");
        assertFalse(this.clientGroup.notifyMessageSent(nonExistentClient, message));
    }

    @Test
    public void testNotifyMessageSentToSubGroup() {
        ClientGroup subGroup = this.clientGroup.createSubGroup();
        Client subGroupClient = new Client("SubGroupClient");
        subGroup.add(subGroupClient);

        Object message = new Object();
        assertTrue(subGroup.notifyMessageSent(this.client, message));
        assertTrue(this.clientGroup.notifyMessageSent(this.client, message));

        assertFalse(this.clientGroup.notifyMessageSent(subGroupClient, message));
    }

    // ... other tests for other methods ...
}
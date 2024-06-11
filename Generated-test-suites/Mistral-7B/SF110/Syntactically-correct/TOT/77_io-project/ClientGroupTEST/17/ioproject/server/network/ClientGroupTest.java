import org.junit.Test;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.Client;
import ioproject.server.network.ClientHandler;
import static org.junit.Assert.*;

public class ClientGroupTest {
    // 
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertTrue(true);
            }
        };
        clientGroup.addClientHandler(handler);
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    // Test edge case: client not in the group
    @Test
    public void testNotifyMessageSentEdgeCase() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                fail("Client not in the group should not invoke messageSent method");
            }
        };
        clientGroup.addClientHandler(handler);
        assertFalse(clientGroup.notifyMessageSent(client, message));
    }

    // Test error scenario: null message
    @Test(expected = NullPointerException.class)
    public void testNotifyMessageSentErrorScenario() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                // do nothing
            }
        };
        clientGroup.addClientHandler(handler);
        clientGroup.add(client);
        clientGroup.notifyMessageSent(client, null);
    }
    // 
}
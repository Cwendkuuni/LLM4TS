import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;
import java.util.HashSet;
import java.util.Set;

public class ClientGroupTest {
    private ClientGroup group;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setUp() {
        group = new ClientGroup(null);
        client = new Client("Test Client");
        handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                // Empty implementation for testing
            }
        };
        group.addClientHandler(handler);
        group.add(client);
    }

    @Test
    public void testNotifyMessageSent() {
        Object message = "Test Message";
        group.notifyMessageSent(client, message);

        // Check that message was sent to clients and sub-groups
        assertTrue(handler.wasMessageSentCalled); // Assuming we have a wasMessageSentCalled flag
        for (WeakReference<ClientGroup> subGroup : group.subGroups) {
            ClientGroup subGroupObj = subGroup.get();
            assertTrue(subGroupObj.wasMessageSentCalled); // Assuming we have a wasMessageSentCalled flag in sub-groups
        }
    }
}
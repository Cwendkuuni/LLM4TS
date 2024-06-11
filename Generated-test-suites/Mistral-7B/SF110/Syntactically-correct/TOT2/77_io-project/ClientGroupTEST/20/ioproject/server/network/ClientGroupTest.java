// ClientGroupTest.java - Basic Test Cases

import static org.junit.Assert.*;
import org.junit.Test;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {
    @Test
    public void testClientGroupCreation() {
        ClientGroup clientGroup = new ClientGroup(null);
        assertNotNull(clientGroup);
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client("Client 1");
        Object message = new Object();
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }

    // Test other basic methods with similar test cases
}
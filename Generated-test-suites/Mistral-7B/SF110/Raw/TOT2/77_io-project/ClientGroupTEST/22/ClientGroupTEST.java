import org.junit.Test;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.Client;
import ioproject.common.network.Message;

public class ClientGroupTest {
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client("Client 1");
        Message message = new Message("Test Message");

        clientGroup.add(client);
        clientGroup.handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                // assert message was sent to correct client
                assertSame(client, sender);
                // assert message is correct
                assertEquals("Test Message", message.getMessage());
            }
        };

        // test method call
        assertTrue(clientGroup.notifyMessageSent(client, message));

        // test method call with unregistered client
        assertFalse(clientGroup.notifyMessageSent(new Client("Client 2"), message));
    }
}
import org.junit.Before;
import org.junit.Test;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.Client;
import ioproject.server.network.ClientHandler;
import ioproject.common.network.Message;

public class ClientGroupTest {
    // Test variables
    private ClientGroup clientGroup;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setup() {
        this.clientGroup = new ClientGroup(null);
        this.client = new Client();
        this.clientGroup.add(this.client);
        this.handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                // Do nothing
            }
        };
        this.clientGroup.addClientHandler(this.handler);
    }

    @Test
    public void testNotifyMessageSent() {
        final Object message = new Object();
        assertTrue(this.clientGroup.notifyMessageSent(this.client, message));
        assertTrue(this.handler.isMessageSentCalled); // Assuming a boolean flag to track method calls
    }
}
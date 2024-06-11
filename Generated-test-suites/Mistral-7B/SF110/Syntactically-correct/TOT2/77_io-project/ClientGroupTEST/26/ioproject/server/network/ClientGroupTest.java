import org.junit.Test;
import static org.junit.Assert.assertTrue;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client("client1");
        ClientHandler clientHandler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                // do nothing
            }
        };
        clientGroup.addClientHandler(clientHandler);
        clientGroup.add(client);
        assertTrue(clientGroup.notifyMessageSent(client, new Object()));
    }
}
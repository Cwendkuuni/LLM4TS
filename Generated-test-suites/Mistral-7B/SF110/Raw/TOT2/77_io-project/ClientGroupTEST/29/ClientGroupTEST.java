import org.junit.Test;
import static org.junit.Assert.assertTrue;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.Client;
import ioproject.common.network.Message;

public class ClientGroupTest {
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Message("Test Message");
        assertTrue(clientGroup.notifyMessageSent(client, message));
    }
}
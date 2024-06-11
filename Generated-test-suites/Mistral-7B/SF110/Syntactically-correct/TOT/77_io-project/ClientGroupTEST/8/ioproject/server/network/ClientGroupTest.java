import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupImpl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClientGroupTest {

    private ClientGroup clientGroup;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setup() {
        client = Mockito.mock(Client.class);
        handler = Mockito.mock(ClientHandler.class);
        clientGroup = new ClientGroupImpl(null);
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);
    }

    @Test
    public void testNotifyMessageSent() {
        Object message = "test message";
        Mockito.doAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.sendMessage(message);
            return true;
        }).when(client).messageReceived(Mockito.anyObject(), Mockito.anyObject());

        Mockito.doAnswer(invocation -> {
            ClientGroup subGroup = invocation.getArgument(0);
            subGroup.notifyMessageSent(client, message);
            return true;
        }).when(clientGroup).notifyMessageSent(client, message);

        boolean result = clientGroup.notifyMessageSent(client, message);
        Mockito.verify(client).messageReceived(clientGroup, message);
        Mockito.verify(handler).messageSent(client, message);
        Mockito.verify(clientGroup, times(clientGroup.subGroups.size())).notifyMessageSent(client, message);
    }
}
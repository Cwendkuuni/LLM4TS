import org.junit.Test;
import static org.mockito.Mockito.*;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;

public class ClientGroupTest {
    // 
    @Test
    public void testNotifyMessageSent() {
        Client client = mock(Client.class);
        Object message = "test message";
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);

        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        when(clientGroup.notifyMessageSent(client, message)).thenReturn(true);

        assertTrue(clientGroup.notifyMessageSent(client, message));
        verify(handler).messageSent(client, message);
    }
    // 

    // ... other test cases for other methods can be added here
}
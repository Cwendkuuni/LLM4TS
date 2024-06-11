import org.junit.Test;
import static org.mockito.Mockito.*;
import ioproject.server.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;

public class ClientGroupTest {
    // 

    @Test
    public void testNotifyMessageSent() {
        Client client = mock(Client.class);
        Object message = "Test message";
        ClientGroup parentGroup = mock(ClientGroup.class);
        ClientGroup clientGroup = new ClientGroup(parentGroup);
        ClientGroup subGroup = mock(ClientGroup.class);
        ClientHandler handler = mock(ClientHandler.class);

        when(parentGroup.contains(client)).thenReturn(true);
        when(subGroup.notifyMessageSent(client, message)).thenReturn(false);
        when(clientGroup.parentGroup()).thenReturn(parentGroup);
        when(clientGroup.subGroups().iterator().next()).thenReturn(new WeakReference<>(subGroup));
        when(clientGroup.handler()).thenReturn(handler);

        assertTrue(clientGroup.notifyMessageSent(client, message));
        verify(handler, times(1)).messageSent(client, message);
    }

    // 
}
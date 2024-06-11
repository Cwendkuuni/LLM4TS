import ioproject.common.network.Client;
import ioproject.server.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ClientGroupTest {
    // ...
    
    private ClientGroup parentGroup;
    private ClientGroup childGroup;
    private Client client1;
    private Client client2;
    private ClientHandler handler;

    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        client1 = Mockito.spy(new Client("client1"));
        client2 = Mockito.spy(new Client("client2"));
        handler = Mockito.mock(ClientHandler.class);
        childGroup = new ClientGroup(parentGroup);
        parentGroup.add(client1);
        parentGroup.add(client2);
        parentGroup.addClientHandler(handler);
    }

    @Test
    public void testNotifyMessageSent_ClientIsMember_MessageIsPropagatedToAllSubGroupsAndHandler() {
        childGroup.add(client1);
        Mockito.when(client1.getName()).thenReturn("client1");
        Mockito.when(client2.getName()).thenReturn("client2");

        Object message = "test message";
        boolean result = parentGroup.notifyMessageSent(client1, message);

        assertTrue(result);
        Mockito.verify(childGroup, times(1)).notifyMessageSent(client1, message);
        Mockito.verify(handler, times(1)).messageSent(client1, message);

        for (WeakReference<ClientGroup> groupReference : childGroup.subGroups) {
            ClientGroup subGroup = groupReference.get();
            Mockito.verify(subGroup, times(1)).notifyMessageSent(client1, message);
        }
    }

    // Add more tests for other methods here
}
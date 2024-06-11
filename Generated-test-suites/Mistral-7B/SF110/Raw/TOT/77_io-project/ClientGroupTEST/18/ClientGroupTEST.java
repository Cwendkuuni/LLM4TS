import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ExceptionEvent;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import static org.mockito.Mockito.*;

public class ClientGroupTest {
    private ClientGroup clientGroup;
    private Client client;
    private ClientHandler clientHandler;
    private Client client2;
    private ClientHandler clientHandler2;
    private ClientGroup clientGroup2;

    @Before
    public void setUp() {
        client = mock(Client.class);
        clientHandler = mock(ClientHandler.class);
        clientGroup = new ClientGroup(null);
        clientGroup.addClientHandler(clientHandler);
        clientGroup.add(client);
        client2 = mock(Client.class);
        clientHandler2 = mock(ClientHandler.class);
        clientGroup2 = mock(ClientGroup.class);
        clientGroup.addClientHandler(clientHandler2);
        clientGroup2.addClientHandler(clientHandler);
        clientGroup.createSubGroup();
        clientGroup.subGroups.add(new WeakReference<>(clientGroup2));
        clientGroup2.add(client2);
    }

    @Test
    public void testNotifyMessageSent() {
        Object message = new Object();
        when(client.getId()).thenReturn(1);
        clientGroup.notifyMessageSent(client, message);
        verify(clientHandler).messageSent(client, message);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyMessageSentToNonExistentClient() {
        Object message = new Object();
        when(client.getId()).thenReturn(999);
        clientGroup.notifyMessageSent(client, message);
        verifyNoInteractions(clientHandler);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyMessageSentToSubGroup() {
        Object message = new Object();
        when(client.getId()).thenReturn(2);
        clientGroup2.notifyMessageSent(client, message);
        verify(clientHandler).messageSent(client, message);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyMessageReceived() {
        Object message = new Object();
        when(client.getId()).thenReturn(1);
        clientGroup.notifyMessageReceived(client, message);
        verify(clientHandler).messageReceived(client, message);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyMessageReceivedToNonExistentClient() {
        Object message = new Object();
        when(client.getId()).thenReturn(999);
        clientGroup.notifyMessageReceived(client, message);
        verifyNoInteractions(clientHandler);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyMessageReceivedToSubGroup() {
        Object message = new Object();
        when(client.getId()).thenReturn(2);
        clientGroup2.notifyMessageReceived(client, message);
        verify(clientHandler).messageReceived(client, message);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyExceptionCaught() {
        Throwable cause = new RuntimeException();
        when(client.getId()).thenReturn(1);
        clientGroup.notifyExceptionCaught(client, cause);
        verify(clientHandler).exceptionCaught(client, cause);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyExceptionCaughtToNonExistentClient() {
        Throwable cause = new RuntimeException();
        when(client.getId()).thenReturn(999);
        clientGroup.notifyExceptionCaught(client, cause);
        verifyNoInteractions(clientHandler);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyExceptionCaughtToSubGroup() {
        Throwable cause = new RuntimeException();
        when(client.getId()).thenReturn(2);
        clientGroup2.notifyExceptionCaught(client, cause);
        verify(clientHandler).exceptionCaught(client, cause);
        verifyNoMoreInteractions(clientGroup);
    }

    @Test
    public void testNotifyExceptionCaughtWithExceptionEvent() {
        Client client2 = mock(Client.class);
        ClientGroup subGroup = clientGroup.createSubGroup();
        WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
        clientGroup.subGroups.add(subGroupReference);
        when(client.getId()).thenReturn(1);
        when(client2.getId()).thenReturn(2);
        ClientHandler clientHandler2 = mock(ClientHandler.class);
        ClientGroup clientGroup2 = mock(ClientGroup.class);
        subGroup.addClientHandler(clientHandler2);
        subGroup.add(client2);
        ExceptionEvent exceptionEvent = new ExceptionEvent(client, new RuntimeException());
        clientGroup.notifyExceptionCaught(client, exceptionEvent);
        verify(clientHandler).exceptionCaught(client, causeCaptor.capture());
        assertEquals(exceptionEvent.getCause(), causeCaptor.getValue());
        verify(clientHandler2).exceptionCaught(client2, causeCaptor.getValue());
        verifyNoMoreInteractions(clientGroup);
    }
}
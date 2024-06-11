package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.IllegalClientException;
import java.util.Set;
import java.util.stream.Collectors;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ClientGroupTest {
    private ClientGroup group;
    private Client client;
    private ClientHandler handler;

    @Before
    public void setUp() {
        group = new ClientGroup(null);
        client = mock(Client.class);
        handler = mock(ClientHandler.class);
        group.addClientHandler(handler);
        when(client.getName()).thenReturn("Client1");
        group.add(client);
    }

    @Test
    public void testNotifyMessageSent() {
        Object message = new Object();
        group.notifyMessageSent(client, message);

        verify(handler).messageSent(client, message);
    }

    @Test
    public void testNotifyMessageReceived() {
        Object message = new Object();
        group.notifyMessageReceived(client, message);

        verify(handler).messageReceived(client, message);
    }

    @Test
    public void testNotifyExceptionCaught() {
        Throwable cause = new IllegalClientException("Test exception");
        group.notifyExceptionCaught(client, cause);

        verify(handler).exceptionCaught(client, cause);
    }

    @Test
    public void testSize() {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        group.add(client1);
        group.add(client2);

        assertEquals(2, group.size());
    }

    @Test
    public void testContains() {
        Client client = mock(Client.class);
        group.add(client);

        assertTrue(group.contains(client));
    }

    @Test
    public void testIterator() {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        group.add(client1);
        group.add(client2);

        Set<Client> clients = group.stream().collect(Collectors.toSet());

        Iterator<Client> iterator = clients.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAdd() {
        Client client = mock(Client.class);
        ClientGroup group = new ClientGroup(null);
        ClientGroup parentGroup = mock(ClientGroup.class);

        when(parentGroup.contains(client)).thenReturn(true);

        assertTrue(group.add(client));

        verify(parentGroup).clientAdded(client);
    }

    @Test
    public void testRemove() {
        Client client = mock(Client.class);
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = mock(ClientGroup.class);

        group.add(client);
        when(subGroup.contains(client)).thenReturn(true);

        assertTrue(group.remove(client));

        verify(group).clientRemoved(client);
        verify(subGroup).clientRemoved(client);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup group = new ClientGroup(null);
        ClientGroup subGroup = group.createSubGroup();

        assertNotNull(subGroup);
        assertEquals(group, subGroup.parentGroup());
    }

    @Test
    public void testAddNullClient() {
        ClientGroup group = new ClientGroup(null);

        assertFalse(group.add(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddClientAlreadyExists() {
        Client client = mock(Client.class);
        ClientGroup group = new ClientGroup(null);
        group.add(client);

        assertFalse(group.add(client));
    }

    @Test
    public void testRemoveNullClient() {
        ClientGroup group = new ClientGroup(null);
        Clientclient = mock(Client.class);

        assertFalse(group.remove(null));
    }

    @Test
    public void testRemoveNonExistentClient() {
        ClientGroup group = new ClientGroup(null);
        Client client = mock(Client.class);

        assertFalse(group.remove(client));
    }
}
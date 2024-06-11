package ioproject.server.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private Client client;
    private ClientHandler handler;
    private Set<Client> clients;
    private Set<WeakReference<ClientGroup>> subGroups;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        subGroup = new ClientGroup(parentGroup);
        client = mock(Client.class);
        handler = mock(ClientHandler.class);
        clients = new LinkedHashSet<>();
        subGroups = new LinkedHashSet<>();
        when(client.getName()).thenReturn("Client");
    }

    @After
    public void tearDown() {
        parentGroup = null;
        subGroup = null;
        client = null;
        handler = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(parentGroup);
        assertNull(parentGroup.parentGroup());
        assertNotNull(subGroups);
        assertNotNull(clients);
        assertNull(handler);
    }

    @Test
    public void testParentGroup() {
        assertNull(parentGroup.parentGroup());
        parentGroup = new ClientGroup(parentGroup);
        assertSame(parentGroup, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_NoClient() {
        assertFalse(parentGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageSent_ClientInGroup() {
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageSent_ClientInSubGroup() {
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageSent_ClientInHandler() {
        parentGroup.addClientHandler(handler);
        parentGroup.add(client);
        when(handler.messageSent(client, "message")).thenReturn(true);
        assertTrue(parentGroup.notifyMessageSent(client, "message"));
    }

    @Test
    public void testNotifyMessageSent_MultipleClients() {
        parentGroup.add(client1 = mock(Client.class));
        parentGroup.add(client2 = mock(Client.class));

        when(client1.getName()).thenReturn("Client1");
        when(client2.getName()).thenReturn("Client2");

        when(handler.messageSent(client1, "message1")).thenReturn(true);
        when(handler.messageSent(client2, "message2")).thenReturn(true);

        parentGroup.notifyMessageSent(client1, "message1");
        parentGroup.notifyMessageSent(client2, "message2");

        verify(handler).messageSent(client1, "message1");
        verify(handler).messageSent(client2, "message2");
    }

    @Test
    public void testNotifyMessageReceived_NoClient() {
        assertFalse(parentGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived_ClientInGroup() {
        parentGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived_ClientInSubGroup() {
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived_ClientInHandler() {
        parentGroup.addClientHandler(handler);
        parentGroup.add(client);
        when(handler.messageReceived(client, "message")).thenReturn(true);
        assertTrue(parentGroup.notifyMessageReceived(client, "message"));
    }

    @Test
    public void testNotifyMessageReceived_MultipleClients() {
        parentGroup.add(client1 = mock(Client.class));
        parentGroup.add(client2 = mock(Client.class));

        when(client1.getName()).thenReturn("Client1");
        when(client2.getName()).thenReturn("Client2");

        when(handler.messageReceived(client1, "message1")).thenReturn(true);
        when(handler.messageReceived(client2, "message2")).thenReturn(true);

        parentGroup.notifyMessageReceived(client1, "message1");
        parentGroup.notifyMessageReceived(client2, "message2");

        verify(handler).messageReceived(client1, "message1");
        verify(handler).messageReceived(client2, "message2");
    }

    @Test
    public void testNotifyExceptionCaught_NoClient() {
        assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInGroup() {
        parentGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInSubGroup() {
        parentGroup.add(subGroup);
        subGroup.add(client);
        assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInHandler() {
        parentGroup.addClientHandler(handler);
        parentGroup.add(client);
        when(handler.exceptionCaught(client, new RuntimeException())).thenReturn(true);
        assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
    }

    @Test
    public void testNotifyExceptionCaught_MultipleClients() {
        parentGroup.add(client1 = mock(Client.class));
        parentGroup.add(client2 = mock(Client.class));

        when(client1.getName()).thenReturn("Client1");
        when(client2.getName()).thenReturn("Client2");

        when(handler.exceptionCaught(client1, new RuntimeException())).thenReturn(true);
        when(handler.exceptionCaught(client2, new RuntimeException())).thenReturn(true);

        parentGroup.notifyExceptionCaught(client1, new RuntimeException());
        parentGroup.notifyExceptionCaught(client2, new RuntimeException());

        verify(handler).exceptionCaught(client1, new RuntimeException());
        verify(handler).exceptionCaught(client2, new RuntimeException());
    }

    @Test
    public void testIterator() {
        parentGroup.add(client1 = mock(Client.class));
        parentGroup.add(client2 = mock(Client.class));

        when(client1.getName()).thenReturn("Client1");
        when(client2.getName()).thenReturn("Client2");

        List<Client> expected = new ArrayList<>(Arrays.asList(client1, client2));
        List<Client> actual = new ArrayList<>();
        for (Client client : parentGroup) {
            actual.add(client);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testSize() {
        parentGroup.add(client1 = mock(Client.class));
        parentGroup.add(client2 = mock(Client.class));

        when(client1.getName()).thenReturn("Client1");
        when(client2.getName()).thenReturn("Client2");

        assertEquals(2, parentGroup.size());
    }

    @Test
    public void testAdd() {
        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testAddDuplicateClient() {
        parentGroup.add(client);
        assertFalse(parentGroup.add(client));
    }

    @Test
    public void testRemove() {
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
    }

    @Test
    public void testRemoveNonExistentClient() {
        parentGroup.add(client);
        assertFalse(parentGroup.remove(new Client()));
        assertTrue(parentGroup.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertSame(parentGroup, subGroup.parentGroup());
        assertTrue(parentGroup.subGroups.contains(new WeakReference<>(subGroup)));
    }

    @Test
    public void testAddClientHandler() {
        parentGroup.addClientHandler(handler);
        assertNotNull(parentGroup.handler);
    }
}
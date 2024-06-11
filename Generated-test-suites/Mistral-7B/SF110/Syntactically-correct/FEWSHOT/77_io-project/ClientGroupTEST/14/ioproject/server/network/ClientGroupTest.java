package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.common.network.ClientHandler.MessageType;

public class ClientGroupTest {

    private ClientGroup parentGroup;
    private ClientGroup subGroup;
    private ClientHandler handler;
    private Set<Client> clients;

    @Before
    public void setUp() {
        parentGroup = new ClientGroup(null);
        clients = new LinkedHashSet<>();
        handler = mock(ClientHandler.class);
        when(handler.messageSent(any(Client.class), any())).thenReturn(true);
        when(handler.messageReceived(any(Client.class), any())).thenReturn(true);
        when(handler.exceptionCaught(any(Client.class), any())).thenReturn(true);
        when(handler.clientAdded(any(Client.class))).thenReturn(true);
        when(handler.clientRemoved(any(Client.class))).thenReturn(true);

        parentGroup.addClientHandler(handler);
        Client client1 = new Client("client1");
        Client client2 = new Client("client2");
        Client client3 = new Client("client3");
        clients.add(client1);
        clients.add(client2);
        clients.add(client3);
        parentGroup.clients = clients;
    }

    @After
    public void tearDown() {
        parentGroup = null;
        subGroup = null;
        handler = null;
        clients = null;
    }

    @Test
    public void testContains() {
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
        assertTrue(parentGroup.contains(client3));
        assertFalse(parentGroup.contains(new Client("client4")));

        ClientGroup subGroup = parentGroup.createSubGroup();
        subGroup.add(client1);
        assertFalse(parentGroup.contains(client1));
        assertTrue(subGroup.contains(client1));
    }

    @Test
    public void testSize() {
        assertEquals(3, parentGroup.size());
    }

    @Test
    public void testAddClient() {
        Client newClient = new Client("newClient");
        assertTrue(parentGroup.add(newClient));
        assertTrue(parentGroup.contains(newClient));
        assertEquals(4, parentGroup.size());

        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(subGroup.add(newClient));
        assertFalse(parentGroup.contains(newClient));
        assertTrue(subGroup.contains(newClient));
    }

    @Test
    public void testRemoveClient() {
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.contains(client1));
        assertEquals(2, parentGroup.size());
        assertEquals(1, clients.size());

        ClientGroup subGroup = parentGroup.createSubGroup();
        assertTrue(subGroup.remove(client1));
        assertFalse(subGroup.contains(client1));
        assertEquals(0, subGroup.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedRemoveFromEmpty() {
        ClientGroup emptyGroup = new ClientGroup(null);
        emptyGroup.remove(new Client("client1"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedAddToEmpty() {
        ClientGroup emptyGroup = new ClientGroup(null);
        emptyGroup.add(new Client("client1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullClientHandler() {
        ClientGroup group = new ClientGroup(null);
        try {
            group.addClientHandler(null);
        } catch (NullPointerException e) {
            assertEquals("handler", e.getMessage());
        }
    }

    @Test
    public void testNotifyMessageSent() {
        Client sender = client1;
        Object message = "test message";

        // Test notification to parent group
        assertTrue(parentGroup.notifyMessageSent(sender, message));
        verify(handler).messageSent(sender, message);

        // Test notification to sub group
        client2.setSender(sender);
        subGroup.add(client2);
        assertTrue(subGroup.notifyMessageSent(sender, message));
        verify(handler).messageSent(client2, message);

        // Test notification to multiple sub groups
        ClientGroup subGroup2 = parentGroup.createSubGroup();
        subGroup2.add(client3);
        assertTrue(subGroup2.notifyMessageSent(sender, message));
        verify(handler).messageSent(client3, message);
    }

    @Test
    public void testNotifyMessageReceived() {
        Client receiver = client1;
        Object message = "test message";

        // Test notification to parent group
        assertTrue(parentGroup.notifyMessageReceived(receiver, message));
        verify(handler).messageReceived(receiver, message);

        // Test notification to sub group
        client2.setReceiver(receiver);
        subGroup.add(client2);
        assertTrue(subGroup.notifyMessageReceived(receiver, message));
        verify(handler).messageReceived(client2, message);

        // Test notification to multiple sub groups
        ClientGroup subGroup2 = parentGroup.createSubGroup();
        subGroup2.add(client3);
        assertTrue(subGroup2.notifyMessageReceived(receiver, message));
        verify(handler).messageReceived(client3, message);
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = client1;
        Throwable cause = new RuntimeException();

        // Test notification to parent group
        assertTrue(parentGroup.notifyExceptionCaught(client, cause));
        verify(handler).exceptionCaught(client, cause);

        // Test notification to sub group
        Client subGroupClient = new Client("subGroupClient");
        subGroup.add(subGroupClient);
        assertTrue(subGroup.notifyExceptionCaught(client, cause));
        verify(handler).exceptionCaught(subGroupClient, cause);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertEquals(0, subGroup.size());

        // Test adding clients to sub group
        Client subGroupClient1 = new Client("subGroupClient1");
        Client subGroupClient2 = new Client("subGroupClient2");
        subGroup.add(subGroupClient1);
        subGroup.add(subGroupClient2);
        assertEquals(2, subGroup.size());

        // Test removing clients from sub group
        subGroup.remove(subGroupClient1);
        assertEquals(1, subGroup.size());
        assertFalse(subGroup.contains(subGroupClient1));
    }
}
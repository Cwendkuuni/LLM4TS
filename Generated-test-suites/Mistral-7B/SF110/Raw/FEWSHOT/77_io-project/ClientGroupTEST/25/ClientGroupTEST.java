package ioproject.server.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

    private ClientGroup rootGroup;
    private ClientGroup subGroup1;
    private ClientGroup subGroup2;
    private Client client1;
    private Client client2;
    private Client client3;

    @Before
    public void setUp() {
        rootGroup = new ClientGroup(null);
        subGroup1 = rootGroup.createSubGroup();
        subGroup2 = rootGroup.createSubGroup();
        client1 = new Client();
        client2 = new Client();
        client3 = new Client();
    }

    @After
    public void tearDown() {
        if (rootGroup != null) {
            rootGroup = null;
        }
    }

    @Test
    public void testAddRemoveClients() {
        rootGroup.add(client1);
        rootGroup.add(client2);
        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.contains(client2));
        assertFalse(rootGroup.contains(client3));
        rootGroup.remove(client1);
        assertFalse(rootGroup.contains(client1));
        assertTrue(rootGroup.contains(client2));
    }

    @Test
    public void testAddRemoveClientsInSubGroups() {
        rootGroup.add(client1);
        subGroup1.add(client2);
        subGroup2.add(client3);
        assertTrue(rootGroup.contains(client1));
        assertTrue(subGroup1.contains(client2));
        assertTrue(subGroup2.contains(client3));
        assertFalse(rootGroup.contains(client2));
        assertFalse(rootGroup.contains(client3));
        rootGroup.remove(client1);
        assertFalse(rootGroup.contains(client1));
        assertFalse(subGroup1.contains(client2));
        assertFalse(subGroup2.contains(client3));
    }

    @Test
    public void testNotifyMessageSent() {
        final Object message = new Object();
        rootGroup.add(client1);
        rootGroup.add(client2);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertEquals(client, client1);
                assertEquals(message, this.message);
            }
        };
        rootGroup.addClientHandler(handler);
        rootGroup.notifyMessageSent(client1, message);
        assertNull(handler.message);
        rootGroup.notifyMessageSent(client1, message);
        assertEquals(message, handler.message);
        assertNull(handler.message);
        rootGroup.notifyMessageSent(client2, message);
        assertNull(handler.message);
    }

    @Test
    public void testNotifyMessageReceived() {
        final Object message = new Object();
        rootGroup.add(client1);
        rootGroup.add(client2);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                assertEquals(client, client1);
                assertEquals(message, this.message);
            }
        };
        rootGroup.addClientHandler(handler);
        rootGroup.notifyMessageReceived(client1, message);
        assertNull(handler.message);
        rootGroup.notifyMessageReceived(client1, message);
        assertEquals(message, handler.message);
        assertNull(handler.message);
        rootGroup.notifyMessageReceived(client2, message);
        assertNull(handler.message);
    }

    @Test
    public void testNotifyExceptionCaught() {
        final Throwable cause = new Throwable();
        rootGroup.add(client1);
        rootGroup.add(client2);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                assertEquals(client, client1);
                assertSame(cause, this.cause);
            }
        };
        rootGroup.addClientHandler(handler);
        rootGroup.notifyExceptionCaught(client1, cause);
        assertNull(handler.cause);
        rootGroup.notifyExceptionCaught(client1, cause);
        assertSame(cause, handler.cause);
        assertNull(handler.cause);
        rootGroup.notifyExceptionCaught(client2, cause);
        assertNull(handler.cause);
    }

    @Test
    public void testSizeAndIterator() {
        rootGroup.addAll(Arrays.asList(client1, client2, client3));
        List<Client> clients = new ArrayList<>(rootGroup.size());
        for (Client client : rootGroup) {
            clients.add(client);
        }
        assertEquals(3, rootGroup.size());
        assertEquals(clients, rootGroup);
    }

    @Test
    public void testcontains() {
        rootGroup.addAll(Arrays.asList(client1, client2, client3));
        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.contains(client2));
        assertTrue(rootGroup.contains(client3));
        assertFalse(rootGroup.contains(new Client()));
    }

    @Test
    public void testCreateSubGroup() {
        rootGroup.add(client1);
        ClientGroup subGroup = rootGroup.createSubGroup();
        assertTrue(subGroup.parentGroup() == rootGroup);
        Assert.assertTrue(rootGroup.subGroups.contains(new WeakReference<>(subGroup)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableSetIterator() {
        rootGroup.addAll(Arrays.asList(client1, client2, client3));
        Iterator<Client> iterator = rootGroup.iterator();
        iterator.remove();
    }
}
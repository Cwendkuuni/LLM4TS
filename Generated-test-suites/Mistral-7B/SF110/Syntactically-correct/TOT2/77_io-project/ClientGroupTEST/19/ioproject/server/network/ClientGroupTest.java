package ioproject.server.network.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientGroupTest {

    @Test
    public void testEmptyConstructor() {
        ClientGroup group = new ClientGroup();
        assertNull(group.parentGroup());
        assertEquals(0, group.size());
        assertFalse(group.contains(new Client()));
        assertTrue(group.iterator().hasNext());
    }

    @Test
    public void testConstructorWithParentGroup() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup group = new ClientGroup(parentGroup);
        assertEquals(parentGroup, group.parentGroup());
        assertEquals(0, group.size());
        assertFalse(group.contains(new Client()));
        assertFalse(group.iterator().hasNext());
    }

    @Test
    public void testParentGroupNotNull() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();
        assertTrue(group.notifyMessageSent(client, message));
        assertTrue(parentGroup.notifyMessageSent(client, message));
    }

    @Test(expected = NullPointerException.class)
    public void testParentGroupNull() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();
        assertFalse(group.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSentWithEmptyGroup() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        Object message = new Object();
        assertFalse(group.notifyMessageSent(client, message));
    }

    @Test
    public void testNotifyMessageSentWithSubGroups() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();
        ClientGroup subGroup = new ClientGroup(group);
        subGroup.add(client);
        assertTrue(group.notifyMessageSent(client, message) || group.subGroups.stream().anyMatch(wRef -> wRef.get().notifyMessageSent(client, message)));
    }

    @Test
    public void testNotifyMessageSentWithHandler() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        Object message = new Object();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                assertEquals(client, sender);
                assertEquals(message, "test message");
            }
        };
        group.addClientHandler(handler);
        group.add(client);
        group.notifyMessageSent(client, "test message");
        assertTrue(handler.isMessageSent);
        handler.isMessageSent = false;
    }

    @Test
    public void testNotifyMessageReceivedWithEmptyGroup() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        Object message = new Object();
        assertFalse(group.notifyMessageReceived(client, message));
    }

    @Test
    public void testNotifyMessageReceivedWithSubGroups() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        Object message = new Object();
        ClientGroup subGroup = new ClientGroup(group);
        subGroup.add(client);
        assertTrue(group.notifyMessageReceived(client, message) || group.subGroups.stream().anyMatch(wRef -> wRef.get().notifyMessageReceived(client, message)));
    }

    @Test
    public void testNotifyMessageReceivedWithHandler() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        Object message = new Object();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client sender, Object message) {
                assertEquals(client, sender);
                assertEquals(message, "test message");
            }
        };
        group.addClientHandler(handler);
        group.add(client);
        group.notifyMessageReceived(client, "test message");
        assertTrue(handler.isMessageReceived);
        handler.isMessageReceived = false;
    }

    @Test
    public void testNotifyExceptionCaughtWithEmptyGroup() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        Throwable cause = new Throwable();
        assertFalse(group.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaughtWithSubGroups() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        Throwable cause = new Throwable();
        ClientGroup subGroup = new ClientGroup(group);
        subGroup.add(client);
        assertTrue(group.notifyExceptionCaught(client, cause) || group.subGroups.stream().anyMatch(wRef -> wRef.get().notifyExceptionCaught(client, cause)));
    }

    @Test
    public void testNotifyExceptionCaughtWithHandler() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        Throwable cause = new Throwable();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client sender, Throwable cause) {
                assertEquals(client, sender);
                assertTrue(cause instanceof Throwable);
            }
        };
        group.addClientHandler(handler);
        group.add(client);
        group.notifyExceptionCaught(client, cause);
        assertTrue(handler.isExceptionCaught);
        handler.isExceptionCaught = false;
    }

    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        List<Client> clients = new LinkedList<>();
        clients.add(client1);
        clients.add(client2);
        Iterator<Client> it = group.iterator();
        while (it.hasNext()) {
            Client client = it.next();
            assertTrue(clients.contains(client));
        }
    }

    @Test
    public void testContainsClient() {
        ClientGroup group = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        assertTrue(group.contains(client1));
        assertTrue(group.contains(client2));
    }

    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        assertEquals(2, group.size());
    }

    @Test
    public void testAddClient() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        assertTrue(group.add(client));
        assertEquals(1, group.size());
        assertTrue(group.contains(client));
    }

    @Test
    public void testAddExistingClient() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        group.add(client);
        assertFalse(group.add(client));
        assertEquals(1, group.size());
        assertTrue(group.contains(client));
    }

    @Test
    public void testRemoveClient() {
        ClientGroup group = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        group.add(client1);
        group.add(client2);
        assertTrue(group.remove(client1));
        assertEquals(1, group.size());
        assertFalse(group.contains(client1));
        assertTrue(group.contains(client2));
    }

    @Test
    public void testRemoveNonExistingClient() {
        ClientGroup group = new ClientGroup();
        Client client = new Client();
        assertFalse(group.remove(client));
        assertEquals(0, group.size());
        assertFalse(group.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup group = new ClientGroup(parentGroup);
        ClientGroup subGroup = group.createSubGroup();
        assertTrue(subGroup instanceof ClientGroup);
        assertTrue(group.subGroups.contains(new WeakReference<>(subGroup)));
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup();
        ClientHandler handler = new ClientHandler();
        group.addClientHandler(handler);
        assertEquals(handler, group.handler);
    }

    @Test
    public void testAddMultipleClientHandlers() {
        ClientGroup group = new ClientGroup();
        ClientHandler handler1 = new ClientHandler();
        ClientHandler handler2 = new ClientHandler();
        group.addClientHandler(handler1);
        group.addClientHandler(handler2);
        assertEquals(2, group.handlers.size());
    }

    // Add edge cases and exceptions testing here

    static class ClientHandler {
        boolean isMessageSent = false;
        boolean isMessageReceived = false;
        boolean isExceptionCaught = false;

        @Override
        public void messageSent(Client sender, Object message) {
            isMessageSent = true;
        }

        @Override
        public void messageReceived(Client sender, Object message) {
            isMessageReceived = true;
        }

        @Override
        public void exceptionCaught(Client sender, Throwable cause) {
            isExceptionCaught = true;
        }
    }
}

//
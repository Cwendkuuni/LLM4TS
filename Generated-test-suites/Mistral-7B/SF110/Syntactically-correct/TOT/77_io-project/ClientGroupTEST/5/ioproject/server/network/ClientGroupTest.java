package ioproject.server.network.test;

import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {
    private ClientGroup rootGroup;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler handler;

    @Before
    public void setUp() {
        rootGroup = new ClientGroup(null);
        client1 = new Client("client1");
        client2 = new Client("client2");
        client3 = new Client("client3");
        handler = new DummyClientHandler();
        rootGroup.addClientHandler(handler);
    }

    @Test
    public void testAddClientToRootGroup() {
        assertTrue(rootGroup.add(client1));
        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.size() == 1);

        assertTrue(rootGroup.add(client2));
        assertTrue(rootGroup.contains(client2));
        assertTrue(rootGroup.size() == 2);

        assertTrue(rootGroup.add(client3));
        assertTrue(rootGroup.contains(client3));
        assertTrue(rootGroup.size() == 3);
    }

    @Test
    public void testAddClientAlreadyInGroup() {
        assertTrue(rootGroup.add(client1));
        assertTrue(rootGroup.contains(client1));

        assertFalse(rootGroup.add(client1));
        assertFalse(rootGroup.contains(client1));
        assertTrue(rootGroup.size() == 1);
    }

    @Test
    public void testAddClientToSubGroup() {
        ClientGroup subGroup = rootGroup.createSubGroup();

        assertTrue(subGroup.add(client1));
        assertTrue(subGroup.contains(client1));
        assertTrue(subGroup.size() == 1);

        rootGroup.add(subGroup);
        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.size() == 4);
    }

    @Test
    public void testRemoveClientFromRootGroup() {
        rootGroup.add(client1);
        rootGroup.add(client2);
        rootGroup.add(client3);

        assertTrue(rootGroup.remove(client1));
        assertFalse(rootGroup.contains(client1));
        assertTrue(rootGroup.size() == 2);

        assertTrue(rootGroup.remove(client2));
        assertFalse(rootGroup.contains(client2));
        assertTrue(rootGroup.size() == 1);

        assertFalse(rootGroup.remove(client3));
        assertTrue(rootGroup.contains(client3));
        assertTrue(rootGroup.size() == 1);
    }

    @Test
    public void testRemoveClientFromSubGroup() {
        ClientGroup subGroup = rootGroup.createSubGroup();
        subGroup.add(client1);

        rootGroup.add(subGroup);
        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.size() == 4);

        assertTrue(subGroup.remove(client1));
        assertFalse(subGroup.contains(client1));
        assertTrue(subGroup.size() == 0);

        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.size() == 3);
    }

    @Test
    public void testNotifyMessageSent() {
        rootGroup.add(client1);
        rootGroup.add(client2);

        assertTrue(rootGroup.notifyMessageSent(client1, "hello"));
        assertTrue(handler.messageSentCallCount == 1);
        handler.messageSentCallCount = 0;

        assertTrue(rootGroup.notifyMessageSent(client2, "world"));
        assertTrue(handler.messageSentCallCount == 1);
        handler.messageSentCallCount = 0;

        assertTrue(rootGroup.notifyMessageSent(client1, "!"));
        assertFalse(handler.messageSentCallCount > 0);
    }

    @Test
    public void testNotifyMessageReceived() {
        rootGroup.add(client1);
        rootGroup.add(client2);

        assertTrue(rootGroup.notifyMessageReceived(client1, "hello"));
        assertTrue(handler.messageReceivedCallCount == 1);
        handler.messageReceivedCallCount = 0;

        assertTrue(rootGroup.notifyMessageReceived(client2, "world"));
        assertTrue(handler.messageReceivedCallCount == 1);
        handler.messageReceivedCallCount = 0;

        assertTrue(rootGroup.notifyMessageReceived(client1, "!"));
        assertFalse(handler.messageReceivedCallCount > 0);
    }

    @Test
    public void testNotifyExceptionCaught() {
        rootGroup.add(client1);
        rootGroup.add(client2);

        Throwable exception = new RuntimeException();
        assertTrue(rootGroup.notifyExceptionCaught(client1, exception));
        assertTrue(handler.exceptionCaughtCallCount == 1);
        handler.exceptionCaughtCallCount = 0;

        assertTrue(rootGroup.notifyExceptionCaught(client2, exception));
        assertTrue(handler.exceptionCaughtCallCount == 1);
        handler.exceptionCaughtCallCount = 0;

        assertTrue(rootGroup.notifyExceptionCaught(client1, new NullPointerException()));
        assertFalse(handler.exceptionCaughtCallCount > 0);
    }

    @Test
    public void testContainNullClient() {
        assertFalse(rootGroup.contains(null));
    }

    @Test
    public void testSizeZeroGroup() {
        assertTrue(rootGroup.size() == 0);
    }

    @Test
    public void testEmptyIterator() {
        Iterator<Client> iterator = rootGroup.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorsAreEqual() {
        rootGroup.add(client1);
        rootGroup.add(client2);

        Iterator<Client> it1 = rootGroup.iterator();
        Iterator<Client> it2 = rootGroup.iterator();

        assertTrue(it1.equals(it2));
    }

    static class DummyClientHandler implements ClientHandler {
        int messageSentCallCount = 0;
        int messageReceivedCallCount = 0;
        int exceptionCaughtCallCount = 0;

        @Override
        public void messageSent(Client client, Object message) {
            messageSentCallCount++;
        }

        @Override
        public void messageReceived(Client client, Object message) {
            messageReceivedCallCount++;
        }

        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtCallCount++;
        }
    }
}
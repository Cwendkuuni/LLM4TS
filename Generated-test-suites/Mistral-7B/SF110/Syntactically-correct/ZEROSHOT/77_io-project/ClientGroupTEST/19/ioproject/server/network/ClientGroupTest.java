package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerImpl;

public class ClientGroupTest {

    private ClientGroup root;
    private ClientGroup subGroup1;
    private ClientGroup subGroup2;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler handler;

    @Before
    public void setUp() {
        root = new ClientGroup(null);
        subGroup1 = root.createSubGroup();
        subGroup2 = root.createSubGroup();
        client1 = new Client("client1");
        client2 = new Client("client2");
        client3 = new Client("client3");
        handler = new ClientHandlerImpl();
        root.addClientHandler(handler);
        root.add(client1);
        subGroup1.add(client2);
        subGroup2.add(client3);
    }

    @After
    public void tearDown() {
        root = null;
        subGroup1 = null;
        subGroup2 = null;
        client1 = null;
        client2 = null;
        client3 = null;
        handler = null;
        System.gc();
    }

    @Test
    public void testContains() {
        assertTrue(root.contains(client1));
        assertTrue(subGroup1.contains(client2));
        assertFalse(root.contains(client2));
        assertFalse(subGroup1.contains(client3));
    }

    @Test
    public void testSize() {
        assertEquals(3, root.size());
        assertEquals(2, subGroup1.size());
        assertEquals(1, subGroup2.size());
    }

    @Test
    public void testAddAndRemove() {
        assertFalse(root.contains(client2));
        assertTrue(root.add(client2));
        assertEquals(4, root.size());
        assertFalse(root.remove(client3));
        assertFalse(root.contains(client3));
        assertTrue(root.remove(client2));
        assertEquals(3, root.size());
    }

    @Test
    public void testNotifyMessageSent() {
        Object message = new Object();
        assertTrue(root.notifyMessageSent(client1, message));
        assertTrue(subGroup1.notifyMessageSent(client2, message));
        assertTrue(subGroup2.notifyMessageSent(client3, message));
        assertFalse(handler.messageSentCalled);
        handler.messageSentCalled = false;
        root.handler.messageSent(client1, message);
        assertTrue(handler.messageSentCalled);
    }

    @Test
    public void testNotifyMessageReceived() {
        Object message = new Object();
        assertTrue(root.notifyMessageReceived(client1, message));
        assertTrue(subGroup1.notifyMessageReceived(client2, message));
        assertTrue(subGroup2.notifyMessageReceived(client3, message));
        assertFalse(handler.messageReceivedCalled);
        handler.messageReceivedCalled = false;
        root.handler.messageReceived(client1, message);
        assertTrue(handler.messageReceivedCalled);
    }

    @Test
    public void testNotifyExceptionCaught() {
        Throwable cause = new Exception();
        assertTrue(root.notifyExceptionCaught(client1, cause));
        assertTrue(subGroup1.notifyExceptionCaught(client2, cause));
        assertTrue(subGroup2.notifyExceptionCaught(client3, cause));
        assertFalse(handler.exceptionCaughtCalled);
        handler.exceptionCaughtCalled = false;
        root.handler.exceptionCaught(client1, cause);
        assertTrue(handler.exceptionCaughtCalled);
    }

    @Test
    public void testIterator() {
        Set<Client> expected = new HashSet<>(Arrays.asList(client1, client2, client3));
        assertEquals(expected, new HashSet<>(root));
        assertEquals(expected, new HashSet<>(subGroup1));
        assertEquals(expected, new HashSet<>(subGroup2));
    }
}

class ClientHandlerImpl implements ClientHandler {
    boolean messageSentCalled = false;
    boolean messageReceivedCalled = false;
    boolean exceptionCaughtCalled = false;

    @Override
    public void messageSent(Client sender, Object message) {
        messageSentCalled = true;
    }

    @Override
    public void messageReceived(Client sender, Object message) {
        messageReceivedCalled = true;
    }

    @Override
    public void exceptionCaught(Client sender, Throwable cause) {
        exceptionCaughtCalled = true;
    }
}
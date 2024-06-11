package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.ClientHandler;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupImpl;
import ioproject.server.network.ClientImpl;

public class ClientGroupTest {

    private ClientGroup rootGroup;
    private ClientGroup parentGroup;
    private ClientGroup subGroup1;
    private ClientGroup subGroup2;
    private Client client1;
    private Client client2;
    private Client client3;
    private ClientHandler handler;

    @Before
    public void setUp() {
        rootGroup = new ClientGroup(null);
        parentGroup = new ClientGroup(rootGroup);
        subGroup1 = parentGroup.createSubGroup();
        subGroup2 = parentGroup.createSubGroup();
        client1 = new ClientImpl();
        client2 = new ClientImpl();
        client3 = new ClientImpl();
        handler = new ClientHandlerImpl();
        rootGroup.addClientHandler(handler);
        parentGroup.addClient(client1);
        parentGroup.addClient(client2);
        subGroup1.add(client3);
    }

    @After
    public void tearDown() {
        // Clear all the references to avoid memory leaks
        rootGroup = null;
        parentGroup = null;
        subGroup1 = null;
        subGroup2 = null;
        client1 = null;
        client2 = null;
        client3 = null;
        handler = null;
    }

    @Test
    public void testIterator() {
        Set<Client> clients = rootGroup.clients;
        Iterator<Client> iterator = clients.iterator();
        List<Client> clientList = new ArrayList<>();
        while (iterator.hasNext()) {
            clientList.add(iterator.next());
        }
        List<Client> expectedClientList = Arrays.asList(client1, client2, client3);
        assertEquals(expectedClientList, clientList);
    }

    @Test
    public void testContains() {
        assertTrue(rootGroup.contains(client1));
        assertTrue(rootGroup.contains(client2));
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
        assertTrue(subGroup1.contains(client3));
        assertFalse(rootGroup.contains(client3));
    }

    @Test
    public void testSize() {
        assertEquals(3, rootGroup.size());
        assertEquals(2, parentGroup.size());
        assertEquals(1, subGroup1.size());
    }

    @Test
    public void testAdd() {
        Client newClient = new ClientImpl();
        assertTrue(rootGroup.add(client1));
        assertFalse(rootGroup.add(client1));
        assertTrue(parentGroup.add(client2));
        assertTrue(subGroup1.add(client3));
        assertFalse(subGroup1.add(client3));
        assertTrue(rootGroup.add(newClient));
        assertEquals(4, rootGroup.size());
    }

    @Test
    public void testRemove() {
        assertTrue(parentGroup.remove(client1));
        assertFalse(parentGroup.remove(client1));
        assertTrue(subGroup1.remove(client3));
        assertFalse(subGroup1.remove(client3));
        assertTrue(rootGroup.remove(client3));
        assertFalse(rootGroup.remove(client3));
    }

    @Test
    public void testNotifyMessageSent() {
        String message = "test message";
        assertTrue(rootGroup.notifyMessageSent(client1, message));
        assertTrue(parentGroup.notifyMessageSent(client1, message));
        assertTrue(subGroup1.notifyMessageSent(client3, message));
        assertFalse(subGroup2.notifyMessageSent(client3, message));
        assertFalse(handler.messageSentCallCount);
        client1.sendMessage(message);
        assertEquals(3, rootGroup.notifyMessageSentCallCount);
        assertEquals(2, parentGroup.notifyMessageSentCallCount);
        assertEquals(1, subGroup1.notifyMessageSentCallCount);
        handler.messageSent.value.clear();
        rootGroup.notifyMessageSent(client1, message);
        assertEquals(4, rootGroup.notifyMessageSentCallCount);
        assertEquals(message, handler.messageSent.value.get(0));
    }

    @Test
    public void testNotifyMessageReceived() {
        String message = "test message";
        assertTrue(rootGroup.notifyMessageReceived(client1, message));
        assertTrue(parentGroup.notifyMessageReceived(client1, message));
        assertTrue(subGroup1.notifyMessageReceived(client3, message));
        assertFalse(subGroup2.notifyMessageReceived(client3, message));
        assertFalse(handler.messageReceivedCallCount);
        client1.receiveMessage(message);
        assertEquals(3, rootGroup.notifyMessageReceivedCallCount);
        assertEquals(2, parentGroup.notifyMessageReceivedCallCount);
        assertEquals(1, subGroup1.notifyMessageReceivedCallCount);
        handler.messageReceived.value.clear();
        rootGroup.notifyMessageReceived(client1, message);
        assertEquals(4, rootGroup.notifyMessageReceivedCallCount);
        assertEquals(message, handler.messageReceived.value.get(0));
    }

    @Test
    public void testNotifyExceptionCaught() {
        Throwable exception = new RuntimeException("test exception");
        assertTrue(rootGroup.notifyExceptionCaught(client1, exception));
        assertTrue(parentGroup.notifyExceptionCaught(client1, exception));
        assertTrue(subGroup1.notifyExceptionCaught(client3, exception));
        assertFalse(subGroup2.notifyExceptionCaught(client3, exception));
        assertFalse(handler.exceptionCaughtCallCount);
        client1.sendException(exception);
        assertEquals(3, rootGroup.notifyExceptionCaughtCallCount);
        assertEquals(2, parentGroup.notifyExceptionCaughtCallCount);
        assertEquals(1, subGroup1.notifyExceptionCaughtCallCount);
        handler.exceptionCaught.value.clear();
        rootGroup.notifyExceptionCaught(client1, exception);
        assertEquals(4, rootGroup.notifyExceptionCaughtCallCount);
        assertEquals(exception, handler.exceptionCaught.value.get(0));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        assertNotNull(subGroup);
        assertEquals(parentGroup, subGroup.parentGroup());
        assertTrue(parentGroup.subGroups.contains(new WeakReference<>(subGroup)));
    }

    static class ClientHandlerImpl implements ClientHandler {
        int messageSentCallCount = 0;
        int messageReceivedCallCount = 0;
        int exceptionCaughtCallCount = 0;

        volatile List<Object> messageSent = new LinkedHashSet<>();
        volatile List<Object> messageReceived = new LinkedHashSet<>();
        volatile List<Throwable> exceptionCaught = new LinkedHashSet<>();

        @Override
        public void messageSent(Client client, Object message) {
            messageSentCallCount++;
            messageSent.add(message);
        }

        @Override
        public void messageReceived(Client client, Object message) {
            messageReceivedCallCount++;
            messageReceived.add(message);
        }

        @Override
        public void exceptionCaught(Client client, Throwable cause) {
            exceptionCaughtCallCount++;
            exceptionCaught.add(cause);
        }
    }
}
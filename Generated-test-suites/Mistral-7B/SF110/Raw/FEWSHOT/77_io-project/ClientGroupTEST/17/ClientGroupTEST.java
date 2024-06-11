package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import org.junit.Test;

import java.util.*;

public class ClientGroupTest {

    @Test
    public void testContainsAndSizeWhenEmpty() {
        ClientGroup group = new ClientGroup(null);
        assertEquals(0, group.size());
        assertFalse(group.contains(new Client("testClient")));
    }

    @Test
    public void testAddAndRemoveClient() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client("testClient");

        assertFalse(group.contains(client));
        assertTrue(group.add(client));
        assertEquals(1, group.size());
        assertTrue(group.contains(client));

        assertTrue(group.remove(client));
        assertEquals(0, group.size());
        assertFalse(group.contains(client));
    }

    @Test
    public void testNotifiesMessageSentToParentAndSubGroups() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = new ClientGroup(parentGroup);
        Client client = new Client("testClient");

        ParentMessageHandler parentHandler = mock(ParentMessageHandler.class);
        SubGroupMessageHandler subGroupHandler = mock(SubGroupMessageHandler.class);
        ParentMessageHandler subGroupParentHandler = mock(ParentMessageHandler.class);

        when(parentHandler.messageSent(client, anyObject())).thenReturn(true);
        when(subGroupHandler.messageSent(client, anyObject())).thenReturn(true);
        when(subGroupParentHandler.messageSent(client, anyObject())).thenReturn(true);

        parentGroup.addClientHandler(parentHandler);
        subGroup.addClientHandler(subGroupHandler);
        parentGroup.add(client);
        subGroup.add(client);

        assertTrue(parentGroup.notifyMessageSent(client, "testMessage"));
        assertTrue(subGroup.notifyMessageSent(client, "testMessage"));
        assertTrue(parentGroup.notifyMessageSent(client, "testMessage")); // Check parent handling again
    }

    @Test
    public void testNotifiesMessageReceivedToParentAndSubGroups() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = new ClientGroup(parentGroup);
        Client client = new Client("testClient");

        ParentMessageHandler parentHandler = mock(ParentMessageHandler.class);
        SubGroupMessageHandler subGroupHandler = mock(SubGroupMessageHandler.class);
        ParentMessageHandler subGroupParentHandler = mock(ParentMessageHandler.class);

        when(parentHandler.messageReceived(client, anyObject())).thenReturn(true);
        when(subGroupHandler.messageReceived(client, anyObject())).thenReturn(true);
        when(subGroupParentHandler.messageReceived(client, anyObject())).thenReturn(true);

        parentGroup.addClientHandler(parentHandler);
        subGroup.addClientHandler(subGroupHandler);
        parentGroup.add(client);
        subGroup.add(client);

        assertTrue(parentGroup.notifyMessageReceived(client, "testMessage"));
        assertTrue(subGroup.notifyMessageReceived(client, "testMessage"));
        assertTrue(parentGroup.notifyMessageReceived(client, "testMessage")); // Check parent handling again
    }

    @Test
    public void testNotifiesExceptionCaughtToParentAndSubGroups() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = new ClientGroup(parentGroup);
        Client client = new Client("testClient");

        ParentExceptionHandler parentHandler = mock(ParentExceptionHandler.class);
        SubGroupExceptionHandler subGroupHandler = mock(SubGroupExceptionHandler.class);
        ParentExceptionHandler subGroupParentHandler = mock(ParentExceptionHandler.class);

        when(parentHandler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);
        when(subGroupHandler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);
        when(subGroupParentHandler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);

        parentGroup.addClientHandler(parentHandler);
        subGroup.addClientHandler(subGroupHandler);
        parentGroup.add(client);
        subGroup.add(client);

        Throwable testException = new TestException("testException");
        assertTrue(parentGroup.notifyExceptionCaught(client, testException));
        assertTrue(subGroup.notifyExceptionCaught(client, testException));
        assertTrue(parentGroup.notifyExceptionCaught(client, testException)); // Check parent handling again
    }

    @Test
    public void testIteratesCorrectly() {
        ClientGroup group = new ClientGroup(null);
        Client client1 = new Client("client1");
        Client client2 = new Client("client2");
        group.add(client1);
        group.add(client2);

        List<Client> clients = new ArrayList<>();
        for (Client client : group) {
            clients.add(client);
        }

        assertEquals(2, clients.size());
        assertEquals(client1, clients.get(0));
        assertEquals(client2, clients.get(1));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup subGroup = parentGroup.createSubGroup();

        assertEquals(parentGroup, subGroup.parentGroup());
        assertTrue(parentGroup.subGroups.contains(new WeakReference<>(subGroup)));
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);

        group.addClientHandler(handler);

        assertEquals(handler, group.handler);
    }

    @Test
    public void testRemoveClientHandler() {
        ClientGroup group = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);
        group.addClientHandler(handler);

        group.removeClientHandler(handler);

        assertNull(group.handler);
    }

    @Test
    public void testParentGroupIsNullWhenRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);

        assertNull(rootGroup.parentGroup());
    }
}

class TestException extends RuntimeException {
    public TestException(String message) {
        super(message);
    }
}

interface ParentMessageHandler {
    boolean messageSent(Client client, Object message);
}

interface ParentExceptionHandler {
    boolean exceptionCaught(Client client, Throwable cause);
}

interface SubGroupMessageHandler {
    boolean messageSent(Client client, Object message);
}

interface SubGroupExceptionHandler {
    boolean exceptionCaught(Client client, Throwable cause);
}
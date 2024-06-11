package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.Client;
import org.junit.Test;

import java.util.*;

public class ClientGroupTest {

    @Test
    public void testClientGroupCreation() {
        ClientGroup rootGroup = new ClientGroup(null);
        assertNull(rootGroup.parentGroup());
        assertEmpty(rootGroup.subGroups);
        assertEmpty(rootGroup.clients);
        assertNull(rootGroup.handler);
    }

    @Test
    public void testAddingClientToRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        assertTrue(rootGroup.add(client));
        assertTrue(rootGroup.contains(client));
        assertEquals(1, rootGroup.size());
    }

    @Test
    public void testAddingDuplicateClientToRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        assertTrue(rootGroup.add(client));
        assertFalse(rootGroup.add(client));
        assertTrue(rootGroup.contains(client));
        assertEquals(1, rootGroup.size());
    }

    @Test
    public void testAddingClientToExistingGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        parentGroup.add(client);
        ClientGroup childGroup = parentGroup.createSubGroup();
        assertFalse(childGroup.add(client));
        assertTrue(childGroup.contains(client));
        assertEquals(1, parentGroup.size());
        assertEquals(1, childGroup.size());
    }

    @Test
    public void testAddingClientHandlerToRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.addClientHandler(handler);
        assertEquals(handler, rootGroup.handler);
    }

    @Test
    public void testAddingClientToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        parentGroup.add(client);
        ClientGroup childGroup = parentGroup.createSubGroup();
        assertTrue(childGroup.add(client));
        assertTrue(childGroup.contains(client));
        assertFalse(parentGroup.contains(client));
        assertEquals(1, parentGroup.size());
        assertEquals(1, childGroup.size());
    }

    @Test
    public void testRemovingClientFromRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        assertTrue(rootGroup.add(client));
        assertTrue(rootGroup.contains(client));
        assertTrue(rootGroup.remove(client));
        assertFalse(rootGroup.contains(client));
        assertEquals(0, rootGroup.size());
    }

    @Test
    public void testRemovingNonExistentClientFromRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        assertFalse(rootGroup.remove(client));
        assertFalse(rootGroup.contains(client));
        assertEquals(0, rootGroup.size());
    }

    @Test
    public void testMessageSentToRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.add(client);
        rootGroup.addClientHandler(handler);
        assertTrue(rootGroup.notifyMessageSent(client, "test message"));
        verify(handler).messageSent(client, "test message");
    }

    @Test
    public void testMessageSentToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientGroup childGroup = parentGroup.createSubGroup();
        ClientHandler handler = mock(ClientHandler.class);
        parentGroup.add(client);
        childGroup.addClientHandler(handler);
        parentGroup.add(childGroup);
        assertTrue(parentGroup.notifyMessageSent(client, "test message"));
        assertTrue(childGroup.notifyMessageSent(client, "test message"));
        verify(handler).messageSent(client, "test message");
    }

    @Test
    public void testMessageSentToNonExistentClient() {
        ClientGroup rootGroup = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.addClientHandler(handler);
        assertFalse(rootGroup.notifyMessageSent(null, "test message"));
        verifyZeroInteractions(handler);
    }

    @Test
    public void testMessageReceivedToRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.add(client);
        rootGroup.addClientHandler(handler);
        assertTrue(rootGroup.notifyMessageReceived(client, "test message"));
        verify(handler).messageReceived(client, "test message");
    }

    @Test
    public void testMessageReceivedToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientGroup childGroup = parentGroup.createSubGroup();
        ClientHandler handler = mock(ClientHandler.class);
        parentGroup.add(client);
        childGroup.addClientHandler(handler);
        parentGroup.add(childGroup);
        assertTrue(parentGroup.notifyMessageReceived(client, "test message"));
        assertTrue(childGroup.notifyMessageReceived(client, "test message"));
        verify(handler).messageReceived(client, "test message");
    }

    @Test
    public void testMessageReceivedToNonExistentClient() {
        ClientGroup rootGroup = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.addClientHandler(handler);
        assertFalse(rootGroup.notifyMessageReceived(null, "test message"));
        verifyZeroInteractions(handler);
    }

    @Test
    public void testExceptionCaughtToRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.add(client);
        rootGroup.addClientHandler(handler);
        assertFalse(rootGroup.notifyExceptionCaught(null, new RuntimeException("test exception")));
        verifyZeroInteractions(handler);
    }

    @Test
    public void testExceptionCaughtToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientGroup childGroup = parentGroup.createSubGroup();
        ClientHandler handler = mock(ClientHandler.class);
        parentGroup.add(client);
        childGroup.addClientHandler(handler);
        parentGroup.add(childGroup);
        assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException("test exception")));
        assertTrue(childGroup.notifyExceptionCaught(client, new RuntimeException("test exception")));
        verify(handler).exceptionCaught(client, new RuntimeException("test exception"));
    }

    @Test
    public void testExceptionCaughtToNonExistentClient() {
        ClientGroup rootGroup = new ClientGroup(null);
        ClientHandler handler = mock(ClientHandler.class);
        rootGroup.addClientHandler(handler);
        assertFalse(rootGroup.notifyExceptionCaught(null, new RuntimeException("test exception")));
        verifyZeroInteractions(handler);
    }

    @Test
    public void testContainsClientInRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        assertTrue(rootGroup.contains(client));
    }

    @Test
    public void testContainsClientInSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        assertFalse(childGroup.contains(client));
    }

    @Test
    public void testSizeOfRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        Client client1 = new Client("127.0.0.1");
        Client client2 = new Client("192.168.0.1");
        rootGroup.add(client1);
        rootGroup.add(client2);
        assertEquals(2, rootGroup.size());
    }

    @Test
    public void testSizeOfSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client("127.0.0.1");
        Client client2 = new Client("192.168.0.1");
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client1);
        parentGroup.add(childGroup);
        childGroup.add(client2);
        assertEquals(1, parentGroup.size());
        assertEquals(1, childGroup.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingRootGroup() {
        ClientGroup rootGroup = new ClientGroup(null);
        rootGroup.add(new Client("127.0.0.1"));
        rootGroup.iterator().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyingSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client("127.0.0.1");
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        childGroup.iterator().remove();
    }
}
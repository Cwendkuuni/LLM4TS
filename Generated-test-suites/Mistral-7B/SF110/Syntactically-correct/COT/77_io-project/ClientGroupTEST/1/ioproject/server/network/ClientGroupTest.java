package ioproject.server.network.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.common.network.Message;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupIterator;

public class ClientGroupTest {

    private ClientGroup group;
    private Client client1, client2, client3;
    private ClientGroup subGroup;
    private ClientHandler handler;
    private ClientHandler subHandler;

    @Before
    public void setup() {
        this.group = new ClientGroup(null);
        this.client1 = new Client("client1");
        this.client2 = new Client("client2");
        this.client3 = new Client("client3");
        this.subGroup = new ClientGroup(this.group);
        this.handler = mock(ClientHandler.class);
        this.subHandler = mock(ClientHandler.class);
    }

    @Test
    public void testConstructor() {
        assertNull(this.group.parentGroup());
        assertEquals(0, this.group.size());
        assertEquals(0, this.group.clients().size());
        assertNull(this.group.handler());
    }

    @Test
    public void testParentGroup() {
        final ClientGroup parent = new ClientGroup(null);
        parent.add(this.client1);
        this.group = new ClientGroup(parent);
        assertSame(parent, this.group.parentGroup());
    }

    @Test
    public void testNotifyMessageSent_NoSuchClient() {
        this.group.add(this.client1);
        assertFalse(this.group.notifyMessageSent(this.client2, new Message("test")));
    }

    @Test
    public void testNotifyMessageSent_ClientInSubGroup() {
        this.group.add(this.client1);
        this.group.add(this.subGroup);
        this.subGroup.add(this.client2);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);

        assertFalse(this.group.notifyMessageSent(this.client2, new Message("test")));
        verify(this.handler).messageSent(this.client2, new Message("test"));
    }

    @Test
    public void testNotifyMessageSent_ClientNotInSubGroupAndHandlerIsSet() {
        this.group.add(this.client1);
        this.group.add(this.subGroup);
        this.subGroup.add(this.client2);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);

        assertTrue(this.group.notifyMessageSent(this.client2, new Message("test")));
        verify(this.handler, times(0)).messageSent(this.client2, new Message("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNotifyMessageSent_IterableIsUnmodifiable() {
        this.group.add(this.client1);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);

        final Iterator<Client> iterator = this.group.iterator();
        iterator.remove();
    }

    @Test
    public void testNotifyMessageReceived_NoSuchClient() {
        this.group.add(this.client1);
        assertFalse(this.group.notifyMessageReceived(this.client2, new Message("test")));
    }

    @Test
    public void testNotifyMessageReceived_ClientInSubGroup() {
        this.group.add(this.client1);
        this.group.add(this.subGroup);
        this.subGroup.add(this.client2);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);
        this.subHandler = mock(ClientHandler.class);
        this.subGroup.addClientHandler(this.subHandler);

        assertFalse(this.group.notifyMessageReceived(this.client2, new Message("test")));
        verify(this.handler).messageReceived(this.client2, new Message("test"));
        verify(this.subHandler).messageReceived(this.client2, new Message("test"));
    }

    @Test
    public void testNotifyMessageReceived_ClientNotInSubGroupAndHandlerIsSet() {
        this.group.add(this.client1);
        this.group.add(this.subGroup);
        this.subGroup.add(this.client2);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);

        assertTrue(this.group.notifyMessageReceived(this.client2, new Message("test")));
        verify(this.handler, times(0)).messageReceived(this.client2, new Message("test"));
    }

    @Test
    public void testNotifyExceptionCaught_NoSuchClient() {
        this.group.add(this.client1);
        assertFalse(this.group.notifyExceptionCaught(this.client2, new RuntimeException()));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInSubGroup() {
        this.group.add(this.client1);
        this.group.add(this.subGroup);
        this.subGroup.add(this.client2);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);
        this.subHandler = mock(ClientHandler.class);
        this.subGroup.addClientHandler(this.subHandler);

        assertFalse(this.group.notifyExceptionCaught(this.client2, new RuntimeException()));
        verify(this.handler).exceptionCaught(this.client2, new RuntimeException());
        verify(this.subHandler).exceptionCaught(this.client2, new RuntimeException());
    }

    @Test
    public void testNotifyExceptionCaught_ClientNotInSubGroupAndHandlerIsSet() {
        this.group.add(this.client1);
        this.group.add(this.subGroup);
        this.subGroup.add(this.client2);
        this.handler = mock(ClientHandler.class);
        this.group.addClientHandler(this.handler);

        assertTrue(this.group.notifyExceptionCaught(this.client2, new RuntimeException()));
        verify(this.handler, times(0)).exceptionCaught(this.client2, new RuntimeException());
    }

    @Test
    public void testIterator() {
        final Set<Client> set = new HashSet<>();
        set.add(this.client1);
        set.add(this.client2);
        this.group.clients = set;

        final ClientGroupIterator iterator = this.group.iterator();
        assertEquals(this.client1, iterator.next());
        assertEquals(this.client2, iterator.next());
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    public void testContains() {
        this.group.add(this.client1);
        assertTrue(this.group.contains(this.client1));
        assertFalse(this.group.contains(this.client2));
    }

    @Test
    public void testSize() {
        this.group.add(this.client1);
        assertEquals(1, this.group.size());
        this.group.add(this.client2);
        assertEquals(2, this.group.size());
    }

    @Test
    public void testAdd_NoDuplicateClient() {
        this.group.add(this.client1);
        assertFalse(this.group.add(this.client1));
        assertEquals(1, this.group.size());
    }

    @Test
    public void testAdd_DuplicateClient_ParentGroupContainsClient() {
        final ClientGroup parent = new ClientGroup(null);
        parent.add(this.client1);
        this.group = new ClientGroup(parent);

        this.group.add(this.client1);
        assertEquals(1, this.group.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAdd_DuplicateClient_ParentGroupDoesnotContainClient() {
        this.group.add(this.client1);
        this.group.add(this.client1);
    }

    @Test
    public void testRemove_NoSuchClient() {
        this.group.add(this.client1);
        assertFalse(this.group.remove(this.client2));
        assertEquals(1, this.group.size());
    }

    @Test
    public void testRemove_SuchClient() {
        this.group.add(this.client1);
        this.group.add(this.client2);
        assertTrue(this.group.remove(this.client2));
        assertEquals(1, this.group.size());
    }

    @Test
    public void testCreateSubGroup() {
        this.group.add(this.client1);
        this.subGroup = this.group.createSubGroup();
        assertFalse(this.subGroup.parentGroup().equals(this.group));
        assertEquals(0, this.subGroup.size());
        assertEquals(1, this.group.size());
    }

    @Test
    public void testAddClientHandler() {
        this.group.add(this.client1);
        this.group.addClientHandler(this.handler);
        assertEquals(this.handler, this.group.handler());
    }

    @After
    public void teardown() {
        this.client1 = null;
        this.client2 = null;
        this.client3 = null;
        this.group = null;
        this.subGroup = null;
        this.handler = null;
        this.subHandler = null;
    }
}
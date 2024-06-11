package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import ioproject.common.network.ClientHandler;
import ioproject.common.network.Client;

public class ClientGroupTest {

ClientGroup createTestClientGroup() {
ClientGroup clientGroup = new ClientGroup(null);
return clientGroup;
}

@Test
public void testCreateClientGroup() {
ClientGroup clientGroup = createTestClientGroup();
assertNull(clientGroup.parentGroup());
assertTrue(clientGroup.clients.isEmpty());
assertFalse(clientGroup.contains(new Client()));
assertFalse(clientGroup.size() > 0);
assertFalse(clientGroup.iterator().hasNext());
}

@Test
public void testAddAndRemoveClient() {
Client client = mock(Client.class);
ClientGroup clientGroup = createTestClientGroup();

// Test adding a client
assertTrue(clientGroup.add(client));
assertTrue(clientGroup.contains(client));
assertEquals(1, clientGroup.size());

// Test removing a client
assertTrue(clientGroup.remove(client));
assertFalse(clientGroup.contains(client));
assertEquals(0, clientGroup.size());

// Test adding and removing the same client multiple times
assertTrue(clientGroup.add(client));
assertTrue(clientGroup.remove(client));
assertTrue(clientGroup.add(client));
assertTrue(clientGroup.remove(client));
assertEquals(0, clientGroup.size());
}

@Test
public void testContainsClient() {
Client client = mock(Client.class);
ClientGroup clientGroup = createTestClientGroup();

// Test containing a client that is not in the group
assertFalse(clientGroup.contains(client));

// Test containing a client after it has been added
assertTrue(clientGroup.add(client));
assertTrue(clientGroup.contains(client));
}

@Test
public void testSize() {
Client client = mock(Client.class);
ClientGroup clientGroup = createTestClientGroup();

// Test size when no clients are added
assertEquals(0, clientGroup.size());

// Test size after adding a client
assertTrue(clientGroup.add(client));
assertEquals(1, clientGroup.size());

// Test size after adding multiple clients
Client client2 = mock(Client.class);
assertTrue(clientGroup.add(client2));
assertEquals(2, clientGroup.size());
}

@Test
public void testIterator() {
Client client = mock(Client.class);
Client client2 = mock(Client.class);
ClientGroup clientGroup = createTestClientGroup();

// Test iterator when no clients are added
assertFalse(clientGroup.iterator().hasNext());

// Test iterator after adding a client
assertTrue(clientGroup.add(client));
assertTrue(clientGroup.iterator().hasNext());
assertEquals(client, clientGroup.iterator().next());

// Test iterator after adding multiple clients
assertTrue(clientGroup.add(client2));
assertTrue(clientGroup.iterator().hasNext());
assertEquals(client, clientGroup.iterator().next());
assertTrue(clientGroup.iterator().hasNext());
assertEquals(client2, clientGroup.iterator().next());
}

@Test
public void testNotifyMessageSent() {
Client client = mock(Client.class);
ClientGroup parentGroup = mock(ClientGroup.class);
ClientGroup clientGroup = new ClientGroup(parentGroup);
Object message = new Object();
ClientHandler handler = mock(ClientHandler.class);

// Test notifyMessageSent when message is not sent to any client in the group or its subgroups
when(clientGroup.notifyMessageSent(client, message)).thenReturn(false);
when(parentGroup.notifyMessageSent(client, message)).thenReturn(false);
assertFalse(clientGroup.notifyMessageSent(client, message));

// Test notifyMessageSent when message is sent to a client in the group
when(clientGroup.notifyMessageSent(client, message)).thenReturn(true);
assertFalse(parentGroup.notifyMessageSent(client, message));
assertTrue(clientGroup.notifyMessageSent(client, message));

// Test notifyMessageSent when message is sent to a client in a subgroup
ClientGroup subGroup = mock(ClientGroup.class);
when(clientGroup.subGroups.iterator().next().get()).thenReturn(subGroup);
when(subGroup.notifyMessageSent(client, message)).thenReturn(true);
assertFalse(parentGroup.notifyMessageSent(client, message));
assertTrue(clientGroup.notifyMessageSent(client, message));
assertTrue(subGroup.notifyMessageSent(client, message));

// Test notifyMessageSent when message is sent to the handler
when(clientGroup.notifyMessageSent(client, message)).thenReturn(false);
when(parentGroup.notifyMessageSent(client, message)).thenReturn(false);
when(clientGroup.handler != null).thenReturn(handler);
handler.messageSent(client, message);
assertTrue(clientGroup.notifyMessageSent(client, message));
}

@Test
public void testNotifyMessageReceived() {
Client client = mock(Client.class);
ClientGroup parentGroup = mock(ClientGroup.class);
ClientGroup clientGroup = new ClientGroup(parentGroup);
Object message = new Object();
ClientHandler handler = mock(ClientHandler.class);

// Test notifyMessageReceived when message is not received by any client in the group or its subgroups
when(clientGroup.notifyMessageReceived(client, message)).thenReturn(false);
when(parentGroup.notifyMessageReceived(client, message)).thenReturn(false);
assertFalse(clientGroup.notifyMessageReceived(client, message));

// Test notifyMessageReceived when message is received by a client in the group
when(clientGroup.notifyMessageReceived(client, message)).thenReturn(true);
assertFalse(parentGroup.notifyMessageReceived(client, message));
assertTrue(clientGroup.notifyMessageReceived(client, message));

// Test notifyMessageReceived when message is received by a client in a subgroup
ClientGroup subGroup = mock(ClientGroup.class);
when(clientGroup.subGroups.iterator().next().get()).thenReturn(subGroup);
when(subGroup.notifyMessageReceived(client, message)).thenReturn(true);
assertFalse(parentGroup.notifyMessageReceived(client, message));
assertTrue(clientGroup.notifyMessageReceived(client, message));
assertTrue(subGroup.notifyMessageReceived(client, message));

// Test notifyMessageReceived when message is received by the handler
when(clientGroup.notifyMessageReceived(client, message)).thenReturn(false);
when(parentGroup.notifyMessageReceived(client, message)).thenReturn(false);
when(clientGroup.handler != null).thenReturn(handler);
handler.messageReceived(client, message);
assertTrue(clientGroup.notifyMessageReceived(client, message));
}

@Test
public void testNotifyExceptionCaught() {
Client client = mock(Client.class);
ClientGroup parentGroup = mock(ClientGroup.class);
ClientGroup clientGroup = new ClientGroup(parentGroup);
Throwable exception = new Throwable();
ClientHandler handler = mock(ClientHandler.class);

// Test notifyExceptionCaught when exception is not caught by any client in the group or its subgroups
when(clientGroup.notifyExceptionCaught(client, exception)).thenReturn(false);
when(parentGroup.notifyExceptionCaught(client, exception)).thenReturn(false);
assertFalse(clientGroup.notifyExceptionCaught(client, exception));

// Test notifyExceptionCaught when exception is caught by a client in the group
when(clientGroup.notifyExceptionCaught(client, exception)).thenReturn(true);
assertFalse(parentGroup.notifyExceptionCaught(client, exception));
assertTrue(clientGroup.notifyExceptionCaught(client, exception));

// Test notifyExceptionCaught when exception is caught by a client in a subgroup
ClientGroup subGroup = mock(ClientGroup.class);
when(clientGroup.subGroups.iterator().next().get()).thenReturn(subGroup);
when(subGroup.notifyExceptionCaught(client, exception)).thenReturn(true);
assertFalse(parentGroup.notifyExceptionCaught(client, exception));
assertTrue(clientGroup.notifyExceptionCaught(client, exception));
assertTrue(subGroup.notifyExceptionCaught(client, exception));

// Test notifyExceptionCaught when exception is caught by the handler
when(clientGroup.notifyExceptionCaught(client, exception)).thenReturn(false);
when(parentGroup.notifyExceptionCaught(client, exception)).thenReturn(false);
when(clientGroup.handler != null).thenReturn(handler);
handler.exceptionCaught(client, exception);
assertTrue(clientGroup.notifyExceptionCaught(client, exception));
}

@Test
public void testCreateSubGroup() {
ClientGroup parentGroup = mock(ClientGroup.class);
ClientGroup clientGroup = new ClientGroup(parentGroup);
ClientGroup subGroup = clientGroup.createSubGroup();

assertNotNull(subGroup);
assertEquals(clientGroup, subGroup.parentGroup());
assertTrue(clientGroup.subGroups.contains(new WeakReference<ClientGroup>(subGroup)));
}

@Test
public void testAddClientHandler() {
Client client = mock(Client.class);
ClientGroup clientGroup = createTestClientGroup();
ClientHandler handler = mock(ClientHandler.class);

// Test adding the same handler multiple times
assertTrue(clientGroup.addClientHandler(handler));
assertTrue(clientGroup.addClientHandler(handler));

// Test adding different handlers
ClientHandler handler2 = mock(ClientHandler.class);
assertTrue(clientGroup.addClientHandler(handler));
assertTrue(clientGroup.addClientHandler(handler2));

// Test notifying the handler when a message is sent
Object message = new Object();
assertTrue(clientGroup.notifyMessageSent(client, message));
assertTrue(handler.messageSent(client, message));

// Test notifying the handler when a message is received
assertTrue(clientGroup.notifyMessageReceived(client, message));
assertTrue(handler.messageReceived(client, message));

// Test notifying the handler when an exception is caught
assertTrue(clientGroup.notifyExceptionCaught(client, new Throwable()));
assertTrue(handler.exceptionCaught(client, new Throwable()));
}
}
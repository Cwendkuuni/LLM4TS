package ioproject.server.network.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerInterface;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientHandler rootHandler;
 private Client client;
 private ClientGroup subGroup;
 private ClientHandler subGroupHandler;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 rootHandler = mock(ClientHandler.class);
 rootGroup.addClientHandler(rootHandler);
 client = new Client();
 subGroup = rootGroup.createSubGroup();
 subGroupHandler = mock(ClientHandler.class);
 subGroup.addClientHandler(subGroupHandler);
 }

 @Test
 public void testAddClientToRootGroup() {
 assertTrue(rootGroup.add(client));
 assertTrue(rootGroup.contains(client));
 }

 @Test
 public void testAddClientToSubGroup() {
 rootGroup.add(client);
 assertTrue(subGroup.add(client));
 assertTrue(subGroup.contains(client));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateClientToRootGroup() {
 rootGroup.add(client);
 assertFalse(rootGroup.add(client));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateClientToSubGroup() {
 rootGroup.add(client);
 subGroup.add(client);
 assertFalse(subGroup.add(client));
 }

 @Test
 public void testSizeOfEmptyGroup() {
 assertTrue(rootGroup.size() == 0);
 }

 @Test
 public void testSizeOfGroupWithClients() {
 rootGroup.add(client);
 assertTrue(rootGroup.size() == 1);
 }

 @Test
 public void testSizeOfGroupWithSubGroups() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 assertTrue(rootGroup.size() == 2);
 }

 @Test
 public void testNotifyMessageSentToEmptyGroup() {
 assertFalse(rootGroup.notifyMessageSent(client, "message"));
 }

 @Test
 public void testNotifyMessageSentToGroupWithClients() {
 rootGroup.add(client);
 assertTrue(rootGroup.notifyMessageSent(client, "message"));
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(rootGroup.notifyMessageSent(client, "message"));
 verify(subGroupHandler).messageSent(client, "message");
 }

 @Test
 public void testNotifyMessageSentToRootGroupWithHandler() {
 rootGroup.add(client);
 rootGroup.addClientHandler(new ClientHandlerInterface() {
 @Override
 public void messageSent(Client client, Object message) {
 assertNotNull(message);
 }
 });
 rootGroup.notifyMessageSent(client, "message");
 }

 @Test
 public void testNotifyMessageSentToEmptySubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 assertFalse(subGroup.notifyMessageSent(client, "message"));
 }

 @Test
 public void testNotifyMessageSentToSubGroupWithClients() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(subGroup.notifyMessageSent(client, "message"));
 }

 @Test
 public void testNotifyMessageSentToRootGroupWithNullClient() {
 assertFalse(rootGroup.notifyMessageSent(null, "message"));
 }

 @Test
 public void testNotifyMessageSentToSubGroupWithNullClient() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertFalse(subGroup.notifyMessageSent(null, "message"));
 }

 @Test
 public void testNotifyMessageReceivedToEmptyGroup() {
 assertFalse(rootGroup.notifyMessageReceived(client, "message"));
 }

 @Test
 public void testNotifyMessageReceivedToGroupWithClients() {
 rootGroup.add(client);
 assertTrue(rootGroup.notifyMessageReceived(client, "message"));
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(rootGroup.notifyMessageReceived(client, "message"));
 verify(subGroupHandler).messageReceived(client, "message");
 }

 @Test
 public void testNotifyMessageReceivedToRootGroupWithHandler() {
 rootGroup.add(client);
 rootGroup.addClientHandler(new ClientHandlerInterface() {
 @Override
 public void messageReceived(Client client, Object message) {
 assertNotNull(message);
 }
 });
 rootGroup.notifyMessageReceived(client, "message");
 }

 @Test
 public void testNotifyMessageReceivedToEmptySubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 assertFalse(subGroup.notifyMessageReceived(client, "message"));
 }

 @Test
 public void testNotifyMessageReceivedToSubGroupWithClients() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(subGroup.notifyMessageReceived(client, "message"));
 }

 @Test
 public void testNotifyExceptionCaughtToEmptyGroup() {
 assertFalse(rootGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaughtToGroupWithClients() {
 rootGroup.add(client);
 assertTrue(rootGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(rootGroup.notifyExceptionCaught(client, new RuntimeException()));
 verify(subGroupHandler).exceptionCaught(client, new RuntimeException());
 }

 @Test
 public void testNotifyExceptionCaughtToRootGroupWithHandler() {
 rootGroup.add(client);
 rootGroup.addClientHandler(new ClientHandlerInterface() {
 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 assertNotNull(cause);
 }
 });
 rootGroup.notifyExceptionCaught(client, new RuntimeException());
 }

 @Test
 public void testNotifyExceptionCaughtToEmptySubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 assertFalse(subGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroupWithClients() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(subGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaughtToRootGroupWithNullClient() {
 assertFalse(rootGroup.notifyExceptionCaught(null, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroupWithNullClient() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertFalse(subGroup.notifyExceptionCaught(null, new RuntimeException()));
 }

 @Test
 public void testIterator() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 Set<Client> clients = new LinkedHashSet<>(rootGroup);
 ArrayList<Client> clientList = new ArrayList<>(clients);
 Client testClient = clientList.get(0);
 Iterator<Client> iterator = rootGroup.iterator();
 Client groupClient = iterator.next();
 assertTrue(groupClient.equals(client));
 assertTrue(iterator.hasNext());
 Client subGroupClient = iterator.next();
 assertTrue(subGroupClient.equals(client));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testRemoveClientFromEmptyGroup() {
 assertFalse(rootGroup.remove(client));
 }

 @Test
 public void testRemoveClientFromGroupWithClients() {
 rootGroup.add(client);
 rootGroup.remove(client);
 assertFalse(rootGroup.contains(client));
 }

 @Test
 public void testRemoveClientFromSubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 subGroup.remove(client);
 assertFalse(subGroup.contains(client));
 }

 @Test
 public void testRemoveClientFromRootGroupAndSubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 rootGroup.remove(client);
 assertFalse(rootGroup.contains(client));
 assertFalse(subGroup.contains(client));
 }

 @Test
 public void testRemoveClientFromEmptySubGroup() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 assertFalse(subGroup.remove(client));
 }

 @Test
 public void testRemoveClientFromSubGroupWithClients() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 subGroup.remove(client);
 assertFalse(subGroup.contains(client));
 }

 @Test
 public void testRemoveClientFromGroupWithNullClient() {
 rootGroup.add(client);
 assertFalse(rootGroup.remove(null));
 }

 @Test
 public void testRemoveClientFromSubGroupWithNullClient() {
 rootGroup.add(client);
 rootGroup.add(subGroup);
 subGroup.add(client);
 assertFalse(subGroup.remove(null));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 assertTrue(subGroup instanceof ClientGroup);
 assertTrue(rootGroup.subGroups.contains(new WeakReference<ClientGroup>(subGroup)));
 }

 @Test
 public void testCreateSubGroupWithNullParent() {
 assertFalse(new ClientGroup(null).createSubGroup() instanceof ClientGroup);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateSubGroupWithParentThatHasParent() {
 rootGroup = new ClientGroup(new ClientGroup(null));
 assertFalse(rootGroup.createSubGroup() instanceof ClientGroup);
 }

 @Test
 public void testAddClientHandlerToEmptyGroup() {
 assertNull(rootGroup.handler);
 rootGroup.addClientHandler(rootHandler);
 assertNotNull(rootGroup.handler);
 }

 @Test
 public void testAddClientHandlerToGroupWithHandler() {
 ClientHandler handler = mock(ClientHandler.class);
 rootGroup.addClientHandler(rootHandler);
 rootGroup.addClientHandler(handler);
 assertNotNull(rootGroup.handler);
 verify(handler).messageSent(Collections.anyObject(), anyObject());
 verify(handler).messageReceived(Collections.anyObject(), anyObject());
 verify(handler).exceptionCaught(Collections.anyObject(), anyObject());
 }

 @Test
 public void testAddClientHandlerToSubGroup() {
 ClientHandler handler = mock(ClientHandler.class);
 rootGroup.addClientHandler(rootHandler);
 rootGroup.add(subGroup);
 subGroup.addClientHandler(handler);
 assertNotNull(subGroup.handler);
 verify(handler).messageSent(Collections.anyObject(), anyObject());
 verify(handler).messageReceived(Collections.anyObject(), anyObject());
 verify(handler).exceptionCaught(Collections.anyObject(), anyObject());
 }

 @Test
 public void testAddClientHandlerToSubGroupWithHandler() {
 ClientHandler handler = mock(ClientHandler.class);
 rootGroup.addClientHandler(rootHandler);
 rootGroup.add(subGroup);
 subGroup.addClientHandler(handler);
 rootGroup.addClientHandler(new ClientHandlerInterface() {
 @Override
 public void messageSent(Client client, Object message) {
 }
 });
 verify(handler, never()).messageSent(anyObject(), anyObject());
 }
}
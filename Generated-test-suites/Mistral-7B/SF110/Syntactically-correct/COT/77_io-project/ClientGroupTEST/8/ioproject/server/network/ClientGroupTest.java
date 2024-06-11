import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.WeakReference;

import static org.junit.Assert.*;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private Client client2;
 private ClientHandler handler;
 private ClientHandler subGroupHandler;

 @Before
 public void setUp() {
 client = Mockito.spy(new Client("Client1"));
 client2 = Mockito.spy(new Client("Client2"));
 handler = Mockito.mock(ClientHandler.class);
 subGroupHandler = Mockito.mock(ClientHandler.class);
 clientGroup = new ClientGroup(null);
 clientGroup.addClientHandler(handler);
 }

 @Test
 public void testConstructor() {
 assertNull(clientGroup.parentGroup());
 assertNotNull(clientGroup.clients());
 assertFalse(clientGroup.contains(client));
 }

 @Test
 public void testNotifyMessageSent_ClientInGroup_True() {
 when(clientGroup.notifyMessageSent(client, Mockito.anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageSent(client, "Message"));
 }

 @Test
 public void testNotifyMessageSent_ClientNotInGroup_False() {
 assertFalse(clientGroup.notifyMessageSent(client, "Message"));
 }

 @Test
 public void testNotifyMessageSent_ClientInSubGroup_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 when(subGroup.notifyMessageSent(client, Mockito.anyObject())).thenReturn(true);
 when(clientGroup.notifyMessageSent(client, Mockito.anyObject())).thenCallRealMethod();
 clientGroup.add(subGroup);
 clientGroup.add(client);
 assertTrue(clientGroup.notifyMessageSent(client, "Message"));
 }

 @Test
 public void testNotifyMessageSent_ClientInSubGroupAndHandler_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 when(subGroup.notifyMessageSent(client, Mockito.anyObject())).thenReturn(false);
 when(clientGroup.notifyMessageSent(client, Mockito.anyObject())).thenCallRealMethod();
 clientGroup.add(subGroup);
 clientGroup.add(client);
 clientGroup.addClientHandler(subGroupHandler);
 Mockito.when(subGroupHandler.messageSent(client, Mockito.anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageSent(client, "Message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientInGroup_True() {
 when(clientGroup.notifyMessageReceived(client, Mockito.anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageReceived(client, "Message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotInGroup_False() {
 assertFalse(clientGroup.notifyMessageReceived(client, "Message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientInSubGroup_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 when(subGroup.notifyMessageReceived(client, Mockito.anyObject())).thenReturn(true);
 when(clientGroup.notifyMessageReceived(client, Mockito.anyObject())).thenCallRealMethod();
 clientGroup.add(subGroup);
 clientGroup.add(client);
 assertTrue(clientGroup.notifyMessageReceived(client, "Message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientInSubGroupAndHandler_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 when(subGroup.notifyMessageReceived(client, Mockito.anyObject())).thenReturn(false);
 when(clientGroup.notifyMessageReceived(client, Mockito.anyObject())).thenCallRealMethod();
 clientGroup.add(subGroup);
 clientGroup.add(client);
 clientGroup.addClientHandler(subGroupHandler);
 Mockito.when(subGroupHandler.messageReceived(client, Mockito.anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageReceived(client, "Message"));
 }

 @Test
 public void testNotifyExceptionCaught_ClientInGroup_True() {
 when(clientGroup.notifyExceptionCaught(client, Mockito.anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotInGroup_False() {
 assertFalse(clientGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientInSubGroup_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 when(subGroup.notifyExceptionCaught(client, Mockito.anyObject())).thenReturn(true);
 when(clientGroup.notifyExceptionCaught(client, Mockito.anyObject())).thenCallRealMethod();
 clientGroup.add(subGroup);
 clientGroup.add(client);
 assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientInSubGroupAndHandler_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 when(subGroup.notifyExceptionCaught(client, Mockito.anyObject())).thenReturn(false);
 when(clientGroup.notifyExceptionCaught(client, Mockito.anyObject())).thenCallRealMethod();
 clientGroup.add(subGroup);
 clientGroup.add(client);
 clientGroup.addClientHandler(subGroupHandler);
 Mockito.when(subGroupHandler.exceptionCaught(client, Mockito.anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testIterator_EmptyList_NotNull() {
 Iterator<Client> iterator = clientGroup.iterator();
 assertNotNull(iterator);
 }

 @Test
 public void testIterator_EmptyList_Empty() {
 Iterator<Client> iterator = clientGroup.iterator();
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testContains_ClientNotInGroup_False() {
 assertFalse(clientGroup.contains(client));
 }

 @Test
 public void testContains_ClientInGroup_True() {
 clientGroup.add(client);
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize_EmptyList_Zero() {
 assertEquals(0, clientGroup.size());
 }

 @Test
 public void testSize_ListWithSizeOne_One() {
 clientGroup.add(client);
 assertEquals(1, clientGroup.size());
 }

 @Test
 public void testSize_ListWithSizeFive_Five() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 clients.add(client2);
 clients.add(Mockito.spy(new Client("Client3")));
 clients.add(Mockito.spy(new Client("Client4")));
 clients.add(Mockito.spy(new Client("Client5")));
 clientGroup = new ClientGroup(null);
 clientGroup.addAll(clients);
 assertEquals(5, clientGroup.size());
 }

 @Test
 public void testAdd_ClientAlreadyInGroup_False() {
 clientGroup.add(client);
 assertFalse(clientGroup.add(client));
 }

 @Test
 public void testAdd_ClientNotInGroup_True() {
 assertFalse(clientGroup.contains(client));
 assertTrue(clientGroup.add(client));
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testAddAll_EmptyGroup_True() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 clients.add(client2);
 assertTrue(clientGroup.addAll(clients));
 assertEquals(2, clientGroup.size());
 }

 @Test
 public void testAddAll_GroupWithSizeOne_SizeTwo() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 assertTrue(clientGroup.add(client));
 clients.add(client2);
 assertTrue(clientGroup.addAll(clients));
 assertEquals(3, clientGroup.size());
 }

 @Test
 public void testRemove_ClientNotInGroup_False() {
 assertFalse(clientGroup.remove(client));
 }

 @Test
 public void testRemove_ClientInGroup_True() {
 clientGroup.add(client);
 assertTrue(clientGroup.remove(client));
 assertFalse(clientGroup.contains(client));
 }

 @Test
 public void testRemove_ClientInSubGroup_True() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 clientGroup.add(subGroup);
 subGroup.add(client);
 assertTrue(clientGroup.remove(client));
 assertFalse(clientGroup.contains(client));
 }

 @Test
 public void testRemoveClientHandler() {
 clientGroup.addClientHandler(handler);
 clientGroup.removeClientHandler(handler);
 assertNull(clientGroup.handler);
 }

 @Test
 public void testCreateSubGroup_EmptyGroup_NotNull() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertNotNull(subGroup);
 }

 @Test
 public void testCreateSubGroup_GroupWithSizeOne_NotNull() {
 clientGroup.add(client);
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertNotNull(subGroup);
 }
}
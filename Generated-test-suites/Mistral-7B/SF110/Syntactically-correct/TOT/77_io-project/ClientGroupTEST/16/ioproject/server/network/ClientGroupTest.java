package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = rootGroup.createSubGroup();
 client1 = Mockito.spy(new Client("Client1"));
 client2 = Mockito.spy(new Client("Client2"));
 client3 = Mockito.spy(new Client("Client3"));
 handler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testAddClientToRootGroup() {
 rootGroup.add(client1);
 assertTrue(rootGroup.contains(client1));
 }

 @Test
 public void testAddClientToSubGroup() {
 subGroup1.add(client2);
 assertTrue(subGroup1.contains(client2));
 assertFalse(rootGroup.contains(client2));
 }

 @Test
 public void testAddDuplicateClient() {
 rootGroup.add(client1);
 assertFalse(rootGroup.add(client1));
 }

 @Test
 public void testRemoveClientFromRootGroup() {
 rootGroup.add(client1);
 rootGroup.remove(client1);
 assertFalse(rootGroup.contains(client1));
 }

 @Test
 public void testRemoveClientFromSubGroup() {
 subGroup1.add(client2);
 subGroup1.remove(client2);
 assertFalse(subGroup1.contains(client2));
 }

 @Test
 public void testContainsClient() {
 rootGroup.add(client1);
 assertTrue(rootGroup.contains(client1));
 }

 @Test
 public void testSize() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 assertEquals(2, rootGroup.size());
 }

 @Test
 public void testNotifyMessageSent() {
 final Object message = new Object();
 handler = Mockito.mock(ClientHandler.class);
 rootGroup.add(client1);
 rootGroup.addClientHandler(handler);
 Mockito.doAnswer(invocation -> invocation.getArgument(1)).when(handler).messageSent(Mockito.any(), Mockito.eq(message));
 rootGroup.notifyMessageSent(client1, message);
 Mockito.verify(handler, Mockito.times(1)).messageSent(client1, message);
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 final Object message = new Object();
 handler = Mockito.mock(ClientHandler.class);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 Mockito.doAnswer(invocation -> invocation.getArgument(1)).when(handler).messageSent(Mockito.any(), Mockito.eq(message));
 rootGroup.notifyMessageSent(client2, message);
 Mockito.verify(handler, Mockito.times(1)).messageSent(client2, message);
 }

 @Test
 public void testNotifyMessageReceived() {
 final Object message = new Object();
 handler = Mockito.mock(ClientHandler.class);
 rootGroup.add(client1);
 rootGroup.addClientHandler(handler);
 Mockito.doAnswer(invocation -> invocation.getArgument(1)).when(handler).messageReceived(Mockito.any(), Mockito.eq(message));
 rootGroup.notifyMessageReceived(client1, message);
 Mockito.verify(handler, Mockito.times(1)).messageReceived(client1, message);
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 final Object message = new Object();
 handler = Mockito.mock(ClientHandler.class);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 Mockito.doAnswer(invocation -> invocation.getArgument(1)).when(handler).messageReceived(Mockito.any(), Mockito.eq(message));
 rootGroup.notifyMessageReceived(client2, message);
 Mockito.verify(handler, Mockito.times(1)).messageReceived(client2, message);
 }

 @Test
 public void testNotifyExceptionCaught() {
 final Throwable cause = new RuntimeException();
 handler = Mockito.mock(ClientHandler.class);
 rootGroup.add(client1);
 rootGroup.addClientHandler(handler);
 Mockito.doAnswer(invocation -> invocation.getArgument(1)).when(handler).exceptionCaught(Mockito.any(), Mockito.eq(cause));
 rootGroup.notifyExceptionCaught(client1, cause);
 Mockito.verify(handler, Mockito.times(1)).exceptionCaught(client1, cause);
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 final Throwable cause = new RuntimeException();
 handler = Mockito.mock(ClientHandler.class);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 Mockito.doAnswer(invocation -> invocation.getArgument(1)).when(handler).exceptionCaught(Mockito.any(), Mockito.eq(cause));
 rootGroup.notifyExceptionCaught(client2, cause);
 Mockito.verify(handler, Mockito.times(1)).exceptionCaught(client2, cause);
 }

 @Test
 public void testIterator() {
 final Set<Client> clients = new HashSet<>(Arrays.asList(client1, client2, client3));
 when(rootGroup.clients.iterator()).thenReturn(clients.iterator());
 Iterator<Client> iterator = rootGroup.iterator();
 assertEquals(client1, iterator.next());
 assertEquals(client2, iterator.next());
 assertEquals(client3, iterator.next());
 assertFalse(iterator.hasNext());
 }
}
package ioproject.server.network.test;

import ioproject.common.network.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private ClientGroup parentGroup;
 private ClientHandler handler;

 @Before
 public void setup() {
 parentGroup = mock(ClientGroup.class);
 client = mock(Client.class);
 handler = mock(ClientHandler.class);
 clientGroup = new ClientGroup(parentGroup);
 }

 @Test
 public void testConstructor() {
 assertNull(clientGroup.handler);
 assertNull(clientGroup.parentGroup());
 assertTrue(clientGroup.clients.isEmpty());
 assertFalse(clientGroup.size() > 0);
 }

 @Test
 public void testAddClient() {
 when(parentGroup.contains(client)).thenReturn(false);
 assertTrue(clientGroup.add(client));
 assertTrue(clientGroup.contains(client));
 assertEquals(1, clientGroup.size());
 verify(handler, times(1)).clientAdded(client);
 }

 @Test
 public void testAddClientAlreadyExists() {
 when(parentGroup.contains(client)).thenReturn(true);
 assertFalse(clientGroup.add(client));
 assertFalse(clientGroup.contains(client));
 assertEquals(0, clientGroup.size());
 verify(handler, never()).clientAdded(client);
 }

 @Test
 public void testRemoveClient() {
 when(clientGroup.contains(client)).thenReturn(true);
 clientGroup.add(client);
 assertTrue(clientGroup.remove(client));
 assertFalse(clientGroup.contains(client));
 assertEquals(0, clientGroup.size());
 verify(handler, times(1)).clientRemoved(client);
 }

 @Test
 public void testRemoveClientNotPresent() {
 when(clientGroup.contains(client)).thenReturn(false);
 assertFalse(clientGroup.remove(client));
 assertFalse(clientGroup.contains(client));
 assertEquals(0, clientGroup.size());
 verify(handler, never()).clientRemoved(client);
 }

 @Test
 public void testNotifyMessageSent() {
 when(clientGroup.contains(client)).thenReturn(true);
 when(parentGroup.notifyMessageSent(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageSent(client, anyObject()));
 verify(handler, times(1)).messageSent(client, anyObject());
 }

 @Test
 public void testNotifyMessageSentSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 WeakReference<ClientGroup> subGroupRef = new WeakReference<>(subGroup);
 when(clientGroup.notifyMessageSent(client, anyObject())).thenReturn(false);
 when(subGroup.notifyMessageSent(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageSent(client, anyObject()));
 verify(handler, times(1)).messageSent(client, anyObject());
 clientGroup.subGroups.add(subGroupRef);
 }

 @Test
 public void testNotifyMessageSentNoHandler() {
 when(clientGroup.contains(client)).thenReturn(true);
 when(parentGroup.notifyMessageSent(client, anyObject())).thenReturn(false);
 for (final WeakReference<ClientGroup> groupReference : clientGroup.subGroups) {
 when(groupReference.get().notifyMessageSent(client, anyObject())).thenReturn(false);
 }
 assertFalse(clientGroup.notifyMessageSent(client, anyObject()));
 }

 @Test
 public void testNotifyMessageReceived() {
 when(clientGroup.contains(client)).thenReturn(true);
 when(parentGroup.notifyMessageReceived(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageReceived(client, anyObject()));
 verify(handler, times(1)).messageReceived(client, anyObject());
 }

 @Test
 public void testNotifyMessageReceivedSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 WeakReference<ClientGroup> subGroupRef = new WeakReference<>(subGroup);
 when(clientGroup.notifyMessageReceived(client, anyObject())).thenReturn(false);
 when(subGroup.notifyMessageReceived(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageReceived(client, anyObject()));
 verify(handler, times(1)).messageReceived(client, anyObject());
 clientGroup.subGroups.add(subGroupRef);
 }

 @Test
 public void testNotifyExceptionCaught() {
 when(clientGroup.contains(client)).thenReturn(true);
 when(parentGroup.notifyExceptionCaught(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyExceptionCaught(client, anyObject()));
 verify(handler, times(1)).exceptionCaught(client, anyObject());
 }

 @Test
 public void testNotifyExceptionCaughtSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 WeakReference<ClientGroup> subGroupRef = new WeakReference<>(subGroup);
 when(clientGroup.notifyExceptionCaught(client, anyObject())).thenReturn(false);
 when(subGroup.notifyExceptionCaught(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyExceptionCaught(client, anyObject()));
 verify(handler, times(1)).exceptionCaught(client, anyObject());
 clientGroup.subGroups.add(subGroupRef);
 }

 @Test
 public void testNotifyExceptionCaughtNoHandler() {
 when(clientGroup.contains(client)).thenReturn(true);
 for (final WeakReference<ClientGroup> groupReference : clientGroup.subGroups) {
 when(groupReference.get().notifyExceptionCaught(client, anyObject())).thenReturn(false);
 }
 assertFalse(clientGroup.notifyExceptionCaught(client, anyObject()));
 }

 @Test
 public void testContains() {
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize() {
 clientGroup.add(client);
 assertEquals(1, clientGroup.size());
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertNotNull(subGroup);
 }

 @Test
 public void testIterator() {
 clientGroup.add(client);
 Iterator<Client> iterator = clientGroup.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client, iterator.next());
 assertFalse(iterator.hasNext());
 }
}
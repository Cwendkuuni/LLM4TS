package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import ioproject.common.network.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup topLevelGroup;
 Client client1, client2;
 ClientHandler handlerMock;

 @Before
 public void setUp() {
 topLevelGroup = new ClientGroup(null);
 client1 = mock(Client.class);
 client2 = mock(Client.class);
 handlerMock = mock(ClientHandler.class);
 }

 @Test
 public void testConstructor() {
 assertEquals(null, topLevelGroup.parentGroup());
 assertTrue(topLevelGroup.subGroups.isEmpty());
 assertTrue(topLevelGroup.clients.isEmpty());
 assertNull(topLevelGroup.handler);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(topLevelGroup);
 assertEquals(topLevelGroup, parentGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);
 when(client1.getId()).thenReturn("client1");

 assertTrue(topLevelGroup.notifyMessageSent(client1, "message"));
 verify(handlerMock).messageSent(client1, "message");
 }

 @Test
 public void testNotifyMessageSentWhenClientNotPresent() {
 assertFalse(topLevelGroup.notifyMessageSent(client2, "message"));
 verifyNoInteractions(handlerMock);
 }

 @Test
 public void testNotifyMessageSentInSubGroup() {
 ClientGroup subGroup = topLevelGroup.createSubGroup();
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);
 subGroup.add(client1);
 when(client1.getId()).thenReturn("client1");

 assertTrue(topLevelGroup.notifyMessageSent(client1, "message"));
 verify(handlerMock).messageSent(client1, "message");

 verifyZeroInteractions(subGroup.handler);
 }

 @Test
 public void testNotifyMessageReceived() {
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);
 when(client1.getId()).thenReturn("client1");

 assertTrue(topLevelGroup.notifyMessageReceived(client1, "message"));
 verify(handlerMock).messageReceived(client1, "message");
 }

 @Test
 public void testNotifyMessageReceivedWhenClientNotPresent() {
 assertFalse(topLevelGroup.notifyMessageReceived(client2, "message"));
 verifyNoInteractions(handlerMock);
 }

 @Test
 public void testNotifyMessageReceivedInSubGroup() {
 ClientGroup subGroup = topLevelGroup.createSubGroup();
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);
 subGroup.add(client1);
 when(client1.getId()).thenReturn("client1");

 assertTrue(topLevelGroup.notifyMessageReceived(client1, "message"));
 verify(handlerMock).messageReceived(client1, "message");

 verifyZeroInteractions(subGroup.handler);
 }

 @Test
 public void testNotifyExceptionCaught() {
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);
 when(client1.getId()).thenReturn("client1");

 Throwable cause = new RuntimeException("test exception");
 assertTrue(topLevelGroup.notifyExceptionCaught(client1, cause));
 verify(handlerMock).exceptionCaught(client1, cause);
 }

 @Test
 public void testNotifyExceptionCaughtWhenClientNotPresent() {
 assertFalse(topLevelGroup.notifyExceptionCaught(client2, new RuntimeException("test exception")));
 verifyNoInteractions(handlerMock);
 }

 @Test
 public void testNotifyExceptionCaughtInSubGroup() {
 ClientGroup subGroup = topLevelGroup.createSubGroup();
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);
 subGroup.add(client1);
 when(client1.getId()).thenReturn("client1");

 Throwable cause = new RuntimeException("test exception");
 assertTrue(topLevelGroup.notifyExceptionCaught(client1, cause));
 verify(handlerMock).exceptionCaught(client1, cause);

 verifyZeroInteractions(subGroup.handler);
 }

 @Test
 public void testIterator() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);

 List<Client> clients = new ArrayList<>();
 clients.add(client1);
 clients.add(client2);

 assertArrayEquals(clients.toArray(), topLevelGroup.iterator().toArray());
 }

 @Test
 public void testContains() {
 topLevelGroup.add(client1);

 assertTrue(topLevelGroup.contains(client1));
 assertFalse(topLevelGroup.contains(client2));
 }

 @Test
 public void testSize() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);

 assertEquals(2, topLevelGroup.size());
 }

 @Test
 public void testAdd() {
 ClientGroup parentGroup = new ClientGroup(topLevelGroup);
 topLevelGroup.add(client1);
 topLevelGroup.addClientHandler(handlerMock);

 assertFalse(topLevelGroup.add(client1));
 assertTrue(topLevelGroup.contains(client1));
 verifyNoMoreInteractions(handlerMock);

 assertTrue(parentGroup.add(client2));
 assertTrue(topLevelGroup.contains(client2));
 verify(handlerMock).clientAdded(client2);
 }

 @Test
 public void testRemove() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 topLevelGroup.addClientHandler(handlerMock);

 assertTrue(topLevelGroup.remove(client1));
 assertFalse(topLevelGroup.contains(client1));
 verify(handlerMock).clientRemoved(client1);

 assertFalse(topLevelGroup.remove(client2));
 assertTrue(topLevelGroup.contains(client2));
 verifyNoMoreInteractions(handlerMock);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = topLevelGroup.createSubGroup();

 assertNotNull(subGroup);
 assertNull(subGroup.parentGroup());
 assertTrue(topLevelGroup.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup subGroup = topLevelGroup.createSubGroup();
 topLevelGroup.addClientHandler(handlerMock);

 assertNull(subGroup.handler);
 assertEquals(handlerMock, topLevelGroup.handler);
 }
}
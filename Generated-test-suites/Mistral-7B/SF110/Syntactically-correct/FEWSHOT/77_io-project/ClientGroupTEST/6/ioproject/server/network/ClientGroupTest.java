package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {

 ClientGroup topGroup;
 ClientGroup subGroup1;
 ClientGroup subGroup2;
 Client client1;
 Client client2;
 Client client3;
 ClientHandler handler;

 @Test
 public void testConstructors() {
 topGroup = new ClientGroup(null);
 assertNull(topGroup.parentGroup());
 assertNull(topGroup.handler);
 assertTrue(topGroup.size() == 0);
 }

 @Test
 public void testAddClient() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.addClientHandler(handler);
 topGroup.add(client1);
 assertTrue(topGroup.contains(client1));
 verify(handler).clientAdded(client1);
 }

 @Test
 public void testAddClientAlreadyPresent() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 topGroup.add(client1);
 topGroup.add(client1);
 assertTrue(topGroup.contains(client1));
 assertTrue(topGroup.size() == 1);
 }

 @Test
 public void testCreateSubGroup() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 assertNotNull(subGroup1.parentGroup());
 assertSame(topGroup, subGroup1.parentGroup());
 assertNull(subGroup1.handler);
 assertTrue(subGroup1.size() == 0);
 }

 @Test
 public void testCreateSubGroupMultipleTimes() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 subGroup2 = topGroup.createSubGroup();
 assertNotNull(subGroup1.parentGroup());
 assertSame(topGroup, subGroup1.parentGroup());
 assertNotNull(subGroup2.parentGroup());
 assertSame(topGroup, subGroup2.parentGroup());
 assertNull(subGroup1.handler);
 assertNull(subGroup2.handler);
 assertTrue(subGroup1.size() == 0);
 assertTrue(subGroup2.size() == 0);
 }

 @Test
 public void testRemoveClient() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 topGroup.add(client1);
 topGroup.add(client2);
 topGroup.remove(client1);
 assertFalse(topGroup.contains(client1));
 assertFalse(topGroup.size() == 1);
 verify(handler).clientRemoved(client1);
 }

 @Test
 public void testRemoveClientNotPresent() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 topGroup.remove(client1);
 assertFalse(topGroup.contains(client1));
 assertFalse(topGroup.size() == 0);
 }

 @Test
 public void testNotifyMessageSentToParentHandler() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(client2);
 topGroup.addClientHandler(handler);
 topGroup.notifyMessageSent(client1, "message");
 verify(handler).messageSent(client1, "message");
 }

 @Test
 public void testNotifyMessageSentToSubGroupHandler() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(subGroup1);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 topGroup.notifyMessageSent(client1, "message");
 verify(handler).messageSent(client2, "message");
 }

 @Test
 public void testNotifyMessageSentToMultipleSubGroups() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 subGroup2 = topGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(subGroup1);
 topGroup.add(subGroup2);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 topGroup.notifyMessageSent(client1, "message");
 verify(handler, times(2)).messageSent(any(Client.class), eq("message"));
 }

 @Test
 public void testNotifyMessageReceivedToParentHandler() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(client2);
 topGroup.addClientHandler(handler);
 topGroup.notifyMessageReceived(client1, "message");
 verify(handler).messageReceived(client1, "message");
 }

 @Test
 public void testNotifyMessageReceivedToSubGroupHandler() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(subGroup1);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 topGroup.notifyMessageReceived(client1, "message");
 verify(handler).messageReceived(client2, "message");
 }

 @Test
 public void testNotifyMessageReceivedToMultipleSubGroups() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 subGroup2 = topGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(subGroup1);
 topGroup.add(subGroup2);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 topGroup.notifyMessageReceived(client1, "message");
 verify(handler, times(2)).messageReceived(any(Client.class), eq("message"));
 }

 @Test
 public void testNotifyExceptionCaughtToParentHandler() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(client2);
 topGroup.addClientHandler(handler);
 topGroup.notifyExceptionCaught(client1, new RuntimeException("Test Exception"));
 verify(handler).exceptionCaught(client1, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroupHandler() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(subGroup1);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 topGroup.notifyExceptionCaught(client1, new RuntimeException("Test Exception"));
 verify(handler).exceptionCaught(client2, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaughtToMultipleSubGroups() {
 topGroup = new ClientGroup(null);
 subGroup1 = topGroup.createSubGroup();
 subGroup2 = topGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 topGroup.add(client1);
 topGroup.add(subGroup1);
 topGroup.add(subGroup2);
 subGroup1.add(client2);
 subGroup1.addClientHandler(handler);
 topGroup.notifyExceptionCaught(client1, new RuntimeException("Test Exception"));
 verify(handler, times(2)).exceptionCaught(any(Client.class), any(Throwable.class));
 }

 @Test
 public void testIterator() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 topGroup.add(client1);
 topGroup.add(client2);
 List<Client> list = new ArrayList<>();
 for (Client c : topGroup) {
 list.add(c);
 }
 assertEquals(2, list.size());
 assertTrue(list.contains(client1));
 assertTrue(list.contains(client2));
 }

 @Test
 public void testSize() {
 topGroup = new ClientGroup(null);
 client1 = new Client();
 topGroup.add(client1);
 assertEquals(1, topGroup.size());
 }
}
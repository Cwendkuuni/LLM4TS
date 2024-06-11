package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

 ClientGroup topLevelGroup;
 ClientGroup subGroup1;
 ClientGroup subGroup2;
 Client client1;
 Client client2;
 Client client3;
 ClientHandler handler;

 @Before
 public void setUp() {
 topLevelGroup = new ClientGroup(null);
 subGroup1 = topLevelGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = mock(ClientHandler.class);
 }

 @Test
 public void testAddClient_ParentGroupDoesntContainClient() {
 assertFalse(topLevelGroup.add(client1));
 }

 @Test
 public void testAddClient_ParentGroupContainsClient() {
 topLevelGroup.add(client1);
 assertTrue(topLevelGroup.add(client1));
 }

 @Test
 public void testAddClient_SubGroupDoesntContainClient() {
 subGroup1.add(client2);
 assertFalse(subGroup1.add(client2));
 }

 @Test
 public void testAddClient_SubGroupContainsClient() {
 subGroup1.add(client2);
 assertTrue(subGroup1.add(client2));
 }

 @Test
 public void testRemoveClient_ClientIsMember() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 topLevelGroup.remove(client1);
 assertFalse(topLevelGroup.contains(client1));
 }

 @Test
 public void testRemoveClient_ClientIsNotMember() {
 topLevelGroup.add(client1);
 topLevelGroup.remove(client2);
 assertFalse(topLevelGroup.contains(client2));
 }

 @Test
 public void testContainsClient_ClientIsMember() {
 topLevelGroup.add(client1);
 assertTrue(topLevelGroup.contains(client1));
 }

 @Test
 public void testContainsClient_ClientIsNotMember() {
 topLevelGroup.add(client1);
 assertFalse(topLevelGroup.contains(client2));
 }

 @Test
 public void testSize_NoClients() {
 assertEquals(0, topLevelGroup.size());
 }

 @Test
 public void testSize_WithClients() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 topLevelGroup.add(client3);
 assertEquals(3, topLevelGroup.size());
 }

 @Test
 public void testIterator_NoClients() {
 List<Client> clients = new ArrayList<>();
 Iterator<Client> iterator = topLevelGroup.iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterator_WithClients() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 topLevelGroup.add(client3);
 List<Client> clients = new ArrayList<>();
 Iterator<Client> iterator = topLevelGroup.iterator();
 assertTrue(iterator.hasNext());
 clients.add(iterator.next());
 assertTrue(clients.get(0).equals(client1));
 assertTrue(iterator.hasNext());
 clients.add(iterator.next());
 assertTrue(clients.get(1).equals(client2));
 assertTrue(iterator.hasNext());
 clients.add(iterator.get(1)); // get instead of next to test iteration after removal
 assertTrue(clients.get(2).equals(client3));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNotifyMessageSent() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 when(handler.messageSent(client1, anyObject())).thenReturn(true);
 assertTrue(topLevelGroup.notifyMessageSent(client1, "message"));
 verify(handler).messageSent(client1, "message");
 }

 @Test
 public void testNotifyMessageSent_NoHandler() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 assertTrue(topLevelGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageSent_ClientNotMember() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 assertFalse(topLevelGroup.notifyMessageSent(client3, "message"));
 }

 @Test
 public void testNotifyMessageSent_SubGroupHandledMessage() {
 subGroup1.addClientHandler(mock(ClientHandler.class));
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 subGroup1.add(client3);
 when(handler.messageSent(client1, anyObject())).thenReturn(false);
 when(subGroup1.handler.messageSent(client3, anyObject())).thenReturn(true);
 assertTrue(topLevelGroup.notifyMessageSent(client3, "message"));
 verify(handler, never()).messageSent(client3, "message");
 }

 @Test
 public void testNotifyMessageReceived() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 when(handler.messageReceived(client1, anyObject())).thenReturn(true);
 assertTrue(topLevelGroup.notifyMessageReceived(client1, "message"));
 verify(handler).messageReceived(client1, "message");
 }

 @Test
 public void testNotifyMessageReceived_NoHandler() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 assertTrue(topLevelGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotMember() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 assertFalse(topLevelGroup.notifyMessageReceived(client3, "message"));
 }

 @Test
 public void testNotifyMessageReceived_SubGroupHandledMessage() {
 subGroup1.addClientHandler(mock(ClientHandler.class));
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 subGroup1.add(client3);
 when(handler.messageReceived(client1, anyObject())).thenReturn(false);
 when(subGroup1.handler.messageReceived(client3, anyObject())).thenReturn(true);
 assertTrue(topLevelGroup.notifyMessageReceived(client3, "message"));
 verify(handler, never()).messageReceived(client3, "message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 when(handler.exceptionCaught(client1, anyObject())).thenReturn(true);
 assertTrue(topLevelGroup.notifyExceptionCaught(client1, new RuntimeException()));
 verify(handler).exceptionCaught(client1, new RuntimeException());
 }

 @Test
 public void testNotifyExceptionCaught_NoHandler() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 assertTrue(topLevelGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotMember() {
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 assertFalse(topLevelGroup.notifyExceptionCaught(client3, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_SubGroupHandledException() {
 subGroup1.addClientHandler(mock(ClientHandler.class));
 topLevelGroup.add(client1);
 topLevelGroup.add(client2);
 subGroup1.add(client3);
 when(handler.exceptionCaught(client1, anyObject())).thenReturn(false);
 when(subGroup1.handler.exceptionCaught(client3, anyObject())).thenReturn(true);
 assertTrue(topLevelGroup.notifyExceptionCaught(client3, new RuntimeException()));
 verify(handler, never()).exceptionCaught(client3, new RuntimeException());
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = topLevelGroup.createSubGroup();
 assertNotNull(subGroup);
 assertFalse(subGroup.parentGroup() == null);
 assertTrue(subGroup.parentGroup() == topLevelGroup);
 }

 @Test
 public void testAddClientHandler() {
 topLevelGroup.addClientHandler(handler);
 assertNotNull(topLevelGroup.handler);
 }
}
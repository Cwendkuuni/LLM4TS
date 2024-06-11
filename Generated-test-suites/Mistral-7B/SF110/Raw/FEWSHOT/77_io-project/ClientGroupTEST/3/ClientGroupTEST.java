package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;
 ClientHandler subHandler;

 @Before
 public void setup() {
 parentGroup = new ClientGroup(null);
 client = mock(Client.class);
 handler = mock(ClientHandler.class);
 subHandler = mock(ClientHandler.class);
 }

 @Test
 public void testAddAndRemoveClient() {
 // Test adding and removing a client
 parentGroup.add(client);
 assertTrue(parentGroup.contains(client));
 parentGroup.remove(client);
 assertFalse(parentGroup.contains(client));
 }

 @Test
 public void testSize() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 when(parentGroup.clients).thenReturn(clients);
 assertEquals(1, parentGroup.size());
 }

 @Test
 public void testContains() {
 when(parentGroup.clients.contains(client)).thenReturn(true);
 assertTrue(parentGroup.contains(client));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = parentGroup.createSubGroup();
 assertEquals(parentGroup, subGroup.parentGroup());
 assertTrue(parentGroup.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test
 public void testAddClientHandler() {
 parentGroup.addClientHandler(handler);
 assertEquals(handler, parentGroup.handler);
 }

 @Test
 public void testNotifyMessageSent() {
 parentGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");
 when(handler.messageSent(client, eq("Test message"))).thenReturn(true);

 assertTrue(parentGroup.notifyMessageSent(client, "Test message"));
 verify(handler).messageSent(client, "Test message");
 }

 @Test
 public void testNotifyMessageSent_NoHandler() {
 parentGroup.add(client);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyMessageSent(client, "Test message"));
 }

 @Test
 public void testNotifyMessageSent_ClientNotInGroup() {
 Client client2 = mock(Client.class);
 when(client2.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyMessageSent(client2, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 parentGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");
 when(handler.messageReceived(client, eq("Test message"))).thenReturn(true);

 assertTrue(parentGroup.notifyMessageReceived(client, "Test message"));
 verify(handler).messageReceived(client, "Test message");
 }

 @Test
 public void testNotifyMessageReceived_NoHandler() {
 parentGroup.add(client);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyMessageReceived(client, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotInGroup() {
 Client client2 = mock(Client.class);
 when(client2.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyMessageReceived(client2, "Test message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 parentGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");
 when(handler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);

 assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
 verify(handler).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaught_NoHandler() {
 parentGroup.add(client);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotInGroup() {
 Client client2 = mock(Client.class);
 when(client2.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyExceptionCaught(client2, new RuntimeException()));
 }

 @Test
 public void testSubGroupsNotification() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 ClientGroup subSubGroup = subGroup.createSubGroup();
 subGroup.addClientHandler(subHandler);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyMessageSent(client, "Test message"));
 verify(handler).messageSent(client, "Test message");
 assertTrue(subGroup.notifyMessageSent(client, "Test message"));
 verify(subHandler).messageSent(client, "Test message");
 }

 @Test
 public void testSubGroupsNotification_NoMessageReceivedInSubGroup() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyMessageSent(client, "Test message"));
 verify(handler).messageSent(client, "Test message");
 assertFalse(subGroup.notifyMessageSent(client, "Test message"));
 assertFalse(subHandler.messageSent(client, "Test message"));
 }

 @Test
 public void testNotifyMessageSent_SubGroupContainsClient() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyMessageSent(client, "Test message"));
 verify(handler).messageSent(client, "Test message");
 assertTrue(subGroup.notifyMessageSent(client, "Test message"));
 verify(subHandler).messageSent(client, "Test message");
 }

 @Test
 public void testNotifyMessageSent_ParentGroupDoesntContainClient() {
 parentGroup.createSubGroup();
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyMessageSent(client, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived_SubGroupContainsClient() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyMessageReceived(client, "Test message"));
 verify(handler).messageReceived(client, "Test message");
 assertTrue(subGroup.notifyMessageReceived(client, "Test message"));
 verify(subHandler).messageReceived(client, "Test message");
 }

 @Test
 public void testNotifyMessageReceived_ParentGroupDoesntContainClient() {
 parentGroup.createSubGroup();
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyMessageReceived(client, "Test message"));
 }

 @Test
 public void testNotifyExceptionCaught_SubGroupContainsClient() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
 verify(handler).exceptionCaught(client, any(Throwable.class));
 assertTrue(subGroup.notifyExceptionCaught(client, new RuntimeException()));
 verify(subHandler).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaught_ParentGroupDoesntContainClient() {
 parentGroup.createSubGroup();
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testSubGroupsMessageSentOrder() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler1);
 subGroup.addClientHandler(handler2);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyMessageSent(client, "Test message"));
 verify(handler1).messageSent(client, "Test message");
 verify(handler2).messageSent(client, "Test message");
 }

 @Test
 public void testSubGroupsExceptionCaughtOrder() {
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.addClientHandler(handler1);
 subGroup.addClientHandler(handler2);
 when(client.getAddress()).thenReturn("127.0.0.1");

 assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException()));
 verify(handler1).exceptionCaught(client, any(Throwable.class));
 verify(handler2).exceptionCaught(client, any(Throwable.class));
 }
}
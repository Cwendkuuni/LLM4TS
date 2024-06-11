package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientAddedEvent;
import ioproject.server.network.ClientGroup.ClientRemovedEvent;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private ClientHandler handler;
 private Set<Client> rootClients;
 private Client client1;
 private Client client2;
 private Client client3;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 rootClients = rootGroup.clients();
 handler = mock(ClientHandler.class);

 client1 = new Client("Client1");
 client2 = new Client("Client2");
 client3 = new Client("Client3");
 }

 @After
 public void tearDown() {
 rootGroup = null;
 subGroup1 = null;
 subGroup2 = null;
 client1 = null;
 client2 = null;
 client3 = null;
 handler = null;
 }

 @Test
 public void testAddClientToRootGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 assertTrue(rootGroup.size() == 2);
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootClients.contains(client1));
 assertTrue(rootClients.contains(client2));

 verify(handler).clientAdded(client1);
 verifyNoMoreInteractions(handler);
 }

 @Test
 public void testAddClientToSubGroup() {
 rootGroup.add(client1);
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client2);

 assertTrue(rootGroup.size() == 1);
 assertTrue(subGroup.size() == 1);
 assertTrue(rootGroup.contains(client1));
 assertTrue(subGroup.contains(client2));
 assertTrue(rootClients.contains(client1));
 assertFalse(rootClients.contains(client2));

 verify(handler).clientAdded(client1);
 verifyNoMoreInteractions(handler);
 }

 @Test
 public void testNotifyMessageSentToRootGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 rootGroup.notifyMessageSent(client1, "Test message");

 assertTrue(rootGroup.notifyMessageSent(client1, "Test message"));
 assertTrue(subGroup1.notifyMessageSent(client1, "Test message"));
 assertTrue(subGroup2.notifyMessageSent(client1, "Test message"));
 assertTrue(handler.messageSentCalled());
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 rootGroup.add(client1);
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client2);
 rootGroup.addClientHandler(handler);

 subGroup.notifyMessageSent(client2, "Test message");

 assertFalse(rootGroup.notifyMessageSent(client2, "Test message"));
 assertTrue(subGroup.notifyMessageSent(client2, "Test message"));
 assertTrue(handler.messageSentCalled());
 }

 @Test
 public void testNotifyMessageReceivedToRootGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 rootGroup.notifyMessageReceived(client1, "Test message");

 assertTrue(rootGroup.notifyMessageReceived(client1, "Test message"));
 assertTrue(subGroup1.notifyMessageReceived(client1, "Test message"));
 assertTrue(subGroup2.notifyMessageReceived(client1, "Test message"));
 assertTrue(handler.messageReceivedCalled());
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 rootGroup.add(client1);
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client2);
 rootGroup.addClientHandler(handler);

 subGroup.notifyMessageReceived(client2, "Test message");

 assertFalse(rootGroup.notifyMessageReceived(client2, "Test message"));
 assertTrue(subGroup.notifyMessageReceived(client2, "Test message"));
 assertTrue(handler.messageReceivedCalled());
 }

 @Test
 public void testNotifyExceptionCaughtToRootGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception"));

 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception")));
 assertTrue(subGroup1.notifyExceptionCaught(client1, new RuntimeException("Test exception")));
 assertTrue(subGroup2.notifyExceptionCaught(client1, new RuntimeException("Test exception")));
 assertTrue(handler.exceptionCaughtCalled());
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 rootGroup.add(client1);
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client2);
 rootGroup.addClientHandler(handler);

 rootGroup.notifyExceptionCaught(client2, new RuntimeException("Test exception"));

 assertFalse(rootGroup.notifyExceptionCaught(client2, new RuntimeException("Test exception")));
 assertTrue(subGroup.notifyExceptionCaught(client2, new RuntimeException("Test exception")));
 assertTrue(handler.exceptionCaughtCalled());
 }

 @Test
 public void testRemoveClientFromRootGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 rootGroup.remove(client1);

 assertFalse(rootGroup.contains(client1));
 assertTrue(rootGroup.size() == 1);
 assertFalse(rootClients.contains(client1));
 assertTrue(rootClients.contains(client2));

 verify(handler).clientRemoved(client1);
 }

 @Test
 public void testRemoveClientFromSubGroup() {
 rootGroup.add(client1);
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client2);
 subGroup.add(client3);

 subGroup.remove(client2);

 assertFalse(subGroup.contains(client2));
 assertTrue(subGroup.size() == 1);
 assertFalse(rootClients.contains(client2));
 assertTrue(rootClients.contains(client1));
 assertTrue(rootClients.contains(client3));

 verify(handler).clientRemoved(client2);
 }

 @Test
 public void testClientAddedEvent() {
 rootGroup.add(client1);
 ClientAddedEvent event = new ClientAddedEvent(client1, rootGroup);

 assertTrue(rootGroup.add(client1));
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootClients.contains(client1));

 assertEquals(event.getClient(), client1);
 assertEquals(event.getGroup(), rootGroup);
 assertTrue(handler.clientAddedCalled());
 }

 @Test
 public void testClientRemovedEvent() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 ClientRemovedEvent event = new ClientRemovedEvent(client1, rootGroup);

 assertTrue(rootGroup.remove(client1));
 assertFalse(rootGroup.contains(client1));
 assertFalse(rootClients.contains(client1));

 assertEquals(event.getClient(), client1);
 assertEquals(event.getGroup(), rootGroup);
 assertTrue(handler.clientRemovedCalled());
 }
}
package ioproject.server.network.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.WeakReference;

import ioproject.common.network.ClientHandler;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupIterator;

public class ClientGroupTest {

 private ClientGroup rootClientGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootClientGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 subGroup1 = rootClientGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 System.out.println("Client added: " + client);
 }

 @Override
 public void clientRemoved(Client client) {
 System.out.println("Client removed: " + client);
 }

 @Override
 public void messageReceived(Client client, Object message) {
 System.out.println("Message received by handler from client: " + client + " with message: " + message);
 }

 @Override
 public void messageSent(Client client, Object message) {
 System.out.println("Message sent by handler to client: " + client + " with message: " + message);
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 System.out.println("Exception caught by handler from client: " + client + " with message: " + cause.getMessage());
 }
 };
 rootClientGroup.addClientHandler(handler);
 rootClientGroup.add(client1);
 rootClientGroup.add(client2);
 rootGroup1.add(client3);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = rootClientGroup.parentGroup();
 Assert.assertNull(parentGroup);
 }

 @Test
 public void testNotifyMessageSent_Valid() {
 rootClientGroup.notifyMessageSent(client1, "message1");
 rootGroup1.notifyMessageSent(client1, "message2");
 rootClientGroup.notifyMessageSent(client2, "message3");
 rootGroup1.notifyMessageSent(client2, "message4");
 subGroup1.notifyMessageSent(client3, "message5");

 Assert.assertEquals(Arrays.asList("Message sent by handler to client: client1 with message: message1",
 "Message sent by handler to client: client1 with message: message2",
 "Message sent by handler to client: client2 with message: message3",
 "Message sent by handler to client: client2 with message: message4",
 "Message sent by handler to client: client3 with message: message5"), handler.getSentMessages());
 }

 @Test
 public void testNotifyMessageSent_Invalid() {
 rootClientGroup.notifyMessageSent(client4, "message");

 // client4 is not in the group, so no message should be sent
 Assert.assertTrue(handler.getSentMessages().isEmpty());
 }

 @Test
 public void testNotifyMessageReceived_Valid() {
 rootClientGroup.notifyMessageReceived(client1, "message1");
 rootGroup1.notifyMessageReceived(client1, "message2");
 rootClientGroup.notifyMessageReceived(client2, "message3");
 rootGroup1.notifyMessageReceived(client2, "message4");
 subGroup1.notifyMessageReceived(client3, "message5");

 Assert.assertEquals(Arrays.asList("Message received by handler from client: client1 with message: message1",
 "Message received by handler from client: client1 with message: message2",
 "Message received by handler from client: client2 with message: message3",
 "Message received by handler from client: client2 with message: message4",
 "Message received by handler from client: client3 with message: message5"), handler.getReceivedMessages());
 }

 @Test
 public void testNotifyMessageReceived_Invalid() {
 rootClientGroup.notifyMessageReceived(client4, "message");

 // client4 is not in the group, so no message should be received
 Assert.assertTrue(handler.getReceivedMessages().isEmpty());
 }

 @Test
 public void testNotifyExceptionCaught_Valid() {
 rootClientGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception"));
 rootGroup1.notifyExceptionCaught(client1, new RuntimeException("Test exception"));
 rootClientGroup.notifyExceptionCaught(client2, new RuntimeException("Test exception"));
 subGroup1.notifyExceptionCaught(client3, new RuntimeException("Test exception"));

 Assert.assertEquals(Arrays.asList("Exception caught by handler from client: client1 with message: Test exception",
 "Exception caught by handler from client: client1 with message: Test exception",
 "Exception caught by handler from client: client2 with message: Test exception",
 "Exception caught by handler from client: client3 with message: Test exception"), handler.getExceptionMessages());
 }

 @Test
 public void testNotifyExceptionCaught_Invalid() {
 rootClientGroup.notifyExceptionCaught(client4, new RuntimeException("Test exception"));

 // client4 is not in the group, so no exception should be caught
 Assert.assertTrue(handler.getExceptionMessages().isEmpty());
 }

 @Test
 public void testContains() {
 Assert.assertTrue(rootClientGroup.contains(client1));
 Assert.assertTrue(rootClientGroup.contains(client2));
 Assert.assertFalse(rootClientGroup.contains(client4));
 }

 @Test
 public void testSize() {
 Assert.assertEquals(3, rootClientGroup.size());
 }

 @Test
 public void testAdd() {
 Client newClient = new Client("newClient");
 Assert.assertFalse(rootClientGroup.add(newClient));
 Assert.assertFalse(rootClientGroup.contains(newClient));
 }

 @Test
 public void testAdd_Valid() {
 Client newClient = new Client("newClient");
 Assert.assertTrue(rootClientGroup.add(newClient));
 Assert.assertTrue(rootClientGroup.contains(newClient));
 Assert.assertEquals(4, rootClientGroup.size());
 }

 @Test
 public void testRemove() {
 Assert.assertTrue(rootClientGroup.remove(client1));
 Assert.assertFalse(rootClientGroup.contains(client1));
 Assert.assertEquals(3, rootClientGroup.size());
 }

 @Test
 public void testRemove_Invalid() {
 Assert.assertFalse(rootClientGroup.remove(client4));
 Assert.assertFalse(rootClientGroup.contains(client4));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup newSubGroup = rootClientGroup.createSubGroup();
 Assert.assertNotNull(newSubGroup);
 }

 private List<String> getSentMessages() {
 return new ArrayList<>(handler.getSentMessages());
 }

 private List<String> getReceivedMessages() {
 return new ArrayList<>(handler.getReceivedMessages());
 }

 private List<String> getExceptionMessages() {
 return new ArrayList<>(handler.getExceptionMessages());
 }
}
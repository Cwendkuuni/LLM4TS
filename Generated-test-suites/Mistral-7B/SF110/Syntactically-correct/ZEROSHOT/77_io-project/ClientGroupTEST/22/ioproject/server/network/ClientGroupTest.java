package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientGroupHandler;

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
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = new ClientGroupHandler() {
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
 System.out.println("Message received: " + client + ", " + message);
 }

 @Override
 public void messageSent(Client client, Object message) {
 System.out.println("Message sent: " + client + ", " + message);
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 System.out.println("Exception caught: " + client + ", " + cause);
 }
 };
 rootGroup.addClientHandler(handler);
 subGroup1.addClientHandler(handler);
 subGroup2.addClientHandler(handler);
 rootGroup.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.contains(client3));
 assertTrue(subGroup1.contains(client2));
 assertFalse(subGroup1.contains(client1));
 assertFalse(subGroup1.contains(client3));
 assertFalse(subGroup2.contains(client1));
 assertFalse(subGroup2.contains(client2));
 assertFalse(subGroup2.contains(client3));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 assertEquals(2, subGroup1.size());
 assertEquals(1, subGroup2.size());
 }

 @Test
 public void testAddClient() {
 Client client4 = new Client("client4");
 assertTrue(rootGroup.add(client4));
 assertTrue(rootGroup.contains(client4));
 assertTrue(subGroup1.contains(client4));
 assertTrue(subGroup2.contains(client4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateClient() {
 assertFalse(rootGroup.add(client1));
 }

 @Test
 public void testRemoveClient() {
 Client client5 = new Client("client5");
 rootGroup.add(client5);
 assertTrue(rootGroup.remove(client5));
 assertFalse(rootGroup.contains(client5));
 assertFalse(subGroup1.contains(client5));
 assertFalse(subGroup2.contains(client5));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup3 = rootGroup.createSubGroup();
 assertNotNull(subGroup3);
 assertTrue(subGroup3.containsAll(subGroup2));
 }

 @Test
 public void testNotifyMessageSent() {
 Object message = new Object();
 client1.sendMessageToServer(message);
 assertTrue(rootGroup.notifyMessageSent(client1, message));
 assertTrue(handler.isMessageSentCalled);
 handler.isMessageSentCalled = false;
 assertTrue(subGroup1.notifyMessageSent(client1, message));
 assertTrue(handler.isMessageSentCalled);
 handler.isMessageSentCalled = false;
 assertTrue(subGroup2.notifyMessageSent(client1, message));
 assertTrue(handler.isMessageSentCalled);
 handler.isMessageSentCalled = false;
 }

 @Test
 public void testNotifyMessageReceived() {
 Object message = new Object();
 client1.receiveMessageFromServer(message);
 assertTrue(rootGroup.notifyMessageReceived(client1, message));
 assertTrue(handler.isMessageReceivedCalled);
 handler.isMessageReceivedCalled = false;
 assertTrue(subGroup1.notifyMessageReceived(client1, message));
 assertTrue(handler.isMessageReceivedCalled);
 handler.isMessageReceivedCalled = false;
 assertTrue(subGroup2.notifyMessageReceived(client1, message));
 assertTrue(handler.isMessageReceivedCalled);
 handler.isMessageReceivedCalled = false;
 }

 @Test
 public void testNotifyExceptionCaught() {
 client1.sendExceptionToServer(new RuntimeException());
 assertTrue(rootGroup.notifyExceptionCaught(client1, client1.getException()));
 assertTrue(handler.isExceptionCaughtCalled);
 handler.isExceptionCaughtCalled = false;
 assertTrue(subGroup1.notifyExceptionCaught(client1, client1.getException()));
 assertTrue(handler.isExceptionCaughtCalled);
 handler.isExceptionCaughtCalled = false;
 assertTrue(subGroup2.notifyExceptionCaught(client1, client1.getException()));
 assertTrue(handler.isExceptionCaughtCalled);
 handler.isExceptionCaughtCalled = false;
 }

 @Test
 public void testIterator() {
 Set<Client> set = new HashSet<Client>();
 for (Client client : rootGroup) {
 set.add(client);
 }
 assertEquals(3, set.size());
 }

 @Test
 public void testListAllClients() {
 List<Client> list = new ArrayList<Client>();
 rootGroup.forEachClient(new ClientConsumer() {
 @Override
 public void accept(Client client) {
 list.add(client);
 }
 });
 assertEquals(3, list.size());
 }
}
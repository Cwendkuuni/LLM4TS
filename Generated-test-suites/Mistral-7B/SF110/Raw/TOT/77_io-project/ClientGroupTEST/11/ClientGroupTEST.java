package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientGroup subGroup;
 private ClientHandler handler;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 subGroup = rootGroup.createSubGroup();
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // do nothing
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // do nothing
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // do nothing
 }

 @Override
 public void clientAdded(Client client) {
 // do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup.add(client3);
 rootGroup.add(subGroup);
 }

 @Test
 public void testNotifyMessageSent() {
 // Test client in root group
 rootGroup.notifyMessageSent(client1, "test message");
 assertTrue(handler.isMessageSentCalled);

 // Test client in subgroup
 subGroup.notifyMessageSent(client3, "test message");
 assertTrue(handler.isMessageSentCalled);

 // Test client not in group
 assertFalse(rootGroup.notifyMessageSent(new Client("client4"), "test message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 // Test client in root group
 rootGroup.notifyMessageReceived(client1, "test message");
 assertTrue(handler.isMessageReceivedCalled);

 // Test client in subgroup
 subGroup.notifyMessageReceived(client3, "test message");
 assertTrue(handler.isMessageReceivedCalled);

 // Test client not in group
 assertFalse(rootGroup.notifyMessageSent(new Client("client4"), "test message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 // Test client in root group
 rootGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled);

 // Test client in subgroup
 subGroup.notifyExceptionCaught(client3, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled);

 // Test null client
 rootGroup.notifyExceptionCaught(null, new RuntimeException());
 assertFalse(handler.isExceptionCaughtCalled);
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.contains(client3));
 assertFalse(rootGroup.contains(new Client("client4")));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 }

 @Test
 public void testAddClient() {
 Client newClient = new Client("newClient");
 rootGroup.add(newClient);
 assertTrue(rootGroup.contains(newClient));
 }

 @Test
 public void testRemoveClient() {
 rootGroup.remove(client1);
 assertFalse(rootGroup.contains(client1));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup newSubGroup = rootGroup.createSubGroup();
 assertTrue(newSubGroup.contains(client3));
 }

 @Test
 public void testAddClientHandler() {
 ClientHandler newHandler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // do nothing
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // do nothing
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // do nothing
 }

 @Override
 public void clientAdded(Client client) {
 // do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(newHandler);
 assertTrue(newHandler.isMessageSentCalled);
 }
}

class Client {
 private String name;

 public Client(String name) {
 this.name = name;
 }

 public boolean equals(Object o) {
 return this.name.equals(o.toString());
 }
}

class ClientHandler {
 private boolean isMessageSentCalled = false;
 private boolean isMessageReceivedCalled = false;
 private boolean isExceptionCaughtCalled = false;
 private boolean isClientAddedCalled = false;
 private boolean isClientRemovedCalled = false;

 @Override
 public void messageSent(Client client, Object message) {
 isMessageSentCalled = true;
 }

 @Override
 public void messageReceived(Client client, Object message) {
 isMessageReceivedCalled = true;
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 isExceptionCaughtCalled = true;
 }

 @Override
 public void clientAdded(Client client) {
 isClientAddedCalled = true;
 }

 @Override
 public void clientRemoved(Client client) {
 isClientRemovedCalled = true;
 }
}
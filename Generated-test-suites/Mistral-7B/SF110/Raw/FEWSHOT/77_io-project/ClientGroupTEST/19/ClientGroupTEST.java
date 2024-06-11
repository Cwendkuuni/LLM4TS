package ioproject.server.network;

import java.util.*;
import org.junit.Assert.*;
import ioproject.common.network.*;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 ClientGroup grandSubGroup;
 Client client1;
 Client client2;
 Client client3;
 Client client4;
 ClientHandler handler;
 ClientHandler subGroupHandler;
 ClientHandler grandSubGroupHandler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 subGroup = parentGroup.createSubGroup();
 grandSubGroup = subGroup.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 client4 = new Client("client4");
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // TODO: Implement
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // TODO: Implement
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // TODO: Implement
 }

 @Override
 public void clientAdded(Client client) {
 // TODO: Implement
 }

 @Override
 public void clientRemoved(Client client) {
 // TODO: Implement
 }
 };
 subGroupHandler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // TODO: Implement
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // TODO: Test notifyMessageReceived in sub-group
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // TODO: Implement
 }

 @Override
 public void clientAdded(Client client) {
 // TODO: Implement
 }

 @Override
 public void clientRemoved(Client client) {
 // TODO: Implement
 }
 };
 grandSubGroupHandler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // TODO: Test notifyMessageSent in grand-sub-group
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // TODO: Implement
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // TODO: Implement
 }

 @Override
 public void clientAdded(Client client) {
 // TODO: Implement
 }

 @Override
 public void clientRemoved(Client client) {
 // TODO: Implement
 }
 };
 parentGroup.addClientHandler(handler);
 subGroup.addClientHandler(subGroupHandler);
 grandSubGroup.addClientHandler(grandSubGroupHandler);
 }

 @After
 public void tearDown() {
 parentGroup = null;
 subGroup = null;
 grandSubGroup = null;
 client1 = null;
 client2 = null;
 client3 = null;
 client4 = null;
 handler = null;
 subGroupHandler = null;
 grandSubGroupHandler = null;
 }

 @Test
 public void testAddAndRemoveClient() {
 // Test adding and removing a client from the root group
 parentGroup.add(client1);
 assertTrue(parentGroup.contains(client1));
 parentGroup.remove(client1);
 assertFalse(parentGroup.contains(client1));
 // Test adding and removing a client from a sub-group
 subGroup.add(client2);
 assertTrue(subGroup.contains(client2));
 subGroup.remove(client2);
 assertFalse(subGroup.contains(client2));
 // Test adding and removing a client from a grand-sub-group
 grandSubGroup.add(client3);
 assertTrue(grandSubGroup.contains(client3));
 grandSubGroup.remove(client3);
 assertFalse(grandSubGroup.contains(client3));
 }

 @Test
 public void testNotifyMessageSent() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client3);
 subGroup.add(client4);
 handler.messageSent(client1, "message1");
 assertTrue(parentGroup.notifyMessageSent(client1, "message1"));
 assertTrue(subGroup.notifyMessageSent(client1, "message1"));
 assertTrue(subGroup.notifyMessageSent(client2, "message1"));
 assertTrue(subGroup.notifyMessageSent(client3, "message1"));
 assertTrue(subGroup.notifyMessageSent(client4, "message1"));
 // Test notifyMessageSent in a sub-group
 subGroupHandler.messageReceived(client3, "message2");
 assertTrue(grandSubGroup.notifyMessageSent(client3, "message2"));
 }

 @Test
 public void testNotifyMessageReceived() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client3);
 subGroup.add(client4);
 client1.sendMessage("message1", "client2");
 assertTrue(parentGroup.notifyMessageReceived(client1, "message1"));
 assertTrue(subGroup.notifyMessageReceived(client1, "message1"));
 assertTrue(subGroup.notifyMessageReceived(client2, "message1"));
 assertTrue(subGroup.notifyMessageReceived(client3, "message1"));
 assertTrue(subGroup.notifyMessageReceived(client4, "message1"));
 // Test notifyMessageReceived in a sub-group
 client3.sendMessage("message3", "client4");
 subGroupHandler.messageReceived(client3, "message3");
 assertTrue(grandSubGroup.notifyMessageReceived(client3, "message3"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client3);
 subGroup.add(client4);
 client1.sendException(new Exception("Exception1"));
 assertTrue(parentGroup.notifyExceptionCaught(client1, (Exception) client1.getException()));
 assertTrue(subGroup.notifyExceptionCaught(client1, (Exception) client1.getException()));
 assertTrue(subGroup.notifyExceptionCaught(client2, (Exception) client1.getException()));
 assertTrue(subGroup.notifyExceptionCaught(client3, (Exception) client1.getException()));
 assertTrue(subGroup.notifyExceptionCaught(client4, (Exception) client1.getException()));
 // Test notifyExceptionCaught in a sub-group
 client3.sendException(new Exception("Exception3"));
 subGroupHandler.exceptionCaught(client3, (Exception) client3.getException());
 assertTrue(grandSubGroup.notifyExceptionCaught(client3, (Exception) client3.getException()));
 }

 @Test
 public void testSizeAndContains() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client3);
 subGroup.add(client4);
 grandSubGroup.add(client3);
 assertTrue(parentGroup.size() == 3);
 assertTrue(parentGroup.contains(client1));
 assertTrue(parentGroup.contains(client2));
 assertTrue(parentGroup.contains(client3));
 assertTrue(subGroup.size() == 4);
 assertTrue(subGroup.contains(client1));
 assertTrue(subGroup.contains(client2));
 assertTrue(subGroup.contains(client3));
 assertTrue(subGroup.contains(client4));
 assertTrue(grandSubGroup.size() == 1);
 assertTrue(grandSubGroup.contains(client3));
 }

 @Test
 public void testIterator() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client3);
 subGroup.add(client4);
 List<Client> clients = new ArrayList<Client>();
 for (Client c : parentGroup) {
 clients.add(c);
 }
 assertTrue(clients.size() == 3);
 assertTrue(clients.get(0) == client1);
 assertTrue(clients.get(1) == client2);
 assertTrue(clients.get(2) == client3);
 clients.clear();
 for (Client c : subGroup) {
 clients.add(c);
 }
 assertTrue(clients.size() == 4);
 assertTrue(clients.get(0) == client1);
 assertTrue(clients.get(1) == client2);
 assertTrue(clients.get(2) == client3);
 assertTrue(clients.get(3) == client4);
 }
}
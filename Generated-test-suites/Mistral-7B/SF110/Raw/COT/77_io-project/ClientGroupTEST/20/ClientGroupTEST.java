package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1, client2, client3;
 private ClientHandler clientHandler;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 clientHandler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 System.out.println("Client added: " + client);
 }

 @Override
 public void clientRemoved(Client client) {
 System.out.println("Client removed: " + client);
 }

 @Override
 public void messageSent(Client client, Object message) {
 System.out.println("Message sent from: " + client + ", to: " + this);
 }

 @Override
 public void messageReceived(Client client, Object message) {
 System.out.println("Message received by: " + client + ", from: " + this);
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 System.out.println("Exception caught by: " + client + ", cause: " + cause);
 }
 };
 rootGroup.addClientHandler(clientHandler);
 rootGroup.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(subGroup1.contains(client2));
 assertTrue(subGroup2.contains(client3));
 assertFalse(rootGroup.contains(client2));
 assertFalse(subGroup1.contains(client3));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 assertEquals(1, subGroup1.size());
 assertEquals(1, subGroup2.size());
 }

 @Test
 public void testAddAndRemove() {
 assertFalse(rootGroup.contains(client2));
 assertFalse(subGroup1.contains(client3));

 rootGroup.add(client2);
 subGroup1.add(client3);

 assertTrue(rootGroup.contains(client2));
 assertTrue(subGroup1.contains(client3));

 rootGroup.remove(client2);
 subGroup1.remove(client3);

 assertFalse(rootGroup.contains(client2));
 assertFalse(subGroup1.contains(client3));
 }

 @Test
 public void testNotifyMessageSent() {
 client1.sendMessage("parent", "message");
 assertTrue(clientHandler.getClass().isInstance(rootGroup.handler));
 assertTrue(clientHandler.getClass().isInstance(subGroup1.handler));
 assertTrue(clientHandler.getClass().isInstance(subGroup2.handler));

 assertFalse(client1.getMessageHandler().hasNewMessage());
 assertFalse(client2.getMessageHandler().hasNewMessage());
 assertFalse(client3.getMessageHandler().hasNewMessage());

 assertEquals(1, clientHandler.getNumMessagesSent());
 assertEquals(0, clientHandler.getNumMessagesSentToSubGroups());
 assertEquals(1, clientHandler.getNumMessagesSentToParent());
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageSentToNonExistentClient() {
 clientHandler.setNumMessagesSent(0);
 clientHandler.setNumMessagesSentToSubGroups(0);
 clientHandler.setNumMessagesSentToParent(0);

 rootGroup.notifyMessageSent(new Client("nonExistentClient"), "message");
 }

 @Test
 public void testNotifyMessageReceived() {
 client1.sendMessage("parent", "message");
 client2.sendMessage("child", "message");
 client3.sendMessage("grandChild", "message");
 client1.getMessageHandler().clear();
 client2.getMessageHandler().clear();
 client3.getMessageHandler().clear();

 assertFalse(client1.getMessageHandler().hasNewMessage());
 assertFalse(client2.getMessageHandler().hasNewMessage());
 assertFalse(client3.getMessageHandler().hasNewMessage());

 assertEquals(1, clientHandler.getNumMessagesSent());
 assertEquals(1, clientHandler.getNumMessagesSentToSubGroups());
 assertEquals(0, clientHandler.getNumMessagesSentToParent());
 assertEquals(2, client1.getMessageHandler().getNumNewMessages());
 assertEquals(1, client2.getMessageHandler().getNumNewMessages());
 assertEquals(1, client3.getMessageHandler().getNumNewMessages());
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageReceivedToNonExistentClient() {
 clientHandler.setNumMessagesSent(0);
 clientHandler.setNumMessagesSentToSubGroups(0);
 clientHandler.setNumMessagesSentToParent(0);
 client1.getMessageHandler().clear();
 client2.getMessageHandler().clear();
 client3.getMessageHandler().clear();

 rootGroup.notifyMessageReceived(new Client("nonExistentClient"), "message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 client1.sendException(new Exception("testException"));
 client2.sendException(new Exception("testException"));
 client3.sendException(new Exception("testException"));
 client1.getExceptionHandler().clear();
 client2.getExceptionHandler().clear();
 client3.getExceptionHandler().clear();

 assertFalse(client1.getExceptionHandler().hasNewException());
 assertFalse(client2.getExceptionHandler().hasNewException());
 assertFalse(client3.getExceptionHandler().hasNewException());

 assertEquals(1, clientHandler.getNumExceptionsCaught());
 assertEquals(1, clientHandler.getNumExceptionsCaughtInSubGroups());
 assertEquals(0, clientHandler.getNumExceptionsCaughtByParent());
 assertEquals(2, client1.getExceptionHandler().getNumNewExceptions());
 assertEquals(1, client2.getExceptionHandler().getNumNewExceptions());
 assertEquals(1, client3.getExceptionHandler().getNumNewExceptions());
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyExceptionCaughtToNonExistentClient() {
 clientHandler.setNumExceptionsCaught(0);
 clientHandler.setNumExceptionsCaughtInSubGroups(0);
 clientHandler.setNumExceptionsCaughtByParent(0);
 client1.getExceptionHandler().clear();
 client2.getExceptionHandler().clear();
 client3.getExceptionHandler().clear();

 rootGroup.notifyExceptionCaught(new Client("nonExistentClient"), new Exception("testException"));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup newSubGroup = rootGroup.createSubGroup();
 assertTrue(newSubGroup.getParentGroup() == rootGroup);
 assertFalse(newSubGroup.getParentGroup() == subGroup1);
 assertFalse(newSubGroup.getParentGroup() == subGroup2);

 assertEquals(4, rootGroup.size());
 assertEquals(1, subGroup1.size());
 assertEquals(1, subGroup2.size());
 assertEquals(1, newSubGroup.size());
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup newSubGroup = rootGroup.createSubGroup();
 ClientGroup newSubSubGroup = newSubGroup.createSubGroup();
 rootGroup.addClientHandler(new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 fail("Should not be called");
 }

 @Override
 public void clientRemoved(Client client) {
 fail("Should not be called");
 }

 @Override
 public void messageSent(Client client, Object message) {
 System.out.println("Message sent from parent to handler: " + message);
 }

 @Override
 public void messageReceived(Client client, Object message) {
 System.out.println("Message received by handler from client: " + client + ", message: " + message);
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 System.out.println("Exception caught by handler from client: " + client + ", cause: " + cause);
 }
 });
 newSubSubGroup.addClientHandler(new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 fail("Should not be called");
 }

 @Override
 public void clientRemoved(Client client) {
 fail("Should not be called");
 }

 @Override
 public void messageSent(Client client, Object message) {
 System.out.println("Message sent from grandchild to handler: " + message);
 }

 @Override
 public void messageReceived(Client client, Object message) {
 System.out.println("Message received by handler from client: " + client + ", message: " + message);
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 System.out.println("Exception caught by handler from client: " + client + ", cause: " + cause);
 }
 });
 client1.sendMessage("parent", "messageFromParent");
 client2.sendMessage("child", "messageFromChild");
 client3.sendMessage("grandChild", "messageFromGrandChild");
 clientHandler.setNumMessagesSent(0);
 clientHandler.setNumMessagesSentToSubGroups(0);
 clientHandler.setNumMessagesSentToParent(0);
 client1.getMessageHandler().clear();
 client2.getMessageHandler().clear();
 client3.getMessageHandler().clear();

 assertEquals(1, clientHandler.getNumMessagesSent());
 assertEquals(1, clientHandler.getNumMessagesSentToSubGroups());
 assertEquals(1, clientHandler.getNumMessagesSentToParent());

 assertEquals(1, newSubGroup.handler.getNumMessagesSent());
 assertEquals(1, newSubSubGroup.handler.getNumMessagesSent());

 assertEquals(2, client1.getMessageHandler().getNumNewMessages());
 assertEquals(1, client2.getMessageHandler().getNumNewMessages());
 assertEquals(1, client3.getMessageHandler().getNumNewMessages());
 }
}
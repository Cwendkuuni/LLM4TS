package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.ClientHandler;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupImpl; // Assuming ClientGroupImpl is used for instantiation

public class ClientGroupTest {

 private ClientGroup root;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 root = new ClientGroupImpl(null);
 subGroup1 = root.createSubGroup();
 subGroup2 = root.createSubGroup();
 client1 = new Client("Client1");
 client2 = new Client("Client2");
 client3 = new Client("Client3");
 handler = new DummyClientHandler();
 root.addClientHandler(handler);
 root.add(client1);
 subGroup1.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 }

 @Test
 public void testAddClient() {
 assertTrue(root.add(client1));
 assertTrue(root.size() == 2);
 assertTrue(root.contains(client1));
 assertTrue(subGroup1.size() == 2);
 assertTrue(subGroup1.contains(client1));
 }

 @Test
 public void testAddClientAlreadyInGroup() {
 assertFalse(root.add(client1));
 assertFalse(root.size() == 3);
 assertTrue(root.contains(client1));
 }

 @Test
 public void testNotifyMessageSent() {
 Object message = new Object();
 assertTrue(root.notifyMessageSent(client1, message));
 assertTrue(subGroup1.notifyMessageSent(client1, message));
 assertTrue(handler.messageSentCallCount == 1);
 handler.messageSentCallCount = 0;
 assertFalse(subGroup2.notifyMessageSent(client1, message));
 }

 @Test
 public void testNotifyMessageSentToNullClient() {
 Object message = new Object();
 assertFalse(root.notifyMessageSent(null, message));
 assertFalse(handler.messageSentCallCount > 0);
 }

 @Test
 public void testNotifyMessageReceived() {
 Object message = new Object();
 assertTrue(root.notifyMessageReceived(client1, message));
 assertTrue(subGroup1.notifyMessageReceived(client1, message));
 assertTrue(handler.messageReceivedCallCount == 1);
 handler.messageReceivedCallCount = 0;
 assertFalse(subGroup2.notifyMessageReceived(client1, message));
 }

 @Test
 public void testNotifyMessageReceivedToNullClient() {
 Object message = new Object();
 assertFalse(root.notifyMessageReceived(null, message));
 assertFalse(handler.messageReceivedCallCount > 0);
 }

 @Test
 public void testNotifyExceptionCaught() {
 Throwable cause = new RuntimeException("Exception caught");
 assertTrue(root.notifyExceptionCaught(client1, cause));
 assertTrue(subGroup1.notifyExceptionCaught(client1, cause));
 assertTrue(handler.exceptionCaughtCallCount == 1);
 handler.exceptionCaughtCallCount = 0;
 assertFalse(subGroup2.notifyExceptionCaught(client1, cause));
 }

 @Test
 public void testNotifyExceptionCaughtToNullClient() {
 Throwable cause = new RuntimeException("Exception caught");
 assertFalse(root.notifyExceptionCaught(null, cause));
 assertFalse(handler.exceptionCaughtCallCount > 0);
 }

 @Test
 public void testRemoveClient() {
 Set<Client> clientsBeforeRemoval = new HashSet<Client>(root.iterator());
 root.remove(client1);
 Set<Client> clientsAfterRemoval = new HashSet<Client>(root.iterator());
 assertTrue(clientsBeforeRemoval.size() == 3);
 assertTrue(clientsAfterRemoval.size() == 2);
 assertFalse(root.contains(client1));
 assertFalse(subGroup1.contains(client1));
 assertFalse(subGroup2.contains(client1));
 }

 @Test
 public void testRemoveNonExistentClient() {
 Set<Client> clientsBeforeRemoval = new HashSet<Client>(root.iterator());
 root.remove(client4);
 Set<Client> clientsAfterRemoval = new HashSet<Client>(root.iterator());
 assertTrue(clientsBeforeRemoval.size() == 3);
 assertTrue(clientsAfterRemoval.size() == 3);
 assertFalse(root.contains(client4));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = root.createSubGroup();
 assertTrue(subGroup.size() == 0);
 }

 @Test
 public void testCreateSubGroupWithParent() {
 ClientGroup subGroup = root.createSubGroup();
 assertTrue(subGroup.parentGroup() == root);
 }

 @Test
 public void testAddClientToSubGroup() {
 ClientGroup subGroup = root.createSubGroup();
 subGroup.add(client2);
 assertTrue(subGroup.size() == 1);
 assertTrue(subGroup.contains(client2));
 assertTrue(root.size() == 3);
 assertTrue(root.contains(client2));
 }

 @Test
 public void testRemoveClientFromSubGroup() {
 ClientGroup subGroup = root.createSubGroup();
 subGroup.add(client2);
 subGroup.remove(client2);
 assertTrue(subGroup.size() == 0);
 }

 @Test
 public void testClientHandlerCallBacks() {
 client1.setHandler(handler);
 root.add(client1);
 Object message = new Object();
 root.notifyMessageSent(client1, message);
 assertEquals("messageSent", handler.messageSentCalls[0].getMessage());
 root.notifyMessageReceived(client1, message);
 assertEquals("messageReceived", handler.messageReceivedCalls[0].getMessage());
 root.notifyExceptionCaught(client1, new RuntimeException("Test Exception"));
 assertEquals("exceptionCaught", handler.exceptionCaughtCalls[0].getMessage());
 }
}

class DummyClientHandler implements ClientHandler {
 String messageSentCalls[] = new String[10];
 String messageReceivedCalls[] = new String[10];
 String exceptionCaughtCalls[] = new String[10];
 int messageSentCallCount = 0;
 int messageReceivedCallCount = 0;
 int exceptionCaughtCallCount = 0;

 @Override
 public void messageSent(Client sender, Object message) {
 messageSentCalls[messageSentCallCount++] = "messageSent";
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 messageReceivedCalls[messageReceivedCallCount++] = "messageReceived";
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 exceptionCaughtCalls[exceptionCaughtCallCount++] = "exceptionCaught";
 }
}
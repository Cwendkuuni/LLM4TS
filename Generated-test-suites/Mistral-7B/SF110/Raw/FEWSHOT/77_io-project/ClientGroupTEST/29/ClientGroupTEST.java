package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 ClientGroup grandSubGroup;
 Client client1;
 Client client2;
 Client client3;
 ClientHandler handler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 subGroup = parentGroup.createSubGroup();
 grandSubGroup = subGroup.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = MockClientHandler.getInstance();
 }

 @Test
 public void testAddAndRemoveClient() {
 // test adding a client to parent group
 assertTrue(parentGroup.add(client1));
 assertEquals(1, parentGroup.size());
 // test adding a client to sub group
 assertTrue(subGroup.add(client1));
 assertEquals(1, subGroup.size());
 // test adding a client to grand sub group
 assertTrue(grandSubGroup.add(client1));
 assertEquals(1, grandSubGroup.size());
 // test removing a client from parent group
 assertTrue(parentGroup.remove(client1));
 assertEquals(0, parentGroup.size());
 // test removing a client from sub group
 assertTrue(subGroup.remove(client1));
 assertEquals(0, subGroup.size());
 // test removing a client from grand sub group
 assertTrue(grandSubGroup.remove(client1));
 assertEquals(0, grandSubGroup.size());
 // test adding the same client again to parent group
 assertFalse(parentGroup.add(client1));
 // test adding the same client again to sub group
 assertFalse(subGroup.add(client1));
 // test adding the same client again to grand sub group
 assertFalse(grandSubGroup.add(client1));
 }

 @Test
 public void testAddClientToMultipleGroups() {
 // add client to parent group
 assertTrue(parentGroup.add(client2));
 // add client to sub group
 assertTrue(subGroup.add(client2));
 // test adding the same client to grand sub group
 assertTrue(grandSubGroup.add(client2));
 // test removing client from parent group
 assertTrue(parentGroup.remove(client2));
 // test removing client from sub group
 assertTrue(subGroup.remove(client2));
 // test removing client from grand sub group
 assertTrue(grandSubGroup.remove(client2));
 }

 @Test
 public void testNotifyMessageSent() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client1);
 handler.setMessageSentListener(new MessageSentListener() {
 @Override
 public void messageSent(final Client client, final Object message) {
 assertEquals(client1, client);
 fail("Unexpected message sent");
 }
 });
 parentGroup.notifyMessageSent(client1, "test message");
 }

 @Test
 public void testNotifyMessageReceived() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client1);
 handler.setMessageReceivedListener(new MessageReceivedListener() {
 @Override
 public void messageReceived(final Client client, final Object message) {
 assertEquals(client1, client);
 fail("Unexpected message received");
 }
 });
 parentGroup.notifyMessageReceived(client1, "test message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 subGroup.add(client1);
 handler.setExceptionCaughtListener(new ExceptionCaughtListener() {
 @Override
 public void exceptionCaught(final Client client, final Throwable cause) {
 assertEquals(client1, client);
 fail("Unexpected exception caught");
 }
 });
 client1.setException(new RuntimeException("Test exception"));
 parentGroup.notifyExceptionCaught(client1, client1.getException());
 }

 @Test
 public void testMessageSentToHandler() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 handler.setMessageSentListener(new MessageSentListener() {
 @Override
 public void messageSent(final Client client, final Object message) {
 assertEquals(client1, client);
 assertEquals("test message", message.toString());
 }
 });
 parentGroup.notifyMessageSent(client1, "test message");
 }

 @Test
 public void testMessageReceivedToHandler() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 handler.setMessageReceivedListener(new MessageReceivedListener() {
 @Override
 public void messageReceived(final Client client, final Object message) {
 assertEquals(client1, client);
 assertEquals("test message", message.toString());
 }
 });
 parentGroup.notifyMessageReceived(client1, "test message");
 }

 @Test
 public void testExceptionCaughtToHandler() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 handler.setExceptionCaughtListener(new ExceptionCaughtListener() {
 @Override
 public void exceptionCaught(final Client client, final Throwable cause) {
 assertEquals(client1, client);
 assertTrue(cause instanceof RuntimeException);
 }
 });
 client1.setException(new RuntimeException("Test exception"));
 parentGroup.notifyExceptionCaught(client1, client1.getException());
 }

 @Test
 public void testIterator() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 Iterator<Client> iterator = parentGroup.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client1, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(client2, iterator.next());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSize() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 assertEquals(2, parentGroup.size());
 }

 @Test
 public void testContains() {
 parentGroup.add(client1);
 parentGroup.add(client2);
 assertTrue(parentGroup.contains(client1));
 assertTrue(parentGroup.contains(client2));
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup subGroup = parentGroup.createSubGroup();
 Client client = new Client("client");
 ClientHandler handler = MockClientHandler.getInstance();
 parentGroup.addClientHandler(handler);
 subGroup.addClientHandler(handler);
 client.setHandler(handler);
 parentGroup.add(client);
 subGroup.add(client);
 assertTrue(handler.getClientAddedCallCount() > 0);
 assertTrue(handler.getClientRemovedCallCount() == 0);
 }

 @Test
 public void testRemoveClientHandler() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup subGroup = parentGroup.createSubGroup();
 Client client = new Client("client");
 ClientHandler handler = MockClientHandler.getInstance();
 parentGroup.addClientHandler(handler);
 subGroup.addClientHandler(handler);
 client.setHandler(handler);
 parentGroup.removeClientHandler(handler);
 subGroup.removeClientHandler(handler);
 assertTrue(handler.getClientAddedCallCount() == 0);
 assertTrue(handler.getClientRemovedCallCount() > 0);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup subGroup = parentGroup.createSubGroup();
 assertNotNull(subGroup);
 }
}
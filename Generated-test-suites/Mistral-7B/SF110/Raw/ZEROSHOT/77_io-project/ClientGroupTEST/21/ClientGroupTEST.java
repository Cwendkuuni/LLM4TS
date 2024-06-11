package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandler_messageReceived_arg_1;
import ioproject.server.network.ClientGroup.ClientHandler_messageSent_arg_1_arg_2;
import ioproject.server.network.ClientGroup.ClientHandler_exceptionCaught_arg_1_arg_2;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 group = new ClientGroup(null);
 client = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 ClientHandler_messageSent_arg_1_arg_2 messageSent = new ClientHandler_messageSent_arg_1_arg_2();
 messageSent.setSender(sender);
 messageSent.setMessage(message);
 // perform assertions here
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 ClientHandler_messageReceived_arg_1 arg1 = new ClientHandler_messageReceived_arg_1();
 arg1.setSender(sender);
 arg1.setMessage(message);
 // perform assertions here
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 ClientHandler_exceptionCaught_arg_1_arg_2 exceptionCaught = new ClientHandler_exceptionCaught_arg_1_arg_2();
 exceptionCaught.setSender(sender);
 exceptionCaught.setCause(cause);
 // perform assertions here
 }
 };
 group.addClientHandler(handler);
 group.add(client);
 group.add(client2);
 group.add(client3);
 }

 @After
 public void tearDown() {
 group = null;
 client = null;
 client2 = null;
 client3 = null;
 handler = null;
 }

 @Test
 public void testNotifyMessageSent() {
 group.notifyMessageSent(client, "test message");
 assertTrue(handler.isMessageSentCalled());
 handler.reset();
 group.notifyMessageSent(client3, "test message");
 assertTrue(handler.isMessageSentCalled());
 handler.reset();
 group.notifyMessageSent(client, "test message");
 assertTrue(handler.isMessageSentCalled());
 handler.reset();
 ClientGroup subGroup = group.createSubGroup();
 subGroup.notifyMessageSent(client2, "test message");
 assertTrue(handler.isMessageSentCalled());
 handler.reset();
 }

 @Test
 public void testNotifyMessageReceived() {
 group.notifyMessageReceived(client, "test message");
 assertTrue(handler.isMessageReceivedCalled());
 handler.reset();
 group.notifyMessageReceived(client3, "test message");
 assertTrue(handler.isMessageReceivedCalled());
 handler.reset();
 group.notifyMessageReceived(client, "test message");
 assertTrue(handler.isMessageReceivedCalled());
 handler.reset();
 ClientGroup subGroup = group.createSubGroup();
 subGroup.notifyMessageReceived(client2, "test message");
 assertTrue(handler.isMessageReceivedCalled());
 handler.reset();
 }

 @Test
 public void testNotifyExceptionCaught() {
 try {
 throw new RuntimeException("test exception");
 } catch (RuntimeException e) {
 group.notifyExceptionCaught(client, e);
 assertTrue(handler.isExceptionCaughtCalled());
 handler.reset();
 }
 group.notifyExceptionCaught(client3, new RuntimeException("test exception"));
 assertTrue(handler.isExceptionCaughtCalled());
 handler.reset();
 group.notifyExceptionCaught(client, new RuntimeException("test exception"));
 assertTrue(handler.isExceptionCaughtCalled());
 handler.reset();
 ClientGroup subGroup = group.createSubGroup();
 subGroup.notifyExceptionCaught(client2, new RuntimeException("test exception"));
 assertTrue(handler.isExceptionCaughtCalled());
 handler.reset();
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new LinkedHashSet<Client>();
 clients.add(client);
 clients.add(client2);
 clients.add(client3);
 Iterator<Client> iterator = group.iterator();
 assertEquals(clients.iterator().next(), iterator.next());
 assertEquals(clients.iterator().next(), iterator.next());
 assertEquals(clients.iterator().next(), iterator.next());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testContains() {
 assertTrue(group.contains(client));
 assertFalse(group.contains(new Client("client4")));
 }

 @Test
 public void testSize() {
 assertEquals(group.size(), 3);
 }

 @Test
 public void testAdd() {
 Client client4 = new Client("client4");
 group.add(client4);
 assertTrue(group.contains(client4));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddThrowException() {
 Client client4 = new Client("client4");
 group.clients.add(client4);
 }

 @Test
 public void testRemove() {
 assertTrue(group.contains(client));
 group.remove(client);
 assertFalse(group.contains(client));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveThrowException() {
 group.clients.remove(client);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 assertTrue(subGroup.parentGroup() == group);
 }
}
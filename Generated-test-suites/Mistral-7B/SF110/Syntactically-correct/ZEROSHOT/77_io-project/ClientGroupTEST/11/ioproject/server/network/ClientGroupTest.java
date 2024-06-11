package ioproject.server.network.test;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1, client2, client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 // no-op
 }

 @Override
 public void clientRemoved(Client client) {
 // no-op
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // no-op
 }

 @Override
 public void messageSent(Client sender, Object message) {
 // no-op
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // no-op
 }
 };
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 rootGroup.add(client2);
 }

 @Test
 public void testSize() {
 assertEquals(2, rootGroup.size());
 }

 @Test
 public void testAdd() {
 assertFalse(rootGroup.add(client3));
 assertEquals(2, rootGroup.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnmodifiableIterator() {
 final Iterator<Client> iterator = rootGroup.iterator();
 iterator.remove();
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testNotifyMessageSent() {
 client3 = new Client("client3");
 rootGroup.add(client3);
 rootGroup.notifyMessageSent(client1, "testMessage");
 assertFalse(handler.isMessageSentCalled);
 rootGroup.addClientHandler(new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 handler.isMessageSentCalled = true;
 }
 });
 rootGroup.notifyMessageSent(client1, "testMessage");
 assertTrue(handler.isMessageSentCalled);
 }

 @Test
 public void testNotifyMessageReceived() {
 rootGroup.notifyMessageReceived(client1, "testMessage");
 assertFalse(handler.isMessageReceivedCalled);
 rootGroup.addClientHandler(new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 handler.isMessageReceivedCalled = true;
 }
 });
 rootGroup.notifyMessageReceived(client1, "testMessage");
 assertTrue(handler.isMessageReceivedCalled);
 }

 @Test
 public void testNotifyExceptionCaught() {
 rootGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertFalse(handler.isExceptionCaughtCalled);
 rootGroup.addClientHandler(new ClientHandler() {
 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 handler.isExceptionCaughtCalled = true;
 }
 });
 rootGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled);
 }

 @Test
 public void testCreateSubGroup() {
 final ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(subGroup);
 assertTrue(rootGroup.contains(subGroup));
 }

 @Test
 public void testRemove() {
 rootGroup.remove(client1);
 assertFalse(rootGroup.contains(client1));
 assertEquals(2, rootGroup.size());
 handler.isMessageSentCalled = false;
 handler.isMessageReceivedCalled = false;
 rootGroup.notifyMessageSent(client2, "testMessage");
 assertFalse(handler.isMessageSentCalled);
 rootGroup.notifyMessageReceived(client2, "testMessage");
 assertFalse(handler.isMessageReceivedCalled);
 }
}
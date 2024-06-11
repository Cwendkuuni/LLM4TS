package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerInterface;

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
 client1 = new Client();
 client2 = new Client();
 client3 = new Client();
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = rootGroup.createSubGroup();
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // handle messageSent event
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // handle messageReceived event
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // handle exceptionCaught event
 }

 @Override
 public void clientAdded(Client client) {
 // handle clientAdded event
 }

 @Override
 public void clientRemoved(Client client) {
 // handle clientRemoved event
 }
 };
 rootGroup.addClientHandler(handler);

 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup1.add(client3);
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 assertEquals(1, subGroup1.size());
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new HashSet<Client>(Arrays.asList(client1, client2, client3));
 Iterator<Client> iterator = rootGroup.iterator();
 assertEquals(clients, new HashSet<Client>(Arrays.asList(iterator.next(), iterator.next(), iterator.next())));
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.contains(client3));
 assertFalse(rootGroup.contains(new Client()));
 }

 @Test
 public void testAdd() {
 assertTrue(rootGroup.add(client3));
 assertFalse(rootGroup.add(client1)); // already a member
 }

 @Test
 public void testRemove() {
 assertFalse(rootGroup.remove(client3)); // not a member
 assertTrue(subGroup1.remove(client3));
 assertTrue(rootGroup.remove(client1));
 }

 @Test
 public void testCreateSubGroup() {
 assertNotNull(subGroup1.parentGroup());
 assertSame(rootGroup, subGroup1.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 client1.sendMessage("testMessage");
 assertTrue(rootGroup.notifyMessageSent(client1, "testMessage"));
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 client3.sendMessage("testMessage");
 subGroup1.notifyMessageSent(client1, "testMessage");
 assertTrue(handler.wasMessageSentCalled);
 }

 @Test
 public void testNotifyMessageReceived() {
 client1.sendMessage("testMessage");
 assertTrue(rootGroup.notifyMessageReceived(client1, "testMessage"));
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 client3.sendMessage("testMessage");
 subGroup1.notifyMessageReceived(client1, "testMessage");
 assertTrue(handler.wasMessageReceivedCalled);
 }

 @Test
 public void testNotifyExceptionCaught() {
 client1.sendException("testException");
 assertTrue(rootGroup.notifyExceptionCaught(client1, new Exception("testException")));
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 client3.sendException("testException");
 subGroup1.notifyExceptionCaught(client1, new Exception("testException"));
 assertTrue(handler.wasExceptionCaughtCalled);
 }

 @Test
 public void testClientAdded() {
 Client client4 = new Client();
 rootGroup.add(client4);
 assertTrue(handler.wasClientAddedCalled);
 }

 @Test
 public void testClientRemoved() {
 rootGroup.remove(client1);
 assertTrue(handler.wasClientRemovedCalled);
 }
}
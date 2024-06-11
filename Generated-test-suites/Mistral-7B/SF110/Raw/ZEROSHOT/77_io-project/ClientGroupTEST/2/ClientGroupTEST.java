package ioproject.server.network.test;

import ioproject.common.network.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 //Do something
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 //Do something
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 //Do something
 }

 @Override
 public void clientAdded(Client client) {
 //Do something
 }

 @Override
 public void clientRemoved(Client client) {
 //Do something
 }
 };
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 }

 @Test
 public void testParentGroup() {
 assertEquals(rootGroup, rootGroup.parentGroup());
 assertTrue(rootGroup.parentGroup() == null || rootGroup.parentGroup().contains(client1));
 }

 @Test
 public void testNotifyMessageSent() {
 assertFalse(rootGroup.notifyMessageSent(client1, "testMessage"));
 assertTrue(subGroup1.notifyMessageSent(client2, "testMessage"));
 assertTrue(subGroup2.notifyMessageSent(client3, "testMessage"));
 assertFalse(handler.isMessageSentCalled);
 rootGroup.notifyMessageSent(client1, "testMessage");
 assertTrue(handler.isMessageSentCalled);
 }

 @Test
 public void testNotifyMessageReceived() {
 assertFalse(rootGroup.notifyMessageReceived(client1, "testMessage"));
 assertTrue(subGroup1.notifyMessageReceived(client2, "testMessage"));
 assertTrue(subGroup2.notifyMessageReceived(client3, "testMessage"));
 assertFalse(handler.isMessageReceivedCalled);
 rootGroup.notifyMessageReceived(client1, "testMessage");
 assertTrue(handler.isMessageReceivedCalled);
 }

 @Test(expected = AssertionError.class)
 public void testNotifyExceptionCaughtWithNullClient() {
 rootGroup.notifyExceptionCaught(null, new AssertionError());
 }

 @Test
 public void testNotifyExceptionCaught() {
 assertFalse(rootGroup.notifyExceptionCaught(client1, new AssertionError()));
 assertTrue(subGroup1.notifyExceptionCaught(client2, new AssertionError()));
 assertTrue(subGroup2.notifyExceptionCaught(client3, new AssertionError()));
 assertFalse(handler.isExceptionCaughtCalled);
 rootGroup.notifyExceptionCaught(client1, new AssertionError());
 assertTrue(handler.isExceptionCaughtCalled);
 }

 @Test
 public void testIterator() {
 List<Client> clients = new ArrayList<>();
 clients.add(client1);
 clients.add(client2);
 clients.add(client3);
 Iterator<Client> iterator = rootGroup.iterator();
 while (iterator.hasNext()) {
 Client next = iterator.next();
 assertTrue(clients.contains(next));
 }
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(subGroup1.contains(client2));
 assertTrue(subGroup2.contains(client3));
 assertFalse(rootGroup.contains(client2));
 assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 assertEquals(1, subGroup1.size());
 assertEquals(1, subGroup2.size());
 }

 @Test
 public void testAdd() {
 assertFalse(rootGroup.contains(client3));
 assertFalse(subGroup1.contains(client3));
 assertFalse(subGroup2.contains(client3));
 rootGroup.add(client3);
 assertTrue(rootGroup.contains(client3));
 assertFalse(subGroup1.contains(client3));
 assertFalse(subGroup2.contains(client3));
 }

 @Test
 public void testRemove() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(subGroup1.contains(client2));
 assertTrue(subGroup2.contains(client3));
 rootGroup.remove(client1);
 assertFalse(rootGroup.contains(client1));
 assertFalse(subGroup1.contains(client1));
 assertFalse(subGroup2.contains(client1));
 }

 @Test
 public void testCreateSubGroup() {
 assertNotSame(rootGroup, subGroup1);
 assertNotSame(rootGroup, subGroup2);
 assertSame(rootGroup, subGroup1.parentGroup());
 assertSame(subGroup1, subGroup2.parentGroup());
 }

 @Test
 public void testAddClientHandler() {
 assertNull(rootGroup.handler);
 rootGroup.addClientHandler(handler);
 assertSame(handler, rootGroup.handler);
 }

}
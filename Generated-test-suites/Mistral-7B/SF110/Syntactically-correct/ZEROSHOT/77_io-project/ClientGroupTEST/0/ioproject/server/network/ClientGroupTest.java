package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerInterface;

public class ClientGroupTest {

 /** ClientHandlerMock for testing */
 static class ClientHandlerMock implements ClientHandlerInterface {
  boolean messageSentCalled = false;
  boolean messageReceivedCalled = false;
  boolean exceptionCaughtCalled = false;

  @Override
  public void messageSent(Client client, Object message) {
    messageSentCalled = true;
  }

  @Override
  public void messageReceived(Client client, Object message) {
    messageReceivedCalled = true;
  }

  @Override
  public void exceptionCaught(Client client, Throwable cause) {
    exceptionCaughtCalled = true;
  }
 }

 ClientGroup clientGroup;
 Client client1, client2, client3;
 ClientGroup subGroup1, subGroup2;
 ClientHandlerMock clientHandlerMock;

 @Before
 public void setUp() {
  clientGroup = new ClientGroup(null);
  clientHandlerMock = new ClientHandlerMock();
  clientGroup.addClientHandler(clientHandlerMock);
  client1 = new Client();
  client2 = new Client();
  client3 = new Client();
 }

 @Test
 public void testAddClientToEmptyGroup() {
  assertTrue(clientGroup.add(client1));
  assertEquals(1, clientGroup.size());
  assertNotNull(clientGroup.iterator().next());
 }

 @Test
 public void testAddClientAlreadyPresent() {
  clientGroup.add(client1);
  assertFalse(clientGroup.add(client1));
  assertEquals(1, clientGroup.size());
 }

 @Test
 public void testAddClientToParentGroupAndSubGroup() {
  clientGroup.add(client1);
  ClientGroup subGroup = clientGroup.createSubGroup();
  subGroup.add(client1);
  assertEquals(2, clientGroup.size());
  assertEquals(1, subGroup.size());
 }

 @Test
 public void testAddClientToParentGroupAndSubGroupWithDifferentClients() {
  clientGroup.add(client1);
  ClientGroup subGroup = clientGroup.createSubGroup();
  subGroup.add(client2);
  assertEquals(2, clientGroup.size());
  assertEquals(1, subGroup.size());
 }

 @Test
 public void testContainsClient() {
  clientGroup.add(client1);
  assertTrue(clientGroup.contains(client1));
 }

 @Test
 public void testContainsClientNotPresent() {
  clientGroup.add(client1);
  assertFalse(clientGroup.contains(client2));
 }

 @Test
 public void testRemoveClientFromEmptyGroup() {
  assertFalse(clientGroup.remove(client1));
  assertEquals(0, clientGroup.size());
 }

 @Test
 public void testRemoveClientFromGroup() {
  clientGroup.add(client1);
  clientGroup.add(client2);
  assertTrue(clientGroup.remove(client1));
  assertEquals(1, clientGroup.size());
  assertFalse(clientGroup.contains(client1));
 }

 @Test
 public void testRemoveClientFromSubGroup() {
  clientGroup.add(client1);
  ClientGroup subGroup = clientGroup.createSubGroup();
  subGroup.add(client1);
  assertTrue(subGroup.remove(client1));
  assertEquals(0, subGroup.size());
  assertFalse(subGroup.contains(client1));
 }

 @Test
 public void testSize() {
  assertEquals(0, clientGroup.size());
  clientGroup.add(client1);
  assertEquals(1, clientGroup.size());
 }

 @Test
 public void testNotifyMessageSent() {
  clientGroup.add(client1);
  clientGroup.add(client2);
  clientGroup.add(client3);
  clientGroup.add(subGroup1 = clientGroup.createSubGroup());
  clientGroup.add(subGroup2 = clientGroup.createSubGroup());
  clientGroup.add(subGroup1.createSubGroup());
  clientGroup.add(subGroup2.createSubGroup());
  clientHandlerMock.messageSentCalled = false;
  clientHandlerMock.messageReceivedCalled = false;
  clientHandlerMock.exceptionCaughtCalled = false;

  clientGroup.notifyMessageSent(client1, "testMessage");
  assertTrue(clientGroup.notifyMessageSent(client1, "testMessage"));
  assertTrue(clientHandlerMock.messageSentCalled);

  clientHandlerMock.messageSentCalled = false;
  clientGroup.notifyMessageSent(client1, "testMessage");
  for (final ClientGroup group : clientGroup.subGroups()) {
    assertTrue(group.notifyMessageSent(client1, "testMessage"));
  }
  assertTrue(clientHandlerMock.messageSentCalled);

  clientHandlerMock.messageSentCalled = false;
  clientGroup.notifyMessageSent(client2, "testMessage");
  assertFalse(clientGroup.notifyMessageSent(client1, "testMessage"));
  assertFalse(clientHandlerMock.messageSentCalled);
 }

 @Test
 public void testNotifyMessageReceived() {
  clientGroup.add(client1);
  clientGroup.add(client2);
  clientGroup.add(client3);
  clientGroup.add(subGroup1 = clientGroup.createSubGroup());
  clientGroup.add(subGroup2 = clientGroup.createSubGroup());
  clientGroup.add(subGroup1.createSubGroup());
  clientGroup.add(subGroup2.createSubGroup());
  clientHandlerMock.messageSentCalled = false;
  clientHandlerMock.messageReceivedCalled = false;
  clientHandlerMock.exceptionCaughtCalled = false;

  clientGroup.notifyMessageReceived(client1, "testMessage");
  assertTrue(clientGroup.notifyMessageReceived(client1, "testMessage"));
  assertTrue(clientHandlerMock.messageReceivedCalled);

  clientHandlerMock.messageReceivedCalled = false;
  clientGroup.notifyMessageReceived(client1, "testMessage");
  for (final ClientGroup group : clientGroup.subGroups()) {
    assertTrue(group.notifyMessageReceived(client1, "testMessage"));
  }
  assertTrue(clientHandlerMock.messageReceivedCalled);

  clientHandlerMock.messageReceivedCalled = false;
  clientGroup.notifyMessageReceived(client2, "testMessage");
  assertFalse(clientGroup.notifyMessageReceived(client1, "testMessage"));
  assertFalse(clientHandlerMock.messageReceivedCalled);
 }

 @Test
 public void testNotifyExceptionCaught() {
  clientGroup.add(client1);
  clientGroup.add(client2);
  clientGroup.add(client3);
  clientGroup.add(subGroup1 = clientGroup.createSubGroup());
  clientGroup.add(subGroup2 = clientGroup.createSubGroup());
  clientGroup.add(subGroup1.createSubGroup());
  clientGroup.add(subGroup2.createSubGroup());
  clientHandlerMock.messageSentCalled = false;
  clientHandlerMock.messageReceivedCalled = false;
  clientHandlerMock.exceptionCaughtCalled = false;

  clientGroup.notifyExceptionCaught(client1, new RuntimeException("testException"));
  assertTrue(clientGroup.notifyExceptionCaught(client1, new RuntimeException("testException")));
  assertTrue(clientHandlerMock.exceptionCaughtCalled);

  clientHandlerMock.exceptionCaughtCalled = false;
  clientGroup.notifyExceptionCaught(client1, new RuntimeException("testException"));
  for (final ClientGroup group : clientGroup.subGroups()) {
    assertTrue(group.notifyExceptionCaught(client1, new RuntimeException("testException")));
  }
  assertTrue(clientHandlerMock.exceptionCaughtCalled);

  clientHandlerMock.exceptionCaughtCalled = false;
  clientGroup.notifyExceptionCaught(client2, new RuntimeException("testException"));
  assertFalse(clientGroup.notifyExceptionCaught(client1, new RuntimeException("testException")));
  assertFalse(clientHandlerMock.exceptionCaughtCalled);
 }

 @Test
 public void testIterator() {
  clientGroup.add(client1);
  clientGroup.add(client2);
  clientGroup.add(client3);
  Iterator<Client> iterator = clientGroup.iterator();
  ArrayList<Client> clients = new ArrayList<>();
  while (iterator.hasNext()) {
    clients.add(iterator.next());
  }
  Set<Client> set = new HashSet<>(clients);
  assertEquals(Collections.unmodifiableSet(clientGroup.clients), set);
 }

 @After
 public void tearDown() {
  clientGroup = null;
 }

}
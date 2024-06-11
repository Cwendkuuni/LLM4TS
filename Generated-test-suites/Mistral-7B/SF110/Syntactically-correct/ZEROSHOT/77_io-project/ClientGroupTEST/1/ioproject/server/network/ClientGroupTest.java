package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerNotFoundException;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private Client client4;
 private ClientHandler handler;
 private ClientGroup subGroup;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 client4 = new Client("client4");
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // Do nothing
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // Do nothing
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // Do nothing
 }

 @Override
 public void clientAdded(Client client) {
 // Do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // Do nothing
 }
 };
 rootGroup.addClientHandler(handler);
 }

 @Test
 public void testClientGroupConstruction() {
 ClientGroup group = new ClientGroup(rootGroup);
 assertEquals(0, group.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClientGroupNullParentGroup() {
 new ClientGroup(null);
 }

 @Test
 public void testAddClient() {
 rootGroup.add(client1);
 assertEquals(1, rootGroup.size());
 assertTrue(rootGroup.contains(client1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateClient() {
 rootGroup.add(client1);
 rootGroup.add(client1);
 }

 @Test
 public void testRemoveClient() {
 rootGroup.add(client1);
 rootGroup.remove(client1);
 assertFalse(rootGroup.contains(client1));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveNonExistentClient() {
 rootGroup.remove(client1);
 }

 @Test
 public void testSubGroupCreation() {
 subGroup = rootGroup.createSubGroup();
 assertEquals(0, subGroup.size());
 assertFalse(rootGroup.contains(subGroup));
 subGroup.add(client2);
 assertTrue(rootGroup.contains(client2));
 }

 @Test
 public void testMessageSent() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 rootGroup.notifyMessageSent(client1, "test message");
 assertTrue(client1.isMessageReceived());
 assertTrue(client2.isMessageReceived());
 assertTrue(client3.isMessageReceived());
 assertTrue(client4.isMessageReceived());
 }

 @Test
 public void testMessageReceived() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 rootGroup.notifyMessageReceived(client1, "test message");
 assertTrue(client1.isMessageReceived());
 assertTrue(client2.isMessageReceived());
 assertTrue(client3.isMessageReceived());
 assertTrue(client4.isMessageReceived());
 }

 @Test
 public void testExceptionCaught() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 rootGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(client1.isExceptionCaught());
 assertTrue(client2.isExceptionCaught());
 assertTrue(client3.isExceptionCaught());
 assertTrue(client4.isExceptionCaught());
 }

 @Test(expected = ClientHandlerNotFoundException.class)
 public void testMessageSentToNullHandler() {
 rootGroup.add(client1);
 rootGroup.removeClientHandler();
 rootGroup.notifyMessageSent(client1, "test message");
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSameClientToSubGroup() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client1);
 subGroup.add(client1);
 }

 @Test
 public void testContains() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.contains(client3));
 assertTrue(rootGroup.contains(client4));
 }

 @Test
 public void testSize() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 assertEquals(4, rootGroup.size());
 }

 @Test
 public void testClear() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 rootGroup.clear();
 assertEquals(0, rootGroup.size());
 }

 @Test
 public void testIterator() {
 rootGroup.addAll(Arrays.asList(client1, client2, client3, client4));
 Set<Client> clients = new LinkedHashSet<Client>(Arrays.asList(client1, client2, client3, client4));
 Iterator<Client> iterator = rootGroup.iterator();
 ArrayList<Client> testList = new ArrayList<Client>();
 while (iterator.hasNext()) {
 testList.add(iterator.next());
 }
 assertEquals(clients, testList);
 }

 @Test
 public void testSubGroupIterator() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);
 rootGroup.add(client4);
 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.addAll(Arrays.asList(client2, client3));
 Set<Client> clients = new LinkedHashSet<Client>(Arrays.asList(client2, client3));
 Iterator<Client> iterator = subGroup.iterator();
 ArrayList<Client> testList = new ArrayList<Client>();
 while (iterator.hasNext()) {
 testList.add(iterator.next());
 }
 assertEquals(clients, testList);
 }

 @Test
 public void testNestedSubGroups() {
 rootGroup.add(client1);
 ClientGroup subGroup1 = rootGroup.createSubGroup();
 subGroup1.add(client2);
 ClientGroup subGroup2 = subGroup1.createSubGroup();
 subGroup2.add(client3);
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.contains(client3));
 }
}
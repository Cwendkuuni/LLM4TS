package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerWrapper;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1, client2, client3;
 private ClientGroup subGroup;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup = rootGroup.createSubGroup();
 subGroup.add(client3);
 }

 @Test
 public void testAddClient() {
 ClientGroup parent = rootGroup;
 Client client = new Client("testing client");
 boolean added = parent.add(client);
 assertTrue(added);
 assertTrue(parent.contains(client));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddClientAlreadyExists() {
 ClientGroup parent = rootGroup;
 boolean added = parent.add(client1); // should return false, but this test might fail if the order of adding clients is not consistent
 added = parent.add(client1);
 fail("Client should not be added twice to the same group.");
 }

 @Test
 public void testRemoveClient() {
 ClientGroup parent = rootGroup;
 boolean removed = parent.remove(client1);
 assertTrue(removed);
 assertFalse(parent.contains(client1));

 // check that client is removed from all subgroups as well
 for (ClientGroup subgroup : rootGroup.getSubGroups()) {
 assertFalse(subgroup.contains(client1));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentClient() {
 ClientGroup parent = rootGroup;
 boolean removed = parent.remove(client4);
 fail("Client that does not exist should not be removed.");
 }

 @Test
 public void testContainsClient() {
 ClientGroup parent = rootGroup;
 assertTrue(parent.contains(client1));
 assertTrue(parent.contains(client2));
 assertFalse(parent.contains(client4));

 // check that client is contained in all subgroups as well
 for (ClientGroup subgroup : rootGroup.getSubGroups()) {
 assertTrue(subgroup.contains(client3));
 }
 }

 @Test
 public void testSize() {
 ClientGroup parent = rootGroup;
 assertEquals(3, parent.size());
 }

 @Test
 public void testSubGroups() {
 ClientGroup parent = rootGroup;
 assertEquals(1, parent.getSubGroups().size());
 }

 @Test
 public void testNotifyMessageSent() {
 ClientHandlerWrapper handler = new ClientHandlerWrapper();
 parent.addClientHandler(handler);
 client1.setHandler(handler);
 parent.notifyMessageSent(client1, "test message");
 assertTrue(handler.messageSentCalled);
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 ClientHandlerWrapper subgroupHandler = new ClientHandlerWrapper();
 subGroup.addClientHandler(subgroupHandler);
 client3.setHandler(subgroupHandler);
 parent.notifyMessageSent(client1, "test message");
 assertTrue(subgroupHandler.messageSentCalled);
 }

 @Test
 public void testNotifyMessageReceived() {
 ClientHandlerWrapper handler = new ClientHandlerWrapper();
 parent.addClientHandler(handler);
 client1.setHandler(handler);
 parent.notifyMessageReceived(client1, "test message");
 assertTrue(handler.messageReceivedCalled);
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 ClientHandlerWrapper subgroupHandler = new ClientHandlerWrapper();
 subGroup.addClientHandler(subgroupHandler);
 client3.setHandler(subgroupHandler);
 parent.notifyMessageReceived(client1, "test message");
 assertTrue(subgroupHandler.messageReceivedCalled);
 }

 @Test
 public void testNotifyExceptionCaught() {
 ClientHandlerWrapper handler = new ClientHandlerWrapper();
 parent.addClientHandler(handler);
 client1.setHandler(handler);
 parent.notifyExceptionCaught(client1, new RuntimeException("test exception"));
 assertTrue(handler.exceptionCaughtCalled);
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 ClientHandlerWrapper subgroupHandler = new ClientHandlerWrapper();
 subGroup.addClientHandler(subgroupHandler);
 client3.setHandler(subgroupHandler);
 parent.notifyExceptionCaught(client1, new RuntimeException("test exception"));
 assertTrue(subgroupHandler.exceptionCaughtCalled);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup parent = rootGroup;
 ClientGroup subgroup = parent.createSubGroup();
 assertTrue(parent.getSubGroups().contains(subgroup));
 }

 @Test
 public void testCreateSubGroupTwice() {
 ClientGroup parent = rootGroup;
 ClientGroup subgroup1 = parent.createSubGroup();
 ClientGroup subgroup2 = parent.createSubGroup();
 assertEquals(subgroup1, subgroup2);
 }

 @Test
 public void testGetSubGroups() {
 ClientGroup parent = rootGroup;
 Set<ClientGroup> subGroups = new ArrayList<ClientGroup>();
 subGroups.add(subGroup);
 assertEquals(subGroups, parent.getSubGroups());
 }

 @Test
 public void testGetSubGroupsEmpty() {
 ClientGroup parent = new ClientGroup(null);
 assertEquals(Collections.emptySet(), parent.getSubGroups());
 }
}
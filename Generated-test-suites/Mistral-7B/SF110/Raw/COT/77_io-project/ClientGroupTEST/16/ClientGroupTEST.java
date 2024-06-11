package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientGroupState;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private ClientHandler handler;
 private Set<Client> testClients = new LinkedHashSet<>();

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 handler = mock(ClientHandler.class);
 rootGroup.addClientHandler(handler);
 testClients.add(new Client("Client1"));
 testClients.add(new Client("Client2"));
 testClients.add(new Client("Client3"));
 rootGroup.addAll(testClients);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = rootGroup.parentGroup();
 assertEquals(null, parentGroup);
 }

 @Test
 public void testNotifyMessageSent_ClientIsMember() {
 Client client = testClients.iterator().next();
 boolean result = rootGroup.notifyMessageSent(client, "testMessage");
 assertTrue(result);
 }

 @Test
 public void testNotifyMessageSent_ClientNotMember() {
 Client client = new Client("NotAMemberClient");
 boolean result = rootGroup.notifyMessageSent(client, "testMessage");
 assertFalse(result);
 }

 @Test
 public void testNotifyMessageSent_ClientIsMember_SubGroup() {
 Client client = testClients.iterator().next();
 boolean result = subGroup1.notifyMessageSent(client, "testMessage");
 assertTrue(result);
 }

 @Test
 public void testNotifyMessageSent_ClientIsMember_SubGroupAndParent() {
 Client client = testClients.iterator().next();
 boolean result = subGroup2.notifyMessageSent(client, "testMessage");
 assertTrue(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageSent_NullMessage() {
 Client client = testClients.iterator().next();
 rootGroup.notifyMessageSent(client, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageSent_NullClient() {
 rootGroup.notifyMessageSent(null, "testMessage");
 }

 @Test
 public void testNotifyMessageReceived_ClientIsMember() {
 Client client = testClients.iterator().next();
 boolean result = rootGroup.notifyMessageReceived(client, "testMessage");
 assertTrue(result);
 }

 @Test
 public void testNotifyMessageReceived_ClientNotMember() {
 Client client = new Client("NotAMemberClient");
 boolean result = rootGroup.notifyMessageReceived(client, "testMessage");
 assertFalse(result);
 }

 @Test
 public void testNotifyMessageReceived_ClientIsMember_SubGroup() {
 Client client = testClients.iterator().next();
 boolean result = subGroup1.notifyMessageReceived(client, "testMessage");
 assertTrue(result);
 }

 @Test
 public void testNotifyMessageReceived_ClientIsMember_SubGroupAndParent() {
 Client client = testClients.iterator().next();
 boolean result = subGroup2.notifyMessageReceived(client, "testMessage");
 assertTrue(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageReceived_NullMessage() {
 Client client = testClients.iterator().next();
 rootGroup.notifyMessageReceived(client, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageReceived_NullClient() {
 rootGroup.notifyMessageReceived(null, "testMessage");
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsMember() {
 Client client = testClients.iterator().next();
 Throwable cause = new RuntimeException("Test Exception");
 boolean result = rootGroup.notifyExceptionCaught(client, cause);
 assertTrue(result);
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotMember() {
 Client client = new Client("NotAMemberClient");
 Throwable cause = new RuntimeException("Test Exception");
 boolean result = rootGroup.notifyExceptionCaught(client, cause);
 assertFalse(result);
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsMember_SubGroup() {
 Client client = testClients.iterator().next();
 Throwable cause = new RuntimeException("Test Exception");
 boolean result = subGroup1.notifyExceptionCaught(client, cause);
 assertTrue(result);
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsMember_SubGroupAndParent() {
 Client client = testClients.iterator().next();
 Throwable cause = new RuntimeException("Test Exception");
 boolean result = subGroup2.notifyExceptionCaught(client, cause);
 assertTrue(result);
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyExceptionCaught_NullClient() {
 rootGroup.notifyExceptionCaught(null, new RuntimeException("Test Exception"));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyExceptionCaught_NullCause() {
 Client client = testClients.iterator().next();
 rootGroup.notifyExceptionCaught(client, null);
 }

 @Test
 public void testIterator() {
 Iterator<Client> iterator = rootGroup.iterator();
 assertEquals(3, iterator.size());
 }

 @Test
 public void testContains() {
 Client client = testClients.iterator().next();
 assertTrue(rootGroup.contains(client));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 }

 @Test
 public void testAdd() {
 Client newClient = new Client("NewClient");
 assertTrue(rootGroup.add(newClient));
 assertEquals(4, rootGroup.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAdd_NullClient() {
 rootGroup.add(null);
 }

 @Test
 public void testRemove() {
 Client clientToRemove = testClients.iterator().next();
 assertTrue(rootGroup.remove(clientToRemove));
 assertEquals(2, rootGroup.size());
 }

 @Test
 public void testRemove_ClientNotPresent() {
 Client clientToRemove = new Client("NotAPresentClient");
 assertFalse(rootGroup.remove(clientToRemove));
 assertEquals(2, rootGroup.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemove_NullClient() {
 rootGroup.remove(null);
 }

 @Test
 public void testCreateSubGroup() {
 assertTrue(rootGroup.createSubGroup() instanceof ClientGroup);
 }

 @Test
 public void testAddClientHandler() {
 assertNotNull(rootGroup.getHandler());
 }
}
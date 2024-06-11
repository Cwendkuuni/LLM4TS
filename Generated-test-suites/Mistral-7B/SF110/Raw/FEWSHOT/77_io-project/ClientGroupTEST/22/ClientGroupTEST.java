package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

 ClientGroup topLevelGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;

 @Before
 public void setup() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 client = new Client();
 handler = mock(ClientHandler.class);
 }

 @Test
 public void testAddAndRemoveClient() {
 // Test adding a client
 topLevelGroup.add(client);
 assertTrue(topLevelGroup.contains(client));
 // Test removing a client
 topLevelGroup.remove(client);
 assertFalse(topLevelGroup.contains(client));
 }

 @Test
 public void testNestingSubGroups() {
 ClientGroup subSubGroup = subGroup.createSubGroup();
 topLevelGroup.add(subGroup);
 topLevelGroup.add(subSubGroup);
 assertTrue(topLevelGroup.contains(subSubGroup));
 }

 @Test
 public void testMessageSentToParentAndSubGroups() {
 topLevelGroup.add(client);
 subGroup.add(client);
 topLevelGroup.addHandler(handler);
 subGroup.addHandler(handler);

 verify(handler, times(3)).messageSent(client, eq(anyObject()));

 topLevelGroup.notifyMessageSent(client, "testMessage");
 subGroup.notifyMessageSent(client, "testMessage");
 }

 @Test
 public void testMessageReceivedToParentAndSubGroups() {
 topLevelGroup.add(client);
 subGroup.add(client);
 topLevelGroup.addHandler(handler);
 subGroup.addHandler(handler);

 verify(handler, times(3)).messageReceived(client, eq(anyObject()));

 topLevelGroup.notifyMessageReceived(client, "testMessage");
 subGroup.notifyMessageReceived(client, "testMessage");
 }

 @Test
 public void testExceptionCaughtByParentAndSubGroups() {
 topLevelGroup.add(client);
 subGroup.add(client);
 topLevelGroup.addHandler(handler);
 subGroup.addHandler(handler);

 verify(handler, times(3)).exceptionCaught(client, eq(any(Throwable.class)));

 topLevelGroup.notifyExceptionCaught(client, new RuntimeException("Test Exception"));
 subGroup.notifyExceptionCaught(client, new RuntimeException("Test Exception"));
 }

 @Test
 public void testIterator() {
 topLevelGroup.add(client);
 topLevelGroup.add(new Client());

 List<Client> clients = new ArrayList<>();
 for (Client groupClient : topLevelGroup) {
 clients.add(groupClient);
 }

 assertEquals(2, clients.size());
 assertTrue(clients.contains(client));
 }

 @Test
 public void testSize() {
 topLevelGroup.add(client);
 topLevelGroup.add(new Client());

 assertEquals(2, topLevelGroup.size());
 }
}
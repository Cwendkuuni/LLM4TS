package ioproject.server.network;

import static org.junit.Assert.*;
import java.util.*;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 subGroup = parentGroup.createSubGroup();
 client = new Client();
 handler = Mockito.mock(ClientHandler.class);
 parentGroup.addClientHandler(handler);
 }

 @Test
 public void testNotification_messageSent_clientInGroup() {
 // given a client is in the group
 parentGroup.add(client);
 // when a message is sent to the client
 boolean result = parentGroup.notifyMessageSent(client, "message");
 // then the message should be propagated to the client and the handler
 assertTrue(result);
 verify(handler).messageSent(client, "message");
 }

 @Test
 public void testNotification_messageSent_clientInSubGroup() {
 // given a client is in a subgroup
 subGroup.add(client);
 // when a message is sent to the client
 boolean result = parentGroup.notifyMessageSent(client, "message");
 // then the message should be propagated to the client and the handler
 assertTrue(result);
 verify(handler).messageSent(client, "message");
 }

 @Test
 public void testNotification_messageSent_clientNotInGroup() {
 // given a client is not in the group
 // when a message is sent to the client
 boolean result = parentGroup.notifyMessageSent(client, "message");
 // then the method should return false
 assertFalse(result);
 }

 @Test
 public void testNotification_messageReceived_clientInGroup() {
 // given a client is in the group
 parentGroup.add(client);
 // when a message is received by the client
 boolean result = parentGroup.notifyMessageReceived(client, "message");
 // then the message should be propagated to the client and the handler
 assertTrue(result);
 verify(handler).messageReceived(client, "message");
 }

 @Test
 public void testNotification_messageReceived_clientInSubGroup() {
 // given a client is in a subgroup
 subGroup.add(client);
 // when a message is received by the client
 boolean result = parentGroup.notifyMessageReceived(client, "message");
 // then the message should be propagated to the client and the handler
 assertTrue(result);
 verify(handler).messageReceived(client, "message");
 }

 @Test
 public void testNotification_messageReceived_clientNotInGroup() {
 // given a client is not in the group
 // when a message is received by the client
 boolean result = parentGroup.notifyMessageReceived(client, "message");
 // then the method should return false
 assertFalse(result);
 }

 @Test
 public void testNotification_exceptionCaught_clientInGroup() {
 // given a client is in the group
 parentGroup.add(client);
 // when an exception is caught by the client
 boolean result = parentGroup.notifyExceptionCaught(client, new RuntimeException());
 // then the exception should be propagated to the handler
 assertTrue(result);
 verify(handler).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotification_exceptionCaught_clientInSubGroup() {
 // given a client is in a subgroup
 subGroup.add(client);
 // when an exception is caught by the client
 boolean result = parentGroup.notifyExceptionCaught(client, new RuntimeException());
 // then the exception should be propagated to the handler
 assertTrue(result);
 verify(handler).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotification_exceptionCaught_clientNotInGroup() {
 // given a client is not in the group
 // when an exception is caught by the client
 boolean result = parentGroup.notifyExceptionCaught(client, new RuntimeException());
 // then the method should return false
 assertFalse(result);
 }

 @Test
 public void testAddClient_clientAlreadyInGroup() {
 // given a client is already in the group
 parentGroup.add(client);
 // when the client is added again
 boolean result = parentGroup.add(client);
 // then the method should return false
 assertFalse(result);
 }

 @Test
 public void testAddClient_clientNotInGroup() {
 // given a client is not in the group
 // when the client is added
 boolean result = parentGroup.add(client);
 // then the client should be added to the group and the handler should be notified
 assertTrue(result);
 // check if the client is in the group
 assertTrue(parentGroup.contains(client));
 // check if the handler was notified
 verify(handler).clientAdded(client);
 }

 @Test
 public void testRemoveClient_clientInGroup() {
 // given a client is in the group
 parentGroup.add(client);
 // when the client is removed
 boolean result = parentGroup.remove(client);
 // then the client should be removed from the group and the handler should be notified
 assertTrue(result);
 // check if the client is no longer in the group
 assertFalse(parentGroup.contains(client));
 // check if the handler was notified
 verify(handler).clientRemoved(client);
 }

 @Test
 public void testRemoveClient_clientInSubGroup() {
 // given a client is in a subgroup
 subGroup.add(client);
 // when the client is removed from the subgroup
 boolean result = subGroup.remove(client);
 // then the client should be removed from the subgroup and the parent group should not be notified
 assertTrue(result);
 // check if the client is no longer in the subgroup
 assertFalse(subGroup.contains(client));
 // check if the handler was not notified
 verifyNoMoreInteractions(handler);
 }

 @Test
 public void testRemoveClient_clientNotInGroup() {
 // given a client is not in the group
 // when the client is removed
 boolean result = parentGroup.remove(client);
 // then the method should return false
 assertFalse(result);
 }

 @Test
 public void testCreateSubGroup() {
 // given the group has no subgroups
 assertFalse(parentGroup.hasSubGroups());
 // when a new subgroup is created
 ClientGroup subgroup = parentGroup.createSubGroup();
 // then the subgroup should be added to the group and the handler should be notified
 assertTrue(parentGroup.hasSubGroups());
 // check if the subgroup is in the group
 assertTrue(parentGroup.containsSubGroup(subgroup));
 // check if the handler was notified
 verify(handler).subgroupCreated(subgroup);
 }
}
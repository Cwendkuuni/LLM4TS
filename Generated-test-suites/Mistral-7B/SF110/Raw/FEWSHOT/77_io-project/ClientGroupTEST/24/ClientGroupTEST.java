package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ioproject.common.network.*;

public class ClientGroupTest {

 Client client1, client2, client3;
 ClientGroup parentGroup, clientGroup1, clientGroup2;
 ClientHandler handler;

 @Before
 public void setUp() {
 client1 = mock(Client.class);
 client2 = mock(Client.class);
 client3 = mock(Client.class);
 parentGroup = new ClientGroup(null);
 clientGroup1 = new ClientGroup(parentGroup);
 clientGroup2 = new ClientGroup(parentGroup);
 handler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testConstructor() {
 assertEquals(null, parentGroup.parentGroup());
 assertTrue(parentGroup.clients.isEmpty());
 assertFalse(parentGroup.handler == null);
 }

 @Test
 public void testAddClient() {
 // Test adding client to empty group
 assertFalse(parentGroup.add(client1));
 assertEquals(0, parentGroup.size());
 assertFalse(parentGroup.clients.contains(client1));

 // Test adding client to group with existing clients
 parentGroup.add(client1);
 parentGroup.add(client2);
 parentGroup.add(client3);
 assertTrue(parentGroup.contains(client1));
 assertTrue(parentGroup.contains(client2));
 assertTrue(parentGroup.contains(client3));
 assertEquals(3, parentGroup.size());

 // Test adding duplicate client
 assertFalse(parentGroup.add(client1));
 // Test adding client to group with same parent
 clientGroup1.add(client1);
 parentGroup.add(client1);
 assertEquals(1, clientGroup1.size());
 assertEquals(2, parentGroup.size());
 }

 @Test
 public void testRemoveClient() {
 // Test removing client from empty group
 assertFalse(parentGroup.remove(client1));
 assertEquals(0, parentGroup.size());
 assertFalse(parentGroup.clients.contains(client1));

 // Test removing client from group with existing clients
 parentGroup.add(client1);
 parentGroup.add(client2);
 parentGroup.add(client3);
 assertTrue(parentGroup.remove(client1));
 assertFalse(parentGroup.contains(client1));
 assertEquals(2, parentGroup.size());

 // Test removing client from subgroup
 clientGroup1.add(client1);
 clientGroup1.add(client2);
 parentGroup.add(clientGroup1);
 assertTrue(clientGroup1.remove(client1));
 assertFalse(clientGroup1.contains(client1));
 assertTrue(parentGroup.contains(clientGroup1));
 assertEquals(1, clientGroup1.size());
 assertEquals(3, parentGroup.size());
 }

 @Test
 public void testCreateSubGroup() {
 // Test creating empty subgroup
 ClientGroup subGroup = clientGroup1.createSubGroup();
 assertFalse(parentGroup.contains(subGroup));
 assertTrue(clientGroup1.subGroups.contains(new WeakReference<ClientGroup>(subGroup)));
 assertEquals(0, subGroup.size());

 // Test creating subgroup with clients
 clientGroup1.add(client1);
 clientGroup1.add(client2);
 ClientGroup subGroup = clientGroup1.createSubGroup();
 assertTrue(subGroup.contains(client1));
 assertTrue(subGroup.contains(client2));
 assertFalse(parentGroup.contains(client1));
 assertFalse(parentGroup.contains(client2));
 assertEquals(2, subGroup.size());
 assertEquals(2, clientGroup1.size());
 assertEquals(3, parentGroup.size());
 }

 @Test
 public void testNotifyMessageSent() {
 // Test notifying message sent to empty group
 assertFalse(parentGroup.notifyMessageSent(client1, Mockito.anyObject()));

 // Test notifying message sent to group with clients and no handler
 parentGroup.add(client1);
 parentGroup.add(client2);
 assertFalse(parentGroup.notifyMessageSent(client1, Mockito.anyObject()));
 assertFalse(parentGroup.notifyMessageSent(client2, Mockito.anyObject()));

 // Test notifying message sent to group with handler
 parentGroup.add(client1);
 parentGroup.add(client2);
 parentGroup.addClientHandler(handler);
 verify(handler).messageSent(client1, Mockito.anyObject());
 verify(handler).messageSent(client2, Mockito.anyObject());
 }

 @Test
 public void testNotifyMessageReceived() {
 // Test notifying message received to empty group
 assertFalse(parentGroup.notifyMessageReceived(client1, Mockito.anyObject()));

 // Test notifying message received to group with clients and no handler
 parentGroup.add(client1);
 parentGroup.add(client2);
 assertFalse(parentGroup.notifyMessageReceived(client1, Mockito.anyObject()));
 assertFalse(parentGroup.notifyMessageReceived(client2, Mockito.anyObject()));

 // Test notifying message received to group with handler
 parentGroup.add(client1);
 parentGroup.add(client2);
 parentGroup.addClientHandler(handler);
 verify(handler).messageReceived(client1, Mockito.anyObject());
 verify(handler).messageReceived(client2, Mockito.anyObject());
 }

 @Test
 public void testNotifyExceptionCaught() {
 // Test notifying exception caught to empty group
 assertFalse(parentGroup.notifyExceptionCaught(client1, Mockito.mock(Throwable.class)));

 // Test notifying exception caught to group with clients and no handler
 parentGroup.add(client1);
 parentGroup.add(client2);
 assertFalse(parentGroup.notifyExceptionCaught(client1, Mockito.any(Throwable.class)));
 assertFalse(parentGroup.notifyExceptionCaught(client2, Mockito.any(Throwable.class)));

 // Test notifying exception caught to group with handler
 parentGroup.add(client1);
 parentGroup.add(client2);
 parentGroup.addClientHandler(handler);
 verify(handler).exceptionCaught(client1, Mockito.any(Throwable.class));
 verify(handler).exceptionCaught(client2, Mockito.any(Throwable.class));
 }
}
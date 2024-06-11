package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupIterator;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler mockHandler;

 @Before
 public void setUp() {
 clientGroup = new ClientGroup(null);
 client1 = Mockito.spy(new Client(1, "client1"));
 client2 = Mockito.spy(new Client(2, "client2"));
 client3 = Mockito.spy(new Client(3, "client3"));
 mockHandler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testAddClient() {
 // testing adding a client to an empty group
 clientGroup.add(client1);
 assertTrue(clientGroup.contains(client1));
 assertEquals(1, clientGroup.size());

 // testing adding a client that is already in the group
 clientGroup.add(client1);
 assertFalse(clientGroup.add(client1));
 assertEquals(1, clientGroup.size());

 // testing adding clients to a group with existing clients
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client1);
 assertEquals(3, clientGroup.size());
 }

 @Test
 public void testRemoveClient() {
 // testing removing a client that is not in the group
 assertFalse(clientGroup.remove(client1));
 assertEquals(0, clientGroup.size());

 // testing removing a client from an empty group
 assertFalse(clientGroup.remove(client1));
 assertEquals(0, clientGroup.size());

 // testing removing a client that is in the group
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 assertTrue(clientGroup.remove(client1));
 assertEquals(2, clientGroup.size());
 assertFalse(clientGroup.contains(client1));
 }

 @Test
 public void testIterator() {
 Set<Client> clientSet = new LinkedHashSet<>(Arrays.asList(client1, client2, client3));
 clientGroup.addAll(clientSet);
 ClientGroupIterator iterator = clientGroup.iterator();
 assertEquals(client1, iterator.next());
 assertEquals(client2, iterator.next());
 assertEquals(client3, iterator.next());
 assertNull(iterator.next());
 }

 @Test
 public void testContains() {
 // testing contains a client that is not in the group
 assertFalse(clientGroup.contains(client1));

 // testing contains a client that is in the group
 clientGroup.add(client1);
 assertTrue(clientGroup.contains(client1));
 }

 @Test
 public void testSize() {
 // testing size of an empty group
 assertEquals(0, clientGroup.size());

 // testing size of a group with clients
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 assertEquals(3, clientGroup.size());
 }

 @Test
 public void testNotifyMessageSent() {
 // testing notifying a message sent to a client that is not in the group
 assertFalse(clientGroup.notifyMessageSent(client1, "testMessage"));

 // testing notifying a message sent to a client in the group
 clientGroup.add(client1);
 assertTrue(clientGroup.notifyMessageSent(client1, "testMessage"));

 // testing notifying a message sent to a client in a subgroup
 ClientGroup subGroup = clientGroup.createSubGroup();
 ClientGroup subGroup2 = subGroup.createSubGroup();
 ClientGroup finalSubGroup = subGroup2.createSubGroup();
 clientGroup.add(client1);
 subGroup.add(client2);
 finalSubGroup.add(client3);
 assertTrue(clientGroup.notifyMessageSent(client1, "testMessage"));
 assertTrue(subGroup.notifyMessageSent(client2, "testMessage"));
 assertTrue(finalSubGroup.notifyMessageSent(client3, "testMessage"));

 // testing notifying a message sent to a client with a handler
 clientGroup.addClientHandler(mockHandler);
 clientGroup.add(client1);
 mockHandler.messageSent(client1, "testMessage");
 verify(mockHandler).messageSent(client1, "testMessage");
 }

 @Test
 public void testNotifyMessageReceived() {
 // testing notifying a message received by a client that is not in the group
 assertFalse(clientGroup.notifyMessageReceived(client1, "testMessage"));

 // testing notifying a message received by a client in the group
 clientGroup.add(client1);
 assertTrue(clientGroup.notifyMessageReceived(client1, "testMessage"));

 // testing notifying a message received by a client in a subgroup
 ClientGroup subGroup = clientGroup.createSubGroup();
 ClientGroup subGroup2 = subGroup.createSubGroup();
 ClientGroup finalSubGroup = subGroup2.createSubGroup();
 clientGroup.add(client1);
 subGroup.add(client2);
 finalSubGroup.add(client3);
 assertTrue(clientGroup.notifyMessageReceived(client1, "testMessage"));
 assertTrue(subGroup.notifyMessageReceived(client2, "testMessage"));
 assertTrue(finalSubGroup.notifyMessageReceived(client3, "testMessage"));

 // testing notifying a message received by a client with a handler
 clientGroup.addClientHandler(mockHandler);
 clientGroup.add(client1);
 mockHandler.messageReceived(client1, "testMessage");
 verify(mockHandler).messageReceived(client1, "testMessage");
 }

 @Test
 public void testNotifyExceptionCaught() {
 // testing notifying an exception caught by a client that is not in the group
 assertFalse(clientGroup.notifyExceptionCaught(client1, new RuntimeException()));

 // testing notifying an exception caught by a client in the group
 clientGroup.add(client1);
 assertTrue(clientGroup.notifyExceptionCaught(client1, new RuntimeException()));

 // testing notifying an exception caught by a client in a subgroup
 ClientGroup subGroup = clientGroup.createSubGroup();
 ClientGroup subGroup2 = subGroup.createSubGroup();
 ClientGroup finalSubGroup = subGroup2.createSubGroup();
 clientGroup.add(client1);
 subGroup.add(client2);
 finalSubGroup.add(client3);
 assertTrue(clientGroup.notifyExceptionCaught(client1, new RuntimeException()));
 assertTrue(subGroup.notifyExceptionCaught(client2, new RuntimeException()));
 assertTrue(finalSubGroup.notifyExceptionCaught(client3, new RuntimeException()));

 // testing notifying an exception caught by a client with a handler
 clientGroup.addClientHandler(mockHandler);
 clientGroup.add(client1);
 mockHandler.exceptionCaught(client1, new RuntimeException());
 verify(mockHandler).exceptionCaught(client1, new RuntimeException());
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup parentGroup = clientGroup;
 ClientGroup subGroup = clientGroup.createSubGroup();
 ClientGroup subSubGroup = subGroup.createSubGroup();
 assertNotSame(parentGroup, subGroup);
 assertNotSame(subGroup, subSubGroup);
 assertSame(clientGroup, parentGroup.parentGroup());
 assertSame(subGroup, subSubGroup.parentGroup());
 }
}
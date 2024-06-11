package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import ioproject.common.network.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;
 ClientHandler subGroupHandler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 subGroup = parentGroup.createSubGroup();
 client = mock(Client.class);
 handler = mock(ClientHandler.class);
 subGroupHandler = mock(ClientHandler.class);
 }

 @Test
 public void testAddClient() {
 // test adding a client to an empty group
 assertTrue(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 // test adding a client that already exists in the parent group
 assertFalse(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 // test adding a client to a subgroup
 assertTrue(subGroup.add(client));
 assertEquals(1, subGroup.size());
 // test adding a client to a full group
 Client existingClient = new Client();
 parentGroup.add(existingClient);
 assertFalse(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 }

 @Test
 public void testRemoveClient() {
 // test removing a non-existent client
 assertFalse(parentGroup.remove(client));
 assertEquals(0, parentGroup.size());
 // test removing a client from an empty group
 assertFalse(parentGroup.remove(client));
 assertEquals(0, parentGroup.size());
 // test removing a client from a group with one client
 assertTrue(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 assertTrue(parentGroup.remove(client));
 assertEquals(0, parentGroup.size());
 // test removing a client that doesn't exist in a subgroup
 assertFalse(subGroup.remove(client));
 assertEquals(0, subGroup.size());
 // test removing a client from a subgroup with one client
 assertTrue(subGroup.add(client));
 assertEquals(1, subGroup.size());
 assertTrue(subGroup.remove(client));
 assertEquals(0, subGroup.size());
 // test removing a client that exists in both the parent group and a subgroup
 when(parentGroup.contains(client)).thenReturn(true);
 when(subGroup.contains(client)).thenReturn(true);
 assertTrue(parentGroup.remove(client));
 assertFalse(subGroup.remove(client));
 assertEquals(0, parentGroup.size());
 assertEquals(0, subGroup.size());
 }

 @Test
 public void testContains() {
 // test containing a non-existent client
 assertFalse(parentGroup.contains(client));
 // test containing a client in an empty group
 assertFalse(parentGroup.contains(client));
 // test containing a client in a group with one client
 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.contains(client));
 // test containing a client in a subgroup
 assertTrue(subGroup.add(client));
 assertTrue(subGroup.contains(client));
 }

 @Test
 public void testSize() {
 // test the size of an empty group
 assertEquals(0, parentGroup.size());
 // test the size of a group with one client
 assertTrue(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 // test the size of a group with multiple clients and a subgroup
 parentGroup.add(client);
 subGroup.add(client);
 assertEquals(2, parentGroup.size());
 }

 @Test
 public void testIterator() {
 // test the iterator of an empty group
 Iterator<Client> parentGroupIterator = parentGroup.iterator();
 assertFalse(parentGroupIterator.hasNext());
 // test the iterator of a group with one client
 assertTrue(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 Client parentGroupClient = parentGroupIterator.next();
 assertEquals(client, parentGroupClient);
 assertFalse(parentGroupIterator.hasNext());
 // test the iterator of a group with multiple clients and a subgroup
 parentGroup.add(client);
 subGroup.add(client);
 Iterator<Client> parentGroupIterator2 = parentGroup.iterator();
 Client parentGroupClient2 = parentGroupIterator2.next();
 assertEquals(client, parentGroupClient2);
 parentGroupIterator2.next(); // skip the subgroup client
 assertFalse(parentGroupIterator2.hasNext());
 }

 @Test
 public void testNotifyMessageSent() {
 // test notifying a message sent to a non-existent client
 assertFalse(parentGroup.notifyMessageSent(client, "message"));
 // test notifying a message sent to a client in an empty group
 assertFalse(parentGroup.add(client));
 assertFalse(parentGroup.notifyMessageSent(client, "message"));
 // test notifying a message sent to a client in a group with one client
 client = new Client();
 parentGroup.add(client);
 assertTrue(parentGroup.notifyMessageSent(client, "message"));
 // test notifying a message sent to a client in a subgroup
 subGroup.add(client);
 parentGroup.add(subGroup);
 assertTrue(parentGroup.notifyMessageSent(client, "message"));
 // test notifying a message sent to a client that doesn't exist in the parent group but in a subgroup
 when(parentGroup.contains(client)).thenReturn(false);
 when(subGroup.contains(client)).thenReturn(true);
 assertTrue(parentGroup.notifyMessageSent(client, "message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 // test notifying a message received by a non-existent client
 assertFalse(parentGroup.notifyMessageReceived(client, "message"));
 // test notifying a message received by a client in an empty group
 assertFalse(parentGroup.add(client));
 assertFalse(parentGroup.notifyMessageReceived(client, "message"));
 // test notifying a message received by a client in a group with one client
 client = new Client();
 parentGroup.add(client);
 assertTrue(parentGroup.notifyMessageReceived(client, "message"));
 // test notifying a message received by a client in a subgroup
 subGroup.add(client);
 parentGroup.add(subGroup);
 assertTrue(parentGroup.notifyMessageReceived(client, "message"));
 // test notifying a message received by a client that doesn't exist in the parent group but in a subgroup
 when(parentGroup.contains(client)).thenReturn(false);
 when(subGroup.contains(client)).thenReturn(true);
 assertTrue(parentGroup.notifyMessageReceived(client, "message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 // test notifying an exception caught by a non-existent client
 assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException("exception")));
 // test notifying an exception caught by a client in an empty group
 assertFalse(parentGroup.add(client));
 assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException("exception")));
 // test notifying an exception caught by a client in a group with one client
 client = new Client();
 parentGroup.add(client);
 assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException("exception")));
 // test notifying an exception caught by a client in a subgroup
 subGroup.add(client);
 parentGroup.add(subGroup);
 assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException("exception")));
 // test notifying an exception caught by a client that doesn't exist in the parent group but in a subgroup
 when(parentGroup.contains(client)).thenReturn(false);
 when(subGroup.contains(client)).thenReturn(true);
 assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException("exception")));
 }

 @Test
 public void testCreateSubGroup() {
 // test creating a subgroup of an empty group
 assertNotNull(parentGroup.createSubGroup());
 assertEquals(0, parentGroup.size());
 // test creating a subgroup of a group with one client
 parentGroup.add(client);
 ClientGroup subGroup = parentGroup.createSubGroup();
 assertEquals(1, parentGroup.size());
 assertTrue(subGroup.contains(client));
 }

 @Test
 public void testAddClientHandler() {
 // test adding a client handler to an empty group
 assertNull(parentGroup.handler);
 parentGroup.addClientHandler(handler);
 assertEquals(handler, parentGroup.handler);
 // test adding a client handler to a group with an existing handler
 parentGroup.handler = handler;
 parentGroup.addClientHandler(subGroupHandler);
 assertEquals(handler, parentGroup.handler);
 }
}
package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup childGroup;
 Client client;
 Client client2;
 ClientHandler handler;
 ClientHandler subHandler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 client = new Client();
 client2 = new Client();
 handler = mock(ClientHandler.class);
 childGroup = parentGroup.createSubGroup();
 childGroup.addClientHandler(subHandler);
 }

 @Test
 public void testAddClient() {
 assertTrue(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 assertEquals(1, childGroup.size());
 }

 @Test
 public void testAddDuplicateClient() {
 assertFalse(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 }

 @Test
 public void testAddClientToSubGroup() {
 assertTrue(childGroup.add(client));
 assertEquals(1, childGroup.size());
 }

 @Test
 public void testAddDuplicateClientToSubGroup() {
 assertFalse(childGroup.add(client));
 assertEquals(1, childGroup.size());
 }

 @Test
 public void testRemoveClient() {
 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.add(client2));
 assertEquals(2, parentGroup.size());
 assertTrue(childGroup.add(client));
 assertEquals(1, childGroup.size());

 assertTrue(parentGroup.remove(client));
 assertEquals(1, parentGroup.size());
 assertFalse(childGroup.contains(client));
 }

 @Test
 public void testRemoveClientFromSubGroup() {
 assertTrue(childGroup.add(client));
 assertEquals(1, childGroup.size());
 assertTrue(parentGroup.remove(client));
 assertEquals(1, parentGroup.size());
 assertFalse(childGroup.contains(client));
 }

 @Test
 public void testRemoveNonExistentClient() {
 assertFalse(parentGroup.remove(client));
 }

 @Test
 public void testContains() {
 assertFalse(parentGroup.contains(client));
 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.contains(client));
 }

 @Test
 public void testSize() {
 assertEquals(0, parentGroup.size());
 assertEquals(0, childGroup.size());
 assertTrue(parentGroup.add(client));
 assertEquals(1, parentGroup.size());
 assertTrue(childGroup.add(client));
 assertEquals(1, childGroup.size());
 }

 @Test
 public void testIterator() {
 Set<Client> set = new LinkedHashSet<Client>();
 set.add(client);
 set.add(client2);
 ParentGroup parentGroup = mock(ClientGroup.class);
 when(parentGroup.iterator()).thenReturn(set.iterator());
 ClientGroup childGroup = new ClientGroup(parentGroup);

 List<Client> clients = new ArrayList<>();
 clients.add(client);
 clients.add(client2);

 assertEquals(clients, childGroup.iterator().asList());
 }

 @Test
 public void testNotifyMessageSent() {
 when(subHandler.messageSent(client, anyObject())).thenReturn(true);

 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.add(client2));
 assertTrue(childGroup.add(client));
 assertEquals(3, parentGroup.size());
 assertEquals(1, childGroup.size());

 verify(subHandler, times(1)).messageSent(client, anyObject());
 }

 @Test
 public void testNotifyMessageSentNoHandler() {
 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.add(client2));
 assertTrue(childGroup.add(client));
 assertEquals(3, parentGroup.size());
 assertEquals(1, childGroup.size());

 // no handler
 verifyNoInteractions(handler);
 }

 @Test
 public void testNotifyExceptionCaught() {
 when(subHandler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);

 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.add(client2));
 assertTrue(childGroup.add(client));
 assertEquals(3, parentGroup.size());
 assertEquals(1, childGroup.size());

 // client throws exception
 throw new RuntimeException();

 verify(subHandler, times(1)).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaughtNoHandler() {
 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.add(client2));
 assertTrue(childGroup.add(client));
 assertEquals(3, parentGroup.size());
 assertEquals(1, childGroup.size());

 // no handler
 verifyNoInteractions(handler);
 }
}
package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import ioproject.common.network.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClientGroupTest {

 ClientHandler mockHandler;
 Client mockClient;
 ClientGroup parentGroup, subGroup, subGroup2;
 ClientGroup clientGroupUnderTest;

 @Before
 public void setUp() {
 mockHandler = mock(ClientHandler.class);
 mockClient = mock(Client.class);
 parentGroup = mock(ClientGroup.class);
 subGroup = new ClientGroup(parentGroup);
 subGroup2 = new ClientGroup(parentGroup);
 clientGroupUnderTest = new ClientGroup(parentGroup);
 }

 @Test
 public void testAddClient() {
 // Test adding a client to the ClientGroup
 assertTrue(clientGroupUnderTest.add(mockClient));
 assertEquals(1, clientGroupUnderTest.size());

 // Test adding the same client twice
 assertFalse(clientGroupUnderTest.add(mockClient));
 assertEquals(1, clientGroupUnderTest.size());

 // Test adding a client to the ClientGroup when it already exists in the parent group
 assertTrue(parentGroup.add(mockClient));
 assertTrue(clientGroupUnderTest.add(mockClient));
 assertEquals(1, clientGroupUnderTest.size());
 }

 @Test
 public void testRemoveClient() {
 // Test removing a client that exists in the ClientGroup
 assertTrue(clientGroupUnderTest.contains(mockClient));
 assertTrue(clientGroupUnderTest.remove(mockClient));
 assertFalse(clientGroupUnderTest.contains(mockClient));
 assertEquals(0, clientGroupUnderTest.size());

 // Test removing a client that does not exist in the ClientGroup
 assertFalse(clientGroupUnderTest.contains(mockClient));
 assertFalse(clientGroupUnderTest.remove(mockClient));
 assertEquals(0, clientGroupUnderTest.size());

 // Test removing a client that exists in the ClientGroup and its subgroups
 clientGroupUnderTest.add(mockClient);
 subGroup.add(mockClient);
 assertTrue(clientGroupUnderTest.contains(mockClient));
 assertTrue(subGroup.contains(mockClient));
 assertTrue(clientGroupUnderTest.remove(mockClient));
 assertFalse(clientGroupUnderTest.contains(mockClient));
 assertFalse(subGroup.contains(mockClient));
 }

 @Test
 public void testContainsClient() {
 // Test containing a client that exists in the ClientGroup
 assertTrue(clientGroupUnderTest.contains(mockClient));

 // Test containing a client that does not exist in the ClientGroup
 assertFalse(clientGroupUnderTest.contains(mockClient));

 // Test containing a client that exists in a subgroup
 clientGroupUnderTest.add(mockClient);
 subGroup.add(mockClient);
 assertTrue(clientGroupUnderTest.contains(mockClient));
 }

 @Test
 public void testSize() {
 // Test the size of an empty ClientGroup
 assertEquals(0, clientGroupUnderTest.size());

 // Test the size of a ClientGroup with one client
 clientGroupUnderTest.add(mockClient);
 assertEquals(1, clientGroupUnderTest.size());

 // Test the size of a ClientGroup with multiple clients and subgroups
 clientGroupUnderTest.add(mockClient);
 subGroup.add(mockClient);
 subGroup2.add(mockClient);
 assertEquals(3, clientGroupUnderTest.size());
 }

 @Test
 public void testIterator() {
 // Test that the iterator returns the correct clients in the correct order
 List<Client> clients = new ArrayList<>();
 clients.add(mockClient);
 clientGroupUnderTest.addAll(clients);
 Iterator<Client> iterator = clientGroupUnderTest.iterator();
 Client client = iterator.next();
 assertEquals(mockClient, client);
 assertNull(iterator.hasNext());
 }

 @Test
 public void testNotifyMessageSent() {
 // Test notifying a message sent to a client in the ClientGroup
 doNothing().when(mockHandler).messageSent(mockClient, anyObject());
 clientGroupUnderTest.addClientHandler(mockHandler);
 clientGroupUnderTest.add(mockClient);
 assertTrue(clientGroupUnderTest.notifyMessageSent(mockClient, "test message"));
 verify(mockHandler, times(1)).messageSent(mockClient, "test message");

 // Test notifying a message sent to a client in a subgroup
 ClientGroup subGroup = new ClientGroup(clientGroupUnderTest);
 doNothing().when(mockHandler).messageSent(mockClient, anyObject());
 subGroup.addClientHandler(mockHandler);
 subGroup.add(mockClient);
 clientGroupUnderTest.add(subGroup);
 assertTrue(clientGroupUnderTest.notifyMessageSent(mockClient, "test message"));
 verify(mockHandler, times(1)).messageSent(mockClient, "test message");
 }

 @Test
 public void testNotifyMessageReceived() {
 // Test notifying a message received from a client in the ClientGroup
 doNothing().when(mockHandler).messageReceived(mockClient, anyObject());
 clientGroupUnderTest.addClientHandler(mockHandler);
 clientGroupUnderTest.add(mockClient);
 assertTrue(clientGroupUnderTest.notifyMessageReceived(mockClient, "test message"));
 verify(mockHandler, times(1)).messageReceived(mockClient, "test message");

 // Test notifying a message received from a client in a subgroup
 ClientGroup subGroup = new ClientGroup(clientGroupUnderTest);
 doNothing().when(mockHandler).messageReceived(mockClient, anyObject());
 subGroup.addClientHandler(mockHandler);
 subGroup.add(mockClient);
 clientGroupUnderTest.add(subGroup);
 assertTrue(clientGroupUnderTest.notifyMessageReceived(mockClient, "test message"));
 verify(mockHandler, times(1)).messageReceived(mockClient, "test message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 // Test notifying an exception caught by a client in the ClientGroup
 doThrow(new RuntimeException("test exception")).when(mockClient).sendMessage(anyString());
 doNothing().when(mockHandler).exceptionCaught(mockClient, any(Throwable.class));
 clientGroupUnderTest.addClientHandler(mockHandler);
 clientGroupUnderTest.add(mockClient);
 try {
 clientGroupUnderTest.notifyMessageSent(mockClient, "test message");
 fail();
 } catch (Exception e) {
 // expected
 }
 assertTrue(clientGroupUnderTest.notifyExceptionCaught(mockClient, any(Throwable.class)));
 verify(mockHandler, times(1)).exceptionCaught(mockClient, any(Throwable.class));

 // Test notifying an exception caught by a client in a subgroup
 ClientGroup subGroup = new ClientGroup(clientGroupUnderTest);
 doThrow(new RuntimeException("test exception")).when(mockClient).sendMessage(anyString());
 doNothing().when(mockHandler).exceptionCaught(mockClient, any(Throwable.class));
 subGroup.addClientHandler(mockHandler);
 subGroup.add(mockClient);
 clientGroupUnderTest.add(subGroup);
 try {
 clientGroupUnderTest.notifyMessageSent(mockClient, "test message");
 fail();
 } catch (Exception e) {
 // expected
 }
 assertTrue(clientGroupUnderTest.notifyExceptionCaught(mockClient, any(Throwable.class)));
 verify(mockHandler, times(1)).exceptionCaught(mockClient, any(Throwable.class));
 }
}
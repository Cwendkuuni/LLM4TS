package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*; // for mockito library for creating mocks

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client1, client2, client3;
 private ClientHandler handler;

 @Before
 public void setup() {
 clientGroup = new ClientGroup(null);
 client1 = mock(Client.class);
 client2 = mock(Client.class);
 client3 = mock(Client.class);
 handler = mock(ClientHandler.class);

 clientGroup.addClientHandler(handler);
 clientGroup.add(client1);
 clientGroup.add(client2);
 }

 @Test
 public void testNotifyMessageSent_whenMessageIsSentToExistingClient_thenTrue() {
 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);

 clientGroup.notifyMessageSent(client1, "test message");

 verify(handler).messageSent(client1, "test message");
 }

 @Test
 public void testNotifyMessageSent_whenMessageIsSentToNonExistingClient_thenFalse() {
 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);

 assertFalse(clientGroup.notifyMessageSent(client3, "test message"));
 }

 @Test
 public void testNotifyMessageSent_whenMessageIsSentToClientInSubGroup_thenTrue() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
 Client client4 = mock(Client.class);
 clientGroup.add(subGroup);
 subGroup.add(client4);

 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client1.equals(client4)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);
 when(client2.equals(client4)).thenReturn(false);
 when(client3.equals(client4)).thenReturn(false);

 clientGroup.notifyMessageSent(client1, "test message");
 verify(subGroupReference.get()).notifyMessageSent(client4, "test message");
 }

 @Test
 public void testNotifyExceptionCaught_whenExceptionIsCaughtByClient_thenTrue() {
 Throwable cause = new RuntimeException("test exception");
 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);

 clientGroup.notifyExceptionCaught(client1, cause);

 verify(handler).exceptionCaught(client1, cause);
 }

 @Test
 public void testNotifyExceptionCaught_whenExceptionIsCaughtByClientInSubGroup_thenTrue() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
 Client client4 = mock(Client.class);
 clientGroup.add(subGroup);
 subGroup.add(client4);

 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client1.equals(client4)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);
 when(client2.equals(client4)).thenReturn(false);
 when(client3.equals(client4)).thenReturn(false);

 Throwable cause = new RuntimeException("test exception");
 clientGroup.notifyExceptionCaught(client1, cause);
 verify(subGroupReference.get()).notifyExceptionCaught(client4, cause);
 }

 @Test
 public void testNotifyMessageReceived_whenMessageIsReceivedByExistingClient_thenTrue() {
 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);

 clientGroup.notifyMessageReceived(client1, "test message");

 verify(handler).messageReceived(client1, "test message");
 }

 @Test
 public void testNotifyMessageReceived_whenMessageIsReceivedByNonExistingClient_thenFalse() {
 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);

 assertFalse(clientGroup.notifyMessageReceived(client3, "test message"));
 }

 @Test
 public void testNotifyMessageReceived_whenMessageIsReceivedByClientInSubGroup_thenTrue() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
 Client client4 = mock(Client.class);
 clientGroup.add(subGroup);
 subGroup.add(client4);

 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client1.equals(client4)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);
 when(client2.equals(client4)).thenReturn(false);
 when(client3.equals(client4)).thenReturn(false);

 clientGroup.notifyMessageReceived(client1, "test message");
 verify(subGroupReference.get()).notifyMessageReceived(client4, "test message");
 }

 @Test
 public void testAddClient_whenExistingClientIsAddedToSameParentGroup_thenTrue() {
 Client client5 = mock(Client.class);
 boolean added = clientGroup.add(client5);
 assertTrue(added);
 }

 @Test
 public void testAddClient_whenExistingClientIsAddedToDifferentParentGroup_thenFalse() {
 Client client5 = mock(Client.class);
 ClientGroup newParentGroup = new ClientGroup(clientGroup);
 boolean added = newParentGroup.add(client5);
 assertFalse(added);
 }

 @Test
 public void testRemoveClient_whenExistingClientIsRemovedFromSameGroup_thenTrue() {
 Client client5 = mock(Client.class);
 boolean removed = clientGroup.remove(client5);
 assertTrue(removed);
 }

 @Test
 public void testRemoveClient_whenExistingClientIsRemovedFromDifferentGroup_thenFalse() {
 Client client5 = mock(Client.class);
 ClientGroup newSubGroup = clientGroup.createSubGroup();
 boolean removed = newSubGroup.remove(client5);
 assertFalse(removed);
 }

 @Test
 public void testSize_whenSizeIsRetrievedFromEmptyGroup_thenZero() {
 int size = clientGroup.size();
 assertEquals(0, size);
 }

 @Test
 public void testSize_whenSizeIsRetrievedFromNonEmptyGroup_thenSizeOfClients() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 int size = clientGroup.size();
 assertEquals(2, size);
 }

 @Test
 public void testContains_whenClientIsMemberOfGroup_thenTrue() {
 boolean contains = clientGroup.contains(client1);
 assertTrue(contains);
 }

 @Test
 public void testContains_whenClientIsNotMemberOfGroup_thenFalse() {
 boolean contains = clientGroup.contains(client3);
 assertFalse(contains);
 }

 @Test
 public void testIterator_whenUsedToIterateOverEmptyGroup_thenReturnsEmptyIterator() {
 Iterator<Client> iterator = clientGroup.iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIterator_whenUsedToIterateOverNonEmptyGroup_thenReturnsIteratorOfClients() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 Iterator<Client> iterator = clientGroup.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client1, iterator.next());
 }

 @Test
 public void testNotifyMessageSent_whenMessageIsSentToClientAffectingSubGroups_thenTrue() {
 ClientGroup subGroup1 = clientGroup.createSubGroup();
 ClientGroup subGroup2 = subGroup1.createSubGroup();
 Client client4 = mock(Client.class);
 Client client5 = mock(Client.class);
 Client client6 = mock(Client.class);
 Client client7 = mock(Client.class);
 clientGroup.add(subGroup1);
 subGroup1.add(client4);
 subGroup1.add(subGroup2);
 subGroup2.add(client5);
 subGroup2.add(client6);
 subGroup2.add(client7);

 when(client1.equals(client2)).thenReturn(false);
 when(client1.equals(client3)).thenReturn(false);
 when(client1.equals(client4)).thenReturn(false);
 when(client1.equals(client5)).thenReturn(false);
 when(client1.equals(client6)).thenReturn(false);
 when(client1.equals(client7)).thenReturn(false);
 when(client2.equals(client3)).thenReturn(false);
 when(client2.equals(client4)).thenReturn(false);
 when(client2.equals(client5)).thenReturn(false);
 when(client2.equals(client6)).thenReturn(false);
 when(client2.equals(client7)).thenReturn(false);
 when(client3.equals(client4)).thenReturn(false);
 when(client3.equals(client5)).thenReturn(false);
 when(client3.equals(client6)).thenReturn(false);
 when(client3.equals(client7)).thenReturn(false);
 when(client4.equals(client5)).thenReturn(false);
 when(client4.equals(client6)).thenReturn(false);
 when(client4.equals(client7)).thenReturn(false);
 when(client5.equals(client6)).thenReturn(false);
 when(client5.equals(client7)).thenReturn(false);
 when(client6.equals(client7)).thenReturn(false);

 Set<Client> clients = new LinkedHashSet<>(Arrays.asList(client1, client2, client4, client5, client6, client7));
 clientGroup.clients = clients;

 clientGroup.notifyMessageSent(client1, "test message");

 verify(handler).messageSent(client1, "test message");
 verify(subGroup1).notifyMessageSent(client4, "test message");
 verify(subGroup2).notifyMessageSent(client5, "test message");
 verify(subGroup2).notifyMessageSent(client6, "test message");
 verify(subGroup2).notifyMessageSent(client7, "test message");
 }
}
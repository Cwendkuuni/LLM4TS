import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private ClientGroup parentGroup;
 private ClientHandler handler;

 @Before
 public void setUp() {
 client = mock(Client.class);
 parentGroup = mock(ClientGroup.class);
 handler = mock(ClientHandler.class);
 clientGroup = new ClientGroup(parentGroup);
 }

 @Test
 public void testConstructor() {
 assertNull(clientGroup.parentGroup());
 assertTrue(clientGroup.clients().isEmpty());
 assertNull(clientGroup.handler);
 }

 @Test
 public void testParentGroup() {
 assertEquals(parentGroup, clientGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent_whenClientIsInGroup_returnsTrue() {
 when(clientGroup.clients().contains(client)).thenReturn(true);
 when(parentGroup.notifyMessageSent(client, anyObject())).thenReturn(false);
 when(handler.messageSent(client, anyObject())).thenReturn(false);
 assertTrue(clientGroup.notifyMessageSent(client, anyObject()));
 }

 @Test
 public void testNotifyMessageSent_whenClientIsNotInGroup_returnsFalse() {
 when(clientGroup.clients().contains(client)).thenReturn(false);
 assertFalse(clientGroup.notifyMessageSent(client, anyObject()));
 }

 @Test
 public void testNotifyMessageSent_whenClientIsInSubGroup_returnsTrue() {
 final ClientGroup subGroup = clientGroup.createSubGroup();
 when(clientGroup.clients().contains(client)).thenReturn(false);
 when(subGroup.notifyMessageSent(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageSent(client, anyObject()));
 }

 @Test
 public void testNotifyMessageSent_whenClientIsInSubGroupAndParentHandlerIsNotified_returnsTrue() {
 final ClientGroup subGroup = clientGroup.createSubGroup();
 when(clientGroup.clients().contains(client)).thenReturn(false);
 when(subGroup.notifyMessageSent(client, anyObject())).thenReturn(false);
 when(handler.messageSent(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageSent(client, anyObject()));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsInGroup_returnsTrue() {
 when(clientGroup.clients().contains(client)).thenReturn(true);
 when(parentGroup.notifyMessageReceived(client, anyObject())).thenReturn(false);
 when(handler.messageReceived(client, anyObject())).thenReturn(false);
 assertTrue(clientGroup.notifyMessageReceived(client, anyObject()));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsNotInGroup_returnsFalse() {
 when(clientGroup.clients().contains(client)).thenReturn(false);
 assertFalse(clientGroup.notifyMessageReceived(client, anyObject()));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsInSubGroup_returnsTrue() {
 final ClientGroup subGroup = clientGroup.createSubGroup();
 when(clientGroup.clients().contains(client)).thenReturn(false);
 when(subGroup.notifyMessageReceived(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageReceived(client, anyObject()));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsInSubGroupAndParentHandlerIsNotified_returnsTrue() {
 final ClientGroup subGroup = clientGroup.createSubGroup();
 when(clientGroup.clients().contains(client)).thenReturn(false);
 when(subGroup.notifyMessageReceived(client, anyObject())).thenReturn(false);
 when(handler.messageReceived(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyMessageReceived(client, anyObject()));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsInGroup_returnsTrue() {
 when(clientGroup.clients().contains(client)).thenReturn(true);
 when(parentGroup.notifyExceptionCaught(client, anyObject())).thenReturn(false);
 when(handler.exceptionCaught(client, anyObject())).thenReturn(false);
 assertTrue(clientGroup.notifyExceptionCaught(client, anyObject()));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsNotInGroup_returnsFalse() {
 when(clientGroup.clients().contains(client)).thenReturn(false);
 assertFalse(clientGroup.notifyExceptionCaught(client, anyObject()));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsInSubGroup_returnsTrue() {
 final ClientGroup subGroup = clientGroup.createSubGroup();
 when(clientGroup.clients().contains(client)).thenReturn(false);
 when(subGroup.notifyExceptionCaught(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyExceptionCaught(client, anyObject()));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsInSubGroupAndParentHandlerIsNotified_returnsTrue() {
 final ClientGroup subGroup = clientGroup.createSubGroup();
 when(clientGroup.clients().contains(client)).thenReturn(false);
 when(subGroup.notifyExceptionCaught(client, anyObject())).thenReturn(false);
 when(handler.exceptionCaught(client, anyObject())).thenReturn(true);
 assertTrue(clientGroup.notifyExceptionCaught(client, anyObject()));
 }

 @Test
 public void testIterator() {
 final List<Client> clients = new ArrayList<>();
 clients.add(client);
 when(clientGroup.clients().iterator().next()).thenReturn(clients.get(0));
 assertEquals(clients.get(0), clientGroup.iterator().next());
 }

 @Test
 public void testContains() {
 when(clientGroup.clients().contains(client)).thenReturn(true);
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize_whenNoClients_returnsZero() {
 assertEquals(0, clientGroup.size());
 }

 @Test
 public void testSize_whenClientsPresent_returnsCorrectSize() {
 final List<Client> clients = new ArrayList<>();
 clients.add(client);
 when(clientGroup.clients().size()).thenReturn(clients.size());
 assertEquals(clients.size(), clientGroup.size());
 }

 @Test
 public void testAddClient_whenParentGroupContainsClient_returnsTrue() {
 when(parentGroup.contains(client)).thenReturn(true);
 clientGroup.add(client);
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testAddClient_whenParentGroupDoesNotContainClient_returnsFalse() {
 when(parentGroup.contains(client)).thenReturn(false);
 assertFalse(clientGroup.add(client));
 }

 @Test
 public void testRemoveClient_whenClientIsNotInGroup_returnsFalse() {
 assertFalse(clientGroup.remove(client));
 }

 @Test
 public void testRemoveClient_whenClientIsInGroup_returnsTrue() {
 clientGroup.add(client);
 assertTrue(clientGroup.remove(client));
 assertFalse(clientGroup.contains(client));
 }

 @Test
 public void testCreateSubGroup_whenCalledTwice_returnsDifferentSubGroups() {
 final ClientGroup subGroup1 = clientGroup.createSubGroup();
 final ClientGroup subGroup2 = clientGroup.createSubGroup();
 assertNotSame(subGroup1, subGroup2);
 }

 @Test
 public void testAddClientHandler() {
 clientGroup.addClientHandler(handler);
 assertEquals(handler, clientGroup.handler);
 }
}
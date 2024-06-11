import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private ClientGroup parentGroupMock;
 private Client clientMock;
 private ClientHandler clientHandlerMock;

 @Before
 public void setUp() {
 parentGroupMock = mock(ClientGroup.class);
 clientMock = mock(Client.class);
 clientHandlerMock = mock(ClientHandler.class);
 clientGroup = new ClientGroup(parentGroupMock);
 }

 @Test
 public void testContainsNullClient() {
 Assert.assertFalse(clientGroup.contains(null));
 }

 @Test
 public void testContainsClient() {
 when(parentGroupMock.contains(clientMock)).thenReturn(true);
 when(clientGroup.clients.contains(clientMock)).thenReturn(true);
 Assert.assertTrue(clientGroup.contains(clientMock));
 }

 @Test
 public void testSizeZero() {
 Assert.assertEquals(0, clientGroup.size());
 }

 @Test
 public void testSizeOne() {
 clientGroup.add(clientMock);
 Assert.assertEquals(1, clientGroup.size());
 }

 @Test
 public void testSizeGreaterThanOne() {
 Set<Client> clients = new HashSet<>(Arrays.asList(clientMock, clientMock, clientMock));
 clientGroup = new ClientGroup(parentGroupMock);
 clients.forEach(clientGroup::add);
 Assert.assertEquals(3, clientGroup.size());
 }

 @Test
 public void testAddClientAlreadyInParentGroup() {
 when(parentGroupMock.contains(clientMock)).thenReturn(true);
 clientGroup.add(clientMock);
 Assert.assertTrue(clientGroup.contains(clientMock));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddClientNotInParentGroup() {
 when(parentGroupMock.contains(clientMock)).thenReturn(false);
 clientGroup.add(clientMock);
 }

 @Test
 public void testAddClientHandler() {
 clientGroup.addClientHandler(clientHandlerMock);
 Assert.assertNotNull(clientGroup.handler);
 }

 @Test
 public void testNotifyMessageSent() {
 when(parentGroupMock.notifyMessageSent(clientMock, Mockito.anyObject())).thenReturn(true);
 when(clientGroup.clients.contains(clientMock)).thenReturn(true);
 when(clientHandlerMock.messageSent(clientMock, Mockito.anyObject())).thenReturn(true);

 boolean result = clientGroup.notifyMessageSent(clientMock, "message");
 Assert.assertTrue(result);
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 ClientGroup subGroupMock = mock(ClientGroup.class);
 when(parentGroupMock.notifyMessageSent(clientMock, Mockito.anyObject())).thenReturn(false);
 WeakReference<ClientGroup> subGroupRef = new WeakReference<>(subGroupMock);
 when(clientGroup.subGroups.contains(subGroupRef)).thenReturn(true);
 when(subGroupMock.notifyMessageSent(clientMock, Mockito.anyObject())).thenReturn(true);

 boolean result = clientGroup.notifyMessageSent(clientMock, "message");
 Assert.assertTrue(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageSentToNullClient() {
 clientGroup.notifyMessageSent(null, "message");
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageSentToNullMessage() {
 clientGroup.notifyMessageSent(clientMock, null);
 }

 @Test
 public void testNotifyMessageReceived() {
 when(parentGroupMock.notifyMessageReceived(clientMock, Mockito.anyObject())).thenReturn(true);
 when(clientGroup.clients.contains(clientMock)).thenReturn(true);
 when(clientHandlerMock.messageReceived(clientMock, Mockito.anyObject())).thenReturn(true);

 boolean result = clientGroup.notifyMessageReceived(clientMock, "message");
 Assert.assertTrue(result);
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 ClientGroup subGroupMock = mock(ClientGroup.class);
 when(parentGroupMock.notifyMessageReceived(clientMock, Mockito.anyObject())).thenReturn(false);
 WeakReference<ClientGroup> subGroupRef = new WeakReference<>(subGroupMock);
 when(clientGroup.subGroups.contains(subGroupRef)).thenReturn(true);
 when(subGroupMock.notifyMessageReceived(clientMock, Mockito.anyObject())).thenReturn(true);

 boolean result = clientGroup.notifyMessageReceived(clientMock, "message");
 Assert.assertTrue(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageReceivedToNullClient() {
 clientGroup.notifyMessageReceived(null, "message");
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageReceivedToNullMessage() {
 clientGroup.notifyMessageReceived(clientMock, null);
 }

 @Test
 public void testNotifyExceptionCaught() {
 when(parentGroupMock.notifyExceptionCaught(clientMock, Mockito.anyObject())).thenReturn(true);
 when(clientGroup.clients.contains(clientMock)).thenReturn(true);
 when(clientHandlerMock.exceptionCaught(clientMock, Mockito.anyObject())).thenReturn(true);

 boolean result = clientGroup.notifyExceptionCaught(clientMock, new RuntimeException());
 Assert.assertTrue(result);
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 ClientGroup subGroupMock = mock(ClientGroup.class);
 when(parentGroupMock.notifyExceptionCaught(clientMock, Mockito.anyObject())).thenReturn(false);
 WeakReference<ClientGroup> subGroupRef = new WeakReference<>(subGroupMock);
 when(clientGroup.subGroups.contains(subGroupRef)).thenReturn(true);
 when(subGroupMock.notifyExceptionCaught(clientMock, Mockito.anyObject())).thenReturn(true);

 boolean result = clientGroup.notifyExceptionCaught(clientMock, new RuntimeException());
 Assert.assertTrue(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyExceptionCaughtToNullClient() {
 clientGroup.notifyExceptionCaught(null, new RuntimeException());
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 Assert.assertNotNull(subGroup);
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new HashSet<>(Arrays.asList(clientMock, clientMock, clientMock));
 clientGroup = new ClientGroup(parentGroupMock);
 clients.forEach(clientGroup::add);
 Iterator<Client> iterator = clientGroup.iterator();

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(clientMock, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(clientMock, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(clientMock, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }
}
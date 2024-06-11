import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupImpl;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler clientHandler;

 @Test
 public void testConstructor() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertNotNull(clientGroup.parentGroup());
 Assert.assertNull(clientGroup.handler);
 Assert.assertNotNull(clientGroup.clients);
 Assert.assertFalse(clientGroup.clients.isEmpty());
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 Assert.assertSame(parentGroup, childGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientHandler = new ClientHandlerImpl();
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);

 when(clientHandler.messageSent(any(Client.class), any(Object.class))).thenReturn(true);

 Assert.assertTrue(clientGroup.notifyMessageSent(client1, "testMessage"));
 Assert.assertTrue(clientGroup.notifyMessageSent(client2, "testMessage"));
 Assert.assertTrue(clientHandler.messageSentCallCount == 2);
 }

 @Test
 public void testNotifyMessageSentWhenClientNotPresent() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();

 Assert.assertFalse(clientGroup.notifyMessageSent(client1, "testMessage"));
 }

 @Test
 public void testNotifyMessageSentWhenSubGroupPresent() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(childGroup);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);
 childGroup.add(clientGroup);
 subGroup.addClientHandler(clientHandler);

 when(clientHandler.messageSent(any(Client.class), any(Object.class))).thenReturn(true);

 Assert.assertTrue(clientGroup.notifyMessageSent(client1, "testMessage"));
 Assert.assertTrue(clientGroup.notifyMessageSent(client2, "testMessage"));
 Assert.assertTrue(childGroup.notifyMessageSent(client1, "testMessage"));
 Assert.assertTrue(childGroup.notifyMessageSent(client2, "testMessage"));
 Assert.assertTrue(subGroup.notifyMessageSent(client1, "testMessage"));
 Assert.assertTrue(subGroup.notifyMessageSent(client2, "testMessage"));
 Assert.assertTrue(clientHandler.messageSentCallCount == 6);
 }

 @Test
 public void testNotifyMessageReceived() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientHandler = new ClientHandlerImpl();
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);

 when(clientHandler.messageReceived(any(Client.class), any(Object.class))).thenReturn(true);

 Assert.assertTrue(clientGroup.notifyMessageReceived(client1, "testMessage"));
 Assert.assertTrue(clientGroup.notifyMessageReceived(client2, "testMessage"));
 Assert.assertTrue(clientHandler.messageReceivedCallCount == 2);
 }

 @Test
 public void testNotifyMessageReceivedWhenClientNotPresent() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();

 Assert.assertFalse(clientGroup.notifyMessageReceived(client1, "testMessage"));
 }

 @Test
 public void testNotifyMessageReceivedWhenSubGroupPresent() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(childGroup);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);
 childGroup.add(clientGroup);
 subGroup.addClientHandler(clientHandler);

 when(clientHandler.messageReceived(any(Client.class), any(Object.class))).thenReturn(true);

 Assert.assertTrue(clientGroup.notifyMessageReceived(client1, "testMessage"));
 Assert.assertTrue(clientGroup.notifyMessageReceived(client2, "testMessage"));
 Assert.assertTrue(childGroup.notifyMessageReceived(client1, "testMessage"));
 Assert.assertTrue(childGroup.notifyMessageReceived(client2, "testMessage"));
 Assert.assertTrue(subGroup.notifyMessageReceived(client1, "testMessage"));
 Assert.assertTrue(subGroup.notifyMessageReceived(client2, "testMessage"));
 Assert.assertTrue(clientHandler.messageReceivedCallCount == 6);
 }

 @Test
 public void testNotifyExceptionCaught() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientHandler = new ClientHandlerImpl();
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);

 when(clientHandler.exceptionCaught(any(Client.class), any(Throwable.class))).thenReturn(true);

 try {
 throw new RuntimeException("Test Exception");
 } catch (RuntimeException e) {
 clientGroup.notifyExceptionCaught(client1, e);
 clientGroup.notifyExceptionCaught(client2, e);
 }
 Assert.assertTrue(clientHandler.exceptionCaughtCallCount == 2);
 }

 @Test
 public void testNotifyExceptionCaughtWhenClientNotPresent() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();

 try {
 throw new RuntimeException("Test Exception");
 } catch (RuntimeException e) {
 clientGroup.notifyExceptionCaught(client1, e);
 }
 Assert.assertFalse(clientHandler.exceptionCaughtCallCount > 0);
 }

 @Test
 public void testNotifyExceptionCaughtWhenSubGroupPresent() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(childGroup);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);
 childGroup.add(clientGroup);
 subGroup.addClientHandler(clientHandler);

 when(clientHandler.exceptionCaught(any(Client.class), any(Throwable.class))).thenReturn(true);

 try {
 throw new RuntimeException("Test Exception");
 } catch (RuntimeException e) {
 clientGroup.notifyExceptionCaught(client1, e);
 clientGroup.notifyExceptionCaught(client2, e);
 childGroup.notifyExceptionCaught(client1, e);
 childGroup.notifyExceptionCaught(client2, e);
 subGroup.notifyExceptionCaught(client1, e);
 subGroup.notifyExceptionCaught(client2, e);
 }
 Assert.assertTrue(clientHandler.exceptionCaughtCallCount == 6);
 }

 @Test
 public void testIterator() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup.add(client1);
 clientGroup.add(client2);

 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client1);
 clients.add(client2);

 Iterator<Client> iterator = clientGroup.iterator();

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client2, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSize() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup.add(client1);
 clientGroup.add(client2);

 Assert.assertEquals(2, clientGroup.size());
 }

 @Test
 public void testAdd() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup.add(client1);
 Assert.assertFalse(clientGroup.add(client1));
 Assert.assertTrue(clientGroup.size() == 1);
 clientGroup.add(client2);
 Assert.assertTrue(clientGroup.size() == 2);
 Assert.assertTrue(clientGroup.contains(client1));
 Assert.assertTrue(clientGroup.contains(client2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullClient() {
 clientGroup = new ClientGroup(null);
 Client nullClient = null;
 clientGroup.add(nullClient);
 }

 @Test
 public void testRemove() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup.add(client1);
 clientGroup.add(client2);

 Assert.assertTrue(clientGroup.contains(client1));
 Assert.assertTrue(clientGroup.contains(client2));
 Assert.assertTrue(clientGroup.remove(client1));
 Assert.assertFalse(clientGroup.contains(client1));
 Assert.assertTrue(clientGroup.size() == 1);
 Assert.assertTrue(clientGroup.contains(client2));
 }

 @Test
 public void testRemoveClientNotPresent() {
 clientGroup = new ClientGroup(null);
 client1 = new ClientImpl();

 Assert.assertFalse(clientGroup.contains(client1));
 Assert.assertFalse(clientGroup.remove(client1));
 }

 @Test
 public void testRemoveClientWithSubGroups() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(childGroup);
 client1 = new ClientImpl();
 client2 = new ClientImpl();
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 childGroup.add(clientGroup);
 subGroup.addClientHandler(clientHandler);

 Assert.assertTrue(clientGroup.contains(client1));
 Assert.assertTrue(clientGroup.contains(client2));
 ClientGroup removedClientGroup = clientGroup.remove(client1);
 Assert.assertFalse(clientGroup.contains(client1));
 Assert.assertFalse(clientGroup.size() > 1);
 Assert.assertSame(removedClientGroup, childGroup);
 }

 @Test
 public void testCreateSubGroup() {
 clientGroup = new ClientGroup(null);
 ClientGroup childGroup = clientGroup.createSubGroup();
 Assert.assertNotNull(childGroup);
 Assert.assertSame(clientGroup, childGroup.parentGroup());
 }

 @Test
 public void testCreateSubGroupWhenParentIsNull() {
 ClientGroup childGroup = new ClientGroup(null).createSubGroup();
 Assert.assertNull(childGroup.parentGroup());
 }

 @Test
 public void testAddClientHandler() {
 clientGroup = new ClientGroup(null);
 clientHandler = new ClientHandlerImpl();
 clientGroup.addClientHandler(clientHandler);
 Assert.assertSame(clientHandler, clientGroup.handler);
 }

 @Test
 public void testAddClientHandlerWhenHandlerAlreadyPresent() {
 clientGroup = new ClientGroup(null);
 clientHandler = new ClientHandlerImpl();
 clientGroup.addClientHandler(clientHandler);
 clientGroup.addClientHandler(clientHandler);
 }
}
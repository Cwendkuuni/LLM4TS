import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientGroupState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private Client client4;
 private ClientHandler handler;

 @Test
 public void testConstructor() {
 rootGroup = new ClientGroup(null);
 Assert.assertNull(rootGroup.parentGroup());
 Assert.assertTrue(rootGroup.clients.isEmpty());
 Assert.assertNull(rootGroup.handler);
 }

 @Test
 public void testParentGroup() {
 rootGroup = new ClientGroup(null);
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup.add(client3);

 Assert.assertNull(client1.parentGroup());
 Assert.assertSame(rootGroup, client2.parentGroup());
 Assert.assertSame(rootGroup, client3.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyMessageSent(client1, "testMessage"));
 Assert.assertTrue(rootGroup.notifyMessageSent(client2, "testMessage"));
 Assert.assertTrue(handler.messageSentCallCount > 0);
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 rootGroup = new ClientGroup(null);
 ClientGroup subGroup = rootGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 client3 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup.add(client3);
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyMessageSent(client1, "testMessage"));
 Assert.assertTrue(subGroup.notifyMessageSent(client3, "testMessage"));
 Assert.assertTrue(handler.messageSentCallCount > 0);
 }

 @Test
 public void testNotifyMessageSentToNullClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyMessageSent(null, "testMessage"));
 Assert.assertEquals(0, handler.messageSentCallCount);
 }

 @Test
 public void testNotifyMessageSentToNonExistentClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyMessageSent(client2, "testMessage"));
 Assert.assertEquals(0, handler.messageSentCallCount);
 }

 @Test
 public void testNotifyMessageReceived() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyMessageReceived(client1, "testMessage"));
 Assert.assertTrue(rootGroup.notifyMessageReceived(client2, "testMessage"));
 Assert.assertTrue(handler.messageReceivedCallCount > 0);
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 rootGroup = new ClientGroup(null);
 ClientGroup subGroup = rootGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 client3 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup.add(client3);
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyMessageReceived(client1, "testMessage"));
 Assert.assertTrue(subGroup.notifyMessageReceived(client3, "testMessage"));
 Assert.assertTrue(handler.messageReceivedCallCount > 0);
 }

 @Test
 public void testNotifyMessageReceivedToNullClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyMessageReceived(null, "testMessage"));
 Assert.assertEquals(0, handler.messageReceivedCallCount);
 }

 @Test
 public void testNotifyMessageReceivedToNonExistentClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyMessageReceived(client2, "testMessage"));
 Assert.assertEquals(0, handler.messageReceivedCallCount);
 }

 @Test
 public void testNotifyExceptionCaught() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 try {
 throw new RuntimeException("testException");
 } catch (RuntimeException e) {
 rootGroup.notifyExceptionCaught(client1, e);
 }

 Assert.assertTrue(handler.exceptionCaughtCallCount > 0);
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 rootGroup = new ClientGroup(null);
 ClientGroup subGroup = rootGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 client3 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup.add(client3);
 rootGroup.addClientHandler(handler);

 try {
 throw new RuntimeException("testException");
 } catch (RuntimeException e) {
 rootGroup.notifyExceptionCaught(client1, e);
 }

 Assert.assertTrue(subGroup.notifyExceptionCaught(client3, e));
 Assert.assertTrue(handler.exceptionCaughtCallCount > 0);
 }

 @Test
 public void testNotifyExceptionCaughtToNullClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.addClientHandler(handler);

 try {
 throw new RuntimeException("testException");
 } catch (RuntimeException e) {
 rootGroup.notifyExceptionCaught(null, e);
 }

 Assert.assertEquals(0, handler.exceptionCaughtCallCount);
 }

 @Test
 public void testNotifyExceptionCaughtToNonExistentClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 handler = new DummyHandler();
 rootGroup.add(client1);
 rootGroup.addClientHandler(handler);

 try {
 throw new RuntimeException("testException");
 } catch (RuntimeException e) {
 rootGroup.notifyExceptionCaught(client2, e);
 }

 Assert.assertEquals(0, handler.exceptionCaughtCallCount);
 }

 @Test
 public void testIterator() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 rootGroup.add(client2);

 Set<Client> clients = new HashSet<>(Arrays.asList(client1, client2));
 Iterator<Client> iterator = rootGroup.iterator();

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client2, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContains() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.contains(client1));
 Assert.assertTrue(rootGroup.contains(client2));
 Assert.assertFalse(rootGroup.contains(new Client()));
 }

 @Test
 public void testSize() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertEquals(2, rootGroup.size());
 }

 @Test
 public void testAddClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(client1);

 Assert.assertTrue(rootGroup.add(client1));
 Assert.assertTrue(subGroup.contains(client1));
 Assert.assertEquals(2, rootGroup.size());
 Assert.assertEquals(1, subGroup.size());
 }

 @Test
 public void testAddClientToExistingClientInSubGroup() {
 rootGroup = new ClientGroup(null);
 ClientGroup subGroup = rootGroup.createSubGroup();
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 subGroup.add(client2);

 Assert.assertFalse(rootGroup.add(client2));
 Assert.assertTrue(subGroup.contains(client2));
 Assert.assertEquals(2, rootGroup.size());
 Assert.assertEquals(1, subGroup.size());
 }

 @Test
 public void testAddClientToNullClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();

 Assert.assertFalse(rootGroup.add(null));
 Assert.assertEquals(0, rootGroup.size());
 }

 @Test
 public void testAddClientToExistentClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertFalse(rootGroup.add(client1));
 Assert.assertEquals(2, rootGroup.size());
 }

 @Test
 public void testRemoveClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.remove(client1));
 Assert.assertFalse(rootGroup.contains(client1));
 Assert.assertEquals(1, rootGroup.size());
 }

 @Test
 public void testRemoveClientFromNullClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();

 Assert.assertFalse(rootGroup.remove(null));
 Assert.assertEquals(0, rootGroup.size());
 }

 @Test
 public void testRemoveClientFromNonExistentClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();

 Assert.assertFalse(rootGroup.remove(client1));
 Assert.assertEquals(0, rootGroup.size());
 }

 @Test
 public void testRemoveClientFromExistentClient() {
 rootGroup = new ClientGroup(null);
 client1 = new Client();
 client2 = new Client();
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.remove(client1));
 Assert.assertFalse(rootGroup.contains(client1));
 Assert.assertEquals(1, rootGroup.size());
 }

 @Test
 public void testCreateSubGroup() {
 rootGroup = new ClientGroup(null);

 ClientGroup subGroup = rootGroup.createSubGroup();

 Assert.assertNotNull(subGroup);
 Assert.assertSame(rootGroup, subGroup.parentGroup());
 Assert.assertTrue(rootGroup.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test
 public void testAddClientHandler() {
 rootGroup = new ClientGroup(null);
 ClientHandler handler = new DummyHandler();
 rootGroup.addClientHandler(handler);

 Assert.assertSame(handler, rootGroup.handler);
 }

 @Test
 public void testAddClientHandlerToExistingClientHandler() {
 rootGroup = new ClientGroup(null);
 ClientHandler handler1 = new DummyHandler();
 ClientHandler handler2 = new DummyHandler();
 rootGroup.addClientHandler(handler1);
 rootGroup.addClientHandler(handler2);

 Assert.assertSame(handler1, rootGroup.handler);
 Assert.assertSame(handler2, rootGroup.handler);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddClientHandlerToNullClientHandler() {
 rootGroup = new ClientGroup(null);
 ClientHandler handler = null;

 try {
 rootGroup.addClientHandler(handler);
 } catch (IllegalStateException e) {
 // expected
 }

 Assert.assertNull(rootGroup.handler);
 }

 @Test
 public void testClientAdded() {
 rootGroup = new ClientGroup(null);
 ClientHandler handler = new DummyHandler();
 Client client = new Client();

 rootGroup.add(client);
 rootGroup.addClientHandler(handler);

 Assert.assertEquals(1, handler.clientAddedCallCount);
 }

 @Test
 public void testClientRemoved() {
 rootGroup = new ClientGroup(null);
 ClientHandler handler = new DummyHandler();
 Client client = new Client();

 rootGroup.add(client);
 rootGroup.addClientHandler(handler);
 rootGroup.remove(client);

 Assert.assertEquals(1, handler.clientRemovedCallCount);
 }

 private static class DummyHandler implements ClientHandler {
 private int messageSentCallCount = 0;
 private int messageReceivedCallCount = 0;
 private int exceptionCaughtCallCount = 0;
 private int clientAddedCallCount = 0;
 private int clientRemovedCallCount = 0;

 @Override
 public void messageSent(Client sender, Object message) {
 messageSentCallCount++;
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 messageReceivedCallCount++;
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 exceptionCaughtCallCount++;
 }

 @Override
 public void clientAdded(Client client) {
 clientAddedCallCount++;
 }

 @Override
 public void clientRemoved(Client client) {
 clientRemovedCallCount++;
 }
 }
}
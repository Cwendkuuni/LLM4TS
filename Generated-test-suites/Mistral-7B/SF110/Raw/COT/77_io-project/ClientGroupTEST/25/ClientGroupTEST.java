import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.WeakHashMap;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientGroupState;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private ClientHandler handler;
 private Client client1;
 private Client client2;
 private Client client3;

 @Test
 public void testConstructor() {
 rootGroup = new ClientGroup(null);
 Assert.assertNotNull(rootGroup);
 Assert.assertNull(rootGroup.parentGroup());
 Assert.assertEquals(0, rootGroup.size());
 Assert.assertFalse(rootGroup.contains(client1));
 }

 @Test
 public void testParentGroup() {
 rootGroup.add(client1);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();

 Assert.assertEquals(rootGroup, subGroup1.parentGroup());
 Assert.assertEquals(subGroup1, subGroup2.parentGroup());
 Assert.assertEquals(null, subGroup2.parentGroup().parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyMessageSent(client1, "test message"));
 Assert.assertTrue(rootGroup.notifyMessageSent(client2, "test message"));
 Assert.assertTrue(subGroup1.notifyMessageSent(client1, "test message"));
 Assert.assertTrue(subGroup1.notifyMessageSent(client2, "test message"));
 Assert.assertTrue(subGroup2.notifyMessageSent(client1, "test message"));
 Assert.assertTrue(subGroup2.notifyMessageSent(client2, "test message"));
 }

 @Test
 public void testNotifyMessageSent_ClientNotInGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyMessageSent(client3, "test message"));
 }

 @Test
 public void testNotifyMessageSent_HandlerNull() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.notifyMessageSent(client1, "test message"));
 Assert.assertTrue(rootGroup.notifyMessageSent(client2, "test message"));
 Assert.assertFalse(rootGroup.handler.messageSentCalled);
 }

 @Test
 public void testNotifyMessageReceived() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyMessageReceived(client1, "test message"));
 Assert.assertTrue(rootGroup.notifyMessageReceived(client2, "test message"));
 Assert.assertTrue(subGroup1.notifyMessageReceived(client1, "test message"));
 Assert.assertTrue(subGroup1.notifyMessageReceived(client2, "test message"));
 Assert.assertTrue(subGroup2.notifyMessageReceived(client1, "test message"));
 Assert.assertTrue(subGroup2.notifyMessageReceived(client2, "test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotInGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyMessageReceived(client3, "test message"));
 }

 @Test
 public void testNotifyMessageReceived_HandlerNull() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.notifyMessageReceived(client1, "test message"));
 Assert.assertTrue(rootGroup.notifyMessageReceived(client2, "test message"));
 Assert.assertFalse(rootGroup.handler.messageReceivedCalled);
 }

 @Test
 public void testNotifyExceptionCaught() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 Assert.assertTrue(rootGroup.notifyExceptionCaught(client2, new RuntimeException()));
 Assert.assertTrue(subGroup1.notifyExceptionCaught(client1, new RuntimeException()));
 Assert.assertTrue(subGroup1.notifyExceptionCaught(client2, new RuntimeException()));
 Assert.assertTrue(subGroup2.notifyExceptionCaught(client1, new RuntimeException()));
 Assert.assertTrue(subGroup2.notifyExceptionCaught(client2, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotInGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // do nothing
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertFalse(rootGroup.notifyExceptionCaught(client3, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_HandlerNull() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 Assert.assertTrue(rootGroup.notifyExceptionCaught(client2, new RuntimeException()));
 Assert.assertFalse(rootGroup.handler.exceptionCaughtCalled);
 }

 @Test
 public void testAdd() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.contains(client1));
 Assert.assertTrue(rootGroup.contains(client2));
 Assert.assertEquals(2, rootGroup.size());
 }

 @Test
 public void testAdd_DuplicateClient() {
 rootGroup.add(client1);
 rootGroup.add(client1);

 Assert.assertEquals(1, rootGroup.size());
 Assert.assertTrue(rootGroup.contains(client1));
 }

 @Test
 public void testAdd_NullClient() {
 rootGroup.add(client1);
 rootGroup.add(null);

 Assert.assertEquals(1, rootGroup.size());
 Assert.assertFalse(rootGroup.contains(null));
 }

 @Test
 public void testRemove() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.remove(client1));
 Assert.assertFalse(rootGroup.contains(client1));
 Assert.assertEquals(1, rootGroup.size());
 }

 @Test
 public void testRemove_ClientNotInGroup() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertFalse(rootGroup.remove(client3));
 Assert.assertFalse(rootGroup.contains(client3));
 Assert.assertEquals(2, rootGroup.size());
 }

 @Test
 public void testRemove_EmptyGroup() {
 ClientGroup emptyGroup = new ClientGroup(null);

 Assert.assertFalse(emptyGroup.remove(client1));
 Assert.assertFalse(emptyGroup.contains(client1));
 Assert.assertEquals(0, emptyGroup.size());
 }

 @Test
 public void testCreateSubGroup() {
 rootGroup.add(client1);

 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = rootGroup.createSubGroup();

 Assert.assertEquals(2, rootGroup.size());
 Assert.assertEquals(0, subGroup1.size());
 Assert.assertEquals(0, subGroup2.size());
 }

 @Test
 public void testCreateSubGroup_NullParentGroup() {
 ClientGroup nullGroup = new ClientGroup(null);

 Assert.assertThrows(IllegalArgumentException.class, () -> nullGroup.createSubGroup());
 }

 @Test
 public void testIterator() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Iterator<Client> it = rootGroup.iterator();

 Assert.assertTrue(it.hasNext());
 Assert.assertEquals(client1, it.next());
 Assert.assertTrue(it.hasNext());
 Assert.assertEquals(client2, it.next());
 Assert.assertFalse(it.hasNext());
 }

 @Test
 public void testContains() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertTrue(rootGroup.contains(client1));
 Assert.assertTrue(rootGroup.contains(client2));
 Assert.assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testSize() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 Assert.assertEquals(2, rootGroup.size());
 }

 @Test
 public void testAddClientHandler() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 ClientGroupState state = new ClientGroupState() {
 @Override
 public void setClientGroupState(String state) {
 // do nothing
 }

 @Override
 public ClientGroupState getClientGroupState() {
 return new ClientGroupState("TestState");
 }
 };
 rootGroup.setState(state);
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertEquals("TestState", rootGroup.getState().getClientGroupState());
 }

 @Test
 public void testAddClientHandler_NullHandler() {
 rootGroup.add(client1);
 rootGroup.add(client2);

 handler = null;
 rootGroup.addClientHandler(handler);

 Assert.assertNull(rootGroup.handler);
 }

 @Test
 public void testState() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 ClientGroupState state = new ClientGroupState() {
 @Override
 public void setClientGroupState(String state) {
 this.state = state;
 }

 @Override
 public String getClientGroupState() {
 return state;
 }
 };
 rootGroup.setState(state);
 }
 };
 rootGroup.addClientHandler(handler);

 Assert.assertEquals("DefaultState", rootGroup.getState().getClientGroupState());
 }
}
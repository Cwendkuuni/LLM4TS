import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientIterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Test
 public void testConstructor() {
 group = new ClientGroup(null);
 Assert.assertNull(group.parentGroup());
 Assert.assertTrue(group.clients.isEmpty());
 Assert.assertNull(group.handler);
 }

 @Test
 public void testParentGroup() {
 group = new ClientGroup(new ClientGroup(null));
 ClientGroup parent = group.parentGroup();
 Assert.assertNotNull(parent);
 Assert.assertEquals(group, parent.subGroups.iterator().next().get());
 }

 @Test
 public void testNotifyMessageSent_ClientNotInGroup_ReturnsFalse() {
 group = new ClientGroup(null);
 client1 = new Client();
 Assert.assertFalse(group.notifyMessageSent(client1, "Test Message"));
 }

 @Test
 public void testNotifyMessageSent_ClientInGroup_ReturnsTrue() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertTrue(group.notifyMessageSent(client1, "Test Message"));
 }

 @Test
 public void testNotifyMessageSent_ClientInSubGroup_ReturnsTrue() {
 ClientGroup subGroup = group.createSubGroup();
 group.add(client1);
 subGroup.add(client2);
 Assert.assertTrue(group.notifyMessageSent(client2, "Test Message"));
 }

 @Test
 public void testNotifyMessageSent_MultipleSubGroups_ReturnsTrue() {
 ClientGroup subGroup1 = group.createSubGroup();
 ClientGroup subGroup2 = subGroup1.createSubGroup();
 group.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 Assert.assertTrue(group.notifyMessageSent(client3, "Test Message"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageSent_NullMessage_ThrowsException() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertFalse(group.notifyMessageSent(client1, null));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotInGroup_ReturnsFalse() {
 group = new ClientGroup(null);
 client1 = new Client();
 Assert.assertFalse(group.notifyExceptionCaught(client1, new RuntimeException("Test Exception")));
 }

 @Test
 public void testNotifyExceptionCaught_ClientInGroup_ReturnsTrue() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertTrue(group.notifyExceptionCaught(client1, new RuntimeException("Test Exception")));
 }

 @Test
 public void testNotifyExceptionCaught_ClientInSubGroup_ReturnsTrue() {
 ClientGroup subGroup = group.createSubGroup();
 group.add(client1);
 subGroup.add(client2);
 Assert.assertTrue(group.notifyExceptionCaught(client2, new RuntimeException("Test Exception")));
 }

 @Test
 public void testNotifyExceptionCaught_MultipleSubGroups_ReturnsTrue() {
 ClientGroup subGroup1 = group.createSubGroup();
 ClientGroup subGroup2 = subGroup1.createSubGroup();
 group.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 Assert.assertTrue(group.notifyExceptionCaught(client3, new RuntimeException("Test Exception")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyExceptionCaught_NullClient_ThrowsException() {
 group = new ClientGroup(null);
 Assert.assertFalse(group.notifyExceptionCaught(null, new RuntimeException("Test Exception")));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyExceptionCaught_NullHandler_ThrowsException() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertFalse(group.notifyExceptionCaught(client1, null));
 }

 @Test
 public void testIterator() {
 group = new ClientGroup(null);
 group.add(client1);
 group.add(client2);
 ClientIterator iterator = group.iterator();
 Assert.assertFalse(iterator.hasNext());
 group.add(client3);
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client1, iterator.next());
 Assert.assertEquals(client2, iterator.next());
 Assert.assertEquals(client3, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContains_ClientNotInGroup_ReturnsFalse() {
 group = new ClientGroup(null);
 client1 = new Client();
 Assert.assertFalse(group.contains(client1));
 }

 @Test
 public void testContains_ClientInGroup_ReturnsTrue() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertTrue(group.contains(client1));
 }

 @Test
 public void testSize_EmptyGroup_ReturnsZero() {
 group = new ClientGroup(null);
 Assert.assertEquals(0, group.size());
 }

 @Test
 public void testSize_GroupWithClients_ReturnsCorrectSize() {
 group = new ClientGroup(null);
 group.add(client1);
 group.add(client2);
 Assert.assertEquals(2, group.size());
 }

 @Test
 public void testAdd_ClientAlreadyInParentGroup_ReturnsFalse() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertFalse(group.add(client1));
 }

 @Test
 public void testAdd_ClientNotInParentGroup_ReturnsTrue() {
 group = new ClientGroup(null);
 ClientGroup parent = new ClientGroup(null);
 parent.add(client1);
 Assert.assertFalse(group.add(client1));
 group.add(client2);
 Assert.assertTrue(group.add(client1));
 }

 @Test
 public void testRemove_ClientNotInGroup_ReturnsFalse() {
 group = new ClientGroup(null);
 client1 = new Client();
 Assert.assertFalse(group.remove(client1));
 }

 @Test
 public void testRemove_ClientInGroup_ReturnsTrue() {
 group = new ClientGroup(null);
 group.add(client1);
 Assert.assertTrue(group.remove(client1));
 }

 @Test
 public void testRemove_ClientNotPresentInSubGroups_ReturnsTrue() {
 ClientGroup subGroup = group.createSubGroup();
 group.add(client1);
 subGroup.add(client1);
 Assert.assertTrue(group.remove(client1));
 }

 @Test
 public void testRemove_ClientPresentInSubGroups_ReturnsTrue() {
 ClientGroup subGroup = group.createSubGroup();
 group.add(client1);
 subGroup.add(client1);
 group.remove(client1);
 Assert.assertFalse(subGroup.contains(client1));
 }

 @Test
 public void testCreateSubGroup_NullParentGroup_ThrowsException() {
 Assert.assertThrows(NullPointerException.class, () -> new ClientGroup(null)).getMessage();
 }

 @Test
 public void testCreateSubGroup_EmptyGroup_ReturnsNewInstance() {
 group = new ClientGroup(null);
 ClientGroup subGroup = group.createSubGroup();
 Assert.assertNotNull(subGroup);
 }

 @Test
 public void testCreateSubGroup_GroupWithClients_ReturnsNewInstance() {
 group = new ClientGroup(null);
 group.add(client1);
 group.add(client2);
 ClientGroup subGroup = group.createSubGroup();
 Assert.assertNotNull(subGroup);
 }

 @Test
 public void testAddClientHandler_NullHandler_ThrowsException() {
 group = new ClientGroup(null);
 Assert.assertThrows(NullPointerException.class, () -> group.addClientHandler(null)).getMessage();
 }

 @Test
 public void testAddClientHandler_HandlerAlreadySet_DoesNotThrowException() {
 group = new ClientGroup(null);
 ClientHandler handler1 = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 }
 };
 group.addClientHandler(handler1);
 ClientHandler handler2 = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 }
 };
 group.addClientHandler(handler2);
 }

 @Test
 public void testAddClientHandler_HandlerSet_InvokesHandlerMethods() {
 group = new ClientGroup(null);
 ClientHandler handler = new ClientHandler() {
 private int count = 0;
 @Override
 public void clientAdded(Client client) {
 count++;
 }
 };
 group.addClientHandler(handler);
 group.add(client1);
 Assert.assertEquals(1, handler.count);
 }
}
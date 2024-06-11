import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private Client client1;
 private Client client2;
 private ClientHandler handler;

 @Before
 public void setUp() {
 group = new ClientGroup(null);
 client = new Client("Client 1");
 client1 = new Client("Client 1.1");
 client2 = new Client("Client 1.2");
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client client, Object message) {
 // Do nothing
 }

 @Override
 public void messageSent(Client client, Object message) {
 // Do nothing
 }

 @Override
 public void clientAdded(Client client) {
 // Do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // Do nothing
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // Do nothing
 }
 };
 group.addClientHandler(handler);
 }

 @Test
 public void testConstructor() {
 // Basic test for constructor
 Assert.assertNotNull(group);
 }

 @Test
 public void testParentGroup() {
 // Test getter method for parentGroup
 ClientGroup parentGroup = new ClientGroup(null);
 group.parentGroup = parentGroup;
 ClientGroup returnedParentGroup = group.parentGroup();
 Assert.assertEquals(parentGroup, returnedParentGroup);
 }

 @Test
 public void testNotifyMessageSent() {
 // Basic test for notifyMessageSent
 group.add(client);
 group.add(client1);
 group.add(client2);
 group.notifyMessageSent(client, "Test message");
 Assert.assertTrue(handler.isMessageSentCalled);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageSentWhenClientNotInGroup() {
 group.notifyMessageSent(client, "Test message");
 }

 @Test
 public void testNotifyMessageSentInSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 subGroup.add(client);
 group.subGroups.add(new WeakReference<>(subGroup));
 group.notifyMessageSent(client, "Test message");
 Assert.assertTrue(handler.isMessageSentCalled);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageSentInNullHandler() {
 group.handler = null;
 group.notifyMessageSent(client, "Test message");
 }

 @Test
 public void testNotifyMessageReceived() {
 group.add(client);
 group.add(client1);
 group.add(client2);
 group.notifyMessageReceived(client, "Test message");
 Assert.assertTrue(handler.isMessageReceivedCalled);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageReceivedWhenClientNotInGroup() {
 group.notifyMessageReceived(client, "Test message");
 }

 @Test
 public void testNotifyMessageReceivedInSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 subGroup.add(client);
 group.subGroups.add(new WeakReference<>(subGroup));
 group.notifyMessageReceived(client, "Test message");
 Assert.assertTrue(handler.isMessageReceivedCalled);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageReceivedInNullHandler() {
 group.handler = null;
 group.notifyMessageReceived(client, "Test message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 group.add(client);
 group.add(client1);
 group.add(client2);
 Assert.assertFalse(handler.isExceptionCaughtCalled);
 try {
 throw new RuntimeException();
 } catch (Exception e) {
 group.notifyExceptionCaught(client, e);
 }
 Assert.assertTrue(handler.isExceptionCaughtCalled);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyExceptionCaughtWhenClientNotInGroup() {
 group.notifyExceptionCaught(client, new RuntimeException());
 }

 @Test
 public void testNotifyExceptionCaughtInSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 subGroup.add(client);
 group.subGroups.add(new WeakReference<>(subGroup));
 try {
 throw new RuntimeException();
 } catch (Exception e) {
 group.notifyExceptionCaught(client, e);
 }
 Assert.assertTrue(handler.isExceptionCaughtCalled);
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyExceptionCaughtInNullHandler() {
 group.handler = null;
 group.notifyExceptionCaught(client, new RuntimeException());
 }

 @Test
 public void testContains() {
 group.add(client);
 Assert.assertTrue(group.contains(client));
 }

 @Test
 public void testContainsWhenClientNotInGroup() {
 Assert.assertFalse(group.contains(client1));
 }

 @Test
 public void testSize() {
 group.add(client);
 group.add(client1);
 group.add(client2);
 Assert.assertEquals(group.size(), 3);
 }

 @Test
 public void testAdd() {
 Set<Client> clientsCopy = new HashSet<>(Arrays.asList(client, client1, client2));
 group.add(client);
 group.add(client1);
 group.add(client2);
 Assert.assertEquals(group.size(), 3);
 Assert.assertFalse(group.add(client)); // Should not add duplicate
 Assert.assertEquals(group, clientsCopy);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWhenNullClient() {
 group.add(null);
 }

 @Test
 public void testRemove() {
 group.add(client);
 group.add(client1);
 group.add(client2);
 Assert.assertEquals(group.size(), 3);
 group.remove(client);
 Assert.assertEquals(group.size(), 2);
 Assert.assertFalse(group.contains(client));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveWhenClientNotInGroup() {
 group.remove(client1);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 Assert.assertNotNull(subGroup);
 Assert.assertFalse(subGroup.contains(client));
 Assert.assertTrue(subGroup.contains(client1));
 Assert.assertFalse(group.contains(client1));
 Assert.assertTrue(group.contains(client));
 }

 @Test
 public void testIterator() {
 List<Client> clients = new CopyOnWriteArrayList<>();
 for (Client c : group) {
 clients.add(c);
 }
 Assert.assertEquals(group.size(), clients.size());
 Assert.assertEquals(group, clients);
 }
}
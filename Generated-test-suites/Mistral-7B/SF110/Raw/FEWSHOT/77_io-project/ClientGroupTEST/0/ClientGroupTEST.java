package ioproject.server.network;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {

 ClientGroup createTestGroup() {
 ClientGroup testGroup = new ClientGroup(null);
 return testGroup;
 }

 @Test
 public void testIterator() {
 ClientGroup testGroup = createTestGroup();
 Client client1 = new Client("1");
 Client client2 = new Client("2");
 testGroup.add(client1);
 testGroup.add(client2);

 Set<Client> clients = new HashSet<>(Arrays.asList(client1, client2));
 Iterator<Client> iterator = testGroup.iterator();

 Assert.assertEquals(clients.size(), testGroup.size());

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client2, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContains() {
 ClientGroup testGroup = createTestGroup();
 Client client1 = new Client("1");
 Client client2 = new Client("2");
 testGroup.add(client1);
 testGroup.add(client2);

 Assert.assertTrue(testGroup.contains(client1));
 Assert.assertTrue(testGroup.contains(client2));
 Assert.assertFalse(testGroup.contains(new Client("3")));
 }

 @Test
 public void testSize() {
 ClientGroup testGroup = createTestGroup();
 Client client1 = new Client("1");
 Client client2 = new Client("2");
 testGroup.add(client1);
 testGroup.add(client2);

 Assert.assertEquals(2, testGroup.size());
 }

 @Test
 public void testAddClient() {
 ClientGroup testGroup = createTestGroup();
 Client client1 = new Client("1");
 Client client2 = new Client("2");

 Assert.assertFalse(testGroup.add(client1));
 Assert.assertFalse(testGroup.add(client2));

 testGroup.add(client1);
 testGroup.add(client2);

 Assert.assertTrue(testGroup.contains(client1));
 Assert.assertTrue(testGroup.contains(client2));
 }

 @Test
 public void testRemoveClient() {
 ClientGroup testGroup = createTestGroup();
 Client client1 = new Client("1");
 Client client2 = new Client("2");
 testGroup.add(client1);
 testGroup.add(client2);

 Assert.assertEquals(2, testGroup.size());

 testGroup.remove(client1);
 Assert.assertEquals(1, testGroup.size());

 Assert.assertFalse(testGroup.contains(client1));
 Assert.assertTrue(testGroup.contains(client2));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup testGroup = createTestGroup();
 ClientGroup subGroup = testGroup.createSubGroup();

 Assert.assertNotNull(subGroup.parentGroup());
 Assert.assertEquals(testGroup, subGroup.parentGroup());
 }

 @Test
 public void testMessageSent() {
 ClientGroup parentGroup = createTestGroup();
 ClientGroup childGroup = parentGroup.createSubGroup();
 Client client = new Client("1");
 ClientHandler handler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 Assert.assertEquals(client, client);
 Assert.assertEquals("test message", message);
 }
 };

 parentGroup.addClientHandler(handler);
 childGroup.addClientHandler(handler);
 childGroup.add(client);

 Assert.assertFalse(parentGroup.notifyMessageSent(client, "test message"));
 Assert.assertTrue(childGroup.notifyMessageSent(client, "test message"));
 }

 @Test
 public void testMessageReceived() {
 ClientGroup parentGroup = createTestGroup();
 ClientGroup childGroup = parentGroup.createSubGroup();
 Client client = new Client("1");
 ClientHandler handler = new ClientHandler() {
 @Override
 public void messageReceived(Client client, Object message) {
 Assert.assertEquals(client, client);
 Assert.assertEquals("test message", message);
 }
 };

 parentGroup.addClientHandler(handler);
 childGroup.addClientHandler(handler);
 childGroup.add(client);

 Assert.assertFalse(parentGroup.notifyMessageReceived(client, "test message"));
 Assert.assertTrue(childGroup.notifyMessageReceived(client, "test message"));
 }

 @Test
 public void testExceptionCaught() {
 ClientGroup parentGroup = createTestGroup();
 ClientGroup childGroup = parentGroup.createSubGroup();
 Client client = new Client("1");
 ClientHandler handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 Assert.assertEquals(client, client);
 Assert.assertEquals("test exception", cause.getMessage());
 }
 };

 parentGroup.addClientHandler(handler);
 childGroup.addClientHandler(handler);
 childGroup.add(client);

 Assert.assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException("test exception")));
 Assert.assertTrue(childGroup.notifyExceptionCaught(client, new RuntimeException("test exception")));
 }
}
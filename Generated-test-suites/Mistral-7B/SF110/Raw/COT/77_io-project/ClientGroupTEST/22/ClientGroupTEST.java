import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {

 private Client client1;
 private Client client2;
 private Client client3;
 private Client client4;
 private ClientGroup clientGroup;
 private ClientGroup subGroup;
 private ClientHandler handler;

 @Test
 public void testConstructor() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertNull(clientGroup.parentGroup());
 Assert.assertNotNull(clientGroup.subGroups);
 Assert.assertNotNull(clientGroup.clients);
 Assert.assertNull(clientGroup.handler);
 }

 @Test
 public void testParentGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 ClientGroup parentGroup = new ClientGroup(clientGroup);
 clientGroup.add(client2);

 Assert.assertSame(clientGroup, parentGroup.parentGroup());
 Assert.assertNotSame(clientGroup, parentGroup);
 Assert.assertEquals(2, parentGroup.size());
 Assert.assertTrue(parentGroup.contains(client1));
 Assert.assertTrue(parentGroup.contains(client2));
 }

 @Test
 public void testNotifyMessageSent_EmptyGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 Assert.fail();
 }
 };
 clientGroup.addClientHandler(handler);

 Assert.assertFalse(clientGroup.notifyMessageSent(client1, "test message"));
 }

 @Test
 public void testNotifyMessageSent_ClientNotInGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 Assert.fail();
 }
 };
 clientGroup.addClientHandler(handler);

 Assert.assertFalse(clientGroup.notifyMessageSent(client3, "test message"));
 }

 @Test
 public void testNotifyMessageSent_ClientInGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 Assert.assertEquals(client1, sender);
 Assert.assertEquals("test message", message);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.notifyMessageSent(client1, "test message");
 }

 @Test
 public void testNotifyMessageSent_ClientInSubGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client3);
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 Assert.assertEquals(client3, sender);
 Assert.assertEquals("test message", message);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.notifyMessageSent(client1, "test message");
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageSent_HandlerIsNull() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.notifyMessageSent(client1, "test message");
 }

 @Test
 public void testNotifyMessageReceived_EmptyGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 Assert.fail();
 }
 };
 clientGroup.addClientHandler(handler);

 Assert.assertFalse(clientGroup.notifyMessageReceived(client1, "test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotInGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 Assert.fail();
 }
 };
 clientGroup.addClientHandler(handler);

 Assert.assertFalse(clientGroup.notifyMessageReceived(client3, "test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientInGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 Assert.assertEquals(client1, sender);
 Assert.assertEquals("test message", message);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.notifyMessageReceived(client1, "test message");
 }

 @Test
 public void testNotifyMessageReceived_ClientInSubGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client3);
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 Assert.assertEquals(client3, sender);
 Assert.assertEquals("test message", message);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.notifyMessageReceived(client1, "test message");
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyMessageReceived_HandlerIsNull() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.notifyMessageReceived(client1, "test message");
 }

 @Test
 public void testNotifyExceptionCaught_EmptyGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 Assert.fail();
 }
 };
 clientGroup.addClientHandler(handler);

 Assert.assertFalse(clientGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotInGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 Assert.fail();
 }
 };
 clientGroup.addClientHandler(handler);

 Assert.assertFalse(clientGroup.notifyExceptionCaught(client3, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientInGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 Assert.assertEquals(client1, sender);
 Assert.assertSame(new RuntimeException(), cause);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.notifyExceptionCaught(client1, new RuntimeException());
 }

 @Test
 public void testNotifyExceptionCaught_ClientInSubGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client3);
 handler = new ClientHandler() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 Assert.assertEquals(client3, sender);
 Assert.assertSame(new RuntimeException(), cause);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.notifyExceptionCaught(client1, new RuntimeException());
 }

 @Test(expected = IllegalStateException.class)
 public void testNotifyExceptionCaught_HandlerIsNull() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.notifyExceptionCaught(client1, new RuntimeException());
 }

 @Test
 public void testIterator() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);

 Set<Client> clients = new HashSet<Client>(Arrays.asList(client1, client2, client3, client4));
 Iterator<Client> iterator = clientGroup.iterator();

 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client1, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client2, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client3, iterator.next());
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(client4, iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContains() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);

 Assert.assertTrue(clientGroup.contains(client1));
 Assert.assertTrue(clientGroup.contains(client2));
 Assert.assertTrue(clientGroup.contains(client3));
 Assert.assertTrue(clientGroup.contains(client4));
 Assert.assertFalse(clientGroup.contains(new Client()));
 }

 @Test
 public void testSize() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);

 Assert.assertEquals(4, clientGroup.size());
 }

 @Test
 public void testAdd() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 Assert.assertEquals(client4, client);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.add(client4);
 }

 @Test
 public void testRemove() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);
 handler = new ClientHandler() {
 @Override
 public void clientRemoved(Client client) {
 Assert.assertEquals(client2, client);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.remove(client2);
 }

 @Test
 public void testCreateSubGroup() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);

 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client5);
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 Assert.assertEquals(client5, client);
 }
 };
 clientGroup.addClientHandler(handler);

 clientGroup.createSubGroup().add(client5);
 }

 @Test
 public void testAddClientHandler() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);

 ClientHandler handler1 = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 Assert.fail();
 }
 };
 ClientHandler handler2 = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 Assert.assertEquals(client4, client);
 }
 };
 clientGroup.addClientHandler(handler1);
 clientGroup.addClientHandler(handler2);
 }

 @Test
 public void testRemoveClientHandler() {
 clientGroup = new ClientGroup(null);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);
 ClientHandler handler1 = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 Assert.fail();
 }
 };
 ClientHandler handler2 = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 Assert.assertEquals(client4, client);
 }
 };
 clientGroup.addClientHandler(handler1);
 clientGroup.addClientHandler(handler2);
 clientGroup.removeClientHandler(handler1);
 }
}
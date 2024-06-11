import org.junit.Assert;
import org.junit.Test;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.Client;
import ioproject.server.network.ClientHandler;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakReference;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private Client client2;
 private ClientHandler clientHandler;

 @Before
 public void setUp() {
 clientGroup = new ClientGroup(null);
 client = new Client("client1");
 client2 = new Client("client2");
 clientHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // do nothing
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // do nothing
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // do nothing
 }

 @Override
 public void clientAdded(Client client) {
 // do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // do nothing
 }
 };
 clientGroup.addClientHandler(clientHandler);
 }

 @Test
 public void testConstructor() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertNotNull(clientGroup.parentGroup);
 Assert.assertNotNull(clientGroup.subGroups);
 Assert.assertNotNull(clientGroup.clients);
 Assert.assertNull(clientGroup.handler);
 }

 @Test
 public void testContains() {
 clientGroup.add(client);
 Assert.assertTrue(clientGroup.contains(client));
 Assert.assertFalse(clientGroup.contains(client2));
 }

 @Test
 public void testSize() {
 clientGroup.add(client);
 clientGroup.add(client2);
 Assert.assertEquals(2, clientGroup.size());
 }

 @Test
 public void testAddClient() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 clientGroup.add(client);
 Assert.assertTrue(clientGroup.contains(client));
 Assert.assertFalse(subGroup.contains(client));
 subGroup.add(client);
 Assert.assertTrue(subGroup.contains(client));
 }

 @Test
 public void testRemoveClient() {
 clientGroup.add(client);
 clientGroup.add(client2);
 Assert.assertTrue(clientGroup.remove(client));
 Assert.assertFalse(clientGroup.contains(client));
 Assert.assertTrue(clientGroup.contains(client2));
 }

 @Test
 public void testNotifyMessageSent() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);
 clientGroup.notifyMessageSent(client, "test message");
 Assert.assertTrue(clientHandler.isMessageSentCalled);
 }

 @Test
 public void testNotifyMessageSent_clientNotInGroup() {
 clientGroup.notifyMessageSent(client2, "test message");
 Assert.assertFalse(clientHandler.isMessageSentCalled);
 }

 @Test
 public void testNotifyMessageSent_handlerIsNull() {
 clientGroup.notifyMessageSent(client, "test message");
 }

 @Test
 public void testNotifyMessageReceived() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addClientHandler(clientHandler);
 clientGroup.notifyMessageReceived(client, "test message");
 Assert.assertTrue(clientHandler.isMessageReceivedCalled);
 }

 @Test
 public void testNotifyMessageReceived_clientNotInGroup() {
 clientGroup.notifyMessageReceived(client2, "test message");
 Assert.assertFalse(clientHandler.isMessageReceivedCalled);
 }

 @Test
 public void testNotifyMessageReceived_handlerIsNull() {
 clientGroup.notifyMessageReceived(client, "test message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 clientGroup.add(client);
 clientGroup.addClientHandler(clientHandler);
 clientGroup.notifyExceptionCaught(client, new RuntimeException("test exception"));
 Assert.assertTrue(clientHandler.isExceptionCaughtCalled);
 }

 @Test
 public void testNotifyExceptionCaught_clientNotInGroup() {
 clientGroup.notifyExceptionCaught(client2, new RuntimeException("test exception"));
 Assert.assertFalse(clientHandler.isExceptionCaughtCalled);
 }

 @Test
 public void testNotifyExceptionCaught_handlerIsNull() {
 clientGroup.notifyExceptionCaught(client, new RuntimeException("test exception"));
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new LinkedHashSet<>();clients.add(client); clients.add(client2); clientGroup.addAll(clients); Iterator<Client> iterator = clientGroup.iterator(); Assert.assertTrue(iterator.hasNext()); Assert.assertEquals(client, iterator.next()); Assert.assertTrue(iterator.hasNext()); Assert.assertEquals(client2, iterator.next()); Assert.assertFalse(iterator.hasNext()); }

 @Test (expected = UnsupportedOperationException.class)
 public void testRemoveWhileIterating() {
 Set<Client> clients = new LinkedHashSet<>(); clients.add(client); clients.add(client2); clientGroup.addAll(clients); Iterator<Client> iterator = clientGroup.iterator(); iterator.remove(); }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup(); Assert.assertNotNull(subGroup); Assert.assertEquals(clientGroup, subGroup.parentGroup()); }

 @Test
 public void testCreateSubGroup_clientInBothGroups() {
 clientGroup.add(client); clientGroup.createSubGroup().add(client); Assert.assertTrue(clientGroup.contains(client)); Assert.assertTrue(clientGroup.subGroups.size() > 0); Assert.assertTrue(clientGroup.subGroups.iterator().next().get().contains(client)); }
}
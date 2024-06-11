import org.junit.Test;
import org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakReference;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private ClientHandler clientHandler;

 @Before
 public void setUp() {
 clientGroup = new ClientGroup(null);
 client = new Client();
 clientHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // empty implementation
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // empty implementation
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // empty implementation
 }

 @Override
 public void clientAdded(Client client) {
 // empty implementation
 }

 @Override
 public void clientRemoved(Client client) {
 // empty implementation
 }
 };
 clientGroup.addClientHandler(clientHandler);
 }

 @Test
 public void testConstructor() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertNotNull(clientGroup.parentGroup());
 Assert.assertNotNull(clientGroup.subGroups);
 Assert.assertNotNull(clientGroup.clients);
 Assert.assertNull(clientGroup.handler);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup clientGroup = new ClientGroup(parentGroup);
 Assert.assertEquals(parentGroup, clientGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSentHappyPath() {
 clientGroup.add(client);
 Assert.assertTrue(clientGroup.notifyMessageSent(client, "test message"));
 }

 @Test
 public void testNotifyMessageSentClientNotInGroup() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertFalse(clientGroup.notifyMessageSent(client, "test message"));
 }

 @Test
 public void testNotifyMessageSentExceptionInSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup clientGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(clientGroup);
 WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
 clientGroup.subGroups.add(subGroupReference);
 when(subGroup.notifyMessageSent(client, "test message")).thenThrow(new RuntimeException("test exception"));

 clientGroup.add(client);
 try {
 clientGroup.notifyMessageSent(client, "test message");
 fail("Expected exception not thrown");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testNotifyMessageReceivedHappyPath() {
 clientGroup.add(client);
 Assert.assertTrue(clientGroup.notifyMessageReceived(client, "test message"));
 }

 @Test
 public void testNotifyMessageReceivedClientNotInGroup() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertFalse(clientGroup.notifyMessageReceived(client, "test message"));
 }

 @Test
 public void testNotifyMessageReceivedExceptionInSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup clientGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(clientGroup);
 WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
 clientGroup.subGroups.add(subGroupReference);
 when(subGroup.notifyMessageReceived(client, "test message")).thenThrow(new RuntimeException("test exception"));

 clientGroup.add(client);
 try {
 clientGroup.notifyMessageReceived(client, "test message");
 fail("Expected exception not thrown");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testNotifyExceptionCaughtHappyPath() {
 clientGroup.add(client);
 try {
 throw new RuntimeException("test exception");
 } catch (Exception e) {
 clientGroup.notifyExceptionCaught(client, e);
 }
 }

 @Test
 public void testNotifyExceptionCaughtClientNotInGroup() {
 ClientGroup clientGroup = new ClientGroup(null);
 try {
 throw new RuntimeException("test exception");
 } catch (Exception e) {
 clientGroup.notifyExceptionCaught(client, e);
 fail("Expected exception not thrown");
 }
 }

 @Test
 public void testNotifyExceptionCaughtExceptionInSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup clientGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = new ClientGroup(clientGroup);
 WeakReference<ClientGroup> subGroupReference = new WeakReference<>(subGroup);
 clientGroup.subGroups.add(subGroupReference);

 try {
 clientGroup.notifyExceptionCaught(client, new RuntimeException("test exception"));
 fail("Expected exception not thrown");
 } catch (Exception e) {
 // expected
 }

 when(subGroup.notifyExceptionCaught(client, any())).thenThrow(new RuntimeException("test subgroup exception"));
 }

 @Test
 public void testIterator() {
 clientGroup.add(client);
 Set<Client> clients = new LinkedHashSet<Client>();
 clients.add(client);
 Iterator<Client> iterator = clientGroup.iterator();
 Client next = iterator.next();
 Assert.assertEquals(client, next);
 Assert.assertTrue(iterator.hasNext());
 }

 @Test
 public void testContains() {
 clientGroup.add(client);
 Assert.assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize() {
 clientGroup.add(client);
 Assert.assertEquals(1, clientGroup.size());
 }

 @Test
 public void testAddClientThatAlreadyExists() {
 clientGroup.add(client);
 Assert.assertTrue(clientGroup.add(client));
 }

 @Test
 public void testAddClientThatIsADiscardedClient() {
 Client discardedClient = new Client();
 clientGroup.add(discardedClient);
 clientGroup.remove(discardedClient);
 Assert.assertFalse(clientGroup.add(discardedClient));
 }

 @Test
 public void testRemoveClientThatDoesNotExist() {
 Assert.assertFalse(clientGroup.remove(client));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup clientGroup = new ClientGroup(parentGroup);
 ClientGroup subGroup = clientGroup.createSubGroup();
 Assert.assertEquals(clientGroup, subGroup.parentGroup());
 }
}
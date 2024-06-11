import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.WeakReference;

public class ClientGroupTest {

 private ClientGroup parentGroup;
 private ClientGroup clientGroupUnderTest;
 private Client client;
 private Client client2;
 private Client client3;
 private Set<Client> clients = new LinkedHashSet<>();
 private ClientHandler handler;

 @Before
 public void setUp() {
 client = new Client();
 client2 = new Client();
 client3 = new Client();
 parentGroup = new ClientGroup(null);
 clientGroupUnderTest = new ClientGroup(parentGroup);
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 }

 @Override
 public void clientRemoved(Client client) {
 }

 @Override
 public void messageReceived(Client client, Object message) {
 }

 @Override
 public void messageSent(Client client, Object message) {
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 }
 };
 clients.add(client);
 clients.add(client2);
 clients.add(client3);
 clientGroupUnderTest.addClientHandler(handler);
 }

 @Test
 public void testConstructor() {
 assertNull(clientGroupUnderTest.parentGroup());
 assertNotNull(clientGroupUnderTest.subGroups);
 assertNotNull(clientGroupUnderTest.clients);
 assertNull(clientGroupUnderTest.handler);
 }

 @Test
 public void testParentGroup() {
 parentGroup.add(clientGroupUnderTest);
 Assert.assertSame(parentGroup, clientGroupUnderTest.parentGroup());
 }

 @Test
 public void testNotifyMessageSent_ClientIsInGroup_ReturnsTrue() {
 Assert.assertTrue(clientGroupUnderTest.notifyMessageSent(client, new Object()));
 }

 @Test
 public void testNotifyMessageSent_ClientIsNotInGroup_ReturnsFalse() {
 Client clientOutsideGroup = new Client();
 assertFalse(clientGroupUnderTest.notifyMessageSent(clientOutsideGroup, new Object()));
 }

 @Test
 public void testNotifyMessageSent_ClientIsInSubGroup_PropagatesToSubGroup() {
 ClientGroup subGroup = clientGroupUnderTest.createSubGroup();
 Assert.assertTrue(subGroup.notifyMessageSent(client, new Object()));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageSent_NullHandler() {
 clientGroupUnderTest.handler = null;
 clientGroupUnderTest.notifyMessageSent(client, new Object());
 }

 @Test
 public void testNotifyMessageReceived_ClientIsInGroup_ReturnsTrue() {
 Assert.assertTrue(clientGroupUnderTest.notifyMessageReceived(client, new Object()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsNotInGroup_ReturnsFalse() {
 assertFalse(clientGroupUnderTest.notifyMessageReceived(client2, new Object()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsInSubGroup_PropagatesToSubGroup() {
 ClientGroup subGroup = clientGroupUnderTest.createSubGroup();
 Assert.assertTrue(subGroup.notifyMessageReceived(client, new Object()));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageReceived_NullHandler() {
 clientGroupUnderTest.handler = null;
 clientGroupUnderTest.notifyMessageReceived(client, new Object());
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsInGroup_ReturnsTrue() {
 throw new RuntimeException();
 try {
 clientGroupUnderTest.notifyExceptionCaught(client, new RuntimeException());
 } catch (Throwable ignore) {
 // expected
 }
 Assert.assertTrue(clientGroupUnderTest.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsNotInGroup_ReturnsFalse() {
 assertFalse(clientGroupUnderTest.notifyExceptionCaught(client2, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsInSubGroup_PropagatesToSubGroup() {
 ClientGroup subGroup = clientGroupUnderTest.createSubGroup();
 try {
 throw new RuntimeException();
 } catch (Throwable ignore) {
 // expected
 }
 Assert.assertTrue(subGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyExceptionCaught_NullHandler() {
 clientGroupUnderTest.handler = null;
 clientGroupUnderTest.notifyExceptionCaught(client, new RuntimeException());
 }

 @Test
 public void testIterator() {
 Assert.assertEquals(3, clientGroupUnderTest.size());
 Iterator<Client> iterator = clientGroupUnderTest.iterator();
 Set<Client> result = new HashSet<>(3);
 while (iterator.hasNext()) {
 result.add(iterator.next());
 }
 Assert.assertEquals(clients, result);
 }

 @Test
 public void testContains() {
 for (Client client : clients) {
 Assert.assertTrue(clientGroupUnderTest.contains(client));
 }
 }

 @Test
 public void testSize_EmptyGroup() {
 ClientGroup emptyGroup = new ClientGroup(null);
 Assert.assertEquals(0, emptyGroup.size());
 }

 @Test
 public void testSize_GroupWithClients() {
 Assert.assertEquals(3, clientGroupUnderTest.size());
 }

 @Test
 public void testAdd_ClientIsAlreadyInParentGroup_ReturnsFalse() {
 Assert.assertFalse(clientGroupUnderTest.add(client));
 }

 @Test
 public void testAdd_ClientIsNotInParentGroup_AddsClientAndNotifiesHandler() {
 Client newClient = new Client();
 Assert.assertFalse(clientGroupUnderTest.add(newClient));
 Assert.assertFalse(parentGroup.contains(newClient));
 Assert.assertEquals(4, clientGroupUnderTest.size());
 }

 @Test
 public void testRemove_ClientIsNotInGroup_ReturnsFalse() {
 Assert.assertFalse(clientGroupUnderTest.remove(client2));
 }

 @Test
 public void testRemove_ClientIsInGroup_RemovesClientFromGroupAndNotifiesHandler() {
 Assert.assertTrue(clientGroupUnderTest.contains(client));
 clientGroupUnderTest.remove(client);
 Assert.assertFalse(clientGroupUnderTest.contains(client));
 handler.clientRemoved(client);
 }

 @Test
 public void testRemove_ClientIsInSubGroup_RemovesClientFromSubGroupAndNotifiesHandler() {
 ClientGroup subGroup = clientGroupUnderTest.createSubGroup();
 Client clientToRemove = new Client();
 subGroup.add(clientToRemove);
 clientGroupUnderTest.add(subGroup);
 Assert.assertTrue(subGroup.contains(clientToRemove));
 subGroup.remove(clientToRemove);
 Assert.assertFalse(subGroup.contains(clientToRemove));
 handler.clientRemoved(clientToRemove);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = clientGroupUnderTest.createSubGroup();
 Assert.assertSame(clientGroupUnderTest, subGroup.parentGroup());
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup clientGroupWithHandler = new ClientGroup(null);
 clientGroupWithHandler.addClientHandler(handler);
 Assert.assertNotNull(clientGroupWithHandler.handler);
 }
}
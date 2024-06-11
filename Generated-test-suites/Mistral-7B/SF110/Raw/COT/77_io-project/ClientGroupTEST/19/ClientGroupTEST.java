import org.junit.Assert;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientGroupIterator;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private ClientHandler handler;
 private Client client1;
 private Client client2;
 private Client client3;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 client1 = new Client("Client1");
 client2 = new Client("Client2");
 client3 = new Client("Client3");
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // Do nothing
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // Do nothing
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
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
 };
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 subGroup1.add(client2);
 subGroup2.add(client3);
 }

 @Test
 public void testConstructor() {
 ClientGroup group = new ClientGroup(rootGroup);
 Assert.assertNotNull(group);
 Assert.assertNull(group.handler);
 Assert.assertTrue(group.clients.isEmpty());
 Assert.assertTrue(group.subGroups.isEmpty());
 }

 @Test
 public void testGetParentGroup() {
 Assert.assertSame(rootGroup, rootGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent_ClientIsMember() {
 Assert.assertTrue(rootGroup.notifyMessageSent(client1, "Message1"));
 Assert.assertTrue(subGroup1.notifyMessageSent(client2, "Message2"));
 Assert.assertTrue(subGroup2.notifyMessageSent(client3, "Message3"));
 }

 @Test
 public void testNotifyMessageSent_ClientIsNotMember() {
 Client client4 = new Client("Client4");
 Assert.assertFalse(rootGroup.notifyMessageSent(client4, "Message1"));
 }

 @Test
 public void testNotifyMessageSent_NullMessage() {
 Assert.assertFalse(rootGroup.notifyMessageSent(client1, null));
 }

 @Test
 public void testNotifyMessageSent_NullClient() {
 Assert.assertFalse(rootGroup.notifyMessageSent(null, "Message1"));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageSent_NullHandler() {
 rootGroup.handler = null;
 rootGroup.notifyMessageSent(client1, "Message1");
 }

 @Test
 public void testNotifyMessageReceived_ClientIsMember() {
 Assert.assertTrue(rootGroup.notifyMessageReceived(client1, "Message1"));
 Assert.assertTrue(subGroup1.notifyMessageReceived(client2, "Message2"));
 Assert.assertTrue(subGroup2.notifyMessageReceived(client3, "Message3"));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsNotMember() {
 Assert.assertFalse(rootGroup.notifyMessageReceived(client4, "Message1"));
 }

 @Test
 public void testNotifyMessageReceived_NullMessage() {
 Assert.assertFalse(rootGroup.notifyMessageReceived(client1, null));
 }

 @Test
 public void testNotifyMessageReceived_NullClient() {
 Assert.assertFalse(rootGroup.notifyMessageReceived(null, "Message1"));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageReceived_NullHandler() {
 rootGroup.handler = null;
 rootGroup.notifyMessageReceived(client1, "Message1");
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsMember() {
 Throwable cause = new RuntimeException("Test Exception");
 Assert.assertTrue(rootGroup.notifyExceptionCaught(client1, cause));
 Assert.assertTrue(subGroup1.notifyExceptionCaught(client2, cause));
 Assert.assertTrue(subGroup2.notifyExceptionCaught(client3, cause));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsNotMember() {
 Assert.assertFalse(rootGroup.notifyExceptionCaught(client4, new RuntimeException("Test Exception")));
 }

 @Test
 public void testNotifyExceptionCaught_NullClient() {
 Assert.assertFalse(rootGroup.notifyExceptionCaught(null, new RuntimeException("Test Exception")));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyExceptionCaught_NullHandler() {
 rootGroup.handler = null;
 rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test Exception"));
 }

 @Test
 public void testContains() {
 Assert.assertTrue(rootGroup.contains(client1));
 Assert.assertTrue(subGroup1.contains(client2));
 Assert.assertTrue(subGroup2.contains(client3));
 Assert.assertFalse(rootGroup.contains(client4));
 }

 @Test
 public void testSize() {
 Assert.assertEquals(3, rootGroup.size());
 }

 @Test
 public void testAdd() {
 Assert.assertFalse(rootGroup.add(client1)); // Should return false as client1 is already a member
 Assert.assertTrue(rootGroup.add(client4));
 Assert.assertEquals(4, rootGroup.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAdd_NullClient() {
 rootGroup.add(null);
 }

 @Test
 public void testRemove() {
 Assert.assertTrue(rootGroup.remove(client1));
 Assert.assertEquals(3, rootGroup.size());
 }

 @Test
 public void testRemove_NonExistentClient() {
 Assert.assertFalse(rootGroup.remove(client4));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup newSubGroup = rootGroup.createSubGroup();
 Assert.assertNotNull(newSubGroup);
 Assert.assertSame(rootGroup, newSubGroup.parentGroup());
 }

 @Test
 public void testAddClientHandler() {
 ClientHandler newHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // Do nothing
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // Do nothing
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
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
 };
 rootGroup.addClientHandler(newHandler);
 }

 @Test
 public void testIterator() {
 Iterator<Client> iterator = rootGroup.iterator();
 List<Client> clients = new ArrayList<>();
 while (iterator.hasNext()) {
 clients.add(iterator.next());
 }
 Assert.assertEquals(4, clients.size());
 Assert.assertTrue(clients.contains(client1));
 Assert.assertTrue(clients.contains(client2));
 Assert.assertTrue(clients.contains(client3));
 Assert.assertTrue(clients.contains(client4));
 }
}
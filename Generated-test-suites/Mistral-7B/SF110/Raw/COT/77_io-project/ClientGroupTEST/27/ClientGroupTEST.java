import org.junit.Test;
import org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakReference;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private ClientHandler handler;

 @Before
 public void setUp() {
 group = new ClientGroup(null);
 client = new Client();
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // do nothing
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // do nothing
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
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
 }

 @Test
 public void testConstructor() {
 ClientGroup clientGroup = new ClientGroup(null);
 Assert.assertNotNull(clientGroup);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 Assert.assertEquals(parentGroup, childGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 group.add(client);
 group.addClientHandler(handler);
 group.notifyMessageSent(client, new Object());
 assertTrue(handler.isMessageSentCalled());
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup1 = new ClientGroup(parentGroup);
 ClientGroup childGroup2 = new ClientGroup(childGroup1);
 ClientGroup subGroup = new ClientGroup(childGroup2);
 client = new Client();
 group = childGroup1;
 group.add(client);
 subGroup.add(client);
 group.addClientHandler(handler);
 subGroup.addClientHandler(handler);
 group.notifyMessageSent(client, new Object());
 assertTrue(handler.isMessageSentCalled());
 assertTrue(handler.isMessageSentCalled());
 }

 @Test
 public void testNotifyMessageSentWhenHandlerIsNull() {
 group.add(client);
 group.notifyMessageSent(client, new Object());
 assertFalse(handler.isMessageSentCalled());
 }

 @Test
 public void testNotifyMessageReceived() {
 group.add(client);
 group.addClientHandler(handler);
 group.notifyMessageReceived(client, new Object());
 assertTrue(handler.isMessageReceivedCalled());
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup1 = new ClientGroup(parentGroup);
 ClientGroup childGroup2 = new ClientGroup(childGroup1);
 ClientGroup subGroup = new ClientGroup(childGroup2);
 client = new Client();
 group = childGroup1;
 group.add(client);
 subGroup.add(client);
 group.addClientHandler(handler);
 subGroup.addClientHandler(handler);
 group.notifyMessageReceived(client, new Object());
 assertTrue(handler.isMessageReceivedCalled());
 assertTrue(handler.isMessageReceivedCalled());
 }

 @Test
 public void testNotifyMessageReceivedWhenHandlerIsNull() {
 group.add(client);
 group.notifyMessageReceived(client, new Object());
 assertFalse(handler.isMessageReceivedCalled());
 }

 @Test
 public void testNotifyExceptionCaught() {
 group.add(client);
 group.addClientHandler(handler);
 group.notifyExceptionCaught(client, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled());
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup1 = new ClientGroup(parentGroup);
 ClientGroup childGroup2 = new ClientGroup(childGroup1);
 ClientGroup subGroup = new ClientGroup(childGroup2);
 client = new Client();
 group = childGroup1;
 group.add(client);
 subGroup.add(client);
 group.addClientHandler(handler);
 subGroup.addClientHandler(handler);
 group.notifyExceptionCaught(client, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled());
 assertTrue(handler.isExceptionCaughtCalled());
 }

 @Test
 public void testNotifyExceptionCaughtWhenHandlerIsNull() {
 group.add(client);
 group.notifyExceptionCaught(client, new RuntimeException());
 assertFalse(handler.isExceptionCaughtCalled());
 }

 @Test
 public void testAddClient() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 group = new ClientGroup(null);
 group.clients = clients;
 group.add(client);
 assertTrue(group.contains(client));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddDuplicateClient() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 group = new ClientGroup(null);
 group.clients = clients;
 group.add(client);
 group.add(client); // should throw an exception
 }

 @Test
 public void testRemoveClient() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 group = new ClientGroup(null);
 group.clients = clients;
 group.add(client);
 Assert.assertTrue(group.contains(client));
 group.remove(client);
 Assert.assertFalse(group.contains(client));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testRemoveNonExistentClient() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 group = new ClientGroup(null);
 group.clients = clients;
 group.remove(client); // should not throw an exception
 group.remove(client); // should throw an exception
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 Assert.assertNotNull(subGroup);
 Assert.assertNotNull(group.subGroups);
 Assert.assertTrue(group.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test (expected = IllegalStateException.class)
 public void testCreateSubGroupWhenParentGroupIsNull() {
 ClientGroup subGroup = new ClientGroup(null).createSubGroup();
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup groupWithHandler = new ClientGroup(null);
 ClientGroup groupWithoutHandler = new ClientGroup(null);
 groupWithHandler.addClientHandler(handler);
 assertFalse(groupWithoutHandler.handler == handler);
 assertTrue(groupWithHandler.handler == handler);
 }
}
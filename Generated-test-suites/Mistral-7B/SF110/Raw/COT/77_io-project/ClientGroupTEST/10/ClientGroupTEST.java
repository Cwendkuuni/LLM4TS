import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupTest;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private ClientGroup subGroup;
 private ClientHandler handler;

 @Before
 public void setup() {
 group = new ClientGroup(null);
 client = new Client();
 subGroup = group.createSubGroup();
 handler = new ClientHandler() {
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
 group.addClientHandler(handler);
 group.add(client);
 }

 // Testing the constructor
 @Test
 public void testClientGroupConstructor() {
 ClientGroup group = new ClientGroup(null);
 assertNotNull(group);
 }

 // Testing the parentGroup method
 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup childGroup = new ClientGroup(parentGroup);
 assertEquals(parentGroup, childGroup.parentGroup());
 }

 // Testing the notifyMessageSent method
 @Test
 public void testNotifyMessageSent() {
 group.notifyMessageSent(client, new Object());
 assertTrue(handler.isMessageSentCalled);
 }

 // Testing the notifyMessageSent method when client is not in the group
 @Test
 public void testNotifyMessageSentWhenClientNotInGroup() {
 group.notifyMessageSent(new Client(), new Object());
 assertFalse(handler.isMessageSentCalled);
 }

 // Testing the notifyMessageSent method in subGroups
 @Test
 public void testNotifyMessageSentInSubGroups() {
 subGroup.notifyMessageSent(client, new Object());
 assertTrue(handler.isMessageSentCalled);
 }

 // Testing the notifyMessageReceived method
 @Test
 public void testNotifyMessageReceived() {
 group.notifyMessageReceived(client, new Object());
 assertTrue(handler.isMessageReceivedCalled);
 }

 // Testing the notifyMessageReceived method when client is not in the group
 @Test
 public void testNotifyMessageReceivedWhenClientNotInGroup() {
 group.notifyMessageReceived(new Client(), new Object());
 assertFalse(handler.isMessageReceivedCalled);
 }

 // Testing the notifyMessageReceived method in subGroups
 @Test
 public void testNotifyMessageReceivedInSubGroups() {
 subGroup.notifyMessageReceived(client, new Object());
 assertTrue(handler.isMessageReceivedCalled);
 }

 // Testing the notifyExceptionCaught method
 @Test
 public void testNotifyExceptionCaught() {
 group.notifyExceptionCaught(client, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled);
 }

 // Testing the notifyExceptionCaught method when client is not in the group
 @Test
 public void testNotifyExceptionCaughtWhenClientNotInGroup() {
 group.notifyExceptionCaught(new Client(), new RuntimeException());
 assertFalse(handler.isExceptionCaughtCalled);
 }

 // Testing the notifyExceptionCaught method in subGroups
 @Test
 public void testNotifyExceptionCaughtInSubGroups() {
 subGroup.notifyExceptionCaught(client, new RuntimeException());
 assertTrue(handler.isExceptionCaughtCalled);
 }

 // Testing the iterator method
 @Test
 public void testIterator() {
 Iterator<Client> iterator = group.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client, iterator.next());
 }

 // Testing the contains method
 @Test
 public void testContains() {
 assertTrue(group.contains(client));
 }

 // Testing the contains method when client is not in the group
 @Test
 public void testContainsWhenClientNotInGroup() {
 assertFalse(group.contains(new Client()));
 }

 // Testing the size method
 @Test
 public void testSize() {
 assertEquals(1, group.size());
 }

 // Testing the add method
 @Test
 public void testAdd() {
 Client newClient = new Client();
 group.add(newClient);
 assertTrue(group.contains(newClient));
 }

 // Testing the add method when client is already a member of the group
 @Test
 public void testAddWhenClientAlreadyInGroup() {
 group.add(client);
 assertFalse(group.add(client));
 }

 // Testing the remove method
 @Test
 public void testRemove() {
 group.remove(client);
 assertFalse(group.contains(client));
 }

 // Testing the remove method when client is not in the group
 @Test
 public void testRemoveWhenClientNotInGroup() {
 group.remove(new Client());
 assertTrue(group.contains(client));
 }

 // Testing the createSubGroup method
 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 assertFalse(subGroup.equals(group));
 assertTrue(group.subGroups.contains(new WeakReference<ClientGroup>(subGroup)));
 }

 // Testing the addClientHandler method
 @Test
 public void testAddClientHandler() {
 ClientHandler newHandler = new ClientHandler() {
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
 group.addClientHandler(newHandler);
 assertTrue(group.handler == newHandler);
 }
}
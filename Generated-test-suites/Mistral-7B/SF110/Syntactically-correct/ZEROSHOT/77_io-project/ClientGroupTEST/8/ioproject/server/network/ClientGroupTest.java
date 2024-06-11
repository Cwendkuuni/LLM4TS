import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientGroup subGroup;
 private ClientHandler handler;

 @Before
 public void setUp() {
 clientGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 subGroup = clientGroup.createSubGroup();
 handler = new ClientHandler() {
 @Override
 public void clientAdded(Client client) {
 // no implementation
 }

 @Override
 public void clientRemoved(Client client) {
 // no implementation
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // no implementation
 }

 @Override
 public void messageSent(Client client, Object message) {
 // no implementation
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // no implementation
 }
 };
 clientGroup.addClientHandler(handler);
 }

 @Test
 public void testConstruction() {
 assertNotNull(clientGroup.parentGroup());
 assertNull(clientGroup.handler);
 assertNull(clientGroup.subGroups);
 assertNull(clientGroup.clients);
 }

 @Test
 public void testContainsEmptyGroup() {
 assertFalse(clientGroup.contains(client1));
 }

 @Test
 public void testAddClientToEmptyGroup() {
 assertTrue(clientGroup.add(client1));
 assertTrue(clientGroup.contains(client1));
 }

 @Test
 public void testAddClientAlreadyExists() {
 assertTrue(clientGroup.add(client1));
 assertFalse(clientGroup.add(client1));
 }

 @Test
 public void testAddClientToSubGroup() {
 clientGroup.add(client1);
 subGroup.add(client1);
 assertTrue(clientGroup.contains(client1));
 assertTrue(subGroup.contains(client1));
 }

 @Test
 public void testRemoveClientFromEmptyGroup() {
 assertFalse(clientGroup.remove(client1));
 }

 @Test
 public void testRemoveClientFromGroup() {
 clientGroup.add(client1);
 assertTrue(clientGroup.remove(client1));
 assertFalse(clientGroup.contains(client1));
 }

 @Test
 public void testRemoveClientFromSubGroup() {
 clientGroup.add(client1);
 subGroup.add(client1);
 subGroup.remove(client1);
 assertFalse(subGroup.contains(client1));
 assertFalse(clientGroup.contains(client1));
 }

 @Test
 public void testNotifyMessageSentToEmptyGroup() {
 assertFalse(clientGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageSentToGroup() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.notifyMessageSent(client1, "message");
 assertTrue(client1.isMessageReceived());
 assertTrue(client2.isMessageReceived());
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 clientGroup.add(client1);
 subGroup.add(client2);
 clientGroup.notifyMessageSent(client1, "message");
 assertTrue(client1.isMessageReceived());
 assertTrue(client2.isMessageReceived());
 }

 @Test
 public void testNotifyMessageReceivedToEmptyGroup() {
 assertFalse(clientGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceivedToGroup() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.notifyMessageReceived(client1, "message");
 assertTrue(client1.isMessageReceived());
 assertTrue(client2.isMessageReceived());
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 clientGroup.add(client1);
 subGroup.add(client2);
 clientGroup.notifyMessageReceived(client1, "message");
 assertTrue(client1.isMessageReceived());
 assertTrue(client2.isMessageReceived());
 }

 @Test
 public void testNotifyExceptionCaughtToEmptyGroup() {
 assertFalse(clientGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaughtToGroup() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(client1.isExceptionCaught());
 assertTrue(client2.isExceptionCaught());
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 clientGroup.add(client1);
 subGroup.add(client2);
 clientGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(client1.isExceptionCaught());
 assertTrue(client2.isExceptionCaught());
 }

 @Test
 public void testSize() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 assertTrue(clientGroup.size() == 2);
 }

 @Test
 public void testIterator() {
 clientGroup.add(client1);
 clientGroup.add(client2);
 Iterator<Client> iterator = clientGroup.iterator();
 assertTrue(Arrays.equals(iterator.next(), client1));
 assertTrue(Arrays.equals(iterator.next(), client2));
 }
}
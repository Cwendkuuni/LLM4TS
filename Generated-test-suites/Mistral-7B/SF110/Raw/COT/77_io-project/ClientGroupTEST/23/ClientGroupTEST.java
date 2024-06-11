import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler clientHandler;

 @Before
 public void setUp() {
 clientGroup = new ClientGroup(null);
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 clientHandler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 // empty implementation for test purpose
 }

 @Override
 public void messageSent(Client sender, Object message) {
 // empty implementation for test purpose
 }

 @Override
 public void clientAdded(Client client) {
 // empty implementation for test purpose
 }

 @Override
 public void clientRemoved(Client client) {
 // empty implementation for test purpose
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // empty implementation for test purpose
 }
 };
 clientGroup.addClientHandler(clientHandler);
 clientGroup.add(client1);
 clientGroup.add(client2);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup testClientGroup = new ClientGroup(parentGroup);
 assertEquals(parentGroup, testClientGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 clientGroup.add(client3);
 assertFalse(clientGroup.notifyMessageSent(client1, "message"));
 assertFalse(clientGroup.notifyMessageSent(client2, "message"));
 assertFalse(clientGroup.notifyMessageSent(client3, "message"));

 clientGroup.notifyMessageSent(client1, "message");
 assertTrue(clientGroup.notifyMessageSent(client1, "message"));

 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client3);
 assertFalse(clientGroup.notifyMessageSent(client3, "message"));
 assertTrue(subGroup.notifyMessageSent(client3, "message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 clientGroup.add(client3);
 assertFalse(clientGroup.notifyMessageReceived(client1, "message"));
 assertFalse(clientGroup.notifyMessageReceived(client2, "message"));
 assertFalse(clientGroup.notifyMessageReceived(client3, "message"));

 clientGroup.notifyMessageReceived(client1, "message");
 assertTrue(clientGroup.notifyMessageReceived(client1, "message"));

 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client3);
 assertFalse(clientGroup.notifyMessageReceived(client3, "message"));
 assertTrue(subGroup.notifyMessageReceived(client3, "message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 clientGroup.add(client3);
 assertFalse(clientGroup.notifyExceptionCaught(client1, new RuntimeException("test")));
 assertFalse(clientGroup.notifyExceptionCaught(client2, new RuntimeException("test")));
 assertFalse(clientGroup.notifyExceptionCaught(client3, new RuntimeException("test")));

 clientGroup.notifyExceptionCaught(client1, new RuntimeException("test"));
 assertTrue(clientGroup.notifyExceptionCaught(client1, new RuntimeException("test")));

 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client3);
 assertFalse(clientGroup.notifyExceptionCaught(client3, new RuntimeException("test")));
 assertTrue(subGroup.notifyExceptionCaught(client3, new RuntimeException("test")));
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new LinkedHashSet<>(Arrays.asList(client1, client2, client3));
 Iterator<Client> iterator = clientGroup.iterator();
 assertEquals(clients.iterator().next(), iterator.next());
 }

 @Test
 public void testContains() {
 assertTrue(clientGroup.contains(client1));
 assertFalse(clientGroup.contains(new Client("differentClient")));
 }

 @Test
 public void testSize() {
 assertEquals(3, clientGroup.size());
 }

 @Test
 public void testAdd() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertFalse(clientGroup.add(client3));
 assertTrue(subGroup.add(client3));
 assertTrue(clientGroup.contains(client1));
 assertTrue(clientGroup.contains(client2));
 assertFalse(clientGroup.contains(client3));
 assertFalse(subGroup.contains(client1));
 assertFalse(subGroup.contains(client2));
 assertTrue(subGroup.contains(client3));
 }

 @Test
 public void testRemove() {
 clientGroup.add(client3);
 assertTrue(clientGroup.remove(client3));
 assertFalse(clientGroup.contains(client3));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveClientNotPresent() {
 clientGroup.remove(client3);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertTrue(subGroup.parentGroup() == clientGroup);
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 ClientHandler subHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // empty implementation for test purpose
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // empty implementation for test purpose
 }

 @Override
 public void clientAdded(Client client) {
 // empty implementation for test purpose
 }

 @Override
 public void clientRemoved(Client client) {
 // empty implementation for test purpose
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // empty implementation for test purpose
 }
 };
 clientGroup.addClientHandler(subHandler);
 subGroup.addClientHandler(subHandler);
 }
}
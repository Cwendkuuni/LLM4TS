import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import java.util.*;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 client1 = new Client("Client1");
 client2 = new Client("Client2");
 client3 = new Client("Client3");
 handler = new TestClientHandler();
 rootGroup.addClientHandler(handler);
 }

 @After
 public void tearDown() {
 rootGroup = null;
 client1 = null;
 client2 = null;
 client3 = null;
 handler = null;
 System.gc();
 }

 @Test
 public void testConstructor() {
 ClientGroup group = new ClientGroup(rootGroup);
 assertEquals(rootGroup, group.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);

 assertTrue(rootGroup.notifyMessageSent(client1, "message"));
 assertTrue(rootGroup.notifyMessageSent(client2, "message"));
 assertTrue(rootGroup.notifyMessageSent(client3, "message"));

 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client1);

 assertFalse(rootGroup.notifyMessageSent(client1, "message"));
 assertTrue(subGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);

 assertTrue(rootGroup.notifyMessageReceived(client1, "message"));
 assertTrue(rootGroup.notifyMessageReceived(client2, "message"));
 assertTrue(rootGroup.notifyMessageReceived(client3, "message"));

 ClientGroup subGroup = rootGroup.createSubGroup();
 subGroup.add(client1);

 assertFalse(rootGroup.notifyMessageReceived(client1, "message"));
 assertTrue(subGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);

 assertFalse(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 assertFalse(rootGroup.notifyExceptionCaught(client2, new RuntimeException()));
 assertFalse(rootGroup.notifyExceptionCaught(client3, new RuntimeException()));
 }

 @Test
 public void testSize() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);

 assertEquals(3, rootGroup.size());
 }

 @Test
 public void testAddClient() {
 assertFalse(rootGroup.contains(client1));
 assertTrue(rootGroup.add(client1));
 assertTrue(rootGroup.contains(client1));
 }

 @Test
 public void testRemoveClient() {
 rootGroup.add(client1);
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.remove(client1));
 assertFalse(rootGroup.contains(client1));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 assertNotNull(subGroup);
 assertFalse(subGroup.parentGroup() == rootGroup);
 }

 @Test
 public void testAddClientHandler() {
 assertNull(rootGroup.handler);
 rootGroup.addClientHandler(handler);
 assertNotNull(rootGroup.handler);
 }

 @Test
 public void testIterator() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);

 List<Client> clients = new ArrayList<>();
 for (Client client : rootGroup) {
 clients.add(client);
 }
 assertEquals(3, clients.size());
 assertEquals(client1, clients.get(0));
 assertEquals(client2, clients.get(1));
 assertEquals(client3, clients.get(2));
 }

 @Test
 public void testContains() {
 rootGroup.add(client1);
 rootGroup.add(client2);
 rootGroup.add(client3);

 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.contains(client3));
 }
}

 class TestClientHandler implements ClientHandler {
 @Override
 public void messageSent(Client sender, Object message) {
 // empty
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // empty
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // empty
 }

 @Override
 public void clientAdded(Client client) {
 // empty
 }

 @Override
 public void clientRemoved(Client client) {
 // empty
 }
}
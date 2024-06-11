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
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // TODO: implement
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // TODO: implement
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // TODO: implement
 }

 @Override
 public void clientAdded(Client client) {
 // TODO: implement
 }

 @Override
 public void clientRemoved(Client client) {
 // TODO: implement
 }
 };
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 rootGroup.add(client2);
 }

 @After
 public void tearDown() {
 rootGroup = null;
 client1 = null;
 client2 = null;
 client3 = null;
 handler = null;
 }

 @Test
 public void testAddAndRemoveClient() {
 // Test adding and removing a client
 rootGroup.add(client3);
 assertTrue(rootGroup.contains(client3));
 rootGroup.remove(client3);
 assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testNotifyMessageSent() {
 // Test notifying a message sent
 client1.sendMessageToServer("TestMessage", rootGroup);
 assertTrue(rootGroup.notifyMessageSent(client1, "TestMessage"));
 }

 @Test
 public void testNotifyMessageSentToSubGroups() {
 // Test notifying a message sent to subgroups
 ClientGroup subGroup = rootGroup.createSubGroup();
 ClientGroup subSubGroup = subGroup.createSubGroup();
 rootGroup.add(subGroup);
 subGroup.add(client3);
 subSubGroup.add(new Client("Client4"));
 client1.sendMessageToServer("TestMessage", rootGroup);
 assertTrue(subSubGroup.notifyMessageSent(client3, "TestMessage"));
 assertTrue(subGroup.notifyMessageSent(client3, "TestMessage"));
 assertTrue(rootGroup.notifyMessageSent(client3, "TestMessage"));
 }

 @Test
 public void testNotifyMessageReceived() {
 // Test notifying a message received
 client1.sendMessageToServer("TestMessage", rootGroup);
 client2.receiveMessageFromServer("TestMessage");
 assertTrue(rootGroup.notifyMessageReceived(client2, "TestMessage"));
 }

 @Test
 public void testNotifyMessageReceivedToSubGroups() {
 // Test notifying a message received to subgroups
 ClientGroup subGroup = rootGroup.createSubGroup();
 ClientGroup subSubGroup = subGroup.createSubGroup();
 rootGroup.add(subGroup);
 subGroup.add(client3);
 subSubGroup.add(new Client("Client4"));
 client1.sendMessageToServer("TestMessage", rootGroup);
 assertTrue(subSubGroup.notifyMessageReceived(client3, "TestMessage"));
 assertTrue(subGroup.notifyMessageReceived(client3, "TestMessage"));
 assertTrue(rootGroup.notifyMessageReceived(client3, "TestMessage"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 // Test notifying an exception caught
 client1.sendExceptionToServer(new RuntimeException("TestException"));
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException("TestException")));
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroups() {
 // Test notifying an exception caught to subgroups
 ClientGroup subGroup = rootGroup.createSubGroup();
 ClientGroup subSubGroup = subGroup.createSubGroup();
 rootGroup.add(subGroup);
 subGroup.add(client3);
 subSubGroup.add(new Client("Client4"));
 client1.sendExceptionToServer(new RuntimeException("TestException"));
 assertTrue(subSubGroup.notifyExceptionCaught(client1, new RuntimeException("TestException")));
 assertTrue(subGroup.notifyExceptionCaught(client1, new RuntimeException("TestException")));
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException("TestException")));
 }

 @Test
 public void testSizeAndContains() {
 // Test size and contains methods
 assertEquals(3, rootGroup.size());
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testIterator() {
 // Test iterator method
 Iterator<Client> iterator = rootGroup.iterator();
 List<Client> clients = new ArrayList<>();
 while (iterator.hasNext()) {
 clients.add(iterator.next());
 }
 assertEquals(2, clients.size());
 assertTrue(clients.contains(client1));
 assertTrue(clients.contains(client2));
 }
}
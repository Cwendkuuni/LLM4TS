import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.WeakReference;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private Client client2;
 private Client client3;
 private Client client4;
 private ClientHandler handler;
 private ClientGroup subGroup;

 @Before
 public void setUp() {
 client = new Client("TestClient1");
 client2 = new Client("TestClient2");
 client3 = new Client("TestClient3");
 client4 = new Client("TestClient4");
 handler = new ClientHandler() {
 @Override
 public void messageReceived(Client sender, Object message) {
 // Implementation not needed for testing
 }

 @Override
 public void messageSent(Client sender, Object message) {
 // Implementation not needed for testing
 }

 @Override
 public void clientAdded(Client client) {
 // Implementation not needed for testing
 }

 @Override
 public void clientRemoved(Client client) {
 // Implementation not needed for testing
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // Implementation not needed for testing
 }
 };
 clientGroup = new ClientGroup(null);
 }

 @Test
 public void testConstructor() {
 assertNull(clientGroup.parentGroup());
 assertNotNull(clientGroup.subGroups);
 assertNotNull(clientGroup.clients);
 assertNull(clientGroup.handler);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(clientGroup);
 clientGroup.add(client);
 assertTrue(clientGroup.parentGroup() == parentGroup);
 }

 @Test
 public void testNotifyMessageSent() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(handler);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");
 then(handler).messageSent(client, anyObject());
 then(handler).messageSent(client2, anyObject());

 assertTrue(clientGroup.notifyMessageSent(client, "TestMessage"));
 assertTrue(clientGroup.notifyMessageSent(client2, "TestMessage"));
 }

 @Test
 public void testNotifyMessageSent_ClientNotPresent() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(handler);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");
 then(handler).messageSent(client, anyObject());
 then(handler).messageSent(client2, anyObject());

 assertFalse(clientGroup.notifyMessageSent(new Client("Client5"), "TestMessage"));
 }

 @Test
 public void testNotifyMessageSent_HandlerNotPresent() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(null);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");

 assertFalse(clientGroup.notifyMessageSent(client, "TestMessage"));
 assertFalse(clientGroup.notifyMessageSent(client2, "TestMessage"));
 }

 @Test
 public void testNotifyMessageReceived() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(handler);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");
 then(handler).messageReceived(client, anyObject());
 then(handler).messageReceived(client2, anyObject());

 assertTrue(clientGroup.notifyMessageReceived(client, "TestMessage"));
 assertTrue(clientGroup.notifyMessageReceived(client2, "TestMessage"));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotPresent() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(handler);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");
 then(handler).messageReceived(client, anyObject());
 then(handler).messageReceived(client2, anyObject());

 assertFalse(clientGroup.notifyMessageReceived(new Client("Client5"), "TestMessage"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(handler);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");
 then(handler).exceptionCaught(client, any(Throwable.class));
 then(handler).exceptionCaught(client2, any(Throwable.class));

 assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException("TestException")));
 assertTrue(clientGroup.notifyExceptionCaught(client2, new RuntimeException("TestException")));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotPresent() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.addHandler(handler);
 when(client.getName()).thenReturn("Client1");
 when(client2.getName()).thenReturn("Client2");
 then(handler).exceptionCaught(client, any(Throwable.class));
 then(handler).exceptionCaught(client2, any(Throwable.class));

 assertFalse(clientGroup.notifyExceptionCaught(new Client("Client5"), new RuntimeException("TestException")));
 }

 @Test
 public void testIterator() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);
 Set<Client> clients = new HashSet<>();
 clients.addAll(clientGroup);
 assertEquals(4, clients.size());
 }

 @Test
 public void testContains() {
 clientGroup.add(client);
 clientGroup.add(client2);
 assertTrue(clientGroup.contains(client));
 assertFalse(clientGroup.contains(new Client("Client5")));
 }

 @Test
 public void testSize() {
 clientGroup.add(client);
 clientGroup.add(client2);
 clientGroup.add(client3);
 clientGroup.add(client4);
 assertEquals(4, clientGroup.size());
 }

 @Test
 public void testAdd() {
 clientGroup.add(client);
 clientGroup.add(client2);
 assertFalse(clientGroup.add(client));
 assertTrue(clientGroup.contains(client2));
 }

 @Test
 public void testRemove() {
 clientGroup.add(client);
 clientGroup.add(client2);
 assertTrue(clientGroup.contains(client2));
 clientGroup.remove(client2);
 assertFalse(clientGroup.contains(client2));
 }

 @Test
 public void testCreateSubGroup() {
 clientGroup.add(client);
 clientGroup.add(client2);
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertEquals(2, clientGroup.size());
 assertEquals(2, subGroup.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateSubGroup_ClientGroupIsNull() {
 ClientGroup subGroup = new ClientGroup(null).createSubGroup();
 fail("Should throw IllegalStateException.");
 }
}
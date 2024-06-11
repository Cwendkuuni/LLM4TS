import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupImpl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup;
 private Client client;
 private Client client2;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 subGroup = rootGroup.createSubGroup();
 client = Mockito.mock(Client.class);
 client2 = Mockito.mock(Client.class);
 handler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testAddAndRemoveClient() {
 rootGroup.add(client);
 rootGroup.remove(client);
 subGroup.add(client2);
 subGroup.remove(client2);

 // Test adding and removing the same client to the root group and a subgroup
 assertFalse(rootGroup.contains(client));
 assertFalse(subGroup.contains(client));
 rootGroup.add(client);
 assertTrue(rootGroup.contains(client));
 assertFalse(subGroup.contains(client));
 subGroup.add(client);
 assertTrue(subGroup.contains(client));
 assertTrue(rootGroup.contains(client));
 subGroup.remove(client);
 assertFalse(subGroup.contains(client));
 assertTrue(rootGroup.contains(client));
 rootGroup.remove(client);
 assertFalse(rootGroup.contains(client));
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new HashSet<Client>(Arrays.asList(client, client2));
 rootGroup.addAll(clients);

 Iterator<Client> iterator = rootGroup.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(client2, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSize() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);

 assertEquals(3, rootGroup.size());
 }

 @Test
 public void testContains() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);

 assertTrue(rootGroup.contains(client));
 assertTrue(rootGroup.contains(client2));
 assertTrue(subGroup.contains(client));
 assertFalse(rootGroup.contains(client2.getClass().cast(new Object(){})));
 }

 @Test
 public void testNotifyMessageSent() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);
 handler.messageSent(client, "test message");

 verify(handler).messageSent(client, "test message");
 }

 @Test
 public void testNotifyMessageSentSubGroup() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);
 handler.messageSent(client, "test message");

 verify(subGroup, times(1)).notifyMessageSent(client, "test message");
 }

 @Test
 public void testNotifyMessageReceived() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);
 handler.messageReceived(client, "test message");

 verify(handler).messageReceived(client, "test message");
 }

 @Test
 public void testNotifyMessageReceivedSubGroup() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);
 handler.messageReceived(client, "test message");

 verify(subGroup, times(1)).notifyMessageReceived(client, "test message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);
 Mockito.doThrow(new RuntimeException()).when(client).methodCall();
 handler.exceptionCaught(client, Mockito.any(Throwable.class));

 verify(handler).exceptionCaught(client, Mockito.any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaughtSubGroup() {
 rootGroup.add(client);
 rootGroup.add(client2);
 subGroup.add(client);
 Mockito.doThrow(new RuntimeException()).when(client).methodCall();
 handler.exceptionCaught(client, Mockito.any(Throwable.class));

 verify(subGroup, times(1)).notifyExceptionCaught(client, Mockito.any(Throwable.class));
 }

 @Test
 public void testAddClientHandler() {
 rootGroup.addClientHandler(handler);
 Handler handler2 = Mockito.mock(ClientHandler.class);
 rootGroup.addClientHandler(handler2);

 assertEquals(2, rootGroup.size());
 }
}
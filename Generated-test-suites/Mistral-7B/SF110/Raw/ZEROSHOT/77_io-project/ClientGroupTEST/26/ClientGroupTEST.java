import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.common.network.Message;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private ClientGroup subGroup;
 private ClientHandler clientHandler;

 @Before
 public void setUp() {
 client = Mockito.mock(Client.class);
 clientGroup = new ClientGroup(null);
 subGroup = clientGroup.createSubGroup();
 clientHandler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testConstructor() {
 assertNotNull(clientGroup.parentGroup());
 assertTrue(clientGroup.clients.isEmpty());
 assertTrue(clientGroup.subGroups.isEmpty());
 assertNull(clientGroup.handler);
 }

 @Test
 public void testNotifyMessageSent() {
 Mockito.when(client.sendMessage(Mockito.any(Object.class))).thenReturn(true);
 Mockito.when(subGroup.notifyMessageSent(client, Mockito.any(Object.class))).thenReturn(false);
 Mockito.when(clientGroup.handler.messageSent(client, Mockito.any(Object.class))).thenReturn(true);

 assertTrue(clientGroup.notifyMessageSent(client, new Message("testMessage")));
 assertTrue(clientGroup.clients.contains(client));
 assertTrue(clientGroup.subGroups.size() > 0);
 assertTrue(client.receivedMessageCalled());
 assertTrue(clientHandler.messageSentCalled());
 }

 @Test
 public void testNotifyMessageSentWhenClientNotInGroup() {
 Mockito.when(client.sendMessage(Mockito.any(Object.class))).thenReturn(true);

 assertFalse(clientGroup.notifyMessageSent(client, new Message("testMessage")));
 assertFalse(clientGroup.clients.contains(client));
 assertFalse(client.receivedMessageCalled());
 assertFalse(clientHandler.messageSentCalled());
 }

 @Test
 public void testNotifyMessageReceived() {
 Mockito.when(client.sendMessage(Mockito.any(Object.class))).thenReturn(true);
 Mockito.when(subGroup.notifyMessageReceived(client, Mockito.any(Object.class))).thenReturn(false);
 Mockito.when(clientGroup.handler.messageReceived(client, Mockito.any(Object.class))).thenReturn(true);

 assertTrue(clientGroup.notifyMessageReceived(client, new Message("testMessage")));
 assertTrue(clientGroup.clients.contains(client));
 assertTrue(clientGroup.subGroups.size() > 0);
 assertTrue(client.receivedMessageCalled());
 assertTrue(clientHandler.messageReceivedCalled());
 }

 @Test
 public void testNotifyMessageReceivedWhenClientNotInGroup() {
 Mockito.when(client.sendMessage(Mockito.any(Object.class))).thenReturn(true);

 assertFalse(clientGroup.notifyMessageReceived(client, new Message("testMessage")));
 assertFalse(clientGroup.clients.contains(client));
 assertFalse(client.receivedMessageCalled());
 assertFalse(clientHandler.messageReceivedCalled());
 }

 @Test
 public void testNotifyExceptionCaught() {
 Mockito.when(subGroup.notifyExceptionCaught(client, Mockito.any(Throwable.class))).thenReturn(false);
 Mockito.when(clientGroup.handler.exceptionCaught(client, Mockito.any(Throwable.class))).thenReturn(true);

 assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException("testException")));
 assertTrue(clientGroup.clients.contains(client));
 assertTrue(clientGroup.subGroups.size() > 0);
 assertTrue(client.receivedExceptionCalled());
 assertTrue(clientHandler.exceptionCaughtCalled());
 }

 @Test
 public void testNotifyExceptionCaughtWhenClientNotInGroup() {
 Mockito.when(subGroup.notifyExceptionCaught(client, Mockito.any(Throwable.class))).thenReturn(false);
 Mockito.when(clientGroup.handler.exceptionCaught(client, Mockito.any(Throwable.class))).thenReturn(false);

 assertFalse(clientGroup.notifyExceptionCaught(client, new RuntimeException("testException")));
 assertFalse(clientGroup.clients.contains(client));
 assertFalse(client.receivedExceptionCalled());
 assertFalse(clientHandler.exceptionCaughtCalled());
 }

 @Test
 public void testIterator() {
 Client client1 = Mockito.mock(Client.class);
 Client client2 = Mockito.mock(Client.class);
 clientGroup.add(client1);
 clientGroup.add(client2);

 Set<Client> set = new HashSet<>(Arrays.asList(client1, client2));
 CopyOnWriteArrayList<Client> expectedList = new CopyOnWriteArrayList<>(set);
 assertTrue(clientGroup.iterator().equals(expectedList.iterator()));
 }

 @Test
 public void testContains() {
 Client client1 = Mockito.mock(Client.class);
 clientGroup.add(client1);

 assertTrue(clientGroup.contains(client1));
 }

 @Test
 public void testSize() {
 Client client1 = Mockito.mock(Client.class);
 Client client2 = Mockito.mock(Client.class);
 clientGroup.add(client1);
 clientGroup.add(client2);

 int expectedSize = 2;
 int actualSize = clientGroup.size();
 assertTrue(expectedSize == actualSize);
 }

 @Test
 public void testAdd() {
 Client client1 = Mockito.mock(Client.class);
 Client client2 = Mockito.mock(Client.class);
 clientGroup.add(client1);
 assertTrue(clientGroup.contains(client1));
 clientGroup.add(client2);
 assertTrue(clientGroup.contains(client2));
 }

 @Test
 public void testRemove() {
 Client client1 = Mockito.mock(Client.class);
 Client client2 = Mockito.mock(Client.class);
 clientGroup.add(client1);
 clientGroup.add(client2);
 clientGroup.remove(client1);
 assertFalse(clientGroup.contains(client1));
 assertTrue(clientGroup.contains(client2));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertTrue(clientGroup.subGroups.size() > 0);
 assertTrue(clientGroup.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test
 public void testAddClientHandler() {
 ClientHandler handler = Mockito.mock(ClientHandler.class);
 clientGroup.addClientHandler(handler);
 assertNotNull(clientGroup.handler);
 }
}
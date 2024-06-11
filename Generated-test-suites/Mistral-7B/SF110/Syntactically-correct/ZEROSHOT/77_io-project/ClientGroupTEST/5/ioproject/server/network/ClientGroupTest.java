import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private ClientGroup subGroup;
 private Set<Client> clients;
 private Client client;
 private ClientHandler clientHandler;

 @Before
 public void setUp() {
 clients = new HashSet<>();
 client = new Client("Client1");
 clientGroup = new ClientGroup(null);
 subGroup = clientGroup.createSubGroup();
 clientHandler = mock(ClientHandler.class);
 }

 @Test
 public void testConstructor() {
 assertNotNull(clientGroup);
 assertNull(clientGroup.parentGroup());
 assertNotNull(clientGroup.clients());
 assertNull(clientGroup.handler());
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 clientGroup = new ClientGroup(parentGroup);
 assertSame(parentGroup, clientGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 clients.add(client);
 clientGroup.addHandler(clientHandler);
 verify(clientHandler).messageSent(client, Mockito.anyObject());
 assertTrue(clientGroup.notifyMessageSent(client, Mockito.anyObject()));
 }

 @Test
 public void testNotifyMessageSent_WhenClientNotPresent() {
 assertFalse(clientGroup.notifyMessageSent(client, Mockito.anyObject()));
 }

 @Test
 public void testNotifyMessageSent_WhenNoHandler() {
 clients.add(client);
 assertFalse(clientGroup.notifyMessageSent(client, Mockito.anyObject()));
 }

 @Test
 public void testNotifyMessageReceived() {
 clients.add(client);
 clientGroup.addHandler(clientHandler);
 verify(clientHandler).messageReceived(client, Mockito.anyObject());
 assertTrue(clientGroup.notifyMessageReceived(client, Mockito.anyObject()));
 }

 @Test
 public void testNotifyMessageReceived_WhenClientNotPresent() {
 assertFalse(clientGroup.notifyMessageReceived(client, Mockito.anyObject()));
 }

 @Test
 public void testNotifyExceptionCaught() {
 clients.add(client);
 clientGroup.addHandler(clientHandler);
 verify(clientHandler).exceptionCaught(client, Mockito.any(Throwable.class));
 assertTrue(clientGroup.notifyExceptionCaught(client, Mockito.any(Throwable.class)));
 }

 @Test
 public void testNotifyExceptionCaught_WhenClientNotPresent() {
 assertFalse(clientGroup.notifyExceptionCaught(client, Mockito.any(Throwable.class)));
 }

 @Test
 public void testNotifyExceptionCaught_WhenNoHandler() {
 clients.add(client);
 assertFalse(clientGroup.notifyExceptionCaught(client, Mockito.any(Throwable.class)));
 }

 @Test
 public void testIterator() {
 clients.addAll(Arrays.asList(client, new Client("Client2")));
 assertTrue(clientGroup.iterator().hasNext());
 assertTrue(clientGroup.iterator().next().equals(client));
 }

 @Test
 public void testContains() {
 clients.add(client);
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize() {
 clients.addAll(Arrays.asList(client, new Client("Client2")));
 assertEquals(2, clientGroup.size());
 }

 @Test
 public void testAdd() {
 assertFalse(clientGroup.contains(client));
 assertTrue(clientGroup.add(client));
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testAdd_WhenClientAlreadyPresent() {
 clients.add(client);
 assertFalse(clientGroup.add(client));
 }

 @Test
 public void testRemove() {
 clients.add(client);
 assertTrue(clientGroup.remove(client));
 assertFalse(clientGroup.contains(client));
 }

 @Test
 public void testRemove_WhenClientNotPresent() {
 clients.add(client);
 assertFalse(clientGroup.remove(new Client("Client3")));
 }

 @Test
 public void testCreateSubGroup() {
 assertNotNull(subGroup);
 assertTrue(clientGroup.contains(subGroup));
 }

 @Test
 public void testAddClientHandler() {
 assertNull(clientGroup.handler());
 clientGroup.addClientHandler(clientHandler);
 assertSame(clientHandler, clientGroup.handler());
 }
}
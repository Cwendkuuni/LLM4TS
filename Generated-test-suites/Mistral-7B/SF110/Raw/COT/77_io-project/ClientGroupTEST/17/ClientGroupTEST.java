import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.*;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private ClientGroup subGroup;
 private ClientGroup parentGroup;
 private ClientHandler handler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 group = new ClientGroup(parentGroup);
 client = new Client();
 subGroup = new ClientGroup(group);
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
 parentGroup.add(group);
 }

 @Test
 public void testConstructor() {
 assertNotNull(group);
 assertEquals(group.parentGroup(), parentGroup);
 assertFalse(group.clients().containsAll(Collections.singleton(client)));
 assertFalse(group.subGroups().contains(subGroup.getReference()));
 assertNull(group.handler);
 }

 @Test
 public void testNotifyMessageSent() {
 when(client.getName()).thenReturn("clientName");
 group.add(client);
 subGroup.add(client);
 handler.messageSent(client, "testMessage");

 verify(parentGroup, times(1)).notifyMessageSent(client, "testMessage");
 verify(subGroup, times(1)).notifyMessageSent(client, "testMessage");
 verify(handler, times(1)).messageSent(client, "testMessage");
 }

 @Test
 public void testNotifyMessageSentWhenClientNotPresent() {
 when(client.getName()).thenReturn("clientName");
 group.notifyMessageSent(client, "testMessage");

 verifyZeroInteractions(parentGroup);
 verifyZeroInteractions(subGroup);
 verifyZeroInteractions(handler);
 }

 @Test
 public void testNotifyMessageSentWhenHandlerIsNull() {
 group.handler = null;
 group.notifyMessageSent(client, "testMessage");

 verifyZeroInteractions(parentGroup);
 verifyZeroInteractions(subGroup);
 }

 @Test
 public void testNotifyMessageReceived() {
 when(client.getName()).thenReturn("clientName");
 group.add(client);
 subGroup.add(client);
 handler.messageReceived(client, "testMessage");

 verify(parentGroup, times(1)).notifyMessageReceived(client, "testMessage");
 verify(subGroup, times(1)).notifyMessageReceived(client, "testMessage");
 verify(handler, times(1)).messageReceived(client, "testMessage");
 }

 @Test
 public void testNotifyMessageReceivedWhenClientNotPresent() {
 when(client.getName()).thenReturn("clientName");
 group.notifyMessageReceived(client, "testMessage");

 verifyZeroInteractions(parentGroup);
 verifyZeroInteractions(subGroup);
 verifyZeroInteractions(handler);
 }

 @Test
 public void testNotifyMessageReceivedWhenHandlerIsNull() {
 group.handler = null;
 group.notifyMessageReceived(client, "testMessage");

 verifyZeroInteractions(parentGroup);
 verifyZeroInteractions(subGroup);
 }

 @Test
 public void testNotifyExceptionCaught() {
 when(client.getName()).thenReturn("clientName");
 group.add(client);
 subGroup.add(client);
 handler.exceptionCaught(client, new RuntimeException("testException"));

 verify(parentGroup, times(1)).notifyExceptionCaught(client, new RuntimeException("testException"));
 verify(subGroup, times(1)).notifyExceptionCaught(client, new RuntimeException("testException"));
 verify(handler, times(1)).exceptionCaught(client, new RuntimeException("testException"));
 }

 @Test
 public void testNotifyExceptionCaughtWhenClientNotPresent() {
 when(client.getName()).thenReturn("clientName");
 group.notifyExceptionCaught(client, new RuntimeException("testException"));

 verifyZeroInteractions(parentGroup);
 verifyZeroInteractions(subGroup);
 verifyZeroInteractions(handler);
 }

 @Test
 public void testNotifyExceptionCaughtWhenHandlerIsNull() {
 group.handler = null;
 group.notifyExceptionCaught(client, new RuntimeException("testException"));

 verifyZeroInteractions(parentGroup);
 verifyZeroInteractions(subGroup);
 }

 @Test
 public void testIterator() {
 List<Client> clients = new ArrayList<>();
 clients.add(client);
 when(group.clients()).thenReturn(clients);
 Iterator<Client> iterator = group.iterator();
 truth(iterator.hasNext());
 truth(iterator.next().equals(client));
 }

 @Test
 public void testContains() {
 truth(group.contains(client));
 falsity(group.contains(new Client()));
 }

 @Test
 public void testSize() {
 truth(group.size() == 1);
 }

 @Test
 public void testAdd() {
 truth(group.add(client));
 truth(group.contains(client));
 truth(group.size() == 2);
 falsity(group.add(client));
 }

 @Test
 public void testRemove() {
 truth(group.remove(client));
 falsity(group.contains(client));
 truth(group.size() == 1);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup newSubGroup = group.createSubGroup();
 truth(newSubGroup.parentGroup() == group);
 truth(group.subGroups().contains(newSubGroup.getReference()));
 truth(newSubGroup.size() == 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifySubGroupIterator() {
 ClientGroup newSubGroup = group.createSubGroup();
 Iterator<ClientGroup> iterator = group.subGroups().iterator();
 iterator.next().add(client);
 }
}
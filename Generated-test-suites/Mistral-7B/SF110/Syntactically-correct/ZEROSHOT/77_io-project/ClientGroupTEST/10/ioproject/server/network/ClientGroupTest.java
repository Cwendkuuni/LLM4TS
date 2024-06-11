import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private Client client2;
 private ClientHandler clientHandler;
 private ClientGroup subGroup;

 @Before
 public void setUp() {
 client = new Client("client1");
 client2 = new Client("client2");
 clientGroup = new ClientGroup(null);
 clientHandler = Mockito.mock(ClientHandler.class);
 subGroup = new ClientGroup(clientGroup);
 }

 @Test
 public void testAddClient() {
 // Test adding a client to an empty group
 assertTrue(clientGroup.add(client));
 // Test adding a client that is already in the group
 assertFalse(clientGroup.add(client));
 // Test adding a client to a group with other clients
 Set<Client> clients = new HashSet<>(Arrays.asList(client, client2));
 clientGroup.clients = clients;
 assertTrue(clientGroup.add(client2));
 }

 @Test
 public void testRemoveClient() {
 // Test removing a client from an empty group
 assertFalse(clientGroup.remove(client));
 // Test removing a client that is not in the group
 assertFalse(clientGroup.remove(client2));
 // Test removing a client from a group with other clients
 Set<Client> clients = new HashSet<>(Arrays.asList(client, client2));
 clientGroup.clients = clients;
 assertTrue(clientGroup.remove(client));
 assertFalse(clientGroup.remove(client2));
 }

 @Test
 public void testContains() {
 // Test containing a client that is not in the group
 assertFalse(clientGroup.contains(client2));
 // Test containing a client that is in the group
 clientGroup.add(client);
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize() {
 // Test the size of an empty group
 assertTrue(clientGroup.size() == 0);
 // Test the size of a group with one client
 clientGroup.add(client);
 assertTrue(clientGroup.size() == 1);
 // Test the size of a group with multiple clients
 Set<Client> clients = new HashSet<>(Arrays.asList(client, client2));
 clientGroup.clients = clients;
 assertTrue(clientGroup.size() == 2);
 }

 @Test
 public void testIterator() {
 // Test the iterator of an empty group
 assertFalse(clientGroup.iterator().hasNext());
 // Test the iterator of a group with multiple clients
 Set<Client> clients = new HashSet<>(Arrays.asList(client, client2));
 clientGroup.clients = clients;
 Set<Client> result = new HashSet<>(clientGroup);
 assertTrue(result.containsAll(clients));
 }

 @Test
 public void testNotifyMessageSent() {
 // Test notifying a message sent to a client that is not in the group
 assertFalse(clientGroup.notifyMessageSent(client, "test message"));
 // Test notifying a message sent to a client in the group
 clientGroup.add(client);
 Mockito.doAnswer(invocation -> true).when(clientHandler).messageSent(ArgumentsMatchers.any(Client.class), ArgumentsMatchers.any());
 clientGroup.addClientHandler(clientHandler);
 assertTrue(clientGroup.notifyMessageSent(client, "test message"));
 // Test notifying a message sent to a client in a subgroup
 clientGroup.add(client2);
 subGroup.addClientHandler(clientHandler);
 assertTrue(clientGroup.notifyMessageSent(client2, "test message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 // Test notifying a message received by a client that is not in the group
 assertFalse(clientGroup.notifyMessageReceived(client, "test message"));
 // Test notifying a message received by a client in the group
 clientGroup.add(client);
 Mockito.doAnswer(invocation -> true).when(clientHandler).messageReceived(ArgumentsMatchers.any(Client.class), ArgumentsMatchers.any());
 clientGroup.addClientHandler(clientHandler);
 assertTrue(clientGroup.notifyMessageReceived(client, "test message"));
 // Test notifying a message received by a client in a subgroup
 clientGroup.add(client2);
 subGroup.addClientHandler(clientHandler);
 assertTrue(clientGroup.notifyMessageReceived(client2, "test message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 // Test notifying an exception caught by a client that is not in the group
 assertFalse(clientGroup.notifyExceptionCaught(client, new RuntimeException()));
 // Test notifying an exception caught by a client in the group
 clientGroup.add(client);
 Mockito.doAnswer(invocation -> true).when(clientHandler).exceptionCaught(ArgumentsMatchers.any(Client.class), ArgumentsMatchers.any(Throwable.class));
 clientGroup.addClientHandler(clientHandler);
 assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException()));
 // Test notifying an exception caught by a client in a subgroup
 clientGroup.add(client2);
 subGroup.addClientHandler(clientHandler);
 assertTrue(clientGroup.notifyExceptionCaught(client2, new RuntimeException()));
 }

 @Test
 public void testCreateSubGroup() {
 // Test creating a subgroup of an empty group
 assertNull(clientGroup.createSubGroup());
 // Test creating a subgroup of a group with clients
 clientGroup.add(client);
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertFalse(subGroup.contains(client));
 assertTrue(subGroup.parentGroup() == clientGroup);
 }
}
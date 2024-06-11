import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import java.util.*;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private Client client;
 private ClientGroup subGroup;
 private ClientHandler clientHandler;

 @Before
 public void setUp() {
 client = new Client("Client1");
 clientGroup = new ClientGroup(null);
 subGroup = clientGroup.createSubGroup();
 clientHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // empty implementation
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // empty implementation
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // empty implementation
 }

 @Override
 public void clientAdded(Client client) {
 // empty implementation
 }

 @Override
 public void clientRemoved(Client client) {
 // empty implementation
 }
 };
 clientGroup.addClientHandler(clientHandler);
 clientGroup.add(client);
 }

 @Test
 public void testParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup clientGroupUnderTest = new ClientGroup(parentGroup);
 assertEquals(parentGroup, clientGroupUnderTest.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 ClientGroup subGroupUnderTest = new ClientGroup(clientGroup);
 subGroupUnderTest.add(client);
 assertTrue(clientGroup.notifyMessageSent(client, "Test message"));
 assertTrue(subGroupUnderTest.notifyMessageSent(client, "Test message"));
 }

 @Test
 public void testNotifyMessageSent_NoSubGroupOrClientHandler() {
 assertFalse(clientGroup.notifyMessageSent(client, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 ClientGroup subGroupUnderTest = new ClientGroup(clientGroup);
 subGroupUnderTest.add(client);
 assertTrue(clientGroup.notifyMessageReceived(client, "Test message"));
 assertTrue(subGroupUnderTest.notifyMessageReceived(client, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived_NoSubGroupOrClientHandler() {
 assertFalse(clientGroup.notifyMessageReceived(client, "Test message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 ClientGroup subGroupUnderTest = new ClientGroup(clientGroup);
 subGroupUnderTest.add(client);
 Throwable cause = new RuntimeException("Test exception");
 assertTrue(clientGroup.notifyExceptionCaught(client, cause));
 assertTrue(subGroupUnderTest.notifyExceptionCaught(client, cause));
 }

 @Test
 public void testNotifyExceptionCaught_NoSubGroupOrClientHandler() {
 assertFalse(clientGroup.notifyExceptionCaught(client, new RuntimeException("Test exception")));
 }

 @Test
 public void testIterator() {
 List<Client> clients = new ArrayList<>(clientGroup);
 Iterator<Client> iterator = clientGroup.iterator();
 assertEquals(clients.get(0), iterator.next());
 }

 @Test
 public void testContains() {
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testSize() {
 assertEquals(1, clientGroup.size());
 }

 @Test
 public void testAdd() {
 Client newClient = new Client("New client");
 assertTrue(clientGroup.add(newClient));
 assertEquals(2, clientGroup.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd_UnableToAddAfterIteratorUsage() {
 Iterator<Client> iterator = clientGroup.iterator();
 iterator.next();
 try {
 clientGroup.add(new Client("New client"));
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testRemove() {
 assertTrue(clientGroup.remove(client));
 assertEquals(0, clientGroup.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemove_NullClient() {
 clientGroup.remove(null);
 }

 @Test
 public void testRemove_ClientNotPresent() {
 Client nonExistentClient = new Client("Non existent client");
 assertFalse(clientGroup.remove(nonExistentClient));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroupUnderTest = clientGroup.createSubGroup();
 assertTrue(subGroupUnderTest.parentGroup() == clientGroup);
 }

 @Test
 public void testCreateSubGroup_NullParentGroup() {
 try {
 ClientGroup subGroupUnderTest = new ClientGroup(null).createSubGroup();
 fail("Expected IllegalArgumentException: parentGroup cannot be null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddClientHandler() {
 ClientHandler newClientHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // empty implementation
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // empty implementation
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // empty implementation
 }

 @Override
 public void clientAdded(Client client) {
 // empty implementation
 }

 @Override
 public void clientRemoved(Client client) {
 // empty implementation
 }
 };
 clientGroup.addClientHandler(newClientHandler);
 }

 @Test
 public void testContains_NullClient() {
 assertFalse(clientGroup.contains(null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove_ConcurrentModificationException() {
 Iterator<Client> iterator = clientGroup.iterator();
 iterator.next();
 clientGroup.remove(client);
 }
}
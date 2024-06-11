import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakReference;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private Client client1;
 private Client client2;
 private ClientHandler handler;

 @Before
 public void setUp() {
 client = new Client("client1");
 client1 = new Client("client1_1");
 client2 = new Client("client1_2");
 group = new ClientGroup(null);
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
 }

 // Test methods for constructor
 @Test
 public void testClientGroupConstructor() {
 ClientGroup group = new ClientGroup(null);
 assertNotNull(group);
 assertNull(group.parentGroup());
 assertNotNull(group.subGroups());
 assertNotNull(group.clients());
 assertNull(group.handler());
 }

 // Test methods for parentGroup()
 @Test
 public void testGetParentGroup() {
 ClientGroup parentGroup = new ClientGroup(null);
 ClientGroup group = new ClientGroup(parentGroup);
 assertSame(parentGroup, group.parentGroup());
 }

 // Test methods for notifyMessageSent()
 @Test
 public void testNotifyMessageSent_ClientIsPresent() {
 group.add(client);
 assertTrue(group.notifyMessageSent(client, new Object()));
 }

 @Test
 public void testNotifyMessageSent_ClientIsNotPresent() {
 assertFalse(group.notifyMessageSent(client, new Object()));
 }

 @Test
 public void testNotifyMessageSent_ClientIsInSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 subGroup.add(client);
 assertFalse(group.notifyMessageSent(client, new Object()));
 assertTrue(subGroup.notifyMessageSent(client, new Object()));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageSent_NullHandler() {
 group.handler = null;
 group.notifyMessageSent(client, new Object());
 }

 // Test methods for notifyMessageReceived()
 @Test
 public void testNotifyMessageReceived_ClientIsPresent() {
 group.add(client);
 assertTrue(group.notifyMessageReceived(client, new Object()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsNotPresent() {
 assertFalse(group.notifyMessageReceived(client, new Object()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsInSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 subGroup.add(client);
 assertFalse(group.notifyMessageReceived(client, new Object()));
 assertTrue(subGroup.notifyMessageReceived(client, new Object()));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageReceived_NullHandler() {
 group.handler = null;
 group.notifyMessageReceived(client, new Object());
 }

 // Test methods for notifyExceptionCaught()
 @Test
 public void testNotifyExceptionCaught_ClientIsPresent() {
 group.add(client);
 assertTrue(group.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsNotPresent() {
 assertFalse(group.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsInSubGroup() {
 ClientGroup subGroup = group.createSubGroup();
 subGroup.add(client);
 assertFalse(group.notifyExceptionCaught(client, new RuntimeException()));
 assertTrue(subGroup.notifyExceptionCaught(client, new RuntimeException()));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyExceptionCaught_NullHandler() {
 group.handler = null;
 group.notifyExceptionCaught(client, new RuntimeException());
 }

 // Test methods for iterator()
 @Test
 public void testIterator_ReturnsExpectedSize() {
 Set<Client> set = new HashSet<Client>(Arrays.asList(client, client1, client2));
 group.addAll(set);
 Iterator<Client> iterator = group.iterator();
 assertEquals(set.size(), iterator.next().size());
 }

 // Test methods for contains()
 @Test
 public void testContains_ClientIsPresent() {
 group.add(client);
 assertTrue(group.contains(client));
 }

 @Test
 public void testContains_ClientIsNotPresent() {
 assertFalse(group.contains(client1));
 }

 // Test methods for size()
 @Test
 public void testSize_ReturnsExpectedSize() {
 Set<Client> set = new HashSet<Client>(Arrays.asList(client, client1, client2));
 group.addAll(set);
 assertEquals(set.size(), group.size());
 }

 // Test methods for add()
 @Test
 public void testAdd_ClientIsAddedToGroup() {
 group.add(client);
 assertTrue(group.contains(client));
 }

 @Test(expected = NullPointerException.class)
 public void testAdd_NullClient() {
 group.add(null);
 }

 @Test
 public void testAdd_ClientIsAlreadyPresent() {
 group.add(client);
 group.add(client);
 assertEquals(1, group.size());
 }

 // Test methods for remove()
 @Test
 public void testRemove_ClientIsRemovedFromGroup() {
 group.add(client);
 group.remove(client);
 assertFalse(group.contains(client));
 }

 @Test(expected = NullPointerException.class)
 public void testRemove_NullClient() {
 group.remove(null);
 }

 @Test
 public void testRemove_ClientIsNotPresent() {
 group.remove(client1);
 assertEquals(1, group.size());
 }

 // Test methods for createSubGroup()
 @Test
 public void testCreateSubGroup_SubGroupIsCreated() {
 ClientGroup subGroup = group.createSubGroup();
 assertTrue(subGroup instanceof ClientGroup);
 }

 @Test
 public void testCreateSubGroup_ParentGroupIsSetCorrectly() {
 ClientGroup subGroup = group.createSubGroup();
 assertSame(group, subGroup.parentGroup());
 }

 @Test
 public void testCreateSubGroup_SubGroupsAreAddedToParentGroup() {
 ClientGroup subGroup = group.createSubGroup();
 group.add(subGroup);
 assertTrue(group.subGroups().contains(new WeakReference<ClientGroup>(subGroup)));
 }
}
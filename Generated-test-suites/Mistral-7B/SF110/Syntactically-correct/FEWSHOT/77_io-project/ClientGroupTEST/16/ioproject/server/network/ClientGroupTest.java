package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {

 ClientGroup testGroup;
 Client testClient1;
 Client testClient2;
 Client testClient3;
 ClientHandler testHandler;

 @Before
 public void setUp() {
 testGroup = new ClientGroup(null);
 testClient1 = new Client("Client1");
 testClient2 = new Client("Client2");
 testClient3 = new Client("Client3");
 testHandler = new ClientHandler() {
 @Override
 public void messageSent(Client sender, Object message) {
 // TODO: Implement
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 // TODO: Implement
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 // TODO: Implement
 }

 @Override
 public void clientAdded(Client client) {
 // TODO: Implement
 }

 @Override
 public void clientRemoved(Client client) {
 // TODO: Implement
 }
 };
 testGroup.addClientHandler(testHandler);
 }

 @After
 public void tearDown() {
 testGroup = null;
 testClient1 = null;
 testClient2 = null;
 testClient3 = null;
 testHandler = null;
 }

 @Test
 public void testSize() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 assertEquals(3, testGroup.size());
 }

 @Test
 public void testContains() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 assertTrue(testGroup.contains(testClient1));
 assertTrue(testGroup.contains(testClient2));
 assertTrue(testGroup.contains(testClient3));
 }

 @Test
 public void testAdd() {
 testGroup.add(testClient1);
 assertFalse(testGroup.add(testClient1));
 testGroup.add(testClient2);
 assertTrue(testGroup.contains(testClient2));
 }

 @Test
 public void testIterator() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 Set<Client> clients = new LinkedHashSet<>(Arrays.asList(testClient1, testClient2, testClient3));
 Iterator<Client> iterator = testGroup.iterator();
 assertEquals(testClient1, iterator.next());
 assertEquals(testClient2, iterator.next());
 assertEquals(testClient3, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testRemove() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 testGroup.remove(testClient2);
 assertFalse(testGroup.contains(testClient2));
 assertTrue(testGroup.contains(testClient1));
 assertTrue(testGroup.contains(testClient3));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = testGroup.createSubGroup();
 testGroup.add(subGroup);
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 subGroup.add(testClient1);
 subGroup.add(testClient4);
 assertTrue(testGroup.contains(testClient1));
 assertTrue(testGroup.contains(testClient2));
 assertTrue(testGroup.contains(testClient3));
 assertTrue(testGroup.contains(subGroup));
 assertTrue(subGroup.size() == 2);
 }

 @Test
 public void testNotifyMessageSent() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 String testMessage = "Test message";
 testHandler.messageSent(testClient1, testMessage);
 assertEquals(1, testGroup.getNumMessagesSent());
 assertTrue(testClient1.hasMessage());
 assertEquals(testMessage, testClient1.getMessage());
 }

 @Test
 public void testNotifyMessageReceived() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 String testMessage = "Test message";
 testClient1.sendMessage(testMessage, testGroup);
 assertEquals(1, testGroup.getNumMessagesReceived());
 assertTrue(testClient1.hasMessage());
 assertEquals(testMessage, testClient1.getMessage());
 }

 @Test
 public void testNotifyExceptionCaught() {
 testGroup.add(testClient1);
 testGroup.add(testClient2);
 testGroup.add(testClient3);
 String testMessage = "Test message";
 testClient1.sendMessage(testMessage, testGroup);
 testClient1.setException(new RuntimeException("Test exception"));
 testGroup.notifyExceptionCaught(testClient1, testClient1.getException());
 assertTrue(testClient1.hasException());
 assertEquals(testClient1.getException().getMessage(), "Test exception");
 }
}
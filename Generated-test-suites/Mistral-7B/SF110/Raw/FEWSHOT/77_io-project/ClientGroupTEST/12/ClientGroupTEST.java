package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {

 private ClientGroup parentGroup;
 private ClientGroup subGroup;
 private ClientHandler handler;
 private Set<Client> clients;
 private Client client1;
 private Client client2;
 private Client client3;

 @Before
 public void setUp() {
 this.parentGroup = new ClientGroup(null);
 this.subGroup = new ClientGroup(this.parentGroup);
 this.handler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 // Do nothing
 }

 @Override
 public void messageReceived(Client client, Object message) {
 // Do nothing
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 // Do nothing
 }

 @Override
 public void clientAdded(Client client) {
 // Do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // Do nothing
 }
 };
 this.parentGroup.addClientHandler(this.handler);
 this.clients = new LinkedHashSet<Client>();
 this.client1 = new Client("client1");
 this.client2 = new Client("client2");
 this.client3 = new Client("client3");
 }

 @After
 public void tearDown() {
 this.parentGroup = null;
 this.subGroup = null;
 this.handler = null;
 this.clients = null;
 this.client1 = null;
 this.client2 = null;
 this.client3 = null;
 System.gc();
 }

 @Test
 public void testConstructor() {
 assertEquals(null, this.parentGroup.parentGroup());
 assertNotNull(this.parentGroup.subGroups);
 assertNotNull(this.parentGroup.clients);
 assertNull(this.parentGroup.handler);
 }

 @Test
 public void testParentGroup() {
 assertEquals(this.parentGroup, this.parentGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.subGroup.add(this.client3);

 assertTrue(this.parentGroup.notifyMessageSent(this.client1, "message1"));
 assertTrue(this.subGroup.notifyMessageSent(this.client3, "message1"));
 assertFalse(this.parentGroup.notifyMessageSent(this.client3, "message1"));

 assertFalse(this.parentGroup.notifyMessageSent(null, "message1"));
 assertFalse(this.parentGroup.notifyMessageSent(new Object(), "message1"));
 }

 @Test
 public void testNotifyMessageReceived() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.subGroup.add(this.client3);

 assertTrue(this.parentGroup.notifyMessageReceived(this.client1, "message1"));
 assertTrue(this.subGroup.notifyMessageReceived(this.client3, "message1"));
 assertFalse(this.parentGroup.notifyMessageReceived(this.client3, "message1"));

 assertFalse(this.parentGroup.notifyMessageReceived(null, "message1"));
 assertFalse(this.parentGroup.notifyMessageReceived(new Object(), "message1"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.subGroup.add(this.client3);

 assertTrue(this.parentGroup.notifyExceptionCaught(this.client1, new RuntimeException("Exception1")));
 assertTrue(this.subGroup.notifyExceptionCaught(this.client3, new RuntimeException("Exception1")));
 assertFalse(this.parentGroup.notifyExceptionCaught(this.client3, new RuntimeException("Exception1")));

 assertFalse(this.parentGroup.notifyExceptionCaught(null, new RuntimeException("Exception1")));
 assertFalse(this.parentGroup.notifyExceptionCaught(new Object(), new RuntimeException("Exception1")));
 }

 @Test
 public void testIterator() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.clients.add(this.client3);

 Iterator<Client> iterator = this.parentGroup.iterator();
 assertEquals(this.client1, iterator.next());
 assertEquals(this.client2, iterator.next());
 assertEquals(this.client3, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testContains() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.clients.add(this.client3);

 assertTrue(this.parentGroup.contains(this.client1));
 assertTrue(this.parentGroup.contains(this.client2));
 assertTrue(this.parentGroup.contains(this.client3));
 assertFalse(this.parentGroup.contains(new Object()));
 }

 @Test
 public void testSize() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.clients.add(this.client3);
 assertEquals(3, this.parentGroup.size());
 }

 @Test
 public void testAdd() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 assertFalse(this.parentGroup.add(this.client1));
 assertTrue(this.parentGroup.add(this.client3));
 assertEquals(3, this.parentGroup.size());
 }

 @Test
 public void testRemove() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 this.clients.add(this.client3);
 assertTrue(this.parentGroup.remove(this.client1));
 assertEquals(2, this.parentGroup.size());
 }

 @Test
 public void testCreateSubGroup() {
 this.clients.add(this.client1);
 this.clients.add(this.client2);
 ClientGroup subGroup = this.parentGroup.createSubGroup();
 assertTrue(this.parentGroup.contains(subGroup));
 assertEquals(0, subGroup.size());
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup group = new ClientGroup(null);
 assertNull(group.handler);
 group.addClientHandler(this.handler);
 assertEquals(this.handler, group.handler);
 }
}
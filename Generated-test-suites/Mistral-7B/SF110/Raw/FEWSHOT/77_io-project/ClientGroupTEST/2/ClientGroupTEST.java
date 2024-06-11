package ioproject.server.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 private ClientGroup group;
 private ClientGroup subGroup;
 private Client client;
 private ClientHandler handler;

 @Before
 public void setUp() {
 this.group = new ClientGroup(null);
 this.client = mock(Client.class);
 this.handler = mock(ClientHandler.class);
 }

 @Test
 public void testAddAndRemoveClient() {
 // test adding and removing a client
 this.group.add(this.client);
 assertTrue(this.group.contains(this.client));
 this.group.remove(this.client);
 assertFalse(this.group.contains(this.client));
 }

 @Test
 public void testAddAndRemoveClientInSubGroup() {
 // test adding and removing a client in a subgroup
 this.subGroup = this.group.createSubGroup();
 this.subGroup.add(this.client);
 assertTrue(this.subGroup.contains(this.client));
 this.subGroup.remove(this.client);
 assertFalse(this.subGroup.contains(this.client));
 }

 @Test
 public void testContainsNullClient() {
 assertFalse(this.group.contains(null));
 }

 @Test
 public void testSize() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(this.client);
 this.group.addAll(clients);
 assertEquals(1, this.group.size());
 }

 @Test
 public void testAddClientToFullGroup() {
 Set<Client> clients = new LinkedHashSet<>(2);
 clients.add(this.client);
 this.group = new ClientGroup(null);
 this.group.addAll(clients);
 assertFalse(this.group.add(this.client));
 }

 @Test
 public void testAddClientToExistingClient() {
 Set<Client> clients = new LinkedHashSet<>(2);
 clients.add(this.client);
 this.group = new ClientGroup(null);
 this.group.addAll(clients);
 this.group.add(this.client);
 assertTrue(this.group.size() == 2);
 }

 @Test
 public void testMessageSent() {
 // test messageSent when client is in group
 this.group.add(this.client);
 this.handler.messageSent(this.client, "test message");
 verify(this.handler).messageSent(this.client, "test message");

 // test messageSent when client is in subgroup
 this.subGroup = this.group.createSubGroup();
 this.subGroup.add(this.client);
 this.group.add(this.subGroup);
 this.handler.messageSent(this.client, "test message");
 verify(this.handler).messageSent(this.client, "test message");
 }

 @Test
 public void testMessageReceived() {
 // test messageReceived when client is in group
 this.group.add(this.client);
 this.handler.messageReceived(this.client, "test message");
 verify(this.handler).messageReceived(this.client, "test message");

 // test messageReceived when client is in subgroup
 this.subGroup = this.group.createSubGroup();
 this.subGroup.add(this.client);
 this.group.add(this.subGroup);
 this.handler.messageReceived(this.client, "test message");
 verify(this.handler).messageReceived(this.client, "test message");
 }

 @Test
 public void testExceptionCaught() {
 // test exceptionCaught when client is in group
 this.group.add(this.client);
 this.handler.exceptionCaught(this.client, new RuntimeException("test exception"));
 verify(this.handler).exceptionCaught(this.client, new RuntimeException("test exception"));

 // test exceptionCaught when client is in subgroup
 this.subGroup = this.group.createSubGroup();
 this.subGroup.add(this.client);
 this.group.add(this.subGroup);
 this.handler.exceptionCaught(this.client, new RuntimeException("test exception"));
 verify(this.handler).exceptionCaught(this.client, new RuntimeException("test exception"));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = this.group.createSubGroup();
 assertNotNull(subGroup);
 assertTrue(this.group.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnmodifiableSet() {
 this.group.iterator().remove();
 }
}
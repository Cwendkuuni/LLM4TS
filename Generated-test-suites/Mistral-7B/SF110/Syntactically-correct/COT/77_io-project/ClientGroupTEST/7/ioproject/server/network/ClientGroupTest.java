import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.common.network.ClientGroup;
import ioproject.common.network.ClientHandler.MessageType;

public class ClientGroupTest {

 private ClientGroup group;
 private Client client;
 private ClientGroup subGroup;
 private ClientHandler handler;

 @Before
 public void setup() {
 this.group = new ClientGroup(null);
 this.client = mock(Client.class);
 this.subGroup = mock(ClientGroup.class);
 this.handler = mock(ClientHandler.class);
 }

 @Test
 public void testConstructor() {
 assertNotNull(this.group);
 assertNull(this.group.parentGroup());
 assertNull(this.group.handler);
 }

 @Test
 public void testParentGroup() {
 this.group.add(this.client);
 this.group.addClientHandler(this.handler);
 ClientGroup parentGroup = new ClientGroup(this.group);
 assertEquals(this.group, parentGroup.parentGroup());
 assertSame(this.handler, parentGroup.handler());
 }

 @Test
 public void testNotifyMessageSent_ClientExists() {
 when(this.group.notifyMessageSent(this.client, "Test message")).thenReturn(true);
 when(this.subGroup.notifyMessageSent(this.client, "Test message")).thenReturn(false);
 when(this.handler.messageSent(this.client, "Test message")).thenReturn(true);

 this.group.add(this.client);
 this.group.add(this.subGroup);
 this.group.addClientHandler(this.handler);

 assertTrue(this.group.notifyMessageSent(this.client, "Test message"));
 }

 @Test
 public void testNotifyMessageSent_ClientDoesNotExist() {
 when(this.handler.messageSent(this.client, "Test message")).thenReturn(true);

 assertFalse(this.group.notifyMessageSent(new Client(), "Test message"));
 }

 @Test
 public void testNotifyMessageSent_MultipleSubGroups() {
 when(this.group.notifyMessageSent(this.client, "Test message")).thenReturn(false);
 when(this.subGroup.notifyMessageSent(this.client, "Test message")).thenReturn(false);
 when(this.subGroup2.notifyMessageSent(this.client, "Test message")).thenReturn(true);
 when(this.handler.messageSent(this.client, "Test message")).thenReturn(true);

 ClientGroup group = new ClientGroup(this.group);
 group.add(this.subGroup);
 group.add(this.subGroup2);
 group.addClientHandler(this.handler);

 this.group.add(this.client);
 this.group.add(this.subGroup);
 this.group.add(this.subGroup2);
 this.group.addClientHandler(this.handler);

 assertTrue(this.group.notifyMessageSent(this.client, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientExists() {
 when(this.group.notifyMessageReceived(this.client, "Test message")).thenReturn(true);
 when(this.subGroup.notifyMessageReceived(this.client, "Test message")).thenReturn(false);
 when(this.handler.messageReceived(this.client, "Test message")).thenReturn(true);

 this.group.add(this.client);
 this.group.add(this.subGroup);
 this.group.addClientHandler(this.handler);

 assertTrue(this.group.notifyMessageReceived(this.client, "Test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientDoesNotExist() {
 when(this.handler.messageReceived(this.client, "Test message")).thenReturn(true);

 assertFalse(this.group.notifyMessageReceived(new Client(), "Test message"));
 }

 @Test
 public void testNotifyExceptionCaught_ClientExists() {
 when(this.group.notifyExceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(true);
 when(this.subGroup.notifyExceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(false);
 when(this.handler.exceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(true);

 this.group.add(this.client);
 this.group.add(this.subGroup);
 this.group.addClientHandler(this.handler);

 assertTrue(this.group.notifyExceptionCaught(this.client, new RuntimeException("Test exception")));
 }

 @Test
 public void testNotifyExceptionCaught_ClientDoesNotExist() {
 when(this.handler.exceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(true);

 assertFalse(this.group.notifyExceptionCaught(new Client(), new RuntimeException("Test exception")));
 }

 @Test
 public void testNotifyExceptionCaught_MultipleSubGroups() {
 when(this.group.notifyExceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(false);
 when(this.subGroup.notifyExceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(false);
 when(this.subGroup2.notifyExceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(true);
 when(this.handler.exceptionCaught(this.client, new RuntimeException("Test exception"))).thenReturn(true);

 ClientGroup group = new ClientGroup(this.group);
 group.add(this.subGroup);
 group.add(this.subGroup2);
 group.addClientHandler(this.handler);

 this.group.add(this.client);
 this.group.add(this.subGroup);
 this.group.add(this.subGroup2);
 this.group.addClientHandler(this.handler);

 assertTrue(this.group.notifyExceptionCaught(this.client, new RuntimeException("Test exception")));
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new LinkedHashSet<Client>(Arrays.asList(this.client));
 Iterator<Client> iterator = this.group.iterator();

 assertEquals(clients, iterator.asList());
 }

 @Test
 public void testContains() {
 this.group.add(this.client);
 assertTrue(this.group.contains(this.client));
 }

 @Test
 public void testSize() {
 this.group.add(this.client);
 assertEquals(1, this.group.size());
 }

 @Test
 public void testAdd() {
 Set<Client> clients = new LinkedHashSet<Client>();
 this.group.add(clients.add(this.client));
 assertEquals(1, this.group.size());
 }

 @Test
 public void testRemove() {
 Set<Client> clients = new HashSet<Client>(Arrays.asList(this.client));
 this.group.addAll(clients);
 assertEquals(1, this.group.size());
 this.group.remove(this.client);
 assertFalse(this.group.contains(this.client));
 assertEquals(0, this.group.size());
 }

 @Test
 public void testAddSubGroup() {
 ClientGroup subGroup = new ClientGroup(this.group);
 this.group.add(subGroup);
 assertEquals(1, this.group.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnmodifiableIterator() {
 Iterator<Client> iterator = this.group.iterator();
 iterator.remove(this.client);
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = this.group.createSubGroup();
 assertSame(this.group, subGroup.parentGroup());
 }

 @Test
 public void testAddClientHandler() {
 ClientHandler handler = mock(ClientHandler.class);
 this.group.addClientHandler(handler);
 assertSame(handler, this.group.handler());
 }

 @Test
 public void testHandlerMessageSent() {
 this.group.add(this.client);
 this.group.addClientHandler(this.handler);
 when(this.handler.messageSent(this.client, MessageType.MESSAGE)).thenReturn(true);
 assertTrue(this.group.notifyMessageSent(this.client, "Test message"));
 }

 @Test
 public void testHandlerMessageReceived() {
 this.group.add(this.client);
 this.group.addClientHandler(this.handler);
 when(this.handler.messageReceived(this.client, MessageType.MESSAGE)).thenReturn(true);
 assertTrue(this.group.notifyMessageReceived(this.client, "Test message"));
 }

 @Test
 public void testHandlerExceptionCaught() {
 this.group.add(this.client);
 this.group.addClientHandler(this.handler);
 when(this.handler.exceptionCaught(this.client, (Throwable) "Test throwable")).thenReturn(true);
 assertTrue(this.group.notifyExceptionCaught(this.client, new RuntimeException("Test exception")));
 }
}
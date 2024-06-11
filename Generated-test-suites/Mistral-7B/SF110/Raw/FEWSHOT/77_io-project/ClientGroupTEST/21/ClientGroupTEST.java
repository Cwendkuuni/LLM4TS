package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup topLevelGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;

 @Test
 public void testAddAndRemoveClient() {
 topLevelGroup = new ClientGroup(null);
 client = new DummyClient();
 handler = new DummyClientHandler();
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(client);
 assertTrue(topLevelGroup.contains(client));
 assertEquals(1, topLevelGroup.size());
 topLevelGroup.remove(client);
 assertFalse(topLevelGroup.contains(client));
 assertEquals(0, topLevelGroup.size());
 }

 @Test
 public void testAddClientToSubGroup() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 client = new DummyClient();
 handler = new DummyClientHandler();
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(subGroup);
 subGroup.addClientHandler(handler);
 subGroup.add(client);
 assertTrue(topLevelGroup.contains(client));
 assertTrue(subGroup.contains(client));
 assertEquals(1, subGroup.size());
 topLevelGroup.remove(client);
 assertFalse(topLevelGroup.contains(client));
 assertFalse(subGroup.contains(client));
 }

 @Test
 public void testNotifyMessageSent() {
 topLevelGroup = new ClientGroup(null);
 client = new DummyClient();
 handler = new DummyClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 assertTrue(message instanceof String);
 assertEquals("test message", message);
 }
 };
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(client);
 topLevelGroup.notifyMessageSent(client, "test message");
 }

 @Test
 public void testNotifyMessageSentToSubGroups() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 client = new DummyClient();
 DummyClientHandler subGroupHandler = new DummyClientHandler() {
 @Override
 public boolean messageSent(Client client, Object message) {
 assertTrue(message instanceof String);
 assertEquals("test message", message);
 return true;
 }
 };
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(subGroup);
 subGroup.addClientHandler(subGroupHandler);
 subGroup.add(client);
 topLevelGroup.notifyMessageSent(client, "test message");
 }

 @Test
 public void testNotifyMessageReceived() {
 topLevelGroup = new ClientGroup(null);
 client = new DummyClient();
 handler = new DummyClientHandler() {
 @Override
 public void messageReceived(Client client, Object message) {
 assertTrue(message instanceof String);
 assertEquals("test message", message);
 }
 };
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(client);
 topLevelGroup.notifyMessageReceived(client, "test message");
 }

 @Test
 public void testNotifyMessageReceivedToSubGroups() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 client = new DummyClient();
 DummyClientHandler subGroupHandler = new DummyClientHandler() {
 @Override
 public boolean messageReceived(Client client, Object message) {
 assertTrue(message instanceof String);
 assertEquals("test message", message);
 return true;
 }
 };
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(subGroup);
 subGroup.addClientHandler(subGroupHandler);
 subGroup.add(client);
 topLevelGroup.notifyMessageReceived(client, "test message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 topLevelGroup = new ClientGroup(null);
 client = new DummyClient();
 handler = new DummyClientHandler() {
 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 assertTrue(cause instanceof RuntimeException);
 assertEquals("test exception", cause.getMessage());
 }
 };
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(client);
 try {
 throw new RuntimeException("test exception");
 } catch (RuntimeException e) {
 topLevelGroup.notifyExceptionCaught(client, e);
 }
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroups() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 client = new DummyClient();
 DummyClientHandler subGroupHandler = new DummyClientHandler() {
 @Override
 public boolean exceptionCaught(Client client, Throwable cause) {
 assertTrue(cause instanceof RuntimeException);
 assertEquals("test exception", cause.getMessage());
 return true;
 }
 };
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(subGroup);
 subGroup.addClientHandler(subGroupHandler);
 subGroup.add(client);
 try {
 throw new RuntimeException("test exception");
 } catch (RuntimeException e) {
 topLevelGroup.notifyExceptionCaught(client, e);
 }
 }

 @Test
 public void testIterator() {
 topLevelGroup = new ClientGroup(null);
 client = new DummyClient();
 topLevelGroup.add(client);
 Iterator<Client> it = topLevelGroup.iterator();
 assertTrue(it.hasNext());
 assertEquals(client, it.next());
 assertFalse(it.hasNext());
 }

 @Test
 public void testContains() {
 topLevelGroup = new ClientGroup(null);
 client = new DummyClient();
 topLevelGroup.add(client);
 assertTrue(topLevelGroup.contains(client));
 assertFalse(topLevelGroup.contains(new DummyClient()));
 }

 @Test
 public void testSize() {
 topLevelGroup = new ClientGroup(null);
 topLevelGroup.add(new DummyClient());
 topLevelGroup.add(new DummyClient());
 assertEquals(2, topLevelGroup.size());
 }

 @Test
 public void testAddSubGroup() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 topLevelGroup.add(subGroup);
 }

 @Test
 public void testCreateSubGroup() {
 topLevelGroup = new ClientGroup(null);
 subGroup = topLevelGroup.createSubGroup();
 assertEquals(0, subGroup.size());
 }
}

class DummyClient {
}

class DummyClientHandler implements ClientHandler {
 @Override
 public void clientAdded(Client client) {
}

 @Override
 public void clientRemoved(Client client) {
}

 @Override
 public void messageReceived(Client client, Object message) {
}

 @Override
 public void messageSent(Client client, Object message) {
}

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
}
}
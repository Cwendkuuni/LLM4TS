package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerAdapter;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private Client client4;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 client4 = new Client("client4");
 handler = new ClientHandlerAdapter();
 rootGroup.addClientHandler(handler);
 }

 @Test
 public void testNotifyMessageSent() {
 ClientGroup group = rootGroup;
 AtomicBoolean inSubGroup = new AtomicBoolean(false);
 ClientHandler subHandler = new ClientHandlerAdapter() {
 @Override
 public void messageSent(Client sender, Object message) {
 inSubGroup.set(true);
 }
 };
 subGroup1.addClientHandler(subHandler);

 group.add(client1);
 group.add(client2);
 group.notifyMessageSent(client1, "test message");
 assertTrue(group.contains(client1));
 assertTrue(group.contains(client2));
 assertTrue(inSubGroup.get());

 group = subGroup1;
 inSubGroup.set(false);
 subGroup2.addClientHandler(subHandler);
 group.add(client3);
 group.add(client4);
 group.notifyMessageSent(client3, "test message");
 assertTrue(group.contains(client3));
 assertTrue(group.contains(client4));
 assertTrue(inSubGroup.get());
 }

 @Test
 public void testNotifyMessageReceived() {
 ClientGroup group = rootGroup;
 Client client = client1;
 AtomicBoolean received = new AtomicBoolean(false);
 ClientHandler subHandler = new ClientHandlerAdapter() {
 @Override
 public void messageReceived(Client sender, Object message) {
 received.set(true);
 }
 };
 subGroup1.addClientHandler(subHandler);

 group.add(client1);
 group.add(client2);
 group.notifyMessageReceived(client1, "test message");
 assertTrue(group.contains(client1));
 assertTrue(group.contains(client2));
 assertFalse(client1.hasMessage());
 assertTrue(received.get());

 group = subGroup1;
 received.set(false);
 group.add(client3);
 group.add(client4);
 group.notifyMessageReceived(client3, "test message");
 assertTrue(group.contains(client3));
 assertTrue(group.contains(client4));
 assertFalse(client3.hasMessage());
 assertTrue(received.get());
 }

 @Test
 public void testNotifyExceptionCaught() {
 ClientGroup group = rootGroup;
 Client client = client1;
 AtomicBoolean exceptionCaught = new AtomicBoolean(false);
 ClientHandler subHandler = new ClientHandlerAdapter() {
 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 exceptionCaught.set(true);
 }
 };
 subGroup1.addClientHandler(subHandler);

 group.add(client1);
 group.add(client2);
 group.notifyExceptionCaught(client1, new RuntimeException("test exception"));
 assertTrue(group.contains(client1));
 assertTrue(group.contains(client2));
 assertTrue("test exception".equals(client1.getCause().getMessage()));
 assertTrue(exceptionCaught.get());
 }

 @Test
 public void testIterator() {
 ClientGroup group = rootGroup;
 CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<>();
 for (Client client : group) {
 clients.add(client);
 }
 assertEquals(4, clients.size());
 for (Client client : clients) {
 assertTrue(group.contains(client));
 }
 }

 @Test
 public void testContains() {
 ClientGroup group = rootGroup;
 assertTrue(group.contains(client1));
 assertTrue(group.contains(client2));
 assertTrue(group.contains(client3));
 assertTrue(group.contains(client4));
 }

 @Test
 public void testSize() {
 ClientGroup group = rootGroup;
 assertEquals(4, group.size());
 }

 @Test
 public void testAdd() {
 ClientGroup group = rootGroup;
 assertFalse(group.add(client1));
 assertTrue(group.contains(client1));
 assertTrue(group.size() == 5);
 }

 @Test
 public void testRemove() {
 ClientGroup group = rootGroup;
 group.add(client1);
 group.add(client2);
 group.remove(client2);
 assertFalse(group.contains(client2));
 assertEquals(1, group.size());
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup group = rootGroup;
 ClientGroup subGroup = group.createSubGroup();
 assertFalse(subGroup.contains(client1));
 assertEquals(0, subGroup.size());
 assertTrue(group.contains(subGroup));
 }

 @Test
 public void testAddClientHandler() {
 ClientGroup group = rootGroup;
 ClientHandler handler1 = new ClientHandlerAdapter();
 ClientHandler handler2 = new ClientHandlerAdapter();
 group.addClientHandler(handler1);
 group.addClientHandler(handler2);
 assertEquals(2, group.getHandlers().size());
 }
}

 class ClientHandlerAdapter implements ClientHandler {
 @Override
 public void clientAdded(Client client) {
 // do nothing
 }

 @Override
 public void clientRemoved(Client client) {
 // do nothing
 }

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
 }
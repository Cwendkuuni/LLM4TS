package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

import java.util.*;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = new ClientHandler() {
 @Override
 public void messageSent(Client client, Object message) {
 System.out.println("Message sent from handler: " + client + " " + message);
 }

 @Override
 public void messageReceived(Client client, Object message) {
 System.out.println("Message received from handler: " + client + " " + message);
 }

 @Override
 public void exceptionCaught(Client client, Throwable cause) {
 System.out.println("Exception caught in handler: " + client + " " + cause.getMessage());
 }

 @Override
 public void clientAdded(Client client) {
 System.out.println("Client added to handler: " + client);
 }

 @Override
 public void clientRemoved(Client client) {
 System.out.println("Client removed from handler: " + client);
 }
 };
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup1.add(client3);
 }

 @Test
 public void testNotifyMessageSent() {
 rootGroup.notifyMessageSent(client1, "message1");
 assertTrue(rootGroup.notifyMessageSent(client1, "message1"));
 assertTrue(subGroup1.notifyMessageSent(client3, "message1"));
 assertFalse(subGroup2.notifyMessageSent(client3, "message1"));
 }

 @Test
 public void testNotifyMessageSentToHandler() {
 rootGroup.notifyMessageSent(client1, "message1");
 assertEquals("Message sent from handler: client1 message1", handler.toString());
 }

 @Test
 public void testNotifyMessageSentToNonExistentClient() {
 rootGroup.notifyMessageSent(new Client("nonExistentClient"), "message");
 assertFalse(handler.receivedMessage);
 }

 @Test
 public void testNotifyMessageReceived() {
 rootGroup.notifyMessageReceived(client1, "message1");
 assertTrue(rootGroup.notifyMessageReceived(client1, "message1"));
 assertTrue(subGroup1.notifyMessageReceived(client3, "message1"));
 assertFalse(subGroup2.notifyMessageReceived(client3, "message1"));
 }

 @Test
 public void testNotifyMessageReceivedToHandler() {
 rootGroup.notifyMessageReceived(client1, "message1");
 assertEquals("Message received from handler: client1 message1", handler.toString());
 }

 @Test
 public void testNotifyMessageReceivedToNonExistentClient() {
 rootGroup.notifyMessageReceived(new Client("nonExistentClient"), "message");
 assertFalse(handler.receivedMessage);
 }

 @Test
 public void testNotifyExceptionCaught() {
 rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception"));
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception")));
 assertTrue(subGroup1.notifyExceptionCaught(client3, new RuntimeException("Test exception")));
 assertFalse(subGroup2.notifyExceptionCaught(client3, new RuntimeException("Test exception")));
 }

 @Test
 public void testNotifyExceptionCaughtToHandler() {
 rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception"));
 assertEquals("Exception caught in handler: client1 Test exception", handler.toString());
 }

 @Test
 public void testNotifyExceptionCaughtToNonExistentClient() {
 rootGroup.notifyExceptionCaught(new Client("nonExistentClient"), new RuntimeException("Test exception"));
 assertFalse(handler.receivedException);
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertFalse(rootGroup.contains(new Client("nonExistentClient")));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 }

 @Test
 public void testAdd() {
 assertFalse(rootGroup.add(client3));
 assertTrue(rootGroup.add(client3));
 assertEquals(4, rootGroup.size());
 }

 @Test
 public void testRemove() {
 assertTrue(rootGroup.contains(client2));
 assertTrue(rootGroup.remove(client2));
 assertFalse(rootGroup.contains(client2));
 }

 @Test
 public void testRemoveNonExistentClient() {
 assertFalse(rootGroup.remove(new Client("nonExistentClient")));
 }

 @Test
 public void testCreateSubGroup() {
 assertNotNull(subGroup1.createSubGroup());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateSubGroupWithExistingParent() {
 ClientGroup subGroup3 = new ClientGroup(subGroup1);
 assertNull(subGroup3.parentGroup());
 // This should throw an exception, but JUnit does not seem to support it directly.
 //assertThrows(IllegalStateException.class, () -> subGroup3.parentGroup());
 }
}
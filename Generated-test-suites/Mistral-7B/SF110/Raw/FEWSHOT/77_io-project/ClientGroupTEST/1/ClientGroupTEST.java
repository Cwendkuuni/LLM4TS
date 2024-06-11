package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

 ClientGroup topLevelGroup;
 ClientGroup subGroup1, subGroup2;
 Client client1, client2;
 ClientHandler handler;

 @Before
 public void setup() {
 topLevelGroup = new ClientGroup(null);
 subGroup1 = topLevelGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 handler = mock(ClientHandler.class);
 topLevelGroup.addClientHandler(handler);
 topLevelGroup.add(client1);
 subGroup1.add(client2);
 }

 @Test
 public void testNotifyMessageSent() {
 assertEquals(true, topLevelGroup.notifyMessageSent(client1, new Object()));
 assertEquals(true, subGroup1.notifyMessageSent(client2, new Object()));
 assertEquals(true, handler.messageSent(client1, new Object()));
 }

 @Test
 public void testNotifyMessageSent_ClientNotPresent() {
 assertEquals(false, topLevelGroup.notifyMessageSent(client2, new Object()));
 assertEquals(false, subGroup1.notifyMessageSent(client1, new Object()));
 assertNull(handler.messageSent(client2));
 }

 @Test
 public void testNotifyMessageReceived() {
 assertEquals(true, topLevelGroup.notifyMessageReceived(client1, new Object()));
 assertEquals(true, subGroup1.notifyMessageReceived(client2, new Object()));
 assertEquals(true, handler.messageReceived(client1, new Object()));
 }

 @Test
 public void testNotifyMessageReceived_ClientNotPresent() {
 assertEquals(false, topLevelGroup.notifyMessageReceived(client2, new Object()));
 assertEquals(false, subGroup1.notifyMessageReceived(client1, new Object()));
 assertNull(handler.messageReceived(client2));
 }

 @Test
 public void testNotifyExceptionCaught() {
 assertEquals(true, topLevelGroup.notifyExceptionCaught(client1, new RuntimeException()));
 assertEquals(true, subGroup1.notifyExceptionCaught(client2, new RuntimeException()));
 assertEquals(true, handler.exceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotPresent() {
 assertEquals(false, topLevelGroup.notifyExceptionCaught(client2, new RuntimeException()));
 assertEquals(false, subGroup1.notifyExceptionCaught(client1, new RuntimeException()));
 assertNull(handler.exceptionCaught(client2));
 }

 @Test
 public void testContains() {
 assertTrue(topLevelGroup.contains(client1));
 assertTrue(subGroup1.contains(client2));
 assertFalse(topLevelGroup.contains(client2));
 }

 @Test
 public void testSize() {
 assertEquals(2, topLevelGroup.size());
 assertEquals(1, subGroup1.size());
 }

 @Test
 public void testAdd() {
 assertFalse(topLevelGroup.add(client1));
 assertTrue(topLevelGroup.add(client2));
 assertFalse(subGroup1.add(client1));
 assertTrue(subGroup1.add(client2));
 }

 @Test
 public void testRemove() {
 assertTrue(topLevelGroup.remove(client1));
 assertFalse(topLevelGroup.remove(client2));
 assertTrue(subGroup1.remove(client2));
 assertFalse(subGroup1.remove(client1));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup newSubGroup = topLevelGroup.createSubGroup();
 assertTrue(newSubGroup.parentGroup() == topLevelGroup);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 Iterator<Client> iterator = topLevelGroup.iterator();
 iterator.remove();
 }

 @Test
 public void testAddClientHandler() {
 ClientHandler newHandler = mock(ClientHandler.class);
 topLevelGroup.addClientHandler(newHandler);
 assertEquals(newHandler, topLevelGroup.handler);
 }
}
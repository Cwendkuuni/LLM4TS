package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import ioproject.common.network.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;
 ClientHandler subGroupHandler;

 @Before
 public void setUp() {
 parentGroup = new ClientGroup(null);
 client = mock(Client.class);
 handler = mock(ClientHandler.class);
 subGroupHandler = mock(ClientHandler.class);
 subGroup = new ClientGroup(parentGroup);
 subGroup.addClientHandler(subGroupHandler);
 }

 @Test
 public void testAddClient() {
 assertTrue(parentGroup.add(client));
 assertTrue(subGroup.add(client));
 }

 @Test
 public void testRemoveClient() {
 assertTrue(parentGroup.add(client));
 assertTrue(subGroup.add(client));
 assertTrue(parentGroup.remove(client));
 assertFalse(subGroup.remove(client));
 }

 @Test
 public void testContainsClient() {
 assertFalse(parentGroup.contains(client));
 parentGroup.add(client);
 assertTrue(parentGroup.contains(client));
 assertTrue(subGroup.contains(client));
 }

 @Test
 public void testSize() {
 assertEquals(0, parentGroup.size());
 parentGroup.add(client);
 assertEquals(1, parentGroup.size());
 subGroup.add(client);
 assertEquals(2, parentGroup.size());
 }

 @Test
 public void testNotifyMessageSent() {
 when(client.getAddress()).thenReturn("127.0.0.1:1234");
 when(subGroupHandler.messageSent(client, "message")).thenReturn(true);

 parentGroup.add(client);
 subGroup.add(client);
 parentGroup.handler = handler;
 subGroup.handler = null;

 assertTrue(parentGroup.notifyMessageSent(client, "message"));
 verify(handler).messageSent(client, "message");
 }

 @Test
 public void testNotifyMessageSentInSubGroup() {
 when(client.getAddress()).thenReturn("127.0.0.1:1234");
 when(subGroupHandler.messageSent(client, "message")).thenReturn(true);

 parentGroup.add(client);
 parentGroup.addSubGroup();
 subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.handler = handler;
 subGroup.handler = subGroupHandler;

 assertTrue(parentGroup.notifyMessageSent(client, "message"));
 verify(subGroupHandler, atLeastOnce()).messageSent(client, "message");
 }

 @Test
 public void testNotifyMessageReceived() {
 when(client.getAddress()).thenReturn("127.0.0.1:1234");
 when(subGroupHandler.messageReceived(client, "message")).thenReturn(true);

 parentGroup.add(client);
 subGroup.add(client);
 parentGroup.handler = handler;
 subGroup.handler = null;

 assertTrue(parentGroup.notifyMessageReceived(client, "message"));
 verifyZeroInteractions(handler);
 }

 @Test
 public void testNotifyMessageReceivedInSubGroup() {
 when(client.getAddress()).thenReturn("127.0.0.1:1234");
 when(subGroupHandler.messageReceived(client, "message")).thenReturn(true);

 parentGroup.add(client);
 parentGroup.addSubGroup();
 subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.handler = handler;
 subGroup.handler = subGroupHandler;

 assertTrue(parentGroup.notifyMessageReceived(client, "message"));
 verifyZeroInteractions(handler);
 verify(subGroupHandler, atLeastOnce()).messageReceived(client, "message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 when(client.getAddress()).thenReturn("127.0.0.1:1234");
 when(subGroupHandler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);

 parentGroup.add(client);
 subGroup.add(client);
 parentGroup.handler = handler;
 subGroup.handler = null;

 try {
 throw new RuntimeException();
 } catch (Exception e) {
 parentGroup.notifyExceptionCaught(client, e);
 }
 verify(handler).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaughtInSubGroup() {
 when(client.getAddress()).thenReturn("127.0.0.1:1234");
 when(subGroupHandler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);

 parentGroup.add(client);
 parentGroup.addSubGroup();
 subGroup = parentGroup.createSubGroup();
 subGroup.add(client);
 parentGroup.handler = handler;
 subGroup.handler = subGroupHandler;

 try {
 throw new RuntimeException();
 } catch (Exception e) {
 parentGroup.notifyExceptionCaught(client, e);
 }
 verify(subGroupHandler, atLeastOnce()).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testCreateSubGroup() {
 assertNotNull(parentGroup.createSubGroup());
 }

 @Test
 public void testAddClientHandler() {
 parentGroup.addClientHandler(handler);
 assertEquals(handler, parentGroup.handler);
 }
}
package ioproject.server.network;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import ioproject.common.network.*;
import org.junit.Before;
import org.junit.Test;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup childGroup;
 Client client;
 ClientHandler handler;

 @Before
 public void setup() {
 parentGroup = new ClientGroup(null);
 childGroup = parentGroup.createSubGroup();
 client = mock(Client.class);
 handler = mock(ClientHandler.class);
 parentGroup.addClientHandler(handler);
 }

 @Test
 public void testAddAndRemoveClient() {
 // test adding a client
 assertTrue(parentGroup.add(client));
 assertTrue(childGroup.add(client));
 assertTrue(parentGroup.contains(client));
 assertTrue(childGroup.contains(client));

 // test removing a client
 assertTrue(parentGroup.remove(client));
 assertTrue(childGroup.remove(client));
 assertFalse(parentGroup.contains(client));
 assertFalse(childGroup.contains(client));
 }

 @Test
 public void testNotifyMessageSent() {
 // test notifyMessageSent in parent group
 doNothing().when(handler).messageSent(client, anyObject());
 parentGroup.notifyMessageSent(client, "message");
 verify(handler, times(1)).messageSent(client, "message");

 // test notifyMessageSent in child group
 childGroup.notifyMessageSent(client, "message");
 verify(handler, times(1)).messageSent(client, "message");
 }

 @Test
 public void testNotifyMessageReceived() {
 // test notifyMessageReceived in parent group
 doNothing().when(handler).messageReceived(client, anyObject());
 parentGroup.notifyMessageReceived(client, "message");
 verify(handler, times(1)).messageReceived(client, "message");

 // test notifyMessageReceived in child group
 childGroup.notifyMessageReceived(client, "message");
 verify(handler, times(1)).messageReceived(client, "message");
 }

 @Test
 public void testNotifyExceptionCaught() {
 // test notifyExceptionCaught in parent group
 doThrow(new RuntimeException("Exception")).when(handler).exceptionCaught(client, any());
 parentGroup.notifyExceptionCaught(client, new RuntimeException("Exception"));
 verify(handler, times(1)).exceptionCaught(client, any());

 // test notifyExceptionCaught in child group
 childGroup.notifyExceptionCaught(client, new RuntimeException("Exception"));
 verify(handler, times(1)).exceptionCaught(client, any());
 }

 @Test
 public void testSizeAndContains() {
 parentGroup.add(client);
 childGroup.add(client);
 assertEquals(2, parentGroup.size());
 assertEquals(2, childGroup.size());
 assertTrue(parentGroup.contains(client));
 assertTrue(childGroup.contains(client));
 }

 @Test
 public void testSubGroups() {
 ClientGroup subGroup = childGroup.createSubGroup();
 parentGroup.subGroups.add(new WeakReference<ClientGroup>(subGroup));
 assertEquals(1, parentGroup.size());
 assertEquals(1, subGroup.size());
 }
}
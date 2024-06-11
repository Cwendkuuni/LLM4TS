package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.*;
import java.util.*;

public class ClientGroupTest {

 ClientGroup parentGroup;
 ClientGroup subGroup;
 Client client;
 ClientHandler handler;

 @Before
 public void setup() {
 parentGroup = new ClientGroup(null);
 subGroup = parentGroup.createSubGroup();
 client = new Client();
 handler = mock(ClientHandler.class);
 }

 @Test
 public void testAddClient() {
 assertTrue(parentGroup.add(client));
 assertTrue(parentGroup.contains(client));
 assertTrue(subGroup.add(client));
 assertTrue(subGroup.contains(client));
 }

 @Test
 public void testRemoveClient() {
 assertTrue(parentGroup.remove(client));
 assertFalse(parentGroup.contains(client));
 assertTrue(subGroup.remove(client));
 assertFalse(subGroup.contains(client));
 }

 @Test
 public void testContainsClient() {
 assertFalse(parentGroup.contains(client));
 assertFalse(subGroup.contains(client));

 parentGroup.add(client);
 assertTrue(parentGroup.contains(client));
 assertFalse(subGroup.contains(client));

 subGroup.add(client);
 assertTrue(parentGroup.contains(client));
 assertTrue(subGroup.contains(client));
 }

 @Test
 public void testSize() {
 assertEquals(0, parentGroup.size());
 assertEquals(0, subGroup.size());

 parentGroup.add(client);
 subGroup.add(client);
 assertEquals(2, parentGroup.size());
 assertEquals(2, subGroup.size());
 }

 @Test
 public void testIterator() {
 Iterator<Client> it = parentGroup.iterator();
 assertTrue(it.hasNext());
 assertEquals(client, it.next());
 assertFalse(it.hasNext());
 }

 @Test
 public void testNotifyMessageSent() {
 when(handler.messageSent(client, anyObject())).thenReturn(true);

 assertTrue(parentGroup.notifyMessageSent(client, "test message"));
 assertTrue(subGroup.notifyMessageSent(client, "test message"));
 verify(handler).messageSent(client, "test message");
 }

 @Test
 public void testNotifyMessageSent_noHandler() {
 when(handler.messageSent(client, anyObject())).thenReturn(false);

 assertTrue(parentGroup.notifyMessageSent(client, "test message"));
 assertFalse(subGroup.notifyMessageSent(client, "test message"));
 verifyZeroInteractions(handler);
 }

 @Test
 public void testNotifyMessageReceived() {
 when(handler.messageReceived(client, anyObject())).thenReturn(true);

 assertTrue(parentGroup.notifyMessageReceived(client, "test message"));
 assertTrue(subGroup.notifyMessageReceived(client, "test message"));
 verify(handler).messageReceived(client, "test message");
 }

 @Test
 public void testNotifyMessageReceived_noHandler() {
 when(handler.messageReceived(client, anyObject())).thenReturn(false);

 assertTrue(parentGroup.notifyMessageReceived(client, "test message"));
 assertFalse(subGroup.notifyMessageReceived(client, "test message"));
 verifyZeroInteractions(handler);
 }

 @Test
 public void testNotifyExceptionCaught() {
 when(handler.exceptionCaught(client, any(Throwable.class))).thenReturn(true);

 throw new RuntimeException("test exception");
 try {
 parentGroup.notifyExceptionCaught(client, new RuntimeException("test exception"));
 } catch (Exception e) {
 // expected
 }
 assertTrue(subGroup.notifyExceptionCaught(client, new RuntimeException("test exception")));
 verify(handler).exceptionCaught(client, any(Throwable.class));
 }

 @Test
 public void testNotifyExceptionCaught_noHandler() {
 when(handler.exceptionCaught(client, any(Throwable.class))).thenReturn(false);

 throw new RuntimeException("test exception");
 try {
 parentGroup.notifyExceptionCaught(client, new RuntimeException("test exception"));
 } catch (Exception e) {
 // expected
 }
 assertFalse(subGroup.notifyExceptionCaught(client, new RuntimeException("test exception")));
 verifyZeroInteractions(handler);
 }
}
package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientGroupState;

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
 handler = new TestClientHandler();
 rootGroup.addClientHandler(handler);
 }

 @After
 public void tearDown() {
 rootGroup = null;
 subGroup1 = null;
 subGroup2 = null;
 client1 = null;
 client2 = null;
 client3 = null;
 handler = null;
 System.gc();
 }

 @Test
 public void testConstructor() {
 ClientGroup group = new ClientGroup(rootGroup);
 assertNotNull(group);
 }

 @Test
 public void testGetParentGroup() {
 assertSame(rootGroup, rootGroup.parentGroup());
 assertNull(subGroup1.parentGroup());
 assertSame(subGroup1, subGroup1.parentGroup());
 assertSame(subGroup2, subGroup2.parentGroup());
 }

 @Test
 public void testNotifyMessageSent_ValidClientAndMessage() {
 assertTrue(rootGroup.notifyMessageSent(client1, "test message"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageSent_NullClient() {
 rootGroup.notifyMessageSent(null, "test message");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageSent_NullMessage() {
 rootGroup.notifyMessageSent(client1, null);
 }

 @Test
 public void testNotifyMessageSent_ClientNotInGroup() {
 assertFalse(rootGroup.notifyMessageSent(client3, "test message"));
 }

 @Test
 public void testNotifyMessageSent_ClientInSubGroup() {
 subGroup1.add(client3);
 assertTrue(rootGroup.notifyMessageSent(client3, "test message"));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageSent_NullHandler() {
 rootGroup.handler = null;
 rootGroup.notifyMessageSent(client1, "test message");
 }

 @Test
 public void testNotifyMessageReceived_ValidClientAndMessage() {
 assertTrue(rootGroup.notifyMessageReceived(client1, "test message"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageReceived_NullClient() {
 rootGroup.notifyMessageReceived(null, "test message");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyMessageReceived_NullMessage() {
 rootGroup.notifyMessageReceived(client1, null);
 }

 @Test
 public void testNotifyMessageReceived_ClientNotInGroup() {
 assertFalse(rootGroup.notifyMessageReceived(client3, "test message"));
 }

 @Test
 public void testNotifyMessageReceived_ClientInSubGroup() {
 subGroup1.add(client3);
 assertTrue(rootGroup.notifyMessageReceived(client3, "test message"));
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyMessageReceived_NullHandler() {
 rootGroup.handler = null;
 rootGroup.notifyMessageReceived(client1, "test message");
 }

 @Test
 public void testNotifyExceptionCaught_ValidClientAndException() {
 throw new RuntimeException("Test exception");
 try {
 rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception"));
 fail("Should not reach this point");
 } catch (Throwable e) {
 assertTrue(e instanceof RuntimeException);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyExceptionCaught_NullClient() {
 rootGroup.notifyExceptionCaught(null, new RuntimeException("Test exception"));
 }

 @Test
 @SuppressWarnings("unchecked")
 public void testNotifyExceptionCaught_ExceptionTypeMismatch() {
 try {
 rootGroup.notifyExceptionCaught(client1, new InterruptedException("Test exception"));
 fail("Should not reach this point");
 } catch (UndeclaredThrowableException e) {
 assertSame(InterruptedException.class, e.getCause().getClass());
 }
 }

 @Test
 public void testNotifyExceptionCaught_ClientNotInGroup() {
 try {
 rootGroup.notifyExceptionCaught(client3, new RuntimeException("Test exception"));
 fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNotifyExceptionCaught_ClientInSubGroup() {
 subGroup1.add(client3);
 try {
 rootGroup.notifyExceptionCaught(client3, new RuntimeException("Test exception"));
 fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNotifyExceptionCaught_NullHandler() {
 rootGroup.handler = null;
 try {
 rootGroup.notifyExceptionCaught(client1, new RuntimeException("Test exception"));
 fail("Should not reach this point");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testIterator() {
 Set<Client> testSet = new HashSet<>(Arrays.asList(client1, client2, client3));
 assertEquals(testSet, rootGroup);
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testSize() {
 assertEquals(0, rootGroup.size());
 rootGroup.add(client1);
 assertEquals(1, rootGroup.size());
 }

 @Test
 public void testAdd() {
 assertFalse(rootGroup.contains(client1));
 assertTrue(rootGroup.add(client1));
 assertTrue(rootGroup.contains(client1));
 }

 @Test
 public void testAdd_ClientAlreadyInGroup() {
 assertFalse(rootGroup.add(client1));
 }

 @Test
 public void testRemove() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.remove(client1));
 assertFalse(rootGroup.contains(client1));
 }

 @Test
 public void testRemove_ClientNotInGroup() {
 assertFalse(rootGroup.remove(client3));
 }

 @Test
 public void testCreateSubGroup() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 assertFalse(rootGroup.contains(subGroup));
 assertTrue(rootGroup.subGroups.contains(new WeakReference<>(subGroup)));
 }

 @Test
 public void testCreateSubGroup_NullParentGroup() {
 try {
 new ClientGroup(null).createSubGroup();
 fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddClientHandler() {
 assertNull(rootGroup.handler);
 rootGroup.addClientHandler(handler);
 assertSame(handler, rootGroup.handler);
 }

 @Test
 public void testAddClientHandler_NullHandler() {
 rootGroup.addClientHandler(null);
 assertNull(rootGroup.handler);
 }

}

 class TestClientHandler implements ClientHandler {
 @Override
 public void messageSent(Client sender, Object message) {
 ClientGroupState.TEST_MESSAGE_SENT.set(true);
 }

 @Override
 public void messageReceived(Client sender, Object message) {
 ClientGroupState.TEST_MESSAGE_RECEIVED.set(true);
 }

 @Override
 public void exceptionCaught(Client sender, Throwable cause) {
 ClientGroupState.TEST_EXCEPTION_CAUGHT.set(true);
 }
 }
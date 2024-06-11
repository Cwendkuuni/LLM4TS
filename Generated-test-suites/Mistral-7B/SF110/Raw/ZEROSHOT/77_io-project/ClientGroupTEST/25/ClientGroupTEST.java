package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandler_MessageSent_Mock;
import ioproject.server.network.ClientGroup.ClientHandler_MessageReceived_Mock;
import ioproject.server.network.ClientGroup.ClientHandler_ExceptionCaught_Mock;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;
 private ClientHandler_MessageSent_Mock messageSentMock;
 private ClientHandler_MessageReceived_Mock messageReceivedMock;
 private ClientHandler_ExceptionCaught_Mock exceptionCaughtMock;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 subGroup = rootGroup.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = new ClientHandler() {
 // do nothing
 };
 messageSentMock = new ClientHandler_MessageSent_Mock();
 messageReceivedMock = new ClientHandler_MessageReceived_Mock();
 exceptionCaughtMock = new ClientHandler_ExceptionCaught_Mock();
 rootGroup.addClientHandler(messageSentMock);
 rootGroup.addClientHandler(messageReceivedMock);
 rootGroup.addClientHandler(exceptionCaughtMock);
 rootGroup.add(client1);
 rootGroup.add(client2);
 subGroup.add(client3);
 }

 @Test
 public void testParentGroup() {
 assertEquals(rootGroup, rootGroup.parentGroup());
 assertNull(subGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent() {
 assertFalse(messageSentMock.messageSentCallCount);
 rootGroup.notifyMessageSent(client1, "message");
 assertTrue(messageSentMock.messageSentCallCount);
 assertFalse(subGroup.notifyMessageSent(client1, "message"));
 assertFalse(messageSentMock.clientCalled.contains(client1));
 rootGroup.notifyMessageSent(client1, "message");
 assertTrue(messageSentMock.clientCalled.contains(client1));
 }

 @Test
 public void testNotifyMessageReceived() {
 assertFalse(messageReceivedMock.messageReceivedCallCount);
 rootGroup.notifyMessageReceived(client1, "message");
 assertTrue(messageReceivedMock.messageReceivedCallCount);
 assertFalse(subGroup.notifyMessageReceived(client1, "message"));
 assertFalse(messageReceivedMock.clientCalled.contains(client1));
 rootGroup.notifyMessageReceived(client1, "message");
 assertTrue(messageReceivedMock.clientCalled.contains(client1));
 }

 @Test
 public void testNotifyExceptionCaught() {
 assertFalse(exceptionCaughtMock.exceptionCaughtCallCount);
 rootGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(exceptionCaughtMock.exceptionCaughtCallCount);
 assertFalse(subGroup.notifyExceptionCaught(client1, new RuntimeException()));
 assertFalse(exceptionCaughtMock.clientCalled.contains(client1));
 rootGroup.notifyExceptionCaught(client1, new RuntimeException());
 assertTrue(exceptionCaughtMock.clientCalled.contains(client1));
 }

 @Test
 public void testContains() {
 assertTrue(rootGroup.contains(client1));
 assertTrue(rootGroup.contains(client2));
 assertTrue(subGroup.contains(client3));
 assertFalse(rootGroup.contains(client3));
 }

 @Test
 public void testSize() {
 assertEquals(3, rootGroup.size());
 }

 @Test
 public void testAdd() {
 assertFalse(rootGroup.add(client3));
 assertTrue(subGroup.add(client3));
 assertFalse(rootGroup.contains(client3));
 assertEquals(3, subGroup.size());
 assertTrue(rootGroup.add(client3));
 assertEquals(4, rootGroup.size());
 assertTrue(rootGroup.contains(client3));
 }

 @Test
 public void testRemove() {
 assertTrue(rootGroup.remove(client1));
 assertEquals(2, rootGroup.size());
 assertFalse(subGroup.remove(client1));
 assertEquals(1, subGroup.size());
 assertFalse(rootGroup.remove(client3));
 assertEquals(3, rootGroup.size());
 assertTrue(subGroup.contains(client3));
 }

 @Test
 public void testCreateSubGroup() {
 assertNotNull(subGroup);
 assertTrue(rootGroup.contains(subGroup));
 }

 @Test
 public void testAddClientHandler() {
 assertNull(rootGroup.handler);
 rootGroup.addClientHandler(messageSentMock);
 assertEquals(messageSentMock, rootGroup.handler);
 }
}
package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.hero.Hero;
import dsachat.share.userEvent.UserEvent;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint mockConnection;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = handler.clients;
 mockConnection = mock(ConnectionEndpoint.class);
 }

 @After
 public void tearDown() {
 handler = null;
 clients = null;
 mockConnection = null;
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 when(mockConnection.isConnected()).thenReturn(true);
 when(mockConnection.getClientPort()).thenReturn(1);
 handler.addConnection(mockConnection);
 assertEquals(1, clients.size());
 verify(mockConnection).send(any(UserEvent.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddConnection_NullConnection() {
 handler.addConnection(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddConnection_DisconnectedConnection() {
 when(mockConnection.isConnected()).thenReturn(false);
 handler.addConnection(mockConnection);
 assertEquals(0, clients.size());
 verifyNoMoreInteractions(mockConnection);
 }

 @Test
 public void testRemoveConnection() {
 Entry entry = new Entry();
 entry.ce = new ConnectionEndpoint() {{
 setClientPort(1);
 }};
 clients.addElement(entry);
 handler.removeConnection(entry.ce);
 assertEquals(0, clients.size());
 verify(entry.ce).send(any(UserEvent.class));
 }

 @Test
 public void testRemoveConnection_NonExistingConnection() {
 Entry entry = new Entry();
 handler.removeConnection(new ConnectionEndpoint());
 assertEquals(1, clients.size());
 verifyZeroInteractions(entry.ce);
 }

 @Test
 public void testHandleInput_StringMessage() {
 String message = "Hello, World!";
 InputEvent mockEvent = new InputEvent(mockConnection, message);
 handler.handleInput(mockEvent);
 verify(mockConnection, times(1)).send(eq(message));
 }

 @Test
 public void testHandleInput_ChallengeMessage() {
 Challenge mockChallenge = mock(Challenge.class);
 InputEvent mockEvent = new InputEvent(mockConnection, mockChallenge);
 handler.handleInput(mockEvent);
 verifyZeroInteractions(mockChallenge);
 }

 @Test
 public void testHandleInput_ChallengeReqMessage() {
 ChallReq mockChallReq = mock(ChallReq.class);
 InputEvent mockEvent = new InputEvent(mockConnection, mockChallReq);
 clients.addElement(new Entry());
 handler.handleInput(mockEvent);
 verify(mockConnection).send(mockChallReq);
 }

 @Test
 public void testHandleInput_HeroMessage() {
 Hero mockHero = new Hero("TestHero");
 InputEvent mockEvent = new InputEvent(mockConnection, mockHero);
 clients.add(new Entry());
 handler.handleInput(mockEvent);
 assertEquals("TestHero", clients.elementAt(0).name);
 }

 @Test
 public void testHandleInput_UserEventMessage_NotGM() {
 UserEvent mockUserEvent = new UserEvent(false, "TestName");
 InputEvent mockEvent = new InputEvent(mockConnection, mockUserEvent);
 handler.handleInput(mockEvent);
 // No interactions expected as it's not the GM's connection
 }

 @Test
 public void testHandleInput_UserEventMessage_GM() {
 UserEvent mockUserEvent = new UserEvent(true, "TestName");
 InputEvent mockEvent = new InputEvent(mockConnection, mockUserEvent);
 clients.add(new Entry());
 clients.add(new Entry());
 clients.get(1).name = "Gamemaster";
 handler.handleInput(mockEvent);
 assertEquals("Gamemaster", clients.elementAt(1).name);
 }

 @Test
 public void testSendAllHeroes() {
 Vector<Hero> heroes = new Vector<>(Arrays.asList(new Hero("Hero1"), new Hero("Hero2")));
 doReturn(heroes).when(handler).getClients();
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 OutputStream outputStream = outStream;
 doReturn(outputStream).when(mockConnection).sendOutputStream();
 handler.sendAllHeroes(mockConnection);
 String expected = "Hero1,Hero2";
 assertEquals(expected, outStream.toString());
 }

 @Test
 public void testIsGMConnected() {
 assertFalse(handler.isGMConnected());
 }

 @Test
 public void testIsGMConnected_GMConnected() {
 clients.add(new Entry());
 clients.add(new Entry());
 clients.get(1).name = "Gamemaster";
 assertTrue(handler.isGMConnected());
 }
}
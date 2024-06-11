package dsachat.server;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import dsachat.share.*;
import dsachat.share.hero.*;

import static org.junit.Assert.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection;
 private Entry mockEntry;
 private Hero mockHero;
 private UserEvent mockUserEvent;
 private Challenge mockChallenge;
 private ChallReq mockChallReq;

 @Before
 public void setup() {
 handler = new Handler();
 mockConnection = mock(ConnectionEndpoint.class);
 mockEntry = new Entry();
 mockEntry.ce = mockConnection;
 mockHero = mock(Hero.class);
 mockUserEvent = mock(UserEvent.class);
 mockChallenge = mock(Challenge.class);
 mockChallReq = mock(ChallReq.class);
 }

 @Test
 public void testAddConnection() {
 // Happy path
 handler.addConnection(mockConnection);
 verify(mockConnection).send(mockUserEvent);
 verify(mockConnection, times(handler.clients.size())).send(mockUserEvent);
 assertEquals(1, handler.clients.size());
 // Edge case: null argument
 handler.addConnection(null);
 assertNull(handler.clients);
 // Error scenario: cannot connect to client
 handler = new Handler();
 handler.clients = new Vector<Entry>();
 when(mockConnection.isConnected()).thenReturn(false);
 handler.addConnection(mockConnection);
 verify(mockConnection, never()).send(mockUserEvent);
 }

 @Test
 public void testRemoveConnection() {
 // Happy path
 String name = "testName";
 handler.addConnection(mockConnection);
 handler.clients.setElementAt(mockEntry, 0);
 handler.removeConnection(mockConnection);
 verify(mockConnection).send(mockUserEvent);
 verify(mockConnection, never()).send(mockUserEvent);
 assertEquals(0, handler.clients.size());
 // Edge case: null argument
 handler.removeConnection(null);
 // Error scenario: client not found
 handler = new Handler();
 handler.clients = new Vector<Entry>();
 handler.addConnection(mockConnection);
 handler.clients.setElementAt(mockEntry, 0);
 when(mockConnection.getClientPort()).thenReturn(-1);
 handler.removeConnection(mockConnection);
 verify(mockConnection, never()).send(mockUserEvent);
 }

 @Test
 public void testHandleInput() throws IOException {
 // Happy path: String input
 handler = new Handler();
 handler.clients = new Vector<Entry>();
 handler.addConnection(mockConnection);
 handler.clients.setElementAt(mockEntry, 0);
 when(mockConnection.getClientPort()).thenReturn(0);
 when(mockConnection.send(anyObject())).thenAnswer(invocation -> invocation.callRealMethod());
 handler.handleInput(new InputEvent(mockConnection, "testMessage"));
 verify(mockConnection, times(handler.clients.size())).send(eq(mockUserEvent));
 // Happy path: Challenge input
 handler = new Handler();
 handler.clients = new Vector<Entry>();
 handler.addConnection(mockConnection);
 handler.clients.setElementAt(mockEntry, 0);
 when(mockConnection.getClientPort()).thenReturn(0);
 when(mockConnection.send(anyObject())).thenAnswer(invocation -> invocation.callRealMethod());
 handler.handleInput(new InputEvent(mockConnection, mockChallenge));
 verify(mockConnection, times(2)).send(anyObject());
 }

 // ... Add other test methods for each method in the Handler class.

}
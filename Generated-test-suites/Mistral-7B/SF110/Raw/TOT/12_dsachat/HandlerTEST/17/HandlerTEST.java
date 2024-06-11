package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnectionEndpoint;
 private Entry mockEntry;
 private Hero mockHero;
 private UserEvent mockUserEvent;
 private Challenge mockChallenge;
 private ChallReq mockChallReq;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 handler = new Handler();
 mockConnectionEndpoint = mock(ConnectionEndpoint.class);
 mockEntry = new Entry();
 mockEntry.ce = mockConnectionEndpoint;
 mockEntry.name = "";
 mockHero = new Hero("TestHero");
 mockUserEvent = new UserEvent(false, "");
 mockChallenge = mock(Challenge.class);
 mockChallReq = new ChallReq("TestUser", "TestHero");
 }

 @Test
 public void testAddConnection() {
 // Test normal case
 handler.addConnection(mockConnectionEndpoint);
 assertEquals(1, handler.clients.size());
 verify(mockConnectionEndpoint, times(handler.clients.size())).send(any(UserEvent.class));

 // Test exception case
 doThrow(IOException.class).when(mockConnectionEndpoint).send(any(UserEvent.class));
 handler.addConnection(mockConnectionEndpoint);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 // Test normal case
 handler.addConnection(mockConnectionEndpoint);
 handler.removeConnection(mockConnectionEndpoint);
 assertEquals(0, handler.clients.size());
 verify(mockConnectionEndpoint, never()).send(any(UserEvent.class));

 // Test edge case - no matching connection
 handler.addConnection(mockConnectionEndpoint);
 handler.removeConnection(new ConnectionEndpoint(0));
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInput() {
 // Test String data
 handler.addConnection(mockConnectionEndpoint);
 handler.clients.add(mockEntry);
 when(mockConnectionEndpoint.isConnected()).thenReturn(true);
 handler.handleInput(new InputEvent(mockUserEvent, mockConnectionEndpoint));
 assertEquals(2, handler.clients.size());
 verify(mockConnectionEndpoint).send(any(UserEvent.class));

 // Test Challenge data
 handler.clients.clear();
 handler.addConnection(mockConnectionEndpoint);
 handler.clients.add(mockEntry);
 handler.handleInput(new InputEvent(mockChallenge, mockConnectionEndpoint));
 assertEquals(2, handler.clients.size());
 verify(mockConnectionEndpoint, times(2)).send(any(String.class));

 // Test ChallReq data
 handler.clients.clear();
 handler.addConnection(mockConnectionEndpoint);
 handler.clients.add(mockEntry);
 handler.handleInput(new InputEvent(mockChallReq, mockConnectionEndpoint));
 assertEquals(2, handler.clients.size());
 verify(mockConnectionEndpoint).send(any(ChallReq.class));

 // Test Hero data
 handler.clients.clear();
 handler.addConnection(mockConnectionEndpoint);
 handler.clients.add(mockEntry);
 handler.handleInput(new InputEvent(mockHero, mockConnectionEndpoint));
 assertEquals(2, handler.clients.size());
 verify(mockConnectionEndpoint, times(2)).send(any(String.class));
 }
}
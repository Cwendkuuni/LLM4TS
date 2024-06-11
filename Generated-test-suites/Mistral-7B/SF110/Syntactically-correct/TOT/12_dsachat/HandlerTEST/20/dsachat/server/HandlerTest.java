package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.junit.*;
import org.mockito.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnectionEndpoint;
 private Entry mockEntry;
 private Hero mockHero;
 private ChallReq mockChallReq;
 private UserEvent mockUserEvent;
 private Challenge mockChallenge;

 @Before
 public void setUp() {
 handler = new Handler();
 mockConnectionEndpoint = mock(ConnectionEndpoint.class);
 mockEntry = new Entry();
 mockEntry.ce = mockConnectionEndpoint;
 mockHero = mock(Hero.class);
 mockChallReq = mock(ChallReq.class);
 mockUserEvent = mock(UserEvent.class);
 mockChallenge = mock(Challenge.class);
 when(mockConnectionEndpoint.getClientPort()).thenReturn(12345);
 }

 @Test
 public void testAddConnection() {
 // Normal case
 handler.addConnection(mockConnectionEndpoint);
 verify(handler.clients, times(1)).addElement(mockEntry);
 // Connection already exists
 handler.addConnection(mockConnectionEndpoint);
 verify(handler.clients, times(0)).addElement(mockEntry);
 }

 @Test
 public void testRemoveConnection() {
 // Client exists
 handler.addConnection(mockConnectionEndpoint);
 handler.removeConnection(mockConnectionEndpoint);
 verify(handler.clients, times(1)).removeElementAt(anyInt());
 // Client does not exist
 handler.removeConnection(mockConnectionEndpoint);
 verify(handler.clients, times(0)).removeElementAt(anyInt());

 // Name is set when connection is added
 handler.addConnection(mockConnectionEndpoint);
 verify(mockEntry).name = "";
 // Name is sent to all clients when connection is removed
 handler.removeConnection(mockConnectionEndpoint);
 final UserEvent newCon = new UserEvent(true, handler.getName(mockConnectionEndpoint));
 handler.sendAllObj(newCon);
 verify(handler.clients, times(1)).get(anyInt()).ce.send(newCon);
 }

 @Test
 public void testHandleInput_String() {
 // Handle valid command
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String("/wisper User1 hello")));
 verify(handler).whisper(mockConnectionEndpoint, "User1 hello");
 // Handle invalid command
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String("/command not found")));
 verifyNoMoreInteractions(handler);
 }

 @Test
 public void testHandleInput_Challenge() {
 // Challenge is sent to intended recipient
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallenge));
 String to = "User1";
 verify(handler).sendToOneStr(mockConnectionEndpoint, to, mockChallenge.toString());
 // Challenge is not sent if recipient is not connected
 handler.removeConnection(mockConnectionEndpoint);
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallenge));
 verifyZeroInteractions(handler);
 }

 @Test
 public void testHandleInput_ChallReq() {
 // Challenge request is sent to intended recipient
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallReq));
 String to = "User1";
 verify(handler).sendOneSystemMsg(mockConnectionEndpoint, "Challenge request from " + handler.getName(mockConnectionEndpoint) + " for " + to);
 verify(handler).sendOneSystemMsg(mockConnectionEndpoint, "You have a new challenge request from " + to);
 // Challenge request is not sent if recipient is not connected
 handler.removeConnection(mockConnectionEndpoint);
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallReq));
 verifyZeroInteractions(handler);
 }

 @Test
 public void testHandleInput_Hero() {
 // Hero is updated
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockHero));
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String("me newhero NewHeroName")));
 String newName = "NewHeroName";
 verify(mockEntry).hero = mockHero;
 verify(mockEntry).name = newName;
 // New hero name is sent to all clients
 handler.sendAllObj(new UserEvent(false, newName));
 verify(handler.clients, times(1)).get(anyInt()).ce.send(new UserEvent(false, newName));
 }

 @Test
 public void testHandleInput_UserEvent() {
 // Gamemaster name is set when they connect
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockUserEvent));
 String gamemasterName = "Gamemaster";
 verify(mockEntry).name = gamemasterName;
 // Gamemaster name is not set if they disconnect
 handler.removeConnection(mockConnectionEndpoint);
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockUserEvent));
 verify(mockEntry).name = "";
 }

 @Test
 public void testSendHeros() {
 // Heroes are sent to client
 handler.addConnection(mockConnectionEndpoint);
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String("/getheros")));
 verify(mockConnectionEndpoint).send(any(Vector.class));
 }

 @Test
 public void testIsGmConnected() {
 // Gamemaster is not connected
 assertFalse(handler.isGmConnected());
 // Gamemaster is connected
 handler.handleInput(new InputEvent(mockConnectionEndpoint, mockUserEvent));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 // Message is sent to all clients
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String("test message")));
 verify(handler.clients, times(handler.clients.size())).get(anyInt()).ce.send("test message");
 }
}
package dsachat.server;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection;
 private Entry mockEntry;
 private Hero mockHero;
 private UserEvent mockUserEvent;
 private ChallReq mockChallReq;
 private ChatMessage mockChatMessage;
 private Challenge mockChallenge;

 @Before
 public void setUp() {
 handler = new Handler();
 mockConnection = mock(ConnectionEndpoint.class);
 mockEntry = new Entry();
 mockEntry.ce = mockConnection;
 mockEntry.name = "Client1";
 mockHero = new Hero("TestHero");
 mockUserEvent = new UserEvent(false, "Client1");
 mockChallReq = new ChallReq("Client2", "/getheros");
 mockChatMessage = new ChatMessage("TestMessage");
 mockChallenge = mock(Challenge.class);
 }

 @Test
 public void testAddConnection() throws Exception {
 handler.addConnection(mockConnection);
 verify(mockConnection).send(mockUserEvent);
 verify(mockConnection).isConnected();
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce.getClientPort() != mockConnection.getClientPort()) {
 continue;
 }
 assertEquals(mockEntry.name, handler.clients.elementAt(i).name);
 }
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithDisconnectedConnection() throws Exception {
 handler.addConnection(mockConnection);
 doThrow(IOException.class).when(mockConnection).send(mockUserEvent);
 handler.addConnection(mockConnection);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(mockConnection);
 handler.removeConnection(mockConnection);
 verify(mockConnection, atLeastOnce()).send(mockUserEvent);
 verify(mockConnection, times(1)).isConnected();
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).name.equals("Client1")) {
 continue;
 }
 assertEquals(null, handler.clients.elementAt(i).name);
 }
 }

 @Test
 public void testHandleInputWithStringMessage() throws Exception {
 String msg = "/me I am a test";
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent(mockUserEvent, mockConnection));
 verify(mockConnection, times(1)).send(msg);
 }

 @Test
 public void testHandleInputWithUnknownCommand() throws Exception {
 String msg = "/unknowcommand";
 handler.handleInput(new InputEvent(new StringData(msg), mockConnection));
 verify(mockConnection, times(1)).send("command /unknowcommand not found");
 }

 @Test
 public void testHandleInputWithRollCommand() throws Exception {
 String msg = "/roll 3d6+1";
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent(new StringData(msg), mockConnection));
 verify(mockConnection, times(1)).send("TestHero rolled 3d6+1 --> ");
 verify(mockChallenge).roll();
 }

 @Test
 public void testHandleInputWithRollCommandWithMultipleDice() throws Exception {
 String msg = "/roll 2d10 3d6";
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent(new StringData(msg), mockConnection));
 verify(mockChallenge).roll();
 verify(mockChallenge).roll();
 verify(mockChallenge).roll();
 }

 @Test
 public void testHandleInputWithChallenge() throws Exception {
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent(mockChallenge, mockConnection));
 verify(mockConnection).send(mockChatMessage.getMessage());
 verify(mockConnection).send(mockChallenge.getTo() + ": " + mockChatMessage.getMessage());
 }

 @Test
 public void testHandleInputWithChallengeRequest() throws Exception {
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection2);
 handler.handleInput(new InputEvent(mockChallReq, mockConnection));
 handler.handleInput(new InputEvent(mockHero, mockConnection2));
 verify(mockConnection2).send(mockHero);
 }

 @Test
 public void testSendHeros() throws Exception {
 Vector<Hero> heroes = new Vector<Hero>();
 heroes.add(mockHero);
 handler.addConnection(mockConnection);
 handler.sendHeros(mockConnection);
 verify(mockConnection).send(heroes);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(mockConnection);
 handler.addConnection(new Entry(new ConnectionEndpoint(), "Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedWithNoGm() {
 handler.addConnection(mockConnection);
 assertFalse(handler.isGmConnected());
 }
}
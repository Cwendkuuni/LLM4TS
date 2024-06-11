package dsachat.server;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallengeReq;
import dsachat.share.hero.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private ByteArrayOutputStream outContent;
 private ByteArrayInputStream inContent;
 private PrintStream systemOut;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = mock(ConnectionEndpoint.class);
 outContent = new ByteArrayOutputStream();
 systemOut = System.out;
 System.setOut(new PrintStream(outContent));
 }

 @Test
 public void testAddConnection() {
 // Test adding a connection with a valid ConnectionEndpoint
 ConnectionEndpoint validConnection = mock(ConnectionEndpoint.class);
 when(validConnection.isConnected()).thenReturn(true);
 when(validConnection.getClientPort()).thenReturn(1234);
 handler.addConnection(validConnection);

 // Test the size of the clients vector after adding a connection
 assertEquals(1, handler.clients.size());

 // Test the name of the new Entry in the clients vector
 assertEquals("", handler.clients.elementAt(0).name);

 // Test sending UserEvents to the new connection
 UserEvent userEvent = new UserEvent(false, "testUser");
 handler.sendAllObj(userEvent);
 verify(validConnection).send(userEvent);
 }

 @Test
 public void testRemoveConnection() {
 // Test removing a connection with a valid ConnectionEndpoint and a name
 ConnectionEndpoint connectionToRemove = mock(ConnectionEndpoint.class);
 when(connectionToRemove.getClientPort()).thenReturn(1234);
 handler.addConnection(connectionToRemove);
 handler.clients.elementAt(0).name = "testUser";
 handler.removeConnection(connectionToRemove);

 // Test the size of the clients vector after removing a connection
 assertEquals(0, handler.clients.size());

 // Test sending a UserEvent to all other connections when a connection is removed
 UserEvent userEvent = new UserEvent(true, "testUser");
 handler.sendAllObj(userEvent);
 verify(connectionToRemove, never()).send(userEvent);
 for (int i = 0; i < handler.clients.size(); i++) {
 if (handler.clients.elementAt(i) != connectionToRemove) {
 verify(handler.clients.elementAt(i).ce).send(userEvent);
 }
 }
 }

 @Test
 public void testHandleInput() throws IOException {
 // Test handling a String input with a valid command
 String input = "/wisper testUser testMessage";
 handler.handleInput(new InputEvent(connectionEndpoint, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));

 // Test sending the whisper message to the target user
 String expectedWhisperMessage = "testUser: testMessage";
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 PrintStream targetOut = new PrintStream(out);
 when(connectionEndpoint.send(eq(expectedWhisperMessage))).thenReturn(true);
 handler.sendOneSystemMsg(connectionEndpoint, expectedWhisperMessage);
 verify(connectionEndpoint, times(1)).send(eq(expectedWhisperMessage));

 // Test sending the whisper message to the target user's console output
 assertEquals(expectedWhisperMessage, out.toString());

 // Test sending the whisper message to all other users
 String senderName = handler.getName(connectionEndpoint);
 UserEvent userEvent = new UserEvent(false, senderName);
 handler.sendAllObj(userEvent);
 verify(handler.clients.elementAt(0).ce, times(1)).send(userEvent);
 verify(handler.clients.elementAt(1).ce, times(1)).send(userEvent);
 }

 @Test
 public void testHandleInputWithInvalidCommand() {
 // Test handling an input with an invalid command
 String input = "testCommand";
 handler.handleInput(new InputEvent(connectionEndpoint, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));

 // Test that no messages are sent to any connections
 verifyNoMoreInteractions(connectionEndpoint);
 }

 @Test
 public void testHandleInputWithChallenge() throws IOException {
 // Test handling a Challenge input with a valid target and a silent roll
 String input = "/challenge testUser 1d6+2";
 Challenge challenge = new Challenge(1, 6, 2, false);
 handler.handleInput(new InputEvent(connectionEndpoint, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));

 // Test sending the roll result to all connections
 String expectedRollMessage = "testUser rolled 3";
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 PrintStream targetOut = new PrintStream(out);
 when(connectionEndpoint.send(eq(expectedRollMessage))).thenReturn(true);
 handler.sendAllStr(connectionEndpoint, expectedRollMessage);
 verify(connectionEndpoint, times(1)).send(eq(expectedRollMessage));

 // Test sending the roll result to the target user's console output
 assertEquals(expectedRollMessage, out.toString());

 // Test sending the roll result to all other users
 String senderName = handler.getName(connectionEndpoint);
 UserEvent userEvent = new UserEvent(false, senderName);
 handler.sendAllObj(userEvent);
 verify(handler.clients.elementAt(0).ce, times(1)).send(userEvent);
 verify(handler.clients.elementAt(1).ce, times(1)).send(userEvent);
 }

 @Test
 public void testHandleInputWithChallengeAndTarget() throws IOException {
 // Test handling a Challenge input with a valid target and a non-silent roll
 String input = "/challenge testUser testUser2 1d6+2";
 Challenge challenge = new Challenge(1, 6, 2, false);
 handler.handleInput(new InputEvent(connectionEndpoint, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));

 // Test sending the roll result to the target user's connection
 when(connectionEndpoint.send(eq("You rolled a 5"))).thenReturn(true);
 handler.sendToOneStr(connectionEndpoint, "testUser2", "You rolled a 5");
 verify(connectionEndpoint, times(1)).send(eq("You rolled a 5"));

 // Test sending the roll result to the target user's console output
 ByteArrayOutputStream targetOut = new ByteArrayOutputStream();
 PrintStream targetConsole = new PrintStream(targetOut);
 when(connectionEndpoint.send(eq("testUser2: You rolled a 5"))).thenReturn(true);
 handler.sendOneSystemMsg(connectionEndpoint, "testUser2: You rolled a 5");
 assertEquals("testUser2: You rolled a 5", targetOut.toString());

 // Test sending the roll result to all other users
 String senderName = handler.getName(connectionEndpoint);
 UserEvent userEvent = new UserEvent(false, senderName);
 handler.sendAllObj(userEvent);
 verify(handler.clients.elementAt(0).ce, times(1)).send(userEvent);
 verify(handler.clients.elementAt(1).ce, times(1)).send(userEvent);
 }

 @Test
 public void testHandleInputWithChallengeAndInvalidTarget() {
 // Test handling a Challenge input with an invalid target
 String input = "/challenge testUser testUser3 1d6+2";
 Challenge challenge = new Challenge(1, 6, 2, false);
 handler.handleInput(new InputEvent(connectionEndpoint, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));

 // Test that no messages are sent to any connections
 verifyNoMoreInteractions(connectionEndpoint);
 }

 @Test
 public void testSendAllSystemMsg() {
 // Test sending a String message to all connections
 String message = "testMessage";
 handler.sendAllSystemMsg(message);

 // Test sending the message to all connections
 for (int i = 0; i < handler.clients.size(); i++) {
 UserEvent userEvent = new UserEvent(false, handler.getName(handler.clients.elementAt(i).ce));
 assertEquals(message, handler.clients.elementAt(i).ce.send(userEvent).toString());
 }
 }

 @Test
 public void testIsGmConnected() {
 // Test that the GM is not connected initially
 assertFalse(handler.isGmConnected());

 // Test adding a GM connection
 handler.addConnection(new ConnectionEndpoint() {});
 handler.clients.elementAt(0).name = "Gamemaster";
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testHandleInputWithInvalidInput() {
 // Test handling an input with an invalid format
 String input = "testInput";
 handler.handleInput(new InputEvent(connectionEndpoint, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));

 // Test that no messages are sent to any connections
 verifyNoMoreInteractions(connectionEndpoint);
 }

 @Test
 public void testUpdateHero() {
 // Test updating a user's hero with a valid Hero object
 Hero hero = new Hero("testHero", 10, 10, 10);
 handler.updateHero(connectionEndpoint, hero);

 // Test that the user's hero is updated in the clients vector
 assertEquals(hero, handler.clients.elementAt(0).hero);

 // Test sending a UserEvent to all other connections when a user updates their hero
 UserEvent userEvent = new UserEvent(false, handler.getName(connectionEndpoint));
 handler.sendAllObj(userEvent);
 verify(handler.clients.elementAt(1).ce, times(1)).send(userEvent);
 }

 @Test
 public void testHandleInputWithUserEvent() {
 // Test handling a UserEvent input from a user
 UserEvent userEvent = new UserEvent(false, "testUser");
 handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()), userEvent));

 // Test sending the UserEvent to all other connections
 String senderName = handler.getName(connectionEndpoint);
 UserEvent senderUserEvent = new UserEvent(false, senderName);
 handler.sendAllObj(senderUserEvent);
 verify(handler.clients.elementAt(0).ce, times(1)).send(senderUserEvent);
 verify(handler.clients.elementAt(1).ce, times(1)).send(senderUserEvent);
 }

 @Test
 public void testSendAllStr() {
 // Test sending a String message to all connections
 String message = "testMessage";
 handler.sendAllStr(connectionEndpoint, message);

 // Test sending the message to all connections
 for (int i = 0; i < handler.clients.size(); i++) {
 assertEquals(message, handler.clients.elementAt(i).ce.send(new StringEvent(message)).toString());
 }
 }

 @Test
 public void testHandleInputWithInvalidUserEvent() {
 // Test handling an input with an invalid UserEvent format
 UserEvent userEvent = new UserEvent(false, "testUser");
 handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()), userEvent));

 // Test that no messages are sent to any connections
 verifyNoMoreInteractions(connectionEndpoint);
 }

 @Test
 public void testSendHeros() {
 // Test sending a Vector of Heroes to a connection
 Vector<Hero> heros = new ArrayList<>();
 heros.add(new Hero("testHero1", 10, 10, 10));
 heros.add(new Hero("testHero2", 15, 15, 15));
 handler.sendHeros(connectionEndpoint);

 // Test sending the Vector of Heroes as a byte array to the connection and then deserializing it
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 objectOutputStream.writeObject(heros);
 objectOutputStream.close();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 handler.handleInput(new InputEvent(connectionEndpoint, byteArrayInputStream, null));

 // Test that the Vector of Heroes is received by all connections
 Vector<Hero> receivedHeros = new Vector<>();
 for (int i = 0; i < handler.clients.size(); i++) {
 receivedHeros.addAll((Vector<Hero>) handler.clients.elementAt(i).ce.receive(new VectorEvent()));
 }
 assertEquals(heros, receivedHeros);
 }

 @Test
 public void testHandleInputWithInvalidHeroVector() {
 // Test handling an input with an invalid Vector of Heroes format
 Vector<Hero> heros = new ArrayList<>();
 heros.add(new Hero("testHero1", 10, 10, 10));
 handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()), heros));

 // Test that no messages are sent to any connections
 verifyNoMoreInteractions(connectionEndpoint);
 }
}
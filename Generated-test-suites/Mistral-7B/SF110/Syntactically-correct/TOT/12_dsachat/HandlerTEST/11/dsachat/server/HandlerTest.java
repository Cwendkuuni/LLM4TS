package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection;
 private InputStream mockInput;
 private OutputStream mockOutput;
 private byte[] expectedInput;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 handler = new Handler();
 mockConnection = mock(ConnectionEndpoint.class);
 expectedInput = new byte[0];
 mockInput = new ByteArrayInputStream(expectedInput);
 mockOutput = new ByteArrayOutputStream();
 when(mockConnection.isConnected()).thenReturn(true);
 when(mockConnection.send((Object) anyObject())).thenReturn(true);
 when(mockConnection.getInputStream()).thenReturn(mockInput);
 when(mockConnection.getOutputStream()).thenReturn(mockOutput);
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 handler.addConnection(mockConnection);
 assertEquals(1, handler.getClients().size());
 }

 @Test
 public void testRemoveConnection() {
 Entry entry = new Entry();
 handler.addConnection(mockConnection);
 handler.removeConnection(mockConnection);
 assertEquals(0, handler.getClients().size());
 }

 @Test
 public void testHandleInput_StringMessage() {
 String message = "Hello all!";
 handler.handleInput(new InputEvent(mockConnection, message));
 assertEquals("*Handler: Hello all!", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInput_ChallengeObject() {
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(mockConnection, challenge));
 verify(mockConnection, times(2)).send((Object) anyObject());
 }

 @Test(expected = IOException.class)
 public void testHandleInput_IOException() {
 doThrow(new IOException()).when(mockConnection).send((Object) anyObject());
 handler.handleInput(new InputEvent(mockConnection, new String("Hello")));
 }

 @Test
 public void testSendHeros_NoConnectedClients() {
 handler.sendHeros(mockConnection);
 verify(mockConnection, never()).send((Object) anyObject());
 }

 @Test
 public void testSendHeros_ConnectedClients() {
 Vector<Hero> heroes = new Vector<Hero>(Arrays.asList(new Hero("Hero1"), new Hero("Hero2")));
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection);
 handler.sendHeros(mockConnection);
 verify(mockConnection, times(2)).send((Object) isA(Vector.class));
 }

 @Test
 public void testIsGmConnected_NoGmConnected() {
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnected_GmConnected() {
 handler.addConnection(new Entry(new ConnectionEndpoint(12345), "Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendOneSystemMsg() {
 String message = "System message!";
 handler.sendOneSystemMsg(mockConnection, message);
 verify(mockConnection).send(message);
 }

 @Test
 public void testSendToOneStr() {
 String receiverName = "Player1";
 String message = "Whisper to Player1: Hello!";
 handler.handleInput(new InputEvent(mockConnection, new String("/w " + receiverName + " Hello!")));
 String expectedMessage = "Handler: Whisper to Player1: Hello!";
 assertEquals(expectedMessage, handler.getLastSentMessage());
 }

 @Test
 public void testRollDice_SingleDice() {
 handler.handleInput(new InputEvent(mockConnection, new String("/roll 6d6 +1")));
 String expectedMessage = "Handler: 6  ";
 assertEquals(expectedMessage, handler.getLastSentMessage());
 }

 @Test
 public void testRollDice_MultipleDice() {
 handler.handleInput(new InputEvent(mockConnection, new String("/roll 3d20 +4")));
 String expectedMessage = "Handler: 12 15 18 ";
 assertEquals(expectedMessage, handler.getLastSentMessage());
 }

 @Test
 public void testRollDice_InvalidDiceFormat() {
 handler.handleInput(new InputEvent(mockConnection, new String("/roll invalidDiceFormat")));
 assertEquals("Handler: command /roll invalidDiceFormat not found", handler.getLastSentMessage());
 }

 @Test
 public void testRollDice_InvalidNumberFormat() {
 handler.handleInput(new InputEvent(mockConnection, new String("/roll 6dab +1")));
 assertEquals("Handler: command /roll 6dab not found", handler.getLastSentMessage());
 }

 @Test
 public void testUpdateHero() {
 Hero hero = new Hero("Hero1");
 handler.handleInput(new InputEvent(mockConnection, hero));
 String expectedName = "Hero1";
 assertEquals(expectedName, handler.getName(mockConnection));
 }

 @Test
 public void testUpdateHero_NullName() {
 Hero hero = new Hero("Hero1");
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent(mockConnection, hero));
 assertEquals("", handler.getName(mockConnection));
 }

 @Test
 public void testSendAllStr() {
 String message = "All: Hello!";
 handler.handleInput(new InputEvent(mockConnection, new String("/me " + message)));
 verify(mockConnection, times(handler.getClients().size())).send(message);
 }
}
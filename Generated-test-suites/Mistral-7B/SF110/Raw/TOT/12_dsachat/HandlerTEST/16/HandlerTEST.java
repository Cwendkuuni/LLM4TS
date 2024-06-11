package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection;
 private OutputStream mockOutputStream;
 private ByteArrayOutputStream expectedOutput;

 @Before
 public void setUp() {
 handler = new Handler();
 mockConnection = mock(ConnectionEndpoint.class);
 mockOutputStream = mock(OutputStream.class);
 expectedOutput = new ByteArrayOutputStream();
 when(mockConnection.send(anyString())).thenReturn(mockOutputStream);
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 handler.addConnection(mockConnection);
 assertTrue(handler.clients.contains(entry));
 }

 @Test
 public void testRemoveConnection() {
 Entry entry = new Entry();
 handler.addConnection(mockConnection);
 handler.removeConnection(mockConnection);
 assertFalse(handler.clients.contains(entry));
 }

 @Test
 public void testIsGmConnected() {
 assertFalse(handler.isGmConnected());
 handler.clients.add(new Entry("Gamemaster", mockConnection));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testHandleInputStringMessage() throws IOException {
 String message = "/hello Gamemaster";
 handler.handleInput(new InputEvent("Gamemaster", message));
 String expected = "Gamemaster: /hello Gamemaster";
 verify(mockConnection).send(expected);
 }

 @Test
 public void testHandleInputChallenge() throws IOException {
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent("Client", challenge));
 String expected = challenge.roll();
 String senderName = handler.getName(mockConnection);
 String message = senderName + ": " + expected;
 verify(mockConnection).send(message);
 }

 @Test
 public void testHandleInputChallReq() throws IOException {
 ChallReq challReq = new ChallReq("Client1", "Client2", "challenge");
 handler.handleInput(new InputEvent("Client2", challReq));
 handler.handleInput(new InputEvent("Client1", challReq));
 String expected = "Client1: challenge";
 verify(mockConnection).send(expected);
 }

 @Test
 public void testHandleInputHero() throws IOException {
 Hero hero = new Hero("TestHero");
 handler.handleInput(new InputEvent("Client", hero));
 String expectedName = hero.getName();
 String senderName = handler.getName(mockConnection);
 String message = "*" + senderName + ": " + "TestHero";
 verify(mockConnection).send(message);
 }

 @Test
 public void testHandleInputUserEvent() throws IOException {
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 handler.handleInput(new InputEvent("Client", userEvent));
 String expectedName = userEvent.getName();
 String expectedMessage = "Client connected";
 String message = expectedName + ": " + expectedMessage;
 verify(mockConnection).send(message);
 }

 @Test
 public void testSendAllSystemMsg() throws IOException {
 String message = "System message";
 handler.sendAllSystemMsg(message);
 for (int i = 0; i < handler.clients.size(); ++i) {
 String senderName = handler.getName(handler.clients.elementAt(i).ce);
 String expected = senderName + ": " + message;
 assertEquals(expected, handler.clients.elementAt(i).ce.getSendQueue().poll().toString());
 }
 }

 @Test
 public void testRollDice() {
 String command = "1d6+1";
 handler.rollDice(mockConnection, command);
 String expected = handler.getName(mockConnection) + " rolled 1d6+1 --> 2  ";
 assertEquals(expected, handler.getLastSentMessage());
 }

 @Test
 public void testWhisper() {
 String command = "whisper Client1 Test";
 handler.whisper(mockConnection, command);
 String expectedTo = "Client1";
 String expectedMessage = handler.getName(mockConnection) + " whispers: Test";
 verify(mockConnection).send(expectedMessage);
 }

 @Test
 public void testSendAllStr() throws IOException {
 String message = "test message";
 handler.sendAllStr(mockConnection, message);
 for (int i = 0; i < handler.clients.size(); ++i) {
 String senderName = handler.getName(handler.clients.elementAt(i).ce);
 String expected = senderName + ": " + message;
 assertEquals(expected, handler.clients.elementAt(i).ce.getSendQueue().poll().toString());
 }
 }
}
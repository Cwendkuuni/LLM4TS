package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Hero;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection1, connection2;
 private InputStream inputStream;

 @Before
 public void setUp() {
 handler = new Handler();
 connection1 = new ConnectionEndpoint(1, "Client 1");
 connection2 = new ConnectionEndpoint(2, "Client 2");
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 }

 @Test
 public void testAddConnection() {
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 handler.removeConnection(connection1);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputString() throws IOException {
 inputStream = new ByteArrayInputStream("/w Client 2 me".getBytes());
 connection2.setInputStream(inputStream);
 handler.handleInput(new InputEvent(connection2, inputStream, null));
 String expected = "Client 1 whispers: me";
 String actual = handler.clients.get(1).ce.getOutputBuffer().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleInputChallenge() throws IOException {
 Challenge challenge = new Challenge();
 inputStream = new ByteArrayInputStream(challenge.getData());
 connection1.setInputStream(inputStream);
 handler.handleInput(new InputEvent(connection1, inputStream, null));
 String expected = "Client 1 rolled 1d20 --> 12";
 String actual = handler.sendToOne.get(connection1).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testHandleInputIOException() throws IOException {
 inputStream = new ByteArrayInputStream("/getheros".getBytes());
 connection2.setInputStream(inputStream);
 handler.handleInput(new InputEvent(connection2, inputStream, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleInputNullInput() {
 handler.handleInput(new InputEvent(connection1, null, null));
 }

 @Test
 public void testSendAllHeros() {
 Vector<Hero> heroes = new Vector<Hero>();
 heroes.add(new Hero("Hero 1"));
 handler.sendHeros(connection1);
 String expected = "[Hero 1]";
 String actual = new String(connection1.getOutputBuffer().getBytes(), 0, (int) connection1.getOutputBuffer().size());
 assertEquals(expected, actual);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint(3, "Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedFalse() {
 assertFalse(handler.isGmConnected());
 }

 @Test(expected = IOException.class)
 public void testSendAllSystemMsgIOException() {
 String message = "Test message";
 handler.sendAllSystemMsg(message);
 }

 @Test
 public void testWhisper() {
 String sender = "Client 1";
 String recipient = "Client 2";
 String message = "Test message";
 handler.whisper(connection1, recipient + " " + message);
 String expectedSender = sender + " whispers: ";
 String expectedRecipient = recipient + ": ";
 String expectedMessage = message;
 String senderOutput = handler.sendToOne.get(connection1).toString();
 String recipientOutput = handler.sendToOne.get(connection2).toString();
 assertEquals(expectedSender, senderOutput.substring(0, expectedSender.length() + sender.length()));
 assertEquals(expectedRecipient, recipientOutput.substring(0, expectedRecipient.length() + recipient.length()));
 assertEquals(expectedMessage, recipientOutput.substring(expectedRecipient.length() + recipient.length() + 1));
 }

 @Test
 public void testSendToOneStr() {
 ConnectionEndpoint client = new ConnectionEndpoint(3, "Client 3");
 handler.addConnection(client);
 handler.sendToOneStr(connection1, "Client 3", "Test message");
 String expected = "Client 1: Test message";
 String actual = new String(connection1.getOutputBuffer().getBytes(), 0, (int) connection1.getOutputBuffer().size());
 assertEquals(expected, actual);
 }

 @Test
 public void testRollDice() {
 ConnectionEndpoint client = new ConnectionEndpoint(3, "Client 3");
 handler.addConnection(client);
 handler.rollDice(connection1, "1d6+2");
 String expected = "Client 1 rolled 1d6 --> 4";
 String actual = handler.sendToOne.get(connection1).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testRollDiceMultipleDice() {
 ConnectionEndpoint client = new ConnectionEndpoint(3, "Client 3");
 handler.addConnection(client);
 handler.rollDice(connection1, "2d6+3");
 String expected = "Client 1 rolled 1d6 --> 2, 1d6 --> 5";
 String actual = handler.sendToOne.get(connection1).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testRollDiceEmptyInput() {
 ConnectionEndpoint client = new ConnectionEndpoint(3, "Client 3");
 handler.addConnection(client);
 handler.rollDice(connection1, "");
 String expected = "Client 1 rolled an empty command";
 String actual = handler.sendAllSystemMsg.get(0).toString();
 assertEquals(expected, actual);
 }
}
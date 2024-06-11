package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.HandlerTest;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Hero;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint connection;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = new Vector<Entry>();
 connection = new ConnectionEndpoint(1234);
 outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 }

 @After
 public void tearDown() {
 System.setOut(System.out);
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 entry.ce = connection;
 handler.addConnection(connection);
 clients.add(entry);
 assertTrue(clients.size() == 1);
 }

 @Test(expected = IOException.class)
 public void testAddConnectionIOException() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 fail("Should have thrown an IOException");
 }

 @Test
 public void testRemoveConnection() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.removeConnection(connection);
 assertTrue(clients.size() == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveConnectionNullName() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 String name = null;
 handler.removeConnection(connection);
 handler.sendAllObj(new UserEvent(true, name));
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testRollDice() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.handleInput(new InputEvent(connection, new String("/roll 2d6+3")));
 String expectedOutput = "dsachat.server.Handler: rolled 2d6+3 --> 7 7 10";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testRollDiceInvalidCommand() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.handleInput(new InputEvent(connection, new String("/roll invalid")));
 String expectedOutput = "dsachat.server.Handler: command /roll invalid not found";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testRollDiceNoArgument() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.handleInput(new InputEvent(connection, new String("/roll")));
 String expectedOutput = "dsachat.server.Handler: command /roll not found";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testWhisper() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 ConnectionEndpoint receiver = new ConnectionEndpoint(1234);
 handler.addConnection(receiver);
 handler.handleInput(new InputEvent(sender, new String("/w Gamemaster this is a test")));
 handler.handleInput(new InputEvent(receiver, new String("/w Gamemaster this is a test")));
 String expectedOutputSender = "dsachat.server.Handler: Gamemaster whispers: this is a test";
 String expectedOutputReceiver = "dsachat.server.Handler: You whisper to Gamemaster: this is a test";
 assertEquals(expectedOutputSender, outputStream.toString());
 assertEquals(expectedOutputReceiver, outputStream.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhisperInvalidCommand() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.handleInput(new InputEvent(sender, new String("/w invalid this is a test")));
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWhisperInvalidTo() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.handleInput(new InputEvent(sender, new String("/w invalid this is a test")));
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testHandleInputString() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.handleInput(new InputEvent(connection, new String("Hello World")));
 String expectedOutput = "dsachat.server.Handler: *dsachat.server.Handler: Hello World";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testHandleInputChallenge() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 ConnectionEndpoint receiver = new ConnectionEndpoint(1234);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(sender, challenge));
 String expectedOutputSender = "dsachat.server.Handler: *dsachat.server.Handler rolled 1 1";
 String expectedOutputReceiver = "dsachat.server.Handler: You rolled 1 1";
 assertEquals(expectedOutputSender, outputStream.toString());
 assertEquals(expectedOutputReceiver, outputStream.toString());
 }

 @Test(expected = IOException.class)
 public void testHandleInputChallengeIOException() {
 ConnectionEndpoint sender = new ConnectionEndpoint(1234);
 ConnectionEndpoint receiver = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 Challenge challenge = new Challenge();
 try {
 handler.handleInput(new InputEvent(sender, challenge));
 fail("Should have thrown an IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testHandleInputChallReq() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 ConnectionEndpoint receiver = new ConnectionEndpoint(1234);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 ChallReq challReq = new ChallReq();
 challReq.setTo("Gamemaster");
 handler.handleInput(new InputEvent(sender, challReq));
 String expectedOutputSender = "dsachat.server.Handler: Gamemaster connected";
 String expectedOutputReceiver = "dsachat.server.Handler: Gamemaster connected";
 assertEquals(expectedOutputSender, outputStream.toString());
 assertEquals(expectedOutputReceiver, outputStream.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleInputChallReqInvalidTo() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 ConnectionEndpoint receiver = new ConnectionEndpoint(1234);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 ChallReq challReq = new ChallReq();
 challReq.setTo("invalid");
 handler.handleInput(new InputEvent(sender, challReq));
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testHandleInputHero() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Hero hero = new Hero();
 handler.addConnection(connection);
 handler.handleInput(new InputEvent(connection, hero));
 String expectedOutput = "dsachat.server.Handler: Gamemaster connected";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testHandleInputUserEventGmConnected() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 handler.addConnection(connection);
 handler.handleInput(new InputEvent(connection, userEvent));
 String expectedOutput = "dsachat.server.Handler: Gamemaster connected";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateHeroNullName() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Hero hero = new Hero();
 handler.updateHero(connection, null);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testUpdateHero() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Hero hero = new Hero();
 handler.updateHero(connection, hero);
 String expectedOutput = "dsachat.server.Handler: Gamemaster updated his hero";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testSendAllStr() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.sendAllStr(connection, "Hello World");
 String expectedOutput = "dsachat.server.Handler: *dsachat.server.Handler: Hello World";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testSendToOneStr() {
 ConnectionEndpoint sender = new ConnectionEndpoint(5678);
 ConnectionEndpoint receiver = new ConnectionEndpoint(1234);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 handler.sendToOneStr(receiver, "Gamemaster", "Hello World");
 String expectedOutputReceiver = "dsachat.server.Handler: You whisper to Gamemaster: Hello World";
 assertEquals(expectedOutputReceiver, outputStream.toString());
 }

 @Test
 public void testSendAllObj() {
 Vector<Hero> heroes = new Vector<Hero>();
 for (int i = 0; i < 5; ++i) {
 heroes.addElement(new Hero());
 }
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.sendAllObj(heroes);
 for (int i = 0; i < 5; ++i) {
 String expectedOutput = "dsachat.server.Handler: *" + heroes.elementAt(i).getName();
 assertEquals(expectedOutput, outputStream.toString());
 }
 }

 @Test
 public void testSendAllObjNull() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.sendAllObj(null);
 fail("Should have thrown a NullPointerException");
 }

 @Test
 public void testIsGmConnected() {
 ConnectionEndpoint gmConnection = new ConnectionEndpoint(1234);
 ConnectionEndpoint userConnection = new ConnectionEndpoint(5678);
 handler.addConnection(gmConnection);
 handler.addConnection(userConnection);
 handler.sendAllObj(new UserEvent(true, "Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedFalse() {
 ConnectionEndpoint gmConnection = new ConnectionEndpoint(1234);
 ConnectionEndpoint userConnection = new ConnectionEndpoint(5678);
 handler.addConnection(gmConnection);
 handler.addConnection(userConnection);
 assertTrue(handler.isGmConnected() == false);
 }

 @Test
 public void testSendAllSystemMsg() {
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 handler.addConnection(connection);
 handler.sendAllSystemMsg("Hello World");
 String expectedOutput = "dsachat.server.Handler: *dsachat.server.Handler: Hello World";
 assertEquals(expectedOutput, outputStream.toString());
 }
}
package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint1;
 private ConnectionEndpoint connectionEndpoint2;
 private InputStream inputStream;
 private OutputStream outputStream;
 private ByteArrayOutputStream outContent;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint1 = new ConnectionEndpoint(1234);
 connectionEndpoint2 = new ConnectionEndpoint(5678);
 handler.addConnection(connectionEndpoint1);
 handler.addConnection(connectionEndpoint2);
 }

 @Test
 public void testAddConnection() {
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 handler.removeConnection(connectionEndpoint1);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testRollDice() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 handler.sendAllObj(new UserEvent(false, "Test"));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), con));
 handler.rollDice(con, "/roll 2d6+3");
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), con));
 String expected = "Test rolled 2d6+3 --> 7 7";
 String actual = handler.getOutput();
 assertEquals(expected, actual);
 }

 @Test
 public void testWhisper() throws IOException {
 ConnectionEndpoint sender = new ConnectionEndpoint(1234);
 ConnectionEndpoint receiver = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("whisper test user1 test".getBytes()), sender));
 handler.handleInput(new InputEvent(new ByteArrayInputStream("whisper test user2 test".getBytes()), receiver));
 String expectedSender = "Test[Test]: whispers: test to user2: test";
 String expectedReceiver = "Test[Test]: You whisper to user2: test";
 String senderOutput = handler.getOutputFromConnection(sender);
 String receiverOutput = handler.getOutputFromConnection(receiver);
 assertEquals(expectedSender, senderOutput);
 assertEquals(expectedReceiver, receiverOutput);
 }

 @Test
 public void testSendHeros() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 Hero hero1 = new Hero("hero1", 10, 20);
 handler.updateHero(con, hero1);
 handler.sendAllObj(new UserEvent(false, "Test"));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), con));
 Vector<Hero> expected = new Vector<>();
 expected.add(hero1);
 Vector<Hero> actual = (Vector<Hero>) handler.getOutput();
 assertEquals(expected, actual);
 }

 @Test
 public void testIsGmConnected() {
 ConnectionEndpoint gm = new ConnectionEndpoint(5678);
 handler.addConnection(gm);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("user1".getBytes()), connectionEndpoint1));
 handler.handleInput(new InputEvent(new ByteArrayInputStream("Gamemaster".getBytes()), gm));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMsg() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("systemMessage test".getBytes()), connectionEndpoint1));
 String expected = "Test: systemMessage test";
 String actual = handler.getOutputFromConnection(con);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleInput() {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("userMessage test".getBytes()), con));
 String expected = "Test: userMessage test";
 String actual = handler.getOutputFromConnection(con);
 assertEquals(expected, actual);
 }

 @Test
 public void testChallenge() throws IOException {
 ConnectionEndpoint sender = new ConnectionEndpoint(1234);
 ConnectionEndpoint receiver = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("challenge user2 1d6".getBytes()), sender));
 String expectedSender = "Test[Test]: challenges user2 to roll a d6";
 String expectedReceiver = "Test[Test]: You have been challenged to roll a d6 by user2";
 String senderOutput = handler.getOutputFromConnection(sender);
 String receiverOutput = handler.getOutputFromConnection(receiver);
 assertEquals(expectedSender, senderOutput);
 assertEquals(expectedReceiver, receiverOutput);
 }

 @Test
 public void testChallReq() throws IOException {
 ConnectionEndpoint sender = new ConnectionEndpoint(1234);
 ConnectionEndpoint receiver = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.addConnection(receiver);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("challenge user2 1d6".getBytes()), sender));
 handler.handleInput(new InputEvent(new ByteArrayInputStream("accept".getBytes()), receiver));
 String expectedSender = "Test[Test]: challenges user2 to roll a d6";
 String expectedReceiver = "Test[Test]: You have accepted the challenge from user2 to roll a d6";
 String senderOutput = handler.getOutputFromConnection(sender);
 String receiverOutput = handler.getOutputFromConnection(receiver);
 assertEquals(expectedSender, senderOutput);
 assertEquals(expectedReceiver, receiverOutput);
 }

 @Test
 public void testUpdateHero() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 Hero hero1 = new Hero("hero1", 10, 20);
 handler.updateHero(con, hero1);
 String expected = "Test[hero1]";
 String actual = handler.getName(con);
 assertEquals(expected, actual);
 }

 @Test
 public void testSendAllStr() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("userMessage test".getBytes()), con));
 String expected = "Test[Test]: userMessage test";
 String actual = handler.getOutput();
 assertEquals(expected, actual);
 }

 @Test
 public void testSendAllObj() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 handler.sendAllObj(new UserEvent(false, "Test"));
 String expected = "Test[Test]:" + System.lineSeparator();
 String actual = handler.getOutput();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetName() throws IOException {
 ConnectionEndpoint con = new ConnectionEndpoint(1234);
 handler.addConnection(con);
 String expected = "Test";
 String actual = handler.getName(con);
 assertEquals(expected, actual);
 }

 private String getOutput() {
 outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 return outContent.toString();
 }

 private String getOutputFromConnection(ConnectionEndpoint con) {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(handler.getOutput().getBytes());
 inputStream.reset();
 InputEvent event = new InputEvent(inputStream, con);
 handler.handleInput(event);
 return outContent.toString();
 }
}
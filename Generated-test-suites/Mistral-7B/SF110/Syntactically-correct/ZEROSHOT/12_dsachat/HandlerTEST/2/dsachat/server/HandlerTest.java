package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.hero.UserEvent;
import dsachat.share.share;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint connection;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = handler.getClients();
 connection = new ConnectionEndpoint();
 }

 @After
 public void tearDown() {
 handler = null;
 clients = null;
 connection = null;
 }

 @Test
 public void testAddConnection() throws Exception {
 handler.addConnection(connection);
 assertEquals(1, clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnectionFail() {
 handler.addConnection(connection);
 handler.addConnection(connection);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connection);
 handler.removeConnection(connection);
 assertEquals(0, clients.size());
 }

 @Test
 public void testHandleInputString() {
 String message = "/dice 2d6";
 handler.handleInput(new InputEvent(new ByteArrayOutputStream(), connection, message));
 // TODO: Implement verification of the result or output.
 }

 @Test
 public void testHandleInputChallenge() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 ConnectionEndpoint receiver = new ConnectionEndpoint() {
 @Override
 public void send(Object message) throws IOException {
 this.output = outputStream;
 }
 };
 handler.addConnection(receiver);
 handler.handleInput(new InputEvent(outputStream, connection, new Challenge()));
 String received = outputStream.toString();
 assertEquals("/challenge rolls a d6: " + Challenge.dice(6), received);
 }

 @Test
 public void testHandleInputChallReq() {
 String recipientName = "testRecipient";
 handler.handleInput(new InputEvent(share.getInstance(), connection, new ChallReq(recipientName)));
 // TODO: Implement verification of the result or output.
 }

 @Test
 public void testHandleInputHero() {
 Hero hero = new Hero("testName", 10, 10);
 handler.handleInput(new InputEvent(share.getInstance(), connection, hero));
 String name = handler.getName(connection);
 assertEquals("testName", name);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint());
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 String message = "Test message";
 handler.sendAllSystemMsg(message);
 for (Entry entry : clients) {
 assertEquals(message, entry.ce.getData().toString());
 }
 }

 @Test
 public void testWhisper() {
 String senderName = "testSender";
 String recipientName = "testRecipient";
 String message = "Test whisper";
 handler.handleInput(new InputEvent(share.getInstance(), connection, new Whisper(senderName, recipientName, message)));
 String received = handler.getName(connection) + " whispers: Test whisper";
 for (Entry entry : clients) {
 if (entry.name.equals(recipientName)) {
 assertEquals(received, entry.ce.getData().toString());
 }
 }
 }

 @Test
 public void testRollDice() {
 String senderName = "testSender";
 String message = "/dice 1d20+5";
 handler.handleInput(new InputEvent(share.getInstance(), connection, message));
 String received = handler.getName(connection) + " rolled 1d20+5 --> ";
 for (int i = 0; i < 5; i++) {
 int diceRoll = Challenge.dice(20);
 received += diceRoll + "  ";
 }
 received += "15";
 for (Entry entry : clients) {
 assertEquals(received, entry.ce.getData().toString());
 }
 }

 @Test
 public void testSendToOneStr() {
 String senderName = "testSender";
 String recipientName = "testRecipient";
 String message = "Test message";
 handler.handleInput(new InputEvent(share.getInstance(), connection, new Whisper(senderName, recipientName, message)));
 String received = handler.getName(connection) + ": " + message;
 for (Entry entry : clients) {
 if (entry.name.equals(recipientName)) {
 assertEquals(received, entry.ce.getData().toString());
 }
 }
 }

 @Test
 public void testUpdateHero() {
 String senderName = "testSender";
 Hero hero = new Hero("testName", 10, 10);
 handler.handleInput(new InputEvent(share.getInstance(), connection, hero));
 assertEquals("testName", handler.getName(connection));
 }
}
package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection1;
 private ConnectionEndpoint connection2;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = new Vector<Entry>();
 connection1 = new ConnectionEndpoint(1, new PrintWriter(System.out));
 connection2 = new ConnectionEndpoint(2, new PrintWriter(System.out));
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 }

 @Test
 public void testAddConnection() {
 EntityEntry expectedEntry1 = new EntityEntry();
 EntityEntry expectedEntry2 = new EntityEntry();

 assertEquals(0, handler.clients.size());
 handler.addConnection(connection1);
 handler.addConnection(connection2);

 EntityEntry actualEntry1 = handler.clients.elementAt(0);
 EntityEntry actualEntry2 = handler.clients.elementAt(1);

 assertEquals(2, handler.clients.size());
 assertEquals(expectedEntry1, actualEntry1);
 assertEquals(expectedEntry2, actualEntry2);
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithIOException() throws Exception {
 ConnectionEndpoint connection3 = new ConnectionEndpoint(3, new PrintWriter(System.out)) {
 @Override
 public void send(Object obj) throws IOException {
 throw new IOException();
 }
 };
 handler.addConnection(connection3);
}

 @Test
 public void testRemoveConnection() {
 EntityEntry expectedEntry1 = new EntityEntry();
 EntityEntry expectedEntry2 = new EntityEntry();

 handler.addConnection(connection1);
 handler.addConnection(connection2);

 assertEquals(2, handler.clients.size());
 handler.removeConnection(connection1);

 EntityEntry actualEntry1 = null;
 EntityEntry actualEntry2 = handler.clients.elementAt(0);

 assertEquals(1, handler.clients.size());
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce == connection2) {
 actualEntry1 = handler.clients.elementAt(i - 1);
 break;
 }
 }

 assertEquals(expectedEntry1.name, actualEntry1.name);
 assertNull(actualEntry1.ce);
 assertNotNull(actualEntry2.ce);
 }

 @Test
 public void testSendAllObj() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(bos);
 UserEvent userEvent = new UserEvent(false, "TestUser");
 out.writeObject(userEvent);
 out.close();
 byte[] data = bos.toByteArray();

 handler.sendAllObj(userEvent);
 verify(handler, times(2)).sendAllStr(userEvent.toString());

 handler.sendAllObj(data);
 verify(handler, times(2)).sendAllStr(new String(data));
 }

 @Test
 public void testSendHeros() {
 Vector<Hero> heroes = new Vector<Hero>();
 heroes.add(new Hero("TestHero1"));
 heroes.add(new Hero("TestHero2"));

 handler.sendHeros(connection1);
 verify(connection1, times(1)).send(heroes);
 }

 @Test
 public void testSendAllSystemMsg() {
 String expectedMsg = "Test message";
 handler.sendAllSystemMsg(expectedMsg);
 verify(handler, times(2)).sendAllStr(expectedMsg);
 }

 @Test
 public void testWhisper() throws IOException {
 String sender = "TestSender";
 String receiver = "TestReceiver";
 String msg = "Test message";

 handler.whisper(connection1, receiver, msg);
 verify(connection2, times(1)).sendOneSystemMsg(eq(receiver), eq(sender + " whispers: " + msg));
 verify(connection1, times(1)).sendOneSystemMsg(eq(sender), eq(sender + " whispers to " + receiver + ": " + msg));
 }

 @Test
 public void testRollDice() throws IOException {
 String sender = "TestSender";
 String expectedMsg = "";
 String diceCommand = "1d6+1";

 handler.rollDice(connection1, diceCommand);
 verify(connection1, times(1)).sendAllSystemMsg(eq(sender + " rolled 1d6+1 --> "));

 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(bos);
 out.writeObject(new Challenge(6));
 out.close();
 byte[] data = bos.toByteArray();
 handler.rollDice(connection1, diceCommand);
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 expectedMsg += "1 ";

 handler.rollDice(connection1, diceCommand);
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 expectedMsg += "1 ";

 handler.rollDice(connection1, diceCommand);
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 expectedMsg += "1 " + Challenge.DICE_SUFFIX;

 handler.rollDice(connection1, diceCommand + "+2");
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 expectedMsg += "3 " + Challenge.DICE_SUFFIX;

 handler.rollDice("2d6+1");
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 handler.handleInput(new InputEvent(connection1, new ByteArrayInputStream(data)));
 expectedMsg += "3, 6 " + Challenge.DICE_SUFFIX;

 handler.sendAllSystemMsg(expectedMsg);
 verify(handler, times(1)).sendAllSystemMsg(eq(expectedMsg));
 }

 @Test
 public void testHandleInputWithString() throws IOException {
 String sender = "TestSender";
 String msg = "Test message";

 handler.handleInput(new InputEvent(connection1, new String(msg)));
 verify(connection1, times(1)).sendAllStr(sender + ": " + msg);
 }

 @Test
 public void testHandleInputWithChallenge() throws IOException {
 String sender = "TestSender";
 String receiver = "TestReceiver";
 String expectedMsg = "TestSender rolled 4d6+2 --> 2, 5, 3, 1 " + Challenge.DICE_SUFFIX;

 ConnectionEndpoint receiverConnection = new ConnectionEndpoint(3, new PrintWriter(System.out));
 handler.addConnection(receiverConnection);

 handler.handleInput(new InputEvent(connection1, new Challenge(4, receiver)));
 handler.handleInput(new InputEvent(receiverConnection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(receiverConnection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(receiverConnection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(receiverConnection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(receiverConnection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(receiverConnection, new ByteArrayInputStream(new byte[0])));

 handler.sendAllStr(expectedMsg);
 verify(handler, times(1)).sendAllStr(expectedMsg);
 }

 @Test
 public void testHandleInputWithChallReq() throws IOException {
 String sender = "TestSender";
 String receiver = "TestReceiver";

 handler.handleInput(new InputEvent(connection1, new ChallReq(sender, receiver, new Challenge(6))));
 verify(receiverConnection, times(1)).send(new ChallReq(sender, receiver, new Challenge(6)));
 }

 @Test
 public void testHandleInputWithHero() throws IOException {
 String sender = "TestSender";
 String name = "TestUser";
 Hero hero = new Hero(name);

 handler.handleInput(new InputEvent(connection1, new Hero(name)));
 assertEquals(name, handler.getName(connection1));
 handler.updateHero(connection1, hero);
 assertEquals(name, handler.getName(connection1));
 }

 @Test
 public void testHandleInputWithUserEvent() throws IOException {
 String sender = "TestSender";
 String name = "TestUser";
 String msg = "Test message";

 handler.handleInput(new InputEvent(connection1, new UserEvent(false, name, msg)));
 verify(connection1, times(1)).sendAllStr(sender + ": " + msg);
 }

 @Test
 public void testIsGmConnected() {
 assertFalse(handler.isGmConnected());
 handler.addConnection(new EntityEntry(new ConnectionEndpoint(3, new PrintWriter(System.out)), "Gamemaster"));
 assertTrue(handler.isGmConnected());
 }
}

class EntityEntry {
 ConnectionEndpoint ce;
 String name;

 EntityEntry() {
 ce = null;
 name = "";
 }
}
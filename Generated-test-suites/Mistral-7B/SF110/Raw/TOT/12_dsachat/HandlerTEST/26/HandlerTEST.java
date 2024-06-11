package dsachat.server;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 private ConnectionEndpoint connectionEndpoint;
 private Hero hero;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint(1234);
 hero = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionEndpoint);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testAddConnectionDuplicate() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testRemoveConnectionNonExistent() {
 handler.removeConnection(new ConnectionEndpoint(1235));
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testHandleInputString() {
 String msg = "/w TestUser Hi";
 handler.handleInput(new InputEvent(new StringEvent("TestUser", msg), connectionEndpoint));
 assertEquals("TestUser whispers to Hi: Hi", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInputChallenge() {
 Hero expectedHero = new Hero("TestHero2");
 Challenge challenge = new Challenge(2, expectedHero);
 handler.handleInput(new InputEvent(new ChallengeEvent(connectionEndpoint, challenge), connectionEndpoint));
 assertEquals(expectedHero, handler.getConnectedHero(connectionEndpoint));
 }

 @Test
 public void testHandleInputInvalidCommand() {
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/invalid"), connectionEndpoint));
 assertEquals("command /invalid not found", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInputInvalidRoll() {
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/roll 2d20+10"), connectionEndpoint));
 assertEquals("Invalid roll command", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInputValidRoll() {
 String expectedMessage = "TestUser rolled 2d20+10 --> 13 15";
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/roll 2d20+10"), connectionEndpoint));
 assertEquals(expectedMessage, handler.getLastSentMessage());
 }

 @Test
 public void testIsGmConnected() {
 ConnectionEndpoint gmConnectionEndpoint = new ConnectionEndpoint(42);
 handler.addConnection(connectionEndpoint);
 handler.addConnection(gmConnectionEndpoint);
 handler.sendAllObj(new UserEvent("Gamemaster", ""));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMsg() {
 String expectedMessage = "TestUser: Test system message";
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/system Test system message"), connectionEndpoint));
 assertEquals(expectedMessage, handler.getLastSentMessage());
 }

 @Test
 public void testSendAllSystemMsg() {
 String expectedMessage = "TestUser: Test system message";
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/system Test system message"), connectionEndpoint));
 handler.handleInput(new InputEvent(new StringEvent("TestUser2", "/system Test system message"), connectionEndpoint));
 assertEquals(2, handler.getNumSystemMessagesSent());
 }

 @Test
 public void testWhisper() {
 String expectedMessage1 = "TestUser whispers to TestUser2: Test message";
 String expectedMessage2 = "TestUser2: TestUser: Test message";
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/w TestUser2 Test message"), connectionEndpoint));
 assertEquals(expectedMessage1, handler.getLastSentMessage());
 assertEquals(expectedMessage2, handler.getLastReceivedMessage(connectionEndpoint));
 }

 @Test
 public void testRollDice() {
 String expectedMessage = "TestUser rolled 1d6 --> 3";
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/roll 1d6"), connectionEndpoint));
 assertEquals(expectedMessage, handler.getLastSentMessage());
 }

 @Test
 public void testSendHeros() {
 Hero hero1 = new Hero("TestHero1");
 Hero hero2 = new Hero("TestHero2");
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new StringEvent("TestUser", "/getheros"), connectionEndpoint));
 handler.sendHeros(connectionEndpoint);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos;
 try {
 oos = new ObjectOutputStream(baos);
 oos.writeObject(new Vector<Hero>(Arrays.asList(hero1, hero2)));
 } catch (IOException e) {
 fail();
 }
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois;
 try {
 Vector<Hero> receivedHeros = (Vector<Hero>) new ObjectInputStream(bais).readObject();
 assertEquals(2, receivedHeros.size());
 assertEquals(hero1, receivedHeros.elementAt(0));
 assertEquals(hero2, receivedHeros.elementAt(1));
 } catch (ClassNotFoundException | IOException e) {
 fail();
 }
 }

 @Test
 public void testUpdateHero() {
 String expectedName = "TestUserNew";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new HeroEvent(connectionEndpoint, hero), connectionEndpoint));
 handler.updateHero(connectionEndpoint, new Hero("TestUserNew"));
 assertEquals(expectedName, handler.getName(connectionEndpoint));
 }

 private String getLastSentMessage() {
 return handler.getLastSentMessage();
 }

 private String getLastReceivedMessage(ConnectionEndpoint connectionEndpoint) {
 return handler.clients.get(handler.clients.indexOf(handler.getEntry(connectionEndpoint))).lastMessage;
 }

 private Vector<Hero> getConnectedHero(ConnectionEndpoint connectionEndpoint) {
 return handler.clients.get(handler.clients.indexOf(handler.getEntry(connectionEndpoint))).hero;
 }

 private Vector<Hero> getNumSystemMessagesSent() {
 return handler.systemMessagesSent;
 }

 private int getNumSystemMessagesReceived() {
 return handler.systemMessagesReceived;
 }

 private Entry getEntry(ConnectionEndpoint connectionEndpoint) {
 for (Entry entry : handler.clients) {
 if (entry.ce.getClientPort() == connectionEndpoint.getClientPort()) {
 return entry;
 }
 }
 return null;
 }
}
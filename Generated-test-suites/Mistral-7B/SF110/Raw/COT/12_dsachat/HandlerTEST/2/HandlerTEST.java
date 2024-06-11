package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Test;
import dsachat.server.Handler;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private Hero hero;
 private Hero updatedHero;
 private Entry entry;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint();
 hero = new Hero("TestHero");
 updatedHero = new Hero("TestUpdatedHero");
 entry = new Entry();
 clients = new Vector<Entry>();
 }

 @Test
 public void testAddConnection() {
 justCall(handler::addConnection, connectionEndpoint);
 assertTrue(handler.clients.size() == 1);
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithException() {
 justCall(handler::addConnection, connectionEndpoint);
 connectionEndpoint.throwIOException();
 assertTrue(true); // This test will fail if no exception is thrown
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertTrue(handler.clients.size() == 1);
 }

 @Test
 public void testRemoveConnectionWithNullName() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(null);
 assertTrue(handler.clients.size() == 1);
 }

 @Test
 public void testHandleInputWithString() {
 String msg = "/dice";
 handler.addConnection(connectionEndpoint);
 entry = new Entry();
 entry.ce = connectionEndpoint;
 entry.name = "";
 handler.clients.addElement(entry);
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertEquals("TestHero rolled dice -->", handler.getLastSystemMessage());
 }

 @Test
 public void testHandleInputWithInvalidCommand() {
 String msg = "/invalidCommand";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertEquals("command /invalidCommand not found", handler.getLastSystemMessage());
 }

 @Test
 public void testHandleInputWithStringAndGmConnected() {
 String msg = "/gm rolled dice";
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertEquals("TestHero: Gamemaster rolled dice", handler.getLastSystemMessage());
 }

 @Test
 public void testHandleInputWithChallenge() {
 Challenge challenge = new Challenge(10, null, false);
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, challenge));
 assertEquals("TestHero rolled 1d10 + 0 --> 3", handler.getLastSystemMessage());
 }

 @Test
 public void testHandleInputWithChallengeToAnotherClient() {
 ConnectionEndpoint anotherClient = new ConnectionEndpoint();
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherClient);
 Challenge challenge = new Challenge(10, "TestClient", false);
 handler.handleInput(new InputEvent(connectionEndpoint, challenge));
 assertEquals("TestClient: TestHero rolled 1d10 + 0 --> 3", handler.getLastSystemMessage());
 }

 @Test
 public void testHandleInputWithHeroUpdate() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 assertEquals(hero.getName(), handler.getName(connectionEndpoint));
 }

 @Test
 public void testHandleInputWithDuplicateHeroUpdate() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 assertEquals(hero.getName(), handler.getName(connectionEndpoint));
 }

 @Test
 public void testHandleInputWithUserEventWhenGmConnected() {
 UserEvent userEvent = new UserEvent(false, "TestGm");
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 handler.handleInput(new InputEvent(connectionEndpoint, userEvent));
 assertEquals("Gamemaster connected", handler.getLastSystemMessage());
 }

 @Test
 public void testSendHeros() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.sendHeros(connectionEndpoint);
 Vector<Hero> heroes = (Vector<Hero>) connectionEndpoint.receive();
 assertEquals(1, heroes.size());
 assertEquals(hero.getName(), heroes.get(0).getName());
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 assertTrue(handler.isGmConnected());
 handler.removeConnection(new ConnectionEndpoint("Gamemaster"));
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMessageToAll() {
 String msg = "TestSystemMessage";
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.sendAllSystemMsg(msg);
 assertEquals(msg, handler.getLastSystemMessage());
 }

 @Test
 public void testWhisper() {
 String msg = "/w TestClient TestMessage";
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint("TestClient"));
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertEquals("You whisper to TestClient: TestMessage", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDice() {
 String msg = "/dice 2d6 + 2";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertEquals("TestHero rolled 2d6 + 2 --> 7, 5", handler.getLastSystemMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRollDiceWithInvalidRollString() {
 String msg = "/dice a";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertTrue(true); // This test will fail if no exception is thrown
 }

 @Test
 public void testRollDiceWithMultipleDice() {
 String msg = "/dice 3d8 + 3";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String(msg)));
 assertEquals("TestHero rolled 3d8 + 3 --> 4, 2, 5", handler.getLastSystemMessage());
 }

 private void justCall(Consumer<? super ConnectionEndpoint> method, ConnectionEndpoint connectionEndpoint) {
 method.accept(connectionEndpoint);
 }

 private String getLastSystemMessage() {
 String lastMessage = handler.getLastSystemMessage();
 assertEquals("", lastMessage); // Initialize the last system message to empty
 return lastMessage;
 }
}
package dsachat.server.test;

import static org.junit.Assert.*;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import org.junit.Test;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private Entry entry;
 private Hero hero;

 @Before
 public void setup() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint(0);
 entry = new Entry();
 entry.ce = connectionEndpoint;
 hero = new Hero("testHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionEndpoint);
 assertEquals(1, handler.clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithException() {
 handler.addConnection(null);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertEquals(0, handler.clients.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveConnectionWithNullConnection() {
 handler.removeConnection(null);
 }

 @Test
 public void testHandleInputString() {
 entry.name = "TestUser";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/hello")));
 assertEquals("TestUser: /hello", handler.getLastSentMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleInputWithNullInput() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, null));
 }

 @Test
 public void testHandleInputChallenge() {
 entry.name = "TestUser";
 handler.addConnection(connectionEndpoint);
 final Challenge challenge = new Challenge(5);
 handler.handleInput(new InputEvent(connectionEndpoint, challenge));
 assertEquals(5, challenge.roll());
 }

 @Test
 public void testHandleInputChallReq() {
 entry.name = "TestUser1";
 handler.addConnection(connectionEndpoint);
 entry.name = "TestUser2";
 handler.addConnection(new ConnectionEndpoint(1));
 final ChallReq challReq = new ChallReq("TestUser1", new Challenge(5));
 handler.handleInput(new InputEvent(connectionEndpoint, challReq));
 assertEquals(5, handler.clients.get(1).hero.getLevel());
 }

 @Test
 public void testHandleInputHero() {
 entry.name = "TestUser";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 assertEquals("TestUser updated his hero", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInputGmConnected() {
 entry.name = "TestUser";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new UserEvent(true, "Gamemaster")));
 assertEquals("Gamemaster connected", handler.getLastSentMessage());
 }

 @Test
 public void testSendHeros() {
 Vector<Hero> heroes = new Vector<>();
 heroes.add(hero);
 entry.hero = hero;
 handler.sendHeros(connectionEndpoint);
 assertEquals(hero, handler.getSentHero(0));
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint(0));
 handler.addConnection(new ConnectionEndpoint(1));
 handler.clients.get(1).name = "Gamemaster";
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendOneSystemMsg() {
 final String message = "Test message";
 handler.sendOneSystemMsg(connectionEndpoint, message);
 assertEquals(message, handler.getLastSentMessage());
 }

 @Test
 public void testSendAllSystemMsg() {
 final String message = "Test message";
 handler.sendAllSystemMsg(message);
 for (int i = 0; i < handler.clients.size(); ++i) {
 assertEquals(message, handler.clients.get(i).lastReceivedMessage);
 }
 }

 @Test
 public void testWhisper() {
 entry.name = "TestUser1";
 handler.addConnection(connectionEndpoint);
 entry.name = "TestUser2";
 handler.addConnection(new ConnectionEndpoint(1));
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/w TestUser2 testwhisper")));
 assertEquals("You whisper to TestUser2: testwhisper", handler.getLastSentMessage());
 }

 @Test
 public void testRollDice() {
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/roll 1d20 + 3")));
 assertEquals("TestUser rolled 1d20 + 3 --> 13", handler.getLastSentMessage());
 }

 @Test
 public void testRollDiceWithMultipleDice() {
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/roll 2d6 + 1")));
 assertEquals("TestUser rolls 2d6 + 1 --> 7 3", handler.getLastSentMessage());
 }

 @Test
 public void testRollDiceWithInvalidFormat() {
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/roll 2d6 +")));
 assertEquals("/roll 2d6 + invalid format", handler.getLastSentMessage());
 }

 @Test
 public void testRollDiceWithInvalidNumberOfDice() {
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/roll 0d6 + 1")));
 assertEquals("/roll 0d6 + 1 invalid number of dice", handler.getLastSentMessage());
 }

 @Test
 public void testRollDiceWithInvalidNumberOfSides() {
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/roll 1d0 + 1")));
 assertEquals("/roll 1d0 + 1 invalid number of sides", handler.getLastSentMessage());
 }

 @Test
 public void testRollDiceWithInvalidMod() {
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/roll 1d6 + -1")));
 assertEquals("/roll 1d6 + -1 invalid modifier", handler.getLastSentMessage());
 }

 @Test
 public void testSendToOneStr() {
 entry.name = "TestUser1";
 handler.addConnection(connectionEndpoint);
 entry.name = "TestUser2";
 handler.addConnection(new ConnectionEndpoint(1));
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/w TestUser2 testwhisper")));
 assertEquals("TestUser1: TestUser2: testwhisper", handler.getLastSentMessage());
 }

 @Test
 public void testUpdateHero() {
 entry.name = "TestUser";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 assertEquals("TestUser updated his hero", handler.getLastSentMessage());
 }

 @Test
 public void testSendAllStr() {
 final String message = "Test message";
 handler.sendAllStr(connectionEndpoint, message);
 for (int i = 0; i < handler.clients.size(); ++i) {
 assertEquals(message, handler.clients.get(i).lastReceivedMessage);
 }
 }

 @Test
 public void testSendAllObj() {
 final Object object = new Object();
 handler.sendAllObj(object);
 for (int i = 0; i < handler.clients.size(); ++i) {
 assertEquals(object, handler.clients.get(i).lastReceivedObject);
 }
 }

 @Test
 public void testGetName() {
 entry.name = "TestUser";
 assertEquals("TestUser", handler.getName(connectionEndpoint));
 }

 @Test
 public void testGetNameWithNullConnection() {
 assertEquals(null, handler.getName(null));
 }

}
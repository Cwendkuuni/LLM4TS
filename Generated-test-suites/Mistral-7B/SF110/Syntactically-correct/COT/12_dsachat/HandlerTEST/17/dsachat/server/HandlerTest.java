package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.Hero;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private Entry entry;
 private Hero hero;
 private Challenge challenge;
 private ChallReq challengereq;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint();
 entry = new Entry();
 hero = new Hero("TestHero");
 challenge = new Challenge("TestChallenge", 6, 20);
 challengereq = new ChallReq("TestUser", "TestUser2");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionEndpoint);
 assertTrue(handler.clients.size() > 0);
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithException() {
 handler.addConnection(connectionEndpoint);
 connectionEndpoint.disconnect();
 handler.addConnection(connectionEndpoint);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertTrue(handler.clients.size() < 2);
 }

 @Test
 public void testRemoveConnectionWithNullName() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(null);
 assertTrue(handler.clients.size() == 2);
 }

 @Test
 public void testHandleInputWithString() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String("test")));
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testHandleInputWithChallenge() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, challenge));
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testHandleInputWithChallReq() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.handleInput(new InputEvent(connectionEndpoint, challengereq));
 assertEquals(3, handler.clients.size());
 }

 @Test
 public void testHandleInputWithHero() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 assertNotNull(handler.clients.get(0).hero);
 }

 @Test
 public void testHandleInputWithUserEvent() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new UserEvent(true, "Gamemaster")));
 assertEquals("Gamemaster", handler.clients.get(0).name);
 }

 @Test
 public void testSendHeros() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.addHero(new Hero("TestHero2"));
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/getheros")));
 assertEquals(3, handler.clients.size());
 assertEquals(2, handler.clients.get(1).hero.getHealthPoints());
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint());
 handler.handleInput(new InputEvent(new ConnectionEndpoint(), new UserEvent(true, "Gamemaster")));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendOneSystemMsg() {
 handler.addConnection(connectionEndpoint);
 String expected = "TestUser: TestMessage";
 handler.handleInput(new InputEvent(connectionEndpoint, new String("TestMessage")));
 handler.sendOneSystemMsg(connectionEndpoint, expected);
 assertTrue(connectionEndpoint.getOutputBuffer().toString().contains(expected));
 }

 @Test
 public void testSendAllSystemMsg() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 String expected = "TestUser: TestMessage";
 handler.handleInput(new InputEvent(connectionEndpoint, new String("TestMessage")));
 handler.sendAllSystemMsg(expected);
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains(expected));
 }

 @Test
 public void testWhisper() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/w TestUser2 TestMessage")));
 handler.handleInput(new InputEvent(new ConnectionEndpoint(), new String("/w TestUser TestMessage2")));
 String expected = "TestUser whispers: TestMessage";
 assertTrue(connectionEndpoint.getOutputBuffer().toString().contains(expected));
 }

 @Test
 public void testRollDice() {
 handler.addConnection(connectionEndpoint);
 String expected = "TestUser rolled 1d20 + 5 --> 12";
 handler.handleInput(new InputEvent(connectionEndpoint, new String("1d20+5")));
 handler.rollDice(connectionEndpoint, expected.split("-->")[0]);
 assertTrue(connectionEndpoint.getOutputBuffer().toString().contains(expected));
 }

 @Test
 public void testSendToOneStr() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.handleInput(new InputEvent(connectionEndpoint, new String("/w TestUser2 TestMessage")));
 handler.sendToOneStr(connectionEndpoint, "TestUser3", "TestMessage");
 String expected = "TestUser2: TestMessage";
 assertTrue(new ConnectionEndpoint().getOutputBuffer().toString().contains(expected));
 }

 @Test
 public void testUpdateHero() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 String expectedName = hero.getName();
 handler.updateHero(connectionEndpoint, new Hero("TestHeroUpdated"));
 assertEquals(expectedName, handler.clients.get(0).name);
 }

 @Test
 public void testSendAllStr() {
 handler.addConnection(connectionEndpoint);
 String expected = "TestUser: TestMessage1, TestUser: TestMessage2";
 handler.handleInput(new InputEvent(connectionEndpoint, new String("TestMessage1")));
 handler.sendAllStr(connectionEndpoint, "TestMessage2");
 assertTrue(connectionEndpoint.getOutputBuffer().toString().contains(expected));
 }

 @Test
 public void testSendAllObj() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.sendAllObj(new String("TestMessage1"));
 handler.sendAllObj(new String("TestMessage2"));
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains("TestMessage1"));
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains("TestMessage2"));
 }

 @Test
 public void testGetName() {
 handler.addConnection(connectionEndpoint);
 assertNull(handler.getName(connectionEndpoint));
 handler.handleInput(new InputEvent(connectionEndpoint, new UserEvent(true, "TestUser")));
 assertEquals("TestUser", handler.getName(connectionEndpoint));
 }
}
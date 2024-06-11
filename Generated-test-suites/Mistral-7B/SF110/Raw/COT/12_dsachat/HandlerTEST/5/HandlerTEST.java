package dsachat.server.test;

import static org.junit.Assert.*;

import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Hero;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint ce;
 private Entry entry;

 @Before
 public void setUp() {
 handler = new Handler();
 ce = new ConnectionEndpoint();
 entry = new Entry();
 }

 @Test
 public void testAddConnection() {
 entry.ce = ce;
 handler.addConnection(ce);
 assertTrue(handler.clients.contains(entry));
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithException() {
 ce.mockException(); // Assuming ConnectionEndpoint has a method to mock exceptions
 handler.addConnection(ce);
 fail("Exception expected but not thrown");
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(ce);
 handler.addConnection(ce);
 handler.removeConnection(ce);
 assertFalse(handler.clients.contains(entry));
 }

 @Test
 public void testRemoveConnectionWithNullName() {
 handler.addConnection(ce);
 handler.removeConnection(ce);
 assertFalse(handler.clients.contains(entry));
 handler.addConnection(ce);
 handler.removeConnection(null);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputWithString() {
 String msg = "/hello world";
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, msg));
 String expected = "*handler: hello world";
 assertTrue(handler.sendAllStrCallCount == 1);
 assertEquals(expected, handler.sendAllStrCalls.get(0));
 }

 @Test
 public void testHandleInputWithStringEmpty() {
 String msg = "";
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, msg));
 String expected = "*handler: ";
 assertTrue(handler.sendAllStrCallCount == 1);
 assertEquals(expected, handler.sendAllStrCalls.get(0));
 }

 @Test
 public void testHandleInputWithChallenge() {
 Challenge challenge = new Challenge();
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, challenge));
 String expected = "Gamemaster rolled: ";
 assertTrue(handler.sendOneSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendOneSystemMsgCalls.get(0));
 }

 @Test(expected = IOException.class)
 public void testHandleInputWithIOException() {
 IOException exception = new IOException();
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, exception));
 fail("IOException expected but not thrown");
 }

 @Test
 public void testHandleInputWithChallReq() {
 ChallReq challReq = new ChallReq("user1", "user2", "message");
 handler.addConnection(ce);
 handler.addConnection(new ConnectionEndpoint());
 handler.handleInput(new InputEvent(new ConnectionEndpoint(), challReq));
 String expectedTo = "user2";
 String expectedSend = "user1: message";
 assertTrue(handler.sendToOneStrCallCount == 1);
 assertEquals(expectedTo, handler.sendToOneStrCalls.get(0).getTo());
 assertEquals(expectedSend, handler.sendToOneStrCalls.get(0).getSend());
 }

 @Test(expected = IOException.class)
 public void testHandleInputWithIOException2() {
 IOException exception = new IOException();
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, exception));
 fail("IOException expected but not thrown");
 }

 @Test
 public void testHandleInputWithHero() {
 Hero hero = new Hero("user", "heroName");
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, hero));
 String expectedName = "heroName";
 assertTrue(handler.updateHeroCallCount == 1);
 assertEquals(expectedName, handler.updateHeroCalls.get(0).getName());
 }

 @Test(expected = IOException.class)
 public void testHandleInputWithIOException3() {
 IOException exception = new IOException();
 handler.addConnection(ce);
 handler.handleInput(new InputEvent(ce, exception));
 fail("IOException expected but not thrown");
 }

 @Test
 public void testSendAllSystemMsg() {
 String expected = "Gamemaster: test message";
 handler.sendAllSystemMsg("test message");
 assertEquals(expected, handler.lastSentMessage);
 }

 @Test
 public void testSendAllSystemMsgWithNullMessage() {
 String expected = "";
 handler.sendAllSystemMsg(null);
 assertEquals(expected, handler.lastSentMessage);
 }

 @Test
 public void testWhisper() {
 String expectedTo = "user1";
 String expectedSend = "user2: test message";
 handler.addConnection(new ConnectionEndpoint());
 handler.addConnection(new ConnectionEndpoint());
 handler.whisper(ce, "user1 user2 test message");
 assertTrue(handler.sendToOneStrCallCount == 1);
 assertEquals(expectedTo, handler.sendToOneStrCalls.get(0).getTo());
 assertEquals(expectedSend, handler.sendToOneStrCalls.get(0).getSend());
 }

 @Test
 public void testRollDice() {
 String expected = "handler rolled 3d6 --> 3 5 1";
 handler.rollDice(ce, "3d6");
 assertTrue(handler.sendAllSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendAllSystemMsgCalls.get(0));
 }

 @Test
 public void testRollDiceWithInvalidNumberOfDice() {
 String expected = "handler rolled 1d6 --> 1";
 handler.rollDice(ce, "1d6+3");
 assertTrue(handler.sendAllSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendAllSystemMsgCalls.get(0));
 }

 @Test
 public void testRollDiceWithInvalidNumberOfSides() {
 String expected = "handler rolled 3d0 --> 0 0 0";
 handler.rollDice(ce, "3d0");
 assertTrue(handler.sendAllSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendAllSystemMsgCalls.get(0));
 }

 @Test(expected = NumberFormatException.class)
 public void testRollDiceWithInvalidRollFormat() {
 handler.rollDice(ce, "3d6asd");
 fail("NumberFormatException expected but not thrown");
 }

 @Test
 public void testRollDiceWithSilent() {
 String expected = "";
 handler.rollDice(ce, "3d6s");
 assertTrue(handler.sendAllSystemMsgCallCount == 0);
 }

 @Test
 public void testRollDiceWithMultipleDice() {
 String expected = "handler rolled 5d6 --> 5 6 3 5 1";
 handler.rollDice(ce, "5d6");
 assertTrue(handler.sendAllSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendAllSystemMsgCalls.get(0));
 }

 @Test
 public void testRollDiceWithModifiers() {
 String expected = "handler rolled 3d6 --> 4 4 2 +3";
 handler.rollDice(ce, "3d6+3");
 assertTrue(handler.sendAllSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendAllSystemMsgCalls.get(0));
 }

 @Test
 public void testRollDiceWithMultipleModifiers() {
 String expected = "handler rolled 3d6 --> 5 4 3 +3 +1";
 handler.rollDice(ce, "3d6+3+1");
 assertTrue(handler.sendAllSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendAllSystemMsgCalls.get(0));
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedWithNoGm() {
 handler.addConnection(new ConnectionEndpoint("user"));
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendAllObj() {
 Object expected = new Vector<Hero>();
 handler.addConnection(ce);
 String[] data = {"hero1Name", "hero1Class", "hero1Level"};
 for (String str : data) {
 Hero hero = new Hero("", str);
 handler.handleInput(new InputEvent(ce, hero));
 }
 handler.sendAllObj(expected);
 assertTrue(handler.clients.get(0).hero.getName().equals("hero1Name"));
 }

 @Test
 public void testSendAllObjWithNullObject() {
 handler.sendAllObj(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testSendAllStr() {
 String expected = "*handler: test message";
 handler.sendAllStr(ce, "test message");
 assertTrue(handler.sendAllStrCallCount == 1);
 assertEquals(expected, handler.sendAllStrCalls.get(0));
 }

 @Test
 public void testSendAllStrWithNullMessage() {
 handler.sendAllStr(ce, null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testSendOneSystemMsg() {
 String expected = "test message";
 handler.sendOneSystemMsg(ce, "test message");
 assertTrue(handler.sendOneSystemMsgCallCount == 1);
 assertEquals(expected, handler.sendOneSystemMsgCalls.get(0));
 }

 @Test(expected = IOException.class)
 public void testSendOneSystemMsgWithIOException() {
 handler.addConnection(ce);
 handler.sendOneSystemMsg(ce, "test message");
 fail("IOException expected but not thrown");
 }

 @Test
 public void testUpdateHero() {
 String expectedName = "newHeroName";
 Hero newHero = new Hero("newHeroName", "newHeroClass");
 handler.updateHero(ce, newHero);
 assertTrue(handler.clients.get(0).hero.getName().equals(expectedName));
 }

 @Test(expected = IOException.class)
 public void testUpdateHeroWithIOException() {
 handler.addConnection(ce);
 handler.updateHero(ce, new Hero("newHeroName", "newHeroClass"));
 fail("IOException expected but not thrown");
 }
}
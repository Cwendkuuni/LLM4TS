package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.ConnectionEndpoint;
import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.Hero;
import dsachat.server.UserEvent;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

public class HandlerTest {

 private Handler handler;
 private Vector<ConnectionEndpoint> mocks;

 @Before
 public void setup() {
 handler = new Handler();
 mocks = new Vector<ConnectionEndpoint>();
 }

 @Test
 public void testAddConnection() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 assertEquals(1, handler.clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnectionThrowsExceptionIfNotConnected() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 mock.setMockDisconnected(true);
 handler.addConnection(mock);
 }

 @Test
 public void testRemoveConnection() {
 ConnectionEndpoint mock1 = new MockConnectionEndpoint();
 handler.addConnection(mock1);
 handler.removeConnection(mock1);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testRemoveConnectionThrowsExceptionIfNotPresent() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 handler.removeConnection(new MockConnectionEndpoint());
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputString() {
 String msg = "/w whispers test";
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 handler.handleInput(new InputEvent(mock, new String(msg)));
 String expected = "You whisper to test: whispers test";
 assertTrue(mock.outContent.contains(expected));
 }

 @Test
 public void testHandleInputChallenge() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(mock, challenge));
 String expected = "You rolled " + challenge.roll() + ".";
 assertTrue(mock.outContent.contains(expected));
 }

 @Test
 public void testHandleInputChallReq() {
 ConnectionEndpoint mock1 = new MockConnectionEndpoint();
 ConnectionEndpoint mock2 = new MockConnectionEndpoint();
 handler.addConnection(mock1);
 handler.addConnection(mock2);
 ChallReq challengeReq = new ChallReq("test", new Hero("test", "test"));
 handler.handleInput(new InputEvent(mock1, challengeReq));
 String expected = "test connected";
 assertTrue(mock2.inContent.contains(expected));
 }

 @Test
 public void testHandleInputHero() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 Hero hero = new Hero("test", "test");
 handler.addConnection(mock);
 handler.handleInput(new InputEvent(mock, hero));
 String expected = "test updated his hero";
 assertTrue(mock.outContent.contains(expected));
 }

 @Test
 public void testHandleInputUserEventGmConnected() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 handler.handleInput(new InputEvent(mock, userEvent));
 String expected = "Gamemaster connected";
 assertTrue(mock.outContent.contains(expected));
 }

 @Test
 public void testSendHeros() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 Hero hero1 = new Hero("test1", "test1");
 Hero hero2 = new Hero("test2", "test2");
 handler.sendHeros(mock);
 assertEquals(2, mock.heroes.size());
 assertTrue(mock.heroes.contains(hero1));
 assertTrue(mock.heroes.contains(hero2));
 }

 @Test
 public void testIsGmConnected() {
 ConnectionEndpoint mock1 = new MockConnectionEndpoint();
 handler.addConnection(mock1);
 ConnectionEndpoint mock2 = new MockConnectionEndpoint();
 handler.addConnection(new MockConnectionEndpoint("Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendOneSystemMsg() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 String msg = "Test message";
 handler.sendOneSystemMsg(mock, msg);
 String expected = "Test message";
 assertTrue(mock.outContent.contains(expected));
 }

 @Test
 public void testSendAllSystemMsg() {
 ConnectionEndpoint mock1 = new MockConnectionEndpoint();
 ConnectionEndpoint mock2 = new MockConnectionEndpoint();
 handler.addConnection(mock1);
 handler.addConnection(mock2);
 String msg = "Test message";
 handler.sendAllSystemMsg(msg);
 assertTrue(mock1.outContent.contains(msg));
 assertTrue(mock2.outContent.contains(msg));
 }

 @Test
 public void testWhisper() {
 ConnectionEndpoint mock1 = new MockConnectionEndpoint();
 ConnectionEndpoint mock2 = new MockConnectionEndpoint();
 handler.addConnection(mock1);
 handler.addConnection(mock2);
 String msg = "/w test test";
 handler.handleInput(new InputEvent(mock1, new String(msg)));
 String expected = "test whispers to test: test";
 assertTrue(mock2.outContent.contains(expected));
 }

 @Test
 public void testRollDice() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 String msg = "d6";
 handler.handleInput(new InputEvent(mock, new String(msg)));
 String expected = "You rolled a d6.";
 assertTrue(mock.outContent.contains(expected));
 }

 @Test
 public void testRollDiceWithMod() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 String msg = "d6+3";
 handler.handleInput(new InputEvent(mock, new String(msg)));
 int expected = 3 + Challenge.dice(6);
 assertTrue(mock.outContent.contains("You rolled " + expected + "."));
 }

 @Test
 public void testRollMultipleDice() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 String msg = "d6d6+3";
 handler.handleInput(new InputEvent(mock, new String(msg)));
 int expected = Challenge.dice(6) + Challenge.dice(6) + 3;
 assertTrue(mock.outContent.contains("You rolled " + expected + "."));
 }

 @Test
 public void testRollMultipleDiceWithMods() {
 ConnectionEndpoint mock = new MockConnectionEndpoint();
 handler.addConnection(mock);
 String msg = "d6d6+3d6";
 handler.handleInput(new InputEvent(mock, new String(msg)));
 int expected = Challenge.dice(6) + Challenge.dice(6) + Challenge.dice(6) + 3;
 assertTrue(mock.outContent.contains("You rolled " + expected + "."));
 }

 @After
 public void teardown() {
 handler = null;
 mocks.clear();
 }
}

class MockConnectionEndpoint implements ConnectionEndpoint {
 private String name;
 private Vector<Object> outContent = new Vector<Object>();
 private Vector<Object> inContent = new Vector<Object>();
 private Vector<Hero> heroes = new Vector<Hero>();
 private boolean disconnected = false;

 public void disconnect() {
 disconnected = true;
 }

 public boolean isConnected() {
 return !disconnected;
 }

 public void send(Object obj) {
 outContent.add(obj);
 }

 public int getClientPort() {
 return 1;
 }

 public void receive(Object obj) {
 inContent.add(obj);
 }

 public void setMockDisconnected(boolean disconnected) {
 this.disconnected = disconnected;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public void sendHero(Hero hero) {
 heroes.add(hero);
 }
}
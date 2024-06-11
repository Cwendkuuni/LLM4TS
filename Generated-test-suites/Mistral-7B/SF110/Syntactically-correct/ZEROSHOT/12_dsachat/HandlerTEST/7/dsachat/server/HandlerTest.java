package dsachat.server.test;

import static org.junit.Assert.*;

import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

import java.util.Vector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint ce1, ce2;

 @Before
 public void setUp() {
 handler = new Handler();
 ce1 = new ConnectionEndpoint();
 ce2 = new ConnectionEndpoint();
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 }

 @Test
 public void testAddConnection() {
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 handler.removeConnection(ce1);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint()); // add a non-GM connection
 assertFalse(handler.isGmConnected());

 handler.addConnection(new ConnectionEndpoint("Gamemaster")); // add a GM connection
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testHandleInputString() {
 final String msg = "/dice 3d6";
 handler.handleInput(new InputEvent(ce1, new String(msg)));

 String expectedOutput = handler.getName(ce1) + " rolled dice 3d6 --> ";
 for (int i = 0; i < 3; ++i) {
 expectedOutput += Challenge.dice(6) + "  ";
 }
 String actualOutput = handler.sendAllSystemMsg(expectedOutput).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testHandleInputChallenge() {
 final String to = "Player2";
 final Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(ce1, challenge));

 String expectedOutput = handler.getName(ce1) + ": ";
 handler.sendToOneStr(ce2, to, expectedOutput);
 assertEquals(expectedOutput, handler.sendAllSystemMsg(handler.sendToOneStr(ce2, to, expectedOutput).toString()).toString());
 }

 @Test
 public void testHandleInputChallReq() {
 final String to = "Player2";
 final ChallReq challReq = new ChallReq("Player1", "/dice 3d6");
 handler.handleInput(new InputEvent(ce1, challReq));

 handler.handleInput(new InputEvent(ce2, challReq));

 String expectedOutput = handler.getName(ce1) + " rolled dice 3d6 --> ";
 for (int i = 0; i < 3; ++i) {
 expectedOutput += Challenge.dice(6) + "  ";
 }
 String actualOutput = handler.sendAllSystemMsg(expectedOutput).toString();
 assertEquals(expectedOutput, handler.sendAllSystemMsg(actualOutput).toString());
 }

 @Test
 public void testHandleInputHero() {
 final String name = "NewHero";
 final Hero hero = new Hero(name);
 handler.handleInput(new InputEvent(ce1, hero));

 String expectedOutput = "NewHero updated his hero";
 String actualOutput = handler.sendAllObj(new UserEvent(false, "Gamemaster")).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testHandleInputGmConnect() {
 handler.handleInput(new InputEvent(ce3, new UserEvent(true, "Gamemaster")));

 String expectedOutput = "Gamemaster connected";
 String actualOutput = handler.sendAllObj(new UserEvent(false, "Gamemaster")).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSendHeros() {
 final Vector<Hero> heroes = new Vector<Hero>();
 heroes.add(new Hero("Player1"));
 heroes.add(new Hero("Player2"));
 handler.sendHeros(ce1);
 String expectedOutput = "[Player1, Player2]";
 String actualOutput = new String(new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(handler.handleInput(new InputEvent(ce1, new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray(new ObjectOutputStream(new ByteArrayOutputStream().writeObject(heroes)).toByteArray()))))).getData());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSendAllStr() {
 final String message = "Hello, everyone!";
 handler.sendAllStr(ce1, message);
 String expectedOutput = handler.getName(ce1) + ": " + message;
 String actualOutput = handler.sendAllSystemMsg(expectedOutput).toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSendAllObj() {
 final Object obj = new Object() {
 @Override public String toString() { return "TestObject"; } };
 handler.sendAllObj(obj);
 String expectedOutput = "";
 for (int i = 0; i < handler.clients.size(); ++i) {
 expectedOutput += "TestObject";
 }
 String actualOutput = handler.sendAllSystemMsg(expectedOutput).toString();
 assertEquals(expectedOutput, actualOutput);
 }
}
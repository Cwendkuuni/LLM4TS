package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.ConnectionEndpoint;
import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private OutputStream outStream;
 private ByteArrayOutputStream byteArrayOutputStream;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint();
 outStream = connectionEndpoint.getOutputStream();
 byteArrayOutputStream = new ByteArrayOutputStream();
 connectionEndpoint.setOutputStream(byteArrayOutputStream);
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
 String msg = "/roll 2d6+1";
 handler.handleInput(new InputEvent(connectionEndpoint, msg));
 String expectedOutput = handler.getName(connectionEndpoint) + " rolled 2d6+1 --> ";
 int expectedSum = Challenge.dice(2) + Challenge.dice(6) + 1;
 assertEquals(expectedOutput, byteArrayOutputStream.toString().trim());
 assertEquals(Integer.toString(expectedSum), byteArrayOutputStream.toString().substring(expectedOutput.length()).trim());
 }

 @Test
 public void testHandleInputChallenge() {
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(connectionEndpoint, challenge));
 String expectedOutput = handler.getName(connectionEndpoint) + ": " + challenge.roll();
 assertEquals(expectedOutput, byteArrayOutputStream.toString().trim());
 }

 @Test
 public void testHandleInputChallReq() {
 ConnectionEndpoint targetConnectionEndpoint = new ConnectionEndpoint();
 handler.addConnection(connectionEndpoint);
 handler.addConnection(targetConnectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new ChallReq("target", "message")));
 assertEquals("message", byteArrayOutputStream.toString().substring(expectedOutput.length()));
 }

 @Test
 public void testHandleInputHero() {
 Hero hero = new Hero("testHero");
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 String expectedName = "testHero";
 assertEquals(expectedName, handler.getName(connectionEndpoint));
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint());
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedFalse() {
 handler.addConnection(new ConnectionEndpoint());
 handler.addConnection(new ConnectionEndpoint());
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 String msg = "testMessage";
 handler.sendAllSystemMsg(msg);
 for (Entry entry : handler.clients) {
 assertEquals(msg, entry.ce.getLastMessageReceived());
 }
 }

 @Test
 public void testWhisper() {
 String senderName = "testSender";
 String targetName = "testTarget";
 String msg = "testMessage";
 handler.addConnection(new ConnectionEndpoint(senderName));
 handler.addConnection(new ConnectionEndpoint(targetName));
 handler.handleInput(new InputEvent(connectionEndpoint, new WhisperCommand(targetName, msg)));
 assertEquals(senderName + " whispers: " + msg, byteArrayOutputStream.toString().trim());
 }

 @Test
 public void testRollDiceWithInvalidCommand() {
 handler.handleInput(new InputEvent(connectionEndpoint, "roll 2d6+1 invalid"));
 assertEquals("command /roll 2d6+1 invalid not found", byteArrayOutputStream.toString().trim());
 }

 @Test
 public void testRollDiceWithInvalidNumberOfDice() {
 handler.handleInput(new InputEvent(connectionEndpoint, "roll 2d6+1d0"));
 assertEquals("You can't roll a dice with 0 faces", byteArrayOutputStream.toString().trim());
 }

 @Test
 public void testRollDiceWithInvalidNumberOfSides() {
 handler.handleInput(new InputEvent(connectionEndpoint, "roll 2d-1"));
 assertEquals("Dice must have a positive number of sides", byteArrayOutputStream.toString().trim());
 }

 @Test(expected = NumberFormatException.class)
 public void testRollDiceWithInvalidNumberOfDiceAndSides() {
 handler.handleInput(new InputEvent(connectionEndpoint, "roll 2d-1+1"));
 }

 @Test
 public void testRollDiceWithInvalidModifier() {
 handler.handleInput(new InputEvent(connectionEndpoint, "roll 2d6+abc"));
 assertEquals("Invalid modifier", byteArrayOutputStream.toString().trim());
 }

 @Test
 public void testUpdateHeroWithNullConnection() {
 Hero hero = new Hero("testHero");
 handler.updateHero(null, hero);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testUpdateHeroWithExistingConnection() {
 Hero hero = new Hero("testHero");
 handler.addConnection(connectionEndpoint);
 handler.updateHero(connectionEndpoint, hero);
 assertEquals("testHero", handler.getName(connectionEndpoint));
 }

 @Test
 public void testUpdateHeroWithNewConnection() {
 Hero hero = new Hero("testHero");
 handler.updateHero(connectionEndpoint, hero);
 assertEquals("testHero", handler.getName(connectionEndpoint));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateHeroWithNullHero() {
 Hero hero = null;
 handler.updateHero(connectionEndpoint, hero);
 }

 @Test
 public void testSendAllStrWithNullString() {
 handler.sendAllStr(connectionEndpoint, null);
 assertEquals("", byteArrayOutputStream.toString().trim());
 }

 @Test
 public void testSendAllObjWithNullObject() {
 handler.sendAllObj(null);
 assertEquals(0, handler.clients.size());
 }
}
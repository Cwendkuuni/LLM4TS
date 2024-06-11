package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.*;
import dsachat.share.hero.*;
import dsachat.server.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint con1, con2, con3;
 private UserEvent userEvent1, userEvent2, userEventGm;
 private Hero hero1;
 private ChallReq challReq;
 private Challenge challenge;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;
 private InputStream input;

 @Before
 public void setUp() {
 handler = new Handler();
 con1 = new ConnectionEndpoint();
 con2 = new ConnectionEndpoint();
 con3 = new ConnectionEndpoint();
 userEvent1 = new UserEvent(false, "Player1");
 userEvent2 = new UserEvent(false, "Player2");
 userEventGm = new UserEvent(false, "Gamemaster");
 hero1 = new Hero("Player1", 10, 10, 10, 10, 10);
 challenge = new ChallengeDie(6);
 challReq = new ChallReq("Player2", challenge);
 }

 @Test
 public void testAddConnection() {
     handler.addConnection(con1);
     assertEquals(1, handler.clients.size());
     handler.addConnection(con2);
     assertEquals(2, handler.clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithNullConnection() {
     Handler handler = new Handler();
     handler.addConnection(null);
 }

 @Test
 public void testAddConnectionWithAlreadyConnectedConnection() {
     handler.addConnection(con1);
     handler.addConnection(con1);
     assertEquals(1, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.removeConnection(con2);
     assertEquals(1, handler.clients.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveConnectionWithNullConnection() {
     handler.removeConnection(null);
 }

 @Test
 public void testRemoveConnectionWithNonExistentConnection() {
     handler.addConnection(con1);
     handler.removeConnection(con2);
     assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputString() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/name Player1")));
     assertEquals("Player1", handler.getName(con1));
     handler.handleInput(new InputEvent(userEvent2, con2, new String("/name Player2")));
     assertEquals("Player2", handler.getName(con2));
 }

 @Test
 public void testHandleInputChallenge() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, challenge));
     assertEquals(challenge.roll(), handler.handleInput(new InputEvent(con1, new String("/getheros"))).toString());
 }

 @Test
 public void testHandleInputChallReq() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(userEvent1, con1, challReq));
     handler.handleInput(new InputEvent(con2, new String("/getheros")));
     assertEquals(hero1, handler.clients.elementAt(0).hero);
 }

 @Test
 public void testHandleInputUserEvent() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(userEventGm, con1, new UserEvent(true, "Gamemaster")));
     assertEquals("Gamemaster", handler.getName(con1));
 }

 @Test
 public void testSendHeros() {
     handler.addConnection(con1);
     Hero hero2 = new Hero("Player2", 1, 1, 1, 1, 1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(userEvent1, con1, new UserEvent(false, "Player1", hero1)));
     handler.handleInput(new InputEvent(userEvent2, con2, new UserEvent(false, "Player2", hero2)));
     handler.sendHeros(con1);
     List<Hero> heroes1 = new ArrayList<>(Arrays.asList(hero1));
     assertEquals(heroes1, handler.getHeroes(con1));
 }

 @Test
 public void testIsGmConnected() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(userEventGm, con1, new UserEvent(false, "Gamemaster")));
     assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedWithNoGm() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMsg() {
     handler.addConnection(con1);
     handler.sendOneSystemMsg(con1, "Test Message");
     handler.handleInput(new InputEvent(con1, new String("")));
     assertEquals("Test Message", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testSendAllSystemMsg() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(con1, new String("")));
     handler.handleInput(new InputEvent(con2, new String("")));
     handler.sendAllSystemMsg("Test Message");
     assertEquals("Test Message", handler.clients.elementAt(0).ce.getReceived().toString());
     assertEquals("Test Message", handler.clients.elementAt(1).ce.getReceived().toString());
 }

 @Test
 public void testWhisper() {
     handler.addConnection(con1);
     handler.addConnection(con2);
     handler.handleInput(new InputEvent(userEvent1, con1, new UserEvent(false, "Player1", hero1)));
     handler.handleInput(new InputEvent(userEvent2, con2, new UserEvent(false, "Player2", hero2)));
     handler.whisper(con1, "Player2: Test Whisper");
     handler.handleInput(new InputEvent(con2, new String("")));
     assertEquals("Player1 whispers: Player2: Test Whisper", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDice() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll 2d6 + 3")));
     handler.handleInput(new InputEvent(con1, new String("")));
     assertEquals("Player1 rolled 2d6 + 3 --> 7 7", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithNoRollCommand() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll")));
     handler.handleInput(new InputEvent(con1, new String("")));
     assertEquals("command /roll not found", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithInvalidRollCommand() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll 2d10 + 3d4 + 5")));
     handler.handleInput(new InputEvent(con1, new String("")));
     assertEquals("command /roll not found", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithMultipleDice() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll 3d6 + 2")));
     handler.handleInput(new InputEvent(con1, new String("")));
     String expectedOutput = "Player1 rolls 3d6 + 2 --> ";
     for (int i = 0; i < 3; ++i) {
         expectedOutput += Challenge.dice(6) + "  ";
     }
     expectedOutput += "1 1 1";
     assertEquals(expectedOutput, handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithModification() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll 2d6 + 3")));
     handler.handleInput(new InputEvent(con1, new String("")));
     String expectedOutput = "Player1 rolled 2d6 + 3 --> ";
     for (int i = 0; i < 2; ++i) {
         expectedOutput += Challenge.dice(6) + "  ";
     }
     expectedOutput += "3 3";
     assertEquals(expectedOutput, handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithInvalidInput() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll invalid")));
     handler.handleInput(new InputEvent(con1, new String("")));
     assertEquals("command /roll not found", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithIOException() {
     handler.addConnection(con1);
     InputStream input = new ByteArrayInputStream(new byte[0]);
     handler.handleInput(new InputEvent(con1, input));
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll 2d6 + 3")));
     handler.handleInput(new InputEvent(con1, new String("")));
     assertEquals("command /roll not found", handler.clients.elementAt(0).ce.getReceived().toString());
 }

 @Test
 public void testRollDiceWithMultipleInputs() {
     handler.addConnection(con1);
     handler.handleInput(new InputEvent(userEvent1, con1, new String("/roll 2d6 + 3")));
     handler.handleInput(new InputEvent(userEvent2, con2, new String("/roll 3d6 + 2")));
     handler.handleInput(new InputEvent(con1, new String("")));
     handler.handleInput(new InputEvent(con2, new String("")));
     String expectedOutput1 = "Player1 rolled 2d6 + 3 --> ";
     for (int i = 0; i < 2; ++i) {
         expectedOutput1 += Challenge.dice(6) + "  ";
     }
     expectedOutput1 += "3 3";
     String expectedOutput2 = "Player2 rolls 3d6 + 2 --> ";
     for (int i = 0; i < 3; ++i) {
         expectedOutput2 += Challenge.dice(6) + "  ";
     }
     expectedOutput2 += "2 1 5";
     assertEquals(expectedOutput1, handler.clients.elementAt(0).ce.getReceived().toString());
     assertEquals(expectedOutput2, handler.clients.elementAt(1).ce.getReceived().toString());
 }

}
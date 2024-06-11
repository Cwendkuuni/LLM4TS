package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallengeReq;
import dsachat.share.hero.Hero;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint client1, client2;

 @Before
 public void setup() {
 handler = new Handler();
 client1 = new ConnectionEndpoint();
 client2 = new ConnectionEndpoint();
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(client1);
 assertEquals(1, handler.clients.size());
 handler.addConnection(client2);
 assertEquals(2, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(client1);
 handler.addConnection(client2);
 handler.removeConnection(client1);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputWithStringMessage() {
 client1.sendInput("text message");
 handler.handleInput(new InputEvent(client1, "text message"));
 // Check if message was sent to all clients
 for (Entry e : handler.clients) {
 assertEquals("" + e.name + ": text message", e.ce.getOutput().toString());
 }
 }

 @Test
 public void testHandleInputWithChallenge() {
 Challenge challenge = new Challenge();
 client1.sendInput("/roll 4d6+1");
 handler.handleInput(new InputEvent(client1, challenge));
 // Check if result was sent to the sender
 assertEquals("" + handler.getName(client1) + " rolled 4d6+1 --> 7 11 2 4 10", handler.clients.get(0).ce.getOutput().toString());
 // Check if result was sent to all clients except the sender
 for (int i = 1; i < handler.clients.size(); i++) {
 assertEquals("" + handler.getName(handler.clients.get(i).ce) + " rolls 4d6+1 --> ", handler.clients.get(i).ce.getOutput().toString());
 }
 }

 @Test
 public void testHandleInputWithChallengeReq() {
 ChallengeReq challengeReq = new ChallengeReq("test", new Hero("test"));
 client1.sendInput("/challenge test user1 3d6");
 handler.handleInput(new InputEvent(client1, challengeReq));
 client2.sendInput("/accept challenge test user2");
 // Check if challenge was sent to the user who issued it
 assertEquals("You have been challenged by test to a 3d6 combat.", handler.clients.get(0).ce.getOutput().toString());
 // Check if challenge was accepted by the other user
 assertEquals("You have accepted a challenge from test to a 3d6 combat.", handler.clients.get(1).ce.getOutput().toString());
 // Check if result was sent to both users
 String expectedOutput = "test challenges test to a 3d6 combat.\n" +
                        "test rolls 3d6:\n" +
                        "3 1 5\n" +
                        "test's hero: test\n" +
                        "user1 rolls 3d6:\n" +
                        "1 4 2\n" +
                        "user1's hero: user1\n" +
                        "test wins the combat!";
 String actualOutput = "";
 for (int i = 0; i < handler.clients.size(); i++) {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 handler.handleInput(new InputEvent(handler.clients.get(i).ce, new UserEvent(true, handler.getName(handler.clients.get(i)))));
 actualOutput += outputStream.toString();
 System.setOut(System.out);
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testHandleInputWithInvalidCommand() {
 client1.sendInput("/invalidcommand");
 assertEquals("command /invalidcommand not found", handler.clients.get(0).ce.getOutput().toString());
 }

 @Test
 public void testGetName() {
 assertEquals("", handler.getName(client1));
 client1.name = "test";
 assertEquals("test", handler.getName(client1));
 }

 @Test(expected = IOException.class)
 public void testHandleInputWithIOException() {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 client1.setInput(inputStream);
 handler.handleInput(new InputEvent(client1, "test"));
 }

}
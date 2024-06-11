package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private Socket socket;
 private ConnectionEndpoint connectionEndpoint;
 private ByteArrayOutputStream outContent;
 private ByteArrayInputStream inContent;
 private ObjectOutputStream out;
 private ObjectInputStream in;
 private PrintWriter writer;

 @Before
 public void setUp() throws IOException {
 handler = new Handler();
 socket = new Socket("localhost", 0);
 connectionEndpoint = new ConnectionEndpoint(socket.getLocalPort());
 outContent = new ByteArrayOutputStream();
 out = new ObjectOutputStream(socket.getOutputStream());
 writer = new PrintWriter(socket.getOutputStream(), true);
 inContent = new ByteArrayInputStream(new byte[0]);
 in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
 }

 @Test
 public void testAddConnection() throws IOException {
 // test adding a connection
 handler.addConnection(connectionEndpoint);
 Entry expectedEntry = new Entry();
 expectedEntry.ce = connectionEndpoint;
 expectedEntry.name = "";
 assertTrue(handler.clients.contains(expectedEntry));
 }

 @Test
 public void testRemoveConnection() throws IOException {
 // test removing a connection
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertFalse(handler.clients.contains(handler.getEntry(connectionEndpoint)));
 }

 @Test
 public void testHandleInputStringCommand() throws IOException {
 // test handling input with a string command
 handler.addConnection(connectionEndpoint);
 String command = "/roll 2d6+2";
 writer.println(command);
 writer.flush();
 byte[] expectedOutput = new byte[]{"*Gamemaster: roll 2d6+2 --> ".getBytes(),
         new String(Challenge.dice(6) + " " + Challenge.dice(6) + "  ").getBytes(),
         "*Gamemaster: roll 2d6+2 --> ".getBytes(),
         new String(Challenge.dice(6) + " " + Challenge.dice(6) + "  ").getBytes()};
 outContent.reset();
 handler.handleInput(new InputEvent("String", connectionEndpoint, command));
 assertEquals(Arrays.toString(expectedOutput.toByteArray()), outContent.toByteArray());
 }

 @Test
 public void testHandleInputStringMessage() throws IOException {
 // test handling input with a string message
 handler.addConnection(connectionEndpoint);
 String message = "Hello world!";
 writer.println(message);
 writer.flush();
 byte[] expectedOutput = new byte[]{"*Gamemaster: Hello world!".getBytes()};
 outContent.reset();
 handler.handleInput(new InputEvent("String", connectionEndpoint, message));
 assertEquals(Arrays.toString(expectedOutput.toByteArray()), outContent.toByteArray());
 }

 @Test
 public void testHandleInputChallenge() throws IOException {
 // test handling input with a challenge
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint(1234));
 Hero hero = new Hero("TestHero");
 handler.handleInput(new InputEvent("Hero", connectionEndpoint, hero));
 String name = handler.getName(connectionEndpoint);
 assertEquals("TestHero", name);
 }

 @Test
 public void testHandleInputChallReq() throws IOException {
 // test handling input with a challenge request
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint(5678));
 String to = "TestUser";
 ChallReq challengeReq = new ChallReq(to);
 handler.handleInput(new InputEvent("ChallReq", connectionEndpoint, challengeReq));
 assertEquals(to, handler.clients.get(1).name);
 }

 @Test
 public void testSendAllSystemMsg() throws IOException {
 // test sending a system message to all clients
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint(1234));
 handler.addConnection(new ConnectionEndpoint(5678));
 String message = "This is a system message.";
 handler.sendAllSystemMsg(message);
 assertEquals(message, handler.clients.get(0).ce.getReceivedData(0).toString());
 assertEquals(message, handler.clients.get(1).ce.getReceivedData(0).toString());
 assertEquals(message, handler.clients.get(2).ce.getReceivedData(0).toString());
 }

 @Test
 public void testWhisper() throws IOException {
 // test whispering a message to another user
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint(1234));
 handler.addConnection(new ConnectionEndpoint(5678));
 String from = "TestUser1";
 String to = "TestUser2";
 String message = "This is a whisper message.";
 handler.handleInput(new InputEvent("String", connectionEndpoint, from + " whispers: " + message));
 assertEquals(message, handler.clients.get(2).ce.getReceivedData(0).toString());
 }

 @Test
 public void testRollDice() throws IOException {
 // test rolling dice
 handler.addConnection(connectionEndpoint);
 String command = "/roll 3d20";
 writer.println(command);
 writer.flush();
 byte[] expectedOutput = new byte[]{"*Gamemaster: roll 3d20 --> ".getBytes()};
 outContent.reset();
 for (int i = 0; i < 3; i++) {
 handler.handleInput(new InputEvent("DiceRollResult", connectionEndpoint, Challenge.dice(20)));
 }
 assertEquals(Arrays.toString(expectedOutput.toByteArray()), outContent.toByteArray());
 }

 @Test
 public void testSendAllObj() throws IOException {
 // test sending an object to all clients
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint(1234));
 handler.addConnection(new ConnectionEndpoint(5678));
 Hero hero = new Hero("TestHero");
 handler.sendAllObj(hero);
 assertEquals(hero, handler.clients.get(0).hero);
 assertEquals(hero, handler.clients.get(1).hero);
 assertEquals(hero, handler.clients.get(2).hero);
 }
}
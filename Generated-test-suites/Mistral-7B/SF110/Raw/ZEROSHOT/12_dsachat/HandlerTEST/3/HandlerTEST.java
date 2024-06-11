package dsachat.server.test;

import static org.junit.Assert.*;
import dsachat.server.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint ce1;
 private ConnectionEndpoint ce2;
 private Hero hero;
 private Challenge challenge;
 private ChallReq challReq;
 private UserEvent userEvent;
 private StringWriter stringWriter;

 @Before
 public void setUp() {
 handler = new Handler();
 ce1 = new ConnectionEndpoint(12345);
 ce2 = new ConnectionEndpoint(67890);
 hero = new Hero("TestHero");
 challenge = new Challenge(20, null, null);
 challReq = new ChallReq("TestUser", challenge);
 userEvent = new UserEvent(true, "TestUser");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(ce1);
 assertTrue(handler.clients.size() == 1);
 handler.addConnection(ce2);
 assertTrue(handler.clients.size() == 2);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.removeConnection(ce1);
 assertTrue(handler.clients.size() == 1);
 handler.removeConnection(ce2);
 assertTrue(handler.clients.size() == 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddConnectionNullConnection() {
 handler.addConnection(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveConnectionNullConnection() {
 handler.removeConnection(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testHandleInputString() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.sendAllObj(userEvent);
 handler.handleInput(new InputEvent(ce1, new String("test string")));
 assertEquals("*TestUser: test string", handler.clients.elementAt(0).ce.getOutputBuffer().toString());
 }

 @Test
 public void testHandleInputChallenge() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.sendAllObj(challenge);
 handler.handleInput(new InputEvent(ce1, challenge));
 assertTrue(handler.clients.get(0).ce.getOutputBuffer().toString().contains("TestUser rolled " + challenge.getRoll()));
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains("TestUser rolled " + challenge.getRoll()));
 }

 @Test
 public void testHandleInputChallReq() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.clients.elementAt(0).hero = hero;
 handler.handleInput(new InputEvent(ce2, challReq));
 assertEquals("TestUser challenged TestUser to a duel", handler.clients.elementAt(1).ce.getOutputBuffer().toString());
 }

 @Test
 public void testHandleInputHero() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, hero));
 assertEquals("TestHero connected", handler.clients.elementAt(0).name);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint(1234));
 handler.addConnection(new ConnectionEndpoint(5678));
 handler.addConnection(new ConnectionEndpoint(91011));
 handler.addConnection(new ConnectionEndpoint("Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.sendAllSystemMsg("Test message");
 assertTrue(handler.clients.get(0).ce.getOutputBuffer().toString().contains("Test message"));
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains("Test message"));
 }

 @Test
 public void testWhisper() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.handleInput(new InputEvent(ce1, new String("/w TestUser test whisper")));
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains("TestHero whispers: test whisper"));
 }

 @Test
 public void testRollDice() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, new String("/roll 2d6+3")));
 assertTrue(handler.clients.elementAt(0).ce.getOutputBuffer().toString().contains("TestUser rolled 2d6+3 --> "));
 }

 @Test
 public void testSendAllStr() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, new String("test string")));
 assertEquals("*TestUser: test string", handler.clients.elementAt(0).ce.getOutputBuffer().toString());
 }

 @Test
 public void testSendOneSystemMsg() {
 handler.addConnection(ce1);
 handler.sendOneSystemMsg(ce1, "Test message");
 assertEquals("Test message", handler.clients.elementAt(0).ce.getOutputBuffer().toString());
 }

 @Test
 public void testSendToOneStr() {
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 handler.handleInput(new InputEvent(ce1, new String("/w TestUser test whisper to user2")));
 assertTrue(handler.clients.get(1).ce.getOutputBuffer().toString().contains("TestUser whispers: test whisper to user2"));
 }

 @Test
 public void testUpdateHero() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, hero));
 assertTrue(handler.clients.elementAt(0).hero != null);
 }

 @Test
 public void testSendAllObj() {
 handler.addConnection(ce1);
 handler.handleInput(new InputEvent(ce1, userEvent));
 assertEquals("*TestUser: ", handler.clients.elementAt(0).ce.getOutputBuffer().toString().substring(0, 8));
 }

 @Test
 public void testGetName() {
 handler.addConnection(ce1);
 assertEquals("", handler.getName(ce1));
 handler.handleInput(new InputEvent(ce1, new String("TestUser")));
 assertEquals("TestUser", handler.getName(ce1));
 }
}
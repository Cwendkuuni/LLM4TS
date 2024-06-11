package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint connection;
 private Hero hero;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = new Vector<>();
 connection = new ConnectionEndpoint();
 hero = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
     handler.addConnection(connection);
     assertEquals(1, clients.size());
 }

 @Test
 public void testRemoveConnection() {
     handler.addConnection(connection);
     handler.removeConnection(connection);
     assertEquals(0, clients.size());
 }

 @Test
 public void testHandleInputString() {
     handler.addConnection(connection);
     handler.addConnection(new ConnectionEndpoint());
     handler.handleInput(new InputEvent(connection, "test"));
     assertEquals(2, clients.size());
 }

 @Test(expected = IOException.class)
 public void testHandleInputStringIOException() {
     handler.addConnection(connection);
     InputEvent event = new InputEvent(connection, "test");
     handler.handleInput(event);
     InputStream input = event.getSource().getInputStream();
     ByteArrayOutputStream output = new ByteArrayOutputStream();
     ObjectOutputStream os = new ObjectOutputStream(output);
     os.writeObject("test exception");
     input.readAllBytes();
     handler.handleInput(new InputEvent(connection, output.toByteArray()));
 }

 @Test
 public void testHandleInputChallenge() {
     handler.addConnection(connection);
     handler.addConnection(new ConnectionEndpoint());
     Challenge challenge = new Challenge(5, "TestChallenge");
     handler.handleInput(new InputEvent(connection, challenge));
     assertEquals(2, clients.size());
 }

 @Test(expected = IOException.class)
 public void testHandleInputChallengeIOException() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, new Challenge(5, "TestChallenge")));
     InputStream input = connection.getInputStream();
     ByteArrayOutputStream output = new ByteArrayOutputStream();
     ObjectOutputStream os = new ObjectOutputStream(output);
     os.writeObject("test exception");
     input.readAllBytes();
     handler.handleInput(new InputEvent(connection, output.toByteArray()));
 }

 @Test
 public void testHandleInputChallReq() {
     handler.addConnection(connection);
     handler.addConnection(new ConnectionEndpoint());
     handler.addConnection(new ConnectionEndpoint());
     handler.handleInput(new InputEvent(connection, new ChallReq("TestUser", "TestUser2", "TestMessage")));
     assertEquals(3, clients.size());
 }

 @Test(expected = IOException.class)
 public void testHandleInputChallReqIOException() {
     handler.addConnection(connection);
     handler.addConnection(new ConnectionEndpoint());
     handler.handleInput(new InputEvent(connection, new ChallReq("TestUser", "TestUser2", "TestMessage")));
     InputStream input = connection.getInputStream();
     ByteArrayOutputStream output = new ByteArrayOutputStream();
     ObjectOutputStream os = new ObjectOutputStream(output);
     os.writeObject("test exception");
     input.readAllBytes();
     handler.handleInput(new InputEvent(connection, output.toByteArray()));
 }

 @Test
 public void testHandleInputHero() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, hero));
     assertEquals(1, clients.size());
     Entry entry = clients.elementAt(0);
     assertEquals(hero.getName(), entry.name);
     assertEquals(hero, entry.hero);
 }

 @Test
 public void testHandleInputUserEvent() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, new UserEvent(true, "TestUser")));
     assertEquals(1, clients.size());
     assertEquals("TestUser", clients.elementAt(0).name);
 }

 @Test
 public void testIsGmConnected() {
     handler.addConnection(new ConnectionEndpoint());
     handler.addConnection(new ConnectionEndpoint());
     handler.addConnection(new Entry(new ConnectionEndpoint(), "Gamemaster", null));
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
     handler.addConnection(connection);
     handler.addConnection(new ConnectionEndpoint());
     handler.sendAllSystemMsg("TestMessage");
     assertEquals(2, clients.size());
 }

 @Test
 public void testWhisper() {
     handler.addConnection(connection);
     handler.addConnection(new ConnectionEndpoint());
     handler.handleInput(new InputEvent(connection, "/w TestUser TestMessage"));
     assertEquals(2, clients.size());
     assertEquals("TestUser: TestMessage", clients.elementAt(1).ce.getLastMessage());
 }

 @Test
 public void testRollDice() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, "/roll 1d6+3"));
     assertEquals(1, clients.size());
     assertEquals("You rolled 1d6+3 --> 4", clients.elementAt(0).ce.getLastMessage());
 }

 @Test
 public void testRollDiceMultipleDice() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, "/roll 2d6"));
     assertEquals(1, clients.size());
     String expected = "You rolled 2d6 --> ";
     assertTrue(clients.elementAt(0).ce.getLastMessage().startsWith(expected));
 }

 @Test(expected = IOException.class)
 public void testSendAllStrIOException() {
     handler.addConnection(connection);
     InputStream input = connection.getInputStream();
     ByteArrayOutputStream output = new ByteArrayOutputStream();
     ObjectOutputStream os = new ObjectOutputStream(output);
     os.writeObject("test exception");
     input.readAllBytes();
     handler.sendAllStr(connection, "TestMessage");
 }

 @Test
 public void testSendOneSystemMsg() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, "test"));
     handler.sendOneSystemMsg(connection, "TestMessage");
     assertEquals("TestMessage", connection.getLastMessage());
 }

 @Test(expected = IOException.class)
 public void testSendOneSystemMsgIOException() {
     handler.addConnection(connection);
     handler.handleInput(new InputEvent(connection, "test"));
     InputStream input = connection.getInputStream();
     ByteArrayOutputStream output = new ByteArrayOutputStream();
     ObjectOutputStream os = new ObjectOutputStream(output);
     os.writeObject("test exception");
     input.readAllBytes();
     handler.sendOneSystemMsg(connection, "TestMessage");
 }

}
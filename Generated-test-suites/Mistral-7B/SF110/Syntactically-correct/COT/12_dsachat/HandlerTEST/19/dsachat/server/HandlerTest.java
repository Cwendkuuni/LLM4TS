package dsachat.server.test;

import static org.junit.Assert.*;

import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection;

 @Before
 public void setUp() {
 handler = new Handler();
 connection = new ConnectionEndpoint();
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connection);
 assertEquals(1, handler.clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnectionWithNullConnection() {
 handler.addConnection(null);
 }

 @Test
 public void testRemoveConnectionWithExistingConnection() {
 handler.addConnection(connection);
 handler.removeConnection(connection);
 assertEquals(0, handler.clients.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveConnectionWithNonExistingConnection() {
 handler.removeConnection(connection);
 }

 @Test
 public void testHandleInputWithStringMessage() {
 String msg = "Hello, world!";
 handler.handleInput(new InputEvent(this, "user", new ByteArrayInputStream(msg.getBytes())));
 assertEquals(msg, handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testHandleInputWithChallengeMessage() {
 ConnectionEndpoint otherConnection = new ConnectionEndpoint();
 handler.addConnection(connection);
 handler.addConnection(otherConnection);
 Hero hero = new Hero("TestHero");
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(this, otherConnection, new ByteArrayInputStream(new byte[] {"C", "h", "a", "l", "l", "e", "n", "g", "e", " ", "r", "o", "l", "l", " ", "1", "d", "2", "0", "+"})));
 assertEquals("TestHero rolled 1d2+0 --> 2", handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testHandleInputWithChallengeMessageWithMultipleDice() {
 ConnectionEndpoint otherConnection = new ConnectionEndpoint();
 handler.addConnection(connection);
 handler.addConnection(otherConnection);
 Hero hero = new Hero("TestHero");
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(this, otherConnection, new ByteArrayInputStream(new byte[] {"C", "h", "a", "l", "l", "e", "n", "g", "e", " ", "r", "o", "l", "l", " ", "2", "d", "6", "0", "+"})));
 String expected = "TestHero rolled 2d6+0 --> 7 6 ";
 assertEquals(expected, handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testHandleInputWithUserEvent() {
 ConnectionEndpoint otherConnection = new ConnectionEndpoint();
 handler.addConnection(connection);
 handler.addConnection(otherConnection);
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(this, otherConnection, new ByteArrayOutputStream() {
 @Override
 public OutputStream writeTo(OutputStream out) throws IOException {
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 objOut.writeObject(new UserEvent(true, "TestUser"));
 return out;
 }
 }));
 assertEquals("TestUser connected", handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testHandleInputWithGmUserEvent() {
 ConnectionEndpoint gmConnection = new ConnectionEndpoint();
 handler.addConnection(gmConnection);
 handler.handleInput(new InputEvent(this, gmConnection, new ByteArrayInputStream(new byte[0])));
 handler.handleInput(new InputEvent(this, gmConnection, new ByteArrayOutputStream() {
 @Override
 public OutputStream writeTo(OutputStream out) throws IOException {
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 objOut.writeObject(new UserEvent(false, "Gamemaster"));
 return out;
 }
 }));
 assertEquals("Gamemaster connected", handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testSendHerosWithEmptyVector() {
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.sendHeros(connection);
 assertTrue(handler.sendBuffer.toString().isEmpty());
 handler.sendBuffer.reset();
 }

 @Test
 public void testSendHerosWithVectorContainingHero() {
 Vector<Hero> heroes = new Vector<>();
 heroes.add(new Hero("TestHero"));
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.sendHeros(connection);
 assertEquals("[TestHero]", handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testIsGmConnectedWithNoGmConnected() {
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedWithGmConnected() {
 ConnectionEndpoint gmConnection = new ConnectionEndpoint();
 handler.addConnection(gmConnection);
 assertTrue(handler.isGmConnected());
 }

 @Test(expected = NullPointerException.class)
 public void testSendToOneStrWithNullTo() {
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.sendToOneStr(connection, null, "TestMessage");
 }

 @Test
 public void testSendToOneStrWithValidTo() {
 String expected = "TestMessage from TestUser to TestUser: TestMessage";
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[] {"C", "h", "a", "l", "l", "e", "n", "g", "e", " ", "t", "o", " ", "TestUser"})));
 handler.handleInput(new InputEvent(this, connection, new ByteArrayOutputStream() {
 @Override
 public OutputStream writeTo(OutputStream out) throws IOException {
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 objOut.writeObject(new UserEvent(true, "TestUser"));
 return out;
 }
 }));
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.sendToOneStr(connection, "TestUser", "TestMessage");
 assertEquals(expected, handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testUpdateHeroWithNullHero() {
 Hero nullHero = null;
 assertNull(handler.updateHero(connection, nullHero));
 }

 @Test
 public void testUpdateHeroWithValidHero() {
 String expected = "TestHero updated his hero";
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.updateHero(connection, new Hero("TestHero"));
 assertEquals(expected, handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testSendAllStrWithEmptyVector() {
 String msg = "Hello, world!";
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.sendAllStr(connection, msg);
 assertTrue(handler.sendBuffer.toString().isEmpty());
 handler.sendBuffer.reset();
 }

 @Test
 public void testSendAllStrWithVectorContainingOneConnection() {
 String msg = "Hello, world!";
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.addConnection(otherConnection);
 handler.sendAllStr(connection, msg);
 assertEquals("Hello, world! from TestUser: Hello, world!", handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testSendAllObjWithEmptyVector() {
 Object obj = new Object();
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.sendAllObj(obj);
 assertTrue(handler.sendBuffer.toString().isEmpty());
 handler.sendBuffer.reset();
 }

 @Test
 public void testSendAllObjWithVectorContainingOneObject() {
 Object obj = new Object();
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[0])));
 handler.addConnection(otherConnection);
 handler.sendAllObj(obj);
 assertEquals("[B@1b", handler.sendBuffer.toString());
 handler.sendBuffer.reset();
 }

 @Test
 public void testGetNameWithExistingConnection() {
 String expected = "TestUser";
 handler.handleInput(new InputEvent(this, connection, new ByteArrayInputStream(new byte[] {"C", "l", "i", "e", "n", "t", " ", "1", "2", "3", "4", ":"})));
 handler.handleInput(new InputEvent(this, connection, new ByteArrayOutputStream() {
 @Override
 public OutputStream writeTo(OutputStream out) throws IOException {
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 objOut.writeObject(new ConnectionEndpoint(1234));
 objOut.writeObject("TestUser");
 return out;
 }
 }));
 assertEquals(expected, handler.getName(connection));
 }

 @Test
 public void testGetNameWithNonExistingConnection() {
 assertNull(handler.getName(connection));
 }
}
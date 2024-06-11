package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.junit.Test;

import dsachat.server.ConnectionEndpoint;
import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.Hero;
import dsachat.server.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint connection;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 handler = new Handler();
 clients = new Vector<>();
 }

 @Test
 public void testAddConnection() {
 connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 clients.add(new Entry());
 assertEquals(1, clients.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddConnectionNullConnection() {
 connection = null;
 handler.addConnection(connection);
 }

 @Test
 public void testRemoveConnection() {
 connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 handler.removeConnection(connection);
 assertEquals(0, clients.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveConnectionNullConnection() {
 connection = null;
 handler.removeConnection(connection);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint());
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
 public void testSendAllSystemMsg() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(outputStream);
 String message = "test message";
 handler.sendAllSystemMsg(message);
 for (Entry e : clients) {
 out.writeObject(e.ce);
 out.writeObject(message);
 Entry expected = clients.get(0);
 Entry actual = (Entry) out.readObject();
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testHandleInputString() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 clients.add(new Entry());
 String message = "/roll 3d6 + 2";
 handler.handleInput(new InputEvent(connection, message));
 assertTrue(message.contains("3d6"));
 }

 @Test
 public void testHandleInputChallenge() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 clients.add(new Entry());
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(connection, challenge));
 assertEquals("test", challenge.getTo());
 }

 @Test
 public void testHandleInputChallReq() throws IOException {
 ConnectionEndpoint sender = new ConnectionEndpoint();
 ConnectionEndpoint receiver = new ConnectionEndpoint();
 handler.addConnection(sender);
 handler.addConnection(receiver);
 clients.add(new Entry());
 clients.add(new Entry());
 ChallReq challReq = new ChallReq("test", new Hero("test"));
 handler.handleInput(new InputEvent(sender, challReq));
 assertEquals("test", receiver.getName());
 }

 @Test
 public void testHandleInputHero() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 clients.add(new Entry());
 Hero hero = new Hero("test");
 handler.handleInput(new InputEvent(connection, hero));
 assertEquals("test", clients.get(0).hero.getName());
 }

 @Test
 public void testHandleInputUserEvent() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 clients.add(new Entry());
 UserEvent userEvent = new UserEvent(true, "test");
 handler.handleInput(new InputEvent(connection, userEvent));
 assertEquals("test", clients.get(0).name);
 }

 @Test
 public void testSendOneSystemMsg() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 String message = "test message";
 handler.addConnection(connection);
 handler.sendOneSystemMsg(connection, message);
 assertEquals(message, connection.getMessageQueue().poll().toString());
 }

 @Test
 public void testSendToOneStr() throws IOException {
 ConnectionEndpoint connection1 = new ConnectionEndpoint();
 ConnectionEndpoint connection2 = new ConnectionEndpoint();
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 clients.add(new Entry());
 clients.add(new Entry());
 handler.sendToOneStr(connection1, "test", "test message");
 assertEquals("test message", connection2.getMessageQueue().poll().toString());
 }

 @Test
 public void testUpdateHero() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 handler.addConnection(connection);
 clients.add(new Entry());
 Hero hero = new Hero("test");
 handler.handleInput(new InputEvent(connection, hero));
 assertEquals("test", clients.get(0).hero.getName());
 }

 @Test
 public void testSendAllStr() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 String message = "test message";
 handler.addConnection(connection);
 handler.sendAllStr(connection, message);
 assertEquals(message, connection.getMessageQueue().poll().toString());
 }

 @Test
 public void testSendAllObj() throws IOException {
 ConnectionEndpoint connection = new ConnectionEndpoint();
 String message = "test message";
 handler.addConnection(connection);
 handler.sendAllObj(message);
 assertEquals(message.getClass(), connection.getMessageQueue().poll().getClass());
 }
}
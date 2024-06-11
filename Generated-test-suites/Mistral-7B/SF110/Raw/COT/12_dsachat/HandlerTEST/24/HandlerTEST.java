package dsachat.server.test;

import static org.junit.Assert.*;

import dsachat.server.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint dummyConnection;
 private InputStream in;
 private OutputStream out;
 private ByteArrayInputStream inputStream;
 private ByteArrayOutputStream outputStream;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 dummyConnection = Mockito.mock(ConnectionEndpoint.class);
 clients = new Vector<Entry>();
 in = Mockito.mock(InputStream.class);
 out = Mockito.mock(OutputStream.class);
 inputStream = new ByteArrayInputStream(new byte[0]);
 outputStream = new ByteArrayOutputStream();
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 handler.addConnection(dummyConnection);
 assertEquals("", entry.name);
 clients.add(entry);
 handler.removeConnection(dummyConnection);
 assertEquals(clients.size(), 0);
 }

 @Test(expected = Exception.class)
 public void testAddConnection_exception() {
 handler.addConnection(dummyConnection);
 handler.removeConnection(dummyConnection);
 handler.addConnection(dummyConnection);
 }

 @Test
 public void testHandleInput_string() {
 String msg = "/hello world";
 handler.handleInput(new InputEvent(dummyConnection, in, out, new String(msg.getBytes())));
 // Add assertions here for the handling of the /hello command
 }

 @Test
 public void testHandleInput_challenge() {
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(dummyConnection, in, out, challenge));
 // Add assertions here for the handling of the Challenge object
 }

 @Test
 public void testHandleInput_challReq() {
 ChallReq challReq = new ChallReq("test", "test2");
 handler.handleInput(new InputEvent(dummyConnection, in, out, challReq));
 // Add assertions here for the handling of the ChallReq object
 }

 @Test
 public void testHandleInput_hero() {
 Hero hero = new Hero("test");
 handler.handleInput(new InputEvent(dummyConnection, in, out, hero));
 // Add assertions here for the handling of the Hero object
 }

 @Test
 public void testHandleInput_userEvent() {
 UserEvent userEvent = new UserEvent(true, "test");
 handler.handleInput(new InputEvent(dummyConnection, in, out, userEvent));
 // Add assertions here for the handling of the UserEvent object
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new Entry(dummyConnection, "Gamemaster"));
 assertTrue(handler.isGmConnected());
 handler.removeConnection(dummyConnection);
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 String msg = "test message";
 handler.sendAllSystemMsg(msg);
 for (Entry entry : clients) {
 String name = entry.name;
 assertEquals(msg, name + ": " + msg);
 }
 }

 @Test
 public void testSendOneSystemMsg() {
 String to = "test";
 String msg = "test message to " + to;
 handler.sendOneSystemMsg(dummyConnection, msg);
 assertEquals(msg, handler.getName(dummyConnection) + ": " + msg);
 }

 @Test
 public void testWhisper() {
 String to = "test";
 String msg = "test message to " + to;
 handler.whisper(dummyConnection, to + " " + msg);
 for (Entry entry : clients) {
 if (entry.name.equals(to)) {
 assertEquals(msg, entry.name + ": " + msg);
 }
 }
 }

 @Test
 public void testRollDice() {
 String cmd = "2d6+3";
 handler.rollDice(dummyConnection, cmd);
 // Add assertions here for the handling of the dice roll command
 }

 @Test
 public void testSendHeros() {
 Vector<Hero> vh = new Vector<Hero>();
 vh.add(new Hero("test"));
 handler.sendHeros(dummyConnection);
 // Add assertions here for the sending of the heroes vector
 }
}
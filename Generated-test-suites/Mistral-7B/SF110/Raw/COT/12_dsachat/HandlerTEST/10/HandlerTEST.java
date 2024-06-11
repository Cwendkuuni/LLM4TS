package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint connectionEndpoint1, connectionEndpoint2;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = new Vector<Entry>();
 connectionEndpoint1 = new ConnectionEndpoint(12345);
 connectionEndpoint2 = new ConnectionEndpoint(67890);
 }

 @Test
 public void testAddConnection() {
 Entry entry1 = new Entry();
 entry1.ce = connectionEndpoint1;
 handler.addConnection(connectionEndpoint1);
 assertEquals(1, clients.size());
 }

 @Test(expected = Exception.class)
 public void testAddConnection_FailedToConnect() throws Exception {
 ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(Integer.MIN_VALUE);
 handler.addConnection(connectionEndpoint);
 fail("Expected exception was not thrown.");
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint1);
 handler.addConnection(connectionEndpoint2);
 handler.removeConnection(connectionEndpoint1);
 assertEquals(1, clients.size());
 }

 @Test
 public void testRemoveConnection_NotFound() {
 handler.addConnection(connectionEndpoint1);
 handler.addConnection(connectionEndpoint2);
 handler.removeConnection(connectionEndpoint3);
 assertEquals(2, clients.size());
 }

 @Test
 public void testHandleInput_String() {
 String msg = "/w foo bar";
 connectionEndpoint1.sendInput(new InputEvent(new ByteArrayOutputStream(), msg.getBytes(), connectionEndpoint1));
 handler.handleInput(connectionEndpoint1.getInputEvent());
 assertEquals("You whisper to foo: bar", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInput_String_MultipleRecipients() {
 String msg = "/w foo bar";
 connectionEndpoint1.sendInput(new InputEvent(new ByteArrayOutputStream(), msg.getBytes(), connectionEndpoint1));
 connectionEndpoint2.sendInput(new InputEvent(new ByteArrayOutputStream(), msg.getBytes(), connectionEndpoint2));
 handler.handleInput(connectionEndpoint1.getInputEvent());
 handler.handleInput(connectionEndpoint2.getInputEvent());
 assertEquals("You whisper to foo: bar", handler.getLastSentMessage());
 assertEquals("foo whispers to you: bar", handler.getLastSentMessageTo(connectionEndpoint2));
 }

 @Test
 public void testHandleInput_Challenge() {
 Challenge challenge = new Challenge();
 connectionEndpoint1.sendInput(new InputEvent(new ByteArrayOutputStream(), challenge, connectionEndpoint1));
 handler.handleInput(connectionEndpoint1.getInputEvent());
 assertEquals("dice roll result: 5", handler.getLastSentMessage());
 }

 @Test
 public void testHandleInput_Challenge_MultipleRecipients() {
 Challenge challenge = new Challenge();
 connectionEndpoint1.sendInput(new InputEvent(new ByteArrayOutputStream(), challenge, connectionEndpoint1));
 connectionEndpoint2.sendInput(new InputEvent(new ByteArrayOutputStream(), challenge, connectionEndpoint2));
 handler.handleInput(connectionEndpoint1.getInputEvent());
 handler.handleInput(connectionEndpoint2.getInputEvent());
 assertEquals("dice roll result: 5", handler.getLastSentMessage());
 assertEquals("You received a challenge: dice roll", handler.getLastSentMessageTo(connectionEndpoint2));
 }

 @Test
 public void testHandleInput_Hero() {
 Hero hero = new Hero("test");
 connectionEndpoint1.sendInput(new InputEvent(new ByteArrayOutputStream(), hero, connectionEndpoint1));
 handler.handleInput(connectionEndpoint1.getInputEvent());
 assertEquals("test updated his hero", handler.getLastSentMessage());
 assertEquals("test connected", handler.getLastSentMessageTo(connectionEndpoint1));
 }

 @Test
 public void testHandleInput_UserEvent_GmConnected() {
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 connectionEndpoint1.sendInput(new InputEvent(new ByteArrayOutputStream(), userEvent, connectionEndpoint1));
 handler.handleInput(connectionEndpoint1.getInputEvent());
 assertEquals("Gamemaster connected", handler.getLastSentMessage());
 }

 @Test
 public void testSendAllStr() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String msg = "test";
 handler.sendAllStr(connectionEndpoint1, msg);
 assertEquals(msg, new String(outputStream.toByteArray()));
 }

 @Test
 public void testSendOneSystemMsg() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String msg = "test";
 handler.sendOneSystemMsg(connectionEndpoint1, msg);
 assertEquals(msg, new String(outputStream.toByteArray()));
 }

 @Test
 public void testSendAllObj() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Object obj = new String("test");
 handler.sendAllObj(obj);
 assertEquals(obj.toString(), new String(outputStream.toByteArray()));
 }

 @Test
 public void testSendToOneStr() throws IOException {
 ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
 ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
 String to = "foo";
 String msg = "test";
 handler.sendToOneStr(connectionEndpoint1, to, msg);
 handler.sendToOneStr(connectionEndpoint2, to, msg);
 assertEquals(msg, new String(outputStream1.toByteArray()));
 assertEquals("", new String(outputStream2.toByteArray()));
 }

 private String getLastSentMessage() {
 String lastSentMessage = "";
 for (Entry entry : clients) {
 if (entry.ce != null) {
 OutputStream outputStream = entry.ce.getOutputStream();
 if (outputStream instanceof ByteArrayOutputStream) {
 lastSentMessage = new String(((ByteArrayOutputStream)outputStream).toByteArray());
 }
 }
 }
 return lastSentMessage;
 }

 private String getLastSentMessageTo(ConnectionEndpoint connectionEndpoint) {
 String lastSentMessageTo = "";
 for (Entry entry : clients) {
 if (entry.ce != null && entry.ce.getClientPort() == connectionEndpoint.getClientPort()) {
 lastSentMessageTo = entry.name;
 break;
 }
 }
 return lastSentMessageTo;
 }
}
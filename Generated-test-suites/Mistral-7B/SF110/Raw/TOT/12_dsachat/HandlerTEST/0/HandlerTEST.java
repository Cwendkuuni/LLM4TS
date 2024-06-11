package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ByteArrayOutputStream outputStream;
 private ConnectionEndpoint mockConnection;
 private List<Hero> heroes;

 @Before
 public void setUp() {
 handler = new Handler();
 outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 mockConnection = Mockito.mock(ConnectionEndpoint.class);
 heroes = Arrays.asList(new Hero("Hero1"), new Hero("Hero2"));
 }

 @After
 public void tearDown() {
 handler = null;
 System.setOut(System.out);
 }

 @Test
 public void testAddConnection() {
 // Test normal case
 handler.addConnection(mockConnection);
 assertEquals(1, handler.clients.size());
 verify(mockConnection, times(handler.clients.size())).send(ArgumentMatchers.any(UserEvent.class));

 // Test exceptional case
 doThrow(IOException.class).when(mockConnection).send(ArgumentMatchers.any(UserEvent.class));
 handler.addConnection(mockConnection);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 // Test normal case
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection);
 handler.removeConnection(mockConnection);
 assertEquals(1, handler.clients.size());

 // Test edge case - connection not found
 handler.removeConnection(mockConnection);
 assertEquals(1, handler.clients.size());

 // Test error case - connection already removed
 handler.removeConnection(mockConnection);
 handler.removeConnection(mockConnection);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInput() {
 // Test sending message to all clients
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection);
 String message = "Hello World";
 handler.handleInput(new InputEvent(message, mockConnection));
 String expectedOutput = "*Handler: Hello World\n*Handler: Hello World";
 assertEquals(expectedOutput, outputStream.toString());

 // Test sending message to single client
 handler.removeConnection(mockConnection);
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent(message, mockConnection));
 expectedOutput = "*Handler: Hello World";
 assertEquals(expectedOutput, outputStream.toString());

 // Test handling different types of input
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent("whisper John Hello", mockConnection));
 handler.handleInput(new InputEvent("/getheros", mockConnection));
 handler.handleInput(new InputEvent(new Challenge(), mockConnection));
 handler.handleInput(new InputEvent(new Hero(), mockConnection));
 handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), mockConnection));
 }

 @Test
 public void testSendHeros() {
 // Test sending heros to all clients
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection);
 handler.sendHeros(mockConnection);
 verify(mockConnection, times(2)).send(ArgumentMatchers.isInstanceOf(Vector.class));
 }

 @Test
 public void testIsGmConnected() {
 // Test normal case
 handler.addConnection(new ConnectionEndpoint() {
 @Override public int getClientPort() { return 1; }
 @Override public void send(Object o) throws IOException {}});
 handler.addConnection(new ConnectionEndpoint() {
 @Override public int getClientPort() { return 2; }
 @Override public void send(Object o) throws IOException {}});
 handler.addConnection(new ConnectionEndpoint() {
 @Override public int getClientPort() { return 3; }
 @Override public void send(Object o) throws IOException {}});
 handler.addConnection(new ConnectionEndpoint() {
 @Override public int getClientPort() { return 4; }
 @Override public void send(Object o) throws IOException {}});
 assertTrue(handler.isGmConnected());

 // Test edge case - no gm connected
 handler.removeConnection(handler.clients.get(0));
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMessage() {
 // Test sending message to all clients
 handler.addConnection(mockConnection);
 handler.addConnection(mockConnection);
 handler.sendSystemMessage("Hello World");
 String expectedOutput = "*Handler: Hello World\n*Handler: Hello World";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testWhisper() {
 // Test whispering to a single client
 handler.addConnection(mockConnection);
 handler.handleInput(new InputEvent("whisper John Hello", mockConnection));
 handler.handleInput(new InputEvent("whisper John Bye", mockConnection));
 String expectedOutput = "You whisper to John: Hello\nYou whisper to John: Bye";
 assertEquals(expectedOutput, outputStream.toString());

 // Test whispering to multiple clients
 handler.addConnection(new ConnectionEndpoint() {
 @Override public int getClientPort() { return 2; }
 @Override public void send(Object o) throws IOException {}});
 handler.handleInput(new InputEvent("whisper John Hello", mockConnection));
 handler.handleInput(new InputEvent("whisper John Bye", mockConnection));
 handler.handleInput(new InputEvent("whisper Jane Hi", mockConnection));
 handler.handleInput(new InputEvent("whisper Jane Bye", mockConnection));
 expectedOutput = "You whisper to John: Hello\nYou whisper to John: Bye\nYou whisper to Jane: Hi\nYou whisper to Jane: Bye";
 assertEquals(expectedOutput, outputStream.toString());
 }
}
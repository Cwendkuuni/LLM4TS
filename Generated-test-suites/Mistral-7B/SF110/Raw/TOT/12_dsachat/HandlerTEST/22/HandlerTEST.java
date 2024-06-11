package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection1, mockConnection2;

 @Before
 public void setUp() {
 handler = new Handler();
 mockConnection1 = mock(ConnectionEndpoint.class);
 mockConnection2 = mock(ConnectionEndpoint.class);
 }

 @After
 public void tearDown() {
 handler = null;
 mockConnection1 = null;
 mockConnection2 = null;
 }

 @Test
 public void testAddConnection() {
 // Test adding a connection with a valid ConnectionEndpoint
 Entry entry = new Entry();
 handler.addConnection(mockConnection1);
 assertTrue(handler.clients.contains(entry));

 // Test adding a connection with an already connected client
 handler.addConnection(mockConnection1);
 handler.addConnection(mockConnection1);
 assertEquals(1, handler.clients.size());

 // Test adding a connection with a null ConnectionEndpoint
 handler.addConnection(null);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 // Test removing a connection with a valid ConnectionEndpoint
 Entry entry = new Entry();
 handler.addConnection(mockConnection1);
 handler.removeConnection(mockConnection1);
 assertFalse(handler.clients.contains(entry));

 // Test removing a connection with a non-existent ConnectionEndpoint
 handler.removeConnection(mockConnection2);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testHandleInputWithStringData() {
 // Test sending a message to all clients when a message is received from a client
 when(mockConnection1.send(any(String.class))).thenReturn(true);
 when(mockConnection2.send(any(String.class))).thenReturn(true);
 String message = "Hello, world!";
 handler.handleInput(new InputEvent(mockConnection1, new StringData(message)));
 verify(mockConnection1, times(1)).send(anyStringThatMatches(".*" + message + ".*"));
 verify(mockConnection2, times(1)).send(anyStringThatMatches(".*" + message + ".*"));

 // Test sending a message to a specific client when a message is received from a client with a recipient
 when(mockConnection1.send(any(String.class))).thenReturn(true);
 String recipientName = "recipient";
 handler.handleInput(new InputEvent(mockConnection1, new StringData("/w " + recipientName + " Hello, world!")));
 verify(mockConnection1).send(argThat(new IsStringThatMatches("You whisper to recipient: Hello, world!")));
 verify(mockConnection2, never()).send(anyString());
 }

 @Test
 public void testHandleInputWithChallengeData() {
 // Test sending a dice roll result to all clients when a dice roll command is received from a client
 when(mockConnection1.send(any(String.class))).thenReturn(true);
 when(mockConnection2.send(any(String.class))).thenReturn(true);
 String rollCommand = "/roll 2d6 + 3";
 handler.handleInput(new InputEvent(mockConnection1, new ChallengeData(rollCommand)));
 assertEquals("Handler rolled 2d6 --> 5, 5", handler.getLastSystemMessage());
 verify(mockConnection1, times(1)).send(anyStringThatMatches(".*" + rollCommand + " --> 5, 5.*"));
 verify(mockConnection2, times(1)).send(anyStringThatMatches(".*" + rollCommand + " --> 5, 5.*"));

 // Test sending a dice roll result to a specific client when a dice roll command is received from a client with a recipient
 when(mockConnection1.send(anyString())).thenReturn(true);
 when(mockConnection2.send(anyString())).thenReturn(false);
 String recipientName = "recipient";
 handler.handleInput(new InputEvent(mockConnection1, new ChallengeData("/roll 2d6 + 3 /w " + recipientName)));
 verify(mockConnection1, times(1)).send(argThat(new IsStringThatMatches("You rolled 2d6 --> " + anyInt() + ", " + anyInt() + ".*")));
 verify(mockConnection2, never()).send(anyString());
 }

 @Test
 public void testHandleInputWithChallengeReqData() {
 // Test sending a challenge request to a client when a challenge request is received from a client
 when(mockConnection1.send(any(ChallengeReq.class))).thenReturn(true);
 when(mockConnection2.send(any(ChallengeReq.class))).thenReturn(true);
 String challengeRequestCommand = "/challenge hero1 hero2 20";
 handler.handleInput(new InputEvent(mockConnection1, new ChallengeReqData(challengeRequestCommand)));
 verify(mockConnection2, times(1)).send(any(ChallengeReq.class));
 }

 @Test
 public void testHandleInputWithHeroData() {
 // Test updating a client's hero when a hero update is received from a client
 when(mockConnection1.send(any(String.class))).thenReturn(true);
 when(mockConnection2.send(any(String.class))).thenReturn(true);
 Hero newHero = new Hero("newHero", 10, 10, 10, 10);
 handler.handleInput(new InputEvent(mockConnection1, new HeroData(newHero)));
 assertEquals(newHero.getName(), handler.getName(mockConnection1));
 verify(mockConnection1, times(1)).send(anyStringThatMatches(".*" + newHero.getName() + " updated his hero.*"));
 }

 @Test
 public void testIsGmConnected() {
 // Test returning false when no gamemaster is connected
 assertFalse(handler.isGmConnected());

 // Test returning true when a gamemaster is connected
 when(mockConnection1.getName()).thenReturn("Gamemaster");
 handler.addConnection(mockConnection1);
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMessageToAllClients() {
 // Test sending a system message to all clients
 String systemMessage = "System message";
 handler.sendAllSystemMsg(systemMessage);
 for (int i = 0; i < handler.clients.size(); i++) {
 assertEquals(systemMessage, handler.clients.elementAt(i).ce.getOutputBuffer().toString());
 }
 }

 @Test
 public void testSendSystemMessageToSpecificClient() {
 // Test sending a system message to a specific client
 when(mockConnection1.send(any(String.class))).thenReturn(true);
 String recipientName = "recipient";
 handler.sendToOneStr(mockConnection1, recipientName, "System message");
 verify(mockConnection1).send(argThat(new IsStringThatMatches("System message")));
 verify(mockConnection2, never()).send(anyString());
 }
}
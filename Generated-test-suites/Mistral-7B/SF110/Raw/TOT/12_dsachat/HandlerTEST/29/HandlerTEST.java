package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
private ConnectionEndpoint mockConnectionEndpoint;
private InputStream mockInput;
private OutputStream mockOutput;

 @Before
 public void setup() {
 handler = new Handler();
 mockConnectionEndpoint = mock(ConnectionEndpoint.class);
 mockInput = new ByteArrayInputStream(new byte[0]);
 mockOutput = new ByteArrayOutputStream();
 when(mockConnectionEndpoint.isConnected()).thenReturn(true);
 when(mockConnectionEndpoint.send(any(Object.class))).thenAnswer(i -> {
 try {
 i.getByteArrayOutputStream().writeTo(mockOutput);
 } catch (IOException e) {
 e.printStackTrace();
 }
 return null;
 });
 }

 @Test
 public void testAddConnection() {
 ConnectionEndpoint cl = mockConnectionEndpoint;
 Entry entry = new Entry();
 handler.addConnection(cl);
 assertEquals(1, handler.clients.size());
 verify(cl).send(any(UserEvent.class));
 }

 @Test
 public void testRemoveConnection() {
 ConnectionEndpoint cl = mockConnectionEndpoint;
 Entry entry = new Entry();
 handler.addConnection(cl);
 handler.removeConnection(cl);
 assertEquals(0, handler.clients.size());
 verify(cl, never()).send(any(UserEvent.class));
 }

 @Test
 public void testHandleInput_StringMessage() {
 String msg = "John: Hello World";
 InputEvent input = new InputEvent(mockConnectionEndpoint, msg);
 handler.handleInput(input);
 String expected = "*John: Hello World";
 assertTrue(handler.sendAllObj(new UserEvent(false, "John")).toString().equals(expected));
 }

 @Test
 public void testHandleInput_Challenge() {
 Challenge challenge = mock(Challenge.class);
 InputEvent input = new InputEvent(mockConnectionEndpoint, challenge);
 handler.handleInput(input);
 verify(mockConnectionEndpoint, times(1)).send(challenge.roll());
 }

 @Test
 public void testHandleInput_ChallReq() {
 ConnectionEndpoint targetConnection = mockConnectionEndpoint;
 ChallReq challReq = new ChallReq("Alice", "gamble");
 InputEvent input = new InputEvent(mockConnectionEndpoint, challReq);
 handler.handleInput(input);
 verify(targetConnection).send(challReq);
 }

 @Test
 public void testHandleInput_Hero() {
 Hero hero = new Hero("Bob");
 InputEvent input = new InputEvent(mockConnectionEndpoint, hero);
 handler.handleInput(input);
 String name = handler.updateHero(mockConnectionEndpoint, hero);
 assertEquals("Bob", name);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(mockConnectionEndpoint);
 handler.addConnection(new ConnectionEndpoint());
 handler.addConnection(new ConnectionEvent("Gamemaster"));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendHeros() {
 Vector<Hero> heroes = new Vector<>();
 heroes.add(new Hero("Alice"));
 heroes.add(new Hero("Bob"));
 handler.sendHeros(mockConnectionEndpoint);
 assertEquals(heroes.toString(), handler.handleInput(new InputEvent(mockConnectionEndpoint, heroes)).toString());
 }
}
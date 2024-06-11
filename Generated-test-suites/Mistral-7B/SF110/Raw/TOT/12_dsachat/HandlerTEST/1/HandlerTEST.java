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
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;
import dsachat.share.hero.UserEvent;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private ConnectionEndpoint con1, con2;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = new Vector<Entry>();
 con1 = mock(ConnectionEndpoint.class);
 con2 = mock(ConnectionEndpoint.class);
 }

 @Test
 public void testAddConnection() {
 Entry entry1 = new Entry();
 when(con1.getClientPort()).thenReturn(1);
 handler.addConnection(con1);
 clients.add(entry1);
 assertEquals(1, clients.size());
 entry1.name = "User1";
 verify(con1).send(isA(UserEvent.class));

 Entry entry2 = new Entry();
 when(con2.getClientPort()).thenReturn(2);
 handler.addConnection(con2);
 clients.add(entry2);
 assertEquals(2, clients.size());
 verify(con1, times(clients.size())).send(isA(UserEvent.class));
 }

 @Test
 public void testRemoveConnection() {
 Entry entry1 = new Entry();
 when(con1.getClientPort()).thenReturn(1);
 clients.add(entry1);
 handler.addConnection(con1);
 handler.removeConnection(con1);
 assertEquals(0, clients.size());

 Entry entry2 = new Entry();
 when(con2.getClientPort()).thenReturn(2);
 clients.add(entry2);
 handler.addConnection(con2);
 handler.removeConnection(con2);
 assertEquals(0, clients.size());
 }

 @Test
 public void testIsGmConnected() {
 Entry gmEntry = new Entry();
 gmEntry.name = "Gamemaster";
 clients.add(gmEntry);
 handler.addConnection(gmEntry);
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnected_notConnected() {
 clients.clear();
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMsg() throws IOException {
 String message = "System Message";
 InputStream inputStream = new ByteArrayInputStream(message.getBytes());
 OutputStream outputStream = new ByteArrayOutputStream();
 when(con1.send(eq(String.class))).thenReturn(outputStream);
 handler.sendAllSystemMsg("System Message");
 verify(con1).send(eq("System Message"));
 }

 @Test
 public void testHandleInput_stringMessage() throws IOException {
 String message = "User1: Hello";
 InputStream inputStream = new ByteArrayInputStream(message.getBytes());
 OutputStream outputStream = mock(OutputStream.class);
 InputEvent event = mock(InputEvent.class);
 when(event.getData()).thenReturn(message);
 when(event.getSource()).thenReturn(con1);
 handler.handleInput(event);
 verify(con1, times(2)).send(isA(String.class));
 }

 @Test
 public void testHandleInput_challenge() throws IOException {
 Challenge challenge = mock(Challenge.class);
 when(challenge.getTo()).thenReturn(null);
 InputStream inputStream = new ByteArrayInputStream(challenge.getClass().getName().getBytes());
 OutputStream outputStream1 = new ByteArrayOutputStream();
 OutputStream outputStream2 = new ByteArrayOutputStream();
 when(con1.send(eq(String.class))).thenReturn(outputStream1);
 when(con2.send(eq(String.class))).thenReturn(outputStream2);
 InputEvent event = mock(InputEvent.class);
 when(event.getData()).thenReturn(challenge);
 when(event.getSource()).thenReturn(con1);
 handler.handleInput(event);
 verify(con1).send(eq("You rolled a dice."));
 verify(con2).send(eq("User1 rolled a dice."));
 }

 @Test
 public void testHandleInput_challengeWithTarget() throws IOException {
 Challenge challenge = mock(Challenge.class);
 String target = "User2";
 when(challenge.getTo()).thenReturn(target);
 InputStream inputStream = new ByteArrayInputStream(challenge.getClass().getName().getBytes());
 OutputStream outputStream1 = new ByteArrayOutputStream();
 OutputStream outputStream2 = new ByteArrayOutputStream();
 when(con1.send(eq(String.class))).thenReturn(outputStream1);
 when(con2.send(eq(String.class))).thenReturn(outputStream2);
 InputEvent event = mock(InputEvent.class);
 when(event.getData()).thenReturn(challenge);
 when(event.getSource()).thenReturn(con1);
 handler.handleInput(event);
 verify(con1).send(eq("You rolled a dice."));
 verify(con2).send(eq(target + " rolled a dice."));
 }

 @Test
 public void testHandleInput_challReq() throws IOException {
 ChallReq challengeReq = mock(ChallReq.class);
 String target = "User2";
 when(challengeReq.getTo()).thenReturn(target);
 InputStream inputStream = new ByteArrayInputStream(challengeReq.getClass().getName().getBytes());
 when(con2.send(eq(ChallReq.class))).thenReturn(outputStream1);
 InputEvent event = mock(InputEvent.class);
 when(event.getData()).thenReturn(challengeReq);
 when(event.getSource()).thenReturn(con1);
 handler.handleInput(event);
 verify(con2).send(eq(challengeReq));
 }

 @Test
 public void testHandleInput_hero() throws IOException {
 Hero hero = new Hero("User1", Arrays.asList("Sword", "Shield"));
 InputStream inputStream = new ByteArrayInputStream(hero.getClass().getName().getBytes());
 InputEvent event = mock(InputEvent.class);
 when(event.getData()).thenReturn(hero);
 when(event.getSource()).thenReturn(con1);
 clients.add(new Entry());
 clients.get(0).hero = null;
 handler.handleInput(event);
 assertEquals("User1", clients.get(0).name);
 verify(con1).send("Welcome User1");
 }

 @Test
 public void testHandleInput_gmConnect() {
 String name = "Gamemaster";
 InputEvent event = new UserEvent(true, name);
 handler.handleInput(event);
 assertEquals(name, handler.getName(con2));
 }
}
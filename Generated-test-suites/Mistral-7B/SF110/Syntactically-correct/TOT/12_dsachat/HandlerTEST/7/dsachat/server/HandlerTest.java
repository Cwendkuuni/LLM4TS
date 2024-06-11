package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection;
 private OutputStream outputStreamMock;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 connection = mock(ConnectionEndpoint.class);
 outputStreamMock = mock(OutputStream.class);
 clients = new Vector<>();
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 when(connection.getClientPort()).thenReturn(123);
 when(connection.isConnected()).thenReturn(false);
 when(connection.send(any(UserEvent.class))).thenReturn(true);

 handler.addConnection(connection);

 assertEquals(1, clients.size());
 verify(connection, times(clients.size())).send(any(UserEvent.class));
 }

 @Test(expected = IOException.class)
 public void testAddConnectionWithIOException() {
 Entry entry = new Entry();
 when(connection.getClientPort()).thenReturn(123);
 when(connection.isConnected()).thenThrow(new IOException());

 handler.addConnection(connection);
 }

 @Test
 public void testRemoveConnection() {
 Entry entry = new Entry();
 when(connection.getClientPort()).thenReturn(123);
 when(clients.size()).thenReturn(2);
 clients.add(entry);
 clients.add(entry);
 String name = "testName";
 handler.removeConnection(connection);
 assertEquals(1, clients.size());
 verify(connection, times(1)).send(any(UserEvent.class));
 verify(clients.elementAt(0), times(1)).ce.equals(connection);
 }

 @Test
 public void testSendAllSystemMsg() {
 String message = "testMessage";
 handler.sendAllSystemMsg(message);
 for (Entry entry : clients) {
 verify(entry.ce, times(1)).send(message);
 }
 }

 @Test
 public void testHandleInputWithStringMessage() {
 String message = "testMessage";
 handler.handleInput(new InputEvent(message, connection));
 for (Entry entry : clients) {
 verify(entry.ce, times(1)).send(message);
 }
 }

 @Test
 public void testHandleInputWithChallenge() {
 Challenge challenge = mock(Challenge.class);
 when(challenge.getTo()).thenReturn("testTo");
 when(challenge.isSilent()).thenReturn(false);
 when(challenge.roll()).thenReturn("testRoll");

 handler.handleInput(new InputEvent(challenge, connection));
 verify(connection, times(1)).send("testRoll");
 verify(clients.elementAt(0), times(1)).ce.send("testRoll");
 }

 @Test
 public void testHandleInputWithChallReq() {
 String to = "testTo";
 ChallReq challReq = new ChallReq(to);
 handler.handleInput(new InputEvent(challReq, connection));
 verify(connection.getByName(to)).send(challReq);
 }

 @Test
 public void testHandleInputWithHero() {
 Hero hero = new Hero("testName", new String[]{"testSkill1", "testSkill2"});
 Entry entry = new Entry();
 when(entry.ce.getClientPort()).thenReturn(123);
 when(entry.hero).thenReturn(null);
 handler.handleInput(new InputEvent(hero, connection));
 assertEquals("testName", handler.getName(connection));
 assertEquals(hero, clients.elementAt(0).hero);
 verify(connection, times(1)).send("Welcome testName");
 }

 @Test
 public void testHandleInputWithUserEventWhenGMIsConnected() {
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 handler.handleInput(new InputEvent(userEvent, connection));
 assertEquals("Gamemaster", handler.getName(connection));
 }

 @Test
 public void testSendAllHeros() {
 List<Hero> heroes = Arrays.asList(new Hero("testHero1"), new Hero("testHero2"));
 handler.sendHeros(connection);
 String expectedMessage = "[" + heroes.toString() + "]";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 when(connection.send(any(String.class))).thenAnswer(i -> {outputStream.write(i.toString().getBytes()); return null;});
 handler.handleInput(new InputEvent(new String(""), connection));
 assertEquals(expectedMessage, outputStream.toString());
 }

}
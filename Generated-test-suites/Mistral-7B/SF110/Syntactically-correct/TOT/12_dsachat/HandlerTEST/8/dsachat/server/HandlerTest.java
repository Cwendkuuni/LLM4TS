package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection1;
 private ConnectionEndpoint mockConnection2;
 private OutputStream mockOutputStream;
 private ByteArrayOutputStream mockByteArrayOutputStream;

 @Before
 public void setUp() {
 handler = new Handler();
 mockConnection1 = mock(ConnectionEndpoint.class);
 mockConnection2 = mock(ConnectionEndpoint.class);
 mockOutputStream = mock(OutputStream.class);
 mockByteArrayOutputStream = new ByteArrayOutputStream();
 when(mockConnection1.getClientPort()).thenReturn(1);
 when(mockConnection1.send(anyObject())).thenAnswer(invocation -> {
 try {
 mockOutputStream.write(invocation.getArgument(0).toString().getBytes());
 } catch (IOException e) {
 e.printStackTrace();
 }
 return null;
 });
 when(mockConnection1.isConnected()).thenReturn(true);
 when(mockConnection2.getClientPort()).thenReturn(2);
 when(mockConnection2.send(anyObject())).thenAnswer(invocation -> {
 try {
 mockOutputStream.write(invocation.getArgument(0).toString().getBytes());
 } catch (IOException e) {
 e.printStackTrace();
 }
 return null;
 });
 when(mockConnection2.isConnected()).thenReturn(true);
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 handler.addConnection(mockConnection1);
 assertTrue(handler.clients.contains(entry));
 assertEquals("", entry.name);
 handler.addConnection(mockConnection2);
 assertTrue(handler.clients.size() == 2);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(mockConnection1);
 handler.addConnection(mockConnection2);
 handler.removeConnection(mockConnection1);
 assertFalse(handler.clients.contains(new Entry().ce));
 }

 @Test
 public void testHandleInput_String() {
 String msg = "/roll 2d6";
 handler.handleInput(new InputEvent("test", new Object[]{msg}, mockConnection1));
 String expected = handler.getName(mockConnection1) + " rolled 2d6 --> ";
 String actual = handler.sendAllSystemMsg(expected).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleInput_Challenge() {
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent("test", new Object[]{challenge}, mockConnection1));
 String expected = handler.getName(mockConnection1) + ": " + challenge.roll();
 String actual = mockConnection1.send(expected).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleInput_ChallReq() {
 ChallReq challReq = new ChallReq("test", new Hero("test"));
 handler.handleInput(new InputEvent("test", new Object[]{challReq}, mockConnection2));
 handler.handleInput(new InputEvent("test", new Object[]{challReq}, mockConnection1));
 String expectedName1 = "test";
 String expectedName2 = "test";
 assertEquals(expectedName1, handler.updateHero(mockConnection2, null).getName());
 assertEquals(expectedName2, handler.updateHero(mockConnection1, new Hero("test")).getName());
 }

 @Test
 public void testHandleInput_UserEvent() {
 UserEvent userEvent = new UserEvent(true, "test");
 handler.handleInput(new InputEvent("test", new Object[]{userEvent}, mockConnection1));
 assertEquals("Gamemaster", handler.getName(mockConnection1));
 }

 @Test
 public void testSendAllSystemMsg() {
 String msg = "test";
 handler.sendAllSystemMsg(msg);
 for (int i = 0; i < handler.clients.size(); ++i) {
 assertEquals(msg, handler.clients.elementAt(i).ce.send(msg).toString());
 }
 }

 @Test
 public void testWhisper() {
 String msg = "/w test test whisper";
 handler.handleInput(new InputEvent("test", new Object[]{msg}, mockConnection1));
 handler.handleInput(new InputEvent("test", new Object[]{msg}, mockConnection2));
 String expectedName1 = handler.getName(mockConnection1);
 String expectedName2 = handler.getName(mockConnection2);
 String expected = expectedName1 + " whispers to test: whisper";
 String actualName1 = handler.sendOneSystemMsg(mockConnection1, expected).toString();
 String actualName2 = handler.sendOneSystemMsg(mockConnection2, expected).toString();
 assertEquals(expectedName1, actualName1.split(": ")[0]);
 assertEquals(expectedName2, actualName2.split(": ")[0]);
 }

 @Test
 public void testRollDice() {
 String msg = "/roll 1d6+1";
 handler.handleInput(new InputEvent("test", new Object[]{msg}, mockConnection1));
 String expected = handler.getName(mockConnection1) + " rolled 1d6+1 --> ";
 String actual = handler.sendAllSystemMsg(expected).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testRollDiceWithMultipleDice() {
 String msg = "/roll 2d6";
 handler.handleInput(new InputEvent("test", new Object[]{msg}, mockConnection1));
 String expected = handler.getName(mockConnection1) + " rolled 2d6 --> ";
 String actual = handler.sendAllSystemMsg(expected).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testRollDiceInvalidFormat() {
 String msg = "/roll d6";
 handler.handleInput(new InputEvent("test", new Object[]{msg}, mockConnection1));
 String expected = handler.getName(mockConnection1) + " rolled d6 not a valid format";
 String actual = handler.sendAllSystemMsg(expected).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSendAllObj() {
 Vector<Hero> heroes = new Vector<Hero>();
 heroes.add(new Hero("test1"));
 heroes.add(new Hero("test2"));
 handler.handleInput(new InputEvent("test", new Object[]{heroes}, mockConnection1));
 assertEquals(heroes, handler.clients.elementAt(0).ce.receive(Vector.class));
 }
}
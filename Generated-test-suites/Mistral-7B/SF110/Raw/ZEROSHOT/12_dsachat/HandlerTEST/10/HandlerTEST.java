package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.HandlerTest;
import dsachat.share.Challenge;
import dsachat.share.Challenge.dice;
import dsachat.share.ChallReq;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint1, connectionEndpoint2;
 private Vector<Entry> clients;

 @Before
 public void setUp() throws IOException {
 handler = new Handler();
 clients = new Vector<Entry>();
 connectionEndpoint1 = new ConnectionEndpoint(1, null);
 connectionEndpoint2 = new ConnectionEndpoint(2, null);
 handler.addConnection(connectionEndpoint1);
 handler.addConnection(connectionEndpoint2);
 }

 @After
 public void tearDown() {
 handler.removeConnection(connectionEndpoint1);
 handler.removeConnection(connectionEndpoint2);
 }

 @Test
 public void testAddConnection() {
 Entry entry1 = new Entry();
 Entry entry2 = new Entry();
 assertEquals(0, handler.clients.size());
 handler.addConnection(connectionEndpoint1);
 handler.addConnection(connectionEndpoint2);
 assertEquals(2, handler.clients.size());
 assertEquals(connectionEndpoint1.getClientPort(), handler.clients.elementAt(0).ce.getClientPort());
 assertEquals(connectionEndpoint2.getClientPort(), handler.clients.elementAt(1).ce.getClientPort());
 }

 @Test
 public void testRemoveConnection() {
 String name = null;
 handler.removeConnection(connectionEndpoint1);
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce.getClientPort() == connectionEndpoint1.getClientPort()) {
 name = handler.clients.elementAt(i).name;
 break;
 }
 }
 assertTrue(name != null);
 handler.removeConnection(connectionEndpoint2);
 assertEquals(0, handler.clients.size());
 }

 @Test
 public void testHandleInputString() throws IOException {
 String msg = "/wuser1 hello";
 handler.handleInput(new InputEvent(connectionEndpoint1, new String(msg)));
 String expected = "You whisper to user1: hello";
 StringWriter stringWriter = new StringWriter();
 PrintWriter printWriter = new PrintWriter(stringWriter);
 String actual = handler.getName(connectionEndpoint1) + ": " + msg;
 printWriter.println(expected);
 printWriter.flush();
 String result = stringWriter.getBuffer().toString();
 assertEquals(expected, result);
 }

 @Test
 public void testHandleInputChallenge() throws IOException {
 ConnectionEndpoint con = connectionEndpoint1;
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(con, challenge));
 String send = challenge.roll();
 String expected = handler.getName(con) + ": rolled a " + challenge.rollResult() + " with " + challenge.getSides() + " sides";
 StringWriter stringWriter = new StringWriter();
 PrintWriter printWriter = new StringWriter(stringWriter);
 printWriter.println(expected);
 printWriter.flush();
 String result = stringWriter.getBuffer().toString();
 assertEquals(expected, result);
 }

 @Test
 public void testHandleInputChallReq() throws IOException {
 ConnectionEndpoint from = connectionEndpoint1;
 ConnectionEndpoint to = connectionEndpoint2;
 ChallReq challReq = new ChallReq("user1", dice.D6, 3);
 handler.handleInput(new InputEvent(from, challReq));
 handler.handleInput(new InputEvent(to, challReq));
 String expected = "user1 challenges user2 to a roll of a D6, 3 times";
 StringWriter stringWriter = new StringWriter();
 PrintWriter printWriter = new StringWriter(stringWriter);
 printWriter.println(expected);
 printWriter.flush();
 String result = stringWriter.getBuffer().toString();
 assertEquals(expected, result);
 }

 @Test
 public void testHandleInputHero() throws IOException {
 ConnectionEndpoint con = connectionEndpoint1;
 Hero hero = new Hero("user1", "Warrior", 10, 20, 5);
 handler.handleInput(new InputEvent(con, hero));
 String expected = "user1 updated his hero";
 String result = "";
 boolean expectedResult = true;
 try {
 result = handler.getName(con);
 } catch (Exception e) {
 e.printStackTrace();
 expectedResult = false;
 }
 assertTrue(expectedResult);
 assertEquals(expected, result);
 }

 @Test
 public void testHandleInputGmConnect() throws IOException {
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 handler.handleInput(new InputEvent(connectionEndpoint1, userEvent));
 String expected = "Gamemaster connected";
 String result = "";
 boolean expectedResult = true;
 try {
 result = handler.getName(connectionEndpoint1);
 } catch (Exception e) {
 e.printStackTrace();
 expectedResult = false;
 }
 assertTrue(expectedResult);
 assertEquals(expected, result);
 }

 @Test
 public void testSendAllSystemMsg() throws IOException {
 String msg = "system: test message";
 handler.sendAllSystemMsg(msg);
 for (int i = 0; i < handler.clients.size(); ++i) {
 try {
 String actual = handler.clients.elementAt(i).ce.receiveString();
 assertEquals(msg, actual);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testWhisper() throws IOException {
 String msg = "/wuser1 user2 hello";
 handler.handleInput(new InputEvent(connectionEndpoint1, new String(msg)));
 String expectedFrom = handler.getName(connectionEndpoint1);
 String expectedTo = handler.getName(connectionEndpoint2);
 String expected = "user1 whispers to user2: hello";
 StringWriter stringWriter = new StringWriter();
 PrintWriter printWriter = new StringWriter(stringWriter);
 printWriter.println(expected);
 printWriter.flush();
 String resultFrom = "";
 String resultTo = "";
 boolean expectedResult = true;
 try {
 resultFrom = handler.clients.elementAt(0).ce.receiveString();
 resultTo = handler.clients.elementAt(1).ce.receiveString();
 } catch (IOException e) {
 e.printStackTrace();
 expectedResult = false;
 }
 assertTrue(expectedResult);
 assertEquals(expectedFrom, handler.getName(connectionEndpoint1));
 assertEquals(expectedTo, handler.getName(connectionEndpoint2));
 assertEquals(expected, resultFrom.substring(0, resultFrom.length() - 1) + resultTo);
 }

 @Test
 public void testRollDice() throws IOException {
 String msg = "/roll 2d6+3";
 handler.handleInput(new InputEvent(connectionEndpoint1, new String(msg)));
 String expected = handler.getName(connectionEndpoint1) + " rolled 2d6+3 --> ";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream output = new ObjectOutputStream(outputStream);
 output.writeObject(handler.clients.elementAt(0).ce);
 ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
 int sum = 0;
 for (int i = 0; i < 2; ++i) {
 sum += dice.roll(6);
 }
 sum += 3;
 assertEquals(expected + sum, handler.clients.elementAt(0).ce.receiveString());
 }
}
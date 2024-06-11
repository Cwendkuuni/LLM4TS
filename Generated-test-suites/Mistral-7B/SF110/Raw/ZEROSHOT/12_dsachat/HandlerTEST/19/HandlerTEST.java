package dsachat.server.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.*;
import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionMock;
 private Hero heroMock;
 private ChallReq challReqMock;
 private Chall challengeMock;
 private UserEvent userEventMock;
 private Entry entryMock;

 @Before
 public void setup() {
 handler = new Handler();
 connectionMock = new ConnectionEndpoint();
 heroMock = new Hero("TestHero");
 challReqMock = new ChallReq("TestUser", "TestUser2", "TestCommand");
 challengeMock = new Challenge(5, "TestUser", "TestUser2", true);
 userEventMock = new UserEvent(true, "TestUser");
 entryMock = new Entry();
 entryMock.ce = connectionMock;
 }

 @After
 public void teardown() {
 handler = null;
 connectionMock = null;
 heroMock = null;
 challReqMock = null;
 challengeMock = null;
 userEventMock = null;
 entryMock = null;
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionMock);
 assertTrue(handler.clients.contains(entryMock));
 }

 @Test(expected = Exception.class)
 public void testAddConnectionInvalidConnection() {
 handler.addConnection(null);
 fail("Exception should be thrown when adding null connection");
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionMock);
 handler.removeConnection(connectionMock);
 assertFalse(handler.clients.contains(entryMock));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveConnectionNullConnection() {
 handler.removeConnection(null);
 fail("NullPointerException should be thrown when removing null connection");
 }

 @Test
 public void testWhisper() {
 handler.addConnection(connectionMock);
 handler.addConnection(new ConnectionEndpoint());
 handler.whisper(connectionMock, "TestUser TestMessage");
 String expectedMessage = "You whisper to TestUser: TestMessage";
 String actualMessage = handler.getName(connectionMock) + ": " + "TestMessage";
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testRollDiceWithNoMod() {
 handler.addConnection(connectionMock);
 handler.rollDice(connectionMock, "4d6");
 int expectedSum = 4 * Challenge.dice(6);
 int actualSum = -1;
 try {
 actualSum = Challenge.stringToIntArray(handler.getName(connectionMock) + " rolled 4d6 --> " + handler.getName(connectionMock) + " [" + Challenge.dice(6) + ", " + Challenge.dice(6) + ", " + Challenge.dice(6) + ", " + Challenge.dice(6) + "]")[0];
 } catch (NumberFormatException e) {
 fail("Expected integer array instead of string");
 }
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testRollDiceWithMod() {
 handler.addConnection(connectionMock);
 handler.rollDice(connectionMock, "3d6+2");
 int expectedSum = 3 * Challenge.dice(6) + 2;
 int actualSum = -1;
 try {
 actualSum = Challenge.stringToIntArray(handler.getName(connectionMock) + " rolled 3d6+2 --> " + handler.getName(connectionMock) + " [" + Challenge.dice(6) + ", " + Challenge.dice(6) + ", " + Challenge.dice(6) + "]")[0];
 } catch (NumberFormatException e) {
 fail("Expected integer array instead of string");
 }
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testSendHeros() {
 handler.addConnection(connectionMock);
 handler.addConnection(new ConnectionEndpoint());
 handler.handler.sendHeros(connectionMock);
 Hero[] expectedHeros = {heroMock};
 Hero[] actualHeros = null;
 try {
 actualHeros = (Hero[])connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedHeros.length, actualHeros.length);
 for (int i = 0; i < expectedHeros.length; i++) {
 assertEquals(expectedHeros[i].getName(), actualHeros[i].getName());
 }
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint());
 handler.addConnection(new ConnectionEndpoint());
 handler.handler.addConnection(new ConnectionEndpoint());
 handler.handler.handler.clients.get(2).name = "Gamemaster";
 assertTrue(handler.handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 String expectedMessage = "TestMessage";
 handler.addConnection(connectionMock);
 handler.handler.sendAllSystemMsg(expectedMessage);
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testHandleInputString() {
 String expectedMessage = "TestMessage";
 String input = "/tell TestUser TestMessage";
 handler.addConnection(connectionMock);
 handler.handler.handleInput(new InputEvent(connectionMock, new StringInputStream(new ByteArrayInputStream(input.getBytes()))));
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage.substring(2)); // Remove prefix "TestUser: "
 }

 @Test
 public void testHandleInputChallenge() {
 challengeMock.setTo("TestUser");
 String expectedMessage = "TestUser rolled " + challengeMock.getSides() + "d" + challengeMock.getSides() + " --> " + challengeMock.roll();
 handler.addConnection(connectionMock);
 handler.addConnection(new ConnectionEndpoint());
 handler.handler.handleInput(new InputEvent(connectionMock, new ByteArrayInputStream(challengeMock.toByteArray())));
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testHandleInputChallReq() {
 String expectedMessage = "TestUser challenged TestUser2 to a dice roll";
 handler.addConnection(connectionMock);
 handler.addConnection(new ConnectionEndpoint());
 handler.handler.handleInput(new InputEvent(connectionMock, new ByteArrayInputStream(challReqMock.toByteArray())));
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testHandleInputHero() {
 String expectedMessage = "TestUser updated his hero";
 Hero newHero = new Hero("NewTestHero");
 handler.addConnection(connectionMock);
 handler.handler.handleInput(new InputEvent(connectionMock, new ByteArrayInputStream(newHero.toByteArray())));
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testHandleInputUserEvent() {
 String expectedMessage = "TestUser: TestMessage";
 UserEvent userEvent = new UserEvent(false, "TestUser", "TestMessage");
 handler.addConnection(connectionMock);
 handler.handler.handleInput(new InputEvent(connectionMock, new ByteArrayInputStream(userEvent.toByteArray())));
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testHandleInputGmConnection() {
 String expectedMessage = "Gamemaster connected";
 handler.addConnection(new ConnectionEndpoint());
 handler.handler.addConnection(new ConnectionEndpoint());
 handler.handler.handler.clients.get(1).name = "Gamemaster";
 String input = "/connect";
 handler.handler.handleInput(new InputEvent(connectionMock, new ByteArrayInputStream(input.getBytes())));
 String actualMessage = "";
 try {
 actualMessage = (String)connectionMock.receive();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }
 assertEquals(expectedMessage, actualMessage);
 }
}
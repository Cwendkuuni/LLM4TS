package dsachat.server.test;

import static org.junit.Assert.*;

import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private ConnectionEndpoint anotherConnectionEndpoint;
 private Hero hero;
 private ChallReq challReq;
 private Chall challenge;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint(1234);
 anotherConnectionEndpoint = new ConnectionEndpoint(5678);
 hero = new Hero("testHero");
 challenge = new Challenge(1, 6);
 challReq = new ChallReq("testUser", challenge);
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionEndpoint);
 assertTrue(handler.clients.size() == 1);
 }

 @Test(expected = IOException.class)
 public void testAddConnectionWithNullConnection() {
 handler.addConnection(null);
 fail("Should throw IOException");
 }

 @Test
 public void testAddConnectionWithAlreadyConnectedConnection() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint);
 assertEquals(handler.clients.size(), 1);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertEquals(handler.clients.size(), 0);
 }

 @Test(expected = IOException.class)
 public void testRemoveConnectionWithNullConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(null);
 fail("Should throw IOException");
 }

 @Test
 public void testRemoveConnectionWithNonExistentConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(anotherConnectionEndpoint);
 assertEquals(handler.clients.size(), 1);
 }

 @Test
 public void testHandleInputString() {
 String message = "/whisper testUser hello";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 String expected = "You whisper to testUser: hello";
 String actual = handler.getLastSystemMsg();
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleInputChallenge() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("1d20+5\n").getBytes()), connectionEndpoint));
 int expectedSum = Challenge.dice(6) + 5;
 int actualSum = handler.rollResult.get(connectionEndpoint);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testHandleInputChallengeWithSilent() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 challenge.setSilent(true);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("1d20\n").getBytes()), connectionEndpoint));
 int expectedSum = Challenge.dice(6);
 int actualSum = handler.rollResult.get(anotherConnectionEndpoint);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testHandleInputChallReq() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/challenge testUser 1d20\n").getBytes()), anotherConnectionEndpoint));
 assertEquals(handler.challenges.size(), 1);
 }

 @Test
 public void testHandleInputHero() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/hero testUser " + hero.toString() + "\n").getBytes()), connectionEndpoint));
 String expectedName = hero.getName();
 String actualName = handler.getName(connectionEndpoint);
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testHandleInputGmConnected() {
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/gm\n").getBytes()), connectionEndpoint));
 String expectedName = "Gamemaster";
 String actualName = handler.getName(connectionEndpoint);
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testSendHeros() {
 Vector<Hero> heroes = new Vector<>();
 heroes.add(hero);
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/getheros\n").getBytes()), connectionEndpoint));
 List<Object> actualHeros = new ArrayList<>(handler.heroList.get(connectionEndpoint));
 List<Object> expectedHeros = new ArrayList<>(heroes);
 assertEquals(expectedHeros, actualHeros);
 }

 @Test
 public void testIsGmConnected() {
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/gm\n").getBytes()), connectionEndpoint));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testSendOneSystemMsg() {
 String message = "System message";
 handler.sendOneSystemMsg(connectionEndpoint, message);
 String expected = handler.getLastSystemMsg();
 assertEquals(message, expected);
 }

 @Test
 public void testSendAllSystemMsg() {
 String message = "System message";
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 handler.sendAllSystemMsg(message);
 String expected = message;
 String actualConnection1 = handler.getLastSystemMsg();
 String actualConnection2 = handler.getLastSystemMsg();
 assertEquals(expected, actualConnection1);
 assertEquals(expected, actualConnection2);
 }

 @Test
 public void testWhisper() {
 String message = "/whisper testUser testUser hello";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 String expected = "testUser whispers to testUser: hello";
 String actual = handler.getLastSystemMsg();
 assertEquals(expected, actual);
 }

 @Test
 public void testRollDice() {
 String message = "1d20";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 int expectedSum = Challenge.dice(6);
 int actualSum = handler.rollResult.get(connectionEndpoint);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testRollDiceWithDiceCount() {
 String message = "3d20";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 List<Integer> expectedSums = new ArrayList<>();
 for (int i = 0; i < 3; i++) {
 expectedSums.add(Challenge.dice(6));
 }
 List<Integer> actualSums = new ArrayList<>(handler.rollResult.get(connectionEndpoint));
 assertEquals(expectedSums, actualSums);
 }

 @Test
 public void testRollDiceWithModifiers() {
 String message = "2d6+3";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 int expectedSum = Challenge.dice(6) + 3;
 int actualSum = handler.rollResult.get(connectionEndpoint);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testRollDiceWithModifiersAndDiceCount() {
 String message = "3d6+4";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 List<Integer> expectedSums = new ArrayList<>();
 for (int i = 0; i < 3; i++) {
 expectedSums.add(Challenge.dice(6) + 4);
 }
 List<Integer> actualSums = new ArrayList<>(handler.rollResult.get(connectionEndpoint));
 assertEquals(expectedSums, actualSums);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRollDiceInvalidFormat() {
 String message = "invalidCommand";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testRollDiceWithNullConnection() {
 String message = "1d20";
 handler.handleInput(new InputEvent(null, connectionEndpoint));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testSendToOneStr() {
 String message = "User1: hello User2";
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 String expected = "User1: hello User2";
 String actual = handler.getLastSentMessage(connectionEndpoint);
 assertEquals(expected, actual);
 }

 @Test
 public void testSendToOneStrWithNullConnection() {
 String message = "User1: hello User2";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), null));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testSendToOneStrWithNullMessage() {
 String message = null;
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testSendToOneStrWithNullRecipient() {
 String message = "User1: hello User2";
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), null));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testSendToOneStrWithNullRecipientAndNullMessage() {
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("").getBytes()), null));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testUpdateHero() {
 String name = "testUser";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/hero " + name + " " + hero.toString() + "\n").getBytes()), connectionEndpoint));
 String actualName = handler.getName(connectionEndpoint);
 assertEquals(name, actualName);
 }

 @Test
 public void testSendAllStr() {
 String message = "This is a test message";
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 String expected = handler.getLastSentMessage(connectionEndpoint);
 String actualConnection1 = handler.getLastSentMessage(connectionEndpoint);
 String actualConnection2 = handler.getLastSentMessage(anotherConnectionEndpoint);
 assertEquals(expected, actualConnection1);
 assertEquals(expected, actualConnection2);
 }

 @Test
 public void testSendAllObj() {
 Vector<Object> expected = new Vector<>(Arrays.asList(hero, hero, hero));
 handler.addConnection(connectionEndpoint);
 handler.addConnection(anotherConnectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/getheros\n").getBytes()), connectionEndpoint));
 Vector<Object> actual = handler.heroList.get(connectionEndpoint);
 assertEquals(expected, actual);
 }

 @Test
 public void testSendAllObjWithNullConnection() {
 Vector<Object> expected = new Vector<>(Arrays.asList(hero, hero, hero));
 handler.handleInput(new InputEvent(null, connectionEndpoint));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetName() {
 String name = "testUser";
 handler.addConnection(new ConnectionEndpoint(1234, name));
 String actual = handler.getName(connectionEndpoint);
 assertEquals(name, actual);
 }

 @Test
 public void testGetNameWithNullConnection() {
 String name = "testUser";
 handler.addConnection(new ConnectionEndpoint(1234, name));
 handler.removeConnection(connectionEndpoint);
 String actual = handler.getName(connectionEndpoint);
 assertNull(actual);
 }

 @Test
 public void testGetLastSentMessage() {
 String message = "test message";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), connectionEndpoint));
 String actual = handler.getLastSentMessage(connectionEndpoint);
 assertEquals(message, actual);
 }

 @Test
 public void testGetLastSentMessageWithNullConnection() {
 String message = "test message";
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(message.getBytes()), null));
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetLastSystemMsg() {
 String message = "test system message";
 handler.sendAllSystemMsg(message);
 String actual = handler.getLastSystemMsg();
 assertEquals(message, actual);
 }

 @Test
 public void testGetLastSystemMsgWithEmptyList() {
 handler.sendAllSystemMsg(null);
 assertNull(handler.getLastSystemMsg());
 }

 @Test
 public void testRollResult() {
 int expectedSum = Challenge.dice(6) + Challenge.dice(6);
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("2d6\n").getBytes()), connectionEndpoint));
 int actualSum = handler.rollResult.get(connectionEndpoint);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testRollResultWithEmptyList() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("2d6\n").getBytes()), connectionEndpoint));
 handler.rollResult.remove(connectionEndpoint);
 int actualSum = handler.rollResult.get(connectionEndpoint);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testHeroList() {
 Vector<Object> expected = new Vector<>(Arrays.asList(hero, hero, hero));
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/getheros\n").getBytes()), connectionEndpoint));
 Vector<Object> actual = handler.heroList.get(connectionEndpoint);
 assertEquals(expected, actual);
 }

 @Test
 public void testHeroListWithEmptyList() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/getheros\n").getBytes()), connectionEndpoint));
 handler.heroList.remove(connectionEndpoint);
 Vector<Object> actual = handler.heroList.get(connectionEndpoint);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testChallenges() {
 Vector<Challenge> expected = new Vector<>(Arrays.asList(challenge, challenge, challenge));
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/challenge testUser 1d20\n").getBytes()), connectionEndpoint));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/challenge testUser 1d20\n").getBytes()), connectionEndpoint));
 Vector<Challenge> actual = handler.challenges.get(connectionEndpoint);
 assertEquals(expected, actual);
 }

 @Test
 public void testChallengesWithEmptyList() {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new String("/challenge testUser 1d20\n").getBytes()), connectionEndpoint));
 handler.challenges.remove(connectionEndpoint);
 Vector<Challenge> actual = handler.challenges.get(connectionEndpoint);
 fail("Should throw NullPointerException");
 }

}
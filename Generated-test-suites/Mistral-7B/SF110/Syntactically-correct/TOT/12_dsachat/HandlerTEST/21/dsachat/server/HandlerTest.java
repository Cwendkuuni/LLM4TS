package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.junit.*;
import org.junit.rules.*;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection;
 private Entry mockEntry;
 private ConnectionEndpoint anotherConnection;
 private Hero hero;
 private UserEvent userEvent;
 private UserEvent gmUserEvent;
 private ChallReq challReq;
 private Challenge challenge;
 private Vector<Hero> heroesVector;

 @Before
 public void setup() {
 handler = new Handler();
 mockConnection = mock(ConnectionEndpoint.class);
 mockEntry = new Entry();
 mockEntry.ce = mockConnection;
 handler.addConnection(mockConnection);
 anotherConnection = mock(ConnectionEndpoint.class);
 hero = new Hero("TestHero");
 userEvent = new UserEvent(false, "TestUser");
 gmUserEvent = new UserEvent(false, "Gamemaster");
 challReq = new ChallReq("TestUser", new Challenge(1, 6, true, null));
 challenge = new Challenge(1, 6, true, null);
 heroesVector = new Vector<>();
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(mockConnection);
 verify(mockConnection).send(isA(UserEvent.class));
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(mockConnection);
 handler.addConnection(anotherConnection);
 handler.removeConnection(mockConnection);
 verify(mockConnection, times(2)).send(isA(UserEvent.class));
 verify(anotherConnection).send(isA(UserEvent.class));
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputStringMessage() {
 handler.handleInput(new InputEvent(mockConnection, new String("TestMessage")));
 verify(mockConnection).send(eq("TestMessage"));
 }

 @Test
 public void testHandleInputChallenge() {
 handler.handleInput(new InputEvent(mockConnection, challenge));
 String expectedSend = "TestHero rolled 1d6: " + challenge.roll();
 verify(mockConnection).send(eq(expectedSend));
 }

 @Test
 public void testHandleInputChallReq() {
 handler.handleInput(new InputEvent(mockConnection, challReq));
 verify(anotherConnection).send(challReq.getRoll());
 }

 @Test
 public void testHandleInputHeroUpdate() {
 handler.handleInput(new InputEvent(mockConnection, hero));
 handler.updateHero(mockConnection, hero);
 assertEquals("TestUser", handler.getName(mockConnection));
 }

 @Test
 public void testSendAllObjHeroesVector() {
 handler.sendAllObj(heroesVector);
 for (int i = 0; i < handler.clients.size(); ++i) {
 Entry client = handler.clients.get(i);
 assertTrue(client.hero != null);
 }
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(gmUserEvent.ce);
 assertTrue(handler.isGmConnected());
 handler.removeConnection(gmUserEvent.ce);
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 String testMessage = "TestMessage";
 handler.sendAllSystemMsg(testMessage);
 for (int i = 0; i < handler.clients.size(); ++i) {
 assertEquals(testMessage, handler.clients.get(i).ce.getLastMessage());
 }
 }

 @Test
 public void testWhisper() {
 handler.addConnection(mockConnection);
 handler.addConnection(anotherConnection);
 handler.whisper(mockConnection, "TestUser TestMessage");
 verify(anotherConnection).send(eq("TestHero: TestMessage"));
 }

 @Test
 public void testRollDice() {
 handler.handleInput(new InputEvent(mockConnection, new String("/roll 1d6+2")));
 String expectedSend = handler.getName(mockConnection) + " rolled 1d6: " + Challenge.dice(6) + " 1d6: " + Challenge.dice(6) + " --> " + (Challenge.dice(6) + 2);
 verify(mockConnection).send(eq(expectedSend));
 }

 @Test
 public void testHandleInputInvalidCommand() {
 handler.handleInput(new InputEvent(mockConnection, new String("/invalidcommand")));
 verify(mockConnection).send(eq("command /invalidcommand not found"));
 }
}
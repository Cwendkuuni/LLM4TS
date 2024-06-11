package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import org.apache.commons.cli.UserEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

 private Handler handler;
 @Mock private ConnectionEndpoint connectionEndpoint;
 @Mock private UserEvent userEvent;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 handler = new Handler();
 }

 @Test
 public void testAddConnection() throws Exception {
 handler.addConnection(connectionEndpoint);
 verify(connectionEndpoint).send(any(UserEvent.class));
 }

 @Test
 public void testRemoveConnection() {
 handler.clients.add(new Entry());
 handler.clients.add(new Entry());
 handler.removeConnection(connectionEndpoint);
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputString() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.handleInput(new InputEvent(connectionEndpoint, new String("welcome user")));
 verify(connectionEndpoint).send(anyString());
 }

 @Test
 public void testHandleInputChallenge() throws Exception {
 handler.addConnection(connectionEndpoint);
 final Challenge challenge = mock(Challenge.class);
 handler.handleInput(new InputEvent(connectionEndpoint, challenge));
 verify(connectionEndpoint).send(anyString());
 }

 @Test
 public void testHandleInputChallReq() throws Exception {
 handler.clients.add(new Entry(new Hero("User1"), "User1"));
 handler.clients.add(new Entry(new Hero("User2"), "User2"));
 handler.handleInput(new InputEvent(connectionEndpoint, new ChallReq("User1", "User2")));
 verify(connectionEndpoint).send(any(ChallReq.class));
 }

 @Test
 public void testHandleInputHero() throws Exception {
 handler.addConnection(connectionEndpoint);
 final Hero hero = new Hero("User");
 handler.handleInput(new InputEvent(connectionEndpoint, hero));
 assertEquals("User", handler.getName(connectionEndpoint));
 }

 @Test
 public void testIsGmConnected() {
 handler.clients.add(new Entry("Gamemaster", new Hero("Gamemaster")));
 assertTrue(handler.isGmConnected());
 handler.clients.remove(0);
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.sendAllSystemMsg("Message");
 verify(connectionEndpoint).send("Message");
 }

 @Test
 public void testSendAllObj() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.sendAllObj(new UserEvent(false, "Test"));
 verify(connectionEndpoint).send(any(UserEvent.class));
 }

 @Test
 public void testSendOneSystemMsg() throws Exception {
 handler.clients.add(new Entry(connectionEndpoint, "User1"));
 handler.clients.add(new Entry(connectionEndpoint, "User2"));
 final String message = "Message for User1";
 handler.sendOneSystemMsg(connectionEndpoint, message);
 assertEquals("User1", handler.getName(connectionEndpoint));
 verify(connectionEndpoint).send(message);
 }

 @Test
 public void testSendToOneStr() throws Exception {
 handler.clients.add(new Entry(connectionEndpoint, "User1"));
 handler.clients.add(new Entry(connectionEndpoint, "User2"));
 handler.sendToOneStr(connectionEndpoint, "User1", "Message for User1");
 verify(connectionEndpoint).send("User1: Message for User1");
 }

 @Test
 public void testWhisper() throws Exception {
 handler.clients.add(new Entry(connectionEndpoint, "User1"));
 handler.clients.add(new Entry(connectionEndpoint, "User2"));
 handler.whisper(connectionEndpoint, "User1 User2");
 verify(connectionEndpoint).send("You whisper to User2: User1 User2");
 }

 @Test
 public void testRollDice() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d20+3");assertEquals("Handler rolled 1d20+3 --> " + (20 + 3), handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithArgs() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "2d6+1");
 assertEquals("Handler rolled 2d6+1 --> " + (Math.random() * 6 + 1) + " " + (Math.random() * 6 + 1) + " ", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithoutDiceSymbol() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "2 6+1");
 assertEquals("Handler rolled 2 6+1 --> " + (Math.random() * 6 + 1) + " " + (Math.random() * 6 + 1) + " ", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidCommand() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "test");
 assertEquals("command /roll not found", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithMultipleRolls() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "3d6");
 assertEquals("Handler rolled 3d6 --> " + (Math.random() * 6 + 1) + " " + (Math.random() * 6 + 1) + " " + (Math.random() * 6 + 1) + " ", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithModifiers() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d20+5");
 assertEquals("Handler rolled 1d20+5 --> " + (20 + 5), handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithMultipleModifiers() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d20+3+2");
 assertEquals("Handler rolled 1d20+3+2 --> " + (20 + 3 + 2), handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidModifiers() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d20+abc");
 assertEquals("command /roll not found", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidNumberOfDice() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "0d6");
 assertEquals("Invalid number of dice. Must be greater than 0", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidNumberOfSides() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d-6");
 assertEquals("Invalid number of sides. Must be greater than 0", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "test");
 assertEquals("command /roll not found", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat2() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "testd6");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat3() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d6+");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat4() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d+6");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat5() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d6+");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat6() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d6d6");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat7() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d6d6+3");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat8() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d6d6+3d4");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 @Test
 public void testRollDiceWithInvalidRollFormat9() throws Exception {
 handler.addConnection(connectionEndpoint);
 handler.rollDice(connectionEndpoint, "1d6d6+3d4+2");
 assertEquals("Invalid roll format. Use 'dN' or 'dNdM' notation, where N is the number of dice and M is the number of sides", handler.getName(connectionEndpoint) + ": ", handler.getLastSystemMessage());
 }

 private class Entry {
 ConnectionEndpoint ce;
 Hero hero;
 String name;

 Entry() {
 ce = mock(ConnectionEndpoint.class);
 name = "";
 }

 Entry(Hero hero, String name) {
 this();
 this.hero = hero;
 this.name = name;
 }
 }
}
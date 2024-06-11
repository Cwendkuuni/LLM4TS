package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Test;
import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Hero;
import java.util.Vector;

public class HandlerTest {

 Handler handler;
 ConnectionEndpoint connectionEndpoint;
 UserEvent userEvent;
 Hero hero;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint();
 userEvent = new UserEvent(false, "");
 hero = new Hero("testHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionEndpoint);
 assertTrue(handler.clients.size() == 1);
 }

 @Test(expected = Exception.class)
 public void testAddConnectionThrowsException() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(connectionEndpoint); // Should throw exception
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertTrue(handler.clients.size() == 0);
 }

 @Test
 public void testRemoveConnectionDoesNotExist() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(new ConnectionEndpoint());
 assertTrue(handler.clients.size() == 1);
 }

 @Test
 public void testWhisperToNonExistentUser() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint()); // Add another user
 handler.whisper(connectionEndpoint, "testUser nonExistentUser testMessage");
 assertEquals(handler.clients.size(), 2); // No messages were sent to the non-existent user
 }

 @Test
 public void testWhisperToExistentUser() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint()); // Add another user
 handler.whisper(connectionEndpoint, "testUser testUser testMessage");
 String expectedUserMessage = "You whisper to testUser: testMessage";
 String expectedRecipientMessage = "testUser whispers: testMessage";

 assertEquals(handler.clients.size(), 2);
 assertEquals(connectionEndpoint.receivedMessages.size(), 1);
 assertEquals(connectionEndpoint.receivedMessages.get(0).toString(), expectedUserMessage);
 assertEquals(handler.clients.get(1).receivedMessages.size(), 1);
 assertEquals(handler.clients.get(1).receivedMessages.get(0).toString(), expectedRecipientMessage);
 }

 @Test
 public void testSendHerosToConnectedUser() {
 handler.addConnection(connectionEndpoint);
 handler.sendHeros(connectionEndpoint);
 Vector<Hero> expectedHeros = new Vector<Hero>();
 expectedHeros.add(new Hero("testHero1"));
 expectedHeros.add(new Hero("testHero2"));
 assertEquals(connectionEndpoint.receivedMessages.size(), 1);
 assertEquals(connectionEndpoint.receivedMessages.get(0).getClass(), expectedHeros.getClass());
 assertEquals(connectionEndpoint.receivedMessages.get(0), expectedHeros);
 }

 @Test
 public void testSendHerosToNonConnectedUser() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint()); // Add another user - not connected
 handler.sendHeros(connectionEndpoint);
 assertEquals(handler.clients.size(), 2);
 assertEquals(connectionEndpoint.receivedMessages.size(), 0);
 }

 @Test
 public void testHandleInputWithStringMessage() {
 handler.addConnection(connectionEndpoint);
 connectionEndpoint.sendInput(new InputEvent(System.in, new String("testMessage")));
 handler.handleInput(new InputEvent(System.in, new String("testMessage")));
 String expectedMessage = "*testUser: testMessage";
 assertEquals(connectionEndpoint.receivedMessages.get(0).toString(), expectedMessage);
 }

 @Test
 public void testHandleInputWithChallengeMessage() {
 handler.addConnection(connectionEndpoint);
 connectionEndpoint.sendInput(new InputEvent(System.in, new Challenge("testMessage", null, null, false)));
 handler.handleInput(new InputEvent(System.in, new Challenge("testMessage", null, null, false)));
 String expectedMessage = handler.getName(connectionEndpoint) + ": testMessage";
 assertEquals(connectionEndpoint.receivedMessages.get(0).toString(), expectedMessage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHandleInputWithInvalidChallengeMessage() {
 handler.addConnection(connectionEndpoint);
 connectionEndpoint.sendInput(new InputEvent(System.in, new Object()));
 handler.handleInput(new InputEvent(System.in, new Object()));
 }

 @Test
 public void testUpdateHeroWithExistingUser() {
 handler.addConnection(connectionEndpoint);
 handler.addConnection(new ConnectionEndpoint()); // Add another user
 handler.updateHero(connectionEndpoint, hero);
 String expectedUserMessage = "testUser updated his hero";
 assertEquals(handler.clients.size(), 2);
 assertEquals(connectionEndpoint.receivedMessages.get(0).toString(), expectedUserMessage);
 }

 @Test
 public void testUpdateHeroWithNonExistingUser() {
 handler.addConnection(connectionEndpoint);
 handler.updateHero(new ConnectionEndpoint(), hero);
 assertEquals(handler.clients.size(), 1);
 assertEquals(connectionEndpoint.receivedMessages.size(), 0);
 }
}
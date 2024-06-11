package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Hero;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private Hero hero;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint(1234);
 hero = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connectionEndpoint);
 assertEquals(1, handler.clients.size());
 }

 @Test(expected = IOException.class)
 public void testAddConnectionWithIOException() {
 ConnectionEndpoint invalidConnection = new ConnectionEndpoint(1234) {
 @Override
 public boolean isConnected() {
 throw new IOException("Simulated IOException");
 }
 };
 handler.addConnection(invalidConnection);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(connectionEndpoint);
 assertEquals(0, handler.clients.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveConnectionWithNullConnection() {
 handler.removeConnection(null);
 }

 @Test
 public void testRemoveConnectionWithNonExistingConnection() {
 handler.addConnection(connectionEndpoint);
 handler.removeConnection(new ConnectionEndpoint(5678));
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputWithStringMessage() {
 String message = "TestMessage";
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{message}));
 assertEquals(message, handler.clients.firstElement().name);
 }

 @Test
 public void testHandleInputWithChallengeObject() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos;
 try {
 oos = new ObjectOutputStream(baos);
 hero.writeObject(oos);
 oos.close();
 }
 catch (IOException e) {
 e.printStackTrace();
 }
 Challenge challenge = new Challenge(baos.toByteArray());
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{challenge}));
 assertEquals(hero.getName(), handler.clients.firstElement().hero.getName());
 }

 @Test
 public void testHandleInputWithChallengeReqObject() {
 ChallReq challengeReq = new ChallReq("TestUser", "/rollDice 3d6+2");
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{challengeReq}));
 String expectedResponse = handler.getName(connectionEndpoint) + " rolled 3d6+2 --> ";
 for (int i = 0; i < 3; ++i) {
 expectedResponse += Challenge.dice(6) + " ";
 }
 expectedResponse += 2;
 assertEquals(expectedResponse, handler.clients.firstElement().ce.getOutputBuffer().toString());
 }

 @Test
 public void testHandleInputWithHeroObject() {
 ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
 ObjectOutputStream oos2;
 try {
 oos2 = new ObjectOutputStream(baos2);
 hero.writeObject(oos2);
 oos2.close();
 }
 catch (IOException e) {
 e.printStackTrace();
 }
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{hero}));
 String expectedResponse2 = handler.getName(connectionEndpoint) + " updated his hero: TestHero";
 assertEquals(expectedResponse2, handler.clients.firstElement().name);
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(new ConnectionEndpoint("Gamemaster", 5678));
 assertTrue(handler.isGmConnected());
 }

 @Test
 public void testIsGmConnectedWithNoGmConnected() {
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendAllSystemMsg() {
 String message = "TestMessage";
 handler.sendAllSystemMsg(message);
 for (int i = 0; i < handler.clients.size(); ++i) {
 assertEquals(message, ((String)handler.clients.elementAt(i).ce.getOutputBuffer().toString()));
 }
 }

 @Test
 public void testSendOneSystemMsg() {
 String message = "TestMessage";
 ConnectionEndpoint recipient = new ConnectionEndpoint("Recipient", 5678);
 handler.sendOneSystemMsg(recipient, message);
 assertEquals(message, recipient.getOutputBuffer().toString());
 }

 @Test
 public void testWhisper() {
 String message = "TestMessage";
 handler.addConnection(new ConnectionEndpoint("TestTo", 5678));
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{"/w TestTo TestMessage"}));
 assertEquals(message, handler.clients.get(1).ce.getOutputBuffer().toString());
 }

 @Test
 public void testRollDice() {
 String message = "TestMessage";
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{"/rollDice 1d20+5"}));
 assertEquals(message, handler.clients.firstElement().ce.getOutputBuffer().toString());
 }

 @Test
 public void testRollDiceWithMultipleDice() {
 String message = "TestMessage";
 handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{"/rollDice 3d6+2"}));
 String expectedResponse = handler.getName(connectionEndpoint) + " rolled 3d6+2 --> ";
 for (int i = 0; i < 3; ++i) {
 expectedResponse += Challenge.dice(6) + " ";
 }
 expectedResponse += 2;
 assertEquals(expectedResponse, handler.clients.firstElement().ce.getOutputBuffer().toString());
 }

}
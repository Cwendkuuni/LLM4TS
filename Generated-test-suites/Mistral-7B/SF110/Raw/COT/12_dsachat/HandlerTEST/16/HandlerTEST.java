package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

import dsachat.server.Handler;
import dsachat.server.HandlerTest;
import dsachat.server.InputEvent;
import dsachat.server.Challenge;
import dsachat.server.ChallReq;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection;
 private Hero hero;

 @Before
 public void setUp() throws Exception {
 handler = new Handler();
 connection = new ConnectionEndpoint(1234);
 hero = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connection);
 assertTrue(handler.clients.contains(new Entry(connection, "")));
 }

 @Test(expected = Exception.class)
 public void testAddConnection_whenConnectionIsNotConnected_shouldThrowException() {
 handler.addConnection(connection);
 connection.disconnect();
 handler.addConnection(connection);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connection);
 handler.removeConnection(connection);
 assertFalse(handler.clients.contains(new Entry(connection, "")));
 }

 @Test
 public void testHandleInput_whenInputIsString_shouldSendMessageToAllClients() {
 ByteArrayOutputStream outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 InputStream in = new ByteArrayInputStream("/say hello".getBytes());
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 handler.handleInput(new InputEvent(in, client));
 String output = outContent.toString();
 assertTrue(output.contains("*" + client.getName() + ": hello"));
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (handler.clients.elementAt(i).ce != client) {
 assertTrue(output.contains("*" + handler.clients.elementAt(i).name + ": " + client.getName() + ": hello"));
 }
 }
 }

 @Test
 public void testHandleInput_whenInputIsStringWithSlashCommand_shouldProcessCommand() throws IOException {
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 handler.handleInput(new InputEvent(new ByteArrayInputStream("/getheros".getBytes()), client));
 assertTrue(handler.clients.get(0).hero != null);
 }

 @Test(expected = IOException.class)
 public void testHandleInput_whenInputIsNotString_shouldThrowIOException() {
 InputStream in = new ByteArrayInputStream(new byte[0]);
 handler.handleInput(new InputEvent(in, connection));
 }

 @Test
 public void testHandleInput_whenInputIsChallenge_shouldSendMessageToSpecifiedClient() throws IOException {
 Challenge challenge = new Challenge();
 ByteArrayOutputStream outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 handler.addConnection(new ConnectionEndpoint(5678));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(challenge.toByteArray()), connection));
 String output = outContent.toString();
 String clientName = handler.clients.get(0).name;
 String recipientName = handler.clients.get(1).name;
 assertTrue(output.contains(clientName + ": you have received a challenge from " + recipientName));
 assertTrue(output.contains(recipientName + ": you have challenged " + clientName));
 }

 @Test
 public void testHandleInput_whenInputIsChallReq_shouldSendChallengeToSpecifiedClient() throws IOException {
 Challenge challenge = new Challenge();
 ConnectionEndpoint sender = new ConnectionEndpoint(1234);
 handler.addConnection(sender);
 handler.addConnection(new ConnectionEndpoint(5678));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(challenge.toByteArray()), sender));
 handler.handleInput(new InputEvent(newByteArrayInputStream(newChallReq(sender.getName(), "TestHero").toByteArray()), new ConnectionEndpoint(5678)));
 Challenge receivedChallenge = (Challenge)handler.clients.get(1).ce.receive();
 assertEquals(sender.getName(), receivedChallenge.getFrom());
 assertEquals("TestHero", receivedChallenge.getTo());
 }

 @Test
 public void testHandleInput_whenInputIsHero_shouldUpdateClientHero() throws IOException {
 Hero newHero = new Hero("NewHero");
 ByteArrayOutputStream outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(hero.toByteArray()), client));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(newHero.toByteArray()), client));
 String output = outContent.toString();
 assertTrue(output.contains("*" + client.getName() + ": " + newHero.getName()));
 }

 @Test
 public void testHandleInput_whenGmConnects_shouldUpdateGmName() throws IOException {
 ConnectionEndpoint gmConnection = new ConnectionEndpoint(9001);
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new UserEvent(true, "Gamemaster").toByteArray()), gmConnection));
 assertEquals("Gamemaster", handler.clients.get(handler.clients.size() - 1).name);
 }

 @Test
 public void testSendHeros_shouldSendHeroListToClient() throws IOException {
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 Vector<Hero> heroes = new Vector<Hero>();
 for (int i = 0; i < 5; ++i) {
 heroes.add(new Hero("TestHero" + i));
 }
 handler.handleInput(new InputEvent(new ByteArrayInputStream("/getheros".getBytes()), client));
 ByteArrayOutputStream outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 handler.sendHeros(client);
 String output = outContent.toString();
 String expectedOutput = "[";
 for (int i = 0; i < heroes.size(); ++i) {
 expectedOutput += (i > 0 ? ", " : "") + heroes.get(i).getName();
 }
 expectedOutput += "]";
 assertTrue(output.equals(expectedOutput));
 }

 @Test(expected = IOException.class)
 public void testSendToOneStr_whenConnectionIsNotConnected_shouldThrowIOException() throws IOException {
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 handler.removeConnection(client);
 handler.sendToOneStr(client, "test", "test");
 }

 @Test
 public void testSendToOneStr_shouldSendMessageToSpecifiedClient() throws IOException {
 ConnectionEndpoint sender = new ConnectionEndpoint(1234);
 ConnectionEndpoint recipient = new ConnectionEndpoint(5678);
 handler.addConnection(sender);
 handler.addConnection(recipient);
 ByteArrayOutputStream outContentSender = new ByteArrayOutputStream();
 ByteArrayOutputStream outContentRecipient = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContentSender));
 System.setErr(new PrintStream(outContentRecipient));
 handler.sendToOneStr(sender, "test", recipient.getName());
 String outputSender = outContentSender.toString();
 String outputRecipient = outContentRecipient.toString();
 String senderName = handler.clients.get(0).name;
 String recipientName = handler.clients.get(1).name;
 assertTrue(outputSender.contains("You have sent a message to " + recipientName));
 assertTrue(outputRecipient.contains(senderName + ": test"));
 }

 @Test
 public void testUpdateHero_shouldUpdateClientHero() throws IOException {
 ConnectionEndpoint client = new ConnectionEndpoint(1234);
 handler.addConnection(client);
 Hero oldHero = handler.clients.get(0).hero;
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new Hero("NewHero").toByteArray()), client));
 assertNotSame(oldHero, handler.clients.get(0).hero);
 }

 @Test
 public void testSendAllStr_shouldSendMessageToAllConnectedClients() throws IOException {
 ConnectionEndpoint client1 = new ConnectionEndpoint(1234);
 ConnectionEndpoint client2 = new ConnectionEndpoint(5678);
 handler.addConnection(client1);
 handler.addConnection(client2);
 ByteArrayOutputStream outContentClient1 = new ByteArrayOutputStream();
 ByteArrayOutputStream outContentClient2 = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContentClient1));
 System.setErr(new PrintStream(outContentClient2));
 handler.sendAllStr(client1, "test");
 String outputClient1 = outContentClient1.toString();
 String outputClient2 = outContentClient2.toString();
 assertTrue(outputClient1.contains("test"));
 assertTrue(outputClient2.contains("test"));
 }

 @Test
 public void testSendAllObj_shouldSendObjectToAllConnectedClients() throws IOException {
 ConnectionEndpoint client1 = new ConnectionEndpoint(1234);
 ConnectionEndpoint client2 = new ConnectionEndpoint(5678);
 handler.addConnection(client1);
 handler.addConnection(client2);
 ByteArrayOutputStream outContentClient1 = new ByteArrayOutputStream();
 ByteArrayOutputStream outContentClient2 = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContentClient1));
 System.setErr(new PrintStream(outContentClient2));
 handler.sendAllObj(new UserEvent(false, "TestUser"));
 String outputClient1 = outContentClient1.toString();
 String outputClient2 = outContentClient2.toString();
 assertTrue(outputClient1.contains("TestUser"));
 assertTrue(outputClient2.contains("TestUser"));
 }

 @Test
 public void testIsGmConnected_shouldReturnTrueWhenGmIsConnected() {
 assertTrue(Handler.isGmConnected(handler));
 }

 @Test
 public void testIsGmConnected_shouldReturnFalseWhenGmIsNotConnected() {
 assertFalse(Handler.isGmConnected(handler));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new UserEvent(true, "Gamemaster").toByteArray()), new ConnectionEndpoint(1234)));
 assertTrue(Handler.isGmConnected(handler));
 }

 @Test(expected = NullPointerException.class)
 public void testSendAllSystemMsg_whenInputIsNull_shouldThrowNullPointerException() {
 handler.sendAllSystemMsg(null);
 }

 @Test
 public void testSendAllSystemMsg_shouldSendMessageToAllConnectedClients() throws IOException {
 ConnectionEndpoint client1 = new ConnectionEndpoint(1234);
 ConnectionEndpoint client2 = new ConnectionEndpoint(5678);
 handler.addConnection(client1);
 handler.addConnection(client2);
 Handler.sendAllSystemMsg(handler, "test");
 ByteArrayOutputStream outContentClient1 = new ByteArrayOutputStream();
 ByteArrayOutputStream outContentClient2 = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContentClient1));
 System.setErr(new PrintStream(outContentClient2));
 handler.handleInput(new InputEvent(new ByteArrayInputStream(new UserEvent(false, "TestUser").toByteArray()), client1));
 String outputClient1 = outContentClient1.toString();
 String outputClient2 = outContentClient2.toString();
 assertTrue(outputClient1.contains("test"));
 assertTrue(outputClient2.contains("test"));
 }
}
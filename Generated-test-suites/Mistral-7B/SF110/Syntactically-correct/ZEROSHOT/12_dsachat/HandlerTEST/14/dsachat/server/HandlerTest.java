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
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint CE1;
 private ConnectionEndpoint CE2;
 private ByteArrayOutputStream baos;
 private ByteArrayInputStream bais;
 private ObjectOutputStream oos;
 private ObjectInputStream ois;
 private Hero hero;

 @Before
 public void setUp() {
 handler = new Handler();
 CE1 = new ConnectionEndpoint(1234);
 CE2 = new ConnectionEndpoint(5678);
 baos = new ByteArrayOutputStream();
 bais = new ByteArrayInputStream(baos.toByteArray());
 oos = new ObjectOutputStream(baos);
 ois = new ObjectInputStream(bais);
 hero = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(CE1);
 assertEquals(1, handler.getClients().size());
 assertEquals("", handler.getClients().get(0).getName());
 try {
 handler.handleInput(new InputEvent(CE1, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 assertEquals(1, handler.getClients().get(0).getCe().getUsers().size());
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 handler.removeConnection(CE1);
 assertEquals(1, handler.getClients().size());
 assertNull(handler.getClients().get(0).getCe());
 assertEquals("", handler.getClients().get(0).getName());
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 assertEquals(1, handler.getClients().get(0).getCe().getUsers().size());
 }

 @Test
 public void testGetName() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 handler.updateHero(CE1, hero);
 handler.handleInput(new InputEvent(CE1, ""));
 assertEquals("TestHero", handler.getName(CE1));
 assertEquals("", handler.getName(CE2));
 }

 @Test
 public void testIsGmConnected() {
 handler.addConnection(CE1);
 handler.handleInput(new InputEvent(CE1, new UserEvent(true, "Gamemaster")));
 assertTrue(handler.isGmConnected());
 handler.handleInput(new InputEvent(CE1, new UserEvent(false, "")));
 assertFalse(handler.isGmConnected());
 }

 @Test
 public void testSendSystemMessage() {
 handler.addConnection(CE1);
 handler.handleInput(new InputEvent(CE1, ""));
 try {
 handler.sendAllSystemMsg("TestMessage");
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String message = sw.getBuffer().toString();
 assertEquals("TestMessage", message);
 }

 @Test
 public void testHandleInputString() {
 handler.addConnection(CE1);
 handler.handleInput(new InputEvent(CE1, new String("test string")));
 assertEquals(1, handler.getClients().get(0).getCe().getUsers().size());
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 assertEquals(1, handler.getClients().get(0).getCe().getUsers().size());
 }

 @Test
 public void testHandleInputChallenge() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 Challenge challenge = new Challenge();
 try {
 handler.handleInput(new InputEvent(CE1, challenge));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String send = sw.getBuffer().toString();
 assertEquals(challenge.roll(), send);
 }

 @Test
 public void testHandleInputChallReq() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 handler.addConnection(CE3);
 ChallengeReq challReq = new ChallengeReq("TestTo", 12);
 try {
 handler.handleInput(new InputEvent(CE1, challReq));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 assertEquals(1, handler.getClients().get(2).getCe().getUsers().size());
 }

 @Test
 public void testHandleInputHero() {
 handler.addConnection(CE1);
 Hero hero = new Hero("TestHero");
 try {
 handler.handleInput(new InputEvent(CE1, hero));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String name = sw.getBuffer().toString();
 assertEquals("TestHero", name);
 }

 @Test
 public void testHandleInputUserEvent() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 try {
 handler.handleInput(new InputEvent(CE1, userEvent));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 assertEquals("Gamemaster", handler.getClients().get(0).getName());
 }

 @Test
 public void testSendOneSystemMsg() {
 handler.addConnection(CE1);
 handler.handleInput(new InputEvent(CE1, ""));
 try {
 handler.sendOneSystemMsg(CE1, "TestMessage");
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String message = sw.getBuffer().toString();
 assertEquals("TestMessage", message);
 }

 @Test
 public void testSendToOneStr() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 handler.handleInput(new InputEvent(CE1, ""));
 try {
 handler.sendToOneStr(CE2, "TestTo", "TestMessage");
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String message2 = sw.getBuffer().toString();
 assertEquals("TestMessage: TestTo", message2);
 }

 @Test
 public void testUpdateHero() {
 handler.addConnection(CE1);
 handler.handleInput(new InputEvent(CE1, ""));
 try {
 handler.updateHero(CE1, hero);
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String name = sw.getBuffer().toString();
 assertEquals("TestHero", name);
 }

 @Test
 public void testSendAllStr() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 String message = "TestMessage";
 try {
 handler.sendAllStr(CE1, message);
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 StringWriter sw = new StringWriter();
 PrintWriter pw = new PrintWriter(sw);
 try {
 handler.handleInput(new InputEvent(CE2, ""));
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 String receivedMessage = sw.getBuffer().toString();
 assertEquals(message, receivedMessage);
 }

 @Test
 public void testSendAllObj() {
 handler.addConnection(CE1);
 handler.addConnection(CE2);
 try {
 handler.sendAllObj(hero);
 }
 catch (IOException e) {
 fail("Should not throw an IOException");
 }
 Vector<Object> clientsObjects = handler.getClients();
 assertEquals(hero, clientsObjects.get(1));
 }
}
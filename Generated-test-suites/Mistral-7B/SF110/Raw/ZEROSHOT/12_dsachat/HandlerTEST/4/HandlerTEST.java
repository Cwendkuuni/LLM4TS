package dsachat.server.test;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.ConnectionEndpoint;
import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.Hero;
import dsachat.server.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection1;
 private ConnectionEndpoint connection2;
 private Hero hero;

 @Before
 public void setUp() {
 handler = new Handler();
 connection1 = new ConnectionEndpoint(1, null);
 connection2 = new ConnectionEndpoint(2, null);
 hero = new Hero("TestHero");
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(connection1);
 assertTrue(handler.clients.size() == 1);
 }

 @Test
 public void testAddConnectionWithName() {
 handler.addConnection(connection1);
 handler.clients.get(0).name = "TestUser";
 assertEquals("TestUser", handler.clients.get(0).name);
 }

 @Test
 public void testRemoveConnection() {
 handler.addConnection(connection1);
 handler.clients.get(0).ce = connection1;
 handler.removeConnection(connection1);
 assertTrue(handler.clients.size() == 0);
 }

 @Test
 public void testRemoveConnectionWithName() {
 handler.addConnection(connection1);
 handler.clients.get(0).name = "TestUser1";
 handler.clients.addElement(new Entry());
 handler.clients.get(1).ce = connection2;
 handler.clients.get(1).name = "TestUser2";
 handler.removeConnection("TestUser1");
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputWithString() {
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 handler.clients.get(0).ce = connection1;
 handler.clients.get(1).ce = connection2;
 handler.handleInput(new InputEvent(connection1, new String("test")));
 assertEquals(2, handler.clients.get(0).ce.getSendCount());
 assertEquals(1, handler.clients.get(1).ce.getSendCount());
 }

 @Test
 public void testHandleInputWithChallenge() {
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 handler.clients.get(0).ce = connection1;
 handler.clients.get(1).ce = connection2;
 handler.handleInput(new InputEvent(connection1, new Challenge()));
 assertEquals(2, handler.clients.get(0).ce.getSendCount());
 assertEquals(1, handler.clients.get(1).ce.getSendCount());
 }

 @Test
 public void testHandleInputWithChallReq() {
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 handler.clients.get(0).ce = connection1;
 handler.clients.get(1).ce = connection2;
 handler.handleInput(new InputEvent(connection1, new ChallReq("TestUser2")));
 assertEquals(1, handler.clients.get(1).ce.getSendCount());
 }

 @Test
 public void testHandleInputWithHero() {
 handler.addConnection(connection1);
 handler.handleInput(new InputEvent(connection1, new Hero(hero)));
 assertNotNull(handler.clients.get(0).hero);
 assertEquals(hero.getName(), handler.clients.get(0).name);
 }

 @Test
 public void testHandleInputWithUserEvent() {
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 handler.clients.get(0).ce = connection1;
 handler.clients.get(1).ce = connection2;
 handler.handleInput(new InputEvent(connection1, new UserEvent(true, "TestUser1")));
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testSendAllObj() {
 handler.addConnection(connection1);
 handler.addConnection(connection2);
 handler.clients.get(0).ce = connection1;
 handler.clients.get(1).ce = connection2;
 handler.sendAllObj(new Object());
 assertEquals(2, handler.clients.get(0).ce.getSendCount());
 assertEquals(2, handler.clients.get(1).ce.getSendCount());
 }

 @Test
 public void testGetName() {
 handler.addConnection(connection1);
 handler.clients.get(0).name = "TestUser";
 assertEquals("TestUser", handler.getName(connection1));
 }

 @Test (expected = NullPointerException.class)
 public void testGetNameWithNullConnection() {
 handler.getName(null);
 }

}
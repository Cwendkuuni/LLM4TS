package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.server.Handler;
import dsachat.server.HandlerTest;

public class HandlerTest {

	Handler handler;
	ConnectionEndpoint con1;
	ConnectionEndpoint con2;
	Entry entry1;
	Entry entry2;
	Hero hero1;
	Hero hero2;
	Challenge challenge;
	ChallReq challReq;
	PrintWriter stdout;

	@Before
	public void setUp() {
		handler = new Handler();
		con1 = new ConnectionEndpoint();
		con2 = new ConnectionEndpoint();
		entry1 = new Entry();
		entry2 = new Entry();
		hero1 = new Hero("name1", 10, 10, 10);
		hero2 = new Hero("name2", 5, 5, 5);
		challenge = new Challenge(5, null, null);
		challReq = new ChallReq("name3", hero2);
		stdout = new PrintWriter(new StringWriter());
		System.setOut(stdout);
	}

	@After
	public void tearDown() {
		System.setOut(System.out);
	}

	@Test
	public void testAddConnection() {
		handler.addConnection(con1);
		assertEquals(2, handler.clients.size());
		assertTrue(handler.clients.contains(entry1));
	}

	@Test(expected = Exception.class)
	public void testAddConnectionFailed() throws Exception {
		handler.addConnection(con1);
		handler.addConnection(con1);
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(con1);
		handler.addConnection(con2);
		assertEquals(2, handler.clients.size());
		handler.removeConnection(con1);
		assertEquals(1, handler.clients.size());
		assertFalse(handler.clients.contains(entry1));
	}

	@Test
	public void testHandleInputString() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new StringDataEvent("message1"), con1));
		assertEquals(1, handler.sendCount);
		assertEquals("message1", handler.lastMessage);
	}

	@Test
	public void testHandleInputChallenge() {
		handler.addConnection(con1);
		handler.clients.add(entry2);
		handler.handleInput(new InputEvent(new DataEvent(challenge), con1));
		assertEquals(2, handler.sendCount);
		assertEquals("name1: message1", handler.lastMessage);
		assertEquals("roll result", entry2.ce.getOutputQueue().poll().toString());
	}

	@Test
	public void testHandleInputChallReq() {
		handler.addConnection(con1);
		handler.addConnection(con2);
		handler.clients.add(entry2);
		handler.handleInput(new InputEvent(new DataEvent(challReq), con1));
		assertEquals(2, handler.sendCount);
		assertEquals("name2: message1", handler.lastMessage);
		assertEquals(hero2, handler.clients.get(1).hero);
	}

	@Test
	public void testHandleInputHero() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new DataEvent(hero1), con1));
		assertEquals(1, handler.sendCount);
		assertEquals("Welcome name1", handler.lastMessage);
		assertEquals(hero1, handler.clients.get(0).hero);
	}

	@Test
	public void testHandleInputGmConnect() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		assertEquals(1, handler.sendCount);
		assertEquals("Gamemaster connected", handler.lastMessage);
	}

	@Test
	public void testSendHeros() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		handler.sendHeros(con1);
		assertEquals(1, handler.sendCount);
		assertEquals("[Hero[name1=name1, str=10, int_=10, wis=10]], Hero[name2=name2, str=5, int_=5, wis=5]]",
				handler.lastMessage);
	}

	@Test
	public void testIsGmConnected() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		assertTrue(handler.isGmConnected());
	}

	@Test
	public void testSendOneSystemMsg() {
		handler.addConnection(con1);
		handler.sendOneSystemMsg(con1, "system message");
		assertEquals(1, handler.sendCount);
		assertEquals("system message", handler.lastMessage);
	}

	@Test
	public void testSendAllSystemMsg() {
		handler.addConnection(con1);
		handler.addConnection(con2);
		handler.sendAllSystemMsg("system message");
		assertEquals(2, handler.sendCount);
		assertEquals("system message", handler.lastMessage);
	}

	@Test
	public void testWhisper() {
		handler.addConnection(con1);
		handler.addConnection(con2);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		handler.whisper(con1, "name2 message");
		assertEquals(2, handler.sendCount);
		assertEquals("You whisper to name2: message1", handler.lastMessage);
		assertEquals("name1 whispers: message1", handler.clients.get(1).ce.getOutputQueue().poll().toString());
	}

	@Test
	public void testRollDice() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		handler.rollDice(con1, "2d6+3");
		assertEquals(1, handler.sendCount);
		assertEquals("You rolled 2d6+3 --> 7 7", handler.lastMessage);
		assertEquals("7, 7", handler.clients.get(0).ce.getOutputQueue().poll().toString());
	}

	@Test
	public void testRollDiceMultiple() {
		handler.addConnection(con1);
		handler.addConnection(con2);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		handler.rollDice(con1, "3d20");
		assertEquals(2, handler.sendCount);
		assertEquals("You rolled 3d20 --> 17, 15, 12", handler.lastMessage);
		assertEquals("15, 12, 17", handler.clients.get(1).ce.getOutputQueue().poll().toString());
		assertEquals("12, 17, 15", handler.clients.get(0).ce.getOutputQueue().poll().toString());
	}

	@Test
	public void testUpdateHero() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new DataEvent(hero1), con1));
		assertTrue(handler.updateHero(con1, new Hero("newName", 15, 15, 15)) != null);
		assertEquals("newName", handler.getName(con1));
	}

	@Test
	public void testSendAllStr() {
		handler.addConnection(con1);
		handler.sendAllStr(con1, "common message");
		assertEquals(2, handler.sendCount);
		assertEquals("name1: common message", handler.lastMessage);
		assertEquals("common message", handler.clients.get(1).ce.getOutputQueue().poll().toString());
	}

	@Test
	public void testSendAllObj() {
		handler.addConnection(con1);
		handler.handleInput(new InputEvent(new UserEvent(true, "Gamemaster"), con1));
		handler.sendAllObj(Arrays.asList(hero1, hero2));
		assertEquals(2, handler.sendCount);
		assertEquals("[Hero[name1=name1, str=10, int_=10, wis=10]], Hero[name2=name2, str=5, int_=5, wis=5]]",
				handler.lastMessage);
	}

	@Test
	public void testgetName() {
		handler.addConnection(con1);
		assertTrue(handler.getName(con1) == null);
		handler.handleInput(new InputEvent(new DataEvent(hero1), con1));
		assertEquals("name1", handler.getName(con1));
	}
}
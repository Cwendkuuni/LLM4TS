package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.HandlerTest;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;

public class HandlerTest {

	private Handler handler;
	private ConnectionEndpoint con1;
	private ConnectionEndpoint con2;
	private ConnectionEndpoint con3;
	private Hero hero;
	private Challenge challenge;
	private ChallReq challReq;

	@Before
	public void setUp() {
		handler = new Handler();
		con1 = new ConnectionEndpoint();
		con2 = new ConnectionEndpoint();
		con3 = new ConnectionEndpoint();
		hero = new Hero("TestHero");
		challenge = new Challenge("TestChallenge");
		challReq = new ChallReq("TestUser", challenge);
		handler.addConnection(con1);
		handler.addConnection(con2);
		handler.addConnection(con3);
	}

	@After
	public void tearDown() {
		handler.removeConnection(con1);
		handler.removeConnection(con2);
		handler.removeConnection(con3);
	}

	@Test
	public void testAddConnection() {
		assertEquals(3, handler.getClients().size());
	}

	@Test
	public void testRemoveConnection() {
		handler.removeConnection(con1);
		assertEquals(2, handler.getClients().size());
		handler.removeConnection(con2);
		assertEquals(1, handler.getClients().size());
		handler.addConnection(con1);
		assertEquals(2, handler.getClients().size());
	}

	@Test
	public void testHandleInputString() {
		handler.handleInput(new InputEvent(con1, new ByteArrayInputStream(new byte[] {}), new StringWriter()));
		assertEquals(0, handler.getClients().size());
		handler.addConnection(con1);

		handler.handleInput(new InputEvent(con1, new ByteArrayInputStream(new byte[] {1}), new StringWriter()));
		assertEquals(1, handler.getClients().get(0).name.length());
		assertEquals("", handler.getClients().get(0).name);

		String msg = "/w TestUser TestMessage";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(msg);
		InputEvent inputEvent = new InputEvent(con1, new ByteArrayInputStream(bos.toByteArray()), new StringWriter());
		handler.handleInput(inputEvent);

		assertEquals("TestUser", handler.getName(con1));
		assertEquals("TestMessage", handler.getLastMessage(con1));
		assertEquals(null, handler.getLastMessage(con2));
		assertEquals(1, handler.getClients().get(0).name.length());

		handler.removeConnection(con1);
		assertEquals(0, handler.getClients().size());
	}

	@Test
	public void testHandleInputChallenge() {
		handler.handleInput(new InputEvent(con1, new ByteArrayInputStream(new byte[] {"/getheros".getBytes()}), new StringWriter()));
		handler.sendAllObj(con2, hero);
		assertEquals(hero, handler.getClients().get(1).hero);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(challenge);
		InputEvent inputEvent = new InputEvent(con1, new ByteArrayInputStream(bos.toByteArray()), new StringWriter());
		handler.handleInput(inputEvent);

		assertEquals(challenge, handler.getLastInput(con1));
		assertEquals(null, handler.getLastInput(con2));
		assertEquals(challenge, handler.getClients().get(0).lastInput);
		assertEquals(null, handler.getClients().get(1).lastInput);
		assertEquals(challenge.getTo(), handler.getLastTo(con1));
		assertEquals(null, handler.getLastTo(con2));
		assertEquals(challenge, handler.getLastObj(con1));
		assertEquals(null, handler.getLastObj(con2));
		assertEquals(challenge.getTo(), handler.getLastObjTo(con1));
		assertEquals(null, handler.getLastObjTo(con2));
		assertEquals(challenge.roll(), handler.getLastRoll(con1));
		assertEquals(null, handler.getLastRoll(con2));
	}

	@Test
	public void testHandleInputChallReq() {
		handler.handleInput(new InputEvent(con2, new ByteArrayInputStream(new byte[] {"/getheros".getBytes()}), new StringWriter()));
		handler.sendAllObj(con1, hero);
		assertEquals(hero, handler.getClients().get(0).hero);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(challReq);
		InputEvent inputEvent = new InputEvent(con2, new ByteArrayInputStream(bos.toByteArray()), new StringWriter());
		handler.handleInput(inputEvent);

		assertEquals(challReq, handler.getLastInput(con2));
		assertEquals(null, handler.getLastInput(con1));
		assertEquals(challReq, handler.getClients().get(1).lastInput);
		assertEquals(null, handler.getClients().get(0).lastInput);
		assertEquals(challReq.getTo(), handler.getLastTo(con2));
		assertEquals(null, handler.getLastTo(con1));
		assertEquals(challReq, handler.getLastObj(con2));
		assertEquals(null, handler.getLastObj(con1));
		assertEquals(challReq.getTo(), handler.getLastObjTo(con2));
		assertEquals(null, handler.getLastObjTo(con1));
		assertEquals(null, handler.getLastRoll(con1));
		assertEquals(null, handler.getLastRoll(con2));
	}

	@Test
	public void testHandleInputObject() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(hero);
		InputEvent inputEvent = new InputEvent(con1, new ByteArrayInputStream(bos.toByteArray()), new StringWriter());
		handler.handleInput(inputEvent);

		assertEquals(hero, handler.getLastInput(con1));
		assertEquals(null, handler.getLastInput(con2));
		assertEquals(hero, handler.getClients().get(0).hero);
		assertEquals(null, handler.getClients().get(1).hero);
		assertEquals(hero, handler.getLastObj(con1));
		assertEquals(null, handler.getLastObj(con2));
		assertEquals(hero, handler.getLastObjTo(con1));
		assertEquals(null, handler.getLastObjTo(con2));
		assertEquals(hero.getName(), handler.getLastMessage(con1));
		assertEquals(null, handler.getLastMessage(con2));
		assertEquals(hero.getName(), handler.getName(con1));
		assertEquals(null, handler.getName(con2));
	}

	@Test
	public void testHandleInputException() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new Exception());
		InputEvent inputEvent = new InputEvent(con1, new ByteArrayInputStream(bos.toByteArray()), new StringWriter());

		try {
			handler.handleInput(inputEvent);
			fail("Expected an IOException");
		} catch (IOException e) {
			// Expected
		}

		assertEquals(null, handler.getLastInput(con1));
		assertEquals(null, handler.getLastInput(con2));
		assertEquals(null, handler.getClients().get(0).lastInput);
		assertEquals(null, handler.getClients().get(1).lastInput);
		assertEquals(null, handler.getLastObj(con1));
		assertEquals(null, handler.getLastObj(con2));
		assertEquals(null, handler.getLastObjTo(con1));
		assertEquals(null, handler.getLastObjTo(con2));
		assertEquals(null, handler.getLastMessage(con1));
		assertEquals(null, handler.getLastMessage(con2));
		assertEquals(null, handler.getName(con1));
		assertEquals(null, handler.getName(con2));
	}

	private Vector<Entry> getClients() {
		return handler.getClients();
	}

	private Object getLastInput(ConnectionEndpoint con) {
		return handler.getLastInput(con);
	}

	private Object getLastObj(ConnectionEndpoint con) {
		return handler.getLastObj(con);
	}

	private Object getLastObjTo(ConnectionEndpoint con) {
		return handler.getLastObjTo(con);
	}

	private String getLastMessage(ConnectionEndpoint con) {
		return handler.getLastMessage(con);
	}

	private String getLastTo(ConnectionEndpoint con) {
		return handler.getLastTo(con);
	}

	private String getName(ConnectionEndpoint con) {
		return handler.getName(con);
	}
}
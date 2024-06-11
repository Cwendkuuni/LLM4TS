package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.Hero;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;

public class HandlerTest {

	private Handler handler;
	private ConnectionEndpoint con1, con2;
	private Hero hero1, hero2;
	private ByteArrayOutputStream outStream1, outStream2;
	private ObjectOutputStream out;

	@Before
	public void setUp() {
		handler = new Handler();
		con1 = new ConnectionEndpoint();
		con2 = new ConnectionEndpoint();
		hero1 = new Hero("test1");
		hero2 = new Hero("test2");
		outStream1 = new ByteArrayOutputStream();
		outStream2 = new ByteArrayOutputStream();
		out = new ObjectOutputStream(outStream1);
	}

	@After
	public void tearDown() {
		handler = null;
		con1 = null;
		con2 = null;
		hero1 = null;
		hero2 = null;
		outStream1 = null;
		outStream2 = null;
		out = null;
	}

	@Test
	public void testAddConnection() throws IOException {
		handler.addConnection(con1);
		assertEquals(1, handler.clients.size());
		assertEquals("", handler.clients.elementAt(0).name);
		assertTrue(con1.isConnected());
	}

	@Test(expected = Exception.class)
	public void testAddConnection_NotConnected() throws Exception {
		handler.addConnection(con2);
		fail("Expected Exception to be thrown");
	}

	@Test
	public void testRemoveConnection() throws IOException {
		handler.addConnection(con1);
		handler.addConnection(con2);
		handler.removeConnection(con1);
		assertEquals(1, handler.clients.size());
		assertEquals("test2", handler.clients.elementAt(0).name);
		assertFalse(con1.isConnected());
		assertTrue(con2.isConnected());
	}

	@Test
	public void testHandleInput() throws IOException {
		handler.addConnection(con1);
		handler.addConnection(con2);

		// String command
		String msg1 = "/hello con1";
		handler.handleInput(new InputEvent(con1, msg1));
		assertEquals(msg1, outStream1.toString().trim());
		assertEquals(msg1, handler.clients.elementAt(1).ce.getReceivedMessages().get(0).getData().toString());

		String msg2 = "/getheros";
		handler.handleInput(new InputEvent(con2, msg2));
		Vector<Hero> heroes = (Vector<Hero>) outStream2.toByteArray();
		assertEquals(2, heroes.size());
		assertEquals(hero1.getName(), heroes.get(0).getName());
		assertEquals(hero2.getName(), heroes.get(1).getName());

		// Challenge command
		Challenge challenge = new Challenge(20);
		out.writeObject(challenge);
		handler.handleInput(new InputEvent(con1, outStream1.toByteArray()));
		assertEquals(1, handler.clients.size());
		assertEquals(hero1.getName(), handler.clients.elementAt(0).name);
		assertEquals(challenge.roll(), handler.clients.elementAt(0).ce.getSentMessages().get(0).getData().toString());

		// Challenge request
		ChallReq challReq = new ChallReq("test3", 10);
		out.writeObject(challReq);
		handler.handleInput(new InputEvent(con2, outStream1.toByteArray()));
		assertEquals(2, handler.clients.size());
		assertEquals("test3", handler.clients.elementAt(1).name);
		assertEquals(challReq.getTo(), handler.clients.elementAt(0).name);
		assertEquals(challReq.getTo(), handler.clients.elementAt(1).hero.getUsername());
		assertEquals(challReq.getRollCommand(), handler.clients.elementAt(1).ce.getSentMessages().get(0).getData().toString());

		// Hero update
		hero1.setName("test1_updated");
		handler.handleInput(new InputEvent(con1, hero1));
		assertEquals(hero1.getName(), handler.clients.elementAt(0).name);

		// Gamemaster connection
		UserEvent userEvent = new UserEvent(true, "Gamemaster");
		handler.handleInput(new InputEvent(con2, userEvent));
		assertEquals(2, handler.clients.size());
		assertEquals("Gamemaster", handler.clients.elementAt(1).name);
	}
}
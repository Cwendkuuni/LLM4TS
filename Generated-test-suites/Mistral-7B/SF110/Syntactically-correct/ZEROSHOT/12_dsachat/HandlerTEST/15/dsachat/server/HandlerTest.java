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
import dsachat.server.HandlerImpl;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.hero.Hero;
import dsachat.share.hero.HeroFactory;
import dsachat.share.userEvent.UserEvent;

public class HandlerTest {

	private Handler handler;
	private ConnectionEndpoint ce1, ce2;
	private Hero hero1, hero2;

	@Before
	public void setUp() {
		handler = new HandlerImpl();
		ce1 = new ConnectionEndpoint(12345);
		ce2 = new ConnectionEndpoint(67890);
		hero1 = HeroFactory.getInstance().createHero("TestHero1");
		hero2 = HeroFactory.getInstance().createHero("TestHero2");
	}

	@After
	public void tearDown() {
		handler.removeConnection(ce1);
		handler.removeConnection(ce2);
	}

	@Test
	public void testAddConnection() {
		handler.addConnection(ce1);
		assertTrue(handler.clients.size() == 1);
		assertEquals("", handler.clients.elementAt(0).name);
		handler.addConnection(ce2);
		assertTrue(handler.clients.size() == 2);
		assertEquals("", handler.clients.elementAt(1).name);
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(ce1);
		handler.addConnection(ce2);
		handler.removeConnection(ce1);
		assertTrue(handler.clients.size() == 1);
		assertEquals("", handler.clients.elementAt(0).name);
		handler.removeConnection(ce2);
		assertTrue(handler.clients.size() == 0);
	}

	@Test
	public void testHandleInputString() throws IOException {
		handler.addConnection(ce1);
		handler.addConnection(ce2);
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		handler.handleInput(new InputEvent(ce1, new ByteArrayInputStream(new byte[]{}), new PrintWriter(new StringWriter())));
		assertEquals("Gamemaster", handler.getName(ce1));
		handler.handleInput(new InputEvent(ce2, new ByteArrayInputStream(new byte[]{}), new PrintWriter(new StringWriter())));
		assertEquals("Gamemaster", handler.getName(ce2));
	}

	@Test
	public void testHandleInputChallenge() throws IOException {
		handler.addConnection(ce1);
		handler.addConnection(ce2);
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		final ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		final ObjectOutputStream out1 = new ObjectOutputStream(outputStream1);
		final ObjectOutputStream out2 = new ObjectOutputStream(outputStream2);
		final Challenge challenge = new Challenge();
		out1.writeObject(challenge);
		final InputEvent inputEvent1 = new InputEvent(ce1, new ByteArrayInputStream(outputStream1.toByteArray()), new PrintWriter(new StringWriter()));
		final InputEvent inputEvent2 = new InputEvent(ce2, new ByteArrayInputStream(outputStream2.toByteArray()), new PrintWriter(new StringWriter()));
		handler.handleInput(inputEvent1);
		handler.handleInput(inputEvent2);
		final Object receivedObject1 = handler.clients.elementAt(0).ce.receive();
		final Object receivedObject2 = handler.clients.elementAt(1).ce.receive();
		assertEquals(challenge, receivedObject1);
		assertEquals(challenge, receivedObject2);
	}

	@Test
	public void testHandleInputChallReq() throws IOException {
		handler.addConnection(ce1);
		handler.addConnection(ce2);
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		final ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		final ObjectOutputStream out1 = new ObjectOutputStream(outputStream1);
		final ObjectOutputStream out2 = new ObjectOutputStream(outputStream2);
		final ChallReq challReq = new ChallReq("TestUser1", new Challenge());
		out1.writeObject(challReq);
		final InputEvent inputEvent1 = new InputEvent(ce1, new ByteArrayInputStream(outputStream1.toByteArray()), new PrintWriter(new StringWriter()));
		handler.handleInput(inputEvent1);
		final InputEvent inputEvent2 = new InputEvent(ce2, new ByteArrayInputStream(new byte[]{}), new PrintWriter(new StringWriter()));
		handler.handleInput(inputEvent2);
		assertEquals("TestUser1", handler.getName(ce1));
		assertEquals("Gamemaster", handler.getName(ce2));
		assertTrue(challReq.isSentTo(ce1));
		assertFalse(challReq.isSentTo(ce2));
		handler.handleInput(inputEvent2);
		assertTrue(challReq.isSentTo(ce2));
	}

	@Test
	public void testHandleInputHero() throws IOException {
		handler.addConnection(ce1);
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(hero1);
		final InputEvent inputEvent = new InputEvent(ce1, new ByteArrayInputStream(outputStream.toByteArray()), new PrintWriter(new StringWriter()));
		handler.handleInput(inputEvent);
		assertEquals(hero1.getName(), handler.getName(ce1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNullConnection() {
		handler.addConnection(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveNullConnection() {
		handler.addConnection(ce1);
		handler.removeConnection(null);
	}

	@Test(expected = IOException.class)
	public void testHandleInputWithNullInputStream() {
		handler.addConnection(ce1);
		handler.handleInput(new InputEvent(ce1, null, new PrintWriter(new StringWriter())));
	}

	@Test(expected = IOException.class)
	public void testHandleInputWithNullPrintWriter() {
		handler.addConnection(ce1);
		handler.handleInput(new InputEvent(ce1, new ByteArrayInputStream(new byte[]{}), null));
	}

	@Test(expected = IllegalStateException.class)
	public void testIsGmConnectedWhenNoGmConnected() {
		assertTrue(handler.isGmConnected() == false);
	}

	@Test(expected = IllegalStateException.class)
	public void testIsGmConnectedWhenGmConnected() {
		handler.addConnection(new ConnectionEndpoint(12345, "Gamemaster"));
		assertTrue(handler.isGmConnected() == true);
	}

	@Test
	public void testSendAllSystemMsg() {
		handler.addConnection(ce1);
		handler.addConnection(ce2);
		final String message = "Test Message";
		handler.sendAllSystemMsg(message);
		for (final Entry entry : handler.clients) {
			assertEquals(message, entry.ce.receive().toString());
		}
	}

	@Test
	public void testWhisper() throws IOException {
		handler.addConnection(ce1);
		handler.addConnection(ce2);
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		final ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
		final ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		final ObjectOutputStream out1 = new ObjectOutputStream(outputStream1);
		final ObjectOutputStream out2 = new ObjectOutputStream(outputStream2);
		final String to = "TestUser2";
		final String message = "This is a test whisper.";
		out1.writeObject(new WhisperCommand(to, message));
		final InputEvent inputEvent1 = new InputEvent(ce1, new ByteArrayInputStream(outputStream1.toByteArray()), new PrintWriter(new StringWriter()));
		handler.handleInput(inputEvent1);
		assertEquals("", handler.getName(ce1));
		assertEquals("TestUser2", handler.getName(ce2));
		handler.handleInput(new InputEvent(ce2, new ByteArrayInputStream(new byte[]{}), new PrintWriter(new StringWriter())));
		assertEquals(message, handler.clients.elementAt(1).ce.receive().toString());
	}

	@Test
	public void testRollDice() throws IOException {
		handler.addConnection(ce1);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final ObjectOutputStream out = new ObjectOutputStream(outputStream);
		out.writeObject(new RollDiceCommand("2d6+3"));
		final InputEvent inputEvent = new InputEvent(ce1, new ByteArrayInputStream(outputStream.toByteArray()), new PrintWriter(new StringWriter()));
		handler.handleInput(inputEvent);
		final Object receivedObject = handler.clients.elementAt(0).ce.receive();
		assertEquals(receivedObject.getClass(), String.class);
		assertEquals("TestUser1 rolled 2d6+3 --> 7", receivedObject.toString());
	}
}
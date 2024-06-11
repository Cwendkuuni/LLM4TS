package dsachat.server;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.hero.Hero;
import dsachat.share.hero.HeroFactory;
import dsachat.share.hero.UserHero;
import dsachat.share.userEvent.UserEvent;
import dsachat.share.userEvent.UserEventFactory;

public class HandlerTest {

	private Handler handler;
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	private PrintWriter writer;
	private ByteArrayOutputStream bos;
	private ByteArrayInputStream bis;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ConnectionEndpoint connectionEndpoint;
	private UserEvent userEvent;
	private Hero hero;

	@Before
	public void setUp() throws IOException {
		handler = new Handler();
		socket = new Socket("localhost", 1234);
		input = socket.getInputStream();
		output = socket.getOutputStream();
		writer = new PrintWriter(socket.getOutputStream(), true);
		bos = new ByteArrayOutputStream();
		bis = new ByteArrayInputStream(bos.toByteArray());
		oos = new ObjectOutputStream(output);
		oos.writeObject(new UserEventFactory().createUserEvent(true, "Gamemaster"));
		oos.flush();
		connectionEndpoint = new ConnectionEndpoint(socket, this);
		handler.addConnection(connectionEndpoint);
		userEvent = new UserEventFactory().createUserEvent(false, "TestUser");
		hero = new HeroFactory().createHero("TestUser", Arrays.asList(new UserHero("TestWeapon", 10.0)));
	}

	@After
	public void tearDown() throws IOException {
		handler.removeConnection(connectionEndpoint);
		socket.close();
	}

	@Test
	public void testAddConnection() {
		assertEquals(0, handler.clients.size());
		handler.addConnection(connectionEndpoint);
		assertEquals(1, handler.clients.size());
		Handler.Entry entry = handler.clients.elementAt(0);
		assertNull(entry.name);
		assertNull(entry.hero);
		assertEquals(connectionEndpoint, entry.ce);
	}

	@Test
	public void testRemoveConnection() {
		Handler.Entry entry = new Handler.Entry();
		entry.ce = connectionEndpoint;
		entry.name = "TestUser";
		handler.clients.addElement(entry);
		handler.removeConnection(connectionEndpoint);
		assertEquals(0, handler.clients.size());
		handler.addConnection(connectionEndpoint);
		handler.removeConnection(connectionEndpoint);
		assertEquals(0, handler.clients.size());
		handler.addConnection(connectionEndpoint);
		handler.removeConnection(connectionEndpoint);
		assertEquals(0, handler.clients.size());
	}

	@Test
	public void testHandleInputStringClass() throws IOException {
		handler.handleInput(new InputEvent(userEvent, connectionEndpoint));
		assertEquals(0, handler.clients.size());
		assertEquals("TestUser: " + userEvent.getMessage(), bos.toString());
	}

	@Test
	public void testHandleInputChallengeClass() throws IOException {
		bos.reset();
		bis.reset();
		ois = new ObjectInputStream(bis);
		oos.writeObject(new ChallengeFactory().createChallenge("TestChallenge", null, null, null, null, null, null, 1, false));
		oos.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(bos.toByteArray()), connectionEndpoint));
		Challenge challenge = (Challenge) ois.readObject();
		assertEquals("TestChallenge", challenge.getName());
		assertEquals(connectionEndpoint, challenge.getSource());
		assertEquals("TestUser", challenge.getTo());
		assertNull(challenge.getRollResult());
		assertEquals(false, challenge.isSilent());
		bos.reset();
		String send = "TestUser rolled TestChallenge --> 2";
		writer.println(send);
		writer.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(bos.toByteArray()), connectionEndpoint));
		assertEquals(send, bos.toString());
	}

	@Test
	public void testHandleInputChallReqClass() throws IOException {
		bos.reset();
		bis.reset();
		ois = new ObjectInputStream(bis);
		oos.writeObject(new ChallReqFactory().createChallReq("TestUser", "Gamemaster", "TestChallenge"));
		oos.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(bos.toByteArray()), connectionEndpoint));
		ChallReq challReq = (ChallReq) ois.readObject();
		assertEquals("TestUser", challReq.getSource());
		assertEquals("Gamemaster", challReq.getTo());
		assertEquals("TestChallenge", challReq.getChallengeName());
		handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), connectionEndpoint));
		String send = "TestChallenge not found";
		assertEquals(send, bos.toString());
	}

	@Test
	public void testHandleInputHeroClass() throws IOException {
		bos.reset();
		bis.reset();
		oos.writeObject(hero);
		oos.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(bos.toByteArray()), connectionEndpoint));
		assertEquals(hero.getName(), handler.getName(connectionEndpoint));
		assertEquals(1, handler.clients.size());
		Handler.Entry entry = handler.clients.elementAt(0);
		assertEquals(hero, entry.hero);
		assertEquals("TestUser", entry.name);
		assertEquals(connectionEndpoint, entry.ce);
	}

	@Test
	public void testIsGmConnected() {
		assertEquals(false, handler.isGmConnected());
		handler.addConnection(new ConnectionEndpoint(new Socket("localhost", 12345), this));
		handler.handleInput(new InputEvent(new UserEventFactory().createUserEvent(true, "Gamemaster"), connectionEndpoint));
		assertEquals(true, handler.isGmConnected());
	}

	@Test
	public void testSendAllSystemMsg() throws IOException {
		bos.reset();
		writer.println("TestMessage");
		writer.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(bos.toByteArray()), connectionEndpoint));
		for (int i = 0; i < handler.clients.size(); ++i) {
			Handler.Entry entry = handler.clients.elementAt(i);
			assertEquals("TestMessage", entry.ce.receiveMessage());
		}
	}

	@Test
	public void testSendAllStr() throws IOException {
		String message = "TestMessage";
		writer.println(message);
		writer.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), connectionEndpoint));
		assertEquals("TestUser: " + message, handler.clients.elementAt(0).ce.receiveMessage());
	}

	@Test
	public void testSendOneSystemMsg() throws IOException {
		String message = "TestMessage";
		for (int i = 1; i < handler.clients.size(); ++i) {
			handler.sendOneSystemMsg(handler.clients.elementAt(i).ce, message);
			assertEquals(message, handler.clients.elementAt(i).ce.receiveMessage());
		}
	}

	@Test
	public void testWhisper() throws IOException {
		Handler.Entry entry = new Handler.Entry();
		entry.ce = new ConnectionEndpoint(new Socket("localhost", 1234), this);
		entry.name = "TestUser1";
		handler.clients.addElement(entry);
		entry = new Handler.Entry();
		entry.ce = new ConnectionEndpoint(new Socket("localhost", 12345), this);
		entry.name = "TestUser2";
		handler.clients.addElement(entry);
		handler.handleInput(new InputEvent(new UserEventFactory().createUserEvent("/w TestUser2 TestMessage", connectionEndpoint), connectionEndpoint));
		assertEquals("TestUser1 whispers: TestMessage", handler.clients.elementAt(0).ce.receiveMessage());
		assertEquals("TestMessage", handler.clients.elementAt(1).ce.receiveMessage());
	}

	@Test
	public void testRollDice() throws IOException {
		writer.println("/roll 2d6 + 3");
		writer.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), connectionEndpoint));
		String send = handler.clients.elementAt(0).ce.receiveMessage();
		String[] parts = send.split(" --> ");
		int sum = Integer.parseInt(parts[1].split(" ")[0]) + Integer.parseInt(parts[1].split(" ")[1]) + 3;
		assertEquals(send, "TestUser: rolled /roll 2d6 + 3 --> " + sum);
	}

	@Test
	public void testRollDiceWithOneDie() throws IOException {
		writer.println("/roll d6");
		writer.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), connectionEndpoint));
		String send = handler.clients.elementAt(0).ce.receiveMessage();
		assertEquals(send, "TestUser: rolled /roll d6 --> " + Challenge.dice(6));
	}

	@Test
	public void testRollDiceWithMultipleDice() throws IOException {
		writer.println("/roll 4d6");
		writer.flush();
		handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), connectionEndpoint));
		String send = handler.clients.elementAt(0).ce.receiveMessage();
		String[] parts = send.split(" --> ");
		List<Integer> rolls = new ArrayList<>();
		for (int i = 0; i < 4; ++i) {
			rolls.add(Challenge.dice(6));
		}
		String expected = "TestUser: rolled /roll 4d6 --> ";
		String actual = send.substring(expected.length());
		assertEquals(expected + rolls.toString(), send);
	}

	@Test
	public void testSendAllObj() throws IOException {
		List<Hero> heroes = new ArrayList<>();
		heroes.add(hero);
		handler.handleInput(new InputEvent(new ByteArrayInputStream(new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(heroes).toByteArray()), connectionEndpoint));
		assertEquals(heroes, handler.clients.elementAt(0).heroList);
	}
}

class ConnectionEndpoint implements InputListener {
	private Socket socket;
	private HandlerTest handlerTest;

	public ConnectionEndpoint(Socket socket, HandlerTest handlerTest) {
		this.socket = socket;
		this.handlerTest = handlerTest;
	}

	public void send(String message) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(message);
	}

	public void send(Object object) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(object);
		out.flush();
	}

	public int getClientPort() {
		return socket.getPort();
	}

	public String receiveMessage() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		return new String(bos.toByteArray());
	}

	public void addHandler(Handler handler) {
		handlerTest.handler = handler;
	}
}
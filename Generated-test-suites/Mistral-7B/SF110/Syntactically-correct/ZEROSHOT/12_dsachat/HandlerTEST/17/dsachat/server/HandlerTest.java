package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.server.Hero;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;
import dsachat.share.UserEvent;

@RunWith(Parameterized.class)
public class HandlerTest {

	private Handler handler;
	private Entry entry;
	private ConnectionEndpoint connectionEndpoint;
	private Hero hero;

	public HandlerTest(ConnectionEndpoint connectionEndpoint) {
		handler = new Handler();
		entry = new Entry();
		this.connectionEndpoint = connectionEndpoint;
	}

	@Parameters
	public static Collection<ConnectionEndpoint> data() {
		return Arrays.asList(new ConnectionEndpoint(0), new ConnectionEndpoint(1), new ConnectionEndpoint(2));
	}

	@Before
	public void setUp() {
		handler.addConnection(connectionEndpoint);
		hero = new Hero("TestHero");
	}

	@Test
	public void testAddConnection() {
		assertEquals(1, handler.clients.size());
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(new ConnectionEndpoint(3));
		handler.removeConnection(connectionEndpoint);
		assertEquals(1, handler.clients.size());
		assertNotEquals(connectionEndpoint, handler.clients.firstElement().ce);
	}

	@Test
	public void testHandleInputString() {
		String msg = "/me dancing";
		handler.handleInput(new InputEvent(new StringEvent(msg), connectionEndpoint));
		assertEquals("*TestHero: dancing", handler.clients.firstElement().name);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		handler.handleInput(new InputEvent(new StringEvent(msg), connectionEndpoint));
		assertEquals("TestHero danced", sw.toString());
	}

	@Test
	public void testHandleInputChallenge() {
		Challenge challenge = new Challenge();
		handler.handleInput(new InputEvent(new ChallengeEvent(challenge), connectionEndpoint));
		assertEquals(challenge.roll(), handler.clients.firstElement().ce.getLastDataSent());
	}

	@Test
	public void testHandleInputChallReq() {
		ChallReq challReq = new ChallReq("test", new Challenge(3, 6, true));
		handler.handleInput(new InputEvent(new ChallReqEvent(challReq), connectionEndpoint));
		assertEquals(challReq.to, handler.clients.firstElement().name);
		assertEquals(challReq.challenge, handler.clients.get(1).ce.getLastDataSent());
		assertEquals(challReq.challenge.roll(), handler.clients.get(1).ce.getLastDataSent());
	}

	@Test
	public void testHandleInputHero() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		try {
			out.writeObject(hero);
			out.close();
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bis);
			Hero heroFromInput = (Hero) in.readObject();
			in.close();
			inputStreamToEvent(bis).setData(heroFromInput);
			handler.handleInput(inputStreamToEvent(bis), connectionEndpoint);
			assertEquals(hero.getName(), handler.clients.firstElement().name);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHandleInputUserEvent() {
		String name = "TestUser";
		UserEvent userEvent = new UserEvent(true, name);
		handler.handleInput(new InputEvent(userEvent, connectionEndpoint));
		assertEquals(name, handler.clients.get(1).name);
	}

	@Test
	public void testSendAllObj() {
		UserEvent userEvent = new UserEvent(true, "TestUser2");
		handler.sendAllObj(userEvent);
		for (int i = 0; i < handler.clients.size(); i++) {
			assertEquals(userEvent, handler.clients.get(i).ce.getLastDataSent());
		}
	}

	@Test
	public void testSendAllStr() {
		String msg = "TestMessage";
		handler.sendAllStr(connectionEndpoint, msg);
		for (int i = 0; i < handler.clients.size(); i++) {
			assertEquals(msg, handler.clients.get(i).ce.getLastDataSent());
		}
	}

	@Test
	public void testSendOneSystemMsg() {
		String msg = "TestMessage";
		handler.sendOneSystemMsg(connectionEndpoint, msg);
		assertEquals(msg, handler.clients.firstElement().ce.getLastDataSent());
	}

	@Test
	public void testWhisper() {
		String toName = "TestUser3";
		handler.addConnection(new ConnectionEndpoint(3));
		handler.clients.get(2).name = toName;
		String msg = "TestMessage";
		handler.whisper(connectionEndpoint, toName, msg);
		assertEquals(msg, handler.clients.get(1).ce.getLastDataSent());
		assertEquals(msg, handler.clients.get(2).ce.getLastDataSent());
	}

	@Test
	public void testRollDice() {
		String cmd = "2d6+3";
		handler.handleInput(new InputEvent(new StringEvent(cmd), connectionEndpoint));
		assertEquals("TestHero rolled 2d6+3 --> 7", handler.clients.firstElement().ce.getLastDataSent());
		cmd = "3d6";
		handler.handleInput(new InputEvent(new StringEvent(cmd), connectionEndpoint));
		assertEquals("TestHero rolls 3d6 --> 3 5 1", handler.clients.firstElement().ce.getLastDataSent());
	}

	@Test
	public void testIsGmConnected() {
		handler.addConnection(new ConnectionEndpoint(3));
		handler.clients.get(2).name = "Gamemaster";
		assertTrue(handler.isGmConnected());
	}

	private InputEvent inputStreamToEvent(ByteArrayInputStream bis) {
		byte[] data = new byte[bis.available()];
		bis.read(data);
		byte[] dataBytes = Arrays.copyOfRange(data, 0, data.length);
		return new InputEvent(new ByteArrayInputStream(dataBytes), connectionEndpoint);
	}
}

class ConnectionEndpoint {
	int clientPort;
	String name;

	public ConnectionEndpoint(int clientPort) {
		this.clientPort = clientPort;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void send(Object data) {
		// Implement actual sending logic here
	}

	public byte[] getLastDataSent() {
		// Implement logic to get the last sent data
		return new byte[0];
	}
}

class Entry {
	ConnectionEndpoint ce;
	Hero hero;
	String name;

	public Entry() {
		ce = new ConnectionEndpoint(0);
	}
}

class StringEvent extends InputEvent {
	String data;

	public StringEvent(String data) {
		this.data = data;
	}

	@Override
	public Object getData() {
		return data;
	}
}

class ByteArrayInputStreamEvent extends InputEvent {
	ByteArrayInputStream bis;

	public ByteArrayInputStreamEvent(ByteArrayInputStream bis) {
		this.bis = bis;
	}

	@Override
	public Object getData() {
		return bis;
	}
}

class ChallengeEvent extends InputEvent {
	Challenge challenge;

	public ChallengeEvent(Challenge challenge) {
		this.challenge = challenge;
	}

	@Override
	public Object getData() {
		return challenge;
	}
}

class ChallReqEvent extends InputEvent {
	ChallReq challReq;

	public ChallReqEvent(ChallReq challReq) {
		this.challReq = challReq;
	}

	@Override
	public Object getData() {
		return challReq;
	}
}
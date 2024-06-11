package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Vector;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.share.hero.HeroFactory;

public class HandlerTest {

	@org.junit.Before
	public void setUp() {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.addConnection(createMockConnection(2));
		handler.addConnection(createMockConnection(3));
		handler.addConnection(createMockGmConnection(4));
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
	}

	@org.junit.Test
	public void testAddConnection() {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		assertEquals(4, handler.clients.size());
	}

	@org.junit.Test
	public void testRemoveConnection() {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.addConnection(createMockConnection(2));
		handler.removeConnection(createMockConnection(1));
		assertEquals(3, handler.clients.size());
		assertEquals("1", handler.getName(createMockConnection(1)));
		assertEquals("Gamemaster", handler.getName(createMockGmConnection(4)));
		assertEquals(null, handler.getName(createMockConnection(5)));
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		assertEquals("Gamemaster", handler.clients.get(3).name);
	}

	@org.junit.Test
	public void testHandleInputStringCmd() throws IOException {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.handleInput(new InputEvent(createMockConnection(1), new String("/roll 2d6 +2")));
		assertEquals("1: rolled 2d6 +2 --> 5 6", handler.getLastSystemMessage());
		handler.handleInput(new InputEvent(createMockConnection(1), new String("/roll 2d6")));
		assertEquals("1: rolls 2d6 --> 3 5", handler.getLastSystemMessage());
		handler.handleInput(new InputEvent(createMockConnection(1), new String("/getheros")));
		assertEquals(Arrays.toString(handler.heroes), handler.getLastSystemMessage().substring(1).replaceAll("\\[|\\]", "").split(" "));
	}

	@org.junit.Test
	public void testHandleInputStringWhisper() throws IOException {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.addConnection(createMockConnection(2));
		handler.handleInput(new InputEvent(createMockConnection(1), new String("/wisper 2 hello")));
		assertEquals("1 whispers to 2: hello", handler.getLastSystemMessage());
		assertEquals("You whisper to 2: hello", handler.getLastSystemMessageFromClient(createMockConnection(1)));
		assertEquals("2: You were whispered: hello", handler.getLastSystemMessageFromClient(createMockConnection(2)));
	}

	@org.junit.Test
	public void testHandleInputChallenge() throws IOException {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.handleInput(new InputEvent(createMockConnection(1), new Challenge("1", 6, false)));
		assertEquals("1: challenge 1 with result 6", handler.getLastSystemMessage());
		assertEquals("1: challenge 1 (roll result: 2)", handler.getLastSystemMessageFromClient(createMockConnection(1)));
		assertEquals("1: challenge 1 (roll result: 4)", handler.getLastSystemMessageFromClient(createMockConnection(1)));
		assertEquals("1: challenge 1 with result 6", handler.getLastSystemMessage());
		handler.handleInput(new InputEvent(createMockConnection(2), new Challenge("1", 6, true)));
		assertEquals("1: challenge 1 with result 6 (silent)", handler.getLastSystemMessage());
		assertEquals(null, handler.getLastSystemMessageFromClient(createMockConnection(2)));
	}

	@org.junit.Test
	public void testHandleInputChallReq() throws IOException {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.addConnection(createMockConnection(2));
		handler.handleInput(new InputEvent(createMockConnection(1), new ChallReq("2", "hello")));
		assertEquals("2: challenge from 1: hello", handler.getLastSystemMessageFromClient(createMockConnection(2)));
		assertEquals("2: You were challenged by 1: hello", handler.getLastSystemMessageFromClient(createMockConnection(1)));
		assertEquals("2: challenge from 1: hello", handler.getLastSystemMessage());
	}

	@org.junit.Test
	public void testUpdateHero() throws IOException {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		Hero hero = new HeroFactory().createHero("test");
		handler.handleInput(new InputEvent(createMockConnection(1), hero));
		assertEquals(hero.getName(), handler.getName(createMockConnection(1)));
		assertEquals(hero, handler.getHero(createMockConnection(1)));
	}

	@org.junit.Test
	public void testHandleInputUserEvent() throws IOException {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.addConnection(createMockConnection(2));
		handler.handleInput(new InputEvent(createMockConnection(1), new UserEvent(true, "test")));
		assertEquals("test connected", handler.getLastSystemMessage());
		assertEquals("test: welcome test", handler.getLastSystemMessageFromClient(createMockConnection(1)));
	}

	private ConnectionEndpoint createMockConnection(int clientPort) {
		return new ConnectionEndpoint() {
			private PrintStream out = new PrintStream(new ByteArrayOutputStream());

			@Override
			public int getClientPort() {
				return clientPort;
			}

			@Override
			public void send(Object data) throws IOException {
				out.println(data);
			}

			@Override
			public InputStream getInputStream() {
				return System.in;
			}
		};
	}

	private ConnectionEndpoint createMockGmConnection(int clientPort) {
		return new ConnectionEndpoint() {
			private boolean isGmConnected = true;

			@Override
			public int getClientPort() {
				return clientPort;
			}

			@Override
			public void send(Object data) throws IOException {
				// not used in test
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(new byte[0]);
			}

			@Override
			public boolean isGmConnected() {
				return isGmConnected;
			}
		};
	}

	private String getLastSystemMessage() {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		String lastMessage = handler.getLastSystemMessage();
		handler.handleInput(new InputEvent(createMockConnection(1), new String("/roll 2d6 +2")));
		return lastMessage;
	}

	private String getLastSystemMessageFromClient(ConnectionEndpoint con) {
		Handler handler = new Handler();
		handler.addConnection(con);
		String lastMessage = handler.getLastSystemMessageFromClient(con);
		handler.handleInput(new InputEvent(con, new String("/roll 2d6 +2")));
		return lastMessage;
	}

	private Vector<Hero> getHeroes() {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.handleInput(new InputEvent(createMockConnection(1), new HeroFactory().createHero("test1")));
		handler.handleInput(new InputEvent(createMockConnection(1), new HeroFactory().createHero("test2")));
		return handler.heroes;
	}

	private Vector<Hero> heroes = null;

	@org.junit.Before
	public void beforeEachTest() {
		Handler handler = new Handler();
		handler.addConnection(createMockConnection(1));
		handler.addConnection(createMockConnection(2));
		handler.addConnection(createMockGmConnection(3));
		handler.sendAllObj(new UserEvent(true, "Gamemaster"));
		heroes = handler.heroes;
	}

	private static class ConnectionEndpoint {
		private int clientPort;
		private Entry entry;
		private Vector<Entry> clients;

		public int getClientPort() {
			return clientPort;
		}

		public void send(Object data) throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			oos.close();
			baos.close();
			byte[] buf = baos.toByteArray();
			for (Entry e : clients) {
				e.ce.send(new InputEvent(this, new ByteArrayInputStream(buf)));
			}
		}

		public InputStream getInputStream() {
			return new ByteArrayInputStream(new byte[0]);
		}

		public void addConnection(ConnectionEndpoint cl) {
			clients = new Vector<Entry>();
			clients.add(new Entry(cl, ""));
			this.clientPort = cl.getClientPort();
		}

		public void removeConnection(ConnectionEndpoint cl) {
			clients.removeIf(e -> e.ce.getClientPort() == cl.getClientPort());
		}

		public String getName(ConnectionEndpoint con) {
			for (Entry e : clients) {
				if (e.ce.getClientPort() == con.getClientPort()) {
					return e.name;
				}
			}
			return null;
		}

		private boolean isGmConnected() {
			for (Entry e : clients) {
				if ("Gamemaster".equals(e.name)) {
					return true;
				}
			}
			return false;
		}
	}

	private static class Entry {
		private ConnectionEndpoint ce;
		private String name;

		public Entry() {
			this.ce = new ConnectionEndpoint();
		}

		public Entry(ConnectionEndpoint cl, String name) {
			this.ce = cl;
			this.name = name;
		}
	}
}
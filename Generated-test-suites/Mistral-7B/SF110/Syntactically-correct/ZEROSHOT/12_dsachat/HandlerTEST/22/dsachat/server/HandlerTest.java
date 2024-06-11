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

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

	private Handler handler;
	private ConnectionEndpoint mockConnection;
	private Hero mockHero;

	@Before
	public void setup() {
		handler = new Handler();
		mockConnection = new ConnectionEndpoint(1, "localhost", 1234);
		mockHero = new Hero("TestHero", 10, 10, 10, 10, 10);
	}

	@Test
	public void testAddConnection() {
		handler.addConnection(mockConnection);
		assertEquals(1, handler.clients.size());
	}

	@Test(expected = IOException.class)
	public void testAddConnection_ExceptionThrown() throws IOException {
		MockPrintStream errStream = new MockPrintStream();
		System.setErr(errStream);
		handler.addConnection(mockConnection);
		assertEquals("IOException thrown in test: IOException", errStream.getInput());
		System.setErr(System.err);
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(mockConnection);
		handler.removeConnection(mockConnection);
		assertEquals(0, handler.clients.size());
	}

	@Test
	public void testHandleInput_String() {
		handler.addConnection(mockConnection);
		handler.handleInput(new InputEvent(mockConnection, new String("/test")));
		assertEquals(1, handler.clients.size());
	}

	@Test
	public void testHandleInput_Challenge() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new Challenge(1, null, null, null, null, null, null, null, false));
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, null));
		assertEquals(1, handler.clients.size());
	}

	@Test
	public void testHandleInput_ChallReq() {
		handler.addConnection(mockConnection);
		handler.addConnection(new ConnectionEndpoint(2, "localhost", 5678));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new ChallReq("TestUser", "TestUser2", "testMessage"));
		mockConnection.setName("TestUser");
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, null));
		assertEquals(2, handler.clients.size());
	}

	@Test
	public void testHandleInput_Hero() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(mockHero);
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, null));
		assertEquals(1, handler.clients.size());
		assertEquals(mockHero, handler.clients.elementAt(0).hero);
	}

	@Test
	public void testHandleInput_UserEvent() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new UserEvent(true, "TestUser"));
		mockConnection.setName("TestUser");
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, null));
		assertEquals(1, handler.clients.size());
		assertEquals("TestUser", handler.getName(mockConnection));
	}

	@Test
	public void testIsGmConnected() {
		handler.addConnection(new ConnectionEndpoint(1, "localhost", 1234));
		handler.addConnection(new ConnectionEndpoint(2, "localhost", 5678));
		handler.addConnection(new ConnectionEndpoint(3, "localhost", 8765));
		handler.clients.elementAt(1).name = "Gamemaster";
		assertTrue(handler.isGmConnected());
		handler.clients.elementAt(1).name = "";
		assertFalse(handler.isGmConnected());
	}

	@Test
	public void testSendHeros() {
		handler.addConnection(mockConnection);
		Handler.Entry entry1 = new Handler.Entry();
		Handler.Entry entry2 = new Handler.Entry();
		entry1.hero = new Hero("TestHero1", 1, 1, 1, 1, 1);
		entry2.hero = new Hero("TestHero2", 2, 2, 2, 2, 2);
		handler.clients.addElement(entry1);
		handler.clients.addElement(entry2);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new Vector<Hero>());
		handler.handleInput(new InputEvent(mockConnection, null));
		handler.sendHeros(mockConnection);
		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		ObjectOutputStream oos2 = new ObjectOutputStream(bos2);
		oos2.writeObject(new Vector<Hero>());
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
		MockPrintStream outStream = new MockPrintStream();
		handler.setOutput(outStream);
		handler.handleInput(new InputEvent(mockConnection, inputStream));
		assertEquals(bos2.toByteArray(), handler.getOutput().toByteArray());
	}

	@Test
	public void testSendToOneStr() {
		handler.addConnection(mockConnection);
		handler.addConnection(new ConnectionEndpoint(2, "localhost", 5678));
		handler.handleInput(new InputEvent(mockConnection, new String("/whisper TestUser2 testMessage")));
		assertEquals("TestUser: testMessage", handler.getOutput().toString());
	}

	@Test
	public void testRollDice() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new String("TestUser rolled 1d6+2 --> 3"));
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, new String("/roll 1d6+2")));
		assertEquals("TestUser rolled 1d6+2 --> 3", handler.getOutput().toString());
	}

	@Test
	public void testRollDice_MultipleDice() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new String("TestUser rolled 2d6 --> [3, 5]"));
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, new String("/roll 2d6")));
		assertEquals("TestUser rolled 2d6 --> [3, 5]", handler.getOutput().toString());
	}

	@Test
	public void testRollDice_InvalidCommand() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new String("TestUser rolled command not found"));
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, new String("/roll test")));
		assertEquals("TestUser rolled command not found", handler.getOutput().toString());
	}

	@Test
	public void testUpdateHero() {
		handler.addConnection(mockConnection);
		handler.handleInput(new InputEvent(mockConnection, mockHero));
		assertEquals("TestHero", handler.getName(mockConnection));
		assertEquals(mockHero, handler.clients.elementAt(0).hero);
	}

	@Test
	public void testSendAllStr() {
		handler.addConnection(mockConnection);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new String("TestUser: testMessage"));
		mockConnection.setInput(new ByteArrayInputStream(bos.toByteArray()));
		handler.handleInput(new InputEvent(mockConnection, null));
		assertEquals("TestUser: testMessage", handler.getOutput().toString());
	}

	@Test
	public void testSendAllObj() {
		handler.addConnection(mockConnection);
		handler.handleInput(new InputEvent(mockConnection, new UserEvent(false, "TestUser")));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(new UserEvent(false, "TestUser"));
		MockPrintStream outStream = new MockPrintStream();
		handler.setOutput(outStream);
		handler.handleInput(new InputEvent(mockConnection, null));
		assertEquals(bos.toByteArray(), handler.getOutput().toByteArray());
	}

	private static class MockPrintStream extends PrintWriter {

		private String input;

		public MockPrintStream() {
			super(new StringWriter());
		}

		public String getInput() {
			return input;
		}

		@Override
		public void println(String message) {
			input += message;
		}
	}

	private static class MockConnection implements ConnectionEndpoint {

		private int clientPort;
		private String host;
		private boolean isConnected;
		private String name;
		private Handler.Entry entry;
		private ByteArrayInputStream inputStream;
		private ByteArrayOutputStream outputStream;
		private PrintWriter output;

		public MockConnection(int clientPort, String host) {
			this.clientPort = clientPort;
			this.host = host;
		}

		@Override
		public int getClientPort() {
			return clientPort;
		}

		@Override
		public String getHost() {
			return host;
		}

		@Override
		public boolean isConnected() {
			return isConnected;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setInput(ByteArrayInputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public InputStream getInput() {
			return inputStream;
		}

		@Override
		public void setOutput(PrintWriter output) {
			this.output = output;
		}

		@Override
		public OutputStream getOutput() {
			return new ByteArrayOutputStream();
		}

		@Override
		public void send(Object object) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			this.outputStream = new ByteArrayOutputStream();
			this.output.println(new String(bos.toByteArray()));
			this.isConnected = true;
		}
	}
}
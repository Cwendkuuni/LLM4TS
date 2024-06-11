package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.hero.Hero;
import dsachat.server.Handler;
import dsachat.server.HandlerTest;
import dsachat.server.InputEvent;
import dsachat.server.StringInputEvent;
import dsachat.server.UserEvent;

public class HandlerTest {
	private Handler handler;
	private Socket socket;
	private ConnectionEndpoint connectionEndpoint;
	private InputStream input;
	private OutputStream output;
	private ByteArrayOutputStream outputStream;
	private ObjectOutputStream objectOutput;
	private Vector<Entry> clients;
	private int clientPort;

	@Before
	public void setUp() throws IOException {
		handler = new Handler();
		socket = new Socket("localhost", 0);
		connectionEndpoint = new ConnectionEndpoint(socket, "127.0.0.1", socket.getPort());
		input = socket.getInputStream();
		output = socket.getOutputStream();
		outputStream = new ByteArrayOutputStream();
		objectOutput = new ObjectOutputStream(outputStream);
		handler.addConnection(connectionEndpoint);
		clients = handler.getClients();
		clientPort = socket.getPort();
	}

	@After
	public void tearDown() throws IOException {
		handler.removeConnection(connectionEndpoint);
		socket.close();
	}

	@Test
	public void testAddConnection() {
		Handler handlerSpy = new SpyHandler(handler);
		handlerSpy.addConnection(connectionEndpoint);
		assertEquals(1, clients.size());
		assertEquals("", clients.elementAt(0).name);
	}

	@Test(expected = IOException.class)
	public void testAddConnectionWithException() throws IOException {
		final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
		PrintWriter errorWriter = new PrintWriter(errorStream);
		System.setErr(errorWriter);
		Handler handlerSpy = spy(Handler.class);
		doThrow(IOException.class).when(handlerSpy).addConnection(connectionEndpoint);
		handlerSpy.addConnection(connectionEndpoint);
		assertEquals(1, clients.size());
		assertEquals("", clients.elementAt(0).name);
		assertEquals("IOException\n", errorStream.toString());
	}

	@Test
	public void testRemoveConnection() {
		Handler handlerSpy = new SpyHandler(handler);
		handlerSpy.addConnection(connectionEndpoint);
		handlerSpy.removeConnection(connectionEndpoint);
		assertEquals(0, clients.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveConnectionWithNull() {
		Handler handlerSpy = spy(Handler.class);
		handlerSpy.removeConnection(null);
	}

	@Test
	public void testHandleInput_String() {
		Handler handlerSpy = new SpyHandler(handler);
		String msg = "/w user Hello";
		handleInput_TestData(handlerSpy, msg, "user", "Hello");
	}

	@Test
	public void testHandleInput_Challenge() {
		Handler handlerSpy = new SpyHandler(handler);
		final Challenge challenge = new Challenge(1, "Gamemaster", 6, false);
		handleInput_TestData(handlerSpy, challenge, "Gamemaster", null);
	}

	@Test
	public void testHandleInput_ChallReq() {
		Handler handlerSpy = new SpyHandler(handler);
		final String name = "user";
		final ChallReq challReq = new ChallReq(name, 1, 6, true);
		handleInput_TestData(handlerSpy, challReq, null, name);
	}

	@Test
	public void testHandleInput_Hero() {
		Handler handlerSpy = new SpyHandler(handler);
		final Hero hero = new Hero("user", 1);
		handleInput_TestData(handlerSpy, hero, "user", hero);
	}

	@Test
	public void testHandleInput_UserEvent() {
		Handler handlerSpy = new SpyHandler(handler);
		final String name = "user";
		final UserEvent userEvent = new UserEvent(true, name);
		handleInput_TestData(handlerSpy, userEvent, null, name);
	}

	private void handleInput_TestData(Handler handlerSpy, Object obj, String expectedName, Object expectedObj) {
		Handler handlerMock = mock(Handler.class);
		when(handlerMock.getName(connectionEndpoint)).thenReturn(expectedName);
		when(handlerMock.handleInput(anyObject())).thenReturn(true);

		doAnswer(invocationOnMock(handlerSpy)).when(handlerMock).handleInput(eq(obj));

		handlerSpy.setHandler(handlerMock);

		handleInput(handlerSpy, obj);

		if (expectedObj != null) {
			assertEquals(expectedObj.getClass().getName(), handlerSpy.getLastObjectReceived().getClass().getName());
			assertEquals(expectedObj, handlerSpy.getLastObjectReceived());
		}
	}

	private void handleInput(Handler handlerSpy, Object obj) {
		try {
			ObjectInputStream objInputStream = new ObjectInputStream(input);
			obj = objInputStream.readObject();
			handlerSpy.handleInput(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static class SpyHandler extends Handler {
		private Handler handler;

		public SpyHandler(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void handleInput(Object obj) {
			super.handleInput(obj);
			HandlerTest.this.lastObjectReceived = obj;
		}
	}

	private Object lastObjectReceived;

	private void sendMessage(Handler handlerSpy, String message) throws IOException {
		StringInputEvent stringInputEvent = new StringInputEvent(message);
		handlerSpy.handleInput(stringInputEvent);
		String msg = "";
		try {
			msg = (String) objectOutput.readObject();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		assertEquals(message, msg);
	}

	@Test
	public void testWhisper() throws IOException {
		Handler handlerSpy = new SpyHandler(handler);
		handleInput_TestData(handlerSpy, new StringInputEvent("/w user Hello"), "user", null);
		sendMessage(handlerSpy, "/w user2 Hello2");
		assertEquals("user: Hello\nuser2: Hello2\n", outputStream.toString());
	}

	@Test
	public void testRollDice() throws IOException {
		Handler handlerSpy = new SpyHandler(handler);
		handleInput_TestData(handlerSpy, new StringInputEvent("/roll 2d6"), null, null);
		sendMessage(handlerSpy, 4);
		sendMessage(handlerSpy, 5);
		assertEquals("2d6 rolled --> 4 5\n", outputStream.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRollDiceInvalidFormat() throws IOException {
		Handler handlerSpy = new SpyHandler(handler);
		handleInput_TestData(handlerSpy, new StringInputEvent("/roll 2d10+5"), null, null);
		assertEquals("Invalid format: 2d10+5\n", outputStream.toString());
	}

	@Test
	public void testRollDiceSingleDice() throws IOException {
		Handler handlerSpy = new SpyHandler(handler);
		handleInput_TestData(handlerSpy, new StringInputEvent("/roll d6"), null, null);
		sendMessage(handlerSpy, 3);
		assertEquals("d6 rolled --> 3\n", outputStream.toString());
	}
}
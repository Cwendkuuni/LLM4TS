package dsachat.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

	private Handler handler;
	private ConnectionEndpoint mockConnection;
	private Vector<Entry> clients;
	private Hero hero;

	@Before
	public void setUp() {
		handler = new Handler();
		mockConnection = mock(ConnectionEndpoint.class);
		clients = new Vector<Entry>();
		hero = new Hero("testHero");
	}

	@Test
	public void testAddConnection() throws IOException {
		handler.addConnection(mockConnection);
		Entry expectedEntry = new Entry();
		expectedEntry.ce = mockConnection;
		expectedEntry.name = "";
		assertEquals(expectedEntry, handler.clients.lastElement());
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(mockConnection);
		handler.removeConnection(mockConnection);
		assertEquals(0, handler.clients.size());

		// Test removing a non-existent connection
		handler.addConnection(mockConnection);
		handler.removeConnection(new ConnectionEndpoint(1234));
		assertEquals(1, handler.clients.size());
	}

	@Test
	public void testHandleInputString() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(hero);
		objectOutputStream.close();
		String inputString = new String(byteArrayOutputStream.toByteArray());

		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));
		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));
		assertEquals(hero.getName(), handler.updateHero(mockConnection, hero).toString());
	}

	@Test
	public void testHandleInputChallenge() throws IOException {
		Challenge challenge = mock(Challenge.class);
		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ challenge }));

		String expectedSend = challenge.roll();
		String to = challenge.getTo();
		if (to != null) {
			for (int i = 0; i < handler.clients.size(); ++i) {
				if (handler.clients.elementAt(i).name.equals(to)) {
					handler.sendOneSystemMsg(handler.clients.elementAt(i).ce, expectedSend);
					break;
				}
			}
		}
		handler.sendOneSystemMsg(mockConnection, expectedSend);
	}

	@Test
	public void testHandleInputChallReq() throws IOException {
		String to = "testTo";
		String inputString = "/w " + to + " hello";
		ChallReq challReq = mock(ChallReq.class);
		when(mockConnection.send(eq(ChallReq.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));
		handler.clients.get(0).ce.send(challReq);
	}

	@Test
	public void testHandleInputUserEvent() {
		String inputString = "me this is a test";
		UserEvent userEvent = new UserEvent(true, "testName");
		when(mockConnection.send(eq(UserEvent.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));
		handler.handleInput(new InputEvent(mockConnection, new Object[]{ userEvent }));

		assertEquals("testName: this is a test", handler.clients.get(0).ce.getLastSentMessage());
		assertEquals("*testName: this is a test", handler.clients.get(1).ce.getLastSentMessage());
	}

	@Test
	public void testHandleInputInvalidCommand() throws IOException {
		String inputString = "/invalidCommand";
		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));

		assertEquals("/invalidCommand not found", handler.clients.get(0).ce.getLastSentMessage());
	}

	@Test
	public void testSendAllStr() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject("testString");
		objectOutputStream.close();
		String inputString = new String(byteArrayOutputStream.toByteArray());

		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));
		handler.sendAllStr(mockConnection, "testString");

		assertEquals("testName: testString", handler.clients.get(0).ce.getLastSentMessage());
		assertEquals("*testName: testString", handler.clients.get(1).ce.getLastSentMessage());
	}

	@Test
	public void testSendAllObj() throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(hero);
		objectOutputStream.close();
		String inputString = new String(byteArrayOutputStream.toByteArray());

		when(mockConnection.send(eq(Hero.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));
		handler.sendAllObj(hero);

		assertEquals(hero.getName(), handler.clients.get(0).hero.getName());
		assertEquals(hero, handler.clients.get(1).hero);
	}

	@Test
	public void testSendHeros() throws IOException {
		Vector<Hero> heros = new Vector<Hero>();
		heros.add(new Hero("testHero1"));
		heros.add(new Hero("testHero2"));
		when(mockConnection.send(eq(Vector.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{}));
		handler.sendHeros(mockConnection);

		assertEquals(Arrays.toString(heros.toArray()), handler.clients.get(0).ce.getLastSentMessage());
	}

	@Test
	public void testIsGmConnected() {
		handler.addConnection(mockConnection);
		assertNull(handler.isGmConnected());

		handler.handleInput(new InputEvent(mockConnection, new Object[]{"/connect Gamemaster"}));
		assertTrue(handler.isGmConnected());
	}

	@Test
	public void testSendOneSystemMsg() {
		String inputString = "/sys test";
		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));

		assertEquals("/sys test", handler.clients.get(0).ce.getLastSentMessage());
	}

	@Test
	public void testWhisper() {
		String inputString = "/w testTo testMessage";
		String to = "testTo";
		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));

		String sender = handler.getName(mockConnection);
		assertEquals(sender + ": testMessage", handler.clients.get(0).ce.getLastSentMessage());
		assertEquals("You whisper to testTo: testMessage", handler.clients.get(1).ce.getLastSentMessage());
	}

	@Test
	public void testRollDice() {
		String inputString = "/roll 2d6+3";
		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));

		assertEquals(handler.getName(mockConnection) + " rolled 2d6+3 --> ", handler.clients.get(0).ce.getLastSentMessage());
		assertEquals("3 (3d6)", handler.clients.get(1).ce.getLastSentMessage());
	}

	@Test
	public void testRollDiceNoMod() {
		String inputString = "/roll 2d6";
		when(mockConnection.send(eq(String.class))).thenReturn(new PrintWriter(mock(PrintWriter.class).mock()));

		handler.handleInput(new InputEvent(mockConnection, new Object[]{ inputString }));

		assertEquals(handler.getName(mockConnection) + " rolls 2d6 --> ", handler.clients.get(0).ce.getLastSentMessage());
		assertEquals("2 (2d6)", handler.clients.get(1).ce.getLastSentMessage());
		assertEquals("2 (2d6)", handler.clients.get(2).ce.getLastSentMessage());
	}

	private PrintWriter mockPrintWriter() {
		return mock(PrintWriter.class);
	}

	private ConnectionEndpoint mockConnection(int clientPort) {
		ConnectionEndpoint conn = mock(ConnectionEndpoint.class);
		when(conn.getClientPort()).thenReturn(clientPort);
		when(conn.send(eq(String.class))).thenAnswer(i -> {
			PrintWriter printWriter = mockPrintWriter();
			printWriter.println(i.getArgument(0).toString());
			return printWriter;
		});
		when(conn.send(eq(Object.class))).thenAnswer(i -> {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(i.getArgument(0));
			objectOutputStream.close();
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			printWriter.println(byteArrayOutputStream.toString());
			printWriter.flush();
			return printWriter;
		});
		when(conn.getLastSentMessage()).thenReturn(stringWriter.getBuffer().toString());
		return conn;
	}

	private static <T> T eq(T object) {
		return eq(object, null);
	}

	private static <T> T eq(T object, T other) {
		return org.mockito.ArgumentMatchers.eq(object, other);
	}
}
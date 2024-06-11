package dsachat.server.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Hero;

public class HandlerTest {

	private Handler handler;
	private ConnectionEndpoint connectionEndpoint;
	private Entry entry;
	private Hero hero;
	private UserEvent userEvent;
	private ChallReq challReq;
	private Challenge challenge;
	private byte[] expectedMessage;
	private InputStream expectedInputStream;
	private OutputStream expectedOutputStream;

	@Before
	public void setUp() {
		handler = new Handler();
		connectionEndpoint = mock(ConnectionEndpoint.class);
		entry = new Handler.Entry();
		entry.ce = connectionEndpoint;
		hero = new Hero("Test Hero");
		userEvent = new UserEvent(false, "Test User");
		challenge = mock(Challenge.class);
		challReq = new ChallReq("Test User", challenge);
		expectedMessage = "Test Message".getBytes();
		expectedInputStream = new ByteArrayInputStream(expectedMessage);
		expectedOutputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testAddConnection() {
		handler.addConnection(connectionEndpoint);
		assertTrue(handler.clients.contains(entry));
	}

	@Test
	public void testRemoveConnection() {
		handler.addConnection(connectionEndpoint);
		handler.removeConnection(connectionEndpoint);
		assertFalse(handler.clients.contains(entry));
	}

	@Test
	public void testHandleInput_StringMessage() throws IOException {
		when(connectionEndpoint.send(any(String.class))).thenReturn(true);

		handler.handleInput(new InputEvent(userEvent, connectionEndpoint));

		verify(connectionEndpoint, times(1)).send(eq("*" + userEvent.getName() + ": " + userEvent.getMessage()));
	}

	@Test
	public void testHandleInput_StringMessageWithMultipleClients() throws IOException {
		Vector<Entry> clients = new Vector<Entry>();
		Handler handler = new Handler();
		handler.clients = clients;
		ConnectionEndpoint client1 = mock(ConnectionEndpoint.class);
		ConnectionEndpoint client2 = mock(ConnectionEndpoint.class);
		Entry entry1 = new Handler.Entry();
		Entry entry2 = new Handler.Entry();
		entry1.ce = client1;
		entry1.name = "Client 1";
		entry2.ce = client2;
		entry2.name = "Client 2";
		clients.add(entry1);
		clients.add(entry2);

		when(client1.send(any(String.class))).thenReturn(true);
		when(client2.send(any(String.class))).thenReturn(true);

		handler.handleInput(new InputEvent(new UserEvent(false, "Test User"), connectionEndpoint));

		verify(client1, times(1)).send(eq("*Client 1: Test User: Test Message"));
		verify(client2, times(1)).send(eq("*Client 2: Test User: Test Message"));
	}

	@Test
	public void testHandleInput_Challenge() throws IOException {
		when(connectionEndpoint.send(any(String.class))).thenReturn(true);
		when(challenge.roll()).thenReturn("Roll Result");

		handler.handleInput(new InputEvent(challenge, connectionEndpoint));

		String expectedSend = "Roll Result";
		String expectedTo = null;
		handler.sendOneSystemMsg(connectionEndpoint, "Roll Result");
		verify(connectionEndpoint, times(1)).send(expectedMessage);
	}

	@Test
	public void testHandleInput_ChallReq() throws IOException {
		Vector<Entry> clients = new Vector<Entry>();
		Handler handler = new Handler();
		handler.clients = clients;
		ConnectionEndpoint recipientClient = mock(ConnectionEndpoint.class);
		Entry recipientEntry = new Handler.Entry();
		recipientEntry.ce = recipientClient;
		recipientEntry.name = "Recipient";
		clients.add(recipientEntry);

		when(recipientClient.send(any(String.class))).thenReturn(true);

		handler.handleInput(new InputEvent(challReq, connectionEndpoint));

		verify(recipientClient, times(1)).send(eq("Recipient: Roll Result"));
	}

	@Test
	public void testUpdateHero() {
		handler.updateHero(connectionEndpoint, hero);
		assertEquals(hero, entry.hero);
	}

	@Test
	public void testSendHeros() throws IOException {
		Vector<Hero> heroes = new Vector<Hero>();
		Hero hero1 = new Hero("Test Hero 1");
		Hero hero2 = new Hero("Test Hero 2");
		heroes.add(hero1);
		heroes.add(hero2);

		when(connectionEndpoint.send(any(Vector.class))).thenReturn(true);

		handler.sendHeros(connectionEndpoint);

		verify(connectionEndpoint, times(1)).send(heroes);
	}

	@Test
	public void testIsGmConnected() {
		assertFalse(handler.isGmConnected());
		Handler handler = new Handler();
		handler.clients.add(new Handler.Entry("Gamemaster", null));
		assertTrue(handler.isGmConnected());
	}

	@Test
	public void testSendAllSystemMsg() throws IOException {
		when(connectionEndpoint.send(any(String.class))).thenReturn(true);

		handler.sendAllSystemMsg("Test Message");

		verify(connectionEndpoint, times(handler.clients.size())).send(anyString());
	}

	@Test
	public void testWhisper() throws IOException {
		Vector<Entry> clients = new Vector<Entry>();
		Handler handler = new Handler();
		handler.clients = clients;
		ConnectionEndpoint senderClient = mock(ConnectionEndpoint.class);
		ConnectionEndpoint receiverClient = mock(ConnectionEndpoint.class);
		Entry senderEntry = new Handler.Entry();
		Entry receiverEntry = new Handler.Entry();
		senderEntry.ce = senderClient;
		receiverEntry.ce = receiverClient;
		senderEntry.name = "Sender";
		receiverEntry.name = "Receiver";
		clients.add(senderEntry);
		clients.add(receiverEntry);

		when(senderClient.send(any(String.class))).thenReturn(true);
		when(receiverClient.send(any(String.class))).thenReturn(true);

		handler.whisper(senderClient, "Receiver Test User: Test Message");

		verify(senderClient, times(1)).send(eq("Sender whispers: Test Message"));
		verify(receiverClient, times(1)).send(eq("You whisper from Sender: Test Message"));
	}

	@Test
	public void testRollDice_SingleRoll() throws IOException {
		when(connectionEndpoint.send(any(String.class))).thenReturn(true);

		handler.rollDice(connectionEndpoint, "1d6");

		verify(connectionEndpoint, times(1)).send(eq("Test User rolled 1d6 --> 3"));
	}

	@Test
	public void testRollDice_MultipleRolls() throws IOException {
		when(connectionEndpoint.send(any(String.class))).thenReturn(true);

		handler.rollDice(connectionEndpoint, "3d6");

		verify(connectionEndpoint, times(1)).send(eq("Test User rolled 3d6 --> 4 5 2"));
	}

	@Test
	public void testRollDice_RollWithModifier() throws IOException {
		when(connectionEndpoint.send(any(String.class))).thenReturn(true);

		handler.rollDice(connectionEndpoint, "1d6+2");

		verify(connectionEndpoint, times(1)).send(eq("Test User rolled 1d6+2 --> 4"));
	}

	@Test
	public void testSendToOneStr() throws IOException {
		Vector<Entry> clients = new Vector<Entry>();
		Handler handler = new Handler();
		handler.clients = clients;
		ConnectionEndpoint recipientClient = mock(ConnectionEndpoint.class);
		Entry recipientEntry = new Handler.Entry();
		recipientEntry.ce = recipientClient;
		recipientEntry.name = "Recipient";
		clients.add(recipientEntry);

		when(recipientClient.send(any(String.class))).thenReturn(true);

		handler.sendToOneStr(connectionEndpoint, "Recipient", "Test Message");

		verify(recipientClient, times(1)).send(eq("Recipient: Test Message"));
	}

	@Test
	public void testgetName() {
		assertNull(handler.getName(connectionEndpoint));

		Handler handler = new Handler();
		handler.clients.add(new Handler.Entry(null, connectionEndpoint));
		assertEquals(connectionEndpoint.getClientPort(), handler.clients.get(0).ce.getClientPort());
		assertEquals("Client " + connectionEndpoint.getClientPort(), handler.getName(connectionEndpoint));
	}
}
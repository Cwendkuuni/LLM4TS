package dsachat.server.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import dsachat.server.*;
import dsachat.share.*;
import dsachat.share.hero.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpointMock;
    private ConnectionEndpoint connectionEndpointMock2;
    private Vector<Entry> clients;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        clients = new Vector<>();
        connectionEndpointMock = Mockito.mock(ConnectionEndpoint.class);
        connectionEndpointMock2 = Mockito.mock(ConnectionEndpoint.class);
        handler.clients = clients;
    }

    @After
    public void tearDown() {
        handler = null;
        clients = null;
        connectionEndpointMock = null;
        connectionEndpointMock2 = null;
        hero = null;
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpointMock);
        assertEquals(1, clients.size());
        verify(connectionEndpointMock).send(any(UserEvent.class));
    }

    @Test(expected = Exception.class)
    public void testAddConnectionWithInvalidConnection() {
        doThrow(IOException.class).when(connectionEndpointMock).isConnected();
        handler.addConnection(connectionEndpointMock);
    }

    @Test
    public void testRemoveConnection() {
        clients.add(new Entry());
        clients.add(new Entry());
        Entry entryToRemove = clients.elementAt(0);
        handler.removeConnection(connectionEndpointMock);
        assertEquals(1, clients.size());
        assertNotEquals(entryToRemove.ce, clients.elementAt(0).ce);
        verify(connectionEndpointMock).getClientPort();
    }

    @Test
    public void testHandleInputStringMessage() {
        String name = "TestUser";
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(name, new Hero(name)));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        handler.handleInput(new InputEvent(connectionEndpointMock, "test message"));
        assertEquals(name, handler.getName(connectionEndpointMock));
        verify(connectionEndpointMock).send(anyString());
    }

    @Test
    public void testHandleInputChallenge() {
        String name = "TestUser";
        Hero hero = new Hero("TestUser");
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(name, hero));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        Challenge challenge = Mockito.mock(Challenge.class);
        handler.handleInput(new InputEvent(connectionEndpointMock, challenge));
        verify(challenge).roll();
        verify(connectionEndpointMock).send(anyString());
    }

    @Test
    public void testHandleInputChallengeWithSilent() {
        String name = "TestUser";
        Hero hero = new Hero("TestUser");
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(name, hero));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        Challenge challenge = Mockito.mock(Challenge.class);
        when(challenge.isSilent()).thenReturn(true);
        handler.handleInput(new InputEvent(connectionEndpointMock, challenge));
        verifyZeroInteractions(connectionEndpointMock);
    }

    @Test
    public void testHandleInputChallReq() {
        String name = "TestUser";
        String to = "AnotherUser";
       Handler handlerSpy = spy(handler);
        handlerSpy.addConnection(connectionEndpointMock);
        clients.add(new Entry(name, null));
        clients.add(new Entry(to, null));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock2.getClientPort()).thenReturn(54321);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        when(connectionEndpointMock2.isConnected()).thenReturn(true);
        handlerSpy.handleInput(new InputEvent(connectionEndpointMock, new ChallReq(to, "test")));
        handlerSpy.handleInput(new InputEvent(connectionEndpointMock2, new ChallReq(name, "test")));
        verify(connectionEndpointMock).send(any(ChallReq.class));
        verify(connectionEndpointMock2).send(any(ChallReq.class));
    }

    @Test
    public void testIsGmConnected() {
        when(clients.size()).thenReturn(1);
        assertFalse(handler.isGmConnected());

        Entry entry = new Entry();
        entry.name = "Gamemaster";
        clients.add(entry);

        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        String testMessage = "Test message";
        handler.sendAllSystemMsg(testMessage);
        for (int i = 0; i < clients.size(); ++i) {
            try {
                verify(clients.elementAt(i).ce).send(testMessage);
            } catch (IOException e) {
                fail("Error sending message to client");
            }
        }
    }

    @Test
    public void testWhisper() {
        String sender = "TestUser";
        String receiver = "AnotherUser";
        String testMessage = "Test message";
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(sender, null));
        clients.add(new Entry(receiver, null));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        handler.whisper(connectionEndpointMock, receiver, testMessage);
        verify(connectionEndpointMock).send(anyString());
        verify(clients.elementAt(1).ce).send(testMessage);
    }

    @Test
    public void testRollDice() {
        String sender = "TestUser";
        String testMessage = "TestUser rolled 4d6+3 --> 14";
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(sender, null));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        handler.rollDice(connectionEndpointMock, "4d6+3");
        handler.sendAllSystemMsg(testMessage);
        verify(connectionEndpointMock).send(testMessage);
    }

    @Test
    public void testRollDiceWithInvalidInput() {
        String sender = "TestUser";
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(sender, null));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        handler.rollDice(connectionEndpointMock, "invalidInput");
        verifyZeroInteractions(connectionEndpointMock);
    }

    @Test
    public void testUpdateHero() {
        String sender = "TestUser";
        Hero hero = new Hero("TestUser");
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(sender, null));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        Entry entry = new Entry();
        entry.ce = connectionEndpointMock;
        entry.hero = hero;
        entry.name = "TestUser";
        clients.add(entry);
        handler.updateHero(connectionEndpointMock, hero);
        assertEquals(hero, clients.elementAt(1).hero);
    }

    @Test
    public void testSendAllStr() {
        String sender = "TestUser";
        String testMessage = "Test message";
        handler.addConnection(connectionEndpointMock);
        clients.add(new Entry(sender, null));
        when(connectionEndpointMock.getClientPort()).thenReturn(12345);
        when(connectionEndpointMock.isConnected()).thenReturn(true);
        handler.sendAllStr(connectionEndpointMock, testMessage);
        for (int i = 0; i < clients.size(); ++i) {
            try {
                verify(clients.elementAt(i).ce).send(testMessage);
            } catch (IOException e) {
                fail("Error sending message to client");
            }
        }
    }

}
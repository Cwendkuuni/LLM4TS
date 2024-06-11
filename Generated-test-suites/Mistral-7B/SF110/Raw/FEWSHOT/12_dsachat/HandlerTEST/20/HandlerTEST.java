package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint mockConnectionEndpoint;
    private Entry mockEntry;
    private Vector<Entry> mockVector;

    @Before
    public void setUp() {
        mockConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        mockEntry = Mockito.mock(Entry.class);
        mockVector = Mockito.mock(Vector.class);
        handler = new Handler();
        handler.clients = mockVector;
    }

    @Test
    public void testAddConnection() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        handler.addConnection(mockConnectionEndpoint);
        verify(mockVector).addElement(mockEntry);
    }

    @Test(expected = IOException.class)
    public void testAddConnection_throwsIOException() {
        when(mockConnectionEndpoint.isConnected()).thenReturn(false);
        handler.addConnection(mockConnectionEndpoint);
    }

    @Test
    public void testRemoveConnection() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        handler.addConnection(mockConnectionEndpoint);
        handler.removeConnection(mockConnectionEndpoint);
        verify(mockVector).removeElementAt(0);
    }

    @Test
    public void testRemoveConnection_connectionNotFound() {
        handler.removeConnection(mockConnectionEndpoint);
        verifyZeroInteractions(mockVector);
    }

    @Test
    public void testHandleInput_stringMessage() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        String message = "/hello world";
        handler.handleInput(new InputEvent(mockConnectionEndpoint, message));
        verify(mockConnectionEndpoint).send(message);
        verify(mockVector).forEach(entry -> entry.ce.send(message));
    }

    @Test
    public void testHandleInput_challenge() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        Challenge mockChallenge = Mockito.mock(Challenge.class);
        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallenge));
        verify(mockChallenge).roll();
        String to = "user1";
        String send = mockChallenge.roll();
        when(mockChallenge.getTo()).thenReturn(to);
        when(mockChallenge.isSilent()).thenReturn(false);
        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallenge));
        verify(mockConnectionEndpoint).send(send);
        verify(mockVector).forEach(entry -> {
            if (entry.name.equals(to)) {
                entry.ce.send(send);
            }
        });
    }

    @Test
    public void testHandleInput_challReq() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        ChallReq mockChallReq = Mockito.mock(ChallReq.class);
        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallReq));
        String to = "user2";
        when(mockChallReq.getTo()).thenReturn(to);
        handler.addConnection(mockConnectionEndpoint);
        handler.clients.add(mockEntry);
        when(mockEntry.ce.getClientPort()).thenReturn(2);
        when(mockEntry.name).thenReturn("User2");
        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallReq));
        verify(mockConnectionEndpoint).send(mockChallReq);
        verify(mockEntry.ce).send(mockChallReq);
    }

    @Test
    public void testHandleInput_hero() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        Hero mockHero = Mockito.mock(Hero.class);
        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockHero));
        String name = "User1";
        when(mockHero.getName()).thenReturn(name);
        handler.addConnection(mockConnectionEndpoint);
        handler.clients.add(mockEntry);
        when(mockEntry.ce.getClientPort()).thenReturn(1);
        when(mockEntry.name).thenReturn("");
        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockHero));
        assertEquals(name, handler.getName(mockConnectionEndpoint));
    }

    @Test
    public void testHandleInput_gmConnection() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        UserEvent mockUserEvent = Mockito.mock(UserEvent.class);
        handler.handleInput(mockUserEvent);
        String name = "Gamemaster";
        handler.addConnection(mockConnectionEndpoint);
        handler.clients.add(mockEntry);
        when(mockEntry.name).thenReturn("");
        when(mockUserEvent.getName()).thenReturn(name);
        handler.handleInput(mockUserEvent);
        assertEquals(name, handler.getName(mockConnectionEndpoint));
    }

    @Test
    public void testSendHeros() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        Vector<Hero> mockHeroVector = Mockito.mock(Vector.class);
        handler.sendHeros(mockConnectionEndpoint);
        Mockito.verify(mockConnectionEndpoint).send(mockHeroVector);
    }

    @Test
    public void testIsGmConnected() {
        boolean result = handler.isGmConnected();
        assertFalse(result);
        handler.addConnection(mockEntry);
        handler.clients.add(mockEntry2);
        when(mockEntry.name).thenReturn("Gamemaster");
        when(mockEntry2.name).thenReturn("User1");
        result = handler.isGmConnected();
        assertTrue(result);
    }

    @Test
    public void testSendOneSystemMsg() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        String message = "System message";
        handler.sendOneSystemMsg(mockConnectionEndpoint, message);
        verify(mockConnectionEndpoint).send(message);
    }
}
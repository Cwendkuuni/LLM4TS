package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import dsachat.share.*;
import dsachat.share.hero.*;
import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private Vector<Entry> clients;
    private ConnectionEndpoint mockConnectionEndpoint;
    private Entry mockEntry;

    @Before
    public void setup() {
        handler = new Handler();
        clients = handler.clients;
        mockConnectionEndpoint = mock(ConnectionEndpoint.class);
        mockEntry = new Entry();
        mockEntry.ce = mockConnectionEndpoint;
    }

    @Test
    public void addConnection() {
        handler.addConnection(mockConnectionEndpoint);
        assertEquals(1, clients.size());
    }

    @Test
    public void removeConnection() {
        clients.add(mockEntry);
        handler.removeConnection(mockConnectionEndpoint);
        assertEquals(0, clients.size());

        mockConnectionEndpoint = mock(ConnectionEndpoint.class);
        mockEntry.name = "TestName";
        clients.add(mockEntry);
        String testName = "TestName";
        handler.removeConnection(mockConnectionEndpoint);
        assertEquals(0, clients.size());
        verify(mockConnectionEndpoint).send(argThat(isA(UserEvent.class)));
    }

    @Test
    public void handleInput_String() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);

        String message = "/w foo bar";
        handler.handleInput(new InputEvent(mockConnectionEndpoint, new String(message.getBytes())));

        String expected = "You whisper to foo: bar";
        verify(mockConnectionEndpoint).send(argThat(isA(String.class).and(matches(expected))));

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void handleInput_Challenge() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);

        Challenge mockChallenge = mock(Challenge.class);
        when(mockChallenge.isSilent()).thenReturn(false);
        when(mockChallenge.getTo()).thenReturn("TestName");

        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallenge));

        String expectedTo = "TestName";
        String expectedSend = mockChallenge.roll();
        String expectedMessage = "TestName: " + expectedSend;

        verify(mockConnectionEndpoint).send(argThat(isA(String.class).and(matches(expectedMessage))));
        verify(mockConnectionEndpoint).send(argThat(isA(String.class).and(matches("You roll " + expectedSend))));

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void handleInput_ChallReq() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);
        clients.get(0).name = "TestName";

        ChallReq mockChallReq = mock(ChallReq.class);
        when(mockChallReq.getTo()).thenReturn("TestName2");

        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockChallReq));

        verify(mockConnectionEndpoint).send(argThat(isA(ChallReq.class)));
        verify(clients.get(0).ce).send(argThat(isA(ChallReq.class)));

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void handleInput_Hero() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);
        clients.get(0).hero = new Hero("TestName");

        Hero mockHero = new Hero("TestName2");

        handler.handleInput(new InputEvent(mockConnectionEndpoint, mockHero));

        verify(mockConnectionEndpoint).send(argThat(isA(UserEvent.class).and(matches("Gamemaster connected"))));
        verify(clients.get(0).ce).send(argThat(isA(UserEvent.class).and(matches("TestName2 connected"))));

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void sendHeros() {
        Vector<Hero> heroes = new Vector<>();
        heroes.add(new Hero("TestName1"));
        heroes.add(new Hero("TestName2"));
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);

        handler.sendHeros(mockConnectionEndpoint);

        byte[] expectedBytes = new ByteArrayOutputStream().writeToByteArray(new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(heroes));

        verify(mockConnectionEndpoint).send(argThat(isA(byte[].class).and(matches(expectedBytes.length))));

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void isGmConnected() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);
        clients.get(0).name = "TestName";

        boolean actual = handler.isGmConnected();
        boolean expected = false;

        assertEquals(expected, actual);

        clients.get(0).name = "Gamemaster";

        actual = handler.isGmConnected();
        expected = true;

        assertEquals(expected, actual);

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void sendOneSystemMsg() {
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        clients.add(mockEntry);

        String message = "TestMessage";

        handler.sendOneSystemMsg(mockConnectionEndpoint, message);

        verify(mockConnectionEndpoint).send(argThat(isA(String.class).and(matches(message))));

        verifyNoMoreInteractions(mockConnectionEndpoint);
    }

    @Test
    public void sendAllSystemMsg() {
        String message = "TestMessage";

        handler.sendAllSystemMsg(message);

        for (int i = 0; i < clients.size(); i++) {
            verify(clients.elementAt(i).ce).send(argThat(isA(String.class).and(matches(message))));
        }

        verifyNoMoreInteractions(clients.get(0).ce);
    }
}
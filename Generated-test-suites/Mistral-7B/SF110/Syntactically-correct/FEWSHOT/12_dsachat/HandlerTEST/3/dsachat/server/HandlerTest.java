package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import dsachat.share.*;
import dsachat.share.hero.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint mockConnection;
    private Entry mockEntry;
    private Vector<Entry> clients;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new Handler();
        mockConnection = mock(ConnectionEndpoint.class);
        mockEntry = new Entry();
        clients = new Vector<>();
        clients.add(mockEntry);
        handler.clients = clients;
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(mockConnection);
        assertEquals(1, clients.size());
        verify(mockConnection, times(2)).send(ArgumentMatchers.any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        mockEntry.name = "John Doe";
        handler.addConnection(mockConnection);

        handler.removeConnection(mockConnection);

        assertEquals(0, clients.size());
        verify(mockConnection, times(1)).send(ArgumentMatchers.any(UserEvent.class));
    }

    @Test
    public void testHandleInputString() {
        handler.addConnection(mockConnection);
        when(mockConnection.getClientPort()).thenReturn(1234);

        String input = "/dice 2d6+1";
        String expectedOutput = "John Doe rolled 2d6+1 --> 4 5";

        handler.handleInput(new InputEvent(mockConnection, input));

        verify(mockConnection, times(1)).send(ArgumentMatchers.eq(expectedOutput));
    }

    @Test
    public void testHandleInputChallenge() {
        handler.addConnection(mockConnection);
        when(mockConnection.getClientPort()).thenReturn(1234);
        Hero hero = new Hero("John Doe", "Warrior");

        handler.handleInput(new InputEvent(mockConnection, new Challenge()));

        verify(mockConnection, times(1)).send("You rolled a dice.");
    }

    @Test
    public void testSendAllSystemMsg() {
        String message = "System message";

        handler.sendAllSystemMsg(message);

        for (Entry entry : clients) {
            verify(entry.ce, times(1)).send(message);
        }
    }

    @Test
    public void testWhisper() {
        handler.addConnection(mockConnection);
        handler.addConnection(mockConnection2);
        when(mockConnection.getClientPort()).thenReturn(1234);
        when(mockConnection2.getClientPort()).thenReturn(5678);
        String recipient = "Recipient";
        String message = "Whisper message";

        handler.handleInput(new InputEvent(mockConnection, "/w " + recipient + " " + message));

        String expectedOutput1 = "John Doe whispers: " + message;
        String expectedOutput2 = "Recipient: " + message;

        verify(mockConnection, times(1)).send(expectedOutput1);
        verify(mockConnection2, times(1)).send(expectedOutput2);
    }

    @Test
    public void testRollDice() {
        handler.addConnection(mockConnection);
        when(mockConnection.getClientPort()).thenReturn(1234);

        String input = "/dice 2d6";
        String expectedOutput = "John Doe rolls 2d6 --> ";

        handler.handleInput(new InputEvent(mockConnection, input));

        OutputStream outputStream = new ByteArrayOutputStream();
        handler.sendAllSystemMsg(outputStream);
        String actualOutput = outputStream.toString().trim();

        assertTrue(actualOutput.startsWith(expectedOutput));
    }

    @Test
    public void testSendHeros() {
        Vector<Hero> heroes = new Vector<>();
        heroes.add(new Hero("John Doe", "Warrior"));
        heroes.add(new Hero("Jane Doe", "Mage"));

        handler.addConnection(mockConnection);

        handler.handleInput(new InputEvent(mockConnection, "/getheros"));

        verify(mockConnection, times(1)).send(heroes);
    }
}
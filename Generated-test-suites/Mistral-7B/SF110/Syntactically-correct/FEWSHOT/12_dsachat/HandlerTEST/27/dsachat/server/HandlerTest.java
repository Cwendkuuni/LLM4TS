package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    @Mock
    private ConnectionEndpoint mockConnectionEndpoint;

    private Handler handler;
    private Entry entry;

    @Test
    public void testAddConnection() {
        handler = new Handler();
        entry = new Entry();

        when(mockConnectionEndpoint.isConnected()).thenReturn(true);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        handler.addConnection(mockConnectionEndpoint);

        assertEquals(1, handler.clients.size());
        verify(mockConnectionEndpoint).send(any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);

        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);
        handler.removeConnection(mockConnectionEndpoint);

        assertEquals(0, handler.clients.size());
        verify(mockConnectionEndpoint, never()).send(any(Object.class));
    }

    @Test
    public void testHandleInputString() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);

        String msg = "/dice 3d6";
        handler.handleInput(new InputEvent(mockConnectionEndpoint, msg));

        verify(mockConnectionEndpoint, times(1)).send(anyStringThat(startsWith("You rolled 3d6 --> ")));
        verify(mockConnectionEndpoint, atLeastOnce()).send(eq("Welcome Gamemaster"));
    }

    @Test
    public void testHandleInputChallenge() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);

        Challenge challenge = mock(Challenge.class);
        when(challenge.roll()).thenReturn("roll result");
        when(mockConnectionEndpoint.send(eq("roll result"))).thenThrow(new IOException());
        when(mockConnectionEndpoint.send(anyString())).thenReturn(true);

        handler.handleInput(new InputEvent(mockConnectionEndpoint, challenge));

        verify(mockConnectionEndpoint, atLeastOnce()).send(eq("roll result"));
        verify(mockConnectionEndpoint, times(1)).send("roll result");
    }

    @Test
    public void testHandleInputChallReq() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);

        ChallReq challReq = mock(ChallReq.class);
        when(challReq.getTo()).thenReturn("toName");

        handler.handleInput(new InputEvent(mockConnectionEndpoint, challReq));

        verify(mockConnectionEndpoint).send(challReq);
    }

    @Test
    public void testHandleInputHero() {
        handler = new Handler();
        entry = new Entry();
        Hero hero = new Hero("name");
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);

        handler.handleInput(new InputEvent(mockConnectionEndpoint, hero));

        verify(mockConnectionEndpoint).send(eq("Welcome name"));
        verify(entry).hero = hero;
        verify(entry).name = "name";
    }

    @Test
    public void testHandleInputUserEvent() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);

        UserEvent userEvent = new UserEvent(true, "name");

        handler.handleInput(new InputEvent(mockConnectionEndpoint, userEvent));

        verify(mockConnectionEndpoint).send(userEvent);
    }

    @Test
    public void testSendAllSystemMsg() {
        handler = new Handler();
        String string = "system message";

        handler.sendAllSystemMsg(string);

        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                verify(handler.clients.elementAt(i).ce).send(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testWhisper() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);

        String to = "toName";

        handler.whisper(mockConnectionEndpoint, to, "whisper message");

        verify(handler, times(1)).sendToOneStr(mockConnectionEndpoint, "toName", "You whisper to toName: whisper message");
        verify(handler, times(1)).sendOneSystemMsg(mockConnectionEndpoint, "Handler.whisper: " + handler.getName(mockConnectionEndpoint) + " whispers: whisper message");
    }

    @Test
    public void testRollDice() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);

        handler.rollDice(mockConnectionEndpoint, "3d6+2");

        verify(handler, times(1)).sendAllSystemMsg(startsWith("You rolled 3d6+2 --> "));
    }

    @Test
    public void testRollDiceMultipleDice() {
        handler = new Handler();
        entry = new Entry();
        handler.clients.addElement(entry);
        when(mockConnectionEndpoint.getSource()).thenReturn(mockConnectionEndpoint);
        when(mockConnectionEndpoint.getClientPort()).thenReturn(1);

        handler.rollDice(mockConnectionEndpoint, "4d20");

        verify(handler, times(1)).sendAllSystemMsg(startsWith("You rolled 4d20 --> "));
    }

    @Test
    public void testIsGmConnected() {
        handler = new Handler();

        assertTrue(handler.isGmConnected());

        Entry gmEntry = new Entry();
        gmEntry.name = "Gamemaster";
        handler.clients.addElement(gmEntry);

        verify(handler, times(1)).isGmConnected();
        assertTrue(handler.isGmConnected());

        handler.clients.removeElementAt(handler.clients.size() - 1);

        verify(handler, times(1)).isGmConnected();
        assertFalse(handler.isGmConnected());
    }
}
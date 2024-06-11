package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Entry entry;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        hero = new Hero("TestHero");
        when(connectionEndpoint.getClientPort()).thenReturn(12345);
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        verify(connectionEndpoint).send(any(UserEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddConnectionNullConnectionEndpoint() {
        handler.addConnection(null);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
        verify(connectionEndpoint).send(any(UserEvent.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveConnectionNullConnectionEndpoint2() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(null);
    }

    @Test
    public void testHandleInputStringMessage() {
        handler.addConnection(connectionEndpoint);
        String message = "/dice 2d6";
        handler.handleInput(new InputEvent(message, connectionEndpoint));
        verify(connectionEndpoint).send(anyStringThatMatches(".*You rolled 2d6 --> .*"));
    }

    @Test
    public void testHandleInputWhisperMessage() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(mock(ConnectionEndpoint.class));
        String message = "whisper test test";
        handler.handleInput(new InputEvent(message, connectionEndpoint));
        verify(handler.clients.get(1)).ce.send(anyStringThatStartsWith("test whispers: test"));
    }

    @Test
    public void testHandleInputInvalidCommand() {
        handler.addConnection(connectionEndpoint);
        String message = "/invalidCommand";
        handler.handleInput(new InputEvent(message, connectionEndpoint));
        verify(connectionEndpoint).send("command /invalidCommand not found");
    }

    @Test
    public void testHandleInputHeroUpdate() {
        handler.addConnection(connectionEndpoint);
        handler.handleInput(new InputEvent(hero, connectionEndpoint));
        assertEquals("TestHero", handler.getName(connectionEndpoint));
    }

    @Test
    public void testHandleInputGMConnection() {
        handler.addConnection(connectionEndpoint);
        UserEvent userEvent = new UserEvent(true, "Gamemaster");
        handler.handleInput(new InputEvent(userEvent, connectionEndpoint));
        assertEquals("Gamemaster", handler.getName(connectionEndpoint));
    }

    @Test
    public void testSendHeros() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(mock(ConnectionEndpoint.class));
        Hero hero1 = new Hero("Hero1");
        Hero hero2 = new Hero("Hero2");
        handler.clients.get(0).hero = hero1;
        handler.clients.get(1).hero = hero2;
        handler.sendHeros(connectionEndpoint);
        verify(connectionEndpoint).send(eq(ArrayList.class));
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new UserEvent(true, "Gamemaster").getConnectionEndpoint());
        handler.addConnection(new UserEvent(false, "").getConnectionEndpoint());
        assertTrue(handler.isGmConnected());
        handler.removeConnection(handler.clients.get(0).ce);
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        handler.addConnection(connectionEndpoint);
        String message = "Test message";
        handler.sendOneSystemMsg(connectionEndpoint, message);
        verify(connectionEndpoint).send(message);
    }

    @Test
    public void testSendAllSystemMsg() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        String message = "Test message";
        handler.sendAllSystemMsg(message);
        verify(connectionEndpoint1).send(message);
        verify(connectionEndpoint2).send(message);
    }

    @Test
    public void testWhisper() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        String message = "whisper test test to test";
        handler.whisper(connectionEndpoint1, message);
        verify(connectionEndpoint2).send(startsWith("test whispers: test to test"));
    }

    @Test
    public void testRollDice() {
        handler.addConnection(connectionEndpoint);
        String message = "/dice 1d6+3";
        handler.rollDice(connectionEndpoint, message);
        verify(connectionEndpoint).send(anyStringThatMatches(".*You rolled 1d6-->.*3"));
    }

    private Matcher anyStringThatMatches(String regex) {
        return ArgumentMatchers.anyStringThat(new TypeSafeMatcher<String>() {
            private final Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean matchesSafely(String string) {
                return pattern.matcher(string).matches();
            }

            @Override
            public void describeTo(Description description) {
                description.matcher(regex);
            }
        });
    }

    private ConnectionEndpoint connectionEndpoint1 = mock(ConnectionEndpoint.class);
    private ConnectionEndpoint connectionEndpoint2 = mock(ConnectionEndpoint.class);
}
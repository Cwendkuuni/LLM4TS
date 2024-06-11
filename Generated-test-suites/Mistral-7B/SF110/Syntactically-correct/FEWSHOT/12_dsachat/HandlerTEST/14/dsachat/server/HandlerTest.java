package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);

        handler.addConnection(connectionEndpoint);

        assertEquals(1, handler.clients.size());
        Mockito.verify(connectionEndpoint).send(Mockito.any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint1 = Mockito.mock(ConnectionEndpoint.class);
        ConnectionEndpoint connectionEndpoint2 = Mockito.mock(ConnectionEndpoint.class);

        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        handler.removeConnection(connectionEndpoint1);

        assertEquals(1, handler.clients.size());
        Mockito.verify(connectionEndpoint1).getClientPort();
        Mockito.verify(connectionEndpoint2).send(Mockito.any(UserEvent.class));
    }

    @Test
    public void testHandleInputString() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String msg = "/roll 2d6";

        handler.handleInput(new InputEvent(connectionEndpoint, msg));

        assertTrue(msg.matches("^(/roll|/dice)\\s+(\\d+)d(\\d+)(\\s+[+]?\\d+)?"));
    }

    @Test
    public void testHandleInputChallenge() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        Hero hero = new Hero("TestHero");

        handler.handleInput(new InputEvent(connectionEndpoint, hero));

        assertEquals(hero.getName(), handler.updateHero(connectionEndpoint, hero));
    }

    @Test
    public void testHandleInputUserEvent() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        UserEvent userEvent = new UserEvent(true, "Gamemaster");

        handler.handleInput(new InputEvent(connectionEndpoint, userEvent));

        assertEquals("Gamemaster", handler.clients.elementAt(handler.clients.size() - 1).name);
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint1 = Mockito.mock(ConnectionEndpoint.class);
        ConnectionEndpoint connectionEndpoint2 = Mockito.mock(ConnectionEndpoint.class);
        String msg = "TestMessage";

        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        handler.sendAllSystemMsg(msg);

        Mockito.verify(connectionEndpoint1).send(msg);
        Mockito.verify(connectionEndpoint2).send(msg);
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint senderConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        ConnectionEndpoint receiverConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String senderName = "TestSender";
        String receiverName = "TestReceiver";
        String msg = "TestWhisperMessage";

        handler.handleInput(new InputEvent(senderConnectionEndpoint, "/w " + receiverName + " " + msg));

        Mockito.verify(senderConnectionEndpoint).send(Mockito.anyString().startsWith("You whisper to " + receiverName + ": "));
        Mockito.verify(receiverConnectionEndpoint).send(Mockito.anyString().startsWith(senderName + " whispers: "));
    }
}
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
        Entry entry = new Entry();

        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        Mockito.verify(connectionEndpoint).send(any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        Entry entry = new Entry();
        handler.addConnection(connectionEndpoint);
        handler.clients.add(entry);

        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
        Mockito.verify(connectionEndpoint).getClientPort();
        Mockito.verify(connectionEndpoint).send(any(UserEvent.class));
    }

    @Test
    public void testHandleInput_String() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String msg = "/roll 2d6";

        handler.handleInput(new InputEvent(connectionEndpoint, msg));

        Mockito.verify(connectionEndpoint).send(anyStringThatMatches("/roll 2d6 --> ([0-9]|[1-6]|[1-6] )+ "));
        Mockito.verify(connectionEndpoint, times(2)).send(anyStringThatMatches("\\d+ "));
    }

    @Test
    public void testHandleInput_Challenge() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        Challenge challenge = Mockito.mock(Challenge.class);

        handler.handleInput(new InputEvent(connectionEndpoint, challenge));

        Mockito.verify(connectionEndpoint).send(challenge.roll());
    }

    @Test
    public void testHandleInput_ChallReq() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        ConnectionEndpoint targetConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        ChallReq challReq = Mockito.mock(ChallReq.class);

        handler.clients.add(new Entry(connectionEndpoint, "User1"));
        handler.clients.add(new Entry(targetConnectionEndpoint, "User2"));

        handler.handleInput(new InputEvent(connectionEndpoint, challReq));

        Mockito.verify(targetConnectionEndpoint).send(challReq);
    }

    @Test
    public void testHandleInput_Hero() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        Hero hero = new Hero("TestHero");

        handler.handleInput(new InputEvent(connectionEndpoint, hero));

        assertEquals("TestHero", handler.getName(connectionEndpoint));
    }

    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        ConnectionEndpoint gmConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        handler.clients.add(new Entry(gmConnectionEndpoint, "Gamemaster"));

        assertTrue(handler.isGmConnected());

        handler.clients.remove(0);

        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String testMessage = "TestMessage";

        handler.sendAllSystemMsg(testMessage);

        for (int i = 0; i < handler.clients.size(); ++i) {
            Mockito.verify(handler.clients.elementAt(i).ce).send(testMessage);
        }
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        ConnectionEndpoint targetConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String testMessage = "TestMessage";

        handler.clients.add(new Entry(connectionEndpoint, "User1"));
        handler.clients.add(new Entry(targetConnectionEndpoint, "User2"));

        handler.whisper(connectionEndpoint, "User2 TestMessage");

        Mockito.verify(connectionEndpoint).send("You whisper to User2: TestMessage");
        Mockito.verify(targetConnectionEndpoint).send("User1 whispers: TestMessage");
    }

    @Test
    public void testRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String testMessage = "TestMessage";

        handler.handleInput(new InputEvent(connectionEndpoint, testMessage));

        Mockito.verify(connectionEndpoint).send(testMessage);

        Mockito.verify(connectionEndpoint, times(2)).send(anyStringThatMatches("\\d+ "));

        Mockito.verify(connectionEndpoint).send(anyStringThatMatches("/roll 2d6 --> ([0-9]|[1-6]|[1-6] )+ "));
    }

    private Matcher anyStringThatMatches(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return argument -> pattern.matcher(argument.toString()).matches();
    }
}
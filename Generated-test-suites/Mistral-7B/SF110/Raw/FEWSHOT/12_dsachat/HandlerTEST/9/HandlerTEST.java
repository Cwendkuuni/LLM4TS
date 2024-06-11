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

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Entry entry;
    private Hero hero;

    @Test
    public void testAddConnection() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();

        handler.addConnection(connectionEndpoint);

        assertEquals(1, handler.clients.size());
        assertEquals(connectionEndpoint, handler.clients.elementAt(0).ce);
        assertEquals("", handler.clients.elementAt(0).name);
    }

    @Test
    public void testRemoveConnection() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "testName";
        handler.clients.addElement(entry);

        handler.removeConnection(connectionEndpoint);

        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputWithStringData() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "";
        handler.clients.addElement(entry);

        String msg = "/roll 2d6";
        handler.handleInput(new InputEvent(connectionEndpoint, msg));

        Mockito.verify(connectionEndpoint).send(eq("/roll 2d6"));
        Mockito.verify(connectionEndpoint).send("You rolled 2d6 --> ");
        Mockito.verify(connectionEndpoint).send(argThat(containsString("2 ")));
        Mockito.verify(connectionEndpoint).send(argThat(containsString("4 ")));
    }

    @Test
    public void testHandleInputWithStringDataAndGamemaster() {
        handler = new Handler();
        ConnectionEndpoint connectionEndpoint1 = Mockito.mock(ConnectionEndpoint.class);
        ConnectionEndpoint connectionEndpoint2 = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint1;
        entry.name = "";
        handler.clients.addElement(entry);

        String msg = "/roll 2d6";
        handler.handleInput(new InputEvent(connectionEndpoint2, msg));

        Mockito.verify(connectionEndpoint2).send("command /roll not found");

        Mockito.verifyNoMoreInteractions(connectionEndpoint1);
        Mockito.verifyNoMoreInteractions(connectionEndpoint2);

        handler.handleInput(new InputEvent(connectionEndpoint1, msg));

        Mockito.verify(connectionEndpoint1).send("/roll 2d6");
        Mockito.verify(connectionEndpoint1).send("You rolled 2d6 --> ");
        Mockito.verify(connectionEndpoint1).send(argThat(containsString("2 ")));
        Mockito.verify(connectionEndpoint1).send(argThat(containsString("4 ")));
    }

    @Test
    public void testHandleInputWithChallenge() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "player1";
        handler.clients.addElement(entry);

        Hero hero1 = new Hero("player1", "Hero1");
        Challenge challenge = new Challenge(hero1, "player2", 10);

        handler.handleInput(new InputEvent(connectionEndpoint, challenge));

        Mockito.verify(connectionEndpoint).send("player1 rolled 1d10+10 --> 21");
        Mockito.verify(entry.ce).send(challenge);
    }

    @Test
    public void testHandleInputWithChallReq() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "player1";
        handler.clients.addElement(entry);

        ConnectionEndpoint connectionEndpoint2 = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint2;
        entry.name = "player2";
        handler.clients.addElement(entry);

        ChallReq challReq = new ChallReq("player1", "player2", 10);

        handler.handleInput(new InputEvent(connectionEndpoint, challReq));

        Mockito.verify(connectionEndpoint2).send(challReq);
    }

    @Test
    public void testHandleInputWithHero() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        hero = new Hero("testName", "TestHero");

        handler.handleInput(new InputEvent(connectionEndpoint, hero));

        assertNull(handler.getName(connectionEndpoint));

        handler.clients.addElement(entry);
        handler.clients.elementAt(0).hero = hero;
        handler.clients.elementAt(0).name = "testName";

        assertEquals("testName", handler.getName(connectionEndpoint));
    }

    @Test
    public void testSendAllSystemMsg() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        String msg = "TestMessage";

        handler.sendAllSystemMsg(msg);

        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                Mockito.verify(handler.clients.elementAt(i).ce).send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private List<Entry> clients;
    private ConnectionEndpoint connectionEndpoint;
    private ByteArrayOutputStream outputStream;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        clients = new ArrayList<>();
        connectionEndpoint = mock(ConnectionEndpoint.class);
        outputStream = new ByteArrayOutputStream();
        hero = new Hero("testHero");
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, clients.size());
        verify(connectionEndpoint).send(any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        clients.add(new Entry());
        clients.get(0).ce = connectionEndpoint;
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, clients.size());
        verify(connectionEndpoint).send(any(UserEvent.class));
    }

    @Test
    public void testHandleInputString() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        handler.handleInput(new InputEvent(connectionEndpoint, "testCommand"));
        verify(connectionEndpoint).send(anyString());
    }

    @Test
    public void testHandleInputChallenge() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        OutputStream outputStream = mock(OutputStream.class);
        Challenge challenge = mock(Challenge.class);
        handler.handleInput(new InputEvent(connectionEndpoint, challenge));
        verify(connectionEndpoint).send(anyString());
        verify(challenge).roll();
    }

    @Test
    public void testHandleInputChallReq() {
        ConnectionEndpoint connectionEndpoint1 = mock(ConnectionEndpoint.class);
        ConnectionEndpoint connectionEndpoint2 = mock(ConnectionEndpoint.class);
        ChallReq challReq = mock(ChallReq.class);
        handler.handleInput(new InputEvent(connectionEndpoint1, challReq));
        handler.clients.add(new Entry());
        handler.clients.get(0).ce = connectionEndpoint2;
        verify(connectionEndpoint2).send(challReq);
    }

    @Test
    public void testHandleInputHero() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        handler.handleInput(new InputEvent(connectionEndpoint, hero));
        Entry entry = new Entry();
        entry.hero = hero;
        handler.clients.add(entry);
        String name = "testName";
        handler.updateHero(connectionEndpoint, hero);
        assertEquals(name, handler.getName(connectionEndpoint));
    }

    @Test
    public void testSendAllSystemMsg() {
        String testMessage = "testMessage";
        handler.sendAllSystemMsg(testMessage);
        for (Entry entry : clients) {
            try {
                assertEquals(testMessage, entry.ce.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testWhisper() {
        ConnectionEndpoint connectionEndpoint1 = mock(ConnectionEndpoint.class);
        ConnectionEndpoint connectionEndpoint2 = mock(ConnectionEndpoint.class);
        handler.handleInput(new InputEvent(connectionEndpoint1, "/w foo testMessage"));
        handler.clients.add(new Entry());
        handler.clients.get(0).ce = connectionEndpoint2;
        handler.clients.get(0).name = "foo";
        handler.sendAllSystemMsg("testMessage");
        try {
            assertEquals("You whisper to foo: testMessage", connectionEndpoint1.readLine());
            assertEquals("foo whispers: testMessage", connectionEndpoint2.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRollDice() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        handler.handleInput(new InputEvent(connectionEndpoint, "/roll 1d6+2"));
        String expectedOutput = handler.getName(connectionEndpoint) + " rolled 1d6+2 --> ";
        try {
            for (int i = 0; i < 3; i++) {
                expectedOutput += Challenge.dice(6) + "  ";
            }
            expectedOutput += "5";
            assertEquals(expectedOutput, connectionEndpoint.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Entry {
    ConnectionEndpoint ce;
    Hero hero;
    String name;
}
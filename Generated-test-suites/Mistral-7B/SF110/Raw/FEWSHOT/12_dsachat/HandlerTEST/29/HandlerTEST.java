package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private Vector<Entry> clients;
    private ConnectionEndpoint connectionEndpoint;
    private Entry entry;

    @Before
    public void setUp() {
        handler = new Handler();
        clients = handler.clients;
        connectionEndpoint = mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, clients.size());
        assertEquals(connectionEndpoint, clients.elementAt(0).ce);
        assertEquals("", clients.elementAt(0).name);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        String name = "testUser";
        entry.name = name;
        clients.add(entry);

        handler.removeConnection(connectionEndpoint);
        assertEquals(0, clients.size());
        assertNull(clients.firstElement());

        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, clients.size());
        assertNull(clients.firstElement());
    }

    @Test
    public void testHandleInputString() {
        handler.addConnection(connectionEndpoint);

        String msg = "/roll 2d6";
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{msg}));

        String expected = handler.getName(connectionEndpoint) + " rolled 2d6 --> ";
        for (int i = 0; i < 2; ++i) {
            expected += Challenge.dice(6) + "  ";
        }
        assertEquals(expected, handler.getName(connectionEndpoint) + ": " + msg);
        handler.sendAllSystemMsg(expected);
    }

    @Test
    public void testHandleInputChallenge() {
        handler.addConnection(connectionEndpoint);

        Challenge challenge = mock(Challenge.class);
        when(challenge.getTo()).thenReturn("testTo");
        when(challenge.isSilent()).thenReturn(false);

        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{challenge}));

        String expectedSend = handler.getName(connectionEndpoint) + ": ";
        String expectedTo = "testTo";

        String expected = expectedSend + challenge.roll();
        handler.sendOneSystemMsg(connectionEndpoint, expected);
        handler.sendToOneStr(connectionEndpoint, expectedTo, expected);
    }

    @Test
    public void testHandleInputChallReq() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(mock(ConnectionEndpoint.class));

        String to = "testTo";
        String msg = "/roll 2d6 @" + to;

        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{msg}));

        for (int i = 0; i < clients.size(); ++i) {
            if (clients.elementAt(i).name.equals(to)) {
                ClientEntry clientEntry = (ClientEntry) clients.elementAt(i);
                ChallReq challReq = new ChallReq();
                challReq.setTo(to);
                clientEntry.ce.send(challReq);
                break;
            }
        }
    }

    @Test
    public void testHandleInputHero() {
        handler.addConnection(connectionEndpoint);

        Hero hero = new Hero();
        hero.setName("testName");

        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{hero}));

        String name = "testName";
        String expected = name;
        handler.updateHero(connectionEndpoint, hero);
        assertEquals(expected, name);

        String expectedName = "testName";
        Entry expectedEntry = new Entry();
        expectedEntry.hero = hero;
        expectedEntry.name = name;
        assertEquals(expectedEntry, clients.elementAt(clients.size() - 1));

        handler.sendAllObj(new String("*testName: " + msg));
    }

    @Test
    public void testHandleInputUserEvent() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(mock(ConnectionEndpoint.class));
        handler.addConnection(mock(ConnectionEndpoint.class));

        String name = "testName";
        UserEvent userEvent = new UserEvent(true, name);

        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{userEvent}));

        for (int i = 0; i < clients.size(); ++i) {
            if (clients.elementAt(i).name.equals("Gamemaster")) {
                assertEquals(name, clients.elementAt(i).name);
                break;
            }
        }

        handler.sendAllObj(userEvent);
    }

    @Test
    public void testSendHeros() {
        Vector<Hero> heroes = new Vector<>();
        Hero hero1 = new Hero();
        hero1.setName("hero1");
        heroes.add(hero1);

        Hero hero2 = new Hero();
        hero2.setName("hero2");
        heroes.add(hero2);

        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{heroes}));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;

        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(heroes);
        }

        byte[] expectedBytes = outputStream.toByteArray();
        byte[] receivedBytes = ((ByteArrayInputStream) handler.clients.elementAt(0).ce).readAllBytes();

        assertArrayEquals(expectedBytes, receivedBytes);
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("Gamemaster"));

        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        String msg = "testMsg";
        handler.sendAllSystemMsg(msg);

        for (int i = 0; i < clients.size(); ++i) {
            assertEquals(msg, clients.elementAt(i).ce.getLastMessage());
        }
    }

    @Test
    public void testWhisper() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(mock(ConnectionEndpoint.class));

        String from = handler.getName(connectionEndpoint);
        String to = "testTo";
        String msg = "testMsg";

        handler.whisper(connectionEndpoint, to, msg);

        for (int i = 0; i < clients.size(); ++i) {
            if (clients.elementAt(i).name.equals(to)) {
                String expected = from + " whispers: " + msg;
                assertEquals(expected, clients.elementAt(i).ce.getLastMessage());
                break;
            }
        }

        String expectedFrom = from;
        String expectedTo = to;
        String expected = expectedFrom + " whispers to " + expectedTo + ": " + msg;
        assertEquals(expected, handler.getName(connectionEndpoint) + ": " + msg);
    }

    @Test
    public void testRollDice() {
        handler.addConnection(connectionEndpoint);

        String msg = "/roll 2d6";
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{msg}));

        String expected = handler.getName(connectionEndpoint) + " rolled 2d6 --> ";
        for (int i = 0; i < 2; ++i) {
            expected += Challenge.dice(6) + "  ";
        }
        assertEquals(expected, handler.getName(connectionEndpoint) + ": " + msg);

        handler.sendAllSystemMsg(expected);

        String expectedRoll = handler.getName(connectionEndpoint) + " rolls 2d6 --> ";
        for (int j = 0; j < 2; ++j) {
            expectedRoll += Challenge.dice(6) + "  ";
        }
        assertEquals(expectedRoll, handler.getName(connectionEndpoint) + ": " + msg);
    }

    @Test
    public void testSendAllStr() {
        handler.addConnection(connectionEndpoint);

        String msg = "testMsg";
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{msg}));

        String expected = handler.getName(connectionEndpoint) + ": " + msg;
        handler.sendAllStr(connectionEndpoint, msg);

        for (int i = 0; i < clients.size(); ++i) {
            assertEquals(expected, clients.elementAt(i).ce.getLastMessage());
        }
    }

    @Test
    public void testSendAllObj() {
        handler.addConnection(connectionEndpoint);

        Vector<Hero> heroes = new Vector<>();
        Hero hero1 = new Hero();
        hero1.setName("hero1");
        heroes.add(hero1);

        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{heroes}));

        handler.sendAllObj(new Object[]{heroes});

        for (int i = 0; i < clients.size(); ++i) {
            assertEquals(heroes, clients.elementAt(i).ce.getLastData());
        }
    }

    @Test
    public void testGetName() {
        handler.addConnection(connectionEndpoint);

        assertEquals("", handler.getName(connectionEndpoint));

        String name = "testName";
        entry.name = name;
        clients.add(entry);

        assertEquals(name, handler.getName(connectionEndpoint));
    }
}
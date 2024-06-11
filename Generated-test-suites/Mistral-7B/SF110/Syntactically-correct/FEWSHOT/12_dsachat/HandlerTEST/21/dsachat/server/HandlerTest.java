package dsachat.server;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Entry entry;
    private Hero hero;
    private Vector<Hero> heroVector;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        hero = Mockito.mock(Hero.class);
        heroVector = new Vector<>();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        Mockito.verify(connectionEndpoint).send(ArgumentMatchers.any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        Mockito.verify(connectionEndpoint, Mockito.times(2)).send(ArgumentMatchers.any(UserEvent.class));
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputString() throws IOException {
        String message = "/hello";
        Mockito.when(connectionEndpoint.getClientPort()).thenReturn(1234);
        Mockito.when(connectionEndpoint.send(ArgumentMatchers.any(String.class))).thenAnswer(i -> i.getData().toString());

        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(String.class)));
        Mockito.verify(connectionEndpoint).send("/command not found");
        assertEquals(0, handler.clients.size());

        handler.addConnection(connectionEndpoint);
        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(String.class)));
        Mockito.verify(connectionEndpoint).send("/hello not found");
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInputChallenge() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        Challenge challenge = Mockito.mock(Challenge.class);
        Mockito.when(challenge.roll()).thenReturn("roll result");

        Mockito.when(connectionEndpoint.getClientPort()).thenReturn(1234);
        Mockito.when(connectionEndpoint.send(ArgumentMatchers.any(String.class))).thenAnswer(i -> i.getData().toString());

        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(Challenge.class)));
        Mockito.verify(connectionEndpoint, Mockito.times(2)).send("roll result");

        Mockito.when(challenge.isSilent()).thenReturn(false);
        String to = "to";
        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(Challenge.class)));
        Mockito.verify(connectionEndpoint).send("roll result: to");
        Mockito.verify(connectionEndpoint).send("You roll result to: to");
    }

    @Test
    public void testHandleInputChallReq() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ChallReq challReq = Mockito.mock(ChallReq.class);
        Mockito.when(challReq.getTo()).thenReturn("to");

        Mockito.when(connectionEndpoint.getClientPort()).thenReturn(1234);
        Mockito.when(connectionEndpoint.send(ArgumentMatchers.any(UserEvent.class))).thenAnswer(i -> i.getData());

        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(ChallReq.class)));
        Mockito.verify(connectionEndpoint).send(challReq);

        Mockito.when(connectionEndpoint.getClientPort()).thenReturn(5678);
        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(ChallReq.class)));
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInputHero() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Hero hero = Mockito.mock(Hero.class);
        Mockito.when(hero.getName()).thenReturn("name");

        Mockito.when(connectionEndpoint.getClientPort()).thenReturn(1234);
        Mockito.when(connectionEndpoint.send(ArgumentMatchers.any(String.class))).thenAnswer(i -> i.getData().toString());

        handler.handleInput(Mockito.argThat(e -> e.getData().getClass().equals(Hero.class)));
        assertEquals("name", handler.getName(connectionEndpoint));
        Mockito.verify(connectionEndpoint).send("Welcome name");
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testSendAllSystemMsg() {
        String message = "message";
        for (int i = 0; i < 5; ++i) {
            Entry entry = new Entry();
            Mockito.when(entry.ce.getClientPort()).thenReturn(i + 1);
            handler.clients.addElement(entry);
        }

        handler.sendAllSystemMsg(message);
        for (int i = 0; i < 5; ++i) {
            Mockito.verify(handler.clients.elementAt(i).ce).send(message);
        }
    }

    @Test
    public void testIsGmConnected() {
        Entry gmEntry = new Entry();
        gmEntry.name = "Gamemaster";
        handler.clients.addElement(gmEntry);

        assertTrue(handler.isGmConnected());

        handler.clients.removeElementAt(0);

        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        String message = "message";
        Entry entry = new Entry();
        entry.name = "to";
        handler.clients.addElement(entry);

        handler.sendOneSystemMsg(connectionEndpoint, message);
        assertNull(entry.ce.sendSpy);
        Mockito.verify(entry.ce).send(message);
    }

    @Test
    public void testWhisper() {
        String sender = "sender";
        String message = "message";
        String to = "to";
        handler.clients.addAll(new ArrayList<>(2) {
            {
                add(new Entry());
                add(new Entry());
            }
        });

        handler.whisper(connectionEndpoint, sender + " " + to + " " + message);
        assertEquals(sender + ": " + message, handler.clients.get(0).ce.sendSpy);
        assertEquals(to + ": " + message, handler.clients.get(1).ce.sendSpy);
    }

    @Test
    public void testRollDice() throws IOException {
        Mockito.when(connectionEndpoint.send(ArgumentMatchers.any(PrintWriter.class))).thenAnswer(i -> i.getMockControl().createMock(PrintWriter.class));

        handler.rollDice(connectionEndpoint, "d6");
        handler.rollDice(connectionEndpoint, "d6+1");
        handler.rollDice(connectionEndpoint, "d6+2");
        handler.rollDice(connectionEndpoint, "2d6");
        handler.rollDice(connectionEndpoint, "3d6");
        handler.rollDice(connectionEndpoint, "5d6");
        handler.rollDice(connectionEndpoint, "10d6");

        Mockito.verify(connectionEndpoint.send(Mockito.any(PrintWriter.class)), Mockito.times(7));
    }

    @Test
    public void testSendHeros() throws IOException {
        Hero hero1 = Mockito.mock(Hero.class);
        Hero hero2 = Mockito.mock(Hero.class);
        handler.clients.addAll(new ArrayList<>(2) {
            {
                add(new Entry());
                add(new Entry());
            }
        });
        handler.clients.get(0).hero = hero1;
        handler.clients.get(1).hero = hero2;

        handler.sendHeros(connectionEndpoint);
        Mockito.verify(connectionEndpoint).send(heroVector);
    }
}

class Entry {
    ConnectionEndpoint ce;
    Hero hero;
    String name;
    PrintWriter sendSpy;
}
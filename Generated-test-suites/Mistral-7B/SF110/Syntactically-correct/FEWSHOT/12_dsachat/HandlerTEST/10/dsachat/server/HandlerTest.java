package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint mockConnectionEndpoint;
    private Vector<Entry> clients;
    private List<Hero> heroes;

    @Before
    public void setUp() {
        handler = new Handler();
        mockConnectionEndpoint = mock(ConnectionEndpoint.class);
        clients = new Vector<>();
        heroes = new ArrayList<>();
        handler.clients = clients;
    }

    @Test
    public void testAddConnection() {
        ConnectionEndpoint cl = mock(ConnectionEndpoint.class);
        Entry entry = new Entry();
        entry.ce = cl;
        entry.name = "";
        handler.addConnection(cl);
        assertEquals(1, clients.size());
        verify(cl).send(any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint cl = mock(ConnectionEndpoint.class);
        int clientPort = 1234;
        String name = "John Doe";
        Entry entry = new Entry();
        entry.ce = cl;
        entry.name = name;
        clients.add(entry);
        handler.removeConnection(cl);
        assertEquals(0, clients.size());
        verify(cl).getClientPort();
        verify(cl).isConnected();
        for (int i = 0; i < clients.size(); ++i) {
            Entry client = clients.get(i);
            if (client.ce.getClientPort() == clientPort) {
                assertEquals(name, client.name);
                break;
            }
        }
        UserEvent newCon = new UserEvent(true, name);
        handler.sendAllObj(newCon);
        verify(cl).send(newCon);
    }

    @Test
    public void testHandleInputString() {
        String msg = "/roll 2d6";
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getClientPort()).thenReturn(1234);
        handler.handleInput(new InputEvent(this, new ObjectInputStream(new ByteArrayInputStream(msg.getBytes()))));
        verify(con, times(1)).send(anyString());
        verify(con, times(1)).send(eq("/roll 2d6 --> 3 4"));
    }

    @Test
    public void testHandleInputChallenge() {
        Challenge challenge = mock(Challenge.class);
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getClientPort()).thenReturn(1234);
        handler.handleInput(new InputEvent(this, challenge));
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(con).send(stringCaptor.capture());
        assertEquals("/roll 2d6 --> 3 4", stringCaptor.getValue());
        verify(con, times(1)).send(eq("/roll 2d6 --> 3 4"));
        verify(challenge).roll();
    }

    @Test
    public void testHandleInputChallReq() {
        ChallReq challReq = mock(ChallReq.class);
        ConnectionEndpoint sender = mockConnectionEndpoint;
        ConnectionEndpoint receiver = mockConnectionEndpoint;
        when(sender.getClientPort()).thenReturn(1234);
        when(receiver.getClientPort()).thenReturn(5678);
        handler.handleInput(new InputEvent(this, challReq));
        verify(sender).send(challReq);
        verify(receiver).send(challReq);
    }

    @Test
    public void testHandleInputHero() {
        ConnectionEndpoint con = mockConnectionEndpoint;
        Hero hero = new Hero("John Doe", 10, 10, 10, 10, 10, 10, 10, 10, 10);
        String name = "Jane Doe";
        handler.handleInput(new InputEvent(this, hero));
        Entry entry = new Entry();
        entry.ce = con;
        entry.hero = hero;
        clients.add(entry);
        assertEquals(1, clients.size());
        verify(con).send("Welcome Jane Doe");
    }

    @Test
    public void testSendAllHeros() {
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getClientPort()).thenReturn(1234);
        Hero hero1 = new Hero("John Doe", 10, 10, 10, 10, 10, 10, 10, 10, 10);
        Hero hero2 = new Hero("Jane Doe", 10, 10, 10, 10, 10, 10, 10, 10, 10);
        handler.sendHeros(con);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        try {
            objectOutputStream.writeObject(heroes);
            byte[] output = outputStream.toByteArray();
            handler.handleInput(new InputEvent(this, new ByteArrayInputStream(output)));
            verify(con).send(eq(heroes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsGmConnected() {
        ConnectionEndpoint gmConnection = mockConnectionEndpoint;
        when(gmConnection.getName()).thenReturn("Gamemaster");
        clients.add(new Entry());
        clients.add(gmConnection);
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        String msg = "System message";
        handler.sendAllSystemMsg(msg);
        for (Entry entry : clients) {
            try {
                entry.ce.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testWhisper() {
        String sender = "John Doe";
        String receiver = "Jane Doe";
        String message = "Hello";
        ConnectionEndpoint senderConnection = mockConnectionEndpoint;
        ConnectionEndpoint receiverConnection = mockConnectionEndpoint;
        when(senderConnection.getName()).thenReturn(sender);
        when(receiverConnection.getName()).thenReturn(receiver);
        handler.whisper(senderConnection, receiver + " " + message);
        verify(senderConnection).send("You whisper to Jane Doe: Hello");
        verify(receiverConnection).send("John Doe whispers: Hello");
    }

    @Test
    public void testRollDice() {
        String sender = "John Doe";
        String message = "/roll 2d6+1";
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getName()).thenReturn(sender);
        handler.rollDice(con, message);
        String expected = sender + " rolled /roll 2d6+1 --> ";
        int diceRoll = Challenge.dice(6) + 1;
        String expectedResult = expected + diceRoll;
        verify(con).send(eq(expectedResult));
    }

    @Test
    public void testRollDiceMultipleDice() {
        String sender = "John Doe";
        String message = "/roll 3d20";
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getName()).thenReturn(sender);
        handler.rollDice(con, message);
        String expected = sender + " rolls /roll 3d20 --> ";
        for (int i = 0; i < 3; i++) {
            int diceRoll = Challenge.dice(20);
            expected += diceRoll + "  ";
        }
        verify(con).send(eq(expected));
    }

    @Test
    public void testRollDiceInvalidCommand() {
        String sender = "John Doe";
        String message = "/roll invalid";
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getName()).thenReturn(sender);
        handler.rollDice(con, message);
        String expected = sender + " rolled command /roll invalid not found";
        verify(con).send(eq(expected));
    }

    @Test
    public void testSendAllStr() {
        String sender = "John Doe";
        String message = "Hello";
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getName()).thenReturn(sender);
        handler.sendAllStr(con, message);
        for (Entry entry : clients) {
            try {
                entry.ce.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetName() {
        ConnectionEndpoint con = mockConnectionEndpoint;
        when(con.getClientPort()).thenReturn(1234);
        String expected = "";
        String name = handler.getName(con);
        assertEquals(expected, name);
        Entry entry = new Entry();
        entry.ce = con;
        entry.name = "John Doe";
        clients.add(entry);
        name = handler.getName(con);
        assertEquals("John Doe", name);
    }
}
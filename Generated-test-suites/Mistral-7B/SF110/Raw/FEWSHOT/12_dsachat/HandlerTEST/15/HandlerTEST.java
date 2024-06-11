package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint mockConnection;
    private Entry mockEntry;
    private Vector<Entry> clients;
    private Hero mockHero;

    @Before
    public void setup() {
        handler = new Handler();
        mockConnection = mock(ConnectionEndpoint.class);
        mockEntry = new Entry();
        mockEntry.ce = mockConnection;
        clients = new Vector<>();
        clients.add(mockEntry);
        handler.clients = clients;
        mockHero = mock(Hero.class);
    }

    @Test
    public void testAddConnection() {
        ConnectionEndpoint cl = mock(ConnectionEndpoint.class);
        Entry entry = new Entry();
        entry.ce = cl;
        entry.name = "";
        handler.addConnection(cl);
        verify(cl, times(1)).send(any(UserEvent.class));
        assertEquals(1, clients.size());
    }

    @Test(expected = Exception.class)
    public void testAddConnectionWithException() {
        ConnectionEndpoint cl = mock(ConnectionEndpoint.class);
        doThrow(new IOException("Test Exception")).when(cl).isConnected();
        handler.addConnection(cl);
    }

    @Test
    public void testRemoveConnection() {
        final String name = "testName";
        ConnectionEndpoint cl = mock(ConnectionEndpoint.class);
        cl.setClientPort(1234);
        mockEntry.ce = cl;
        mockEntry.name = name;
        clients.add(mockEntry);
        handler.removeConnection(cl);
        assertEquals(0, clients.size());

        UserEvent newCon = new UserEvent(true, name);
        handler.sendAllObj(newCon);
        verify(cl, times(1)).send(newCon);
    }

    @Test
    public void testHandleInputString() {
        String msg = "/dice 2d6+3";
        when(mockConnection.send(any(String.class))).thenReturn(true);

        handler.handleInput(new InputEvent() {
            @Override
            public Object getData() {
                return msg;
            }

            @Override
            public ConnectionEndpoint getSource() {
                return mockConnection;
            }
        });

        verify(mockConnection, times(1)).send(argThat(str -> str.startsWith("Handler " + this.getName(mockConnection) + " rolled dice 2d6+3 --> ")));
    }

    @Test
    public void testHandleInputChallenge() {
        Challenge challenge = mock(Challenge.class);
        when(challenge.roll()).thenReturn("testRoll");

        handler.handleInput(new InputEvent() {
            @Override
            public Object getData() {
                return challenge;
            }

            @Override
            public ConnectionEndpoint getSource() {
                return mockConnection;
            }
        });

        String to = "testTo";
        String send = "testRoll";

        handler.sendToOneStr(mockConnection, to, send);
        verify(mockConnection, times(1)).send(argThat(str -> str.startsWith("You whisper to testTo: testRoll")));
    }

    @Test
    public void testHandleInputChallReq() {
        ConnectionEndpoint receiverConnection = mock(ConnectionEndpoint.class);
        String to = "ReceiverName";
        ChallReq challengeRequest = new ChallReq(to);

        handler.handleInput(new InputEvent() {
            @Override
            public Object getData() {
                return challengeRequest;
            }

            @Override
            public ConnectionEndpoint getSource() {
                return mockConnection;
            }
        });

        verify(receiverConnection, times(1)).send(challengeRequest);
    }

    @Test
    public void testUpdateHero() {
        String name = "testName";
        ConnectionEndpoint connection = mock(ConnectionEndpoint.class);
        Entry entry = new Entry();
        entry.ce = connection;
        entry.hero = null;
        handler.clients = clients;

        handler.updateHero(connection, mockHero);

        assertEquals(name, mockHero.getName());
        assertEquals(name, entry.name);
    }

    @Test
    public void testSendAllStr() {
        String message = "Test Message";
        ConnectionEndpoint receiverConnection = mock(ConnectionEndpoint.class);

        handler.sendAllStr(mockConnection, message);

        for (Entry entry : clients) {
            if (!entry.ce.equals(mockConnection)) {
                verify(entry.ce).send(argThat(str -> str.startsWith("Handler " + this.getName(mockConnection) + ": " + message)));
            }
        }
    }

    @Test
    public void testSendAllObj() {
        Object obj = new Object();

        handler.sendAllObj(obj);

        for (Entry entry : clients) {
            verify(entry.ce).send(obj);
        }
    }

    private String getName(ConnectionEndpoint connection) {
        for (Entry entry : clients) {
            if (entry.ce.equals(connection)) {
                return entry.name;
            }
        }
        return null;
    }
}
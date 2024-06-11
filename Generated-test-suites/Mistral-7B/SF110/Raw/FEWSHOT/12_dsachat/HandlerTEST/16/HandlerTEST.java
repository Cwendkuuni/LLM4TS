package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import dsachat.share.*;
import dsachat.share.hero.*;

public class HandlerTest {

    private Handler handler;
    private Vector<Entry> clients;
    private ConnectionEndpoint dummyConnection;
    private OutputStream mockOutputStream;
    private UserEvent mockUserEvent;

    @Before
    public void setUp() {
        handler = new Handler();
        clients = handler.clients;
        dummyConnection = Mockito.mock(ConnectionEndpoint.class);
        mockOutputStream = Mockito.mock(OutputStream.class);
        mockUserEvent = Mockito.mock(UserEvent.class);
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(dummyConnection);
        assertEquals(1, clients.size());
        verify(dummyConnection).send(ArgumentMatchers.any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        Entry entry = new Entry();
        entry.ce = dummyConnection;
        entry.name = "testUser";
        clients.addElement(entry);

        handler.removeConnection(dummyConnection);
        assertEquals(0, clients.size());
        verify(mockUserEvent).setTo("testUser");
        verify(mockUserEvent).setMessage("You have been disconnected");
        verify(dummyConnection).send(mockUserEvent);

        handler.addConnection(dummyConnection);
        handler.removeConnection(dummyConnection);
        verify(dummyConnection, times(2)).send(mockUserEvent);
    }

    @Test
    public void testHandleInputWithString() {
        String msg = "/w foo bar";
        when(dummyConnection.getClientPort()).thenReturn(1);
        when(dummyConnection.send(ArgumentMatchers.any(String.class))).then(invocation -> {
            OutputStream os = mockOutputStream;
            invocation.getArgument(0).printStackTrace(os);
            return null;
        });

        handler.handleInput(new InputEvent(mockUserEvent, dummyConnection, msg));
        verify(dummyConnection).send(ArgumentMatchers.eq("/w foo: bar"));
    }

    @Test
    public void testHandleInputWithChallenge() {
        Challenge challenge = Mockito.mock(Challenge.class);
        when(challenge.getTo()).thenReturn("testUser");
        when(challenge.isSilent()).thenReturn(false);

        Entry entry = new Entry();
        entry.ce = dummyConnection;
        entry.name = "testUser";
        clients.addElement(entry);

        handler.handleInput(new InputEvent(challenge, dummyConnection, null));
        verify(dummyConnection).send(ArgumentMatchers.eq("/w testUser: " + challenge.roll()));
        verify(dummyConnection).send(ArgumentMatchers.eq("You rolled a " + challenge.roll()));
    }

    @Test
    public void testHandleInputWithChallengeRequest() {
        ChallReq challReq = Mockito.mock(ChallReq.class);
        String to = "testUser";

        Entry entry = new Entry();
        entry.ce = dummyConnection;
        entry.name = to;
        clients.addElement(entry);

        handler.handleInput(new InputEvent(challReq, dummyConnection, null));
        verify(dummyConnection).send(challReq);
    }

    @Test
    public void testHandleInputWithHero() {
        Hero hero = new Hero("testName");
        when(dummyConnection.getClientPort()).thenReturn(1);

        Entry entry = new Entry();
        entry.ce = dummyConnection;
        entry.hero = null;
        entry.name = "";
        clients.addElement(entry);

        handler.handleInput(new InputEvent(hero, dummyConnection, null));
        assertEquals("testName", clients.elementAt(0).name);
    }

    @Test
    public void testHandleInputWithSystemMessage() {
        when(dummyConnection.getClientPort()).thenReturn(1);
        String msg = "testMessage";

        handler.handleInput(new InputEvent(msg, dummyConnection, null));
        for (int i = 0; i < clients.size(); ++i) {
            try {
                clients.elementAt(i).ce.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
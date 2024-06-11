package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import dsachat.share.*;
import dsachat.share.hero.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpointMock;
    private Entry entryMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionEndpointMock = mock(ConnectionEndpoint.class);
        entryMock = new Entry();
        handler = new Handler();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpointMock);
        verify(connectionEndpointMock).send(any(UserEvent.class));
        verify(connectionEndpointMock, times(handler.clients.size())).send(any(UserEvent.class));
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        final String name = "testName";
        handler.addConnection(connectionEndpointMock);
        handler.clients.add(entryMock);
        entryMock.name = name;

        handler.removeConnection(connectionEndpointMock);
        verify(connectionEndpointMock).send(any(UserEvent.class));
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputWithString() {
        handler.addConnection(connectionEndpointMock);

        final String msg = "/roll 2d6+2";
        handler.handleInput(new InputEvent(this, connectionEndpointMock, new String(msg)));

        verify(connectionEndpointMock).send(anyString());
        verifyNoMoreInteractions(connectionEndpointMock);
    }

    @Test
    public void testHandleInputWithChallenge() {
        handler.addConnection(connectionEndpointMock);
        final Challenge challengeMock = mock(Challenge.class);

        handler.handleInput(new InputEvent(this, connectionEndpointMock, challengeMock));

        verify(connectionEndpointMock).send(anyString());
        verify(connectionEndpointMock).send(eq("to"), anyString());
        verifyNoMoreInteractions(connectionEndpointMock);
    }

    @Test
    public void testHandleInputWithChallReq() {
        handler.addConnection(connectionEndpointMock);
        final String to = "testTo";
        final ChallReq challReqMock = new ChallReq(to);

        handler.handleInput(new InputEvent(this, connectionEndpointMock, challReqMock));

        verify(connectionEndpointMock).send(any(ChallReq.class));
        verifyNoMoreInteractions(connectionEndpointMock);
    }

    @Test
    public void testHandleInputWithHero() {
        handler.addConnection(connectionEndpointMock);
        final Hero heroMock = mock(Hero.class);

        handler.handleInput(new InputEvent(this, connectionEndpointMock, heroMock));

        verify(connectionEndpointMock).send(anyString());
        verify(connectionEndpointMock).send(eq("name"), anyString());
        verifyNoMoreInteractions(connectionEndpointMock);
    }
}

class Entry {
    ConnectionEndpoint ce;
    String name;
    Hero hero;
}
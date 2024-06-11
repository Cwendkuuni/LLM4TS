import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import dsachat.server.*;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.*;
import java.util.*;
import org.junit.*;

public class HandlerTest {

    private Handler handler;

    @Before
    public void setUp() {
        handler = new Handler();
    }

    @Test
    public void testAddConnection() {
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1, "localhost", 12345);
        String name = "";
        Handler handler = new Handler();
        handler.addConnection(connectionEndpoint);
        Entry entry = handler.clients.lastElement();
        assertEquals("", entry.name);
        assertTrue(entry.ce.isConnected());
    }

    @Test(expected = Exception.class)
    public void testAddConnectionException() {
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1, "localhost", 12345);
        Handler handler = new Handler();
        handler.addConnection(connectionEndpoint);
        connectionEndpoint.disconnect();
        handler.addConnection(connectionEndpoint);
    }

    @Test
    public void testAddMultipleConnections() {
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1, "localhost", 12345);
        Handler handler = new Handler();
        for (int i = 0; i < 10; i++) {
            handler.addConnection(connectionEndpoint);
        }
        assertEquals(10, handler.clients.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddConnectionNullConnection() {
        Handler handler = new Handler();
        handler.addConnection(null);
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1, "localhost", 12345);
        Handler handler = new Handler();
        handler.addConnection(connectionEndpoint);
        String name = handler.removeConnection(connectionEndpoint).name;
        assertEquals("", name);
        assertEquals(0, handler.clients.size());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveConnectionNullConnection() {
        Handler handler = new Handler();
        handler.removeConnection(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveConnectionDisconnectedConnection() {
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1, "localhost", 12345);
        Handler handler = new Handler();
        handler.addConnection(connectionEndpoint);
        connectionEndpoint.disconnect();
        handler.removeConnection(connectionEndpoint);
    }

    @Test
    public void testHandleInputString() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        String input = "/help";
        when(connectionEndpoint.send(anyString())).thenReturn(true);
        this.handler.addConnection(connectionEndpoint);
        this.handler.handleInput(new InputEvent(connectionEndpoint, new String(input)));
        verify(connectionEndpoint).send(eq("/help not found"));
    }

    @Test
    public void testHandleInputChallenge() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        Hero hero = new Hero("testHero");
        Challenge challenge = new Challenge();
        when(connectionEndpoint.send(anyString())).thenReturn(true);
        this.handler.addConnection(connectionEndpoint);
        this.handler.handleInput(new InputEvent(connectionEndpoint, challenge));
        verify(connectionEndpoint, times(2)).send(anyString());
    }

    @Test
    public void testHandleInputChallReq() {
        ConnectionEndpoint connectionEndpoint1 = mock(ConnectionEndpoint.class);
        ConnectionEndpoint connectionEndpoint2 = mock(ConnectionEndpoint.class);
        when(connectionEndpoint1.getClientPort()).thenReturn(1);
        when(connectionEndpoint2.getClientPort()).thenReturn(2);
        Hero hero = new Hero("testHero");
        ChallReq challReq = new ChallReq("testUser", hero);
        this.handler.addConnection(connectionEndpoint1);
        this.handler.addConnection(connectionEndpoint2);
        this.handler.handleInput(new InputEvent(connectionEndpoint1, challReq));
        verify(connectionEndpoint2).send(eq(challReq));
    }

    @Test
    public void testHandleInputUserEvent() {
        ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        UserEvent userEvent = new UserEvent(true, "testUser");
        this.handler.addConnection(connectionEndpoint);
        this.handler.handleInput(new InputEvent(connectionEndpoint, userEvent));
        verify(this.handler).sendAllObj(eq(userEvent));
    }
}
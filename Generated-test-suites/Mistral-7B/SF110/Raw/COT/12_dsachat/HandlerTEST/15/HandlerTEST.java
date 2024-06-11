package dsachat.server.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.*;
import dsachat.share.hero.Hero;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private ConnectionEndpoint connectionEndpoint2;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = new ConnectionEndpoint(1, new ByteArrayOutputStream(1024));
        connectionEndpoint2 = new ConnectionEndpoint(2, new ByteArrayOutputStream(1024));
        hero = new Hero("TestHero");
    }

    

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
    }

    @Test(expected = IOException.class)
    public void testAddConnectionWithException() throws IOException {
        final ConnectionEndpoint cl = new ConnectionEndpoint(1, new ByteArrayOutputStream(1024));
        handler.addConnection(cl);
        handler.removeConnection(cl); // Clean up
        handler.addConnection(connectionEndpoint); // Simulate a second call to addConnection with the same port
        handler.handleInput(new InputEvent(connectionEndpoint, new String("/quit")));
        handler.removeConnection(connectionEndpoint); // Clean up
        handler.addConnection(connectionEndpoint);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputWithString() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);

        handler.handleInput(new InputEvent(connectionEndpoint, new String("/hello")));
        assertEquals(2, handler.clients.size());

        handler.removeConnection(connectionEndpoint);
        handler.handleInput(new InputEvent(connectionEndpoint, new String("/hello")));
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInputWithChallenge() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);

        final OutputStream outputStream = new ByteArrayOutputStream(1024);
        connectionEndpoint.setOutputStream(outputStream);
        connectionEndpoint2.setOutputStream(outputStream);

        handler.handleInput(new InputEvent(connectionEndpoint, new Challenge("TestRoll", connectionEndpoint2)));
        handler.handleInput(new InputEvent(connectionEndpoint2, new Challenge("TestRoll", connectionEndpoint)));

        final String expectedResult = "TestHero: TestRoll --> 1 1\nGamemaster: TestRoll --> 2 3\n";
        assertEquals(expectedResult, outputStream.toString());
    }

    @Test
    public void testHandleInputWithChallReq() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint2);

        handler.handleInput(new InputEvent(connectionEndpoint2, new ChallReq("TestHero", connectionEndpoint, "TestRoll")));

        assertEquals(1, handler.clients.size());
        assertTrue(handler.clients.elementAt(0).ce.getClientPort() == connectionEndpoint.getClientPort());
    }

    @Test
    public void testHandleInputWithHero() {
        handler.addConnection(connectionEndpoint);

        handler.handleInput(new InputEvent(connectionEndpoint, new Hero("TestHero2")));

        assertEquals(1, handler.clients.size());
        assertNull(handler.clients.elementAt(0).hero);

        handler.handleInput(new InputEvent(connectionEndpoint, new Hero("TestHero")));
        assertEquals(1, handler.clients.size());
        assertEquals(hero, handler.clients.elementAt(0).hero);
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new ConnectionEndpoint(1, new ByteArrayOutputStream(1024)));
        handler.addConnection(new ConnectionEndpoint(2, new ByteArrayOutputStream(1024)));
        handler.addConnection(new ConnectionEndpoint(3, new ByteArrayOutputStream(1024)));
        handler.setNameForConnections("Gamemaster", connectionEndpoint3);

        assertFalse(handler.isGmConnected());

        handler.handleInput(new InputEvent(connectionEndpoint3, new UserEvent(true, "Gamemaster")));

        assertTrue(handler.isGmConnected());
    }

    
}
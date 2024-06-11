package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Test;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.io.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Hero hero;
    private Vector<Entry> clients;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = new ConnectionEndpoint();
        clients = handler.getClients();
        hero = new Hero("TestHero");
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        Entry entry = clients.lastElement();
        assertEquals(connectionEndpoint, entry.ce);
        assertNull(entry.name);
    }

    @Test(expected = Exception.class)
    public void testAddConnection_ThrowsException() {
        handler.addConnection(null);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertNull(clients.lastElement());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveConnection_NullConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(null);
    }

    @Test
    public void testHandleInput_String() {
        handler.addConnection(connectionEndpoint);
        handler.handleInput(new InputEvent(this, connectionEndpoint, new String("test")));
        String expected = handler.getName(connectionEndpoint) + ": test";
        assertEquals(expected, clients.lastElement().name);
    }

    @Test
    public void testHandleInput_Challenge() {
        handler.addConnection(connectionEndpoint);
        Challenge challenge = new Challenge();
        handler.handleInput(new InputEvent(this, connectionEndpoint, challenge));
        String expected = challenge.roll();
        assertEquals(expected, clients.lastElement().name);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleInput_NullObject() {
        handler.handleInput(new InputEvent(this, connectionEndpoint, null));
    }

    @Test
    public void testHandleInput_ChallReq() {
        handler.addConnection(connectionEndpoint1 = new ConnectionEndpoint());
        handler.addConnection(connectionEndpoint2 = new ConnectionEndpoint());
        handler.handleInput(new InputEvent(this, connectionEndpoint1, new ChallReq("Player1", "Player2")));
        assertEquals("Player2", clients.get(1).name);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleInput_NullChallReq() {
        handler.handleInput(new InputEvent(this, connectionEndpoint, null));
    }

    @Test
    public void testHandleInput_Hero() {
        handler.addConnection(connectionEndpoint);
        handler.handleInput(new InputEvent(this, connectionEndpoint, hero));
        String expected = hero.getName();
        assertEquals(expected, clients.lastElement().name);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleInput_NullHero() {
        handler.handleInput(new InputEvent(this, connectionEndpoint, null));
    }

    @Test
    public void testSendHeros() {
        handler.addConnection(connectionEndpoint);
        Hero hero1 = new Hero("Hero1");
        handler.addConnection(connectionEndpoint2 = new ConnectionEndpoint());
        handler.sendHeros(connectionEndpoint);
        assertEquals(0, clients.get(0).hero.getHealth());
        assertEquals(hero1, clients.get(1).hero);
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("Gamemaster"));
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        String msg = "Test Message";
        handler.sendAllSystemMsg(msg);
        for (int i = 0; i < clients.size(); ++i) {
            assertEquals(msg, clients.get(i).ce.receivedMessage);
        }
    }

    @Test
    public void testWhisper() {
        handler.addConnection(connectionEndpoint1 = new ConnectionEndpoint());
        handler.addConnection(connectionEndpoint2 = new ConnectionEndpoint());
        String expected = "TestHero whispers: test";
        handler.whisper(connectionEndpoint1, "TestHero test");
        assertEquals(expected, clients.get(1).ce.receivedMessage);
    }

    @Test
    public void testRollDice() {
        handler.addConnection(connectionEndpoint);
        String cmd = "1d6+3";
        String expected = handler.getName(connectionEndpoint) + " rolled " + cmd + " --> 4";
        handler.rollDice(connectionEndpoint, cmd);
        assertEquals(expected, clients.lastElement().ce.receivedMessage);
    }

    @Test
    public void testSendToOneStr() {
        handler.addConnection(connectionEndpoint1 = new ConnectionEndpoint("Player1"));
        handler.addConnection(connectionEndpoint2 = new ConnectionEndpoint("Player2"));
        String expected = "TestHero: test";
        handler.sendToOneStr(connectionEndpoint1, "Player2", "test");
        assertEquals(expected, clients.get(0).ce.receivedMessage);
    }

    @Test
    public void testUpdateHero() {
        handler.addConnection(connectionEndpoint);
        handler.updateHero(connectionEndpoint, hero);
        assertEquals(hero.getName(), clients.lastElement().name);
    }

    @Test
    public void testSendAllStr() {
        handler.addConnection(connectionEndpoint);
        String message = "Test Message";
        handler.sendAllStr(connectionEndpoint, message);
        assertEquals(message, clients.lastElement().ce.receivedMessage);
    }

    @Test
    public void testSendAllObj() {
        handler.addConnection(connectionEndpoint);
        Object obj
            = new Vector<Hero>() {
                {
                    add(new Hero("Hero1"));
                    add(new Hero("Hero2"));
                }
            };
        handler.sendAllObj(obj);
        Vector<Hero> expected = (Vector<Hero>) obj;
        for (int i = 0; i < clients.size(); ++i) {
            assertEquals(expected.get(i).getName(), clients.get(i).hero.getName());
        }
    }

    @Test
    public void testGetName() {
        handler.addConnection(connectionEndpoint);
        String expected = handler.getName(connectionEndpoint);
        assertNull(expected);
    }
}
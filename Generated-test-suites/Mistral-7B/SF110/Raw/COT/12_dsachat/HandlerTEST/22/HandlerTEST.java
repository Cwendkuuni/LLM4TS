package dsachat.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = new ConnectionEndpoint();
        hero = new Hero("TestHero");
    }

    

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
    }

    @Test(expected = Exception.class)
    public void testAddConnection_Exception() {
        ConnectionEndpoint connectionEndpoint = null;
        handler.addConnection(connectionEndpoint);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveConnection_NullName() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        handler.removeConnection(null);
    }

    @Test
    public void testHandleInput_String() {
        handler.addConnection(connectionEndpoint);
        String message = "test message";
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{message}));
        assertTrue(handler.clients.size() > 0);
        assertEquals(message, handler.clients.elementAt(0).ce.getSendQueue().poll());
    }

    @Test
    public void testHandleInput_Challenge() {
        handler.addConnection(connectionEndpoint);
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{new Challenge()}));
        assertTrue(handler.clients.size() > 0);
    }

    @Test
    public void testHandleInput_ChallReq() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(new ConnectionEndpoint());
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{new ChallReq("TestUser")}));
        assertEquals("TestUser", handler.clients.elementAt(1).name);
    }

    @Test
    public void testHandleInput_Hero() {
        handler.addConnection(connectionEndpoint);
        handler.handleInput(new InputEvent(connectionEndpoint, new Object[]{hero}));
        assertEquals(hero.getName(), handler.getName(connectionEndpoint));
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("Gamemaster"));
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testIsGmConnected_NoGm() {
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("TestUser"));
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        String message = "test message";
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint());
        handler.sendAllSystemMsg(message);
        for (Entry entry : handler.clients) {
            assertEquals(message, entry.ce.getSendQueue().poll());
        }
    }

    @Test
    public void testWhisper() {
        handler.addConnection(new ConnectionEndpoint("User1"));
        handler.addConnection(new ConnectionEndpoint("User2"));
        handler.whisper(connectionEndpoint, "User2 test");
        assertEquals("User1 whispers: User2 test", handler.clients.elementAt(0).ce.getSendQueue().poll().toString());
        assertEquals("You whisper to User2: test", handler.clients.elementAt(1).ce.getSendQueue().poll().toString());
    }

    @Test
    public void testRollDice() {
        handler.addConnection(new ConnectionEndpoint());
        handler.rollDice(connectionEndpoint, "1d6");
        Challenge challenge = new Challenge();
        assertEquals(String.valueOf(challenge.dice(6)), handler.clients.elementAt(0).ce.getSendQueue().poll().toString());

        handler.rollDice(connectionEndpoint, "1d6+1");
        assertEquals(String.valueOf(challenge.dice(6) + 1), handler.clients.elementAt(0).ce.getSendQueue().poll().toString());
    }

    @Test
    public void testRollDice_MultipleDice() {
        handler.addConnection(new ConnectionEndpoint());
        handler.rollDice(connectionEndpoint, "3d6");
        String diceResult = "";
        for (int i = 0; i < 3; i++) {
            diceResult += String.valueOf(challenge.dice(6)) + " ";
        }
        assertEquals(diceResult, handler.clients.elementAt(0).ce.getSendQueue().poll().toString());
    }

    
}
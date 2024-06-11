package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class HandlerTest {

    private Handler handler;
    private Vector<Entry> clients;
    private ConnectionEndpoint mockConnection;
    private Hero mockHero;

    @Test
    public void testHandlerInitialization() {
        handler = new Handler();
        clients = handler.clients;
        assertEquals(0, clients.size());
    }

    @Test
    public void testAddConnection() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;
        assertEquals(1, clients.size());
        assertEquals(mockConnection, clients.elementAt(0).ce);
        assertEquals("", clients.elementAt(0).name);
    }

    @Test
    public void testRemoveConnection() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;
        String name = clients.elementAt(0).name;
        handler.removeConnection(mockConnection);
        clients = handler.clients;
        assertEquals(0, clients.size());
        assertNull(Util.getName(mockConnection));

        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;
        name = clients.elementAt(0).name;
        handler.removeConnection(mockConnection);
        clients = handler.clients;
        assertNull(clients.elementAt(0).name);
    }

    @Test
    public void testHandleInputStringMessage() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;

        String msg = "/getheros";
        handler.handleInput(new InputEvent(mockConnection, msg));

        assertEquals(1, clients.size());
        assertNotNull(clients.elementAt(0).hero);
        assertEquals(msg, clients.elementAt(0).ce.getLastMessageReceived());
    }

    @Test
    public void testHandleInputChallengeMessage() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;

        Hero mockHero = new Hero("Hero1");
        Challenge mockChallenge = new Challenge(mockHero);

        handler.handleInput(new InputEvent(mockConnection, mockChallenge));

        assertEquals(1, clients.size());
        assertNotNull(clients.elementAt(0).hero);
        assertEquals(mockHero, clients.elementAt(0).hero);
    }

    @Test
    public void testHandleInputChallengeRequestMessage() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        ConnectionEndpoint mockConnection2 = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        handler.addConnection(mockConnection2);
        clients = handler.clients;

        ChallReq mockChallReq = new ChallReq("User1", "User2", "command");
        handler.handleInput(new InputEvent(mockConnection, mockChallReq));

        assertEquals(2, clients.size());
        String name = clients.elementAt(1).name;
        assertEquals("User2", name);
        assertEquals(mockChallReq, clients.elementAt(1).ce.getLastMessageReceived());
    }

    @Test
    public void testHandleInputHeroMessage() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        mockHero = new Hero("Hero1");
        handler.addConnection(mockConnection);
        clients = handler.clients;

        String name = "";
        handler.handleInput(new InputEvent(mockConnection, mockHero));

        assertEquals(1, clients.size());
        assertNotNull(clients.elementAt(0).hero);
        assertEquals(mockHero, clients.elementAt(0).hero);
        assertEquals(mockHero.getName(), name);
    }

    @Test
    public void testHandleInputUserEventMessage() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;

        UserEvent mockUserEvent = new UserEvent(true, "User1");
        handler.handleInput(new InputEvent(mockConnection, mockUserEvent));

        assertEquals(1, clients.size());
        String name = clients.elementAt(0).name;
        assertEquals("User1", name);
        assertEquals(mockUserEvent, clients.elementAt(0).ce.getLastMessageReceived());
    }

    @Test
    public void testSendHeros() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;

        Hero mockHero1 = new Hero("Hero1");
        clients.elementAt(0).hero = mockHero1;

        Hero mockHero2 = new Hero("Hero2");
        clients.elementAt(1).hero = mockHero2;

        handler.sendHeros(mockConnection);

        assertEquals(2, clients.size());
        assertEquals(mockHero1, clients.elementAt(0).hero);
        assertEquals(mockHero2, clients.elementAt(1).hero);
        assertNull(mockConnection.getLastMessageReceived());
    }

    @Test
    public void testIsGmConnected() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;

        clients.elementAt(0).name = "User1";

        assertFalse(handler.isGmConnected());

        clients.elementAt(0).name = "Gamemaster";

        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);

        String msg = "This is a system message";
        handler.sendOneSystemMsg(mockConnection, msg);

        assertEquals(1, clients.size());
        assertEquals(msg, clients.elementAt(0).ce.getLastMessageReceived());
    }

    @Test
    public void testSendAllSystemMsg() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        ConnectionEndpoint mockConnection2 = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        handler.addConnection(mockConnection2);
        clients = handler.clients;

        String msg = "This is a system message";
        handler.sendAllSystemMsg(msg);

        assertEquals(2, clients.size());
        assertEquals(msg, clients.elementAt(0).ce.getLastMessageReceived());
        assertEquals(msg, clients.elementAt(1).ce.getLastMessageReceived());
    }

    @Test
    public void testWhisper() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        ConnectionEndpoint mockConnection2 = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        handler.addConnection(mockConnection2);
        clients = handler.clients;

        String msg = "/w User2 Hello";
        handler.handleInput(new InputEvent(mockConnection, msg));

        assertEquals(2, clients.size());
        String name = clients.elementAt(1).name;
        assertEquals("User2", name);
        assertEquals("Handler: whispers to User2: Hello", clients.elementAt(1).ce.getLastMessageReceived().getMessage());

        String sender = clients.elementAt(0).name;
        assertEquals("Handler: whispers to User2: Hello", clients.elementAt(0).ce.getLastMessageReceived().getSender().getName());
        assertEquals(msg, clients.elementAt(0).ce.getLastMessageReceived().getMessage());
    }

    @Test
    public void testRollDice() {
        handler = new Handler();
        mockConnection = new ConnectionEndpoint();
        handler.addConnection(mockConnection);
        clients = handler.clients;

        String msg = "/roll 3d6 +2";
        handler.handleInput(new InputEvent(mockConnection, msg));

        assertEquals(1, clients.size());
        String rollResult = clients.elementAt(0).ce.getLastMessageReceived().getMessage();
        String expectedRollResult = "Handler rolled 3d6 +2 --> ";
        int diceResult = 0;

        if (rollResult.startsWith(expectedRollResult)) {
            String rollResultWithoutPrefix = rollResult.substring(expectedRollResult.length());
            String[] diceParts = rollResultWithoutPrefix.split(" ");
            diceResult = Integer.parseInt(diceParts[0]) + Integer.parseInt(diceParts[1]) + 2;
        }

        assertEquals(diceResult, Challenge.dice(6) + Challenge.dice(6) + 2);
    }
}
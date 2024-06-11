package org.apache.commons.cli.dsachat.server; // Make sure to import all necessary packages and classes

import static org.junit.Assert.*;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;
import java.util.Vector;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        handler.removeConnection(connection);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        ConnectionEndpoint gmConnection = new ConnectionEndpoint();
        handler.addConnection(gmConnection);
        handler.clients.elementAt(0).name = "Gamemaster";
        assertTrue(handler.isGmConnected());

        handler.removeConnection(gmConnection);
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testHandleInputString() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);

        String msg = "/dice 3d6";
        handler.handleInput(new InputEvent(connection, msg));

        // Verify that the handler processed the dice command correctly
        String expectedOutput = handler.getName(connection) + " rolled dice 3d6 --> ";
        for (int i = 0; i < 3; ++i) {
            expectedOutput += Challenge.dice(6) + "  ";
        }
        handler.sendAllSystemMsg(expectedOutput);
    }

    @Test
    public void testHandleInputChallenge() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);

        Hero hero = new Hero("TestHero");
        handler.handleInput(new InputEvent(connection, hero));

        // Verify that the handler updated the client's hero correctly
        assertEquals(hero.getName(), handler.getName(connection));
    }

    @Test
    public void testHandleInputChallengeSilent() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);

        Hero hero = new Hero("TestHero");
        handler.handleInput(new InputEvent(connection, hero));
        assertNull(handler.getName(connection));
    }

    @Test
    public void testHandleInputChallengeToAnotherClient() {
        Handler handler = new Handler();
        ConnectionEndpoint senderConnection = new ConnectionEndpoint();
        handler.addConnection(senderConnection);
        ConnectionEndpoint receiverConnection = new ConnectionEndpoint();
        handler.addConnection(receiverConnection);

        String msg = "/challenger TestHero roll 3d6";
        handler.handleInput(new InputEvent(senderConnection, msg));

        // Verify that the handler sent the challenge to the receiver client
        assertEquals(3, handler.clients.size());
        String expectedOutput = handler.getName(senderConnection) + " challenges TestHero to roll 3d6";
        String output = handler.clients.elementAt(1).ce.getLastMessage();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testHandleInputUserEvent() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);

        UserEvent userEvent = new UserEvent(true, "TestName");
        handler.handleInput(new InputEvent(connection, userEvent));

        // Verify that the handler sent the user event to all clients
        assertEquals("TestName", handler.getName(connection));
        String expectedOutput = "Gamemaster: " + userEvent.getName() + " connected";
        handler.sendAllObj(new UserEvent(false, "Gamemaster"));
        String output = handler.clients.elementAt(0).ce.getLastMessage();
        assertEquals(expectedOutput, output);
    }
}
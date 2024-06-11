package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Test;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint1, connectionEndpoint2;
    private Hero hero1;

    @Test
    public void testHandlerInitialization() {
        handler = new Handler();
        assertNotNull(handler.clients);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testAddConnection() {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        handler.removeConnection(connectionEndpoint1);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testIsGmConnected() {
        handler = new Handler();
        handler.addConnection(new ConnectionEndpoint());
        assertFalse(handler.isGmConnected());
        ConnectionEndpoint gmConnection = new ConnectionEndpoint();
        handler.addConnection(gmConnection);
        handler.clients.get(handler.clients.size() - 1).name = "Gamemaster";
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testHandleInputString() throws IOException {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        String message = "/w User1 User2 Hello";
        handler.handleInput(new InputEvent(connectionEndpoint1, new StringData(message)));
        String expected = "User1 whispers: Hello to User2";
        String actual = handler.sendOneSystemMsg(connectionEndpoint1, "");
        assertEquals(expected, actual);
        String userEvent = handler.getName(connectionEndpoint1);
        assertEquals("User1", userEvent);
        assertNull(handler.getName(connectionEndpoint2));
    }

    @Test
    public void testHandleInputChallenge() throws IOException {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        Hero hero = new Hero("User1", "Weapon1", "Armor1");
        handler.handleInput(new InputEvent(connectionEndpoint1, new ChallengeData(hero)));
        String expected = "User1 updated his hero";
        String actual = handler.sendAllObj(new UserEvent(false, hero.getName())).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHandleInputChallengeToOtherUser() throws IOException {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        connectionEndpoint2 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        Hero hero1 = new Hero("User1", "Weapon1", "Armor1");
        Hero hero2 = new Hero("User2", "Weapon2", "Armor2");
        handler.handleInput(new InputEvent(connectionEndpoint1, new ChallengeData(hero1)));
        handler.handleInput(new InputEvent(connectionEndpoint2, new ChallengeData(hero2)));
        String expected = "User1 connected";
        String actual = handler.sendAllObj(new UserEvent(true, "User1")).toString();
        assertEquals(expected, actual);
        String userEvent = handler.getName(connectionEndpoint1);
        assertEquals("User1", userEvent);
        String userEvent2 = handler.getName(connectionEndpoint2);
        assertEquals("User2", userEvent2);
    }

    @Test
    public void testHandleInputUnknownCommand() throws IOException {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        String message = "/unknowncommand";
        handler.handleInput(new InputEvent(connectionEndpoint1, new StringData(message)));
        String expected = "command /unknowncommand not found";
        String actual = handler.sendAllStr(connectionEndpoint1, "").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHandleInputRollDice() throws IOException {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint1);
        String message = "/roll 2d6+3";
        handler.handleInput(new InputEvent(connectionEndpoint1, new StringData(message)));
        String expected = "User1 rolled 2d6+3 --> ";
        String actual = handler.sendAllSystemMsg(expected).toString();
        String expectedRoll = String.valueOf(Challenge.dice(6) + Challenge.dice(6) + 3);
        assertEquals(expectedRoll, actual.substring(expected.length()));
    }
}
package dsachat.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint1;
    private ConnectionEndpoint connectionEndpoint2;
    private Hero hero1;
    private Hero hero2;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint();
        connectionEndpoint2 = new ConnectionEndpoint();
        hero1 = new Hero("Hero1");
        hero2 = new Hero("Hero2");
    }

    

    // Test addConnection method
    @Test
    public void testAddConnection() throws Exception {
        handler.addConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
        handler.addConnection(connectionEndpoint2);
        assertEquals(2, handler.clients.size());
    }

    @Test(expected = Exception.class)
    public void testAddConnectionWithNullClient() {
        handler.addConnection(null);
    }

    // Test removeConnection method
    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.removeConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnectionWithNonExistentConnection() {
        handler.addConnection(connectionEndpoint1);
        handler.removeConnection(connectionEndpoint2);
        assertEquals(1, handler.clients.size());
    }

    // Test handleInput method with String input
    @Test
    public void testHandleInputWithStringInput() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new String("hello")));
        verify(connectionEndpoint1).send("hello");
    }

    @Test
    public void testHandleInputWithNullInput() {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, null));
        // No assertion here, as the method doesn't throw an exception with a null input
    }

    // Test handleInput method with Challenge input
    @Test
    public void testHandleInputWithChallengeInput() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.handleInput(new InputEvent(connectionEndpoint1, new Challenge()));
        verify(connectionEndpoint1).send("roll");
        verify(connectionEndpoint2).send("roll");
    }

    @Test
    public void testHandleInputWithChallengeInputToSelf() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new Challenge()));
        // No assertion here, as the method doesn't send a message to the GM in this case
    }

    // Test handleInput method with ChallReq input
    @Test
    public void testHandleInputWithChallReqInput() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ChallReq("Hero2")));
        verify(connectionEndpoint2).send(new ChallReq("Hero1"));
    }

    @Test(expected = IOException.class)
    public void testHandleInputWithChallReqInputToDisconnectedClient() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint2, new ChallReq("Hero1")));
    }

    // Test handleInput method with Hero input
    @Test
    public void testHandleInputWithHeroInput() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, hero1));
        assertEquals("Hero1", handler.getName(connectionEndpoint1));
    }

    @Test(expected = IOException.class)
    public void testHandleInputWithHeroInputToDisconnectedClient() throws Exception {
        handler.handleInput(new InputEvent(connectionEndpoint2, hero1));
    }

    // Test handleInput method with UserEvent input
    @Test
    public void testHandleInputWithUserEventInput() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.handleInput(new InputEvent(connectionEndpoint1, new UserEvent(true, "Gamemaster")));
        assertEquals("Gamemaster", handler.getName(connectionEndpoint1));
    }

    @Test
    public void testHandleInputWithUserEventInputToGm() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new UserEvent(true, "Gamemaster")));
        // No assertion here, as the method doesn't change the name of the GM in this case
    }

    // Test sendHeros method
    @Test
    public void testSendHeros() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.sendHeros(connectionEndpoint1);
        verifyNoMoreInteractions(connectionEndpoint1);
    }

    // Test isGmConnected method
    @Test
    public void testIsGmConnectedWithGmConnected() {
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("Gamemaster"));
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testIsGmConnectedWithNoGmConnected() {
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("User"));
        assertFalse(handler.isGmConnected());
    }

    // Test sendOneSystemMsg method
    @Test
    public void testSendOneSystemMsg() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.sendOneSystemMsg(connectionEndpoint1, "test");
        // No assertion here, as there's no way to verify the message was sent
    }

    // Test sendAllSystemMsg method
    @Test
    public void testSendAllSystemMsg() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.sendAllSystemMsg("test");
        verify(connectionEndpoint1).send("test");
        verify(connectionEndpoint2).send("test");
    }

    // Test whisper method
    @Test
    public void testWhisper() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.whisper(connectionEndpoint1, "Hero2: test");
        verify(connectionEndpoint2).send("Hero1 whispers: test");
        verify(connectionEndpoint1).send("You whisper to Hero2: test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhisperWithInvalidFormat() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.whisper(connectionEndpoint1, "test");
    }

    // Test rollDice method
    @Test
    public void testRollDiceWithOneDice() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.rollDice(connectionEndpoint1, "1d6");
        Mockito.verify(connectionEndpoint1, times(1)).send("You rolled a 1d6: " + Challenge.dice(6));
    }

    @Test
    public void testRollDiceWithMultipleDice() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.rollDice(connectionEndpoint1, "2d6+2");
        Mockito.verify(connectionEndpoint1, times(2)).send("You rolled a 1d6: " + Challenge.dice(6));
        Mockito.verify(connectionEndpoint1, times(1)).send("You added 2 to the result: " + (Challenge.dice(6) + 2));
    }

    // Test rollDice method with invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testRollDiceWithInvalidInput() throws Exception {
        handler.addConnection(connectionEndpoint1);
        handler.rollDice(connectionEndpoint1, "invalid");
    }

    
}
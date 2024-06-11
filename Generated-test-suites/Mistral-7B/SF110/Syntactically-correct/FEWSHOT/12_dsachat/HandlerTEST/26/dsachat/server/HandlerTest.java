package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Test;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.util.regex.*;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(12345);
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        assertEquals(connectionEndpoint.getClientPort(), handler.clients.elementAt(0).ce.getClientPort());
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint1 = new ConnectionEndpoint(12345);
        ConnectionEndpoint connectionEndpoint2 = new ConnectionEndpoint(54321);
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        assertEquals(2, handler.clients.size());

        handler.removeConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
        assertNull(handler.getName(connectionEndpoint1));

        handler.removeConnection(connectionEndpoint2);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputStringMessage() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(12345);
        handler.addConnection(connectionEndpoint);

        String msg = "/roll 2d6+1";
        handler.handleInput(new InputEvent(connectionEndpoint, msg));

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }

    @Test
    public void testHandleInputChallengeObject() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(12345);
        handler.addConnection(connectionEndpoint);

        Challenge challenge = new Challenge();
        handler.handleInput(new InputEvent(connectionEndpoint, challenge));

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }

    @Test
    public void testHandleInputChallReqObject() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint1 = new ConnectionEndpoint(12345);
        ConnectionEndpoint connectionEndpoint2 = new ConnectionEndpoint(54321);
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        String to = "user2";
        ChallReq challReq = new ChallReq(to, "message");
        handler.handleInput(new InputEvent(connectionEndpoint1, challReq));

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }

    @Test
    public void testHandleInputHeroObject() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(12345);
        Hero hero = new Hero("heroName");
        handler.addConnection(connectionEndpoint);

        handler.handleInput(new InputEvent(connectionEndpoint, hero));

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        String string = "system message";
        handler.sendAllSystemMsg(string);

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(12345);
        String to = "user2";
        String cmd = "whisper user2 message";
        handler.addConnection(connectionEndpoint);

        handler.whisper(connectionEndpoint, cmd);

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }

    @Test
    public void testRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(12345);
        String cmd = "roll 2d6+1";
        handler.addConnection(connectionEndpoint);

        handler.rollDice(connectionEndpoint, cmd);

        // You might need to mock or stub the send methods here
        // or use a mocking framework like Mockito to test the send methods
    }
}
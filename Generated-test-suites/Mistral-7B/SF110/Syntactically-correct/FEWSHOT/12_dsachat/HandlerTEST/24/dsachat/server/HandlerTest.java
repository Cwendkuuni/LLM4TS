package dsachat.server;

import static org.junit.Assert.*;
import org.junit.Test;

import dsachat.share.*;
import dsachat.share.hero.*;
import java.util.*;
import java.io.*;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        assertNotNull(handler.clients.elementAt(0).name);
        assertNull(handler.clients.elementAt(0).hero);
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputString() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        String msg = "/roll 2d6+3";
        InputEvent event = new InputEvent(connectionEndpoint, msg);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'rollDice' method.

        msg = "/whisper foo bar";
        event = new InputEvent(connectionEndpoint, msg);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'whisper' method.
    }

    @Test
    public void testHandleInputChallenge() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        Challenge challenge = new Challenge();
        InputEvent event = new InputEvent(connectionEndpoint, challenge);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'roll' method and the 'sendToOneStr' method.
    }

    @Test
    public void testHandleInputChallReq() {
        Handler handler = new Handler();
        ConnectionEndpoint senderConnectionEndpoint = new ConnectionEndpoint(5678);
        ConnectionEndpoint receiverConnectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(senderConnectionEndpoint);
        handler.addConnection(receiverConnectionEndpoint);
        ChallReq challReq = new ChallReq("receiver");
        InputEvent event = new InputEvent(senderConnectionEndpoint, challReq);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'sendOneSystemMsg' method.
    }

    @Test
    public void testHandleInputHero() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        Hero hero = new Hero("testHero");
        InputEvent event = new InputEvent(connectionEndpoint, hero);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'updateHero' method and the 'sendAllObj' method.
    }

    @Test
    public void testHandleInputUserEvent() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        UserEvent userEvent = new UserEvent(false, "testName");
        InputEvent event = new InputEvent(connectionEndpoint, userEvent);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'sendAllObj' method.
    }

    @Test
    public void testSendHeros() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        Hero hero1 = new Hero("hero1");
        Hero hero2 = new Hero("hero2");
        handler.clients.add(new Entry());
        handler.clients.get(0).hero = hero1;
        handler.clients.add(new Entry());
        handler.clients.get(1).hero = hero2;
        handler.sendHeros(connectionEndpoint);
        // You may need to add assertions here based on the expected behavior of the 'sendHeros' method.
    }

    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        ConnectionEndpoint gmConnectionEndpoint = new ConnectionEndpoint(5678);
        ConnectionEndpoint clientConnectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(gmConnectionEndpoint);
        handler.addConnection(clientConnectionEndpoint);
        assertTrue(handler.isGmConnected());
        handler.clients.get(0).name = "Gamemaster";
        handler.clients.get(1).name = "client";
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        String msg = "test message";
        handler.sendOneSystemMsg(connectionEndpoint, msg);
        // You may need to add assertions here based on the expected behavior of the 'sendOneSystemMsg' method.
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint1 = new ConnectionEndpoint(1234);
        ConnectionEndpoint connectionEndpoint2 = new ConnectionEndpoint(5678);
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        String msg = "test message";
        handler.sendAllSystemMsg(msg);
        // You may need to add assertions here based on the expected behavior of the 'sendAllSystemMsg' method.
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint senderConnectionEndpoint = new ConnectionEndpoint(5678);
        ConnectionEndpoint receiverConnectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(senderConnectionEndpoint);
        handler.addConnection(receiverConnectionEndpoint);
        String msg = "/whisper receiver test message";
        InputEvent event = new InputEvent(senderConnectionEndpoint, msg);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'whisper' method.
    }

    @Test
    public void testRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        String msg = "/roll 2d6+3";
        InputEvent event = new InputEvent(connectionEndpoint, msg);
        handler.handleInput(event);
        // You may need to add assertions here based on the expected behavior of the 'rollDice' method.
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.hero.Hero;

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
    public void testHandleInput_StringMessage() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        String message = "test";
        handler.handleInput(new InputEvent(connection, message));
        // Check if message was sent to all clients
        // Add assertions for sending message to all clients
    }

    @Test
    public void testHandleInput_Challenge() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        Challenge challenge = new Challenge();
        handler.handleInput(new InputEvent(connection, challenge));
        // Check if challenge was rolled and sent to the intended recipient
        // Add assertions for rolling and sending challenge
    }

    @Test
    public void testHandleInput_ChallReq() {
        Handler handler = new Handler();
        ConnectionEndpoint sender = new ConnectionEndpoint();
        ConnectionEndpoint receiver = new ConnectionEndpoint();
        ChallReq challengeReq = new ChallReq("receiver");
        handler.addConnection(sender);
        handler.addConnection(receiver);
        handler.handleInput(new InputEvent(sender, challengeReq));
        // Check if challenge request was sent to the intended recipient
        // Add assertions for sending challenge request
    }

    @Test
    public void testHandleInput_Hero() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        Hero hero = new Hero("testHero");
        handler.handleInput(new InputEvent(connection, hero));
        // Check if hero was updated and connected to the client
        // Add assertions for updating and connecting hero
    }

    @Test
    public void testIsGmConnected() {
        Handler handler = new Handler();
        handler.addConnection(new ConnectionEndpoint());
        handler.addConnection(new ConnectionEndpoint("Gamemaster"));
        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        Handler handler = new Handler();
        String message = "test";
        handler.sendAllSystemMsg(message);
        // Check if message was sent to all clients
        // Add assertions for sending message to all clients
    }

    @Test
    public void testWhisper() {
        Handler handler = new Handler();
        ConnectionEndpoint sender = new ConnectionEndpoint();
        ConnectionEndpoint receiver = new ConnectionEndpoint();
        handler.addConnection(sender);
        handler.addConnection(receiver);
        String message = "test";
        handler.whisper(sender, receiver.getName() + " " + message);
        // Check if message was sent to the intended recipient
        // Add assertions for sending whisper
    }

    @Test
    public void testRollDice() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        String command = "3d6+2";
        handler.handleInput(new InputEvent(connection, command));
        // Check if dice were rolled and result was sent to the client
        // Add assertions for rolling and sending dice
    }
}
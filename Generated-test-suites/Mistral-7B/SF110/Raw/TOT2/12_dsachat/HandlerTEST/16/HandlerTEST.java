import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.server.UserEvent;
import dsachat.server.Challenge;
import dsachat.server.ChallReq;
import dsachat.server.Hero;
import org.junit.Before;
import java.io.IOException;

public class HandlerTest {

    private Handler handler;

    @Before
    public void setUp() {
        handler = new Handler();
    }

    @Test
    public void testAddConnection() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);

        // Test normal usage
        handler.addConnection(connection);
        Entry addedEntry = handler.clients.lastElement();
        assertEquals(connection, addedEntry.ce);
        assertTrue(addedEntry.name.isEmpty());

        // Test edge case: check if the method throws an exception when trying to add a connection while another one is already connected
        ConnectionEndpoint existingConnection = new ConnectionEndpoint(12345);
        handler.addConnection(existingConnection);
        try {
            handler.addConnection(connection);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testRemoveConnection() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);

        // Test normal usage
        handler.removeConnection(connection);
        assertTrue(handler.clients.isEmpty());

        // Test edge case: check if the method removes the correct client when called multiple times
        handler.addConnection(new ConnectionEndpoint(54321));
        handler.removeConnection(connection);
        handler.removeConnection(connection);
        assertTrue(handler.clients.isEmpty());
    }

    @Test
    public void testRemoveConnectionWithName() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);
        handler.addConnection(new ConnectionEndpoint(67890));
        String name = "John";
        handler.clients.elementAt(0).name = name;

        // Test normal usage: remove a client by its name
        handler.removeConnection("John");
        assertTrue(handler.clients.size() == 1);
    }

    @Test
    public void testHandleInputStringCommand() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);

        // Test normal usage: handle a simple string command
        handler.handleInput(new InputEvent(connection, new String("hello")));

        // Test edge case: handle a command that starts with "/getheros"
        handler.handleInput(new InputEvent(connection, new String("/getheros")));

        // Test error case: handle an invalid command
        handler.handleInput(new InputEvent(connection, new String("invalidCommand")));
    }

    @Test
    public void testHandleInputChallengeCommand() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);

        // Test normal usage: handle a challenge command
        Challenge challenge = new Challenge();
        handler.handleInput(new InputEvent(connection, challenge));

        // Test edge case: handle a challenge command with a recipient
        Challenge challengeWithRecipient = new Challenge();
        String recipient = "John";
        challenge.setTo(recipient);
        handler.handleInput(new InputEvent(connection, challenge));

        // Test error case: handle a challenge command when there is no recipient connected
        challenge.setTo("Bob");
        handler.handleInput(new InputEvent(connection, challenge));
    }

    @Test
    public void testHandleInputHeroCommand() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);

        // Test normal usage: handle a hero command
        Hero hero = new Hero("John");
        handler.handleInput(new InputEvent(connection, hero));

        // Test edge case: handle a hero command with a name that already exists
        Hero existingHero = new Hero("John");
        handler.handleInput(new InputEvent(connection, existingHero));

        // Test error case: handle a hero command when there is no client connected
        Hero invalidHero = new Hero("Bob");
        handler.handleInput(new InputEvent(connection, invalidHero));
    }

    @Test
    public void testHandleInputUserEventCommand() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);

        // Test normal usage: handle a user event command
        UserEvent userEvent = new UserEvent(false, "John");
        handler.handleInput(new InputEvent(connection, userEvent));

        // Test error case: handle a user event command when there is no recipient connected
        UserEvent userEvent2 = new UserEvent(false, "Bob");
        handler.handleInput(new InputEvent(connection, userEvent2));
    }

    @Test
    public void testHandleInputGmConnectedCommand() {
        ConnectionEndpoint connection = new ConnectionEndpoint(12345);
        handler.addConnection(connection);

        // Test normal usage: handle a command when the Gamemaster is connected
        UserEvent userEvent = new UserEvent(true, "Gamemaster");
        handler.handleInput(new InputEvent(connection, userEvent));

        // Test error case: handle a command when the Gamemaster is not connected
        UserEvent userEvent2 = new UserEvent(true, "Gamemaster");
        handler.handleInput(new InputEvent(connection, userEvent2));
    }
}
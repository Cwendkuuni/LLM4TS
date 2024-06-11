import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.server.Entry;
import dsachat.server.UserEvent;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        String expectedName = "";

        handler.addConnection(connection);
        Entry actualEntry = handler.clients.lastElement();

        assertEquals(expectedName, actualEntry.name);
        assertEquals(1, handler.clients.size());

        UserEvent userEvent = new UserEvent(false, "Client Name"); // Replace "Client Name" with a valid name
        connection.send(userEvent);

        assertEquals(userEvent.getName(), actualEntry.name);
    }
}
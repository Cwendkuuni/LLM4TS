import org.junit.Test;
import static org.junit.Assert.assertEquals;
import dsachat.server.*;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);

        assertEquals(1, handler.clients.size());
        Entry entry = handler.clients.get(0);
        assertEquals(connection, entry.ce);
        assertEquals("", entry.name);

        UserEvent userEvent = new UserEvent(false, "Test User");
        for (Entry client : handler.clients) {
            client.ce.send(userEvent);
        }

        // Check if all clients receive the user event
        for (Entry client : handler.clients) {
            assertEquals("Test User", client.name);
        }
    }
}
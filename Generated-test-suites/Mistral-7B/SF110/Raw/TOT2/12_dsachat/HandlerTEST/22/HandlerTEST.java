import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint(1234);
        String expectedName = "";

        handler.addConnection(connection);

        Entry entry = handler.clients.lastElement();
        assertEquals(connection, entry.ce);
        assertEquals(expectedName, entry.name);

        for (Entry client : handler.clients) {
            UserEvent userEvent = new UserEvent(false, client.name);
            connection.send(userEvent);
        }
    }
}
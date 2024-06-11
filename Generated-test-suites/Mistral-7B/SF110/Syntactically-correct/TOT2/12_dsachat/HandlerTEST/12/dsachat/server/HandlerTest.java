import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        String expectedName = "";

        handler.addConnection(connectionEndpoint);
        Entry actualEntry = handler.clients.lastElement();

        assertEquals(expectedName, actualEntry.name);
        assertEquals(connectionEndpoint, actualEntry.ce);

        // Test notifying all connected clients
        for (int i = 0; i < handler.clients.size(); ++i) {
            Entry client = handler.clients.elementAt(i);
            assertNotNull(client.ce);
        }
    }
}
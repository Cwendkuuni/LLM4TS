import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint ce = new ConnectionEndpoint();

        handler.addConnection(ce);

        assertEquals(1, handler.clients.size());
        assertNotNull(handler.clients.elementAt(0).name);
        assertEquals("", handler.clients.elementAt(0).name);

        // New client should be notified by all other clients
        for (Entry entry : handler.clients) {
            if (entry.ce != ce) {
                UserEvent receivedEvent = (UserEvent) entry.ce.receive();
                assertEquals("", receivedEvent.getName());
            }
        }
    }
}
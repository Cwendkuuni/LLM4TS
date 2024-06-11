import static org.junit.Assert.*;
import dsachat.server.ConnectionEndpoint;
import dsachat.server.Entry;
import dsachat.server.Handler;

public class HandlerTest {
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        Entry expectedEntry = new Entry();
        expectedEntry.ce = connectionEndpoint;
        expectedEntry.name = "";

        handler.addConnection(connectionEndpoint);

        assertEquals(1, handler.clients.size());
        Entry actualEntry = handler.clients.get(0);
        assertEquals(expectedEntry.ce, actualEntry.ce);
        assertEquals(expectedEntry.name, actualEntry.name);
    }
}
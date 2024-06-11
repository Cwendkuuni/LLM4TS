import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;

public class HandlerTest {
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        assertEquals(1, handler.clients.size());
        assertTrue(handler.clients.contains(new Entry(connection, "")));
    }
}
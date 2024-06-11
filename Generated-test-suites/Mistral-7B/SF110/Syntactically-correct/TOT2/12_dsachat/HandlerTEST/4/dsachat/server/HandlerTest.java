import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;

public class HandlerTest {
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint cl = new ConnectionEndpoint(1, "127.0.0.1", 12345);
        handler.addConnection(cl);
        assertEquals(1, handler.clients.size());
        assertTrue(handler.clients.contains(new Entry()));
    }
}
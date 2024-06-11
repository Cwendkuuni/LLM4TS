import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;

public class HandlerTest {
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
    }
}
import static org.junit.Assert.*;
import dsachat.server.*;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        assertTrue(handler.clients.size() == 1);
    }
}
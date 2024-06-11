import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;

public class HandlerTest {

    @Test
    public void testAddConnectionSuccess() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        assertTrue(handler.clients.size() > 0);
    }

    @Test(expected = Exception.class)
    public void testAddConnectionAlreadyConnected() {
        Handler handler = new Handler();
        ConnectionEndpoint connection = new ConnectionEndpoint();
        handler.addConnection(connection);
        handler.addConnection(connection);
    }
}
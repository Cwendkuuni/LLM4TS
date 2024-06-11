import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import java.io.IOException;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint);
        int expectedConnectionsSize = 1;
        assertEquals(expectedConnectionsSize, handler.clients.size());
    }

    @Test(expected = Exception.class)
    public void testAddInvalidConnection() {
        Handler handler = new Handler();
        handler.addConnection(null);
    }

    @Test
    public void testAddAlreadyConnectedClient() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint);
        handler.addConnection(connectionEndpoint);
        int expectedConnectionsSize = 1;
        assertEquals(expectedConnectionsSize, handler.clients.size());
    }

    @Test
    public void testAddConnectionWithException() throws Exception {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint() {
            @Override
            public boolean isConnected() throws IOException {
                throw new IOException();
            }
        };
        handler.addConnection(connectionEndpoint);
        int expectedConnectionsSize = 0;
        assertEquals(expectedConnectionsSize, handler.clients.size());
    }
}
//
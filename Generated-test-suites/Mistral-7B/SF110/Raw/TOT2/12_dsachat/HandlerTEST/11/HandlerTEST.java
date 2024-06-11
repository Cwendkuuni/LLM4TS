import org.junit.Test;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;
import static org.junit.Assert.*;

public class HandlerTest {
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint testConnection = new ConnectionEndpoint();
        handler.addConnection(testConnection);
        int expectedNumberOfConnections = 1;
        int actualNumberOfConnections = handler.clients.size();
        assertEquals(expectedNumberOfConnections, actualNumberOfConnections);
    }
}
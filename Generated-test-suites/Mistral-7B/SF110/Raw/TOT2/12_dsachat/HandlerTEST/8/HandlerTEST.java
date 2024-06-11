import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint);
        Entry entry = handler.clients.lastElement();
        assertNotNull(entry.ce);
    }
}
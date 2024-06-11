import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;

public class HandlerTest {
    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint ce = new ConnectionEndpoint();
        handler.addConnection(ce);
        assertEquals(1, handler.clients.size());

        handler.addConnection(ce); // test exception
        try {
            handler.handleInput(new InputEvent(new StringObject("test"), ce));
            fail("Expected exception");
        } catch (Exception e) {
            // expected exception
        }
    }
}
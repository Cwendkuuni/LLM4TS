import static org.junit.Assert.*;
import dsachat.server.*;
import org.junit.Test;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint ce = new ConnectionEndpoint();
        String expectedName = "";
        handler.addConnection(ce);
        Entry actualEntry = handler.clients.lastElement();
        assertEquals(expectedName, actualEntry.name);
        assertTrue(handler.clients.size() > 0);
    }
}
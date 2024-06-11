import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.ConnectionEndpoint;
import dsachat.share.hero.Hero;

public class HandlerTest {

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        Hero hero = new Hero("TestHero");

        handler.addConnection(connectionEndpoint);
        Entry expectedEntry = new Entry();
        expectedEntry.ce = connectionEndpoint;
        expectedEntry.name = "";

        assertEquals(1, handler.clients.size());
        assertEquals(expectedEntry, handler.clients.elementAt(0));

        handler.sendAllObj(new UserEvent(false, "TestHero"));

        connectionEndpoint.isConnected = true;
        handler.addConnection(connectionEndpoint);

        assertEquals(2, handler.clients.size());
        Entry secondEntry = handler.clients.elementAt(1);
        assertNull(secondEntry.name);

        handler.removeConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        assertNull(handler.clients.elementAt(0).name);
    }
}
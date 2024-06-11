import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint = mock(ConnectionEndpoint.class);
    }

    @Test
    public void testAddConnection() {
        when(connectionEndpoint.isConnected()).thenReturn(true);
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        assertNotNull(handler.clients.elementAt(0).name);

        // Test edge cases
        handler.clients.clear();
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());

        handler.clients.clear();
        when(connectionEndpoint.isConnected()).thenReturn(false);
        handler.addConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    // Add test cases for other methods here
}
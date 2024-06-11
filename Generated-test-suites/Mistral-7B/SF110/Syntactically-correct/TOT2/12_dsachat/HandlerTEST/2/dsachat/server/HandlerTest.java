package dsachat.server;

import org.junit.Test;
import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.share.ConnectionEndpoint;
import java.io.IOException;

public class HandlerTest {

    // Add test methods for all public methods in the Handler class here

    @Test
    public void testAddConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint);
        assertTrue(handler.clients.size() > 0);

        // Testing if the addConnection method can handle multiple connections from the same client
        ConnectionEndpoint sameClient = new ConnectionEndpoint();
        handler.addConnection(sameClient);
        assertTrue(handler.clients.size() > 1);

        // Testing if the addConnection method can handle connections with invalid ports
        ConnectionEndpoint invalidPort = new ConnectionEndpoint(-1);
        try {
            handler.addConnection(invalidPort);
            fail("Expected an exception");
        } catch (Exception e) {
            assertTrue(true);
        }

        // Testing if the addConnection method can handle exceptions when sending messages to clients
        ConnectionEndpoint exceptionThrowingClient = new ConnectionEndpoint() {
            @Override
            public void send(Object o) throws IOException {
                throw new IOException("Simulated IOException");
            }
        };
        try {
            handler.addConnection(exceptionThrowingClient);
            fail("Expected an exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Simulated IOException"));
        }
    }

    @Test
    public void testRemoveConnection() {
        Handler handler = new Handler();
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint();
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertTrue(handler.clients.size() == 0);

        // Testing if the removeConnection method can handle multiple removal attempts for the same connection
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertTrue(handler.clients.size() == 0);

        // Testing if the removeConnection method can handle connections with non-existent ports
        ConnectionEndpoint nonExistentPort = new ConnectionEndpoint(-1);
        handler.removeConnection(nonExistentPort);
        assertTrue(handler.clients.size() > 0);

        // Testing if the removeConnection method can handle exceptions when sending messages to clients
        ConnectionEndpoint exceptionThrowingClient = new ConnectionEndpoint() {
            @Override
            public void send(Object o) throws IOException {
                throw new IOException("Simulated IOException");
            }
        };
        handler.addConnection(exceptionThrowingClient);
        handler.removeConnection(exceptionThrowingClient);
        assertTrue(handler.clients.size() > 0);
    }

    // Add other test methods for other public methods in the Handler class here
}
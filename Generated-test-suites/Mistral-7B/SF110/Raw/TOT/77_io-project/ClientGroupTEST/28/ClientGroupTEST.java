package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.lang.RuntimeException;

public class ClientGroupTest {
    private ClientGroup group;
    private Client client;
    private ClientHandler handler;
    private Object message;

    @Before
    public void setUp() {
        group = new ClientGroup(null);
        client = new Client();
        handler = new ClientHandler() {
            @Override
            public void messageReceived(Client sender, Object message) {
                this.receivedMessage = message;
            }

            private Object receivedMessage;
        };
        group.addClientHandler(handler);
        group.add(client);
        this.message = "Test message";
    }

    @Test
    public void testNotifyMessageSent() {
        group.notifyMessageSent(client, "Test message");
        assertSame(message, handler.receivedMessage);
    }

    @Test
    public void testNotifyMessageReceived() {
        group.notifyMessageReceived(client, message);
        assertSame(message, handler.receivedMessage);
    }

    @Test
    public void testNotifyExceptionCaught() {
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            group.notifyExceptionCaught(client, e);
            assertNotNull(handler.lastException);
            assertEquals("Test exception", handler.lastException.getMessage());
        }
    }

    // Add more test cases here if necessary
}
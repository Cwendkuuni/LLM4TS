package ioproject.server.network.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandlerStub;
import java.lang.RuntimeException;

public class ClientGroupTest {
    private ClientGroup clientGroup;
    private Client client;
    private Object message;
    private ClientHandlerStub clientHandlerStub;

    @Before
    public void setUp() {
        this.client = new Client();
        this.clientGroup = new ClientGroup(null);
        this.message = new Object();
        this.clientHandlerStub = new ClientHandlerStub();
        this.clientGroup.addClientHandler(this.clientHandlerStub);
        this.clientGroup.add(this.client);
    }

    @Test
    public void testNotifyMessageSent() {
        this.clientGroup.notifyMessageSent(this.client, this.message);
        this.clientHandlerStub.assertMessageSent(this.client, this.message);

        // Test edge cases
        this.clientGroup.notifyMessageSent(null, this.message);
        this.clientGroup.notifyMessageSent(this.client, null);
        this.clientGroup.notifyMessageSent(this.client, this.message); // Test multiple calls

        // Test error scenarios
        this.clientGroup.notifyMessageSent(this.client, this.message); // Test multiple calls with the same data
    }

    @Test
    public void testNotifyMessageReceived() {
        this.clientGroup.notifyMessageReceived(this.client, this.message);
        this.clientHandlerStub.assertMessageReceived(this.client, this.message);

        // Test edge cases
        this.clientGroup.notifyMessageReceived(null, this.message);
        this.clientGroup.notifyMessageReceived(this.client, null);
        this.clientGroup.notifyMessageReceived(this.client, this.message); // Test multiple calls

        // Test error scenarios
        this.clientGroup.notifyMessageReceived(this.client, this.message); // Test multiple calls with the same data
    }

    @Test
    public void testNotifyExceptionCaught() {
        this.clientGroup.add(this.client);
        this.clientGroup.addClientHandler(this.clientHandlerStub);

        try {
            this.clientGroup.notifyExceptionCaught(this.client, new RuntimeException("Test Exception"));
            Assert.fail("Exception should have been caught");
        } catch (Exception e) {
            // Expected exception
        }

        this.clientHandlerStub.assertExceptionCaught(this.client, RuntimeException.class);

        // Test edge cases
        this.clientGroup.notifyExceptionCaught(null, new RuntimeException("Test Exception"));
        this.clientGroup.notifyExceptionCaught(this.client, null);
        this.clientGroup.notifyExceptionCaught(this.client, new RuntimeException("Test Exception")); // Test multiple calls

        // Test error scenarios
        this.clientGroup.notifyExceptionCaught(this.client, new RuntimeException("Test Exception")); // Test multiple calls with the same exception
    }

    class ClientHandlerStub {
        private List<Object[]> messageSentCalls = new ArrayList<>();
        private List<Throwable> exceptionCaughtCalls = new ArrayList<>();

        public void messageSent(Client client, Object message) {
            this.messageSentCalls.add(new Object[]{client, message});
        }

        public void assertMessageSent(Client client, Object message) {
            final Object[] call = this.messageSentCalls.get(0);
            if (client != call[0] || message != call[1]) {
                fail("Unexpected client or message");
            }
            this.messageSentCalls.remove(0);
        }

        public void messageReceived(Client client, Object message) {
            this.messageReceivedCalls.add(new Object[]{client, message});
        }

        public void assertMessageReceived(Client client, Object message) {
            final Object[] call = this.messageReceivedCalls.get(0);
            if (client != call[0] || message != call[1]) {
                fail("Unexpected client or message");
            }
            this.messageReceivedCalls.remove(0);
        }

        public void exceptionCaught(Client client, Throwable cause) {
            this.exceptionCaughtCalls.add(cause);
        }

        public void assertExceptionCaught(Client client, Class<? extends Throwable> exceptionType) {
            final Throwable call = this.exceptionCaughtCalls.get(0);
            if (client != this.client || !exceptionType.isInstance(call)) {
                fail("Unexpected client or exception type");
            }
            this.exceptionCaughtCalls.remove(0);
        }
    }
}
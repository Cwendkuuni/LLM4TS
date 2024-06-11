package ioproject.server.network.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientHandler;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientGroupTest {
    private ClientHandler clientHandlerMock;

    @Before
    public void setup() {
        clientHandlerMock = mock(ClientHandler.class);
    }

    @Test
    public void testAddClient() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client testClient = new Client("Test Client");
        clientGroup.add(testClient);
        assertTrue(clientGroup.contains(testClient));
        verify(clientHandlerMock).clientAdded(testClient);
    }

    @Test
    public void testRemoveClient() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client testClient = new Client("Test Client");
        clientGroup.add(testClient);
        clientGroup.remove(testClient);
        assertFalse(clientGroup.contains(testClient));
        verify(clientHandlerMock).clientRemoved(testClient);
    }

    @Test
    public void testMessagePropagation() {
        ClientHandler clientHandlerMock = mock(ClientHandler.class);
        ClientGroup rootClientGroup = new ClientGroup(null);
        Client testClient = new Client("Test Client");
        ClientGroup subGroup = rootClientGroup.createSubGroup();
        rootClientGroup.add(testClient);
        subGroup.add(testClient);
        rootClientGroup.addClientHandler(clientHandlerMock);
        subGroup.addClientHandler(clientHandlerMock);

        Object message = "Test Message";
        rootClientGroup.notifyMessageSent(testClient, message);
        verify(clientHandlerMock).messageSent(testClient, message);
        verify(subGroup).notifyMessageSent(testClient, message);

        rootClientGroup.notifyMessageReceived(testClient, message);
        verify(clientHandlerMock).messageReceived(testClient, message);
        verify(subGroup).notifyMessageReceived(testClient, message);

        Throwable cause = new RuntimeException("Test Exception");
        rootClientGroup.notifyExceptionCaught(testClient, cause);
        verify(clientHandlerMock).exceptionCaught(testClient, cause);
        verify(subGroup).notifyExceptionCaught(testClient, cause);
    }
}
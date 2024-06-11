package ioproject.server.network;

import org.junit.Before;
import org.junit.Test;
import ioproject.common.network.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;
import java.util.*;

public class ClientGroupTest {
    private ClientGroup parentGroup;
    private ClientGroup childGroup;
    private List<Client> clients = new ArrayList<>();
    private ClientHandler parentHandler = mock(ClientHandler.class);
    private ClientHandler childHandler = mock(ClientHandler.class);

    @Before
    public void setUp() {
        clients.add(new Client("Client1"));
        clients.add(new Client("Client2"));
        parentGroup = new ClientGroup(null);
        childGroup = parentGroup.createSubGroup();
        parentGroup.add(childGroup);
        parentGroup.addClientHandler(parentHandler);
        childGroup.addClientHandler(childHandler);
        parentGroup.add(clients.get(0));
        childGroup.add(clients.get(0));
        childGroup.add(clients.get(1));
        parentGroup.add(clients.get(1));
    }

    @Test
    public void testAddClient() {
        Client client = clients.get(0);

        parentGroup.add(client);
        assertTrue(parentGroup.contains(client));
        assertTrue(childGroup.contains(client));
        childGroup.add(client);
        assertTrue(parentGroup.contains(client));
        assertTrue(childGroup.contains(client));

        parentGroup.addClientHandler(parentHandler);
        childGroup.addClientHandler(childHandler);

        parentGroup.notifyMessageSent(client, "Test Message");
        verify(parentHandler).messageSent(client, "Test Message");
        verify(childHandler).messageSent(client, "Test Message");

        parentGroup.notifyMessageReceived(client, "Test Message");
        verify(parentHandler).messageReceived(client, "Test Message");
        verify(childHandler).messageReceived(client, "Test Message");

        parentGroup.notifyExceptionCaught(client, new RuntimeException("Test Exception"));
        verify(parentHandler).exceptionCaught(client, new RuntimeException("Test Exception"));
        verify(childHandler).exceptionCaught(client, new RuntimeException("Test Exception"));
    }

    @Test
    public void testAddSubGroup() {
        ClientGroup subGroup = parentGroup.createSubGroup();
        parentGroup.add(subGroup);
        assertTrue(parentGroup.contains(subGroup));

        parentGroup.addClientHandler(parentHandler);
        subGroup.addClientHandler(childHandler);

        parentGroup.notifySubgroupAdded(subGroup);
        verify(parentHandler).subgroupAdded(subGroup);
    }

    @Test
    public void testNotifyMessageReceived() {
        Client client = clients.get(0);
        parentGroup.add(client);
        childGroup.add(client);
        parentGroup.addClientHandler(parentHandler);
        childGroup.addClientHandler(childHandler);

        parentGroup.notifyMessageReceived(client, "Test Message");
        verify(parentHandler).messageReceived(client, "Test Message");
        verify(childHandler).messageReceived(client, "Test Message");

        childGroup.notifyMessageReceived(client, "Test Message");
        verify(parentHandler).messageReceived(client, "Test Message");
        verify(childHandler).messageReceived(client, "Test Message");
    }

    @Test
    public void testNotifyExceptionCaught() {
        Client client = clients.get(0);
        parentGroup.add(client);
        childGroup.add(client);
        parentGroup.addClientHandler(parentHandler);
        childGroup.addClientHandler(childHandler);

        parentGroup.notifyExceptionCaught(client, new RuntimeException("Test Exception"));
        verify(parentHandler).exceptionCaught(client, new RuntimeException("Test Exception"));
        verify(childHandler).exceptionCaught(client, new RuntimeException("Test Exception"));

        childGroup.notifyExceptionCaught(client, new RuntimeException("Test Exception"));
        verify(parentHandler).exceptionCaught(client, new RuntimeException("Test Exception"));
        verify(childHandler).exceptionCaught(client, new RuntimeException("Test Exception"));
    }

    @Test
    public void testRemoveClient() {
        Client client = clients.get(0);

        parentGroup.remove(client);
        assertFalse(parentGroup.contains(client));
        assertFalse(childGroup.contains(client));

        parentGroup.addClientHandler(parentHandler);
        childGroup.addClientHandler(childHandler);

        parentGroup.notifyMessageSent(client, "Test Message");
        verifyNoMoreInteractions(parentHandler);
        verifyNoMoreInteractions(childHandler);

        parentGroup.notifyMessageReceived(client, "Test Message");
        verifyNoMoreInteractions(parentHandler);
        verifyNoMoreInteractions(childHandler);

        parentGroup.notifyExceptionCaught(client, new RuntimeException("Test Exception"));
        verifyNoMoreInteractions(parentHandler);
        verifyNoMoreInteractions(childHandler);

        parentGroup.add(client);
        parentGroup.remove(client);
        assertFalse(parentGroup.contains(client));
    }
}
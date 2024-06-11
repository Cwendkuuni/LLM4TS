package ioproject.server.network.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroup.ClientHandlerFactory;

public class ClientGroupTest
{
    private ClientGroup rootGroup;
    private ClientHandler rootHandler;
    private ClientGroup subGroup;
    private ClientHandler subGroupHandler;
    private Client client;
    private Client client2;
    private Client client3;
    private ClientHandler spyHandler;

    @Before
    public void setUp()
    {
        rootGroup = new ClientGroup(null);
        rootHandler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message)
            {
                spyHandler.messageSent(sender, message);
            }

            @Override
            public void messageReceived(Client sender, Object message)
            {
                spyHandler.messageReceived(sender, message);
            }

            @Override
            public void exceptionCaught(Client sender, Throwable cause)
            {
                spyHandler.exceptionCaught(sender, cause);
            }

            @Override
            public void clientAdded(Client client)
            {
                // No action required
            }

            @Override
            public void clientRemoved(Client client)
            {
                // No action required
            }
        };
        rootGroup.addClientHandler(rootHandler);
        spyHandler = new ClientHandler() {};

        client = new Client();
        client2 = new Client();
        client3 = new Client();

        subGroup = rootGroup.createSubGroup();
        subGroupHandler = new ClientHandler() {};
        subGroup.addClientHandler(subGroupHandler);
    }

    @Test
    public void testContains()
    {
        rootGroup.add(client);
        assertTrue(rootGroup.contains(client));
        assertFalse(rootGroup.contains(client2));
    }

    @Test
    public void testSize()
    {
        rootGroup.add(client);
        assertEquals(1, rootGroup.size());
        rootGroup.add(client2);
        assertEquals(2, rootGroup.size());
    }

    @Test
    public void testIterator()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        Set<Client> clients = new HashSet<Client>(Arrays.asList(client, client2));
        Iterator<Client> iterator = rootGroup.iterator();
        for (Client client : clients) {
            assertTrue(iterator.hasNext());
            Client next = iterator.next();
            assertEquals(client, next);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddClient()
    {
        assertFalse(rootGroup.add(client));
        assertTrue(rootGroup.add(client2));
        assertTrue(rootGroup.contains(client2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddClientTwice()
    {
        rootGroup.add(client);
        rootGroup.add(client);
    }

    @Test
    public void testRemoveClient()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        assertTrue(rootGroup.remove(client2));
        assertFalse(rootGroup.contains(client2));
        assertFalse(rootGroup.remove(client2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentClient()
    {
        rootGroup.remove(client2);
    }

    @Test
    public void testNotifyMessageSent()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        rootGroup.add(subGroup);
        subGroup.add(client);

        assertEquals(3, spyHandler.messageSentCallCount); // Initial call from root group
        rootGroup.notifyMessageSent(client, "test");

        assertEquals(4, spyHandler.messageSentCallCount); // Call from subgroup
    }

    @Test
    public void testNotifyMessageReceived()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        rootGroup.add(subGroup);
        subGroup.add(client);

        assertEquals(3, spyHandler.messageReceivedCallCount); // Initial call from root group
        rootGroup.notifyMessageReceived(client, "test");

        assertEquals(4, spyHandler.messageReceivedCallCount); // Call from subgroup
    }

    @Test
    public void testNotifyExceptionCaught()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        rootGroup.add(subGroup);
        subGroup.add(client);

        Throwable exception = new RuntimeException("Test exception");
        rootGroup.notifyExceptionCaught(client, exception);
        assertNotNull(spyHandler.exceptionCaughtException);
        assertEquals(exception.getMessage(), spyHandler.exceptionCaughtException.getMessage());
    }

    @Test
    public void testSubGroupNotifyMessageSent()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        rootGroup.add(subGroup);
        subGroup.add(client);

        ClientHandler subGroupSpyHandler = new ClientHandler() {};
        subGroup.addClientHandler(subGroupSpyHandler);

        assertEquals(1, spyHandler.messageSentCallCount); // Initial call from root group
        rootGroup.notifyMessageSent(client, "test");

        assertEquals(1, subGroupSpyHandler.messageSentCallCount); // Call from subgroup
    }

    @Test
    public void testSubGroupNotifyMessageReceived()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        rootGroup.add(subGroup);
        subGroup.add(client);

        ClientHandler subGroupSpyHandler = new ClientHandler() {};
        subGroup.addClientHandler(subGroupSpyHandler);

        assertEquals(1, spyHandler.messageReceivedCallCount); // Initial call from root group
        rootGroup.notifyMessageReceived(client, "test");

        assertEquals(1, subGroupSpyHandler.messageReceivedCallCount); // Call from subgroup
    }

    @Test
    public void testSubGroupNotifyExceptionCaught()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        rootGroup.add(subGroup);
        subGroup.add(client);

        ClientHandler subGroupSpyHandler = new ClientHandler() {};
        subGroup.addClientHandler(subGroupSpyHandler);

        Throwable exception = new RuntimeException("Test exception");
        rootGroup.notifyExceptionCaught(client, exception);

        assertNotNull(subGroupSpyHandler.exceptionCaughtException);
        assertEquals(exception.getMessage(), subGroupSpyHandler.exceptionCaughtException.getMessage());
    }

    @Test
    public void testParentGroupNotifyMessageSent()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        subGroup.add(client);

        ClientHandler rootGroupSpyHandler = new ClientHandler() {};
        rootGroup.addClientHandler(rootGroupSpyHandler);

        assertEquals(1, spyHandler.messageSentCallCount); // Initial call from subgroup
        subGroup.notifyMessageSent(client, "test");

        assertEquals(2, rootGroupSpyHandler.messageSentCallCount); // Call from parent group and from subgroup
    }

    @Test
    public void testParentGroupNotifyMessageReceived()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        subGroup.add(client);

        ClientHandler rootGroupSpyHandler = new ClientHandler() {};
        rootGroup.addClientHandler(rootGroupSpyHandler);

        assertEquals(1, spyHandler.messageReceivedCallCount); // Initial call from subgroup
        subGroup.notifyMessageReceived(client, "test");

        assertEquals(2, rootGroupSpyHandler.messageReceivedCallCount); // Call from parent group and from subgroup
    }

    @Test
    public void testParentGroupNotifyExceptionCaught()
    {
        rootGroup.add(client);
        rootGroup.add(client2);
        rootGroup.add(client3);
        subGroup.add(client);

        ClientHandler rootGroupSpyHandler = new ClientHandler() {};
        rootGroup.addClientHandler(rootGroupSpyHandler);

        Throwable exception = new RuntimeException("Test exception");
        subGroup.notifyExceptionCaught(client, exception);

        assertNotNull(rootGroupSpyHandler.exceptionCaughtException);
        assertEquals(exception.getMessage(), rootGroupSpyHandler.exceptionCaughtException.getMessage());
    }
}

package ioproject.server.network.test;

import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {
    @Test
    public void testAddClient() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertTrue(group.add(client));
    }

    @Test
    public void testIterator() {
        ClientGroup group = new ClientGroup(null);
        group.add(new Client());
        group.add(new Client());
        Iterator<Client> iterator = group.iterator();

        assertTrue(iterator.hasNext());
        Client client1 = iterator.next();

        assertTrue(iterator.hasNext());
        Client client2 = iterator.next();

        assertTrue(iterator.hasNext() == false);
    }

    @Test
    public void testSize() {
        ClientGroup group = new ClientGroup(null);
        group.add(new Client());
        group.add(new Client());
        assertEquals(group.size(), 2);
    }

    @Test
    public void testNotifyMessageSentFalse() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyMessageSent(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageSentTrue() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(group.notifyMessageSent(client, "Test Message"));
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyMessageSentNullMessage() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.notifyMessageSent(client, null);
    }

    @Test
    public void testNotifyMessageReceivedFalse() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyMessageReceived(client, "Test Message"));
    }

    @Test
    public void testNotifyMessageReceivedTrue() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(group.notifyMessageReceived(client, "Test Message"));
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyMessageReceivedNullMessage() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.notifyMessageReceived(client, null);
    }

    @Test
    public void testNotifyExceptionCaughtFalse() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.notifyExceptionCaught(client, new Exception("Test Exception")));
    }

    @Test
    public void testNotifyExceptionCaughtTrue() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = new ClientGroup(parentGroup);
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(group.notifyExceptionCaught(client, new Exception("Test Exception")));
    }

    @Test(expected = NullPointerException.class)
    public void testNotifyExceptionCaughtNullException() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.notifyExceptionCaught(client, null);
    }

    @Test
    public void testContainsTrue() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        assertTrue(group.contains(client));
    }

    @Test
    public void testContainsFalse() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        assertFalse(group.contains(client));
    }

    @Test
    public void testContainsSameClientMultipleTimes() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        group.add(client);
        assertTrue(group.contains(client));
    }

    @Test
    public void testSizeZero() {
        ClientGroup group = new ClientGroup(null);
        assertEquals(group.size(), 0);
    }

    @Test
    public void testSizeAfterAddingClient() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        assertEquals(group.size(), 1);
    }

    @Test
    public void testSizeAfterRemovingClient() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        group.remove(client);
        assertEquals(group.size(), 0);
    }

    @Test
    public void testSizeAfterAddingSameClientMultipleTimes() {
        ClientGroup group = new ClientGroup(null);
        Client client = new Client();
        group.add(client);
        group.add(client);
        assertEquals(group.size(), 1);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup group = parentGroup.createSubGroup();
        assertNotNull(group);
    }

    @Test(expected = NullPointerException.class)
    public void testAddClientHandlerNullHandler() {
        ClientGroup group = new ClientGroup(null);
        group.addClientHandler(null);
    }
}
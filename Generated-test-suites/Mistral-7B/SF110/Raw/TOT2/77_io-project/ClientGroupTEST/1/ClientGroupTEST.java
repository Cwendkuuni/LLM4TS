import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import java.util.WeakHashMap;

public class ClientGroupTest {
    @Test
    public void testEmptyConstructor() {
        ClientGroup clientGroup = new ClientGroup();
        assertTrue(clientGroup.size() == 0);
    }

    @Test
    public void testAddSameClientMultipleTimes() {
        ClientGroup clientGroup = new ClientGroup();
        Client client = new Client();
        assertTrue(clientGroup.add(client));
        assertFalse(clientGroup.add(client));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullClient() {
        ClientGroup clientGroup = new ClientGroup();
        assertFalse(clientGroup.add(null));
    }

    @Test
    public void testAddClientToSubgroup() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(client);
        assertFalse(subGroup.add(client));
    }

    @Test
    public void testAddClientWhenHandlerIsNull() {
        ClientGroup clientGroup = new ClientGroup();
        Client client = new Client();
        clientGroup.add(client);
    }

    @Test
    public void testBasicAddMethod() {
        ClientGroup clientGroup = new ClientGroup();
        Client client = new Client();
        assertTrue(clientGroup.add(client));
    }

    @Test
    public void testIterator() {
        ClientGroup clientGroup = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup();
        Client client = new Client();
        clientGroup.add(client);
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testSize() {
        ClientGroup clientGroup = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testRemove() {
        ClientGroup clientGroup = new ClientGroup();
        Client client = new Client();
        assertTrue(clientGroup.add(client));
        assertTrue(clientGroup.remove(client));
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup subGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(client);
        assertFalse(subGroup.contains(client));
        subGroup.add(client);
        assertTrue(subGroup.contains(client));
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup();
        ClientHandler handler = new ClientHandler() {};
        clientGroup.addClientHandler(handler);
    }
}
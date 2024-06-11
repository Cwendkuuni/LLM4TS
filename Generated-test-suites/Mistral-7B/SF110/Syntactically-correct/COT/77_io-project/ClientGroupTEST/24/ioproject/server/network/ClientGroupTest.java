import org.junit.Test;
import org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakReference;

public class ClientGroupTest {

    @Test
    public void testConstructor() {
        ClientGroup clientGroup = new ClientGroup(null);
        assertNull(clientGroup.parentGroup());
        assertTrue(clientGroup.clients.isEmpty());
        assertNull(clientGroup.handler);
    }

    @Test
    public void testNotifyMessageSent_ClientInGroup_ReturnsTrue() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.notifyMessageSent(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageSent_ClientNotInGroup_ReturnsFalse() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();

        assertFalse(clientGroup.notifyMessageSent(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageSent_ClientInSubGroup_ReturnsTrueIfSubGroupHandlesMessage() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(childGroup);
        childGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                return true;
            }
        };
        childGroup.addClientHandler(handler);

        assertTrue(parentGroup.notifyMessageSent(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageSent_ClientInSubGroup_ReturnsFalseIfSubGroupDoesNotHandleMessage() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(childGroup);
        childGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                return false;
            }
        };
        childGroup.addClientHandler(handler);

        assertFalse(parentGroup.notifyMessageSent(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageSent_ClientHasHandler_HandlerIsNotified() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client sender, Object message) {
                assertNotNull(sender);
                assertEquals("testMessage", message);
            }
        };
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        clientGroup.notifyMessageSent(client, "testMessage");
    }

    @Test
    public void testNotifyMessageReceived_ClientInGroup_ReturnsTrue() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client sender, Object message) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.notifyMessageReceived(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageReceived_ClientNotInGroup_ReturnsFalse() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();

        assertFalse(clientGroup.notifyMessageReceived(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageReceived_ClientInSubGroup_ReturnsTrueIfSubGroupHandlesMessage() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(childGroup);
        childGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client sender, Object message) {
                return true;
            }
        };
        childGroup.addClientHandler(handler);

        assertTrue(parentGroup.notifyMessageReceived(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageReceived_ClientInSubGroup_ReturnsFalseIfSubGroupDoesNotHandleMessage() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(childGroup);
        childGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client sender, Object message) {
                return false;
            }
        };
        childGroup.addClientHandler(handler);

        assertFalse(parentGroup.notifyMessageReceived(client, "testMessage"));
    }

    @Test
    public void testNotifyMessageReceived_ClientHasHandler_HandlerIsNotified() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client sender, Object message) {
                assertNotNull(sender);
                assertEquals("testMessage", message);
            }
        };
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        clientGroup.notifyMessageReceived(client, "testMessage");
    }

    @Test
    public void testNotifyExceptionCaught_ClientInGroup_ReturnsTrue() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        clientGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client sender, Throwable cause) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.notifyExceptionCaught(client, new RuntimeException("testException")));
    }

    @Test
    public void testNotifyExceptionCaught_ClientNotInGroup_ReturnsFalse() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Throwable cause = new RuntimeException("testException");

        assertFalse(clientGroup.notifyExceptionCaught(client, cause));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInSubGroup_ReturnsTrueIfSubGroupHandlesException() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(childGroup);
        childGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client sender, Throwable cause) {
                return true;
            }
        };
        childGroup.addClientHandler(handler);

        assertTrue(parentGroup.notifyExceptionCaught(client, new RuntimeException("testException")));
    }

    @Test
    public void testNotifyExceptionCaught_ClientInSubGroup_ReturnsFalseIfSubGroupDoesNotHandleException() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        Client client = new Client();
        parentGroup.add(childGroup);
        childGroup.add(client);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client sender, Throwable cause) {
                return false;
            }
        };
        childGroup.addClientHandler(handler);

        assertFalse(parentGroup.notifyExceptionCaught(client, new RuntimeException("testException")));
    }

    @Test
    public void testNotifyExceptionCaught_ClientHasHandler_HandlerIsNotified() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client sender, Throwable cause) {
                assertNotNull(sender);
                assertEquals(cause.getClass(), new RuntimeException("testException").getClass());
            }
        };
        clientGroup.add(client);
        clientGroup.addClientHandler(handler);

        clientGroup.notifyExceptionCaught(client, new RuntimeException("testException"));
    }

    @Test
    public void testIterator() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);
        clientGroup.add(client3);

        Iterator<Client> iterator = clientGroup.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(client1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(client3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContains() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);

        assertTrue(clientGroup.contains(client1));
        assertTrue(clientGroup.contains(client2));
    }

    @Test
    public void testSize() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        clientGroup.add(client1);
        clientGroup.add(client2);

        assertEquals(2, clientGroup.size());
    }

    @Test
    public void testAdd() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientAdded(Client sender) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.add(client1));
        assertTrue(clientGroup.add(client2));
        assertTrue(clientGroup.size() == 2);
        assertTrue(clientGroup.contains(client1));
        assertTrue(clientGroup.contains(client2));
    }

    @Test
    public void testAdd_SameClient_ReturnsFalse() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientAdded(Client sender) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.add(client));
        assertFalse(clientGroup.add(client));
        assertEquals(1, clientGroup.size());
        assertTrue(clientGroup.contains(client));
    }

    @Test
    public void testRemove() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientRemoved(Client sender) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertTrue(clientGroup.add(client1));
        assertTrue(clientGroup.add(client2));
        assertTrue(clientGroup.size() == 2);
        assertTrue(clientGroup.contains(client1));
        assertTrue(clientGroup.contains(client2));

        assertTrue(clientGroup.remove(client1));
        assertFalse(clientGroup.remove(client1));
        assertFalse(clientGroup.contains(client1));
        assertEquals(1, clientGroup.size());
        assertTrue(clientGroup.contains(client2));
    }

    @Test
    public void testRemove_NonExistentClient_ReturnsFalse() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientRemoved(Client sender) {
            }
        };
        clientGroup.addClientHandler(handler);

        assertFalse(clientGroup.remove(client));
        assertEquals(0, clientGroup.size());
        assertFalse(clientGroup.contains(client));
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientGroup subGroup = clientGroup.createSubGroup();

        assertNotNull(subGroup);
        assertTrue(clientGroup.subGroups.contains(new WeakReference<>(subGroup)));
        assertEquals(1, clientGroup.subGroups.size());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup clientGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void clientAdded(Client sender) {
            }
        };

        clientGroup.addClientHandler(handler);
        ClientHandler handler2 = new ClientHandler() {
            @Override
            public void clientAdded(Client sender) {
            }
        };

        clientGroup.addClientHandler(handler2);
        assertEquals(2, clientGroup.handlers.size());
    }
}
package ioproject.server.network;

import static org.junit.Assert.*;
import ioproject.common.network.Client;
import ioproject.server.network.ClientGroup;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.*;

public class ClientGroupTest {

    @Test
    public void testCreateClientGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertNull(parentGroup.parentGroup());
        assertNotNull(parentGroup.subGroups);
        assertNotNull(parentGroup.clients);
        assertNull(parentGroup.handler);
    }

    @Test
    public void testAddClientToEmptyGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        assertTrue(parentGroup.add(client));
        assertTrue(parentGroup.contains(client));
        assertEquals(1, parentGroup.size());
    }

    @Test
    public void testAddClientToGroupWithParent() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        assertTrue(parentGroup.add(client));
        assertFalse(childGroup.add(client));
        assertTrue(parentGroup.contains(client));
        assertTrue(childGroup.contains(client));
        assertEquals(2, parentGroup.size());
        assertEquals(1, childGroup.size());
    }

    @Test
    public void testAddClientToGroupWithExistingClient() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client1 = new Client();
        Client client2 = new Client();
        parentGroup.add(client1);
        assertFalse(parentGroup.add(client2));
        assertTrue(parentGroup.contains(client1));
        assertTrue(parentGroup.contains(client2));
        assertEquals(2, parentGroup.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullClientToGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        parentGroup.add(null);
    }

    @Test
    public void testRemoveClientFromEmptyGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertFalse(parentGroup.remove(new Client()));
        assertEquals(0, parentGroup.size());
    }

    @Test
    public void testRemoveClientFromGroupWithClient() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        parentGroup.add(client);
        assertTrue(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        assertEquals(0, parentGroup.size());
    }

    @Test
    public void testRemoveClientFromGroupWithNonExistingClient() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        assertFalse(parentGroup.remove(client));
        assertFalse(parentGroup.contains(client));
        assertEquals(0, parentGroup.size());
    }

    @Test
    public void testAddClientHandler() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {}

            @Override
            public void messageSent(Client client, Object message) {}

            @Override
            public void clientAdded(Client client) {}

            @Override
            public void clientRemoved(Client client) {}

            @Override
            public void exceptionCaught(Client client, Throwable cause) {}
        };
        parentGroup.addClientHandler(handler);
        assertNotNull(parentGroup.handler);
    }

    @Test
    public void testCreateSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup childGroup = parentGroup.createSubGroup();
        assertNotNull(childGroup);
        assertSame(parentGroup, childGroup.parentGroup());
        assertNotNull(childGroup.subGroups);
        assertNotNull(childGroup.clients);
        assertNull(childGroup.handler);
    }

    @Test
    public void testAddClientToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        assertFalse(childGroup.add(client));
        assertTrue(parentGroup.contains(client));
        assertTrue(childGroup.contains(client));
        assertEquals(2, parentGroup.size());
        assertEquals(1, childGroup.size());
    }

    @Test
    public void testAddClientToSubGroupWithParentContainClient() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        assertTrue(childGroup.add(client));
        assertTrue(parentGroup.contains(client));
        assertTrue(childGroup.contains(client));
        assertEquals(3, parentGroup.size());
        assertEquals(2, childGroup.size());
    }

    @Test
    public void testAddClientToSubGroupWithSubGroupContainClient() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup1 = parentGroup.createSubGroup();
        ClientGroup childGroup2 = childGroup1.createSubGroup();
        parentGroup.add(childGroup1);
        childGroup1.add(client);
        assertFalse(childGroup2.add(client));
        assertTrue(parentGroup.contains(childGroup1));
        assertTrue(childGroup1.contains(client));
        assertTrue(childGroup2.contains(client));
        assertEquals(3, parentGroup.size());
        assertEquals(3, childGroup1.size());
        assertEquals(1, childGroup2.size());
    }

    @Test
    public void testNotifyMessageSent() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertEquals(client, this.client);
                assertEquals("message", message);
            }

            private Client client;
        };
        parentGroup.addClientHandler(handler);
        handler.client = client;
        parentGroup.notifyMessageSent(client, "message");
        assertTrue(handler.client.equals(client));
        assertTrue(handler.message.equals("message"));
    }

    @Test
    public void testNotifyMessageReceived() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                assertEquals(client, this.client);
                assertEquals("message", message);
            }

            private Client client;
        };
        parentGroup.addClientHandler(handler);
        handler.client = client;
        parentGroup.notifyMessageReceived(client, "message");
        assertTrue(handler.client.equals(client));
        assertTrue(handler.message.equals("message"));
    }

    @Test
    public void testNotifyExceptionCaught() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                assertEquals(client, this.client);
                assertEquals(cause.getClass(), this.cause.getClass());
            }

            private Client client;
            private Throwable cause;
        };
        parentGroup.addClientHandler(handler);
        handler.client = client;
        try {
            throw new RuntimeException("Test Exception");
        } catch (RuntimeException e) {
            handler.cause = e;
            parentGroup.notifyExceptionCaught(client, e);
        }
        assertTrue(handler.client.equals(client));
        assertTrue(handler.cause.getClass().equals(RuntimeException.class));
    }

    @Test
    public void testNotifyMessageSentToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertEquals(client, this.client);
                assertEquals("message", message);
            }

            private Client client;
        };
        parentGroup.addClientHandler(handler);
        handler.client = client;
        childGroup.addClientHandler(new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertFalse(client.equals(handler.client));
                assertNotNull(message);
                assertNotNull(parentGroup);
                assertTrue(parentGroup.contains(client));
                assertTrue(parentGroup.notifyMessageSent(client, message));
            }
        });
        parentGroup.notifyMessageSent(client, "message");
    }

    @Test
    public void testNotifyMessageReceivedToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                assertEquals(client, this.client);
                assertEquals("message", message);
            }

            private Client client;
        };
        parentGroup.addClientHandler(handler);
        handler.client = client;
        childGroup.addClientHandler(new ClientHandler() {
            @Override
            public void messageReceived(Client client, Object message) {
                assertFalse(client.equals(handler.client));
                assertNotNull(message);
                assertNotNull(parentGroup);
                assertTrue(parentGroup.contains(client));
                assertTrue(parentGroup.notifyMessageReceived(client, message));
            }
        });
        parentGroup.notifyMessageReceived(client, "message");
    }

    @Test
    public void testNotifyExceptionCaughtToSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup = parentGroup.createSubGroup();
        parentGroup.add(client);
        parentGroup.add(childGroup);
        ClientHandler handler = new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                assertEquals(client, this.client);
                assertEquals(cause.getClass(), this.cause.getClass());
            }

            private Client client;
            private Throwable cause;
        };
        parentGroup.addClientHandler(handler);
        handler.client = client;
        childGroup.addClientHandler(new ClientHandler() {
            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                assertFalse(client.equals(handler.client));
                assertNotNull(cause);
                assertNotNull(parentGroup);
                assertTrue(parentGroup.contains(client));
                assertTrue(parentGroup.notifyExceptionCaught(client, cause));
            }
        });
        try {
            throw new RuntimeException("Test Exception");
        } catch (RuntimeException e) {
            handler.cause = e;
            parentGroup.notifyExceptionCaught(client, e);
        }
    }

    @Test
    public void testRemoveClientFromSubGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup1 = parentGroup.createSubGroup();
        ClientGroup childGroup2 = childGroup1.createSubGroup();
        parentGroup.add(childGroup1);
        childGroup1.add(client);
        childGroup1.add(childGroup2);
        assertFalse(childGroup2.remove(client));
        assertTrue(parentGroup.contains(childGroup1));
        assertTrue(childGroup1.contains(client));
        assertTrue(childGroup2.contains(client));
        assertEquals(4, parentGroup.size());
        assertEquals(3, childGroup1.size());
        assertEquals(1, childGroup2.size());
    }

    @Test
    public void testRemoveClientFromSubGroupWithClientNotExist() {
        ClientGroup parentGroup = new ClientGroup(null);
        Client client = new Client();
        ClientGroup childGroup1 = parentGroup.createSubGroup();
        parentGroup.add(childGroup1);
        assertFalse(childGroup1.remove(client));
        assertFalse(parentGroup.contains(childGroup1));
        assertFalse(childGroup1.contains(client));
        assertEquals(1, parentGroup.size());
    }
}
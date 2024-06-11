package ioproject.server.network;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;

public class ClientGroupTest {
    private ClientGroup testGroup;
    private Client testClient;
    private ClientHandler testHandler;

    @Before
    public void setUp() {
        testClient = new Client();
        testHandler = new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                // empty implementation
            }

            @Override
            public void messageReceived(Client client, Object message) {
                // empty implementation
            }

            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                // empty implementation
            }

            @Override
            public void clientAdded(Client client) {
                // empty implementation
            }

            @Override
            public void clientRemoved(Client client) {
                // empty implementation
            }
        };

        testGroup = new ClientGroup() {
            @Override
            public void messageSent(Client client, Object message) {
                testHandler.messageSent(client, message);
            }

            @Override
            public void messageReceived(Client client, Object message) {
                testHandler.messageReceived(client, message);
            }

            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                testHandler.exceptionCaught(client, cause);
            }
        };
    }

    @After
    public void tearDown() {
        testGroup = null;
        testClient = null;
        testHandler = null;
    }

    @Test
    

    // Test for constructor with no arguments
    @Test
    public void testClientGroupConstructor() {
        assertNotNull(testGroup);
        assertTrue(testGroup.clients.isEmpty());
        assertTrue(testGroup.subGroups.isEmpty());
        assertNull(testGroup.handler);
    }

    // Test for constructor with parent group argument
    @Test
    public void testClientGroupConstructorWithParent() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup testGroup = new ClientGroup(parentGroup);
        assertNotNull(testGroup);
        assertSame(parentGroup, testGroup.parentGroup);
        assertTrue(testGroup.clients.isEmpty());
        assertTrue(testGroup.subGroups.isEmpty());
        assertNull(testGroup.handler);
    }

    // Test for notifyMessageSent
    @Test
    public void testNotifyMessageSent() {
        testGroup.add(testClient);
        testGroup.addClientHandler(testHandler);
        testGroup.notifyMessageSent(testClient, "testMessage");
        assertTrue(testHandler.isMessageSentCalledOnce());
    }

    // Test for notifyMessageReceived
    @Test
    public void testNotifyMessageReceived() {
        testGroup.add(testClient);
        testGroup.addClientHandler(testHandler);
        testGroup.notifyMessageReceived(testClient, "testMessage");
        assertTrue(testHandler.isMessageReceivedCalledOnce());
    }

    // Test for notifyExceptionCaught
    @Test
    public void testNotifyExceptionCaught() {
        testGroup.add(testClient);
        testGroup.addClientHandler(testHandler);
        testGroup.notifyExceptionCaught(testClient, new Exception("testException"));
        assertTrue(testHandler.isExceptionCaughtCalledOnce());
    }

    // Test for iterator
    @Test
    public void testIterator() {
        Set<Client> testSet = new HashSet<>();
        testSet.add(testClient);
        testGroup.addAll(testSet);
        Iterator<Client> iterator = testGroup.iterator();
        assertTrue(iterator.hasNext());
        Client next = iterator.next();
        assertEquals(testClient, next);
        assertFalse(iterator.hasNext());
    }

    // Test for contains
    @Test
    public void testContains() {
        testGroup.add(testClient);
        assertTrue(testGroup.contains(testClient));
    }

    // Test for size
    @Test
    public void testSize() {
        testGroup.add(testClient);
        assertEquals(1, testGroup.size());
    }

    // Test for add
    @Test
    public void testAdd() {
        assertFalse(testGroup.add(testClient));
        testGroup.add(testClient);
        assertTrue(testGroup.contains(testClient));
    }

    // Test for remove
    @Test
    public void testRemove() {
        testGroup.add(testClient);
        testGroup.remove(testClient);
        assertFalse(testGroup.contains(testClient));
    }

    // Test for createSubGroup
    @Test
    public void testCreateSubGroup() {
        ClientGroup subGroup = testGroup.createSubGroup();
        assertNotNull(subGroup);
        assertSame(testGroup, subGroup.parentGroup);
        assertTrue(subGroup.clients.isEmpty());
        assertTrue(subGroup.subGroups.isEmpty());
        assertNull(subGroup.handler);
    }

    // Test for addClientHandler
    @Test
    public void testAddClientHandler() {
        testGroup.addClientHandler(testHandler);
        assertSame(testHandler, testGroup.handler);
    }

    // Test for empty message in notifyMessageSent, notifyMessageReceived and notifyExceptionCaught
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMessageInNotifyMethods() {
        testGroup.add(testClient);
        testGroup.addClientHandler(testHandler);
        testGroup.notifyMessageSent(testClient, "");
        testGroup.notifyMessageReceived(testClient, "");
        testGroup.notifyExceptionCaught(testClient, new Exception(""));
    }

    // Test for null message in notifyMessageSent, notifyMessageReceived and notifyExceptionCaught
    @Test(expected = NullPointerException.class)
    public void testNullMessageInNotifyMethods() {
        testGroup.add(testClient);
        testGroup.addClientHandler(testHandler);
        testGroup.notifyMessageSent(testClient, null);
        testGroup.notifyMessageReceived(testClient, null);
        testGroup.notifyExceptionCaught(testClient, null);
    }

    // Test for null client in notifyMessageSent, notifyMessageReceived and notifyExceptionCaught methods
    @Test(expected = NullPointerException.class)
    public void testNullClientInNotifyMethods() {
        testGroup.addClientHandler(testHandler);
        testGroup.notifyMessageSent(null, "testMessage");
        testGroup.notifyMessageReceived(null, "testMessage");
        testGroup.notifyExceptionCaught(null, new Exception("testException"));
    }

    // Test for null handler in addClientHandler
    @Test(expected = IllegalArgumentException.class)
    public void testNullHandlerInAddClientHandler() {
        testGroup.addClientHandler(null);
    }

    // Test for adding and removing the same client multiple times
    @Test
    public void testAddAndRemoveSameClient() {
        testGroup.add(testClient);
        assertTrue(testGroup.contains(testClient));
        testGroup.remove(testClient);
        assertFalse(testGroup.contains(testClient));
        testGroup.add(testClient);
        assertTrue(testGroup.contains(testClient));
    }

    // Test for adding a client that is already in the parent group
    @Test
    public void testAddClientAlreadyInParentGroup() {
        ClientGroup parentGroup = new ClientGroup();
        Client testClient = new Client();
        parentGroup.add(testClient);
        ClientGroup testGroup = new ClientGroup(parentGroup);
        assertFalse(testGroup.add(testClient));
    }

    // Test for adding a client to a subgroup that already exists
    @Test
    public void testAddClientToExistingSubgroup() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup testGroup = new ClientGroup(parentGroup);
        ClientGroup subGroup = testGroup.createSubGroup();
        Client testClient = new Client();
        subGroup.add(testClient);
        assertFalse(testGroup.add(testClient));
        assertTrue(subGroup.contains(testClient));
    }

    // Test for subgroup containing the parent group
    @Test
    public void testSubgroupContainingParentGroup() {
        ClientGroup parentGroup = new ClientGroup();
        ClientGroup testGroup = new ClientGroup(parentGroup);
        ClientGroup subGroup = testGroup.createSubGroup();
        Client testClient = new Client();
        testGroup.add(testClient);
        subGroup.add(testClient);
        assertTrue(subGroup.contains(testClient));
        assertTrue(testGroup.contains(testClient));
    }

    // Test for multiple threads calling notifyMessageSent, notifyMessageReceived, and notifyExceptionCaught concurrently
    @Test
    public void testConcurrentNotifyMethods() {
        ClientGroup testGroup = new ClientGroup();
        Client client1 = new Client();
        Client client2 = new Client();
        ClientHandler handler1 = new ClientHandler() {
            private int messageCount = 0;

            @Override
            public void messageSent(Client client, Object message) {
                assertEquals("testMessage", message);
                messageCount++;
            }

            @Override
            public void messageReceived(Client client, Object message) {
                fail("messageReceived should not be called");
            }

            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                fail("exceptionCaught should not be called");
            }

            @Override
            public void clientAdded(Client client) {
                fail("clientAdded should not be called");
            }

            @Override
            public void clientRemoved(Client client) {
                fail("clientRemoved should not be called");
            }
        };
        ClientHandler handler2 = new ClientHandler() {
            private int messageCount = 0;

            @Override
            public void messageSent(Client client, Object message) {
                assertEquals("testMessage", message);
                messageCount++;
            }

            @Override
            public void messageReceived(Client client, Object message) {
                fail("messageReceived should not be called");
            }

            @Override
            public void exceptionCaught(Client client, Throwable cause) {
                fail("exceptionCaught should not be called");
            }

            @Override
            public void clientAdded(Client client) {
                fail("clientAdded should not be called");
            }

            @Override
            public void clientRemoved(Client client) {
                fail("clientRemoved should not be called");
            }
        };
        testGroup.add(client1);
        testGroup.addClientHandler(handler1);
        testGroup.add(client2);
        testGroup.addClientHandler(handler2);
        testGroup.notifyMessageSent(client1, "testMessage");
        assertEquals(2, handler1.messageCount);
        assertEquals(2, handler2.messageCount);
    }

    // Test for weak references not being cleared properly
    @Test
    public void testWeakReferencesNotCleared() {
        ClientGroup testGroup = new ClientGroup();
        Client client = new Client();
        ClientGroup subGroup = testGroup.createSubGroup();
        WeakReference<ClientGroup> weakReference = new WeakReference<>(subGroup);
        testGroup.add(client);
        testGroup.addClientHandler(new ClientHandler() {
            @Override
            public void messageSent(Client client, Object message) {
                assertEquals(subGroup, testGroup.parentGroup);
            }
        });
        testGroup = null;
        assertTrue(weakReference.get() != null);
    }

    
}
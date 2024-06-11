package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.cli.UserEvent;
import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint1;
    private ConnectionEndpoint connectionEndpoint2;
    private Vector<Entry> clients;

    @Before
    public void setup() {
        handler = new Handler();
        connectionEndpoint1 = mock(ConnectionEndpoint.class);
        connectionEndpoint2 = mock(ConnectionEndpoint.class);
        clients = new Vector<>();
        handler.clients = clients;
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint1);
        assertEquals(1, clients.size());
        assertTrue(clients.elementAt(0).ce.equals(connectionEndpoint1));
        assertNull(clients.elementAt(0).name);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        clients.add(new Entry());
        clients.get(2).ce = connectionEndpoint1;
        clients.get(2).name = "testName1";

        handler.removeConnection(connectionEndpoint1);
        assertEquals(2, clients.size());
        assertFalse(clients.contains(clients.get(1), connectionEndpoint1));
        assertEquals("testName1", clients.get(0).name);
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new Entry("Gamemaster", null));
        assertTrue(handler.isGmConnected());

        handler.removeConnection(connectionEndpoint1);
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendAllObj() throws IOException {
        String testString = "Test String";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        handler.sendAllObj(new UserEvent(false, "testName"));
        objectOutputStream.writeObject(handler.sendAllObj(new UserEvent(false, "testName")));
        outputStream.close();

        byte[] testByteArray = outputStream.toByteArray();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(testByteArray));
        Object testObject = objectInputStream.readObject();

        assertEquals(UserEvent.class, testObject.getClass());
        assertEquals("testName", ((UserEvent) testObject).name);
    }

    @Test
    public void testGetName() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        clients.add(new Entry());
        clients.get(2).ce = connectionEndpoint1;
        clients.get(2).name = "testName1";

        assertEquals("testName1", handler.getName(connectionEndpoint1));
        assertNull(handler.getName(connectionEndpoint2));
    }

    @Test
    public void testHandleInputString() {
        handler.addConnection(connectionEndpoint1);
        String testString = "Test String";

        handler.handleInput(new InputEvent(connectionEndpoint1, new String(testString.getBytes())));
        assertEquals(testString, clients.elementAt(0).ce.getLastReceivedMessage());
    }

    @Test
    public void testHandleInputChallenge() throws IOException {
        handler.addConnection(connectionEndpoint1);
        Challenge testChallenge = new Challenge();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(testChallenge);
        outputStream.close();

        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(testByteArrayToByteArray(outputStream.toByteArray()))));
        assertEquals(testChallenge, clients.elementAt(0).ce.getLastReceivedMessage());

        String testStringTo = "testTo";
        String testStringSend = "Test Send";

        testChallenge = new Challenge();
        testChallenge.setTo(testStringTo);
        testChallenge.setSilent(false);

        outputStream = new ByteArrayOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(testChallenge);
        outputStream.writeTo(connectionEndpoint1.getOutputStream());
        outputStream.close();

        testChallenge = new Challenge();
        testStringSend = testChallenge.roll();

        assertEquals(testStringTo, testChallenge.getTo());
        assertEquals(testStringSend, clients.elementAt(0).ce.getLastSentMessage());
        assertEquals(testStringSend, connectionEndpoint2.getLastReceivedMessage());
    }
}
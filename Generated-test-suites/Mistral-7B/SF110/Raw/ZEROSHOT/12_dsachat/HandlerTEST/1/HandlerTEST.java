package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;
import dsachat.share.hero.Hero;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Vector<Entry> clients;

    @Before
    public void setUp() {
        handler = new Handler();
        clients = new Vector<Entry>();
        connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
    }

    @After
    public void tearDown() {
        handler.removeConnection(connectionEndpoint);
        clients.clear();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, clients.size());
        assertNotNull(clients.get(0).name);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        String name = "TestUser";
        handler.addConnection(new ConnectionEndpoint(5678));
        handler.removeConnection(connectionEndpoint);
        assertEquals(1, clients.size());
        assertNull(clients.get(0).name);

        handler.addConnection(new ConnectionEndpoint(5678));
        handler.removeConnection(connectionEndpoint);
        assertEquals(1, clients.size());
        assertNotNull(clients.get(0).name);
        assertEquals("TestUser", clients.get(0).name);
    }

    @Test
    public void testHandleInputString() {
        handler.addConnection(connectionEndpoint);
        String message = "/whisper TestUser hello";

        handler.handleInput(new InputEvent(connectionEndpoint, new StringInputEvent(message)));

        assertEquals(1, clients.size());
        Entry entry = clients.get(0);
        assertNotNull(entry.name);
        assertEquals("TestUser", entry.name);

        String expectedOutput = "You whisper to TestUser: hello";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        connectionEndpoint.setOutput(printWriter);

        handler.handleInput(new InputEvent(connectionEndpoint, new StringInputEvent(message)));

        String actualOutput = stringWriter.toString();
        assertEquals(expectedOutput, actualOutput.trim());
    }

    @Test
    public void testHandleInputChallenge() throws IOException {
        handler.addConnection(connectionEndpoint);
        ConnectionEndpoint receiverConnection = new ConnectionEndpoint(5678);
        handler.addConnection(receiverConnection);
        String senderName = "TestSender";
        Hero senderHero = new Hero("TestSender", 50, 10);

        handler.handleInput(new InputEvent(connectionEndpoint, new ObjectInputEvent(new Challenge(senderName, 6, 2))));

        assertEquals(2, clients.size());
        assertEquals(senderHero, clients.get(0).hero);
        assertEquals(senderName, clients.get(0).name);
        assertNull(clients.get(1).name);

        String expectedOutput = "TestSender rolled 6-sided dice: 3 4 1 5 2 6";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        receiverConnection.setOutput(printWriter);

        handler.handleInput(new InputEvent(receiverConnection, new ObjectInputEvent(new Challenge(""))));

        String actualOutput = stringWriter.toString();
        assertEquals(expectedOutput.replace("TestSender", clients.get(0).name), actualOutput.trim());
    }

    @Test
    public void testHandleInputChallReq() throws IOException {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(new ConnectionEndpoint(5678));
        String receiverName = "TestReceiver";
        ConnectionEndpoint receiverConnection = new ConnectionEndpoint(5678);
        handler.handleInput(new InputEvent(connectionEndpoint, new ObjectInputEvent(new ChallReq("TestSender", receiverName))));

        assertEquals(2, clients.size());
        assertEquals("TestReceiver", clients.get(1).name);
        assertNull(clients.get(0).name);

        String expectedOutput = "TestSender challenges TestReceiver to a duel.";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        connectionEndpoint.setOutput(printWriter);

        handler.handleInput(new InputEvent(connectionEndpoint, new StringInputEvent("/accept")));

        String actualOutput = stringWriter.toString();
        assertEquals(expectedOutput.replace("TestReceiver", receiverName), actualOutput.trim());

        expectedOutput = "TestReceiver challenges TestSender to a duel.";
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        receiverConnection.setOutput(printWriter);

        handler.handleInput(new InputEvent(receiverConnection, new StringInputEvent("/accept")));

        assertEquals(2, clients.size());
        assertEquals("TestSender", clients.get(0).name);
        assertEquals("TestReceiver", clients.get(1).name);
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new ConnectionEndpoint(1234));
        handler.addConnection(new ConnectionEndpoint(5678));
        handler.addConnection(new ConnectionEndpoint(8765));
        handler.handleInput(new InputEvent(connectionEndpoint, new UserEvent(true, "Gamemaster")));

        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        String expectedOutput = "TestUser: TestMessage";
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        handler.sendAllSystemMsg(new PrintWriter(printWriter) {
            @Override
            public void write(String text) {
                super.write(expectedOutput + text);
            }
        });
        String actualOutput = stringWriter.toString();
        assertEquals(expectedOutput, actualOutput.trim());
    }

    @Test
    public void testRollDice() {
        ConnectionEndpoint connectionEndpoint = new ConnectionEndpoint(1234);
        handler.addConnection(connectionEndpoint);
        String message = "2d6+3";

        handler.handleInput(new InputEvent(connectionEndpoint, new StringInputEvent(message)));

        int expectedSum = 0;
        for (int i = 0; i < 2; ++i) {
            expectedSum += Challenge.dice(6);
        }
        expectedSum += 3;

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        connectionEndpoint.setOutput(printWriter);

        handler.handleInput(new InputEvent(connectionEndpoint, new ObjectInputEvent(new Challenge(""))));

        String actualOutput = stringWriter.toString();
        assertNotNull(actualOutput);
        int actualSum = 0;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(actualOutput.getBytes()))) {
            actualSum = ((Challenge) ois.readObject()).getResult();
        }
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testSendToOneStr() throws IOException {
        handler.addConnection(new ConnectionEndpoint(1234));
        handler.addConnection(new ConnectionEndpoint(5678));
        String message = "/whisper TestUser TestMessage";

        handler.handleInput(new InputEvent(new ConnectionEndpoint(1234), new StringInputEvent(message)));

        String expectedOutput1 = "You whisper to TestUser: TestMessage";
        StringWriter stringWriter1 = new StringWriter();
        PrintWriter printWriter1 = new PrintWriter(stringWriter1);
        new ConnectionEndpoint(5678).setOutput(printWriter1);

        handler.handleInput(new InputEvent(new ConnectionEndpoint(5678), new ObjectInputEvent(new Challenge(""))));

        String actualOutput1 = stringWriter1.toString();
        assertEquals(expectedOutput1, actualOutput1.trim());

        String expectedOutput2 = "TestUser: TestMessage";
        StringWriter stringWriter2 = new StringWriter();
        PrintWriter printWriter2 = new PrintWriter(stringWriter2);
        new ConnectionEndpoint(1234).setOutput(printWriter2);

        handler.handleInput(new InputEvent(new ConnectionEndpoint(1234), new ObjectInputEvent(new Challenge(""))));

        String actualOutput2 = stringWriter2.toString();
        assertEquals(expectedOutput2, actualOutput2.trim());
    }
}
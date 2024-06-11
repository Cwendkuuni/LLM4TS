package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallReq;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint1;
    private ConnectionEndpoint connectionEndpoint2;
    private Vector<Entry> clients;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint(12345);
        connectionEndpoint2 = new ConnectionEndpoint(67890);
        clients = handler.getClients();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint1);
        assertEquals(1, clients.size());
        Entry entry = clients.elementAt(0);
        assertNull(entry.name);
        assertNull(entry.hero);
        assertTrue(entry.ce.isConnected());

        handler.addConnection(connectionEndpoint2);
        assertEquals(2, clients.size());
        entry = clients.elementAt(1);
        assertNull(entry.name);
        assertNull(entry.hero);
        assertTrue(entry.ce.isConnected());
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        handler.removeConnection(connectionEndpoint1);
        assertEquals(1, clients.size());
        Entry entry = clients.elementAt(0);
        assertNull(entry.name);
        assertNull(entry.hero);
        assertFalse(entry.ce.isConnected());

        handler.removeConnection(connectionEndpoint2);
        assertEquals(0, clients.size());
    }

    @Test
    public void testHandleInputWithString() throws IOException {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), connectionEndpoint1, "hello world"));

        assertEquals(2, clients.size());
        for (Entry entry : clients) {
            assertNotNull(entry.name);
            assertEquals("hello world", entry.name);
            assertNull(entry.hero);
        }
    }

    @Test
    public void testHandleInputWithChallenge() throws IOException {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        Challenge challenge = new Challenge(1, 6);
        handler.handleInput(new InputEvent(new ByteArrayInputStream(serialize(challenge)), connectionEndpoint1, challenge));

        assertEquals(2, clients.size());
        for (Entry entry : clients) {
            if (entry.ce == connectionEndpoint1) {
                assertNotNull(entry.hero);
                assertEquals("1d6", entry.hero.getName());
                assertEquals(6, entry.hero.getLevel());
                assertEquals(5, entry.hero.getHealth());
                assertEquals(5, entry.hero.getMana());
                assertEquals(5, entry.hero.getStrength());
                assertEquals(5, entry.hero.getIntelligence());
                assertEquals(5, entry.hero.getAgility());
                assertEquals(5, entry.hero.getDefense());
                assertEquals(5, entry.hero.getArmor());
            } else {
                assertNull(entry.hero);
            }
        }
    }

    @Test
    public void testHandleInputWithChallReq() throws IOException {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        Hero hero1 = new Hero("John Doe", 1, 10, 10, 10, 10, 10, 10, 10);
        Hero hero2 = new Hero("Jane Doe", 1, 10, 10, 10, 10, 10, 10, 10);
        handler.updateHero(connectionEndpoint1, hero1);

        ChallReq challReq = new ChallReq("Jane Doe", "1d6");
        handler.handleInput(new InputEvent(new ByteArrayInputStream(serialize(challReq)), connectionEndpoint2, challReq));

        assertEquals(2, clients.size());
        for (Entry entry : clients) {
            if (entry.ce == connectionEndpoint1) {
                assertEquals("John Doe", entry.name);
                assertNotNull(entry.hero);
                assertEquals("John Doe", entry.hero.getName());
                assertEquals(1, entry.hero.getLevel());
                assertEquals(10, entry.hero.getHealth());
                assertEquals(10, entry.hero.getMana());
                assertEquals(10, entry.hero.getStrength());
                assertEquals(10, entry.hero.getIntelligence());
                assertEquals(10, entry.hero.getAgility());
                assertEquals(10, entry.hero.getDefense());
                assertEquals(10, entry.hero.getArmor());
            } else if (entry.ce == connectionEndpoint2) {
                assertEquals("Jane Doe", entry.name);
                assertNotNull(entry.hero);
                assertEquals("Jane Doe", entry.hero.getName());
                assertEquals(1, entry.hero.getLevel());
                assertEquals(10, entry.hero.getHealth());
                assertEquals(10, entry.hero.getMana());
                assertEquals(10, entry.hero.getStrength());
                assertEquals(10, entry.hero.getIntelligence());
                assertEquals(10, entry.hero.getAgility());
                assertEquals(10, entry.hero.getDefense());
                assertEquals(10, entry.hero.getArmor());
            }
        }
    }

    @Test
    public void testUpdateHero() {
        handler.addConnection(connectionEndpoint1);
        Hero hero = new Hero("John Doe", 1, 10, 10, 10, 10, 10, 10, 10);

        String name = handler.updateHero(connectionEndpoint1, hero);
        assertEquals("John Doe", name);
        assertNotNull(clients.elementAt(0).hero);
        assertEquals("John Doe", clients.elementAt(0).hero.getName());
    }

    @Test
    public void testSendAllSystemMsg() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);

        String message = "Test message";
        handler.sendAllSystemMsg(message);

        for (Entry entry : clients) {
            assertEquals(message, entry.ce.getMessage());
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }

    private class InputEvent implements Handler.InputEvent {
        private ConnectionEndpoint source;
        private byte[] data;

        public InputEvent(InputStream input, ConnectionEndpoint source, Object data) {
            this.source = source;
            this.data = new ByteArrayOutputStream().toByteArray(input);
            this.data = new ByteArrayInputStream(this.data).readAllBytes();
            this.data = new ByteArrayInputStream(this.data);
            this.data = new ObjectInputStream(this.data).readObject();
        }

        @Override
        public ConnectionEndpoint getSource() {
            return source;
        }

        @Override
        public Object getData() {
            return data;
        }
    }

    private static class Entry {
        ConnectionEndpoint ce;
        Hero hero;
        String name;

        private Entry() {
            ce = null;
            hero = null;
            name = null;
        }
    }
}
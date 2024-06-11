package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    private ConnectionEndpoint ce1, ce2;
    private Hero hero1;

    @Before
    public void setUp() {
        handler = new Handler();
        ce1 = new ConnectionEndpoint(1);
        ce2 = new ConnectionEndpoint(2);
        hero1 = new Hero("TestHero");
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(ce1);
        assertTrue(handler.clients.size() == 1);
        assertEquals("", handler.clients.elementAt(0).name);
        assertNull(handler.clients.elementAt(0).hero);
        assertNull(handler.getName(ce1));
    }

    @Test(expected = Exception.class)
    public void testAddConnectionFailedConnection() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        handler.addConnection(ce1);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        handler.removeConnection(ce1);
        assertTrue(handler.clients.size() == 1);
        assertNull(handler.getName(ce1));
        assertNotNull(handler.getName(ce2));
    }

    @Test
    public void testRollDice() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);
        handler.addConnection(ce3 = new ConnectionEndpoint(3));

        handler.sendAllObj(ce1, new UserEvent(true, "Gamemaster"));
        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("wisper Gamemaster roll 2d6")));

        assertEquals("Gamemaster: 3 4", handler.getLastSystemMessage());

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("roll 1d20+3")));
        assertEquals("TestHero: 13", handler.getLastSystemMessage());
    }

    @Test
    public void testWhisper() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("wisper TestHero TestMessage")));
        assertEquals("You whisper to TestHero: TestMessage", handler.getLastSystemMessageFor(ce1));
        assertEquals("TestHero: You whisper to TestHero: TestMessage", handler.getLastSystemMessageFor(ce2));
    }

    @Test
    public void testSendHeros() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("/getheros")));

        Vector<Hero> heros = (Vector<Hero>) handler.getLastObjectSentToAll();
        assertEquals(2, heros.size());
        assertNull(heros.elementAt(0));
        assertEquals("TestHero", heros.elementAt(1).getName());
    }

    @Test
    public void testSendHerosTo() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("/getheros " + ce2.getClientPort())));

        Vector<Hero> heros = (Vector<Hero>) handler.getLastObjectSentTo(ce2);
        assertEquals(2, heros.size());
        assertNull(heros.elementAt(0));
        assertEquals("TestHero", heros.elementAt(1).getName());
    }

    @Test
    public void testUpdateHero() {
        handler.addConnection(ce1);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new Hero("TestHero")));

        assertNull(handler.clients.elementAt(0).hero);
        assertNull(handler.getName(ce1));

        handler.handleInput(new InputEvent(new ByteArrayInputStream(serializeHero(hero1).toByteArray()), ce1, new Object[]{}));

        assertEquals(hero1.getName(), handler.getName(ce1));
        assertSame(hero1, handler.clients.elementAt(0).hero);
    }

    @Test
    public void testSystemMessage() {
        handler.addConnection(ce1);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("/sysmsg TestMessage")));

        assertEquals("TestMessage", handler.getLastSystemMessage());
    }

    @Test
    public void testSendSystemMessage() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("/sysmsg TestMessage")));

        assertEquals("TestMessage", handler.getLastSystemMessageFor(ce2));
    }

    @Test
    public void testSendObjectTo() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new Object[]{hero1}));

        assertEquals(hero1, handler.getLastObjectSentTo(ce2));
    }

    @Test
    public void testSendObjectToAll() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new Object[]{hero1}));

        assertEquals(hero1, handler.getLastObjectSentToAll());
    }

    @Test
    public void testHandleInputChallenge() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(serializeChallenge(new Challenge("TestChallenge")).toByteArray()), ce1, new Object[]{ce2}));

        assertEquals("TestChallenge", handler.getLastChallengeReceivedBy(ce2).roll());
    }

    @Test
    public void testHandleInputChallengeResponse() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        handler.handleInput(new InputEvent(new ByteArrayInputStream(serializeChallReq(new ChallReq("TestChallenge", "TestTarget")).toByteArray()), ce2, new Object[]{ce1}));

        assertEquals("TestTarget", handler.getLastChallengeResponseFor(ce1).getTo());
    }

    @Test
    public void testHandleInputUserEvent() {
        handler.addConnection(ce1);
        handler.handleInput(new InputEvent(new ByteArrayInputStream(serializeUserEvent(new UserEvent(true, "TestUser")).toByteArray()), ce1, new Object[]{}));

        assertEquals("TestUser", handler.getName(ce1));
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(ce1);
        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new UserEvent(true, "Gamemaster")));

        assertTrue(handler.isGmConnected());
    }

    @Test
    public void testSendAllSystemMsg() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        String systemMessage = "TestSystemMessage";
        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("/sysmsg " + systemMessage)));

        assertEquals(systemMessage, handler.getLastSystemMessageFor(ce2));
    }

    @Test
    public void testSendOneSystemMsg() {
        handler.addConnection(ce1);
        handler.addConnection(ce2);

        String systemMessage = "TestSystemMessage";
        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), ce1, new String("/sysmsg " + systemMessage)));

        assertNull(handler.getLastSystemMessageFor(ce1));
    }

    private byte[] serializeHero(Hero hero) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(hero);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing hero", e);
        }
    }

    private byte[] serializeChallenge(Challenge challenge) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(challenge);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing challenge", e);
        }
    }

    private byte[] serializeChallReq(ChallReq challReq) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(challReq);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing challReq", e);
        }
    }

    private byte[] serializeUserEvent(UserEvent userEvent) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(userEvent);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing userEvent", e);
        }
    }

    private String getLastSystemMessage() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        handler.handleInput(new InputEvent(new ByteArrayInputStream(new byte[0]), null, new PrintWriter(pw)) {
            @Override
            public void close() {
                // not used
            }
        });
        return sw.toString();
    }

    private String getLastSystemMessageFor(ConnectionEndpoint ce) {
        String lastMessage = "";
        for (String message : handler.getLastSystemMessages()) {
            if (message.startsWith(ce.getName() + ": ")) {
                lastMessage = message.substring(ce.getName().length() + 1);
                break;
            }
        }
        return lastMessage;
    }

    private Object getLastObjectSentTo(ConnectionEndpoint ce) {
        Object lastObject = null;
        for (Object o : handler.getLastObjectsSentToAll()) {
            if (o instanceof ConnectionEndpoint && ce.equals(o)) {
                lastObject = o;
                break;
            }
        }
        return lastObject;
    }

    private Object getLastObjectSentToAll() {
        return handler.getLastObjectsSentToAll().lastElement();
    }

    private Challenge getLastChallengeReceivedBy(ConnectionEndpoint ce) {
        Challenge lastChallenge = null;
        for (Object o : handler.getLastObjectsReceivedBy(ce)) {
            if (o instanceof Challenge) {
                lastChallenge = (Challenge) o;
                break;
            }
        }
        return lastChallenge;
    }

    private Object getLastChallengeResponseFor(ConnectionEndpoint ce) {
        Object lastObject = null;
        for (Object o : handler.getLastObjectsSentToAll()) {
            if (o instanceof ChallReq && ce.equals(((ChallReq) o).getFrom())) {
                lastObject = handler.getLastObjectsSentTo(ce).lastElement();
                break;
            }
        }
        return lastObject;
    }
}
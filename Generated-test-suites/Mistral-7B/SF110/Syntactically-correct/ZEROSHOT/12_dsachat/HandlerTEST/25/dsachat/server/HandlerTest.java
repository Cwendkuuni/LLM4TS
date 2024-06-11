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
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connection1;
    private ConnectionEndpoint connection2;
    private List<Hero> heroes;

    @Before
    public void setUp() {
        handler = new Handler();
        connection1 = new DummyConnectionEndpoint();
        connection2 = new DummyConnectionEndpoint();
        handler.addConnection(connection1);
        handler.addConnection(connection2);
        heroes = new ArrayList<>();
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connection1);
        assertEquals(2, handler.clients.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExistingConnection() {
        handler.addConnection(connection1);
        handler.addConnection(connection1);
    }

    @Test
    public void testRemoveConnection() {
        handler.removeConnection(connection1);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveNonExistingConnection() {
        handler.removeConnection(connection2);
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testHandleInputString() {
        handler.handleInput(new InputEvent(UserEvent.class, connection1, "hello"));
        assertEquals(1, handler.clients.size());

        Entry client = handler.clients.get(0);
        assertEquals("hello", client.ce.getLastMessageReceived());
    }

    @Test
    public void testHandleInputStringWhisper() {
        handler.handleInput(new InputEvent(UserEvent.class, connection1, "/w user hello"));
        assertEquals(1, handler.clients.size());

        Entry sender = handler.clients.get(0);
        assertEquals("Gamemaster: You whisper to user: hello", sender.ce.getLastMessageReceived());

        for (Entry client : handler.clients) {
            if (!client.name.equals(sender.name)) {
                assertEquals("user:hello", client.ce.getLastMessageReceived());
            }
        }
    }

    @Test
    public void testHandleInputStringRoll() {
        handler.handleInput(new InputEvent(UserEvent.class, connection1, "/roll 1d6+3"));
        assertEquals(1, handler.clients.size());

        Entry sender = handler.clients.get(0);
        assertEquals("Gamemaster: " + handler.getName(connection1) + " rolled 1d6+3 --> 4", sender.ce.getLastMessageReceived());
    }

    @Test
    public void testHandleInputStringRollMultiple() {
        handler.handleInput(new InputEvent(UserEvent.class, connection1, "/roll 4d6+2"));
        assertEquals(1, handler.clients.size());

        Entry sender = handler.clients.get(0);
        assertEquals("Gamemaster:" + handler.getName(connection1) + " rolls 4d6+2 --> 7 5 3 1 11", sender.ce.getLastMessageReceived());
    }

    @Test
    public void testHandleInputStringInvalidCommand() {
        handler.handleInput(new InputEvent(UserEvent.class, connection1, "/invalid"));
        assertEquals(1, handler.clients.size());

        Entry sender = handler.clients.get(0);
        assertEquals("command /invalid not found", sender.ce.getLastMessageReceived());
    }

    @Test
    public void testHandleInputObjectChallenge() {
        Hero hero = new Hero("TestHero");
        handler.handleInput(new InputEvent(Challenge.class, connection1, hero));
        assertEquals(1, handler.clients.size());

        Entry sender = handler.clients.get(0);
        assertEquals(hero.getName(), sender.name);
    }

    @Test
    public void testHandleInputObjectChallengeToAnotherClient() {
        Hero hero1 = new Hero("TestHero1");
        Hero hero2 = new Hero("TestHero2");
        handler.handleInput(new InputEvent(Challenge.class, connection1, hero1));
        handler.handleInput(new InputEvent(Challenge.class, connection2, hero2));

        assertEquals(2, handler.clients.size());

        for (Entry client : handler.clients) {
            if (client.name.equals(hero1.getName())) {
                assertEquals(hero1, client.hero);
            } else if (client.name.equals(hero2.getName())) {
                assertEquals(hero2, client.hero);
            }
        }
    }

    @Test
    public void testHandleInputObjectChallengeSilent() {
        Hero hero1 = new Hero("TestHero1");
        Hero hero2 = new Hero("TestHero2");
        handler.handleInput(new InputEvent(Challenge.class, connection1, hero1));
        handler.handleInput(new InputEvent(Challenge.class, connection2, hero2));
        handler.handleInput(new InputEvent(Challenge.class, connection1, hero1));

        assertEquals(2, handler.clients.size());

        for (Entry client : handler.clients) {
            if (client.name.equals(hero1.getName())) {
                assertTrue(client.hero.isSilent());
            } else if (client.name.equals(hero2.getName())) {
                assertFalse(client.hero.isSilent());
            }
        }
    }

    @Test
    public void testHandleInputObjectChallReq() {
        Hero hero1 = new Hero("TestHero1");
        Hero hero2 = new Hero("TestHero2");
        handler.handleInput(new InputEvent(Challenge.class, connection1, hero1));
        handler.handleInput(new InputEvent(Challenge.class, connection2, hero2));
        handler.handleInput(new InputEvent(ChallReq.class, connection2, hero1.getName()));

        assertEquals(2, handler.clients.size());

        Entry sender = handler.clients.get(1);
        assertEquals(hero1, sender.hero);
    }

    @Test
    public void testHandleInputObjectHeroUpdate() {
        Hero hero1 = new Hero("TestHero1");
        handler.handleInput(new InputEvent(Challenge.class, connection1, hero1));
        Hero hero2 = new Hero("TestHero2");
        hero2.setName("TestHero3");
        handler.handleInput(new InputEvent(Hero.class, connection2, hero2));

        assertEquals(2, handler.clients.size());

        for (Entry client : handler.clients) {
            if (client.name.equals(hero1.getName())) {
                assertEquals(hero1, client.hero);
            } else if (client.name.equals(hero2.getName())) {
                assertEquals(hero2, client.hero);
                assertEquals("TestHero3", client.name);
            }
        }
    }

    @Test
    public void testSendHeros() {
        heroes.add(new Hero("TestHero1"));
        heroes.add(new Hero("TestHero2"));
        handler.sendHeros(connection1);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
        try {
            objOutStream.writeObject(heroes);
            byte[] bytes = outStream.toByteArray();

            InputStream inStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objInStream = new ObjectInputStream(inStream);
            List<Hero> receivedHeros = (List<Hero>) objInStream.readObject();

            assertEquals(heroes, receivedHeros);
        } catch (IOException e) {
            fail("IOException occurred during test: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException occurred during test: " + e.getMessage());
        }
    }

    @Test
    public void testIsGmConnected() {
        handler.handleInput(new InputEvent(UserEvent.class, connection1, new UserEvent(true, "Gamemaster")));
        assertTrue(handler.isGmConnected());

        handler.handleInput(new InputEvent(UserEvent.class, connection2, new UserEvent(false, "user")));
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        handler.sendOneSystemMsg(connection1, "Test message");

        assertEquals("Test message", pw.getBuffer().toString());
    }

    @Test
    public void testSendAllSystemMsg() {
        StringWriter sw1 = new StringWriter();
        PrintWriter pw1 = new PrintWriter(sw1);
        StringWriter sw2 = new StringWriter();
        PrintWriter pw2 = new PrintWriter(sw2);

        handler.handleInput(new InputEvent(UserEvent.class, connection1, new UserEvent(false, "user1")));
        handler.handleInput(new InputEvent(UserEvent.class, connection2, new UserEvent(false, "user2")));

        handler.sendAllSystemMsg("Test message");

        assertEquals("user1: Test message", pw1.getBuffer().toString());
        assertEquals("user2: Test message", pw2.getBuffer().toString());
    }

    @Test
    public void testWhisper() {
        StringWriter sw1 = new StringWriter();
        PrintWriter pw1 = new PrintWriter(sw1);
        StringWriter sw2 = new StringWriter();
        PrintWriter pw2 = new PrintWriter(sw2);

        handler.handleInput(new InputEvent(UserEvent.class, connection1, new UserEvent(false, "user1")));
        handler.handleInput(new InputEvent(UserEvent.class, connection2, new UserEvent(false, "user2")));

        handler.whisper(connection1, "user2", "Test message");

        assertEquals("user1: Test message", pw1.getBuffer().toString());
        assertEquals("user2: Test message", pw2.getBuffer().toString());
    }

    @Test
    public void testRollDice() {
        StringWriter sw1 = new StringWriter();
        PrintWriter pw1 = new PrintWriter(sw1);
        ConnectionEndpoint connection3 = new DummyConnectionEndpoint();
        handler.addConnection(connection3);

        handler.handleInput(new InputEvent(UserEvent.class, connection1, new UserEvent(false, "user1")));
        handler.handleInput(new InputEvent(UserEvent.class, connection2, new UserEvent(false, "user2")));
        handler.handleInput(new InputEvent(UserEvent.class, connection3, new UserEvent(false, "user3")));

        handler.rollDice(connection1, "/roll 1d6+3");

        assertEquals("user1 rolled 1d6+3 --> 3", pw1.getBuffer().toString());
    }

    private static class DummyConnectionEndpoint implements ConnectionEndpoint {
        private String lastMessageReceived;

        @Override
        public int getClientPort() {
            return 0;
        }

        @Override
        public void send(Object obj) throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void send(String message) throws IOException {
            lastMessageReceived = message;
        }

        @Override
        public String getLastMessageReceived() {
            return lastMessageReceived;
        }
    }
}
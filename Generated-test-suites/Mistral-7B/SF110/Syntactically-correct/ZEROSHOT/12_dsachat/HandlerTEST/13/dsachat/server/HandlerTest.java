package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {
    private Handler handler;
    private ConnectionEndpoint connectionEndpoint1, connectionEndpoint2;
    private Hero hero;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint1 = new ConnectionEndpoint(1, new PrintStream(new ByteArrayOutputStream()));
        connectionEndpoint2 = new ConnectionEndpoint(2, new PrintStream(new ByteArrayOutputStream()));
        hero = new Hero("TestHero");
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
        handler.addConnection(connectionEndpoint2);
        assertEquals(2, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.removeConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
        assertEquals(connectionEndpoint2, handler.clients.elementAt(0).ce);
    }

    @Test
    public void testHandleInputString() {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/hello world\r\n".getBytes()})));
        assertEquals("TestHero: hello world", handler.clients.elementAt(0).ce.getOutput().toString());
    }

    @Test
    public void testHandleInputChallenge() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.clients.elementAt(0).hero = hero;
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/challenge 1d6".getBytes()})));
        Challenge challenge = new Challenge(6, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        try {
            objectOutputStream.writeObject(challenge);
            byte[] challengeBytes = outputStream.toByteArray();
            handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(challengeBytes)));
            assertEquals("TestHero rolled 1d6 --> 3", handler.clients.elementAt(0).ce.getOutput().toString());
            assertEquals("3", handler.clients.elementAt(1).ce.getOutput().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleInputChallengeWithTo() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.clients.elementAt(0).hero = hero;
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/challenge 1d6 to TestHero2".getBytes()})));
        Challenge challenge = new Challenge(6, "TestHero2");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        try {
            objectOutputStream.writeObject(challenge);
            byte[] challengeBytes = outputStream.toByteArray();
            handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(challengeBytes)));
            assertEquals("TestHero rolled 1d6 --> 3", handler.clients.elementAt(0).ce.getOutput().toString());
            assertEquals("3 TestHero: You were challenged by TestHero: roll a dice\r\n", handler.clients.elementAt(1).ce.getOutput().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleInputChallReq() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.handleInput(new InputEvent(connectionEndpoint2, new ByteArrayInputStream(new byte[]{"/chall 1d6 TestHero".getBytes()})));
        ChallReq challReq = new ChallReq("1d6", "TestHero");
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/accept".getBytes()})));
        Challenge challenge = new Challenge(6, "TestHero");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        try {
            objectOutputStream.writeObject(challenge);
            byte[] challengeBytes = outputStream.toByteArray();
            handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(challengeBytes)));
            assertEquals("TestHero rolled 1d6 --> 3", handler.clients.elementAt(1).ce.getOutput().toString());
            assertEquals("3 TestHero: You were challenged by TestHero2: roll a dice\r\n", handler.clients.elementAt(0).ce.getOutput().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AssertionError.class)
    public void testHandleInputUnknownCommand() {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/unknown".getBytes()})));
    }

    @Test
    public void testSendHeros() {
        handler.addConnection(connectionEndpoint1);
        handler.clients.elementAt(0).hero = hero;
        handler.sendHeros(connectionEndpoint1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        Vector<Hero> heroes = new Vector<Hero>(Arrays.asList(hero));
        try {
            objectOutputStream.writeObject(heroes);
            byte[] heroBytes = outputStream.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(heroBytes);
            handler.handleInput(new InputEvent(connectionEndpoint1, inputStream));
            assertEquals(1, handler.clients.elementAt(0).hero.getId());
            assertEquals("TestHero", handler.clients.elementAt(0).hero.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new ConnectionEndpoint(1, new PrintStream(new ByteArrayOutputStream())));
        handler.addConnection(new ConnectionEndpoint(2, new PrintStream(new ByteArrayOutputStream())));
        handler.handleInput(new InputEvent(new ConnectionEndpoint(3, new PrintStream(new ByteArrayOutputStream())), new ByteArrayInputStream(new byte[]{"/connect Gamemaster".getBytes()})));
        assertTrue(handler.isGmConnected());
        handler.handleInput(new InputEvent(new ConnectionEndpoint(4, new PrintStream(new ByteArrayOutputStream())), new ByteArrayInputStream(new byte[]{"/disconnect".getBytes()})));
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        handler.addConnection(connectionEndpoint1);
        handler.sendOneSystemMsg(connectionEndpoint1, "Test message");
        assertEquals("Test message", handler.clients.elementAt(0).ce.getOutput().toString());
    }

    @Test
    public void testSendAllSystemMsg() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.sendAllSystemMsg("Test message");
        assertEquals("Test message", handler.clients.elementAt(0).ce.getOutput().toString());
        assertEquals("Test message", handler.clients.elementAt(1).ce.getOutput().toString());
    }

    @Test
    public void testWhisper() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/w TestHero2 Test message".getBytes()})));
        assertEquals("Test message", handler.clients.elementAt(1).ce.getOutput().toString());
        assertEquals("You whisper to TestHero2: Test message", handler.clients.elementAt(0).ce.getOutput().toString());
    }

    @Test
    public void testRollDice() {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/roll 1d6".getBytes()})));
        assertEquals("TestHero rolled 1d6 --> 3", handler.clients.elementAt(0).ce.getOutput().toString());
        assertEquals("3", handler.clients.elementAt(1).ce.getOutput().toString());

        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/roll 1d6+2".getBytes()})));
        assertEquals("TestHero rolled 1d6 --> 5", handler.clients.elementAt(0).ce.getOutput().toString());
        assertEquals("5", handler.clients.elementAt(1).ce.getOutput().toString());
    }

    @Test
    public void testUpdateHero() {
        handler.addConnection(connectionEndpoint1);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/hero TestHero TestHeroName".getBytes()})));
        assertEquals("TestHeroName", handler.clients.elementAt(0).name);
        assertEquals(null, handler.clients.elementAt(0).hero);

        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/hero TestHero TestHeroName TestHeroId".getBytes()})));
        assertEquals("TestHeroName", handler.clients.elementAt(0).name);
        Hero testHero = new Hero("TestHeroId");
        testHero.setName("TestHeroNewName");
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/hero TestHero TestHeroNewName TestHeroId".getBytes()})));
        assertEquals("TestHeroNewName", handler.clients.elementAt(0).hero.getName());
        assertEquals("TestHeroId", handler.clients.elementAt(0).hero.getId().toString());
    }

    @Test
    public void testSendAllStr() {
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        handler.handleInput(new InputEvent(connectionEndpoint1, new ByteArrayInputStream(new byte[]{"/broadcast Test message".getBytes()})));
        assertEquals("Test message", handler.clients.elementAt(0).ce.getOutput().toString());
        assertEquals("Test message", handler.clients.elementAt(1).ce.getOutput().toString());
    }
}
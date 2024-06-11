package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.share.hero.Hero;
import dsachat.share.hero.HeroFactory;
import dsachat.share.UserEvent;
import dsachat.share.hero.Challenge;
import dsachat.share.hero.ChallengeFactory;
import dsachat.share.hero.ChallReq;
import dsachat.share.hero.Challenge.dice;
import dsachat.share.hero.Challenge.diceResult;
import dsachat.share.hero.HeroFactory.HeroType;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint con;
    private ConnectionEndpoint con2;
    private Hero hero;
    private List<Entry> clients;
    private ByteArrayOutputStream output;
    private ObjectOutputStream outputStream;
    private PrintWriter writer;

    @Before
    public void setUp() {
        handler = new Handler();
        con = mock(ConnectionEndpoint.class);
        con2 = mock(ConnectionEndpoint.class);
        clients = new ArrayList<>();
        clients.add(new Entry());
        clients.add(new Entry());
        handler.clients = clients;

        output = new ByteArrayOutputStream();
        outputStream = new ObjectOutputStream(output);
        writer = new PrintWriter(new PrintWriter(output), true);
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(con);
        verify(con, times(1)).send(any(UserEvent.class));
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(con);
        handler.removeConnection(con);
        verify(con, times(1)).isConnected();
        verify(con, times(1)).getClientPort();
        verify(con, times(1)).send(any(UserEvent.class));
    }

    @Test
    public void testHandleInputString() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);
        handler.handleInput(new InputEvent(null, con, ""));
        handler.handleInput(new InputEvent(null, con, "/help"));
        handler.handleInput(new InputEvent(null, con, "/roll 2d6"));
        handler.handleInput(new InputEvent(null, con, "/roll 2d6+3"));
        handler.handleInput(new InputEvent(null, con, "/whisper user1 roll result"));
        handler.handleInput(new InputEvent(null, con, "/challenge user1 2d6"));
        handler.handleInput(new InputEvent(null, con, "/challenge user1 user2 2d6"));
        handler.handleInput(new InputEvent(null, con, "/getheros"));
        handler.handleInput(new InputEvent(null, con, "/me I am here"));
        handler.handleInput(new InputEvent(null, con, "/w Gamemaster help"));

        String expectedOutput = "You: \n" +
                "Help:\n" +
                "/roll <number>d<sides> [<number>]: Roll a dice with given number of sides and optional number of rolls.\n" +
                "/roll <number>d<sides>+<modifier>: Roll a dice with given number of sides, optional number of rolls and a modifier.\n" +
                "/whisper <user>: <message>: Whisper a message to a user.\n" +
                "/challenge <user> [<number>d<sides>]: Challenge a user to a dice roll.\n" +
                "/challenge <user1> <user2> <number>d<sides>: Challenge user1 to a dice roll against user2.\n" +
                "/getheros: Show a list of all connected users and their heroes.\n" +
                "/me <message>: Announce a message as the user in the chat.\n" +
                "/w <user> <message>: Whisper a message to a user.\n" +
                "/help: Show the available commands and their usage.\n" +
                "user1: roll result\n" +
                "user1 rolled 2d6 --> 4, 6\n" +
                "user1 rolled 2d6+3 --> 7\n" +
                "You rolled 2d6 --> 3, 6\n" +
                "You rolled 2d6+3 --> 6\n" +
                "user1: 4, 6\n" +
                "user1: 7\n" +
                "You: I am here\n" +
                "Gamemaster: help";

        String actualOutput = output.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testHandleInputChallenge() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        Challenge challenge = ChallengeFactory.getChallenge("user1", 2, 6);
        handler.handleInput(new InputEvent(null, con, "/challenge user1 2d6"));

        verify(con, times(1)).send(eq("user1 challenged you to a dice roll: 2d6"));

        Hero hero = new Hero("testHero", HeroType.WARRIOR);
        challenge = ChallengeFactory.getChallenge("user1", hero, 2, 6);
        handler.handleInput(new InputEvent(null, con, "/challenge user1"));
        handler.handleInput(new InputEvent(null, con, hero));

        verify(con, times(1)).send(eq("user1 challenged you to a dice roll with hero testHero"));
    }

    @Test
    public void testHandleInputChallengeResponse() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);
        Challenge challenge = ChallengeFactory.getChallenge("user1", 2, 6);
        handler.handleInput(new InputEvent(null, con, "/challenge user1 2d6"));
        handler.handleInput(new InputEvent(null, con2, challenge));

        verify(con, times(1)).send(eq("user1 challenged you to a dice roll: 2d6"));
        verify(con2, times(1)).send(eq("user1 challenged you to a dice roll: 2d6"));
        verify(con2, times(1)).send(eq("user1 rolled 5, 4"));

        handler.handleInput(new InputEvent(null, con, diceResult.SUCCESS));

        verify(con, times(1)).send(eq("user1 won the dice roll against you with a result of 5, 4"));
    }

    @Test
    public void testHandleInputHeroUpdate() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        hero = HeroFactory.getHero("testHero", HeroType.WARRIOR);
        handler.handleInput(new InputEvent(null, con, hero));

        verify(con, times(1)).send(eq("Welcome testHero"));
    }

    @Test
    public void testHandleInputUserEvent() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        UserEvent userEvent = new UserEvent(false, "testUser");
        handler.handleInput(new InputEvent(null, con, userEvent));

        verify(con, times(1)).send("*" + userEvent.getName() + ": " + userEvent.getMessage());
    }

    @Test
    public void testHandleInputUserEventGmConnected() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        UserEvent userEvent = new UserEvent(true, "Gamemaster");
        handler.handleInput(new InputEvent(null, con, userEvent));

        verify(con, times(1)).send("Gamemaster connected");
    }

    @Test
    public void testHandleInputInvalidCommand() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.handleInput(new InputEvent(null, con, "/invalid"));

        verify(con, times(1)).send("command /invalid not found");
    }

    @Test
    public void testSendHeros() throws IOException {
        when(con.send(any(Object.class))).thenReturn(true);

        List<Hero> heroes = new ArrayList<>();
        Hero hero1 = new Hero("user1_hero", HeroType.ARCHER);
        Hero hero2 = new Hero("user2_hero", HeroType.MAGE);
        heroes.add(hero1);
        heroes.add(hero2);

        handler.sendHeros(con);

        verify(con, times(1)).send(heroes);
    }

    @Test
    public void testIsGmConnected() {
        assertTrue(handler.isGmConnected());

        clients.remove(1);
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.sendOneSystemMsg(con, "testMessage");

        verify(con, times(1)).send("testMessage");
    }

    @Test
    public void testSendAllSystemMsg() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.sendAllSystemMsg("testMessage");

        for (Entry client : clients) {
            verify(client.ce, times(1)).send("testMessage");
        }
    }

    @Test
    public void testWhisper() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);
        when(con2.send(any(String.class))).thenReturn(true);

        handler.whisper(con, "user1 testMessage");

        verify(con, times(1)).send("You whisper to user1: testMessage");
        verify(con2, times(1)).send("You whisper to user1: testMessage");
    }

    @Test
    public void testRollDice() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.rollDice(con, "1d6");
        handler.rollDice(con, "2d6");
        handler.rollDice(con, "3d6");
        handler.rollDice(con, "4d6");
        handler.rollDice(con, "5d6");
        handler.rollDice(con, "6d6");
        handler.rollDice(con, "7d6");
        handler.rollDice(con, "8d6");
        handler.rollDice(con, "9d6");
        handler.rollDice(con, "10d6");

        for (int i = 0; i < 10; i++) {
            verify(con, times(1)).send("You rolled d6 --> " + dice.rollResult(1, 6));
        }
    }

    @Test
    public void testRollDiceWithMod() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.rollDice(con, "1d6+3");
        handler.rollDice(con, "2d6+3");
        handler.rollDice(con, "3d6+3");
        handler.rollDice(con, "4d6+3");
        handler.rollDice(con, "5d6+3");
        handler.rollDice(con, "6d6+3");
        handler.rollDice(con, "7d6+3");
        handler.rollDice(con, "8d6+3");
        handler.rollDice(con, "9d6+3");
        handler.rollDice(con, "10d6+3");

        for (int i = 0; i < 10; i++) {
            int rollResult = dice.rollResult(1, 6) + 3;
            verify(con, times(1)).send("You rolled 1d6+3 --> " + rollResult);
        }
    }

    @Test
    public void testRollDiceWithCount() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.rollDice(con, "2d6");
        handler.rollDice(con, "3d6");
        handler.rollDice(con, "4d6");
        handler.rollDice(con, "5d6");
        handler.rollDice(con, "6d6");
        handler.rollDice(con, "7d6");
        handler.rollDice(con, "8d6");
        handler.rollDice(con, "9d6");
        handler.rollDice(con, "10d6");

        for (int i = 0; i < 10; i++) {
            List<diceResult> results = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                results.add(dice.rollResult(1, 6));
            }
            String expectedOutput = "You rolled 2d6 --> ";
            for (diceResult result : results) {
                expectedOutput += result.getRoll() + ", ";
            }
            expectedOutput = expectedOutput.substring(0, expectedOutput.length() - 2);
            verify(con, times(1)).send(expectedOutput);
        }
    }

    @Test
    public void testRollDiceWithCountAndMod() throws IOException {
        when(con.send(any(String.class))).thenReturn(true);

        handler.rollDice(con, "2d6+3");
        handler.rollDice(con, "3d6+3");
        handler.rollDice(con, "4d6+3");
        handler.rollDice(con, "5d6+3");
        handler.rollDice(con, "6d6+3");
        handler.rollDice(con, "7d6+3");
        handler.rollDice(con, "8d6+3");
        handler.rollDice(con, "9d6+3");
        handler.rollDice(con, "10d6+3");

        for (int i = 0; i < 10; i++) {
            List<diceResult> results = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                results.add(dice.rollResult(1, 6));
            }
            int sum = 0;
            for (diceResult result : results) {
                sum += result.getRoll() + 3;
            }
            String expectedOutput = "You rolled 2d6+3 --> " + sum;
            verify(con, times(1)).send(expectedOutput);
        }
    }
}

class Entry {
    ConnectionEndpoint ce;
    String name;
    Hero hero;
}
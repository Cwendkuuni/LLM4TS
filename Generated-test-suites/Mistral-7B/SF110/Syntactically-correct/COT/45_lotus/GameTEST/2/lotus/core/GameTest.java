package lotus.core.test;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.Player;
import lotus.core.Stack;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);
        Player playingPlayer = Game.getPlayingPlayer();
        Player nonPlayingPlayer = Game.getNonPlayingPlayer();

        assertEquals(name1, playingPlayer.getName());
        assertEquals(name2, nonPlayingPlayer.getName());
    }

    @Test
    public void testGetOtherPlayer() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        Player player1 = new Player(ip1, name1, new CardCollection());
        Player player2 = new Player(ip2, name2, new CardCollection());

        assertEquals(player2, Game.getOtherPlayer(player1));
        assertEquals(player1, Game.getOtherPlayer(player2));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPlayingPlayer_InvalidPlayingPlayer() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);
        Game.playingPlayer = -1;
        Player playingPlayer = Game.getPlayingPlayer();
    }

    @Test(expected = NullPointerException.class)
    public void testInit_NullUserInterface() {
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(null, new DummyUserInterface(), name1, name2, deck1, deck2);
    }

    @Test(expected = NullPointerException.class)
    public void testInit_NullPlayerName() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = null;
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);
    }

    @Test(expected = NullPointerException.class)
    public void testInit_NullDeck() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = null;
        CardCollection deck2 = new ArrayList<>();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);
    }

    @Test
    public void testInit() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player player1 = Game.player1;
        Player player2 = Game.player2;
        Stack stack = Game.stack;

        assertEquals(ip1, player1.getUserInterface());
        assertEquals(name1, player1.getName());
        assertEquals(deck1, player1.getDeck());

        assertEquals(ip2, player2.getUserInterface());
        assertEquals(name2, player2.getName());
        assertEquals(deck2, player2.getDeck());

        assertEquals(0, stack.size());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NoPlayersSpeak() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Game.givePriorityToCurrentPlayer();
        assertTrue(Game.stack.empty());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_PlayersSpeakInOrder() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        player1.setLetPlayerSpeakReturn(true);
        player2.setLetPlayerSpeakReturn(true);

        Game.givePriorityToCurrentPlayer();
        Game.givePriorityToCurrentPlayer();

        Stack stack = Game.stack;
        assertEquals(1, stack.size());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_PlayersSpeakOutOfOrder() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        player2.setLetPlayerSpeakReturn(true);
        player1.setLetPlayerSpeakReturn(true);

        Game.givePriorityToCurrentPlayer();
        Game.givePriorityToCurrentPlayer();

        Stack stack = Game.stack;
        assertEquals(1, stack.size());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NoResolution() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        player1.setLetPlayerSpeakReturn(true);
        player2.setLetPlayerSpeakReturn(true);

        Game.givePriorityToCurrentPlayer();
        Game.givePriorityToCurrentPlayer();

        Stack stack = Game.stack;
        assertEquals(1, stack.size());

        Game.givePriorityToCurrentPlayer();
        Stack stackAfter = Game.stack;

        assertEquals(1, stackAfter.size());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_EmptyStack() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Game.givePriorityToCurrentPlayer();
        Game.givePriorityToCurrentPlayer();
        Game.stack.resolveLast();

        Stack stack = Game.stack;
        assertEquals(0, stack.size());
    }

    @Test
    public void testInit_StackIsNotEmpty() {
        UserInterface ip1 = new DummyUserInterface();
        UserInterface ip2 = new DummyUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new ArrayList<>();
        CardCollection deck2 = new ArrayList<>();

        Card card1 = new Card("Card1");
        Card card2 = new Card("Card2");

        deck1.add(card1);
        deck2.add(card2);

        Player player1 = new Player(ip1, name1, deck1);
        Player player2 = new Player(ip2, name2, deck2);

        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Stack stack = Game.stack;
        assertEquals(0, stack.size());
    }
}
package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;
    private Game game;
    private CardCollection deck1;
    private CardCollection deck2;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        deck1 = new ArrayList<>();
        deck2 = new ArrayList<>();
        player1 = new Player(ip1, "Player1", deck1);
        player2 = new Player(ip2, "Player2", deck2);
        game = new Game();
        game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
    }

    @Test
    public void testGetPlayingPlayer() {
        assertEquals(player1, game.getPlayingPlayer());
        game.playingPlayer = 2;
        assertEquals(player2, game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertEquals(player2, game.getNonPlayingPlayer());
        game.playingPlayer = 1;
        assertEquals(player1, game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        assertEquals(player2, game.getOtherPlayer(player1));
        assertEquals(player1, game.getOtherPlayer(player2));
    }

    @Test
    public void testInit() {
        assertNotNull(game.player1);
        assertNotNull(game.player2);
        assertEquals(player1, game.getPlayingPlayer());
        assertEquals(player2, game.getNonPlayingPlayer());
        assertEquals(new UntapPhase(), game.currentPhase);
        assertEquals(Collections.emptyList(), game.player1.hand);
        assertEquals(Collections.emptyList(), game.player2.hand);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Mockito.doReturn(true).when(player1).letPlayerSpeak();
        Mockito.doReturn(true).when(player2).letPlayerSpeak();
        game.givePriorityToCurrentPlayer();
        Mockito.verify(player1).letPlayerSpeak();
        Mockito.verify(player2).letPlayerSpeak();

        Mockito.doReturn(false).when(player1).letPlayerSpeak();
        Mockito.doReturn(false).when(player2).letPlayerSpeak();
        Mockito.doReturn(new Card("TestCard")).when(deck1).get(0);
        Mockito.doReturn(new Card("TestCard2")).when(deck2).get(0);

        game.givePriorityToCurrentPlayer();
        Mockito.verify(player1, Mockito.times(2)).letPlayerSpeak();
        Mockito.verify(player2, Mockito.times(2)).letPlayerSpeak();

        game.stack.push(new Ability("TestAbility", player1));
        game.givePriorityToCurrentPlayer();
        Mockito.verify(player1, Mockito.times(3)).letPlayerSpeak();
        Mockito.verify(player2, Mockito.times(3)).letPlayerSpeak();
        Mockito.verify(player1).resolveAbility(game.stack.peek());
    }
}
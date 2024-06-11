package lotus.core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTest {
    private CardCollection deck1;
    private CardCollection deck2;
    private UserInterface ip1;
    private UserInterface ip2;
    private Player player1;
    private Player player2;

    @Before
    public void setup() {
        ip1 = mock(UserInterface.class);
        ip2 = mock(UserInterface.class);
        deck1 = new CardCollection();
        deck2 = new CardCollection();
    }

    @Test
    public void testGetPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(Game.player1, Game.getPlayingPlayer());
        Game.playingPlayer = 2;
        assertEquals(Game.player2, Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(Game.player2, Game.getNonPlayingPlayer());
        Game.playingPlayer = 1;
        assertEquals(Game.player1, Game.getNonPlayingPlayer());
    }

    @Test
    public void testInit() {
        Game.init(ip1, ip2, "Player1", "Player2", deck1, deck2);
        assertEquals(ip1, Game.player1.getUserInterface());
        assertEquals(ip2, Game.player2.getUserInterface());
        assertEquals("Player1", Game.player1.getName());
        assertEquals("Player2", Game.player2.getName());
        assertEquals(deck1, Game.player1.getLibrary());
        assertEquals(deck2, Game.player2.getLibrary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullUserInterfaces() {
        Game.init(null, null, "Player1", "Player2", deck1, deck2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullNames() {
        Game.init(ip1, ip2, null, null, deck1, deck2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullDecks() {
        Game.init(ip1, ip2, "Player1", "Player2", null, null);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = mock(UserInterface.class);
        UserInterface ip2 = mock(UserInterface.class);
        Player player1 = new Player(ip1, "Player1");
        Player player2 = new Player(ip2, "Player2");
        Game game = new Game();
        game.player1 = player1;
        game.player2 = player2;
        game.init(ip1, ip2, "Player1", "Player2", new CardCollection(), new CardCollection());
        List<String> expectedSpeech = new ArrayList<>();
        expectedSpeech.add("Player1's turn");
        when(ip1.letPlayerSpeak()).thenReturn(true).thenReturn(false).thenReturn(true);
        when(ip2.letPlayerSpeak()).thenReturn(true).thenReturn(false).thenReturn(true);

        game.givePriorityToCurrentPlayer();
        verify(ip1).speak(expectedSpeech.get(0));
        verify(ip2).speech(Collections.emptyList());

        assertTrue(game.stack.isEmpty());
        game.stack.push(new Card("Card1"));
        game.givePriorityToCurrentPlayer();
        verify(ip1).speech(Collections.emptyList());
        verify(ip2).speech(Collections.singletonList("Resolution: Card1"));
    }
}
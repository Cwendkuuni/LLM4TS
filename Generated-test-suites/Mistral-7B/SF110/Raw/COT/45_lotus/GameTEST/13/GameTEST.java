import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.Player;
import lotus.core.Stack;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player expectedPlayer = Game.player1;
        Player actualPlayer = Game.getPlayingPlayer();

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    public void testGetNonPlayingPlayer() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player expectedPlayer = Game.player2;
        Player actualPlayer = Game.getNonPlayingPlayer();

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    public void testGetOtherPlayer() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        Player player1 = Game.player1;
        Player player2 = Game.player2;

        Player expectedPlayer = player2;
        Player actualPlayer = Game.getOtherPlayer(player1);

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitWithNullUserInterface() {
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(null, new StubUserInterface(), name1, name2, deck1, deck2);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullPlayerName() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = null;
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullDeck() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = null;
        CardCollection deck2 = new CardCollection();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithSameDecks() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = deck1;
        Game.init(ip1, ip2, name1, name2, deck1, deck2);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        Player player1 = new Player(ip1, "Player1", new CardCollection());
        Player player2 = new Player(ip2, "Player2", new CardCollection());
        Game.init(ip1, ip2, player1, player2, new CardCollection(), new CardCollection());

        Game.givePriorityToCurrentPlayer();
        boolean APdone = player1.hasSpoken();
        boolean NAPdone = player2.hasSpoken();
        assertTrue(APdone);
        assertTrue(NAPdone);

        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();
        Game.stack.resolveLast();
        Game.givePriorityToCurrentPlayer();
    }

    @Test
    public void testInitWithEmptyDecks() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertEquals(0, deck1.size());
        assertEquals(0, deck2.size());
    }

    @Test
    public void testInitWithNonEmptyDecks() {
        UserInterface ip1 = new StubUserInterface();
        UserInterface ip2 = new StubUserInterface();
        String name1 = "Player1";
        String name2 = "Player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        deck1.add(new Card());
        deck2.add(new Card());
        Game.init(ip1, ip2, name1, name2, deck1, deck2);

        assertEquals(1, deck1.size());
        assertEquals(1, deck2.size());
    }

    static class StubUserInterface implements UserInterface {
        private boolean hasSpoken = false;

        @Override
        public void init(Player p) {
            p.setUserInterface(this);
        }

        @Override
        public void speak() {
            hasSpoken = true;
        }

        public boolean hasSpoken() {
            return hasSpoken;
        }
    }
}
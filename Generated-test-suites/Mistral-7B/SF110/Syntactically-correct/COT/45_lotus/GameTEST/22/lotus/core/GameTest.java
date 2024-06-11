import org.junit.Assert;
import org.junit.Test;
import lotus.core.*;
import static lotus.core.Game.*; // for static imports

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Assert.assertEquals(p1, getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Assert.assertEquals(p2, getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Assert.assertEquals(p2, getOtherPlayer(p1));
        Assert.assertEquals(p1, getOtherPlayer(p2));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new DummyUserInterface();
        UserInterface IP2 = new DummyUserInterface();
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();
        init(IP1, IP2, "Player1", "Player2", deck1, deck2);
        Assert.assertEquals(IP1, getPlayingPlayer().getUserInterface());
        Assert.assertEquals(IP2, getNonPlayingPlayer().getUserInterface());
        Assert.assertEquals("Player1", getPlayingPlayer().getName());
        Assert.assertEquals("Player2", getNonPlayingPlayer().getName());
        Assert.assertEquals(deck1, getPlayingPlayer().getLibrary());
        Assert.assertEquals(deck2, getNonPlayingPlayer().getLibrary());
    }

    @Test(expected = IllegalStateException.class)
    public void testGivePriorityToCurrentPlayer_NoPlayers() {
        init(new DummyUserInterface(), new DummyUserInterface(), new CardCollection(), new CardCollection());
        givePriorityToCurrentPlayer();
    }

    @Test
    public void testGivePriorityToCurrentPlayer_BothPlayersSpeak() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Stack stack = new Stack();
        stack.push(new Card("TestCard", "TestCardType", null, null, null));
        Game.stack = stack;
        boolean p1Speak = p1.letPlayerSpeak();
        boolean p2Speak = p2.letPlayerSpeak();
        givePriorityToCurrentPlayer();
        Assert.assertTrue(p1Speak);
        Assert.assertTrue(p2Speak);
        Assert.assertEquals(stack.size(), 1);
        cardTest(stack.peek());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_OnePlayerSpeaks() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Stack stack = new Stack();
        stack.push(new Card("TestCard1", "TestCardType1", null, null, null));
        Game.stack = stack;
        boolean p1Speak = p1.letPlayerSpeak();
        givePriorityToCurrentPlayer();
        Assert.assertTrue(p1Speak);
        Assert.assertFalse(p2.letPlayerSpeak());
        Assert.assertEquals(stack.size(), 1);
        cardTest(stack.peek());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_CurrentPlayerPasses() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Stack stack = new Stack();
        stack.push(new Card("TestCard1", "TestCardType1", null, null, null));
        Game.stack = stack;
        boolean p1Speak = p1.letPlayerSpeak();
        p1.pass();
        givePriorityToCurrentPlayer();
        Assert.assertFalse(p1Speak);
        Assert.assertTrue(p2.letPlayerSpeak());
        Assert.assertEquals(stack.size(), 1);
        cardTest(stack.peek());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_NonPlayingPlayerPasses() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Stack stack = new Stack();
        stack.push(new Card("TestCard1", "TestCardType1", null, null, null));
        Game.stack = stack;
        boolean p1Speak = p1.letPlayerSpeak();
        p2.pass();
        givePriorityToCurrentPlayer();
        Assert.assertTrue(p1Speak);
        Assert.assertFalse(p2.letPlayerSpeak());
        Assert.assertEquals(stack.size(), 1);
        cardTest(stack.peek());
    }

    @Test
    public void testGivePriorityToCurrentPlayer_EmptyStack() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        givePriorityToCurrentPlayer();
        Assert.assertFalse(p1.letPlayerSpeak());
        Assert.assertFalse(p2.letPlayerSpeak());
        Assert.assertEquals(Game.stack.size(), 0);
    }

    @Test
    public void testStackResolveLast() {
        Player p1 = new Player(new DummyUserInterface(), "Player1", new CardCollection());
        Player p2 = new Player(new DummyUserInterface(), "Player2", new CardCollection());
        init(new DummyUserInterface(), new DummyUserInterface(), "Player1", "Player2", new CardCollection(), new CardCollection());
        Stack stack = new Stack();
        stack.push(new Card("TestCard1", "TestCardType1", null, null, null));
        Game.stack = stack;
        givePriorityToCurrentPlayer();
        stack.resolveLast();
        givePriorityToCurrentPlayer();
        Assert.assertFalse(p1.letPlayerSpeak());
        Assert.assertFalse(p2.letPlayerSpeak());
        Assert.assertEquals(Game.stack.size(), 0);
    }

    private void cardTest(Card card) {
        // Add more test cases for properties and methods of the Card class as needed
    }

    private static class DummyUserInterface implements UserInterface {
        @Override
        public void init(Player player) {
        }

        @Override
        public boolean letPlayerSpeak() {
            return true;
        }

        @Override
        public String getName() {
            return "DummyUserInterface";
        }
    }

}
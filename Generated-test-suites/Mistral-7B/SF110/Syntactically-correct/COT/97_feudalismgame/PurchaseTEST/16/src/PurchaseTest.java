import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.Map;
import src.CurrentPlayers;

import java.util.*;

public class PurchaseTest {

    private Purchase purchase;
    private Iterator<Object> argIterator;
    private List<String> args;
    private Fiefdoms map;
    private CurrentPlayers currentPlayers;
    private GameAutoActions gameAutoActions;
    private Actions actions;

    @Before
    public void setup() {
        map = Mockito.mock(Fiefdoms.class);
        currentPlayers = Mockito.mock(CurrentPlayers.class);
        gameAutoActions = Mockito.mock(GameAutoActions.class);
        actions = Mockito.mock(Actions.class);
        purchase = new Purchase(map, currentPlayers, gameAutoActions, actions);
        args = new ArrayList<>();
        argIterator = args.iterator();
    }

    @Test
    public void testPerform() {
        // Test with valid arguments
        args.add("Purchase");
        args.add(argIterator);
        purchase.perform(args);

        // Test with invalid arguments
        args.clear();
        args.add("InvalidClass");
        args.add(argIterator);
        try {
            purchase.perform(args);
            fail("Expected SecurityException");
        } catch (SecurityException e) {
            // Expected exception
        }

        args.clear();
        args.add("Purchase");
        args.add(null);
        try {
            purchase.perform(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testVassal() {
        // Test with valid arguments
        args.clear();
        args.add("vassal");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Albion");
        args.add("Albion");

        Mockito.when(map.get(Mockito.eq("Albion"))).thenReturn(new Fiefdoms("Albion", null, null, null, null, null, null, null, null));
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "knight", 100));

        purchase.perform(args);
        verify(map).get(eq("Albion"));
        verify(currentPlayers).get(eq("Alfred"));

        // Test with invalid arguments
        args.clear();
        args.add("vassal");
        args.add(argIterator);
        args.add(null);
        args.add(null);
        args.add(null);
        try {
            purchase.perform(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testCastleFiefdom() {
        // Test with valid arguments
        args.clear();
        args.add("castleFiefdom");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Albion");
        args.add("Albion");

        Fiefdoms fiefdom = new Fiefdoms("Albion", null, null, null, null, null, null, null, null);
        Mockito.when(map.get(Mockito.eq("Albion"))).thenReturn(fiefdom);
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "king", 500));

        purchase.perform(args);
        verify(map).get(eq("Albion"));
        verify(currentPlayers).get(eq("Alfred"));

        // Test with insufficient funds
        args.clear();
        args.add("castleFiefdom");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Albion");
        args.add("Albion");
        Mockito.when(map.get(Mockito.eq("Albion"))).thenReturn(fiefdom);
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "king", 250));

        purchase.perform(args);
        verify(System.out, times(1)).println("You cannot buy more Fiefdom castles, you have insufficient funds");

        // Test with invalid fiefdom owner
        args.clear();
        args.add("castleFiefdom");
        args.add(argIterator);
        args.add("Alfred");
        args.add("AnotherKingdom");
        args.add("AnotherKingdom");

        Mockito.when(map.get(Mockito.eq("AnotherKingdom"))).thenReturn(new Fiefdoms("AnotherKingdom", null, null, null, null, null, null, null, null));

        purchase.perform(args);
        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom");
    }

    @Test
    public void testCastlePersonal() {
        // Test with valid arguments
        args.clear();
        args.add("castlePersonal");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Albion");
        args.add("Albion");

        Fiefdoms fiefdom = new Fiefdoms("Albion", null, null, null, null, null, null, null, null);
        Mockito.when(map.get(Mockito.eq("Albion"))).thenReturn(fiefdom);
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "king", 500));

        purchase.perform(args);
        verify(map).get(eq("Albion"));
        verify(currentPlayers).get(eq("Alfred"));

        // Test with insufficient funds
        args.clear();
        args.add("castlePersonal");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Albion");
        args.add("Albion");
        Mockito.when(map.get(Mockito.eq("Albion"))).thenReturn(fiefdom);
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "king", 20));

        purchase.perform(args);
        verify(System.out, times(1)).println("You cannot buy more Personal castle, you have insufficient funds");

        // Test with invalid fiefdom owner
        args.clear();
        args.add("castlePersonal");
        args.add(argIterator);
        args.add("Alfred");
        args.add("AnotherKingdom");
        args.add("AnotherKingdom");

        Mockito.when(map.get(Mockito.eq("AnotherKingdom"))).thenReturn(new Fiefdoms("AnotherKingdom", null, null, null, null, null, null, null, null));

        purchase.perform(args);
        verify(System.out, times(1)).println("Fiefdom doesn't belong to the kingdom: Albion owner: AnotherKingdom");
    }

    @Test
    public void testMercenary() {
        // Test with valid arguments
        args.clear();
        args.add("mercenary");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Brunhilde");

        Knight knight = new Knight("Brunhilde", "knight", 10);
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "king", 100));
        Mockito.when(currentPlayers.get(Mockito.eq("Brunhilde"))).thenReturn(knight);

        purchase.perform(args);
        verify(currentPlayers).get(eq("Alfred"));
        verify(currentPlayers).get(eq("Brunhilde"));

        // Test with insufficient funds
        args.clear();
        args.add("mercenary");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Brunhilde");
        Mockito.when(currentPlayers.get(Mockito.eq("Alfred"))).thenReturn(new Knight("Alfred", "king", 5));

        purchase.perform(args);
        verify(System.out, times(1)).println("You cannot buy more mercenaries, you have insufficient funds");

        // Test with the same knight
        args.clear();
        args.add("mercenary");
        args.add(argIterator);
        args.add("Alfred");
        args.add("Alfred");

        purchase.perform(args);
        verify(System.out, times(1)).println("Inexistent Knight");
    }
}
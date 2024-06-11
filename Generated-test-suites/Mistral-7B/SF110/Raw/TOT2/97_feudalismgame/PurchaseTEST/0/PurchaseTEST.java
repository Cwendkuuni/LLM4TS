import static org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.CurrentPlayers;
import src.Actions;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class PurchaseTest {
    Purchase purchase;
    Fiefdoms map;
    CurrentPlayers currentPlayers;
    Actions actions;

    @Before
    public void setUp() {
        map = new Fiefdoms();
        currentPlayers = new CurrentPlayers();
        purchase = new Purchase();
        actions = new Actions();
    }

    @Test
    public void testVassal() {
        Iterator<String> args = Arrays.asList("3", "player1", "fiefdom1").iterator();
        purchase.vassal(args);

        assertEquals(3, Map.map.get("fiefdom1").getLoyalVassals());
    }

    @Test
    public void testVassalInsufficientFunds() {
        Iterator<String> args = Arrays.asList("5", "player1", "fiefdom1").iterator();
        Purchase purchase = new Purchase();

        // Set up player1 to have insufficient funds
        currentPlayers.setTotalMoney("player1", 5);

        purchase.vassal(args);

        assertEquals(0, Map.map.get("fiefdom1").getLoyalVassals());
        assertEquals("You cannot buy more vassals, you have insufficient funds", System.out.getHistogram().toString().trim());
    }

    @Test
    public void testVassalFiefdomDoesntBelongToKingdom() {
        Iterator<String> args = Arrays.asList("3", "player1", "fiefdom2").iterator();
        Purchase purchase = new Purchase();

        map.setFiefdomOwner("fiefdom2", null);

        purchase.vassal(args);

        assertEquals(0, Map.map.get("fiefdom2").getLoyalVassals());
        assertEquals("Fiefdom doesn't belong to the kingdom", System.out.getHistogram().toString().trim());
    }

    @Test
    public void testVassalMaximumVassals() {
        Iterator<String> args = Arrays.asList("4", "player1", "fiefdom1").iterator();
        Purchase purchase = new Purchase();

        Map.map.get("fiefdom1").setLoyalVassals(Map.map.get("fiefdom1").getLoyalVassals() + 1);

        purchase.vassal(args);

        assertEquals(4, Map.map.get("fiefdom1").getLoyalVassals());
        assertEquals("You cannot buy more vassals, the fiefdom already has the maximum number of vassals", System.out.getHistogram().toString().trim());
    }

    @Test
    public void testVassalInexistentPlayer() {
        Iterator<String> args = Arrays.asList("3", "playerX", "fiefdom1").iterator();
        Purchase purchase = new Purchase();

        purchase.vassal(args);

        assertEquals(0, Map.map.get("fiefdom1").getLoyalVassals());
        assertEquals("Inexistent Player", System.out.getHistogram().toString().trim());
    }
}
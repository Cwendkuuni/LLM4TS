import static org.junit.Assert.*;
import src.Purchase;
import src.Fiefdoms;
import src.Knight;
import src.CurrentPlayers;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdom);

        Knight buyerObj = new Knight("buyer1", "count");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer1", buyerObj);

        Iterator<Object> argIter = Arrays.asList(5, "buyer1", "Fiefdom1").iterator();

        purchase.vassal(argIter);

        assertEquals(5, fiefdom.getLoyalVassals());
    }

    @Test(expected = NoSuchElementException.class)
    public void testVassalNoEnoughElements() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdom);

        Knight buyerObj = new Knight("buyer1", "count");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer1", buyerObj);

        Iterator<Object> argIter = Arrays.asList(5).iterator();

        purchase.vassal(argIter);
    }

    @Test(expected = NumberFormatException.class)
    public void testVassalInvalidInteger() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdom);

        Knight buyerObj = new Knight("buyer1", "count");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer1", buyerObj);

        Iterator<Object> argIter = Arrays.asList("five", "buyer1", "Fiefdom1").iterator();

        purchase.vassal(argIter);
    }

    @Test(expected = NullPointerException.class)
    public void testVassalNullBuyerName() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdom);

        Iterator<Object> argIter = Arrays.asList(5, null, "Fiefdom1").iterator();

        purchase.vassal(argIter);
    }

    @Test(expected = NullPointerException.class)
    public void testVassalNullFiefdom() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put(null, fiefdom);

        Knight buyerObj = new Knight("buyer1", "count");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer1", buyerObj);

        Iterator<Object> argIter = Arrays.asList(5, "buyer1", null).iterator();

        purchase.vassal(argIter);
    }

    @Test
    public void testVassalInvalidQty() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdom);

        Knight buyerObj = new Knight("buyer1", "count");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer1", buyerObj);

        Iterator<Object> argIter = Arrays.asList(-1, "buyer1", "Fiefdom1").iterator();

        try {
            purchase.vassal(argIter);
            fail();
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test
    public void testVassalBuyersNotInKingdom() {
        Purchase purchase = new Purchase();
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdom);

        Knight buyerObj1 = new Knight("buyer1", "count");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer1", buyerObj1);

        Knight buyerObj2 = new Knight("buyer2", "viking");
        CurrentPlayers.currentPlayers = currentPlayers;
        CurrentPlayers.currentPlayers.put("buyer2", buyerObj2);

        Iterator<Object> argIter = Arrays.asList(5, "buyer2", "Fiefdom1").iterator();

        purchase.vassal(argIter);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import src.Purchase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Purchase purchase = new Purchase();
        Collection<Object> args = new ArrayList<>();
        args.add("feudalism.Purchase::vassal");
        args.add(Arrays.asList(1, "player1", "fiefdom1").iterator());
        purchase.perform(args);
        // Add other test cases for other methods
    }

    @Test
    public void testVassal() {
        Purchase purchase = new Purchase();
        Fiefdoms fiefdom = new Fiefdoms();
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("fiefdom1", fiefdom);
        CurrentPlayers currentPlayers = new CurrentPlayers();
        Knight buyerObj = new Knight();
        buyerObj.setRank("king");
        buyerObj.setTotalMoney(100);
        currentPlayers.put("player1", buyerObj);
        fiefdom.setOwner("player2");
        fiefdom.setLoyalVassals(0);
        when(map.get("fiefdom1").getOwner()).thenReturn("player2");

        purchase.vassal(Arrays.asList(1, "player1", "fiefdom1").iterator());
        verify(purchase, times(1)).setTotalMoney(buyerObj, argThat(arg -> arg.equals(1)));
        verify(fiefdom, times(1)).setLoyalVassals(1, anyInt());
    }

    // Add other test cases for other methods
}
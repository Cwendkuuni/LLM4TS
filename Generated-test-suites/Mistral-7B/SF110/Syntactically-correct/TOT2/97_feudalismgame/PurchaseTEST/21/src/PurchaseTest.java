import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchaseTest {
    @Test
    public void testVassal() {
        Iterator<String> arg = mock(Iterator.class);
        when(arg.next()).thenReturn("vassalQty").thenReturn("buyerName").thenReturn("fiefdomName");

        Purchase purchase = new Purchase();
        purchase.vassal(arg);

        Fiefdoms fiefdom = Map.map.get("fiefdomName");
        Knight buyerObj = CurrentPlayers.currentPlayers.get("buyerName");

        // Test successful case
        assertEquals(5, fiefdom.getLoyalVassals());
        assertEquals(10, buyerObj.getTotalMoney());

        // Test insufficient funds case
        when(arg.next()).thenReturn("insufficientFunds");
        purchase.vassal(arg);
        assertEquals(5, fiefdom.getLoyalVassals());
        assertEquals(9, buyerObj.getTotalMoney());

        // Test fiefdom not belonging to the kingdom
        when(arg.next()).thenReturn("notBelongingFiefdomName");
        purchase.vassal(arg);
        assertEquals(5, fiefdom.getLoyalVassals());
        assertEquals(10, buyerObj.getTotalMoney());
    }
}
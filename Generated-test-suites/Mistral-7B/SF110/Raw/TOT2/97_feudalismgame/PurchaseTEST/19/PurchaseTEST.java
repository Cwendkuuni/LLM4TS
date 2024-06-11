import org.junit.Test;
import static org.junit.Assert.*;
import src.Purchase;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Collection args = Arrays.asList("arg1", Arrays.asList(1, "John").iterator(), null);
        Purchase purchase = new Purchase();
        purchase.perform(args);
    }

    @Test
    public void testVassal() {
        Iterator arg = Arrays.asList(2, "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.vassal(arg);
    }

    @Test
    public void testCastleFiefdom() {
        Iterator arg = Arrays.asList(3, "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.castleFiefdom(arg);
    }

    @Test
    public void testCastlePersonal() {
        Iterator arg = Arrays.asList(5, "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.castlePersonal(arg);
    }

    @Test
    public void testMercenary() {
        Iterator arg = Arrays.asList(1, "John1", "knight1").iterator();
        Purchase purchase = new Purchase();
        purchase.mercenary(arg);

        Iterator arg2 = Arrays.asList(1, "John2", "knight2").iterator();
        Purchase purchase2 = new Purchase();
        purchase2.mercenary(arg2);

        Iterator arg3 = Arrays.asList(1, "John", "knight3").iterator();
        Purchase purchase3 = new Purchase();
        purchase3.mercenary(arg3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassalInsufficientFunds() {
        Iterator arg = Arrays.asList(-5, "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.vassal(arg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVassalNonExistentFiefdom() {
        Iterator arg = Arrays.asList(2, "John", "nonExistentFiefdom").iterator();
        Purchase purchase = new Purchase();
        purchase.vassal(arg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdomInsufficientFunds() {
        Iterator arg = Arrays.asList(-5, "John", "fiefdom1").iterator();
        Purchase purchase = new Purchase();
        purchase.castleFiefdom(arg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCastleFiefdomNonExistentFiefdom() {
        Iterator arg = Arrays.asList(5, "John", "nonExistentFiefdom").iterator();
        Purchase purchase = new Purchase();
        purchase.castleFiefdom(arg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMercenaryNonExistentKnight() {
        Iterator arg = Arrays.asList(1, "John", "nonExistentKnight").iterator();
        Purchase purchase = new Purchase();
        purchase.mercenary(arg);
    }

    @Test
    public void testPerformNullCollection() {
        Purchase purchase = new Purchase();
        purchase.perform(null);
    }

    @Test
    public void testPerformEmptyCollection() {
        Collection emptyCollection = Collections.emptyList();
        Purchase purchase = new Purchase();
        purchase.perform(emptyCollection.iterator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformInvalidIterator() {
        Collection invalidCollection = Arrays.asList(new Object());
        Purchase purchase = new Purchase();
        purchase.perform(invalidCollection.iterator());
    }
}
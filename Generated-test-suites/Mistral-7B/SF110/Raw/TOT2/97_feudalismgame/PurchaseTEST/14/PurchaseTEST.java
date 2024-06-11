import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class PurchaseTest {

    @Test
    public void testPerform() {
        Collection args = Arrays.asList("vassal", Arrays.asList(1, "player1", "fiefdom1").iterator(), "castleFiefdom", Arrays.asList(2, "player2", "fiefdom2").iterator());
        Purchase purchase = new Purchase();

        Mockito.doThrow(new NoSuchMethodException("")).when(purchase).getClass();

        purchase.perform(args);

        Mockito.verify(purchase, Mockito.times(1)).getClass();
        Mockito.verify(purchase, Mockito.atLeastOnce()).getClass();
        Mockito.verify(purchase, Mockito.times(2)).getMethod(ArgumentMatchers.anyString(), ArgumentMatchers.any(Iterator.class));
    }
}
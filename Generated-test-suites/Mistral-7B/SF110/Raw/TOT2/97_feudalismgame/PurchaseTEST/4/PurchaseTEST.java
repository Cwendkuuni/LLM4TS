import org.junit.Test;
import static org.mockito.Mockito.*;

public class PurchaseTest {
    @Test
    public void testPerform() {
        Iterator argIterator = Arrays.asList("vassal", Map.class.getName(), "feudalDomain1", CurrentPlayers.class.getName(), "player1").iterator();
        Purchase purchase = new Purchase();
        when(Map.map.get(argIterator.next())).thenReturn(new Fiefdoms() {{ setName("feudalDomain1"); setOwner(null); }});
        when(CurrentPlayers.currentPlayers.get(argIterator.next())).thenReturn(new Knight() {{ setName("player1"); setRank("king"); }});

        purchase.perform(Collections.singletonList(argIterator));

        verify(purchase, times(1)).vassal(argIterator);
    }
}
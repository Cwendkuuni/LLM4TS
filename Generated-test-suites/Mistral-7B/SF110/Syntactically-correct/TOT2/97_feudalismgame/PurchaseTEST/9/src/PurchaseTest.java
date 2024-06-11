import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;;

public class PurchaseTest {
    @Test
    public void testVassal() {
        // Set up mocks and objects
        Fiefdoms fiefdom = mock(Fiefdoms.class);
        when(Map.map.get(anyString())).thenReturn(fiefdom);
        Knight buyerObj = mock(Knight.class);
        CurrentPlayers currentPlayers = mock(CurrentPlayers.class);
        when(currentPlayers.get(anyString())).thenReturn(buyerObj);
        Iterator arg = mock(Iterator.class);
        when(arg.next()).thenReturn("vassalName");
        when(arg.next()).thenReturn("10");
        when(arg.next()).thenReturn("fiefdomName");

        // Call the method being tested
        new Purchase().vassal(arg);

        // Verify the mocks
        verify(buyerObj).setTotalMoney(10, false);
        verify(fiefdom).setLoyalVassals(1, true);

        // Assert the output
        assertEquals("You cannot buy more vassals, you have insufficient funds", systemOut().getHistory().get(0));
    }
}
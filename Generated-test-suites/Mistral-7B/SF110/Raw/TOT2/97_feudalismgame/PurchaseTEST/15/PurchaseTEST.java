import org.junit.Test;
import static org.mockito.Mockito.*;
import src.Purchase;
import src.Actions;
import src.Fiefdoms;
import src.GameAutoActions;
import src.Knight;
import src.CurrentPlayers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class PurchaseTest {
    @Test
    public void testPerformVassalMethod() {
        Purchase purchase = new Purchase();
        Actions actions = mock(Actions.class);
        GameAutoActions gameAutoActions = mock(GameAutoActions.class);
        Fiefdoms map = mock(Fiefdoms.class);
        CurrentPlayers currentPlayers = mock(CurrentPlayers.class);

        when(map.get(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            String name = invocation.getArgument(0).toString();
            return new Fiefdoms() {{
                this.getName() = name;
                this.getOwner() = null;
            }};
        });

        when(currentPlayers.get(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            String name = invocation.getArgument(0).toString();
            Knight knight = new Knight() {{
                this.getName() = name;
                this.getRank() = "knight";
                this.setTotalMoney(100);
            }};
            return knight;
        });

        when(gameAutoActions.saveAll()).thenReturn(null);

        Iterator<Object> args = Collections.singletonList("vassal").iterator();
        Iterator arg = args.iterator();
        when(arg.next()).thenReturn("arg1");
        when(arg.next()).thenReturn(1);
        when(arg.next()).thenReturn("fiefdom1");

        purchase.perform(args);

        verify(actions).getClass();
        verify(map).get(eq("fiefdom1"));
        verify(currentPlayers).get("arg1");
        verify(map, times(1)).get(eq("fiefdom1"));
        verify(currentPlayers, times(1)).get("arg1");
        verify(gameAutoActions).saveAll();
    }
}
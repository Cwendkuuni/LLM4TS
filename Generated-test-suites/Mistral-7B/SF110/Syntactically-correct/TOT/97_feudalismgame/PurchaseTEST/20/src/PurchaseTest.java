import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import src.Actions;
import src.Fiefdoms;
import src.Knight;
import src.Map;
import src.Purchase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PurchaseTest {
    @InjectMocks
    private Purchase purchase;
    private Actions actions;
    private Fiefdoms fiefdom;
    private Knight buyer;
    private Knight vassal;
    private Map map;

    @Before
    public void setup() {
        actions = Mockito.mock(Actions.class);
        fiefdom = Mockito.mock(Fiefdoms.class);
        buyer = Mockito.mock(Knight.class);
        vassal = Mockito.mock(Knight.class);
        map = Mockito.mock(Map.class);

        Mockito.when(map.get(Mockito.anyString())).thenReturn(fiefdom);
        Mockito.when(fiefdom.getOwner()).thenReturn(null);
        Mockito.when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyer);
        Mockito.when(buyer.getRank()).thenReturn("knight");
        Mockito.when(buyer.getName()).thenReturn("John Doe");
        Mockito.when(buyer.setTotalMoney(Mockito.anyInt(), Mockito.booleanArg(0))).thenReturn(true);
        Mockito.when(buyer.isFiefdomMine(Mockito.anyString())).thenReturn(false);
        Mockito.when(map.get(Mockito.anyString())).thenReturn(fiefdom);
        Mockito.when(fiefdom.getOwner()).thenReturn(buyer);
        Mockito.when(fiefdom.getLoyalVassals()).thenReturn(0);

        Collection<Iterator<Object>> args = new ArrayList<>();
        Iterator<Object> arg = Mockito.mock(Iterator.class);
        Mockito.when(arg.next()).thenReturn("fiefdomName");
        Mockito.when(arg.next()).thenReturn(Integer.toString(5));
        Mockito.when(arg.next()).thenReturn("John Doe");
        Mockito.when(arg.next()).thenReturn("fiefdomName");
        args.add(arg);

        purchase.perform(args);
    }

    @Test
    public void testVassalMethod() {
        Mockito.when(arg.next()).thenReturn("5");
        Mockito.when(arg.next()).thenReturn("John Doe");
        Mockito.when(arg.next()).thenReturn("fiefdomName");
        Mockito.when(arg.next()).thenReturn("King John Doe");
        Mockito.when(CurrentPlayers.currentPlayers.get(Mockito.anyString())).thenReturn(buyer);
        Mockito.when(buyer.isFiefdomMine(Mockito.anyString())).thenReturn(true);

        Collection<Iterator<Object>> argsForTest = new ArrayList<>();
        argsForTest.add(arg);

        purchase.perform(argsForTest);

        Mockito.verify(fiefdom, Mockito.times(1)).setLoyalVassals(Mockito.eq(5), Mockito.eq(true));
        assertEquals(5, fiefdom.getLoyalVassals());
    }
}
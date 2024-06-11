import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import src.Actions;
import src.Fiefdoms;
import src.Map;

import java.util.*;

import static org.junit.Assert.*;

public class VassalRebellionTest {

    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    private Map map;

    @Before
    public void setUp() {
        map = Mockito.mock(Map.class);
        fiefdomObj = Mockito.mock(Fiefdoms.class);
        vassalRebellion = new VassalRebellion() {
            @Override
            public void perform(Collection<String> col) {
                this.fiefdomObj = map.get(col.iterator().next());
                vassalRebellion.perform(col);
            }
        };
        Mockito.when(map.get(ArgumentMatchers.anyString())).thenReturn(fiefdomObj);
    }

    @Test
    public void testPerformWithLoyalVassals() {
        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(2);
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(5);
        vassalRebellion.perform(Arrays.asList("TestFiefdom"));
        assertEquals(2, fiefdomObj.getRebelliousVassals().size());
        assertEquals(false, fiefdomObj.isKingdom());
    }

    // Add other test cases for edge cases and error scenarios here

    // Complete test file starts here
    

}
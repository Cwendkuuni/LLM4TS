import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class VassalRebellionTest {

    @Test
    public void testPerformWithValidArguments() {
        Fiefdoms fiefdomObj = mock(Fiefdoms.class);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Set<String> map = new HashMap<>();
        map.put("testFiefdom", fiefdomObj);
        Collection<String, Fiefdoms> col = new ArrayList<>(map.entrySet());

        when(fiefdomObj.getPersonalCastles()).thenReturn(1);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(2);
        when(fiefdomObj.getLoyalVassals()).thenReturn(new ArrayList<>());
        when(fiefdomObj.getValue()).thenReturn(1);

        vassalRebellion.perform(col);

        assertFalse(fiefdomObj.isKingdom());
        assertNull(fiefdomObj.getRebelliousVassals());
    }
}
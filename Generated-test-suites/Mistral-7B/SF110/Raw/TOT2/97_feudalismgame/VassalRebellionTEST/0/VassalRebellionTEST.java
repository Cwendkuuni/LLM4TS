import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setup() {
        fiefdomObj = mock(Fiefdoms.class);
        vassalRebellion = new VassalRebellion() {
            @Override
            public void perform(Collection<String> col) {
                super.perform(col);
            }

            @Override
            public Fiefdoms getFiefdomObj() {
                return fiefdomObj;
            }

            @Override
            public int getPersonalCastles() {
                return 2;
            }

            @Override
            public int getFiefdomCastles() {
                return 1;
            }

            @Override
            public List<String> getLoyalVassals() {
                return new ArrayList<>();
            }

            @Override
            public int getValue() {
                return 3;
            }

            @Override
            public boolean isRebelliousVassals() {
                return false;
            }

            @Override
            public boolean isKingdom() {
                return true;
            }

            @Override
            public void setRebelliousVassals(int value, boolean isRebellious) {
                // No-op
            }

            @Override
            public void setKingdom(boolean isKingdom) {
                // No-op
            }
        };

        when(fiefdomObj.getLoyalVassals().size()).thenReturn(1);
        when(fiefdomObj.getValue()).thenReturn(2);
        when(Map.map.get(anyString())).thenReturn(fiefdomObj);
    }

    @Test
    public void testPerform() {
        List<String> input = Arrays.asList("TestFiefdom");
        vassalRebellion.perform(input);
        assertFalse(vassalRebellion.isRebelliousVassals());
        assertFalse(vassalRebellion.isKingdom());
    }
}
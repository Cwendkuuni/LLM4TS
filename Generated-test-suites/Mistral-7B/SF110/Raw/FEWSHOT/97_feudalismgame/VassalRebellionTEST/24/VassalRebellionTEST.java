package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class VassalRebellionTest {

    private Fiefdoms fiefdomMock;
    private VassalRebellion vassalRebellion;
    private List<String> fiefdomNames;

    @Before
    public void setUp() {
        fiefdomMock = mock(Fiefdoms.class);
        vassalRebellion = new VassalRebellion();
        vassalRebellion.fiefdomObj = fiefdomMock;
        fiefdomNames = new ArrayList<>();
    }

    @Test
    public void testPerformEmptyCollection() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

        vassalRebellion.perform(Collections.emptyList());

        verify(fiefdomMock).setRebelliousVassals(0, true);
        verify(fiefdomMock).setKingdom(false);
        verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
    }

    @Test
    public void testPerformWithLoyalVassals() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomMock.getValue()).thenReturn(3);
        when(fiefdomMock.getPersonalCastles()).thenReturn(2);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

        fiefdomNames.add("testFiefdom");
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("testFiefdom", fiefdomMock);

        vassalRebellion.perform(new ArrayList<>(fiefdomNames));

        verify(fiefdomMock).setRebelliousVassals(3, true);
        verify(fiefdomMock).setKingdom(false);
        verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());

        // Check if the rebellion condition was met
        List<Vassal> loyalVassals = new ArrayList<>(fiefdomMock.getLoyalVassals());
        List<Vassal> rebelliousVassals = fiefdomMock.getRebelliousVassals();

        assertEquals(2, loyalVassals.size() - rebelliousVassals.size());

        // Check if the castle condition was met
        int personalCastles = fiefdomMock.getPersonalCastles();
        int fiefdomCastles = fiefdomMock.getFiefdomCastles();

        if (personalCastles > 0 || fiefdomCastles > 0) {
            int expectedRebelliousVassals = loyalVassals.size() - (personalCastles * 10 + fiefdomCastles * 25);
            assertEquals(expectedRebelliousVassals, rebelliousVassals.size());
        }
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals().isEmpty()).thenReturn(true);

        vassalRebellion.perform(new ArrayList<>(fiefdomNames));

        verify(fiefdomMock).setRebelliousVassals(0, true);
        verify(fiefdomMock).setKingdom(false);
        verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
    }

    @Test(expected = NullPointerException.class)
    public void testPerformWithNullFiefdom() {
        vassalRebellion.perform(null);
    }
}
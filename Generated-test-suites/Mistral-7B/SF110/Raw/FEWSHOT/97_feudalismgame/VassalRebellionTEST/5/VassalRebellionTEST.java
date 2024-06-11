package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

    private Fiefdoms fiefdomMock;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        fiefdomMock = Mockito.mock(Fiefdoms.class);
        Map map = Mockito.mock(Map.class);
        when(map.get(eq("testFiefdomName"))).thenReturn(fiefdomMock);
        vassalRebellion = new VassalRebellion() {
            {
                Map map = this.map;
            }
        };
    }

    @Test
    public void testPerformWithLoyalVassalsAndEnoughCastles() {
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
        when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2", "vassal3"));
        vassalRebellion.perform(Arrays.asList("testFiefdomName"));
        verify(fiefdomMock, times(1)).setRebelliousVassals(eq(2), eq(true));
    }

    @Test
    public void testPerformWithLoyalVassalsButNotEnoughCastles() {
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(0);
        when(fiefdomMock.getLoyalVassals()).thenReturn(Arrays.asList("vassal1", "vassal2", "vassal3"));
        vassalRebellion.perform(Arrays.asList("testFiefdomName"));
        verify(fiefdomMock, times(1)).setRebelliousVassals(eq(3), eq(true));
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(2);
        when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());
        vassalRebellion.perform(Arrays.asList("testFiefdomName"));
        verify(fiefdomMock, times(1)).newRollEvent();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithNullFiefdomName() {
        vassalRebellion.perform(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithEmptyCollection() {
        vassalRebbellion.perform(Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void testPerformWithNullFiefdomsMap() {
        vassalRebbellion.perform(Arrays.asList("testFiefdomName"));
        verify(map, times(1)).get(eq("testFiefdomName"));
        assertNull(Map.map);
    }
}
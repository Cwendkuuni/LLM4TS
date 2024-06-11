package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import src.Fiefdom;
import src.VassalRebellion;

public class VassalRebellionTest {

    private Fiefdom fiefdomMock;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        fiefdomMock = Mockito.mock(Fiefdom.class);
        vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomMock);
    }

    @Test
    public void testPerformWithLoyalVassalsAndCastles() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomMock.getValue()).thenReturn(2);
        when(fiefdomMock.getPersonalCastles()).thenReturn(2);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(2, true);
        verify(fiefdomMock, times(1)).setKingdom(false);
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(0, true);
        verify(fiefdomMock, times(1)).newRollEvent();
    }

    @Test
    public void testPerformWithInsufficientLoyalVassals() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(2);
        when(fiefdomMock.getValue()).thenReturn(3);

        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(1, true);
    }

    @Test
    public void testPerformWithInsufficientPersonalCastlesAndFiefdomCastles() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(7);
        when(fiefdomMock.getValue()).thenReturn(1);
        when(fiefdomMock.getPersonalCastles()).thenReturn(0);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(0);

        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        verify(fiefdomMock, times(1)).newRollEvent();
    }

    @Test
    public void testPerformWithInsufficientPersonalCastles() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(7);
        when(fiefdomMock.getValue()).thenReturn(1);
        when(fiefdomMock.getPersonalCastles()).thenReturn(0);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(1);

        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(1, true);
    }

    @Test
    public void testPerformWithInsufficientFiefdomCastles() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(7);
        when(fiefdomMock.getValue()).thenReturn(1);
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(0);

        vassalRebellion.perform(Arrays.asList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(6, true);
    }

    @Test(expected = NullPointerException.class)
    public void testPerformWithNullCollection() {
        vassalRebellion.perform(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithEmptyCollection() {
        vassalRebbellion.perform(Collections.emptyList());
    }
}
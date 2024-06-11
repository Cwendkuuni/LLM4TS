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
    public void setUp() {
        fiefdomMock = Mockito.mock(Fiefdoms.class);
        vassalRebellion = new VassalRebellion() {
            @Override
            public void perform(Collection<String> col) {
                super.perform(col);
            }
        };
        vassalRebellion.setFiefdomObj(fiefdomMock);
    }

    @Test
    public void testPerform_LoyalVassals_NoRebellion() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
        verify(fiefdomMock, times(1)).setRebelliousVassals(0, false);
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerform_LoyalVassals_RebellionConditionMet() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomMock.getValue()).thenReturn(3);

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(3, true);
        verify(fiefdomMock, times(1)).setKingdom(false);
    }

    @Test
    public void testPerform_LoyalVassals_RebellionConditionNotMet() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomMock.getValue()).thenReturn(4);

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(2, true);
        verify(fiefdomMock, times(1)).setKingdom(false);
    }

    @Test
    public void testPerform_NoLoyalVassals() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        verify(fiefdomMock, times(1)).newRollEvent();
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerform_CastlesConditionMet() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(10);
        when(fiefdomMock.getPersonalCastles()).thenReturn(2);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(3);

        vassalRebellion.perform(Collections.singletonList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(50, true);
        verify(fiefdomMock, times(1)).setKingdom(false);
    }

    @Test
    public void testPerform_CastlesConditionNotMet() {
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(10);
        when(fiefdomMock.getPersonalCastles()).thenReturn(2);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(2);

        vassalRebbellion.perform(Collections.singletonList("TestFiefdom"));

        verify(fiefdomMock, times(1)).setRebelliousVassals(1, true);
        verify(fiefdomMock, times(1)).setKingdom(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullCollection() {
        vassalRebellion.perform(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullFiefdom() {
        vassalRebbellion.setFiefdomObj(null);
        vassalRebbellion.perform(Collections.singletonList("TestFiefdom"));
    }
}
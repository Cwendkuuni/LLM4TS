package src;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class VassalRebellionTest {

    private Fiefdoms fiefdomMock;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.fiefdomMock = mock(Fiefdoms.class);
        this.vassalRebellion = new VassalRebellion();
        this.vassalRebellion.setFiefdomObj(this.fiefdomMock);
    }

    @Test
    public void testPerformWithLoyalVassalsAndEnoughCastles() {
        when(this.fiefdomMock.getName()).thenReturn("testFiefdom");
        when(this.fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(this.fiefdomMock.getFiefdomCastles()).thenReturn(2);
        when(this.fiefdomMock.getLoyalVassals().size()).thenReturn(10);
        when(this.fiefdomMock.getValue()).thenReturn(5);

        this.vassalRebellion.perform(Arrays.asList("testFiefdom"));

        verify(this.fiefdomMock, times(1)).setRebelliousVassals(5, true);
        verify(this.fiefdomMock, times(1)).setKingdom(false);
        verifyNoMoreInteractions(this.fiefdomMock);
    }

    @Test
    public void testPerformWithLoyalVassalsButNotEnoughCastles() {
        when(this.fiefdomMock.getName()).thenReturn("testFiefdom");
        when(this.fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(this.fiefdomMock.getFiefdomCastles()).thenReturn(0);
        when(this.fiefdomMock.getLoyalVassals().size()).thenReturn(11);
        when(this.fiefdomMock.getValue()).thenReturn(5);

        this.vassalRebellion.perform(Arrays.asList("testFiefdom"));

        verify(this.fiefdomMock, times(1)).setRebelliousVassals(11, true);
        verify(this.fiefdomMock, times(1)).setKingdom(false);
        verifyNoMoreInteractions(this.fiefdomMock);
    }

    @Test
    public void testPerformWithoutLoyalVassals() {
        when(this.fiefdomMock.getName()).thenReturn("testFiefdom");
        when(this.fiefdomMock.getPersonalCastles()).thenReturn(0);
        when(this.fiefdomMock.getFiefdomCastles()).thenReturn(0);
        when(this.fiefdomMock.getLoyalVassals().size()).thenReturn(0);

        this.vassalRebellion.perform(Arrays.asList("testFiefdom"));

        verify(this.fiefdomMock, times(1)).new RollEvent(eq("testFiefdom"));
        verifyNoMoreInteractions(this.fiefdomMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithNullCollection() {
        this.vassalRebbellion.perform(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithNullFiefdomObj() {
        this.vassalRebbellion.setFiefdomObj(null);
        this.vassalRebbellion.perform(Arrays.asList("testFiefdom"));
    }
}
package src.test.java;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.Mockito;

public class VassalRebellionTest {

    @Test
    public void testPerform_LoyalVassals_NoRebellion() {
        // Given
        Fiefdoms fiefdomMock = Mockito.mock(Fiefdoms.class);
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
        when(fiefdomMock.getValue()).thenReturn(10);
        when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> input = Arrays.asList("testFiefdom");

        // When
        vassalRebellion.perform(input);

        // Then
        verify(fiefdomMock, times(1)).setRebelliousVassals(0, true);
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerform_LoyalVassals_RebellionPossible() {
        // Given
        Fiefdoms fiefdomMock = Mockito.mock(Fiefdoms.class);
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomMock.getValue()).thenReturn(5);
        when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> input = Arrays.asList("testFiefdom");

        // When
        vassalRebellion.perform(input);

        // Then
        verify(fiefdomMock, times(1)).setRebelliousVassals(5, true);
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerform_LoyalVassals_RebellionNotPossible() {
        // Given
        Fiefdoms fiefdomMock = Mockito.mock(Fiefdoms.class);
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(6);
        when(fiefdomMock.getValue()).thenReturn(5);
        when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> input = Arrays.asList("testFiefdom");

        // When
        vassalRebellion.perform(input);

        // Then
        verify(fiefdomMock, times(1)).setRebelliousVassals(1, true);
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerform_NoLoyalVassals() {
        // Given
        Fiefdoms fiefdomMock = Mockito.mock(Fiefdoms.class);
        when(fiefdomMock.getLoyalVassals().size()).thenReturn(0);
        when(Map.map.get("testFiefdom")).thenReturn(fiefdomMock);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> input = Arrays.asList("testFiefdom");

        // When
        vassalRebellion.perform(input);

        // Then
        verify(fiefdomMock, times(1)).new RollEvent(eq("testFiefdom"));
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerform_NullInput() {
        // Given
        VassalRebbellion vassalRebellion = new VassalRebbellion();
        Collection<String> input = null;

        // When & Then
        vassalRebellion.perform(input);
    }
}
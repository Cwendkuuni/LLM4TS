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
    private List<String> collection;

    @Before
    public void setup() {
        fiefdomMock = mock(Fiefdoms.class);
        vassalRebellion = new VassalRebellion();
        collection = new ArrayList<>();
    }

    @Test
    public void testPerformWithLoyalVassalsAndCastles() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals()).thenReturn(new ArrayList<>(Collections.singletonList("testVassal")));
        when(fiefdomMock.getValue()).thenReturn(1);
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
        when(Map.map.get(eq("testFiefdom"))).thenReturn(fiefdomMock);

        vassalRebellion.perform(collection);

        verify(fiefdomMock).setRebelliousVassals(eq(1), anyBoolean());
        verify(fiefdomMock, times(2)).getLoyalVassals();
        verify(fiefdomMock).getPersonalCastles();
        verify(fiefdomMock).getFiefdomCastles();
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerformWithoutLoyalVassals() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals()).thenReturn(Collections.emptyList());

        vassalRebellion.perform(collection);

        verify(fiefdomMock).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomMock).setKingdom(eq(false));
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerformWithInsufficientLoyalVassalsAndCastles() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals()).thenReturn(new ArrayList<>(Collections.singletonList("testVassal")));
        when(fiefdomMock.getValue()).thenReturn(1);
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
        when(Map.map.get(eq("testFiefdom"))).thenReturn(fiefdomMock);

        doThrow(IllegalArgumentException.class).when(vassalRebellion).calculateRebellionConditions(anyInt(), anyInt(), anyInt(), anyInt());

        vassalRebellion.perform(collection);

        verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomMock).setKingdom(eq(false));
        verify(fiefdomMock).rollEvent();
        verifyNoMoreInteractions(fiefdomMock);
    }

    @Test
    public void testPerformWithSufficientLoyalVassalsAndCastles() {
        when(fiefdomMock.getName()).thenReturn("testFiefdom");
        when(fiefdomMock.getLoyalVassals()).thenReturn(new ArrayList<>(Collections.singletonList("testVassal")));
        when(fiefdomMock.getValue()).thenReturn(1);
        when(fiefdomMock.getPersonalCastles()).thenReturn(1);
        when(fiefdomMock.getFiefdomCastles()).thenReturn(1);
        when(Map.map.get(eq("testFiefdom"))).thenReturn(fiefdomMock);

        vassalRebellion.perform(collection);

        verify(fiefdomMock, never()).setRebelliousVassals(anyInt(), anyBoolean());
        verify(fiefdomMock, never()).setKingdom(anyBoolean());
        verify(fiefdomMock).rollEvent();
        verifyNoMoreInteractions(fiefdomMock);
    }
}
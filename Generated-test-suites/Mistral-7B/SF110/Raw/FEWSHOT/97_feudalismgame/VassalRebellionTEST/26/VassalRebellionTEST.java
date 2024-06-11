package src.test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class VassalRebellionTest {

    private Map<String, Fiefdom> map;
    private Fiefdom fiefdomObj;

    @Before
    public void setup() {
        map = new HashMap<>();
        fiefdomObj = mock(Fiefdom.class);
    }

    @Test
    public void testPerformEmptyCollection() {
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.perform(Collections.emptyList());
    }

    @Test
    public void testPerformWithLoyalVassalsAndNotEnoughRebelsToStartRebellion() {
        when(fiefdomObj.getName()).thenReturn("testFiefdom");
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(5);
        when(fiefdomObj.getValue()).thenReturn(2);

        VassalRebellion vassalRebellion = new VassalRebellion();
        List<String> fiefdomNames = new ArrayList<>();
        fiefdomNames.add("testFiefdom");
        vassalRebellion.perform(fiefdomNames);

        ArgumentCaptor<Integer> rebelCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> rebelStatusCaptor = ArgumentCaptor.forClass(Boolean.class);
        assertFalse(vassalRebellion.fiefdomObj.isRebellious());
        assertNull(vassalRebbellion.fiefdomObj.getKingdom());
        verify(fiefdomObj).setRebelliousVassals(rebelCaptor.capture(null), rebelStatusCaptor.capture(null));
        assertEquals(2, rebelCaptor.getValue().intValue());
        assertTrue(rebelStatusCaptor.getValue());
    }

    @Test
    public void testPerformWithLoyalVassalsAndEnoughRebelsToStartRebellion() {
        when(fiefdomObj.getName()).thenReturn("testFiefdom");
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(10);
        when(fiefdomObj.getValue()).thenReturn(2);

        VassalRebellion vassalRebellion = new VassalRebellion();
        List<String> fiefdomNames = new ArrayList<>();
        fiefdomNames.add("testFiefdom");
        vassalRebellion.perform(fiefdomNames);

        ArgumentCaptor<Integer> rebelCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> rebelStatusCaptor = ArgumentCaptor.forClass(Boolean.class);
        assertFalse(vassalRebellion.fiefdomObj.isRebellious());
        assertNull(vassalRebbellion.fiefdomObj.getKingdom());
        verify(fiefdomObj).setRebelliousVassals(rebelCaptor.capture(null), rebelStatusCaptor.capture(null));
        assertEquals(8, rebelCaptor.getValue().intValue());
        assertTrue(rebelStatusCaptor.getValue);
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        when(fiefdomObj.getName()).thenReturn("testFiefdom");
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(0);

        VassalRebellion vassalRebellion = new VassalRebellion();
        List<String> fiefdomNames = new ArrayList<>();
        fiefdomNames.add("testFiefdom");
        vassalRebellion.perform(fiefdomNames);

        new RollEvent(fiefdomObj.getName());
        verify(fiefdomObj, times(1)).setRebelliousVassals(anyInt(), anyBoolean());
    }

    @Test
    public void testPerformWithLoyalVassalsAndCastles() {
        when(fiefdomObj.getName()).thenReturn("testFiefdom");
        when(fiefdomObj.getLoyalVassals().size()).thenReturn(15);
        when(fiefdomObj.getValue()).thenReturn(2);
        when(fiefdomObj.getPersonalCastles()).thenReturn(2);
        when(fiefdomObj.getFiefdomCastles()).thenReturn(3);

        VassalRebellion vassalRebellion = new VassalRebellion();
        List<String> fiefdomNames = new ArrayList<>();
        fiefdomNames.add("testFiefdom");
        vassalRebellion.perform(fiefdomNames);

        ArgumentCaptor<Integer> rebelCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> rebelStatusCaptor = ArgumentCaptor.forClass(Boolean.class);
        assertFalse(vassalRebellion.fiefdomObj.isRebellious());
        assertNull(vassalRebbellion.fiefdomObj.getKingdom());
        verify(fiefdomObj).setRebelliousVassals(rebelCaptor.capture(null), rebelStatusCaptor.capture(null));
        assertEquals(13, rebelCaptor.getValue().intValue());
        assertTrue(rebelStatusCaptor.getValue);
    }
}
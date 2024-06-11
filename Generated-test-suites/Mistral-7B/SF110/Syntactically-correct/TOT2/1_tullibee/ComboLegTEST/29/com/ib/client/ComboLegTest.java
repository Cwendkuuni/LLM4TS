package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {
    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegNullConstructor() {
        new ComboLeg(null, 0, null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegNegativeConId() {
        new ComboLeg(-1, 0, null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegNegativeRatio() {
        new ComboLeg(0, -1, null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegEmptyAction() {
        new ComboLeg(0, 0, "", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegNullExchange() {
        new ComboLeg(0, 0, null, null, 0);
    }

    @Test
    public void testComboLegWithArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("action", comboLeg.getm_action());
        assertEquals("exchange", comboLeg.getm_exchange());
        assertEquals(3, comboLeg.getm_openClose());
        assertEquals(4, comboLeg.getm_shortSaleSlot());
        assertEquals("location", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "location1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "location1");
        assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "location2");
        assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(2, 2, "action1", "exchange1", 3, 4, "location1");
        assertFalse(comboLeg1.equals(comboLeg4));
    }
}
package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegCreation() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 1, "designatedLocation1");

        assertTrue(comboLeg1 instanceof ComboLeg);
        assertNull(comboLeg1.m_conId);
        assertNull(comboLeg1.m_ratio);
        assertNull(comboLeg1.m_action);
        assertNull(comboLeg1.m_exchange);
        assertSame(ComboLeg.SAME, comboLeg1.m_openClose);
        assertNull(comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);

        assertTrue(comboLeg2 instanceof ComboLeg);
        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("action1", comboLeg2.m_action);
        assertEquals("exchange1", comboLeg2.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);
        assertEquals(0, comboLeg2.m_shortSaleSlot);
        assertNull(comboLeg2.m_designatedLocation);

        assertTrue(comboLeg3 instanceof ComboLeg);
        assertEquals(1, comboLeg3.m_conId);
        assertEquals(2, comboLeg3.m_ratio);
        assertEquals("action1", comboLeg3.m_action);
        assertEquals("exchange1", comboLeg3.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg3.m_openClose);
        assertEquals(1, comboLeg3.m_shortSaleSlot);
        assertEquals("designatedLocation1", comboLeg3.m_designatedLocation);
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action2", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "action1", "exchange2", ComboLeg.OPEN);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
        assertFalse(comboLeg1.equals(null));

        ComboLeg comboLeg5 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 1, "designatedLocation1");
        assertTrue(comboLeg1.equals(comboLeg5));

        ComboLeg comboLeg6 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN, 2, "designatedLocation1");
        assertFalse(comboLeg1.equals(comboLeg6));

        ComboLeg comboLeg7 = new ComboLeg(1, 2, "Action1", "exchange1", ComboLeg.OPEN);
        assertTrue(comboLeg1.equals(comboLeg7));

        ComboLeg comboLeg8 = new ComboLeg(1, 2, "action1", "Exchange1", ComboLeg.OPEN);
        assertTrue(comboLeg1.equals(comboLeg8));
    }

    @Test
    public void testComboLegHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action2", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "action1", "exchange2", ComboLeg.OPEN);

        int hashCode1 = comboLeg1.hashCode();
        int hashCode2 = comboLeg2.hashCode();
        int hashCode3 = comboLeg3.hashCode();
        int hashCode4 = comboLeg4.hashCode();

        assertSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
    }
}